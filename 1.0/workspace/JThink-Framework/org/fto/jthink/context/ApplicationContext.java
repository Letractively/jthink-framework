/*
 * ApplicationContext.java	2005-6-26
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

package org.fto.jthink.context;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.fto.jthink.resource.ResourceContainer;
import org.fto.jthink.util.EnumerationHelper;


/**
 * 应用程序全局资源容器。采用单件模式，此类型在同一上下文环境中只可能生成一个实例。
 * 那种在整个JVM环境中共享的资源可以放入此容器中。
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

public final class ApplicationContext implements ResourceContainer {
	
	/* 用于保存被加入到此容器中的资源 */
	private Map resourcesHM = new HashMap();
	
	/* 类型ApplicationContext的唯一实例 */
	private static ApplicationContext appnContext = new ApplicationContext();
	
	
	/**
	 * 构建方法，因为被定义为private类型，所以只可能在类内部实例化。
	 */
	private ApplicationContext(){}
	

	/**
	 * 返回资源容器
	 * 
	 * @return ApplicationContext的唯一实例
	 */
	public static ApplicationContext getApplicationContext(){
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
