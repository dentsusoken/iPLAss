<%--
 Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.

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

<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" uri="http://iplass.org/tags/mtp"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ page import="org.iplass.mtp.view.generic.element.section.SearchConditionSection"%>
<%@ page import="org.iplass.mtp.view.generic.SearchFormView"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil"%>
<%@ page import="org.iplass.gem.command.generic.search.SearchFormViewData"%>
<%@ page import="org.iplass.gem.command.Constants"%>
<%@ page import="org.iplass.gem.command.ViewUtil"%>

<%
	SearchFormViewData data = (SearchFormViewData) request.getAttribute(Constants.DATA);
	SearchFormView view = data.getView();
	SearchConditionSection section = view.getCondSection();
%>
<div id="csv-download-dialog" class="mtp-jq-dialog" title="${m:rs('mtp-gem-messages', 'generic.element.section.SearchConditionSection.dwnCsv')}" style="display:none;">
	<fieldset class="fs-checkbox" id="fsForUpload">
		<input type="checkbox" name="forUpload" id="forUpload" value="1"  onchange="checkForUpload()"/>
		<label for="forUpload">${m:rs("mtp-gem-messages", "generic.element.section.SearchConditionSection.forUpload")}</label>
	</fieldset>
	<fieldset class="fs-checkbox disabled" id="fsNoDispName">
		<input type="checkbox" name="noDispName" id="noDispName" value="1" disabled="disabled" />
		<label for="noDispName">${m:rs("mtp-gem-messages", "generic.element.section.SearchConditionSection.noDispName")}</label>
	</fieldset>
	<fieldset class="fs-checkbox" id="fsDownloadCodeValue">
		<input type="checkbox" name="downloadCodeValue" id="downloadCodeValue" value="1" />
		<label for="downloadCodeValue">${m:rs("mtp-gem-messages", "generic.element.section.SearchConditionSection.downloadCodeValue")}</label>
	</fieldset>
<%
	if (StringUtil.isNotBlank(section.getCsvdownloadProperties())) {
		// CSVを検索結果ベースで出力するか
%>
	<fieldset class="fs-checkbox" id="fsOutputResult">
		<input type="checkbox" name="outputResult" id="outputResult" value="1"  onchange="checkOutputResult()"/>
		<label for="outputResult">${m:rs("mtp-gem-messages", "generic.element.section.SearchConditionSection.outputDisplayItem")}</label>
	</fieldset>
<%
	}
	if (ViewUtil.isEntityViewCsvDownloadSpecifyCharacterCode(section.getSpecifyCharacterCode())){
%>
	<fieldset>
		<select name="characterCode" id="characterCode">
<%		for (String characterCode : ViewUtil.getCsvDownloadCharacterCode()) {%>
			<option value="<%=characterCode%>"><%=characterCode%></option>
<%		}%>
		</select>
		<label for="characterCode">${m:rs("mtp-gem-messages", "generic.element.section.SearchConditionSection.charCd")}</label>
	</fieldset>
<%	}%>
	<input type="hidden" name="execType"/>
	<input type="hidden" id="trigger"/>
</div>

<style type="text/css">
.fs-checkbox.disabled .pseudo-checkbox::after {
  -webkit-transition: 0ms;
  transition: 0ms;
}
</style>
<script type="text/javascript">
function checkForUpload() {
	if ($("#forUpload").prop('checked')) {
		$("#characterCode").val("UTF-8");
		$("#characterCode").prop('disabled', true);
		$("#noDispName").prop('disabled', false);
		$("#downloadCodeValue").prop('disabled', true);
		$("#outputResult").prop('disabled', true);
		$("#fsNoDispName").removeClass('disabled');
		$("#fsDownloadCodeValue").addClass('disabled');
		$("#fsOutputResult").addClass('disabled');
	} else {
		$("#characterCode").prop('disabled', false);
		$("#noDispName").prop('disabled', true);
		$("#downloadCodeValue").prop('disabled', false);
		$("#outputResult").prop('disabled', false);
		$("#fsNoDispName").addClass('disabled');
		$("#fsDownloadCodeValue").removeClass('disabled');
		$("#fsOutputResult").removeClass('disabled');
	}
}
<%
if (StringUtil.isNotBlank(section.getCsvdownloadProperties())) {
%>
function checkOutputResult() {
	if ($("#outputResult").prop("checked")) {
		$("#forUpload").prop('disabled', true);
		$("#fsForUpload").addClass('disabled');
	} else {
		$("#forUpload").prop('disabled', false);
		$("#fsForUpload").removeClass('disabled');
	}
}
<%
}
%>
function showCsvDownloadDialog(searchType, buttonId, validate, callback) {
	var dialog = $("#csv-download-dialog");
	dialog.dialog({
		resizable: false,
		autoOpen: false,
		height: 200,
		width: 300,
		modal: true,
		buttons: [
			{
				text: "${m:rs('mtp-gem-messages', 'generic.element.section.SearchConditionSection.csvDl')}",
				class: "dialog-csvdl-btn",
				click: function() {
						var execType = $("input[name=execType]", dialog).val();
						var forUpload = $("#forUpload").is(':checked');
						var noDispName = $("#noDispName").is(':checked');
						var downloadCodeValue = $("#downloadCodeValue").prop('checked');
						var characterCode = $("#characterCode").val();
						var outputResult = null;
						<%if (StringUtil.isNotBlank(section.getCsvdownloadProperties())) {%>
						outputResult = $("#outputResult").is(':checked');
						<%}%>

						var id = $("#trigger", dialog).val();
						var $target = $("#"+id);
						callback.call($target, execType, forUpload, characterCode, noDispName, outputResult, downloadCodeValue);

						$(this).dialog("close");
				}
			},
			{
				text: "${m:rs('mtp-gem-messages', 'generic.element.section.SearchConditionSection.cancel')}",
				click: function() {
						$(this).dialog("close");
				}
			}
		],
		close: function() {
		}
	});
	dialog.on("dialogopen", function(e) {
		adjustDialogLayer($(".ui-widget-overlay"));
	});

	$("#" + buttonId).on("click", function() {
		$("input[name=execType]", dialog).val(searchType);
		var id = $(this).attr("id");
		if (!validation(searchType)) return;

		if (validate != "") {
			<%-- webapi.js --%>
			searchValidate(validate, searchType, searchType + 'Form', function() {
				$("#trigger", dialog).val(id);
				dialog.dialog("open");
			});
		} else {
			$("#trigger", dialog).val(id);
			dialog.dialog("open");
		}
	});
}
</script>
