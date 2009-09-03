/*
 * SQLExecutor.java	2005-7-15
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

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.fto.jthink.exception.JThinkErrorCode;
import org.fto.jthink.exception.JThinkRuntimeException;
import org.fto.jthink.lang.ObjectBuffered;
import org.fto.jthink.util.ReflectHelper;
import org.jdom.Element;

/**
 * 此类型的实例负责执行SQL语句。
 * 
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2005-07-16  创建此类型
 * 2008-10-24  增加方法：setTimeout()和getTimeout()方法，设置在执行SQL时的超时时间
 * 2008-10-27  对于更新操作的返回结果，以前是返回null，现在调整为返回Integer类型的更新计数
 * 2008-10-27  增加批量执行SQL方法executeBatch()
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 */

public class SQLExecutor {
	private String connId;
	private JDBCTransaction transaction;
	/* 设置执行SQL超时时间,单位：秒，默认值0,0表式不做超时处理 */
	private int timeout=0;
	
	/* 将此 Statement 对象生成的所有 ResultSet 对象可以包含的最大行数限制设置为给定数。如果超过了该限制，则直接撤消多出的行 */
	private int maxrows=0;
	
//	/* 请求将 Statement 池化或非池化,值：1=true/0=false/-1=默认, 需要在jre 1.6及以后版本支持 */
//	private int poolable=-1;
	
	/* 用于存储SQLExecutor监听器 */
	private List listeners = new ArrayList();
	/* 结果集生成器 */
	private ResultMaker resultMaker = null;
	
	/**
	 * 创建SQLExecutor的实例
	 * 
	 * @param transaction JDBC事务对象
	 * @param connId 数据库连接ID
	 */
	public SQLExecutor(JDBCTransaction transaction, String connId)  {
		this.transaction = transaction;
		this.connId = connId;
		
    /* 返回SQLExecutor的配置信息 */
    Element config = (Element)transaction.removeAttribute(connId);
    if(config!=null){
      Element maker = config.getChild("result-maker");
      if(maker!=null){
        String factoryClass = maker.getChildTextTrim("factory-class");
        Class factoryCls = ReflectHelper.forName(factoryClass);
        ResultMakerFactory mackerFactory = (ResultMakerFactory)ReflectHelper.newInstance(factoryCls);
        resultMaker = mackerFactory.create();
      }
      String timeout = config.getChildTextTrim("timeout");
      if(timeout!=null){
        setTimeout(Integer.parseInt(timeout));
      }
      String maxrows = config.getChildTextTrim("max-rows");
      if(maxrows!=null){
        setMaxrows(Integer.parseInt(maxrows));
      }
//      String poolable = config.getChildTextTrim("poolable");
//      if(poolable!=null){
//        setPoolable(new Boolean(poolable).booleanValue());
//      }
    }
	}
	
//  public boolean isPoolable() {
//    return poolable==1;
//  }
//
//  public void setPoolable(boolean flag) {
//    this.poolable = flag?1:0;
//  }

	
  public int getTimeout() {
    return timeout;
  }



  public void setTimeout(int seconds) {
    if(seconds<0){
      throw new JThinkRuntimeException(JThinkErrorCode.ERRCODE_SYS_RUNTIME, "不被支持的值，只允许大于等于0的值!");
    }
    this.timeout = seconds;
  }
  public int getMaxrows() {
    return maxrows;
  }

  public void setMaxrows(int maxrows) {
    if(maxrows<0){
      throw new JThinkRuntimeException(JThinkErrorCode.ERRCODE_SYS_RUNTIME, "不被支持的值，只允许大于等于0的值!");
    }
    this.maxrows = maxrows;
  }
	/**
	 * 返回一个可用连接
	 */
	protected Connection getConnection() {
		return transaction.getConnection(connId);
	}	
	
	/**
	 * 加入SQLExecutor监听器
	 * 
	 * @param listener SQLExecutor监听器
	 */
	public void addSQLExecutorListener(SQLExecutorListener listener){
		listeners.add(listener);
	}
	
