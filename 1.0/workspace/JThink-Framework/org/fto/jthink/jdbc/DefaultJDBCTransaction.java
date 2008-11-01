/*
 * DefaultJDBCTransaction.java	2005-6-23
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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.fto.jthink.exception.JThinkErrorCode;
import org.fto.jthink.exception.JThinkRuntimeException;
import org.fto.jthink.resource.ResourceManager;
import org.fto.jthink.util.EnumerationHelper;
import org.fto.jthink.util.ReflectHelper;
import org.jdom.Element;


/**
 * 默认JDBC事务管理的类，DefaultJDBCTransaction实际上是直接封装了数据库连接java.sql.Connection中的事务操作的方法。
 * DefaultJDBCTransaction没有处理分布事务问题，如果具体应用中要处理分布事务，需要重新实现org.fto.jthink.jdbc.JDBCTransaction接口, 
 * 并且还要使用XA类型的JDBC驱动。
 * 
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2005-06-23  创建此类型
 * 2008-8-20   在getSQLExecutorFactory()方法中加入
 *             setAttribute(connId, executor.getChild("config"));
 *             将SQLExecutor的配置信息加入到事务容器
 * 2008-10-24  加入事务隔离级别，在fto-jthink.xml中可配置
 * 2008-10-24  从连接池中返回连接时，如果没有找到连接，将创建新连接并返回，
 *              如果还没有返回有效连接，将尝试重复3次,以前只有一次
 * 2008-10-27  增加针对具体连接设置自动提交方式,在fto-jthink.xml中可配置
 * 2008-10-30  优化initConnection(String connId)方法，使之如果在连接池中找不到连接，将创建一个新连接返回
 * 
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 * @see org.fto.jthink.jdbc.JDBCTransaction
 * @see org.fto.jthink.transaction.Transaction
 * @see org.fto.jthink.resource.ResourceContainer
 */
public class DefaultJDBCTransaction implements JDBCTransaction {

	/* 资源管理器 */
	private ResourceManager resManager = null;
	
	/* 用于存储资源对象的HashMap */
	private Map attrsHM = new HashMap();
	
  /** 在配置文件中定义的所有连接信息 */
	protected Map connCfgsHM = new HashMap();
	
  /** 当前正在被使用的连接 */
	protected Map openedConnsHM = new HashMap();

  /** 事务隔离级别,只对已经打开的连接设置级别 */
  protected Map levels = new HashMap();

  /** 连接是否自动提交标记 */
  protected Map autoCommitFlags = new HashMap();

  
  
  /* 事务嵌套标志，此标志用于事务的嵌套处理 */
  private int recursionFlag = 0;

  /* 事务是否自动提交标志 */
  private boolean autoCommit = false;
  
  /**
   * 创建此类型的一个实例。在dataSourceConfig中必须正确配置数据库连接信息
   * 
   * @param resManager 资源管理器
   * @param dataSourceConfig  数据源配置信息
   */
  public DefaultJDBCTransaction(ResourceManager resManager, Element dataSourceConfig, Element trsctnConfig) {
   this.resManager = resManager;
    /* 将dataSourceConfig中定义的数据库连接配置信息压入到connCfgsHM中 */
    Iterator dataSourcesIT = dataSourceConfig.getChildren().iterator();
    while(dataSourcesIT.hasNext()){
      Element connEL = (Element)dataSourcesIT.next();
      String connId = connEL.getAttributeValue("id");
      connCfgsHM.put(connId, connEL);

      Element connFactory = connEL.getChild("connection-factory");
      String levelStr = connFactory.getChildTextTrim("isolation-level");
      if(levelStr!=null){
        setTransactionLevel(connId, parseTransactionLevel(levelStr));
      }
      
      String autoCommitFlagStr =connFactory.getChildTextTrim("auto-commit");
      if(autoCommitFlagStr!=null){
        autoCommitFlags.put(connId, new Boolean(autoCommitFlagStr));
      }

      
      
    }
   }

  private int parseTransactionLevel(String level){
    if(level.equalsIgnoreCase("TRANSACTION_NONE")){
      return TRANSACTION_NONE;
    }
    if(level.equalsIgnoreCase("TRANSACTION_READ_COMMITTED")){
      return TRANSACTION_READ_COMMITTED;
    }
    if(level.equalsIgnoreCase("TRANSACTION_READ_UNCOMMITTED")){
      return TRANSACTION_READ_UNCOMMITTED;
    }
    if(level.equalsIgnoreCase("TRANSACTION_REPEATABLE_READ")){
      return TRANSACTION_REPEATABLE_READ;
    }
    if(level.equalsIgnoreCase("TRANSACTION_SERIALIZABLE")){
      return TRANSACTION_SERIALIZABLE;
    }
    throw new JThinkRuntimeException(JThinkErrorCode.ERRCODE_DB_SQL_EXCEPTION, "不被支持的事务隔离级别！");
  }
  
