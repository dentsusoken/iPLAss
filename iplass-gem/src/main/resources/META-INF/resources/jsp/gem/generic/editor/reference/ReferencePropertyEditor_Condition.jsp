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

<%@page import="org.iplass.gem.command.generic.reftree.SearchTreeDataCommand"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" uri="http://iplass.org/tags/mtp"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.Collections"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.HashMap"%>
<%@ page import="org.iplass.mtp.entity.definition.*" %>
<%@ page import="org.iplass.mtp.entity.definition.properties.*"%>
<%@ page import="org.iplass.mtp.entity.query.condition.expr.And"%>
<%@ page import="org.iplass.mtp.entity.query.condition.predicate.Equals"%>
<%@ page import="org.iplass.mtp.entity.query.condition.Condition"%>
<%@ page import="org.iplass.mtp.entity.query.PreparedQuery"%>
<%@ page import="org.iplass.mtp.entity.query.Query" %>
<%@ page import="org.iplass.mtp.entity.query.SortSpec"%>
<%@ page import="org.iplass.mtp.entity.query.SortSpec.SortType"%>
<%@ page import="org.iplass.mtp.entity.Entity" %>
<%@ page import="org.iplass.mtp.entity.EntityManager" %>
<%@ page import="org.iplass.mtp.entity.LoadOption"%>
<%@ page import="org.iplass.mtp.util.StringUtil" %>
<%@ page import="org.iplass.mtp.view.generic.*" %>
<%@ page import="org.iplass.mtp.view.generic.editor.*" %>
<%@ page import="org.iplass.mtp.view.generic.editor.SelectPropertyEditor.SelectDisplayType"%>
<%@ page import="org.iplass.mtp.view.generic.editor.ReferencePropertyEditor.RefComboSearchType"%>
<%@ page import="org.iplass.mtp.view.generic.editor.ReferencePropertyEditor.ReferenceDisplayType" %>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.mtp.ManagerLocator" %>
<%@ page import="org.iplass.gem.command.generic.detail.DetailViewCommand"%>
<%@ page import="org.iplass.gem.command.generic.reflink.GetReferenceLinkItemCommand"%>
<%@ page import="org.iplass.gem.command.generic.refcombo.GetEditorCommand"%>
<%@ page import="org.iplass.gem.command.generic.refcombo.ReferenceComboCommand"%>
<%@ page import="org.iplass.gem.command.generic.refcombo.SearchParentCommand"%>
<%@ page import="org.iplass.gem.command.generic.search.SearchViewCommand"%>
<%@ page import="org.iplass.gem.command.Constants" %>
<%@ page import="org.iplass.gem.command.CommandUtil" %>
<%@ page import="org.iplass.gem.command.GemResourceBundleUtil" %>
<%@ page import="org.iplass.gem.command.ViewUtil" %>
<%!
	boolean isDispProperty(PropertyDefinition pd, NestProperty property) {
// 		if (pd instanceof BinaryProperty) return false;
// 		if (pd instanceof ReferenceProperty) {
// 			if (pd.getMultiplicity() != 1) return false;
// 		}
		if (property.getEditor() == null) return false;
		return true;
	}
	String[] getReferenceValue(String searchCond, String key) {
		ArrayList<String> list = new ArrayList<String>();
		if (searchCond != null && searchCond.indexOf(key) > -1) {
			String[] split = searchCond.split("&");
			if (split != null && split.length > 0) {
				for (String tmp : split) {
					String[] kv = tmp.split("=");
					if (kv != null && kv.length > 1 && key.equals(kv[0])) {
						list.add(kv[1]);
					}
				}
			}
		}
		return list.size() > 0 ? list.toArray(new String[list.size()]) : null;
	}
	String[] getRefComboUpperCondition(String searchCond, String propName, ReferenceComboSetting setting) {
		String name = propName + "." + setting.getPropertyName();
		if (searchCond != null && searchCond.indexOf(name) > -1) {
			String[] split = searchCond.split("&");
			if (split != null && split.length > 0) {
				for (String tmp : split) {
					String[] kv = tmp.split("=");
					if (kv != null && kv.length > 1 && name.equals(kv[0]) && StringUtil.isNotBlank(kv[1])) {
						return kv;
					}
				}
				if (setting.getParent() != null) {
					return getRefComboUpperCondition(searchCond, name, setting.getParent());
				}
			}
		}
		return null;
	}

	PropertyEditor getLinkUpperPropertyEditor(String defName, String viewName, LinkProperty linkProperty) {
		EntityViewManager evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
		return evm.getPropertyEditor(defName, Constants.VIEW_TYPE_SEARCH, viewName, linkProperty.getLinkFromPropertyName());
	}

	String getLinkUpperType(PropertyEditor editor) {
		if (editor != null) {
			if (editor instanceof SelectPropertyEditor) {
				SelectPropertyEditor spe = (SelectPropertyEditor)editor;
				if (spe.getDisplayType() == SelectDisplayType.SELECT) {
					return "select";
//				} else if (spe.getDisplayType() == SelectDisplayType.Radio
//						|| spe.getDisplayType() == SelectDisplayType.Checkbox) {
//					//CheckBoxの場合も多重度が1の場合のみRadioになるのでラジオで(CheckBoxの場合、反応しない)
//					return "radio";
				} else if (spe.getDisplayType() == SelectDisplayType.RADIO) {
					return "radio";
				}
			} else if (editor instanceof ReferencePropertyEditor) {
				ReferencePropertyEditor rpe = (ReferencePropertyEditor)editor;
				if (rpe.getDisplayType() == ReferenceDisplayType.SELECT) {
					return "select";
//				} else if (rpe.getDisplayType() == ReferenceDisplayType.Checkbox) {
//					//CheckBoxの場合も多重度が1の場合のみRadioになるのでラジオで(CheckBoxの場合、反応しない)
//					return "radio";
				} else if (rpe.getDisplayType() == ReferenceDisplayType.REFCOMBO) {
					return "select";
				}
			}
		}
		return null;
	}

	List<Entity> getSelectItems(ReferencePropertyEditor editor, Condition defaultCondition, String searchCond, HashMap<String, Object> defaultSearchCond,
			PropertyEditor upperEditor) {
		Condition condition = defaultCondition;

		boolean doSearch = true;
		LinkProperty linkProperty = editor.getLinkProperty();
		if (linkProperty != null) {
			//連動の場合は上位値を取得して値が設定されている場合のみ検索
			doSearch = false;
			String upperValue = null;
			if (StringUtil.isNotEmpty(searchCond)) {
				//こっちはPrefixが必要
				if (searchCond.contains(Constants.SEARCH_COND_PREFIX + linkProperty.getLinkFromPropertyName())) {
					String[] split = searchCond.split("&");
					if (split != null && split.length > 0) {
						for (String tmp : split) {
							String[] kv = tmp.split("=");
							if (kv != null && kv.length > 1
									&& kv[0].equals(Constants.SEARCH_COND_PREFIX + linkProperty.getLinkFromPropertyName())
									&& StringUtil.isNotEmpty(kv[1])) {
								upperValue = kv[1];
								break;
							}
						}
					}

				}
			}
			if (upperValue == null) {
				//パラメータで設定されていない場合は、初期値用Mapからチェック
				if (defaultSearchCond != null) {
					//こっちはPrefixは不要
					Object tmp = defaultSearchCond.get(linkProperty.getLinkFromPropertyName());
					if (tmp instanceof String[] && ((String[])tmp).length > 0) {
						upperValue = ((String[])tmp)[0];
					}
				}
			}
			if (upperValue != null) {
				//参照元の値を条件に追加
				String upperPropName = linkProperty.getLinkToPropertyName();
				//ReferenceプロパティかをEditorでチェック(PropertyDefinitionを取得するのは余分になるため)
				if (upperEditor instanceof ReferencePropertyEditor) {
					upperPropName = upperPropName + "." + Entity.OID;
				}
				if (condition != null) {
					condition = new And(condition, new Equals(upperPropName, upperValue));
				} else {
					condition = new Equals(upperPropName, upperValue);
				}
				doSearch = true;
			}
		}

		if (doSearch) {
			Query q = new Query();
			q.from(editor.getObjectName());
			q.select(Entity.OID, Entity.NAME, Entity.VERSION);
			if (editor.getDisplayLabelItem() != null) {
				q.select().add(editor.getDisplayLabelItem());
			}
			if (condition != null) {
				q.where(condition);
			}
			if (editor.getSortType() != null) {
				String sortItem = editor.getSortItem() != null ? editor.getSortItem() : Entity.OID;
				if (!Entity.OID.equals(sortItem) && !Entity.NAME.equals(sortItem)) {
					q.select().add(sortItem);
				}
				SortType sortType = SortSpec.SortType.ASC;
				if ("DESC".equals(editor.getSortType().name())) {
					sortType = SortSpec.SortType.DESC;
				}
				q.order(new SortSpec(sortItem, sortType));
			}

			EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);
			return em.searchEntity(q).getList();
		} else {
			return Collections.emptyList();
		}
	}

	String getTitle(String defName, String viewName) {
		EntityDefinitionManager edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
		EntityViewManager evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);

		EntityDefinition ed = edm.get(defName);
		EntityView ev = evm.get(defName);
		SearchFormView fv = null;
		if (ev != null) {
			fv = ev.getSearchFormView(viewName);
		}
		if (fv == null) fv = FormViewUtil.createDefaultSearchFormView(ed);

		return TemplateUtil.getMultilingualString(fv.getTitle(), fv.getLocalizedTitleList(), ed.getDisplayName(), ed.getLocalizedDisplayNameList());
	}
	
	String getDisplayPropLabel(ReferencePropertyEditor editor, Entity refEntity) {
		String displayPropName = editor.getDisplayLabelItem();
		if (displayPropName == null) {
			displayPropName = Entity.NAME;
		}
		return refEntity.getValue(displayPropName);
	}
