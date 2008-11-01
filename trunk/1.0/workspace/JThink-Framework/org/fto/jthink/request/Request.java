/*
 * HttpRequest.java	2005-4-18
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

package org.fto.jthink.request;


import java.io.Serializable;
import java.util.Enumeration;
//import java.util.HashMap;

import org.fto.jthink.resource.ResourceContainer;

/**
 * 描述请求的接口。
 * 
 * <p>
 * 此类型的具体实现可以被序列化，但只有它包含的资源对象支持序列化时，
 * 那些资源对象才可能被序列化。
 * </p>
 * 
 * <p>
 * 此接口扩展了资源容器抽象接口org.fto.jthink.resource.ResourceContainer，
 * 所以可以将此类型的具体实现做为资源容器来使用。
 * </p>
 *
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2005-04-18  创建此类型
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 * 
 */

public interface Request extends ResourceContainer, Serializable{
	
	/**
	 * 加入参数值
	 * @param name 参数名称
	 * @param value 值
	 */
	public void putParameter(String name, String value);

	/**
	 * 加入参数值，可以一次加入多个值
	 * @param name 参数名称
	 * @param values 值数组
	 */
	public void putParameter(String name, String[] values);
	
	/**
	 * 返回参数值
	 * @param name 参数名称
	 * @return 如果不存在，返回null
	 */
	public String getParameter(String name) ;
	
	/**
	 * 移除并返回指定名称的参数值
	 * @param name 参数名称
	 * @return 请求参数值,如果不存在，返回null
	 */
	public String removeParameter(String name);
	
	
	/**
	 * 以数组方式返回请求数据
	 * @param name 请求关键字段名称
	 * @return 如果参数不存在,返回null
	 */
	public String[] getParameterValues(String name);
	
	
	/**
	 * 枚举所有参数关键字名称
	 */
	public Enumeration getParameterNames(); 

}