  /**
   * 返回事务隔离级别，如果没有设置，返回-1(表示采用默认级别)
   * 如果返回-1，说明没有找到打开的连接
   */
  public int getTransactionLevel(String connId) {
    Integer level = (Integer)levels.get(connId);
    if(level==null){
      return -1;
    }else{
      return level.intValue();
    }
  }

  /**
   * 设置事务隔离级别
   */
  public void setTransactionLevel(String connId, int level) {
    if(!(level==TRANSACTION_NONE ||
        level==TRANSACTION_READ_COMMITTED ||
        level==TRANSACTION_READ_UNCOMMITTED ||
        level==TRANSACTION_REPEATABLE_READ ||
        level==TRANSACTION_SERIALIZABLE)){
      throw new JThinkRuntimeException(JThinkErrorCode.ERRCODE_DB_SQL_EXCEPTION, "不被支持的事务隔离级别！");      
    }
    levels.put(connId, new Integer(level));
    Connection conn = (Connection)openedConnsHM.get(connId);
    try{
      if(conn!=null && conn.getMetaData().supportsTransactionIsolationLevel(level)){
         conn.setTransactionIsolation(level);
      }
    } catch (SQLException e) {
      throw new JThinkRuntimeException(JThinkErrorCode.ERRCODE_DB_SQL_EXCEPTION, "设置事务隔离级别时发生异常！", e);
    }
  }
  
	/**
	 * 根据配置的数据库连接ID创建或返回一个数据库可用的连接(java.sql.Connection)。如果在同一线程中已经有一个连接在使用，
	 * 将直接将此连接返回；否则，如果采用了连接池管理，首先在连接池中查找连接，如果找到，返回连接，否则创建新连接，
	 * 并将此连接加入到连接池；否则如果没有采用连接池管理连接，将每次都直接创建新连接。
	 * 
	 * 
	 * @param connId 在配置文件中定义的数据库连接ID值
	 * 
	 * @return　返回根据connId创建或找到的可用数据库连接 
	 */
	public Connection getConnection(String connId){
		Connection conn = initConnection(connId);
//  	try {
//  	  conn.setAutoCommit(autoCommit);
//  	} catch (SQLException e) {
//  		throw new JThinkRuntimeException(JThinkErrorCode.ERRCODE_DB_CANNOT_GET_CONNECTION, e);
//  	}
  	return conn;
	}  
  
