<%--
 Copyright (C) 2021 DENTSU SOKEN INC. All Rights Reserved.
 
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
<%@page import="org.iplass.gem.command.Constants"%>
<%@page import="org.iplass.gem.command.binary.DownloadCommand"%>
<%@page import="org.iplass.mtp.entity.BinaryReference"%>
<%@page import="org.iplass.mtp.util.StringUtil"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<meta http-equiv="x-ua-compatible" content="IE=edge" />
<%
	BinaryReference br = (BinaryReference)request.getAttribute(Constants.CMD_RSLT_STREAM);
	if (br == null || br.getType().indexOf("image") < 0) {
		return;
	}
	
	String contextPath = TemplateUtil.getTenantContextPath();
	String download = contextPath + "/" + DownloadCommand.REFERENCE_ACTION_NAME;
	String url = download + "?id=" + br.getLobId();
	if (br.getDefinitionName() != null) {
		url = url + "&defName=" + br.getDefinitionName() + "&propName=" + br.getPropertyName();
	}
	
%>
<title>Image Viewer</title>

<%@include file="../layout/resource/coreResource.inc.jsp"%>
<%@include file="../layout/resource/viewerjsResource.inc.jsp"%>
</head>
<body>
<div id="container">
<div id="content">
<div id="content-inner">
<div id="main">
<div id="main-inner">
<img id="viewer_dummy_img_<%=br.getLobId() %>" src="<c:out value="<%=url %>" />" class="img-viewer" style="display:none;" alt="<c:out value="<%=br.getName() %>" />" data-lobid="<%=br.getLobId() %>" />
</div>
</div>
</div>
</div>
</div>
<script>
$(function(){
	var image = document.getElementById("viewer_dummy_img_<%=br.getLobId() %>");
	var lobId = image.getAttribute("data-lobid");
	
	//起動元で保存されている設定でViewerを生成
	var callback = window.opener.document.scriptContext["imgViewerCallback"];
	if (callback && $.isFunction(callback)) {
		var viewer = callback.call(this, image);
		viewer.view();
	} else {
		var viewer = new Viewer(image, {
			//モーダルを閉じない
			backdrop: false,
			//1つなのでナビは非表示
			navbar: false,
			toolbar: {
				zoomIn: true,
				zoomOut: true,
				oneToOne: true,
				reset: true,
				//画像１つずつに設定するため前後なし
				prev: false,
				next: false,
				//全画面表示なし
				play: false, 
				rotateLeft: true,
				rotateRight: true,
				flipHorizontal: true,
				flipVertical: true,
			}
		}).view(); 
	}
});
</script>
</body>
</html>