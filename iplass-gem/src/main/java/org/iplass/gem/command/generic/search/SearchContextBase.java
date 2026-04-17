/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.gem.command.generic.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.iplass.gem.command.CommandUtil;
import org.iplass.gem.command.Constants;
import org.iplass.gem.command.GemResourceBundleUtil;
import org.iplass.gem.command.ViewUtil;
import org.iplass.gem.command.common.SearchResultData;
import org.iplass.gem.command.generic.search.handler.CheckPermissionLimitConditionOfEditLinkHandler;
import org.iplass.gem.command.generic.search.handler.CreateSearchResultEvent;
import org.iplass.gem.command.generic.search.handler.CreateSearchResultEventHandler;
import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.SystemException;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.VersionControlType;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.entity.query.AsOf;
import org.iplass.mtp.entity.query.From;
import org.iplass.mtp.entity.query.Limit;
import org.iplass.mtp.entity.query.OrderBy;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.Select;
import org.iplass.mtp.entity.query.SortSpec;
import org.iplass.mtp.entity.query.SortSpec.NullOrderingSpec;
import org.iplass.mtp.entity.query.SortSpec.SortType;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.value.primary.EntityField;
import org.iplass.mtp.entity.query.value.primary.Literal;
import org.iplass.mtp.impl.parser.ParseContext;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.parser.SyntaxService;
import org.iplass.mtp.impl.query.OrderBySyntax;
import org.iplass.mtp.impl.query.QuerySyntaxRegister;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.utilityclass.definition.UtilityClassDefinitionManager;
import org.iplass.mtp.view.filter.EntityFilterItem;
import org.iplass.mtp.view.generic.EntityView;
import org.iplass.mtp.view.generic.EntityViewManager;
import org.iplass.mtp.view.generic.EntityViewUtil;
import org.iplass.mtp.view.generic.FormViewUtil;
import org.iplass.mtp.view.generic.NullOrderType;
import org.iplass.mtp.view.generic.OutputType;
import org.iplass.mtp.view.generic.SearchFormView;
import org.iplass.mtp.view.generic.SearchFormViewHandler;
import org.iplass.mtp.view.generic.SearchQueryContext;
import org.iplass.mtp.view.generic.SearchQueryInterrupter;
import org.iplass.mtp.view.generic.SearchQueryInterrupter.SearchQueryType;
import org.iplass.mtp.view.generic.editor.JoinPropertyEditor;
import org.iplass.mtp.view.generic.editor.NestProperty;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.RangePropertyEditor;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor;
import org.iplass.mtp.view.generic.editor.UserPropertyEditor;
import org.iplass.mtp.view.generic.element.property.PropertyColumn;
import org.iplass.mtp.view.generic.element.property.PropertyItem;
import org.iplass.mtp.view.generic.element.section.SearchConditionSection;
import org.iplass.mtp.view.generic.element.section.SearchResultSection;
import org.iplass.mtp.view.generic.element.section.SearchResultSection.ExclusiveControlPoint;
import org.iplass.mtp.view.generic.element.section.SortSetting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: Metadata定義クラス（Entity/EntityView/Form/...）が持つべきロジックがこのクラスに漏れ出している（カプセル化ができない）
public abstract class SearchContextBase implements SearchContext, CreateSearchResultEventHandler {

	private static Logger log = LoggerFactory.getLogger(SearchContextBase.class);

	private RequestContext request;
	private EntityDefinition definition;
	private EntityView view;
	private SearchFormView form;
	private Set<String> useUserPropertyEditorPropertyNameList;
	private SearchQueryInterrupterHandler interrupterHandler;
	private List<SearchFormViewHandler> searchFormViewHandlers;

	private EntityViewManager evm;
	private UtilityClassDefinitionManager ucdm;

	public SearchContextBase() {
		super();

		evm = ManagerLocator.manager(EntityViewManager.class);
		ucdm = ManagerLocator.manager(UtilityClassDefinitionManager.class);
	}

	@Override
	public RequestContext getRequest() {
		return request;
	}

	@Override
	public void setRequest(RequestContext request) {
		this.request = request;
	}

