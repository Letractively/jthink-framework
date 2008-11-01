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

import java.util.Enumeration;

/**
 * 事务管理器。
 * 
 * <pre>
 * 实现此接口,必须提供如下构造方法:
 *   public TransactionManager(ResourceManager resManager, Configuration config){
 *   	...
 *   }
 * </pre>
 * 
 * <pre>
 * 注意：在实际应用中，事务的开始，提交，回退，关闭必须成对使用，不然容易出错，具体错类型与具体事务实现有关。
 * 以下是在实际应用中使用管理事务的使用方法：
 * 
 * <b>有更新操作的情况</b>
 * 
 * ... ...
 * try{
 *   transactionManager.begin(); //事务开始
 *   
 *   ... ...
 *   ... ...
 *   
 *   transactionManager.commit();
 * }catch(Exception e){
 *   transactionManager.rollback();
 *   throw e;
 * }finally{
 *   transactionManager.close();
 * }
 * ... ...
 * 
 * <b>无更新操作，只有查询操作的情况</b>
 * ... ...
 * try{
 *   transactionManager.begin();
 *   
 *   ... ...
 *   ... ...
 *
 * }finally{
 *   transactionManager.close();
 * }
 * ... ...
 * 
 * </pre>
 * 
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2005-06-23  创建此类型
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 * @see org.fto.jthink.resource.ResourceContainer
 * @see org.fto.jthink.transaction.TransactionManager
 */
public interface TransactionManager {

	/**
	 * 加入事务监听器, 可以加入多个事务监听器
	 * 
	 * @param listener 事务监听器
	 */
	public void addTransactionListener(TransactionListener listener);

	/**
	 * 返回所有事务监听器
	 * 
	 * @return 所有事务监听器
	 */
	public TransactionListener[] getTransactionListeners();

	/**
	 * 移除指定的事务监听器
	 * 
	 * @param listener 事务监听器
	 */
	public void removeTransactionListener(TransactionListener listener);
	
	/**
	 * 创建并返回事务工厂，程序将首先检查ResourceManager中是否已经存在该事务工厂实例，如果已经存在直接返回此工厂对象，
	 * 否则，创建新的事务工厂，并将其加入到ResourceManager中
	 * 
	 * @param factoryId 在配置文件中定义工厂ID
	 * 
	 * @return TransactionFactory实例
	 */
	public TransactionFactory getTransactionFactory(String factoryId);

	/**
	 * 向事务管理器加入事务
	 * @param name 名称
	 * @param transaction 事务对象 
	 */
	public void addTransaction(String name, Transaction transaction);

	/**
	 * 返回指定的事务
	 * @param name
	 * @return Transaction
	 */
	public Transaction getTransaction(String name);

	/**
	 * 返回所有事务名称
	 */
	public Enumeration getTransactionNames();

	/**
	 * 开始所有事务 
	 */
	public void begin();

	/**
	 * 提交所有事务。如果调用setAutoCommit()方法设置为了自动事务提交，
	 * 将不允许再调用commit()方法提交事务，否则将抛异常
	 */
	public void commit();

	/**
	 * 回退所有事务。如果调用setAutoCommit()方法设置为了自动事务提交，
	 * 将不允许再调用commit()方法提交事务，否则将抛异常
	 */
	public void rollback();

	/**
	 * 关闭所有事务
	 */
	public void close();

	/**
	 * 设置事务是否自动提交
	 */
	public void setAutoCommit(boolean autoCommit);

	/**
	 * 返回事务是否自动提交标记
	 */
	public boolean getAutoCommit();

}