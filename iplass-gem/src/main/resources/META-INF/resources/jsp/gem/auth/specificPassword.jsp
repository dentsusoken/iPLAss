<%--
 Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.

 Unless you have purchased a commercial license,
 the following license terms apply:

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as
 published by the Free Software Foundation, either version 3 of the
 License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program. If not, see <https://www.gnu.org/licenses/>.
 --%>

<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" uri="http://iplass.org/tags/mtp"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>

<%@ page import="org.iplass.mtp.auth.AuthContext"%>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.web.WebRequestConstants"%>
<%@ page import="org.iplass.gem.command.auth.AuthCommandConstants"%>
<%@ page import="org.iplass.gem.command.auth.ResetSpecificPasswordCommand"%>
<%@ page import="org.iplass.gem.command.Constants"%>

<%
	String modalTarget = request.getParameter(Constants.MODAL_TARGET);
	modalTarget = StringUtil.escapeHtml(modalTarget);
	if (modalTarget == null) modalTarget = "";

	String oid = request.getParameter(Constants.OID);
	oid = StringUtil.escapeHtml(oid);

	String execType = request.getParameter(Constants.EXEC_TYPE);
	execType = StringUtil.escapeHtml(execType);

	Exception e = (Exception) request.getAttribute(AuthCommandConstants.RESULT_ERROR);
	String errorMessage = null;
	if (e != null) {
		errorMessage = e.getMessage();
	}
	if (errorMessage == null) {
		errorMessage = (String) request.getAttribute(Constants.MESSAGE);
	}
	if (errorMessage == null) {
		errorMessage = "";
	}

%>
<script type="text/javascript">
<!--
$(function () {
	$("#resetRandomPassword").on("change", function(){
		if($(this).is(":checked")){
			$("input[type=password][name='<%=AuthCommandConstants.PARAM_NEW_PASSWORD%>']").prop("disabled",true);
			$("input[type=password][name='<%=AuthCommandConstants.PARAM_CONFIRM_PASSWORD%>']").prop("disabled",true);
		} else {
			$("input[type=password][name='<%=AuthCommandConstants.PARAM_NEW_PASSWORD%>']").prop("disabled",false);
			$("input[type=password][name='<%=AuthCommandConstants.PARAM_CONFIRM_PASSWORD%>']").prop("disabled",false);
		}
	});
});
//-->
</script>
<div class="user-profile">
<h3 class="hgroup-02 hgroup-02-01">${m:rs("mtp-gem-messages", "auth.Password.enterPass")}</h3>
<%
if ("SUCCESS".equals(request.getAttribute(WebRequestConstants.COMMAND_RESULT))) {
%>
	<div class="completePasswordChange">
		<span>${m:rs("mtp-gem-messages", "auth.Password.successMsg")}</span>
	</div>
<%
}
%>
<div class="error" style="color:red;">
<c:out value="<%=errorMessage%>"/>
</div>
<form id="resetSpecificPassForm" method="POST" action="${m:tcPath()}/<%=ResetSpecificPasswordCommand.ACTION_RESET_SPECIFIC_PASSWORD%>">
${m:outputToken('FORM_XHTML', true)}
<% if (oid != null) {%>
<input type="hidden" name="oid" value="<%=oid%>" />
<% }%>
<% if (execType != null) {%>
<input type="hidden" name="execType" value="<%=execType%>" />
<% }%>
<% if (modalTarget != null) {%>
<input type="hidden" name="modalTarget" value="<%=modalTarget%>" />
<% }%>
<div class="formArchive">
<div>
<table class="tbl-maintenance mb10">
<tbody>
<tr>
<th>${m:rs("mtp-gem-messages", "auth.Password.newPass")}</th>
<td><input type="password" name="<%=AuthCommandConstants.PARAM_NEW_PASSWORD%>" value="" class="form-size-01 inpbr" /></td>
</tr>
<tr>
<th>${m:rs("mtp-gem-messages", "auth.Password.cnfrmNewPass")}</th>
<td><input type="password" name="<%=AuthCommandConstants.PARAM_CONFIRM_PASSWORD%>" value="" class="form-size-01 inpbr" /></td>
</tr>
<tr>
<th></th>
<td><label><input type="checkbox" id="resetRandomPassword" name="<%=AuthCommandConstants.PARAM_RESET_RANDOM_PASSWORD%>" value="1" />${m:rs("mtp-gem-messages", "auth.SpecificPassword.resetRandomPass")}</label></td>
</tr>
</tbody>
</table>
</div>
</div>
<div class="operation-bar operation-bar_bottom">
<ul class="list_operation">
<li class="btn"><input type="submit" value="${m:rs('mtp-gem-messages', 'auth.SpecificPassword.reset')}" class="gr-btn" /></li>
<li class="mt05 cancel-link"><a href="javascript:void(0)" onclick="cancel();return false;">${m:rs("mtp-gem-messages", "auth.SpecificPassword.cancel")}</a></li>
</ul>
</div>
</form>
</div>
<script type="text/javascript">
var key = "<%=modalTarget%>";
var modalTarget = key != "" ? key : null;
function cancel() {
	if (!modalTarget) {
		$("#modal-dialog-root .modal-close", parent.document).click();
	} else {
		$("#modal-dialog-" + modalTarget + " .modal-close", parent.document).click();
	}
}
$(function(){
	if (!modalTarget) {
		$("#modal-title", parent.document).text("${m:rs('mtp-gem-messages', 'auth.SpecificPassword.resetPass')}");
	} else {
		$("#modal-title-" + modalTarget, parent.document).text("${m:rs('mtp-gem-messages', 'auth.SpecificPassword.resetPass')}");
	}
	$("#resetSpecificPassForm").submit(function(){
		if(window.confirm("${m:rs('mtp-gem-messages', 'auth.SpecificPassword.passResetConfirm')}")) {
			return true;
		}
		return false;
	})
})
</script>