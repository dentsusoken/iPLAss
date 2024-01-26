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

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" uri="http://iplass.org/tags/mtp"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.List"%>

<%@ page import="org.iplass.gem.command.Constants" %>
<%@ page import="org.iplass.gem.command.GemResourceBundleUtil"%>
<%@ page import="org.iplass.gem.command.ViewUtil"%>
<%@ page import="org.iplass.gem.command.generic.refcombo.GetReferenceComboSettingCommand"%>
<%@ page import="org.iplass.gem.command.generic.refcombo.ReferenceComboCommand"%>
<%@ page import="org.iplass.gem.command.generic.refcombo.ReferenceComboData"%>
<%@ page import="org.iplass.gem.command.generic.refcombo.SearchParentCommand"%>
<%@ page import="org.iplass.mtp.ManagerLocator"%>
<%@ page import="org.iplass.mtp.entity.Entity" %>
<%@ page import="org.iplass.mtp.entity.EntityManager"%>
<%@ page import="org.iplass.mtp.entity.definition.EntityDefinition"%>
<%@ page import="org.iplass.mtp.entity.definition.EntityDefinitionManager"%>
<%@ page import="org.iplass.mtp.entity.definition.properties.ReferenceProperty"%>
<%@ page import="org.iplass.mtp.entity.query.Query"%>
<%@ page import="org.iplass.mtp.entity.query.PreparedQuery"%>
<%@ page import="org.iplass.mtp.entity.query.SortSpec"%>
<%@ page import="org.iplass.mtp.entity.query.SortSpec.SortType"%>
<%@ page import="org.iplass.mtp.entity.query.condition.Condition"%>
<%@ page import="org.iplass.mtp.entity.query.condition.expr.And"%>
<%@ page import="org.iplass.mtp.entity.query.condition.predicate.Equals"%>
<%@ page import="org.iplass.mtp.util.StringUtil" %>
<%@ page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@ page import="org.iplass.mtp.view.generic.editor.*" %>
<%@ page import="org.iplass.mtp.view.generic.editor.ReferencePropertyEditor.RefSortType"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>

