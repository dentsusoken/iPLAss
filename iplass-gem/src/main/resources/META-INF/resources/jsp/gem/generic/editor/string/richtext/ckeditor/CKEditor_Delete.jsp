<%--
 Copyright (C) 2024 DENTSU SOKEN INC. All Rights Reserved.
 
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

<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" %>
<%@page import="org.iplass.gem.command.Constants"%>
<%@page import="org.iplass.mtp.util.StringUtil"%>
<%
	// 編集画面での削除処理

	String targetName = (String)request.getAttribute(Constants.EDITOR_RICHTEXT_TARGET_NAME);
%>
<script type="text/javascript">
function deleteRichtextItem_<%=StringUtil.escapeJavaScript(targetName)%>(targetId) {
	if (CKEDITOR.instances[targetId]) {
		CKEDITOR.instances[targetId].destroy();
	}
}
</script>
