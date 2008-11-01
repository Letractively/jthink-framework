/*
 * EJBContainerTransaction.java	2005-7-18
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

package org.fto.jthink.j2ee.ejb;

import java.sql.Connection;
import java.util.Iterator;

import javax.ejb.SessionContext;

import org.fto.jthink.exception.JThinkErrorCode;
import org.fto.jthink.exception.JThinkRuntimeException;
import org.fto.jthink.jdbc.ConnectionPool;
import org.fto.jthink.jdbc.DefaultJDBCTransaction;
import org.fto.jthink.resource.ResourceManager;
import org.jdom.Element;

/**
 * 容器管理的EJB事务处理的类，虽然在EJB容器内采用了容器来管理事物，但对于有些非EJB规范中定义的异常，
 * EJB容器是不能对其进行正确处理的，这种情况下就要求在程序里手动调用javax.ejb.SessionContext的setRollbackOnly()方法。
 * 类型EJBContainerTransaction本身并不处理事务的具体实现，事务的具体由EJB容器去处理。
 * EJBContainerTransaction相当于只是一个事务代理，并实现了在JThink中定义的事务处理接口org.fto.jthink.jdbc.JDBCTransaction，
 * 方便于在不同事务管理方式下的动态切换。
 *
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2005-07-18  创建此类型
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 * @see org.fto.jthink.jdbc.DefaultJDBCTransaction
 * @see org.fto.jthink.jdbc.JDBCTransaction
 * @see org.fto.jthink.transaction.Transaction
 * @see org.fto.jthink.resource.ResourceContainer 
 */

public class EJBContainerTransaction extends DefaultJDBCTransaction {
	
	/* Sun的J2EE/EJB2.0规范中的会话上下文 */
	private javax.ejb.SessionContext sessionCtx;

  /* 事务嵌套标志，此标志用于事务的嵌套处理 */
  private int recursionFlag = 0;
  
	/**
	 * 创建类型EJBContainerTransaction的实例，在创建此实例之前，
	 * 必须将javax.ejb.SessionContext的实例对象以SessionContext.class.getName()返回的值
	 * 作为主键加入到资源管理器(ResourceManager)中
	 * 
	 * @param resManager  资源管理器ResourceManager的实例
	 * @param dataSourceConfig 数据源配置信息
	 */
	public EJBContainerTransaction(ResourceManager resManager, Element dataSourceConfig, Element trsctnConfig){
		super(resManager, dataSourceConfig, trsctnConfig);
		sessionCtx = (SessionContext)resManager.getResource(SessionContext.class.getName());
		if(sessionCtx==null){
			throw new JThinkRuntimeException(JThinkErrorCode.ERRCODE_SYS_RUNTIME, "javax.ejb.SessionContext没有找到, " +
							"请将SessionContext以SessionContext.class.getName()返回的值作为主键加入到资源管理器(ResourceManager)中.");
		}

	}

	/**
	 * 根据配置的数据库连接ID创建或返回数据库连接(Connection)。如果在同一线程中已经有一个连接在使用，
	 * 将直接将此连接返回；否则，如果采用了连接池管理，首先在连接池中查找连接，如果找到，返回连接，否则创建新连接，
	 * 并将此连接加入到连接池；否则如果没有采用连接池管理连接，将每次都直接创建新连接
	 * 
	 * @param connId 在配置文件中定义的数据库连接ID值
	 * 
	 * @return　返回根据connId创建或找到的可用数据库连接 
	 */
	public Connection getConnection(String connId)  {
		return initConnection(connId);
	}  

	/**
	 * 设置是否自动提交, EJB容器管理事务, 此方法无意义
	 * 
	 * @param autoCommit true或false，如果值为true,表示设置为自动事务提交方式
	 */
	public void setAutoCommit(boolean autoCommit){

	}

	/**
	 * 返回事务提交方式, EJB容器管理事务, 此方法无意义
	 * 
	 * @return 事务提交方式
	 */
	public boolean getAutoCommit(){
		return false;
	}
	
  /**
   * 事务开始，因为是EJB容器管理事务，所以此方法除了事务嵌套标志加1以外不做其它任何事情
   */
  public void begin() {
  	recursionFlag++;
  }

  /**
   * 事务提交, 在EJB容器管理事务的情况下，此方法不做任何事情
   */
  public void commit() {

  }

  /**
   * 事务回滚, 直接调用javax.ejb.SessionContext的setRollbackOnly()方法
   */
  public void rollback() {
    if (recursionFlag > 1) {
    	return;
    }		
  	sessionCtx.setRollbackOnly();
  }

	/**
	 * 关闭事务, 通常的事务管理接口里没有关闭事务的操作，但在JThink框架的事务管理接口中定义了此方法,
	 * 方便在事务结束时做一些扫尾工作，比如关闭数据库连接或将连接返回到连接池，
	 * 这样在应该系统的开发过程中，我们就不用再关心数据库连接的创建和关闭等繁杂并容易引起程序出错的问题
	 */
	public void close()  {
		recursionFlag--;
    if (recursionFlag > 0) {
      return;
    }
    else if (recursionFlag < 0) {
      throw new JThinkRuntimeException(JThinkErrorCode.ERRCODE_SYS_RUNTIME,
      						"事务嵌套标志小于了0. begin()与close()方法不匹配!");
    }
    
    try{
    	Iterator keysIT = openedConnsHM.keySet().iterator();
	    while(keysIT.hasNext()){
	    	String connId = (String)keysIT.next();
	    	Connection conn = (Connection)openedConnsHM.get(connId);
	    	/* 如果采用了连接池管理连接 */
	    	if(useConnectionPool(connId)){
	    		ConnectionPool connPool = ConnectionPool.getConnectionPool(connId);
	    		/* 将活动连接返回到连接池 */
	    		connPool.returnConnection(conn);
	    	}else{
		      /* 释放连接 */
		      conn.close();
	    	}
	    }
    }catch(JThinkRuntimeException e){
    	throw e;
    }catch(Exception e){
			throw new JThinkRuntimeException(JThinkErrorCode.ERRCODE_DB_SQL_EXCEPTION, "事务关闭时发生异常！", e);
		}finally{    
			openedConnsHM.clear();
		}
	}

  
}
