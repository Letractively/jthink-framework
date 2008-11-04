/*
 * ConnectionPool.java	2005-6-25
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.fto.jthink.exception.JThinkRuntimeException;
import org.fto.jthink.log.LogManager;
import org.fto.jthink.log.Logger;


/**
 * 连接池, 可为每一个连接ID，创建一个连接池,连接ID在fto-jthink.xml文件中配置。
 * 此连接池不创建具体连接，它只对连接统一进行管理。如果应用服务器已经提供了对数据源的池管理功能，
 * 建议不要使用此连接池，即用JNDIConnectionFactory工厂创建的连接不要使用此连接池。
 * 而用DirectConnectionFactory工厂创建的连接推荐使用此连接池。是否使用连接池来管理连接，
 * 只须在fto-jthink.xml中的数据源配置中设置即可。
 * 
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2005-06-25  创建此类型
 * 2005-10-29  修改了closeConnectionPool()方法
 * 2008-10-24  在PooledConnection中记录默认事务隔离级别，当返回连接时将恢复默认事务隔离级别
 * 2008-10-30  解决以前没有处理的问题，连接池满的检查
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 */

public class ConnectionPool {
	private static final Logger logger = LogManager.getLogger(ConnectionPool.class);
	
	/* 连接池对应的连接ID */
	private String connId = null;
	/* 连接池中的连接 */
	private List conns = new ArrayList();
	
	/* 用于检查连接是否可用的SQL */
	private String checkConnSQL = null;
	/* 初始连接池大小 */
	private int minPoolSize = 0;
	/* 最大连接池大小,如果值为0或小于0将不限制最大值 */
	private int maxPoolSize = 0;	
	/* 池连接的增量值 */
	private int incrementalPoolSize = 1; 
	/* 池连接的超时限,单位:分钟, 如果设置为0或小于0，将永远不会超时 */
	private int idleTimeoutMinutes = 0;

	/* 处理连接超时的线程 */
	private static IdleTimeoutProcessor idleTimeoutProcessor = null;

	/* 所有连接池 */
	private static Map poolsHM = new HashMap();
	
	
	static{
	  /* 系统退出进事件 */
	  Runtime.getRuntime().addShutdownHook(new ExitEvent());
    /* 连接超时处理 */
    idleTimeoutProcessor = new IdleTimeoutProcessor(poolsHM);
    idleTimeoutProcessor.start();
	}
	/**
	 * 创建一个连接池
	 * 
	 * @param connId 连接ID
	 */
	private ConnectionPool(String connId){
		this.connId = connId;
	}

	
	
 /**
	* 返回连接池
	* 
	* @param connId 连接ID
	* 
	* @return 连接池,如果连接池不存在,返回null
	*/
	public static ConnectionPool getConnectionPool(String connId){
		return (ConnectionPool)poolsHM.get(connId);
	}	

	
	/**
	 * 创建连接池
	 * 
	 * @param connId 连接ID
	 * 
	 * @return 连接池
	 */
	public static ConnectionPool createConnectionPool(String connId){
		return createConnectionPool(connId, 0, 0, 1, 0, null);
	}

	/**
	 * 创建连接池, 如果连接池已经创建将直接返回连接池
	 * 
	 * @param connId 连接ID
	 * @param minPoolSize 连接池初始大小
	 * @param maxPoolSize 连接池最大连接数量
	 * @param incrementalPoolSize 连接池连接的增量值
	 * @param idleTimeoutMinutes 池连接的超时时限
	 * @param checkConnectionSQL 检索连接是否可用的SQL
	 * @return 连接池
	 */
	public synchronized static ConnectionPool createConnectionPool(String connId, int minPoolSize, int maxPoolSize, int incrementalPoolSize, int idleTimeoutMinutes, String checkConnectionSQL	){
		ConnectionPool connPool = (ConnectionPool)poolsHM.get(connId);
		if(connPool!=null){
			return connPool;
		}
		connPool = new ConnectionPool(connId);
		poolsHM.put(connId, connPool);
		connPool.setMinPoolSize(minPoolSize);
		connPool.setMaxPoolSize(maxPoolSize);
		connPool.setIncrementalPoolSize(incrementalPoolSize);
		connPool.setIdleTimeoutMinutes(idleTimeoutMinutes);
		connPool.setCheckConnectionSQL(checkConnectionSQL);

		return connPool;
	}

