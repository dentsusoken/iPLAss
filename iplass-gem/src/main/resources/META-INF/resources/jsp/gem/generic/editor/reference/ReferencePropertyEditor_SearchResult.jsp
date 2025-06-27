<%--
 Copyright (C) 2020 DENTSU SOKEN INC. All Rights Reserved.

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

<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>

<%@page import="java.io.StringWriter"%>
<%@page import="java.util.Map"%>
<%@page import="org.iplass.gem.command.Constants"%>
<%@page import="org.iplass.gem.command.ViewUtil"%>
<%@page import="org.iplass.gem.command.generic.detail.DetailViewCommand"%>
<%@page import="org.iplass.mtp.ManagerLocator"%>
<%@page import="org.iplass.mtp.entity.Entity"%>
<%@page import="org.iplass.mtp.entity.definition.EntityDefinition"%>
<%@page import="org.iplass.mtp.entity.definition.EntityDefinitionManager"%>
<%@page import="org.iplass.mtp.entity.definition.PropertyDefinition"%>
<%@page import="org.iplass.mtp.entity.definition.properties.ReferenceProperty"%>
<%@page import="org.iplass.mtp.impl.web.WebResponseWrapper"%>
<%@page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@page import="org.iplass.mtp.view.generic.ViewConst"%>
<%@page import="org.iplass.mtp.view.generic.editor.NestProperty"%>
<%@page import="org.iplass.mtp.view.generic.editor.PropertyEditor"%>
<%@page import="org.iplass.mtp.view.generic.editor.ReferencePropertyEditor"%>
<%@page import="org.iplass.mtp.view.generic.editor.ReferencePropertyEditor.ReferenceDisplayType"%>
<%@page import="org.iplass.mtp.view.generic.editor.StringPropertyEditor"%>
<%@page import="org.iplass.mtp.view.generic.editor.StringPropertyEditor.StringDisplayType"%>
<%@page import="org.iplass.mtp.util.StringUtil"%>
<%@page import="org.iplass.mtp.web.template.TemplateUtil"%>

<%!
	boolean isDispProperty(PropertyDefinition pd, NestProperty property) {
		if (property.getEditor() == null) return false;
		return true;
	}

	String getDisplayPropLabel(ReferencePropertyEditor editor, Entity refEntity) {
		String displayPropName = editor.getDisplayLabelItem();
		if (displayPropName == null) {
			displayPropName = Entity.NAME;
		}
		return refEntity.getValue(displayPropName);
	}
