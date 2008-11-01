/*
 * HttpRequestFilter.java	2005-6-25
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

import javax.servlet.Filter;

/**
 * Http请求过滤器，主要事件：初始化Web服务器，Http请求，停止服务器。
 * 要正确使用过滤器，必须要在web.xml中配置此资源。
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
 * @see 			javax.servlet.Filter
 * 
 */
public interface HttpRequestFilter extends Filter {

}