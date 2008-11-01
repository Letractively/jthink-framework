/*
 * LogFactory.java 2005-7-24
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


/**
 * 日志工厂。用此工厂的具体实现来创建日志处理器，JThink提供了默认的日志处理器工厂。
 * 注意: 此工厂的具体实现必须要提供以org.fto.jthink.config.Configuration类型为参数的构造方法。
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
public interface LogFactory {

	/**
	 * 创建并返回日志处理器
	 * 
	 * @param clazz 用于标识日志信息所属对象的类
	 * 
	 * @return 日志处理器
	 */
	public Logger create(Class clazz);

  /**
   * 创建并返回日志处理器
   * 
   * @param name 用于标识日志信息的名称
   * 
   * @return 日志处理器
   */
  public Logger create(String name);
}
