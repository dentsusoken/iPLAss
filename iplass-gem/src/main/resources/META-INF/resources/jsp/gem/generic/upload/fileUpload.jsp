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

<%@ page import="org.iplass.mtp.async.TaskStatus"%>
<%@ page import="org.iplass.mtp.auth.AuthContext" %>
<%@ page import="org.iplass.mtp.entity.Entity"%>
<%@ page import="org.iplass.mtp.entity.TargetVersion"%>
<%@ page import="org.iplass.mtp.entity.permission.EntityPermission" %>
<%@ page import="org.iplass.mtp.entity.definition.EntityDefinition"%>
<%@ page import="org.iplass.mtp.entity.definition.IndexType" %>
<%@ page import="org.iplass.mtp.entity.definition.PropertyDefinition" %>
<%@ page import="org.iplass.mtp.entity.definition.VersionControlType"%>
<%@ page import="org.iplass.mtp.util.StringUtil" %>
<%@ page import="org.iplass.mtp.view.generic.DetailFormView" %>
<%@ page import="org.iplass.mtp.view.generic.SearchFormView"%>
<%@ page import="org.iplass.mtp.view.generic.element.section.SearchConditionSection.FileSupportType"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.gem.command.generic.detail.DetailFormViewData" %>
<%@ page import="org.iplass.gem.command.generic.search.SearchViewCommand"%>
<%@ page import="org.iplass.gem.command.generic.upload.EntityFileUploadCommand"%>
<%@ page import="org.iplass.gem.command.generic.upload.EntityFileUploadStatusCommand"%>
<%@ page import="org.iplass.gem.command.generic.upload.EntityFileSampleDownloadCommand"%>
<%@ page import="org.iplass.gem.command.Constants"%>
<%@ page import="org.iplass.gem.command.GemResourceBundleUtil"%>
<%@ page import="org.iplass.gem.command.ViewUtil"%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map"%>

<%!
	/**
	 * プロパティに対する表示名を返します。
	 *
	 * @param propertyName プロパティ名
	 * @param customColumnNameMap プロパティ名に対する出力CSV列名のマッピング定義
	 */
	String getDispPropertyName(String propertyName, Map<String, String> customColumnNameMap) {
		if (customColumnNameMap != null) {
			String displayPropertyName = customColumnNameMap.get(propertyName);
			if (StringUtil.isNotEmpty(displayPropertyName)) {
				return displayPropertyName;
			}
		}
		
		return propertyName;
	}

	/**
	 * 必須プロパティに対する表示名を返します。
	 *
	 * @param requiredProperties 必須プロパティ名
	 * @param customColumnNameMap プロパティ名に対する出力CSV列名のマッピング定義
	 */
	String getRequiredPropertyNames(List<String> requiredProperties, Map<String, String> customColumnNameMap) {
		StringBuilder requiredPropertyNames = new StringBuilder();
		if (requiredProperties != null) {
			requiredProperties.forEach(propertyName -> {
				if (requiredPropertyNames.length() > 0) {
					requiredPropertyNames.append(", ");
				}
				requiredPropertyNames.append(getDispPropertyName(propertyName, customColumnNameMap));
			});
		}
		return requiredPropertyNames.toString();
	}
