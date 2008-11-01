/*
 * TransactionListener.java	2005-7-4
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
package org.fto.jthink.transaction;

import java.util.EventListener;


/**
 * 事务监听器。
 * 
 * 
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2005-07-04  创建此类型
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 */
public interface TransactionListener extends EventListener{

  /**
   * 当开始事务时触发此事件
   * @param evt 事务事件对象(TransactionEvent)
   */
  public void transactionBeginned(TransactionEvent evt);
  /**
   * 当提交事务时触发此事件
   * @param evt 事务事件对象(TransactionEvent)
   */
  public void transactionCommitted(TransactionEvent evt);
  /**
   * 当回滚事务时触发此事件
   * @param evt 事务事件对象(TransactionEvent)
   */
  public void transactionRollbacked(TransactionEvent evt);
  /**
   * 当关闭事务时触发此事件
   * @param evt 事务事件对象(TransactionEvent)
   */
  public void transactionClosed(TransactionEvent evt);
	
	
}
