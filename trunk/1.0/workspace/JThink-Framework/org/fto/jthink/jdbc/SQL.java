
/*
 * SQL.java	2005-7-15
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
 * 用于描述一个SQL语句。此SQL语句对象包含有SQL的相关信息，SQL语句类型，SQL语句串，SQL语句中的值，
 * 以及用于SELECT语句的分页信息。SQL类型有如下值：UNDEFINED,UPDATE,SELECT,UNDEFINED_SP,UPDATE_SP,SELECT_SP。
 * 
 * 
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2005-07-15  创建此类型
 * 2009-09-08  对于SQL语句增加StringBuffered支持
 * 
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 */
public class SQL implements java.io.Serializable{
  private static final long serialVersionUID = 6626942489436364862L;
  private int type;
	private StringBuffered sqlBuff;
  private String sql;
	//private Object[] values;
  private ObjectBuffered values;
	
	private int rowStartIndex = -1;  //结果集的开始行索引,从0开始
	private int rowLength = -1;			//结果集的行数, 从索引位置(rowStartIndex)开始的行
	
	/**
	 * 创建一个SQL对象
	 * 
	 * @param type SQL语句类型
	 * @param sql SQL语句串, SQL串中的值可用"?"代替
	 * @param values 与SQL串中"?"对应的值对象数组
   * 
	 */
	public SQL(int type, String sql, Object[] values){
		if(type<1 ||type>6){
			throw new IllegalArgumentException(
					"类型必须是在此类中定义的常量.");
		}
		this.type = type;
		this.sql = sql;
		//this.values = values==null?new Object[0]:values;
    if(values!=null){
      this.values = new ObjectBuffered(1).append(values);
    }
	}
	
  /**
   * 创建一个SQL对象
   * 
   * @param type SQL语句类型
   * @param sql SQL语句串, SQL串中的值可用"?"代替
   * @param values 与SQL串中"?"对应的值对象数组
   */
  public SQL(int type, StringBuffered sqlBuff, ObjectBuffered values){
    if(type<1 ||type>6){
      throw new IllegalArgumentException(
          "类型必须是在此类中定义的常量.");
    }
    this.type = type;
    this.sqlBuff = sqlBuff;
    //this.values = values==null?new Object[0]:values;
    this.values = values;
  }
  
  
  
	/**
	 * 创建一个SQL对象, 此构造方法主要用来创建带有分页信息的SQL语句
	 * 
	 * @param type SQL语句类型
	 * @param sql SQL语句串
	 * @param values SQL语句中的值数组
	 * @param rowStartIndex 结果集的开始索引位置
	 * @param rowLength 结果集的长度
   * 
	 */
	public SQL(int type, String sql, Object[] values, int rowStartIndex, int rowLength){
		if(type<1 ||type>6){
			throw new IllegalArgumentException(
					"类型必须是在此类中定义的常量.");
		}
		this.type = type;
		this.sql = sql;
		//this.values = values==null?new Object[0]:values;
    if(values!=null){
      this.values = new ObjectBuffered(1).append(values);
      //this.values.add(values);
    }
		this.rowStartIndex = rowStartIndex;
		this.rowLength = rowLength;
	}

  /**
   * 创建一个SQL对象, 此构造方法主要用来创建带有分页信息的SQL语句
   * 
   * @param type SQL语句类型
   * @param sql SQL语句串
   * @param values SQL语句中的值数组
   * @param rowStartIndex 结果集的开始索引位置
   * @param rowLength 结果集的长度
   */
  public SQL(int type, StringBuffered sqlBuff, ObjectBuffered values, int rowStartIndex, int rowLength){
    if(type<1 ||type>6){
      throw new IllegalArgumentException(
          "类型必须是在此类中定义的常量.");
    }
    this.type = type;
    this.sqlBuff = sqlBuff;
    //this.values = values==null?new Object[0]:values;
    this.values = values;
    this.rowStartIndex = rowStartIndex;
    this.rowLength = rowLength;
  }
  
  
	/**
	 * 返回SQL语句的类型
	 */
	public int getType(){
		return type;
	}
	
	/**
	 * 返回SQL语句串
	 */
	public String getSQLString(){
		return sql!=null?sql:sqlBuff.toString();
	}
  
  /**
   * 返回SQL语句串
   * 
   */
  public StringBuffered getSQLStatement(){
    return sqlBuff!=null?sqlBuff:new StringBuffered(sql);
  }
	
  /**
   * 检查SQL语句是否是StringBuffered类型
   */
  public boolean isStringBufferedType(){
    return sqlBuff!=null;
  }
  
	/**
	 * 返回在SQL语句串中的值数组, 如果没有，返回空数组
   * 
	 */
	public Object[] getValues(){
    if(values!=null){
      return values.toArray();
    }else{
      return new Object[0];
    }
	}
	
  
  /**
   * 返回ObjectBuffered类型的值缓冲, 如果没有，返回null
   */
  public ObjectBuffered getValueBuffered(){
    return values;
  }
  
	/**
	 * 返回结果集的开始索引值
	 */
	public int getRowStartIndex(){
		return rowStartIndex;
	}
	
	
	/**
	 * 返回结果集长度
	 */
	public  int getRowLength(){
		return rowLength;
	}
	
	
  /**
   * 常量，SQL类型，不明确的操作
   */
  public static final int UNDEFINED = 1;	
  /**
   * 常量，SQL类型，更新操作(Insert, Update)
   */
  public static final int UPDATE = 2;
  /**
   * 常量，SQL类型，查询操作(Select)
   */
  public static final int SELECT = 3;

  /**
   * 常量，SQL类型，不明确的存储过程调用
   */
  public static final int UNDEFINED_SP = 4;
  /**
   * 常量，SQL类型，更新操作的存储过程调用
   */
  public static final int UPDATE_SP = 5;
  /**
   * 常量，SQL类型，查询操作的存储过程调用
   */
  public static final int SELECT_SP = 6;

}
