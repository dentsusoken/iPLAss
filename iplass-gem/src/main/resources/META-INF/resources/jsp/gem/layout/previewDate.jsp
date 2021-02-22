<%--
 Copyright (C) 2021 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.

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

<%@taglib prefix="m" uri="http://iplass.org/tags/mtp"%>
<%@page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="org.iplass.gem.command.Constants"%>
<%@page import="org.iplass.gem.command.preview.PreviewDateViewCommand"%>
<%@page import="org.iplass.mtp.util.StringUtil"%>
<%
String modalTarget = request.getParameter(Constants.MODAL_TARGET);
if (modalTarget == null) modalTarget = "";
request.setAttribute(Constants.MODAL_TARGET, modalTarget);

Timestamp previewDate = (Timestamp)request.getAttribute(PreviewDateViewCommand.PREVIEW_DATE);
%>
<div class="preview-date">
<h3 class="hgroup-02 hgroup-02-01">${m:rs('mtp-gem-messages', 'layout.preview.PreviewDateTitle')}</h3>

<form id="detailForm">
<div class="formArchive">
<div>
<table class="tbl-preview-date tbl-section mb10">
	<tbody>
		<tr>
			<th class="section-data col1">${m:rs('mtp-gem-messages', 'layout.preview.PreviewDate')}</th>
			<td class="section-data col1">
				<input type="text" class="datetimepicker inpbr" data-timeformat="HH:mm:ss" data-stepmin="1" />
			</td>
		</tr>
	</tbody>
</table>
</div>
</div>

<div class="operation-bar operation-bar_bottom">
<ul class="list_operation">
	<li class="btn">
		<input type="button" class="gr-btn btn-change" value="${m:rs('mtp-gem-messages', 'layout.header.change')}">
	</li>
	<li class="btn">
		<input type="button" class="gr-btn btn-reset" value="${m:rs('mtp-gem-messages', 'layout.header.reset')}">
	</li>
</ul>
</div>

</form>
</div>

<script type="text/javascript">
$(function() {
	var key = "${m:escJs(modalTarget)}";
	var modalTarget = key != "" ? key : null;
	var target = null;
	var windowManager = document.rootWindow.scriptContext["windowManager"];
	if (modalTarget && windowManager && windowManager[document.targetName]) {
		$("#modal-title-" + modalTarget, parent.document).text("${m:escJs(previewDateTitle)}");
		target = windowManager[modalTarget];
	} else {
		$("#modal-title", parent.document).text("${m:escJs(previewDateTitle)}");
		target = parent.document;
	}

	var $previewDatetime = $(".datetimepicker", ".preview-date");
	
	$("input.btn-change", ".preview-date").on("click", function() {
		var date = $previewDatetime.val();
		if (date != null && date != "") {
			var datetimeString = convertFromLocaleDatetimeString(date);
			setSystemDate(datetimeString, function() {
				alert("${m:rs('mtp-gem-messages', 'layout.preview.updatedPreviewDateMsg')}");
			});
		} else {
			alert("${m:rs('mtp-gem-messages', 'layout.header.incorrect')}");
		}
	});

	$("input.btn-reset", ".preview-date").on("click", function() {
		setSystemDate(null, function() {
			$previewDatetime.val("");
			alert("${m:rs('mtp-gem-messages', 'layout.preview.updatedPreviewDateMsg')}");
		});
	});
	
<%	if (previewDate != null) { %>
	var previewDate = new Date();
	previewDate.setTime(<%=previewDate.getTime()%>);
	var localizedString = convertToLocaleDatetimeString(dateUtil.format(previewDate, dateUtil.getServerDatetimeFormat()), dateUtil.getServerDatetimeFormat());
	$previewDatetime.val(localizedString);
<%	} %>
});
</script>