	/**
	 * 释放连接池所有连接
	 */
	public synchronized static void releasePoolConnections(){
		Iterator poolsIT = poolsHM.values().iterator();
		while(poolsIT.hasNext()){
			((ConnectionPool)poolsIT.next()).releaseConnections();
		}
	}
	
	/**
	 * 关闭连接池
	 */
	public synchronized static void closeConnectionPool(){
	  if(poolsHM==null || poolsHM.size()==0){
	    /* 连接池已经被关闭 */
	    return;
	  }
		logger.debug("关闭数据库连接池.");
    releasePoolConnections();
    poolsHM.clear();
		if(idleTimeoutProcessor!=null){
		  idleTimeoutProcessor.close();
			idleTimeoutProcessor = null;
		}
	}
	
	
	/**
	 * 返回连接ID
	 * @return 连接ID
	 */
	public String getConnectionId(){
		return connId;
	}

	/**
	 * 返回所有池连接
	 */
	private List getConnections(){
		return conns;
	}
	
	/**
	 * 设置池初始大小
	 */
	public synchronized void setMinPoolSize(int minPoolSize){
		this.minPoolSize = minPoolSize;
	}
	
	/**
	 * 返回池初始大小
	 */
	public int getMinPoolSize(){
		return minPoolSize;
	}
	
	/**
	 * 设置池最大连接数量，如果设置为0，将没有最大值，直到内存溢出
	 */
	public synchronized void setMaxPoolSize(int maxPoolSize){
		this.maxPoolSize = maxPoolSize;
	}
	
	/**
	 * 返回池最大连接数量
	 */
	public int getMaxPoolSize(){
		return maxPoolSize;
	}

	/**
	 * 设置池连接增量值
	 */
	public synchronized void setIncrementalPoolSize(int incrementalPoolSize){
		this.incrementalPoolSize = incrementalPoolSize;
	}
	/**
	 * 返回池连接增量值
	 */
	public int getIncrementalPoolSize(){
		return incrementalPoolSize;
	}

	/**
	 * 设置连接超时时间值，如果设置为0，将永远不会超时，单位：分钟
	 */
	public synchronized void setIdleTimeoutMinutes(int idleTimeoutMinutes){
		this.idleTimeoutMinutes = idleTimeoutMinutes;
	}

	/**
	 * 返回连接超时时间值
	 */
	public int getIdleTimeoutMinutes(){
		return idleTimeoutMinutes;
	}
	
	
	/**
	 * 设置用于测试连接的SQL
	 * @param sql
	 */
	public synchronized void setCheckConnectionSQL(String sql){
		if(sql!=null && sql.length()!=0){
			this.checkConnSQL = sql;
		}else{
			this.checkConnSQL = null;
		}
	}
	
	/**
	 * 返回用于测试连接的SQL
	 */
	public String getCheckConnectionSQL(){
		return checkConnSQL;
	}
	
	/**
	 * 从连接池返回一个可用连接
	 * 
	 * @return java.sql.Connection连接, 如果没找到,返回null
	 */
	public synchronized Connection getConnection(){
		Connection conn = null;
		/* 返回特定连接池 */
		for(int i=0;i<conns.size();){
			PooledConnection pConn = (PooledConnection)conns.get(i);
			if(!pConn.isBusy()){
				conn = pConn.getConnection();
				pConn.setBusy(true);
				if(testConnection(conn)){
					break;
				}else{
					/* 连接不可用，将其释放 */
					conns.remove(i);
					try {
						conn.close();
					} catch (SQLException e) {
						logger.warn("关闭连接时发生错误!", e);
					}
				}
			}else{
				i++;
			}
		}
		return conn;
	}

