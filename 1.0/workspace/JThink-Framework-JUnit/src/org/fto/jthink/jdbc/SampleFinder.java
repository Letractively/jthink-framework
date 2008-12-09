
package org.fto.jthink.jdbc;

import java.util.List;

import org.fto.jthink.jdbc.Condition;
import org.fto.jthink.jdbc.JDBCTransaction;
import org.fto.jthink.jdbc.SQL;
import org.fto.jthink.jdbc.SQLBuilder;
import org.fto.jthink.jdbc.SQLExecutor;
import org.fto.jthink.log.LogManager;
import org.fto.jthink.log.Logger;
import org.fto.jthink.resource.ResourceManager;
import org.fto.jthink.transaction.TransactionFactory;
import org.fto.jthink.transaction.TransactionManager;

/**
 */
public class SampleFinder {

  private static Logger logger = LogManager.getLogger(SampleFinder.class);
  
  ResourceManager resManager;
  
  public SampleFinder(ResourceManager resManager){
    this.resManager = resManager;
  }
  /**
   * 统计数量
   */
  public List count(){
    
    logger.info("统计数量。");
    
    //JDBCTransaction transaction = (JDBCTransaction)resManager.getResource(JDBCTransaction.class.getName());
    JDBCTransaction transaction = (JDBCTransaction)((TransactionFactory)resManager.getResource("SampleTransaction")).create();
    /* 返回SQLExecutor工厂，并创建SQLExecutor对象 */
    SQLExecutor sqlExecutor = transaction.getSQLExecutorFactory("SampleDataSource").create();
    //sqlExecutor.setPoolable(true);
    //sqlExecutor.setResultMaker(new MapResultMaker());
    SQLBuilder sqlBuilder = transaction.getSQLBuilderFactory("SampleDataSource").create("");
    try{
      /* 开始事务 */
      transaction.begin(); 
      /* 直接执行SQL查询语句 */
      return (List)sqlExecutor.execute(sqlBuilder.constructSQLForCount("messages", "*", "MsgCount", null));
      
    }finally{
      /* 关闭事务 */
      transaction.close();
    }
  }
  /**
   * 返回数据库表中所有的数据。
   * 在默认情况下，SQLExecutor会将执行SQL后返回的结果集以org.jdom.Element类型的数据结构形式
   * 将其以List的方式返回。org.jdom.Element表示XML文档的一个节点，数据表中的一行数据将被构建
   * 成一个org.jdom.Element。
   */
  public List findAll(){
    
    logger.info("返回数据库表中所有的数据。");
    
    //JDBCTransaction transaction = (JDBCTransaction)resManager.getResource(JDBCTransaction.class.getName());
    JDBCTransaction transaction = (JDBCTransaction)((TransactionFactory)resManager.getResource("SampleTransaction")).create();
    /* 返回SQLExecutor工厂，并创建SQLExecutor对象 */
    SQLExecutor sqlExecutor = transaction.getSQLExecutorFactory("SampleDataSource").create();
    //sqlExecutor.setPoolable(true);
    //sqlExecutor.setResultMaker(new MapResultMaker());
    
    try{
      /* 开始事务 */
      transaction.begin(); 
      /* 直接执行SQL查询语句 */
      return (List)sqlExecutor.executeQuery("select * from messages");
      
    }finally{
      /* 关闭事务 */
      transaction.close();
    }
  }
  
  /**
   * 返回数据库表中与ID匹配的数据
   */
  public List findBy(Condition condition){
    
    logger.info("返回数据库表中与ID匹配的数据。");
    
    //JDBCTransaction transaction = (JDBCTransaction)resManager.getResource(JDBCTransaction.class.getName());
    JDBCTransaction transaction = (JDBCTransaction)((TransactionFactory)resManager.getResource("SampleTransaction")).create();
    /* 返回SQLExecutor工厂，并创建SQLExecutor对象 */
    SQLExecutor sqlExecutor = transaction.getSQLExecutorFactory("SampleDataSource").create();
    
    /* 返回SQLBuilder工厂，并创建SQLBuilder对象 */
    SQLBuilder sqlBuilder = transaction.getSQLBuilderFactory("SampleDataSource").create("");
    
    //sqlExecutor.setResultMaker(new MapResultMaker());
    
    try{
      /* 开始事务 */
      transaction.begin(); 
      
//      /* 构建查询条件 */
//      Condition condition = new Condition();
//      condition.add(new ConditionItem("Id","=",id));
      
      /* 通过SQLBuilder来构建SQL语句 */
      SQL sql = sqlBuilder.constructSQLForSelect("Messages",false,new Column[]{new Column("SUBJECT"),new Column("SENDER")},condition,null,null,0,20);
      //SQL sql = sqlBuilder.constructSQLForSelect("Messages",true,null,condition,null,null);
      
      logger.debug("通过SQLBuilder构建的SQL: " + sql.getSQLString());
      
      return (List)sqlExecutor.execute(sql);
      
    }finally{
      /* 关闭事务 */
      transaction.close();
    }
  }
}
