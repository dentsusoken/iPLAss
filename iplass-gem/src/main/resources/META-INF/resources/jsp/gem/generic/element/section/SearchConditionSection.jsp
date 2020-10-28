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

<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>
<%@ page import="org.iplass.mtp.auth.AuthContext"%>
<%@ page import="org.iplass.mtp.entity.permission.EntityPermission"%>
<%@ page import="org.iplass.mtp.entity.definition.properties.LongTextProperty"%>
<%@ page import="org.iplass.mtp.entity.definition.properties.ReferenceProperty"%>
<%@ page import="org.iplass.mtp.entity.definition.EntityDefinition"%>
<%@ page import="org.iplass.mtp.entity.definition.EntityDefinitionManager"%>
<%@ page import="org.iplass.mtp.entity.definition.PropertyDefinition"%>
<%@ page import="org.iplass.mtp.entity.definition.PropertyDefinitionType"%>
<%@ page import="org.iplass.mtp.entity.definition.VersionControlType"%>
<%@ page import="org.iplass.mtp.entity.Entity"%>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.filter.EntityFilterItem"%>
<%@ page import="org.iplass.mtp.view.generic.editor.*"%>
<%@ page import="org.iplass.mtp.view.generic.common.AutocompletionSetting"%>
<%@ page import="org.iplass.mtp.view.generic.element.BlankSpace"%>
<%@ page import="org.iplass.mtp.view.generic.element.Element"%>
<%@ page import="org.iplass.mtp.view.generic.element.VirtualPropertyItem"%>
<%@ page import="org.iplass.mtp.view.generic.element.property.validation.RequiresAtLeastOneFieldValidator"%>
<%@ page import="org.iplass.mtp.view.generic.element.property.validation.PropertyValidationCondition"%>
<%@ page import="org.iplass.mtp.view.generic.element.property.validation.ViewValidatorBase"%>
<%@ page import="org.iplass.mtp.view.generic.element.property.PropertyItem"%>
<%@ page import="org.iplass.mtp.view.generic.element.section.FilterSetting"%>
<%@ page import="org.iplass.mtp.view.generic.element.section.SearchConditionSection"%>
<%@ page import="org.iplass.mtp.view.generic.element.section.SearchConditionSection.CsvDownloadSpecifyCharacterCode"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@ page import="org.iplass.mtp.view.generic.OutputType"%>
<%@ page import="org.iplass.mtp.view.generic.SearchFormView"%>
<%@ page import="org.iplass.mtp.view.generic.ViewConst"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil"%>
<%@ page import="org.iplass.mtp.ManagerLocator"%>
<%@ page import="org.iplass.gem.command.generic.delete.TrashCommand"%>
<%@ page import="org.iplass.gem.command.generic.search.CsvDownloadCommand"%>
<%@ page import="org.iplass.gem.command.generic.search.SearchCommand"%>
<%@ page import="org.iplass.gem.command.generic.search.SearchFormViewData"%>
<%@ page import="org.iplass.gem.command.generic.search.SearchValidateCommand"%>
<%@ page import="org.iplass.gem.command.Constants"%>
<%@ page import="org.iplass.gem.command.GemResourceBundleUtil"%>
<%@ page import="org.iplass.gem.command.ViewUtil"%>
<%@ page import="org.iplass.mtp.spi.ServiceRegistry"%>
<%@ page import="org.iplass.mtp.impl.entity.property.PropertyService"%>


<%!
	boolean isDispProperty(PropertyDefinition property) {
		if (property instanceof LongTextProperty) {
			//ロングテキストを表示するかは設定次第
			PropertyService service = ServiceRegistry.getRegistry().getService(PropertyService.class);
			return service.isRemainInlineText();
		}

// 		if (property instanceof ReferenceProperty) {
// 			if (property.getMultiplicity() != 1) return false;
// 		}
		return true;
	}
	boolean checkDtlCndData(String searchCond, int index) {
		return (searchCond.contains(Constants.DETAIL_COND_PROP_NM + index)
				|| searchCond.contains(Constants.DETAIL_COND_PREDICATE + index)
		  		|| searchCond.contains(Constants.DETAIL_COND_VALUE + index));
	}
	String getSearchCondParam(String src, String key) {
		if (StringUtil.isBlank(src) || !src.contains(key + "=")) return null;
		String tmp = src.substring(src.indexOf(key + "="));
		if (tmp.contains("&")) tmp = tmp.substring(tmp.indexOf("=") + 1, tmp.indexOf("&"));
		return tmp;
	}

	Map<String, String> createReferencePropTypesMap(ReferencePropertyEditor editor, String propName, ReferenceProperty rp, Map<String, String> propTypeMap) {
		EntityDefinitionManager edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
		EntityDefinition ed = edm.get(rp.getObjectDefinitionName());

		if (editor.getNestProperties().isEmpty()) {
			//Nestが未指定の場合はname
			propTypeMap.put(propName, PropertyDefinitionType.REFERENCE.toString());
		} else {
			if (editor.isUseNestConditionWithProperty()) {
				//ネストと同時に名前も表示にチェックが付いている場合はname
				propTypeMap.put(propName, PropertyDefinitionType.REFERENCE.toString());
			}

			for (NestProperty np : editor.getNestProperties()) {
				String npName = propName + "." + np.getPropertyName();
				PropertyDefinition pd = ed.getProperty(np.getPropertyName());
				if (np.getEditor() instanceof ReferencePropertyEditor) {
				    createReferencePropTypesMap((ReferencePropertyEditor) np.getEditor(), npName, (ReferenceProperty) pd, propTypeMap);
				} else {
					propTypeMap.put(npName, pd.getType().toString());
				}
			}
		}

		return propTypeMap;
	}
	PropertyItem getProperty(String propName, SearchFormView view) {
		String _propName = null;
		if (propName.contains(".")) {
			//参照のネストの場合は参照自体を対象にする
			int indexOfDot = propName.indexOf('.');
			_propName = propName.substring(0, indexOfDot);
		} else {
			_propName = propName;
		}
		return ViewUtil.filterPropertyItem(view.getCondSection().getElements(), propName);
	}
	void getRequiresAtLeastOneFieldValidatorNestParam(String originPropName, String propName, ReferencePropertyEditor editor, StringBuilder properties, boolean isNormal) {
		String objPropName = null;
		if (propName.contains(".")) {
			//nestのnest
			int indexOfDot = propName.indexOf('.');
			propName.substring(0, indexOfDot);
			//String subPropPath = propName.substring(indexOfDot + 1, propName.length());
		} else {
			objPropName = propName;
		}

		for (NestProperty np : editor.getNestProperties()) {
			if (np.getEditor() == null) continue;

			if (objPropName.equals(np.getPropertyName())) {
				getRequiresAtLeastOneFieldValidatorNestParam(np.getEditor(), originPropName, propName, objPropName, properties, isNormal);
			}
		}
	}
	void getRequiresAtLeastOneFieldValidatorNestParam(PropertyEditor editor, String originPropName, String propName, String objPropName, StringBuilder properties, boolean isNormal) {
		if (editor instanceof DateTimePropertyEditor) {
			DateTimePropertyEditor _editor = (DateTimePropertyEditor) editor;
			if (isNormal) {
				boolean hideFrom = _editor.isSingleDayCondition() ? false : _editor.isHideSearchConditionFrom();
				boolean hideTo = _editor.isSingleDayCondition() ? true : _editor.isHideSearchConditionTo();
				if (!hideFrom) {
					properties.append(",");
					properties.append("\"").append(StringUtil.escapeJavaScript(originPropName)).append("From\"");
				}
				if (!hideTo) {
					properties.append(",");
					properties.append("\"").append(StringUtil.escapeJavaScript(originPropName)).append("To\"");
				}
			} else {
				properties.append(",");
				properties.append("\"").append(StringUtil.escapeJavaScript(originPropName)).append("\"");
			}
		} else if (editor instanceof NumberPropertyEditor) {
			NumberPropertyEditor _editor = (NumberPropertyEditor) editor;
			properties.append(",");
			properties.append("\"").append(StringUtil.escapeJavaScript(originPropName)).append("\"");
			if (isNormal && _editor.isSearchInRange()) {
				properties.append(",");
				properties.append("\"").append(StringUtil.escapeJavaScript(originPropName)).append("To\"");
			}
		} else if (editor instanceof ReferencePropertyEditor) {
			if (!propName.contains(".")) {
				//この階層のネストが対象
				properties.append(",");
				properties.append("\"").append(StringUtil.escapeJavaScript(originPropName)).append("\"");
			} else {
				//さらに子階層のnestが対象
				int indexOfDot = objPropName.indexOf('.');
				String subPropName = objPropName.substring(indexOfDot + 1, objPropName.length());
				getRequiresAtLeastOneFieldValidatorNestParam(originPropName, subPropName, (ReferencePropertyEditor) editor, properties, isNormal);
			}
		} else if (editor instanceof ExpressionPropertyEditor) {
			ExpressionPropertyEditor _editor = (ExpressionPropertyEditor) editor;
			if (_editor.getEditor() == null) {
				properties.append(",");
				properties.append("\"").append(StringUtil.escapeJavaScript(originPropName)).append("\"");
			} else {
				getRequiresAtLeastOneFieldValidatorNestParam(((ExpressionPropertyEditor) editor).getEditor(), originPropName, propName, objPropName, properties, isNormal);
			}
		} else {
			//その他のEditor
			properties.append(",");
			properties.append("\"").append(StringUtil.escapeJavaScript(originPropName)).append("\"");
		}
	}
	//defaultSearchCondからリクエストパラメータor画面定義の動的パラメータを取得
	//画面表示時以外(searchCond指定時)は値は使わない
	String checkDefaultValue(HashMap<String, Object> defaultSearchCond, String searchCond, String key, String expect, String retStr) {
		String value = getDefaultValue(defaultSearchCond, searchCond, key);
		if (expect.equals(value)) return retStr;
		return "";
	}
	String getDefaultValue(HashMap<String, Object> defaultSearchCond, String searchCond, String key) {
		if (StringUtil.isNotBlank(searchCond)) return "";
		if (!defaultSearchCond.containsKey(key)) return "";
		Object value = defaultSearchCond.get(key);
		if (value instanceof Object[] && ((Object[]) value).length > 0) {
			value = ((Object[]) value)[0];
		}
		return value.toString();
	}
