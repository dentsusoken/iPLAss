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

<%@ page import="java.net.URLEncoder"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Arrays"%>
<%@ page import="java.util.List"%>
<%@ page import="org.iplass.mtp.auth.AuthContext" %>
<%@ page import="org.iplass.mtp.entity.permission.EntityPropertyPermission" %>
<%@ page import="org.iplass.mtp.entity.BinaryReference"%>
<%@ page import="org.iplass.mtp.entity.definition.EntityDefinition" %>
<%@ page import="org.iplass.mtp.entity.definition.PropertyDefinition" %>
<%@ page import="org.iplass.mtp.util.StringUtil" %>
<%@ page import="org.iplass.mtp.view.generic.editor.BinaryPropertyEditor" %>
<%@ page import="org.iplass.mtp.view.generic.editor.BinaryPropertyEditor.BinaryDisplayType" %>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil.TokenOutputType"%>
<%@ page import="org.iplass.gem.command.binary.DownloadCommand"%>
<%@ page import="org.iplass.gem.command.binary.UploadCommand"%>
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
	PropertyDefinition pd = (PropertyDefinition) request.getAttribute(Constants.EDITOR_PROPERTY_DEFINITION);
	String execType = (String) request.getAttribute(Constants.EXEC_TYPE);
	Boolean nestDummyRow = (Boolean) request.getAttribute(Constants.EDITOR_REF_NEST_DUMMY_ROW);

	Boolean isVirtual = (Boolean) request.getAttribute(Constants.IS_VIRTUAL);
	if (isVirtual == null) isVirtual = false;

	boolean isInsert = Constants.EXEC_TYPE_INSERT.equals(execType);
	String defName = (String)request.getAttribute(Constants.DEF_NAME);
	String propName = editor.getPropertyName();
	AuthContext auth = AuthContext.getCurrentContext();
	boolean isEditable = true;
	if (isVirtual) {
		isEditable = true;//仮想プロパティは権限チェック要らない
	} else {
		if(isInsert) {
			isEditable = auth.checkPermission(new EntityPropertyPermission(defName, pd.getName(), EntityPropertyPermission.Action.CREATE));
		} else {
			isEditable = auth.checkPermission(new EntityPropertyPermission(defName, pd.getName(), EntityPropertyPermission.Action.UPDATE));
		}
	}
	boolean updatable = ((pd == null || pd.isUpdatable()) || isInsert) && isEditable;

	//詳細編集
	if (editor.getDisplayType() != BinaryDisplayType.HIDDEN && editor.getDisplayType() != BinaryDisplayType.LABEL && updatable) {

		boolean hideDeleteButton = editor.isHideDeleteButton();
		boolean hideSelectButton = editor.isHideSelectButton();

		String contextPath = TemplateUtil.getTenantContextPath();
		String upload = "";
		if (StringUtil.isNotBlank(editor.getUploadActionName())) {
			upload = contextPath + "/" + editor.getUploadActionName();
		} else {
			upload = contextPath + "/" + UploadCommand.ACTION_NAME;
		}

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

		String displayType = editor.getDisplayType().name();

		String ulId = "ul_" + propName;
		String fileId = "file_" + propName;

		List<BinaryReference> brList = new ArrayList<BinaryReference>();
		if (value instanceof BinaryReference[]) {
			brList.addAll(Arrays.asList((BinaryReference[]) value));
		} else if (value instanceof BinaryReference) {
			brList.add((BinaryReference)value);
		}
		int length = brList.size();

		String style = length >= pd.getMultiplicity() ? "display : none;" : "";
		String cls = "";
		if (nestDummyRow != null && nestDummyRow) {
			cls = "";
		} else {
			cls = "upload-button";
		}
		String multiple = "";
		if (pd.getMultiplicity() > 1) multiple = "multiple";

		String viewName = (String)request.getAttribute(Constants.VIEW_NAME);
		if (viewName == null) {
			viewName = "";
		}
		// NOTE ネストプロパティの場合、defName ではネスト先のエンティティ名が取得できてしまう。
		//      entityDefinition キーで取得する値は表示しているビューのエンティティ定義なので、そこからエンティティ名を取得する
		EntityDefinition entityDefinition = (EntityDefinition)request.getAttribute(Constants.ENTITY_DEFINITION);
		String rootDefName = defName;
		if (entityDefinition != null) {
			rootDefName = entityDefinition.getName();
		}

		if (request.getAttribute(Constants.UPLOAD_LIB_LOADED) == null) {
			request.setAttribute(Constants.UPLOAD_LIB_LOADED, true);
%>
<script type="text/javascript" src="${m:esc(staticContentPath)}/webjars/blueimp-file-upload/10.30.1/js/jquery.fileupload.js?cv=<%=TemplateUtil.getAPIVersion()%>"></script>
<script type="text/javascript" src="${m:esc(staticContentPath)}/webjars/blueimp-file-upload/10.30.1/js/jquery.iframe-transport.js?cv=<%=TemplateUtil.getAPIVersion()%>"></script>
<%
		}
		if (!hideSelectButton){
%>
<input type="file" id="<c:out value="<%=fileId %>"/>" name="filePath" value="${m:rs('mtp-gem-messages', 'generic.editor.binary.BinaryPropertyEditor_Edit.upload')}"
 style="<c:out value="<%=style %>"/>" class="<c:out value="<%=cls %>"/>" data-pname="<c:out value="<%=propName %>"/>" data-displayType="<c:out value="<%=displayType%>"/>"
 data-binCount="<c:out value="<%=length %>" />" data-uploadUrl="<c:out value="<%=upload %>" />" data-downloadUrl="<c:out value="<%=download %>" />" data-refUrl="<c:out value="<%=ref %>" />"
 data-pdfviewerUrl="<c:out value="<%=pdfviewer %>" />" data-usePdfjs="<%=editor.isUsePdfjs()%>"
 data-imgviewerUrl="<c:out value="<%=imgviewer %>" />" data-useImageViewer="<%=editor.isUseImageViewer()%>"
 data-showImageRotateButton="<%=editor.isShowImageRotateButton() %>"  data-openNewTab="<%=editor.isOpenNewTab() %>"
 data-multiplicity="<c:out value="<%=pd.getMultiplicity() %>" />" data-binWidth="<c:out value="<%=editor.getWidth() %>" />"
 data-binHeight="<c:out value="<%=editor.getHeight() %>" />" data-token="${m:fixToken()}" <c:out value="<%=multiple%>" /> 
 data-vname="<c:out value="<%=viewName %>"/>" data-dname="<c:out value="<%=rootDefName %>"/>"
 />
 <%
		}
 %>

<div>
<span id="em_<c:out value="<%=propName %>"/>" class="ul_error error" style="display:none;" ></span>
<p id="img_<c:out value="<%=propName %>"/>" class="loading" style="display:none;" />
</div>
<ul id="<c:out value="<%=ulId %>"/>">
<%
		String listStyle = "";
		if (editor.getDisplayType() == BinaryDisplayType.PREVIEW) {
			listStyle = "noimage";
		}
		for (int i = 0; i < brList.size(); i++) {
			BinaryReference br = brList.get(i);
			String liId = "li_" + propName + i;
%>
<li id="<c:out value="<%=liId %>"/>" class="list-bin <c:out value="<%=listStyle %>"/>">
<%
			//ファイルリンク
			if (editor.getDisplayType() == BinaryDisplayType.BINARY || editor.getDisplayType() == BinaryDisplayType.LINK) {
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

			//画像ローテ―トボタン
			if (editor.isShowImageRotateButton() && br.getType().indexOf("image") != -1) {
				if (editor.getDisplayType() == BinaryDisplayType.BINARY || editor.getDisplayType() == BinaryDisplayType.PREVIEW) {
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

			//削除ボタン
			if (!hideDeleteButton){
%>
 <a href="javascript:void(0)" class="binaryDelete del-btn link-bin" data-fileId="<c:out value="<%=fileId %>"/>">${m:rs("mtp-gem-messages", "generic.editor.binary.BinaryPropertyEditor_Edit.delete")}</a>
<%
			}

			if (editor.getDisplayType() == BinaryDisplayType.BINARY || editor.getDisplayType() == BinaryDisplayType.PREVIEW) {
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
${m:rs("mtp-gem-messages", "generic.editor.binary.BinaryPropertyEditor_Edit.canNotViewFlash")}
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
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=br.getLobId() %>"/>" />
</li>
<%
		}
%>
</ul>
<%
	} else {
		//HIDDENか更新不可

		if (editor.getDisplayType() != BinaryDisplayType.HIDDEN) {
			request.setAttribute(Constants.OUTPUT_HIDDEN, true);
		}
%>
<jsp:include page="BinaryPropertyEditor_View.jsp"></jsp:include>
<%
		if (editor.getDisplayType() != BinaryDisplayType.HIDDEN) {
			request.removeAttribute(Constants.OUTPUT_HIDDEN);
		}
	}
%>