%>
<%
	ReferencePropertyEditor editor = (ReferencePropertyEditor) request.getAttribute(Constants.EDITOR_EDITOR);
	Object value = request.getAttribute(Constants.EDITOR_PROP_VALUE);
	PropertyDefinition pd = (PropertyDefinition) request.getAttribute(Constants.EDITOR_PROPERTY_DEFINITION);

	String propName = editor.getPropertyName();
	
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
	
	Entity rootEntity = (Entity) request.getAttribute(Constants.ROOT_ENTITY);
	String rootOid = rootEntity != null ? rootEntity.getOid() : "";
	String rootVersion = rootEntity != null && rootEntity.getVersion() != null ? rootEntity.getVersion().toString() : "";

	String rootDefName = (String) request.getAttribute(Constants.ROOT_DEF_NAME);
	String viewName = (String) request.getAttribute(Constants.VIEW_NAME);
	viewName = StringUtil.escapeHtml(viewName, true);
	
	// 検索結果である場合は viewType = SearchResult,
	// 大規模参照プロパティ検索である場合は viewType = Detail
	String viewType = (String) request.getAttribute(Constants.VIEW_TYPE);
	viewType = StringUtil.escapeHtml(viewType, true);

	Entity entity = value instanceof Entity ? (Entity) value : null;
	
	if (editor.getNestProperties().size() == 0) {
		if (entity != null) {
			if (editor.getDisplayType() == ReferenceDisplayType.HIDDEN) {
				//HIDDEN
				String key = entity.getOid() + "_" + entity.getVersion();
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=key %>"/>"/>
<%
			} else {
				//HIDDEN以外
				if (getDisplayPropLabel(editor, entity) != null) {
					String linkId = propName + "_" + entity.getOid();
					String displayPropLabel = getDisplayPropLabel(editor, entity);
					if (editor.getDisplayType() == ReferenceDisplayType.LABEL) {
						//ラベルの場合はリンクにしない
%>
<c:out value="<%=displayPropLabel %>" />
<%
					} else {
						String showReference = "showReference(" 
							+ "'" + StringUtil.escapeJavaScript(view) + "'" 
							+ ", '" + StringUtil.escapeJavaScript(editor.getObjectName()) + "'"
							+ ", '" + StringUtil.escapeJavaScript(entity.getOid()) + "'"
							+ ", '" + entity.getVersion() + "'"
							+ ", '" + StringUtil.escapeJavaScript(linkId) + "'"
							+ ", " + refEdit
							+ ", null"
							+ ", '" + StringUtil.escapeJavaScript(rootDefName) + "'"
							+ ", '" + StringUtil.escapeJavaScript(viewName) + "'"
							+ ", '" + StringUtil.escapeJavaScript(propName) + "'"
							+ ", '" + StringUtil.escapeJavaScript(viewType) + "'"
							+ ", null"
							+ ", '" + StringUtil.escapeJavaScript(rootOid) + "'"
							+ ", '" + StringUtil.escapeJavaScript(rootVersion) + "'"
							+ ")";
%>
<a href="javascript:void(0)" class="modal-lnk" id="<c:out value="<%=linkId %>" />" data-linkId="<c:out value="<%=linkId %>" />"
 onclick="<c:out value="<%=showReference %>"/>"><c:out value="<%=displayPropLabel %>" /></a>
<%
					}
				}
			}
		}
	} else if (editor.getNestProperties().size() > 0) {
		@SuppressWarnings("unchecked") Map<String, String> eval = (Map<String, String>) request.getAttribute(Constants.EDITOR_REF_ENTITY_VALUE_MAP);
		ReferenceProperty rp = (ReferenceProperty) pd;
		EntityDefinitionManager edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
		EntityDefinition ed = edm.get(rp.getObjectDefinitionName());
		for (NestProperty np : editor.getNestProperties()) {
			String key = editor.getPropertyName() + "." + np.getPropertyName();
			PropertyDefinition _pd = EntityViewUtil.getNestTablePropertyDefinition(np, ed);
			if (isDispProperty(_pd, np)) {
				PropertyEditor npEditor = np.getEditor();
				if (!npEditor.isHide() 
						&& Entity.NAME.equals(np.getPropertyName())) {
					//NestPropertyのEditorがHIDDENではなく、NAMEの場合
					
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
							String showReference = "showReference(" 
								+ "'" + StringUtil.escapeJavaScript(view) + "'" 
								+ ", '" + StringUtil.escapeJavaScript(editor.getObjectName()) + "'"
								+ ", '" + StringUtil.escapeJavaScript(entity.getOid()) + "'"
								+ ", '" + entity.getVersion() + "'"
								+ ", '" + StringUtil.escapeJavaScript(linkId) + "'"
								+ ", " + refEdit
								+ ", null"
								+ ", '" + StringUtil.escapeJavaScript(rootDefName) + "'"
								+ ", '" + StringUtil.escapeJavaScript(viewName) + "'"
								+ ", '" + StringUtil.escapeJavaScript(propName) + "'"
								+ ", '" + StringUtil.escapeJavaScript(viewType) + "'"
								+ ", null"
								+ ", '" + StringUtil.escapeJavaScript(rootOid) + "'"
								+ ", '" + StringUtil.escapeJavaScript(rootVersion) + "'"
								+ ")";
%>
<a href="javascript:void(0)" class="modal-lnk" id="<c:out value="<%=linkId %>" />" data-linkId="<c:out value="<%=linkId %>" />"
 onclick="<c:out value="<%=showReference %>"/>"><c:out value="<%=entity.getName() %>" /></a>
<%
						}
					}
				} else {
					Object rValue = entity != null ? entity.getValue(_pd.getName()) : null;
					npEditor.setPropertyName(key);
					request.setAttribute(Constants.EDITOR_EDITOR, npEditor);
					request.setAttribute(Constants.EDITOR_PROP_VALUE, rValue);
					request.setAttribute(Constants.EDITOR_PROPERTY_DEFINITION, _pd);
					request.setAttribute(Constants.EDITOR_REF_NEST, true);//2重ネスト防止用フラグ
					request.setAttribute(Constants.EDITOR_REF_NEST_VALUE, entity);//JoinProperty用
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
					request.removeAttribute(Constants.EDITOR_REF_NEST);
					request.removeAttribute(Constants.EDITOR_REF_NEST_VALUE);
				}
			}
		}
	}
%>