/*
  例子数据库，此数据库描述了留言板的相关表和字段信息
*/

DROP DATABASE IF EXISTS MessageBoard;
CREATE DATABASE `MessageBoard` DEFAULT CHARACTER SET utf8;
Use MessageBoard;

/* Messages（留言板数据表） */
Create table Messages( 
	ID		   int	 not null,	    /* ID */
	Subject            varchar(100) ,	    /* 主题 */
	Content            varchar(255) not null,   /* 内容 */
	Sender             varchar(20) ,	    /* 留言者 */
	IP                 varchar(20) ,	    /* 留言者的IP地址 */
	Email              varchar(50),		    /* 留言者的电子邮箱地址 */
	Contact            varchar(50),		    /* 留言者联系方式 */
	SendTime	   datetime,		    /* 留言时间 */

	RevertContent	   text,	            /* 回复内容 */
	Reverter	   varchar(20) ,	    /* 回复者 */
	RevertTime	   datetime,		    /* 回复时间 */

	PRIMARY KEY  (ID),
	UNIQUE KEY Index_ID (ID),
	KEY Index_SendTime (SendTime)
	
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
