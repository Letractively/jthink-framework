/*
 * MysqlSQLBuilderFactory.java	2008-10-28
 *
 * 版权所有: 自由思考组织(FTO)软件团队 2000-2008, 保留所有权利.
 * 这个软件是自由思考组织(FTO)软件团队开发的，如果要使用这个软件，请首先阅读并接受许可协议。
 *
 * Copyright 2000-2008 FTO Software Team, Inc. All Rights Reserved.
 * This software is the proprietary information of FTO Software Team, Inc.
 * Use is subject to license terms.
 *
 * FTO站点：http://www.free-think.org
 */
package org.fto.jthink.jdbc.hsql;

import org.fto.jthink.jdbc.SQLBuilder;
import org.fto.jthink.jdbc.SQLBuilderFactory;

/**
 * SQLBuilder工厂，创建针对HSQL数据库的SQLBuilder。
 * 
 * 
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2008-10-28  创建此类型
 * </pre></p>
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 */
public class HsqlSQLBuilderFactory implements SQLBuilderFactory {

	public SQLBuilder create(String sqlBuilderId) {
		return new HsqlSQLBuilder();
	}

}
