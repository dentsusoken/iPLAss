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
	// 編集画面での自動補完処理
	// StringPropertyAutocompletion.jspに組込み
	// multiplicity、propName、valueが定義されている
	
	Integer multiplicity = (Integer) request.getAttribute(Constants.AUTOCOMPLETION_MULTIPLICTTY);
	if (multiplicity == 1) {
%>
const id = $("[name='" + propName + "']").attr("id");
CKEDITOR.instances[id].setData(value);
<%
	} else {
%>
for (let i = 0; i < value.length; i++) {
	if ($("[name='" + propName + "']:eq(" + i + ")").length == 0) {
		$("#id_addBtn_" + propName).click();
	}
	const id = $("[name='" + propName + "']:eq(" + i + ")").attr("id");
	CKEDITOR.instances[id].setData(value[i]);
}
<%
	}
%>