%>
<%
	//searchCondによる通常検索条件の復元はSearchResultSection側で行っている。
	//SearchConditionSectionはデフォルト検索条件に対してのみ意識している。

	//Commandで設定されるパラメータ
	SearchFormViewData data = (SearchFormViewData) request.getAttribute(Constants.DATA);
	String viewName = (String) request.getAttribute(Constants.VIEW_NAME);
	if (viewName == null) viewName = "";
	HashMap<String, Object> defaultSearchCond = (HashMap<String, Object>) request.getAttribute(Constants.DEFAULT_SEARCH_COND);

	SearchFormView view = data.getView();
	SearchConditionSection section = view.getCondSection();

	EntityDefinition ed = data.getEntityDefinition();
	String defName = ed.getName();
	List<EntityFilterItem> filters = data.getFilters();

	//選択タイプ(selectの場合のみ設定される。see common.js#searchReference)
	String selectType = request.getParameter(Constants.SELECT_TYPE);

	//指定条件復元用
	String searchCond = request.getParameter(Constants.SEARCH_COND);
	if (searchCond == null) searchCond = "";

	//特定バージョン指定
	String specVersion = request.getParameter(Constants.SEARCH_SPEC_VERSION);
	if (specVersion == null) specVersion = "";

	String pleaseSelectLabel = "";
	if (ViewUtil.isShowPulldownPleaseSelectLabel()) {
		pleaseSelectLabel = GemResourceBundleUtil.resourceString("generic.element.section.SearchConditionSection.pleaseSelect");
	}

	//指定検索タイプ復元用
	String searchType = getDefaultValue(defaultSearchCond, searchCond, Constants.SEARCH_TYPE);

	//詳細検索条件復元用(value)
	Map<String, String> detailCondValueMap = new HashMap<String, String>();
	for (String s : searchCond.split("&")) {
		if (s.startsWith(Constants.DETAIL_COND_VALUE)) {
	        String[] value = s.split("=");
	        if (value.length > 1) {
	            detailCondValueMap.put(value[0], value[1]);
	        } else {
	            detailCondValueMap.put(value[0], "");
	        }
	    }
	}

	//詳細検索条件復元用(propName)
	Map<String, String> detailCondPropMap = new HashMap<String, String>();
	for (String s : searchCond.split("&")) {
		if (s.startsWith(Constants.DETAIL_COND_PROP_NM)) {
			String[] value = s.split("=");
			if (value.length > 1) {
				detailCondPropMap.put(value[0], value[1]);
			}
		}
	}

	//検索対象Property情報
	Map<String, String> propTypeMap = new HashMap<String, String>();
	Map<String, PropertyDefinition> defMap = new HashMap<String, PropertyDefinition>();
	List<PropertyItem> properties = ViewUtil.filterPropertyItem(view.getCondSection().getElements());// JSPでラムダ使えない・・・
	for (PropertyItem pi : properties) {
		if (pi.isBlank()) continue;
		String propName = pi.getPropertyName();
		//PropertyDefinition pd = ed.getProperty(propName);
		PropertyDefinition pd = EntityViewUtil.getPropertyDefinition(propName, ed);

		defMap.put(propName, pd);

		if (pi.getEditor() instanceof ReferencePropertyEditor) {
			createReferencePropTypesMap((ReferencePropertyEditor) pi.getEditor(), propName, (ReferenceProperty) pd, propTypeMap);
		} else {
			String propType = pd.getType().toString();
			propTypeMap.put(propName, propType);
		}
	}

	//検索アクション
	String search = "";
	if (StringUtil.isNotBlank(view.getSearchWebapiName())) {
		search = view.getSearchWebapiName();
	} else {
		search = SearchCommand.WEBAPI_NAME;
	}

	//検索検証アクション
	String validate = "";
	if (StringUtil.isNotBlank(view.getSearchWebapiName())) {
		//検索アクションがカスタマイズされている場合はチェックしない
	} else {
		validate = SearchValidateCommand.WEBAPI_NAME;
	}

	//ダウンロードアクション
	String download = "";
	if (StringUtil.isNotBlank(view.getDownloadActionName())) {
		download = "/" + view.getDownloadActionName();
	} else {
		download = "/" + CsvDownloadCommand.ACTION_NAME;
	}

	//ごみ箱アクション
	String trash = TrashCommand.ACTION_NAME;
	String urlPath = ViewUtil.getParamMappingPath(ed.getName(), viewName);
	if (StringUtil.isNotBlank(urlPath)) {
		//ビュー名があればアクションの後につける
		trash = trash + urlPath;
	}

	//ごみ箱の表示
	boolean showTrash = (selectType == null
			&& AuthContext.getCurrentContext().checkPermission(new EntityPermission(
					data.getEntityDefinition().getName(), EntityPermission.Action.DELETE))
			&& !view.isHideTrash());

	//詳細検索表示
	boolean showDetail = !section.isHideDetailCondition();
	//定型検索表示
	boolean showFixed = !section.isHideFixedCondition() && filters.size() > 0;

	//検索結果件数表示
	boolean showCount = !view.getResultSection().isHideCount();

	//リセットボタン表示
	boolean showResetButton = ViewUtil.isShowSeachCondResetButton();

	//テーブル列数
	if (section.getColNum() == 0) section.setColNum(1);

	//カスタムスタイル用のSectionKEYをセット
	request.setAttribute(Constants.SECTION_SCRIPT_KEY, section.getScriptKey());

	//連打防止のインターバル
	int searchInterval = ViewUtil.getSearchInterval();
	int csvDownloadInterval = ViewUtil.getCsvDownloadInterval();
