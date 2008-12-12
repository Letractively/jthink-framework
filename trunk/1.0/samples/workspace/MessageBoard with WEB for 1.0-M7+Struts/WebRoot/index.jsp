<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" errorPage="/error.jsp"%>
<%@page import="java.util.*" %>
<%@page import="org.jdom.*" %>
<%@page import="org.fto.jthink.j2ee.web.util.*" %>
<%@page import="org.fto.jthink.util.*" %>
<%@page import="org.fto.jthink.sample.mboard.*" %>
<%@page import="org.fto.jthink.sample.mboard.po.*" %>
<%@page import="com.opensymphony.xwork2.util.ValueStack" %>
<% 

	//ValueStack vs = (ValueStack)request.getAttribute("struts.valueStack");
	//List messages = (List)vs.findValue("messages");

	//总行数
	int pageRowTotal = 10;//mboardJBean.getPageTotalRows();
	//当前页
	int pageOffset = 1;//mboardJBean.getPageOffset();
	//页行数
	int pageRowSize = 10;//mboardJBean.getPageRows();
	//总页数
	int pageTotal = 10;//mboardJBean.getPages(pageRowTotal);

%>

<%@taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:text name="Base.MessageBoard"/></title>
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
    <td width="200">&nbsp;&nbsp;<strong><font color="#FFFFFF"><s:text name="Base.MessageBoard"/></font></strong></td>
    <td align="right" valign="bottom"><a href="http://code.google.com/p/jthink-framework/" target="_blank"><font color="#99FF00" size="2">JThink-Framework</font></a><font color="#99FF00" size="2">&nbsp;<s:text name="Base.ExampleProgram"/></font><font color="#99FF00" size="2">，<s:text name="Base.Author"/>:</font><a href="mailto:try.jiwen@gmail.com"><font color="#99FF00" size="2"><s:text name="Base.AuthorWenJian"/></font></a></td>
  </tr>
</table>
<table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td></td>
  </tr>
</table>

<s:iterator value="messages" status="status">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	      <tr>
	        <td>${status.index+1}. <s:property value="Subject" /></td>
	        <td width="150" align="right">
	        	<a href="reverter.jsp?ID=<s:property value="Id" />"><s:text name="List.Action.Revert"/></a>&nbsp;&nbsp;<a href="javascript:deleteMessage(<s:property value="Id" />)"><s:text name="List.Action.Delete"/></a>
	        </td>
	      </tr>
	</table>
	<table width="100%" border="0" cellpadding="3" cellspacing="1" bgcolor="#666666">
	  <tr> 
	    <td width="120" height="50" align="right" bgcolor="#CCCCCC"><s:text name="List.Content"/></td>
	    <td height="50" valign="top" bgcolor="#FFFFFF"><s:property value="Content" /></td>
	  </tr>
	  <tr> 
	    <td width="120" height="26" bgcolor="#CCCCCC">&nbsp;</td>
	    <td height="26" bgcolor="#CCCCCC"><table width="100%" border="0" cellspacing="0" cellpadding="0">
	        <tr> 
	          <td width="160"><s:text name="List.Sender"/>:<a href="mailto:<s:property value="Email" />"><s:property value="Sender" /></a></td>
	          <td><s:text name="List.SendTime"/>:<s:property value="SendTime" /></td>
	          <td width="160"><s:text name="List.IP"/>:<s:property value="IP" /></td>
	        </tr>
	      </table></td>
	  </tr>
	  <tr> 
	    <td width="120" height="50" align="right" bgcolor="#CCCCCC"><s:text name="List.RevertContent"/></td>
	    <td height="50" valign="top" bgcolor="#FFFFFF"><s:property value="RevertContent" /></td>
	  </tr>
	  <tr> 
	    <td width="120" height="26" bgcolor="#CCCCCC">&nbsp;</td>
	    <td height="26" bgcolor="#CCCCCC"><table width="100%" border="0" cellspacing="0" cellpadding="0">
	        <tr> 
	          <td width="160"><s:text name="List.Reverter"/>:<s:property value="Reverter" /></td>
	          <td><s:text name="List.RevertTime"/>:<s:property value="RevertTime"  /></td>
	        </tr>
	      </table></td>
	  </tr>
	</table>
	<table width="100%" height="10" border="0" cellpadding="0" cellspacing="0">
	  <tr>
	    <td></td>
	  </tr>
	</table>
</s:iterator>
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
<table width="100%" border="0" cellspacing="3" cellpadding="2">
  <tr> 
    <td height="25" colspan="2" bgcolor="#999999">&nbsp;&nbsp;<strong><s:text name="SendForm.MessageTitle"/></strong></td>
  </tr>
</table>
<s:fielderror></s:fielderror> 
<s:form name="SendForm" action="send" method="post">
	
	<s:textfield name="Subject" label="%{getText('SendForm.Subject')}" size="50" maxlength="100"/>
	<s:textarea name="Content" label="%{getText('SendForm.Content')}" cols="60" rows="5"/>
	<s:textfield name="Sender" label="%{getText('SendForm.Sender')}" size="20" maxlength="20"/>
	<s:textfield name="Email" label="%{getText('SendForm.Email')}" size="30" maxlength="50"/>
	<s:textfield name="Contact" label="%{getText('SendForm.Contact')}" size="30" maxlength="50"/>
	<s:submit value="%{getText('SendForm.Action.Submit')}" align="left" />
</s:form>
<!-- 
<table width="100%" border="0" cellspacing="3" cellpadding="2">
  <tr> 
    <td height="25" colspan="2" bgcolor="#999999">&nbsp;&nbsp;<strong><s:text name="SendForm.MessageTitle"/></strong></td>
  </tr>
  <tr> 
    <td width="120" height="23" align="right" bgcolor="#CCCCCC"><s:text name="SendForm.Subject"/></td>
    <td height="23"><input name="Subject" type="text" id="Subject" size="50" maxlength="100"></td>
  </tr>
  <tr> 
    <td width="120" height="23" align="right" bgcolor="#CCCCCC"><s:text name="SendForm.Content"/></td>
    <td height="23"><textarea name="Content" cols="60" rows="5" id="Content"></textarea>
        *</td>
  </tr>
  <tr> 
    <td height="23" align="right" bgcolor="#CCCCCC"><s:text name="SendForm.Sender"/></td>
    <td height="23"><input name="Sender" type="text" id="Sender" size="20" maxlength="20">
        *</td>
  </tr>
  <tr> 
    <td height="23" align="right" bgcolor="#CCCCCC"><s:text name="SendForm.Email"/></td>
    <td height="23"><input name="Email" type="text" id="Email" size="30" maxlength="50"></td>
  </tr>
  <tr>
    <td height="23" align="right" bgcolor="#CCCCCC"><s:text name="SendForm.Contact"/></td>
    <td height="23"><input name="Contact" type="text" id="Contact" size="30" maxlength="50"></td>
  </tr>
  <tr> 
    <td width="120" height="23" align="right">&nbsp;</td>
    <td height="23">
        <input type="submit" name="Submit" value="<s:text name="SendForm.Action.Submit"/>">
      </td>
  </tr>
</table>
 -->

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
