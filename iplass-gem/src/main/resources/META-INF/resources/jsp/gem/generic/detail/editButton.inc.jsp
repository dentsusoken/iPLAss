<%--
 Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.

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
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ page import="org.iplass.mtp.entity.Entity"%>
<%@ page import="org.iplass.mtp.entity.definition.VersionControlType"%>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.generic.element.Button"%>
<%@ page import="org.iplass.mtp.view.generic.element.DisplayType"%>
<%@ page import="org.iplass.mtp.view.generic.DetailFormView"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewManager"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@ page import="org.iplass.mtp.view.generic.OutputType"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil"%>
<%@ page import="org.iplass.mtp.ManagerLocator"%>
<%@ page import="org.iplass.gem.command.generic.delete.DeleteCommand"%>
<%@ page import="org.iplass.gem.command.generic.detail.DetailFormViewData"%>
<%@ page import="org.iplass.gem.command.generic.detail.InsertCommand"%>
<%@ page import="org.iplass.gem.command.generic.detail.UpdateCommand"%>
<%@ page import="org.iplass.gem.command.Constants"%>
<%@ page import="org.iplass.gem.command.GemResourceBundleUtil"%>
<%@ page import="org.iplass.gem.command.ViewUtil"%>
<%
	DetailFormViewData data = (DetailFormViewData) request.getAttribute(Constants.DATA);

	DetailFormView form = data.getView();

	String defName = data.getEntityDefinition().getName();
	String viewName = form.getName();
	String execType = data.getExecType();

	//ビュー名があればアクションの後につける
	String urlPath = ViewUtil.getParamMappingPath(defName, viewName);

	//追加アクション
	String insert = "";
	if (StringUtil.isNotBlank(form.getInsertActionName())) {
		insert = form.getInsertActionName() + urlPath;
	} else {
		insert = InsertCommand.INSERT_ACTION_NAME + urlPath;
	}

	//更新アクション
	String update = "";
	if (StringUtil.isNotBlank(form.getUpdateActionName())) {
		update = form.getUpdateActionName() + urlPath;
	} else {
		update = UpdateCommand.UPDATE_ACTION_NAME + urlPath;
	}

	//削除アクション
	String delete = "";
	if (StringUtil.isNotBlank(form.getDeleteActionName())) {
		delete = form.getDeleteActionName() + urlPath;
	} else {
		delete = DeleteCommand.ACTION_NAME + urlPath;
	}

	EntityViewManager evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
%>