	@Override
	public EntityDefinition getEntityDefinition() {
		return definition;
	}

	@Override
	public void setEntityDefinition(EntityDefinition definition) {
		this.definition = definition;
	}

	@Override
	public EntityView getEntityView() {
		return view;
	}

	@Override
	public void setEntityView(EntityView view) {
		this.view = view;
	}

	@Override
	public String getDefName() {
		return request.getParam(Constants.DEF_NAME);
	}

	@Override
	public Select getSelect() {
		ArrayList<String> select = new ArrayList<>();
		select.add(Entity.OID);
		select.add(Entity.NAME);
		select.add(Entity.VERSION);

		if (getResultSection().getExclusiveControlPoint() == ExclusiveControlPoint.WHEN_SEARCH) {
			select.add(Entity.UPDATE_DATE);
		}

		List<PropertyColumn> properties = getColumnProperties();
		for (PropertyColumn p : properties) {
			if (EntityViewUtil.isDisplayElement(getDefName(), p.getElementRuntimeId(), OutputType.SEARCHRESULT, null)) {
				String propName = p.getPropertyName();
				if (p.getEditor() instanceof ReferencePropertyEditor) {
					List<NestProperty> nest = ((ReferencePropertyEditor) p.getEditor()).getNestProperties();
					addSearchProperty(select, propName, p.getEditor(), nest.toArray(new NestProperty[nest.size()]));
				} else if (p.getEditor() instanceof JoinPropertyEditor) {
					JoinPropertyEditor je = (JoinPropertyEditor) p.getEditor();
					addSearchProperty(select, propName, je.getEditor());
					for (NestProperty nest : je.getProperties()) {
						addSearchProperty(select, nest.getPropertyName(), nest.getEditor());
					}
				} else if (p.getEditor() instanceof RangePropertyEditor) {
					addSearchProperty(select, propName);
					RangePropertyEditor re = (RangePropertyEditor) p.getEditor();
					if (StringUtil.isNotBlank(re.getToPropertyName())) {
						addSearchProperty(select, re.getToPropertyName());
					}
				} else {
					addSearchProperty(select, propName);
				}
			}
		}
		// ソート条件のデータを取得カラムにしておかないと、DistinctでSQLエラーになる。
		OrderBy orderBy = getOrderBy();
		if (orderBy != null) {
			for (SortSpec sortSpec : orderBy.getSortSpecList()) {
				String sortKey = sortSpec.getSortKey()
						.toString();
				if (!select.contains(sortKey))
					addSearchProperty(select, sortKey);
			}
		}
		boolean distinct = getConditionSection().isDistinct();
		Select s = new Select().add(select.toArray());
		s.setDistinct(distinct);
		return s;
	}

	@Override
	public From getFrom() {
		From from = new From(getDefName());

		//バージョン管理されてるEntityでリクエストパラメータに対象とするバージョンがあるか確認
		Object specValue = null;
		String paramSpec = getRequest().getParam(Constants.SEARCH_SPEC_VERSION);
		if (definition.getVersionControlType() != null && StringUtil.isNotBlank(paramSpec)) {
			if (VersionControlType.TIMEBASE.equals(definition.getVersionControlType())
					|| VersionControlType.SIMPLE_TIMEBASE.equals(definition.getVersionControlType())) {
				//日時
				specValue = CommandUtil.getTimestamp(paramSpec);
			} else if (VersionControlType.VERSIONED.equals(definition.getVersionControlType())
					|| VersionControlType.STATEBASE.equals(definition.getVersionControlType())) {
				//数値
				specValue = CommandUtil.getLong(paramSpec);
			}
		}

		//リテラル化してAsOfに設定
		if (specValue != null) {
			AsOf asof = new AsOf(new Literal(specValue));
			from.setAsOf(asof);
		}

		return from;
	}

	@Override
	public OrderBy getOrderBy() {
		Optional<String> requestSortKey = getRequestSortKey();
		SearchConditionSection conditionSection = getConditionSection();

		return getOrderBy(requestSortKey, Optional.empty(), Optional.of(conditionSection), new SortSpec(Entity.OID, SortType.DESC)).orElse(null);
	}

