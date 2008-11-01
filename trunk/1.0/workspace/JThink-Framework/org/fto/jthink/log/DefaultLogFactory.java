/*
 * DefaultLogFactory.java 2005-7-24
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

/**
 * 默认日志(DefaultLogger)工厂，用此工厂创建DefaultLogger类型的实例。
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
public class DefaultLogFactory implements LogFactory {
	private Configuration config;
//	private DefaultLogger logger;
	private DefaultLogWriter logWriter;
	
	/**
	 * 创建DefaultLogFactory的实例
	 * 
	 * @param config 系统配置文件信息
	 */
	public DefaultLogFactory(Configuration config){
		this.config = config;
		/* 创建日志Writer, 用于向日志文件输出日志信息 */
		logWriter = new DefaultLogWriter(config);
		if(logWriter.isAsynchronism()){
			/* 如果配置为异步处理, 启动异步处理线程 */
			logWriter.start();
		}
	}
	
	/**
	 * 创建默认日志处理器, 以接口类型Logger返回
	 */
	public Logger create(String name){
		return new DefaultLogger(config, name, logWriter);
	}
	
  /**
   * 创建默认日志处理器, 以接口类型Logger返回
   */
  public Logger create(Class clazz){ 
    return new DefaultLogger(config, clazz.getName(), logWriter);
  }
}
