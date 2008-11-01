/*
 * ResourceContainer.java	2005-6-24
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

package org.fto.jthink.resource;

import java.util.Enumeration;

/**
 * 资源容器，用于存储向资源管理器ResourceManager中加入的资源。
 *
 * <p>
 * 历史更新记录:<BR>
 * 2005-06-24  创建此类型
 * </p>
 * 
 * <p>
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 * @see 			org.fto.jthink.resource.ResourceManager
 * </p>
 */

public interface ResourceContainer {

	/**
	 * 加入资源对象
	 * 
	 * @param name 资源名称
	 * @param resource 资源对象
	 * 
	 */
	public void setAttribute(String name, Object resource);

	/**
	 * 返回资源对象
	 * 
	 * @param name 资源名称
	 * 
	 * @return 资源对象
	 * 
	 */
	public Object getAttribute(String name);

	/**
	 * 删除并返回资源对象
	 * 
	 * @param name 资源名称
	 * 
	 * @return 资源对象
	 * 
	 */
	public Object removeAttribute(String name);

	/**
	 * 返回所有资源名称
	 * 
	 * @return 通过java.util.Enumeration枚举所有资源名称
	 * 
	 * @see java.util.Enumeration
	 */
	public Enumeration getAttributeNames();
	
	
}