	@Override
	public Limit getLimit() {
		Limit limit = new Limit(getSearchLimit(), getOffset());
		return limit;
	}

	@Override
	public boolean isVersioned() {
		if (getEntityDefinition().getVersionControlType() != VersionControlType.NONE) {
			String allVer = getRequest().getParam(Constants.SEARCH_ALL_VERSION);
			return "1".equals(allVer);
		} else if (getForm().isCanVersionedReferenceSearchForNoneVersionedEntity()) {
			String referenceVer = getRequest().getParam(Constants.SEARCH_SAVED_VERSION);
			return "1".equals(referenceVer);
		}
		return false;
	}

	@Override
	public boolean isSearch() {
		String isSearch = getRequest().getParam("isSearch");
		return "true".equals(isSearch);
	}

	@Override
	public boolean isCount() {
		String isCount = getRequest().getParam("isCount");
		return "true".equals(isCount);
	}

	@Override
	public boolean validation() {
		return true;
	}

	/**
	 * リクエスト値と設定値から、OrderByを決定します。
	 */
	protected final Optional<OrderBy> getOrderBy(Optional<String> requestSortKey, Optional<EntityFilterItem> filter,
			Optional<SearchConditionSection> conditionSection, SortSpec defaultSortSpec) {
		if (requestSortKey.isEmpty() && filter.isEmpty() && (conditionSection.isPresent() && conditionSection.get()
				.getSortSetting()
				.isEmpty()
				&& conditionSection.get()
						.isUnsorted())) {
			return Optional.empty();
		}

		List<SortSpec> settingSortSpecs = filter.map(f -> getOrderBy(f).map(OrderBy::getSortSpecList)
				.orElse(Collections.emptyList()))
				.orElseGet(() -> conditionSection.map(c -> c.getSortSetting()
						.stream()
						.map(this::getSettingSortSpec)
						.toList())
						.orElseGet(() -> Collections.emptyList()));

		List<SortSpec> additionalSortSpecs = settingSortSpecs.isEmpty() ? List.of(defaultSortSpec) : settingSortSpecs;

		OrderBy orderBy = new OrderBy();
		Stream.concat(requestSortKey.map(this::getRequestSortSpec)
				.stream(), additionalSortSpecs.stream())
				.forEach(orderBy::add);

		return Optional.of(orderBy);
	}

	/**
	 * ソート設定からソート条件を取得します。
	 */
	private SortSpec getSettingSortSpec(SortSetting ss) {
		String sortKey = ss.getSortKey();
		EntityField field = switch (getPropertyDefinition(sortKey)) {
		case ReferenceProperty ref -> {
			// ソートキーに参照プロパティ自体が指定された場合（参照先Entityのプロパティまで明示指定された場合は除く）
			PropertyColumn property = getLayoutPropertyColumn(sortKey);
			EntityField fallbackField = new EntityField(sortKey + "." + Entity.NAME);

			if (property == null) {
				// 画面上に表示されない場合
				yield fallbackField;
			}
			yield findInSearchResult(sortKey, property, () -> new EntityField(sortKey + "." + getReferencePropertyDisplayName(property.getEditor())),
					(np) -> new EntityField(sortKey + "." + getReferencePropertyDisplayName(np.getEditor())))
							.orElse(fallbackField);

		}
		default -> new EntityField(sortKey);
		};

		return new SortSpec(field, SortType.valueOf(ss.getSortType()
				.name()), getNullOrderingSpec(ss.getNullOrderType()));
	}

	/**
	 * リクエストからソート条件を取得します。
	 */
	private SortSpec getRequestSortSpec(String sortKey) {
		PropertyDefinition pd = getPropertyDefinition(sortKey);
		if (pd == null) {
			throw new ApplicationException("invalid sort key: " + sortKey);
		}

		if (Entity.OID.equals(sortKey)) {
			return new SortSpec(sortKey, getSortType());
		}
		PropertyColumn property = getLayoutPropertyColumn(sortKey);
		// OID以外はSearchResultに定義されているPropertyのみ許可
		if (property == null) {
			throw new ApplicationException("invalid sort key: " + sortKey);
		}

		return (switch (pd) {
		case ReferenceProperty ref -> findInSearchResult(sortKey, property,
				() -> new EntityField(sortKey + "." + getReferencePropertyDisplayName(property.getEditor())),
				(np) -> new EntityField(sortKey + "." + getReferencePropertyDisplayName(np.getEditor())));
		default -> findInSearchResult(sortKey, property, () -> new EntityField(sortKey), (np) -> new EntityField(sortKey));
		}).map(field -> new SortSpec(field, getSortType(), getNullOrderingSpec(property.getNullOrderType())))
				.orElseThrow(() -> new ApplicationException("invalid sort key: " + sortKey));
	}

