/*
 * EJBHomeFactory.java	2005-7-5
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
package org.fto.jthink.j2ee.ejb;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import org.fto.jthink.config.Configuration;
import org.fto.jthink.exception.JThinkErrorCode;
import org.fto.jthink.exception.JThinkRuntimeException;
import org.fto.jthink.resource.ResourceContainer;
import org.fto.jthink.resource.ResourceManager;
import org.jdom.Element;

/**
 * J2EE/EJBRemoteHome工厂，通过此工厂查找远程J2EE/EJB主接口。
 * 此工厂的主要职责就是通过配置信息查找远程J2EE/EJB主接口，当前应用必须运行在J2EE/EJB容器中。
 *
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2005-07-05  创建此类型
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 */

public class EJBRemoteHomeFactory implements EJBHomeFactory{

	/* 用于缓存Home接口的资源容器 */
	private ResourceContainer resContainer;
	/* 资源名称，将以此名称为主鍵在资源容器中缓存EJBRemoteHome接口 */
	private String resName;
	/* J2EE/EJB应用服务器ID，此值在配置文件设置 */
	private String serverId;
	/* 用于查找EJBRemoteHome接口的JNDI的前缀，此值在配置文件设置 */
	private String jnidPrefixion;
	/* 是否缓存EJBLocalHome接口，此值在配置文件设置 */
	private boolean storeHome = false;
	/* 存放查找EJBRemoteHome接口需要的参数信息 */
	Properties ctxp = new Properties(); 
	
	/**
	 * 创建工厂EJBRemoteHomeFactory的实例。
	 * 
	 * @param resManager        资源管理器
	 * @param resContainerName  用于缓存Home接口的资源容器名
	 * @param config						 系统配置文件信息
	 * @param serverId          J2EE/EJB应用服务器ID,此值在配置文件设置
	 */
	public EJBRemoteHomeFactory(ResourceManager resManager, String resContainerName, Configuration config, String serverId){
		/* 返回与resContainerName匹配的资源容器 */
		this.resContainer = resManager.getResourceContainer(resContainerName);
		this.serverId = serverId;
		this.resName = EJBRemoteHomeFactory.class.getName()+"#"+serverId;
		
		/* 返回相关配置信息：JNDI前缀(jndi-prefixion)、是否缓存Home接口(store-home),以及查找EJBRemoteHome接口需要的参数信息 */
		Element ejbCallerEL = config.getEJBServerConfig(serverId).getChild("ejb-caller");
		jnidPrefixion = ejbCallerEL.getChildText("jndi-prefixion");
		storeHome = ejbCallerEL.getChildText("store-home").equalsIgnoreCase("yes");
		/* 初始化Context参数 */
		Iterator iccIT = config.getEJBServerConfig(serverId).getChild("initial-context-config").getChildren().iterator();
		while(iccIT.hasNext()){
			Element el = (Element)iccIT.next();
      String text = el.getText();
      if (text.trim().length()>0) {
      	ctxp.put(el.getName(), text);
      }
		}
	}
	


	/**
	 * 创建并返回J2EE/EJB远程主接口, 如果在资源容器中缓存有Home接口,将直接返回此接口
	 * 
	 * @param homeClass J2EE/EJBRemoteHome接口的子Home接口
	 * @param jndi 用于查找EJBRemoteHome接口的JNDI名称
	 * 
	 * @return EJBRemoteHome接口的实现
	 */

	public Object create(Class homeClass, String jndi) {
//		if (!homeClass.isInstance(javax.ejb.EJBHome.class)) {
//			throw new JFreeRuntimeException("不是远程Home接口! homeClass:"+homeClass.getName());
//		}
		/* 如果缓存了Home接口, 直接返回此接口 */
		if(storeHome){
		  Map homesHM = (Map)resContainer.getAttribute(resName);
			if(homesHM!=null){
				Object homeObj = homesHM.get(jndi);
				if(homeObj!=null){
					return homeObj;
				}
			}
		}
		/* 否则，查找新的Home接口 */
		Context ctx = null;
		try {
			/* New Context */
			ctx = new InitialContext(ctxp);
			/* lookup ejb */
			Object objRef =  ctx.lookup(jnidPrefixion + jndi);
			Object homeObj = PortableRemoteObject.narrow(objRef, homeClass);
			
			/* 如果须要缓存Home接口, 将此接口保存到serversHM中 */
			if(storeHome){
			  Map homesHM = (Map)resContainer.getAttribute(resName);
				if(homesHM==null){
					homesHM = new HashMap();
					resContainer.setAttribute(resName, homesHM);
				}
				homesHM.put(jndi, homeObj);
			}
			
			return homeObj;
			
		} catch (Exception ex) {
			if (ex instanceof JThinkRuntimeException) {
				throw (JThinkRuntimeException) ex;
			}
		  throw new JThinkRuntimeException(JThinkErrorCode.ERRCODE_JB_LOOKUP_EJB_FAILURE,
		  			"Look up ejb failure! jndi = " + jndi, ex);
		} finally {
			if (ctx != null) {
				try {
					ctx.close();
				} catch (NamingException e) {
				  throw new JThinkRuntimeException(JThinkErrorCode.ERRCODE_JB_LOOKUP_EJB_FAILURE,
			  			"Look up ejb failure! jndi = " + jndi, e);
				}
			}
		}
	}
	
}