	/**
	 * 根据配置的数据库连接ID创建或返回一个数据库可用的连接(java.sql.Connection)。如果在同一线程中已经有一个连接在使用，
	 * 将直接将此连接返回；否则，如果采用了连接池管理，首先在连接池中查找连接，如果找到，返回连接，否则创建新连接，
	 * 并将此连接加入到连接池；否则如果没有采用连接池管理连接，将每次都直接创建新连接。
	 * 
	 * <pre>
	 * 如果采用连接池管理连接且连接池没有被创建，还得首先创建数据库连接池。初始化连接池要用到的参数有：
	 * min-pool-size  在初始化连接池时的最少连接数量
	 * max-pool-size  连接池允许的最大连接数量
	 * incremental-pool-size 连接池中连接的增量数量
	 * idle-timeout-minutes  连接池中连接的过期时间，单位为(分钟)
	 * check-connection-sql  用于检验连接是否可用的SQL语句
	 * </pre>
	 * 
	 * @param connId 数据库连接ID
	 * 
	 * @return　数据库连接
	 * 
	 */
	protected Connection initConnection(String connId)  {
		if (connId == null){
			throw new IllegalArgumentException(
					"The id of an connection cannot be null.");
		}
		
		try{
			/* 从当前打开的连接集中返回一个连接 */
	    Connection conn = (Connection)openedConnsHM.get(connId);
	    if(conn==null){
	    	/* 如果连接不存在 */
	    	Element connCfgEL = (Element)connCfgsHM.get(connId);
	    	Element poolEL = connCfgEL.getChild("connection-pool");
	    	String usePool = poolEL.getChildText("use-pool");
	    	if(usePool.equalsIgnoreCase("yes")){
	    		ConnectionPool connPool = ConnectionPool.getConnectionPool(connId);
	    		/* 如果使用连接池 */
	    		if(connPool==null){
		    			/* 如果连接池不存在，建立并初始化连接池 */
		    			int minPoolSize = Integer.parseInt(poolEL.getChildTextTrim("min-pool-size"));
		    			minPoolSize = minPoolSize<=0?1:minPoolSize;	    			
	
		    			int maxPoolSize = Integer.parseInt(poolEL.getChildTextTrim("max-pool-size"));
		    			maxPoolSize = maxPoolSize<=0?0:maxPoolSize;	  
		    			
		    			int incrementalPoolSize = Integer.parseInt(poolEL.getChildTextTrim("incremental-pool-size"));
		    			incrementalPoolSize = incrementalPoolSize<=0?1:incrementalPoolSize;	  
		    				    			
		    			int idleTimeoutMinutes = Integer.parseInt(poolEL.getChildTextTrim("idle-timeout-minutes"));
		    			idleTimeoutMinutes = idleTimeoutMinutes<=0?0:idleTimeoutMinutes;	  
		
	    				String checkConnectionSQL = poolEL.getChildTextTrim("check-connection-sql");
	
	    				connPool = ConnectionPool.createConnectionPool(connId, minPoolSize, maxPoolSize, incrementalPoolSize, idleTimeoutMinutes, checkConnectionSQL);
		    			
			    		/* 初始化连接池中的连接 */
		    			createConnection(connPool, connId, minPoolSize); 
	    		}
	    		/* 从连接池中查找连接 */
	    		conn = connPool.getConnection();
	    		for(int i=0;i<3 && conn==null;i++){
	    			/* 如果连接不存在, 创建新连接, 重复尝试三次 */
	    			createConnection(connPool, connId, connPool.getIncrementalPoolSize());
	    			conn = connPool.getConnection();
	    		}
//	    	}else{
//	    		/* 不使用连接池，创建一个新连接 */
//	    		conn = createConnection(connCfgEL);
	    	}
	    	if(conn==null){
          /* 真接创建一个新连接 */
          conn = createConnection(connCfgEL);
	    	  //throw new JThinkRuntimeException(JThinkErrorCode.ERRCODE_DB_CANNOT_GET_CONNECTION, "不能创建连接! Connection is "+connId);
	    	}
	    	if(levels.containsKey(connId)){
	    	  int level = ((Integer)levels.get(connId)).intValue();
  	    	if(conn.getMetaData().supportsTransactionIsolationLevel(level) && conn.getTransactionIsolation()!=level){
  	    	  /* 设置事务隔离级别 */
  	    	  conn.setTransactionIsolation(level);
  	    	}
	    	}
	    	if(autoCommitFlags.containsKey(connId)){
	    	  boolean flag = ((Boolean)autoCommitFlags.get(connId)).booleanValue();
	    	  if(conn.getAutoCommit()!=flag){
	    	    conn.setAutoCommit(flag);
	    	  }
	    	}else{
	    	  conn.setAutoCommit(false);
	    	}
	      openedConnsHM.put(connId, conn);
	    }
	    
	    /* 如果连接无效，抛异常 */
    	if(conn.isClosed()){
    		throw new JThinkRuntimeException(JThinkErrorCode.ERRCODE_DB_CANNOT_GET_CONNECTION, "此连接已经被关闭! Connection is "+connId);
    	}
	    return conn;
	    
		}catch(JThinkRuntimeException e){	
			throw e;
		}catch(Exception e){
			throw new JThinkRuntimeException(JThinkErrorCode.ERRCODE_DB_CANNOT_GET_CONNECTION, e);
		}
	}  

	/**
	 * 创建连接到连接池
	 * 
	 * @param connPool 连接池
	 * @param connId 连接ID
	 * @param connCount 将要被创建的连接数量
	 */
	private void createConnection(ConnectionPool connPool, String connId, int connCount){
		Element connCfgEL = (Element)connCfgsHM.get(connId);
		for(int i=0;i<connCount;i++){
			Connection conn = createConnection(connCfgEL);
			boolean status = connPool.setConnection(conn);
			if(status==false){
			  return;
			}
		}
	}
	
