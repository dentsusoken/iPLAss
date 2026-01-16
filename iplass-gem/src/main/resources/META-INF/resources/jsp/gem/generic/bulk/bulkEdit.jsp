<%--
 Copyright (C) 2018 DENTSU SOKEN INC. All Rights Reserved.

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

<%@ page import="java.util.*" %>
<%@ page import="org.iplass.mtp.auth.AuthContext" %>
<%@ page import="org.iplass.mtp.entity.BinaryReference"%>
<%@ page import="org.iplass.mtp.entity.definition.*" %>
<%@ page import="org.iplass.mtp.entity.definition.properties.*"%>
<%@ page import="org.iplass.mtp.entity.Entity" %>
<%@ page import="org.iplass.mtp.entity.permission.EntityPermission" %>
<%@ page import="org.iplass.mtp.entity.SelectValue" %>
<%@ page import="org.iplass.mtp.util.StringUtil" %>
<%@ page import="org.iplass.mtp.view.generic.*" %>
<%@ page import="org.iplass.mtp.view.generic.editor.*" %>
<%@ page import="org.iplass.mtp.view.generic.element.Element" %>
<%@ page import="org.iplass.mtp.view.generic.element.section.*" %>
<%@ page import="org.iplass.mtp.view.generic.element.property.*" %>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.mtp.web.WebRequestConstants" %>
<%@ page import="org.iplass.gem.command.generic.bulk.BulkUpdateFormViewData" %>
<%@ page import="org.iplass.gem.command.generic.bulk.BulkCommandContext.BulkUpdatedProperty"%>
<%@ page import="org.iplass.gem.command.generic.bulk.BulkUpdateListCommand" %>
<%@ page import="org.iplass.gem.command.generic.bulk.BulkUpdateAllCommand" %>
<%@ page import="org.iplass.gem.command.generic.search.CountCommand" %>
<%@ page import="org.iplass.gem.command.Constants" %>
<%@ page import="org.iplass.gem.command.GemResourceBundleUtil" %>
<%@ page import="org.iplass.gem.command.ViewUtil"%>

<%!
	boolean isSelectAllPage(Boolean selectAllPage) {
		return selectAllPage != null && selectAllPage;
	}

	boolean isSelectAll(String selectAllType) {
		return "all".equals(selectAllType);
	}

	boolean isFirstSelect(String selectAllType) {
		return StringUtil.isEmpty(selectAllType);
	}

	boolean isUpdateFailed(String bulkUpdatePropNm) {
		return StringUtil.isNotEmpty(bulkUpdatePropNm);
	}

	boolean canBulkUpdate(String defName, PropertyColumn pc) {
		if (!EntityViewUtil.isDisplayElement(defName, pc.getElementRuntimeId(), OutputType.BULK, null)
				|| pc.getBulkUpdateEditor() == null
				|| pc.getEditor() instanceof UserPropertyEditor
				|| pc.getEditor() instanceof ExpressionPropertyEditor
				|| pc.getEditor() instanceof AutoNumberPropertyEditor) {
			return false;
		}
		return true;
	}

	boolean isDefaultSelection(SearchResultSection section) {
		return section.getBulkUpdateDefaultSelection() != null;
	}

	//プロパティ値の表示値を取得する
	String getPropertyDisplayValue(PropertyDefinition p, Object propValue, PropertyEditor editor) {
		String dispValue = "";
		boolean isMultiple = p.getMultiplicity() != 1;
		if (propValue == null) {
			return "";
		} else if (isMultiple) {
			Object[] values = (Object[]) propValue;
			String[] tmp = new String[values.length];
			for (int i = 0; i < values.length; i++) {
				tmp[i] = convertPropValueToString(p, values[i], editor);
			}
			dispValue = Arrays.toString(tmp);
		} else {
			dispValue = convertPropValueToString(p, propValue, editor);
		}
		return dispValue;
	}

	String convertPropValueToString(PropertyDefinition p, Object propValue, PropertyEditor editor) {
		if (propValue == null) return "";
		String dispValue = "";
		if (propValue instanceof Entity) {
			ReferencePropertyEditor rpe = (ReferencePropertyEditor) editor;
			if (rpe.getDisplayLabelItem() != null) {
				dispValue = ((Entity) propValue).getValue(rpe.getDisplayLabelItem());
			} else {
				dispValue = ((Entity) propValue).getName();
			}
		} else if (propValue instanceof BinaryReference) {
			dispValue = ((BinaryReference) propValue).getName();
		} else if (propValue instanceof SelectValue) {
			SelectValue sv = (SelectValue) propValue;
			SelectValue lsv = ((SelectProperty) p).getLocalizedSelectValue(sv.getValue(), TemplateUtil.getLanguage());
			dispValue = ((SelectValue) lsv).getDisplayName();
		} else {
			dispValue = propValue.toString();
		}
		return dispValue;
	}

	PropertyEditor getJoinPropertyEditor(JoinPropertyEditor editor, String propName) {
		for (NestProperty nest : editor.getJoinProperties()) {
			if (nest.getPropertyName().equals(propName)) {
				return nest.getEditor();
			}
		}
		return null;
	}

	PropertyEditor getRangePropertyEditor(RangePropertyEditor editor, String propName) {
		if (editor.getPropertyName().equals(propName)) {
			return editor.getEditor();
		}
		if (editor.getToPropertyName().equals(propName)) {
			return editor.getToEditor();
		}
		return null;
	}
