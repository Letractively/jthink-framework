/*
 * TransactionEvent.java	2005-7-4
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

import java.util.EventObject;

/**
 * 事务事件对象。在事务监听器TransactionListener中的监听方法中返回此类型的实例。
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
public class TransactionEvent extends EventObject {

  private static final long serialVersionUID = -8902929862646562433L;
  private TransactionManager transactionManager;
	
	/**
	 * 创建TransactionEvent类型的实例
	 * 
	 * @param transactionManager 事务管理器(TransactionManager)
	 */
	public TransactionEvent(TransactionManager transactionManager){
		super(transactionManager);

		this.transactionManager = transactionManager;
	}
	
	
	/**
	 * 返回事务管理器
	 * 
	 * @return TransactionManager
	 */
	public TransactionManager getTransactionManager(){
		return transactionManager;
	}
	
	
	
}