	/**
	 * 返回所有SQLExecutor监听器
	 */
	public SQLExecutorListener[] getSQLExecutorListeners(){
		return (SQLExecutorListener[])listeners.toArray(new SQLExecutorListener[listeners.size()]);
	}
	
	/**
	 * 移除指定的SQLExecutor监听器
	 * 
	 * @param listener SQLExecutor监听器
	 */
	public void removeSQLExecutorListener(SQLExecutorListener listener){
		listeners.remove(listener);
	}
	
	/**
	 * 设置结果集生成器,ResultMaker的实现
	 * 
	 * @param resultMaker ResultMaker的具体实现
	 */
	public void setResultMaker(ResultMaker resultMaker){
		this.resultMaker = resultMaker;
	}
	
	/**
	 * 返回结果集生成器,如果不存在,创建一个默认的XML结果集生成器
	 * 
	 * @return 结果集生成器
	 */
	public ResultMaker getResultMaker(){
		if(resultMaker==null){
			resultMaker = new ElementResultMaker();
		}
		return resultMaker;
	}


	/**
	 * 释放Statement资源
	 */
	protected void releaseStatement(Statement statement)
			 {
		try {
			if (statement != null) {
			  statement.close();
			}
		} catch (SQLException ex) {
			throw new JThinkRuntimeException(JThinkErrorCode.ERRCODE_DB_CLOSE_PSTMT_FAILURE,
					ex);
		}
	}
  /**
   * 释放PreparedStatement资源
   */
  protected void releasePreparedStatement(PreparedStatement statement)
       {
    try {
      if (statement != null) {
        statement.close();
      }
    } catch (SQLException ex) {
      throw new JThinkRuntimeException(JThinkErrorCode.ERRCODE_DB_CLOSE_PSTMT_FAILURE,
          ex);
    }
  }

	/**
	 * 释放CallableStatement资源
	 */
	protected void releaseCallableStatement(CallableStatement cstmt)
			 {
		try {
			if (cstmt != null) {
				cstmt.close();
			}
		} catch (SQLException ex) {
			throw new JThinkRuntimeException(JThinkErrorCode.ERRCODE_DB_CLOSE_PSTMT_FAILURE,
					ex);
		}
	}
	
	
	/**
	 * 释放ResultSet资源
	 */
	protected void releaseResultSet(ResultSet resultset)  {
		try {
			if (resultset != null) {
				resultset.close();
			}
		} catch (SQLException ex) {
			throw new JThinkRuntimeException(JThinkErrorCode.ERRCODE_DB_CLOSE_RS_FAILURE, ex);
		}
	}

	/**
	 * 准备PreparedStatement
	 * @param sql SQL语句
	 */
	protected PreparedStatement prepareStatement(String sql) throws SQLException{
	  PreparedStatement statement = getConnection().prepareStatement(sql);
	  configStatement(statement);
	  return statement;
	}
  /**
   * 准备CallableStatement
   * @param sql SQL语句
   */
  protected CallableStatement prepareCall(String sql) throws SQLException{
    CallableStatement statement = getConnection().prepareCall(sql);
    configStatement(statement);
    return statement;    
  }	
	
  private void configStatement(Statement statement) throws SQLException{
    statement.setQueryTimeout(timeout);
    statement.setMaxRows(maxrows);
  }
  
	//*****************************STATEMENT AND EXECUTE****************************