%>
<%
	// データ取得
	String contextPath = TemplateUtil.getTenantContextPath();

	String message = (String) request.getAttribute(Constants.MESSAGE);
	List<String> requiredProperties = (List<String>) request.getAttribute("requiredProperties");
	Map<String, String> customColumnNameMap = (Map<String, String>) request.getAttribute("customColumnNameMap");

	EntityDefinition ed = (EntityDefinition) request.getAttribute(Constants.ENTITY_DEFINITION);
	DetailFormView form = (DetailFormView) request.getAttribute("detailFormView");
	SearchFormView searchFormView = (SearchFormView) request.getAttribute("searchFormView");
	
	FileSupportType fileSupportType = (FileSupportType) request.getAttribute("fileSupportType");

	String defName = ed.getName();
	String viewName = form.getName();
	if (viewName == null) viewName = "";

	List<String> uniquePropList = new ArrayList<String>();
	// oid以外のユニークキーがないか確認
	for (PropertyDefinition pd : ed.getDeclaredPropertyList()) {
		if (pd.getIndexType() == IndexType.UNIQUE || pd.getIndexType() == IndexType.UNIQUE_WITHOUT_NULL) {
			uniquePropList.add(pd.getName());
		}
	}

	// 権限データ取得
	AuthContext auth = AuthContext.getCurrentContext();
	boolean cPermission = auth.checkPermission(new EntityPermission(defName, EntityPermission.Action.CREATE));
	boolean uPermission = auth.checkPermission(new EntityPermission(defName, EntityPermission.Action.UPDATE));

	//ビュー名があればアクションの後につける
	String urlPath = ViewUtil.getParamMappingPath(defName, viewName);

	//アップロードアクション
	String upload = contextPath + "/" + EntityFileUploadCommand.ACTION_NAME + urlPath;
	String sampleDl = contextPath + "/" + EntityFileSampleDownloadCommand.ACTION_NAME + urlPath;

	//キャンセルアクション
	String cancel = null;
	if (StringUtil.isNotBlank(form.getCancelActionName())) {
		cancel = form.getCancelActionName() + urlPath;
	} else {
		cancel = SearchViewCommand.SEARCH_ACTION_NAME + urlPath;
	}

	// 表示名
	String displayName = TemplateUtil.getMultilingualString(ed.getDisplayName(), ed.getLocalizedDisplayNameList());
	String viewTitle = TemplateUtil.getMultilingualString(form.getTitle(), form.getLocalizedTitleList());
	if(StringUtil.isNotBlank(viewTitle)) {
		displayName = viewTitle;
	}

	String imageColor = ViewUtil.getEntityImageColor(form);
	String imageColorStyle = "";
	if (imageColor != null) {
		imageColorStyle = "imgclr_" + imageColor;
	}

	String className = defName.replaceAll("\\.", "_");
%>
<%-- XSS対応-メタの設定のため対応なし(displayName) --%>
<div class="generic_search_csvupload d_<c:out value="<%=className%>"/>">
<script type="text/javascript">
function cancel() {
	submitForm(contextPath + "/<%=StringUtil.escapeJavaScript(cancel)%>", {
			defName:"<%=StringUtil.escapeJavaScript(defName)%>",
			searchCond:$(":hidden[name='searchCond']").val()
		});
}

function sampleDonwload() {
<%
	if (fileSupportType == FileSupportType.SPECIFY) {
%>
	submitForm("<%=sampleDl%>", {
			defName:"<%=StringUtil.escapeJavaScript(defName)%>",
			fileSupportType:$('input:radio[name="fileSupportType"]:checked').val()
		});
<%
	} else {
%>
	submitForm("<%=sampleDl%>", {
			defName:"<%=StringUtil.escapeJavaScript(defName)%>"
		});
<%
	}
%>
}

function showUpdateMessage() {
	$("#upload").hide();
	$(".error").hide();
	$(".operation-bar").hide();
	$(".uploading").show();
}
</script>

<%
	if (ViewUtil.isCsvUploadAsync()) {
%>
<script type="text/javascript">

var wait_time = function(time){
	return (function(){
		var d = $.Deferred();
		setTimeout(function(){ d.resolve(); }, time);
		return d.promise();
	})
}

function csvUploadStatusPollingHandler(results){
	var $tbody = $('#csvUploadStatusList tbody');
	$tbody.empty();
	$.each(results.result, function(index){
		var $tr = $('<tr>').appendTo($tbody);
		$('<td>').appendTo($tr).text(this.targetDisplayName).attr('title', this.targetDisplayName);
		$('<td>').appendTo($tr).text(this.fileName).attr('title', this.fileName);
		$('<td>').appendTo($tr).text(this.uploadDate).attr('title', this.uploadDate);
		$('<td>').appendTo($tr).text(this.statusLabel);

		var $msg = $('<td>').appendTo($tr);
		if (this.status === '<%=TaskStatus.ABORTED.name()%>') {
			$msg.addClass("error");
		}

		if (this.status === '<%=TaskStatus.COMPLETED.name()%>') {
			if (this.insertCount != null) {
				$('<p>').appendTo($msg).text('${m:rs("mtp-gem-messages", "generic.csvUploadResult.regCnt")}' + this.insertCount);
			}
			if (this.updateCount != null) {
				$('<p>').appendTo($msg).text('${m:rs("mtp-gem-messages", "generic.csvUploadResult.upCnt")}' + this.updateCount);
			}
			if (this.deleteCount != null) {
				$('<p>').appendTo($msg).text('${m:rs("mtp-gem-messages", "generic.csvUploadResult.delCnt")}' + this.deleteCount);
			}
		} else {
			$msg.html(this.message != null ? this.message : "");
		}
	});
	$(".tableStripe").tableStripe();
}

<%
		String _defName = StringUtil.escapeJavaScript(defName);
		String _viewName = StringUtil.escapeJavaScript(viewName);
%>
function pollingCsvUploadStatus(){
	var params = "{";
	params += "\"defName\":\"<%=_defName%>\"";
	params += ",\"viewName\":\"<%=_viewName%>\"";
	params += "}";
	var d = $.Deferred();
	d.then(postAsync("<%=EntityFileUploadStatusCommand.WEBAPI_NAME%>", params, csvUploadStatusPollingHandler))
		.then(wait_time(<%= ViewUtil.getCsvUploadStatusPollingInterval() %>))
		.then(pollingCsvUploadStatus);
	d.resolve();
	return d.promise();
}

$(function(){
	pollingCsvUploadStatus();
});
</script>
<%	} %>

