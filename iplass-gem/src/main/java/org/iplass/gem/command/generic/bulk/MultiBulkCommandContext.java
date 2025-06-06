/*
 * Copyright (C) 2019 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.gem.command.generic.bulk;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.iplass.gem.GemConfigService;
import org.iplass.gem.command.CommandUtil;
import org.iplass.gem.command.Constants;
import org.iplass.gem.command.generic.detail.NestTableReferenceRegistHandler;
import org.iplass.gem.command.generic.detail.ReferenceRegistHandler;
import org.iplass.gem.command.generic.detail.ReferenceRegistOption;
import org.iplass.gem.command.generic.detail.RegistrationCommandContext;
import org.iplass.gem.command.generic.detail.RegistrationPropertyBaseHandler;
import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityKey;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.LoadOption;
import org.iplass.mtp.entity.ValidateError;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.impl.util.ConvertUtil;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.CollectionUtil;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.BulkFormView;
import org.iplass.mtp.view.generic.BulkOperationInterrupter;
import org.iplass.mtp.view.generic.EntityViewUtil;
import org.iplass.mtp.view.generic.FormViewUtil;
import org.iplass.mtp.view.generic.OutputType;
import org.iplass.mtp.view.generic.SearchFormView;
import org.iplass.mtp.view.generic.editor.DateRangePropertyEditor;
import org.iplass.mtp.view.generic.editor.JoinPropertyEditor;
import org.iplass.mtp.view.generic.editor.NestProperty;
import org.iplass.mtp.view.generic.editor.NumericRangePropertyEditor;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.RangePropertyEditor;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor.ReferenceDisplayType;
import org.iplass.mtp.view.generic.element.Element;
import org.iplass.mtp.view.generic.element.property.PropertyItem;
import org.iplass.mtp.view.generic.element.section.DefaultSection;
import org.iplass.mtp.view.generic.element.section.SearchResultSection.ExclusiveControlPoint;
import org.iplass.mtp.view.generic.element.section.Section;
import org.iplass.mtp.web.template.TemplateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultiBulkCommandContext extends RegistrationCommandContext {

	private static Logger logger = LoggerFactory.getLogger(MultiBulkCommandContext.class);

	/** 一括画面用のFormレイアウト情報 */
	private BulkFormView view;

	private GemConfigService gemConfig = null;

	private BulkUpdateInterrupterHandler bulkInterrupterHandler = null;

	private List<BulkCommandParams> bulkCommandParams = new ArrayList<>();

	/** 一括更新プロパティリスト*/
	private List<PropertyItem> propList = null;

	/** 一括更新時未入力のプロパティリスト*/
	private List<PropertyItem> blankPropList = null;
	/**
	 * クライアントから配列で受け取ったパラメータは自動設定する対象外
	 */
	@SuppressWarnings("serial")
	private Set<String> skipProps = new HashSet<String>() {
		{
			add(Entity.OID);
			add(Entity.VERSION);
			add(Entity.UPDATE_DATE);
		}
	};

	@Override
	protected Logger getLogger() {
		return logger;
	}

	public MultiBulkCommandContext(RequestContext request, EntityManager entityLoader, EntityDefinitionManager definitionLoader) {
		super(request, entityLoader, definitionLoader);

		gemConfig = ServiceRegistry.getRegistry().getService(GemConfigService.class);
		init();
	}

	private void init() {
		populateBulkCommandParam(Constants.OID, String.class, true, true, false);
		populateBulkCommandParam(Constants.VERSION, Long.class, false, true, false);
		populateBulkCommandParam(Constants.TIMESTAMP, Long.class, false, false, true);
	}

	private void populateBulkCommandParam(String name, Class<?> cls, boolean create, boolean notBlank, boolean checkDiff) {
		//BulkUpdateAllCommandからのChainの可能性があるので、Attributeから取得する
		String[] param = (String[]) request.getAttribute(name);
		if (param == null || param.length == 0) {
			param = getParams(name);
		}
		if (param != null) {
			for (int i = 0; i < param.length; i++) {
				// 先頭に「行番号_」が付加されているので分離する
				String[] params = splitRowParam(param[i]);
				Integer targetRow = Integer.parseInt(params[0]);
				String targetParam = params[1];
				if (StringUtil.isBlank(targetParam) && notBlank) {
					getLogger().error("can not be empty. name=" + name + ", param=" + param[i]);
					throw new ApplicationException(resourceString("command.generic.bulk.BulkCommandContext.invalidFormat"));
				}
				BulkCommandParams bulkParams = getBulkCommandParams(targetRow);
				if (create) {
					// targetParamをキーとして設定する
					if (bulkParams != null) {
						getLogger().error("duplicate row. row=" + targetRow);
						throw new ApplicationException(resourceString("command.generic.bulk.BulkCommandContext.duplicateRow"));
					}
					bulkCommandParams.add(new BulkCommandParams(targetRow, targetParam));
				} else {
					if (bulkParams == null) {
						getLogger().error("selected row does not exist. params=" + Arrays.toString(params));
						throw new ApplicationException(resourceString("command.generic.bulk.BulkCommandContext.invalidRow"));
					}
					bulkParams.setValue(name, ConvertUtil.convertFromString(cls, targetParam));
				}

				if (checkDiff && i == param.length - 1) {
					// バージョン管理されているエンティティでマルチリファレンスのプロパティ定義がある場合、同じOIDとバージョンで行番号が異なるデータが存在するので、
					// 設定されたプロパティ値が同じ値であるかチェックします。
					boolean hasDiffPropValue = hasDifferentPropertyValue(name);
					if (hasDiffPropValue) {
						getLogger().error("has different prop value. name=" + name + ", bulkCommandParams=" + bulkCommandParams.toString());
						throw new ApplicationException(resourceString("command.generic.bulk.BulkCommandContext.diffPropVal"));
					}
				}
			}
		}
	}

	private String[] splitRowParam(String rowParam) {
		if (rowParam.indexOf("_") == -1) {
			getLogger().error("invalid parameter format. rowParam=" + rowParam);
			throw new ApplicationException(resourceString("command.generic.bulk.BulkCommandContext.invalidFormat"));
		}
		String targetRow = rowParam.substring(0, rowParam.indexOf("_"));
		String targetParam = rowParam.substring(rowParam.indexOf("_") + 1);
		String[] params = { targetRow, targetParam };
		return params;
	}

	private BulkCommandParams getBulkCommandParams(Integer row) {
		List<BulkCommandParams> paramsList = bulkCommandParams.stream()
				.filter(p -> p.getRow().equals(row))
				.collect(Collectors.toList());
		if (paramsList.size() == 0) {
			return null;
		} else if (paramsList.size() > 1) {
			getLogger().error("duplicate row. paramsList=" + paramsList);
			throw new ApplicationException(resourceString("command.generic.bulk.BulkCommandContext.duplicateRow"));
		}
		return paramsList.get(0);
	}

	private boolean hasDifferentPropertyValue(String propName) {
		Set<String> oids = getOids();
		for (String oid : oids) {
			for (Long version : getVersions(oid)) {
				List<Object> propValues = bulkCommandParams.stream()
						.filter(p -> p.getOid().equals(oid) && p.getVersion().equals(version))
						.map(p -> p.getValue(propName))
						.collect(Collectors.toList());
				Object first = propValues.get(0);
				if (first == null) {
					return propValues.stream().anyMatch(v -> v != null);
				} else {
					return propValues.stream().anyMatch(v -> !first.equals(v));
				}
			}
		}
		return false;
	}

	/**
	 * リクエストパラメータから参照型の更新データを作成します。
	 * @param p プロパティ定義
	 * @param prefix 参照型のプロパティのリクエストパラメータに設定されているプレフィックス
	 * @return プロパティの値
	 */
	@Override
	protected Object createReference(PropertyDefinition p, String prefix) {
		final ReferenceProperty rp = (ReferenceProperty) p;
		final String defName = rp.getObjectDefinitionName();

		//NestTableの場合の件数取得
		//prefixが付くケース=NestTable内の多重参照なのであり得ない
		//→件数取れないため通常の参照扱いで処理が終わる
		Long count = getLongValue(prefix + p.getName() + "_count");
		if (p.getMultiplicity() == 1) {
			List<Entity> list = null;
			if (count == null) {
				String key = getParam(prefix + p.getName());
				list = getRefEntities(rp.getObjectDefinitionName(), new String[] { key });
			} else {
				list = getRefTableValues(rp, defName, count, prefix);
			}
			if (CollectionUtil.isNotEmpty(list)) {
				return list.get(0);
			} else {
				return null;
			}
		} else {
			List<Entity> list = null;
			if (count == null) {
				String[] params = getParams(prefix + p.getName());
				if (params != null) {
					list = getRefEntities(rp.getObjectDefinitionName(), params);
				}
			} else {
				//参照型で参照先のデータを作成・編集するケース
				list = getRefTableValues(rp, defName, count, prefix);
			}

			if (CollectionUtil.isNotEmpty(list)) {
				//マッピングクラスの配列を生成する
				EntityDefinition ed = getEntityDefinition();
				setEntityDefinition(definitionManager.get(defName));
				Entity emptyEntity = newEntity();
				setEntityDefinition(ed);

				Object[] array = (Object[]) Array.newInstance(emptyEntity.getClass(), list.size());
				return list.toArray(array);

			} else {
				return null;
			}
		}
	}

	/**
	 * NestTable以外の参照先編集データを返します。
	 *
	 * @param definitionName 参照先Entity定義名
	 * @param keyParameters oidとversionのパラメータ情報
	 * @return 編集データ
	 */
	private List<Entity> getRefEntities(String definitionName, String[] keyParameters) {

		List<EntityKey> keys = Arrays.stream(keyParameters)
				.filter(key -> StringUtil.isNotEmpty(key))
				.map(key -> {
					int lastIndex = key.lastIndexOf("_");
					String oid = null;
					Long version = null;
					if (lastIndex < 0) {
						oid = key;
					} else {
						oid = key.substring(0, lastIndex);
						version = CommandUtil.getLong(key.substring(lastIndex + 1));
					}
					return new EntityKey(oid, version);
				})
				.collect(Collectors.toList());

		List<Entity> entities = null;
		if (!keys.isEmpty()) {
			if (gemConfig.isMustLoadWithReference()) {
				// 参照Entityをロードし直す
				if (gemConfig.isLoadWithReference()) {
					entities = entityManager.batchLoad(keys, definitionName);
				} else {
					entities = entityManager.batchLoad(keys, definitionName, new LoadOption(false, false));
				}
			} else {
				// 参照Entityをロードし直さない
				final EntityDefinition referenceEntityDefinition = definitionManager.get(definitionName);
				entities = keys.stream()
						.map(key -> {
							Entity entity = newEntity(referenceEntityDefinition);
							entity.setOid(key.getOid());
							entity.setVersion(key.getVersion());
							return entity;
						})
						.collect(Collectors.toList());
			}
		}
		return entities;
	}

	/**
	 * NestTableの編集データを返します。
	 *
	 * @param p 対象ReferenceProperty定義
	 * @param defName 参照先Entity定義名
	 * @param count 件数
	 * @param prefix 参照型のプロパティのリクエストパラメータに設定されているプレフィックス
	 * @return NestTableの編集データ
	 */
	private List<Entity> getRefTableValues(ReferenceProperty p, String defName, Long count, String prefix) {
		final List<Entity> list = new ArrayList<>();
		EntityDefinition ed = getEntityDefinition();
		EntityDefinition red = definitionManager.get(defName);
		setEntityDefinition(red);//参照先の定義に詰め替える
		for (int i = 0; i < count; i++) {
			//データあり
			String paramPrefix = prefix + p.getName() + "[" + Integer.toString(i) + "].";
			String errorPrefix = (i != list.size() ? prefix + p.getName() + "[" + Integer.toString(list.size()) + "]." : null);
			Entity entity = createEntityInternal(paramPrefix, errorPrefix);

			//入力エラー時に再Loadされないようにフラグ設定
			entity.setValue(Constants.REF_RELOAD, Boolean.FALSE);

			//Validationエラーが出るとhiddenに"null"が入るのでクリアする
			if (entity.getOid() != null && entity.getOid().equals("null")) {
				entity.setOid(null);
			}

			//Entity生成時にエラーが発生していないかチェック
			String checkPrefix = (errorPrefix != null ? errorPrefix : paramPrefix);
			boolean hasError = getErrors().stream()
					.filter(error -> error.getPropertyName().startsWith(checkPrefix))
					.findFirst()
					.isPresent();

			//エラーがなくて、何もデータが入ってないものは破棄する
			if (hasError || !isEmpty(entity)) {
				entity.setDefinitionName(defName);
				entity.setValue(Constants.REF_INDEX, list.size());

				Long orderIndex = getLongValue("tableOrderIndex[" + i + "]");
				if (orderIndex != null) {
					entity.setValue(Constants.REF_TABLE_ORDER_INDEX, orderIndex);
				}

				list.add(entity);
			}
		}
		setEntityDefinition(ed);//元の定義に詰め替える

		// ネストテーブル用の登録処理を追加
		Optional<PropertyItem> ret = getProperty().stream().filter(pc -> pc.getPropertyName().equals(p.getName())).findFirst();
		if (ret.isPresent() && !list.isEmpty()) {
			addNestTableRegistHandler(p, list, red, ret.get());
		}

		return list;
	}

	private void addNestTableRegistHandler(ReferenceProperty p, List<Entity> list, EntityDefinition red, PropertyItem property) {
		// ネストテーブルはプロパティ単位で登録可否決定
		if (!NestTableReferenceRegistHandler.canRegist(property, getRegistrationPropertyBaseHandler())) {
			return;
		}

		// カスタム登録処理によるNestTableの更新制御
		ReferenceRegistOption option = getRegistrationInterrupterHandler().getNestTableRegistOption(red, p.getName());
		// isSpecifyAllPropertiesがtrue、且つReference項目が除外、且つNestされたEntityの個々プロパティも全て指定なしの場合
		// 参照先EntityにおけるEntityのデータ追加不可
		if (option.isSpecifyAllProperties() && !option.isSpecifiedAsReference()
				&& option.getSpecifiedUpdateNestProperties().isEmpty()) {
			return;
		}

		ReferencePropertyEditor editor = (ReferencePropertyEditor) property.getEditor();

		List<Entity> target = null;
		if (StringUtil.isNotBlank(editor.getTableOrderPropertyName())) {
			//表示順再指定
			PropertyDefinition pd = red.getProperty(editor.getTableOrderPropertyName());
			target = EntityViewUtil.sortByOrderProperty(list, Constants.REF_TABLE_ORDER_INDEX);
			for (int i = 0; i < target.size(); i++) {
				target.get(i).setValue(editor.getTableOrderPropertyName(), ConvertUtil.convert(pd.getJavaType(), i));
			}
		} else {
			target = list;
		}

		ReferenceRegistHandler handler = NestTableReferenceRegistHandler.get(this, list, red, p, property, editor.getNestProperties(),
				getRegistrationPropertyBaseHandler(), option);
		if (handler != null) {
			handler.setForceUpdate(editor.isForceUpadte());
			getReferenceRegistHandlers().add(handler);
		}
	}

	/**
	 * Entityにデータが設定されているかチェックします。
	 * @param entity 画面で入力されたデータ
	 * @return Entityにデータが設定されているか
	 */
	private boolean isEmpty(Entity entity) {
		for (PropertyDefinition pd : getPropertyList()) {
			if (pd.getMultiplicity() != 1) {
				Object[] obj = entity.getValue(pd.getName());
				if (obj != null && obj.length > 0) {
					return false;
				}
			} else {
				if (entity.getValue(pd.getName()) != null) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	protected String getInterrupterName() {
		return getView().getInterrupterName();
	}

	@Override
	protected String getLoadEntityInterrupterName() {
		return getView().getLoadEntityInterrupterName();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected RegistrationPropertyBaseHandler<PropertyItem> createRegistrationPropertyBaseHandler() {
		return new RegistrationPropertyBaseHandler<PropertyItem>() {
			@Override
			public boolean isDispProperty(PropertyItem property) {
				return EntityViewUtil.isDisplayElement(entityDefinition.getName(), property.getElementRuntimeId(), OutputType.BULK, null);
			}

			@Override
			public PropertyEditor getEditor(PropertyItem property) {
				return property.getEditor();
			}
		};
	}

	public LinkedHashSet<String> getOids() {
		return bulkCommandParams.stream()
				.map(p -> p.getOid())
				.collect(Collectors.toCollection(LinkedHashSet::new));
	}

	/**
	 * 一括更新画面用のFormレイアウト情報を取得します。
	 * @return 一括更新画面用のFormレイアウト情報
	 */
	@SuppressWarnings("unchecked")
	@Override
	public BulkFormView getView() {
		String viewName = getViewName();
		if (view == null) {
			view = FormViewUtil.getBulkFormView(entityDefinition, entityView, viewName);
		}
		return view;
	}

	/**
	 * 一括更新の排他制御起点を取得します。
	 * @return 一括更新の排他制御起点
	 */
	public ExclusiveControlPoint getExclusiveControlPoint() {
		String viewName = getViewName();
		SearchFormView view = FormViewUtil.getSearchFormView(entityDefinition, entityView, viewName);
		return view.getResultSection().getExclusiveControlPoint();
	}

	/**
	 * 編集画面用のFormレイアウト情報を設定します。
	 * @param view 編集画面用のFormレイアウト情報
	 */
	public void setView(BulkFormView view) {
		this.view = view;
	}

	@Override
	public boolean isNewVersion() {
		return false;
	}

	@Override
	public boolean isLoadVersioned() {
		return false;
	}

	@Override
	protected boolean isPurgeCompositionedEntity() {
		return getView().isPurgeCompositionedEntity();
	}

	@Override
	protected boolean isLocalizationData() {
		return getView().isLocalizationData();
	}

	@Override
	protected boolean isForceUpadte() {
		return getView().isForceUpadte();
	}

	/**
	 * 更新可能な被参照（ネストテーブル）を定義内に保持しているかを取得します。
	 * @return
	 */
	@Override
	public boolean hasUpdatableMappedByReference() {
		List<PropertyItem> properties = getProperty();
		for (PropertyItem property : properties) {
			PropertyDefinition pd = getProperty(property.getPropertyName());
			if (pd instanceof ReferenceProperty) {
				String mappedBy = ((ReferenceProperty) pd).getMappedBy();
				if (StringUtil.isBlank(mappedBy)) {
					continue;
				}

				if (property.getEditor() instanceof ReferencePropertyEditor) {
					ReferencePropertyEditor editor = (ReferencePropertyEditor) property.getEditor();
					if (editor.getDisplayType() == ReferenceDisplayType.NESTTABLE) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * リクエストから検索条件を取得します。
	 * @return 検索条件
	 */
	public String getSearchCond() {
		return getParam(Constants.SEARCH_COND);
	}

	public Integer getRow(String oid, Long version) {
		Integer row = null;
		// バージョン管理されているエンティティでマルチリファレンスのプロパティ定義がある場合、同じOIDとバージョンで行番号が異なるデータが存在するので、
		// 一括更新する際に、一行目だけ更新します。
		Optional<Integer> rw = bulkCommandParams.stream()
				.filter(p -> p.getOid().equals(oid) && p.getVersion().equals(version))
				.map(p -> p.getRow())
				.findFirst();
		if (rw.isPresent()) {
			row = rw.get();
		}
		return row;
	}

	public Set<Long> getVersions(String oid) {
		// バージョン管理の場合、同じOIDで異なるバージョンが存在するので、
		// バージョンセットを取得する。
		return bulkCommandParams.stream()
				.filter(p -> p.getOid().equals(oid))
				.map(p -> p.getVersion())
				.collect(Collectors.toSet());
	}

	public Timestamp getTimestamp(String oid, Long version) {
		Timestamp ts = null;
		// バージョン管理されているエンティティでマルチリファレンスのプロパティ定義がある場合、同じOIDとバージョンで行番号が異なるデータが存在するので、
		// 一括更新する際に、一行目だけ更新します。
		Optional<Long> updateDate = bulkCommandParams.stream()
				.filter(p -> p.getOid().equals(oid) && p.getVersion().equals(version))
				.map(p -> p.getUpdateDate())
				.findFirst();
		if (updateDate.isPresent() && updateDate.get() != null) {
			ts = new Timestamp(updateDate.get());
		}
		return ts;
	}

	public Boolean getSelectAllPage() {
		return request.getParamAsBoolean(Constants.BULK_UPDATE_SELECT_ALL_PAGE);
	}

	public String getSelectAllType() {
		return getParam(Constants.BULK_UPDATE_SELECT_TYPE);
	}

	public Entity createEntity() {
		return createEntityInternal("", null);
	}

	/**
	 * 更新エンティティを作成します。
	 * @param oid
	 * @param version
	 * @param updateDate
	 * @return 空のプロパティは更新しないので、更新するプロパティが1件もない場合、nullを返します。
	 */
	public Entity createEntity(String oid, Long version, Timestamp updateDate) {
		Entity entity = createEntityInternal("", null);
		entity.setOid(oid);
		entity.setUpdateDate(updateDate);
		//		if (isVersioned()) {
		// バージョン管理にかかわらず、セットする問題ないかな..
		entity.setVersion(version);
		//		}
		//		setVirtualPropertyValue(entity);
		getRegistrationInterrupterHandler().dataMapping(entity);
		validate(entity);

		// カスタム登録処理に対応する為、入力値が空のプロパティリストを保持しておく
		if (blankPropList == null) {
			blankPropList = createBlankPropList(entity);
		}

		//更新しないプロパティを外す。
		removePropIfBlank(entity);

		//更新するプロパティが1件もない場合、nullを返します。
		return getProperty().size() > 0 ? entity : null;
	}

	private Entity createEntityInternal(String paramPrefix, String errorPrefix) {
		Entity entity = newEntity();
		for (PropertyDefinition p : getPropertyList()) {
			if (skipProps.contains(p.getName())) {
				continue;
			}
			Object value = getPropValue(p, paramPrefix);
			entity.setValue(p.getName(), value);
			if (errorPrefix != null) {
				String name = paramPrefix + p.getName();
				// Entity生成時にエラーが発生していないかチェックして置き換え
				String errorName = errorPrefix + p.getName();
				getErrors().stream()
						.filter(error -> error.getPropertyName().equals(name))
						.forEach(error -> error.setPropertyName(errorName));
			}
		}
		return entity;
	}

	/**
	 * 標準の入力チェック以外のチェック、PropertyEditor絡みのもの
	 * @param entity
	 */
	protected void validate(Entity entity) {
		List<PropertyItem> properties = getDisplayProperty();
		for (PropertyItem property : properties) {
			if (property.getEditor() instanceof DateRangePropertyEditor) {
				//日付の逆転チェック
				DateRangePropertyEditor editor = (DateRangePropertyEditor) property.getEditor();
				checkDateRange(editor, entity, property.getPropertyName(), editor.getToPropertyName(), "");
			} else if (property.getEditor() instanceof NumericRangePropertyEditor) {
				//数値の逆転チェック
				NumericRangePropertyEditor editor = (NumericRangePropertyEditor) property.getEditor();
				checkNumericRange(editor, entity, property.getPropertyName(), editor.getToPropertyName(), "");
			} else if (property.getEditor() instanceof ReferencePropertyEditor) {
				ReferencePropertyEditor editor = (ReferencePropertyEditor) property.getEditor();
				Object val = entity.getValue(property.getPropertyName());

				Entity[] ary = null;
				if (val != null) {
					if (val instanceof Entity) {
						ary = new Entity[] { (Entity) val };
					} else if (val instanceof Entity[]) {
						ary = (Entity[]) val;
					}
				}

				if (editor.getDisplayType() == ReferenceDisplayType.NESTTABLE
						&& ary != null && ary.length > 0
						&& editor.getNestProperties() != null && !editor.getNestProperties().isEmpty()) {
					//NestTable、参照セクション
					for (int i = 0; i < ary.length; i++) {
						String errorPrefix = property.getPropertyName() + "[" + i + "].";
						for (NestProperty np : editor.getNestProperties()) {
							if (np.getEditor() instanceof DateRangePropertyEditor) {
								//日付の逆転チェック
								DateRangePropertyEditor de = (DateRangePropertyEditor) np.getEditor();
								checkDateRange(de, ary[i], np.getPropertyName(), de.getToPropertyName(), errorPrefix);
							} else if (np.getEditor() instanceof NumericRangePropertyEditor) {
								//数値の逆転チェック
								NumericRangePropertyEditor de = (NumericRangePropertyEditor) np.getEditor();
								checkNumericRange(de, ary[i], np.getPropertyName(), de.getToPropertyName(), errorPrefix);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 日付範囲のチェック
	 * @param editor
	 * @param entity
	 * @param fromName
	 * @param toName
	 * @param errorPrefix
	 */
	private void checkDateRange(DateRangePropertyEditor editor, Entity entity, String fromName, String toName, String errorPrefix) {
		java.util.Date from = entity.getValue(fromName);
		java.util.Date to = entity.getValue(editor.getToPropertyName());

		if (from == null && to != null) {
			if (!editor.isInputNullFrom()) {
				setValidateErrorMessage(editor, fromName, errorPrefix, "command.generic.detail.DetailCommandContext.inputDateRangeErr");
			}
		} else if (from != null && to == null) {
			if (!editor.isInputNullTo()) {
				setValidateErrorMessage(editor, fromName, errorPrefix, "command.generic.detail.DetailCommandContext.inputDateRangeErr");
			}
		} else if (from != null && to != null) {
			boolean result = false;

			if (editor.isEquivalentInput()) {
				result = (from.compareTo(to) > 0) ? true : false;
			} else {
				result = (from.compareTo(to) >= 0) ? true : false;
			}

			if (result) {
				setValidateErrorMessage(editor, fromName, errorPrefix, "command.generic.detail.DetailCommandContext.invalidDateRange");
			}
		}
	}

	/**
	 * 数値範囲のチェック
	 * @param editor
	 * @param entity
	 * @param fromName
	 * @param toName
	 * @param errorPrefix
	 */
	private void checkNumericRange(NumericRangePropertyEditor editor, Entity entity, String fromName, String toName, String errorPrefix) {
		Number from = entity.getValue(fromName);
		Number to = entity.getValue(editor.getToPropertyName());
		BigDecimal from_tmp = castNumericRangeNumber(from);
		BigDecimal to_tmp = castNumericRangeNumber(to);

		if (from_tmp == null && to_tmp != null) {
			if (!editor.isInputNullFrom()) {
				setValidateErrorMessage(editor, fromName, errorPrefix, "command.generic.bulk.BulkCommandContext.inputNumericRangeErr");
			}
		} else if (from_tmp != null && to_tmp == null) {
			if (!editor.isInputNullTo()) {
				setValidateErrorMessage(editor, fromName, errorPrefix, "command.generic.bulk.BulkCommandContext.inputNumericRangeErr");
			}
		} else if (from_tmp != null && to_tmp != null) {
			boolean result = false;

			if (editor.isEquivalentInput()) {
				result = (from_tmp.compareTo(to_tmp) > 0) ? true : false;
			} else {
				result = (from_tmp.compareTo(to_tmp) >= 0) ? true : false;
			}
			if (result) {
				setValidateErrorMessage(editor, fromName, errorPrefix, "command.generic.bulk.BulkCommandContext.invalidNumericRange");
			}
		}
	}

	/**
	 * 数値範囲のキャスト
	 * @param number
	 * @return castNumber
	 */
	private BigDecimal castNumericRangeNumber(Number number) {
		if (number instanceof Double) {
			return new BigDecimal(number.doubleValue());
		} else if (number instanceof Long) {
			return new BigDecimal(number.longValue());
		} else if (number instanceof BigDecimal) {
			return (BigDecimal) number;
		} else {
			return null;
		}
	}

	/**
	 * エラーメッセージの設定
	 * @param editor
	 * @param entity
	 * @param fromName
	 * @param errorPrefix
	 * @param inputNullFlag
	 * @param comparisonFlag
	 */
	private void setValidateErrorMessage(RangePropertyEditor editor, String fromName, String errorPrefix, String resourceStringKey) {
		String errorMessage = TemplateUtil.getMultilingualString(editor.getErrorMessage(), editor.getLocalizedErrorMessageList());
		if (StringUtil.isBlank(errorMessage)) {
			errorMessage = resourceString(resourceStringKey);
		}
		ValidateError e = new ValidateError();
		e.setPropertyName(errorPrefix + fromName + "_" + editor.getToPropertyName());//fromだけだとメッセージが変なとこに出るので細工
		e.addErrorMessage(errorMessage);
		getErrors().add(e);
	}

	/**
	 * ブランクの項目は未入力と見なし、更新しないので、一括更新可能なプロパティリストから外す。
	 * @param entity
	 */
	private void removePropIfBlank(Entity entity) {
		getProperty().removeIf(pi -> entity.getValue(pi.getPropertyName()) == null);
	}

	/**
	 * ブランクの項目のリストを作成
	 * @param entity
	 */
	private List<PropertyItem> createBlankPropList(Entity entity) {
		return (getProperty().stream()
				.filter(pi -> entity.getValue(pi.getPropertyName()) == null)
				.collect(Collectors.toList()));
	}

	/**
	 * 一括更新するプロパティを取得します。 組み合わせで使うプロパティである場合、通常のプロパティ扱いにします。
	 *
	 * @return 一括更新するプロパティ
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PropertyItem> getProperty() {
		if (propList == null) {
			propList = new ArrayList<>();
			for (Section section : getView().getSections()) {
				if (section instanceof DefaultSection) {
					if (EntityViewUtil.isDisplayElement(entityDefinition.getName(), section.getElementRuntimeId(), OutputType.BULK, null)) {
						propList.addAll(getProperty((DefaultSection) section));
					}
				}
			}
		}
		return propList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PropertyItem> getUpdateProperty() {
		return getProperty();
	}

	/**
	 * セクション内のプロパティ取得を取得します。
	 * @param section セクション
	 * @return プロパティの一覧
	 */
	@SuppressWarnings("unchecked")
	protected List<PropertyItem> getProperty(DefaultSection section) {
		List<PropertyItem> propList = new ArrayList<>();
		for (Element elem : section.getElements()) {
			if (elem instanceof PropertyItem) {
				PropertyItem prop = (PropertyItem) elem;
				if (getRegistrationPropertyBaseHandler().isDispProperty(prop)) {
					if (prop.getEditor() instanceof JoinPropertyEditor) {
						//組み合わせで使うプロパティを通常のプロパティ扱いに
						JoinPropertyEditor je = (JoinPropertyEditor) prop.getEditor();
						for (NestProperty nest : je.getProperties()) {
							PropertyItem dummy = new PropertyItem();
							dummy.setDispFlag(true);
							dummy.setPropertyName(nest.getPropertyName());
							dummy.setEditor(nest.getEditor());
							propList.add(dummy);
						}
					} else if (prop.getEditor() instanceof DateRangePropertyEditor) {
						//組み合わせで使うプロパティを通常のプロパティ扱いに
						DateRangePropertyEditor de = (DateRangePropertyEditor) prop.getEditor();
						PropertyItem dummy = new PropertyItem();
						dummy.setDispFlag(true);
						dummy.setPropertyName(de.getToPropertyName());
						dummy.setEditor(de.getEditor());
						propList.add(dummy);
					} else if (prop.getEditor() instanceof NumericRangePropertyEditor) {
						//組み合わせで使うプロパティを通常のプロパティ扱いに
						NumericRangePropertyEditor de = (NumericRangePropertyEditor) prop.getEditor();
						PropertyItem dummy = new PropertyItem();
						dummy.setDispFlag(true);
						dummy.setPropertyName(de.getToPropertyName());
						dummy.setEditor(de.getEditor());
						propList.add(dummy);
					}
					propList.add(prop);
				}
			} else if (elem instanceof DefaultSection) {
				if (EntityViewUtil.isDisplayElement(entityDefinition.getName(), elem.getElementRuntimeId(), OutputType.BULK, null)) {
					propList.addAll(getProperty((DefaultSection) elem));
				}
			}
		}
		return propList;
	}

	/**
	 * 表示プロパティを取得します。
	 * @return プロパティの一覧
	 */
	private List<PropertyItem> getDisplayProperty() {
		List<PropertyItem> propList = new ArrayList<>();

		for (Section section : getView().getSections()) {
			if (!EntityViewUtil.isDisplayElement(entityDefinition.getName(), section.getElementRuntimeId(), OutputType.BULK, null)) {
				continue;
			}

			if (section instanceof DefaultSection) {
				DefaultSection ds = (DefaultSection) section;
				propList.addAll(getDisplayProperty(ds));
			}
		}
		return propList;
	}

	/**
	 * セクション内の表示プロパティ取得を取得します。
	 * @param section セクション
	 * @return プロパティの一覧
	 */
	private List<PropertyItem> getDisplayProperty(DefaultSection section) {
		List<PropertyItem> propList = new ArrayList<>();

		for (Element elem : section.getElements()) {
			if (!EntityViewUtil.isDisplayElement(entityDefinition.getName(), elem.getElementRuntimeId(), OutputType.BULK, null)) {
				continue;
			}

			if (elem instanceof PropertyItem) {
				PropertyItem prop = (PropertyItem) elem;
				if (prop.getEditor() instanceof JoinPropertyEditor) {
					//組み合わせで使うプロパティを通常のプロパティ扱いに
					JoinPropertyEditor je = (JoinPropertyEditor) prop.getEditor();
					for (NestProperty nest : je.getProperties()) {
						PropertyItem dummy = new PropertyItem();
						dummy.setDispFlag(true);
						dummy.setPropertyName(nest.getPropertyName());
						dummy.setEditor(nest.getEditor());
						propList.add(dummy);
					}
				}
				propList.add(prop);
			} else if (elem instanceof DefaultSection) {
				DefaultSection ds = (DefaultSection) elem;
				propList.addAll(getDisplayProperty(ds));
			}
		}
		return propList;
	}

	/**
	 * 更新するエンティティリスト、interrupterで利用されます。
	 */
	public List<Entity> getEntities() {
		List<Entity> entities = new ArrayList<>();
		for (String oid : getOids()) {
			for (Long version : getVersions(oid)) {
				Entity entity = newEntity();
				entity.setOid(oid);
				entity.setVersion(version);
				entity.setUpdateDate(getTimestamp(oid, version));
				entities.add(entity);
			}
		}
		return entities;
	}

	public BulkUpdateInterrupterHandler getBulkUpdateInterrupterHandler() {
		if (bulkInterrupterHandler == null) {
			BulkOperationInterrupter bulkInterrupter = createBulkInterrupter(getBulkInterrupterName());
			bulkInterrupterHandler = new BulkUpdateInterrupterHandler(request, this, bulkInterrupter);
		}
		return bulkInterrupterHandler;
	}

	protected String getBulkInterrupterName() {
		String viewName = getViewName();
		//FIXME SearchResult定義からカスタム一括更新クラスを取得します。
		SearchFormView view = FormViewUtil.getSearchFormView(entityDefinition, entityView, viewName);
		return view.getResultSection().getBulkUpdateInterrupterName();
	}

	protected BulkOperationInterrupter createBulkInterrupter(String className) {
		BulkOperationInterrupter interrupter = null;
		if (StringUtil.isNotEmpty(className)) {
			getLogger().debug("set bulk operation interrupter. class=" + className);
			try {
				interrupter = ucdm.createInstanceAs(BulkOperationInterrupter.class, className);
			} catch (ClassNotFoundException e) {
				getLogger().error(className + " can not instantiate.", e);
				throw new ApplicationException(resourceString(""));
			}
		}
		if (interrupter == null) {
			// 何もしないデフォルトInterrupter生成
			getLogger().debug("set default bulk operation interrupter.");
			interrupter = new BulkOperationInterrupter() {
			};
		}
		return interrupter;
	}

	/**
	 * 一括更新時空白のプロパティリストを取得します。
	 * @return propBlankList
	 */
	public List<PropertyItem> getBlankPropList() {
		return blankPropList;
	}

	@SuppressWarnings("unused")
	private class BulkCommandParams {

		private Map<String, Object> params;

		public BulkCommandParams(Integer row, String oid) {
			setRow(row);
			setOid(oid);
		}

		public Integer getRow() {
			return getValue(Constants.ID);
		}

		public void setRow(Integer row) {
			setValue(Constants.ID, row);
		}

		public String getOid() {
			return getValue(Constants.OID);
		}

		public void setOid(String oid) {
			setValue(Constants.OID, oid);
		}

		public Long getVersion() {
			return getValue(Constants.VERSION);
		}

		public void setVersion(Long version) {
			setValue(Constants.VERSION, version);
		}

		public Long getUpdateDate() {
			return getValue(Constants.TIMESTAMP);
		}

		public void setUpdateDate(Long updateDate) {
			setValue(Constants.TIMESTAMP, updateDate);
		}

		public void setValue(String name, Object value) {
			if (value == null && getValue(name) == null) {
				return;
			}
			if (params == null) {
				params = new HashMap<>();
			}

			if (value == null) {
				params.remove(name);
			} else {
				params.put(name, value);
			}
		}

		@SuppressWarnings("unchecked")
		public <T> T getValue(String name) {
			if (params != null) {
				return (T) params.get(name);
			}
			return null;
		}

		@Override
		public String toString() {
			return "BulkCommandParams [row=" + getRow() + ", oid=" + getOid() + ", version=" + getVersion() + ", updateDate=" + getUpdateDate() + "]";
		}
	}
}