  /**
   * 批量执行SQL, 注意:有的JDBC驱动对批量处理不支持事务，比如微软的MSSQL JDBC驱动！衰
   * @param sqls SQL语句数组
   * @return 更新计数所组成的数组
   */
  public int[] executeBatch(String[] sqls){
    int len = sqls.length;
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = getConnection().createStatement();
      configStatement(stmt);
      stmt.setEscapeProcessing(false);      
      for(int i=0;i<len;i++){
        String sql = sqls[i];
        executeCommand(sql, SQL.UNDEFINED);
        stmt.addBatch(sql);
      }
      return stmt.executeBatch();
      
    } catch (SQLException ex) {
      throw new JThinkRuntimeException(JThinkErrorCode.ERRCODE_DB_EXEC_SQL_EXCEPTION,
          ex);
    } finally {
      releaseResultSet(rs);
      releaseStatement(stmt);
    }
  }
  
	/**
	 * 执行SQL语句，如果此SQL语句不会产生结果集，返回null, 比如Insert, Update，Delete等更新操作的SQL。
	 * 如果是执行Select等查询操作的SQL，在Object中返回结果集, 结果集的数据结构由ResultMaker来决定
	 *
	 * @param sql    SQL语句串
	 * @param doClazz 数据对象类型，只有采用DataObjectResultMaker时此参数才有意义
	 * 
	 * @return 结果集或null
	 *
	 */
	public Object execute(String sql, Class doClazz)  {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			executeCommand(sql, SQL.UNDEFINED);
			pstmt = prepareStatement(sql);
			pstmt.execute();
			rs = pstmt.getResultSet();
			if (rs != null) {
//			  if(doClazz==null){
//			    return getResultMaker().create(rs);
//			  }else{
			    return getResultMaker().create(rs, doClazz);
//			  }
			} else {
				return null;
			}
		} catch (SQLException ex) {
			throw new JThinkRuntimeException(JThinkErrorCode.ERRCODE_DB_EXEC_SQL_EXCEPTION,
					"执行SQL失败!", ex);
		} finally {
			releaseResultSet(rs);
			releaseStatement(pstmt);
		}
	}
  /**
   * 执行SQL语句，如果此SQL语句不会产生结果集，返回null, 比如Insert, Update，Delete等更新操作的SQL。
   * 如果是执行Select等查询操作的SQL，在Object中返回结果集, 结果集的数据结构由ResultMaker来决定
   *
   * @param sql    SQL语句串
   * 
   * @return 结果集或null
   *
   */
  public Object execute(String sql)  {
    return execute(sql, (Class)null);
  }
	/**
	 * 执行SQL语句，如果此SQL语句不会产生结果集，返回null, 比如Insert, Update，Delete等更新操作的SQL。
	 * 如果是执行Select等查询操作的SQL，在Object中返回结果集, 结果集的数据结构由ResultMaker来决定
	 *
	 * @param sql    	SQL语句串
	 * @param values  SQL语句串中需要的值, values中的值将用于替换SQL语句串中的"?"
	 * @param doClazz 数据对象类型，只有采用DataObjectResultMaker时此参数才有意义
	 * 
	 * @return 结果集对象或null
	 *
	 */
	public Object execute(String sql, Object[] values, Class doClazz)  {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			executeCommand(sql, values, SQL.UNDEFINED);
			pstmt = prepareStatement(sql);
			if(values!=null){
				for (int i = 0; i < values.length; i++) {
					pstmt.setObject(i + 1, values[i]);
				}
			}
			pstmt.execute();
			rs = pstmt.getResultSet();
			if (rs != null) {
//			  if(doClazz==null){
//			    return getResultMaker().create(rs);
//			  }else{
			    return getResultMaker().create(rs, doClazz);
//			  }
			} else {
				return new Integer(pstmt.getUpdateCount());
			}
		} catch (SQLException ex) {
			throw new JThinkRuntimeException(JThinkErrorCode.ERRCODE_DB_EXEC_SQL_EXCEPTION,
					ex);
		} finally {
			releaseResultSet(rs);
			releaseStatement(pstmt);
		}
	}

  /**
   * 执行SQL语句，如果此SQL语句不会产生结果集，返回null, 比如Insert, Update，Delete等更新操作的SQL。
   * 如果是执行Select等查询操作的SQL，在Object中返回结果集, 结果集的数据结构由ResultMaker来决定
   *
   * @param sql     SQL语句串
   * @param values  SQL语句串中需要的值, values中的值将用于替换SQL语句串中的"?"
   * 
   * @return 结果集对象或null
   *
   */
  public Object execute(String sql, Object[] values)  {
    return execute(sql, values, (Class)null);
  }
	/**
	 * 执行更新操作的SQL，比如：Insert, Update等操作
	 *
	 * @param sql    SQL语句串
	 * 
	 * @return 被更新的数据行数量
	 *
	 */
	public int executeUpdate(String sql)  {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			executeCommand(sql, SQL.UPDATE);
			pstmt = prepareStatement(sql);
			return pstmt.executeUpdate();
		} catch (SQLException ex) {
			throw new JThinkRuntimeException(JThinkErrorCode.ERRCODE_DB_EXEC_SQL_EXCEPTION,
					"执行SQL失败!", ex);
		} finally {
			releaseResultSet(rs);
			releaseStatement(pstmt);
		}
	}

	/**
	 * 执行更新操作的SQL，比如：Insert, Update等操作
	 *
	 * @param sql      更新操作的SQL串
	 * @param values   更新的值数据,用于替换sql中的?号
	 *
	 * @return 被更新的数据行数量
	 */
	public int executeUpdate(String sql, Object[] values)  {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			executeCommand(sql, values, SQL.UPDATE);
			pstmt = prepareStatement(sql);
			if(values!=null){
				for (int i = 0; i < values.length; i++) {
					pstmt.setObject(i + 1, values[i]);
				}
			}
			return pstmt.executeUpdate();
		} catch (SQLException ex) {
			throw new JThinkRuntimeException(JThinkErrorCode.ERRCODE_DB_EXEC_SQL_EXCEPTION,
					ex);
		} finally {
			releaseResultSet(rs);
			releaseStatement(pstmt);
		}
	}

	/**
	 * 执行查询操作的SQL，比如：Select等操作
	 *
	 * @param sql   查询操作的SQL串
	 * @param values  值数据,用于替换sql中的?号
	 * @param doClazz 数据对象类型，只有采用DataObjectResultMaker时此参数才有意义
	 *   
	 * @return  结果集, 结果集的数据结构由ResultMaker来决定            
	 *
	 */
	public Object executeQuery(String sql, Object[] values, Class doClazz) {
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
//			if(doClazz==null){
//			  return getResultMaker().create(rs);
//			}else{
			  return getResultMaker().create(rs, doClazz);
//			}
		} catch (SQLException ex) {
			throw new JThinkRuntimeException(JThinkErrorCode.ERRCODE_DB_EXEC_SQL_EXCEPTION,
					ex);
		} finally {
			releaseResultSet(rs);
			releaseStatement(pstmt);
		}
	}
  /**
   * 执行查询操作的SQL，比如：Select等操作
   *
   * @param sql   查询操作的SQL串
   * @param values  值数据,用于替换sql中的?号
   *   
   * @return  结果集, 结果集的数据结构由ResultMaker来决定            
   *
   */
  public Object executeQuery(String sql, Object[] values) {
    return executeQuery(sql, values, (Class)null);
  }
	/**
	 * 执行查询操作的SQL
	 * 
	 * @param sql SQL语句串
	 * @param doClazz 数据对象类型，只有采用DataObjectResultMaker时此参数才有意义
	 * 
	 * @return 结果集, 结果集的数据结构由ResultMaker来决定
	 */
	public Object executeQuery(String sql, Class doClazz){
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			executeCommand(sql, SQL.SELECT);
			pstmt = prepareStatement(sql);
			rs = pstmt.executeQuery();
//			if(doClazz==null){
//			  return getResultMaker().create(rs);
//			}else{
			  return getResultMaker().create(rs, doClazz);
//			}
			
		} catch (SQLException ex) {
			throw new JThinkRuntimeException(JThinkErrorCode.ERRCODE_DB_EXEC_SQL_EXCEPTION,
					ex);
		} finally {
			releaseResultSet(rs);
			releaseStatement(pstmt);
		}
	}
  /**
   * 执行查询操作的SQL
   * 
   * @param sql SQL语句串
   * 
   * @return 结果集, 结果集的数据结构由ResultMaker来决定
   */
  public Object executeQuery(String sql){
    return executeQuery(sql, (Class)null);
  }

	/**
	 * 执行查询操作的SQL，比如：Select等操作, 用此查询方法可实现数据分页。
	 * 
	 * @param sql 查询操作的SQL语句
	 * @param startIndex 结果集的开始索引位置
	 * @param len 需要选择出的结果集的长度
	 * 
	 * @return 结果集, 结果集的数据结构由ResultMaker来决定
	 * 
	 * @see SQLExecutor#executeQuery(String, Object[], int, int)
	 */
	
	public Object executeQuery(String sql, int startIndex, int len)  {
		return executeQuery(sql, null, startIndex, len);
	}

	/**
	 * 执行查询操作的SQL，比如：Select等操作, 用此查询方法可实现数据分页。
	 * 此方法必须要在实际应用中根据具体数据库在此类的子类型中覆盖，以便实现分页功能
	 * 
	 * @param sql 查询操作的SQL语句
	 * @param values 值数组,用于替换SQL语句中的?, values可以为null对象
	 * @param startIndex 结果集的开始索引位置
	 * @param rowlen 需要选择出的结果集的长度
	 * 
	 * @return 结果集, 结果集的数据结构由ResultMaker来决定
	 */
	public Object executeQuery(String sql, Object[] values, int startIndex, int rowlen)  {
    return executeQuery(sql, values, startIndex, rowlen, null);
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
    throw new java.lang.UnsupportedOperationException(
        "Method executeQuery() not yet implemented.");
  }
	
	/**
	 * 执行存储过程
	 * 
	 * @param name 过程名称
	 * @param parameters 参数
	 * @param doClazz 数据对象类型，只有采用DataObjectResultMaker时此参数才有意义
	 * 
	 * @return 结果集或null
	 */
	public Object executeStoredProcedure(String name, Object[] parameters, Class doClazz){
		CallableStatement cstmt = null;
		ResultSet rs = null;
		try {
			String qsign = "";
			if(parameters!=null){
				int len = parameters.length;
				for(int i=0;i<len;i++){
					qsign += ",?";
				}
				if(len>0){
					qsign = qsign.substring(1);
				}
			}
			String sql = "{call "+name+" ("+qsign+")}";
			executeCommand(sql, SQL.UNDEFINED);
			cstmt = prepareCall(sql);
			if(parameters!=null){
				int len = parameters.length;
				for (int i = 0; i < len; i++) {
					cstmt.setObject(i + 1, parameters[i]);
				}
			}
			cstmt.execute();
			rs = cstmt.getResultSet();
			if (rs != null) {
//			  if(doClazz==null){
//			    return getResultMaker().create(rs);
//			  }else{
			    return getResultMaker().create(rs, doClazz);
//			  }
			} else {
				return new Integer(cstmt.getUpdateCount());
			}
		} catch (SQLException ex) {
			throw new JThinkRuntimeException(JThinkErrorCode.ERRCODE_DB_EXEC_SQL_EXCEPTION,
					ex);
		} finally {
			releaseResultSet(rs);
			releaseStatement(cstmt);
		}
	}
	
  /**
   * 执行存储过程
   * 
   * @param name 过程名称
   * @param parameters 参数
   * 
   * @return 结果集或null
   */
  public Object executeStoredProcedure(String name, Object[] parameters){
    return executeStoredProcedure(name, parameters, (Class)null);
  }
	
  /**
   * 执行SQL语句对象, sql是由SQLBuilder构建的SQL语句
   *
   * 
   * @param sql SQL对象
   * 
   * @return  结果集或null
   */
  public Object execute(SQL sql)  {
    return execute(sql, null);
  }

  /**
   * 执行SQL语句对象, sql是由SQLBuilder构建的SQL语句
   *
   * 
   * @param sql SQL对象
   * @param doClazz 数据对象类型，只有采用DataObjectResultMaker时此参数才有意义,
   *                并且只针对SQL.type为SQL.SELECT或SQL.SELECT_SP

   * @return  结果集或Integer类型的更新记录数量
   */
  public Object execute(SQL sql, Class doClazz)  {
    int startIndex;
    int length;
    switch(sql.getType()){
      case SQL.UPDATE:
        int rs = executeUpdate(sql.getSQLString(), sql.getValues());
        return new Integer(rs);
      case SQL.SELECT:
        startIndex = sql.getRowStartIndex();
        length = sql.getRowLength();
        if(startIndex>=0 && length>=0){
          return executeQuery(sql.getSQLString(), sql.getValues(), startIndex, length, doClazz);
        }else{
          return executeQuery(sql.getSQLString(), sql.getValues(), doClazz);
        }
      case SQL.UNDEFINED:
        return execute(sql.getSQLString(), sql.getValues(), doClazz);
      case SQL.UPDATE_SP:
        return executeStoredProcedure(sql.getSQLString(), sql.getValues());
      case SQL.SELECT_SP:
        startIndex = sql.getRowStartIndex();
        length = sql.getRowLength();
        if(startIndex>=0 && length>=0){
          //List values = Arrays.asList(sql.getValues());
          ObjectBuffered values = sql.getValueBuffered();
          values.append(new Integer(startIndex));
          values.append(new Integer(length));
          return executeStoredProcedure(sql.getSQLString(), values.toArray(), doClazz);
        }else{
          return executeStoredProcedure(sql.getSQLString(), sql.getValues(), doClazz);
        }
      case SQL.UNDEFINED_SP:
        return executeStoredProcedure(sql.getSQLString(), sql.getValues(), doClazz);
    }
    throw new JThinkRuntimeException(JThinkErrorCode.ERRCODE_DB_EXEC_SQL_EXCEPTION, "错误的SQL类型!");
  }
	
	
