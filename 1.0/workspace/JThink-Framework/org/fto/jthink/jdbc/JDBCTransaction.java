/*
 * JDBCTransaction.java	2005-6-23
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

import java.sql.Connection;

import org.fto.jthink.transaction.Transaction;



/**
 * 数据库事务管理接口，要使用JDBCTransaction，必须提供正确的JDBC驱动，
 * 因为JDBCTransaction的实现是与JDBC密切联系在一起的。
 * 
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2005-06-23  创建此类型
 * 2008-10-24  加入数据库事务隔离级别
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 * @see org.fto.jthink.transaction.Transaction
 * @see org.fto.jthink.resource.ResourceContainer
 */

public interface JDBCTransaction extends Transaction {

  /** 事务级别：0, 不支持事务 */
  final public static int TRANSACTION_NONE=Connection.TRANSACTION_NONE;//0;
  
  /** 事务级别：1, 未授权读取 允许脏读取，但不允许更新丢失。
   * 如果一个事务已经开始写数据，则另外一个数据则不允许同时进行写操作，
   * 但允许其他事务读此行数据。该隔离级别可以通过“排他写锁”实现。 */
  final public static int TRANSACTION_READ_UNCOMMITTED=Connection.TRANSACTION_READ_UNCOMMITTED;//1;
  
  /** 事务级别：2, 授权读取 允许不可重复读取，但不允许脏读取。这可以通过
   * “瞬间共享读锁”和“排他写锁”实现。读取数据的事务允许其他事务继续访
   * 问该行数据，但是未提交的写事务将会禁止其他事务访问该行 */
  final public static int TRANSACTION_READ_COMMITTED=Connection.TRANSACTION_READ_COMMITTED;//2;
  
  /** 事务级别：4, 可重复读取 禁止不可重复读取和脏读取，但是有时可能出
   * 现幻影数据。这可以通过“共享读锁”和“排他写锁”实现。读取数据的事
   * 务将会禁止写事务（但允许读事务），写事务则禁止任何其他事务。 */
  final public static int TRANSACTION_REPEATABLE_READ=Connection.TRANSACTION_REPEATABLE_READ;//4;
  
  /** 事务级别：8, 全序列化 提供严格的事务隔离。它要求事务序列化执行，
   * 事务只能一个接着一个地执行，但不能并发执行。如果仅仅通过“行级锁”
   * 是无法实现事务序列化的，必须通过其他机制保证新插入的数据不会被
   * 刚执行查询操作的事务访问到。 */
  final public static int TRANSACTION_SERIALIZABLE=Connection.TRANSACTION_SERIALIZABLE;//8;
  
  /**
   * 设置事务隔离级别
   * 
   * @param connId 数据库连接ID
   * @param level 事务隔离级别，只能是常量
   * JDBCTransaction.TRANSACTION_NONE,
   * JDBCTransaction.TRANSACTION_READ_UNCOMMITTED,
   * JDBCTransaction.TRANSACTION_READ_COMMITTED,
   * JDBCTransaction.TRANSACTION_REPEATABLE_READ,
   * JDBCTransaction.TRANSACTION_SERIALIZABLE之一
   * 
   */
  public void setTransactionLevel(String connId, int level);
  
  /**
   * 返回事务隔离级别，如果没有设置，返回-1(表示采用默认级别)
   * 
   * @param connId 数据库连接ID
   * @return 事务隔离级别
   */
  public int getTransactionLevel(String connId);
  
  /**
   * 设置事务提交方式
   * 
   * @param connId 数据库连接ID
   * @param autoCommit true或false，如果值为true,表示设置为自动事务提交方式
   * 
   */
  public void setAutoCommit(String connId, boolean autoCommit);

  /**
   * 返回事务提交方式
   * 
   * @param connId 数据库连接ID
   * @return 事务提交方式
   * 
   */
  public boolean getAutoCommit(String connId);
  
	/**
	 * 根据配置的数据库连接ID创建或返回一个可用的数据库JDBC连接(java.sql.Connection)
	 * 
	 * @param connId 在配置文件中定义的数据库连接ID值
	 * 
	 * @return　返回根据connId创建或找到的可用数据库连接 
	 */

	public Connection getConnection(String connId);
	
	/**
	 * 返回SQLExecutorFactory工厂的具体实现, 此工厂是在配置文件中与连接ID相关的配置项中定义的
	 * 
	 * @param connId 数据库连接ID
	 * 
	 * @return SQLExecutorFactory工厂的具体实现
	 */
	public SQLExecutorFactory getSQLExecutorFactory(String connId);
	
	/**
	 * 返回SQLBuilderFactory工厂的具体实现, 此工厂是在配置文件中与连接ID相关的配置项中定义的
	 * 
	 * @param connId 连接ID
	 * 
	 * @return SQLBuilderFactory工厂的具体实现
	 */
	public SQLBuilderFactory getSQLBuilderFactory(String connId);
	
}
