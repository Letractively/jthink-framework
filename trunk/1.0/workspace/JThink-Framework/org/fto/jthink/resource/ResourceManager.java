/*
 * 创建日期 2005-6-25
 *
 * 版权所有: 自由思考组织(FTO)软件团队 2000-2005, 保留所有权利.
 * 这个软件是自由思考组织(FTO)软件团队开发的，如果要使用这个软件，请首先阅读许可协议。
 *
 * Copyright 2000-2005 FTO Software Team, Inc. All Rights Reserved.
 * This software is the proprietary information of FTO Software Team, Inc.
 * Use is subject to license terms.
 *
 * FTO站点：http://www.free-think.org
 */

package org.fto.jthink.resource;

import java.util.*;

import org.fto.jthink.config.Configuration;
import org.fto.jthink.context.ApplicationContext;
import org.fto.jthink.transaction.DefaultTransactionManager;
import org.fto.jthink.transaction.TransactionManager;



/**
 *<p>
 * 资源管理器, 主要用于对应用系统中的各种资源进行方便有效灵活的管理。
 *</p>
 *
 * <p>注意：一个资源管理器不要跨越多个线程使用，否则有可能会出现意想不到的问题。</p>
 *
 * <p>
 * 历史更新记录:<BR>
 * 2005-06-24  创建此类型
 * </p>
 *
 * @author   wenjian
 * @version  1.00
 * @since    JThink 1.0
 */

public class ResourceManager {

	
	/**
	 * 资源容器集
	 */
	private Map resContainersHM = new HashMap();
	
	/**
	 * 构造方法
	 */
	public ResourceManager(){
		/* 加入当前资源容器 */
		setResourceContainer(ThisResourceContainer.class.getName(), new ThisResourceContainer());
		/* 加入应用程序全局资源容器 */
		setResourceContainer(ApplicationContext.class.getName(), ApplicationContext.getApplicationContext());
		
//		Configuration config = Configuration.getConfiguration();
		/* 设置资源 */
//		/* 基本配置信息 */
//		setResource(ApplicationContext.class.getName(), Configuration.class.getName(), config);
//		/* 事务 */
//    if(getResource(TransactionManager.class.getName())==null){
//      /* 设置资源, 初始化事务管理器, 将TransactionManager加入到ResourceManager中 */
//      //logger.debug("初始化事务管理器, TransactionManager.");
//      TransactionManager transactionManager = new DefaultTransactionManager(this, config);
//      setResource(TransactionManager.class.getName(), transactionManager);
//      
//      // 加入WEB端事务到事务管理器
//      //logger.debug("初始化Web端事务, WebTransaction.");
//      TransactionFactory transactionFactory = transactionManager.getTransactionFactory("WebTransaction");
//      JDBCTransaction transaction  = (JDBCTransaction)transactionFactory.create();
//      transactionManager.addTransaction(JDBCTransaction.class.getName(), transaction);
//      
//    }
	}
	
	
	
	/**
	 * 设置资源容器
	 * @param name				资源容器名称
	 * @param resContainer		资源容器
	 */
	public void setResourceContainer(String name, ResourceContainer resContainer){
		if (name == null){
			throw new IllegalArgumentException(
					"The name of an resource container cannot be null.");
		}
		if(resContainersHM.get(name)!=null){
			throw new RuntimeException("The resource container is already exist! name is "+name);
		}
		resContainersHM.put(name, resContainer);
	}
	/**
	 * 返回资源容器
	 * @param name		资源容器名称
	 * @return	资源容器
	 */
	public ResourceContainer getResourceContainer(String name){
		if (name == null){
			throw new IllegalArgumentException(
					"The name of an resource container cannot be null.");
		}
		return (ResourceContainer)resContainersHM.get(name);
	}
	
	/**
	 * 移除资源容器
	 * @param name		资源容器名称
	 * @return			资源容器
	 */
	public ResourceContainer removeResourceContainer(String name){
		if (name == null){
			throw new IllegalArgumentException(
					"The name of an resource container cannot be null.");
		}
		return (ResourceContainer)resContainersHM.remove(name);
	}

	
	
	/**
	 * 设置资源
	 * @param name
	 * @param resource
	 */
	public void setResource(String name, Object resource){
		if (name == null){
			throw new IllegalArgumentException(
					"The name of an resource cannot be null.");
		}

		setResource(ThisResourceContainer.class.getName(), name, resource);
	}
	
