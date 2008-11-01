
/*
 * DefaultJDBCTransactionFactory.java	2005-7-16
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

import org.fto.jthink.config.Configuration;
import org.fto.jthink.resource.ResourceManager;
import org.fto.jthink.transaction.Transaction;
import org.fto.jthink.transaction.TransactionFactory;
import org.jdom.Element;

/**
 * 默认JDBC事务工厂, 负责创建DefaultJDBCTransaction类型的实例。
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

public class DefaultJDBCTransactionFactory implements TransactionFactory {

	private ResourceManager resManager;
	private Element dataSourceConfigEL;
	private Element trsctnConfig;
	
	/**
	 * 创建DefaultJDBCTransactionFactory的实例
	 * 
	 * @param resManager 资源管理器
	 * @param config 系统配置信息
	 */
	public 	DefaultJDBCTransactionFactory(ResourceManager resManager, Configuration config, Element trsctnConfig){
		this.resManager = resManager;
		this.dataSourceConfigEL = config.getDataSourceConfig();
		this.trsctnConfig = trsctnConfig;
	}
	
	/**
	 * 创建并返回事务对象
	 * 
	 * @return Transaction的具体实现
	 */
	public Transaction create() {
		return new DefaultJDBCTransaction(resManager, dataSourceConfigEL, trsctnConfig);
	}

}
