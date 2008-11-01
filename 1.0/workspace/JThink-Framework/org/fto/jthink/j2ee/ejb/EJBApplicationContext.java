
/*
 * EJBApplicationContext.java	2005-6-26
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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.fto.jthink.resource.ResourceContainer;
import org.fto.jthink.util.EnumerationHelper;


/**
 * 应用程序EJB端上下文资源容器, 此容器中的资源在一定范围内是共享的，
 * 这要根据EJB模块ID(ejbModuleId)来决定, 因为在EJB容器端里没有象在WEB容器端的那种
 * ServletContext WEB端全局资源容器，所以只能自定义一个EJB端的资源容器来用,
 * 但必须要将不同的EJB端资源容器区别开，不然容易引起资源的冲突。
 * <p>
 * 此类型实现了资源容器抽象接口org.fto.jthink.resource.ResourceContainer，
 * 所以可以将此类型的实例做为资源容器来使用。
 * </p>
 * 
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2005-06-26  创建此类型
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 * @see 			org.fto.jthink.resource.ResourceContainer
 * 
 */
public final class EJBApplicationContext implements ResourceContainer {

	private Map resourcesHM = new HashMap();
	
	private EJBApplicationContext(){}
	
	private static Map appnContextHM = new HashMap();
	
	/**
	 * 返回一个EJB端上下文资源容器, 如果此资源容器不存在，将会创建一个新的资源容器，
	 * 但一个<code>ejbModuleId</code>只可能创建一个EJBApplicationContext上下文资源容器对象
	 * 
	 * @param ejbModuleId 用于区别不同的EJB端资源容器的ID，将根据此<code>ejbModuleId</code>返回EJBApplicationContext的实例
	 * 
	 * @return 与<code>ejbModuleId</code>匹配的ApplicationContext实例
	 */
	public static synchronized EJBApplicationContext getApplicationContext(String ejbModuleId){
		if (ejbModuleId == null){
			throw new IllegalArgumentException(
					"The ejbModuleId cannot be null.");
		}
		EJBApplicationContext appnContext = (EJBApplicationContext)appnContextHM.get(ejbModuleId);
		if(appnContext==null){
			appnContext = new EJBApplicationContext();
			appnContextHM.put(ejbModuleId, appnContext);
		}
		
		return appnContext;
	}
	
	/**
	 * 加入资源对象
	 * 
	 * @param name 资源名称
	 * @param resource 资源对象
	 * 
	 * @see org.fto.jthink.resource.ResourceContainer#setAttribute(java.lang.String, java.lang.Object)
	 */
	public synchronized void  setAttribute(String name, Object resource) {
		if (name == null){
			throw new IllegalArgumentException(
					"The name of an attribute cannot be null.");
		}
		resourcesHM.put(name, resource);
	}

	/**
	 * 返回资源对象
	 * 
	 * @param name 资源名称
	 * 
	 * @return 资源对象
	 * 
	 * @see org.fto.jthink.resource.ResourceContainer#getAttribute(java.lang.String)
	 */
	public Object getAttribute(String name) {
		if (name == null){
			throw new IllegalArgumentException(
					"The name of an attribute cannot be null.");
		}
		return resourcesHM.get(name);
	}

	/**
	 * 删除并返回资源对象
	 * 
	 * @param name 资源名称
	 * 
	 * @return 资源对象
	 * 
	 * @see org.fto.jthink.resource.ResourceContainer#removeAttribute(java.lang.String)
	 */
	public synchronized Object removeAttribute(String name) {
		if (name == null){
			throw new IllegalArgumentException(
					"The name of an attribute cannot be null.");
		}
		return resourcesHM.remove(name);
	}

	/**
	 * 返回所有资源名称
	 * 
	 * @return 通过java.util.Enumeration枚举所有资源名称
	 * 
	 * @see org.fto.jthink.resource.ResourceContainer#getAttributeNames()
	 * @see java.util.Enumeration
	 */
	public Enumeration getAttributeNames() {
		return EnumerationHelper.toEnumerator(resourcesHM.keySet().iterator());
	}

	
}