	/**
	 * 测试连接
	 * @param conn 连接对象
	 */
	private boolean testConnection(Connection conn) {
		Statement stmt = null;
		ResultSet rs = null;
		try{
			
			if(conn.isClosed()){
				logger.warn("连接不可用.已经被异常关闭! "+conn);
				return false;
			}
			
			if(checkConnSQL!=null && checkConnSQL.length()!=0){
				stmt = conn.createStatement(); 
				stmt.setQueryTimeout(60);//设置查询超时时间，60秒
				stmt.execute(checkConnSQL); 
				rs = stmt.getResultSet();
			}
		}catch(SQLException se){
			logger.warn("连接不可用.");
			/* 连接不可用 */
			return false;
		}finally{
		  boolean closeStatus = true;
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					logger.warn("关闭ResultSet时发生错误!", e);
					closeStatus = false;
				}
			}
			if(stmt!=null){
				try {
					stmt.close();
				} catch (SQLException e) {
					logger.warn("关闭Statement时发生错误!", e);
					closeStatus = false;
				}
			}
			if(closeStatus==false){
			  return false;
			}
		}
		return true;
	}

	/**
	 * 加入一个连接到连接池, 如果连接池满，返回false
	 * 
	 * @param conn java.sql.Connection连接
	 */
	public synchronized boolean setConnection(Connection conn){
	  if(maxPoolSize==conns.size()){
	    logger.warn("数据库连接池满，请调整连接池大小！");
	    return false;
	  }
	  try{
	    conns.add(new PooledConnection(conn));
	    return true;
	  }catch(SQLException e){
	    throw new JThinkRuntimeException(e);
	  }
	}

  /**
   * 从连接池移除一个连接
   * 
   * @param conn java.sql.Connection连接
   */
  public synchronized void removeConnection(Connection conn){
    for(int i=conns.size()-1;i>=0;i--){
      PooledConnection pConn = (PooledConnection)conns.get(i);
      if(pConn.getConnection()==conn){
        conns.remove(i);
        break;
      }
    }
  }
  
  /**
   * 从连接池移除一个连接
   * 
   * @param conn java.sql.Connection连接
   */
  private synchronized void removeConnection(PooledConnection pConn){
    conns.remove(pConn);
  }

	
	/**
	 * 返回一个不用了的连接到连接池
	 * 
	 * @param conn 连接对象
	 */
	public synchronized void returnConnection(Connection conn) {
		Iterator connsIT = conns.iterator();
		while(connsIT.hasNext()){
			PooledConnection pConn = (PooledConnection)connsIT.next();
			if(pConn.getConnection()==conn){
				pConn.setBusy(false);
				pConn.resumeTransactionIsolation();
				break;
			}
		}
	}

	/**
	 * 释放并销毁连接池中的所有连接
	 */
	public synchronized void releaseConnections() {
		Iterator connsIT = conns.iterator();
		while(connsIT.hasNext()){
			PooledConnection pConn = (PooledConnection)connsIT.next();
			try {
				pConn.getConnection().close();
			} catch (SQLException e) {
				logger.warn("关闭连接池中的连接时发生错误!", e);
			}
		}
		conns.clear();
	}

	/**
	 * 释放并销毁连接池中指定的连接
	 */
	public synchronized void releaseConnection(Connection conn) {
		Iterator connsIT = conns.iterator();
		while(connsIT.hasNext()){
			PooledConnection pConn = (PooledConnection)connsIT.next();
			if(pConn.getConnection()==conn){
				conns.remove(pConn);
				try {
					conn.close();
				} catch (SQLException e) {
					logger.warn("关闭连接池中的连接时发生错误!", e);
				}
				break;
			}
		}
	}

	/**
	 * 返回当前连接池所有连接数量
	 */
	public int getCurrentConnections(){
		return conns.size();
	}

	/**
	 * 返回当前所有空闲连接数量
	 */
	public int getFreeConnections(){
		int freeConnsCount = 0;
		Iterator connsIT = conns.iterator();
		while(connsIT.hasNext()){
			PooledConnection pConn = (PooledConnection)connsIT.next();
			if(!pConn.isBusy()){
				freeConnsCount++;
			}
		}
		return freeConnsCount;
	}

	/** 
	 * 内部使用的用于保存连接池中连接对象的类。
	 * 此类中有两个成员，一个是数据库的连接，另一个是指示此连接是否 
	 * 正在使用的标志。 
	 */
	class PooledConnection {

		private Connection connection = null;	// 数据库连接 
		private boolean busy = false; 				// 此连接的忙标志，默认非忙 
		private long startIdleTime = 0;				// 开始空闲时间点
		private int level = -1;                // 连接当前事务隔离级别

		/**
		 * 构造方法
		 * @throws SQLException 
		 */
		public PooledConnection(Connection connection) throws SQLException {
			this.connection = connection;
			this.level = connection.getTransactionIsolation();
		}
		/**
		 * 恢复事务隔离级别
		 */
		public void resumeTransactionIsolation(){
		  try{
		    if(connection.getMetaData().supportsTransactionIsolationLevel(level)){
		      connection.setTransactionIsolation(level);
		    }
		  }catch(SQLException e){
		    throw new JThinkRuntimeException(e);
		  }
		}
		/**
		 * 返回连接 
		 */
		public Connection getConnection() {
			return connection;
		}

//		/**
//		 * 设置连接 
//		 */
//		public void setConnection(Connection connection) {
//			this.connection = connection;
//		}

		/**
		 * 此连接是否正忙
		 */
		public boolean isBusy() {
			return busy;
		}

		/**
		 * 设置忙标志
		 */
		public void setBusy(boolean busy) {
			this.busy = busy;
			if(busy==false){
				this.startIdleTime = System.currentTimeMillis();
			}
		}
		
		/**
		 * 返回开始空闲时间点
		 */
		public long getStartIdleTime(){
			return startIdleTime;
		}
	} 

	/**
	 * 处理连接超时的线程
	 */
	static class IdleTimeoutProcessor extends Thread{
		private Map poolsHM = null;;
		private boolean enable = true;
		private IdleTimeoutProcessor(Map poolsHM){
			super("ConnectionPool Connection IdleTimeoutProcessor");
		  this.poolsHM = poolsHM;
			this.setDaemon(true);
		}
		
		/**
		 * 退出线程
		 */
		public synchronized void close() {
		  enable=false;
    }

    public void run(){
			while(enable){
        try{
          sleep(60000);
        }catch(Exception e){
          e.printStackTrace();
        }
//        if(logger.isDebugEnabled()){
//          logger.debug("检查是否有超时连接.");
//        }
        
				/* 检查是否有超时连接 */
				Iterator poolsIT = poolsHM.values().iterator();
				while(poolsIT.hasNext()){
					ConnectionPool connPool = (ConnectionPool)poolsIT.next();
					long idleTimeoutTime = connPool.getIdleTimeoutMinutes()*60*1000;
					if(idleTimeoutTime==0){
						break;
					}
					List conns = connPool.getConnections();
          if(logger.isDebugEnabled()){
            //logger.debug("连接池("+connPool.getConnectionId()+")中的可用连接数量:"+ConnectionPool.getConnectionPool(connPool.getConnectionId()).getCurrentConnections());
            logger.debug("连接池("+connPool.getConnectionId()+")中的可用连接数量:"+conns.size());
          }
					for(int i=0;i<conns.size();){
						PooledConnection pConn = (PooledConnection)conns.get(i);
						if(!pConn.isBusy()){
							long currTime = System.currentTimeMillis();
							long startIdleTime = pConn.getStartIdleTime();
							if((currTime-startIdleTime)>idleTimeoutTime){
								if(logger.isDebugEnabled()){
									logger.debug("池连接超时，释放超时连接:"+pConn.getConnection());
								}
								connPool.removeConnection(pConn);
								try {
									pConn.getConnection().close();
								} catch (SQLException e) {
									logger.warn("关闭连接时发生错误!", e);
								}
								
							}else{
								i++;
							}
								
						}else{
							i++;
						}
					}
//					if(logger.isDebugEnabled()){
//						logger.debug("连接池("+connPool.getConnectionId()+")中的可用连接数量:"+ConnectionPool.getConnectionPool(connPool.getConnectionId()).getCurrentConnections());
//					}
				}
			}
		}
	}
	
	/**
	 *  程序退出时事件代码
	 */
	static class ExitEvent
	    extends Thread{
	  
	  public void run(){
	    try{
	      //System.out.println("Release ConnectionPool resource ...");
	      /* 关闭数据库连接池 */
	      ConnectionPool.closeConnectionPool();
	    }
	    catch(Exception ex){
	      ex.printStackTrace();
	      logger.warn(ex.getMessage(), ex);
	    }
	  }
	}	
	
}
