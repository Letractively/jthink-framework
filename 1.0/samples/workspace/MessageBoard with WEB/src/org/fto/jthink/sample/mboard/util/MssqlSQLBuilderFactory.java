/*
 * MssqlSQLBuilderFactory.java	2005-10-14
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
package org.fto.jthink.sample.mboard.util;

import org.fto.jthink.jdbc.SQLBuilder;
import org.fto.jthink.jdbc.SQLBuilderFactory;
import org.fto.jthink.jdbc.mssql.MssqlSQLBuilder;

/**
 * SQLBuilder工厂，创建针对MSSQL数据库的SQLBuilder。
 * 
 * 
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink Sample Message Board 1.0
 * @see 	  JThink 1.0
 * 
 */
public class MssqlSQLBuilderFactory implements SQLBuilderFactory {

	public SQLBuilder create(String sqlBuilderId) {
		return new MssqlSQLBuilder();
	}

}
