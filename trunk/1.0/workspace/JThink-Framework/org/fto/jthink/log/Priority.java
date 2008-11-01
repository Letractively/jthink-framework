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

/**
 * 日志输出优先级别。日志信息的输出级别顺序为：
 * DEBUG->INFO->WARN->ERROR->FATAL。DEBUG为最底级别，FATAL为最高级别。
 * 如果在配置文件中设置优先级别为DEBUG，将输出所有日志信息，如果配置为FATAL，
 * 将只会输出级别为FATAL的日志信息。
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
public class Priority {

	/* 优先级别值 */
  private int level;
  /* 优先级别串 */
  private String levelStr;
	
  /* 描述严重错误的优先级别值: 50000 */
  public final static int FATAL_INT = 50000;
  /* 描述错误的优先级别值: 50000 */
  public final static int ERROR_INT = 40000;
  /* 描述警告的优先级别值: 30000 */
  public final static int WARN_INT  = 30000;
  /* 描述一般信息的优先级别值: 20000 */
  public final static int INFO_INT  = 20000;
  /* 描述调试信息的优先级别值: 10000 */
  public final static int DEBUG_INT = 10000;	
	
  /** 定义优先级别对象，级别为严重错误（FATAL） */
  public final static Priority FATAL = new Priority(FATAL_INT, "FATAL");

  /** 定义优先级别对象，级别为错误（ERROR） */
  public final static Priority ERROR = new Priority(ERROR_INT, "ERROR");

  /** 定义优先级别对象，级别为警告（FATAL） */
  public final static Priority WARN  = new Priority(WARN_INT, "WARN");

  /** 定义优先级别对象，级别为一般信息（WARN） */
  public final static Priority INFO  = new Priority(INFO_INT, "INFO");

  /** 定义优先级别对象，级别为调用（DEBUG） */
  public final static Priority DEBUG = new Priority(DEBUG_INT, "DEBUG");

  private Priority(int level, String levelStr) {
    this.level = level;
    this.levelStr = levelStr;
  }
  
  /**
   * 判断是否严重错误
   */
  public boolean isFatal(Priority p){
  	return p.level == FATAL_INT;
  }
  
  /**
   * 判断是否错误
   */
  public boolean isError(Priority p){
  	return p.level == ERROR_INT;
  }
  
  /**
   * 判断是否警告
   */
  public boolean isWarn(Priority p){
  	return p.level == WARN_INT;
  }
  
  /**
   * 判断是否信息
   */
  public boolean isInfo(Priority p){
  	return p.level == INFO_INT;
  }
  
  /**
   * 判断是否调用
   */
  public boolean isDebug(Priority p){
  	return p.level == DEBUG_INT;
  }
 
  /**
   * 返回优先级别值
   */
  public int getLevel(){
  	return level;
  }

  /**
   * 判断是否严重错误
   */
  public String getLevelString(){
  	return levelStr;
  }
  
  
  public String toString(){
  	return levelStr;
  }
  
}
