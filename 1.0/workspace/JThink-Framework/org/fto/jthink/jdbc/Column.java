
/*
 * Column.java	2005-7-30
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

import org.fto.jthink.lang.ObjectBuffered;
import org.fto.jthink.lang.StringBuffered;

/**
 * 列, 用于描述Select语句返回的数据列. 构建的列可以是标准的表字段,也可以是一个表达式或一个SQL子查询.
 * 
 * <pre>
 *  以下是可以构建的列的几种形式:
 *  表字段: fieldName1, fieldName2, fieldName3 AS tmpfieldName3
 *  表达式: (fieldName1*100-50) AS tmpfieldName1, (3+2-5) AS tmpfieldName2
 *  SQL了查询: (select table2.fieldName1 from table2 where table2.id = table1.id) AS tmpfieldName1
 * </pre>
 * 
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2005-07-30  创建此类型
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 */

public class Column  implements java.io.Serializable{

  private static final long serialVersionUID = 1L;
  
  String columnName=null;
	Object columnValue=null;
	
	/**
	 * 建立列, 列值可以是字段名称,也可以是一个表达式,但请不要直接写SQL语句串,这样会降低可移植性 
	 * 
	 * <pre>
	 * 以下是此构造方法构建列的几种方式:
	 * new Column(fieldName1, fieldName1), 构建出的列: fieldName1
	 * new Column(tmpfieldName1, fieldName1), 构建出的列: (fieldName1) AS tmpfieldName1
	 * new Column(tmpfieldName1, fieldName1*0.50), 构建出的列: (fieldName1*0.50) AS tmpfieldName1 
	 * new Column(tmpfieldName1,3+2-5), 构建出的列: (3+2-5) AS tmpfieldName1 
	 * new Column(fieldName1,(String)null), 构建出的列: tmpfieldName1 
	 * 
	 * </pre>
	 * 
	 * @param columnName  列名称
	 * @param columnValue 列值
	 */
	public Column(String columnName, String columnValue){
		if (columnName == null){
			throw new IllegalArgumentException(
					"The column name cannot be null.");
		}
		this.columnName = columnName;
		this.columnValue = columnValue;
	}

	/**
	 * 建立列,只给出列名称,构建出的列也只包含列名,
	 * 比如: new Column(fieldName1), 构建出的列是:fieldName1
	 * 
	 * @param columnName 列名称
	 */
	public Column(String columnName){
		if (columnName == null){
			throw new IllegalArgumentException(
					"The column name cannot be null.");
		}
		this.columnName = columnName;
		this.columnValue = columnName;
	}
	
	/**
	 * 建立列,列值为一个SQL语句.此构造器将构建一个包含子查询的列
	 * 
	 * <pre>
	 *  Condition userNameCDN = new Condition();
	 *  userNameCDN.setItem(new ConditionItem("Users.UserId", "=", "Departments.UserId", true));
	 *  SQL userNameSQL = sqlBuilder.constructSQLForSelect("Users", new Column[]{new Column("UserName")}, userNameCDN);
	 *  
	 *  new Column("UserName", userNameSQL), 构建出的列:(SELECT UserName FROM Users WHERE  Users.UserId = Departments.UserId ) AS UserName
	 * 
	 * </pre>
	 * 
	 * @param columnName 列名称
	 * @param sql SQL类型的实例
	 */
	public Column(String columnName, SQL sql){
		if (columnName == null){
			throw new IllegalArgumentException(
					"The column name cannot be null.");
		}
		this.columnName = columnName;
		this.columnValue = sql;
	}

	/**
	 * 返回列名称
	 * 
	 * @return 列名称
	 */
	public String getColumnName(){
		return columnName;
	}
	
	/**
	 * 返回列值
	 * 
	 * @return 列值
	 */
	public Object getColumnValue(){
		return columnValue;
	}

  
  /**
   * 检查是否是简单列, 即：columnValue==null || columnName==columnValue的列
   */
  public boolean isSimpleColumn(){
    return columnValue==null || columnName==columnValue;
  }
  
	/**
	 * 返回列的SQL语句形式
	 * 
	 * @return 描述此列的SQL语句
	 * 
	 */
	public SQL getColumn(){
    StringBuffered column=null;
    ObjectBuffered values=null;
		if(columnValue instanceof java.lang.String){
      if(columnName==columnValue){
        return new SQL(SQL.UNDEFINED, columnName, null);
			}else{
				column = new StringBuffered(4).append("(").append(columnValue).append(") AS ").append(columnName);
			}
		}else{
			SQL sql = (SQL)columnValue;
      StringBuffered sqlStatement = sql.getSQLStatement();
			column = new StringBuffered(3+sqlStatement.size()).append("(").append(sqlStatement).append(") AS ").append(columnName);
			values = sql.getValueBuffered();
		}
		return new SQL(SQL.UNDEFINED, column, values);
	}
}