%>
<div class="tab-wrap">
<script type="text/javascript">
function validation(searchType) {
	<%-- common.js --%>
	return condTypeValidate(searchType);
}
function onclick_normalSearch(target) {
	doSearch('normal', 0, true, target, "button");
}
function onclick_detailSearch(target) {
	doSearch('detail', 0, true, target, "button");
}
function onclick_fixedSearch(target) {
	doSearch('fixed', 0, true, target, "button");
}
function sort(sortKey, sortType) {
	$(":hidden[name='sortKey']").val(sortKey);
	$(":hidden[name='sortType']").val(sortType);
	doSearch($(":hidden[name='searchType']").val(), $(":hidden[name='offset']").val(), false, "sort");
}
function doSearch(searchType, offset, resetSort, target, src) {
	if (!validation(searchType)) return;

	var interval = <%=searchInterval%>;
	var timeout = null;
	$(target).prop("disabled", true);
	if (interval > 0) {
		timeout = setTimeout(function() {
			$(target).prop("disabled", false);
		}, interval);
	}

	$(":hidden[name='offset']").val(offset);
	if (resetSort) {
		$(":hidden[name='sortKey']").val("");
		$(":hidden[name='sortType']").val("");
		var $sortable = $("#gview_searchResult tr.ui-jqgrid-labels th .ui-jqgrid-sortable");
		//フラット以外のソートマーククリア
		$sortable.removeClass('asc desc');
		//フラットのソートマーククリア
		$("span.s-ico", $sortable).hide();
	}

	$("div.result-block").show();
	$(".chagne-condition").hide();
	$(".searching").show();

	<%-- webapi.js --%>
	search("<%=StringUtil.escapeJavaScript(search)%>", searchType, searchType + 'Form', <%=showCount%>, function(list, count) {
		$(".searching").hide();
		setData(list, count);
		$(target).prop("disabled", false);
		if (timeout !== null) {
			clearTimeout(timeout);
		}

		$(".result-block").trigger("iplassAfterSearch", [src]);
	}, function() {
		$(".searching").hide();
		$(target).prop("disabled", false);
		if (timeout !== null) {
			clearTimeout(timeout);
		}
	});
}
<%
	if (showResetButton){
%>
function onclick_normalReset() {
	<%-- common.js --%>
	resetNormalCondition();
}
function onclick_detailReset() {
	<%-- common.js --%>
	resetDetailCondition();
}
<%
	}
%>
function onclick_normalCsvDownload(target) {
	doCsvDownload('normal', target);
}
function onclick_detailCsvDownload(target) {
	doCsvDownload('detail', target);
}
function onclick_fixedCsvDownload(target) {
	doCsvDownload('fixed', target);
}
function doCsvDownload(searchType, target) {
	if (!validation(searchType)) return;

	$(".chagne-condition").hide();
	$(".searching").show();

<%
	if (StringUtil.isNotEmpty(validate)) {
%>
	<%-- webapi.js --%>
	searchValidate("<%=StringUtil.escapeJavaScript(validate)%>", searchType, searchType + 'Form', function() {
		$(".searching").hide();
		<%-- common.js --%>
		csvDownload(searchType, searchType + "Form", "<%=StringUtil.escapeJavaScript(download)%>", $(target), <%=csvDownloadInterval%>);
	}, function() {
		$(".searching").hide();
	});
<%
	} else {
%>
	<%-- common.js --%>
	csvDownload(searchType, searchType + "Form", "<%=StringUtil.escapeJavaScript(download)%>", $(target), <%=csvDownloadInterval%>);
<%
	}
%>
}
function showTrash(event) {
	event.stopPropagation();

	<%-- common.js --%>
	submitForm(contextPath + '/<%=StringUtil.escapeJavaScript(trash)%>', {
		"defName":$(":hidden[name='defName']").val(),
		"searchCond":$(":hidden[name='searchCond']").val()
		});
}
function setSearchTab(searchType) {
	var $menu = $("#main-inner ul.tab-menu");
	var $panel = $("#main-inner .tab-panel");
	var $selMenu = $("." + searchType, $menu);
	$selMenu.siblings().removeClass('current');
	$selMenu.addClass('current');
	var clickIndex = $menu.children().index($selMenu);
	$panel.hide().eq(clickIndex).show();
	$(".tab-wrap").trigger("iplassAfterSetSearchTab");
	$(".fixHeight").fixHeight();
}

$(function() {
<%
	for(Map.Entry<String, String> e : detailCondValueMap.entrySet()) {
		String cnt = StringUtil.escapeJavaScript(e.getKey().split("_")[1]);
		String propName = detailCondPropMap.get(Constants.DETAIL_COND_PROP_NM + cnt);
		String propType = propTypeMap.get(propName);

		String condKey = StringUtil.escapeJavaScript(e.getKey());
		String condValue =StringUtil.escapeJavaScript(e.getValue());

		if ("DATE".equals(propType)) {
%>
	$("#<%=condKey%> :text").attr("onblur", "detailDateChange(this, '<%=cnt%>')");
	$("#<%=condKey%> :text").attr("value", convertToLocaleDateString("<%=condValue%>"));
	$("#<%=condKey%>").append("<input type='hidden' name='<%=condKey%>' value='' />");
<%
		} else if ("DATETIME".equals(propType)) {
			//condValueはサーバ送信用の日付フォーマットのはず
			//時間部分省略されてても、再フォーマット時間まで表示する
%>
	var datetime = dateUtil.newFormatString("<%=condValue%>", dateUtil.getServerDatetimeFormat(), dateUtil.getServerDatetimeFormat());
	$("#<%=condKey%> :text").attr("onblur", "detailDatetimeChange(this, '<%=cnt%>')");
	$("#<%=condKey%> :text").attr("value", datetime);
	$("#<%=condKey%>").append("<input type='hidden' name='<%=condKey%>' value='' />");
<%
		} else {
%>
	$("#<%=condKey%> :text").attr("name", "<%=condKey%>");
<%
		}
	}

	//初期タブ選択処理
	//searchCond != nullの場合(検索画面に戻ってきた場合)はSearchResultSectionでセットされる
	String _searchType = Constants.SEARCH_TYPE_NORMAL;
	if (searchCond.isEmpty()) {
		// xssは起こらないが、セレクタにタグが混ざるとjqueryでエラーになるため、特定文字のみ渡すようにする
		if (Constants.SEARCH_TYPE_DETAIL.equals(searchType) 
				|| Constants.SEARCH_TYPE_FIXED.equals(searchType)) {
			_searchType = searchType;
		}
%>
	setSearchTab("<%=_searchType%>");
<%
	}

	//入力チェック
	for (PropertyItem pi : properties) {
		if (pi.isBlank()) continue;
		String propName = pi.getPropertyName();
		PropertyDefinition pd = defMap.get(propName);
		if (!EntityViewUtil.isDisplayElement(defName, pi.getElementRuntimeId(), OutputType.SEARCHCONDITION, null)
				|| !isDispProperty(pd) || pi.getValidator() == null) {
			continue;
		}

		// 種類が増えるようなら分離も検討
		RequiresAtLeastOneFieldValidator validator = (RequiresAtLeastOneFieldValidator) pi.getValidator();
		String message = TemplateUtil.getMultilingualString(validator.getMessage(), validator.getLocalizedMessageList());
		StringBuilder normal = new StringBuilder();
		StringBuilder detail = new StringBuilder();
		getRequiresAtLeastOneFieldValidatorNestParam(pi.getEditor(), pi.getPropertyName(), pi.getPropertyName(), pi.getPropertyName(), normal, true);
		getRequiresAtLeastOneFieldValidatorNestParam(pi.getEditor(), pi.getPropertyName(), pi.getPropertyName(), pi.getPropertyName(), detail, false);
		for (PropertyValidationCondition condition : validator.getPropertyList()) {
			PropertyItem prop = getProperty(condition.getPropertyName(), view);
			if (prop == null) continue;
			getRequiresAtLeastOneFieldValidatorNestParam(prop.getEditor(), condition.getPropertyName(), condition.getPropertyName(), condition.getPropertyName(), normal, true);
			getRequiresAtLeastOneFieldValidatorNestParam(prop.getEditor(), condition.getPropertyName(), condition.getPropertyName(), condition.getPropertyName(), detail, false);
		}
		if (!pi.isHideNormalCondition() && validator.isValidateNormal()) {
			if (normal.length() > 1) {
				normal = normal.deleteCharAt(0);
			}
%>
	<%-- common.js --%>
	addNormalValidator(function() {
		var message = "<%=StringUtil.escapeJavaScript(message)%>";
		var properties = {"properties":[<%=normal.toString()%>]};
		<%-- common.js --%>
		return normalRequiresAtLeastOneFieldValidate(message, properties);
	});
<%
		}
		if (showDetail && !pi.isHideDetailCondition() && validator.isValidateDetail()) {
			if (detail.length() > 1) {
				detail = detail.deleteCharAt(0);
			}
%>
	<%-- common.js --%>
	addDetailValidator(function() {
		var message = "<%=StringUtil.escapeJavaScript(message)%>";
		var properties = {"properties":[<%=detail.toString()%>]};
		<%-- common.js --%>
		return detailRequiresAtLeastOneFieldValidate(message, properties);
	});
<%
		}
	}
%>
});
</script>

