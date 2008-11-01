/*
 * MapResultMaker.java  2008-8-19
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
package org.fto.jthink.jdbc;

  import java.sql.ResultSet;
  import java.sql.ResultSetMetaData;
  import java.sql.SQLException;
  import java.util.ArrayList;
  import java.util.HashMap;
  import java.util.List;
  import java.util.Map;
  import org.fto.jthink.exception.JThinkErrorCode;
  import org.fto.jthink.exception.JThinkRuntimeException;
import org.fto.jthink.jdbc.ResultMaker;

  /**
   * JThink默认返回结果集为jdom XML格式的Element对象，
   * 此类型用于将JThink的SQLExecutor执行后的JDBC结果集转换为Map格式.
   * 
   * @author try.jiwen
   */
  public class MapResultMaker implements ResultMaker{

    /**
     * 根据java.sql.ResultSet建立结果集值对象, ResultSet中的每一行将创建到一个Map对象，
       * Map的Key为字段名，Value为值
     * 
     * @param rs java.sql.ResultSet结果集
     * 
     * @return 包含有Map结果集的java.util.List类型列表
     */
    public Object create(ResultSet rs, Class doClazz){
        List rses=new ArrayList();
        try {
          while (rs.next()) {
            ResultSetMetaData rsMD = rs.getMetaData();
            int cloumnCount = rsMD.getColumnCount();
            Map fieldMap = new HashMap();
            for (int i = 0; i < cloumnCount; i++) {
              String attName = rsMD.getColumnName(i+1);
              if(fieldMap.containsKey(attName)==false){
                  String attValue = rs.getString(i+1);
                  if(attValue!=null){
                    fieldMap.put(attName, attValue);
                  }
              }
            }
            rses.add(fieldMap);
          }
        }
        catch (SQLException ex) {
          throw new JThinkRuntimeException(JThinkErrorCode.ERRCODE_DB_EXEC_SQL_EXCEPTION, ex);
        }
        return rses;
      }

    /**
     * 根据java.sql.ResultSet建立结果集值对象, ResultSet中的每一行将创建到一个Map对象，
       * Map的Key为字段名，Value为值
     * 
     * @param rs java.sql.ResultSet结果集
     * @param startIndex  开始索引, 在构建结果集时,将根据此索引位置开始选择结果集数据
     * @param len 选择的结果集长度
     * 
     * @return 包含有Map结果集的java.util.List类型列表
     */
    public Object create(ResultSet rs, Class doClazz, int startIndex, int len){
      int currentRow = 0;
      int minRow = startIndex;
      List rses=new ArrayList(len);
      try {
        while (rs.next()) {
          if (currentRow < minRow) {
            currentRow++;
            continue;
          }
          ResultSetMetaData rsMD = rs.getMetaData();
          int cloumnCount = rsMD.getColumnCount();
          Map fieldMap = new HashMap();
          for (int i = 0; i < cloumnCount; i++) {
            String attName = rsMD.getColumnName(i+1);
            if(fieldMap.containsKey(attName)==false){
              String attValue = rs.getString(i+1);
              if(attValue!=null){
                fieldMap.put(attName, attValue);
              }
            }
          }
          rses.add(fieldMap);
        }
      }
      catch (SQLException ex) {
        throw new JThinkRuntimeException(JThinkErrorCode.ERRCODE_DB_EXEC_SQL_EXCEPTION, ex);
      }
      return rses;
    }
    
    /**
     * 根据java.sql.ResultSet建立结果集值对象, ResultSet中的每一行将创建到一个Map对象，
       * Map的Key为字段名，Value为值
     * 
     * @param rs java.sql.ResultSet结果集
     * 
     * @return 包含有Map结果集的java.util.List类型列表
     */
    public Object create(ResultSet rs) {
      return create(rs,null);
    }
    
    /**
     * 根据java.sql.ResultSet建立结果集值对象, ResultSet中的每一行将创建到一个Map对象，
       * Map的Key为字段名，Value为值
     * 
     * @param rs java.sql.ResultSet结果集
     * @param startIndex  开始索引, 在构建结果集时,将根据此索引位置开始选择结果集数据
     * @param len 选择的结果集长度
     * 
     * @return 包含有Map结果集的java.util.List类型列表
     */
    public Object create(ResultSet rs, int startIndex, int len) {
      return create(rs, null, startIndex, len);
    } 

}