<ul class="list_operation edit-bar">
<%
	if (form.getButtons().size() > 0) {
		for (Button button : form.getButtons()) {
			boolean isDisplayCustomType = false;
			if (button.getDisplayType() == DisplayType.CUSTOM) {
				isDisplayCustomType = evm.isDisplayButton(data.getEntityDefinition().getName(), button.getCustomDisplayTypeScriptKey(), OutputType.EDIT, data.getEntity());
			}
			Entity rootEntity = (Entity) request.getAttribute(Constants.ROOT_ENTITY);
			if (EntityViewUtil.isDisplayElement(defName, button.getElementRuntimeId(), OutputType.EDIT, rootEntity)
					&& (button.getDisplayType() == null || button.getDisplayType() == DisplayType.DETAIL
						|| button.getDisplayType() == DisplayType.BOTH || isDisplayCustomType)
					&& ViewUtil.dispElement(button)) {
				String cssClass = button.isPrimary() ? "gr-btn" : "gr-btn-02";
				if (StringUtil.isNotBlank(button.getStyle())) {
					cssClass = button.getStyle();
				}
				String customStyle = "";
				if (StringUtil.isNotBlank(button.getInputCustomStyle())) {
					Object value = request.getAttribute(Constants.ENTITY_DATA);
					Entity entity = value instanceof Entity ? (Entity) value : null;
					String scriptKey = (String)request.getAttribute(Constants.FORM_SCRIPT_KEY);
					customStyle = EntityViewUtil.getCustomStyle(defName, scriptKey, button.getInputCustomStyleScriptKey(), entity, null);
				}
				String buttonDisplayLabel = TemplateUtil.getMultilingualString(button.getDisplayLabel(), button.getLocalizedDisplayLabelList());
%>
<%-- XSS対応-メタの設定のため対応なし(button.getOnclickEvent) --%>
<li class="btn">
<input type="button" value="<c:out value="<%=buttonDisplayLabel %>"/>" class="<c:out value="<%=cssClass %>"/>" 
	style="<c:out value="<%=customStyle %>"/>" onclick="<%=button.getOnclickEvent() %>" />
</li>
<%
			}
		}
	}
	if (Constants.EXEC_TYPE_UPDATE.equals(execType)) {
		if (data.isCanUpdate()) {
			String updateDisplayLabel = GemResourceBundleUtil.resourceString("generic.detail.detailButton.save");
			String localizedUpdateDisplayLabel = TemplateUtil.getMultilingualString(form.getUpdateDisplayLabel(), form.getLocalizedUpdateDisplayLabelList());
			if (StringUtil.isNotBlank(localizedUpdateDisplayLabel)) {
				updateDisplayLabel = localizedUpdateDisplayLabel;
			}
%>
<li class="btn save-btn">
<input type="button" class="gr-btn disabled-btn" value="<c:out value="<%=updateDisplayLabel %>"/>" 
	disabled="disabled" onclick="onclick_save('<%=StringUtil.escapeJavaScript(update) %>', this)" />
</li>
<%
		}
		if (data.isCanDelete() && !form.isHideDelete()) {
			String deleteDisplayLabel = GemResourceBundleUtil.resourceString("generic.detail.detailButton.delete");
			String localizedDeleteDisplayLabel = TemplateUtil.getMultilingualString(form.getDeleteDisplayLabel(), form.getLocalizedDeleteDisplayLabelList());
			if (StringUtil.isNotBlank(localizedDeleteDisplayLabel)) {
				deleteDisplayLabel = localizedDeleteDisplayLabel;
			}
%>
<li class="btn delete-btn">
<input type="button" class="gr-btn disabled-btn" value="<c:out value="<%=deleteDisplayLabel %>"/>" 
	disabled="disabled" onclick="confirm_delete('<%=StringUtil.escapeJavaScript(delete) %>', this)" />
</li>
<%
		}
		if (data.getEntityDefinition().getVersionControlType() != VersionControlType.NONE
				&& data.isCanDelete() && form.isShowDeleteSpecificVersion()) {
			String displayLabel = GemResourceBundleUtil.resourceString("generic.detail.detailButton.deleteSpecificVersion");
			String localizedDisplayLabel = TemplateUtil.getMultilingualString(form.getDeleteSpecificVersionDisplayLabel(), form.getLocalizedDeleteSpecificVersionDisplayLabelList());
			if (StringUtil.isNotBlank(localizedDisplayLabel)) {
				displayLabel = localizedDisplayLabel;
			}
%>
<li class="btn delete-version-btn">
<input type="button" class="gr-btn disabled-btn" value="<c:out value="<%=displayLabel %>"/>" 
	disabled="disabled" onclick="confirm_delete('<%=StringUtil.escapeJavaScript(delete) %>', this, {'<%=DeleteCommand.DELETE_SPECIFIC_VERSION%>':true})" />
</li>
<%
		}
	} else {
		if (data.isCanCreate()) {
			String insertDisplayLabel = GemResourceBundleUtil.resourceString("generic.detail.detailButton.reg");
			String localizedInsertDisplayLabel = TemplateUtil.getMultilingualString(form.getInsertDisplayLabel(), form.getLocalizedInsertDisplayLabelList());
			if (StringUtil.isNotBlank(localizedInsertDisplayLabel)) {
				insertDisplayLabel = localizedInsertDisplayLabel;
			}
%>
<li class="btn insert-btn">
<input type="button" class="gr-btn disabled-btn" value="<c:out value="<%=insertDisplayLabel %>"/>" 
	disabled="disabled" onclick="onclick_insert('<%=StringUtil.escapeJavaScript(insert) %>', this)" />
</li>
<%
		}
	}
%>
<li class="mt05 cancel-link">
<a href="javascript:void(0)" onclick="cancel();return false;">${m:rs("mtp-gem-messages", "generic.detail.detailButton.cancel")}</a>
</li>
</ul>
