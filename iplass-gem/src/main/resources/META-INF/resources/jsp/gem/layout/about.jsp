<%--
 Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.

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
<%@ page import="org.iplass.gem.command.GemResourceBundleUtil"%>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil"%>
<%
String modalTarget = request.getParameter("modalTarget");
if (modalTarget == null) modalTarget = "";
modalTarget = StringUtil.escapeJavaScript(modalTarget);
%>
<h3 class="hgroup-02 hgroup-02-01 about-title">${m:rs("mtp-gem-messages", "layout.about.title")}</h3>
<div class="formArchive about-contents">
<c:forEach var="line" items="${notice}">
<p class="about-content"><c:out value="${line}" /></p>
</c:forEach>
</div>

<script type="text/javascript">
var key = "<%=modalTarget%>";
var modalTarget = key != "" ? key : null;
$(function() {
	var target = null;
	var windowManager = document.rootDocument.scriptContext["windowManager"];
	if (modalTarget && windowManager && windowManager[document.targetName]) {
		$("#modal-title-" + modalTarget, parent.document).text("${m:rsp("mtp-gem-messages", "layout.header.about", appName)}");
		target = windowManager[modalTarget];
	} else {
		$("#modal-title", parent.document).text("${m:rsp("mtp-gem-messages", "layout.header.about", appName)}");
		target = parent.document;
	}
});
</script>
