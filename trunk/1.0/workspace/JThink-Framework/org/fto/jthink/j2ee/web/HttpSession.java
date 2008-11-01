/*
 * HttpSession.java	2005-6-25
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
package org.fto.jthink.j2ee.web;

import java.util.Enumeration;

import org.fto.jthink.resource.ResourceContainer;

/**
 * Http用户会话(HttpSession)，相当于javax.servlet.http.HttpSession。
 * 
 * <p>
 * 此类型实现了资源容器抽象接口org.fto.jthink.resource.ResourceContainer，
 * 所以可以将此类型的实例做为资源容器来使用。
 * </p>
 *
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2005-06-25  创建此类型
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 * @see 			javax.servlet.http.HttpSession
 * 
 */
public class HttpSession implements ResourceContainer {

	javax.servlet.http.HttpSession session;
	
	/**
	 * 创建类型HttpSession的实例。
	 * 
	 * @param session 标准Http用户会话javax.servlet.http.HttpSession的实例
	 */
	public HttpSession(javax.servlet.http.HttpSession session){
		this.session = session;
	}
	
	/**
	 * 返回标准Http用户会话
	 * 
	 * @return 标准Http用户会话javax.servlet.http.HttpSession的实例
	 */
	public javax.servlet.http.HttpSession getSession(){
		return this.session;
	}
	
	/**
	 * 加入资源对象
	 * 
	 * @param name 资源名称
	 * @param resource 资源对象
	 * 
	 */
	public void setAttribute(String name, Object resource) {
		session.setAttribute(name, resource);
	}

	/**
	 * 返回资源对象
	 * 
	 * @param name 资源名称
	 * 
	 * @return 资源对象
	 * 
	 */
	public Object getAttribute(String name) {
		return session.getAttribute(name);
	}

	/**
	 * 删除并返回资源对象
	 * 
	 * @param name 资源名称
	 * 
	 * @return 资源对象
	 * 
	 */
	public Object removeAttribute(String name) {
		Object obj = session.getAttribute(name);
		session.removeAttribute(name);
		return obj;
	}

	/**
	 * 返回所有资源名称
	 * 
	 * @return 通过java.util.Enumeration枚举所有资源名称
	 * 
	 * @see java.util.Enumeration
	 */
	public Enumeration getAttributeNames() {
		return session.getAttributeNames();
	}


}
