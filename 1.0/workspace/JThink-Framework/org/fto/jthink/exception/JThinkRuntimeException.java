/*
 * JThinkRuntimeException.java	2005-6-25
 *
 * 版权所有: 自由思考组织(FTO)软件团队 2000-2005, 保留所有权利.
 * 这个软件是自由思考组织(FTO)软件团队开发的，如果要使用这个软件，请首先阅读并接受许可协议。
 *
 * Copyright 2000-2005 FTO Software Team, Inc. All Rights Reserved.
 * This software is the proprietary information of FTO Software Team, Inc.
 * Use is subject to license terms.
 *
 * FTO站点：http://www.free-think.org
 */

package org.fto.jthink.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * JThink-Framework中定义的运行时异常, 在JThink中的所有异常情况都将以此异常对象抛出。
 *
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2005-06-25  创建此类型
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 * @see 			java.lang.RuntimeException
 * 
 */

public class JThinkRuntimeException extends RuntimeException {

  private static final long serialVersionUID = 8082438544282367978L;

  /* 异常编码，见org.fto.jthink.exception.JThinkErrorCode */  
	private int code = 0;
	
	/** 被JThinkRuntimeException包含的最初的异常对象 */
	public Throwable detail;
	  
	  /**
	   * 构造方法
	   */
	  public JThinkRuntimeException() {
	  	super();
	  }

	  
	  /**
	   * 构造方法
	   *
	   * @param message   异常描述信息
	   */
	  public JThinkRuntimeException(String message) {
	    super(message);
	  }


	  /**
	   * 构造方法
	   */
	  public JThinkRuntimeException(Throwable e) {
	    super(e.getMessage());
	    detail = e;
	  }
	  
	  
	  
	  /**
	   * 构造方法
	   *
	   * @param c         异常编码
	   * @param message   异常描述信息
	   */
	  public JThinkRuntimeException(int c, String message) {
	    super(message);
	    code = c;
	  }

	  /**
	   * 构造方法
	   *
	   * @param c         异常编码
	   * @param e         当前实际的异常对象
	   */
	  public JThinkRuntimeException(int c, Throwable e) {
	    super(e.getMessage());
	    code = c;
	    detail = e;
	  }
	  /**
	   * 构造方法
	   *
	   * @param message   异常描述信息
	   * @param e         当前实际的异常对象
	   * 
	   */
	  public JThinkRuntimeException(String message, Throwable e) {
	    super(message);
	    detail = e;
	  }

	  /**
	   * 构造方法
	   *
	   * @param c         异常编码
	   * @param message   异常描述信息
	   * @param e         当前实际的异常对象
	   * 
	   */
	  public JThinkRuntimeException(int c, String message, Throwable e) {
	    super(message);
	    code = c;
	    detail = e;
	  }

	  
	  
	  /**
	   * 返回异常消息
	   * 
	   * @return 如果没有嵌套子异常对象；则否，将输出子异常对象信息
	   */
	  public String getMessage() {
	  	if (detail == null) {
		    return super.getMessage();
			} else {
			  return super.getMessage() + "; nested exception is: \n\t" + detail.toString();
			}
	  }

	  /**
	   * 返回异常编码
	   */
	  public int getCode() {
	    return code;
	  }

	  /**
	   * 返回实际的异常对象,如果没有,返回自己
	   * 
	   */
    public Throwable getCause() {
      return detail==null?this:detail;
    }

	  /**
	   * 打印异常跟踪信息
	   */
	  public void printStackTrace(){
	  	super.printStackTrace();
	    if(detail!=null){
	    	detail.printStackTrace();
	    }
	  }

	  /**
	   * 打印异常跟踪信息
	   */
	  public void printStackTrace(PrintStream ps){
	  	super.printStackTrace(ps);
	    if(detail!=null){
	    	detail.printStackTrace(ps);
	    }
	  }

	  /**
	   * 打印异常跟踪信息
	   */
	  public void printStackTrace(PrintWriter pw){
	  	super.printStackTrace(pw);
	    if(detail!=null){
	    	detail.printStackTrace(pw);
	    }
	  }
	
	
}
