/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 *
 * Unless you have purchased a commercial license,
 * the following license terms apply:
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.iplass.gem.command.fulltext;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.ServletException;

import org.iplass.gem.command.Constants;
import org.iplass.gem.command.CreateSearchResultUtil;
import org.iplass.gem.command.GemResourceBundleUtil;
import org.iplass.gem.command.ViewUtil;
import org.iplass.gem.command.fulltext.FullTextSearchResult.ColModel;
import org.iplass.gem.command.generic.detail.DetailViewCommand;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.SystemException;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.auth.User;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.entity.fulltextsearch.FulltextSearchManager;
import org.iplass.mtp.entity.fulltextsearch.FulltextSearchRuntimeException;
import org.iplass.mtp.entity.permission.EntityPermission;
import org.iplass.mtp.entity.query.OrderBy;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.SortSpec.NullOrderingSpec;
import org.iplass.mtp.entity.query.SortSpec.SortType;
import org.iplass.mtp.entity.query.condition.predicate.In;
import org.iplass.mtp.util.CollectionUtil;
import org.iplass.mtp.util.DateUtil;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.EntityView;
import org.iplass.mtp.view.generic.EntityViewManager;
import org.iplass.mtp.view.generic.EntityViewUtil;
import org.iplass.mtp.view.generic.FormViewUtil;
import org.iplass.mtp.view.generic.NullOrderType;
import org.iplass.mtp.view.generic.OutputType;
import org.iplass.mtp.view.generic.SearchFormView;
import org.iplass.mtp.view.generic.editor.DateRangePropertyEditor;
import org.iplass.mtp.view.generic.editor.JoinPropertyEditor;
import org.iplass.mtp.view.generic.editor.NestProperty;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor;
import org.iplass.mtp.view.generic.editor.UserPropertyEditor;
import org.iplass.mtp.view.generic.element.property.PropertyColumn;
import org.iplass.mtp.view.generic.element.section.SearchConditionSection;
import org.iplass.mtp.view.generic.element.section.SearchConditionSection.ConditionSortType;
import org.iplass.mtp.view.generic.element.section.SearchResultSection;
import org.iplass.mtp.view.generic.element.section.SortSetting;
import org.iplass.mtp.view.top.TopViewDefinition;
import org.iplass.mtp.view.top.TopViewDefinitionManager;
import org.iplass.mtp.view.top.parts.FulltextSearchViewParts;
import org.iplass.mtp.view.top.parts.TopViewParts;
import org.iplass.mtp.web.template.TemplateUtil;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.RequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebApi(
		name=FullTextSearchCommand.SEARCH_WEB_API_NAME,
		displayName="全文検索",
		accepts = {RequestType.REST_FORM},
		methods = MethodType.POST,
		results={
			Constants.MESSAGE, Constants.DATA,
			"searchDefName", "fulltextKey", Constants.SEARCH_COND},
		checkXRequestedWithHeader=true
	)
@CommandClass(name="gem/fulltext/FullTextSearchCommand", displayName="全文検索")
public final class FullTextSearchCommand implements Command {

	public static final String SEARCH_WEB_API_NAME = "gem/fulltext/search";
	public static final String SEARCH_VIEW_ACTION_NAME = "gem/fulltext/searchview";

	private static Logger logger = LoggerFactory.getLogger(FullTextSearchCommand.class);

	@Override
	public String execute(RequestContext request) {
		// ロールからsectionを取得
		String roleName = (String) request.getSession().getAttribute(Constants.ROLE_NAME);

		String fulltextKey = request.getParam("fulltextKey");
		if (StringUtil.isNotEmpty(fulltextKey)) {
			fulltextKey = fulltextKey.replaceAll("　"," ");
		} else {
			request.setAttribute(Constants.MESSAGE, getRS("research"));
			return Constants.CMD_EXEC_FAILURE;
		}

		String[] searchDefNames =request.getParams("searchDefName");
		final List<String> defNameList = getSelectedDefNameList(searchDefNames);
		final Map<String, String> sortKeyMap = getSelectedSortKeyMap(searchDefNames, request);
		final Map<String, String> sortTypeMap = getSelectedSortTypeMap(searchDefNames, request);

		//詳細画面に遷移した時のための検索条件保持
		StringBuilder searchCond = new StringBuilder();
		//fulltextKeyには&が含まれる可能性があるため、解析を楽にするため最後に追加
		if (!defNameList.isEmpty()) {
			defNameList.stream()
				.map(defName -> "&searchDefName=" + defName)
				.forEach(param -> searchCond.append(param));
			searchCond.deleteCharAt(0);
		}
		searchCond.append("&fulltextKey=" + fulltextKey);
		request.setAttribute(Constants.SEARCH_COND, searchCond.toString());

		//表示画面の条件設定用
		request.setAttribute("fulltextKey", fulltextKey);
		if (!defNameList.isEmpty()) {
			request.setAttribute("searchDefName", defNameList);
		}

		// Entityごとの検索条件情報取得
		List<EntitySearchInfo> searchCondList = getEntitySearchInfo(roleName, defNameList, sortKeyMap, sortTypeMap);

		//ソート条件
		Map<String, OrderBy> orderByMap = getOrderByMap(searchCondList);

		//Entity毎の検索対象プロパティ情報を取得
		Map<String, List<String>> entityProperties = searchCondList.stream()
			.collect(Collectors.toMap(EntitySearchInfo::getDefinitionName, info -> info.getProperties()));

		//検索処理
		EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);
		List<Entity> searchResult = null;
		try {
			searchResult = em.fulltextSearchEntity(entityProperties, fulltextKey, orderByMap).getList();
		} catch (FulltextSearchRuntimeException e) {
			logger.error("fulltext search error.", e);
			request.setAttribute(Constants.MESSAGE, getRS("validString"));
			return Constants.CMD_EXEC_FAILURE;
		}

