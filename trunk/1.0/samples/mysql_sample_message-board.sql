/*
  �������ݿ⣬�����ݿ����������԰����ر���ֶ���Ϣ
*/

DROP DATABASE IF EXISTS MessageBoard;
CREATE DATABASE `MessageBoard` DEFAULT CHARACTER SET utf8;
Use MessageBoard;

/* Messages�����԰����ݱ� */
Create table Messages( 
	ID		   int	 not null,	    /* ID */
	Subject            varchar(100) ,	    /* ���� */
	Content            varchar(255) not null,   /* ���� */
	Sender             varchar(20) ,	    /* ������ */
	IP                 varchar(20) ,	    /* �����ߵ�IP��ַ */
	Email              varchar(50),		    /* �����ߵĵ��������ַ */
	Contact            varchar(50),		    /* ��������ϵ��ʽ */
	SendTime	   datetime,		    /* ����ʱ�� */

	RevertContent	   text,	            /* �ظ����� */
	Reverter	   varchar(20) ,	    /* �ظ��� */
	RevertTime	   datetime,		    /* �ظ�ʱ�� */

	PRIMARY KEY  (ID),
	UNIQUE KEY Index_ID (ID),
	KEY Index_SendTime (SendTime)
	
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