%>
<%
	//権限確認用
	AuthContext auth = AuthContext.getCurrentContext();

	//コマンドから
	BulkUpdateFormViewData data = (BulkUpdateFormViewData) request.getAttribute(Constants.DATA);
	Boolean selectAllPage = (Boolean) request.getAttribute(Constants.BULK_UPDATE_SELECT_ALL_PAGE);
	String selectAllType = (String) request.getAttribute(Constants.BULK_UPDATE_SELECT_TYPE);
	String searchCond = (String) request.getAttribute(Constants.SEARCH_COND);
	String message = (String) request.getAttribute(Constants.MESSAGE);
	String bulkUpdatePropNm = (String) request.getAttribute(Constants.BULK_UPDATE_PROP_NM);
	Integer count = (Integer) request.getAttribute(Constants.BULK_UPDATE_COUNT);
	Integer updated = (Integer) request.getAttribute(Constants.BULK_UPDATED_COUNT);

	//排他制御チェックエラー（更新ダイアログが開く時）
	Boolean isExCheckErr = (Boolean) request.getAttribute(Constants.BULK_UPDATE_EXCHECK_ERR);
	if (isExCheckErr == null) isExCheckErr = false;

	boolean isSuccess = Constants.CMD_EXEC_SUCCESS.equals(request.getAttribute(WebRequestConstants.COMMAND_RESULT));
	boolean isSelectAllPage = isSelectAllPage(selectAllPage);
	//全選択フラグ
	boolean isSelectAll = isSelectAll(selectAllType);

	OutputType type = OutputType.BULK;
	String contextPath = TemplateUtil.getTenantContextPath();
	String action = contextPath + "/" + BulkUpdateListCommand.BULK_UPDATE_ACTION_NAME;
	if (isSelectAll) {
		action = contextPath + "/" + BulkUpdateAllCommand.BULK_UPDATE_ALL_ACTION_NAME;
	}
	EntityDefinition ed = data.getEntityDefinition();
	SearchFormView form = data.getView();
	SearchResultSection section = form.getResultSection();

	String defName = data.getEntityDefinition().getName();
	String viewName = form.getName();
	//表示名
	String displayName = TemplateUtil.getMultilingualString(form.getTitle(), form.getLocalizedTitleList(),
	data.getEntityDefinition().getDisplayName(), data.getEntityDefinition().getLocalizedDisplayNameList());

	//編集対象情報
	List<String> oids = new ArrayList<String>();
	List<String> versions = new ArrayList<String>();
	List<String> updateDates = new ArrayList<String>();
	// 検索結果一覧チェックを付け直すため
	List<String> id = new ArrayList<String>();
	if (data.getSelected() != null) {
		for (Map.Entry<Integer, Entity> entry : data.getSelected().entrySet()) {
			oids.add(entry.getKey() + "_" + entry.getValue().getOid());
			versions.add(entry.getKey() + "_" + entry.getValue().getVersion());
			updateDates.add(entry.getKey() + "_" + entry.getValue().getUpdateDate().getTime());
			id.add("\"" + entry.getValue().getOid() + "_" + entry.getValue().getVersion() + "\"");
		}
	}

	// プロパティリスト
	List<PropertyColumn> properties = ViewUtil.filterPropertyColumn(section.getElements());
	// Property情報
	Map<String, PropertyDefinition> defMap = new HashMap<String, PropertyDefinition>();
	Map<String, PropertyColumn> colMap = new HashMap<String, PropertyColumn>();
	for (PropertyColumn pc : properties) {
		String propName = pc.getPropertyName();
		colMap.put(propName, pc);

		PropertyDefinition pd = EntityViewUtil.getPropertyDefinition(propName, ed);
		defMap.put(propName, pd);
	}

	String pleaseSelectLabel = "";
	if (ViewUtil.isShowPulldownPleaseSelectLabel()) {
		pleaseSelectLabel = GemResourceBundleUtil.resourceString("generic.bulk.pleaseSelect");
	}

	//各プロパティでの権限チェック用に定義名をリクエストに保存
	request.setAttribute(Constants.DEF_NAME, defName);
	request.setAttribute(Constants.ROOT_DEF_NAME, defName); //NestTableの場合にDEF_NAMEが置き換わるので別名でRootのDefNameをセット

	//editor以下で参照するパラメータ
	request.setAttribute(Constants.VIEW_NAME, viewName);

	//section以下で参照するパラメータ
	request.setAttribute(Constants.OUTPUT_TYPE, type);
	request.setAttribute(Constants.ENTITY_DEFINITION, data.getEntityDefinition());
