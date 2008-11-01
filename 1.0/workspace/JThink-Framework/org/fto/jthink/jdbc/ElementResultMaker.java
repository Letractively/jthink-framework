/*
 * ElementResultMaker.java	2005-8-30
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

import org.fto.jthink.exception.JThinkErrorCode;
import org.fto.jthink.exception.JThinkRuntimeException;
import org.jdom.*;
import java.util.*;
import java.sql.*;


/**
 * 此类型的实例用于构建XML结果集，结果集记录节点类型为org.jdom.Element。
 * 
 * 
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2005-08-30  创建此类型
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 * @see org.jdom.Element
 */
public class ElementResultMaker implements ResultMaker {

	/* 节点名称 */
  private String elementName = null;

  /**
   * 创建类型ElementResultMaker的实例
   */
  public ElementResultMaker(){
  	this.elementName = "Record";
  }
  
  /**
   * 创建类型ElementResultMaker的实例
   * 
   * @param elementName 节点名称
   */
  public ElementResultMaker(String elementName){
  	this.elementName = elementName;
  }
  
  /**
   * 设置节点名称
   * @param name 节点名称
   */
  public void setElementName(String name){
  	this.elementName = name;
  }
  
  /**
   * 返回节点名称
   * 
   * @return 节点名称
   */
  public String getElementName(){
  	return elementName;
  }
  
  /**
   * 创建org.jdom.Element元素列表，ResultSet中的每一行将创建到一个org.jdom.Element对象，
   * ResultSet中的列将创建到org.jdom.Attribute对象。
   * 
   * @param rs 表结果集,java.sql.ResultSet
   * 
   * @return 包含有XML结果集的java.util.List类型列表
   */
  public Object create(ResultSet rs, Class doClazz){
    ArrayList al=new ArrayList();
    try {
      while (rs.next()) {
        ResultSetMetaData rsMD = rs.getMetaData();
        int cloumnCount = rsMD.getColumnCount();
        Element knotElement = new Element(elementName);
        for (int i = 0; i < cloumnCount; i++) {
          String attName = rsMD.getColumnName(i+1);
          if(knotElement.getAttribute(attName)==null){
	          String attValue = rs.getString(i+1);
	          if(attValue!=null){
	          	knotElement.setAttribute(attName, attValue);
	          }
          }
        }
        al.add(knotElement);
      }
    }
    catch (SQLException ex) {
      throw new JThinkRuntimeException(JThinkErrorCode.ERRCODE_DB_EXEC_SQL_EXCEPTION, ex);
    }
    return al;
  }


  /**
   * 创建org.jdom.Element元素列表，ResultSet中的每一行将创建到一个org.jdom.Element对象，
   * ResultSet中的列将创建到org.jdom.Attribute对象。
   * 
   * 
   * @param rs 表结果集,java.sql.ResultSet
   * @param startIndex 开始索引位置, 用于定位从结果集rs中选择数据的开始位置
   * @param len 记录数,用于决定从结果集rs中选择多少记录
   * 
   * @return 包含有XML结果集的java.util.List类型列表
   */
 public Object create(ResultSet rs, Class doClazz, int startIndex, int len) {
    int currentRow = 0;
    int minRow = startIndex;
    ArrayList al=new ArrayList(len);
    try {
      while (rs.next()) {
        if (currentRow < minRow) {
          currentRow++;
          continue;
        }
        ResultSetMetaData rsMD = rs.getMetaData();
        int cloumnCount = rsMD.getColumnCount();
        Element knotElement = new Element(elementName);
        for (int i = 0; i < cloumnCount; i++) {
          String attName = rsMD.getColumnName(i+1);
          if(knotElement.getAttribute(attName)==null){
	          String attValue = rs.getString(i+1);
	          if(attValue!=null){
	          	knotElement.setAttribute(attName, attValue);
	          }
          }
        }
        al.add(knotElement);
      }
    }
    catch (SQLException ex) {
      throw new JThinkRuntimeException(JThinkErrorCode.ERRCODE_DB_EXEC_SQL_EXCEPTION, ex);
    }
    return al;
  }

 /**
  * 创建org.jdom.Element元素列表，ResultSet中的每一行将创建到一个org.jdom.Element对象，
  * ResultSet中的列将创建到org.jdom.Attribute对象。
  * 
  * @param rs 表结果集,java.sql.ResultSet
  * 
  * @return 包含有XML结果集的java.util.List类型列表
  */
  public Object create(ResultSet rs) {
    return create(rs, null);
  }
  
  /**
   * 创建org.jdom.Element元素列表，ResultSet中的每一行将创建到一个org.jdom.Element对象，
   * ResultSet中的列将创建到org.jdom.Attribute对象。
   * 
   * 
   * @param rs 表结果集,java.sql.ResultSet
   * @param startIndex 开始索引位置, 用于定位从结果集rs中选择数据的开始位置
   * @param len 记录数,用于决定从结果集rs中选择多少记录
   * 
   * @return 包含有XML结果集的java.util.List类型列表
   */
  public Object create(ResultSet rs, int startIndex, int len) {
    return create(rs, null, startIndex, len);
  }



}