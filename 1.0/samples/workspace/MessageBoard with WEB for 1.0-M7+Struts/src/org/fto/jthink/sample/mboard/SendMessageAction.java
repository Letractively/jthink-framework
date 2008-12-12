package org.fto.jthink.sample.mboard;

import org.fto.jthink.sample.mboard.po.Message;

import com.opensymphony.xwork2.ActionContext;


/**
 * 发留言
 * 
 * @author wenjian
 *
 */
public class SendMessageAction extends Message{
  
  public String execute() throws Exception {
    //new MBoardBusinessBean().sendMessage();
    
    System.out.println("send message ...");
    System.out.println("Subject:"+getSubject());
    System.out.println("Content:"+getContent());
    return "success";
  }

}
