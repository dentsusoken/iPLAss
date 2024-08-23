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

<%@taglib prefix="m" uri="http://iplass.org/tags/mtp"%>
<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" %>
<%@page import="org.iplass.gem.GemConfigService"%>
<%@page import="org.iplass.gem.command.Constants"%>
<%@page import="org.iplass.mtp.spi.ServiceRegistry"%>
<%@page import="org.iplass.mtp.view.generic.editor.StringPropertyEditor.RichTextLibrary"%>
<%
// デフォルトのRichTextライブラリの取得
RichTextLibrary defaultRichTextLibrary = (RichTextLibrary)request.getAttribute(Constants.EDITOR_RICHTEXT_DEFAULT_LIBRARY);
if (defaultRichTextLibrary == null) {
	GemConfigService gemConfigService = ServiceRegistry.getRegistry().getService(GemConfigService.class);
	defaultRichTextLibrary = gemConfigService.getRichTextLibrary();
	request.setAttribute(Constants.EDITOR_RICHTEXT_DEFAULT_LIBRARY, defaultRichTextLibrary);
}

// EditorのRichTextライブラリの取得
RichTextLibrary richTextLibrary = (RichTextLibrary)request.getAttribute(Constants.EDITOR_RICHTEXT_LIBRARY);
if (richTextLibrary == null) {
	richTextLibrary = defaultRichTextLibrary;
}

if (request.getAttribute(Constants.RICHTEXT_LIB_LOADED + "_" + richTextLibrary.name()) == null) {

	String templateName = null;
	switch (richTextLibrary) {
		case QUILL:
			templateName = Constants.TEMPLATE_EDITOR_RICHTEXT_QUILL;
			break;
		case CKEDITOR:
			templateName = Constants.TEMPLATE_EDITOR_RICHTEXT_CKEDITOR;
			break;
		default:
	}
%>
<m:include template="<%=templateName %>" /> 
<%
	request.setAttribute(Constants.RICHTEXT_LIB_LOADED + "_" + richTextLibrary.name(), true);
}
%>