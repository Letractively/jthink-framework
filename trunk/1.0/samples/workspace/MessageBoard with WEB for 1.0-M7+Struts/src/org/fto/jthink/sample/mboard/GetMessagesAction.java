package org.fto.jthink.sample.mboard;

import java.util.List;

import org.fto.jthink.sample.mboard.po.Message;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * 列表留言信息
 * 
 * @author wenjian
 *
 */
public class GetMessagesAction{
  /**
   * 处理用户请求
   * @return
   * @throws Exception
   */
  public String execute() throws Exception {
    List messages = new MBoardBusinessBean().searchMessages();
    ActionContext.getContext().getValueStack().set("messages", messages);
    return "success";
  }
}