		//検索結果チェック
		if (CollectionUtil.isEmpty(searchResult)) {
			request.setAttribute(Constants.MESSAGE, getRS("nodata"));
			return Constants.CMD_EXEC_FAILURE;
		}

		FulltextSearchManager fsm = ManagerLocator.getInstance().getManager(FulltextSearchManager.class);
		int maxRow = fsm.getMaxRows();
		boolean isThrowExceptionWhenOverLimit = fsm.isThrowExceptionWhenOverLimit();
		if (maxRow <= searchResult.size()) {
			if (isThrowExceptionWhenOverLimit) {
				request.setAttribute(Constants.MESSAGE, getRS("maxRows", maxRow));
				return Constants.CMD_EXEC_FAILURE;
			} else {
				request.setAttribute(Constants.MESSAGE, getRS("overLimit", maxRow));
			}
		}

		//Crawl時間を取得
		Map<String, Timestamp> crawlDateMap = fsm.getLastCrawlTimestamp(
				entityProperties.keySet().toArray(new String[0]));

		//検索結果をentity定義ごとに整理
		Map<String, List<Entity>> entityMap = searchResult.parallelStream()
				.collect(Collectors.groupingBy(Entity :: getDefinitionName));

		//レスポンス生成のための情報(EntityResultInfo)を取得
		AuthContext auth = AuthContext.getCurrentContext();
		EntityDefinitionManager edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
		List<EntityResultInfo> resultList = searchCondList.stream()
				.map(search -> {
					//検索結果をセット
					search.setSearchResult(entityMap.get(search.getDefinitionName()));
					//Crawl日時をセット
					search.setCrawlDate(crawlDateMap.get(search.getDefinitionName()));
					return search;
				})
				.filter(search -> search.getSearchResult() != null)	//結果があるもののみ
				.map(search -> {
					//返す処理で必要な情報を取得
					return getResultInfo(search, edm, auth);
				})
				.collect(Collectors.toList());

		//UserEditorが設定されている場合はUser情報を検索
		//(UserPropertyEditor.jspにて、ここから値を取得する)
		Set<String> userOidSet = resultList.stream()
			.map(res -> res.getUserOid())  //EntityごとのUserOidを取得
			.flatMap(oidSet -> oidSet.stream())
			.collect(Collectors.toSet());
		if (!userOidSet.isEmpty()) {
			//User名の検索
			Map<String, Entity> userInfoMap = getUserInfoMap(em, userOidSet);
			request.setAttribute(Constants.USER_INFO_MAP, userInfoMap);
		}

		//レスポンス情報の生成
		List<FullTextSearchResult> resultData = resultList.stream()
				.sorted(Comparator.comparing(EntityResultInfo :: getDefinitionName))  //定義名でソート
				.map(res -> res.toFullTextSearchResult())	//FullTextSearchResultに変換
				.collect(Collectors.toList());
		request.setAttribute(Constants.DATA, resultData);

