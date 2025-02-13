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

<%@ page import="org.iplass.mtp.entity.Entity"%>
<%@ page import="org.iplass.mtp.util.StringUtil" %>
<%@ page import="org.iplass.mtp.view.generic.*"%>
<%@ page import="org.iplass.mtp.view.generic.element.section.*"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.gem.command.generic.delete.DeleteCommand"%>
<%@ page import="org.iplass.gem.command.generic.detail.DetailViewCommand"%>
<%@ page import="org.iplass.gem.command.generic.detail.DetailFormViewData" %>
<%@ page import="org.iplass.gem.command.generic.detail.InsertCommand"%>
<%@ page import="org.iplass.gem.command.generic.detail.UpdateCommand"%>
<%@ page import="org.iplass.gem.command.generic.search.SearchViewCommand"%>
<%@ page import="org.iplass.gem.command.Constants" %>
<%@ page import="org.iplass.gem.command.GemResourceBundleUtil" %>
<%@ page import="org.iplass.gem.command.ViewUtil"%>
<%@ page import="org.iplass.mtp.entity.definition.PropertyDefinition"%>
<%@ page import="org.iplass.mtp.entity.definition.EntityDefinition"%>
<%@ page import="org.iplass.mtp.entity.definition.properties.SelectProperty"%>