%>
<div class="bulk-edit">
<h3 class="hgroup-02 hgroup-02-01"><%=GemResourceBundleUtil.resourceString("generic.bulk.title", displayName)%></h3>
<span class="error page-error"></span>
<%
	if (isSuccess) {
		//更新に成功した場合
		if (count != null) {
%>
<span class="success"><%=GemResourceBundleUtil.resourceString("command.generic.bulk.BulkUpdateListCommand.successMsg", updated)%></span>
<%
		}
	} else {
%>
<span class="error"><c:out value="<%=message%>"/></span>
<%
		//分割トランザクションの場合、一部データの更新に成功した可能性があります。
		if (count != null && updated > 0) {
%>
<span class="error"><%=GemResourceBundleUtil.resourceString("command.generic.bulk.BulkUpdateListCommand.failedMsg", count, count - updated)%></span>
<%
		}
	}
%>
<form id="detailForm" method="post" action="<%=action%>">
<%
	if (!isSelectAll) {
%>
<input type="hidden" name="defName" value="<c:out value="<%=defName%>"/>" />
<input type="hidden" name="viewName" value="<c:out value="<%=viewName%>"/>" />
<%
	}
	if (selectAllPage != null) {
%>
<input type="hidden" name="selectAllPage" value="<c:out value="<%=selectAllPage%>"/>" />
<%
	}
	if(selectAllType != null) {
%>
<input type="hidden" name="selectAllType" value="<c:out value="<%=selectAllType%>"/>" />
<%
	}
	if(searchCond != null) {
%>
<input type="hidden" name="searchCond" value="<c:out value="<%=searchCond%>"/>" />
<%
	}
	if (!isSelectAll && oids != null && oids.size() > 0) {
		for (String  oid : oids) {
%>
<input type="hidden" name="oid" value="<c:out value="<%=oid%>"/>" />
<%
		}
	}
	if (!isSelectAll && updateDates != null && updateDates.size() > 0) {
		for (String updateDate : updateDates) {
%>
<input type="hidden" name="timestamp" value="<c:out value="<%=updateDate%>"/>" />
<%
		}
	}
	if (!isSelectAll && versions != null && versions.size() > 0) {
		for (String version : versions) {
%>
<input type="hidden" name="version" value="<c:out value="<%=version%>"/>" />
<%
		}
	}
