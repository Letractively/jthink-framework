/*
 * ComparatorHelper.java	2005-7-17
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

import java.util.Arrays;

/**
 * 对象比较助手。
 * 
 * 
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2005-07-17  创建此类型
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 */

public final class ComparatorHelper {

	private ComparatorHelper() {}
	
	/**
	 * 对象比较
	 * 
	 * @param x 对象x
	 * @param y 对象y
	 * 
	 * @return 如果对象相等,返回true,否则,返回false
	 * 
	 */
	public static boolean equals(Object x, Object y) {
		return x==y || ( x!=null && y!=null && x.equals(y) );
	}

	/**
	 * 数组对象比较
	 * 
	 * @param xs
	 * @param ys
	 * 
	 * @return  
	 * <pre>
	 *  xs,ys 是同一数组对象,返回true
	 *  xs,ys 长度不相等,返回false
	 *  xs,ys 长度相等, 但其中有一组值不相等, 返回false
	 *  xs,ys 长度相等, 且所有的数组元素都相等,返回true，否则返回false
	 * </pre>
	 * 
	 */
	public static boolean equals(Object[] xs, Object[] ys){
		return Arrays.equals(xs, ys);
//		if(xs==ys){
//			return true;
//		}
//		if(xs==null || ys==null){
//			return false;
//		}
//		int xslen = xs.length;
//		int yslen = ys.length;
//		if(xslen != yslen){
//			return false;
//		}
//		for(int i=0;i<xslen;i++){
//			if(equals(xs[i], ys[i])==false){
//				return false;
//			}
//		}
//		return true;
	}
	
}