<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%
	String newversion = request.getParameter(Constants.NEWVERSION);
	String backPath = request.getParameter(Constants.BACK_PATH);
	String topViewListOffset = request.getParameter(Constants.TOPVIEW_LIST_OFFSET);
	if (topViewListOffset == null) {topViewListOffset = "";}
	boolean fromView = request.getParameter(Constants.FROM_VIEW) != null
						&& "true".equals(request.getParameter(Constants.FROM_VIEW));

	//コマンドから
	DetailFormViewData data = (DetailFormViewData) request.getAttribute(Constants.DATA);
	String searchCond = (String) request.getAttribute(Constants.SEARCH_COND);
	String message = (String) request.getAttribute(Constants.MESSAGE);

	OutputType type = OutputType.EDIT;
	String contextPath = TemplateUtil.getTenantContextPath();
	DetailFormView form = data.getView();

	String errorMsg = GemResourceBundleUtil.resourceString("command.generic.detail.InsertCommand.exceedMaximumSelection.error");
	EntityDefinition entityDefinition = (EntityDefinition)data.getEntityDefinition();
	Map<String,Integer> map = new HashMap<String,Integer>();
	List<PropertyDefinition> propertyDefinitionList = entityDefinition.getPropertyList();
	for (PropertyDefinition proDefinition : propertyDefinitionList){
		if(!proDefinition.isInherited() && proDefinition instanceof SelectProperty){
			map.put(proDefinition.getName(), proDefinition.getMultiplicity());
		}
	}

	String defName = data.getEntityDefinition().getName();
	String viewName = form.getName();

	//表示名
	String displayName = TemplateUtil.getMultilingualString(form.getTitle(), form.getLocalizedTitleList(),
			data.getEntityDefinition().getDisplayName(), data.getEntityDefinition().getLocalizedDisplayNameList());
	//新規or更新
	String execType = data.getExecType();
	//数値のカンマ区切りのセパレータ
	String separator = ViewUtil.getGroupingSeparator();
	//css用クラス
	String className = defName.replaceAll("\\.", "_");
	//タイトルのアイコン
	String imageColor = ViewUtil.getEntityImageColor(form);
	String imageColorStyle = "";
	if (imageColor != null) {
		imageColorStyle = "imgclr_" + imageColor;
	}

	String iconTag = ViewUtil.getIconTag(form);

	//編集対象
	String oid = null;
	Long updateDate = null;
	Long version = null;
	if (data.getEntity() != null) {
		oid = data.getEntity().getOid();
		version = data.getEntity().getVersion();
		if (data.getEntity().getUpdateDate() != null) {
			updateDate = data.getEntity().getUpdateDate().getTime();
		}

		//参照プロパティで利用
		request.setAttribute(Constants.EDITOR_PARENT_ENTITY, data.getEntity());
	}

	//ビュー名があればアクションの後につける
	String urlPath = ViewUtil.getParamMappingPath(defName, viewName);

	//キャンセルアクション
	String cancel = "";
	boolean moveToView = false;
	boolean moveToSearchList = false;
	if (StringUtil.isNotBlank(form.getCancelActionName())) {
		//指定Actionに遷移
		cancel = form.getCancelActionName() + urlPath;
	} else if (fromView) {
		if (StringUtil.isNotEmpty(backPath) && ViewUtil.isTopViewEditCancelBackToTop()) {
			//backPathに遷移
			cancel = backPath;
		} else {
			//詳細画面に遷移
			SearchFormView searchView = (SearchFormView)ViewUtil.getFormView(defName, viewName, true);
			String viewAction = DetailViewCommand.VIEW_ACTION_NAME;
			if (searchView != null && StringUtil.isNotBlank(searchView.getViewActionName())) {
				viewAction = searchView.getViewActionName();
			}
			cancel = viewAction + urlPath;
			moveToView = true;
		}
	} else {
		if (StringUtil.isEmpty(backPath)) {
			//検索画面に遷移(検索結果から直接編集画面を起動。TopViewからの場合はbackPath指定あり)
			cancel = SearchViewCommand.SEARCH_ACTION_NAME + urlPath;
			moveToSearchList = true;
		} else {
			//backPathに遷移
			cancel = backPath;
		}
	}

	//formにセットする初期アクション（Enter押下時に実行）
	String defaultAction = "";
	if (Constants.EXEC_TYPE_UPDATE.equals(execType)) {
		if (StringUtil.isNotBlank(form.getUpdateActionName())) {
			defaultAction = contextPath + "/" + form.getUpdateActionName() + urlPath;
		} else {
			defaultAction = contextPath + "/" + UpdateCommand.UPDATE_ACTION_NAME + urlPath;
		}
	} else {
		if (StringUtil.isNotBlank(form.getInsertActionName())) {
			defaultAction = contextPath + "/" + form.getInsertActionName() + urlPath;
		} else {
			defaultAction = contextPath + "/" + InsertCommand.INSERT_ACTION_NAME + urlPath;
		}
	}

	//各プロパティでの権限チェック用に定義名をリクエストに保存
	request.setAttribute(Constants.DEF_NAME, defName);
	request.setAttribute(Constants.ROOT_DEF_NAME, defName);	//NestTableの場合にDEF_NAMEが置き換わるので別名でRootのDefNameをセット

	Entity rootEntity = null;
	//更新の時のみ、エンティティを設定します。新規の場合はnullを設定します。
	if (Constants.EXEC_TYPE_UPDATE.equals(execType)) {
		rootEntity = data.getEntity();
	}
	//新規時に初期化スクリプト定義が存在する場合、data.getEntity()がnullにならないので、nullに統一するために別のキー名としてスコープに保持しなければなりません。。
	request.setAttribute(Constants.ROOT_ENTITY, rootEntity); //NestTableの場合に内部の表示判定スクリプトで利用

	//editor以下で参照するパラメータ
	request.setAttribute(Constants.VIEW_NAME, viewName);

	//section以下で参照するパラメータ
	request.setAttribute(Constants.OUTPUT_TYPE, type);
	request.setAttribute(Constants.ENTITY_DATA, data.getEntity());
	request.setAttribute(Constants.ENTITY_DEFINITION, data.getEntityDefinition());
	request.setAttribute(Constants.EXEC_TYPE, execType);

	//ボタンのカスタムスタイル用のscriptKey
	request.setAttribute(Constants.FORM_SCRIPT_KEY, form.getScriptKey());
%>

