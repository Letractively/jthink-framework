/*
 * MBoardWEBApplicationContextListener.java	2005-10-14
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

import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextEvent;

import org.fto.jthink.config.Configuration;
import org.fto.jthink.j2ee.web.WEBApplicationContextListener;
import org.fto.jthink.jdbc.ConnectionPool;
import org.fto.jthink.log.LogManager;
import org.fto.jthink.log.Logger;

/**
 * 应用程序上下文监听器，此程序演示了在应用程序起动时的初始化工作，装入fto-jthink.xml配置文件信息，
 * 初始日志等。以及在应用程序终止时对系统的清理工作。
 * 
 * 
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink Sample Message Board 1.0
 * @see 	  JThink 1.0
 * 
 */
public class MBoardWEBApplicationContextListener
	implements WEBApplicationContextListener {

	private static final Logger logger = LogManager.getLogger(MBoardWEBApplicationContextListener.class);
	

	/**
	 * @see javax.servlet.ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext webContext = sce.getServletContext();
		/* 
		 * 装入fto-jthink.xml文件, 
		 * 在资源管理器(ResourceManager)中可以通过getResource方法返回fto-jthink.xml配置
		 */
		String jthinkConfig = webContext.getRealPath(webContext.getInitParameter("jthink-config-file"));
		Configuration config = new Configuration(jthinkConfig);
		webContext.setAttribute(Configuration.class.getName(), config);

		/*
		 * 初始化日志, LogManager
		 */
		LogManager.configure(config);
	}

	/**
	 * @see javax.servlet.ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
		/* 关闭连接池, 以便于释放数据库连接 */
		ConnectionPool.closeConnectionPool();
		/* 同步所有日志信息 */
		logger.synchronize();
	}

	/**
	 * @see javax.servlet.ServletContextAttributeListener#attributeAdded(ServletContextAttributeEvent)
	 */
	public void attributeAdded(ServletContextAttributeEvent arg0) {
	}

	/**
	 * @see javax.servlet.ServletContextAttributeListener#attributeRemoved(ServletContextAttributeEvent)
	 */
	public void attributeRemoved(ServletContextAttributeEvent arg0) {
	}

	/**
	 * @see javax.servlet.ServletContextAttributeListener#attributeReplaced(ServletContextAttributeEvent)
	 */
	public void attributeReplaced(ServletContextAttributeEvent arg0) {
	}

}
