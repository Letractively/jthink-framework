/*
 * HttpRequest.java	2005-4-19
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



import java.io.IOException;
import java.io.Serializable;
import java.util.*;


import org.fto.jthink.request.Request;
import org.fto.jthink.util.EnumerationHelper;



/**
 * 接口org.fto.jthink.request.Request的默认实现。
 * 
 * <p>
 * 此HttpRequest类型的实例可以被序列化，但只有它包含的资源对象支持序列化时，
 * 那些资源对象才可能被序列化。
 * </p>
 * 
 * <p>
 * 此类型实现了资源容器抽象接口org.fto.jthink.resource.ResourceContainer，
 * 所以可以将此类型的实现做为资源容器来使用。
 * </p>
 *
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2005-04-19  创建此类型
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 * 
 */
public class DefaultRequest implements Request {

  private static final long serialVersionUID = -6551734962710342959L;
  protected Map reqsHM = new HashMap();
	protected Map attrsHM = new HashMap();

	
	/**
	 * 在序列化时调用, 由JVM自动调用
	 * 
	 * @param out
	 * @throws IOException
	 */
	private void writeObject(java.io.ObjectOutputStream out)
	     throws IOException{
		
	  Map tmpAttrsHM = new HashMap();
		/* 筛选出能够被序列化的资源对象 */
		Enumeration attrNames = getAttributeNames();
		while(attrNames.hasMoreElements()){
			String name = (String)attrNames.nextElement();
			Object obj = getAttribute(name);
			if(Serializable.class.isInstance(obj)){
				tmpAttrsHM.put(name, obj);
			}
		}
		out.writeObject(reqsHM);
		out.writeObject(tmpAttrsHM);
	}
	/**
	 * 在反序列化时调用, 由JVM自动调用
	 * @param in
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	 private void readObject(java.io.ObjectInputStream in)
	     throws IOException, ClassNotFoundException{
	 	reqsHM = (Map)in.readObject();
		attrsHM = (Map)in.readObject();
	 }

	
	
	/**
	 * 加入资源对象
	 * 
	 * @param name 资源名称
	 * @param obj 资源对象
	 * 
	 */
	public void setAttribute(String name, java.lang.Object obj) {
		if (name == null){
			throw new IllegalArgumentException(
					"The name of an element cannot be null.");
		}
		attrsHM.put(name, obj);
	}

	/**
	 * 返回资源对象
	 * 
	 * @param name 资源名称
	 * 
	 * @return 资源对象
	 * 
	 */
	public java.lang.Object getAttribute(String name) {
		if (name == null){
			throw new IllegalArgumentException(
					"The name of an element cannot be null.");
		}
		return attrsHM.get(name);
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
		if (name == null){
			throw new IllegalArgumentException(
					"The name of an element cannot be null.");
		}
		return attrsHM.remove(name);
	}

	/**
	 * 返回所有资源名称
	 * 
	 * @return 通过java.util.Enumeration枚举所有资源名称
	 * 
	 * @see java.util.Enumeration
	 */
	public Enumeration getAttributeNames() {
		return EnumerationHelper.toEnumerator(attrsHM.keySet().iterator());
	}


	/**
	 * 加入参数值
	 * 
	 * @param name 参数名称
	 * @param value 值
	 */
	public void putParameter(String name, String value) {
		if (name == null){
			throw new IllegalArgumentException(
					"The name of an element cannot be null.");
		}
		if (reqsHM.containsKey(name)) {
		  Map values = (HashMap) reqsHM.get(name);
			values.put(new Integer(values.size()), value);
		} else {
		  Map values = new HashMap();
			values.put(new Integer(0), value);
			reqsHM.put(name, values);
		}
	}

	
	/**
	 * 加入参数值，可以一次加入多个值
	 * @param name 参数名称
	 * @param values 值数组
	 */
	public void putParameter(String name, String[] values) {
		if (name == null){
			throw new IllegalArgumentException(
					"The name of an element cannot be null.");
		}
		Map valuesHM = null;
		if (reqsHM.containsKey(name)) {
			valuesHM = (Map) reqsHM.get(name);
		} else {
			valuesHM = new HashMap();
			reqsHM.put(name, valuesHM);
		}
		int len = values.length;
		for(int i=0; i<len; i++){
			valuesHM.put(new Integer(valuesHM.size()), values[i]);
		}
	}

	/**
	 * 返回参数值
	 * 
	 * @param name 参数名称
	 * 
	 * @return 如果不存在，返回null
	 */
	public String getParameter(String name) {
		if (name == null){
			throw new IllegalArgumentException(
					"The name of an element cannot be null.");
		}
		Map values = (HashMap) reqsHM.get(name);
		if (values == null){
			return null;
		}else{
			return (String) values.get(new Integer(0));
		}
	}
	
	
	
	/**
	 * 移除并返回指定名称的参数值
	 * 
	 * @param name 参数名称
	 * 
	 * @return 请求参数值,如果不存在，返回null
	 */
	public String removeParameter(String name) {
		if (name == null){
			throw new IllegalArgumentException(
					"The name of an element cannot be null.");
		}
		Map values = (HashMap)reqsHM.remove(name);
		if (values == null){
			return null;
		}else{
			return (String) values.get(new Integer(0));
		}
	}

	/**
	 * 以数组方式返回请求数据
	 * 
	 * @param name 请求关键字段名称
	 * 
	 * @return 请求参数值数组，如果参数不存在,返回null
	 */
	public String[] getParameterValues(String name) {
		if (name == null){
			throw new IllegalArgumentException(
					"The name of an element cannot be null.");
		}
		Map values = (HashMap) reqsHM.get(name);
		if (values == null){
			return null;
		}
//		return (String[])values.values().toArray(new String[values.size()]);
		String strValues[] = new String[values.size()];
		for (int i = 0; i < values.size(); i++){
			strValues[i] = (String) values.get(new Integer(i));
		}
		return strValues;
	}
	
	
	/**
	 * 枚举所有参数字段名称
	 * 
	 * @return 通过java.util.Enumeration返回所有资源名称
	 */
	public Enumeration getParameterNames() {
		return EnumerationHelper.toEnumerator(reqsHM.keySet().iterator());
	}


	
}
