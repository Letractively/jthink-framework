/*
 * MBoardJBeanInitialization.java	2005-10-14
 *
 * 版权所有: 自由思考组织(FTO)软件团队 2000-2005, 保留所有权利.
 * 这个软件是自由思考组织(FTO)软件团队开发的，如果要使用这个软件，请首先阅读并接受许可协议。
 *
 * Copyright 2000-2005 FTO Software Team, Inc. All Rights Reserved.
 * This software is the proprietary information of FTO Software Team, Inc.
 * Use is subject to license terms.
 *
 * FTO站点：http://www.free-think.org
 */
package org.fto.jthink.sample.mboard.util;

import javax.servlet.http.HttpServletRequest;

import org.fto.jthink.config.Configuration;
import org.fto.jthink.j2ee.web.WEBApplicationContext;
import org.fto.jthink.jdbc.JDBCTransaction;
import org.fto.jthink.log.LogManager;
import org.fto.jthink.log.Logger;
import org.fto.jthink.resource.ResourceManager;
import org.fto.jthink.transaction.DefaultTransactionManager;
import org.fto.jthink.transaction.TransactionFactory;
import org.fto.jthink.transaction.TransactionManager;

/**
 * 初始化JavaBean, 此程序演示了初始化事务管理器，初始化事务处理对象。此类型在具体的JavaBean初始化阶段创建，
 * 并调用initialize()方法初始化JB，同时返回资源管理器，此资源管理器是在过滤器(MBoardHttpRequestFilter)中创建的。
 * 
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink Sample Message Board 1.0
 * @see 	  JThink 1.0
 * 
 */
public class MBoardJBeanInitialization {
		
	private static final Logger logger = LogManager.getLogger(MBoardJBeanInitialization.class);

	/**
	 * 初始化JB
	 */
	public ResourceManager initialize(HttpServletRequest req){
		
		logger.debug("初始化JavaBean.");
		
		/* 返回资源管理器 */
		ResourceManager resManager = (ResourceManager)req.getAttribute(ResourceManager.class.getName());
		
		Configuration jthinkConfig = (Configuration)resManager.getResource(WEBApplicationContext.class.getName(), Configuration.class.getName());
		
		if(resManager.getResource(TransactionManager.class.getName())==null){
			/* 设置资源, 初始化事务管理器, 将TransactionManager加入到ResourceManager中 */
			logger.debug("初始化事务管理器, TransactionManager.");
			TransactionManager transactionManager = new DefaultTransactionManager(resManager, jthinkConfig);
			resManager.setResource(TransactionManager.class.getName(), transactionManager);
			
			// 加入WEB端事务到事务管理器
			logger.debug("初始化Web端事务, WebTransaction.");
			TransactionFactory transactionFactory = transactionManager.getTransactionFactory("WebTransaction");
			JDBCTransaction transaction  = (JDBCTransaction)transactionFactory.create();
			transactionManager.addTransaction(JDBCTransaction.class.getName(), transaction);
			
		}
		
		return resManager;
	}
}
