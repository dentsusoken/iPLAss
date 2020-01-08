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

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" uri="http://iplass.org/tags/mtp"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>

<%@ page import="org.iplass.mtp.ManagerLocator"%>
<%@ page import="org.iplass.mtp.auth.AuthContext"%>
<%@ page import="org.iplass.mtp.entity.permission.EntityPermission"%>
<%@ page import="org.iplass.mtp.entity.definition.properties.ReferenceProperty"%>
<%@ page import="org.iplass.mtp.entity.definition.EntityDefinition"%>
<%@ page import="org.iplass.mtp.entity.Entity"%>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.generic.element.section.MassReferenceSection"%>
<%@ page import="org.iplass.mtp.view.generic.element.section.MassReferenceSection.MassReferenceEditType"%>
<%@ page import="org.iplass.mtp.view.generic.element.Element"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewManager"%>
<%@ page import="org.iplass.mtp.view.generic.OutputType"%>
<%@ page import="org.iplass.mtp.view.generic.PagingPosition"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil.TokenOutputType"%>
<%@ page import="org.iplass.gem.command.generic.detail.DetailFormViewData"%>
<%@ page import="org.iplass.gem.command.generic.detail.DetailViewCommand"%>
<%@ page import="org.iplass.gem.command.generic.detail.GetMassReferencesCommand"%>
<%@ page import="org.iplass.gem.command.generic.detail.UpdateMappedbyReferenceCommand"%>
<%@ page import="org.iplass.gem.command.Constants"%>
<%@ page import="org.iplass.gem.command.ViewUtil"%>
<%
	Element element = (Element) request.getAttribute(Constants.ELEMENT);
	Object value = request.getAttribute(Constants.ENTITY_DATA);
	OutputType type = (OutputType) request.getAttribute(Constants.OUTPUT_TYPE);
	EntityDefinition ed = (EntityDefinition) request.getAttribute(Constants.ENTITY_DEFINITION);

	DetailFormViewData data = (DetailFormViewData) TemplateUtil.getRequestContext().getAttribute(Constants.DATA);

	MassReferenceSection section = (MassReferenceSection) element;
	String propName = section.getPropertyName();

	if ((type == OutputType.EDIT && section.isHideDetail())
			|| (type == OutputType.VIEW && section.isHideView())) return;

	EntityViewManager evm = ManagerLocator.manager(EntityViewManager.class);

	Entity entity = value instanceof Entity ? (Entity) value : null;
	if (entity == null || entity.getOid() == null) return;//新規の場合しかないはず

	ReferenceProperty rp = (ReferenceProperty) ed.getProperty(propName);

	String id = "";
	if (StringUtil.isNotBlank(section.getId())) {
		id = section.getId();
	}

	String style = "";
	if (StringUtil.isNotBlank(section.getStyle())) {
		style = section.getStyle();
	}

	String disclosure = "";
	String disclosureStyle = "";
	if (!section.isExpandable()) {
		disclosure = " disclosure-close";
		disclosureStyle = "display: none;";
	}

	int limit = section.getLimit();
	if (limit == 0) limit = 10;

	String pagingPosition = PagingPosition.BOTH.name();
	if (section.getPagingPosition() != null) {
		pagingPosition = section.getPagingPosition().name();
	}

	//詳細画面のアクション設定
	String urlPath = ViewUtil.getParamMappingPath(rp.getObjectDefinitionName(), section.getViewName());

	String viewAction = "";
	if (StringUtil.isNotBlank(section.getViewActionName())) {
		viewAction = section.getViewActionName() +  urlPath;
	} else {
		viewAction = DetailViewCommand.REF_VIEW_ACTION_NAME + urlPath;
	}

	String detailAction = "";
	if (StringUtil.isNotBlank(section.getDetailActionName())) {
		detailAction = section.getDetailActionName() +  urlPath;
	} else {
		detailAction = DetailViewCommand.REF_DETAIL_ACTION_NAME + urlPath;
	}

	String title = TemplateUtil.getMultilingualString(section.getTitle(), section.getLocalizedTitleList());

	//ボタンの表示設定
	String addStyle = "";
	if (section.isHideAddButton()) addStyle = "display:none;";
	String delStyle = "";
	if (section.isHideDeleteButton()) delStyle = "display:none;";

	//編集タイプと出力タイプで編集可否を決定
	String _type = "";
	if (type == OutputType.EDIT) {
		if (section.getEditType() == null || section.getEditType() == MassReferenceEditType.DETAIL) {
			_type = "Edit";
		} else if (section.getEditType() == MassReferenceEditType.VIEW) {
			_type = "View";
		}
	} else if (type == OutputType.VIEW) {
		if (section.getEditType() == null || section.getEditType() == MassReferenceEditType.DETAIL) {
			_type = "View";
		} else if (section.getEditType() == MassReferenceEditType.VIEW) {
			_type = "Edit";
		}
	}

	String key = "";
	if (StringUtil.isNotBlank(section.getFilterScriptKey())) {
		key = section.getFilterScriptKey();
	}

	Entity rootEntity = (Entity) request.getAttribute(Constants.ROOT_ENTITY);
	String rootOid = rootEntity != null ? rootEntity.getOid() : "";
	String rootVersion = rootEntity != null && rootEntity.getVersion() != null ? rootEntity.getVersion().toString() : "";

	//追加、編集、削除の権限を参照先のEntityの権限で判断
	AuthContext auth = AuthContext.getCurrentContext();
	boolean creatable = auth.checkPermission(new EntityPermission(rp.getObjectDefinitionName(), EntityPermission.Action.CREATE)) && !section.isHideAddButton();
	boolean updatable = auth.checkPermission(new EntityPermission(rp.getObjectDefinitionName(), EntityPermission.Action.UPDATE));
	boolean deletable = auth.checkPermission(new EntityPermission(rp.getObjectDefinitionName(), EntityPermission.Action.DELETE)) && !section.isHideDeleteButton();

	//削除時のデータの消し方
	boolean purge = data.getView().isPurgeCompositionedEntity();

	//非同期で参照先のEntityを被参照のOIDで検索する