%>
<div class="formArchive">
<div>
<table class="tbl-section mb10">
<tbody>
<%
	if (isSelectAllPage) {
%>
<tr>
<th class="section-data col1" rowspan="2">${m:rs("mtp-gem-messages", "generic.bulk.selectBulkUpdateType")}</th>
<td class="section-data col1"><label><input type="radio" name="selectAllType" value="select" <%=!isSelectAll ? "checked" : ""%>>${m:rs("mtp-gem-messages", "generic.bulk.updateRow")}</label></td>
</tr>
<tr>
<td class="section-data col1"><label><input type="radio" name="selectAllType" value="all" <%=isSelectAll ? "checked" : ""%>>${m:rs("mtp-gem-messages", "generic.bulk.updateAll")}<span id="bulkUpdateCount"></span></label></td>
</tr>
<script>
$(function() {
	var $radio = $(":radio[name='selectAllType']");
	var createSearchCondParams = function() {
		var searchCond = $(":hidden[name='searchCond']").val();
		// 検索条件を元に一括更新
		if (searchCond && searchCond.length > 0) {
			var $form = $(this);
			var params = parseSearchCond(searchCond);
			if (params.length > 0) {
				for (var i = 0; i < params.length; i++) {
					var param = params[i];
					if ($(":hidden[name='" + param.key + "']").length > 0) {
						$(":hidden[name='" + param.key + "']").val(param.val);
					} else {
						$("<input />").attr({type:"hidden", name:param.key, value:param.val}).appendTo($form);
					}
				}
			}
		}
	};
<%
	if(isSelectAll) {
%>
	$("#detailForm").on("submit", createSearchCondParams);
<%
	}
%>
	$radio.on("change", function(){
		if ($(this).val() == "all") {
			$("#detailForm").attr("action", "<%=contextPath + "/" + BulkUpdateAllCommand.BULK_UPDATE_ALL_ACTION_NAME%>").on("submit", createSearchCondParams);
		} else {
			$("#detailForm").attr("action", "<%=contextPath + "/" + BulkUpdateListCommand.BULK_UPDATE_ACTION_NAME%>").off("submit", createSearchCondParams);
		}
	});
	//二回目以降は編集不可にする
	if ("<%=isFirstSelect(selectAllType)%>" == "false") {
		$radio.prop("disabled", true);
	}
})
</script>
<%
	}
%>
<tr>
<th class="section-data col1">${m:rs("mtp-gem-messages", "generic.bulk.updatePropName")}</th>
<td class="section-data col1">
<select id="sel_<c:out value="<%=Constants.BULK_UPDATE_PROP_NM%>"/>" name="<c:out value="<%=Constants.BULK_UPDATE_PROP_NM%>"/>" class="inpbr form-size" onchange="propChange(this)">
<option value="" selected="selected"><%= pleaseSelectLabel %></option>
<%
	for (PropertyColumn pc : colMap.values()) {
		if (!canBulkUpdate(defName, pc)) continue;
		String propName = pc.getPropertyName();
		PropertyDefinition pd = defMap.get(propName);
		String displayLabel = TemplateUtil.getMultilingualString(pc.getDisplayLabel(), pc.getLocalizedDisplayLabelList(), pd.getDisplayName(),
				pd.getLocalizedDisplayNameList());
		String selected = propName.equals(bulkUpdatePropNm) || propName.equals(section.getBulkUpdateDefaultSelection()) ? "selected" : "";
%>
<option value="<c:out value="<%=propName%>"/>" <c:out value="<%=selected%>" />><c:out value="<%=displayLabel%>" /></option>
<%
	}
%>
</select>
</td>
</tr>
</tbody>
</table>
</div>
<div>
<table id="id_tbl_bulkupdate" class="tbl-section">
<%
	for (PropertyColumn pc : colMap.values()) {
		if (canBulkUpdate(defName, pc)) {
			request.setAttribute(Constants.ELEMENT, pc);

			String path = EntityViewUtil.getJspPath(pc, ViewConst.DESIGN_TYPE_GEM);
			if (path != null) {
%>
<tr id="id_tr_<c:out value="<%=pc.getPropertyName()%>"/>" style="display: none;">
<jsp:include page="<%=path%>" />
</tr>
<%
			}
		}
	}
