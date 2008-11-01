/*
 * HttpRequest.java	2005-6-25
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


import java.io.IOException;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.fto.jthink.j2ee.web.fileload.FileUpload;
import org.fto.jthink.request.DefaultRequest;

/**
 * Http请求(HttpRequest)，相当于javax.servlet.http.HttpServletRequest。
 * HttpRequest在HttpServletRequest的基础上加入了些新特性。可以适应不同请求类型，
 * 比如流数据方式的请求(上传文件的情况), HttpRequest能自动收集请求字段；
 * 可以再次向HttpRequest中加入请求参数值；HttpRequest已经支持文件上传功能。
 * 
 * <p>
 * 此HttpRequest类型的实例可以被序列化，但只有它包含的资源对象支持序列化时，
 * 那些资源对象才可能被序列化。
 * </p>
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
 * @see 			javax.servlet.http.HttpServletRequest
 * 
 */
public class HttpRequest extends DefaultRequest {

  private static final long serialVersionUID = -7072772587494793897L;
  private HttpServletRequest request;
	private FileUpload fileUpload;
	
	/**
	 * 创建类型HttpRequest的实例。
	 * 
	 * @param request HttpServletRequest请求对象
	 */
	public HttpRequest(HttpServletRequest request){
		if (request == null){
			throw new IllegalArgumentException(
					"The request cannot be null.");
		}
		this.request = request;
		initRequestData();
	}

	/**
	 * 在序列化时调用, 由JVM自动调用
	 * 
	 * @param out
	 * @throws IOException
	 */
	private void writeObject(java.io.ObjectOutputStream out)
	     throws IOException{
		
	  Map attrsHM = new HashMap();
		/* 筛选出能够被序列化的资源对象 */
		Enumeration attrNames = getAttributeNames();
		while(attrNames.hasMoreElements()){
			String name = (String)attrNames.nextElement();
			Object obj = getAttribute(name);
			if(Serializable.class.isInstance(obj)){
				attrsHM.put(name, obj);
			}
		}
		out.writeObject(super.reqsHM);
		out.writeObject(attrsHM);
	}
	/**
	 * 在反序列化时调用, 由JVM自动调用
	 * @param in
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	 private void readObject(java.io.ObjectInputStream in)
	     throws IOException, ClassNotFoundException{
	 	super.reqsHM = (Map)in.readObject();
		super.attrsHM = (Map)in.readObject();
	 }
	
	/**
	 * 返回HttpServletRequest请求对象
	 * 
	 * @return HttpServletRequest请求
	 */
	public HttpServletRequest getServletRequest(){
		return request;
	}

	/**
	 * 设置HttpServletRequest请求对象
	 * 
	 * @param request HttpServletRequest请求
	 */
	public void setServletRequest(HttpServletRequest request){
		if (request == null){
			throw new IllegalArgumentException(
					"The request cannot be null.");
		}
		this.request=request;
	}	
	
  /**
   * 初始化请求数据，可以对不同表单类型的数据进行处理,比如表单为文件域等
   */
  private void initRequestData() {
      /* 初始化请求数据 */
	    Enumeration namesEM = request.getParameterNames();
	    while(namesEM.hasMoreElements()){
	    	String name = (String)namesEM.nextElement();
	    	String[] values = request.getParameterValues(name);
	    	if(values!=null){
		    	for(int i=0;i<values.length;i++){
		    		String value = values[i];
		    		if(value!=null && value.length()!=0){
		    			putParameter(name, value);
		    		}
		    	}
	    	}
	    }
  }

  /**
   * 返回文件上传对象FileUpload的实例。如果是表单类型是multipart/form-data，
   * 要获取表单中的请求字段值，必须首先调用FileUpload的upload()方法。
   *
   * @return 文件上传对象，如果表单类型不是multipart/form-data，返回null
   */
  public FileUpload getFileUpload(){
  	if(fileUpload!=null){
  		return fileUpload;
  	}
    String contentType = request.getContentType();
    if (contentType != null &&
        contentType.toLowerCase().indexOf("multipart/form-data") != -1) {
    	fileUpload = new FileUpload(request.getSession().getServletContext(), this);
    }
  	return fileUpload;
  }
  
  /**
   * 从HttpServletRequest中返回一请求数据。为了兼容不同的WEB应用服务器，
   * 请求字段的值如果没有找到或是值长度为0的空串""，统一返回null。
   * 
   * @param name 请求字段名称
   * 
   * @return 请求的值
   */
  public String getRequestParameter(String name) {
  	if(request==null){
  		return null;
  	}
    String value = request.getParameter(name);
    if (value != null && value.length()==0) {
      value = null;
    }
    return value;
  }


	/**
	 * 返回请求参数值
	 * 
	 * @param name 参数字段名称
	 * 
	 * @return 参数值，如果不存在，返回null
	 */
	public String getParameter(String name) {
    String value = super.getParameter(name);
    if(value==null){
      value = getRequestParameter(name);
    }
    return value;
	}

	/**
	 * 加入资源对象
	 * 
	 * @param name 资源名称
	 * @param resource 资源对象
	 */
	public void setAttribute(String name, Object resource) {
		if(request!=null){
			request.setAttribute(name, resource);
		}else{
			super.setAttribute(name, resource);
		}
	}

	/**
	 * 返回资源对象
	 * 
	 * @param name 资源名称
	 * 
	 * @return 资源对象
	 */
	public Object getAttribute(String name) {
		if(request!=null){
			return request.getAttribute(name);
		}else{
			return super.getAttribute(name);
		}
	}

	/**
	 * 删除并返回资源对象
	 * 
	 * @param name 资源名称
	 * 
	 * @return 资源对象
	 */
	public Object removeAttribute(String name) {
		if(request!=null){
			Object obj = request.getAttribute(name);
			request.removeAttribute(name);
			return obj;
		}else{
			return super.removeAttribute(name);
		}
	}

	/**
	 * 返回所有资源名称
	 * 
	 * @return 通过java.util.Enumeration枚举所有资源名称
	 * 
	 * @see java.util.Enumeration
	 */
	public Enumeration getAttributeNames() {
		if(request!=null){		
			return request.getAttributeNames();
		}else{
			return super.getAttributeNames();
		}
	}



	

}
