/*
 * 创建日期 2005-6-23
 *
 * 版权所有: 自由思考组织(FTO)软件团队 2000-2005, 保留所有权利.
 * 这个软件是自由思考组织(FTO)软件团队开发的，如果要使用这个软件，请首先阅读许可协议。
 *
 * Copyright 2000-2005 FTO Software Team, Inc. All Rights Reserved.
 * This software is the proprietary information of FTO Software Team, Inc.
 * Use is subject to license terms.
 *
 * FTO Home：http://www.free-think.org
 */

package org.fto.jthink.transaction;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.fto.jthink.config.Configuration;
import org.fto.jthink.exception.JThinkRuntimeException;
import org.fto.jthink.resource.ResourceManager;
import org.fto.jthink.util.EnumerationHelper;
import org.fto.jthink.util.ReflectHelper;
import org.jdom.Element;

/**
 * 事务管理器的默认实现。
 * 
 * 
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2005-06-23  创建此类型
 * 2005-10-16  修改了getTransactionFactory()方法，将Class.forName()换成了ReflectHelper.forName()
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 */
public class DefaultTransactionManager implements TransactionManager{

	/* 资源管理器 */
	private ResourceManager resManager;
	/* 描述系统配置文件的对象 */
	private Configuration config;
	/* 存储所有事务监听器 */
	private List listeners = new ArrayList();
	/* 事务监听事件 */
	private TransactionEvent evt;
	
	/* 存储所有被加入的事务对象 */
	private Map trnscnsHM = new HashMap();
	
	/* 是否自动提交 */
	private boolean autoCommit = false;
	
	/**
	 * 创建DefaultTransactionManager的实例
	 * 
	 * @param resManager 资源管理器
	 * @param config 描述配置信息的对象
	 */
	public DefaultTransactionManager(ResourceManager resManager, Configuration config){
		this.resManager = resManager;
		this.config = config;
		this.evt = new TransactionEvent(this);
	}
	
	/**
	 * 加入事务监听器
	 */
	public void addTransactionListener(TransactionListener listener){
		listeners.add(listener);
	}
	
	/**
	 * 返回所有事务监听器
	 */
	public TransactionListener[] getTransactionListeners(){
		return (TransactionListener[])listeners.toArray(new TransactionListener[listeners.size()]);
	}

	/**
	 * 移除指定的事务监听器
	 */
	public void removeTransactionListener(TransactionListener listener){
		listeners.remove(listener);
	}
	
	/**
	 * 创建并返回事务工厂，程序将首先检查ResourceManager中是否已经存在将事务工厂实例，如果已经存在直接返回此工厂对象，
	 * 否则，创建新的事务工厂，并将其加入到ResourceManager中
	 * 
	 * @param factoryId 在配置文件中定义工厂ID
	 * 
	 * @return 事务工厂
	 */
	public TransactionFactory getTransactionFactory(String factoryId){
		TransactionFactory trsctnFactory = (TransactionFactory)resManager.getResource(factoryId);
		if(trsctnFactory==null){
			Element trsctnConfig = config.getTransactionConfig(factoryId);
			String trsctnFactoryClass = trsctnConfig.getChildText("factory-class");
			//Element config = trsctnConfig.getChild("config");
			try {
				Constructor trsctnFactoryCR =
					ReflectHelper.forName(trsctnFactoryClass).getConstructor(
							new Class[]{ ResourceManager.class, Configuration.class, Element.class});

				trsctnFactory = 
					(TransactionFactory)trsctnFactoryCR.newInstance(
							new Object[]{resManager, config, trsctnConfig});
				resManager.setResource(factoryId, trsctnFactory);
			} catch (Exception e) {
				if (e instanceof JThinkRuntimeException) {
					throw (JThinkRuntimeException)e;
				}
				throw new JThinkRuntimeException("创建事务工厂失败, 检查是否正确配置了事务工厂", e);
			}
		}
		return trsctnFactory;
	}
	
	
	/**
	 * 	向事务管理器加入事务
	 */
	public void addTransaction(String name, Transaction transaction){
		if (name == null){
			throw new IllegalArgumentException(
					"The name of an transaction cannot be null.");
		}
		trnscnsHM.put(name, transaction);
	}
	/**
	 * 返回指定的事务
	 */
	public Transaction getTransaction(String name){
		if (name == null){
			throw new IllegalArgumentException(
					"The name of an transaction cannot be null.");
		}
		return (Transaction)trnscnsHM.get(name);
	}
	

	/**
	 * 返回所有事务名称
	 */
	public Enumeration getTransactionNames(){
		return EnumerationHelper.toEnumerator(trnscnsHM.keySet().iterator());
	}
	
	
	/**
	 * 开始所有事务 
	 */
	public void begin() {
		Iterator transactionsIT = trnscnsHM.values().iterator();
		while(transactionsIT.hasNext()){
			Transaction transaction =(Transaction)transactionsIT.next();
			transaction.begin();
		}
		transactionBeginned();
	}
	/**
	 * 提交所有事务　
	 */
	public void commit() {
		Iterator transactionsIT = trnscnsHM.values().iterator();
		while(transactionsIT.hasNext()){
			Transaction transaction =(Transaction)transactionsIT.next();
			transaction.commit();
		}
		transactionCommitted();
	}
	/**
	 * 回退所有事务
	 */
	public void rollback() {
		Iterator transactionsIT = trnscnsHM.values().iterator();
		while(transactionsIT.hasNext()){
			Transaction transaction =(Transaction)transactionsIT.next();
			transaction.rollback();
		}
		transactionRollbacked();
	}
	/**
	 * 关闭所有事务
	 */
	public void close() {
		Iterator transactionsIT = trnscnsHM.values().iterator();
		while(transactionsIT.hasNext()){
			Transaction transaction =(Transaction)transactionsIT.next();
			transaction.close();
		}
		transactionClosed();
	}
	
	/**
	 * 设置事务是否自动提交
	 * @deprecated
	 */
	public void setAutoCommit(boolean autoCommit){
		this.autoCommit = autoCommit;
		Iterator transactionsIT = trnscnsHM.values().iterator();
		while(transactionsIT.hasNext()){
			Transaction transaction =(Transaction)transactionsIT.next();
			transaction.setAutoCommit(autoCommit);
		}
	}

	/**
	 * 返回事务是否自动提交标记
	 * @deprecated
	 */
	public boolean getAutoCommit(){
		return autoCommit;
	}
	
	private void transactionBeginned(){
		int len = listeners.size();
		for(int i=0;i<len;i++){
			((TransactionListener)listeners.get(i)).transactionBeginned(evt);
		}
	}
	private void transactionCommitted(){
		int len = listeners.size();
		for(int i=0;i<len;i++){
			((TransactionListener)listeners.get(i)).transactionCommitted(evt);
		}
	}
	private void transactionRollbacked(){
		int len = listeners.size();
		for(int i=0;i<len;i++){
			((TransactionListener)listeners.get(i)).transactionRollbacked(evt);
		}
	}
	private void transactionClosed(){
		int len = listeners.size();
		for(int i=0;i<len;i++){
			((TransactionListener)listeners.get(i)).transactionClosed(evt);
		}
	}
	
}
