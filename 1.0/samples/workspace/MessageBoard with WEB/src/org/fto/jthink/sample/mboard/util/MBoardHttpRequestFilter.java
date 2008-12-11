/*
 * MBoardHttpRequestFilter.java	2005-10-14
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
package org.fto.jthink.sample.mboard.util;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.fto.jthink.j2ee.web.HttpRequest;
import org.fto.jthink.j2ee.web.HttpRequestFilter;
import org.fto.jthink.j2ee.web.HttpSession;
import org.fto.jthink.j2ee.web.WEBApplicationContext;
import org.fto.jthink.log.LogManager;
import org.fto.jthink.log.Logger;
import org.fto.jthink.resource.ResourceManager;

/**
 * 请求过滤器, 此程序演示了创建创建和设置资源容器，创建资源管理器, 向资源管理器中设置资源。
 * 此过滤器需要在web.xml中配置。
 * 
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink Sample Message Board 1.0
 * @see 	  JThink 1.0
 * 
 */
public class MBoardHttpRequestFilter implements HttpRequestFilter {

	private static final Logger logger = LogManager.getLogger(MBoardHttpRequestFilter.class);
	private static String encoding;
	
	/* （非 Javadoc）
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig config) throws ServletException {
		encoding = config.getInitParameter("encoding");
	}

	/* （非 Javadoc）
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		logger.debug("初始化请求.");
		try{
			WEBApplicationContext webContext = new WEBApplicationContext(((HttpServletRequest)req).getSession().getServletContext());

			/* 初始化请求 */
			req.setCharacterEncoding(encoding);
			HttpRequest request = new HttpRequest((HttpServletRequest)req);
			
			/* 初始化资源管理器 */
			ResourceManager resManager = new ResourceManager();
			
			/* 设置资源容器, WEBApplicationContext上下文 */
			resManager.setResourceContainer(WEBApplicationContext.class.getName(), webContext);

			/* 设置资源容器, HttpSession用户会话 */
			HttpSession session = new HttpSession(((HttpServletRequest)req).getSession());
			resManager.setResourceContainer(HttpSession.class.getName(), session);
			
			/* 设置资源容器, HttpRequest请求 */
			resManager.setResourceContainer(HttpRequest.class.getName(), request);
			
			
			/* 设置资源, WEBApplicationContext */
			resManager.setResource(WEBApplicationContext.class.getName(), webContext);
			
			/* 设置资源, HttpSession */
			resManager.setResource(HttpSession.class.getName(), session);
			
			/* 设置资源, HttpRequest */
			resManager.setResource(HttpRequest.class.getName(), request);			
			
			/* 设置资源, ServletResponse */
			resManager.setResource(ServletResponse.class.getName(), res);
			
			/* 将资源管理器加入到Request中,以便于在具休JSP或Servlet中能使用到资源管理器 */
			request.setAttribute(ResourceManager.class.getName(), resManager);
			
			/* 转到下一过滤器 */
			chain.doFilter(req, res);

		}catch(ServletException e){
			throw e;
		}
	}

	/* （非 Javadoc）
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {

	}

}