<div class="tabList tabList-search-01">
<ul class="tab-menu">
<li class="normal"><a href="#">${m:rs("mtp-gem-messages", "generic.element.section.SearchConditionSection.search")}</a></li>
<%
	if (showDetail) {
%>
<li class="detail"><a href="#">${m:rs("mtp-gem-messages", "generic.element.section.SearchConditionSection.advSearch")}</a></li>
<%
	}
	if (showFixed) {
%>
<li class="fixed"><a href="#">${m:rs("mtp-gem-messages", "generic.element.section.SearchConditionSection.RouSearch")}</a></li>
<%
	}
%>
</ul>
<%
	if (showTrash) {
%>
<p class="trash"><a href="javascript:void(0)" onclick="showTrash(event)">${m:rs("mtp-gem-messages", "generic.element.section.SearchConditionSection.dispTrash")}</a></p>
<%
	}
%>
</div><!--tabList-search-01-->

<div class="box-search-01">
<div class="data-search tab-panel" style="display: none;">
<form name="normalForm" method="POST">
<input type="hidden" name="viewName" value="<c:out value="<%=viewName%>"/>"/>
<input type="text" style="display: none;" />
<%-- 複数列に変更しているのはfunction.jsのmultiColumnTable --%>
<table class="tbl-search-01 multi-col" data-colNum=<%=section.getColNum() %>>
<tbody>
<%
	//通常検索で表示する項目の抽出(非表示の場合ブランク扱い)
	List<Element> elementList = new ArrayList<>();
	List<Element> hiddenList = new ArrayList<>();
	for (Element element : section.getElements()) {
		if (element instanceof PropertyItem) {
			PropertyItem property = (PropertyItem) element;
			if (property.isBlank()) {
				elementList.add(new BlankSpace());
			} else {
				PropertyDefinition pd = defMap.get(property.getPropertyName());
				if (EntityViewUtil.isDisplayElement(defName, property.getElementRuntimeId(), OutputType.SEARCHCONDITION, null)
						&& !property.isHideNormalCondition() && isDispProperty(pd)) {
					//hiddenはレイアウトを保持するためBlankSpaceに置き換えたうえで退避
					if (property.getEditor() != null && property.getEditor().isHide()) {
						elementList.add(new BlankSpace());
						hiddenList.add(property);
					} else {
						elementList.add(property);
					}
				} else {
					elementList.add(new BlankSpace());
				}
			}
		} else if (element instanceof VirtualPropertyItem) {
			VirtualPropertyItem property = (VirtualPropertyItem) element;
			if (EntityViewUtil.isDisplayElement(defName, property.getElementRuntimeId(), OutputType.SEARCHCONDITION, null)) {
				//hiddenはレイアウトを保持するためBlankSpaceに置き換えたうえで退避
				if (property.getEditor() != null && property.getEditor().isHide()) {
					elementList.add(new BlankSpace());
					hiddenList.add(property);
				} else {
					elementList.add(property);
				}
			} else {
				elementList.add(new BlankSpace());
			}
		} else {
			// BlankSpaceか仮想プロパティ
			elementList.add(element);
		}
	}

	for (Element element : elementList) {
%>
<tr class="col">
<%
		if (element instanceof PropertyItem) {
			PropertyItem property = (PropertyItem) element;
			PropertyDefinition pd = defMap.get(property.getPropertyName());
			if (EntityViewUtil.isDisplayElement(defName, property.getElementRuntimeId(), OutputType.SEARCHCONDITION, null)
					&& !property.isHideNormalCondition() && isDispProperty(pd)) {
				String style = property.getStyle() != null ? property.getStyle() : "";
				String displayLabel = TemplateUtil.getMultilingualString(property.getDisplayLabel(), property.getLocalizedDisplayLabelList(), pd.getDisplayName(), pd.getLocalizedDisplayNameList());
%>
<th id="id_th_<c:out value="<%=property.getPropertyName() %>"/>" class="<c:out value="<%=style %>"/>">
<%-- XSS対応-メタの設定のため対応なし(displayLabel) --%>
<%=displayLabel%>
<%
				if (property.isRequiredNormal()) {
%>
<span class="ico-required ml10 vm" style="text-shadow: none;">${m:rs("mtp-gem-messages", "generic.element.property.Property.required")}</span>
<%
				}
				String tooltip = TemplateUtil.getMultilingualString(property.getTooltip(), property.getLocalizedTooltipList());
				if (StringUtil.isNotBlank(tooltip)) {
%>
<%-- XSS対応-メタの設定のため対応なし(tooltip) --%>
<span class="ml05"><img src="${m:esc(skinImagePath)}/icon-help-01.png" alt="" class="vm tp"  title="<%=tooltip %>" /></span>
<%
				}
%>
</th>
<td id="id_td_<c:out value="<%=property.getPropertyName() %>"/>" class="<c:out value="<%=style %>"/>">
<%
				property.getEditor().setPropertyName(property.getPropertyName());
				String path = EntityViewUtil.getJspPath(property.getEditor(), ViewConst.DESIGN_TYPE_GEM);
				if (path != null) {
					request.setAttribute(Constants.EDITOR_STYLE, style);//nest項目があった場合のクラスのプレフィックスに
					request.setAttribute(Constants.EDITOR_DISPLAY_LABEL, displayLabel);
					request.setAttribute(Constants.EDITOR_REQUIRED, property.isRequiredNormal());
					request.setAttribute(Constants.EDITOR_EDITOR, property.getEditor());
					request.setAttribute(Constants.EDITOR_PROPERTY_DEFINITION, pd);
					Object defaultValue = defaultSearchCond.get(property.getPropertyName());
					if (StringUtil.isEmpty(searchCond)) {
						//指定検索条件がない場合はデフォルトから指定
						request.setAttribute(Constants.EDITOR_PROP_VALUE, defaultValue);
					} else {
						//指定検索条件がある場合は、Editor側ではセットせずSearchResultSection側で設定
					}
					request.setAttribute(Constants.EDITOR_DEFAULT_VALUE, defaultValue);
					request.setAttribute(Constants.AUTOCOMPLETION_SETTING, property.getAutocompletionSetting());
%>
<jsp:include page="<%=path%>" />
<%
					request.removeAttribute(Constants.EDITOR_STYLE);
					request.removeAttribute(Constants.EDITOR_DISPLAY_LABEL);
					request.removeAttribute(Constants.EDITOR_REQUIRED);
					request.removeAttribute(Constants.EDITOR_EDITOR);
					request.removeAttribute(Constants.EDITOR_PROPERTY_DEFINITION);
					request.removeAttribute(Constants.EDITOR_PROP_VALUE);
					request.removeAttribute(Constants.EDITOR_DEFAULT_VALUE);
				}
				if (property.getAutocompletionSetting() != null) {
					request.setAttribute(Constants.AUTOCOMPLETION_DEF_NAME, ed.getName());
					request.setAttribute(Constants.AUTOCOMPLETION_VIEW_NAME, viewName);
					request.setAttribute(Constants.AUTOCOMPLETION_PROP_NAME, property.getPropertyName());
					request.setAttribute(Constants.AUTOCOMPLETION_MULTIPLICTTY, 1);
					String autocompletionPath = "/jsp/gem/generic/common/SearchConditionAutocompletion.jsp";
%>
<jsp:include page="<%=autocompletionPath %>"/>
<%
					request.removeAttribute(Constants.AUTOCOMPLETION_SETTING);
					request.removeAttribute(Constants.AUTOCOMPLETION_DEF_NAME);
					request.removeAttribute(Constants.AUTOCOMPLETION_VIEW_NAME);
					request.removeAttribute(Constants.AUTOCOMPLETION_PROP_NAME);
					request.removeAttribute(Constants.AUTOCOMPLETION_MULTIPLICTTY);
					request.removeAttribute(Constants.AUTOCOMPLETION_SCRIPT_PATH);
				}
%>
</td>
<%
			}
		} else if (element instanceof VirtualPropertyItem) {
			VirtualPropertyItem property = (VirtualPropertyItem) element;
			if (EntityViewUtil.isDisplayElement(defName, property.getElementRuntimeId(), OutputType.SEARCHCONDITION, null)) {
				PropertyDefinition pd = EntityViewUtil.getPropertyDefinition(property);
				String style = property.getStyle() != null ? property.getStyle() : "";
				String displayLabel = TemplateUtil.getMultilingualString(property.getDisplayLabel(), property.getLocalizedDisplayLabelList());
%>
<th id="id_th_<c:out value="<%=property.getPropertyName() %>"/>" class="<c:out value="<%=style %>"/>">
<%-- XSS対応-メタの設定のため対応なし(displayLabel) --%>
<%=displayLabel%>
<%
				String tooltip = TemplateUtil.getMultilingualString(property.getTooltip(), property.getLocalizedTooltipList());
				if (StringUtil.isNotBlank(tooltip)) {
%>
<%-- XSS対応-メタの設定のため対応なし(tooltip) --%>
<span class="ml05"><img src="${m:esc(skinImagePath)}/icon-help-01.png" alt="" class="vm tp" title="<%=tooltip %>" /></span>
<%
				}
%>
</th>
<td id="id_td_<c:out value="<%=property.getPropertyName() %>"/>" class="<c:out value="<%=style %>"/>">
<%
				property.getEditor().setPropertyName(property.getPropertyName());
				String path = EntityViewUtil.getJspPath(property.getEditor(), ViewConst.DESIGN_TYPE_GEM);
				if (path != null) {
					request.setAttribute(Constants.EDITOR_STYLE, style);
					request.setAttribute(Constants.EDITOR_DISPLAY_LABEL, displayLabel);
					request.setAttribute(Constants.EDITOR_REQUIRED, false);
					request.setAttribute(Constants.EDITOR_EDITOR, property.getEditor());
					request.setAttribute(Constants.EDITOR_PROPERTY_DEFINITION, pd);
					Object defaultValue = defaultSearchCond.get(property.getPropertyName());
					if (StringUtil.isEmpty(searchCond)) {
						//指定検索条件がない場合はデフォルトから指定
						request.setAttribute(Constants.EDITOR_PROP_VALUE, defaultValue);
					} else {
						//指定検索条件がある場合は、Editor側ではセットせずSearchResultSection側で設定
					}
					request.setAttribute(Constants.EDITOR_DEFAULT_VALUE, defaultValue);
%>
<jsp:include page="<%=path%>" />
<%
					request.removeAttribute(Constants.EDITOR_STYLE);
					request.removeAttribute(Constants.EDITOR_DISPLAY_LABEL);
					request.removeAttribute(Constants.EDITOR_REQUIRED);
					request.removeAttribute(Constants.EDITOR_EDITOR);
					request.removeAttribute(Constants.EDITOR_PROPERTY_DEFINITION);
					request.removeAttribute(Constants.EDITOR_PROP_VALUE);
					request.removeAttribute(Constants.EDITOR_DEFAULT_VALUE);
				}
%>
</td>
<%
			}
		} else {
%>
<th></th>
<td></td>
<%
		}
%>
</tr>
<%
	}
	if (selectType == null && ed.getVersionControlType() != VersionControlType.NONE) {
		String allVersion = getSearchCondParam(searchCond, "allVersion");
		String checked = "";
		if ("1".equals(allVersion)) checked = "checked";
%>
<tr class="version-area col<%=section.getColNum()%>">
<th>&nbsp;</th>
<td>
<label for="cb-version"><input type="checkbox" value="1" name="allVersion" id="cb-version" <c:out value="<%=checked %>"/>/>${m:rs("mtp-gem-messages", "generic.element.section.SearchConditionSection.allVerSearch")}</label>
</td>
</tr>
<%
	}
