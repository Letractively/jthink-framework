/*
 * JThinkEJBHomeFactory.java	2005-7-17
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

import java.lang.reflect.Constructor;

import org.fto.jthink.config.Configuration;
import org.fto.jthink.exception.JThinkErrorCode;
import org.fto.jthink.exception.JThinkRuntimeException;
import org.fto.jthink.resource.ResourceManager;

/**
 * JThink/EJBHome工厂，通过此工厂查找JThink/EJB主类。
 * 此工厂的主要职责就是通过配置信息查找JThink/EJB主类，当前应用可以运行在任何遵循JSP规范的WEB容器中, 或桌面应用。
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

public class JThinkEJBHomeFactory implements EJBHomeFactory {
	ResourceManager resManager;
	
	/**
	 * 创建工厂JThinkEJBHomeFactory的实例。由于JThink/EJB实际上全是本地应用，
	 * 所以resContainerName，config，serverId等参数没有实际意义
	 * 
	 * @param resManager        资源管理器
	 * @param resContainerName  用于缓存Home接口的资源容器名
	 * @param config						 系统配置文件信息
	 * @param serverId          JThink/EJB应用服务器ID,此值在配置文件设置
	 */
	public JThinkEJBHomeFactory(ResourceManager resManager,String resContainerName, Configuration config, String serverId){
		this.resManager = resManager;
	}
	
	/**
	 * 创建并返回JThink/EJB主类
	 * 
	 * @param homeClass JThink/EJBHome接口的子类
	 * @param jndi 用于查找EJBRemoteHome接口的JNDI名称
	 * 
	 * @return JThink/EJBHome接口的实现
	 */

	public Object create(Class homeClass, String jndi) {
//		if (!homeClass.isInstance(org.fto.jthink.j2ee.ejb.EJBHome.class)) {
//			throw new JFreeRuntimeException("不是JFree/EJB的Home类! homeClass:"+homeClass.getName());
//		}
		try{
			Constructor homeCR = homeClass.getConstructor(new Class[]{ResourceManager.class});
			Object homeObj = homeCR.newInstance(new Object[]{resManager});
		  return homeObj;
		}
		catch (Exception ex) {
		  throw new JThinkRuntimeException(JThinkErrorCode.ERRCODE_JB_LOOKUP_EJB_FAILURE,
		  		"Look up ejb failure! jndi = " + jndi, ex);
		}
	}

}