<%!
	private EntityDefinitionManager edm =  ManagerLocator.manager(EntityDefinitionManager.class);
	private EntityManager em = ManagerLocator.manager(EntityManager.class);

	/**
	 * コンボ情報の取得
	 *
	 * コンボ設定を解析して、表示コンボとその選択肢を取得します。
	 *
	 * @param editor ReferencePropertyEditor
	 * @param referenceProperty 参照プロパティ定義
	 * @param propertyName プロパティ名(フルパス)
	 * @param currentValue 最下層の選択値
	 * @return コンボ情報
	 */
	List<ReferenceComboData> getComboList(ReferencePropertyEditor editor, ReferenceProperty referenceProperty, 
			String propertyName, String currentValue) {

		List<ReferenceComboData> comboList = new ArrayList<>();
		
		if (editor.getReferenceComboSetting() == null) {
			return comboList;
		}
		ReferenceComboSetting setting = editor.getReferenceComboSetting();
		
		// 上位階層のデータ作成
		initParent(comboList, setting, referenceProperty, propertyName, currentValue);

		// 最下位層のデータ作成
		ReferenceComboData comboData = new ReferenceComboData(propertyName);
		comboData.setEntityName(referenceProperty.getObjectDefinitionName());
		comboData.setParentPropertyName(setting.getPropertyName());
		comboData.setCondition(editor.getCondition());
		comboData.setDisplayLabelItem(editor.getDisplayLabelItem());
		comboData.setSortItem(editor.getSortItem());
		comboData.setSortType(editor.getSortType());
		comboData.setCurrentValue(currentValue);
		comboList.add(comboData);

		// 選択値の取得
		for (int i = 0; i < comboList.size(); i++) {
			if (i == 0) {
				// 先頭
				searchOptionValues(comboList.get(i), null);
			} else if (StringUtil.isNotEmpty(comboList.get(i - 1).getCurrentValue())) {
				// 上位が選択されている場合
				searchOptionValues(comboList.get(i), comboList.get(i - 1).getCurrentValue());
			} else {
				// 選択値なし
				comboList.get(i).setOptionValues(Collections.emptyList());
			}
		}
			
		return comboList;
	}

	/**
	 * 上位階層データの取得
	 *
	 * 上位階層のコンボ生成のための情報を取得します。
	 *
	 * @param comboList コンボデータのリスト
	 * @param setting 連動コンボ設定
	 * @param currentProperty 子階層の参照プロパティ定義
	 * @param currentPath 子階層までのパス
	 * @param childOid 子階層の選択OID
	 */
	void initParent(List<ReferenceComboData> comboList, ReferenceComboSetting setting, 
			ReferenceProperty currentProperty, String currentPath, String childOid) {
		EntityDefinition currentEntityDefinition = edm.get(currentProperty.getObjectDefinitionName());
		ReferenceProperty parentProperty = (ReferenceProperty) currentEntityDefinition.getProperty(setting.getPropertyName());

		String parentPath = currentPath + "." + setting.getPropertyName();
		String parentOid = null;
		if (StringUtil.isEmpty(childOid)) {
			if (setting.getParent() != null && StringUtil.isNotEmpty(setting.getParent().getPropertyName())) {
				//更に上位がいれば先に生成
				initParent(comboList, setting.getParent(), parentProperty, parentPath, parentOid);
			}
			
		} else {
			// 子から親の選択値を検索
			Entity parentEntity = searchParent(currentEntityDefinition, parentProperty, childOid);
			parentOid = parentEntity != null ? parentEntity.getOid() : null;
			
			if (setting.getParent() != null && StringUtil.isNotEmpty(setting.getParent().getPropertyName())) {
				//更に上位がいれば先に生成
				initParent(comboList, setting.getParent(), parentProperty, parentPath, parentOid);
			}
		}
		
		ReferenceComboData comboData = new ReferenceComboData(parentPath);
		comboData.setEntityName(parentProperty.getObjectDefinitionName());
		if (setting.getParent() != null && StringUtil.isNotEmpty(setting.getParent().getPropertyName())) {
			comboData.setParentPropertyName(setting.getParent().getPropertyName());
		} 
		comboData.setCondition(setting.getCondition());
		comboData.setDisplayLabelItem(setting.getDisplayLabelItem());
		comboData.setSortItem(setting.getSortItem());
		comboData.setSortType(setting.getSortType());
		comboData.setCurrentValue(parentOid);
		comboList.add(comboData);
	}

	/**
	 * 上位階層の選択データの取得
	 *
	 * 子階層の選択値から親階層の選択値を取得します。
	 *
	 * @param currentEntityDefinition 子階層のEntity定義
	 * @param parentProperty 親の参照プロパティ定義
	 * @param childOid 子階層の選択OID
	 * @return 上位階層の選択データ
	 */
	Entity searchParent(EntityDefinition currentEntityDefinition, ReferenceProperty parentProperty, String childOid) {
		if (parentProperty != null) {
			//子階層のEntityデータを検索し、その親階層のデータを取得
			Query query = new Query()
					.select(parentProperty.getName() + "." + Entity.OID)
					.from(currentEntityDefinition.getName())
					.where(new Equals(Entity.OID, childOid));
			Entity ret = em.searchEntity(query).getFirst();
			if (ret != null && ret.getValue(parentProperty.getName()) != null) {
				//最初の項目をデフォルト選択させる
				Entity ref = ret.getValue(parentProperty.getName());
				return ref;
			}
		}
		return null;
	}
	
	/**
	 * コンボの選択オプション値の取得
	 * 
	 * 上位の選択値をもとに、オプション値を取得します。
	 * 
	 * @param comboData コンボ情報
	 * @param parentOid 親階層の選択OID
	 */
	void searchOptionValues(ReferenceComboData comboData, String parentOid) {

		Query query = new Query();
		query.select(Entity.OID);
		if (comboData.getDisplayLabelItem() != null) {
			// 表示ラベルとして扱うプロパティ
			query.select().add(comboData.getDisplayLabelItem());
		} else {
			query.select().add(Entity.NAME);
		}
		query.from(comboData.getEntityName());
		
		List<Condition> conditions = new ArrayList<>();
		// デフォルト条件
		if (comboData.getCondition() != null) {
			conditions.add(new PreparedQuery(comboData.getCondition()).condition(null));
		}
		// 上位選択条件
		if (StringUtil.isNotEmpty(parentOid)) {
			conditions.add(new Equals(comboData.getParentPropertyName() + "." + Entity.OID, parentOid));
		}
		
		if (!conditions.isEmpty()) {
			And and = new And(conditions);
			query.where(and);
		}

		String sortItem = comboData.getSortItem();
		if (sortItem == null || sortItem.isEmpty()) {
			sortItem = Entity.OID;
		}
		SortType sortType = null;
		if (comboData.getSortType() == null || comboData.getSortType() == RefSortType.ASC) {
			sortType = SortType.ASC;
		} else {
			sortType = SortType.DESC;
		}
		query.order(new SortSpec(sortItem, sortType));

		final List<Entity> optionValues = new ArrayList<>();
		em.searchEntity(query, (entity) -> {
			if (comboData.getDisplayLabelItem() != null) {
				// 表示ラベルとして扱うプロパティをNameに設定
				entity.setName(entity.getValue(comboData.getDisplayLabelItem()));
			}
			optionValues.add(entity);
			return true;
		});
		comboData.setOptionValues(optionValues);
	}