		return Constants.CMD_EXEC_SUCCESS;
	}

	private List<String> getSelectedDefNameList(String[] searchDefNames) {

		if (searchDefNames != null) {
			return Arrays.stream(searchDefNames)
					.filter(defName -> StringUtil.isNotEmpty(defName))	//ブランク除去
					.collect(Collectors.toList());
		} else {
			return Collections.emptyList();
		}
	}

	private Map<String, String> getSelectedSortKeyMap(String[] searchDefNames, RequestContext request) {

		if (searchDefNames != null) {
			return Arrays.stream(searchDefNames)
					.filter(defName -> StringUtil.isNotEmpty(defName) //ブランク除去
							&& StringUtil.isNotEmpty(request.getParam("sortKey_" + defName)))
					.collect(Collectors.toMap(defName -> defName, defName -> request.getParam("sortKey_" + defName)));
		} else {
			return Collections.emptyMap();
		} 
	}

	private Map<String, String> getSelectedSortTypeMap(String[] searchDefNames, RequestContext request) {

		if (searchDefNames != null) {
			return Arrays.stream(searchDefNames)
					.filter(defName -> StringUtil.isNotEmpty(defName) //ブランク除去
							&& StringUtil.isNotBlank(request.getParam("sortType_" + defName)))
					.collect(Collectors.toMap(defName -> defName, defName -> request.getParam("sortType_" + defName)));
		} else {
			return Collections.emptyMap();
		} 
	}

	/**
	 * 検索対象のEntityとそのPropertyを取得
	 *
	 * @param roleName ロール名
	 * @param selectedDefNameList 選択Entity名のリスト
	 * @param sortKeyMap エンティティ名とソート項目名のマップ
	 * @param sortTypeMap エンティティ名と並べ順のマップ
	 * @return 検索対象Entity情報
	 */
	private List<EntitySearchInfo> getEntitySearchInfo(String roleName, List<String> selectedDefNameList, Map<String, String> sortKeyMap, Map<String, String> sortTypeMap) {

		//FulltextSearchViewPartsの取得
		FulltextSearchViewParts parts = getTopViewParts(roleName);

		EntityViewManager evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
		EntityDefinitionManager edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);

		if (parts == null){
			//Partsが未設定の場合は、選択Entityを対象にする
			//選択Entityがない場合はEntity内のCrawl情報をすべて対象とする
			List<String> defList = selectedDefNameList.isEmpty() ? edm.definitionList() : selectedDefNameList;

			return defList.stream()
				.map(defName -> edm.get(defName))	//EntityDefinitionにする
				.filter(ed -> ed != null && ed.isCrawl())	//検索対象のみ
				.map(ed -> {
					//画面からのソート項目
					String sortKey = sortKeyMap.get(ed.getName());
					String sortType = sortTypeMap.get(ed.getName());
					return getSearchInfo(ed, null, evm, edm, sortKey, sortType); // Partsがないため、Viewはデフォルト
				})	
				.filter(i -> i != null)
				.collect(Collectors.toList());

		} else {
			//Partsで指定されている表示対象Entity情報を取得
			Map<String, Boolean> isShowEntities = parts.getDispEntities();

			if (isShowEntities != null) {

				//Partsで指定されているView名を取得
				Map<String, String> viewNames = parts.getViewNames();

				//表示対象のみView名マッピング情報を保持
				return isShowEntities.entrySet().stream()
					.filter(p -> p.getValue())	//表示対象のみ
					.filter(p -> selectedDefNameList.isEmpty() || selectedDefNameList.contains(p.getKey()))	//検索対象のみ
					.map(p -> edm.get(p.getKey()))		//EntityDefinitionにする
					.filter(ed -> ed != null && ed.isCrawl())	//検索対象のみ
					.map(ed -> {
						//View名を取得
						String viewName = null;
						if (viewNames != null) {
							viewName = viewNames.get(ed.getName());
						}
						//画面からのソート項目
						String sortKey = sortKeyMap.get(ed.getName());
						String sortType = sortTypeMap.get(ed.getName());
						//EntitySearchInfoを取得
						return getSearchInfo(ed, viewName, evm, edm, sortKey, sortType);
					})
					.filter(i -> i != null)
					.collect(Collectors.toList());
			}
		}
		return Collections.emptyList();
	}

	private Map<String, OrderBy> getOrderByMap(List<EntitySearchInfo> searchCondList) {
		Map<String, OrderBy> orderByMap = new HashMap<>();
		searchCondList.stream().forEach(info -> {
			List<SortSetting> sortSettings = getSortSetting(info);
			EntityDefinition ed = info.getEntityDefinition();
			OrderBy orderBy = getOrderBy(ed, sortSettings);
			if (orderBy != null) {
				orderByMap.put(ed.getName(), orderBy);
			}
		});
		return orderByMap;
	}

	private FulltextSearchViewParts getTopViewParts(String roleName) {

		if (roleName == null){
			roleName = "DEFAULT";
		}

		TopViewDefinitionManager tvdm = ManagerLocator.getInstance().getManager(TopViewDefinitionManager.class);
		TopViewDefinition definition = tvdm.get(roleName);

		if (definition != null && definition.getParts() != null) {
			Optional<TopViewParts> fulltextParts = definition.getParts().stream()
					.filter(part -> part instanceof FulltextSearchViewParts).findFirst();
			if (fulltextParts.isPresent()) {
				return (FulltextSearchViewParts)fulltextParts.get();
			}
		}
		return null;
	}

	private EntitySearchInfo getSearchInfo(EntityDefinition ed, String viewName, EntityViewManager evm, EntityDefinitionManager edm, String sortKey, String sortType) {

		EntitySearchInfo search = new EntitySearchInfo(ed);

		search.setEntityDefinition(ed);

		//EntityView取得
		String defName = ed.getName();
		EntityView view = evm.get(defName);

		//SearchFormView取得
		SearchFormView searchFormView = FormViewUtil.getSearchFormView(ed, view, viewName);
		if (searchFormView == null) {
			logger.warn("Entity [" + defName + "] is not defined SearchView [" + viewName + "], skip fulltext search target.");
			return null;
		}

		search.setSearchFormView(searchFormView);
//		search.setDetailViewName(detailViewName);
		search.setDetailViewName(viewName);
		search.setSortKey(sortKey);
		search.setSortType(sortType);

		//SearchResultSection
		SearchResultSection resultSection = searchFormView.getResultSection();

		List<String> select = new ArrayList<String>();
		select.add(Entity.OID);
		select.add(Entity.VERSION);

		List<PropertyColumn> properties = resultSection.getElements().stream()
				.filter(e -> e instanceof PropertyColumn).map(e -> (PropertyColumn) e)
				.collect(Collectors.toList());

		for (PropertyColumn p : properties) {
			if (EntityViewUtil.isDisplayElement(ed.getName(), p.getElementRuntimeId(), OutputType.SEARCHRESULT, null)) {
				String propName = p.getPropertyName();
				if (p.getEditor() instanceof ReferencePropertyEditor) {
					List<NestProperty> nest = ((ReferencePropertyEditor)p.getEditor()).getNestProperties();
					addSearchProperty(select, ed, propName, p.getEditor(), nest.toArray(new NestProperty[nest.size()]));
				} else if (p.getEditor() instanceof JoinPropertyEditor) {
					addSearchProperty(select, ed, propName, p.getEditor());
					JoinPropertyEditor je = (JoinPropertyEditor) p.getEditor();
					for (NestProperty nest : je.getProperties()) {
						addSearchProperty(select, ed, nest.getPropertyName(), nest.getEditor());
					}
				} else if (p.getEditor() instanceof DateRangePropertyEditor) {
					addSearchProperty(select, ed, propName, p.getEditor());
					DateRangePropertyEditor de = (DateRangePropertyEditor) p.getEditor();
					if (StringUtil.isNotBlank(de.getToPropertyName())) {
						addSearchProperty(select, ed, de.getToPropertyName(), de.getToEditor());
					}
				} else {
					addSearchProperty(select, ed, propName, p.getEditor());
				}
			}
		}
		search.setProperties(select);

		return search;
	}

	private void addSearchProperty(List<String> select, EntityDefinition ed, String propName, PropertyEditor editor, NestProperty... nest) {
		PropertyDefinition pd = EntityViewUtil.getPropertyDefinition(propName, ed);
		if (pd instanceof ReferenceProperty) {
			if (!select.contains(propName + "." + Entity.NAME)) {
				select.add(propName + "." + Entity.NAME);
			}
			if (!select.contains(propName + "." + Entity.OID)) {
				select.add(propName + "." + Entity.OID);
			}
			if (!select.contains(propName + "." + Entity.VERSION)) {
				select.add(propName + "." + Entity.VERSION);
			}
			if (editor instanceof ReferencePropertyEditor) {
				addDisplayLabelProperty(select, ed, propName, (ReferencePropertyEditor) editor);
			}
			if (nest != null && nest.length > 0) {
				EntityDefinition red = getReferenceEntityDefinition((ReferenceProperty) pd);
				for (NestProperty np : nest) {
					PropertyDefinition rpd = red.getProperty(np.getPropertyName());
					if (rpd != null
							&&!Entity.OID.equals(np.getPropertyName())
							&& !Entity.NAME.equals(np.getPropertyName())
							&& !Entity.VERSION.equals(np.getPropertyName())) {
						String nestPropName = propName + "." + np.getPropertyName();
						if (rpd instanceof ReferenceProperty) {
							if (!select.contains(nestPropName + "." + Entity.NAME)) {
								select.add(nestPropName + "." + Entity.NAME);
							}
							if (!select.contains(nestPropName + "." + Entity.OID)) {
								select.add(nestPropName + "." + Entity.OID);
							}
							if (!select.contains(nestPropName + "." + Entity.VERSION)) {
								select.add(nestPropName + "." + Entity.VERSION);
							}
						} else {
							if (!select.contains(nestPropName)) {
								select.add(nestPropName);
							}
						}
						if (np.getEditor() instanceof ReferencePropertyEditor) {
							ReferencePropertyEditor rpe = (ReferencePropertyEditor) np.getEditor();
							if (!rpe.getNestProperties().isEmpty()) {
								List<NestProperty> _nest = rpe.getNestProperties();
								addSearchProperty(select, ed, nestPropName, rpe, _nest.toArray(new NestProperty[_nest.size()]));
							}
							addDisplayLabelProperty(select, ed, nestPropName, rpe);
						} else if (np.getEditor() instanceof JoinPropertyEditor) {
							JoinPropertyEditor je = (JoinPropertyEditor) np.getEditor();
							addSearchProperty(select, ed, nestPropName, je.getEditor());
							if (!je.getProperties().isEmpty()) {
								List<NestProperty> _nest = je.getProperties();
								addSearchProperty(select, ed, propName, editor, _nest.toArray(new NestProperty[_nest.size()]));
							}
						}
					}
				}
			}
//		} else if(pd instanceof BinaryProperty) {
			//検索から外しておく
		} else {
			if (!select.contains(propName)) select.add(propName);
		}
	}

	protected void addDisplayLabelProperty(List<String> select, EntityDefinition ed, String propName, ReferencePropertyEditor rpe) {
		if (rpe.getDisplayLabelItem() == null) return;

		PropertyDefinition pd = EntityViewUtil.getPropertyDefinition(propName, ed);
		if (pd instanceof ReferenceProperty) {
			if (!select.contains(propName + "." + rpe.getDisplayLabelItem())) {
				select.add(propName + "." + rpe.getDisplayLabelItem());
			}
		}
	}

	/**
	 * 参照型プロパティのEntity定義を取得します。
	 * @param rp
	 * @return
	 */
	private EntityDefinition getReferenceEntityDefinition(ReferenceProperty rp) {
		EntityDefinitionManager edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
		return edm.get(rp.getObjectDefinitionName());
	}

	protected SearchConditionSection getConditionSection(SearchFormView form) {
		return form != null ? form.getCondSection() : null;
	}
	
	protected List<SortSetting> getSortSetting(EntitySearchInfo info) {
		List<SortSetting> setting = new ArrayList<>();

		//画面でソート条件が指定されれば第1キーに
		String sortKey = info.getSortKey();
		if (StringUtil.isNotBlank(sortKey)) {
			PropertyDefinition pd = EntityViewUtil.getPropertyDefinition(sortKey, info.getEntityDefinition());
			if (pd != null) {
				// 有効なプロパティのみ対象にする
				SortSetting ss = new SortSetting();
				ss.setSortKey(sortKey);
				String sortType = info.getSortType();
				if (StringUtil.isBlank(sortType)) {
					ss.setSortType(ConditionSortType.DESC);
				} else {
					ss.setSortType(ConditionSortType.valueOf(sortType));
				}
				SearchResultSection section = info.getSearchFormView().getResultSection();
				PropertyColumn property = getLayoutPropertyColumn(sortKey, section);
				if (property != null) {
					ss.setNullOrderType(property.getNullOrderType());
					setting.add(ss);
				}
			}
		}

		SearchConditionSection section = info.getSearchFormView().getCondSection();
		if (section != null && !section.isFulltextSearchUnsorted() && !section.getSortSetting().isEmpty()) {
			setting.addAll(section.getSortSetting());
		}
		return setting;
	}

	protected PropertyColumn getLayoutPropertyColumn(String propName, SearchResultSection section) {
		Optional<PropertyColumn> property = section.getElements().stream()
				.filter(e -> e instanceof PropertyColumn).map(e -> (PropertyColumn) e)
				.filter(e -> propName.equals(e.getPropertyName())).findFirst();
		if (property.isPresent()) {
			return property.get();
		}
		return null;
	}

	private OrderBy getOrderBy(EntityDefinition ed, List<SortSetting> setting) {
		OrderBy orderBy = null;
		if (setting != null && !setting.isEmpty()) {
			for (SortSetting ss : setting) {
				if (ss.getSortKey() != null) {
					String key = null;
					PropertyDefinition pd = EntityViewUtil.getPropertyDefinition(ss.getSortKey(), ed);
					if (pd instanceof ReferenceProperty) {
						key = ss.getSortKey() + "." + Entity.OID;
					} else {
						key = ss.getSortKey();
					}
					SortType type = SortType.valueOf(ss.getSortType().name());
					NullOrderingSpec nullOrderingSpec = getNullOrderingSpec(ss.getNullOrderType());
					if (orderBy == null) orderBy = new OrderBy();
					orderBy.add(key, type, nullOrderingSpec);
				}
			}
		}
		return orderBy;
	}

	private EntityResultInfo getResultInfo(EntitySearchInfo serchCond, EntityDefinitionManager edm, AuthContext auth) {

		EntityResultInfo result = new EntityResultInfo(serchCond);

		//Form情報
		SearchFormView formView = serchCond.getSearchFormView();

		String mappingPath = ViewUtil.getParamMappingPath(serchCond.getDefinitionName(), serchCond.getDetailViewName());

		//詳細アクション
		if (StringUtil.isNotEmpty(formView.getViewActionName())) {
			result.setViewUrl(formView.getViewActionName() + mappingPath);
		} else {
			result.setViewUrl(DetailViewCommand.VIEW_ACTION_NAME + mappingPath);
		}

		//編集アクション
		if (StringUtil.isNotEmpty(formView.getEditActionName())) {
			result.setDetailUrl(formView.getEditActionName() + mappingPath);
		} else {
			result.setDetailUrl(DetailViewCommand.DETAIL_ACTION_NAME + mappingPath);
		}

		//displayName
		result.setDisplayName(TemplateUtil.getMultilingualString(
				formView.getTitle(),
				formView.getLocalizedTitleList(),
				serchCond.getEntityDefinition().getDisplayName(),
				serchCond.getEntityDefinition().getLocalizedDisplayNameList()));

		//SearchResultSection
		SearchResultSection resultSection = formView.getResultSection();

		//編集リンク表示
		boolean canUpdate = auth.checkPermission(new EntityPermission(serchCond.getDefinitionName(), EntityPermission.Action.UPDATE));
		boolean canDelete = auth.checkPermission(new EntityPermission(serchCond.getDefinitionName(), EntityPermission.Action.DELETE));
		boolean isHideDetailLink = resultSection.isHideDetailLink();
		result.setShowDetailLink(!isHideDetailLink && (canUpdate || canDelete));

		//ColModelを作成
		createColModel(result, resultSection, serchCond.getEntityDefinition(), edm);

		return result;
	}

	private void createColModel(EntityResultInfo result, SearchResultSection resultSection, EntityDefinition ed, EntityDefinitionManager edm) {

		List<ColModel> colModels = new ArrayList<ColModel>();
		List<String> userPropertyNames = new ArrayList<String>();

		Integer fixedCount = 0;

		ColModel colModel = new ColModel("orgOid", "oid");
		colModel.setHidden(true);
		colModel.setFrozen(true);
		colModels.add(colModel);

		colModel = new ColModel("orgVersion", "version");
		colModel.setHidden(true);
		colModel.setFrozen(true);
		colModels.add(colModel);

		colModel = new ColModel("score", "score");
		colModel.setHidden(true);
		colModel.setFrozen(true);
		colModels.add(colModel);

		colModel = new ColModel("_mtpDetailLink", "");
		colModel.setFrozen(true);
		colModel.setWidth(Integer.parseInt(getRS("detailLinkWidth")));
		colModel.setAlign("center");
		colModel.setClasses("detail-links");
		colModels.add(colModel);

		List<PropertyColumn> properties = resultSection.getElements().stream()
				.filter(e -> e instanceof PropertyColumn).map(e -> (PropertyColumn) e)
				.collect(Collectors.toList());

		for (PropertyColumn property : properties) {
			String propName = property.getPropertyName();
			PropertyDefinition pd = EntityViewUtil.getPropertyDefinition(propName, ed);

			String displayLabel = TemplateUtil.getMultilingualString(
					property.getDisplayLabel(), property.getLocalizedDisplayLabelList(),
					pd.getDisplayName(), pd.getLocalizedDisplayNameList());

			if (EntityViewUtil.isDisplayElement(ed.getName(), property.getElementRuntimeId(), OutputType.SEARCHRESULT, null)) {
				if (!(pd instanceof ReferenceProperty)) {
					String sortPropName = StringUtil.escapeHtml(propName);
					boolean frozen = false;
					Integer width = property.getWidth() > 0 ? property.getWidth() : null;
					String align = property.getTextAlign() != null ? property.getTextAlign().name().toLowerCase() : null;
					String style = property.getStyle() != null ? property.getStyle() : "";

					//UserPropertyEditorのチェック
					if (property.getEditor() instanceof UserPropertyEditor) {
						userPropertyNames.add(propName);
					}

					colModel = new ColModel(sortPropName, displayLabel);
					colModel.setFrozen(frozen);
					colModel.setWidth(width);
					colModel.setAlign(align);
					colModel.setClasses(style);
					colModel.setSortable(true);
					colModels.add(colModel);

				} else if (property.getEditor() instanceof ReferencePropertyEditor) {
					List<NestProperty> nest = ((ReferencePropertyEditor) property.getEditor()).getNestProperties();
					if (nest.isEmpty()) {
						//参照型のNameを表示する場合
						String sortPropName = StringUtil.escapeHtml(propName);
						boolean frozen = false;
						Integer width = property.getWidth() > 0 ? property.getWidth() : null;
						String align = property.getTextAlign() != null ? property.getTextAlign().name().toLowerCase() : null;
						String style = property.getStyle() != null ? property.getStyle() : null;

						colModel = new ColModel(sortPropName, displayLabel);
						colModel.setFrozen(frozen);
						colModel.setWidth(width);
						colModel.setAlign(align);
						colModel.setClasses(style);
						colModel.setSortable(true);
						colModels.add(colModel);
					} else {
						//参照型のName以外を表示する場合

						String style = property.getStyle() != null ? property.getStyle() : null;

						fixedCount = createNestColModel(colModels, userPropertyNames,
								nest, propName, (ReferenceProperty)pd, style, fixedCount, edm);
					}
				}
			}
		}

		//ColModel
		result.setColModel(colModels);
		//UserPropertyName
		result.setUserPropertyName(userPropertyNames);

	}

	private int createNestColModel(List<ColModel> colModels, List<String> userPropertyNames,
			List<NestProperty> nest, String propName, ReferenceProperty rp, String style,
			int fixedCount, EntityDefinitionManager edm) {

		EntityDefinition red = edm.get(rp.getObjectDefinitionName());
		int colCount = 0;
		for (NestProperty np : nest) {
			PropertyDefinition rpd = red.getProperty(np.getPropertyName());
			if (np.getEditor() != null) {
				String nestPropName = propName + "." + np.getPropertyName();
				String nestStyle = StringUtil.isNotEmpty(style) ? style + "_col" + colCount++ : null;
				if (rpd instanceof ReferenceProperty
					&& np.getEditor() instanceof ReferencePropertyEditor
					&& !((ReferencePropertyEditor) np.getEditor()).getNestProperties().isEmpty()) {

					//再Nest
					fixedCount = createNestColModel(colModels, userPropertyNames,
							((ReferencePropertyEditor)np.getEditor()).getNestProperties(),
							nestPropName, (ReferenceProperty)rpd, nestStyle, fixedCount, edm);
				} else {
					String sortPropName = StringUtil.escapeHtml(nestPropName);
					String displayLabel = TemplateUtil.getMultilingualString(
							np.getDisplayLabel(), np.getLocalizedDisplayLabelList(),
							rpd.getDisplayName(), rpd.getLocalizedDisplayNameList());
					boolean frozen = false;
					Integer width = np.getWidth() > 0 ? np.getWidth() : null;
					String align = np.getTextAlign() != null ? np.getTextAlign().name().toLowerCase() : null;

					ColModel colModel = new ColModel(sortPropName, displayLabel);
					colModel.setFrozen(frozen);
					colModel.setWidth(width);
					colModel.setAlign(align);
					colModel.setClasses(nestStyle);
					colModels.add(colModel);

					//UserPropertyEditorのチェック
					if (np.getEditor() instanceof UserPropertyEditor) {
						userPropertyNames.add(nestPropName);
					}

				}
			}
		}
		return fixedCount;
	}

	private Map<String, Entity> getUserInfoMap(EntityManager em, final Set<String> userOidSet) {

		Query q = new Query().select(Entity.OID, Entity.NAME)
							 .from(User.DEFINITION_NAME)
							 .where(new In(Entity.OID, userOidSet.toArray()));

		return em.searchEntity(q).getList().stream()
				.collect(Collectors.toMap(Entity :: getOid, entity -> entity));
	}

	protected NullOrderingSpec getNullOrderingSpec(NullOrderType type) {
		if (type == null) return null;
		switch (type) {
		case FIRST:
			return NullOrderingSpec.FIRST;
		case LAST:
			return NullOrderingSpec.LAST;
		default:
			break;
		}
		return null;
	}

	private String getRS(String key, Object... args) {
		return resourceString("command.fulltext.FullTextSearchCommand." + key, args);
	}

	/**
	 * 検索条件を生成する際に必要な情報
	 */
	private class EntitySearchInfo {

		/** EntityDefinition */
		private EntityDefinition ed;

		/** DetailFormView名 */
		private String detailViewName;

		/** SearchFormView */
		private SearchFormView searchFormView;

		/** 検索対象Property */
		private List<String> properties;

		/**	検索結果 */
		private List<Entity> searchResult;

		/**	Crawl日時 */
		private Timestamp crawlDate;

		/** ソート項目（画面）*/
		private String sortKey;

		/** ソートタイプ（画面）*/
		private String sortType;

		public EntitySearchInfo(EntityDefinition ed) {
			setEntityDefinition(ed);
		}

		public EntityDefinition getEntityDefinition() {
			return ed;
		}

		public void setEntityDefinition(EntityDefinition ed) {
			this.ed = ed;
		}

		public String getDefinitionName() {
			return ed.getName();
		}

		public SearchFormView getSearchFormView() {
			return searchFormView;
		}

		public void setSearchFormView(SearchFormView searchFormView) {
			this.searchFormView = searchFormView;
		}

		public String getDetailViewName() {
			return detailViewName;
		}

		public void setDetailViewName(String detailViewName) {
			this.detailViewName = detailViewName;
		}

		public List<String> getProperties() {
			return properties;
		}

		public void setProperties(List<String> properties) {
			this.properties = properties;
		}

		public List<Entity> getSearchResult() {
			return searchResult;
		}

		public void setSearchResult(List<Entity> searchResult) {
			this.searchResult = searchResult;
		}

		public Timestamp getCrawlDate() {
			return crawlDate;
		}

		public void setCrawlDate(Timestamp crawlDate) {
			this.crawlDate = crawlDate;
		}

		public String getSortKey() {
			return sortKey;
		}

		public void setSortKey(String sortKey) {
			this.sortKey = sortKey;
		}

		public String getSortType() {
			return sortType;
		}

		public void setSortType(String sortType) {
			this.sortType = sortType;
		}

	}

	/**
	 * 検索結果を処理する際に必要となる情報
	 */
	private class EntityResultInfo {

		/** 検索情報 */
		private EntitySearchInfo searchInfo;

		/** 表示名(タイトル) */
		private String displayName;

		/** 詳細リンクURL */
		private String viewUrl;
		/** 編集リンクURL */
		private String detailUrl;
		/** 編集リンク表示 */
		private boolean showDetailLink;

		/** 検索結果ColModel */
		private List<ColModel> colModel;

		/** UserPropertyEditorが指定されたPropertyName */
		private List<String> userPropertyName;

		public EntityResultInfo(EntitySearchInfo view) {
			this.searchInfo = view;
		}

		public String getDefinitionName() {
			return searchInfo.getDefinitionName();
		}

		public String getDisplayName() {
			return displayName;
		}

		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}

		public String getViewUrl() {
			return viewUrl;
		}

		public void setViewUrl(String viewUrl) {
			this.viewUrl = viewUrl;
		}

		public String getDetailUrl() {
			return detailUrl;
		}

		public void setDetailUrl(String detailUrl) {
			this.detailUrl = detailUrl;
		}

		public boolean isShowDetailLink() {
			return showDetailLink;
		}

		public void setShowDetailLink(boolean showDetailLink) {
			this.showDetailLink = showDetailLink;
		}

		public List<ColModel> getColModel() {
			return colModel;
		}

		public void setColModel(List<ColModel> colModel) {
			this.colModel = colModel;
		}

		public List<String> getUserPropertyName() {
			return userPropertyName;
		}

		public void setUserPropertyName(List<String> userPropertyName) {
			this.userPropertyName = userPropertyName;
		}

		/**
		 * 検索結果に含まれるUserPropertyEditorが設定された値(UserのOID値)を返す。
		 *
		 * @return UserのOID値
		 */
		public Set<String> getUserOid() {
			if (getUserPropertyName() == null) {
				return Collections.emptySet();
			}

			return searchInfo.getSearchResult().stream()
				.map(entity -> {
					//検索結果から値を取得
					return getUserPropertyName().stream()
							.map(propName -> (String)entity.getValue(propName))
							.collect(Collectors.toSet());
				})
				.flatMap(oidSet -> oidSet.stream())
				.filter(oid -> oid != null)
				.collect(Collectors.toSet());
		}

		/**
		 * レスポンスとして返すFullTextSearchResultを返す。
		 *
		 * @return FullTextSearchResult
		 */
		public FullTextSearchResult toFullTextSearchResult() {
			FullTextSearchResult result = new FullTextSearchResult();

			result.setDefName(getDefinitionName());
			result.setDisplayName(getDisplayName());
			result.setViewAction(getViewUrl());
			result.setDetailAction(getDetailUrl());
			result.setShowDetailLink(isShowDetailLink());

			String crawlDate = "-";
			if (searchInfo.getCrawlDate() != null) {
				DateFormat format = DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getOutputDatetimeSecFormat(), true);
				crawlDate = format.format(searchInfo.getCrawlDate());
			}
			result.setCrawlDate(resourceString("fulltext.search.crawlDate", crawlDate));

			result.setColModels(getColModel());

			try {
				result.setValues(CreateSearchResultUtil.getHtmlData(
						searchInfo.getSearchResult(), searchInfo.getEntityDefinition(),
						searchInfo.getSearchFormView().getResultSection(),
						searchInfo.getSearchFormView().getName()));
			} catch (IOException e) {
				throw new SystemException(e);
			} catch (ServletException e) {
				throw new SystemException(e);
			}
			return result;
		}

	}

	private static String resourceString(String key, Object... arguments) {
		return GemResourceBundleUtil.resourceString(key, arguments);
	}
}
