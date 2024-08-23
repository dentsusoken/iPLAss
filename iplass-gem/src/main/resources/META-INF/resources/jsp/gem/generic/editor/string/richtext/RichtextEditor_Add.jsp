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

<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" %>
<%@page import="org.iplass.gem.command.Constants"%>
<%@page import="org.iplass.mtp.view.generic.editor.StringPropertyEditor.RichTextLibrary"%>
<%
	// EditorのRichTextライブラリの取得
	RichTextLibrary richTextLibrary = (RichTextLibrary)request.getAttribute(Constants.EDITOR_RICHTEXT_LIBRARY);
	if (richTextLibrary == null) {
		richTextLibrary = (RichTextLibrary)request.getAttribute(Constants.EDITOR_RICHTEXT_DEFAULT_LIBRARY);
	}

	String path = null;
	switch (richTextLibrary) {
		case QUILL:
			path = "quill/Quill_Add.jsp";
			break;
		case CKEDITOR:
			path = "ckeditor/CKEditor_Add.jsp";
			break;
		default:
	}
%>
<jsp:include page="<%=path %>" />
