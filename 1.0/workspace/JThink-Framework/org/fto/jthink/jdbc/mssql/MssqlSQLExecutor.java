/*
 * MssqlSQLExecutor.java	2005-7-1
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
package org.fto.jthink.jdbc.mssql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.fto.jthink.exception.JThinkErrorCode;
import org.fto.jthink.exception.JThinkRuntimeException;
import org.fto.jthink.jdbc.JDBCTransaction;
import org.fto.jthink.jdbc.SQL;
import org.fto.jthink.jdbc.SQLExecutor;


/**
 * 此类型的实例负责执行SQL语句，针对MS SQL Server数据库。此类型扩展了SQLExecutor类型，
 * 并覆盖了executeQuery（）和executeStoredProcedure（）两个方法，以便执行具有分页功能的SQL语句。
 * 
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2005-07-01  创建此类型
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 */

public class MssqlSQLExecutor extends SQLExecutor {
  		
  /**
   * 创建MssqlSQLExecutor的实例
   * 
   * @param transaction JDBC事件
   * @param connId 数据库连接ID 
   */
  public MssqlSQLExecutor(JDBCTransaction transaction, String connId){
  	super(transaction, connId);
  }	

  /**
   * 执行查询操作的SQL，比如：Select等操作, 用此查询方法可实现数据分页。
   *
   * @param sql          			 查询操作的SQL串
   * @param values						 值对象数组,用于填充SQL中的“?”
   * @param startIndex        选择出的结果集的行开始索引
   * @param rowlen            选择出的结果集的行数
   * @param doClazz           数据对象类型，只有采用DataObjectResultMaker时此参数才有意义
   *
   * @return   结果集               
   *
   */
	public Object executeQuery(String sql, Object[] values, int startIndex, int rowlen, Class doClazz) {
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
    	executeCommand(sql, values, SQL.SELECT);
      pstmt = prepareStatement(sql);
      if(values!=null){
				for (int i = 0; i < values.length; i++) {
					pstmt.setObject(i + 1, values[i]);
				}
      }
      rs = pstmt.executeQuery();
      return getResultMaker().create(rs, doClazz, startIndex, rowlen);
    }
    catch (SQLException ex) {
      throw new JThinkRuntimeException(JThinkErrorCode.ERRCODE_DB_EXEC_SQL_EXCEPTION, ex);
    }
    finally {
      releaseResultSet(rs);
      releasePreparedStatement(pstmt);
    }
	}

}
