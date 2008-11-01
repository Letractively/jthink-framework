/*
 * HsqlSQLExecutorFactory.java	2008-10-28
 *
 * 版权所有: 自由思考组织(FTO)软件团队 2000-2008, 保留所有权利.
 * 这个软件是自由思考组织(FTO)软件团队开发的，如果要使用这个软件，请首先阅读并接受许可协议。
 *
 * Copyright 2000-2008 FTO Software Team, Inc. All Rights Reserved.
 * This software is the proprietary information of FTO Software Team, Inc.
 * Use is subject to license terms.
 *
 * FTO站点：http://www.free-think.org
 */

package org.fto.jthink.jdbc.hsql;

import org.fto.jthink.jdbc.JDBCTransaction;
import org.fto.jthink.jdbc.SQLExecutor;
import org.fto.jthink.jdbc.SQLExecutorFactory;

/**
 * HsqlSQLExecutor工厂，用工厂创建HsqlSQLExecutor类型的实例。
 * 
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2008-10-28  创建此类型
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 */

public class HsqlSQLExecutorFactory implements SQLExecutorFactory {

	private String connId;
	private JDBCTransaction transaction;
	
  /**
   * 创建DefaultSQLExecutorFactory类型的实例
   * 
   * @param transaction  JDBCTransaction事务对象
   * @param connId 在配置文件中配置的数据库连接ID
   */
  public HsqlSQLExecutorFactory(JDBCTransaction transaction, String connId){
    this.transaction = transaction;
    this.connId = connId;
  }
	
	/**
	 * 创建HsqlSQLExecutor的实例，以SQLExecutor类型返回
	 */
	public SQLExecutor create() {
    return new HsqlSQLExecutor(transaction, connId);
	}

}
