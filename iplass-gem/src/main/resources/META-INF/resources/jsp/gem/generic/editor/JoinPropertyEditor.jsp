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
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ page import="java.util.List" %>
<%@ page import="org.iplass.mtp.ManagerLocator"%>
<%@ page import="org.iplass.mtp.entity.Entity"%>
<%@ page import="org.iplass.mtp.entity.definition.*" %>
<%@ page import="org.iplass.mtp.entity.ValidateError" %>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewUtil" %>
<%@ page import="org.iplass.mtp.view.generic.OutputType"%>
<%@ page import="org.iplass.mtp.view.generic.ViewConst"%>
<%@ page import="org.iplass.mtp.view.generic.editor.JoinPropertyEditor" %>
<%@ page import="org.iplass.mtp.view.generic.parser.Token" %>
<%@page import="org.iplass.gem.command.Constants"%>

<%
	JoinPropertyEditor editor = (JoinPropertyEditor) request.getAttribute(Constants.EDITOR_EDITOR);
	OutputType type = (OutputType) request.getAttribute(Constants.OUTPUT_TYPE);

	Entity entity = request.getAttribute(Constants.ENTITY_DATA) instanceof Entity ? (Entity) request.getAttribute(Constants.ENTITY_DATA) : null;

	String rootDefName = (String)request.getAttribute(Constants.ROOT_DEF_NAME);
	String scriptKey = (String)request.getAttribute(Constants.SECTION_SCRIPT_KEY);

	Boolean nest = (Boolean) request.getAttribute(Constants.EDITOR_REF_NEST);
	EntityDefinition ed = (EntityDefinition) request.getAttribute(Constants.ENTITY_DEFINITION);
	String propName = editor.getPropertyName();

	ValidateError[] errors = (ValidateError[]) request.getAttribute(Constants.ERROR_PROP);

	String prefix = null;
	if (nest != null && nest) {
		EntityDefinition _ed = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class).get(editor.getObjectName());
		request.setAttribute(Constants.ENTITY_DEFINITION, _ed);

		//ダミーのプロパティ名と本来のプロパティ名を分離して、出力時に結合するプロパティを含め再設定する
		int lastIndex = propName.lastIndexOf(".");
		prefix = propName.substring(0, lastIndex);
		editor.setPropertyName(propName.substring(lastIndex + 1));
		request.setAttribute(Constants.EDITOR_JOIN_NEST_PREFIX, prefix);
	}

	String customStyle = "";
	if (type == OutputType.VIEW) {
		if (StringUtil.isNotEmpty(editor.getCustomStyle())) {
			customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getOutputCustomStyleScriptKey(), entity, null);
		}
	} else if (type == OutputType.EDIT) {
		//入力不可の場合
		if (StringUtil.isNotEmpty(editor.getInputCustomStyle())) {
			customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getInputCustomStyleScriptKey(), entity, null);
		}
	}

	if (StringUtil.isNotEmpty(customStyle)) {
%>
<span style="<c:out value="<%=customStyle %>"/>">
<%
	}

	if (editor.isShowNestPropertyErrors() && errors != null) {
		//検証エラーリストをnullに設定します。
		request.setAttribute(Constants.ERROR_PROP, null);
	}

	request.setAttribute(ViewConst.DESIGN_TYPE, ViewConst.DESIGN_TYPE_GEM);

	List<Token> tokenList = EntityViewUtil.perse(editor);
	int i = 0;
	for (Token token : tokenList) {
		String key = "";
		if (token.getKey() != null) {
			key = "join_" + propName + "_" + token.getKey() + "_" + i;
			key = key.replaceAll("\\.", "_");
			i++;
		}
%>
<span id="<c:out value="<%=key %>" />">
<%
		token.printOut(pageContext);
%>
</span>
<%
	}

	if (editor.isShowNestPropertyErrors() && errors != null) {
		ValidateError[] tmp = EntityViewUtil.collectNestPropertyErrors(editor, prefix, errors);
		request.setAttribute(Constants.ERROR_PROP, tmp);
		String joinPropName = "join_" + propName;

		if (tmp != null) {
%>
<jsp:include page="ErrorMessage.jsp">
	<jsp:param value="<%=joinPropName %>" name="propName" />
</jsp:include>
<%
		}

		// 検証エラーリストを元に戻します。
		request.setAttribute(Constants.ERROR_PROP, errors);
	}

	if (StringUtil.isNotEmpty(customStyle)) {
%>
</span>
<%
	}

	request.setAttribute(Constants.ENTITY_DEFINITION, ed);

	request.removeAttribute(Constants.EDITOR_EDITOR);
	request.removeAttribute(Constants.EDITOR_PROP_VALUE);
	request.removeAttribute(Constants.EDITOR_PROPERTY_DEFINITION);
	request.removeAttribute(Constants.EDITOR_DISPLAY_LABEL);
%>
