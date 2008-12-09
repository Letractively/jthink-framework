<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<%@page import="org.fto.jthink.log.*" %>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<TITLE>error.jsp</TITLE>
</HEAD>
<body >
	<BR>错误消息:
<hr size="1" noshade>
<%
	Logger logger = LogManager.getLogger("error.jsp");
	if(exception!=null){
		out.println(exception.getMessage());
		logger.error(exception.getMessage(), exception);
	}
%>
	<hr size="1" noshade>
	<input name="" type="button" value="返回" onClick="history.back()" />
</body>
</HTML>
