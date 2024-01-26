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

<%@taglib prefix="m" uri="http://iplass.org/tags/mtp"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.google.gwt.safehtml.shared.SafeHtmlUtils"%>
<%@page import="org.iplass.adminconsole.server.base.service.AdminConsoleService"%>
<%@page import="org.iplass.gem.command.ViewUtil"%>
<%@page import="org.iplass.mtp.spi.ServiceRegistry"%>
<%@page import="org.iplass.mtp.tenant.Tenant"%>
<%@page import="org.iplass.mtp.util.StringUtil"%>
<%@page import="org.iplass.mtp.web.template.TemplateUtil"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<meta http-equiv="x-ua-compatible" content="IE=edge" />
<title>Admin Console</title>

<%!

	AdminConsoleService service = ServiceRegistry.getRegistry().getService(AdminConsoleService.class);

	String getResourcePath(String contextPath) {
		return contextPath + service.getResourcePrefixPath() + "/mtpadmin/";
	}

	boolean isBlank(String str) {
		int strLen;
		if(str == null || (strLen = str.length()) == 0){
			return true;
		}
		for(int i = 0; i < strLen; i++){
			if(!Character.isWhitespace(str.charAt(i))){
				return false;
			}
		}

		return true;
	}

	boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	//@see https://code.google.com/p/google-web-toolkit/source/browse/trunk/dev/core/src/com/google/gwt/util/tools/?r=10489#tools%2Fshared
	char[] HEX_CHARS = new char[] {
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
		'E', 'F'};

	/**
	 * Returns a string representation of the byte array as a series of
	 * hexadecimal characters.
	 *
	 * @param bytes byte array to convert
	 * @return a string representation of the byte array as a series of
	 *				 hexadecimal characters
	 */
	String toHexString(byte[] bytes) {
		char[] hexString = new char[2 * bytes.length];
		int j = 0;
		for (int i = 0; i < bytes.length; i++) {
			hexString[j++] = HEX_CHARS[(bytes[i] & 0xF0) >> 4];
			hexString[j++] = HEX_CHARS[bytes[i] & 0x0F];
		}
		return new String(hexString);
	}

%>

<%
	String scVersion = "13.0p_2022-11-05.js";				//SmartGWT変更時に設定

	String scVersionParam = toHexString(scVersion.getBytes());

	//テナント
	Tenant tenant = TemplateUtil.getTenant();

	//リソースパス
	String rsPath = getResourcePath(request.getContextPath());
	request.setAttribute("rsPath", StringUtil.escapeHtml(rsPath));
	
	//アイコン
	String iconUrl = ViewUtil.getTenantGemInfo(tenant).getIconUrl();
	if (StringUtil.isNotEmpty(iconUrl)) {
		String type = "";
		if (iconUrl.endsWith(".png")) {
			type = "image/png";
		} else if (iconUrl.endsWith(".gif")) {
			type = "image/gif";
		}
%>
<link rel="icon" href="<%=TemplateUtil.getResourceContentPath(iconUrl)%>" type="<%=type%>" />
<%
	}

	//言語
	String adminLang = "ja";	//未指定時はja
	String argLang = TemplateUtil.getLanguage();
	//現状はjaとenのみサポートなのでチェック
	if (!"ja".equalsIgnoreCase(argLang) && !"en".equalsIgnoreCase(argLang)) {
		//サポート外の場合はenに設定
		argLang = "en";
	}
	adminLang = argLang;
%>
<%-- GWT系のデフォルトメッセージ表示(カレンダー表示など)をlangに指定したものにする --%>
<meta name="gwt:property" content="locale=<%=adminLang%>" />

<script>
<!--
var _tenantId=<%=tenant.getId()%>;
var _language="<%=SafeHtmlUtils.fromString(argLang).asString()%>";
<%-- SuperDevモードでSkinがロードできない対応
http://forums.smartclient.com/forum/smart-gwt-technical-q-a/32571-gwt-2-7-smartgwt-5-does-not-load-skins
--%>
window.isomorphicDir = '${rsPath}sc/';
<%-- imageパスとしてテナント名を除外 --%>
window.imgDir = '${rsPath}images/';
//-->
</script>

<%-- include the SC Core API --%>
<script src="${rsPath}sc/modules/ISC_Core.js?isc_version=<%=scVersionParam%>"></script>