%>
<%
	ReferencePropertyEditor editor = (ReferencePropertyEditor) request.getAttribute(Constants.EDITOR_EDITOR);

	String rootDefName = (String) request.getAttribute(Constants.ROOT_DEF_NAME);
	PropertyDefinition pd = (PropertyDefinition) request.getAttribute(Constants.EDITOR_PROPERTY_DEFINITION);
	String scriptKey = (String) request.getAttribute(Constants.SECTION_SCRIPT_KEY);
	String displayLabel = (String) request.getAttribute(Constants.EDITOR_DISPLAY_LABEL);
	Boolean required = (Boolean) request.getAttribute(Constants.EDITOR_REQUIRED);
	if (required == null) required = false;
	@SuppressWarnings("unchecked") HashMap<String, Object> defaultSearchCond = (HashMap<String, Object>) request.getAttribute(Constants.DEFAULT_SEARCH_COND);

	String viewName = request.getParameter(Constants.VIEW_NAME);
	String searchCond = request.getParameter(Constants.SEARCH_COND);


	String propName = Constants.SEARCH_COND_PREFIX + editor.getPropertyName();

	boolean isMultiple = !editor.isSingleSelect();

	Condition condition = null;
	if (editor.getCondition() != null && !editor.getCondition().isEmpty()) {
		condition = new PreparedQuery(editor.getCondition()).condition(null);
	}

	EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);
	EntityDefinitionManager edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
	ReferenceProperty rp = (ReferenceProperty) pd;

	boolean showProperty = true;

	String pleaseSelectLabel = "";
	if (ViewUtil.isShowPulldownPleaseSelectLabel()) {
		pleaseSelectLabel = GemResourceBundleUtil.resourceString("generic.editor.reference.ReferencePropertyEditor_Condition.pleaseSelect");
	}

	//カスタムスタイル
	String customStyle = "";
	if (StringUtil.isNotEmpty(editor.getInputCustomStyle())) {
		customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getInputCustomStyleScriptKey(), null, null);
	}

	if (ViewUtil.isAutocompletionTarget()) {
		request.setAttribute(Constants.AUTOCOMPLETION_EDITOR, editor);
		request.setAttribute(Constants.AUTOCOMPLETION_SCRIPT_PATH, "/jsp/gem/generic/editor/reference/ReferencePropertyAutocompletion.jsp");
	}

	//検索条件
	if (editor.getDisplayType() == ReferenceDisplayType.SELECT) {
		//リスト
		PropertyEditor upperEditor = null;
		String upperType = null;
		if (editor.getLinkProperty() != null) {
			upperEditor = getLinkUpperPropertyEditor(rootDefName, viewName, editor.getLinkProperty());
			upperType = getLinkUpperType(upperEditor);
		}

		List<Entity> entityList = getSelectItems(editor, condition, searchCond, defaultSearchCond, upperEditor);

		String value = "";
		String[] propValue = (String[]) request.getAttribute(Constants.EDITOR_PROP_VALUE);
		if (propValue != null && propValue.length > 0) {
			value = propValue[0];
		}

		String strDefault = "";
		String[] defaultValue = (String[]) request.getAttribute(Constants.EDITOR_DEFAULT_VALUE);
		if (defaultValue != null && defaultValue.length > 0) {
			strDefault = defaultValue[0];
		}

		if (editor.getLinkProperty() != null && upperType != null) {
			//連動設定(連動元のタイプがサポートの場合のみサポート)
			LinkProperty link = editor.getLinkProperty();
%>
<select name="<c:out value="<%=propName %>"/>" class="form-size-02 inpbr refLinkSelect" style="<c:out value="<%=customStyle%>"/>"
data-defName="<c:out value="<%=rootDefName %>"/>"
data-viewType="<%=Constants.VIEW_TYPE_SEARCH %>"
data-viewName="<c:out value="<%=viewName %>"/>"
data-propName="<c:out value="<%=pd.getName() %>"/>"
data-linkName="<c:out value="<%=link.getLinkFromPropertyName() %>"/>"
data-prefix="<%=Constants.SEARCH_COND_PREFIX %>"
data-getItemWebapiName="<%=GetReferenceLinkItemCommand.WEBAPI_NAME %>"
data-upperType="<c:out value="<%=upperType %>"/>"
>
<option value=""><%= pleaseSelectLabel %></option>
<%
			for (Entity ref : entityList) {
				String selected = "";
				if (value.equals(ref.getOid())) selected = " selected";
				String displayPropLabel = getDisplayPropLabel(editor, ref);
%>
<option value="<c:out value="<%=ref.getOid() %>"/>" <%=selected %>><c:out value="<%=displayPropLabel %>" /></option>
<%
			}
%>
</select>
<%
		} else {
%>
<select name="<c:out value="<%=propName %>"/>" class="form-size-02 inpbr" style="<c:out value="<%=customStyle%>"/>">
<option value=""><%= pleaseSelectLabel %></option>
<%
			for (Entity ref : entityList) {
				String selected = "";
				String displayPropLabel = getDisplayPropLabel(editor, ref);
				if (value.equals(ref.getOid())) selected = " selected";
%>
<option value="<c:out value="<%=ref.getOid() %>"/>" <%=selected %>><c:out value="<%=displayPropLabel %>" /></option>
<%
			}
%>
</select>
<%
		}
%>

<script type="text/javascript">
$(function() {
	<%-- common.js --%>
	addNormalConditionItemResetHandler(function(){
		$("select[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>") + "']").val("<%=StringUtil.escapeJavaScript(strDefault) %>");
	});
<%
		if (required) {
%>
	<%-- common.js --%>
	addNormalValidator(function() {
		var val = $("select[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>") + "']").val();
		if (typeof val === "undefined" || val == null || val == "") {
			alert(scriptContext.locale.requiredMsg.replace("{0}", "<%=StringUtil.escapeJavaScript(displayLabel)%>"));
			return false;
		}
		return true;
	});
<%
		}
%>
});
</script>
<%
	} else if (editor.getDisplayType() == ReferenceDisplayType.CHECKBOX) {
		List<String> oids = new ArrayList<String>();
		String[] _propValue = getReferenceValue(searchCond, propName);
		if (_propValue == null || _propValue.length == 0) {
			String[] propValue = (String[]) request.getAttribute(Constants.EDITOR_PROP_VALUE);
			if (propValue != null && propValue.length > 0) {
				oids.addAll(Arrays.asList(propValue));
			}
		} else {
			//js側で復元できないのでこっちで復元
			oids.addAll(Arrays.asList(_propValue));
		}

		String[] defaultValue = (String[]) request.getAttribute(Constants.EDITOR_DEFAULT_VALUE);

		Query q = new Query();
		q.from(editor.getObjectName());
		q.select(Entity.OID, Entity.NAME);
		if (condition != null) {
			q.where(condition);
		}
		if (editor.getSortType() != null) {
			String sortItem = editor.getSortItem() != null ? editor.getSortItem() : Entity.OID;
			if (!Entity.OID.equals(sortItem) && !Entity.NAME.equals(sortItem)) q.select().add(sortItem);
			SortType sortType = SortSpec.SortType.ASC;
			if ("DESC".equals(editor.getSortType().name())) {
				sortType = SortSpec.SortType.DESC;
			}
			q.order(new SortSpec(sortItem, sortType));
		}

		List<Entity> entityList = em.searchEntity(q).getList();
%>
<ul class="list-check-01">
<%
		for (Entity ref : entityList) {
			String checked = "";
			if (oids.contains(ref.getOid())) checked = " checked";
%>
<li><label style="<c:out value="<%=customStyle%>"/>" title="<c:out value="<%=ref.getName() %>" />">
<input type="checkbox" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=ref.getOid() %>"/>" <%=checked %>/><c:out value="<%=ref.getName() %>" />
</label></li>
<%
		}
%>
</ul>

<script type="text/javascript">
$(function() {
	<%-- common.js --%>
	addNormalConditionItemResetHandler(function(){
		<%-- 全部外す --%>
		$(":checkbox[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>") + "']").prop("checked",false).trigger("iplassCheckboxPropChange");
<%
		//個別に選択
		if (defaultValue != null && defaultValue.length > 0) {
			for (String strDefault: defaultValue) {
%>
		$(":checkbox[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>") + "'][value='<%=StringUtil.escapeJavaScript(strDefault)%>']").prop("checked",true).trigger("iplassCheckboxPropChange");
<%
			}
		}
%>
	});
<%
		if (required) {
%>
	<%-- common.js --%>
	addNormalValidator(function() {
		var val = $(":checkbox[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>") + "']:checked").val();
		if (typeof val === "undefined" || val == null || val == "") {
			alert(scriptContext.locale.requiredMsg.replace("{0}", "<%=StringUtil.escapeJavaScript(displayLabel)%>"));
			return false;
		}
		return true;
	});
<%
		}
