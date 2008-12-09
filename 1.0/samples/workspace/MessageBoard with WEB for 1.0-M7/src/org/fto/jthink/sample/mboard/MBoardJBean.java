/*
 * MBoardJBean.java	2005-10-14
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
package org.fto.jthink.sample.mboard;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.fto.jthink.exception.JThinkRuntimeException;
import org.fto.jthink.j2ee.web.HttpRequest;
import org.fto.jthink.jdbc.Column;
import org.fto.jthink.jdbc.Condition;
import org.fto.jthink.jdbc.ConditionItem;
import org.fto.jthink.jdbc.DataObject;
import org.fto.jthink.jdbc.DefaultDataObject;
import org.fto.jthink.jdbc.JDBCTransaction;
import org.fto.jthink.jdbc.SQL;
import org.fto.jthink.jdbc.SQLBuilder;
import org.fto.jthink.jdbc.SQLExecutor;
import org.fto.jthink.jdbc.SQLExecutorEvent;
import org.fto.jthink.jdbc.SQLExecutorListener;
import org.fto.jthink.log.LogManager;
import org.fto.jthink.log.Logger;
import org.fto.jthink.resource.ResourceManager;
import org.fto.jthink.sample.mboard.po.Message;
import org.fto.jthink.transaction.TransactionFactory;
import org.fto.jthink.util.DateTimeHelper;
import org.jdom.Element;


/**
 * 留言板，这个程序演示了对数据库的增、删、改、查等基本操作，以及如何从资源管理器中返回需要的资源，SQLExecutor监听器，
 * 构建SQL语句，执行SQL语句，事务处理，日志处理等以及在1.0-M7中加入的DataObject的结果集处理方式。
 * 
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink Sample Message Board 1.0
 * @see 	  JThink 1.0
 * 
 */
public class MBoardJBean {
	/* 返回日志处理接口 */
	private static final Logger logger = LogManager.getLogger(MBoardJBean.class);

	/* 资源管理器 */
	private ResourceManager resManager;
	/* JThink中定义的Http请求 */
	private HttpRequest request;
	/* JDBC事务 */
	private JDBCTransaction transaction;
	/* 用于执行SQL语句 */
	private SQLExecutor sqlExecutor;
	/* 用于构建SQL语句 */
	private SQLBuilder sqlBuilder;
	/* 在fto-jthink.xml中定义的数据源连接ID */
	private static final String connId = "SampleDataSource";
	
	public MBoardJBean(){}
	
	/**
	 * 构造方法
	 * @param req HttpServletRequest请求
	 * @throws Exception
	 */
	public MBoardJBean(HttpServletRequest req) throws Exception{
		initialize(req);
	}
	
	/**
	 * 初始化JavaBean
	 * @param req
	 * @throws Exception
	 */
	public void initialize(HttpServletRequest req) throws Exception {
		/* 创建资源管理器 */
		resManager = new ResourceManager(req);
		/* 返回客户请求 */
		request = (HttpRequest)resManager.getResource(HttpRequest.class.getName());
		/* 返回定义的JDBC事务工厂，并创建事务 */
		transaction = (JDBCTransaction)((TransactionFactory)resManager.getResource("WebTransaction")).create();
		/* 创建SQLExecutor */
		sqlExecutor = transaction.getSQLExecutorFactory(connId).create();
		/* 设置SQLExecutor监听器 */
		sqlExecutor.addSQLExecutorListener(new SQLExecutorListener(){
			/* 监听器的事件方法，当在执行SQL语句时调用此方法 */
			public void executeSQLCommand(SQLExecutorEvent evt) {
				logger.debug(evt.getSQL().getSQLString());
			}
		});
		/* 创建SQLBuilder */
		sqlBuilder = transaction.getSQLBuilderFactory(connId).create("");
	}
	

	
	/**
	 * 返回留言信息, 分页，每页10行
	 */
	public List searchMessages() throws Exception{
		try{
			transaction.begin();
			
			/* 构建SQL语句 */
			SQL sql = sqlBuilder.constructSQLForSelect("Messages", false, null, null, "", "SendTime desc", (getPageOffset()-1)*getPageRows(), getPageRows()); 
			/* 执行SQL语句,并返回结果 */
			return (List)sqlExecutor.execute(sql, Message.class);
			
		}finally{
			transaction.close();
		}
	}
	

	
	/**
	 * 生成ID
	 */
	private Integer generateID(){
		List maxIdLT = (List)sqlExecutor.executeQuery("SELECT MAX(ID) AS MAX_ID FROM Messages");
		DataObject maxIdDO = (DataObject)maxIdLT.iterator().next();
		if(maxIdDO.get("MAX_ID")==null){
			return new Integer(1);
		}
		return new Integer(Integer.parseInt(maxIdDO.get("MAX_ID"))+1);
	}
	
