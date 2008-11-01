/*
 * EnumerationHelper.java	2005-7-4
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
 * java.util.Iterator类型助手。将其它容器类型对象转换为java.util.Iterator类型。
 * 
 * 
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2005-07-04  创建此类型
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 */
public final class IteratorHelper {

	private IteratorHelper(){}
	
	/**
	 * 返回空Iterator
	 * 
	 * @return Iterator
	 */
	public static Iterator emptyIterator() {
		return new Iterator() {

			public boolean hasNext() {
				return false;
			}

			public Object next() {
				throw new NoSuchElementException("Iterator");
			}

			public void remove() {
				throw new IllegalStateException("Iterator");
			}
		};
	}

	/**
	 * 将Enumeration转换为Iterator
	 * 
	 * @param e　Enumeration
	 * @return Iterator
	 */
	public static Iterator toIterator(Enumeration e){
		return new EnumerationToIterator(e);
	}
	
  private static class EnumerationToIterator implements Iterator {
  	private Enumeration e;
  	EnumerationToIterator(Enumeration e) {
  		this.e = e;
  	}

		public void remove() {
	    throw new java.lang.UnsupportedOperationException(
      "Method returnResource() not yet implemented.");
		}
	
		public boolean hasNext() {
			return e.hasMoreElements();
		}
	
		public Object next() {
			return e.nextElement();
		}

  }


  

	/**
	 * 将数组转换为Iterator
	 * 
	 * @param objs　Object[]
	 * @return  Iterator
	 */
	public static Iterator toIterator(Object[] objs){
		
		return new ArrayToIterator(objs);

	}
  private static class ArrayToIterator implements Iterator {
  	private Object[] objs;
  	private int length = 0;
  	private int currIndex = 0;
  	ArrayToIterator(Object[] objs) {
  		this.objs = objs;
  		length = objs.length;
  	}

  	public boolean hasNext() {
  	    return currIndex<length;
  	}

  	public Object next() {
  	    return objs[currIndex++];
  	}
  	
		public void remove() {
				throw new java.lang.UnsupportedOperationException(
						"Method returnResource() not yet implemented.");
		}  	
  }	
	
  
  
	/**
	 * 将Collection转换为Iterator
	 * 
	 * @param c　Collection
	 * @return Iterator
	 */
	public static Iterator toIterator(Collection c){
		return c.iterator();
	}
	

	
}