<h2 class="hgroup-01">
<span class="<c:out value="<%=imageColorStyle%>"/>">
<i class="far fa-circle default-icon"></i>
</span>
<c:out value="<%=displayName%>"/>
</h2>

<form name="detailForm" method="post" action="<c:out value="<%=upload%>"/>" enctype="multipart/form-data">
<h3 class="hgroup-02 hgroup-02-01"><%= GemResourceBundleUtil.resourceString("generic.csvUpload.csvUpTitle", displayName) %></h3>
<%
	if (StringUtil.isNotBlank(message)) {
%>
<div class="error" style="margin-bottom:17px;">
<%= message %>
</div>
<%
	}
%>
<input type="hidden" name="defName" value="<c:out value="<%=defName%>"/>" />
<input type="hidden" name="searchCond" value="${m:esc(searchCond)}" />

<ul class="csvupload_csvfile clear"><li>
<%
	if (fileSupportType == FileSupportType.SPECIFY) {
%>
<ul class="csvupload_fileSupportType clear">
<li><label><input name="fileSupportType" type="radio" value="<%=FileSupportType.CSV%>" checked />CSV</label></li>
<li><label><input name="fileSupportType" type="radio" value="<%=FileSupportType.EXCEL%>" />EXCEL</label></li>
</ul>
<script>
$(function(){
	$('input[name="fileSupportType"]:radio').on('change', function () {
		const value = $(this).val();
		if (value == "<%=FileSupportType.CSV%>") {
			$('input[name="filePath"]:file').attr("accept", ".csv");
		} else {
			$('input[name="filePath"]:file').attr("accept", ".xlsx");
		}
	});
});
</script>
<%
	}

	String acceptType = ".csv";
	if (fileSupportType == FileSupportType.EXCEL) {
		acceptType = ".xlsx";
	}
%>
<input id="upload" type="file" name="filePath" accept="<%=acceptType%>">
</li></ul>

<h3 class="hgroup-02 hgroup-02-01">${m:rs("mtp-gem-messages", "generic.csvUpload.selectUniqueKey")}</h3>

<ul class="csvupload_uniquekey clear">
<li><label><input name="uniqueKey" type="radio" value="<%=Entity.OID%>" checked /><c:out value="<%=getDispPropertyName(Entity.OID, customColumnNameMap) %>"/></label></li>
<%	for (String propName : uniquePropList) { %>
<li><label><input name="uniqueKey" type="radio" value="<%=propName%>" /><c:out value="<%=getDispPropertyName(propName, customColumnNameMap) %>"/></label></li>
<%	} %>
</ul>

<%
	// バージョン管理対象外のEntityの場合の更新時の対象バージョンの選択
	if (ed.getVersionControlType() == VersionControlType.NONE
			&& searchFormView != null && searchFormView.getCondSection().isCanCsvUploadTargetVersionSelectForNoneVersionedEntity()) {
		String selected = searchFormView.getCondSection().getCsvUploadTargetVersionForNoneVersionedEntity() == TargetVersion.SPECIFIC ? "checked" : "";
%>
<h3 class="hgroup-02 hgroup-02-01">${m:rs("mtp-gem-messages", "generic.csvUpload.selectUpdateTargetVersion")}</h3>
<ul class="csvupload-update-target clear">
<li><label>
<input name="updateTargetVersion" type="checkbox" value="<%=TargetVersion.SPECIFIC%>" <%=selected%> />${m:rs("mtp-gem-messages", "generic.csvUpload.updateTargetVersionSpecific")}
</label></li>
</ul>
<%
	}
