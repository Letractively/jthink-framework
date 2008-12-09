package org.fto.jthink.jdbc;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fto.jthink.config.Configuration;
import org.fto.jthink.log.LogManager;
import org.fto.jthink.log.Logger;
import org.fto.jthink.resource.ResourceManager;
import org.fto.jthink.transaction.DefaultTransactionManager;
import org.fto.jthink.transaction.TransactionFactory;
import org.fto.jthink.transaction.TransactionManager;
import org.fto.jthink.util.PackageResourceReader;
import org.fto.jthink.util.XMLHelper;
import org.jdom.Element;

import junit.framework.TestCase;

public class SQLTestCase extends TestCase {
  private static Logger logger = LogManager.getLogger(SQLTestCase.class);
    public static void main(String[] args) {
      junit.textui.TestRunner.run(SQLTestCase.class);
    }
    
    
    
      public void test() throws Exception{ 
        System.out.println("\n[正在测试: SQL相关功能()...]");

        //ResourceManager resManager = new ResourceManagerInitialization().initResourceManager();
        ResourceManager resManager = new ResourceManager();
        
        /* --向数据库表中加入数据--------------------------------------------------------------------- */
        SampleSender sender = new SampleSender(resManager);
        HashMap[] msgsMaps = new HashMap[2];
        HashMap msgsHM = new HashMap();
        msgsHM.put("Subject","事务问题,ID:"+Math.random());
        msgsHM.put("Content","在JThink-Framework中如何处理事务？");
        msgsHM.put("Sender","try");
        msgsHM.put("IP","127.0.0.1");
        msgsHM.put("Email","try_wen@yahoo.com.cn");
        msgsHM.put("Contact","32312569");
        msgsMaps[0] = msgsHM;
        //java.sql.Savepoint;
        try {
          sender.sendMessage((Map)msgsHM.clone()); //发送留言
        } catch (Exception e) {
          logger.error("发送留言异常，"+e.getMessage(), e);
        }
        msgsHM = new HashMap();
        msgsHM.put("Subject","日志问题,ID:"+Math.random());
        msgsHM.put("Content","在JThink-Framework中如何处理日志？");
        msgsHM.put("Sender","wenjian");
        msgsHM.put("IP","192.168.0.1");
        msgsHM.put("Email","wjian@free-think.org");
        msgsHM.put("Contact","32312569");
        msgsMaps[1] = msgsHM;
        try {
          sender.sendMessage((Map)msgsHM.clone()); //发送留言
        } catch (Exception e) {
          logger.error(e.getMessage(), e);
        }
        try {
          sender.batchSendMessages(msgsMaps); //发送留言
        } catch (Exception e) {
          logger.error(e.getMessage(), e);
        }  
        
        try {
          sender.modiMessage(msgsHM, "101"); //修改留言
        } catch (Exception e) {
          logger.error(e.getMessage(), e);
        }  
        try {
          sender.deleteMessage("101"); //删除留言
        } catch (Exception e) {
          logger.error(e.getMessage(), e);
        }  
//        try{
//          Thread.sleep(1000*60*3);
//        }catch(Exception e){
//          e.printStackTrace();
//        }
        /* --查询数据库表中的数据----------------------------------------------------------------------- */
        SampleFinder finder = new SampleFinder(resManager); 
        
        System.out.println(finder.count());
        
        List messages = finder.findAll();//返回数据库表中的所有数据
        
        /* 将结果构建为一个XML树，再打印整个XML结果集 */
        //Element messagesEL = new Element("Messages").addContent(messages);
        //logger.info(XMLHelper.toXMLString(messagesEL));
        logger.info("messages.size():"+messages.size());
        logger.info(messages);
        
        /* 循环编历XML结果集，并打印每一行记录 */
//        for(int i=0;i<allData.size();i++){
//          Element dataEL = (Element)allData.get(i);
//          logger.info(XMLHelper.toXMLString(dataEL));
//        }

        
        
        /* 条件检索数据库表数据 */
        Condition condition = new Condition();
        condition.add(new ConditionItem("Subject","like","%ID:0.5%"));
        condition.add(new ConditionItem("Sender","=","try"));
        
        messages = finder.findBy(condition);//查询满足条件的数据
        logger.info("messages.size():"+messages.size());
        logger.info(messages);
//        messagesEL = new Element("Messages").addContent(messages);
//        logger.info(XMLHelper.toXMLString(messagesEL));
        
        
//        try{
//          Thread.sleep(1000*60*3);
//        }catch(Exception e){
//          e.printStackTrace();
//        }

        
      }
}
