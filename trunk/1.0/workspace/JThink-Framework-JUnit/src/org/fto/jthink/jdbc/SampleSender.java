
package org.fto.jthink.jdbc;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fto.jthink.exception.JThinkRuntimeException;
import org.fto.jthink.jdbc.JDBCTransaction;
import org.fto.jthink.jdbc.SQL;
import org.fto.jthink.jdbc.SQLBuilder;
import org.fto.jthink.jdbc.SQLExecutor;
import org.fto.jthink.log.LogManager;
import org.fto.jthink.log.Logger;
import org.fto.jthink.resource.ResourceManager;
import org.fto.jthink.transaction.TransactionFactory;
import org.fto.jthink.transaction.TransactionManager;
import org.fto.jthink.util.DateTimeHelper;
import org.fto.jthink.util.XMLHelper;
import org.jdom.Element;

/**
 */
public class SampleSender {

  private static Logger logger = LogManager.getLogger(SampleSender.class);
  
  ResourceManager resManager;
  
  public SampleSender(ResourceManager resManager){
    this.resManager = resManager;
  }
  
  /**
   * 批量发送留言信息
   * @throws Exception 
   */
  public void batchSendMessages(Map[] messagesMaps) throws Exception{
    logger.info("向数据库表中插入数据。");
    String connId = "SampleDataSource";
    //JDBCTransaction transaction = (JDBCTransaction)resManager.getResource(JDBCTransaction.class.getName());
    JDBCTransaction transaction = (JDBCTransaction)((TransactionFactory)resManager.getResource("SampleTransaction")).create();
    /* 返回SQLExecutor工厂，并创建SQLExecutor对象 */
    SQLExecutor sqlExecutor = transaction.getSQLExecutorFactory(connId).create();
    /* 返回SQLBuilder工厂，并创建SQLBuilder对象 */
    SQLBuilder sqlBuilder = transaction.getSQLBuilderFactory(connId).create("");

    try{
      /* 开始事务 */
      transaction.begin(); 
      //transaction.setAutoCommit(connId, true);
      
      System.out.println("transaction.getAutoCommit(connId):"+transaction.getAutoCommit(connId));
      
      //transaction.setTransactionLevel(connId, level);
      
//      SQL sql = sqlBuilder.constructSQLForInsert("Messages", messagesHM);
//      if(logger.isDebugEnabled()){
//        logger.debug("通过SQLBuilder构建的SQL: " + sql.getSQLString()+"     "+messagesHM);
//      }
      String[] sqls = new String[messagesMaps.length];
      for(int i=0;i<messagesMaps.length;i++){
        String sqlstr =
        "INSERT INTO Messages (Sender,Email,Subject,Contact,IP,SendTime,ID,Content) " +
        "VALUES ('"+messagesMaps[i].get("Sender")+
        "','"+messagesMaps[i].get("Email")+
        "','"+messagesMaps[i].get("Subject")+
        "','"+messagesMaps[i].get("Contact")+
        "','"+messagesMaps[i].get("IP")+
        "','"+DateTimeHelper.formatDateTimetoString(DateTimeHelper.getSystemDate())+
        "','"+(generateID(sqlExecutor).intValue()+i)+
        "','"+messagesMaps[i].get("Content")+"')"; 
        sqls[i]=sqlstr;
        if(logger.isDebugEnabled()){
          logger.debug("通过SQLBuilder构建的SQL: " + sqlstr+"     ");
        }
      }
      /* 执行SQ语句 */
      int[] rs = sqlExecutor.executeBatch(sqls);
      System.out.println(rs[0]+","+rs[1]);
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
   * 发送留言信息
   * 
   * @param messagesHM 发送的信息内容
   * @throws Exception
   */
  public void sendMessage(Map messagesHM) throws Exception{
    
    logger.info("向数据库表中插入数据。");
    
    //JDBCTransaction transaction = (JDBCTransaction)resManager.getResource(JDBCTransaction.class.getName());
    JDBCTransaction transaction = (JDBCTransaction)((TransactionFactory)resManager.getResource("SampleTransaction")).create();
    
    /* 返回SQLExecutor工厂，并创建SQLExecutor对象 */
    SQLExecutor sqlExecutor = transaction.getSQLExecutorFactory("SampleDataSource").create();
    //sqlExecutor.setTimeout(60);
//    PreparedStatement ps = sqlExecutor.getConnection().prepareStatement("select 1");
//    logger.debug("timeout:"+ps.getQueryTimeout());
//    ps.close();
//    logger.debug("timeout:"+sqlExecutor.getTimeout());
    /* 返回SQLBuilder工厂，并创建SQLBuilder对象 */
    SQLBuilder sqlBuilder = transaction.getSQLBuilderFactory("SampleDataSource").create("");
    
    try{
      /* 开始事务 */
      transaction.begin(); 
      
      //logger.debug("getTransactionLevel():"+transaction.getTransactionLevel("SampleDataSource"));
      
      messagesHM.put("ID", generateID(sqlExecutor));
      messagesHM.put("SendTime", DateTimeHelper.formatDateTimetoString(DateTimeHelper.getSystemDate()));
      
      SQL sql = sqlBuilder.constructSQLForInsert("Messages", messagesHM);
      if(logger.isDebugEnabled()){
        logger.debug("通过SQLBuilder构建的SQL: " + sql.getSQLString()+"     "+messagesHM);
      }
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
  }

  /**
   * 修改信息
   * 
   * @param messagesHM 发送的信息内容
   * @throws Exception
   */
  public void modiMessage(Map messagesHM, String id) throws Exception{
    
    logger.info("修改数据。");
    
    //JDBCTransaction transaction = (JDBCTransaction)resManager.getResource(JDBCTransaction.class.getName());
    JDBCTransaction transaction = (JDBCTransaction)((TransactionFactory)resManager.getResource("SampleTransaction")).create();
    /* 返回SQLExecutor工厂，并创建SQLExecutor对象 */
    SQLExecutor sqlExecutor = transaction.getSQLExecutorFactory("SampleDataSource").create();
    /* 返回SQLBuilder工厂，并创建SQLBuilder对象 */
    SQLBuilder sqlBuilder = transaction.getSQLBuilderFactory("SampleDataSource").create("");
    
    try{
      /* 开始事务 */
      transaction.begin(); 

      Condition condi = new Condition();
      condi.add(new ConditionItem("ID","=",id));
      
      SQL sql = sqlBuilder.constructSQLForUpdate("Messages", messagesHM, condi);
      if(logger.isDebugEnabled()){
        logger.debug("通过SQLBuilder构建的SQL: " + sql.getSQLString()+"     "+messagesHM);
      }
      /* 执行SQ语句 */
      Object rs = sqlExecutor.execute(sql);
      
      System.out.println("update rows="+rs);
      
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
   * 删除信息
   * 
   * @throws Exception
   */
  public void deleteMessage(String id) throws Exception{
    
    logger.info("修改数据。");
    
    //JDBCTransaction transaction = (JDBCTransaction)resManager.getResource(JDBCTransaction.class.getName());
    JDBCTransaction transaction = (JDBCTransaction)((TransactionFactory)resManager.getResource("SampleTransaction")).create();
    /* 返回SQLExecutor工厂，并创建SQLExecutor对象 */
    SQLExecutor sqlExecutor = transaction.getSQLExecutorFactory("SampleDataSource").create();
    /* 返回SQLBuilder工厂，并创建SQLBuilder对象 */
    SQLBuilder sqlBuilder = transaction.getSQLBuilderFactory("SampleDataSource").create("");
    
    try{
      /* 开始事务 */
      transaction.begin(); 

      Condition condi = new Condition();
      condi.add(new ConditionItem("ID","=",id));
      
      SQL sql = sqlBuilder.constructSQLForDelete("Messages", condi);
      if(logger.isDebugEnabled()){
        logger.debug("通过SQLBuilder构建的SQL: " + sql.getSQLString()+"     ");
      }
      /* 执行SQ语句 */
      Object rs = sqlExecutor.execute(sql);
      
      System.out.println("delete rows="+rs);
      
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
   * 生成ID
   */
  private Integer generateID(SQLExecutor sqlExecutor){
    List maxIdLT = (List)sqlExecutor.executeQuery("SELECT MAX(ID) AS MAXID FROM Messages");
    if(sqlExecutor.getResultMaker() instanceof ElementResultMaker){
      Element maxIdEL = (Element)maxIdLT.iterator().next();
      if(maxIdEL.getAttributeValue("MAXID")==null){
        return new Integer(1);
      }
      return new Integer(Integer.parseInt(maxIdEL.getAttributeValue("MAXID"))+1);
    }
    if(sqlExecutor.getResultMaker() instanceof MapResultMaker){
      Map maxId = (Map)maxIdLT.iterator().next();
      if(maxId.get("MAXID")==null){
        return new Integer(1);
      }
      return new Integer(Integer.parseInt((String)maxId.get("MAXID"))+1);
    }
    if(sqlExecutor.getResultMaker() instanceof DataObjectResultMaker){
      DataObject maxId = (DataObject)maxIdLT.iterator().next();
      if(maxId.get("MAXID")==null){
        return new Integer(1);
      }
      return new Integer(Integer.parseInt((String)maxId.get("MAXID"))+1);
    }
    throw new JThinkRuntimeException("not find!");
  }
  
  
}
