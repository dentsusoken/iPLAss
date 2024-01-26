<%--
 Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
 
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

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" uri="http://iplass.org/tags/mtp"%>
<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>

<%@ page import="org.iplass.mtp.web.WebRequestConstants"%>
<%@ page import="org.iplass.mtp.ApplicationException"%>
<%@ page import="org.iplass.gem.command.Constants"%>
<%@ page import="org.iplass.gem.command.GemResourceBundleUtil"%>
<%@ page import="org.iplass.gem.command.ViewUtil"%>
<%
	String selectType = request.getParameter(Constants.SELECT_TYPE);

	Exception e = (Exception) request.getAttribute(WebRequestConstants.EXCEPTION);
	String message = (String) request.getAttribute(Constants.MESSAGE);
	if (message == null) {
		if (e != null && e instanceof ApplicationException) {
			message = e.getMessage();
		} else {
			message = GemResourceBundleUtil.resourceString("generic.error.retryMsg");
		}
	}

	boolean isEditable = true;
	if (selectType != null) {
		isEditable = false;
	}
%>
<div class="error-block">
<h2 class="hgroup-01">
<span>
<i class="far fa-circle default-icon"></i>
</span>
${m:rs("mtp-gem-messages", "generic.error.errOccurred")}
</h2>
<table class="tbl-error mb10">
<tbody>
<tr>
<td>
<c:out value="<%=message%>"/><br />
</td>
</tr>
</tbody>
</table>
<p><a href="javascript:void(0)" onclick="historyBack();">${m:rs("mtp-gem-messages", "generic.error.back")}</a></p>
</div>
<script type="text/javascript">
function historyBack() {
	if ($("body.modal-body").length != 0) {
		document.rootDocument.scriptContext["closeModalFunction"].call(this);
	} else {
		history.back();
	}
}
</script>
