package org.fto.jthink.sample.mboard;

import java.util.Iterator;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.fto.jthink.sample.mboard.po.Message;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;


/**
 * 发留言
 * 
 * @author wenjian
 *
 */
public class SendMessageAction extends ActionSupport{

  private static final long serialVersionUID = -3893519792398428369L;

  public String execute() throws Exception {
    //setIP(ServletActionContext.getRequest().getRemoteAddr());
    //new MBoardBusinessBean().sendMessage(this);
    return "success";
  }

  //public void validate(){
  //  System.out.println("test ...");
//    //ActionSupport as = new ActionSupport().
//    ActionContext.getContext().getValueStack().
//    addFieldError("password", ActionSupport.this.getText("SendForm.Required.Subject"));
  //}
  
}
