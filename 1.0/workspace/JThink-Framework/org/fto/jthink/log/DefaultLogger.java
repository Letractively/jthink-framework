/*
 * DefaultLogger.java 2005-7-24
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
import org.fto.jthink.exception.JThinkRuntimeException;
import org.fto.jthink.util.DateTimeHelper;
import org.jdom.Element;

/**
 * 日志处理接口(Logger)的默认实现。
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
public class DefaultLogger implements Logger {

	/* 输出日志的类名称 */
	private String className;
	/* 在配置文件中设置的日志输出优先级 */
	private Priority cfgPriority;
	/* 仅输出日志信息 */
	private boolean onlyMessge = false;

	/* 用于向文件中输出日志 */
	private DefaultLogWriter logWriter;

	/**
	 * 创建DefaultLogger的实例
	 * 
	 * @param config 描述配置文件信息的对象
	 * @param name 输出日志的类的名称
	 * @param logWriter 日志输出器
	 */
	public DefaultLogger(Configuration config,String name, DefaultLogWriter logWriter){
		this.className = name;
		this.logWriter = logWriter; 
		if(config==null){
			cfgPriority = Priority.INFO;
			return;
		}
		
		Element logConfig = config.getLogManagerConfig();
		
		/* 是否只输入描述信息 */
		onlyMessge = logConfig.getChildText("only-message").equalsIgnoreCase("yes");
		
		/* 返回配置的日志优先级 */
		String priorityStr = logConfig.getChildText("priority");
		if(priorityStr.equalsIgnoreCase(Priority.DEBUG.getLevelString())){
			cfgPriority = Priority.DEBUG;
			
		}else if(priorityStr.equalsIgnoreCase(Priority.INFO.getLevelString())){
			cfgPriority = Priority.INFO;
			
		}else if(priorityStr.equalsIgnoreCase(Priority.WARN.getLevelString())){
			cfgPriority = Priority.WARN;
			
		}else if(priorityStr.equalsIgnoreCase(Priority.ERROR.getLevelString())){
			cfgPriority = Priority.ERROR;
			
		}else if(priorityStr.equalsIgnoreCase(Priority.FATAL.getLevelString())){
			cfgPriority = Priority.FATAL;
			
		}else{
			throw new JThinkRuntimeException("错误的日志优先级定义(priority)定义!");
		}
		
	}

	/**
	 * 输出调试信息
	 * 
	 * @param message 被输出的信息
	 */
	public void debug(Object message) {
		log(Priority.DEBUG, message);
		
	}

	/**
	 * 输出调试信息
	 * 
	 * @param message 被输出的信息
	 * @param t Throwable的实例或其子类型的实例
	 */
	public void debug(Object message, Throwable t) {
		log(Priority.DEBUG, message, t);
		
	}

	/**
	 * 输出错误信息
	 * 
	 * @param message 被输出的信息
	 */
	public void error(Object message) {
		log(Priority.ERROR, message);
		
	}

	/**
	 * 输出错误信息
	 * 
	 * @param message 被输出的信息
	 * @param t Throwable的实例或其子类型的实例
	 */
	public void error(Object message, Throwable t) {
		log(Priority.ERROR, message, t);
		
	}

	/**
	 * 输出严重错误信息
	 * 
	 * @param message 被输出的信息
	 */
	public void fatal(Object message) {
		log(Priority.FATAL, message);
		
	}

	/**
	 * 输出严重错误信息
	 * 
	 * @param message 被输出的信息
	 * @param t Throwable的实例或其子类型的实例
	 */
	public void fatal(Object message, Throwable t) {
		log(Priority.FATAL, message, t);
		
	}

	/**
	 * 输出一般信息
	 * 
	 * @param message 被输出的信息
	 */
	public void info(Object message) {
		log(Priority.INFO, message);
		
	}

	/**
	 * 输出一般信息
	 * 
	 * @param message 被输出的信息
	 * @param t Throwable的实例或其子类型的实例
	 */
	public void info(Object message, Throwable t) {
		log(Priority.INFO, message, t);
		
	}

	/**
	 * 输出警告信息
	 * 
	 * @param message 被输出的信息
	 */
	public void warn(Object message) {
		log(Priority.WARN, message);
	}

	/**
	 * 输出警告信息
	 * 
	 * @param message 被输出的信息
	 * @param t Throwable的实例或其子类型的实例
	 */
	public void warn(Object message, Throwable t) {
		log(Priority.WARN, message, t);
	}

	/**
	 * 绝对输出日志信息,此方法不管在配置文件中是否设置了日志输出优先级别,都会将日志输出 
	 * 
	 * @param message 被输出的信息
	 */
	public void log(Object message) {
		log(null, message, null);
	}

	/**
	 * 绝对输出日志信息,此方法不管在配置文件中是否设置了日志输出优先级别,都会将日志输出 
	 * 
	 * @param message 被输出的信息
	 * @param t Throwable的实例或其子类型的实例
	 */
	public void log(Object message, Throwable t) {
		log(null, message, t);
	}
	
	/**
	 * 输出日志信息
	 * 
	 * @param priority 优先级别, 如果此参数为null,将绝对输出日志信息
	 * @param message 被输出的信息
	 */
	public void log(Priority priority, Object message) {
		log(priority, message, null);
	}	
	
	/**
	 * 输出日志信息
	 * 
	 * @param priority 优先级别, 如果此参数为null,将绝对输出日志信息
	 * @param message 被输出的信息
	 * @param t Throwable的实例或其子类型的实例
	 */
	public void log(Priority priority, Object message, Throwable t) {
		if(priority!=null && cfgPriority!=null && priority.getLevel()<cfgPriority.getLevel()){
			return;
		}
    StringBuffer sb=new StringBuffer();
    if(onlyMessge==false){
  		String currentTime = "["+DateTimeHelper.formatDateTimetoString(DateTimeHelper.getSystemDate(), DateTimeHelper.FMT_yyyyMMddHHmmssS)+"]";
	    sb.append(currentTime);
	    sb.append(" [");
	    sb.append(priority==null?"LOG":priority.toString());
	    sb.append("] [");
	    sb.append((Thread.currentThread()).getName());
	    sb.append("] [");
	    sb.append(className);
	    sb.append("] ");
    }
    if(message!=null){
    	sb.append(message.toString());
    }else{
    	sb.append("null");
    }
    /* 写日志信息 */
    if(logWriter!=null){
      logWriter.write(priority, sb.toString(), t);
    }

	}

	/**
	 * 同步所有日志信息的输出, 将其输出到文件或其它设备
	 */
	public void synchronize(){
	  if(logWriter!=null){
	    logWriter.synchronize();
	  }
	}
	
	
	/**
	 * 判断当前配置的日志输出优先级别是否是DEBUG
	 */
	public boolean isDebugEnabled() {
		return cfgPriority.getLevel()<=Priority.DEBUG.getLevel();
	}

	/**
	 * 判断当前配置的日志输出优先级别是否是INFO
	 */
	public boolean isInfoEnabled() {
		return cfgPriority.getLevel()<=Priority.INFO.getLevel();
	}
	
}
