/*
 * ResultMaker.java	2005-7-21
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

import java.sql.ResultSet;

/**
 * 用于构建执行SQL后的结果集, 具体采用什么数据结构返回结果集,
 * 由ResultMaker的实现者来确定。
 * 
 * 
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2005-07-21  创建此类型
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 */
public interface ResultMaker {
	
	/**
	 * 根据java.sql.ResultSet建立结果集值对象
	 * 
	 * @param rs java.sql.ResultSet结果集
	 * 
	 * @return 包含有结果数据的数据结构
	 */
	public Object create(ResultSet rs);

	/**
	 * 根据java.sql.ResultSet建立结果集值对象
	 * 
	 * @param rs java.sql.ResultSet结果集
	 * @param startIndex  开始索引, 在构建结果集时,将根据此索引位置开始选择结果集数据
	 * @param len 选择的结果集长度
	 * 
	 * @return 包含有结果数据的数据结构
	 */
	public Object create(ResultSet rs, int startIndex, int len);	
	
	
	
  /**
   * 根据java.sql.ResultSet建立结果集值对象, 以doClazz指定的DataObject类型返回
   * 如果doClazz为null以默认DataObject类型返回结果集
   * 
   * @param rs java.sql.ResultSet结果集
   * @param doClazz 数据对象类型
   * 
   * @return 包含有结果数据的数据结构
   */
  public Object create(ResultSet rs, Class doClazz);

  /**
   * 根据java.sql.ResultSet建立结果集值对象, 以doClazz指定的DataObject类型返回
   * 如果doClazz为null以默认DataObject类型返回结果集
   * 
   * @param rs java.sql.ResultSet结果集
   * @param doClazz 数据对象类型 
   * @param startIndex  开始索引, 在构建结果集时,将根据此索引位置开始选择结果集数据
   * @param len 选择的结果集长度
   * 
   * @return 包含有结果数据的数据结构
   */
  public Object create(ResultSet rs, Class doClazz, int startIndex, int len);  
}