	/**
	 * 検索結果一覧プロパティからソートキーに対応するEntityFieldを取得します。
	 */
	private Optional<EntityField> findInSearchResult(String sortKey, PropertyColumn property, Supplier<EntityField> dndFieldGetter,
			Function<NestProperty, EntityField> nestPropFieldGetter) {
		if (property.getPropertyName()
				.equals(sortKey)) {
			// ソートキーが直接D&Dされた列の場合
			return Optional.of(dndFieldGetter.get());
		}
		// ネストの存在チェック
		return Optional.ofNullable(getLayoutNestProperty(property, sortKey))
				.map(nestPropFieldGetter);
	}

	/**
	 * フィルタ設定のソート設定からOrderByを取得します。
	 */
	private Optional<OrderBy> getOrderBy(EntityFilterItem item) {
		SyntaxService service = ServiceRegistry.getRegistry()
				.getService(SyntaxService.class);
		OrderBySyntax syntax = service.getSyntaxContext(QuerySyntaxRegister.QUERY_CONTEXT)
				.getSyntax(OrderBySyntax.class);

		return Optional.ofNullable(item.getSort())
				.filter(StringUtil::isNotEmpty)
				.map(sort -> {
					try {
						return syntax.parse(new ParseContext("order by " + sort));
					} catch (ParseException e) {
						throw new SystemException(e.getMessage(), e);
					}
				});
	}

	protected SearchFormView getForm() {
		String viewName = getViewName();
		if (form == null) {
			form = FormViewUtil.getSearchFormView(definition, view, viewName);
		}
		return form;
	}

	/**
	 * 検索条件セクションを取得します。
	 * @return 検索条件セクション
	 */
	protected SearchConditionSection getConditionSection() {
		// TODO: これはnullを返すことがあるのか？ 利用側を見ると、nullチェックをしてないものがある。
		return getForm() != null ? getForm().getCondSection() : null;
	}

	/**
	 * 検索条件プロパティを取得します。
	 * @return
	 */
	protected List<PropertyItem> getLayoutProperties() {
		List<PropertyItem> filteredList = getConditionSection().getElements()
				.stream()
				.filter(e -> e instanceof PropertyItem)
				.map(e -> (PropertyItem) e)
				.collect(Collectors.toList());

		List<PropertyItem> properties = new ArrayList<>();
		for (PropertyItem property : filteredList) {
			if (property.getEditor() instanceof RangePropertyEditor) {
				//範囲系の場合FromとToを分離しておく
				RangePropertyEditor editor = (RangePropertyEditor) property.getEditor();
				PropertyItem from = ObjectUtil.deepCopy(property);
				properties.add(from);

				PropertyItem to = ObjectUtil.deepCopy(property);
				to.setPropertyName(editor.getToPropertyName());
				properties.add(to);
			} else {
				properties.add(property);
			}
		}

		return properties;
	}

	/**
	 * 検索結果セクションを取得します。
	 * @return 検索結果セクション
	 */
	protected SearchResultSection getResultSection() {
		return getForm() != null ? getForm().getResultSection() : null;
	}

	/**
	 * 検索結果プロパティを取得します。
	 * @return
	 */
	protected List<PropertyColumn> getColumnProperties() {
		List<PropertyColumn> properties = getResultSection().getElements()
				.stream()
				.filter(e -> e instanceof PropertyColumn)
				.map(e -> (PropertyColumn) e)
				.collect(Collectors.toList());
		return properties;
	}

