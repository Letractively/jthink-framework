package org.fto.jthink.sample.mboard;

import java.util.Iterator;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.fto.jthink.sample.mboard.po.Message;

import com.opensymphony.xwork2.ActionContext;


/**
 * 发留言
 * 
 * @author wenjian
 *
 */
public class SendMessageAction extends Message{

  private static final long serialVersionUID = -3893519792398428369L;

  public String execute() throws Exception {
    setIP(ServletActionContext.getRequest().getRemoteAddr());
    new MBoardBusinessBean().sendMessage(this);
    return "success";
  }

}
