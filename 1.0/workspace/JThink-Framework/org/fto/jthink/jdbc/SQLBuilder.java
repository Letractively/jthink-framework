
/*
 * SQLBuilder.java	2005-7-15
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.fto.jthink.exception.JThinkRuntimeException;
import org.fto.jthink.lang.StringBuffered;

/**
 * 构建SQL。 此类型已经提供了构建常用SQL语句的方法，但如果要构建复杂SQL，
 * 建议扩展此类型，然后覆盖在此类型中没有实现的方法：constructSQL()和constructSQLForSelect()。
 * 如果扩展了此类型，相应的也需要实现SQLBuilderFactory工厂来创建SQLBuilder类型的实例。
 * 
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2005-07-15  创建此类型
 * 2009-07-29  优化SQL构建
 * </pre></p>
 * 
 * </pre>
 *   想法：
 *       
 * </pre>
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 */
public class SQLBuilder {

	/**
	 * 根据sqlParameter中描述的信息构建SQL声明, 此方法需要在子类中实现。
	 * 可以用此方法来构建与数据库特性相关的SQL语句，充分利用了数据库的特性，
	 * 且又能非常方便的在数据库之间移植应用
	 * 
	 * @param type           类别, 根据不同的类别构建SQL
	 * @param sqlParameters   构建SQL需要的数据信息
	 * 
	 * @return  包含SQL串和值的SQL声明
	 */
	public SQL constructSQL(String type, Map sqlParameters){
		throw new java.lang.UnsupportedOperationException(
								"Method constructSQL() not yet implemented.");

	}
	
	
	/**
	 * 构建Insert操作的SQL声明
	 * 
	 * @param tableName  表名
	 * @param columns    将要创建的记录的列和列值
	 * 
	 * @return 包含SQL串和值的SQL声明
	 */
  public SQL constructSQLForInsert(String tableName, Map columns){
    
    if(tableName==null){
      throw new IllegalArgumentException(
          "The name of an table cannot be null.");
    }
    if(columns.size()==0){
      throw new IllegalArgumentException(
          "The value of an columns cannot be empty.");
    }
    int size = columns.size();
    StringBuffered names = new StringBuffered(size*2);
    StringBuffered valueStatement = new StringBuffered(size);
    List values  = new ArrayList(size);
    Iterator columnsIT = columns.entrySet().iterator();
    while(columnsIT.hasNext()){
      Map.Entry column = (Map.Entry)columnsIT.next();
      Object value = column.getValue();
      if(value!=null){
        if(values.size()==0){
          names.append(column.getKey());
          valueStatement.append("?");
        }else{
          names.append(",").append(column.getKey());
          valueStatement.append(",?");
        }
        values.add(value);
      };      
    }
    StringBuffered sql = new StringBuffered(5+names.size()+valueStatement.size())
      .append("INSERT INTO ") 
      .append(tableName)
      .append(" (").append(names)
      .append(") VALUES (").append(valueStatement)
      .append(") ");
    return new SQL(SQL.UPDATE, sql, values.toArray());
  } 	
	
  /**
   * 构建Insert操作的SQL声明
   * 
   * @param data 数据对象，只有采用DataObjectResultMaker时此参数才有意义
   * 
   * @return 包含SQL串和值的SQL声明
   */
  public SQL constructSQLForInsert(DataObject data){
    return constructSQLForInsert(data.getTableName(), data.getValues());
  }
	
	/**
	 * 构建更新记录操作的SQL声明
	 * 
	 * @param tableName  表名
	 * @param columns    包含有列和列值的Map
	 * @param condition  条件
	 * 
	 * @return 包含SQL串和值的SQL声明
	 */
  public SQL constructSQLForUpdate(String tableName, Map columns, Condition condition){
    if(tableName==null){
      throw new IllegalArgumentException(
          "The name of an table cannot be null.");
    }
    if(columns.size()==0){
      throw new IllegalArgumentException(
          "The value of an columns cannot be empty.");
    }
    int columnsCapacity = columns.size();
    int conditionSize = 0;
    StringBuffered conditionStatement = null;
    int conditionStatementSize = 0;
    if(condition!=null){
      conditionSize = condition.size();
      conditionStatement = condition.getConditionStatement();
      conditionStatementSize = conditionStatement.size();
    }
    List values  = new ArrayList(columnsCapacity+conditionSize);
    StringBuffered sql = new StringBuffered(columnsCapacity+columnsCapacity+conditionStatementSize+4)
    .append("UPDATE ")
    .append(tableName)
    .append(" SET ");
    
    /* 处理被更新的字段 */
    Iterator columnsIT = columns.entrySet().iterator();
    while(columnsIT.hasNext()){
      Map.Entry column = (Map.Entry)columnsIT.next();
      Object value = column.getValue();
      int valuesSize = values.size();
      if(value==null){
        sql.append(valuesSize>0?",":"").append(column.getKey()).append("=NULL");
      }else{
        sql.append(valuesSize>0?",":"").append(column.getKey()).append("=?");
        values.add(value);
      }
    }
    /* 处理条件 */
    if(conditionStatement!=null){
      sql.append(" WHERE ").append(conditionStatement);
      Object[] objs = condition.getValues();
      int len=objs.length;
      for(int i=0;i<len;i++){
        values.add(objs[i]);
      }
    }
    
    return new SQL(SQL.UPDATE, sql, values.toArray());
  }  

