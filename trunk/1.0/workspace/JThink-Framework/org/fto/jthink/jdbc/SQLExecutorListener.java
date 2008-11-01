/*
 * SQLExecutorEvent.java	2005-7-5
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
package org.fto.jthink.jdbc;

import java.util.EventListener;

/**
 * SQLExecutor监听器。使用此接口的实现来监听在SQLExecutor，
 * 在事件方法executeSQLCommand()中返回当前正被执行的SQL。
 * 
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2005-07-05  创建此类型
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 */
public interface SQLExecutorListener  extends EventListener{

  /**
   * 当执行一个SQL时触发此事件
   * 
   * @param evt 事件，SQLExecutorEvent的实例
   */
  public void executeSQLCommand(SQLExecutorEvent evt);
	
	
}
