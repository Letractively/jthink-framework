
/*
 * MysqlSQLBuilder.java	2005-7-15
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
package org.fto.jthink.jdbc.mysql;


import java.util.ArrayList;
import java.util.List;

import org.fto.jthink.exception.JThinkRuntimeException;
import org.fto.jthink.jdbc.Column;
import org.fto.jthink.jdbc.Condition;
import org.fto.jthink.jdbc.SQL;
import org.fto.jthink.jdbc.SQLBuilder;
/**
 * 构建SQL声明, 在具体应用中扩展类, 以便更灵活的构建SQL, 
 * 特别是需要用constructSQL(String type, HashMap sqlParameters)方法来
 * 构建自定义的SQL的时候.
 *
 */
import org.fto.jthink.lang.StringBuffered;

/**
 * 构建SQL，针对Mysql数据库。 此类型扩展了SQLBuilder类型，并覆盖了constructSQLForSelect()方法，
 * 以便构建具有分页功能的SQL语句。
 * 
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2005-07-15  创建此类型
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 */
public class MysqlSQLBuilder extends SQLBuilder{


  /**
   * 构建查询记录操作的SQL。此方法可以实例数据分页
   *
   * @param tableName          表名称，确定对那个表进行操作
   * @param distinct           在SELECT中是否加上DISTINCT子句
   * @param columns            列名，指定须要返回的数据字段列
   * @param condition          条件
   * @param groupby            分组字段
   * @param orderby            排序字段
   * @param startIndex         开始行记录索引, 此值不能小于0
   * @param rowLen             返回记录的行数, 此值不能小于0
   * 
   * @return 	包含SQL串和值的SQL语句对象
   */
  public SQL constructSQLForSelect(String tableName,
                                              boolean distinct,
                                              Column[] columns,
                                              Condition condition,
                                              String groupby, String orderby,
																							int startIndex, int rowLen
                                              ){
    if(startIndex<0){
    	throw new JThinkRuntimeException("startIndex不能小于0!");
    }
    if(rowLen<0){
    	throw new JThinkRuntimeException("rowLen不能小于0!");
    }

    StringBuffered sqlStr = new StringBuffered("SELECT ");
    List values = new ArrayList();
    
    /* 生成DISTINCT串 */
    if (distinct) {
      sqlStr.append(" DISTINCT ");
    }
    
    /* 生成返回列的串 */
    if (columns != null && columns.length != 0) {
      SQL columnSQL = constructSelectedColumn(columns);
      sqlStr.append(columnSQL.getSQLStatement());
      Object[] objs = columnSQL.getValues();
      int len=objs.length;
      for(int i=0;i<len;i++){
        values.add(objs[i]);
      }
    }

    /* 生成FROM子串, 如果tableName为空,将不构建FROM子句 */
    if (tableName != null && tableName.length() != 0) {
      sqlStr.append(" FROM ").append(tableName);
    }
    
    /* 生成查询条件串 */
    if (condition != null && condition.size() != 0) {
      sqlStr.append(" WHERE ").append(condition.getConditionStatement());
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
    
    /* 生成limit串 */
    if (rowLen != -1) {
      sqlStr.append(" LIMIT ").append(startIndex).append(",").append(rowLen);
    }
    
    return new SQL(SQL.SELECT, sqlStr, values.toArray(), startIndex, rowLen);
    
  } 
 
  
  
}