	/**
	 * 设置资源
	 * @param resContainerName
	 * @param name
	 * @param resource
	 */
	public void setResource(String resContainerName, String name, Object resource){
		if (resContainerName == null){
			throw new IllegalArgumentException(
					"The name of an resource container cannot be null.");
		}
		if (name == null){
			throw new IllegalArgumentException(
					"The name of an resource cannot be null.");
		}
		
		ResourceContainer resContainer = (ResourceContainer)resContainersHM.get(resContainerName);
		if(resContainer==null){
			throw new NullPointerException("The resource container ResourceContainer not exist! resource container name is "+resContainerName);
		}
		if(resContainer.getAttribute(name)!=null){
			throw new RuntimeException("The resource is already exist! name is "+name);
		}
		resContainer.setAttribute(name, resource);
	}
	
	/**
	 * 返回资源
	 * @param name  资源名称
	 * @return 资源
	 */
	public Object getResource(String name){
		return getResource(ThisResourceContainer.class.getName(), name);
	}
	
	/**
	 * 返回资源
	 * @param resContainerName 资源容器名称
	 * @param name 资源名称
	 * @return 资源
	 * 
	 */
	public Object getResource(String resContainerName, String name) {
		if (resContainerName == null){
			throw new IllegalArgumentException(
					"The name of an resource container cannot be null.");
		}
		if (name == null){
			throw new IllegalArgumentException(
					"The name of an resource cannot be null.");
		}

		ResourceContainer resContainer = (ResourceContainer)resContainersHM.get(resContainerName);
		if(resContainer==null){
			throw new NullPointerException("The resource container ResourceContainer not exist! resource container name is "+resContainerName);
		}
		return resContainer.getAttribute(name);
	}
	
	/**
	 * 移除资源
	 * @param name 资源名称
	 * @return 资源
	 * 
	 */
	public Object removeResource(String name) {
		return removeResource(ThisResourceContainer.class.getName(), name);
	}
	/**
	 * 移除资源
	 * @param resContainerName 资源容器名称
	 * @param name 资源名称
	 * @return 资源
	 * 
	 */
	public Object removeResource(String resContainerName, String name) {
		if (resContainerName == null){
			throw new IllegalArgumentException(
					"The name of an resource container cannot be null.");
		}
		if (name == null){
			throw new IllegalArgumentException(
					"The name of an resource cannot be null.");
		}

		ResourceContainer resContainer = (ResourceContainer)resContainersHM.get(resContainerName);
		if(resContainer==null){
			throw new NullPointerException("the resource container ResourceContainer not exist! resource container name is "+resContainerName);
		}
		return resContainer.removeAttribute(name);
	}

	/**
	 * 创建资源
	 * @param resFactory 资源工厂
	 * @return 资源
	 * 
	 */
	private Object createResource(ResourceFactory resFactory) {
		return resFactory.create();
	}
	
	/**
	 * 创建资源, 当资源创建后,将被加入到当前资源容器中
	 * @param name 资源名称
	 * @param resFactory 资源工厂
	 * @return 资源
	 * 
	 */
	private Object createResource(String name, ResourceFactory resFactory) {
		if (name == null){
			throw new IllegalArgumentException(
					"The name of an resource cannot be null.");
		}
		Object resource = createResource(resFactory);
		setResource(name, resource);
		return resource;
	}

	/**
	 * 创建资源, 当资源创建后,将被加入到指定的资源容器中
	 * @param resContainerName 资源容器名称
	 * @param name 资源名称
	 * @param resFactory 资源工厂
	 * @return 资源
	 * 
	 */
	private Object createResource(String resContainerName, String name, ResourceFactory resFactory) {
		if (resContainerName == null){
			throw new IllegalArgumentException(
					"The name of an resource container cannot be null.");
		}
		if (name == null){
			throw new IllegalArgumentException(
					"The name of an resource cannot be null.");
		}
		Object resource = createResource(resFactory);
		setResource(resContainerName, name, resource);
		return resource;
	}


	
	
	/**
	 * 当前的资源容器
	 */
	class ThisResourceContainer implements ResourceContainer{

	  Map resourcesHM = new HashMap();
		
		public void setAttribute(String name, Object resource) {
			resourcesHM.put(name, resource);
		}

		public Object getAttribute(String name) {
			return resourcesHM.get(name);
		}

		public Object removeAttribute(String name) {
			return resourcesHM.remove(name);
		}

		
		public Enumeration getAttributeNames() {
			return new Enumeration(){
				Iterator it = resourcesHM.keySet().iterator();
				public boolean hasMoreElements() {
					return it.hasNext();
				}

				public Object nextElement() {
					return it.next();
				}
			};
		}

	}
	
}
