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

<%@ page import="java.net.URLEncoder"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Arrays"%>
<%@ page import="java.util.List"%>
<%@ page import="org.iplass.mtp.entity.BinaryReference"%>
<%@ page import="org.iplass.mtp.util.StringUtil" %>
<%@ page import="org.iplass.mtp.view.generic.OutputType"%>
<%@ page import="org.iplass.mtp.view.generic.editor.BinaryPropertyEditor" %>
<%@ page import="org.iplass.mtp.view.generic.editor.BinaryPropertyEditor.BinaryDisplayType"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.gem.command.binary.DownloadCommand"%>
<%@ page import="org.iplass.gem.command.Constants" %>

<%!
	String url(BinaryReference br, String action) {
		String url = action + "?" + Constants.ID + "=" + br.getLobId();
		if (br.getDefinitionName() != null) {
			url = url + "&" + Constants.DEF_NAME + "=" + br.getDefinitionName() 
				+ "&" + Constants.PROP_NAME + "=" + br.getPropertyName();
		}
		return url;
	}
%>

<%
	BinaryPropertyEditor editor = (BinaryPropertyEditor) request.getAttribute(Constants.EDITOR_EDITOR);
	Object value = request.getAttribute(Constants.EDITOR_PROP_VALUE);
	Boolean outputHidden = (Boolean) request.getAttribute(Constants.OUTPUT_HIDDEN);
	if (outputHidden == null) outputHidden = false;
	OutputType type = (OutputType) request.getAttribute(Constants.OUTPUT_TYPE);

	String propName = editor.getPropertyName();

	List<BinaryReference> brList = new ArrayList<BinaryReference>();
	if (value instanceof BinaryReference[]) {
		brList.addAll(Arrays.asList((BinaryReference[]) value));
	} else if (value instanceof BinaryReference) {
		brList.add((BinaryReference)value);
	}
	int length = brList.size();

	if (editor.getDisplayType() != BinaryDisplayType.HIDDEN) {
		//HIDDEN以外

		String contextPath = TemplateUtil.getTenantContextPath();
		String download = "";
		String ref = contextPath + "/" + DownloadCommand.REFERENCE_ACTION_NAME;
		if (StringUtil.isNotBlank(editor.getDownloadActionName())) {
			download = contextPath + "/" + editor.getDownloadActionName();
		} else {
			download = contextPath + "/" + DownloadCommand.DOWNLOAD_ACTION_NAME;
		}
		String pdfviewer = contextPath + "/" + DownloadCommand.PDFVIEWER_ACTION_NAME;
		String imgviewer = contextPath + "/" + DownloadCommand.IMGVIEWER_ACTION_NAME;

		String width = "";
		if (editor.getWidth() > 0) {
			width = " width=\"" + editor.getWidth() + "\"";
		}
		String height = "";
		if (editor.getHeight() > 0) {
			height = " height=\"" + editor.getHeight() + "\"";
		}

		String target = "";
		if (editor.isOpenNewTab()) {
			target = " target=\"_blank\"";
		}

		String listStyle = "";
		if (editor.getDisplayType() == BinaryDisplayType.PREVIEW) {
			listStyle = "noimage";
		}
%>
<ul class="data-label">
<%
		for (int i = 0; i < brList.size(); i++) {
			BinaryReference br = brList.get(i);
%>
<li class="list-bin <c:out value="<%=listStyle %>"/>">
<%
			if (editor.getDisplayType() == BinaryDisplayType.BINARY || editor.getDisplayType() == BinaryDisplayType.LINK || editor.getDisplayType() == BinaryDisplayType.LABEL) {
				if (br.getType().indexOf("application/pdf") != -1 && editor.isUsePdfjs()) {
					//PDFViewer
					String pdfPath = pdfviewer+ "?file=" + URLEncoder.encode(url(br, download), "utf-8");
%>
<a href="<%=pdfPath%>" <%=target %> class="link-bin"><c:out value="<%=br.getName() %>" /></a>
<%
				} else if (br.getType().indexOf("image") != -1 && editor.isUseImageViewer() && editor.isOpenNewTab()) {
					//ImageViewer＋別タブ
%>
<a href="javascript:void(0)" data-viewerUrl="<c:out value="<%=url(br, imgviewer) %>" />" data-lobid="<%=br.getLobId() %>" class="link-bin img-viewer" onclick="showImageViewer(this);"><c:out value="<%=br.getName() %>" /></a>
<%
				} else {
					//Download
%>
<a href="<c:out value="<%=url(br, download) %>" />" <%=target %> class="link-bin"><c:out value="<%=br.getName() %>" /></a>
<%
				}
			}

			//詳細画面の画像ローテ―トボタン
			if (type == OutputType.VIEW && editor.isShowImageRotateButton() && br.getType().indexOf("image") != -1) {
				if (editor.getDisplayType() == BinaryDisplayType.BINARY || editor.getDisplayType() == BinaryDisplayType.PREVIEW || editor.getDisplayType() == BinaryDisplayType.LABEL) {
%>
<span class="viewer-toolbar" style="display:inline-block">
<ul>
<li class="viewer-rotate-left" onclick="rotateImage('<%=br.getLobId() %>', -90)"></li>
<li class="viewer-rotate-right" onclick="rotateImage('<%=br.getLobId() %>', 90)"></li>
</ul>
</span>
<%
					
				}
			}

			if (editor.getDisplayType() == BinaryDisplayType.BINARY || editor.getDisplayType() == BinaryDisplayType.PREVIEW || editor.getDisplayType() == BinaryDisplayType.LABEL) {
				if (br.getType().indexOf("image") != -1) {
%>
<p>
<%
					if (editor.isUseImageViewer() && editor.isOpenNewTab()) {
						//別タブ
%>
<img src="<c:out value="<%=url(br, ref) %>" />" alt="<c:out value="<%=br.getName() %>" />" onload="imageLoad()" <%=width + height %> data-lobid="<%=br.getLobId() %>" class="img-viewer" onclick="showImageViewer(this);" data-viewerUrl="<c:out value="<%=url(br, imgviewer) %>" />" />
<%
					} else {
						if (editor.isUseImageViewer()) {
							//Inline
%>
<img src="<c:out value="<%=url(br, ref) %>" />" alt="<c:out value="<%=br.getName() %>" />" onload="imageLoad()" <%=width + height %> data-lobid="<%=br.getLobId() %>" class="img-viewer" onclick="inlineImageViewer(this);" />
<%
						} else {
%>
<img src="<c:out value="<%=url(br, ref) %>" />" alt="<c:out value="<%=br.getName() %>" />" onload="imageLoad()" <%=width + height %> data-lobid="<%=br.getLobId() %>" />
<%
						}
					}
%>
<span class="dummy"></span>
</p>
<%
				} else if (br.getType().indexOf("application/x-shockwave-flash") != -1) {
%>
<p>
<object data="<c:out value="<%=url(br, ref) %>" />" type="application/x-shockwave-flash" <%=width + height %> >
<param name="movie" value="<c:out value="<%=url(br, ref) %>" />" />
<param name="quality" value="high">
${m:rs("mtp-gem-messages", "generic.editor.binary.BinaryPropertyEditor_View.canNotViewFlash")}
</object>
</p>
<%
				} else if (br.getType().indexOf("video/mpeg") != -1) {
%>
<p>
<object classid="clsid:02BF25D5-8C17-4B23-BC80-D3488ABDDC6B" <%=width + height %>>
  <param name="src" value="<c:out value="<%=url(br, ref) %>" />" />
  <param name="autostart" value="true" />
  <param name="type" value="video/mpeg">
  <param name="controller" value="true" />
  <param name="scale" value="ToFit">
  <embed
      src="<c:out value="<%=url(br, ref) %>" />"
      scale="ToFit"
      <%=width + height %>>
  </embed>
</object>
</p>
<%
				} else if (br.getType().indexOf("video/quicktime") != -1) {
%>
<p>
<object classid="clsid:02BF25D5-8C17-4B23-BC80-D3488ABDDC6B"
    codebase="http://www.apple.com/qtactivex/qtplugin.cab" <%=width + height %>>
  <param name="src" value="<c:out value="<%=url(br, ref) %>" />">
  <param name="autoplay" value="true">
  <param name="type" value="video/quicktime">
  <param name="scale" value="ToFit">
  <embed src="<c:out value="<%=url(br, ref) %>" />"
       autoplay="true"
       scale="ToFit"
       type="video/quicktime"
       pluginspage="http://www.apple.com/quicktime/download/"
       <%=width + height %>>
</object>
</p>
<%
				} else if (br.getType().indexOf("audio/mpeg") > -1) {
%>
<audio id="<c:out value="<%=propName%>" /><%=i%>" src="<c:out value="<%=url(br, ref) %>" />" controls="controls"></audio>
<%
				} else if (br.getType().indexOf("audio/webm") > -1) {
%>
<audio id="<c:out value="<%=propName%>" /><%=i%>" src="<c:out value="<%=url(br, ref) %>" />" controls="controls"></audio>
<%
				} else if (br.getType().indexOf("audio/ogg") > -1) {
%>
<audio id="<c:out value="<%=propName%>" /><%=i%>" src="<c:out value="<%=url(br, ref) %>" />" controls="controls"></audio>
<%
				} else if (br.getType().indexOf("video/mp4") > -1) {
%>
<video id="<c:out value="<%=propName%>" /><%=i%>" src="<c:out value="<%=url(br, ref) %>" />" controls="controls" <%=width + height %>></video>
<%
				} else if (br.getType().indexOf("video/webm") > -1) {
%>
<video id="<c:out value="<%=propName%>" /><%=i%>" src="<c:out value="<%=url(br, ref) %>" />" controls="controls" <%=width + height %>></video>
<%
				} else if (br.getType().indexOf("video/ogg") > -1) {
					//音声ファイルでもvideo/oggになることがあるので拡張子で判断
					if (br.getName().indexOf(".") > 1 && br.getName().substring(br.getName().indexOf(".")).toLowerCase().indexOf("ogg") > -1) {
%>
<audio id="<c:out value="<%=propName%>" /><%=i%>" src="<c:out value="<%=url(br, ref) %>" />" controls="controls"></audio>
<%
					} else {
%>
<video id="<c:out value="<%=propName%>" /><%=i%>" src="<c:out value="<%=url(br, ref) %>" />" controls="controls" <%=width + height %>></video>
<%
					}
				} else if (br.getType().indexOf("video/x-ms-wmv") != -1) {
%>
<video id="<c:out value="<%=propName%>" /><%=i%>" src="<c:out value="<%=url(br, ref) %>" />" controls="controls" <%=width + height %>></video>
<%
				} else if (br.getType().indexOf("video/flv") > -1) {
%>
<video id="<c:out value="<%=propName%>" /><%=i%>" src="<c:out value="<%=url(br, ref) %>" />" controls="controls" <%=width + height %>></video>
<%
				} else if (br.getType().indexOf("application/octet-stream") > -1) {
					//mimeタイプで動画か判別不可な場合、拡張子で判断
					if (br.getName().indexOf(".") > -1) {
						if (br.getName().substring(br.getName().indexOf(".")).toLowerCase().indexOf("flv") > -1) {
							//拡張子flvなら動画扱い
%>
<video id="<c:out value="<%=propName%>" /><%=i%>" src="<c:out value="<%=url(br, ref) %>" />" controls="controls" <%=width + height %>></video>
<%
						}
					}
				}
			}

			if (outputHidden) {
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=br.getLobId() %>"/>" />
<%
			}
%>
</li>
<%
		}
%>
</ul>
<%
	} else {
		//HIDDEN
		for (int i = 0; i < brList.size(); i++) {
			BinaryReference br = brList.get(i);
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=br.getLobId() %>"/>" />
<%
		}
	}
%>
