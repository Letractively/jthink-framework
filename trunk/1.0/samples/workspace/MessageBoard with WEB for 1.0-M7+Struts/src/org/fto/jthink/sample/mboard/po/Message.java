package org.fto.jthink.sample.mboard.po;

import org.fto.jthink.jdbc.DataObject;

public class Message extends DataObject {

  private static final long serialVersionUID = 8914525096052347979L;
  
  public Message(){
  	this.setTableName("Messages");
  	this.setPrimaryKey("ID");
  	this.setFieldNameCoincident(false);
  }
//  ID       int   not null,      /* ID */
//  Subject            varchar(100) ,     /* 主题 */
//  Content            varchar(255) not null,   /* 内容 */
//  Sender             varchar(20) ,      /* 留言者 */
//  IP                 varchar(20) ,      /* 留言者的IP地址 */
//  Email              varchar(50),       /* 留言者的电子邮箱地址 */
//  Contact            varchar(50),       /* 留言者联系方式 */
//  SendTime     datetime,        /* 留言时间 */
//
//  RevertContent    text,              /* 回复内容 */
//  Reverter     varchar(20) ,      /* 回复者 */
//  RevertTime     datetime,        /* 回复时间 */
  
	public void setId(String id){
		set("ID", id);
	}
	public String getId(){
	  return get("ID");
	}
  public void setSubject(String subject){
    set("Subject", subject);
  }
  public String getSubject(){
    return get("Subject");
  }
  public void setContent(String content){
    set("Content", content);
  }
  public String getContent(){
    return get("Content");
  }
  public void setSender(String sender){
    set("Sender", sender);
  }
  public String getSender(){
    return get("Sender");
  }
  public void setIP(String ip){
    set("IP", ip);
  }
  public String getIP(){
    return get("IP");
  }
  public void setEmail(String email){
    set("Email", email);
  }
  public String getEmail(){
    return get("Email");
  }
  public void setContact(String contact){
    set("Contact", contact);
  }
  public String getContact(){
    return get("Contact");
  }
  public void setSendTime(String sendTime){
    set("SendTime", sendTime);
  }
  public String getSendTime(){
    return get("SendTime");
  }
  public void setRevertContent(String revertContent){
    set("RevertContent", revertContent);
  }
  public String getRevertContent(){
    return get("RevertContent");
  }
  public void setReverter(String reverter){
    set("Reverter", reverter);
  }
  public String getReverter(){
    return get("Reverter");
  }
  public void setRevertTime(String revertTime){
    set("RevertTime", revertTime);
  }
  public String getRevertTime(){
    return get("RevertTime");
  }
   
}