%>
<tr class="submit-area col<%=section.getColNum()%>">
<th>&nbsp;</th>
<td>
<input type="hidden" name="specVersion" value="<c:out value="<%=specVersion%>"/>"/>
<p>
<input type="button"  class="btn-search-01 gr-btn" value="${m:rs('mtp-gem-messages', 'generic.element.section.SearchConditionSection.search')}" onclick="onclick_normalSearch(this)"/>
<%
	if (!section.isHideCsvdownload()) {
		//編集等でデータを選択する場合は、CSVダウンロードボタン非表示
		if (section.isHideCsvdownloadDialog()) {
%>
<input type="button" class="btn-csv-01 gr-btn gr-size-02" value="${m:rs('mtp-gem-messages', 'generic.element.section.SearchConditionSection.dwnCsv')}" onclick="onclick_normalCsvDownload(this)"/>
<%
		} else {
%>
<input type="button" class="btn-csv-01 gr-btn gr-size-02" value="${m:rs('mtp-gem-messages', 'generic.element.section.SearchConditionSection.dwnCsv')}" id="csv-download" />
<%
		}
	}

	if (showResetButton){
%>
<span style="margin-right:30px;">&nbsp;</span>
<input type="button"  class="btn-reset-01 gr-btn-02" value="${m:rs('mtp-gem-messages', 'generic.element.section.SearchConditionSection.reset')}" onclick="onclick_normalReset()"/>
<%
	}
%>
</p>
</td>
</tr>
</tbody>
</table>
<div class="hidden-cond-area">
<%
	//hidden出力
	for (Element hiddenElement : hiddenList) {
		String propertyName = null;
		PropertyDefinition pd = null;
		PropertyEditor editor = null;
		AutocompletionSetting autocompletionSetting = null;
		
		//PropertyItemかVirtualPropertyItemしか存在しない(共通のIFがないためElementで保持)
		if (hiddenElement instanceof PropertyItem) {
			PropertyItem property = (PropertyItem)hiddenElement;
			propertyName = property.getPropertyName();
			pd = defMap.get(propertyName);
			editor = property.getEditor();
			editor.setPropertyName(propertyName);
			autocompletionSetting = property.getAutocompletionSetting();
		} else if (hiddenElement instanceof VirtualPropertyItem) {
			VirtualPropertyItem property = (VirtualPropertyItem)hiddenElement;
			propertyName = property.getPropertyName();
			pd = EntityViewUtil.getPropertyDefinition(property);
			editor = property.getEditor();
			editor.setPropertyName(propertyName);
		}
		String path = EntityViewUtil.getJspPath(editor, ViewConst.DESIGN_TYPE_GEM);
		if (path != null) {
//			request.setAttribute(Constants.EDITOR_STYLE, style);//nest項目があった場合のクラスのプレフィックスに
//			request.setAttribute(Constants.EDITOR_DISPLAY_LABEL, displayLabel);
//			request.setAttribute(Constants.EDITOR_REQUIRED, property.isRequiredNormal());
			request.setAttribute(Constants.EDITOR_EDITOR, editor);
			request.setAttribute(Constants.EDITOR_PROPERTY_DEFINITION, pd);
			Object defaultValue = defaultSearchCond.get(propertyName);
			if (StringUtil.isEmpty(searchCond)) {
				//指定検索条件がない場合はデフォルトから指定
				request.setAttribute(Constants.EDITOR_PROP_VALUE, defaultValue);
			} else {
				//指定検索条件がある場合は、Editor側ではセットせずSearchResultSection側で設定
			}
			request.setAttribute(Constants.EDITOR_DEFAULT_VALUE, defaultValue);
			request.setAttribute(Constants.AUTOCOMPLETION_SETTING, autocompletionSetting);
%>
<jsp:include page="<%=path%>" />
<%
//			request.removeAttribute(Constants.EDITOR_STYLE);
//			request.removeAttribute(Constants.EDITOR_DISPLAY_LABEL);
//			request.removeAttribute(Constants.EDITOR_REQUIRED);
			request.removeAttribute(Constants.EDITOR_EDITOR);
			request.removeAttribute(Constants.EDITOR_PROPERTY_DEFINITION);
			request.removeAttribute(Constants.EDITOR_PROP_VALUE);
			request.removeAttribute(Constants.EDITOR_DEFAULT_VALUE);
		}
	}
