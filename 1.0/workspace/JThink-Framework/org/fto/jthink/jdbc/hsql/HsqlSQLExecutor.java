/*
 * HsqlSQLExecutor.java	2008-10-28
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

/**
 * 此类型的实例负责执行SQL语句，针对Hsql数据库。此类型扩展了SQLExecutor类型，
 * 并覆盖了executeQuery（）和executeStoredProcedure（）两个方法，以便执行具有分页功能的SQL语句。
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
public class HsqlSQLExecutor extends SQLExecutor {
  		
  /**
   * 创建HsqlSQLExecutor的实例
   * 
   * @param transaction JDBC事件
   * @param connId 数据库连接ID 
   */
  public HsqlSQLExecutor(JDBCTransaction transaction, String connId){
  	super(transaction, connId);
  }	

  /**
   * 执行查询操作的SQL，比如：Select等操作, 用此查询方法可实现数据分页。
   * 此方法必须要在实际应用中根据具体数据库在此类的子类型中覆盖，以便实现分页功能
   * 
   * @param sql 查询操作的SQL语句
   * @param values 值数组,用于替换SQL语句中的?, values可以为null对象
   * @param startIndex 结果集的开始索引位置
   * @param rowlen 需要选择出的结果集的长度
   * @param doClazz 数据对象类型，只有采用DataObjectResultMaker时此参数才有意义
   * 
   * @return 结果集, 结果集的数据结构由ResultMaker来决定
   */
  public Object executeQuery(String sql, Object[] values, int startIndex, int rowlen, Class doClazz)  {
    return super.executeQuery(sql, values, doClazz);
  }
  
}