%>
<span class="uploading" style="display:none;"><img src="${m:esc(skinImagePath)}/loading.gif" alt="" />　${m:rs("mtp-gem-messages", "generic.csvUpload.uploding")}</span>

<div class="operation-bar operation-bar_top">
<ul class="list_operation edit-bar">
<%	if (cPermission && uPermission) { %>
<li class="btn insert-btn"><input type="submit" value="${m:rs('mtp-gem-messages', 'generic.csvUpload.csvUpBtn')}" class="gr-btn" onclick="showUpdateMessage();return true;" /></li>
<%	} %>
<li class="btn insert-btn"><input type="submit" value="${m:rs('mtp-gem-messages', 'generic.csvUpload.sampleCsv')}" class="gr-btn" onclick="sampleDonwload();return false;" /></li>
<li class="cancel-link mt05"><a href="javascript:void(0)" onclick="cancel();return false;">${m:rs("mtp-gem-messages", "generic.csvUpload.cancel")}</a></li>
</ul>
</div>
${m:outputToken('FORM_XHTML', true)}
</form>

<div class="csvupload_description flat-block-top">
<ul>
<li class="description">${m:rs("mtp-gem-messages", "generic.csvUpload.uploadDescription2")}</li>
<li class="description">${m:rs("mtp-gem-messages", "generic.csvUpload.uploadDescription3")}</li>
<li class="description">${m:rs("mtp-gem-messages", "generic.csvUpload.uploadDescription4")}</li>
<li class="description">${m:rs("mtp-gem-messages", "generic.csvUpload.uploadDescription6")}</li>
<li class="description">${m:rs("mtp-gem-messages", "generic.csvUpload.uploadDescription7")}</li>
<li class="description"><%= GemResourceBundleUtil.resourceString("generic.csvUpload.uploadDescription8", getRequiredPropertyNames(requiredProperties, customColumnNameMap)) %></li>
<%
	if (ed.getVersionControlType() == VersionControlType.NONE
			&& searchFormView != null && searchFormView.getCondSection().isCanCsvUploadTargetVersionSelectForNoneVersionedEntity()) {
%>
<li class="description">${m:rs("mtp-gem-messages", "generic.csvUpload.uploadDescription10")}</li>
<%
	}

	if (fileSupportType != FileSupportType.EXCEL) {
%>
<li class="description">${m:rs("mtp-gem-messages", "generic.csvUpload.uploadCsvDescriptionTitle")}</li>
<li class="description">${m:rs("mtp-gem-messages", "generic.csvUpload.uploadCsvDescription1")}</li>
<li class="description">${m:rs("mtp-gem-messages", "generic.csvUpload.uploadCsvDescription2")}</li>
<li class="description">${m:rs("mtp-gem-messages", "generic.csvUpload.uploadCsvDescription3")}</li>
<%
	}
%>
</ul>
</div>

<%	if (ViewUtil.isCsvUploadAsync()) { %>
<div class="csvupload_status flat-block-top">
<div class="hgroup-03 sechead disclosure-close">
<h3><span>${m:rs("mtp-gem-messages", "generic.csvUploadAsyncResult.title")}</span></h3>
</div>
<div style="display:none;">
<table id="csvUploadStatusList" class="csv-upload-result tableStripe">
<thead>
<tr>
<th class="file-name">${m:rs("mtp-gem-messages", "generic.csvUploadAsyncResult.header.target")}</th>
<th class="file-name">${m:rs("mtp-gem-messages", "generic.csvUploadAsyncResult.header.fileName")}</th>
<th class="upload-date">${m:rs("mtp-gem-messages", "generic.csvUploadAsyncResult.header.uploadDate")}</th>
<th class="status">${m:rs("mtp-gem-messages", "generic.csvUploadAsyncResult.header.status")}</th>
<th class="message">${m:rs("mtp-gem-messages", "generic.csvUploadAsyncResult.header.message")}</th>
</tr>
</thead>
<tbody></tbody>
</table>
</div>
<%	} %>
</div>

</div>
