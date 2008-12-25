<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" errorPage="/error.jsp"%>
<%@page import="java.util.*" %>
<%@page import="org.jdom.*" %>
<%@page import="org.fto.jthink.j2ee.web.util.*" %>
<%@page import="org.fto.jthink.util.*" %>
<%@page import="org.fto.jthink.sample.mboard.*" %>
<%@page import="org.fto.jthink.sample.mboard.po.*" %>
<% 
	MBoardJBean mboardJBean = new MBoardJBean(request);
	String action = request.getParameter("ACTION");

	action = action==null?"":action;
	if(action.equals("DELETE")){
		mboardJBean.deleteMessage();
	}
	if(action.equals("NEW")){
		mboardJBean.sendMessage();
	}
	
	List messages = mboardJBean.searchMessages();

	//总行数
	int pageRowTotal = mboardJBean.getPageTotalRows();
	//当前页
	int pageOffset = mboardJBean.getPageOffset();
	//页行数
	int pageRowSize = mboardJBean.getPageRows();
	//总页数
	int pageTotal = mboardJBean.getPages(pageRowTotal);

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>留言板</title>
<style type="text/css">
<!--
.page {
	cursor: hand;
	color: #0000FF;
	text-decoration: underline;
}
-->
</style>
</head>
<script language="JavaScript">
   		/*
			分页，转到其它页，JavaScript脚本
		*/
		function goOtherPage(pageOffset){	
			if(pageOffset>0 && pageOffset<=<%=pageTotal%> && pageOffset!=<%=pageOffset%>){
				window.location = "?PAGE_OFFSET="+pageOffset+"&PAGE_ROW_TOTAL=<%=pageRowTotal%>";
			}
		}
		
		function deleteMessage(id){
			window.location = "?ACTION=DELETE&ID="+id+"&PAGE_OFFSET=<%=pageOffset%>&PAGE_ROW_TOTAL=<%=pageRowTotal%>";
		}
</script>

<body>
<table width="100%" height="35" border="0" cellpadding="0" cellspacing="0" bgcolor="#006699">
  <tr> 
    <td width="200">&nbsp;&nbsp;<strong><font color="#FFFFFF">留言板</font></strong></td>
    <td align="right" valign="bottom"><a href="http://code.google.com/p/jthink-framework/" target="_blank"><font color="#99FF00" size="2">JThink-Framework</font></a><font color="#99FF00" size="2">&nbsp;例子程序</font><font color="#99FF00" size="2">，作者：</font><a href="mailto:try.jiwen@gmail.com"><font color="#99FF00" size="2">WenJian(QQ:32312569,Mail:try.jiwen@gmail.com)</font></a></td>
  </tr>
</table>
<table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td></td>
  </tr>
</table>
<%
	for(int i=0;i<messages.size();i++){
		Message message = (Message)messages.get(i);

%>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td><%=mboardJBean.getSeqNo(i)%>. <%=HTMLHelper.toHTMLString(message.getSubject())%></td>
          <td width="100" align="right"><a href="reverter.jsp?ID=<%=message.getId()%>">回复</a>&nbsp;&nbsp;<a href="javascript:deleteMessage(<%=message.getId()%>)">删除</a></td>
        </tr>
</table>
<table width="100%" border="0" cellpadding="3" cellspacing="1" bgcolor="#666666">
  <tr> 
    <td width="100" height="50" align="right" bgcolor="#CCCCCC">留言内容</td>
    <td height="50" valign="top" bgcolor="#FFFFFF"><%=HTMLHelper.toHTMLString(message.getContent())%></td>
  </tr>
  <tr> 
    <td width="100" height="26" bgcolor="#CCCCCC">&nbsp;</td>
    <td height="26" bgcolor="#CCCCCC"><table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td width="160">留言者：<a href="mailto:<%=message.getEmail()%>"><%=HTMLHelper.toHTMLString(message.getSender())%></a></td>
          <td>时间：<%=DateTimeHelper.formatDateTimetoString(message.getSendTime())%></td>
          <td width="160">IP：<%=message.getIP()%></td>
        </tr>
      </table></td>
  </tr>
  <tr> 
    <td width="100" height="50" align="right" bgcolor="#CCCCCC">回复内容</td>
    <td height="50" valign="top" bgcolor="#FFFFFF"><%=HTMLHelper.toHTMLString(message.getRevertContent())%></td>
  </tr>
  <tr> 
    <td width="100" height="26" bgcolor="#CCCCCC">&nbsp;</td>
    <td height="26" bgcolor="#CCCCCC"><table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td width="160">回复者：<%=HTMLHelper.toHTMLString(message.getReverter())%></td>
          <td>时间：<%=DateTimeHelper.formatDateTimetoString(message.getRevertTime())%></td>
        </tr>
      </table></td>
  </tr>
</table>
<table width="100%" height="10" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td></td>
  </tr>
</table>
<%
	}
