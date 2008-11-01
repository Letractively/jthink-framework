/*
 * SQLBuilderFactory.java	2005-7-15
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
package org.fto.jthink.jdbc;

/**
 * SQLBuilder工厂, 实现此接口来创建SQLBuilder。通常情况下，
 * 在具体应用中须要根据不同的数据库和应用要求，须要扩展SQLBuilder来构建复杂的SQL。
 * 并且，一个SQLBuilder工厂可以创建多个SQLBuilder，在实现在工厂时，
 * 可根据sqlBuilderId值来创建不同的SQLBuilder。
 * 此工厂接口的实现需要无参数的构造方法。
 * 
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2005-07-16  创建此类型
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 */

public interface SQLBuilderFactory {

	/**
	 * 创建SQLBuilder，如果sqlBuilderId对应的SQLBuilder不存在，必须抛出运行时异常
	 * 
	 * @param sqlBuilderId  用于区别SQLBuilder的ID值，根据此值创建多个SQLBuilder
	 * 
	 * @return SQLBuilder的实例或SQLBuilder的子类的实例
	 * 
	 * @throws org.fto.jthink.exception.JThinkRuntimeException 当sqlBuilderId对应的SQLBuilder不存在时，抛出此异常
	 */
	public SQLBuilder create(String sqlBuilderId);
	
}