	/**
	 * 发送留言信息
	 */
	public void sendMessage() throws Exception{
		try{
			/* 开始事务 */
			transaction.begin(); 
			
			HashMap msgsHM = new HashMap();
			msgsHM.put("ID",generateID());
			putToHashMap(msgsHM, "Subject", request.getParameter("Subject"));
			putToHashMap(msgsHM, "Content", request.getParameter("Content"));
			putToHashMap(msgsHM, "Sender", request.getParameter("Sender"));
			putToHashMap(msgsHM, "Email", request.getParameter("Email"));
			putToHashMap(msgsHM, "Contact", request.getParameter("Contact"));
			
			msgsHM.put("IP", request.getServletRequest().getRemoteAddr());
			msgsHM.put("SendTime", DateTimeHelper.formatDateTimetoString(DateTimeHelper.getSystemDate()));
			
			SQL sql = sqlBuilder.constructSQLForInsert("Messages", msgsHM);
			
			/* 执行SQL语句 */
			sqlExecutor.execute(sql);
			
			/* 提交事务 */
			transaction.commit();
		}catch(Exception e){
			/* 回退事务 */
			transaction.rollback();
			throw e;
		}finally{
			/* 关闭事务 */
			transaction.close();
		}
	}	

	
	/**
	 * 返回回复信息
	 * @throws Exception
	 */
	public Message getRevertInfo() throws Exception{
		try{
			transaction.begin();
			
			/* 返回的列 */
			Column[] column = {
				new Column("RevertContent"),
				new Column("Reverter"),
			};
			
			/* 条件 */
			Condition condition = new Condition();
			condition.add(new ConditionItem("ID","=", request.getParameter("ID")));
			
			/* 构建SQL语句 */
			SQL sql = sqlBuilder.constructSQLForSelect("Messages", column, condition); 
			/* 执行SQL语句,并返回结果 */
			Iterator it = ((List)sqlExecutor.execute(sql, Message.class)).iterator();
			
			if(it.hasNext()){
				return (Message)it.next();
			}else{
				throw new Exception("留言信息不存在,可能已经被删除!");
			}
		}finally{
			transaction.close();
		}

	}	
	
	/**
	 * 回复留言信息
	 */
	public void revertMessage() throws Exception{
		
		
		try{
			/* 开始事务 */
			transaction.begin(); 
			
			HashMap msgsHM = new HashMap();
			
			String revertContent = request.getParameter("RevertContent");
			if(revertContent!=null){
				msgsHM.put("RevertContent",revertContent);
			}else{
				msgsHM.put("RevertContent","");
			}
			
			String reverter = request.getParameter("Reverter");
			if(reverter!=null){
				msgsHM.put("Reverter",reverter);
			}else{
				msgsHM.put("Reverter","");
			}
			
			msgsHM.put("RevertTime", DateTimeHelper.formatDateTimetoString(DateTimeHelper.getSystemDate()));

			Condition condition = new Condition();
			condition.add(new ConditionItem("ID","=", request.getParameter("ID")));


			SQL sql = sqlBuilder.constructSQLForUpdate("Messages", msgsHM, condition);
			
			/* 执行SQL语句 */
			sqlExecutor.execute(sql);
			
			/* 提交事务 */
			transaction.commit();
		}catch(JThinkRuntimeException e){
			e.getCause();
		}catch(Exception e){
			/* 回退事务 */
			transaction.rollback();
			throw e;
		}finally{
			/* 关闭事务 */
			transaction.close();
		}
	}	
	/**
	 * 删除留言信息
	 */
	public void deleteMessage() throws Exception{
		
		
		try{
			/* 开始事务 */
			transaction.begin(); 
			
			Condition condition = new Condition();
			condition.add(new ConditionItem("ID","=", request.getParameter("ID")));
			
			SQL sql = sqlBuilder.constructSQLForDelete("Messages", condition);
			
			/* 执行SQL语句 */
			sqlExecutor.execute(sql);
			
			/* 提交事务 */
			transaction.commit();
		}catch(Exception e){
			/* 回退事务 */
			transaction.rollback();
			throw e;
		}finally{
			/* 关闭事务 */
			transaction.close();
		}
	}	

	/**
	 * 返回留言的总行数
	 */
	public int getPageTotalRows(){
		if(request.getParameter("PAGE_ROW_TOTAL")!=null){
			return Integer.parseInt(request.getParameter("PAGE_ROW_TOTAL"));
		}
		try{
			transaction.begin();
			
			/* 构建SQL语句 */
			SQL sql = sqlBuilder.constructSQLForCount("Messages", "*", "ROW_COUNT", null); 
			
			DataObject countDO = (DataObject)((List)sqlExecutor.execute(sql)).iterator().next();
			
			return Integer.parseInt(countDO.get("ROW_COUNT"));
			
		}finally{
			transaction.close();
		}
	}
	
	/**
	 * 返回当前页偏移
	 */
	public int getPageOffset(){
		return request.getParameter("PAGE_OFFSET")!=null?Integer.parseInt(request.getParameter("PAGE_OFFSET")):1;
	}
	
	/**
	 * 返回每页显示的记录行数
	 */
	public int getPageRows(){
		return 10;
	}
	
	/**
	 * 返回总页数
	 */
	public int getPages(int pageTotalRows){
		return pageTotalRows/getPageRows() + ((pageTotalRows%getPageRows())>0?1:0);
	}
	
	/**
	 * 返回记录序号
	 */
	public int getSeqNo(int i){
		return (getPageOffset()-1)*getPageRows()+i+1;
	}
	
	/**
	 * 向HashMap中压入对象，如果value等于null, 将被忽略
	 */
	private void putToHashMap(HashMap hm, String name, Object value){
		if(value!=null){
			hm.put(name, value);
		}
	}
}
