/*
 * Configuration.java	2005-7-16
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

package org.fto.jthink.config;

import java.io.Serializable;

import org.fto.jthink.exception.JThinkRuntimeException;
import org.fto.jthink.util.XMLHelper;
import org.jdom.Element;

/**
 * 系统配置文件，针对fto-jthink.xml进行处理，通过此类，可以返回大部分配置信息，
 * 在启动应用的时候初始化此类型。关于配置文件fto-jthink.xml的详细说明参见相关文件。
 * 
 * <p>
 * <pre><b>历史更新记录:</b>
 * 2005-07-16  创建此类型
 * </pre>
 * </p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 */

public class Configuration implements Serializable {

  private static final long serialVersionUID = -5068628861863428715L;
  private Element configEL;
  
  private static Configuration cfg = null;

	/**
	 * 创建Configuration类型的对象, 输入参数为配置文件fto-jthink.xml,包含全路径。
	 * 
	 * @param configFile 配置文件, fto-jthink.xml及其全路径
	 */
	public Configuration(String configFile) {
		configEL = XMLHelper.load(configFile);
	}

	/**
	 * 返回fto-jthink.xml配置
	 * @return
	 */
	public static Configuration getConfiguration(){
	  if(cfg==null){
	    throw new JThinkRuntimeException("还没有正确配置,fto-jthink.xml文件没有找到！");
	  }
	  return cfg;
	}
	
	/**
	 * 创建Configuration类型的对象, 输入参数为配置文件fto-jthink.xml的org.jdom.Element对象形式。
	 * 
	 * @param configEL 配置文件fto-jthink.xml的org.jdom.Element对象形式
	 */
	public Configuration(Element configEL) {
		this.configEL = configEL;
	}

	
	
	/**
	 * 返回JThink的配置信息。
	 * 
	 * @return 配置文件的org.jdom.Element对象形式 
	 */
	public Element getConfig() {
		return configEL;
	}

	/**
	 * 返回数据源配置节点。
	 * 
	 * @return 数据源配置节点，节点名称为“data-source”
	 */
	public Element getDataSourceConfig() {
		return configEL.getChild("data-source");
	}

	/**
	 * 返回数据源配置中的一个数据库连接配置信息。
	 * 
	 * @param id  连接配置ID
	 * @return 数据库连接配置信息
	 */
	public Element getConnectionConfig(String id) {
		return XMLHelper.getChild(configEL.getChild("data-source"), "id", id);
	}

	/**
	 * 返回事务配置信息。
	 * 
	 * @param id  事务配置ID
	 * @return 包含有事务配置信息的org.jdom.Element对象
	 */
	public Element getTransactionConfig(String id) {
		return XMLHelper.getChild(configEL.getChild("transactions"), "id", id);
	}

	/**
	 * 返回EJB服务器配置信息。
	 * 
	 * @param id EJB应用服务器配置ID
	 * @return EJB服务器配置信息
	 */
	public Element getEJBServerConfig(String id) {
		return XMLHelper.getChild(configEL.getChild("ejb-servers"), "id", id);
	}

	/**
	 * 返回日志管理配置信息
	 * 
	 * @return 日志管理中的配置信息
	 */
	public Element getLogManagerConfig() {
		return configEL.getChild("log-manager").getChild("config");
	}
}