//  /**
//   * 执行更新操作的SQL，只针对Insert
//   *
//   * @param doClazz 数据对象类型
//   * 
//   * @return 被更新的数据行数量
//   *
//   */
//  public void insert(String tableName, DataObject dobj)  {
//    SQL sql = new SQLBuilder().constructSQLForInsert(tableName, dobj.getValues());
//    execute(sql);
//  }	
//  /**
//   * 执行更新操作的SQL，只针对Update
//   *
//   * @param doClazz 数据对象类型
//   * 
//   * @return 被更新的数据行数量
//   *
//   */
//  public int update(String tableName, DataObject dobj)  {
//
//  }
	
	  /**
	   * 执行SQLExecutor监听器中的事件方法，执行SQL时调用此方法。
	   * 
	   * @param sql 被执行的SQL语句串
	   * @param type SQL语句的类型
	   */
	  protected void executeCommand(String sql, int type){
	  	int len = listeners.size();
	  	for(int i=0;i<len;i++){
	  		((SQLExecutorListener)listeners.get(i)).executeSQLCommand(new SQLExecutorEvent(this, sql, type));
	    }
	  }
	  
	  /**
	   * 执行SQLExecutor监听器中的事件方法，执行SQL时调用此方法。
	   */
	  protected void executeCommand(String sql, Object[] values, int type){
	  	int len = listeners.size();
	  	for(int i=0;i<len;i++){
	  		((SQLExecutorListener)listeners.get(i)).executeSQLCommand(new SQLExecutorEvent(this, sql, values, type));
	    }
	  }
	  
	  /**
	   * 执行SQLExecutor监听器中的事件方法，执行SQL时调用此方法。
	   */
	  protected void executeCommand(SQL sql){
	  	int len = listeners.size();
	  	for(int i=0;i<len;i++){
	  		((SQLExecutorListener)listeners.get(i)).executeSQLCommand(new SQLExecutorEvent(this, sql));
	    }
	  }


	  
}