<h2 class="hgroup-01">
<span class="<c:out value="<%=imageColorStyle%>"/>">
<%if (StringUtil.isNotBlank(iconTag)) {%>
<%=iconTag%>
<%} else {%>
<i class="far fa-circle default-icon"></i>
<%} %>
</span>
<c:out value="<%= displayName %>"/>
</h2>
<%-- XSS対応-メタの設定のため対応なし(displayName) --%>
<div class="generic_detail detail_edit d_<c:out value="<%=className %>"/>">
<%@include file="../../layout/resource/mediaelementResource.inc.jsp" %>
<script type="text/javascript">
separator = "<%= separator %>";

function confirm_delete(action, target, hidden) {
	if (confirm("${m:rs('mtp-gem-messages', 'generic.detail.detail.deleteMsg')}")) {
		button_onclick(action, hidden);
		if (target) $(target).prop("disabled", true);
	}
}
function onclick_save(action, target, hidden) {
	if ($("#confirmEditSave").val() == "true" && !confirm("${m:rs('mtp-gem-messages', 'generic.detail.detail.saveMsg')}")) {
		return;
	}

	if (!validation()) return;

	button_onclick(action, hidden);
	if (target) $(target).prop("disabled", true);
}
function onclick_insert(action, target, hidden) {
	if ($("#confirmEditSave").val() == "true" && !confirm("${m:rs('mtp-gem-messages', 'generic.detail.detail.saveMsg')}")) {
		return;
	}

	if (!validation()) return;

	button_onclick(action, hidden);
	if (target) $(target).prop("disabled", true);
}
function button_onclick(action, hidden) {
	var $form = $("#detailForm");
	if (hidden) {
		if ($form) {
			for (var keyString in hidden) {
				$form.prepend("<input type='hidden\' name='" + keyString + "' value='" + hidden[keyString] + "'/>");
			}
		}
	}
	$form.attr("action", contextPath + "/" + action).submit();
}
function cancel() {
	if ($("#confirmEditCancel").val() == "true" && !confirm("${m:rs('mtp-gem-messages', 'generic.detail.detail.cancelMsg')}")) {
		return;
	}

<%	if (moveToView) { %>
	var cancelPath = contextPath + "/<%=StringUtil.escapeJavaScript(cancel)%>" + "/" + encodeURIComponent($(":hidden[name='oid']").val());
<%	} else { %>
	var cancelPath = contextPath + "/<%=StringUtil.escapeJavaScript(cancel)%>";
<%	} %>
	submitForm(cancelPath, {
<%	if (!moveToSearchList) {
		//一覧に戻らない場合
%>
		<%=Constants.VERSION%>:$(":hidden[name='version']").val(),
		<%=Constants.BACK_PATH%>:$(":hidden[name='backPath']").val(),
<%	}
	//一覧画面表示用
%>
		<%=Constants.SEARCH_COND%>:$(":hidden[name='searchCond']").val(),
		<%=Constants.TOPVIEW_LIST_OFFSET%>:"<%=StringUtil.escapeJavaScript(topViewListOffset)%>"
	});
}
function validation() {
	<%-- common.js --%>
	var ret = editValidate();
	var errorMsg = "<%= errorMsg %>"
	var jsMap = {
	        <% 
	            for (Map.Entry<String,Integer> entry : map.entrySet()) {
	                out.print("\"" + entry.getKey() + "\": \"" + entry.getValue() + "\",");
	            }
	        %>
	    };
	for (var key in jsMap) {
	    if (jsMap.hasOwnProperty(key)) {
	    	
	    	var checkedCount = $("#id_td_"+key+" input[type='checkbox']:checked").length;
	    	if (checkedCount>jsMap[key]) {
	    		alert(errorMsg)
	    		ret = false;
	    	}
	    }
	}
	
	var message = !ret ? "${m:rs('mtp-gem-messages', 'command.generic.detail.DetailCommandBase.inputErr')}" : "";
	$(".detail_edit > .page-error").text(message);
	return ret;
}
$(function(){
	$('.disabled-btn').removeAttr('disabled');
});
</script>
<%
	if (form.isValidJavascriptDetailPage() && StringUtil.isNotBlank(form.getJavaScript())) {
		//View定義で設定されたJavaScript
%>
<script type="text/javascript">
<%=form.getJavaScript()%><%-- XSS対応-メタの設定のため対応なし(form.getJavaScript) --%>
</script>
<%
	}
