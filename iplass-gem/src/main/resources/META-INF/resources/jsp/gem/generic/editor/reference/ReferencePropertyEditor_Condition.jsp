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

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.Collections"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Map" %>
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
<%@ page import="org.iplass.mtp.view.generic.editor.ReferencePropertyEditor.UrlParameterActionType"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.mtp.ManagerLocator" %>
<%@ page import="org.iplass.mtp.impl.util.ConvertUtil" %>
<%@ page import="org.iplass.gem.command.generic.detail.DetailViewCommand"%>
<%@ page import="org.iplass.gem.command.generic.refcombo.GetReferenceComboSettingCommand"%>
<%@ page import="org.iplass.gem.command.generic.reflink.GetReferenceLinkItemCommand"%>
<%@ page import="org.iplass.gem.command.generic.refcombo.ReferenceComboCommand"%>
<%@ page import="org.iplass.gem.command.generic.refcombo.SearchParentCommand"%>
<%@ page import="org.iplass.gem.command.generic.reftree.SearchTreeDataCommand"%>
<%@ page import="org.iplass.gem.command.generic.refunique.GetReferenceUniqueItemCommand"%>
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

	/**
	 * 連動コンボの上位層の検索状態を取得します。
	 *
	 * @param searchCondMap 検索条件Map
	 * @param propName 下層プロパティ名
	 * @param setting 対象のコンボ設定
	 * @return [0]：選択最下層のプロパティ名、[1]：選択最下層のOID
	 */
	String[] getRefComboUpperCondition(Map<String, List<String>> searchCondMap, String propName, ReferenceComboSetting setting) {
		String name = propName + "." + setting.getPropertyName();
		String[] value = ViewUtil.getSearchCondValue(searchCondMap, name);
		if (value != null) {
			return new String[] {
				name, value[0]
			};
		}
		if (setting.getParent() != null) {
			return getRefComboUpperCondition(searchCondMap, name, setting.getParent());
		}
		return null;
	}

	/**
	 * Entityデータを最小限のプロパティでロードします。
	 * 
	 * @param definitionName Entity定義名
	 * @param oid 対象OID
	 * @param addPropNames OID、VERSION以外に取得したいプロパティ。多重度複数のReferenceは考慮していません。
	 * @return ロードEntityデータ
	 */
	Entity loadReferenceEntity(String definitionName, String oid, Object... addPropNames) {
		Query query = new Query().select(Entity.OID, Entity.VERSION);
		if (addPropNames != null && addPropNames.length > 0) {
			query.getSelect().add(addPropNames);
		}

		query.from(definitionName)
			.where(new Equals(Entity.OID, oid));

		EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);
		Entity ret = em.searchEntity(query).getFirst();

		return ret;
	}

	PropertyEditor getLinkUpperPropertyEditor(String defName, String viewName, LinkProperty linkProperty, String nestPropertyName) {
		String upperPropertyName = null;
		if (linkProperty.isWithNestProperty()) {
			// 同じNestTable内のプロパティに連動する場合は、NestTableのプロパティを付加
			upperPropertyName = nestPropertyName + "." + linkProperty.getLinkFromPropertyName();
		} else {
			upperPropertyName = linkProperty.getLinkFromPropertyName();
		}
		EntityViewManager evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
		return evm.getPropertyEditor(defName, Constants.VIEW_TYPE_SEARCH, viewName, upperPropertyName, null);
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

	List<Entity> getSelectItems(ReferencePropertyEditor editor, Condition defaultCondition, Map<String, List<String>> searchCondMap, 
			HashMap<String, Object> defaultSearchCond, PropertyEditor upperEditor, String nestPropertyName) {

		Condition condition = defaultCondition;

		boolean doSearch = true;
		LinkProperty linkProperty = editor.getLinkProperty();
		if (linkProperty != null) {
			//連動の場合は上位値を取得して値が設定されている場合のみ検索
			doSearch = false;

			String upperPropertyName = null;
			if (linkProperty.isWithNestProperty()) {
				upperPropertyName = nestPropertyName + "." + linkProperty.getLinkFromPropertyName();
			} else {
				upperPropertyName = linkProperty.getLinkFromPropertyName();
			}
			String[] upperValyeArray = ViewUtil.getSearchCondValue(searchCondMap, Constants.SEARCH_COND_PREFIX + upperPropertyName);
			String upperValue = upperValyeArray != null && upperValyeArray.length > 0 ? upperValyeArray[0] : null;
			if (upperValue == null) {
				//パラメータで設定されていない場合は、初期値用Mapからチェック
				if (defaultSearchCond != null) {
					//こっちはPrefixは不要
					Object tmp = defaultSearchCond.get(upperPropertyName);
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
			q.select(Entity.OID, Entity.VERSION);
			if (editor.getDisplayLabelItem() != null) {
				q.select().add(editor.getDisplayLabelItem());
			} else {
				q.select().add(Entity.NAME);
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

	boolean isUniqueProp(ReferencePropertyEditor editor) {
		if (editor.getDisplayType() == ReferenceDisplayType.UNIQUE && editor.getUniqueItem() != null) {
			// OIDをユニークキーフィールドとして使えるように
			if (Entity.OID.equals(editor.getUniqueItem())) return true;

			EntityDefinition ed = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class).get(editor.getObjectName());
			PropertyDefinition pd = ed.getProperty(editor.getUniqueItem());
			if (pd.getIndexType() == IndexType.UNIQUE || pd.getIndexType() == IndexType.UNIQUE_WITHOUT_NULL) {
				return true;
			}
		}
		return false;
	}

	String getUniquePropValue(ReferencePropertyEditor editor, Entity refEntity) {
		String uniquePropName = editor.getUniqueItem();
		if (uniquePropName == null || refEntity.getValue(uniquePropName) == null) return "";
		// FIXME ユニークキー項目のプロパティエディター定義が存在しないので、文字列に変換して問題ないかな。。
		String str = ConvertUtil.convertToString(refEntity.getValue(uniquePropName));
		return StringUtil.escapeHtml(str);
	}

	boolean checkActionType(List<UrlParameterActionType> actions, UrlParameterActionType actionType) {
		if (actions == null) {
			//未指定の場合は、SELECTとADDはOK
			return actionType == UrlParameterActionType.SELECT || actionType == UrlParameterActionType.ADD;
		} else {
			//指定されている場合は、対象かどうかをチェック
			return actions.contains(actionType);
		}
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

	String viewName = (String)request.getAttribute(Constants.VIEW_NAME);
	if (viewName == null) viewName = "";
	Map<String, List<String>> searchCondMap = (Map<String, List<String>>)request.getAttribute(Constants.SEARCH_COND_MAP);
	String defName = request.getParameter(Constants.DEF_NAME);

	String propName = Constants.SEARCH_COND_PREFIX + editor.getPropertyName();

	// NestTable参照元プロパティ名
	String nestPropertyName = (String)request.getAttribute(Constants.EDITOR_REF_NEST_PROP_NAME);
	
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
	if (editor.getDisplayType() != ReferenceDisplayType.LABEL) {
		if (StringUtil.isNotEmpty(editor.getInputCustomStyle())) {
			customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getInputCustomStyleScriptKey(), null, null);
		}
	} else {
		if (StringUtil.isNotEmpty(editor.getCustomStyle())) {
			customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getOutputCustomStyleScriptKey(), null, null);
		}
	}

	if (ViewUtil.isAutocompletionTarget()) {
		request.setAttribute(Constants.AUTOCOMPLETION_EDITOR, editor);
		request.setAttribute(Constants.AUTOCOMPLETION_SCRIPT_PATH, "/jsp/gem/generic/editor/reference/ReferencePropertyAutocompletion.jsp");
	}
	
	// 「値なし」を検索条件の選択肢に追加するか
	String isNullLabel = "";
	String isNullValue = "";
	if(editor.isIsNullSearchEnabled()) {
		isNullLabel = GemResourceBundleUtil.resourceString("generic.editor.select.SelectPropertyEditor_Condition.isNullDisplayName");
		isNullValue = Constants.ISNULL_VALUE;
	}

	//検索条件
	if (editor.getDisplayType() == ReferenceDisplayType.SELECT) {
		//リスト
		PropertyEditor upperEditor = null;
		String upperType = null;
		if (editor.getLinkProperty() != null) {
			upperEditor = getLinkUpperPropertyEditor(rootDefName, viewName, editor.getLinkProperty(), nestPropertyName);
			upperType = getLinkUpperType(upperEditor);
		}

		List<Entity> entityList = getSelectItems(editor, condition,  searchCondMap, defaultSearchCond, upperEditor, nestPropertyName);

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
			
			// 連動先のプロパティ名
			String linkToPropName = null;
			if (StringUtil.isNotEmpty(nestPropertyName)) {
				// NestTable上の場合は、参照元も付加
				linkToPropName = nestPropertyName + "." + pd.getName();
			} else {
				// 参照プロパティ名
				linkToPropName = pd.getName();
			}

			// 連動元のアイテム名
			String linkFromName = null;
			if (link.isWithNestProperty()) {
				// 連動元がNestTableの場合は、参照元も付加
				linkFromName = nestPropertyName + "." + link.getLinkFromPropertyName();
			} else {
				linkFromName = link.getLinkFromPropertyName();
			}
%>
<select name="<c:out value="<%=propName %>"/>" class="form-size-02 inpbr refLinkSelect" style="<c:out value="<%=customStyle%>"/>"
data-defName="<c:out value="<%=rootDefName %>"/>"
data-viewType="<%=Constants.VIEW_TYPE_SEARCH %>"
data-viewName="<c:out value="<%=viewName %>"/>"
data-propName="<c:out value="<%=linkToPropName %>"/>"
data-linkName="<c:out value="<%=linkFromName %>"/>"
data-prefix="<%=Constants.SEARCH_COND_PREFIX %>"
data-getItemWebapiName="<%=GetReferenceLinkItemCommand.WEBAPI_NAME %>"
data-upperType="<c:out value="<%=upperType %>"/>"
>
<option value=""><%= pleaseSelectLabel %></option>
<%
			// 「値なし」を検索条件の選択肢に追加するか
			if (editor.isIsNullSearchEnabled()) {
				String selected = isNullValue.equals(value) ? " selected" : "";
%>
<option value="<c:out value="<%=isNullValue %>"/>" <c:out value="<%=selected %>"/>><c:out value="<%=isNullLabel %>" /></option>
<%
			}
%>
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
			// 「値なし」を検索条件の選択肢に追加するか
			if (editor.isIsNullSearchEnabled()) {
				String selected = isNullValue.equals(value) ? " selected" : "";
%>
<option value="<c:out value="<%=isNullValue %>"/>" <c:out value="<%=selected %>"/>><c:out value="<%=isNullLabel %>" /></option>
<%
			}
%>
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
			alert(scriptContext.gem.locale.common.requiredMsg.replace("{0}", "<%=StringUtil.escapeJavaScript(displayLabel)%>"));
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
		String[] _propValue = ViewUtil.getSearchCondValue(searchCondMap, propName);
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
		q.select(Entity.OID);
		if (editor.getDisplayLabelItem() != null) {
			q.select().add(editor.getDisplayLabelItem());
		} else {
			q.select().add(Entity.NAME);
		}
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
			String displayPropLabel = getDisplayPropLabel(editor, ref);
%>
<li <c:if test="<%=editor.isItemDirectionColumn() %>">style="display: block;"</c:if>><label style="<c:out value="<%=customStyle%>"/>" title="<c:out value="<%=displayPropLabel %>" />">
<input type="checkbox" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=ref.getOid() %>"/>" <%=checked %>/><c:out value="<%=displayPropLabel %>" />
</label></li>
<%
		}
%>
<%
		
		// 「値なし」を検索条件の選択肢に追加するか
		if (editor.isIsNullSearchEnabled()) {
			String checked = oids.contains(isNullValue) ? " checked" : "";
%>
<li <c:if test="<%=editor.isItemDirectionColumn() %>">style="display: block;"</c:if>><label style="<c:out value="<%=customStyle%>"/>" title="<c:out value="<%=isNullLabel %>" />">
<input type="checkbox" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=isNullValue %>"/>" <%=checked %>/><c:out value="<%=isNullLabel %>" />
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
			alert(scriptContext.gem.locale.common.requiredMsg.replace("{0}", "<%=StringUtil.escapeJavaScript(displayLabel)%>"));
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

		//最下層の選択データの取得
		Entity currentEntity = null;
		
		//パラメータに初期値があれば初期値でロード
		//検索実行済の場合(searchCondMapがnullでない場合）は設定されない
		String[] propValue = (String[]) request.getAttribute(Constants.EDITOR_PROP_VALUE);
		if (propValue != null && propValue.length > 0) {
			currentEntity = loadReferenceEntity(rp.getObjectDefinitionName(), propValue[0]);
		}

		//検索実行時の条件が設定されていれば条件値の値でロード
		if (currentEntity == null) {
			//js側で復元できないのでこっちで復元
			String[] searchCondValue = ViewUtil.getSearchCondValue(searchCondMap, propName);
			if (searchCondValue != null && searchCondValue.length > 0) {
				currentEntity = loadReferenceEntity(rp.getObjectDefinitionName(), searchCondValue[0]);
			}
		}

		String currentOid = currentEntity != null ? currentEntity.getOid() : "";

		RefComboSearchType searchType = editor.getSearchType();
		if (searchType == null) searchType = RefComboSearchType.NONE;

		//最下層が未選択の場合、上位階層の選択状態をチェック
		String upperName = "";
		String upperOid = "";
		if (currentEntity == null && editor.getReferenceComboSetting() != null) {
			String[] upperCondition = getRefComboUpperCondition(searchCondMap, pd.getName(), editor.getReferenceComboSetting());
			if (upperCondition != null && upperCondition.length > 1
					&& StringUtil.isNotBlank(upperCondition[0]) && StringUtil.isNotBlank(upperCondition[1])) {
				upperName = upperCondition[0];
				upperOid = upperCondition[1];
			}
		}

		//デフォルト値(リセット値)の取得
		//初期設定では上位のみの指定は許可していないので考慮不要
		String defaultOid = "";
		String[] defaultValue = (String[]) request.getAttribute(Constants.EDITOR_DEFAULT_VALUE);
		if (defaultValue != null && defaultValue.length > 0) {
			Entity defaultEntity = loadReferenceEntity(rp.getObjectDefinitionName(), defaultValue[0]);
			if (defaultEntity != null) {
				defaultOid = defaultEntity.getOid();
			}
		}

		String _defName = (String) request.getAttribute(Constants.DEF_NAME);
		if (viewName == null) viewName = "";
		else viewName = StringUtil.escapeHtml(viewName);

		//連動コンボの生成はfunction.js
%>
<%-- XSS対応-メタの設定のため対応なし(searchType) --%>
<select name="<c:out value="<%=propName %>"/>" class="form-size-02 inpbr refCombo" style="<c:out value="<%=customStyle%>"/>"
 data-defName="<c:out value="<%=_defName %>"/>"
 data-viewName="<c:out value="<%=viewName %>"/>"
 data-propName="<c:out value="<%=pd.getName() %>"/>"
 data-viewType="<%=Constants.VIEW_TYPE_SEARCH %>"
 data-searchType="<%=searchType%>"
 data-prefix="sc_"
 data-webapiName="<%=ReferenceComboCommand.WEBAPI_NAME%>"
 data-getComboSettingWebapiName="<%=GetReferenceComboSettingCommand.WEBAPI_NAME %>"
 data-searchParentWebapiName="<%=SearchParentCommand.WEBAPI_NAME %>"
 data-oid="<c:out value="<%=currentOid%>"/>"
 data-upperName="<c:out value="<%=upperName%>" />"
 data-upperOid="<c:out value="<%=upperOid%>" />"
 data-norewrite="true"
 data-customStyle="<c:out value="<%=customStyle%>"/>">
</select>
<script type="text/javascript">
$(function() {
	<%-- common.js --%>
	addNormalConditionItemResetHandler(function(){
		<%-- 再ロード --%>
		$("select[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>") + "']").refCombo({
			reset:true,
			oid:"<%=StringUtil.escapeJavaScript(defaultOid)%>"
		});
	});
<%
		if (required) {
%>
	<%-- common.js --%>
	addNormalValidator(function() {
		var val = $("select[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>") + "']").val();
		if (typeof val === "undefined" || val == null || val == "") {
			alert(scriptContext.gem.locale.common.requiredMsg.replace("{0}", "<%=StringUtil.escapeJavaScript(displayLabel)%>"));
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
	} else if ((editor.getDisplayType() == ReferenceDisplayType.LINK && editor.isUseSearchDialog()) || (editor.getDisplayType() == ReferenceDisplayType.LABEL)) {
		String _defName = editor.getObjectName();
		String _viewName = editor.getViewName() != null ? editor.getViewName() : "";

		if (viewName == null) viewName = "";
		else viewName = StringUtil.escapeHtml(viewName);

		String contextPath = TemplateUtil.getTenantContextPath();
		String urlPath = ViewUtil.getParamMappingPath(_defName, _viewName);

		//選択ボタン
		String select = "";
		if (StringUtil.isNotBlank(editor.getSelectActionName())) {
			select = contextPath + "/" + editor.getSelectActionName() + urlPath;
		} else {
			select = contextPath + "/" + SearchViewCommand.SELECT_ACTION_NAME + urlPath;
		}

		//詳細リンクAction
		String viewAction = "";
		if (StringUtil.isNotBlank(editor.getViewrefActionName())) {
			viewAction = contextPath + "/" + editor.getViewrefActionName() + urlPath;
		} else {
			viewAction = contextPath + "/" + DetailViewCommand.REF_VIEW_ACTION_NAME + urlPath;
		}

		String ulId = "ul_" + propName;
%>
<ul id="<c:out value="<%=ulId %>"/>" data-deletable="true" class="mb05">
<%
		//デフォルト検索条件からリンク作成(searchCondMapがnullでない場合は設定されてこない)
		String[] propValue = (String[]) request.getAttribute(Constants.EDITOR_PROP_VALUE);
		if (propValue != null && propValue.length > 0) {
			for (int i = 0; i < propValue.length; i++) {
				String oid = propValue[i];
				//TODO serchEntityのINで一度に検索
				Entity entity = em.load(oid, _defName);
				if (entity == null || getDisplayPropLabel(editor, entity) == null) continue;
				String displayPropLabel = getDisplayPropLabel(editor, entity);
				String liId = "li_" + propName + i;
				String linkId = propName + "_" + entity.getOid();
				String key = entity.getOid() + "_" + entity.getVersion();

				String showReference = "showReference(" 
					+ "'" + StringUtil.escapeJavaScript(viewAction) + "'" 
					+ ", '" + StringUtil.escapeJavaScript(_defName) + "'"
					+ ", '" + StringUtil.escapeJavaScript(entity.getOid()) + "'"
					+ ", '" + entity.getVersion() + "'"
					+ ", '" + StringUtil.escapeJavaScript(linkId) + "'"
					+ ", false"
					+ ")";
%>
<li id="<c:out value="<%=liId %>"/>" class="list-add">
<a href="javascript:void(0)" class="modal-lnk" id="<c:out value="<%=linkId %>" />" data-linkId="<c:out value="<%=linkId %>"/>" style="<c:out value="<%=customStyle%>"/>"
 onclick="<c:out value="<%=showReference %>"/>"><c:out value="<%=displayPropLabel %>" /></a>
<%
				if (editor.getDisplayType() != ReferenceDisplayType.LABEL) {
					String deleteItem = "deleteItem(" 
						+ "'" + StringUtil.escapeJavaScript(liId) + "'" 
						+ ")";
%>
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_Edit.delete')}" class="gr-btn-02 del-btn" 
 onclick="<c:out value="<%=deleteItem %>"/>" />
<%
				}
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=key %>"/>"/>
</li>
<%
			}
		}
		//searchCondMapを解析してリンク作成
		String[] linkKv = ViewUtil.getSearchCondValue(searchCondMap, propName);
		if (linkKv != null && linkKv.length > 0) {
			for (int i = 0; i < linkKv.length; i++) {
				int index = linkKv[i].lastIndexOf("_");
				String oid = linkKv[i].substring(0, index);
				//TODO serchEntityのINで一度に検索
				Entity entity = em.load(oid,_defName);
				if (entity == null || getDisplayPropLabel(editor, entity) == null) continue;
				String displayPropLabel = getDisplayPropLabel(editor, entity);
				String liId = "li_" + propName + i;
				String linkId = propName + "_" + entity.getOid();
				String key = entity.getOid() + "_" + entity.getVersion();
				//hiddenにjavascriptで値上書きしないようにnorewrite属性をつけておく

				String showReference = "showReference(" 
					+ "'" + StringUtil.escapeJavaScript(viewAction) + "'" 
					+ ", '" + StringUtil.escapeJavaScript(_defName) + "'"
					+ ", '" + StringUtil.escapeJavaScript(entity.getOid()) + "'"
					+ ", '" + entity.getVersion() + "'"
					+ ", '" + StringUtil.escapeJavaScript(linkId) + "'"
					+ ", false"
					+ ")";
%>
<li id="<c:out value="<%=liId %>"/>" class="list-add">
<a href="javascript:void(0)" class="modal-lnk" id="<c:out value="<%=linkId %>" />" data-linkId="<c:out value="<%=linkId %>"/>" style="<c:out value="<%=customStyle%>"/>"
 onclick="<c:out value="<%=showReference %>"/>"><c:out value="<%=displayPropLabel %>" /></a>
<%
			if (editor.getDisplayType() != ReferenceDisplayType.LABEL) {
				String deleteItem = "deleteItem(" 
					+ "'" + StringUtil.escapeJavaScript(liId) + "'" 
					+ ")";
%>
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_Edit.delete')}" class="gr-btn-02 del-btn"
 onclick="<c:out value="<%=deleteItem %>"/>"/>
<%
			}
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=key %>"/>" data-norewrite="true"/>
</li>
<%
			}
		}
%>
</ul>
<%
		String selBtnId = "sel_btn_" + propName;

		EntityViewManager evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
		String selBtnUrlParam = evm.getUrlParameter(rootDefName, editor, null, UrlParameterActionType.SELECT);
		if (editor.getDisplayType() != ReferenceDisplayType.LABEL) {
%>
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_Edit.select')}" class="gr-btn-02 modal-btn" id="<c:out value="<%=selBtnId %>"/>" />
<script type="text/javascript">
$(function() {
	var dynamicParamCallback = function(urlParam) {
<%if (checkActionType(editor.getDynamicUrlParameterAction(), UrlParameterActionType.SELECT) && StringUtil.isNotBlank(editor.getDynamicUrlParameter())) {%>
<%=editor.getDynamicUrlParameter()%>
<%} else {%>
		return urlParam;
<%}%>
	};
	var params = {
		selectAction: "<%=StringUtil.escapeJavaScript(select) %>"
		, viewAction: "<%=StringUtil.escapeJavaScript(viewAction) %>"
		, defName: "<%=StringUtil.escapeJavaScript(_defName) %>"
		, propName: "<%=StringUtil.escapeJavaScript(propName) %>"
		, multiplicity: "-1"
		, urlParam: "<%=StringUtil.escapeJavaScript(selBtnUrlParam) %>"
		, refEdit: false
		, viewName: "<%=StringUtil.escapeJavaScript(_viewName) %>"
		, permitConditionSelectAll: <%=editor.isPermitConditionSelectAll()%>
		, permitVersionedSelect: false
		, parentDefName: "<%=StringUtil.escapeJavaScript(defName)%>"
		, parentViewName: "<%=StringUtil.escapeJavaScript(viewName)%>"
		, viewType: "<%=Constants.VIEW_TYPE_SEARCH %>"
		, customStyle: "<%=StringUtil.escapeJavaScript(customStyle)%>"
	}
	var $selBtn = $(":button[id='<%=StringUtil.escapeJavaScript(selBtnId)%>']");
	for (key in params) {
		$selBtn.attr("data-" + key, params[key]);
	}
	$selBtn.on("click", function() {
		searchReference(params.selectAction, params.viewAction, params.defName, params.propName, params.multiplicity, <%=isMultiple%>,
				 params.urlParam, params.refEdit, function(){}, null, params.viewName, params.permitConditionSelectAll, params.permitVersionedSelect,
				 params.parentDefName, params.parentViewName, params.viewType, null, null, null, null, dynamicParamCallback, params.customStyle);
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
				//TODO serchEntityのINで一度に検索
				Entity entity = em.load(oid, _defName);
				if (entity == null || getDisplayPropLabel(editor, entity) == null) continue;
				String displayPropLabel = getDisplayPropLabel(editor, entity);

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
			alert(scriptContext.gem.locale.common.requiredMsg.replace("{0}", "<%=StringUtil.escapeJavaScript(displayLabel)%>"));
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
		}
	} else if (editor.getDisplayType() == ReferenceDisplayType.TREE) {
		//詳細編集でのリンククリック
		String ulId = "ul_" + propName;

		String contextPath = TemplateUtil.getTenantContextPath();
		String urlPath = ViewUtil.getParamMappingPath(editor.getObjectName(), editor.getViewName());

		String viewAction = "";
		if (StringUtil.isNotBlank(editor.getViewrefActionName())) {
			viewAction = contextPath + "/" + editor.getViewrefActionName() + urlPath;
		} else {
			viewAction = contextPath + "/" + DetailViewCommand.REF_VIEW_ACTION_NAME + urlPath;
		}
%>
<ul id="<c:out value="<%=ulId %>"/>" data-deletable="true" class="mb05">
<%
		//デフォルト検索条件からリンク作成(searchCondMapがnullでない場合は設定されてこない)
		String[] propValue = (String[]) request.getAttribute(Constants.EDITOR_PROP_VALUE);
		if (propValue != null && propValue.length > 0) {
			for (int i = 0; i < propValue.length; i++) {
				String oid = propValue[i];
				//TODO serchEntityのINで一度に検索
				Entity entity = em.load(oid, rp.getObjectDefinitionName());
				if (entity == null || getDisplayPropLabel(editor, entity) == null) continue;
				String liId = "li_" + propName + i;
				String linkId = propName + "_" + entity.getOid();
				String key = entity.getOid() + "_" + entity.getVersion();
				String displayPropLabel = getDisplayPropLabel(editor, entity);

				String showReference = "showReference(" 
					+ "'" + StringUtil.escapeJavaScript(viewAction) + "'" 
					+ ", '" + StringUtil.escapeJavaScript(rp.getObjectDefinitionName()) + "'"
					+ ", '" + StringUtil.escapeJavaScript(entity.getOid()) + "'"
					+ ", '" + entity.getVersion() + "'"
					+ ", '" + StringUtil.escapeJavaScript(linkId) + "'"
					+ ", false"
					+ ")";
				String deleteItem = "deleteItem(" 
					+ "'" + StringUtil.escapeJavaScript(liId) + "'" 
					+ ")";
%>
<li id="<c:out value="<%=liId %>"/>" class="list-add">
<a href="javascript:void(0)" class="modal-lnk" id="<c:out value="<%=linkId %>" />" data-linkId="<c:out value="<%=linkId %>"/>" style="<c:out value="<%=customStyle%>"/>"
 onclick="<c:out value="<%=showReference %>"/>"><c:out value="<%=displayPropLabel %>" /></a>
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_Edit.delete')}" class="gr-btn-02 del-btn"
 onclick="<c:out value="<%=deleteItem %>"/>" />
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=key %>"/>"/>
</li>
<%
			}
		}
		//searchCondMapを解析してリンク作成
		String[] linkKv = ViewUtil.getSearchCondValue(searchCondMap, propName);
		if (linkKv != null && linkKv.length > 0) {
			for (int i = 0; i < linkKv.length; i++) {
				int index = linkKv[i].lastIndexOf("_");
				String oid = linkKv[i].substring(0, index);
				//TODO serchEntityのINで一度に検索
				Entity entity = em.load(oid, rp.getObjectDefinitionName());
				if (entity == null || getDisplayPropLabel(editor, entity) == null) continue;
				String liId = "li_" + propName + i;
				String linkId = propName + "_" + entity.getOid();
				String key = entity.getOid() + "_" + entity.getVersion();
				String displayPropLabel = getDisplayPropLabel(editor, entity);
				//hiddenにjavascriptで値上書きしないようにnorewrite属性をつけておく

				String showReference = "showReference(" 
					+ "'" + StringUtil.escapeJavaScript(viewAction) + "'" 
					+ ", '" + StringUtil.escapeJavaScript(rp.getObjectDefinitionName()) + "'"
					+ ", '" + StringUtil.escapeJavaScript(entity.getOid()) + "'"
					+ ", '" + entity.getVersion() + "'"
					+ ", '" + StringUtil.escapeJavaScript(linkId) + "'"
					+ ", false"
					+ ")";
				String deleteItem = "deleteItem(" 
					+ "'" + StringUtil.escapeJavaScript(liId) + "'" 
					+ ")";
%>
<li id="<c:out value="<%=liId %>"/>" class="list-add">
<a href="javascript:void(0)" class="modal-lnk" id="<c:out value="<%=linkId %>" />" data-linkId="<c:out value="<%=linkId %>"/>" style="<c:out value="<%=customStyle%>"/>"
 onclick="<c:out value="<%=showReference %>"/>"><c:out value="<%=displayPropLabel %>" /></a>
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_Edit.delete')}" class="gr-btn-02 del-btn"
 onclick="<c:out value="<%=deleteItem %>"/>"/>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=key %>"/>" data-norewrite="true"/>
</li>
<%
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
			PropertyEditor upperEditor = getLinkUpperPropertyEditor(rootDefName, viewName, editor.getLinkProperty(), nestPropertyName);
			upperType = getLinkUpperType(upperEditor);
		}
%>
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_Edit.select')}" class="gr-btn-02 recursiveTreeTrigger" id="<c:out value="<%=selBtnId %>"/>"
 data-defName="<c:out value="<%=rootDefName%>"/>"
 data-viewType="<%=Constants.VIEW_TYPE_SEARCH%>"
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
 data-viewAction="<c:out value="<%=viewAction%>"/>"
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
				//TODO serchEntityのINで一度に検索
				Entity entity = em.load(oid, rp.getObjectDefinitionName());
				if (entity == null || getDisplayPropLabel(editor, entity) == null) continue;

				String liId = StringUtil.escapeJavaScript("li_" + propName + i);
				String _viewAction = StringUtil.escapeJavaScript(viewAction);
				String _defName = StringUtil.escapeJavaScript(rp.getObjectDefinitionName());
				String key = StringUtil.escapeJavaScript(entity.getOid() + "_" + entity.getVersion());
				String label = StringUtil.escapeJavaScript(getDisplayPropLabel(editor, entity));
				String _propName = StringUtil.escapeJavaScript(propName);
%>
		<%-- common.js --%>
		addReference("<%=liId%>", "<%=_viewAction%>", "<%=_defName%>", "<%=key%>", "<%=label%>", "<%=_propName%>", "ul_<%=_propName%>", false);
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
			alert(scriptContext.gem.locale.common.requiredMsg.replace("{0}", "<%=StringUtil.escapeJavaScript(displayLabel)%>"));
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
	} else if (editor.getDisplayType() == ReferenceDisplayType.UNIQUE && isUniqueProp(editor) && editor.isUseSearchDialog()) {

		String _defName = editor.getObjectName();
		String _viewName = editor.getViewName() != null ? editor.getViewName() : "";

		if (viewName == null) viewName = "";
		else viewName = StringUtil.escapeHtml(viewName);

		String contextPath = TemplateUtil.getTenantContextPath();
		String urlPath = ViewUtil.getParamMappingPath(_defName, _viewName);

		//選択ボタン
		String select = "";
		if (StringUtil.isNotBlank(editor.getSelectActionName())) {
			select = contextPath + "/" + editor.getSelectActionName() + urlPath;
		} else {
			select = contextPath + "/" + SearchViewCommand.SELECT_ACTION_NAME + urlPath;
		}

		//詳細リンク
		String viewAction = "";
		if (StringUtil.isNotBlank(editor.getViewrefActionName())) {
			viewAction = contextPath + "/" + editor.getViewrefActionName() + urlPath;
		} else {
			viewAction = contextPath + "/" + DetailViewCommand.REF_VIEW_ACTION_NAME + urlPath;
		}

		String ulId = "ul_" + propName; 

		EntityViewManager evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
		String selBtnUrlParam = evm.getUrlParameter(rootDefName, editor, null, UrlParameterActionType.SELECT);
		String insBtnUrlParam = evm.getUrlParameter(rootDefName, editor, null, UrlParameterActionType.ADD);

		String selDynamicParamCallback = "selDynamicParamCallback_" + StringUtil.escapeJavaScript(propName);
		String insDynamicParamCallback = "insDynamicParamCallback_" + StringUtil.escapeJavaScript(propName);
%>
<script>
$(function() {
	var selDynamicParamCallback = function(urlParam) {
		<%if (checkActionType(editor.getDynamicUrlParameterAction(), UrlParameterActionType.SELECT) && StringUtil.isNotBlank(editor.getDynamicUrlParameter())) {%>
		<%=editor.getDynamicUrlParameter()%>
		<%} else {%>
		return urlParam;
		<%}%>
	}; 
	var selDynamicParamKey = "<%=selDynamicParamCallback%>";
	scriptContext[selDynamicParamKey] = selDynamicParamCallback;

	var insDynamicParamCallback = function(urlParam) {
		<%if (checkActionType(editor.getDynamicUrlParameterAction(), UrlParameterActionType.ADD) && StringUtil.isNotBlank(editor.getDynamicUrlParameter())) {%>
		<%=editor.getDynamicUrlParameter()%>
		<%} else {%>
		return urlParam;
		<%}%>
	}; 
	var insDynamicParamKey = "<%=insDynamicParamCallback%>";
	scriptContext[insDynamicParamKey] = insDynamicParamCallback;
});
</script>
<ul id="<c:out value="<%=ulId %>"/>" data-deletable="true" class="mb05">
<%
		int length = 0;
		//デフォルト検索条件からリンク作成(searchCondMapがnullでない場合は設定されてこない)
		String[] propValue = (String[]) request.getAttribute(Constants.EDITOR_PROP_VALUE);
		if (propValue != null && propValue.length > 0) {
			for (int i = 0; i < propValue.length; i++) {
				String oid = propValue[i];
				//TODO serchEntityのINで一度に検索
				Entity entity = em.load(oid, _defName);
				if (entity == null || getDisplayPropLabel(editor, entity) == null) continue;
				String displayPropLabel = getDisplayPropLabel(editor, entity);
				String uniquePropValue = getUniquePropValue(editor, entity);
				String liId = "li_" + propName + i;
				String linkId = propName + "_" + entity.getOid();
				String key = entity.getOid() + "_" + entity.getVersion();

				String showReference = "showReference(" 
					+ "'" + StringUtil.escapeJavaScript(viewAction) + "'" 
					+ ", '" + StringUtil.escapeJavaScript(_defName) + "'"
					+ ", '" + StringUtil.escapeJavaScript(entity.getOid()) + "'"
					+ ", '" + entity.getVersion() + "'"
					+ ", '" + StringUtil.escapeJavaScript(linkId) + "'"
					+ ", false"
					+ ")";
				String deleteItem = "deleteItem(" 
					+ "'" + StringUtil.escapeJavaScript(liId) + "'" 
					+ ")";
%>
<li id="<c:out value="<%=liId %>"/>" class="list-add unique-list refUnique"
 data-defName="<c:out value="<%=rootDefName%>"/>"
 data-viewType="<%=Constants.VIEW_TYPE_SEARCH%>"
 data-viewName="<c:out value="<%=viewName%>"/>"
 data-propName="<c:out value="<%=propName%>"/>"
 data-webapiName="<%=GetReferenceUniqueItemCommand.WEBAPI_NAME%>"
 data-selectAction="<c:out value="<%=select %>"/>"
 data-viewAction="<c:out value="<%=viewAction %>"/>"
 data-selectUrlParam="<c:out value="<%=selBtnUrlParam %>"/>"
 data-selectDynamicParamCallback="<c:out value="<%=selDynamicParamCallback%>"/>"
 data-insertUrlParam="<c:out value="<%=insBtnUrlParam %>"/>"
 data-insertDynamicParamCallback="<c:out value="<%=insDynamicParamCallback%>"/>"
 data-refDefName="<c:out value="<%=rp.getObjectDefinitionName()%>"/>"
 data-refViewName="<c:out value="<%=_viewName%>"/>"
 data-refEdit="false"
 data-permitConditionSelectAll="<c:out value="<%=editor.isPermitConditionSelectAll()%>"/>"
 data-permitVersionedSelect="false"
 data-multiplicity="-1"
 data-customStyle="<c:out value="<%=StringUtil.escapeJavaScript(customStyle)%>"/>"
>
<span class="unique-key">
<input type="text" id="uniq_txt_<c:out value="<%=liId%>"/>" style="<c:out value="<%=customStyle%>"/>" class="unique-form-size-01 inpbr" value="<c:out value="<%=uniquePropValue %>" />" />
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_Edit.select')}" class="gr-btn-02 modal-btn sel-btn" data-propName="<c:out value="<%=propName %>"/>" />
</span>
<span class="unique-ref">
<a href="javascript:void(0)" class="modal-lnk" id="<c:out value="<%=linkId %>" />" data-linkId="<c:out value="<%=linkId %>"/>" style="<c:out value="<%=customStyle%>"/>"
 onclick="<c:out value="<%=showReference %>"/>"><c:out value="<%=displayPropLabel %>"/></a>
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_Edit.delete')}" class="gr-btn-02 del-btn"
 onclick="<c:out value="<%=deleteItem %>"/>"/>
</span>
<input type="hidden" id="i_<c:out value="<%=liId%>"/>" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=key %>"/>"/>
</li>
<%
				length++;
			}
		}
		//searchCondMapを解析してリンク作成
		String[] linkKv = ViewUtil.getSearchCondValue(searchCondMap, propName);
		if (linkKv != null && linkKv.length > 0) {
			for (int i = 0; i < linkKv.length; i++) {
				int index = linkKv[i].lastIndexOf("_");
				String oid = linkKv[i].substring(0, index);
				//TODO serchEntityのINで一度に検索
				Entity entity = em.load(oid,_defName);
				if (entity == null || getDisplayPropLabel(editor, entity) == null) continue;
				String displayPropLabel = getDisplayPropLabel(editor, entity);
				String uniquePropValue = getUniquePropValue(editor, entity);
				String liId = "li_" + propName + i;
				String linkId = propName + "_" + entity.getOid();
				String key = entity.getOid() + "_" + entity.getVersion();
				//hiddenにjavascriptで値上書きしないようにnorewrite属性をつけておく

				String showReference = "showReference(" 
					+ "'" + StringUtil.escapeJavaScript(viewAction) + "'" 
					+ ", '" + StringUtil.escapeJavaScript(_defName) + "'"
					+ ", '" + StringUtil.escapeJavaScript(entity.getOid()) + "'"
					+ ", '" + entity.getVersion() + "'"
					+ ", '" + StringUtil.escapeJavaScript(linkId) + "'"
					+ ", false"
					+ ")";
				String deleteItem = "deleteItem(" 
					+ "'" + StringUtil.escapeJavaScript(liId) + "'" 
					+ ")";
%>
<li id="<c:out value="<%=liId %>"/>" class="list-add unique-list refUnique"
 data-defName="<c:out value="<%=rootDefName%>"/>"
 data-viewType="<%=Constants.VIEW_TYPE_SEARCH%>"
 data-viewName="<c:out value="<%=viewName%>"/>"
 data-propName="<c:out value="<%=propName%>"/>"
 data-webapiName="<%=GetReferenceUniqueItemCommand.WEBAPI_NAME%>"
 data-selectAction="<c:out value="<%=select %>"/>"
 data-viewAction="<c:out value="<%=viewAction %>"/>"
 data-selectUrlParam="<c:out value="<%=selBtnUrlParam %>"/>"
 data-selectDynamicParamCallback="<c:out value="<%=selDynamicParamCallback%>"/>"
 data-insertUrlParam="<c:out value="<%=insBtnUrlParam %>"/>"
 data-insertDynamicParamCallback="<c:out value="<%=insDynamicParamCallback%>"/>"
 data-refDefName="<c:out value="<%=rp.getObjectDefinitionName()%>"/>"
 data-refViewName="<c:out value="<%=_viewName%>"/>"
 data-refEdit="false"
 data-permitConditionSelectAll="<c:out value="<%=editor.isPermitConditionSelectAll()%>"/>"
 data-permitVersionedSelect="false"
 data-multiplicity="-1"
 data-customStyle="<c:out value="<%=StringUtil.escapeJavaScript(customStyle)%>"/>"
>
<span class="unique-key">
<input type="text" id="uniq_txt_<c:out value="<%=liId%>"/>" style="<c:out value="<%=customStyle%>"/>" class="unique-form-size-01 inpbr" value="<c:out value="<%=uniquePropValue %>" />" />
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_Edit.select')}" class="gr-btn-02 modal-btn sel-btn" data-propName="<c:out value="<%=propName %>"/>" />
</span>
<span class="unique-ref">
<a href="javascript:void(0)" class="modal-lnk" id="<c:out value="<%=linkId %>" />" data-linkId="<c:out value="<%=linkId %>"/>" style="<c:out value="<%=customStyle%>"/>"
 onclick="<c:out value="<%=showReference %>"/>"><c:out value="<%=displayPropLabel %>" /></a>
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_Edit.delete')}" class="gr-btn-02 del-btn"
 onclick="<c:out value="<%=deleteItem %>"/>"/>
</span>
<input type="hidden" id="i_<c:out value="<%=liId%>"/>" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=key %>"/>" data-norewrite="true" />
</li>
<%
				length++;
			}
		}

		if (length == 0) {
			String liId = "li_" + propName + "0";
%>
<li id="<c:out value="<%=liId %>"/>" class="list-add unique-list refUnique"
 data-defName="<c:out value="<%=rootDefName%>"/>"
 data-viewType="<%=Constants.VIEW_TYPE_SEARCH%>"
 data-viewName="<c:out value="<%=viewName%>"/>"
 data-propName="<c:out value="<%=propName%>"/>"
 data-webapiName="<%=GetReferenceUniqueItemCommand.WEBAPI_NAME%>"
 data-selectAction="<c:out value="<%=select %>"/>"
 data-viewAction="<c:out value="<%=viewAction %>"/>"
 data-selectUrlParam="<c:out value="<%=selBtnUrlParam %>"/>"
 data-selectDynamicParamCallback="<c:out value="<%=selDynamicParamCallback%>"/>"
 data-insertUrlParam="<c:out value="<%=insBtnUrlParam %>"/>"
 data-insertDynamicParamCallback="<c:out value="<%=insDynamicParamCallback%>"/>"
 data-refDefName="<c:out value="<%=rp.getObjectDefinitionName()%>"/>"
 data-refViewName="<c:out value="<%=_viewName%>"/>"
 data-refEdit="false"
 data-permitConditionSelectAll="<c:out value="<%=editor.isPermitConditionSelectAll()%>"/>"
 data-permitVersionedSelect="false"
 data-multiplicity="-1"
 data-customStyle="<c:out value="<%=StringUtil.escapeJavaScript(customStyle)%>"/>"
>
<span class="unique-key">
<input type="text" id="uniq_txt_<c:out value="<%=liId%>"/>" style="<c:out value="<%=customStyle%>"/>" class="unique-form-size-01 inpbr" value="" />
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_Edit.select')}" class="gr-btn-02 modal-btn sel-btn" data-propName="<c:out value="<%=propName%>"/>" />
</span>
<span class="unique-ref">
<a href="javascript:void(0)" class="modal-lnk" style="<c:out value="<%=customStyle%>"/>"></a>
<%
			if (isMultiple) {
				String deleteItem = "deleteItem(" 
					+ "'" + StringUtil.escapeJavaScript(liId) + "'" 
					+ ")";
%>
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_Edit.delete')}" class="gr-btn-02 del-btn"
 onclick="<c:out value="<%=deleteItem %>"/>"/>
<%
			} else {
%>
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_Edit.delete')}" class="gr-btn-02 del-btn"/>
<%
			}
%>
</span>
<input type="hidden" id="i_<c:out value="<%=liId%>"/>" name="<c:out value="<%=propName %>"/>" value=""/>
</li>
<%
			length++;
		}

		String dummyRowId = "id_li_" + propName + "Dummmy";
%>
<li id="<c:out value="<%=dummyRowId %>"/>" class="list-add unique-list" style="display: none;"
 data-defName="<c:out value="<%=rootDefName%>"/>"
 data-viewType="<%=Constants.VIEW_TYPE_SEARCH%>"
 data-viewName="<c:out value="<%=viewName%>"/>"
 data-propName="<c:out value="<%=propName%>"/>"
 data-webapiName="<%=GetReferenceUniqueItemCommand.WEBAPI_NAME%>"
 data-selectAction="<c:out value="<%=select %>"/>"
 data-viewAction="<c:out value="<%=viewAction %>"/>"
 data-selectUrlParam="<c:out value="<%=selBtnUrlParam %>"/>"
 data-selectDynamicParamCallback="<c:out value="<%=selDynamicParamCallback%>"/>"
 data-insertUrlParam="<c:out value="<%=insBtnUrlParam %>"/>"
 data-insertDynamicParamCallback="<c:out value="<%=insDynamicParamCallback%>"/>"
 data-refDefName="<c:out value="<%=rp.getObjectDefinitionName()%>"/>"
 data-refViewName="<c:out value="<%=_viewName%>"/>"
 data-refEdit="false"
 data-permitConditionSelectAll="<c:out value="<%=editor.isPermitConditionSelectAll()%>"/>"
 data-permitVersionedSelect="false"
 data-multiplicity="-1"
 data-customStyle="<c:out value="<%=StringUtil.escapeJavaScript(customStyle)%>"/>"
>
<span class="unique-key">
<input type="text" style="<c:out value="<%=customStyle%>"/>" class="unique-form-size-01 inpbr" />
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_Edit.select')}" class="gr-btn-02 modal-btn sel-btn" data-propName="<c:out value="<%=propName %>"/>" />
</span>
<span class="unique-ref">
<a href="javascript:void(0)" class="modal-lnk" style="<c:out value="<%=customStyle%>"/>"></a>
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_Edit.delete')}" class="gr-btn-02 del-btn" />
</span>
<input type="hidden"/>
</li>
</ul>
<%
		if (isMultiple) {
// 			String selBtnId = "sel_btn_" + propName;

			String addUniqueRefItem = "addUniqueRefItem(" 
				+ "'" + StringUtil.escapeJavaScript(ulId) + "'" 
				+ ", -1"
				+ ", '" + StringUtil.escapeJavaScript(dummyRowId) + "'"
				+ ", '" + StringUtil.escapeJavaScript(propName) + "'"
				+ ", 'id_count_" + StringUtil.escapeJavaScript(propName) + "'"
				+ ")";
%>
<%-- <input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_Edit.select')}" class="gr-btn-02 modal-btn" id="<c:out value="<%=selBtnId %>"/>" /> --%>
<input type="button" id="id_addBtn_<c:out value="<%=propName%>"/>" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_Edit.add')}" class="gr-btn-02 add-btn"
 onclick="<c:out value="<%=addUniqueRefItem %>"/>" />
<%
		}
%>
<input type="hidden" id="id_count_<c:out value="<%=propName%>"/>" value="<c:out value="<%=length%>"/>" />
<script type="text/javascript">
$(function() {
	<%-- common.js --%>
	addNormalConditionItemResetHandler(function(){
		<%-- 全部削除 (ダミ行は削除しない)--%>
		var $ul = $("#" + es("<%=StringUtil.escapeJavaScript(ulId)%>"));
		$ul.children("li:not(:hidden)").each(function(){
			$(this).remove();
		});

		var propName = "<%=StringUtil.escapeJavaScript(propName) %>";
		var _propName = propName.replace(/\[/g, "\\[").replace(/\]/g, "\\]").replace(/\./g, "\\.");
		var multiplicity = -1;
<%
		//デフォルトで設定されているものを追加
		String[] defaultValue = (String[]) request.getAttribute(Constants.EDITOR_DEFAULT_VALUE);
		if (defaultValue != null && defaultValue.length > 0) {

			for (int i = 0; i < defaultValue.length; i++) {
				String oid = defaultValue[i];
				//TODO serchEntityのINで一度に検索
				Entity entity = em.load(oid, _defName);
				if (entity == null || getDisplayPropLabel(editor, entity) == null) continue;
				String displayPropLabel = getDisplayPropLabel(editor, entity);
				String uniquePropValue = getUniquePropValue(editor, entity);

				String _viewAction = StringUtil.escapeJavaScript(viewAction);
				String label = StringUtil.escapeJavaScript(displayPropLabel);
				String key = StringUtil.escapeJavaScript(entity.getOid() + "_" + entity.getVersion());
				String unique = StringUtil.escapeJavaScript(uniquePropValue);
%>
		<%-- common.js --%>
		addUniqueReference("<%=_viewAction%>", "<%=key%>", "<%=label%>", "<%=unique%>", "<%=_defName%>", propName, multiplicity, "ul_" + _propName, "<%=dummyRowId%>", false, "id_count_" + _propName);
<%
			}
		} else {

			String _viewAction = StringUtil.escapeJavaScript(viewAction);
			String label = "";
			String key = "";
			String unique = "";
%>
		<%-- common.js --%>
		<%-- リセットした後に、最低でも1行を表示します。 --%>
		addUniqueReference("<%=_viewAction%>", "<%=key%>", "<%=label%>", "<%=unique%>", "<%=_defName%>", propName, multiplicity, "ul_" + _propName, "<%=dummyRowId%>", false, "id_count_" + _propName);
<%
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
			alert(scriptContext.gem.locale.common.requiredMsg.replace("{0}", "<%=StringUtil.escapeJavaScript(displayLabel)%>"));
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
	} else if (editor.getDisplayType() == ReferenceDisplayType.HIDDEN) {
		String _defName = editor.getObjectName();

		//デフォルト検索条件から作成(searchCondMapがnullでない場合は設定されてこない)
		String[] propValue = (String[]) request.getAttribute(Constants.EDITOR_PROP_VALUE);
		if (propValue != null && propValue.length > 0) {
			for (int i = 0; i < propValue.length; i++) {
				String oid = propValue[i];
				//TODO serchEntityのINで一度に検索
				Entity entity = em.load(oid, _defName);
				if (entity == null) continue;
				String key = entity.getOid() + "_" + entity.getVersion();
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=key %>"/>" data-norewrite="true"/>
<%
			}
		}
		//searchCondMapを解析してリンク作成
		String[] linkKv = ViewUtil.getSearchCondValue(searchCondMap, propName);
		if (linkKv != null && linkKv.length > 0) {
			for (int i = 0; i < linkKv.length; i++) {
				int index = linkKv[i].lastIndexOf("_");
				String oid = linkKv[i].substring(0, index);
				//TODO serchEntityのINで一度に検索
				Entity entity = em.load(oid,_defName);
				if (entity == null) continue;
				String key = entity.getOid() + "_" + entity.getVersion();
				//hiddenにjavascriptで値上書きしないようにnorewrite属性をつけておく
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=key %>"/>" data-norewrite="true"/>
<%
			}
		}

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