<%-- include SmartClient --%>
<script src="${rsPath}sc/modules/ISC_Foundation.js?isc_version=<%=scVersionParam%>"></script>
<script src="${rsPath}sc/modules/ISC_Containers.js?isc_version=<%=scVersionParam%>"></script>
<script src="${rsPath}sc/modules/ISC_Grids.js?isc_version=<%=scVersionParam%>"></script>
<script src="${rsPath}sc/modules/ISC_Forms.js?isc_version=<%=scVersionParam%>"></script>
<script src="${rsPath}sc/modules/ISC_RichTextEditor.js?isc_version=<%=scVersionParam%>"></script>
<script src="${rsPath}sc/modules/ISC_DataBinding.js?isc_version=<%=scVersionParam%>"></script>
<script src="${rsPath}sc/modules/ISC_Calendar.js?isc_version=<%=scVersionParam%>"></script>
<script src="${rsPath}sc/modules/ISC_Drawing.js?isc_version=<%=scVersionParam%>"></script>

<script src="${rsPath}locale/locale_<%=adminLang%>.js?cv=<%=TemplateUtil.getAPIVersion()%>"></script>

<%-- load skin --%>
<script src="${rsPath}sc/skins/Enterprise/load_skin.js?isc_version=<%=scVersionParam%>"></script>

<script src="${rsPath}mtpadmin.nocache.js?cv=<%=TemplateUtil.getAPIVersion()%>"></script>

<%
	String aceVersion = "1.12.3";

	//switch ace source mode, [src-min-noconflict, src-noconflict, src-min, src]
	//String aceSrcMode = "src-min-noconflict";
	String aceSrcMode = "src-noconflict";

	String acePath = TemplateUtil.getStaticContentPath() + "/webjars/ace-builds/" + aceVersion + "/" + aceSrcMode + "/";

	request.setAttribute("acePath", StringUtil.escapeHtml(acePath));
%>
<%-- ACE - main .js file --%>
<script src="${acePath}ace.js"></script>

<%-- Get .js files for any needed ACE modes and themes --%>
<script src="${acePath}theme-ambiance.js" charset="utf-8"></script>
<script src="${acePath}theme-cobalt.js" charset="utf-8"></script>
<script src="${acePath}theme-chrome.js" charset="utf-8"></script>
<script src="${acePath}theme-eclipse.js" charset="utf-8"></script>
<script src="${acePath}theme-gob.js" charset="utf-8"></script>
<script src="${acePath}theme-solarized_dark.js" charset="utf-8"></script>
<script src="${acePath}theme-textmate.js" charset="utf-8"></script>
<script src="${acePath}theme-terminal.js" charset="utf-8"></script>
<script src="${acePath}theme-xcode.js" charset="utf-8"></script>

<script src="${acePath}mode-css.js" charset="utf-8"></script>
<script src="${acePath}mode-groovy.js" charset="utf-8"></script>
<script src="${acePath}mode-html.js" charset="utf-8"></script>
<script src="${acePath}mode-java.js" charset="utf-8"></script>
<script src="${acePath}mode-javascript.js" charset="utf-8"></script>
<script src="${acePath}mode-jsp.js" charset="utf-8"></script>
<script src="${acePath}mode-sql.js" charset="utf-8"></script>
<script src="${acePath}mode-text.js" charset="utf-8"></script>
<script src="${acePath}mode-typescript.js" charset="utf-8"></script>
<script src="${acePath}mode-xml.js" charset="utf-8"></script>

<%-- Ace snippets files --%>
<script src="${acePath}ext-language_tools.js" charset="utf-8"></script>
<script src="${acePath}snippets/css.js" charset="utf-8"></script>
<script src="${acePath}snippets/groovy.js" charset="utf-8"></script>
<script src="${acePath}snippets/html.js" charset="utf-8"></script>
<script src="${acePath}snippets/java.js" charset="utf-8"></script>
<script src="${acePath}snippets/javascript.js" charset="utf-8"></script>
<script src="${acePath}snippets/jsp.js" charset="utf-8"></script>
<script src="${acePath}snippets/sql.js" charset="utf-8"></script>
<script src="${acePath}snippets/text.js" charset="utf-8"></script>
<script src="${acePath}snippets/typescript.js" charset="utf-8"></script>
<script src="${acePath}snippets/xml.js" charset="utf-8"></script>
</head>
<body>
	<iframe src="javascript:''" id="__gwt_historyFrame" style="position:absolute;width:0;height:0;border:0"></iframe>
	<%-- <div id="mainArea"></div> --%>
	<div id="__gwt_exportFrame"></div>
</body>
</html>