	protected PropertyItem getLayoutProperty(String propName) {
		List<PropertyItem> properties = getLayoutProperties();
		Optional<PropertyItem> property = properties.stream()
				.filter(e -> propName.equals(e.getPropertyName()))
				.findFirst();
		if (property.isPresent()) {
			return property.get();
		}
		return null;
	}

	protected PropertyItem getLayoutPropertyForCheck(String propName) {
		List<PropertyItem> properties = getLayoutProperties();
		for (PropertyItem property : properties) {
			String condPropName = property.getPropertyName();

			if (StringUtil.isNotEmpty(condPropName) && condPropName.contains(".")) {
				condPropName = condPropName.split("\\.")[0];
			}

			if (propName.equals(condPropName))
				return property;
		}
		return null;
	}

	protected PropertyColumn getLayoutPropertyColumn(String propName) {
		Optional<PropertyColumn> property = getColumnProperties().stream()
				.filter(e -> propName.equals(e.getPropertyName()))
				.findFirst();
		if (property.isPresent()) {
			return property.get();
		}

		// プロパティ名で一致する列がない場合、参照の各階層を下からチェック
		int dotIndex = propName.lastIndexOf(".");
		if (dotIndex > -1) {
			return getLayoutPropertyColumn(propName.substring(0, dotIndex));
		}

		return null;
	}

	/**
	 * プロパティからNestPropertyを取得
	 * @param property
	 * @param propName
	 * @return
	 */
	private NestProperty getLayoutNestProperty(PropertyColumn property, String propName) {
		if (property.getEditor() != null && !(property.getEditor() instanceof ReferencePropertyEditor)) {
			return null;
		}

		// 親階層以降のプロパティ名で再帰検索
		String subPropName = propName.substring(property.getPropertyName()
				.length() + 1);
		return findLayoutNestPropertyRecursive(subPropName, (ReferencePropertyEditor) property.getEditor());
	}

	/**
	 * プロパティ名に一致するネストプロパティを再帰的に検索し取得する
	 * @param propertyName プロパティ名
	 * @param editor 参照プロパティエディタ
	 * @return ネストプロパティ
	 */
	private NestProperty findLayoutNestPropertyRecursive(String propertyName, ReferencePropertyEditor editor) {
		if (editor.getNestProperties()
				.isEmpty()) {
			return null;
		}

		int dotIndex = propertyName.indexOf(".");
		if (dotIndex > -1) {
			// 子階層を再帰呼び出し
			String topPropName = propertyName.substring(0, dotIndex);
			String subPropName = propertyName.substring(dotIndex + 1);

			Optional<NestProperty> opt = editor.getNestProperties()
					.stream()
					.filter(np -> np.getPropertyName()
							.equals(topPropName))
					.findFirst();
			if (!opt.isPresent())
				return null;

			NestProperty subProp = opt.get();
			if (subProp.getEditor() instanceof ReferencePropertyEditor) {
				return findLayoutNestPropertyRecursive(subPropName, (ReferencePropertyEditor) subProp.getEditor());
			}
		}

		// 一致するNestPropetyを取得
		Optional<NestProperty> opt = editor.getNestProperties()
				.stream()
				.filter(np -> np.getPropertyName()
						.equals(propertyName))
				.findFirst();
		return opt.orElse(null);
	}

	/**
	 * プロパティ定義の一覧を取得します。
	 * @return プロパティ定義の一覧
	 */
	protected List<PropertyDefinition> getPropertyList() {
		return definition.getPropertyList();
	}

	protected Condition getDefaultCondition() {
		SearchConditionSection section = getConditionSection();
		if (section == null || StringUtil.isEmpty(section.getDefaultCondition())) {
			return null;
		}

		return evm.getSearchConditionSectionDefaultCondition(getDefName(), section);
	}

	protected String getViewName() {
		return request.getParam(Constants.VIEW_NAME);
	}

	/**
	 * リクエストからソートタイプを取得します。
	 * ソート種別が指定されていない場合は検索画面のデフォルトソートタイプを取得します。
	 * @return ソートタイプ
	 */
	protected SortType getSortType() {
		String sortType = request.getParam(Constants.SEARCH_SORTTYPE);
		if (StringUtil.isBlank(sortType)) {
			return SortType.DESC;
		}
		return SortType.valueOf(sortType);
	}

