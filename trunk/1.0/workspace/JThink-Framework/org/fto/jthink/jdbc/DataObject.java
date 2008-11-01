
/*
 * Condition.java 2008-9-22
 *
 * 版权所有: 自由思考组织(FTO)软件团队 2000-2005, 保留所有权利.
 * 这个软件是自由思考组织(FTO)软件团队开发的，如果要使用这个软件，请首先阅读并接受许可协议。
 *
 * Copyright 2000-2005 FTO Software Team, Inc. All Rights Reserved.
 * This software is the proprietary information of FTO Software Team, Inc.
 * Use is subject to license terms.
 *
 */
package org.fto.jthink.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fto.jthink.util.ReflectHelper;

/**
 * 数据对象，用于封装访问数据库返回的结果集。此为抽象类型，针对不同的数据库表要单独扩展，否则：
 * 将使用默认数据对象DefaultDataObject。注意：所有字段名称都将被转换为大写形式。
 * 
 * 
 * 
 * 
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2005-09-22  创建此类型
 * </pre></p>
 * 
 * 
 * @author   try.jiwen
 * @version  1.0
 * @since    JThink 1.0
 */

public abstract class DataObject implements java.io.Serializable {
  
  /** 
   * 结果数据信息, 在子类中可以访问此结果数据
   */
  protected HashMap values = new HashMap();
  
  /** 主键名列表 */
  protected ArrayList primaryKeyNames=new ArrayList();
  
  /** 表名称 */
  protected String tableName=null;
  
  /**
   * 返回表名称
   */
  public String getTableName(){
    return tableName;
  }
  
  /**
   * 设置表名称
   * @param tableName 表名称
   */
  public void setTableName(String tableName){
    this.tableName = tableName;
  }
  
  /**
   * 返回主键名列表
   */
  public List getPrimaryKeyNames(){
    return primaryKeyNames;
  }
  
  /**
   * 设置主键
   * @param fieldName 字段名，将此字段名设置为主键
   */
  public void setPrimaryKey(String fieldName){
    if(fieldName==null || fieldName.trim().length()==0){
      throw new IllegalArgumentException(
      "The field name cannot be null.");
    }
    primaryKeyNames.add(fieldName);
  }
  
  /**
   * 返回主键,包括了值
   */
  public Map getPrimaryKeys(){
    Map pks = new HashMap();
    for(int i=primaryKeyNames.size()-1;i>=0;i--){
      String pkName = (String)primaryKeyNames.get(i);
      pks.put(pkName, values.get(pkName));
    }
    return pks;
  }
  
  /**
   * 根据字段名返回值
   * @param fieldName 字段名称
   */
  public String get(String fieldName) {
    return (String)values.get(fieldName.toUpperCase());
  }
  /**
   * 设置字段值
   * @param fieldName 字段名称
   * @param value 字段值
   */
  public void set(String fieldName, String value) {
    values.put(fieldName.toUpperCase(), value);
  }
  /**
   * 以Map结构返回字段和值
   */
  public Map getValues(){
    return values;
  }
  /**
   * 判断字段是否已经存在
   * @param fieldName 字段名称
   */
  public boolean exist(String fieldName) {
    return values.containsKey(fieldName.toUpperCase());
  }
  
  /**
   * 将DataObject类型的通用数据对象转换为具体的数据对象
   * 
   * @param doClazz 具体数据对象类型，必须是DataObject扩展类型
   */
  public DataObject toDataObject(Class doClazz){
    DataObject data = (DataObject)ReflectHelper.newInstance(doClazz);
    data.tableName = this.tableName;
    data.primaryKeyNames = (ArrayList)this.primaryKeyNames.clone();
    data.values = (HashMap)this.values.clone();
    return data;
  }
}