%>
<h3 class="hgroup-02 hgroup-02-01"><%= GemResourceBundleUtil.resourceString("generic.detail.detail.edit", displayName) %></h3>
<span class="error page-error">
<%
	if (StringUtil.isNotBlank(message)) {
%>
<c:out value="<%= message %>"/>
<%
	}
%>
</span>
<form id="detailForm" method="post" action="<c:out value="<%=defaultAction%>"/>">
<input type="hidden" name="<%=Constants.DEF_NAME%>" value="<c:out value="<%=defName%>"/>" />
<input type="hidden" name="<%=Constants.SEARCH_COND%>" value="<c:out value="<%=searchCond%>"/>" />
<input type="hidden" name="<%=Constants.EXEC_TYPE%>" value="<c:out value="<%=execType%>"/>" />
<input type="hidden" name="<%=Constants.BACK_PATH%>" value="<c:out value="<%=backPath%>"/>" />
<input type="hidden" name="<%=Constants.TOPVIEW_LIST_OFFSET%>" value="<c:out value="<%=topViewListOffset%>"/>" />
<%
	if (newversion != null) {
%>
<input type="hidden" name="<%=Constants.NEWVERSION%>" value="true" />
<%
	}
	if (oid != null) {
%>
<input type="hidden" name="<%=Constants.OID%>" value="<c:out value="<%=oid%>"/>" />
<%
	}
	if (updateDate != null) {
%>
<input type="hidden" name="<%=Constants.TIMESTAMP%>" value="<c:out value="<%=updateDate%>"/>" />
<%
	}
	if (version != null) {
%>
<input type="hidden" name="<%=Constants.VERSION%>" value="<c:out value="<%=version%>"/>" />
<%
	}
%>
<input type="hidden" id="confirmEditSave" value="<%=ViewUtil.isConfirmEditSave()%>" />
<input type="hidden" id="confirmEditCancel" value="<%=ViewUtil.isConfirmEditCancel()%>" />
<%-- ページトップ部分のボタン --%>
<div class="operation-bar operation-bar_top">
<jsp:include page="editButton.inc.jsp" />

<ul class="nav-disc-all">
<li class="all-open"><a href="#">${m:rs("mtp-gem-messages", "generic.detail.detail.allOpen")}</a></li>
<li class="all-close"><a href="#">${m:rs("mtp-gem-messages", "generic.detail.detail.allClose")}</a></li>
</ul>
</div><!--operation-bar-->
<%
	request.setAttribute(Constants.NAV_SECTION, form.getSections());
%>
<jsp:include page="sectionNavi.inc.jsp" />
<%
	for (Section section : form.getSections()) {
		if (!EntityViewUtil.isDisplayElement(defName, section.getElementRuntimeId(), OutputType.EDIT, rootEntity)
				|| !ViewUtil.dispElement(section)) {
			continue;
		}

		request.setAttribute(Constants.ELEMENT, section);
		String path = EntityViewUtil.getJspPath(section, ViewConst.DESIGN_TYPE_GEM);
		if (path != null) {
%>
<jsp:include page="<%=path %>" />
<%
		}
	}
%>

<%-- ページボトム部分のボタン --%>
<div class="operation-bar operation-bar_bottom">
<jsp:include page="editButton.inc.jsp" />
</div><!--operation-bar-->
${m:outputToken('FORM_XHTML', true)}
</form>
</div>
