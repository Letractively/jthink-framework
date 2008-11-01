/*
 * LogManager.java 2005-7-24
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
package org.fto.jthink.log;

import org.fto.jthink.config.Configuration;
import org.fto.jthink.util.ReflectHelper;

/**
 * 日志管理器。此类是日志处理的关键入口，在应用系统启动时，
 * 须调用configure()方法来初始化日志管理器。
 * 
 * <pre>
 *  在应用中定义日志处理接口：
 *    private static final Logger logger = LogManager.getLogger(ConcreteType.class);
 *  //ConcreteType为具体的类型
 * </pre>
 * 
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2005-07-24  创建此类型
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 */
public class LogManager {
	
	/* 当前日志工厂 */
	private static LogFactory logFactory;

  /**
   * 设置日志工厂
   * 
   * @param factory 日志工厂
   */
	public static void setLogFactory(LogFactory factory){
		logFactory = factory;
	}

	/**
	 * 返回日志工厂, 如果没有设置日志工厂,将创建一个默认的日志处理工厂
	 * 
	 * @return 日志工厂
	 */
	public static LogFactory getLogFactory(){
		if(logFactory==null){
			logFactory = new DefaultLogFactory(null);
		}
		return logFactory;
	}

	/**
	 * 返回日志处理器
	 * 
	 * @param clazz 要使用此日志处理器的类型
	 * 
	 * @return 日志处理器
	 */
	public static Logger getLogger(Class clazz){
	  return getLogFactory().create(clazz);
	}

	/**
	 * 返回日志处理器
	 * 
	 * @param name 要使用此日志处理器的类型名称
	 * 
	 * @return 日志处理器
	 */
	public static Logger getLogger(String name){
		return getLogFactory().create(name);
	}
	
	/**
	 * 从配置文件中查找并创建日志工厂
	 * 
	 * @param config
	 */
	public static void configure(Configuration config){
		String factoryClass = config.getConfig().getChild("log-manager").getChildText("factory-class");
		LogFactory factory = (LogFactory)ReflectHelper.newInstance(ReflectHelper.forName(factoryClass), 
				new Class[]{Configuration.class}, 
				new Object[]{config});
		setLogFactory(factory);
	}
	
	
}