	protected NullOrderingSpec getNullOrderingSpec(NullOrderType type) {
		if (type == null)
			return null;
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

	protected final Optional<String> getRequestSortKey() {
		return Optional.ofNullable(getRequest().getParam(Constants.SEARCH_SORTKEY))
				.filter(StringUtil::isNotBlank);
	}

	/**
	 * 参照項目の表示ラベルを取得
	 * @param editor
	 * @return
	 */
	protected String getReferencePropertyDisplayName(PropertyEditor editor) {
		if (editor instanceof ReferencePropertyEditor
				&& StringUtil.isNotEmpty(((ReferencePropertyEditor) editor).getDisplayLabelItem())) {
			return ((ReferencePropertyEditor) editor).getDisplayLabelItem();
		} else {
			return Entity.NAME;
		}
	}

	/**
	 * リクエストから検索上限を取得します。検索上限が指定されていない場合は、定義またはservice-config設定から補完します。
	 * リクエストの検索上限は、定義またはservice-config設定の上限を超えることはできません。
	 *
	 * @return
	 */
	protected Integer getSearchLimit() {
		// 定義またはservice-config設定の上限
		Integer limit = ViewUtil.getSearchLimit(getResultSection());

		// リクエストパラメータの検索上限
		Integer requestedLimit = request.getParamAsInt(Constants.SEARCH_LIMIT);
		if (requestedLimit == null) {
			return limit;
		}

		return limit > requestedLimit ? requestedLimit : limit;
	}

	/**
	 * リクエストから現在の検索位置を取得します。
	 * 検索位置が指定されていない場合は0が返ります。
	 * @return
	 */
	protected Integer getOffset() {
		Integer offset = request.getParamAsInt(Constants.SEARCH_OFFSET);
		if (offset == null)
			offset = 0;
		return offset;
	}

	/**
	 * 検索項目として利用可能な場合にプロパティを検索項目に追加します。
	 * @param context コンテキスト
	 * @param select SELECTするプロパティの一覧
	 * @param propName セットするプロパティ名
	 * @param nest 参照先Entityのプロパティ
	 */
	protected void addSearchProperty(ArrayList<String> select, String propName, NestProperty... nest) {
		addSearchProperty(select, propName, null, nest);
	}

	protected void addSearchProperty(ArrayList<String> select, String propName, PropertyEditor editor, NestProperty... nest) {
		PropertyDefinition pd = getPropertyDefinition(propName);
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
				addDisplayLabelProperty(select, propName, (ReferencePropertyEditor) editor);
			}
			if (nest != null && nest.length > 0) {
				EntityDefinition red = getReferenceEntityDefinition((ReferenceProperty) pd);
				for (NestProperty np : nest) {
					// 仮想プロパティは検索項目に含めない
					if (np.isVirtual()) {
						continue;
					}

					PropertyDefinition rpd = red.getProperty(np.getPropertyName());
					if (rpd != null
							&& !Entity.OID.equals(np.getPropertyName())
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
							if (!rpe.getNestProperties()
									.isEmpty()) {
								List<NestProperty> _nest = rpe.getNestProperties();
								addSearchProperty(select, nestPropName, rpe, _nest.toArray(new NestProperty[_nest.size()]));
							}
							addDisplayLabelProperty(select, nestPropName, rpe);
						} else if (np.getEditor() instanceof JoinPropertyEditor) {
							JoinPropertyEditor jpe = (JoinPropertyEditor) np.getEditor();
							addSearchProperty(select, nestPropName, jpe.getEditor());
							if (!jpe.getProperties()
									.isEmpty()) {
								List<NestProperty> _nest = jpe.getProperties();
								addSearchProperty(select, propName, editor, _nest.toArray(new NestProperty[_nest.size()]));
							}
						} else if (np.getEditor() instanceof RangePropertyEditor) {
							RangePropertyEditor jpe = (RangePropertyEditor) np.getEditor();
							if (jpe.getToPropertyName() != null) {
								select.add(propName + "." + jpe.getToPropertyName());
							}
						}
					}
				}
			}
		} else {
			if (!select.contains(propName))
				select.add(propName);
		}
	}

	protected void addDisplayLabelProperty(ArrayList<String> select, String propName, ReferencePropertyEditor rpe) {
		if (rpe.getDisplayLabelItem() == null)
			return;

		PropertyDefinition pd = getPropertyDefinition(propName);
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
	protected EntityDefinition getReferenceEntityDefinition(ReferenceProperty rp) {
		EntityDefinitionManager edm = ManagerLocator.getInstance()
				.getManager(EntityDefinitionManager.class);
		return edm.get(rp.getObjectDefinitionName());
	}

	//TODO: EntityViewUtil.getPropertyDefinition() とロジックが重複
	protected PropertyDefinition getPropertyDefinition(String propName) {
		int firstDotIndex = propName.indexOf('.');
		if (firstDotIndex > 0) {
			String topPropName = propName.substring(0, firstDotIndex);
			String subPropName = propName.substring(firstDotIndex + 1);
			PropertyDefinition topProperty = definition.getProperty(topPropName);
			if (topProperty instanceof ReferenceProperty) {
				EntityDefinition red = getReferenceEntityDefinition((ReferenceProperty) topProperty);
				if (red != null) {
					//definitionを参照先のdefinitionに置き換えて再帰呼び出し
					EntityDefinition _definition = definition;
					definition = red;
					PropertyDefinition pd = getPropertyDefinition(subPropName);
					//definitionを戻す
					definition = _definition;
					return pd;
				}
			}
		} else {
			return definition.getProperty(propName);
		}
		return null;
	}

	/**
	 * UserPropertyEditorを利用しているか
	 * @return UserPropertyEditorを利用しているか
	 */
	public boolean isUseUserPropertyEditor() {
		Set<String> propNameList = getUseUserPropertyEditorPropertyName();
		return !propNameList.isEmpty();
	}

	/**
	 * UserPropertyEditorを利用しているプロパティ名の一覧を取得します。
	 * @return UserPropertyEditorを利用しているプロパティ名の一覧
	 */
	public Set<String> getUseUserPropertyEditorPropertyName() {

		if (useUserPropertyEditorPropertyNameList != null) {
			return useUserPropertyEditorPropertyNameList;
		}

		useUserPropertyEditorPropertyNameList = new HashSet<>();

		List<PropertyColumn> properties = getColumnProperties();
		for (PropertyColumn property : properties) {
			String propertyName = property.getPropertyName();

			if (property.getEditor() instanceof ReferencePropertyEditor) {
				// ネストの項目を確認
				ReferencePropertyEditor editor = (ReferencePropertyEditor) property.getEditor();
				if (!editor.getNestProperties()
						.isEmpty()) {
					Set<String> nest = getUseUserPropertyEditorNestPropertyName(editor);
					for (String nestPropertyName : nest) {
						String _nestPropertyName = propertyName + "." + nestPropertyName;
						useUserPropertyEditorPropertyNameList.add(_nestPropertyName);
					}
				}
			} else if (property.getEditor() instanceof UserPropertyEditor) {
				useUserPropertyEditorPropertyNameList.add(propertyName);
			}

		}

		return useUserPropertyEditorPropertyNameList;
	}

	private Set<String> getUseUserPropertyEditorNestPropertyName(ReferencePropertyEditor editor) {
		Set<String> ret = new HashSet<>();
		for (NestProperty property : editor.getNestProperties()) {

			if (property.getEditor() instanceof ReferencePropertyEditor) {
				//再ネストの項目を確認
				ReferencePropertyEditor nestEditor = (ReferencePropertyEditor) property.getEditor();
				if (!nestEditor.getNestProperties()
						.isEmpty()) {
					Set<String> nest = getUseUserPropertyEditorNestPropertyName(nestEditor);
					for (String nestPropertyName : nest) {
						String _nestPropertyName = property.getPropertyName() + "." + nestPropertyName;
						ret.add(_nestPropertyName);
					}
				}
			} else if (property.getEditor() instanceof UserPropertyEditor) {
				// NestProperty項目
				ret.add(property.getPropertyName());
			}
		}
		return ret;
	}

	/**
	 * 検索処理実行前にクエリに対する操作を行います。
	 * @param query
	 * @return
	 */
	public SearchQueryContext beforeSearch(Query query, SearchQueryType type) {
		return getSearchQueryInterrupterHandler().beforeSearch(query, type);
	}

	/**
	 * 検索処理実行後にカスタム処理を実行します。
	 * @param query
	 * @param resultList
	 * @param type
	 */
	public void afterSearch(Query query, Entity entity, SearchQueryType type) {
		getSearchQueryInterrupterHandler().afterSearch(query, entity, type);
	}

	/**
	 * カスタム登録処理を取得します。
	 * @return カスタム登録処理
	 */
	public SearchQueryInterrupterHandler getSearchQueryInterrupterHandler() {
		if (interrupterHandler == null) {
			SearchQueryInterrupter interrupter = createInterrupter(getForm().getInterrupterName());
			interrupterHandler = new SearchQueryInterrupterHandler(request, this, interrupter);
		}
		return interrupterHandler;
	}

	protected SearchQueryInterrupter createInterrupter(String className) {
		SearchQueryInterrupter interrupter = null;
		if (StringUtil.isNotEmpty(className)) {
			if (log.isDebugEnabled()) {
				log.debug("set search query interrupter. class=" + className);
			}
			try {
				interrupter = ucdm.createInstanceAs(SearchQueryInterrupter.class, className);
			} catch (ClassNotFoundException e) {
				log.error(className + " can not instantiate.", e);
				throw new EntityRuntimeException(resourceString("command.generic.detail.DetailCommandContext.internalErr"));
			}
		}
		if (interrupter == null) {
			//何もしないデフォルトInterrupter生成
			if (log.isDebugEnabled()) {
				log.debug("set defaul search query interrupter.");
			}
			interrupter = getDefaultSearchQueryInterrupter();
		}
		return interrupter;
	}

	protected SearchQueryInterrupter getDefaultSearchQueryInterrupter() {
		return new SearchQueryInterrupter() {
		};
	}

	@Override
	public void fireCreateSearchResultEvent(final SearchResultData result) {

		CreateSearchResultEvent event = new CreateSearchResultEvent(getRequest(), getDefName(), getViewName(), result);
		for (SearchFormViewHandler handler : getSearchFormViewHandlers()) {
			handler.onCreateSearchResult(event);
		}
	}

	private List<SearchFormViewHandler> getSearchFormViewHandlers() {
		if (searchFormViewHandlers == null) {
			searchFormViewHandlers = new ArrayList<>();
			if (getForm().getSearchFormViewHandlerName() != null) {
				for (String handlerClassName : getForm().getSearchFormViewHandlerName()) {
					searchFormViewHandlers.add(createSearchFormViewHandler(handlerClassName));
				}
			}
			//編集リンクの範囲権限チェックを行う場合は、先頭にHandlerを追加
			if (!getResultSection().isHideDetailLink()
					&& getResultSection().isCheckEntityPermissionLimitConditionOfEditLink()) {
				if (log.isDebugEnabled()) {
					log.debug("add CheckUpdateConditionOfEditLinkHandler to the first of search form view handler.");
				}
				searchFormViewHandlers.add(0, new CheckPermissionLimitConditionOfEditLinkHandler());
			}
		}
		return searchFormViewHandlers;
	}

	private SearchFormViewHandler createSearchFormViewHandler(String handlerClassName) {
		SearchFormViewHandler handler = null;
		if (StringUtil.isNotEmpty(handlerClassName)) {
			if (log.isDebugEnabled()) {
				log.debug("create search form view handler. class=" + handlerClassName);
			}
			try {
				handler = ucdm.createInstanceAs(SearchFormViewHandler.class, handlerClassName);
			} catch (ClassNotFoundException e) {
				log.error(handlerClassName + " can not instantiate.", e);
				throw new EntityRuntimeException(resourceString("command.generic.detail.DetailCommandContext.internalErr"));
			}
		}
		return handler;
	}

	private static String resourceString(String key, Object... arguments) {
		return GemResourceBundleUtil.resourceString(key, arguments);
	}

}
