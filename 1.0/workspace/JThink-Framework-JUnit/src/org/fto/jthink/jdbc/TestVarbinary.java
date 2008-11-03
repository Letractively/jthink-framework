package org.fto.jthink.jdbc;

import java.util.HashMap;
import java.util.Map;

import org.fto.jthink.resource.ResourceManager;
import org.fto.jthink.util.DateTimeHelper;

public class TestVarbinary {

  /**
   * @param args
   */
  public static void main(String[] args) {
    // TODO Auto-generated method stub
    try {
      ResourceManager resManager = new ResourceManagerInitialization().initResourceManager();
      String connId = "SampleDataSource";
      JDBCTransaction transaction = (JDBCTransaction)resManager.getResource(JDBCTransaction.class.getName());
      /* 返回SQLExecutor工厂，并创建SQLExecutor对象 */
      SQLExecutor sqlExecutor = transaction.getSQLExecutorFactory(connId).create();
      /* 返回SQLBuilder工厂，并创建SQLBuilder对象 */
      SQLBuilder sqlBuilder = transaction.getSQLBuilderFactory(connId).create("");
      
      try{
        /* 开始事务 */
        transaction.begin(); 
        
        //logger.debug("getTransactionLevel():"+transaction.getTransactionLevel("SampleDataSource"));
        Map fields = new HashMap();
        
        fields.put("varbinary", "测试！".getBytes());
        
        SQL sql = sqlBuilder.constructSQLForInsert("test", fields);
//        if(logger.isDebugEnabled()){
//          logger.debug("通过SQLBuilder构建的SQL: " + sql.getSQLString()+"     "+messagesHM);
//        }
        /* 执行SQ语句 */
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
      
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
  }

  
  
}
