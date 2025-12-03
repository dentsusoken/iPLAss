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

<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="m" uri="http://iplass.org/tags/mtp"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>

<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>
<%@ page import="org.iplass.mtp.entity.Entity" %>
<%@ page import="org.iplass.mtp.entity.permission.EntityPermission"%>
<%@ page import="org.iplass.mtp.entity.permission.EntityPropertyPermission"%>
<%@ page import="org.iplass.mtp.auth.AuthContext"%>
<%@ page import="org.iplass.mtp.auth.User"%>
<%@ page import="org.iplass.mtp.entity.definition.EntityDefinition"%>
<%@ page import="org.iplass.mtp.entity.definition.EntityDefinitionManager"%>
<%@ page import="org.iplass.mtp.entity.EntityManager"%>
<%@ page import="org.iplass.mtp.view.generic.editor.*"%>
<%@ page import="org.iplass.mtp.view.generic.element.property.PropertyItem"%>
<%@ page import="org.iplass.mtp.view.generic.element.section.*"%>
<%@ page import="org.iplass.mtp.view.generic.element.Element"%>
<%@ page import="org.iplass.mtp.view.generic.DetailFormView"%>
<%@ page import="org.iplass.mtp.view.generic.EntityView"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewManager"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@ page import="org.iplass.mtp.view.generic.OutputType"%>
<%@ page import="org.iplass.mtp.view.generic.ViewConst"%>
<%@ page import="org.iplass.mtp.view.top.parts.UserMaintenanceParts"%>
<%@ page import="org.iplass.mtp.view.top.parts.TopViewParts"%>
<%@ page import="org.iplass.mtp.view.top.TopViewDefinition"%>
<%@ page import="org.iplass.mtp.view.top.TopViewDefinitionManager"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.mtp.web.WebRequestConstants"%>
<%@ page import="org.iplass.mtp.ManagerLocator"%>
<%@ page import="org.iplass.gem.command.auth.AuthCommandConstants"%>
<%@ page import="org.iplass.gem.command.auth.UpdatePasswordCommand"%>
<%@ page import="org.iplass.gem.command.auth.UpdateUserCommand"%>
<%@ page import="org.iplass.gem.command.Constants"%>
<%@ page import="org.iplass.gem.command.ViewUtil"%>
<%!

	List<PropertyItem> getProperty(DetailFormView view, Entity entity) {
		List<PropertyItem> propList = new ArrayList<PropertyItem>();
		for (Section section : view.getSections()) {
			if (section instanceof DefaultSection
					&& EntityViewUtil.isDisplayElement(User.DEFINITION_NAME, section.getElementRuntimeId(), OutputType.EDIT, entity)
					&& ViewUtil.dispElement(Constants.EXEC_TYPE_UPDATE, section)) {
				propList.addAll(getProperty((DefaultSection) section, entity));
			}
		}
		return propList;
	}

	List<PropertyItem> getProperty(DefaultSection section, Entity entity) {
		List<PropertyItem> propList = new ArrayList<PropertyItem>();
		for (Element elem : section.getElements()) {
			if (elem instanceof PropertyItem) {
				PropertyItem prop = (PropertyItem) elem;
				if (EntityViewUtil.isDisplayElement(User.DEFINITION_NAME, prop.getElementRuntimeId(), OutputType.EDIT, entity)
						&& ViewUtil.dispElement(Constants.EXEC_TYPE_UPDATE, prop)) propList.add(prop);
			} else if (elem instanceof DefaultSection) {
				propList.addAll(getProperty((DefaultSection) elem, entity));
			}
		}
		return propList;
	}
%>
<%
	Exception e = (Exception) request.getAttribute(AuthCommandConstants.RESULT_ERROR);
	String errorMessage = null;
	if (e != null) {
		errorMessage = e.getMessage();
	}
	if (errorMessage == null) {
		errorMessage = (String) request.getAttribute(Constants.MESSAGE);
	}
	if (errorMessage == null) {
		errorMessage = "";
	}

	//ユーザー情報メンテナンス用
	String defName = User.DEFINITION_NAME;
	AuthContext auth = AuthContext.getCurrentContext();

	String roleName = (String) request.getSession().getAttribute(Constants.ROLE_NAME);
	if (roleName == null) roleName = "DEFAULT";
	TopViewDefinitionManager manager = ManagerLocator.getInstance().getManager(TopViewDefinitionManager.class);
	TopViewDefinition td = manager.get(roleName);
	UserMaintenanceParts setting = null;
	if (td != null) {
		for (TopViewParts parts : td.getParts()) {
			if (parts instanceof UserMaintenanceParts) {
				setting = (UserMaintenanceParts) parts;
				break;
			}
		}
	}
	EntityDefinitionManager edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
	EntityViewManager evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
	EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);
	
	String styleAttr = setting != null ? ViewUtil.buildHeightStyleAttr(setting.getMaxHeight()) : "";

	User user = (User) em.load(auth.getUser().getOid(), auth.getUser().getVersion(), User.DEFINITION_NAME);
