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

<%@page import="org.iplass.gem.command.ViewUtil"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>

<%@ page import="java.io.StringWriter"%>
<%@ page import="java.util.Map"%>
<%@ page import="org.iplass.mtp.entity.*"%>
<%@ page import="org.iplass.mtp.entity.definition.*" %>
<%@ page import="org.iplass.mtp.entity.definition.properties.*"%>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.generic.editor.*" %>
<%@ page import="org.iplass.mtp.view.generic.editor.ReferencePropertyEditor.ReferenceDisplayType" %>
<%@ page import="org.iplass.mtp.view.generic.editor.StringPropertyEditor.StringDisplayType"%>
<%@ page import="org.iplass.mtp.view.generic.element.property.*" %>
<%@ page import="org.iplass.mtp.view.generic.*" %>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil"%>
<%@ page import="org.iplass.mtp.ManagerLocator"%>
<%@ page import="org.iplass.gem.command.generic.detail.DetailViewCommand"%>
<%@ page import="org.iplass.gem.command.Constants"%>
<%@ page import="org.iplass.mtp.impl.web.WebResponseWrapper"%>
<%!
	boolean isDispProperty(PropertyDefinition pd, NestProperty property) {
		if (property.getEditor() == null) return false;
		return true;
	}
%>
<%
	ReferencePropertyEditor editor = (ReferencePropertyEditor) request.getAttribute(Constants.EDITOR_EDITOR);
	Object value = request.getAttribute(Constants.EDITOR_PROP_VALUE);
	OutputType type = (OutputType) request.getAttribute(Constants.OUTPUT_TYPE);
	PropertyDefinition pd = (PropertyDefinition) request.getAttribute(Constants.EDITOR_PROPERTY_DEFINITION);

	if (pd == null || !(pd instanceof ReferenceProperty)) {
		//定義がReferencePropertyでなければ表示不可
		return;
	}

	String contextPath = TemplateUtil.getTenantContextPath();
	String urlPath = ViewUtil.getParamMappingPath(editor.getObjectName(), editor.getViewName());
	//表示用のアクション
	String view = "";
	if (StringUtil.isNotBlank(editor.getViewrefActionName())) {
		view = contextPath + "/" + editor.getViewrefActionName() + urlPath;
	} else {
		view = contextPath + "/" + DetailViewCommand.REF_VIEW_ACTION_NAME + urlPath;
	}

	boolean refEdit = false;
	if (editor.isEditableReference()) {
		refEdit = true;
	}

	String propName = editor.getPropertyName();

	//タイプ毎に表示内容かえる
	if (OutputType.EDIT == type) {
		//詳細編集
%>
<jsp:include page="./reference/ReferencePropertyEditor_Edit.jsp" />
<jsp:include page="ErrorMessage.jsp">
	<jsp:param value="<%=propName %>" name="propName" />
</jsp:include>
<%
	} else if (OutputType.VIEW == type) {
		//詳細表示
%>
<jsp:include page="./reference/ReferencePropertyEditor_View.jsp" />
<%
	} else if (OutputType.SEARCHCONDITION == type) {
		//検索条件
%>
<jsp:include page="./reference/ReferencePropertyEditor_Condition.jsp" />
<%
	} else {
		//検索結果
// 		if (pd.getMultiplicity() != 1) {
//			複数表示不可→検索が出来ない
// 		} else {
			Entity entity = value instanceof Entity ? (Entity) value : null;
			if (editor.getNestProperties().size() == 0) {
				if (entity != null && entity.getName() != null) {
					String linkId = propName + "_" + entity.getOid();
					if (editor.getDisplayType() == ReferenceDisplayType.LABEL) {
						//ラベルの場合はリンクにしない
%>
<c:out value="<%=entity.getName() %>" />
<%
					} else {
%>
<a href="javascript:void(0)" class="modal-lnk" id="<c:out value="<%=linkId %>" />" onclick="showReference('<%=StringUtil.escapeJavaScript(view)%>', '<%=StringUtil.escapeJavaScript(editor.getObjectName())%>', '<%=StringUtil.escapeJavaScript(entity.getOid())%>', '<%=entity.getVersion() %>', '<%=StringUtil.escapeJavaScript(linkId)%>', <%=refEdit %>)"><c:out value="<%=entity.getName() %>" /></a>
<%
					}
				}
			} else if (editor.getNestProperties().size() > 0) {
				@SuppressWarnings("unchecked") Map<String, String> eval = (Map<String, String>) request.getAttribute(Constants.EDITOR_REF_ENTITY_VALUE_MAP);
				ReferenceProperty rp = (ReferenceProperty) pd;
				EntityDefinitionManager edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
				EntityDefinition ed = edm.get(rp.getObjectDefinitionName());
				for (NestProperty np : editor.getNestProperties()) {
					String key = editor.getPropertyName() + "." + np.getPropertyName();
					PropertyDefinition _pd = ed.getProperty(np.getPropertyName());
					if (isDispProperty(_pd, np)) {
						if (Entity.NAME.equals(np.getPropertyName())) {
							if (entity != null && entity.getName() != null) {
								String linkId = propName + "_" + entity.getOid();
								//こっちはリンクのままに
								//コマンド側で参照先のプロパティか判断するためフラグ設定
								request.setAttribute(Constants.EDITOR_REF_NEST_PROPERTY_PREFIX + editor.getPropertyName(), true);
								if (np.getEditor() instanceof StringPropertyEditor
										&& ((StringPropertyEditor) np.getEditor()).getDisplayType() == StringDisplayType.LABEL) {
%>
<c:out value="<%=entity.getName() %>" />
<%
								} else {
%>
<a href="javascript:void(0)" class="modal-lnk" id="<c:out value="<%=linkId %>" />" onclick="showReference('<%=StringUtil.escapeJavaScript(view)%>', '<%=StringUtil.escapeJavaScript(editor.getObjectName())%>', '<%=StringUtil.escapeJavaScript(entity.getOid())%>', '<%=entity.getVersion() %>', '<%=StringUtil.escapeJavaScript(linkId)%>', <%=refEdit %>)"><c:out value="<%=entity.getName() %>" /></a>
<%
								}
							}
						} else {
							Object rValue = entity != null ? entity.getValue(_pd.getName()) : null;
							PropertyEditor npEditor = np.getEditor();
							npEditor.setPropertyName(key);
							request.setAttribute(Constants.EDITOR_EDITOR, npEditor);
							request.setAttribute(Constants.EDITOR_PROP_VALUE, rValue);
							request.setAttribute(Constants.EDITOR_PROPERTY_DEFINITION, _pd);
							String path =  EntityViewUtil.getJspPath(npEditor, ViewConst.DESIGN_TYPE_GEM);
							if (path != null) {
								//プロパティ単位でhtmlを保持する仕組みの為、ネストの項目がうまく表示されない
								//ネストの項目は個別にhtmlを保持する
								//→直接htmlとして出力できないので、出力するケースが出てきた場合は対応が必要
								RequestDispatcher dispatcher = request.getRequestDispatcher(path);
								WebResponseWrapper res = new WebResponseWrapper(response, new StringWriter());
								dispatcher.include(request, res);
								String html = res.toString().replace("\r\n", "").replace("\n", "").replace("\r", "");
								Boolean isNest = (Boolean) request.getAttribute(Constants.EDITOR_REF_NEST_PROPERTY_PREFIX + npEditor.getPropertyName());
								if (isNest != null && isNest) {
									eval.put(key + ".name", html);
								} else {
									eval.put(key, html);
								}

							}
						}
					}
				}
			}
// 		}
	}

	request.removeAttribute(Constants.EDITOR_EDITOR);
	request.removeAttribute(Constants.EDITOR_PROP_VALUE);
	request.removeAttribute(Constants.EDITOR_PROPERTY_DEFINITION);
%>
