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

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="org.iplass.mtp.ManagerLocator"%>
<%@page import="org.iplass.mtp.entity.SelectValue" %>
<%@page import="org.iplass.mtp.entity.definition.LocalizedSelectValueDefinition"%>
<%@page import="org.iplass.mtp.entity.definition.properties.selectvalue.SelectValueDefinition"%>
<%@page import="org.iplass.mtp.entity.definition.properties.selectvalue.SelectValueDefinitionManager"%>
<%@page import="org.iplass.mtp.entity.definition.PropertyDefinition"%>
<%@page import="org.iplass.mtp.entity.definition.PropertyDefinitionType"%>
<%@page import="org.iplass.mtp.entity.definition.properties.ExpressionProperty"%>
<%@page import="org.iplass.mtp.entity.definition.properties.SelectProperty"%>
<%@page import="org.iplass.mtp.view.generic.editor.SelectPropertyEditor" %>
<%@page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@page import="org.iplass.mtp.view.generic.OutputType"%>
<%@page import="org.iplass.mtp.util.StringUtil"%>
<%@page import="org.iplass.mtp.impl.core.ExecuteContext"%>
<%@page import="org.iplass.gem.command.Constants"%>

<%
	SelectPropertyEditor editor = (SelectPropertyEditor) request.getAttribute(Constants.EDITOR_EDITOR);
	OutputType type = (OutputType) request.getAttribute(Constants.OUTPUT_TYPE);

	PropertyDefinition pd = (PropertyDefinition) request.getAttribute(Constants.EDITOR_PROPERTY_DEFINITION);

	if (pd == null || !(pd instanceof SelectProperty)) {
		//定義がSelectPropertyか、Expression(resultType=Select)でなければ表示不可
		if (pd instanceof ExpressionProperty) {
			ExpressionProperty ep = (ExpressionProperty) pd;
			if (ep.getResultType() != PropertyDefinitionType.SELECT) {
				return;
			}
		} else {
			return;
		}
	}

	if (pd instanceof SelectProperty) {
		SelectProperty sp = (SelectProperty) pd;
		List<SelectValue> selectValueList = sp.getSelectValueList();
		List<LocalizedSelectValueDefinition> localeValueList = sp.getLocalizedSelectValueList();

		//attributeにセット
		request.setAttribute(Constants.EDITOR_SELECT_VALUE_LIST, selectValueList);
		request.setAttribute(Constants.EDITOR_LOCAL_VALUE_LIST, localeValueList);

		//EditorにEditorValueが未指定の場合、SelectValueから生成
		if (editor.getValues().isEmpty()) {
			//言語は対象言語のみ取得
			editor.setValues(EntityViewUtil.createEditorValueList(sp, ExecuteContext.getCurrentContext().getLanguage()));
		}

	} else if (pd instanceof ExpressionProperty) {
		ExpressionProperty ep = (ExpressionProperty) pd;
		if (ep.getResultTypeSpec() != null
				&& ep.getResultTypeSpec() instanceof SelectProperty) {

			SelectProperty resultTypeSpec = (SelectProperty)ep.getResultTypeSpec();
			if (StringUtil.isEmpty(resultTypeSpec.getSelectValueDefinitionName())) {
				//Globalが未指定の場合、表示不可
				return;
			}

			SelectValueDefinitionManager svdm = ManagerLocator.getInstance().getManager(SelectValueDefinitionManager.class);
			SelectValueDefinition svd = svdm.get(resultTypeSpec.getSelectValueDefinitionName());
			if (svd != null) {
				List<SelectValue> selectValueList = svd.getSelectValueList();
				List<LocalizedSelectValueDefinition> localeValueList = svd.getLocalizedSelectValueList();

				//attributeにセット
				request.setAttribute(Constants.EDITOR_SELECT_VALUE_LIST, selectValueList);
				request.setAttribute(Constants.EDITOR_LOCAL_VALUE_LIST, localeValueList);

				//EditorにEditorValueが未指定の場合、SelectValueから生成
				if (editor.getValues().isEmpty()) {
					//言語は対象言語のみ取得
					editor.setValues(EntityViewUtil.createEditorValueList(svd, ExecuteContext.getCurrentContext().getLanguage()));
				}
			} else {
				//Global定義が見つからないため、表示不可
				return;
			}
		}
	}

	String propName = editor.getPropertyName();

	//タイプ毎に出力内容かえる
	if (OutputType.EDIT == type) {
%>
<jsp:include page="./select/SelectPropertyEditor_Edit.jsp"></jsp:include>
<jsp:include page="ErrorMessage.jsp">
	<jsp:param value="<%=propName %>" name="propName" />
</jsp:include>
<%
	} else if (OutputType.VIEW == type) {
		//詳細表示
%>
<jsp:include page="./select/SelectPropertyEditor_View.jsp"></jsp:include>
<%
	} else if (OutputType.SEARCHCONDITION == type) {
		//検索条件
%>
<jsp:include page="./select/SelectPropertyEditor_Condition.jsp"></jsp:include>
<%
	} else {
		//検索結果
%>
<jsp:include page="./select/SelectPropertyEditor_View.jsp"></jsp:include>
<%
	}

	request.removeAttribute(Constants.EDITOR_EDITOR);
	request.removeAttribute(Constants.EDITOR_PROP_VALUE);
	request.removeAttribute(Constants.EDITOR_PROPERTY_DEFINITION);
	request.removeAttribute(Constants.EDITOR_SELECT_VALUE_LIST);
	request.removeAttribute(Constants.EDITOR_LOCAL_VALUE_LIST);
%>