	/**
	 * 创建新连接
	 * 
	 * @param connId 连接ID
	 * @param connCfgEL 
	 * @return
	 * @throws Exception
	 */
	private Connection createConnection(Element connCfgEL){
		
		/* 返回ConnectionFactory实现类的描述信息 */
  	String factoryClass = connCfgEL.getChild("connection-factory").getChildText("factory-class");
  	
  	/* 实例化连接工厂 */
  	Class[] parameterTypes = new Class[]{Element.class};
  	Object[] parameters = new Object[]{connCfgEL};
  	ConnectionFactory factory = 
  		(ConnectionFactory)ReflectHelper.newInstance(
  				ReflectHelper.forName(factoryClass), parameterTypes, parameters);
					
  	/* 由连接工厂建立并返回新连接 */
  	return factory.create();
	}

	
	/**
	 * 返回SQLExecutorFactory工厂的具体实现, 此工厂是在配置文件中与连接ID相关的配置项中定义的
	 * 
	 * @param connId 连接ID
	 * 
	 * @return SQLExecutorFactory工厂的具体实现
	 */
	public SQLExecutorFactory getSQLExecutorFactory(String connId){
		/* 返回SQLExecutorFactory工厂信息 */
  	Element executor = ((Element)connCfgsHM.get(connId)).getChild("sql-executor");

    /* 将SQLExecutor的配置信息加入到事务容器中 */
    setAttribute(connId, executor.getChild("config"));
    
    /* 返回SQLExecutorFactory实现类的描述信息 */
  	String factoryClass = executor.getChildText("factory-class");
  	/* 实例化并返回SQLExecutorFactory工厂的实现 */
  	Class factoryCls = ReflectHelper.forName(factoryClass);
  	return (SQLExecutorFactory)ReflectHelper.newInstance(factoryCls, 
  			new Class[]{JDBCTransaction.class, String.class}, 
				new Object[]{this, connId}
  			);
	}
	
	/**
	 * 返回SQLBuilderFactory工厂的具体实现, 此工厂是在配置文件中与连接ID相关的配置项中定义的
	 * 
	 * @param connId 连接ID
	 * 
	 * @return SQLBuilderFactory工厂的具体实现
	 */
	public SQLBuilderFactory getSQLBuilderFactory(String connId){
		/* 返回SQLBuilderFactory实现类的描述信息 */
  	Element connCfgEL = (Element)connCfgsHM.get(connId);
  	
  	String factoryClass = connCfgEL.getChild("sql-builder").getChildText("factory-class");
  	
  	/* 实例化并返回SQLBuilderFactory工厂的实现 */
  	Class factoryCls = ReflectHelper.forName(factoryClass);
  	return (SQLBuilderFactory)ReflectHelper.newInstance(factoryCls);
	}
	
	
	/**
	 * 加入资源对象
	 * 
	 * @param name 资源名称
	 * @param resource 资源对象
	 * 
	 * @see org.fto.jthink.resource.ResourceContainer#setAttribute(java.lang.String, java.lang.Object)
	 */
	public void setAttribute(String name, java.lang.Object resource) {
		if (name == null){
			throw new IllegalArgumentException(
					"The name of an element cannot be null.");
		}
		attrsHM.put(name, resource);
		
	}

	/**
	 * 返回资源对象
	 * 
	 * @param name 资源名称
	 * 
	 * @return 资源对象
	 * 
	 * @see org.fto.jthink.resource.ResourceContainer#getAttribute(java.lang.String)
	 */
	public java.lang.Object getAttribute(String name) {
		if (name == null){
			throw new IllegalArgumentException(
					"The name of an element cannot be null.");
		}
		return attrsHM.get(name);
	}

	/**
	 * 删除并返回资源对象
	 * 
	 * @param name 资源名称
	 * 
	 * @return 资源对象
	 * 
	 * @see org.fto.jthink.resource.ResourceContainer#removeAttribute(java.lang.String)
	 */
	public Object removeAttribute(String name) {
		if (name == null){
			throw new IllegalArgumentException(
					"The name of an element cannot be null.");
		}
		return attrsHM.remove(name);
	}

	/**
	 * 返回所有资源名称
	 * 
	 * @return 通过java.util.Enumeration枚举所有资源名称
	 * 
	 * @see org.fto.jthink.resource.ResourceContainer#getAttributeNames()
	 * @see java.util.Enumeration
	 */
	public Enumeration getAttributeNames() {
		return EnumerationHelper.toEnumerator(attrsHM.keySet().iterator());
	}

	/**
	 * 开始事务,因为是采用java.sql.Connection来管理事务，所以此方法除了事务嵌套标志加1以外不做其它任何事情
	 * 
	 * @see org.fto.jthink.jdbc.JDBCTransaction#begin()
	 */
	public void begin()  {
		recursionFlag++;
	}

