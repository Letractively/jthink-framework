/*
 * DefaultSQLBuilderFactory.java	2005-7-16
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
 * 默认SQLBuilder工厂。
 * 
 * 
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

public class DefaultSQLBuilderFactory implements SQLBuilderFactory {

	/**
	 * 创建默认SQLBuilder
	 * 
	 * @param sqlBuilderId 此ID值在当前工厂无意义
	 */
	public SQLBuilder create(String sqlBuilderId) {
		return new SQLBuilder();
	}

}
