/*
 * ReflectHelper.java	2005-7-19
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

import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.fto.jthink.exception.JThinkRuntimeException;

/**
 * 此助手提供对类和实例操作的相关方法。创建类对象，创建实例等。
 * 
 * 
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2005-07-19  创建此类型
 * 2008-10-28  增加方法：invokeDeclared()
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 */
public final class ReflectHelper {
	
	private ReflectHelper(){}
	
	
	/**
	 * 根据类路径创建类对象
	 * @param name 类名称，包含类路径
	 * @return 类
	 */
	public static Class forName(String name){
		try {
			ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
			if(contextClassLoader!=null) {
				return contextClassLoader.loadClass(name);
			} else {
				return Class.forName(name);
			}
		}
		catch (Exception e) {
			try {
				return Class.forName(name);
			} catch (ClassNotFoundException cnfEx) {
				throw new JThinkRuntimeException(cnfEx);
			}
		}
	}

	/**
	 * 创建类的实例
	 * @param clazz 类
	 * @param types 构造方法参数类型
	 * @param initargs 构造方法参数实例
	 * @return 类的实例
	 */
	public static Object newInstance(Class clazz, Class[] types, Object[] initargs){
		try{
			Constructor cr = clazz.getConstructor(types);
			return cr.newInstance(initargs);
		}catch(Exception e){
			throw new JThinkRuntimeException(e);
		}
	}

	/**
	 * 创建类的实例
	 * @param clazz 类
	 * @return 类的实例
	 */
	public static Object newInstance(Class clazz){
		try{
			return clazz.newInstance();
		}catch(Exception e){
			throw new JThinkRuntimeException(e);
		}
	}



  /**
   * 方法调用,包括类或接口声明的以及从超类和超接口继承的那些的类或接口的公共成员方法
   * @param obj          对象
   * @param methodName   被调用的方法名
   * @param types        方法参数类型
   * @param args         方法参数值
   * @return             结果
   */
  public static Object invoke(Object obj, String methodName, Class[] types, Object[] args){

    try{
      Method method = obj.getClass().getMethod(methodName, types);
      method.setAccessible(true);
      return method.invoke(obj, args);
    }catch(Exception e){
      throw new JThinkRuntimeException(e);
    }
  }
  
  /**
   * 方法调用,包括类或接口声明的以及从超类和超接口继承的那些的类或接口的公共成员方法
   * @param obj         对象
   * @param methodName  被调用的方法名
   * @return 结果
   */
  public static Object invoke(Object obj, String methodName){
    return invoke(obj, methodName, null,null);
  }

  /**
   * 方法调用,包括对象所表示的类或接口的指定已声明方法,不包括超类和超接口继承的方法
   * @param obj          对象
   * @param methodName   被调用的方法名
   * @param types        方法参数类型
   * @param args         方法参数值
   * @return             结果
   */
  public static Object invokeDeclared(Object obj, String methodName, Class[] types, Object[] args){
    try{
      Method method = obj.getClass().getDeclaredMethod(methodName, types);
      method.setAccessible(true);
      return method.invoke(obj, args);
    }catch(Exception e){
      throw new JThinkRuntimeException(e);
    }
  }
  
  /**
   * 方法调用,包括对象所表示的类或接口的指定已声明方法,不包括超类和超接口继承的方法
   * @param obj          对象
   * @param methodName   被调用的方法名
   * @return             结果
   */
  public static Object invokeDeclared(Object obj, String methodName){
    return invokeDeclared(obj, methodName,null,null);
  }
 
	/**
	 * 判断是否Public方法或成员字段
	 * @param clazz 类
	 * @param member Member
	 */
	public static boolean isPublic(Class clazz, Member member) {
		return Modifier.isPublic( member.getModifiers() ) && Modifier.isPublic( clazz.getModifiers() );
	}

	/**
	 * 判断是否抽象类
	 * @param clazz 类
	 */
	public static boolean isAbstract(Class clazz) {
		int modifier = clazz.getModifiers();
		return Modifier.isAbstract(modifier);
	}

	/**
	 * 判断是否接口
	 * @param clazz 类
	 */
	public static boolean isInterface(Class clazz) {
		int modifier = clazz.getModifiers();
		return Modifier.isInterface(modifier);
	}

}