  /**
   * 构建更新记录操作的SQL声明
   * 
   * @param data 数据对象，只有采用DataObjectResultMaker时此参数才有意义
   * 
   * @return 包含SQL串和值的SQL声明
   */
  public SQL constructSQLForUpdate(DataObject data){
    Condition condition = new Condition();
    Iterator pks = data.getPrimaryKeys().entrySet().iterator();
    while(pks.hasNext()){
      Map.Entry pk = (Map.Entry)pks.next();
      Object value = pk.getValue();
      if(value==null){
        throw new IllegalArgumentException(
          "The primary key value cannot be null.");
      }
      condition.add(new ConditionItem(pk.getKey().toString(), "=", (Object)value));
    }
      
    if(condition.size()==0){
      throw new JThinkRuntimeException("The condition size cannot be 0");
    }
    return constructSQLForUpdate(data.getTableName(), data.getValues(), condition);
  }
  
  /**
   * 构建删除记录操作的SQL声明
   * 
   * @param tableName 表名
   * @param condition 条件
   * 
   * @return 包含SQL串和值的SQL声明
   */
  public SQL constructSQLForDelete(String tableName, Condition condition){
		if(tableName==null){
			throw new IllegalArgumentException(
					"The name of an table cannot be null.");
		}
    StringBuffered conditionStatement = null;
    int conditionStatementSize = 0;
    if (condition != null && condition.size()>0) {
      conditionStatement = condition.getConditionStatement();
      conditionStatementSize = conditionStatement.size();
    }
    
		StringBuffered sqlstr = new StringBuffered(3+conditionStatementSize)
      .append("DELETE FROM ")
      .append(tableName);
		Object[] values = null;
		if (conditionStatement != null) {
			sqlstr.append(" WHERE ").append(conditionStatement);
			values = condition.getValues();
		}else{
			values = new Object[0];
		}
		return new SQL(SQL.UPDATE, sqlstr, values);
	}
  
  /**
   * 构建删除记录操作的SQL声明
   * 
   * @param data 数据对象，只有采用DataObjectResultMaker时此参数才有意义
   * 
   * @return 包含SQL串和值的SQL声明
   */
  public SQL constructSQLForDelete(DataObject data){
    Condition condition = new Condition();
    Iterator pks = data.getPrimaryKeys().entrySet().iterator();
    while(pks.hasNext()){
      Map.Entry pk = (Map.Entry)pks.next();
      Object value = pk.getValue();
      if(value==null){
        throw new IllegalArgumentException(
          "The primary key value cannot be null.");
      }
      condition.add(new ConditionItem(pk.getKey().toString(), "=", (Object)value));
    }
      
    if(condition.size()==0){
      throw new JThinkRuntimeException("The condition size cannot be 0");
    }
    
    return constructSQLForDelete(data.getTableName(), condition);
  }
  
  /**
   * 构建查询记录操作的SQL。
   *
   * @param tableName          表名称，确定对那个表进行操作
   * @param distinct           在SELECT中是否加上DISTINCT子句
   * @param columns            列名，指定须要返回的数据字段列
   * @param condition          条件
   * @param groupby            分组字段
   * @param orderby            排序字段 
   *
   * @return 包含SQL串和值的SQL声明
   */

