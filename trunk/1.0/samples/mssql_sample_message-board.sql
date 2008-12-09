/*
  �������ݿ⣬�����ݿ����������԰����ر���ֶ���Ϣ
*/

IF EXISTS (SELECT NAME FROM MASTER.DBO.SYSDATABASES WHERE NAME = N'MESSAGEBOARD')
	DROP DATABASE [MESSAGEBOARD]
GO

CREATE DATABASE [MESSAGEBOARD]
GO

USE [MESSAGEBOARD]
GO

/* MESSAGES�����԰����ݱ� */
CREATE TABLE MESSAGES( 
	ID		   INT	 NOT NULL PRIMARY KEY,   /* ID */
	SUBJECT            VARCHAR(100) ,	    /* ���� */
	CONTENT            VARCHAR(255)	 NOT NULL,  /* ���� */
	SENDER             VARCHAR(20) ,	    /* ������ */
	IP                 VARCHAR(20) ,	    /* �����ߵ�IP��ַ */
	EMAIL              VARCHAR(50),		    /* �����ߵĵ��������ַ */
	CONTACT            VARCHAR(50),	            /* ��������ϵ��ʽ */
	SENDTIME	   DATETIME,		    /* ����ʱ�� */

	REVERTCONTENT	   TEXT,	            /* �ظ����� */
	REVERTER	   VARCHAR(20) ,	    /* �ظ��� */
	REVERTTIME	   DATETIME		    /* �ظ�ʱ�� */

) 
GO

CREATE INDEX INDEX_SENDTIME
   ON MESSAGES (SENDTIME)

GO
