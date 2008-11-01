/*
 * JThinkErrorCode.java	2005-6-25
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

/**
 * JThink-Framework中定义的异常代码。
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
 * @see 			org.fto.jthink.exception.JThinkRuntimeException
 * @see 			org.fto.jthink.exception.JThinkException
 * 
 */
public class JThinkErrorCode {
	
	/** 不可见的构造方法 */
	private JThinkErrorCode(){}
	
  /** 运行时异常, 不被允许的异常情况。值：100000 */
  public final static int ERRCODE_SYS_RUNTIME = 100000;
  /** 开始事物失败。值：100001 */
  public final static int ERRCODE_SYS_BEGIN_TRANSACTION_FAILURE = 100001;
  /** 事物提交失败。值：100002 */
  public final static int ERRCODE_SYS_COMMIT_TRANSACTION_FAILURE = 100002;
  /** 事物回退失败。值：100003 */
  public final static int ERRCODE_SYS_ROLLBACK_TRANSACTION_FAILURE = 100003;
  
  /** 不能连接到数据源。值：120000 */
  public final static int ERRCODE_DB_CANNOT_GET_DATASOURCE = 120000;
  /** 不能打开数据库连接。值：120001 */
  public final static int ERRCODE_DB_CANNOT_GET_CONNECTION = 120001;
  /** 关闭数据库连接失败。值：120002 */
  public final static int ERRCODE_DB_CLOSE_CONN_FAILURE = 120002;
  /** 关闭PreparedStatement失败。值：120003 */
  public final static int ERRCODE_DB_CLOSE_PSTMT_FAILURE = 120003;
  /** 关闭Statement失败。值：120004 */
  public final static int ERRCODE_DB_CLOSE_STMT_FAILURE = 120004; 
  /** 关闭ResultSet失败。值：120005 */
  public final static int ERRCODE_DB_CLOSE_RS_FAILURE = 120005; 
  /** 执行SQL时发生异常。值：120006 */
  public final static int ERRCODE_DB_EXEC_SQL_EXCEPTION = 120006;
  /** SQL异常,其它未知的SQL异常。值：120007 */
  public final static int ERRCODE_DB_SQL_EXCEPTION = 120007;

  /** 装载XML串失败, 可能是由于字符编码方式或非法的XML串格式引起的错误。值：130000 */
  public final static int ERRCODE_XML_XML_STRING_LOAD_FAILED = 130000;
  /** 装载XML文件失败, 可能是由于字符编码方式或非法的XML文件格式引起的错误。值：130001 */
  public final static int ERRCODE_XML_XML_FILE_LOAD_FAILED = 130001; 
  /** 装载XML失败,可能是由于字符编码方式或非法的XML格式引起的错误。值：130002 */
  public final static int ERRCODE_XML_XML_LOAD_FAILED = 130002; 
  /** 保存XML文件失败。值：130003 */
  public final static int ERRCODE_XML_XML_FILE_SAVE_FAILED = 130003; 
  /** 保存为XML串失败。值：130004 */
  public final static int ERRCODE_XML_XML_STRING_SAVE_FAILED = 130004;

  /** 查找EJB失败，可能是EJB没有准备好或JNDI错误。值：140000*/
  public final static int ERRCODE_JB_LOOKUP_EJB_FAILURE = 140000; //

}