%>
</table>
</div>
<div class="operation-bar operation-bar_bottom">
<ul class="list_operation edit-bar">
<%
	if (auth.checkPermission(new EntityPermission(defName, EntityPermission.Action.UPDATE))) {
		//ボタンの表示ラベル
		String bulkUpdateDisplayLabel = GemResourceBundleUtil.resourceString("generic.bulk.update");

		if (isExCheckErr) {
%>
<li class="btn save-btn"><input id="bulkUpdateBtn" type="button" class="gr-btn-02" value="<c:out value="<%=bulkUpdateDisplayLabel %>" />" disabled="disabled" /></li>
<%
		} else {
%>
<li class="btn save-btn"><input id="bulkUpdateBtn" type="button" class="gr-btn" value="<c:out value="<%=bulkUpdateDisplayLabel %>" />" onclick="onclick_bulkupdate(this)" /></li>
<%
		}
	}
%>
<li class="mt05 cancel-link"><a href="javascript:void(0)" onclick="onclick_cancel()">${m:rs("mtp-gem-messages", "generic.bulk.cancel")}</a></li>
</ul>
</div>
<div>

<%
	if (data.getUpdatedProperties() != null) {
%>
<table class="tbl-section">
<tr>
<th class="section-data col1">${m:rs("mtp-gem-messages", "generic.bulk.updatedPropName")}</th>
<th class="section-data w-auto">${m:rs("mtp-gem-messages", "generic.bulk.updatedPropValue")}</th>
</tr>
<%
		for (BulkUpdatedProperty updatedProp : data.getUpdatedProperties()) {
			Integer updateNo = updatedProp.getUpdateNo();
			String updatedPropName = StringUtil.escapeHtml(updatedProp.getPropertyName());
			PropertyDefinition pd = defMap.get(updatedPropName);
			PropertyColumn pc = colMap.get(updatedPropName);
			PropertyEditor editor = pc.getBulkUpdateEditor();
			String updatedPropDispName = TemplateUtil.getMultilingualString(pc.getDisplayLabel(), pc.getLocalizedDisplayLabelList(),
					pd.getDisplayName(), pd.getLocalizedDisplayNameList());
			Object updatedPropValue = updatedProp.getPropertyValue();
			String updatedPropDispValue = "";
			// 表示値に変更済み
			if (updatedPropValue instanceof String) {
				updatedPropDispValue = StringUtil.escapeHtml((String)updatedPropValue);
			} else {
				if (editor instanceof JoinPropertyEditor ||  editor instanceof RangePropertyEditor) {
					Map<String, Object> updatedPropsMap = (Map<String, Object>) updatedPropValue;
					List<String> tmp = new ArrayList<>(updatedPropsMap.size());
					for(Map.Entry<String, Object> entry : updatedPropsMap.entrySet()) {
						String propName = entry.getKey();
						Object propValue = entry.getValue();
						PropertyEditor nestEditor = null;
						if (editor instanceof RangePropertyEditor) {
							nestEditor = getRangePropertyEditor((RangePropertyEditor) editor, propName);
						} else if (editor instanceof JoinPropertyEditor) {
							nestEditor = getJoinPropertyEditor((JoinPropertyEditor) editor, propName);
						}
						tmp.add(convertPropValueToString(EntityViewUtil.getPropertyDefinition(propName, ed), propValue, nestEditor));
					}
					updatedPropValue = tmp.toString();
				}
				updatedPropDispValue = StringUtil.escapeHtml(getPropertyDisplayValue(pd, updatedPropValue, editor));
			}
%>
<tr>
<th class="section-data col1">
<%=updatedPropDispName%>
<input type="hidden" name="<%=Constants.BULK_UPDATED_PROP_NM%>" value="<%=updateNo + "_" + updatedPropName%>"/>
</th>
<td class="section-data col1">
<%=updatedPropDispValue%>
<input type="hidden" name="<%=Constants.BULK_UPDATED_PROP_VALUE%>" value="<%=updateNo + "_" + updatedPropDispValue%>"/>
</td>
</tr>
<%
		}
%>
</table>
<%
	}
