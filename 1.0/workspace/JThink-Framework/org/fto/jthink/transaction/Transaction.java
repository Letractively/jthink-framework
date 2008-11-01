/*
 * Transaction.java	2005-6-23
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

import org.fto.jthink.resource.ResourceContainer;


/**
 * 事务接口，此类型定义了JThink中的所有事务处理的超接口。如果想让TransactionManager能统一的管理所有事务，
 * 就必须实现此事务接口。JThink中已经提供了管理数据库事务的Transaction实现，能够方便的管理数据库事务。
 *
 * 
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2005-06-23  创建此类型
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 * @see org.fto.jthink.resource.ResourceContainer
 * @see org.fto.jthink.transaction.TransactionManager
 */

public interface Transaction extends ResourceContainer{
	
	/**
	 * 设置事务提交方式
	 * 
	 * @param autoCommit true或false，如果值为true,表示设置为自动事务提交方式
	 * 
	 * @deprecated
	 */
	public void setAutoCommit(boolean autoCommit);

	/**
	 * 返回事务提交方式
	 * 
	 * @return 事务提交方式
	 * 
	 * @deprecated
	 */
	public boolean getAutoCommit();
	
	/**
	 * 开始事务
	 */
	public void begin();
	
	/**
	 * 提交事务　
	 */
	public void commit();
	
	/**
	 * 回退事务
	 */
	public void rollback();
	
	/**
	 * 关闭事务
	 */
	public void close();
}




