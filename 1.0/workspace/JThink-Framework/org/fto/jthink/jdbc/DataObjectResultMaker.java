/*
 * ElementResultMaker.java  2008-9-22
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

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import org.fto.jthink.exception.JThinkErrorCode;
import org.fto.jthink.exception.JThinkRuntimeException;
import org.fto.jthink.jdbc.ResultMaker;
import org.fto.jthink.util.ReflectHelper;
/**
 * 此类型的实例用于构建DataObject结果集，结果集记录类型为DataObject的子类型。
 * 
 * 
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2008-09-22  创建此类型
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 * @see DataObject
 */
public class DataObjectResultMaker implements ResultMaker {
  
  /**
   * 根据java.sql.ResultSet建立结果集值对象, ResultSet中的每一行将创建到一个DataObject对象，
   * 
   * @param rs java.sql.ResultSet结果集
   * 
   * @return 包含有DataObject结果集的java.util.List类型列表
   */
  public Object create(ResultSet rs){
    return create(rs, null);
  }


  /**
   * 根据java.sql.ResultSet建立结果集值对象, ResultSet中的每一行将创建到一个DataObject对象，
   * 
   * @param rs java.sql.ResultSet结果集
   * @param startIndex  开始索引, 在构建结果集时,将根据此索引位置开始选择结果集数据
   * @param len 选择的结果集长度
   * 
   * @return 包含有DataObject结果集的java.util.List类型列表
   */
  public Object create(ResultSet rs, int startIndex, int len){
    return create(rs, null, startIndex, len);
  }

  /**
   * 根据java.sql.ResultSet建立结果集值对象, ResultSet中的每一行将创建到一个DataObject对象，
   * 
   * @param rs java.sql.ResultSet结果集
   * @param doClazz 数据对象类型
   * 
   * @return 包含有DataObject结果集的java.util.List类型列表
   */
  public Object create(ResultSet rs, Class doClazz) {
    List rses=new ArrayList();
    try {
      if(doClazz==null){
        doClazz = DefaultDataObject.class;
      }
      while (rs.next()) {
        rses.add(createDataObject(rs, doClazz));
      }
    }
    catch (SQLException ex) {
      throw new JThinkRuntimeException(JThinkErrorCode.ERRCODE_DB_EXEC_SQL_EXCEPTION, ex);
    }
    return rses;
  }

  private DataObject createDataObject(ResultSet rs, Class doClazz) throws SQLException{
    ResultSetMetaData rsMD = rs.getMetaData();
    int cloumnCount = rsMD.getColumnCount();
    DataObject dataObj = (DataObject)ReflectHelper.newInstance(doClazz);
    dataObj.setTableName(rsMD.getTableName(1));
    for (int i = 0; i < cloumnCount; i++) {
      String attName = rsMD.getColumnName(i+1);
      if(dataObj.exist(attName)==false){
          String attValue = rs.getString(i+1);
          if(attValue!=null){
            dataObj.set(attName, attValue);
          }
      }
    }
    return dataObj;
  }
  
  /**
   * 根据java.sql.ResultSet建立结果集值对象, ResultSet中的每一行将创建到一个DataObject对象，
   * 
   * @param rs java.sql.ResultSet结果集
   * @param doClazz 数据对象类型
   * @param startIndex  开始索引, 在构建结果集时,将根据此索引位置开始选择结果集数据
   * @param len 选择的结果集长度
   * 
   * @return 包含有DataObject结果集的java.util.List类型列表
   */
  public Object create(ResultSet rs, Class doClazz, int startIndex, int len) {
    int currentRow = 0;
    int minRow = startIndex;
    List rses=new ArrayList(len);
    try {
      while (rs.next()) {
        if (currentRow < minRow) {
          currentRow++;
          continue;
        }
        if(doClazz==null){
          doClazz = DefaultDataObject.class;
        }
        while (rs.next()) {
          rses.add(createDataObject(rs, doClazz));
        }
      }
    }
    catch (SQLException ex) {
      throw new JThinkRuntimeException(JThinkErrorCode.ERRCODE_DB_EXEC_SQL_EXCEPTION, ex);
    }
    return rses;
  } 

}