%>
});
</script>
<%
	} else if (editor.getDisplayType() == ReferenceDisplayType.REFCOMBO) {
		Entity entity = null;
		//パラメータに初期値があれば初期値でロード(searchCondがnullでない場合は入ってこない)
		String[] propValue = (String[]) request.getAttribute(Constants.EDITOR_PROP_VALUE);
		if (propValue != null && propValue.length > 0) {
			entity = em.load(propValue[0], rp.getObjectDefinitionName(), new LoadOption(false, false));
		}
		//searchCondに初期値があればsearchCondの値でロード
		if (entity == null && searchCond != null && searchCond.contains(propName)) {
			//js側で復元できないのでこっちで復元
			String[] ary = getReferenceValue(searchCond, propName);
			if (ary != null && ary.length > 0) {
				entity = em.load(ary[0], rp.getObjectDefinitionName(), new LoadOption(false, false));
			}
		}

		RefComboSearchType searchType = editor.getSearchType();
		if (searchType == null) searchType = RefComboSearchType.NONE;

		//最階層が未選択で上位階層で検索を許可している場合、上位階層の選択条件をチェック
		String upperName = "";
		String upperOid = "";
		if (entity == null && editor.getReferenceComboSetting() != null && searchType == RefComboSearchType.UPPER) {
			String[] upperCondition = getRefComboUpperCondition(searchCond, pd.getName(), editor.getReferenceComboSetting());
			if (upperCondition != null && upperCondition.length > 1
					&& StringUtil.isNotBlank(upperCondition[0]) && StringUtil.isNotBlank(upperCondition[1])) {
				upperName = upperCondition[0];
				upperOid = upperCondition[1];
			}
		}
		String oid = entity != null ? entity.getOid() != null ? entity.getOid() : "" : "";

		//初期設定では上位のみの指定は許可していないので考慮不要
		Entity defEntity = null;
		String[] defaultValue = (String[]) request.getAttribute(Constants.EDITOR_DEFAULT_VALUE);
		if (defaultValue != null && defaultValue.length > 0) {
			defEntity = em.load(defaultValue[0], rp.getObjectDefinitionName(), new LoadOption(false, false));
		}
		String defOid = defEntity != null ? defEntity.getOid() != null ? defEntity.getOid() : "" : "";

		String _defName = (String) request.getAttribute(Constants.DEF_NAME);
		if (viewName == null) viewName = "";
		else viewName = StringUtil.escapeHtml(viewName);

		//連動コンボの生成はfunction.js
%>
<%-- XSS対応-メタの設定のため対応なし(searchType) --%>
<select name="<c:out value="<%=propName %>"/>" class="form-size-02 inpbr refCombo" style="<c:out value="<%=customStyle%>"/>" data-oid="<c:out value="<%=oid%>"/>"
 data-propName="<c:out value="<%=pd.getName() %>"/>" data-defName="<c:out value="<%=_defName %>"/>"
 data-viewName="<c:out value="<%=viewName %>"/>" data-webapiName="<%=ReferenceComboCommand.WEBAPI_NAME%>"
 data-getEditorWebapiName="<%=GetEditorCommand.WEBAPI_NAME %>" data-searchParentWebapiName="<%=SearchParentCommand.WEBAPI_NAME %>"
 data-viewType="<%=Constants.VIEW_TYPE_SEARCH %>" data-prefix="sc_" data-searchType="<%=searchType%>"
 data-upperName="<c:out value="<%=upperName%>" />" data-upperOid="<c:out value="<%=upperOid%>" />" data-norewrite="true" data-customStyle="<c:out value="<%=customStyle%>"/>">
</select>
<script type="text/javascript">
$(function() {
	<%-- common.js --%>
	addNormalConditionItemResetHandler(function(){
		<%-- 再ロード --%>
		$("select[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>") + "']").refCombo({
			reset:true,
			oid:"<%=StringUtil.escapeJavaScript(defOid)%>"
		});
	});
<%
		if (required) {
%>
	<%-- common.js --%>
	addNormalValidator(function() {
		var val = $("select[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>") + "']").val();
		if (typeof val === "undefined" || val == null || val == "") {
			alert(scriptContext.locale.requiredMsg.replace("{0}", "<%=StringUtil.escapeJavaScript(displayLabel)%>"));
			return false;
		}
		return true;
	});
<%
		}
%>
});
</script>
<%
	} else if (editor.getDisplayType() == ReferenceDisplayType.LINK && editor.isUseSearchDialog()) {
		String _defName = editor.getObjectName();
		String _viewName = editor.getViewName() != null ? editor.getViewName() : "";

		String contextPath = TemplateUtil.getTenantContextPath();
		String urlPath = ViewUtil.getParamMappingPath(_defName, _viewName);

		//選択ボタン
		String select = "";
		if (StringUtil.isNotBlank(editor.getSelectActionName())) {
			select = contextPath + "/" + editor.getSelectActionName() + urlPath;
		} else {
			select = contextPath + "/" + SearchViewCommand.SELECT_ACTION_NAME + urlPath;
		}

		//詳細編集でのリンククリック
		String view = "";
		if (StringUtil.isNotBlank(editor.getViewrefActionName())) {
			view = contextPath + "/" + editor.getViewrefActionName() + urlPath;
		} else {
			view = contextPath + "/" + DetailViewCommand.REF_VIEW_ACTION_NAME + urlPath;
		}

		String urlParam = "";
		if (StringUtil.isNotBlank(editor.getUrlParameterScriptKey())) {
			urlParam = ManagerLocator.getInstance().getManager(EntityViewManager.class).getUrlParameter(rootDefName, editor.getUrlParameterScriptKey(), null);
		}

		String ulId = "ul_" + propName;
%>
<ul id="<c:out value="<%=ulId %>"/>" data-deletable="true" class="mb05">
<%
		//デフォルト検索条件からリンク作成(searchCondがnull出ない場合は設定されてこない)
		String[] propValue = (String[]) request.getAttribute(Constants.EDITOR_PROP_VALUE);
		if (propValue != null && propValue.length > 0) {
			for (int i = 0; i < propValue.length; i++) {
				String oid = propValue[i];
				Entity entity = em.load(oid, _defName);
				String displayPropLabel = getDisplayPropLabel(editor, entity);
				if (entity == null || displayPropLabel == null) continue;
				String liId = "li_" + propName + i;
				String linkId = propName + "_" + entity.getOid();
				String key = entity.getOid() + "_" + entity.getVersion();
%>
<li id="<c:out value="<%=liId %>"/>" class="list-add">
<a href="javascript:void(0)" class="modal-lnk"id="<c:out value="<%=linkId %>"/>" style="<c:out value="<%=customStyle%>"/>" onclick="showReference('<%=StringUtil.escapeJavaScript(view)%>', '<%=StringUtil.escapeJavaScript(_defName)%>', '<%=StringUtil.escapeJavaScript(entity.getOid())%>', '<%=entity.getVersion() %>', '<%=StringUtil.escapeJavaScript(linkId)%>', false)"><c:out value="<%=displayPropLabel %>" /></a>
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_Edit.delete')}" class="gr-btn-02 del-btn" onclick="deleteItem('<%=StringUtil.escapeJavaScript(liId)%>')" />
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=key %>"/>"/>
</li>
<%
			}
		}
		//searchCondを解析してリンク作成
		if (searchCond != null && searchCond.contains(propName)) {
			String[] params  = searchCond.split("&");
			for (int i = 0; i < params.length; i++) {
				String[] kv = params[i].split("=");
				if (kv.length > 1 && kv[0].equals(propName)) {
					int index = kv[1].lastIndexOf("_");
					String oid = kv[1].substring(0, index);
					Entity entity = em.load(oid,_defName);
					String displayPropLabel = getDisplayPropLabel(editor, entity);
					if (entity == null || displayPropLabel == null) continue;
					String liId = "li_" + propName + i;
					String linkId = propName + "_" + entity.getOid();
					String key = entity.getOid() + "_" + entity.getVersion();
					//hiddenにjavascriptで値上書きしないようにnorewrite属性をつけておく
%>
<li id="<c:out value="<%=liId %>"/>" class="list-add">
<a href="javascript:void(0)" class="modal-lnk"id="<c:out value="<%=linkId %>"/>" style="<c:out value="<%=customStyle%>"/>" onclick="showReference('<%=StringUtil.escapeJavaScript(view)%>', '<%=StringUtil.escapeJavaScript(_defName)%>', '<%=StringUtil.escapeJavaScript(entity.getOid())%>', '<%=entity.getVersion() %>', '<%=StringUtil.escapeJavaScript(linkId)%>', false)"><c:out value="<%=displayPropLabel %>" /></a>
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_Edit.delete')}" class="gr-btn-02 del-btn" onclick="deleteItem('<%=StringUtil.escapeJavaScript(liId)%>')" />
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=key %>"/>" data-norewrite="true"/>
</li>
<%
				}
			}
		}
%>
</ul>
<%
		String selBtnId = "sel_btn_" + propName;
%>
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_Edit.select')}" class="gr-btn-02 modal-btn" id="<c:out value="<%=selBtnId %>"/>" />
<script type="text/javascript">
$(function() {
	var params = {
		selectAction: "<%=StringUtil.escapeJavaScript(select) %>"
		, viewAction: "<%=StringUtil.escapeJavaScript(view) %>"
		, defName: "<%=StringUtil.escapeJavaScript(_defName) %>"
		, propName: "<%=StringUtil.escapeJavaScript(propName) %>"
		, multiplicity: "-1"
		, urlParam: "<%=StringUtil.escapeJavaScript(urlParam) %>"
		, refEdit: false
		, viewName: "<%=StringUtil.escapeJavaScript(_viewName) %>"
		, permitConditionSelectAll: <%=editor.isPermitConditionSelectAll()%>
	}
	var $selBtn = $(":button[id='<%=StringUtil.escapeJavaScript(selBtnId)%>']");
	for (key in params) {
		$selBtn.attr("data-" + key, params[key]);
	}
	$selBtn.on("click", function() {
		searchReference(params.selectAction, params.viewAction, params.defName, params.propName, params.multiplicity, <%=isMultiple%>,
				 params.urlParam, params.refEdit, function(){}, null, params.viewName, params.permitConditionSelectAll);
	});

	<%-- common.js --%>
	addNormalConditionItemResetHandler(function(){
		<%-- 全部削除 --%>
		var $ul = $("#" + es("<%=StringUtil.escapeJavaScript(ulId)%>"));
		$ul.children("li").each(function(){
			$(this).remove();
		});
<%
		//デフォルトで設定されているものを追加
		String[] defaultValue = (String[]) request.getAttribute(Constants.EDITOR_DEFAULT_VALUE);
		if (defaultValue != null && defaultValue.length > 0) {
%>
			var _propName = params.propName.replace(/\[/g, "\\[").replace(/\]/g, "\\]").replace(/\./g, "\\.");
<%			for (int i = 0; i < defaultValue.length; i++) {
				String oid = defaultValue[i];
				Entity entity = em.load(oid, _defName);
				String displayPropLabel = getDisplayPropLabel(editor, entity);
				if (entity == null || displayLabel == null) continue;

				String liId = StringUtil.escapeJavaScript("li_" + propName + i);
				String label = StringUtil.escapeJavaScript(displayPropLabel);
				String key = StringUtil.escapeJavaScript(entity.getOid() + "_" + entity.getVersion());
%>
		<%-- common.js --%>
		addReference("<%=liId%>", params.viewAction, params.defName, "<%=key%>", "<%=label%>", params.propName, "ul_" + _propName, params.refEdit);
<%
			}
		}
%>
	});

<%
		if (required) {
%>
	<%-- common.js --%>
	addNormalValidator(function() {
		var $ul = $("#" + es("<%=StringUtil.escapeJavaScript(ulId)%>"));
		if ($ul.children().length == 0) {
			alert(scriptContext.locale.requiredMsg.replace("{0}", "<%=StringUtil.escapeJavaScript(displayLabel)%>"));
			return false;
		}
		return true;
	});
<%
		}
%>
});
</script>
<%
	} else if (editor.getDisplayType() == ReferenceDisplayType.TREE) {
		//詳細編集でのリンククリック
		String ulId = "ul_" + propName;

		String contextPath = TemplateUtil.getTenantContextPath();
		String urlPath = ViewUtil.getParamMappingPath(editor.getObjectName(), editor.getViewName());

		String view = "";
		if (StringUtil.isNotBlank(editor.getViewrefActionName())) {
			view = contextPath + "/" + editor.getViewrefActionName() + urlPath;
		} else {
			view = contextPath + "/" + DetailViewCommand.REF_VIEW_ACTION_NAME + urlPath;
		}
%>
<ul id="<c:out value="<%=ulId %>"/>" data-deletable="true" class="mb05">
<%
		//デフォルト検索条件からリンク作成(searchCondがnull出ない場合は設定されてこない)
		String[] propValue = (String[]) request.getAttribute(Constants.EDITOR_PROP_VALUE);
		if (propValue != null && propValue.length > 0) {
			for (int i = 0; i < propValue.length; i++) {
				String oid = propValue[i];
				Entity entity = em.load(oid, rp.getObjectDefinitionName());
				if (entity == null || entity.getName() == null) continue;
				String liId = "li_" + propName + i;
				String linkId = propName + "_" + entity.getOid();
				String key = entity.getOid() + "_" + entity.getVersion();
%>
<li id="<c:out value="<%=liId %>"/>" class="list-add">
<a href="javascript:void(0)" class="modal-lnk"id="<c:out value="<%=linkId %>"/>" style="<c:out value="<%=customStyle%>"/>" onclick="showReference('<%=StringUtil.escapeJavaScript(view)%>', '<%=StringUtil.escapeJavaScript(rp.getObjectDefinitionName())%>', '<%=StringUtil.escapeJavaScript(entity.getOid())%>', '<%=entity.getVersion() %>', '<%=StringUtil.escapeJavaScript(linkId)%>', false)"><c:out value="<%=entity.getName() %>" /></a>
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_Edit.delete')}" class="gr-btn-02 del-btn" onclick="deleteItem('<%=StringUtil.escapeJavaScript(liId)%>')" />
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=key %>"/>"/>
</li>
<%
			}
		}
		//searchCondを解析してリンク作成
		if (searchCond != null && searchCond.contains(propName)) {
			String[] params  = searchCond.split("&");
			for (int i = 0; i < params.length; i++) {
				String[] kv = params[i].split("=");
				if (kv.length > 1 && kv[0].equals(propName)) {
					int index = kv[1].lastIndexOf("_");
					String oid = kv[1].substring(0, index);
					Entity entity = em.load(oid, rp.getObjectDefinitionName());
					if (entity == null || entity.getName() == null) continue;
					String liId = "li_" + propName + i;
					String linkId = propName + "_" + entity.getOid();
					String key = entity.getOid() + "_" + entity.getVersion();
					//hiddenにjavascriptで値上書きしないようにnorewrite属性をつけておく
%>
<li id="<c:out value="<%=liId %>"/>" class="list-add">
<a href="javascript:void(0)" class="modal-lnk"id="<c:out value="<%=linkId %>"/>" style="<c:out value="<%=customStyle%>"/>" onclick="showReference('<%=StringUtil.escapeJavaScript(view)%>', '<%=StringUtil.escapeJavaScript(rp.getObjectDefinitionName())%>', '<%=StringUtil.escapeJavaScript(entity.getOid())%>', '<%=entity.getVersion() %>', '<%=StringUtil.escapeJavaScript(linkId)%>', false)"><c:out value="<%=entity.getName() %>" /></a>
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_Edit.delete')}" class="gr-btn-02 del-btn" onclick="deleteItem('<%=StringUtil.escapeJavaScript(liId)%>')" />
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=key %>"/>" data-norewrite="true"/>
</li>
<%
				}
			}
		}
%>
</ul>
<%
		String selBtnId = "sel_btn_" + propName;
		String title = getTitle(rp.getObjectDefinitionName(), viewName);

		String prefix = "";
		int index = propName.indexOf(pd.getName());
		if (index > 0) {
			//propNameから実際のプロパティ名を除去してプレフィックスを取得
			prefix = propName.substring(0, index);
		}

		String linkPropName = "";
		String upperType = "";
		if (editor.getLinkProperty() != null) {
			linkPropName = editor.getLinkProperty().getLinkFromPropertyName();
			PropertyEditor upperEditor = getLinkUpperPropertyEditor(rootDefName, viewName, editor.getLinkProperty());
			upperType = getLinkUpperType(upperEditor);
		}
%>
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_Edit.select')}" class="gr-btn-02 recursiveTreeTrigger" id="<c:out value="<%=selBtnId %>"/>"
 data-defName="<c:out value="<%=rootDefName%>"/>"
 data-viewType="search"
 data-viewName="<c:out value="<%=viewName%>"/>"
 data-propName="<c:out value="<%=pd.getName()%>"/>"
 data-prefix="<c:out value="<%=prefix%>"/>"
 data-multiplicity="-1"
 data-linkPropName="<c:out value="<%=linkPropName%>"/>"
 data-upperType="<c:out value="<%=upperType%>"/>"
 data-webapiName="<%=SearchTreeDataCommand.WEBAPI_NAME %>"
 data-container="<c:out value="<%=ulId %>"/>"
 data-title="<c:out value="<%=title%>"/>"
 data-deletable="true"
 data-customStyle="<c:out value="<%=customStyle%>"/>"
 data-viewAction="<c:out value="<%=view%>"/>"
 data-refDefName="<c:out value="<%=rp.getObjectDefinitionName()%>"/>"
 data-refEdit="false"
 />
<script type="text/javascript">
$(function() {
	<%-- common.js --%>
	addNormalConditionItemResetHandler(function(){
		<%-- 全部削除 --%>
		var $ul = $("#" + es("<%=StringUtil.escapeJavaScript(ulId)%>"));
		$ul.children("li").each(function(){
			$(this).remove();
		});
<%
		//デフォルトで設定されているものを追加
		String[] defaultValue = (String[]) request.getAttribute(Constants.EDITOR_DEFAULT_VALUE);
		if (defaultValue != null && defaultValue.length > 0) {
%>
			var _propName = params.propName.replace(/\[/g, "\\[").replace(/\]/g, "\\]").replace(/\./g, "\\.");
<%			for (int i = 0; i < defaultValue.length; i++) {
				String oid = defaultValue[i];
				Entity entity = em.load(oid, rp.getObjectDefinitionName());
				if (entity == null || entity.getName() == null) continue;

				String liId = StringUtil.escapeJavaScript("li_" + propName + i);
				String viewAction = StringUtil.escapeJavaScript(view);
				String _defName = StringUtil.escapeJavaScript(rp.getObjectDefinitionName());
				String key = StringUtil.escapeJavaScript(entity.getOid() + "_" + entity.getVersion());
				String label = StringUtil.escapeJavaScript(entity.getName());
				String _propName = StringUtil.escapeJavaScript(propName);
%>
		<%-- common.js --%>
		addReference("<%=liId%>", "<%=viewAction%>", "<%=_defName%>", "<%=key%>", "<%=label%>", "<%=_propName%>", "ul_<%=_propName%>", false);
<%
			}
		}
%>
	});

<%
		if (required) {
%>
	<%-- common.js --%>
	addNormalValidator(function() {
		var $ul = $("#" + es("<%=StringUtil.escapeJavaScript(ulId)%>"));
		if ($ul.children().length == 0) {
			alert(scriptContext.locale.requiredMsg.replace("{0}", "<%=StringUtil.escapeJavaScript(displayLabel)%>"));
			return false;
		}
		return true;
	});
<%
		}
%>
});
</script>
<%
	} else {
		//→今までのネストと同じ動き
		showProperty = false;
	}

	String nestStyle = (String) request.getAttribute(Constants.EDITOR_STYLE);
	//内部でentityから.付きのプロパティ名で値取るので、プレフィックスついてない名前渡す
	request.setAttribute(Constants.EDITOR_REF_SHOW_PROPERTY, showProperty);
	request.setAttribute(Constants.EDITOR_REF_NEST_PROP_NAME, editor.getPropertyName());
	request.setAttribute(Constants.EDITOR_REF_NEST_PROPERTY, rp);
	request.setAttribute(Constants.EDITOR_REF_NEST_STYLE, nestStyle);
	request.setAttribute(Constants.EDITOR_REF_NEST_REQUIRED, required);
	request.setAttribute(Constants.EDITOR_REF_NEST_DISPLAY_LABEL, displayLabel);
	request.setAttribute(Constants.EDITOR_REF_NEST_EDITOR, editor);
%>
<jsp:include page="ReferencePropertyEditor_Condition_Nest.jsp" />
<%
	request.removeAttribute(Constants.EDITOR_REF_SHOW_PROPERTY);
	request.removeAttribute(Constants.EDITOR_REF_NEST_PROP_NAME);
	request.removeAttribute(Constants.EDITOR_REF_NEST_PROPERTY);
	request.removeAttribute(Constants.EDITOR_REF_NEST_STYLE);
	request.removeAttribute(Constants.EDITOR_REF_NEST_REQUIRED);
	request.removeAttribute(Constants.EDITOR_REF_NEST_DISPLAY_LABEL);
	request.removeAttribute(Constants.EDITOR_REF_NEST_EDITOR);
%>
