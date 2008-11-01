/*
 * SQLExecutorFactory.java	2005-7-1
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
 * SQLExecutor工厂, 此工厂的具体实现必须提供以JDBCTransaction和connId为参数的构造方法。
 * <pre>
 * 	public XxxxSQLExecutorFactory(JDBCTransaction transaction, String connId){
 *		
 *	}
 * </pre>
 * 
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2005-07-01  创建此类型
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 */

public interface SQLExecutorFactory {
	
	public SQLExecutor create();
	
}