  public SQL constructSQLForSelect(String tableName,
                                              boolean distinct,
                                              Column[] columns,
                                              Condition condition,
                                              String groupby, String orderby
                                              ){
    if(tableName==null){
      throw new IllegalArgumentException(
          "The name of an table cannot be null.");
    }
    SQL columnSQL = null;
    StringBuffered columnSQLStatement = null;
    int columnSQLStatementSize = 0;
    if (columns != null && columns.length != 0) {
      columnSQL = constructSelectedColumn(columns);
      columnSQLStatement = columnSQL.getSQLStatement();
      columnSQLStatementSize = columnSQLStatement.size();
    }
    StringBuffered conditionStatement = null;
    int conditionStatementSize = 0;
    if (condition != null && condition.size() != 0) {
      conditionStatement = condition.getConditionStatement();
      conditionStatementSize = conditionStatement.size();
    }
    
    StringBuffered sqlStr = new StringBuffered(columnSQLStatementSize+conditionStatementSize+10)
    .append("SELECT ");
    List values = new ArrayList();

    /* 生成DISTINCT串 */
    if (distinct) {
      sqlStr.append(" DISTINCT ");
    }
    /* 生成返回列的串 */
    if (columnSQL!=null) {
      sqlStr.append(columnSQLStatement);
      Object[] objs = columnSQL.getValues();
      int len=objs.length;
      for(int i=0;i<len;i++){
        values.add(objs[i]);
      }
    }else{
      sqlStr.append("*");
    }
    
    /* 生成FROM子串, 如果tableName为空,将不构建FROM子句 */
    if (tableName != null && tableName.length() != 0) {
      sqlStr.append(" FROM ").append(tableName);
    }
    
    /* 生成查询条件串 */
    if (conditionStatement != null) {
      sqlStr.append(" WHERE ")
            .append(conditionStatement);
      Object[] objs = condition.getValues();
      int len=objs.length;
      for(int i=0;i<len;i++){
        values.add(objs[i]);
      }      
    }
    /* 生成GROUP BY串 */
    if (groupby != null && groupby.length() != 0) {
      sqlStr.append(" GROUP BY ").append(groupby);
    }
    /* 生成ORDER BY串 */
    if (orderby != null && orderby.length() != 0) {
      sqlStr.append(" ORDER BY ").append(orderby);
    }
    return new SQL(SQL.SELECT, sqlStr, values.toArray());
  }

  
  /**
   * 构建查询记录操作的SQL。
   *
   * @param tableName          表名称，确定对那个表进行操作
   * @param columns           列名，指定须要返回的数据字段列
   * @param condition          条件
   *
   * @return        包含SQL串和值的SQL声明
   *
   */  
  public SQL constructSQLForSelect(String tableName,
                                            Column[] columns,
                                            Condition condition){
    return constructSQLForSelect(tableName, false, columns, condition, null, null);
  }

  
  /**
   * 构建查询记录操作的SQL。此方法需要在子类中实现
   *
   * @param tableName          表名称，确定对那个表进行操作
   * @param distinct           在SELECT中是否加上DISTINCT子句
   * @param columns            列名，指定须要返回的数据字段列
   * @param condition          条件
   * @param groupby            分组字段 
   * @param orderby            排序字段
   * @param startIndex         开始行记录索引, 此值不能小于0
   * @param len                返回记录的行数, 此值不能小于0
   * 
   * @return   包含SQL串和值的SQL声明
   */
  public SQL constructSQLForSelect(String tableName,
                                              boolean distinct,
                                              Column[] columns,
                                              Condition condition,
                                              String groupby, String orderby,
                                              int startIndex, int len
                                              ){
    throw new java.lang.UnsupportedOperationException(
    "Method constructSQLForSelect() not yet implemented.");
  } 
  
  
  
  /**
   * 构建统计记录数量的SQL。
   *
   * @param tableName      表名称，确定对那个表进行操作
   * @param fieldName      字段列名，可以是“*”字符, 也可以是列名
   * @param attrName       属性名，通过此属性名称得到统计数据值
   * @param condition      条件
   *
   * @return               包含SQL串和值的SQL声明
   */
  public SQL constructSQLForCount(
      String tableName, String fieldName, String attrName,Condition condition) {
    fieldName = fieldName==null?"*":fieldName;

    StringBuffered conditionStatement = null;
    int conditionStatementSize = 0;
    if (condition != null && condition.size() != 0) {
      conditionStatement = condition.getConditionStatement();
      conditionStatementSize = conditionStatement.size();
    }    
    
    StringBuffered sqlstr = new StringBuffered(conditionStatementSize+7)
      .append("SELECT COUNT(").append(fieldName).append(") AS ").append(attrName).append(" FROM ").append(tableName);
    Object[] values = null;
    if (conditionStatement != null) {
      sqlstr.append(" WHERE ").append(conditionStatement);
      values = condition.getValues();
    }else{
      values = new Object[0]; 
    }
    return new SQL(SQL.SELECT, sqlstr, values);
  }

  
  /**
   * 构建被选择的列
   * 
   * @param columns 列对象数组  
   * 
   * @return  描述列的SQL子语句
   */
  protected SQL constructSelectedColumn(Column[] columns){
    int len = columns.length;
    StringBuffered columnsStr = new StringBuffered();
    List valuesLT = new ArrayList();
    for(int i=0;i<len;i++){
      SQL columnSQL = columns[i].getColumn();
      columnsStr.append(i==0?"":",");
      if(columnSQL.isStringBufferedType()){
        columnsStr.append(columnSQL.getSQLStatement());
      }else{
        columnsStr.append(columnSQL.getSQLString());
      }
      
      Object[] values = columnSQL.getValues();
      if(values!=null){
        int vlen = values.length;
        for(int vi=0;vi<vlen;vi++){
          valuesLT.add(values[vi]);
        }
      }
    }
    return new SQL(SQL.UNDEFINED, columnsStr, valuesLT.toArray());
  }

}
