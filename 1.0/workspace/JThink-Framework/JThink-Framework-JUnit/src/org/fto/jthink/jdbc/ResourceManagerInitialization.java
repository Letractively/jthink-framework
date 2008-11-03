package org.fto.jthink.jdbc;

import java.io.IOException;

import org.fto.jthink.config.Configuration;
import org.fto.jthink.log.LogManager;
import org.fto.jthink.resource.ResourceManager;
import org.fto.jthink.transaction.DefaultTransactionManager;
import org.fto.jthink.transaction.TransactionFactory;
import org.fto.jthink.transaction.TransactionManager;
import org.fto.jthink.util.PackageResourceReader;
import org.fto.jthink.util.XMLHelper;

public class ResourceManagerInitialization {
  public ResourceManager initResourceManager() throws Exception{
    /* 初始化配置文件 */
    Configuration config = 
      new Configuration(
          XMLHelper.load(
               new PackageResourceReader(this.getClass()).getResource("/fto-jthink.xml").openStream()));
  
    /* 初始化日志管理器 */
    LogManager.configure(config);
//    /* 返回日志处理接口 */
//    logger = LogManager.getLogger(SQLTestCase.class);
    
    /* 初始化资源管理器 */
    ResourceManager resManager = new ResourceManager();
    /* 将配置文件加入到资源管理器中 */
    resManager.setResource(Configuration.class.getName(), config);
    
    /* 初始化事务管理器 */
    TransactionManager transactionManager = new DefaultTransactionManager(resManager, config);
    /* 将事务管理器加入到资源管理器中 */
    resManager.setResource(TransactionManager.class.getName(), transactionManager);
    
    /* 返回在fto-jthink.xml中配置的JDBC事务 */
    TransactionFactory transactionFactory = transactionManager.getTransactionFactory("SampleTransaction");
    JDBCTransaction transaction  = (JDBCTransaction)transactionFactory.create();
    /* 将此事务处理对象加入到事务管理器中 */
    transactionManager.addTransaction(JDBCTransaction.class.getName(), transaction);
    resManager.setResource(JDBCTransaction.class.getName(), transaction);
    
    return resManager;
  }
}