%>
<%
	ReferencePropertyEditor editor = (ReferencePropertyEditor) request.getAttribute(Constants.EDITOR_EDITOR);

	if (editor.getReferenceComboSetting() == null) {
		return;
	}
	ReferenceComboSetting setting = editor.getReferenceComboSetting();
	
	Entity entity = request.getAttribute(Constants.ENTITY_DATA) instanceof Entity ? (Entity) request.getAttribute(Constants.ENTITY_DATA) : null;
	Object propValue = request.getAttribute(Constants.EDITOR_PROP_VALUE);
	ReferenceProperty pd = (ReferenceProperty) request.getAttribute(Constants.EDITOR_PROPERTY_DEFINITION);

	String rootDefName = (String)request.getAttribute(Constants.ROOT_DEF_NAME);
	String scriptKey = (String)request.getAttribute(Constants.SECTION_SCRIPT_KEY);

	String viewName = StringUtil.escapeHtml((String)request.getAttribute(Constants.VIEW_NAME), true);

	Entity rootEntity = (Entity) request.getAttribute(Constants.ROOT_ENTITY);
	String rootOid = rootEntity != null ? rootEntity.getOid() : "";
	String rootVersion = rootEntity != null && rootEntity.getVersion() != null ? rootEntity.getVersion().toString() : "";

	Boolean nest = (Boolean) request.getAttribute(Constants.EDITOR_REF_NEST);
	if (nest == null) nest = false;

	// NestTable参照元プロパティ名
	String nestPropertyName = "";
	if (nest) {
		nestPropertyName = (String)request.getAttribute(Constants.EDITOR_REF_NEST_PROP_NAME) + ".";
	}

	// プロパティ名(フルパス)
	String propertyName = nestPropertyName + pd.getName();

	//カスタムスタイル
	String customStyle = "";
	if (StringUtil.isNotEmpty(editor.getInputCustomStyle())) {
		customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getInputCustomStyleScriptKey(), entity, propValue);
	}

	String pleaseSelectLabel = "";
	if (ViewUtil.isShowPulldownPleaseSelectLabel()) {
		pleaseSelectLabel = GemResourceBundleUtil.resourceString("generic.editor.reference.ReferencePropertyEditor_Edit.pleaseSelect");
	}

	if (pd.getMultiplicity() == 1) {
		Entity refEntity = propValue instanceof Entity ? (Entity) propValue : null;
		String oid = refEntity != null ? refEntity.getOid() != null ? refEntity.getOid() : "" : "";
		
		// コンボ情報取得
		List<ReferenceComboData> comboList = getComboList(editor, pd, propertyName, oid);
		
		for (int i = 0; i < comboList.size(); i++) {
			ReferenceComboData comboData = comboList.get(i);
			if (i != comboList.size() - 1) {
				// 上位コンボ
%>
<select name="<c:out value="<%=comboData.getPropertyPath()%>"/>" 
  class="form-size-02 inpbr" style="<c:out value="<%=customStyle%>"/>" 
  data-norewrite="true"
>
<%
			} else {
				// 最下層コンボ
%>
<select name="${m:esc(editor.propertyName)}" class="form-size-02 inpbr ref-combo-sync" style="<c:out value="<%=customStyle%>"/>"
 data-defName="<c:out value="<%=rootDefName%>"/>"
 data-viewName="<%=viewName %>" 
 data-propName="<c:out value="<%=propertyName%>"/>" 
 data-viewType="<%=Constants.VIEW_TYPE_DETAIL %>" 
 data-entityOid="<%=StringUtil.escapeJavaScript(rootOid)%>" 
 data-entityVersion="<%=StringUtil.escapeJavaScript(rootVersion)%>"
 data-webapiName="<%=ReferenceComboCommand.WEBAPI_NAME%>"
>
<%
			}
			
			// 選択肢の出力
%>
<option value=""><%= pleaseSelectLabel %></option>
<%
			for (Entity optionValue : comboData.getOptionValues()) {
				String selected = optionValue.getOid().equals(comboData.getCurrentValue()) ? " selected" : "";
%>
<option value="<c:out value="<%=optionValue.getOid() %>"/>" <c:out value="<%=selected %>"/>>
<c:out value="<%=optionValue.getName() %>" />
</option>
<%
			}
%>
</select>
<%
			if (i != comboList.size() - 1) {
				// 上位コンボ
%>
&gt; 
<%
			}
		}
	} else {
		String ulId = "ul_" + editor.getPropertyName();
		int length = 0;

		//テンプレート行
		String dummyRowId = "id_li_" + editor.getPropertyName() + "Dummmy";
%>
<ul id="<c:out value="<%=ulId %>"/>" class="mb05">
<li id="<c:out value="<%=dummyRowId %>"/>" style="display: none;">
<select class="form-size-02 inpbr" style="<c:out value="<%=customStyle%>"/>"
 data-defName="<c:out value="<%=rootDefName%>"/>"
 data-viewName="<%=viewName %>" 
 data-propName="<c:out value="<%=propertyName%>"/>" 
 data-viewType="<%=Constants.VIEW_TYPE_DETAIL %>" 
 data-entityOid="<%=StringUtil.escapeJavaScript(rootOid)%>" 
 data-entityVersion="<%=StringUtil.escapeJavaScript(rootVersion)%>"
 data-webapiName="<%=ReferenceComboCommand.WEBAPI_NAME%>"
 data-getComboSettingWebapiName="<%=GetReferenceComboSettingCommand.WEBAPI_NAME %>" 
 data-searchParentWebapiName="<%=SearchParentCommand.WEBAPI_NAME %>"
 data-prefix="" 
 data-searchType="NONE" 
 data-oid=""
 data-upperName="" 
 data-upperOid="" 
 data-customStyle="<c:out value="<%=customStyle%>"/>">
</select>
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_RefCombo.delete')}" class="gr-btn-02 del-btn" />
</li>
<%
		//データ出力
		Entity[] array = propValue instanceof Entity[] ? (Entity[]) propValue : null;
		if (array != null) {
			length = array.length;
			for (int i = 0; i < array.length; i++) {
				String oid = array[i] != null ? array[i].getOid() != null ? array[i].getOid() : "" : "";
				String liId = "li_" + editor.getPropertyName() + i;

				String deleteItem = "deleteItem(" 
					+ "'" + StringUtil.escapeJavaScript(liId) + "'" 
					+ ")";
%>
<li id="<c:out value="<%=liId %>"/>">
<select name="${m:esc(editor.propertyName)}" class="form-size-02 inpbr refCombo" style="<c:out value="<%=customStyle%>"/>"
 data-defName="<c:out value="<%=rootDefName%>"/>"
 data-viewName="<%=viewName %>" 
 data-propName="<c:out value="<%=propertyName%>"/>"
 data-viewType="<%=Constants.VIEW_TYPE_DETAIL %>" 
 data-entityOid="<%=StringUtil.escapeJavaScript(rootOid)%>" 
 data-entityVersion="<%=StringUtil.escapeJavaScript(rootVersion)%>"
 data-webapiName="<%=ReferenceComboCommand.WEBAPI_NAME%>"
 data-getComboSettingWebapiName="<%=GetReferenceComboSettingCommand.WEBAPI_NAME %>" 
 data-searchParentWebapiName="<%=SearchParentCommand.WEBAPI_NAME %>"
 data-prefix="" 
 data-searchType="NONE" 
 data-oid="<c:out value="<%=oid%>"/>"
 data-upperName="" 
 data-upperOid="" 
 data-customStyle="<c:out value="<%=customStyle%>"/>">
</select>
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_RefCombo.delete')}" class="gr-btn-02 del-btn"
 onclick="<c:out value="<%=deleteItem %>"/>" />
</li>
<%
			}
		}
%>
</ul>
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_RefCombo.add')}" class="gr-btn-02 add-btn refComboController" 
 data-multiplicity="${propertyDefinition.multiplicity}" 
 data-ulId="<c:out value="<%=ulId %>"/>" 
 data-dummyId="<c:out value="<%=dummyRowId %>"/>" 
 data-propName="${m:esc(editor.propertyName)}" />
<%
	}
%>