	/**
	 * 提交所有被使用的数据库连接中的事务, 实际上是直接调用了java.sql.Connection中的commit方法。
	 * 
	 * @see org.fto.jthink.jdbc.JDBCTransaction#commit()
	 */
	public void commit()  {
    if (recursionFlag > 1) {
    	return;
    }		
		try{
	    Iterator it = openedConnsHM.values().iterator();
	    while (it.hasNext()) {
	      Connection conn =  (Connection) it.next();
	      if(conn.getAutoCommit()==false){
	        conn.commit();
	      }
	    }
		}catch(SQLException e){
			throw new JThinkRuntimeException(JThinkErrorCode.ERRCODE_DB_SQL_EXCEPTION, "事务提交时发生异常！", e);
		}
	}

	/**
	 * 回退所有被使用的数据库连接中的事务，实际上是直接调用了java.sql.Connection中的rollback方法。
	 * 
	 * @see org.fto.jthink.transaction.Transaction#rollback()
	 */
	public void rollback()  {
    if (recursionFlag > 1) {
    	return;
    }		
		try{
	    Iterator it = openedConnsHM.values().iterator();
	    while (it.hasNext()) {
	    	Connection conn = (Connection) it.next();
	    	if(conn.getAutoCommit()==false){
	    	  conn.rollback();
	    	}
	    }
		}catch(SQLException e){
			throw new JThinkRuntimeException(JThinkErrorCode.ERRCODE_DB_SQL_EXCEPTION, "事务回退时发生异常！", e);
		}
    
	}

	/**
	 * 关闭事务，关闭所有被使用的数据库连接或返回所有被使用的数据库连接到连接池。
	 * 实际上是直接调用了java.sql.Connection中的close方法关闭连接，
	 * 或如果采用了连接池，将连接返回给连接池
	 * 
	 * @see org.fto.jthink.transaction.Transaction#close()
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
	    	if(useConnectionPool(connId)){
	    		ConnectionPool connPool = ConnectionPool.getConnectionPool(connId);
	    		/* 将活动连接返回到连接池 */
	    		if(connPool!=null){
	    		  connPool.returnConnection(conn);
	    		}
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

	/**
	 * 设置事务提交方式
	 * 
	 * @param autoCommit true或false，如果值为true,表示设置为自动事务提交方式
	 * 
	 * @deprecated
	 */
	public void setAutoCommit(boolean autoCommit){
	  this.autoCommit = autoCommit;
	  try{
  	  Iterator it = openedConnsHM.entrySet().iterator();
  	  while(it.hasNext()){
  	    Entry entry = (Entry)it.next();
  	    ((Connection)entry.getValue()).setAutoCommit(autoCommit);
  	  }
	  }catch(Exception e){
	    throw new JThinkRuntimeException(JThinkErrorCode.ERRCODE_DB_SQL_EXCEPTION, "设置连接自动提交标记时异常！", e);
	  }
	}

	/**
	 * 返回事务提交方式
	 * 
	 * @return 事务提交方式
	 * 
	 * @deprecated
	 */
	public boolean getAutoCommit(){
		return false;
	}

  /**
   * 设置事务提交方式
   */
  public void setAutoCommit(String connId, boolean flag) {
    autoCommitFlags.put(connId, new Boolean(flag));
    Connection conn = (Connection)openedConnsHM.get(connId);
    try{
      if(conn!=null && conn.getAutoCommit()!=flag){
         conn.setAutoCommit(flag);
      }
    } catch (SQLException e) {
      throw new JThinkRuntimeException(JThinkErrorCode.ERRCODE_DB_SQL_EXCEPTION, "设置连接自动提交标记时异常！", e);
    }
  }
  /**
   * 返回事务提交方式
   * 
   * @return 事务提交方式
   * 
   */
  public boolean getAutoCommit(String connId){
    Boolean flag = (Boolean)autoCommitFlags.get(connId);
    if(flag==null){
      return false;
    }else{
      return flag.booleanValue();
    }

  }
	/**
	 * 判断是否采用连接池管理
	 * 
	 * @param connId 数据库连接ID
	 * 
	 * @return 如果是采用连接池管理连接，返回true，否则返回false
	 */
	protected boolean useConnectionPool(String connId){
		Element connCfgEL = (Element)connCfgsHM.get(connId);
  	String usePool = connCfgEL.getChild("connection-pool").getChildText("use-pool");
  	if(usePool.equalsIgnoreCase("yes")){
  		return true;
  	}
  	return false;
	}

	
}