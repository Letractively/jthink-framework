/*
  例子数据库，此数据库描述了留言板的相关表和字段信息
*/

IF EXISTS (SELECT NAME FROM MASTER.DBO.SYSDATABASES WHERE NAME = N'MESSAGEBOARD')
	DROP DATABASE [MESSAGEBOARD]
GO

CREATE DATABASE [MESSAGEBOARD]
GO

USE [MESSAGEBOARD]
GO

/* MESSAGES（留言板数据表） */
CREATE TABLE MESSAGES( 
	ID		   INT	 NOT NULL PRIMARY KEY,   /* ID */
	SUBJECT            VARCHAR(100) ,	    /* 主题 */
	CONTENT            VARCHAR(255)	 NOT NULL,  /* 内容 */
	SENDER             VARCHAR(20) ,	    /* 留言者 */
	IP                 VARCHAR(20) ,	    /* 留言者的IP地址 */
	EMAIL              VARCHAR(50),		    /* 留言者的电子邮箱地址 */
	CONTACT            VARCHAR(50),	            /* 留言者联系方式 */
	SENDTIME	   DATETIME,		    /* 留言时间 */

	REVERTCONTENT	   TEXT,	            /* 回复内容 */
	REVERTER	   VARCHAR(20) ,	    /* 回复者 */
	REVERTTIME	   DATETIME		    /* 回复时间 */

) 
GO

CREATE INDEX INDEX_SENDTIME
   ON MESSAGES (SENDTIME)

GO
