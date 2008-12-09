<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" errorPage="/error.jsp"%>
<%@page import="org.jdom.*" %>
<%@page import="org.fto.jthink.util.*" %>
<%@page import="org.fto.jthink.j2ee.web.util.*" %>
<%@page import="org.fto.jthink.sample.mboard.*" %>
<%@page import="org.fto.jthink.sample.mboard.po.*" %>
<% 
	MBoardJBean mboardJBean = new MBoardJBean(request);
	String action = request.getParameter("ACTION");
	action = action==null?"":action;
	if(action.equals("EDIT")){
		mboardJBean.revertMessage();
		response.sendRedirect("index.jsp");
	} 

	Message message = mboardJBean.getRevertInfo();
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>留言板-回复</title>
</head>

<body>
<table width="100%" height="35" border="0" cellpadding="0" cellspacing="0" bgcolor="#006699">
  <tr> 
    <td width="200">&nbsp;&nbsp;<strong><font color="#FFFFFF">留言板</font></strong></td>
    <td align="right" valign="bottom"><a href="http://cosoft.org.cn/projects/jthink" target="_blank"><font color="#99FF00" size="2">JThink-Framework</font></a><font color="#99FF00" size="2">&nbsp;例子程序</font><font color="#99FF00" size="2">，作者：</font><a href="mailto:try_wen@yahoo.com.cn"><font color="#99FF00" size="2">wenjian</font></a></td>
  </tr>
</table>
<table width="100%" height="10" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td></td>
  </tr>
</table>
<form name="form1" method="post" action="?ACTION=EDIT&ID=<%=request.getParameter("ID")%>">
  <table width="100%" border="0" cellspacing="3" cellpadding="2">
    <tr> 
      <td height="25" colspan="2" bgcolor="#999999">&nbsp;&nbsp;<strong>回复</strong></td>
    </tr>
    <tr> 
      <td width="100" height="23" align="right" bgcolor="#CCCCCC">回复内容</td>
      <td height="23"><textarea name="RevertContent" cols="60" rows="5" id="RevertContent"><%=HTMLHelper.toHTMLString(message.getRevertContent(), HTMLHelper.ES_DEF$INPUT)%></textarea>
        *</td>
    </tr>
    <tr> 
      <td height="23" align="right" bgcolor="#CCCCCC">回复者</td>
      <td height="23"><input name="Reverter" type="text" id="Reverter" size="20" maxlength="20" value="<%=HTMLHelper.toHTMLString(message.getReverter(), HTMLHelper.ES_DEF$INPUT)%>">
        * </td>
    </tr>
    <tr> 
      <td width="100" height="23" align="right">&nbsp;</td>
      <td height="23"> <input type="submit" name="Submit" value="提交">
        <input type="button" value="返回" onClick="history.back()"> </td>
    </tr>
  </table>
</form>
<hr noshade="noshade" size="1" color="#CCCCCC"/>
<table align="center">
  <tr> 
    <td colspan="2" align="center"> <font size="-1">&nbsp; </font> </td>
  </tr>
  <tr> 
    <td colspan="2" align="center"> <font size="-1"> <em>Copyright &#169; 2003-2005, 
      <a href="http://www.free-think.org" target="_blank">Free Think Organization-FTO</a> 
      </em> </font> </td>
  </tr>
</table>
</body>
</html>
