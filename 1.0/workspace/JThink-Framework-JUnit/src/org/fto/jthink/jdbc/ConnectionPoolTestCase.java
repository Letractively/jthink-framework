package org.fto.jthink.jdbc;

import java.sql.Connection;

import org.fto.jthink.resource.ResourceManager;

import junit.framework.TestCase;

public class ConnectionPoolTestCase extends TestCase {

    public static void main(String[] args) {
      junit.textui.TestRunner.run(ConnectionPoolTestCase.class);
    }
    
    public void test() throws Exception{ 
      System.out.println("\n[正在测试方法: ConnectionPool.*()...]");
      ResourceManager resManager = new ResourceManagerInitialization().initResourceManager();
      JDBCTransaction transn = (JDBCTransaction)resManager.getResource(JDBCTransaction.class.getName());
      
      String connId = "SampleDataSource";
      //transn.setTransactionLevel(connId,JDBCTransaction.TRANSACTION_REPEATABLE_READ);
      Connection conn=null;
      try{
        transn.begin();
        //conn.
        conn = transn.getConnection(connId);
        //System.out.println(transn.getTransactionLevel());
        
        System.out.println("conn.getTransactionIsolation()="+conn.getTransactionIsolation());
        //conn.prepareStatement().
        //ConnectionPool pool = ConnectionPool.getConnectionPool(connId);
        
        transn.commit();
      }catch(Exception e){
        e.printStackTrace();
        transn.rollback();
      }finally{
        transn.close();
      }
      //System.out.println("conn.getTransactionIsolation()="+conn.getTransactionIsolation());
    }
    
    
    
    
    
    
    
    
}
