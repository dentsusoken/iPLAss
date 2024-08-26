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
	// NestTableでの追加処理
	// NestTableの追加時に列が追加された際のCallbackを登録する

	String nestPropName = (String) request.getAttribute(Constants.EDITOR_REF_NEST_PROP_NAME);

	boolean allowedContent = (boolean)request.getAttribute(Constants.EDITOR_RICHTEXT_ALLOWED_CONTENT);
	String editorOption = (String)request.getAttribute(Constants.EDITOR_RICHTEXT_EDITOR_OPTION);
	String targetName = (String)request.getAttribute(Constants.EDITOR_RICHTEXT_TARGET_NAME);
%>
<script type="text/javascript">
addNestRow_ColumnCallback("<%=StringUtil.escapeJavaScript(nestPropName) %>", "<%=StringUtil.escapeJavaScript(targetName) %>", 
		function($td, index) {

			const $textArea = $td.children("textarea");
			const editorId = $textArea.attr("id");

<%	if (StringUtil.isNotBlank(editorOption)) {%>
			const opt = <%=editorOption%>;
<%	} else {%>
			const opt = { allowedContent:<%=allowedContent%> };
<%	}%>
			$("#" + editorId).ckeditor(function() {}, opt);
		}
);
</script>
