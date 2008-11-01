/*
 * HttpPageContext.java	2005-7-16
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

import javax.servlet.jsp.PageContext;

import org.fto.jthink.resource.ResourceContainer;

/**
 * JSP面页上下文容器(HttpPageContext)，相当于javax.servlet.jsp.PageContext。
 * <p>
 * 此类型实现了资源容器抽象接口org.fto.jthink.resource.ResourceContainer，
 * 所以可以将此类型的实现做为资源容器来使用。
 * </p>
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
 * 
 * @see 			javax.servlet.jsp.PageContext
 * 
 */

public class HttpPageContext implements ResourceContainer{
	/* JSP页面上下文 */
	javax.servlet.jsp.PageContext pageContext;
	
	/**
	 * 创建类型HttpPageContext的实例
	 *  
	 * @param pageContext javax.servlet.jsp.PageContext的实例
	 */
	public HttpPageContext(javax.servlet.jsp.PageContext pageContext){
		this.pageContext = pageContext;
	}
	
  /**
   * 返回面页上下文javax.servlet.jsp.PageContext对象
   * 
   * @return javax.servlet.jsp.PageContext对象
   */
	public javax.servlet.jsp.PageContext getPageContext(){
		return this.pageContext;
	}

	
	/**
	 * 加入资源对象。实际上调用PageContext的setAttribute()方法
	 * 
	 * @param name 资源名称
	 * @param resource 资源对象
	 * 
	 * @see org.fto.jthink.resource.ResourceContainer#setAttribute(java.lang.String, java.lang.Object)
	 * @see javax.servlet.jsp.PageContext#setAttribute(java.lang.String, java.lang.Object, int)
	 */
	public void setAttribute(String name, Object resource) {
		pageContext.setAttribute(name, resource, PageContext.PAGE_SCOPE);
	}

	/**
	 * 返回资源对象。实际上调用PageContext的getAttribute()方法
	 * 
	 * @param name 资源名称
	 * 
	 * @return 资源对象
	 * 
	 * @see org.fto.jthink.resource.ResourceContainer#getAttribute(java.lang.String)
	 * @see javax.servlet.jsp.PageContext#getAttribute(java.lang.String, int)
	 */
	public Object getAttribute(String name) {
		return pageContext.getAttribute(name, PageContext.PAGE_SCOPE);
	}

	/**
	 * 删除并返回资源对象。实际上调用PageContext的removeAttribute()方法
	 * 
	 * @param name 资源名称
	 * 
	 * @return 资源对象
	 * 
	 * @see org.fto.jthink.resource.ResourceContainer#removeAttribute(java.lang.String)
	 * @see javax.servlet.jsp.PageContext#removeAttribute(java.lang.String, int)
	 */
	public Object removeAttribute(String name) {
		Object obj = pageContext.getAttribute(name, PageContext.PAGE_SCOPE);
		pageContext.removeAttribute(name, PageContext.PAGE_SCOPE);
		return obj;
	}

	/**
	 * 返回所有资源名称。实际上调用PageContext的removeAttribute()方法
	 * 
	 * @return 通过java.util.Enumeration枚举所有资源名称
	 * 
	 * @see org.fto.jthink.resource.ResourceContainer#getAttributeNames()
	 * @see java.util.Enumeration
	 * @see javax.servlet.jsp.PageContext#getAttributeNamesInScope(int)
	 */
	public Enumeration getAttributeNames() {
		return pageContext.getAttributeNamesInScope(PageContext.PAGE_SCOPE);
	}

}
