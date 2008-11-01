/*
 * PackageResourceReader.java	2005-7-13
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
package org.fto.jthink.util;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import org.fto.jthink.exception.JThinkRuntimeException;

/**
 * 匹配查找给定类所在库或类目录中的资源, 
 * 注意:只有当前给定的类所在的库或类目录中的资源才能被找到。
 * 
 * 
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2005-07-13  创建此类型
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 */

public class PackageResourceReader {

	private Class clazz;
	
	/**
	 * 构造方法, 创建一个ThisPackageResourceReader实例
	 * 
	 * @param clazz 类实例, 将根据这个类实例所在的jar包或类目录来查找资源
	 */
	public PackageResourceReader(Class clazz){
		this.clazz = clazz;
	}

	/**
	 * 返回包中与name匹配的第一个资源, 可以从根开始匹配查找,也可以从类的当前位置开始匹配查找
	 * 
	 * <pre>
	 * 资源名称"name"的格式: [/]<包1>/<包2>/<包n>/<资源名>
	 *    /org/fto/jthink/util/PackageResourceReader.class
	 *     images/xxxx.gif 相对路径
	 *     /META-INF/fto-jfree.xml
	 * </pre>
	 * 
	 * <p>
	 * 资源类型为: java.net.URL
	 * </p>
	 * 
	 * @param name 资源名称, 如果是以"/"开始的资源名称, 从包的根开始匹配资源. 
	 * 							否则,将从当前类位置开始匹配资源. 
	 * 
	 * @return  资源的URL位置, 如果没找到,返回null
	 */
	public URL getResource(String name){
		if(name.startsWith("/")){
			name = name.substring(1);
			return getResourceFromRoot(name);
		}else{
			return clazz.getResource(name);
		}
	}

	/**
	 * 返回与name匹配的资源, 从包的根位置开始匹配.
	 * @param name 资源名称
	 * @return 所有匹配的资源
	 */
	private URL getResourceFromRoot(String name){
		try {
			ClassLoader clsLder = clazz.getClassLoader();
			if(clsLder!=null){
				String clsPath = getClassURL(clazz).getPath();
				String clsPkgPath = clazz.getPackage().getName().replace('.','/')+"/"+getClassName(clazz);
				String rootPath = StringHelper.replace(clsPath, clsPkgPath, "");
				Enumeration resURLs =
					Thread.currentThread().getContextClassLoader().getResources(name);
	      while(resURLs.hasMoreElements()){
	      	URL res = (URL)resURLs.nextElement();
	        if(res.getPath().indexOf(rootPath)>=0){
	        	return res;
	        }
	      }
	      return null;
			}else{
				return clazz.getResource("/"+name);
			}
		} catch (IOException e) {
			throw new JThinkRuntimeException(e);
		}
	}
	

	
	/**
	 * 返回给定类实例的实际URL位置
	 * @param cls 类实例
	 * @return 类的URL位置
	 */
	public static URL getClassURL(Class cls){
		URL url = cls.getResource(getClassName(cls));
		return url;
	}

	
	
	/**
	 * 返回类名称
	 * @param cls 类的实例
	 * @return 返回类全名
	 */
	private static String getClassName(Class cls){
		String clsName = cls.getName();
		return clsName.substring(clsName.lastIndexOf(".")+1)+".class";
	}		
	
}