%>

<div id="<c:out value="<%=id %>"/>" class="<c:out value="<%=style %>"/>">
<div class="hgroup-03 sechead<c:out value="<%=disclosure %>"/>">
<h3><span><c:out value="<%=title %>"/></span></h3>
</div>
<div style="<c:out value="<%=disclosureStyle %>"/>">
<%
	if (StringUtil.isNotBlank(section.getUpperContents())) {
		String defName = (String)request.getAttribute(Constants.ROOT_DEF_NAME);
		evm.executeTemplate(defName, section.getContentScriptKey() + "_UpperContent", request, response, application, pageContext);
	}
%>
<div class="massReference" data-oid="<c:out value="<%=entity.getOid() %>"/>" data-defName="${m:esc(defName)}"
 data-propName="<c:out value="<%=propName%>"/>" data-viewName="${m:esc(param.viewName)}" data-offset="0" data-limit="<%=limit%>" data-orgOutputType="<c:out value="<%=type%>"/>"
 data-outputType="<c:out value="<%=_type%>"/>" data-webapiName="<%=GetMassReferencesCommand.WEBAPI_NAME%>" data-removeWebapiName="<%=UpdateMappedbyReferenceCommand.WEBAPI_NAME%>"
 data-viewAction="<c:out value="<%=viewAction%>"/>" data-detailAction="<c:out value="<%=detailAction%>"/>"
 data-targetDefName="<c:out value="<%=rp.getObjectDefinitionName()%>"/>" data-mappedBy="<c:out value="<%=rp.getMappedBy()%>"/>" data-changeEditLinkToViewLink="<%=section.isChangeEditLinkToViewLink() %>"
 data-sortKey="" data-sortType="" data-creatable="<%=creatable %>" data-updatable="<%=updatable %>" data-deletable=<%=deletable%> data-purge=<%=purge %>
 data-showPaging=<%=!section.isHidePaging()%> data-showPageJump=<%=!section.isHidePageJump()%> data-showPageLink=<%=!section.isHidePageLink()%> data-showCount=<%=!section.isHideCount()%>
 data-showSearchBtn=<%=section.isShowSearchBtn()%> data-condKey="<c:out value="<%=key %>" />" data-tokenValue=<%= TemplateUtil.outputToken(TokenOutputType.VALUE)%>
 data-entityOid="<c:out value="<%=rootOid %>"/>" data-entityVersion="<c:out value="<%=rootVersion %>"/>" >
<%
	if (!PagingPosition.BOTTOM.name().equals(pagingPosition)) {
%>
<div class="result-nav"></div>
<%
	}
%>
<table class="massReferenceTable"></table>
<div class="mr-btn mb10" style="display:none;">
<%
	if (creatable) {
%>
<input type="button" class="gr-btn-02 btn-mr-01" style="<c:out value="<%=addStyle%>"/>" value="${m:rs('mtp-gem-messages', 'generic.element.section.MassReferenceSection.add')}" />
<%
	}
	if (deletable) {
%>
<input type="button" class="gr-btn-02 btn-mr-02" style="<c:out value="<%=delStyle%>"/>" value="${m:rs('mtp-gem-messages', 'generic.element.section.MassReferenceSection.delete')}"/>
<%
	}
%>
</div>
<%
	if (!PagingPosition.TOP.name().equals(pagingPosition)) {
%>
<div class="result-nav"></div>
<%
	}
%>
</div>
<%
	if (StringUtil.isNotBlank(section.getLowerContents())) {
		String defName = (String)request.getAttribute(Constants.ROOT_DEF_NAME);
		evm.executeTemplate(defName, section.getContentScriptKey() + "_LowerContent", request, response, application, pageContext);
	}
%>
</div>
</div>
