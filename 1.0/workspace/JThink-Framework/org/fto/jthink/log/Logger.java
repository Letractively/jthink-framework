/*
 * Logger.java 2005-7-24
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
 * 日志处理器接口。要采用JThink来统一处理日志，必须为应用的日志处理器实现此接口。
 * 在JThink中已经提供了日志处理处理的默认实现，但如果想使用其它第三方的日志处理组件，
 * 请重新实现此接口。
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
public interface Logger {

	/**
	 * 输出调试信息
	 * 
	 * @param message 被输出的信息
	 */
  public void debug(Object message);
  
	/**
	 * 输出调试信息
	 * 
	 * @param message 被输出的信息
	 * @param t Throwable的实例或其子类型的实例
	 */
  public void debug(Object message, Throwable t);

	/**
	 * 输出错误信息
	 * 
	 * @param message 被输出的信息
	 */
  public void error(Object message);
  
	/**
	 * 输出错误信息
	 * 
	 * @param message 被输出的信息
	 * @param t Throwable的实例或其子类型的实例
	 */
  public void error(Object message, Throwable t);

	/**
	 * 输出严重错误信息
	 * 
	 * @param message 被输出的信息
	 */
  public void fatal(Object message);
  
	/**
	 * 输出严重错误信息
	 * 
	 * @param message 被输出的信息
	 * @param t Throwable的实例或其子类型的实例
	 */
  public void fatal(Object message, Throwable t);

	/**
	 * 输出一般信息
	 * 
	 * @param message 被输出的信息
	 */
  public void info(Object message);
  
	/**
	 * 输出一般信息
	 * 
	 * @param message 被输出的信息
	 * @param t Throwable的实例或其子类型的实例
	 */
  public void info(Object message, Throwable t);

	/**
	 * 输出警告信息
	 * 
	 * @param message 被输出的信息
	 */
  public void warn(Object message);
  
	/**
	 * 输出警告信息
	 * 
	 * @param message 被输出的信息
	 * @param t Throwable的实例或其子类型的实例
	 */
  public void warn(Object message, Throwable t);
  
	/**
	 * 绝对输出日志信息,此方法不管在配置文件中是否设置了日志输出优先级别,都会将日志输出 
	 * 
	 * @param message 被输出的信息
	 */
  public void log(Object message);
  
	/**
	 * 绝对输出日志信息,此方法不管在配置文件中是否设置了日志输出优先级别,都会将日志输出 
	 * 
	 * @param message 被输出的信息
	 * @param t Throwable的实例或其子类型的实例
	 */
  public void log(Object message, Throwable t);
  
	/**
	 * 输出日志信息
	 * 
	 * @param priority 优先级别, 如果此参数为null,将绝对输出日志信息
	 * @param message 被输出的信息
	 */
  public void log(Priority priority, Object message, Throwable t);
  
	/**
	 * 输出日志信息
	 * 
	 * @param priority 优先级别, 如果此参数为null,将绝对输出日志信息
	 * @param message 被输出的信息
	 */
  public void log(Priority priority, Object message);
  
	/**
	 * 同步所有日志信息的输出, 将其输出到文件或其它设备
	 */
  public void synchronize();
  
	/**
	 * 判断当前配置的日志输出优先级别是否是DEBUG
	 */
  public boolean isDebugEnabled();
  
	/**
	 * 判断当前配置的日志输出优先级别是否是INFO
	 */
  public boolean isInfoEnabled();
  
  
}

