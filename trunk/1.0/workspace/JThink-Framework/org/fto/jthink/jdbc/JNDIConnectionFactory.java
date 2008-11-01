/*
 * JNDIConnectionFactory.java	2005-7-16
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


import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.naming.*;
import javax.sql.*;

import org.fto.jthink.exception.JThinkErrorCode;
import org.fto.jthink.exception.JThinkRuntimeException;
import org.jdom.Element;


/**
 * 数据库连接工厂,用于创建java.sql.Connection数据库连接。
 * 此工厂通过JNDI名称来建立数据库连接，JNDI名称一般情况下是在应用服务器中配置的，比如JBoss, Websphere等。
 * 如果采用此工厂创建数据库连接, 建议不要采用JThink提供的连接池来管理连接,因为应用服务器已经提供了对连接的池管理功能，
 * 在配置文件fto-jthink.xml中可直接设置是否采用连接池。
 * 
 * <pre><code>
 * 要使用此工厂,必须要在配置文件中正确配置相关连接信息，以下是在fto-jthink.xml文件中的配置信息
 * 用于连接到Mysql数据库。
 * 
			&lt;connection-factory&gt;
			  <blockquote>
				&lt;!-- 工厂类定义 --&gt;
				&lt;factory-class&gt;org.fto.jthink.jdbc.JNDIConnectionFactory&lt;/factory-class&gt;
				&lt;!-- 在应用服务器中配置的数据源JNDI名称 --&gt;
				&lt;jndi-name&gt;java:sample-mysql-datasource&lt;/jndi-name&gt;
				&lt;!--是否缓存DataSoruce, 值:{yes,no}={是,否}  --&gt;
				&lt;store-data-source&gt;yes&lt;/store-data-source&gt;
				</blockquote>
 			&lt;/connection-factory&gt;

 * </code></pre>
 * 
 * 
 * 
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2005-07-16  创建此类型
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 */

public final class JNDIConnectionFactory implements ConnectionFactory {

	/**
	 * 数据源JNDI名称
	 */
	private String jndi = null;
	private boolean storeDS = false;
	private static Map dsHM = new HashMap(); //用于缓存数据源
	
	/**
	 * 创建DirectConnectionFactory的实例
	 * 
	 * @param connCfg 连接配置信息
	 */
	public JNDIConnectionFactory(Element connCfg){
		Element connFctyCfg = connCfg.getChild("connection-factory");
		this.jndi = connFctyCfg.getChildText("jndi-name");
		this.storeDS = connFctyCfg.getChildText("store-data-source").trim().equalsIgnoreCase("yes");

	}	

	  
  /**
   * 建立数据库连接(Connection)
   * 
   * @return 数据库连接(Connection)
   * 
   */
  public Connection create()  {
    try {
    	DataSource ds = null;
    	if(storeDS==true){
	    	ds = (DataSource)dsHM.get(jndi);
	    	if(ds==null){
	    		ds = getDataSource(jndi);
	    		dsHM.put(jndi, ds);
	    	}
    	}else{
    		ds = getDataSource(jndi);
    	}
      return ds.getConnection();
    } catch (SQLException ex) {
      throw new JThinkRuntimeException(
      		JThinkErrorCode.ERRCODE_DB_CANNOT_GET_CONNECTION,
        "Cannot get connection! JNDI = "+jndi,ex);
    }
	}

  /**
   * 根据数据源JNDI返回数据源对象。
   * 
   * @param jndi 数据源JNDI名称
   * 
   * @return DataSource数据源对象
   */
  private DataSource getDataSource(String jndi)  {
    Context ctx = null;
    try {
      ctx = new InitialContext();
      return (DataSource) ctx.lookup(jndi);
    }
    catch (Exception ex) {
      throw new JThinkRuntimeException(
      		JThinkErrorCode.ERRCODE_DB_CANNOT_GET_DATASOURCE,
          "DataSource JNDI = " + jndi,ex);
    }finally{
      if(ctx!=null){
        try {
          ctx.close();
        }
        catch (NamingException ne) {
          throw new JThinkRuntimeException(
          		JThinkErrorCode.ERRCODE_DB_CANNOT_GET_DATASOURCE,
              "关闭Context对象失败, DataSource JNDI = " + jndi, ne);
        }
      }
    }
  }


}