%>
<table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td align="right">
		共<font color="#FF3300"><%=pageTotal%></font>页 第<font color="#FF3300"><%=pageOffset%></font>页 
		<a onClick="javascript:goOtherPage(1)" class="page">首页</a>  	
    	<a onClick="goOtherPage(<%=pageOffset-1%>)" class="page">上页</a>
    	<a onClick="goOtherPage(<%=pageOffset+1%>)" class="page">下页</a>
		<a onClick="goOtherPage(<%=pageTotal%>)" class="page">末页</a> 
    	&nbsp;&nbsp;转到: 
		<select name="jump" onChange="goOtherPage(this.options[this.selectedIndex].value)">
			<option value=""></option>
			<%for(int p=1;p<=pageTotal;p++){%>
				<option value="<%=p%>">-<%=p%>-</option>
			<%}%>
		  </select>页
	  </td>
  </tr>
</table>
<table width="100%" height="10" border="0" cellpadding="0" cellspacing="0">
  <tr> 
    <td></td>
  </tr>
</table>
<form name="form1" method="post" action="?ACTION=NEW">
<table width="100%" border="0" cellspacing="3" cellpadding="2">
  <tr> 
    <td height="25" colspan="2" bgcolor="#999999">&nbsp;&nbsp;<strong>留言</strong></td>
  </tr>
  <tr> 
    <td width="100" height="23" align="right" bgcolor="#CCCCCC">主题</td>
    <td height="23"><input name="Subject" type="text" id="Subject" size="50" maxlength="100"></td>
  </tr>
  <tr> 
    <td width="100" height="23" align="right" bgcolor="#CCCCCC">留言</td>
    <td height="23"><textarea name="Content" cols="60" rows="5" id="Content"></textarea>
        *</td>
  </tr>
  <tr> 
    <td height="23" align="right" bgcolor="#CCCCCC">留言者</td>
    <td height="23"><input name="Sender" type="text" id="Sender" size="20" maxlength="20">
        *</td>
  </tr>
  <tr> 
    <td height="23" align="right" bgcolor="#CCCCCC">邮箱地址</td>
    <td height="23"><input name="Email" type="text" id="Email" size="30" maxlength="50"></td>
  </tr>
  <tr>
    <td height="23" align="right" bgcolor="#CCCCCC">联系方式</td>
    <td height="23"><input name="Contact" type="text" id="Contact" size="30" maxlength="50"></td>
  </tr>
  <tr> 
    <td width="100" height="23" align="right">&nbsp;</td>
    <td height="23">
        <input type="submit" name="Submit" value="提交">
      </td>
  </tr>
</table>
</form>
<hr noshade="noshade" size="1" color="#CCCCCC"/>
<table align="center">
   <tr>
     <td colspan="2" align="center">
        <font size="-1">&nbsp;
        </font>
     </td>
   </tr>
   <tr>
     
    <td colspan="2" align="center"> <font size="-1"> <em>Copyright &#169; 2003-2005, 
      <a href="http://www.free-think.org" target="_blank">Free Think Organization-FTO</a> 
      </em> </font> </td>
   </tr>
</table>


</body>
</html>
