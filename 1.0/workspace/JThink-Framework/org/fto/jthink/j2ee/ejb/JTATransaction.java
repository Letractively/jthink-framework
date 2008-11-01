/*
 * JTATransaction.java	2005-7-17
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

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.fto.jthink.exception.JThinkErrorCode;
import org.fto.jthink.exception.JThinkRuntimeException;
import org.fto.jthink.jdbc.ConnectionPool;
import org.fto.jthink.jdbc.DefaultJDBCTransaction;
import org.fto.jthink.resource.ResourceManager;
import org.jdom.Element;

/**
 * WEB容器端事务管理。用于Jboss, Websphere,
 *     Weblogic等企业级应用服务器的WEB容器端容器管理的事物.
 *
 */
/**
 * 封装了JTA(Java Transaction API)事务处理的类。类型JTATransaction本身并处理事务的具体实现，事务的具体由容器去处理。
 * JTATransaction相当于只是一个事务代理，并实现了在JThink中定义的事务处理接口org.fto.jthink.jdbc.JDBCTransaction，
 * 方便于在不同事务管理方式下的动态切换。所有的J2EE/EJB容器都支持JTA事务，新版本的Tomcat也已经支持JTA事务。
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
 * @see javax.transaction.UserTransaction
 * @see org.fto.jthink.jdbc.DefaultJDBCTransaction
 * @see org.fto.jthink.jdbc.JDBCTransaction
 * @see org.fto.jthink.transaction.Transaction
 * @see org.fto.jthink.resource.ResourceContainer
 * 
 */

public class JTATransaction extends DefaultJDBCTransaction {

	/* JTA中定义的用户事务接口 */
	private UserTransaction ut = null;
	
  /* 事务嵌套标志 */
  private int recursionFlag = 0;

  /** 事务超时时间,单位：秒,默认为-1，表示采用容器默认值 */
  protected int timeout=-1;
  
	/**
	 * 创建类型JTATransaction的实例
	 * 
	 * @param resManager  资源管理器ResourceManager的实例
	 * @param dataSourceConfig 数据源配置信息
	 */

	public JTATransaction(ResourceManager resManager, Element dataSourceConfig, Element trsctnConfig) {
		super(resManager, dataSourceConfig, trsctnConfig);
		Element config = trsctnConfig.getChild("config");
		if(config!=null){
		  String timeout = config.getChildTextTrim("timeout");
		  if(timeout!=null){
		    setTransactionTimeout(Integer.parseInt(timeout));
		  }
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
	 * 设置是否自动提交方式, JTA方式管理事务, 此方法无意义
	 */
	public void setAutoCommit(boolean autoCommit){

	}

	/**
	 * 返回事务提交方式, JTA方式管理事务, 此方法无意义
	 * 
	 * @return 事务提交方式
	 */
	public boolean getAutoCommit(){
		return false;
	}
	
	
  /**
   * 设置事务超时时间，单位：秒
   */
  public void setTransactionTimeout(int seconds){
    if(seconds<0){
      throw new JThinkRuntimeException(JThinkErrorCode.ERRCODE_SYS_RUNTIME, "不被支持的值，只允许大于等于0的值!");
    }
    this.timeout = seconds;
    if(ut!=null && seconds!=-1){
      try {
        ut.setTransactionTimeout(seconds);
      } catch (SystemException e) {
        throw new JThinkRuntimeException(
            JThinkErrorCode.ERRCODE_SYS_RUNTIME,
            "设置事务超时时间!", e);
      }
    }
  }	
	
	/**
	 * 事务开始。采用JTA管理事务，实际上是调用javax.transaction.UserTransaction的begin()方法
	 * 
	 * @see javax.transaction.UserTransaction#begin()
	 */
	public void begin() {
		Context ctx = null;
		try {
			recursionFlag++;
			if (recursionFlag == 1) {
					ctx = new InitialContext();
					ut = (UserTransaction) ctx
							.lookup("java:comp/UserTransaction");
					if(timeout>=0){
					  ut.setTransactionTimeout(timeout);
					}
					ut.begin();
			}

		} catch (Exception e) {
			throw new JThinkRuntimeException(
					JThinkErrorCode.ERRCODE_SYS_BEGIN_TRANSACTION_FAILURE,
					"Not begin transaction!", e);
		} finally {
			if (ctx != null) {
				try {
					ctx.close();
				} catch (NamingException ne) {
					throw new JThinkRuntimeException(
							JThinkErrorCode.ERRCODE_SYS_BEGIN_TRANSACTION_FAILURE,
							"关闭Context对象失败!", ne);
				}
			}
		}
	}

	/**
	 * 提交事务。采用JTA管理事务，实际上是调用javax.transaction.UserTransaction的commit()方法
	 * 
	 * @see javax.transaction.UserTransaction#commit()
	 */
	public void commit() {
    if (recursionFlag > 1) {
    	return;
    }		
    try {
      /* Transaction committed */
    	if(ut!=null){
	      ut.commit();
	      ut = null;
    	}
    } catch (Exception e) {
        throw new JThinkRuntimeException(
            JThinkErrorCode.ERRCODE_SYS_COMMIT_TRANSACTION_FAILURE,
            "Commit transaction failure", e);
    }
	}

	/**
	 * 回退事务。采用JTA管理事务，实际上是调用javax.transaction.UserTransaction的rollback()方法
	 * 
	 * @see javax.transaction.UserTransaction#commit()
	 */
	public void rollback() {
    if (recursionFlag > 1) {
    	return;
    }		
    try {
      /* Transaction rollbacked */
    	if(ut!=null){
	      ut.rollback();
	      ut = null;
    	}
    } catch (Exception e) {
        throw new JThinkRuntimeException(
            JThinkErrorCode.ERRCODE_SYS_ROLLBACK_TRANSACTION_FAILURE,
            "Rollback transaction failure", e);
    }
	}

	/**
	 * 关闭事务, 通常的事务管理接口里没有关闭事务的操作，但在JThink框架的事务管理接口中定义了此方法,
	 * 方便在事务结束时做一些扫尾工作，比如关闭数据库连接或将连接返回到连接池，
	 * 这样在应该系统的开发过程中，我们就不用再关心数据库连接的创建和关闭等繁杂并容易引起程序出错的问题
	 */
	public void close() {
    /* Transaction flag -1 */
		recursionFlag--;
    if (recursionFlag > 0) {
      return;
    }
    else if (recursionFlag < 0) {
      throw new JThinkRuntimeException(JThinkErrorCode.ERRCODE_SYS_RUNTIME,
      						"事务嵌套标志小于了0. begin(),close()方法不匹配!");
    }
    
    try{
    	if(ut!=null){
    		ut.rollback();
    		ut=null;
    	}
    	Iterator keysIT = openedConnsHM.keySet().iterator();
	    while(keysIT.hasNext()){
	    	String connId = (String)keysIT.next();
	    	Connection conn = (Connection)openedConnsHM.get(connId);
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