%>
</div>
</div>
${m:outputToken('FORM_XHTML', true)}
</form>
<script type="text/javascript">
function onclick_cancel() {
	$("#modal-dialog-root .modal-close", parent.document).trigger("click");
}
function onclick_bulkupdate(target){
	// 一括更新する項目で選択中の値
	var selectedProp = $("#sel_<%=Constants.BULK_UPDATE_PROP_NM%>").val();
	if (!selectedProp) {
		alert("${m:rs('mtp-gem-messages', 'generic.bulk.pleaseSelect')}");
		return;
	}
	var selectedPropEscaped = $.escapeSelector(selectedProp);
	$("#id_tbl_bulkupdate tbody tr").each(function () {
		var $tr = $(this);
		if ($tr.is("#id_tr_" + selectedPropEscaped)) {
			return; // 選択された項目に対応する行はスキップ（値をそのまま残す）
		}
		$tr.find(":input").each(function () {
			var el = this;
			var $el = $(el);
			if ($el.is(":button, :submit, :reset")) {
				return;
			}
			if ($el.is(":checkbox, :radio")) {
				el.checked = el.defaultChecked;
			} else if ($el.is("select")) {
				Array.from(el.options).forEach(function (opt) {
					opt.selected = opt.defaultSelected;
				});
			} else {
				el.value = el.defaultValue;
			}
		});
	});
	if (!validation()) return;
	if (!confirm("${m:rs('mtp-gem-messages', 'generic.bulk.updateMsg')}")) {
		return;
	}
	$(target).prop("disabled", true);
	$("#detailForm").submit();
}
function onDialogClose() {
	var edited = <%=count != null && updated > 0 %>;
	if (!edited) return true;
	var func = parent.document.scriptContext["bulkUpdateModalWindowCallback"];
	if (func && $.isFunction(func)) {
		var id = "<%=isSelectAll%>" == "true" ? "all" : <%=Arrays.toString(id.toArray())%>;
		func.call(parent.window, id);
	}
	return true;
}
function propChange(obj) {
	var propName = obj.options[obj.selectedIndex].value;
	$("table#id_tbl_bulkupdate tbody").children("tr").each(function() {
		$(this).css("display", "none").val("");
	});
	$("tr#id_tr_" + propName).css("display", "");
}
function validation() {
	<%-- common.js --%>
	var ret = editValidate();
	if (!ret) {
		$(".bulk-edit > .page-error").text("${m:rs('mtp-gem-messages', 'command.generic.detail.DetailCommandBase.inputErr')}");
		return false;
	}
	$(".bulk-edit > .page-error").text("");
	return true;
}
$(function() {
<%
	String selectPropName = null;
	if (isUpdateFailed(bulkUpdatePropNm)) {
		selectPropName = bulkUpdatePropNm;
	} else if (isDefaultSelection(section)) {
		selectPropName = section.getBulkUpdateDefaultSelection();
	}
	if (selectPropName != null) {
	//前回の更新に失敗したプロパティに対してエラーメッセージを表示します、
	//またはデフォルト選択項目が設定された場合、その入力項目を表示します。
%>
	$("tr#id_tr_<%=selectPropName%>").css("display", "");
<%
	}
%>
	// タイトルの設定
	$("#modal-title", parent.document).text("<%=displayName%>");<%-- XSS対応-メタの設定のため対応なし(displayName) --%>
	// 一括更新件数
	var func = parent.document.scriptContext["countBulkUpdate"];
	if(func && $.isFunction(func)) {
		func(this, function(count){
			var bulkUpdateItem = "${m:rs('mtp-gem-messages', 'generic.bulk.updateItem')}";
			bulkUpdateItem = bulkUpdateItem.replace("{0}", count);
			$("#bulkUpdateCount").text(bulkUpdateItem);
			// 検索条件で一括更新した後、該当するデータの件数が0件になった場合、一括更新ボタンを無効化にします。
			if ("" + count === "0") {
				$("#bulkUpdateBtn").prop("disabled", true);
			}
		});
	}
})
</script>
</div>