%>
</div>
</form>
</div><!--data-search-->
<%
	//詳細検索
	if (showDetail) {
		int dtlCndCount = section.getConditionDispCount();
		String tmp = null;
		if (StringUtil.isNotBlank(searchCond)) {
			tmp = getSearchCondParam(searchCond, Constants.DETAIL_COND_COUNT);
		} else if (defaultSearchCond.containsKey(Constants.DETAIL_COND_COUNT)) {
			tmp = getDefaultValue(defaultSearchCond, searchCond, Constants.DETAIL_COND_COUNT);
		}
		if (StringUtil.isNotBlank(tmp)) {
			try {
				dtlCndCount = Integer.parseInt(tmp);
			} catch (NumberFormatException e) {
			}
		}
		String searchCondSearchType = getSearchCondParam(searchCond, "searchType");

		StringBuilder propTypes = new StringBuilder();
		boolean isFirst = true;
		for(Map.Entry<String, String> e : propTypeMap.entrySet()) {

		    if (isFirst) {
				propTypes.append("\"").append(e.getKey()).append("\":\"").append(e.getValue()).append("\"");
				isFirst = false;
			} else {
				propTypes.append(",\"").append(e.getKey()).append("\":\"").append(e.getValue()).append("\"");
			}
		}
%>

<script type="text/javascript">
function propChange(obj, cnt) {
	var propTypeMap = {<%=propTypes.toString()%>};
	var propName = obj.options[obj.selectedIndex].value;
	var propType = propTypeMap[propName];

	var condKey = "<%=Constants.DETAIL_COND_VALUE%>" + cnt;

	$("#" + condKey + " :text").removeAttr("onblur");
	$("#" + condKey + " [type='hidden']").remove();

	if (propType == "DATE") {
		$("#" + condKey + " :text").removeAttr("name");
		$("#" + condKey + " :text").attr("onblur", "detailDateChange(this, " + cnt + ")");

		var dateValue = convertFromLocaleDateString($("#" + condKey + " :text").val(), dateUtil.getServerDateFormat());
		$("#" + condKey).append("<input type=\"hidden\" name=\"" + condKey + "\" value=\"" + dateValue + "\" />");
	} else if (propType == "DATETIME") {
		$("#" + condKey + " :text").removeAttr("name");
		$("#" + condKey + " :text").attr("onblur", "detailDatetimeChange(this, " + cnt + ")");

		var datetimeValue = convertFromLocaleDatetimeString($("#" + condKey + " :text").val(), dateUtil.getServerDatetimeFormat());
		$("#" + condKey).append("<input type=\"hidden\" name=\"" + condKey + "\" value=\"" + datetimeValue + "\" />");
	} else {
		$("#" + condKey + " :text").attr("name", condKey);
	}
}

function detailDateChange(obj, cnt) {
	var condKey = "<%=Constants.DETAIL_COND_VALUE%>" + cnt;
	var value = obj.value;
	var yyyymmdd = convertFromLocaleDateString(value, dateUtil.getServerDateFormat());
	$("#" + condKey + " :hidden").attr("value", yyyymmdd);
}

function detailDatetimeChange(obj, cnt) {
	var condKey = "<%=Constants.DETAIL_COND_VALUE%>" + cnt;
	var value = obj.value;
	var ms = dateUtil.newFormatString(value, dateUtil.getServerDatetimeFormat(), "SSS");
	var datetime = convertFromLocaleDatetimeString(value, dateUtil.getServerDatetimeFormat());
	$("#" + condKey + " :hidden").attr("value", datetime);
}

$(function() {
	var $div = $(".data-deep-search");
	if ($("option.required", $div).length > 0) {
		var propList = new Array();
		$("option.required", $div).each(function() {
			if ($.inArray($(this).val(), propList) == -1) {
				propList.push($(this).val());
			}
		});

		<%-- common.js --%>
		addDetailValidator(function() {
			var msg = scriptContext.gem.locale.common.requiredMsg;
			for (var i = 0; i < propList.length; i++) {
				var propName = es(propList[i]);
				var displayLabel = $("option[value='" + propName + "']:first", $div).text();
				var $opt = $("option:selected[value='" + propName + "']", $div);
				if ($opt.length > 0) {
					var ret = false;
					$opt.each(function() {
						var val = $(this).parents("td").next().next().children(":text").val();
						if (typeof val !== "undeined" && val != null && val != "") {
							ret = true;
							return false;
						}
					});
					if (!ret) alert(msg.replace("{0}", displayLabel));
					return ret;
				} else {
					alert(msg.replace("{0}", displayLabel));
					return false;
				}
			}
		});
	}
});
</script>

<div class="data-deep-search tab-panel" style="display: none;">
<form name="detailForm" method="POST">
<input type="hidden" name="viewName" value="<c:out value="<%=viewName%>"/>"/>
<table class="tbl-search-01">
<thead>
<tr>
<th class="col1"></th>
<th class="col2">${m:rs("mtp-gem-messages", "generic.element.section.SearchConditionSection.searchItem")}</th>
<th class="col3">${m:rs("mtp-gem-messages", "generic.element.section.SearchConditionSection.cond")}</th>
<th class="col4">${m:rs("mtp-gem-messages", "generic.element.section.SearchConditionSection.keyword")}</th>
<th class="col5 pl15"><p class="btn-toggle-01 tp02 add" title="${m:rs('mtp-gem-messages', 'generic.element.section.SearchConditionSection.addItemBottom')}">＋</p></th>
</tr>
</thead>
<tbody>

<%
		//検索条件で初期設定された場合、値のnameを設定する必要があるので、後でchangeイベントを発生させる
		List<String> changeTargetList = new ArrayList<String>();

		for (int i = 0; i < dtlCndCount; i++) {
			//検索画面に戻ってきた場合で、番号が飛んでいるものは除外
			if (Constants.SEARCH_TYPE_DETAIL.equals(searchCondSearchType) && !checkDtlCndData(searchCond, i)) continue;
			String condPropName = Constants.DETAIL_COND_PROP_NM + i;
			String rowId = "detailCond_" + i;
%>
<tr id="<c:out value="<%=rowId%>"/>">
<td><c:out value="<%=i+1%>"/>.</td>
<td>
<select class="form-size inpbr propList" name="<c:out value="<%=condPropName%>"/>" onchange="propChange(this, '<%=i%>')">
<option value="" selected="selected"><%= pleaseSelectLabel %></option>
<%
			for (PropertyItem pi : properties) {
				if (pi.isBlank()) continue;

				String propName = pi.getPropertyName();
//				PropertyDefinition pd = ed.getProperty(propName);
				PropertyDefinition pd = defMap.get(propName);
				String displayLabel = TemplateUtil.getMultilingualString(pi.getDisplayLabel(), pi.getLocalizedDisplayLabelList(), pd.getDisplayName(), pd.getLocalizedDisplayNameList());
				if (!EntityViewUtil.isDisplayElement(defName, pi.getElementRuntimeId(), OutputType.SEARCHCONDITION, null)
						|| pi.isHideDetailCondition() || !isDispProperty(pd)) continue;
				if (pi.getEditor() instanceof ReferencePropertyEditor) {
					ReferencePropertyEditor editor = (ReferencePropertyEditor) pi.getEditor();

					//ネストがいれば参照先のプロパティを表示
					request.setAttribute(Constants.PROPERTY_ITEM, pi);
					request.setAttribute(Constants.EDITOR_REF_NEST_PROPERTY, pd);
					request.setAttribute(Constants.EDITOR_REF_NEST_EDITOR, editor);
					request.setAttribute(Constants.EDITOR_REF_NEST_PROP_NAME, propName);
					request.setAttribute(Constants.COND_PROP_NAME, condPropName);
%>
<jsp:include page="SearchConditionSection_Nest.jsp" />
<%
					request.removeAttribute(Constants.PROPERTY_ITEM);
					request.removeAttribute(Constants.EDITOR_REF_NEST_PROPERTY);
					request.removeAttribute(Constants.EDITOR_REF_NEST_EDITOR);
					request.removeAttribute(Constants.EDITOR_REF_NEST_PROP_NAME);
					request.removeAttribute(Constants.COND_PROP_NAME);
				} else {
					String optClass = pi.isRequiredDetail() ? "required" : "";
					String selected = checkDefaultValue(defaultSearchCond, searchCond, condPropName, propName, "selected");
					if (StringUtil.isNotBlank(selected) && !changeTargetList.contains(rowId)) {
						changeTargetList.add(rowId);
					}
%>
<option value="<c:out value="<%=propName%>"/>" class="<c:out value="<%=optClass%>" />" <c:out value="<%=selected%>" />><c:out value="<%=displayLabel%>" /></option>
<%
				}
			}
%>
</select>
</td>
<td>
<%
			String prdcName = Constants.DETAIL_COND_PREDICATE + i;
%>
<select class="form-size inpbr" name="<c:out value="<%=prdcName %>"/>">
<option value="<c:out value="<%=Constants.EQUALS %>"/>" <c:out value="<%=checkDefaultValue(defaultSearchCond, searchCond, prdcName, Constants.EQUALS, \"selected\")%>" />>${m:rs("mtp-gem-messages", "generic.element.section.SearchConditionSection.equal")}</option>
<option value="<c:out value="<%=Constants.NOTEQUALS %>"/>" <c:out value="<%=checkDefaultValue(defaultSearchCond, searchCond, prdcName, Constants.NOTEQUALS, \"selected\")%>" />>${m:rs("mtp-gem-messages", "generic.element.section.SearchConditionSection.notEq")}</option>
<option value="<c:out value="<%=Constants.FRONTMATCH %>"/>" <c:out value="<%=checkDefaultValue(defaultSearchCond, searchCond, prdcName, Constants.FRONTMATCH, \"selected\")%>" />>${m:rs("mtp-gem-messages", "generic.element.section.SearchConditionSection.forwardMatch")}</option>
<option value="<c:out value="<%=Constants.BACKWARDMATCH %>"/>" <c:out value="<%=checkDefaultValue(defaultSearchCond, searchCond, prdcName, Constants.BACKWARDMATCH, \"selected\")%>" />>${m:rs("mtp-gem-messages", "generic.element.section.SearchConditionSection.backMatch")}</option>
<option value="<c:out value="<%=Constants.INCLUDE %>"/>" <c:out value="<%=checkDefaultValue(defaultSearchCond, searchCond, prdcName, Constants.INCLUDE, \"selected\")%>" />>${m:rs("mtp-gem-messages", "generic.element.section.SearchConditionSection.include")}</option>
<option value="<c:out value="<%=Constants.NOTINCLUDE %>"/>" <c:out value="<%=checkDefaultValue(defaultSearchCond, searchCond, prdcName, Constants.NOTINCLUDE, \"selected\")%>" />>${m:rs("mtp-gem-messages", "generic.element.section.SearchConditionSection.notinclude")}</option>
<option value="<c:out value="<%=Constants.IN %>"/>" <c:out value="<%=checkDefaultValue(defaultSearchCond, searchCond, prdcName, Constants.IN, \"selected\")%>" />>${m:rs("mtp-gem-messages", "generic.element.section.SearchConditionSection.in")}</option>
<option value="<c:out value="<%=Constants.LESSER %>"/>" <c:out value="<%=checkDefaultValue(defaultSearchCond, searchCond, prdcName, Constants.LESSER, \"selected\")%>" />>${m:rs("mtp-gem-messages", "generic.element.section.SearchConditionSection.smaller")}</option>
<option value="<c:out value="<%=Constants.GREATER %>"/>" <c:out value="<%=checkDefaultValue(defaultSearchCond, searchCond, prdcName, Constants.GREATER, \"selected\")%>" />>${m:rs("mtp-gem-messages", "generic.element.section.SearchConditionSection.larger")}</option>
<option value="<c:out value="<%=Constants.LESSEREQUALS %>"/>" <c:out value="<%=checkDefaultValue(defaultSearchCond, searchCond, prdcName, Constants.LESSEREQUALS, \"selected\")%>" />>${m:rs("mtp-gem-messages", "generic.element.section.SearchConditionSection.below")}</option>
<option value="<c:out value="<%=Constants.GREATEREQUALS %>"/>" <c:out value="<%=checkDefaultValue(defaultSearchCond, searchCond, prdcName, Constants.GREATEREQUALS, \"selected\")%>" />>${m:rs("mtp-gem-messages", "generic.element.section.SearchConditionSection.orMore")}</option>
<option value="<c:out value="<%=Constants.NOTNULL %>"/>" <c:out value="<%=checkDefaultValue(defaultSearchCond, searchCond, prdcName, Constants.NOTNULL, \"selected\")%>" />>${m:rs("mtp-gem-messages", "generic.element.section.SearchConditionSection.notNull")}</option>
<option value="<c:out value="<%=Constants.NULL %>"/>" <c:out value="<%=checkDefaultValue(defaultSearchCond, searchCond, prdcName, Constants.NULL, \"selected\")%>" />>${m:rs("mtp-gem-messages", "generic.element.section.SearchConditionSection.null")}</option>
</select>
</td>
<%
			String vlName = Constants.DETAIL_COND_VALUE + i;
			String vlValue = getDefaultValue(defaultSearchCond, searchCond, vlName);
%>
<td id="<c:out value="<%=vlName%>"/>"><input type="text" class="form-size inpbr" value="<c:out value="<%=vlValue%>"/>"/></td>
<td class="pl15"><p class="btn-toggle-01 delete">－</p></td>
</tr>
<%
		}
%>
</tbody>
<tfoot>
<tr class="submit-area submit-criteria">
<td>&nbsp;</td>
<td colspan="4">
<ul class="list-radio-01">
<%
			String andChecked = defaultSearchCond.containsKey(Constants.DETAIL_COND_EXPR) ? checkDefaultValue(defaultSearchCond, searchCond, Constants.DETAIL_COND_EXPR, Constants.AND, "checked") : " checked";
			String orChecked = checkDefaultValue(defaultSearchCond, searchCond, Constants.DETAIL_COND_EXPR, Constants.OR, "checked");
			String notChecked = checkDefaultValue(defaultSearchCond, searchCond, Constants.DETAIL_COND_EXPR, Constants.NOT, "checked");
			String expChecked = checkDefaultValue(defaultSearchCond, searchCond, Constants.DETAIL_COND_EXPR, Constants.EXPRESSION, "checked");
%>
<li><label for="ra-all"><input type="radio" name="<c:out value="<%=Constants.DETAIL_COND_EXPR %>"/>" id="ra-all" value="<c:out value="<%=Constants.AND %>"/>" <c:out value="<%=andChecked%>"/> />${m:rs("mtp-gem-messages", "generic.element.section.SearchConditionSection.matchAll")}</label></li>
<li><label for="ra-or"><input type="radio" name="<c:out value="<%=Constants.DETAIL_COND_EXPR %>"/>" id="ra-or" value="<c:out value="<%=Constants.OR %>"/>" <c:out value="<%=orChecked%>"/> />${m:rs("mtp-gem-messages", "generic.element.section.SearchConditionSection.matchOne")}</label></li>
<li><label for="ra-not"><input type="radio" name="<c:out value="<%=Constants.DETAIL_COND_EXPR %>"/>" id="ra-not" value="<c:out value="<%=Constants.NOT %>"/>" <c:out value="<%=notChecked%>"/> />${m:rs("mtp-gem-messages", "generic.element.section.SearchConditionSection.notMatchAll")}</label></li>
</ul>
<ul class="list-radio-01">
<li><label for="ra-ve"><input type="radio" name="<c:out value="<%=Constants.DETAIL_COND_EXPR %>"/>" id="ra-ve" value="<c:out value="<%=Constants.EXPRESSION %>"/>" <c:out value="<%=expChecked%>"/> />${m:rs("mtp-gem-messages", "generic.element.section.SearchConditionSection.writeCond")}</label></li>
<li>
<%
			String expression = getDefaultValue(defaultSearchCond, searchCond, Constants.DETAIL_COND_FILTER_EXPRESSION);
%>
<input type="text" name="<%=Constants.DETAIL_COND_FILTER_EXPRESSION %>" class="form-size-01 mr05 inpbr" value="<c:out value="<%=expression%>"/>" />
<img src="${m:esc(skinImagePath)}/icon-help-01.png" class="vm tp" alt="" title="${m:rs('mtp-gem-messages', 'generic.element.section.SearchConditionSection.filterExpressionDescription')}" />
</li>
</ul>
<%
		if (selectType == null && ed.getVersionControlType() != VersionControlType.NONE) {
			String allVersion = getSearchCondParam(searchCond, "allVersionDtl");
			String checked = "";
			if ("1".equals(allVersion)) checked = "checked";
%>
<p><label for="cb-version-02"><input type="checkbox" value="1" name="allVersionDtl" id="cb-version-02" <c:out value="<%=checked %>"/>/>${m:rs("mtp-gem-messages", "generic.element.section.SearchConditionSection.allVerSearch")}</label></p>
<input type="hidden" name="specVersion" value="<c:out value="<%=specVersion%>"/>"/>
<%
		}
%>
</td>
</tr>
<tr class="submit-area">
<th>&nbsp;</th>
<td colspan="4"><p>
<input type="hidden" name="dtlCndCount" value="<%=dtlCndCount%>" id="id_detailConditionCount" />
<input type="button" class="btn-search-01 gr-btn" value="${m:rs('mtp-gem-messages', 'generic.element.section.SearchConditionSection.search')}" onclick="onclick_detailSearch(this)" />
<%
		if (!section.isHideCsvdownload()) {
			//編集等でデータを選択する場合は、CSVダウンロードボタン非表示
			if (section.isHideCsvdownloadDialog()) {
%>
<input type="button" class="btn-csv-01 gr-btn gr-size-02" value="${m:rs('mtp-gem-messages', 'generic.element.section.SearchConditionSection.dwnCsv')}" onclick="onclick_detailCsvDownload(this)"/>
<%
			} else {
%>
<input type="button" class="btn-csv-01 gr-btn gr-size-02" value="${m:rs('mtp-gem-messages', 'generic.element.section.SearchConditionSection.dwnCsv')}" id="detail-csv-download" />
<%
			}
		}

		if (showResetButton){
%>
<span style="margin-right:30px;">&nbsp;</span>
<input type="button" class="btn-reset-01 gr-btn-02" value="${m:rs('mtp-gem-messages', 'generic.element.section.SearchConditionSection.reset')}" onclick="onclick_detailReset()" />
<%
		}
%>
</p>
</td>
</tr>
</tfoot>
</table>
</form>
<%
		if (!changeTargetList.isEmpty()) {
			// イベント発生させてテキストにnameを設定する
%>
<script type="text/javascript">
$(function() {
<%
			for (String rowId : changeTargetList) {
%>
	$("#<%=StringUtil.escapeJavaScript(rowId)%> .propList").change();
<%
			}
%>
});
</script>
<%
		}
%>
</div><!--data-deep-search-->
<%
	}

	//定型検索
	if (showFixed) {
		List<String> showFilters = new ArrayList<String>();
		for (FilterSetting setting : section.getFilterSetting()) {
			showFilters.add(setting.getFilterName());
		}
%>
<div class="data-fixed-search tab-panel" style="display: none;">
<form name="fixedForm" method="POST">
<input type="hidden" name="viewName" value="<c:out value="<%=viewName%>"/>"/>
<table class="tbl-search-01">
<tbody>
<tr>
<td>
<ul class="list-radio-01">
<%
		for (EntityFilterItem filter : filters) {
			if (showFilters.isEmpty() || showFilters.contains(filter.getName())) {
				String displayName = TemplateUtil.getMultilingualString(filter.getDisplayName(), filter.getLocalizedDisplayNameList());
				String checked = checkDefaultValue(defaultSearchCond, searchCond, Constants.FILTER_NAME, filter.getName(), "checked");
%>
<li>
<label for="filter_<c:out value="<%=filter.getName()%>"/>">
<input type="radio" name="filterName" value="<c:out value="<%=filter.getName()%>"/>" id="filter_<c:out value="<%=filter.getName()%>"/>" <c:out value="<%=checked%>" /> />
<c:out value="<%=displayName%>"/>
</label>
</li>
<%
			}
		}
%>
</ul>
</td>
</tr>

<tr class="submit-area">
<td><p>
<input type="button"  class="btn-search-01 gr-btn" value="${m:rs('mtp-gem-messages', 'generic.element.section.SearchConditionSection.search')}" onclick="onclick_fixedSearch(this)" />
<%
		if (!section.isHideCsvdownload()) {
			//編集等でデータを選択する場合は、CSVダウンロードボタン非表示
			if (section.isHideCsvdownloadDialog()) {
%>
<input type="button" class="btn-csv-01 gr-btn gr-size-02" value="${m:rs('mtp-gem-messages', 'generic.element.section.SearchConditionSection.dwnCsv')}" onclick="onclick_fixedCsvDownload(this)"/>
<%
			} else {
%>
<input type="button" class="btn-csv-01 gr-btn gr-size-02" value="${m:rs('mtp-gem-messages', 'generic.element.section.SearchConditionSection.dwnCsv')}" id="fixed-csv-download" />
<%
			}
		}
%>
</p>
</td>
</tr>
</tbody>
</table>
</form>
</div><!--data-fixed-search-->
<%
	}

	if (!section.isHideCsvdownload()) {
		if (!section.isHideCsvdownloadDialog()) {
%>
<%-- CsvDownloadダイアログ --%>
<jsp:include page="SearchConditionSection_CsvDownloadDialog.jsp" />

<script type="text/javascript">
function applyCsvDownloadDialog(searchType, buttonId) {
	<%-- SearchConditionSection_CsvDownloadDialog.jsp --%>
	showCsvDownloadDialog(searchType, buttonId, "<%=StringUtil.escapeJavaScript(validate)%>", function(searchType, forUpload, characterCode, noDispName, outputResult, downloadCodeValue){
		csvDownload(searchType, searchType + "Form", "<%=StringUtil.escapeJavaScript(download)%>", this, <%=csvDownloadInterval%>, forUpload, characterCode, noDispName, outputResult, downloadCodeValue);
	});
}

$(function() {
	$("#dialog:ui-dialog").dialog("destroy");

	applyCsvDownloadDialog("normal","csv-download");
<%
			if (showDetail) {
%>
	applyCsvDownloadDialog("detail", "detail-csv-download");
<%
			}
			if (showFixed) {
%>
	applyCsvDownloadDialog("fixed", "fixed-csv-download");
<%
			}
%>
});
</script>
<%
		}
	}
%>

<input type="hidden" name="defName" value="<c:out value="<%=ed.getName()%>"/>">
<input type="hidden" name="searchType" value="<c:out value="<%=_searchType%>"/>">
<input type="hidden" name="formName" value="<c:out value="<%=_searchType%>"/>Form">
<input type="hidden" name="sortKey" value="">
<input type="hidden" name="sortType" value="">
<input type="hidden" name="isForUpload" value="">
</div><!--box-search-01-->
</div><!--tab-wrap-->