%>
<h2 class="hgroup-01">
<span>
<i class="far fa-circle default-icon"></i>
</span>
${m:rs("mtp-gem-messages", "auth.Password.changeUserInfo")}</h2>
<div class="user-profile">
<h3 class="hgroup-02 hgroup-02-01">${m:rs("mtp-gem-messages", "auth.Password.enterPass")}</h3>
<%
if (!"true".equals(request.getAttribute(Constants.UPDATE_USER_INFO))) {
	if ("SUCCESS".equals(request.getAttribute(WebRequestConstants.COMMAND_RESULT))) {
%>
		<div class="completePasswordChange">
			<span>${m:rs("mtp-gem-messages", "auth.Password.successMsg")}</span>
		</div>
<%
	}
%>
<div class="error" style="color:red;">
<c:out value="<%= errorMessage %>"/>
</div>
<%
}
%>
<form method="POST" action="${m:tcPath()}/<%=UpdatePasswordCommand.ACTION_DO_UPDATE_PASSWORD%>">
<div class="formArchive">
<div>
<table class="tbl-maintenance tbl-section mb10">
<tbody><tr>
<th class="section-data col1">${m:rs("mtp-gem-messages", "auth.Password.curPass")}</th>
<td class="section-data col1">
<input type="password" name="<%= AuthCommandConstants.PARAM_PASSWORD %>" value="" class="form-size-01 inpbr" />
</td>
</tr>
<tr>
<th class="section-data col1">${m:rs("mtp-gem-messages", "auth.Password.newPass")}</th>
<td class="section-data col1"><input type="password" name="<%= AuthCommandConstants.PARAM_NEW_PASSWORD %>" value="" class="form-size-01 inpbr" /></td>
</tr>
<tr>
<th class="section-data col1">${m:rs("mtp-gem-messages", "auth.Password.cnfrmNewPass")}</th>
<td class="section-data col1"><input type="password" name="<%= AuthCommandConstants.PARAM_CONFIRM_PASSWORD %>" value="" class="form-size-01 inpbr" /></td>
</tr>
</tbody>
</table>
</div>
</div>
<p class="mb30"><input type="submit" value=${m:rs("mtp-gem-messages", "auth.Password.save")} class="gr-btn" /></p>
</form>

<%@include file="./webAuthnManage.inc.jsp" %>


<%
	if (user != null && setting != null) {
		EntityDefinition ed = edm.get(defName);
		EntityView view = evm.get(defName);
		DetailFormView form = view.getDetailFormView(setting.getViewName());
		List<PropertyItem> propList = getProperty(form, user);
		if (propList.size() > 0) {
			// 自身の情報更新は特権実行で行う
%>
<m:auth privileged="true">
<h3 class="hgroup-02 hgroup-02-01">${m:rs("mtp-gem-messages", "auth.Password.inputUserInfo")}</h3>
<form action="${m:tcPath()}/<%=UpdateUserCommand.ACTION_NAME%>" method="POST">
<input type="hidden" name="defName" value="<c:out value="<%=User.DEFINITION_NAME%>"/>" />
<input type="hidden" name="oid" value="<c:out value="<%=user.getOid()%>"/>" />
<input type="hidden" name="version" value="<c:out value="<%=user.getVersion()%>"/>" />
<input type="hidden" name="timestamp" value="<c:out value="<%=user.getUpdateDate().getTime()%>"/>" />
<input type="hidden" name="roleName" value="<c:out value="<%=roleName%>"/>" />
<%
if ("true".equals(request.getAttribute(Constants.UPDATE_USER_INFO))) {
	if ("SUCCESS".equals(request.getAttribute(WebRequestConstants.COMMAND_RESULT))) {
%>
		<div class="completePasswordChange">
			<span>${m:rs("mtp-gem-messages", "auth.Password.updateUserInfoMsg")}</span>
		</div>
<%
	}
%>
<div class="error" style="color:red;">
<c:out value="<%= errorMessage %>"/>
</div>
<%
}
%>
<div class="formArchive">
<div <%=styleAttr%>>
<table class="tbl-maintenance tbl-section">
<tbody>
<%
			request.setAttribute(Constants.DEF_NAME, defName);
			request.setAttribute(Constants.ROOT_DEF_NAME, defName); //NestTableの場合にDEF_NAMEが置き換わるので別名でRootのDefNameをセット
			request.setAttribute(Constants.ROOT_ENTITY, user); //NestTableの場合に内部の表示判定スクリプトで利用
			request.setAttribute(Constants.VIEW_NAME, setting.getViewName());
			request.setAttribute(Constants.OUTPUT_TYPE, OutputType.EDIT);
			request.setAttribute(Constants.ENTITY_DEFINITION, ed);
			request.setAttribute(Constants.EXEC_TYPE, Constants.EXEC_TYPE_UPDATE);
			request.setAttribute(Constants.ENTITY_DATA, user);
			for (PropertyItem property : propList) {
				boolean isPropertyUpdatable = auth.checkPermission(new EntityPropertyPermission(defName, property.getPropertyName(), EntityPropertyPermission.Action.UPDATE));
				if (isPropertyUpdatable) {
					request.setAttribute(Constants.ELEMENT, property);

					String path = EntityViewUtil.getJspPath(property, ViewConst.DESIGN_TYPE_GEM);
					if (path != null) {
%>
<tr>
<jsp:include page="<%=path %>" />
</tr>
<%
					}
				}
			}
%>
</tbody>
</table>
</div>
</div>
<p><input type="submit" value="${m:rs('mtp-gem-messages', 'auth.Password.save')}" class="gr-btn" /></p>
${m:outputToken('FORM_XHTML', true)}
</form>
</m:auth>
<%
		}
	}
%>
</div>
