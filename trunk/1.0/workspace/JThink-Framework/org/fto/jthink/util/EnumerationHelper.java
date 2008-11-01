/*
 * EnumerationHelper.java	2005-7-3
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

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * java.util.Enumeration类型助手。将其它容器类型对象转换为java.util.Enumeration类型。
 * 
 * 
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2005-07-03  创建此类型
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 */
public final class EnumerationHelper {
	
	private EnumerationHelper(){}
	
	/**
	 * 返回空Enumeration枚举对象
	 * 
	 * @return Enumeration类型对象
	 */
	public static Enumeration emptyEnumerator(){
		return new Enumeration(){
			
	  	public boolean hasMoreElements() {
  	    return false;
  		}

	  	public Object nextElement() {
	  	    throw new NoSuchElementException("Enumerator");
	  	}
		};
	}

	/**
	 * 将Iterator转换为Enumeration
	 * 
	 * @param it　Iterator类型参数
	 * @return Enumeration类型对象
	 */
	public static Enumeration toEnumerator(Iterator it){
		return new IteratorToEnumerator(it);
	}
	
	
	/**
	 * 将Collection转换为Enumeration
	 * 
	 * @param c Collection类型参数
	 * @return Enumeration类型对象
	 */
	public static Enumeration toEnumerator(Collection c){
		return toEnumerator(c.iterator());
	}


	/**
	 * 将数组转换为Enumeration
	 * 
	 * @param objs Object[] 对象数组
	 * @return Enumeration类型对象
	 */
	public static Enumeration toEnumerator(Object[] objs){
		return new ArrayToEnumerator(objs);
	}
	
	/**
	 * 将数组转换到Enumerator类型
	 */
  private static class ArrayToEnumerator implements Enumeration {
  	private Object[] objs;
  	private int length = 0;
  	private int currIndex = 0;
  	ArrayToEnumerator(Object[] objs) {
  		this.objs = objs;
  		length = objs.length;
  	}

  	public boolean hasMoreElements() {
  	    return currIndex<length;
  	}

  	public Object nextElement() {
  	    return objs[currIndex++];
  	}
  }

	/**
	 * Iterator类型转换到Enumerator类型
	 */
  private static class IteratorToEnumerator implements Enumeration {
  	private Iterator it;
  	IteratorToEnumerator(Iterator it) {
  		this.it = it;
  	}

  	public boolean hasMoreElements() {
  	    return it.hasNext();
  	}

  	public Object nextElement() {
  	    return it.next();
  	}
  }  
  

}
