/*
 * Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.iplass.gem.GemConfigService;
import org.iplass.gem.command.CommandUtil;
import org.iplass.gem.command.Constants;
import org.iplass.gem.command.generic.detail.NestTableReferenceRegistHandler;
import org.iplass.gem.command.generic.detail.ReferenceRegistHandler;
import org.iplass.gem.command.generic.detail.RegistrationCommandContext;
import org.iplass.gem.command.generic.detail.RegistrationPropertyBaseHandler;
import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.LoadOption;
import org.iplass.mtp.entity.ValidateError;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.impl.util.ConvertUtil;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.EntityViewUtil;
import org.iplass.mtp.view.generic.FormViewUtil;
import org.iplass.mtp.view.generic.SearchFormView;
import org.iplass.mtp.view.generic.editor.DateRangePropertyEditor;
import org.iplass.mtp.view.generic.editor.JoinPropertyEditor;
import org.iplass.mtp.view.generic.editor.NestProperty;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor.ReferenceDisplayType;
import org.iplass.mtp.view.generic.element.Element;
import org.iplass.mtp.view.generic.element.property.PropertyColumn;
import org.iplass.mtp.view.generic.element.section.SearchResultSection;
import org.iplass.mtp.web.template.TemplateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BulkCommandContext extends RegistrationCommandContext {

	private static Logger logger = LoggerFactory.getLogger(BulkCommandContext.class);

	/** 検索画面用のFormレイアウト情報 */
	private SearchFormView view;

	private GemConfigService gemConfig = null;

	/** パラメータマップ。 key: oid, value: パラメータ対象  */
	private Map<String, BulkCommandParams> bulkCommandParams = new HashMap<>();

	/** 更新されたプロパティリスト */
	private List<BulkUpdatedProperty> updatedProps = new ArrayList<>();

	/** パラメータパターン */
	private Pattern pattern = Pattern.compile("^(\\d+)\\_(.+)$");

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

	public BulkCommandContext(RequestContext request, EntityManager entityLoader, EntityDefinitionManager definitionLoader) {
		super(request, entityLoader, definitionLoader);

		gemConfig = ServiceRegistry.getRegistry().getService(GemConfigService.class);
		init();
	}

	private void init() {
		populateBulkCommandParam(Constants.OID, String.class, true);
		populateBulkCommandParam(Constants.VERSION, Long.class, false);
		populateBulkCommandParam(Constants.TIMESTAMP, Long.class, false);

		populateBulkUpdatedProperty(Constants.BULK_UPDATED_PROP_NM, true);
		populateBulkUpdatedProperty(Constants.BULK_UPDATED_PROP_VALUE, false);
	}

	private void populateBulkCommandParam(String name, Class<?> cls, boolean create) {
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
				BulkCommandParams bulkParams = getBulkCommandParams(targetRow);
				if (create) {
					// targetParamをキーとして設定する
					if (bulkParams != null) {
						getLogger().error("duplicate row. row=" + targetRow);
						throw new ApplicationException(resourceString("command.generic.bulk.BulkCommandContext.duplicateRow"));
					}
					bulkCommandParams.put(targetParam, new BulkCommandParams(targetRow, targetParam));
				} else {
					if (bulkParams == null) {
						getLogger().error("selected row does not exist. param=" + param[i]);
						throw new ApplicationException(resourceString("command.generic.bulk.BulkCommandContext.invalidRow"));
					}
					bulkParams.setValue(name, ConvertUtil.convertFromString(cls, targetParam));
				}
			}
		}
	}

	private void populateBulkUpdatedProperty(String name, boolean create) {
		String[] param = (String[]) request.getParams(name);
		if (param != null) {
			for (int i = 0; i < param.length; i++) {
				String[] params = splitRowParam(param[i]);
				Integer updateNo = Integer.parseInt(params[0]);
				String targetParam = params[1];
				BulkUpdatedProperty updatedProp = getBulkUpdatedProperty(updateNo);
				if (create) {
					if (updatedProp != null) {
						getLogger().error("duplicate updateNo. updateNo=" + updateNo);
						throw new ApplicationException(resourceString("command.generic.bulk.BulkCommandContext.duplicateUpdateNo"));
					}
					updatedProps.add(new BulkUpdatedProperty(updateNo, targetParam));
				} else {
					if (updatedProp == null) {
						getLogger().error("updateNo does not exist. params=" + param[i]);
						throw new ApplicationException(resourceString("command.generic.bulk.BulkCommandContext.invalidUpdateNo"));
					}
					updatedProp.setValue(name, targetParam);
				}
			}
		}
	}

	private String[] splitRowParam(String rowParam) {
		Matcher m = pattern.matcher(rowParam);
		if (!m.matches()) {
			getLogger().error("invalid parameter format. rowParam=" + rowParam);
			throw new ApplicationException(resourceString("command.generic.bulk.BulkCommandContext.invalidFormat"));
		}
		String[] params = new String[] { m.group(1), m.group(2) };
		return params;
	}

	private BulkCommandParams getBulkCommandParams(Integer row) {
		List<BulkCommandParams> paramsList = bulkCommandParams.values().stream()
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

	private BulkUpdatedProperty getBulkUpdatedProperty(Integer updateNo) {
		List<BulkUpdatedProperty> updatedPropList = updatedProps.stream()
				.filter(p -> p.getUpdateNo().equals(updateNo))
				.collect(Collectors.toList());
		if (updatedPropList.size() == 0) {
			return null;
		} else if (updatedPropList.size() > 1) {
			getLogger().error("duplicate updateNo. updatedPropList=" + updatedPropList);
			throw new ApplicationException(resourceString("command.generic.bulk.BulkCommandContext.duplicateUpdateNo"));
		}
		return updatedPropList.get(0);
	}

	/**
	 * リクエストパラメータから参照型の更新データを作成します。
	 * @param p プロパティ定義
	 * @param prefix 参照型のプロパティのリクエストパラメータに設定されているプレフィックス
	 * @return プロパティの値
	 */
	@Override
	protected Object createReference(PropertyDefinition p, String prefix) {
		ReferenceProperty rp = (ReferenceProperty) p;
		String defName = rp.getObjectDefinitionName();
		//NestTable、ReferenceSectionの場合の件数取得
		//prefixが付くケース=NestTable内の多重参照なのであり得ない
		//→件数取れないため通常の参照扱いで処理が終わる
		Long count = getLongValue(prefix + p.getName() + "_count");
		if (p.getMultiplicity() == 1) {
			Entity entity = null;
			if (count == null) {
				String key = getParam(prefix + p.getName());
				entity = getRefEntity(rp.getObjectDefinitionName(), key);
			} else {
				List<Entity> list = getRefTableValues(rp, defName, count, prefix);
				if (list.size() > 0) entity = list.get(0);
			}
			return entity;
		} else {
			List<Entity> list = new ArrayList<Entity>();
			if (count == null) {
				String[] params = getParams(prefix + p.getName());
				if (params != null) {
					for (String key : params) {
						Entity entity = getRefEntity(rp.getObjectDefinitionName(), key);
						if (entity != null) list.add(entity);
					}
				}
			} else {
				//参照型で参照先のデータを作成・編集するケース
				list = getRefTableValues(rp, defName, count, prefix);
			}

			//マッピングクラスの配列を生成する
			Object[] array = null;
			EntityDefinition ed = getEntityDefinition();
			setEntityDefinition(definitionManager.get(defName));
			Entity emptyEntity = newEntity();
			setEntityDefinition(ed);

			array = (Object[]) Array.newInstance(emptyEntity.getClass(), list.size());
			return list.toArray(array);
		}
	}

	private Entity getRefEntity(String definitionName, String key) {
		Entity entity = null;
		String oid = null;
		Long version = null;
		if (key != null) {
			int lastIndex = key.lastIndexOf("_");

			if (lastIndex < 0) {
				oid = key;
			} else {
				oid = key.substring(0, lastIndex);
				version = CommandUtil.getLong(key.substring(lastIndex + 1));
			}
		}
		if (StringUtil.isNotBlank(oid)) {
			//バリデーションエラー時に消えないようにデータ読み込み
			//gemの設定により、参照を合わせて読み込むか切り替える
			if (gemConfig.isLoadWithReference()) {
				entity = entityManager.load(oid, version, definitionName);
			} else {
				entity = entityManager.load(oid, version, definitionName, new LoadOption(false, false));
			}
		}
		return entity;
	}

	/**
	 * リクエストパラメータからテーブルの参照型データの値を取得します。
	 * @param p プロパティ定義
	 * @param defName 参照型のEntity定義名
	 * @param count 参照データの最大件数
	 * @return 参照データのリスト
	 */
	private List<Entity> getRefTableValues(ReferenceProperty p, String defName, Long count, String prefix) {
		final List<Entity> list = new ArrayList<Entity>();
		EntityDefinition ed = getEntityDefinition();
		EntityDefinition red = definitionManager.get(defName);
		setEntityDefinition(red);//参照先の定義に詰め替える
		for (int i = 0; i < count; i++) {
			//データあり
			String paramPrefix = prefix + p.getName() + "[" + Integer.toString(i) + "].";
			String errorPrefix = (i != list.size() ? prefix + p.getName() + "[" + Integer.toString(list.size()) + "]." : null);
			Entity entity = createEntity(paramPrefix, errorPrefix);

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
					.findFirst().isPresent();

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
		Optional<PropertyColumn> ret = getProperty().stream().filter(pc -> pc.getPropertyName().equals(p.getName())).findFirst();
		if (ret.isPresent()) {
			addNestTableRegistHandler(p, list, red, ret.get());
		}

		return list;
	}

	private void addNestTableRegistHandler(ReferenceProperty p, List<Entity> list, EntityDefinition red, PropertyColumn property) {
		// ネストテーブルはプロパティ単位で登録可否決定
		if (!NestTableReferenceRegistHandler.canRegist(property, getRegistrationPropertyBaseHandler())) return;

		ReferencePropertyEditor editor = (ReferencePropertyEditor) property.getBulkUpdateEditor();

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

		ReferenceRegistHandler handler = NestTableReferenceRegistHandler.get(this, list, red, p, property, editor.getNestProperties(), getRegistrationPropertyBaseHandler());
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
				if (obj != null && obj.length > 0) return false;
			} else {
				if (entity.getValue(pd.getName()) != null) return false;
			}
		}
		return true;
	}

	@Override
	protected String getInterrupterName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getLoadEntityInterrupterName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected RegistrationPropertyBaseHandler<PropertyColumn> createRegistrationPropertyBaseHandler() {
		return new RegistrationPropertyBaseHandler<PropertyColumn>() {
			@Override
			public boolean isDispProperty(PropertyColumn property) {
				//一括更新プロパティエディタが未設定のプロパティは対象外にする
				return property.isDispFlag() && property.getBulkUpdateEditor() != null;
			}

			@Override
			public PropertyEditor getEditor(PropertyColumn property) {
				return property.getBulkUpdateEditor();
			}
		};
	}

	public Set<String> getOids() {
		return bulkCommandParams.keySet();
	}

	/**
	 * 一括更新画面用のFormレイアウト情報を取得します。
	 * @return 一括更新画面用のFormレイアウト情報
	 */
	@SuppressWarnings("unchecked")
	@Override
	public SearchFormView getView() {
		String viewName = getViewName();
		if (view == null) {
			view = FormViewUtil.getSearchFormView(entityDefinition, entityView, viewName);
		}
		return view;
	}

	/**
	 * 編集画面用のFormレイアウト情報を設定します。
	 * @param view 編集画面用のFormレイアウト情報
	 */
	public void setView(SearchFormView view) {
		this.view = view;
	}

	/**
	 * 新しいバージョンとして更新を行うかを取得します。
	 * @return 新しいバージョンとして更新を行うか
	 */
	public boolean isNewVersion() {
		String newVersion = getParam(Constants.NEWVERSION);
		return newVersion != null && "true".equals(newVersion);
	}

	/**
	 * リクエストから検索条件を取得します。
	 * @return 検索条件
	 */
	public String getSearchCond() {
		return getParam(Constants.SEARCH_COND);
	}

	/**
	 * リクエストから処理タイプを取得します。
	 * @return
	 */
	public String getExecType() {
		return getParam(Constants.EXEC_TYPE);
	}

	/**
	 * ロードしたデータをリクエストパラメータの値で上書きするかを取得します。
	 * @return
	 */
	public boolean isUpdateByParam() {
		String updateByParam = getParam("updateByParam");
		return updateByParam != null && "true".equals(updateByParam);
	}

	public Integer getRow(String oid) {
		Integer row = null;
		BulkCommandParams params = bulkCommandParams.get(oid);
		if (params != null) {
			row = params.getRow();
		}
		return row;
	}

	public Long getVersion(String oid) {
		Long version = null;
		BulkCommandParams params = bulkCommandParams.get(oid);
		if (params != null) {
			version = params.getVersion();
		}
		return version;
	}

	public Timestamp getTimestamp(String oid) {
		Timestamp ts = null;
		BulkCommandParams params = bulkCommandParams.get(oid);
		if (params != null) {
			Long l = params.getUpdateDate();
			if (l != null) {
				ts = new Timestamp(l);
			}
		}
		return ts;
	}

	public String getSelectAllType() {
		return getParam(Constants.BULK_UPDATE_SELECT_TYPE);
	}

	public String getBulkUpdatePropName() {
		return getParam(Constants.BULK_UPDATE_PROP_NM);
	}

	public List<BulkUpdatedProperty> getUpdatedProps() {
		return updatedProps;
	}

	public Object getBulkUpdatePropertyValue(String propertyName) {
		PropertyDefinition p = entityDefinition.getProperty(propertyName);
		return getPropValue(p, "");
	}

	public Entity createEntity(String oid) {
		Entity entity = createEntity("" , null);
		entity.setOid(oid);
		entity.setUpdateDate(getTimestamp(oid));
//		if (isVersioned()) {
		// バージョン管理にかかわらず、セットする問題ないかな..
		entity.setVersion(getVersion(oid));
//		}
//		setVirtualPropertyValue(entity);
		getRegistrationInterrupterHandler().dataMapping(entity);
		validate(entity);
		return entity;
	}

	private Entity createEntity(String paramPrefix, String errorPrefix) {
		Entity entity = newEntity();
		for (PropertyDefinition p : getPropertyList()) {
			if (skipProps.contains(p.getName())) continue;
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
		for (PropertyColumn property : getProperty()) {
			if (property.getBulkUpdateEditor() instanceof DateRangePropertyEditor) {
				//日付の逆転チェック
				DateRangePropertyEditor editor = (DateRangePropertyEditor) property.getBulkUpdateEditor();
				checkDateRange(editor, entity, property.getPropertyName(), editor.getToPropertyName(), "");
			} else if (property.getBulkUpdateEditor() instanceof ReferencePropertyEditor) {
				ReferencePropertyEditor editor = (ReferencePropertyEditor) property.getBulkUpdateEditor();
				Object val = entity.getValue(property.getPropertyName());

				Entity[] ary = null;
				if (val != null) {
					if (val instanceof Entity) {
						ary = new Entity[] {(Entity) val};
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
		if (from != null && to != null && from.compareTo(to) >= 0) {
			String errorMessage = TemplateUtil.getMultilingualString(editor.getErrorMessage(), editor.getLocalizedErrorMessageList());
			if (StringUtil.isBlank(errorMessage )) {
				errorMessage = resourceString("command.generic.bulk.BulkCommandContext.invalitDateRange");
			}
			ValidateError e = new ValidateError();
			e.setPropertyName(errorPrefix + fromName + "_" + editor.getToPropertyName());//fromだけだとメッセージが変なとこに出るので細工
			e.addErrorMessage(errorMessage);
			getErrors().add(e);
		}
	}

	/**
	 * 一括更新するプロパティを取得します。 組み合わせで使うプロパティである場合、通常のプロパティ扱いにします。
	 *
	 * @return 一括更新するプロパティ
	 */
	public List<PropertyColumn> getProperty() {
//		String execType = getExecType();
		List<PropertyColumn> propList = new ArrayList<PropertyColumn>();
		String updatePropName = getBulkUpdatePropName();
		if (StringUtil.isEmpty(updatePropName)) {
			getLogger().error("update property name is empty. updatePropName=" + updatePropName);
			throw new ApplicationException(resourceString("command.generic.bulk.BulkCommandContext.propNameNullValueErr"));
		}
		PropertyDefinition pd = getEntityDefinition().getProperty(updatePropName);
		if (pd == null) {
			getLogger().error("can not find property definition. updatePropName=" + updatePropName);
			throw new ApplicationException(resourceString("command.generic.bulk.BulkCommandContext.propDefNotFoundErr"));
		}

		SearchResultSection section = getView().getResultSection();
		List<PropertyColumn> propertyColumns = section.getElements().stream()
				.filter(e -> e instanceof PropertyColumn)
				.map(e -> (PropertyColumn) e)
				.filter(e -> getRegistrationPropertyBaseHandler().isDispProperty(e))
				.collect(Collectors.toList());

		for (PropertyColumn pc : propertyColumns) {
			if(pc.getPropertyName().equals(updatePropName)) {
				propList.add(pc);
				//組み合わせで使うプロパティを通常のプロパティ扱いに
				if (pc.getBulkUpdateEditor() instanceof DateRangePropertyEditor) {
					DateRangePropertyEditor de = (DateRangePropertyEditor) pc.getBulkUpdateEditor();
					PropertyColumn dummy = new PropertyColumn();
					dummy.setDispFlag(true);
					dummy.setPropertyName(de.getToPropertyName());
					dummy.setEditor(de.getEditor());
					propList.add(dummy);
				//組み合わせで使うプロパティを通常のプロパティ扱いに
				} else if (pc.getBulkUpdateEditor() instanceof JoinPropertyEditor) {
					JoinPropertyEditor je = (JoinPropertyEditor) pc.getBulkUpdateEditor();
					for (NestProperty nest : je.getProperties()) {
						PropertyColumn dummy = new PropertyColumn();
						dummy.setDispFlag(true);
						dummy.setPropertyName(nest.getPropertyName());
						dummy.setEditor(nest.getEditor());
						propList.add(dummy);
					}
				}
				break;
			}
		}

		return propList;
	}

	/**
	 * 表示対象の参照プロパティ名を取得します
	 * @return
	 */
	public List<String> getReferencePropertyName() {
		List<String> loadReferences = new ArrayList<String>();
		SearchResultSection section = getView().getResultSection();
		for (Element element : section.getElements()) {
			if (element instanceof PropertyColumn) {
				PropertyColumn property = (PropertyColumn) element;
				PropertyDefinition pd = getProperty(property.getPropertyName());
				if (pd instanceof ReferenceProperty) {
					//大量データ用のPropertyEditorを使わない参照プロパティのみ
					loadReferences.add(property.getPropertyName());
				}
			}
		}
		return loadReferences;
	}

	/**
	 * 更新可能な被参照（ネストテーブル、参照セクション）を定義内に保持しているかを取得します。
	 * @return
	 */
	public boolean hasUpdatableMappedByReference() {
		List<PropertyColumn> properties = getProperty();
		for (PropertyColumn property : properties) {
			PropertyDefinition pd = getProperty(property.getPropertyName());
			if (pd instanceof ReferenceProperty) {
				String mappedBy = ((ReferenceProperty) pd).getMappedBy();
				if (StringUtil.isBlank(mappedBy)) continue;
				if (property.getBulkUpdateEditor() instanceof ReferencePropertyEditor) {
					ReferencePropertyEditor editor = (ReferencePropertyEditor) property.getBulkUpdateEditor();
					if (editor.getDisplayType() == ReferenceDisplayType.NESTTABLE) {
						return true;
					}
				}
			}
		}
		return false;
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
				params = new HashMap<String, Object>();
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


	public static class BulkUpdatedProperty {

		private Map<String, Object> params;

		public BulkUpdatedProperty(Integer updateNo, String propertyName) {
			this(updateNo, propertyName, null);
		}

		public BulkUpdatedProperty(Integer updateNo, String propertyName, Object propertyValue) {
			setUpdateNo(updateNo);
			setPropertyName(propertyName);
			setPropertyValue(propertyValue);
		}

		public Integer getUpdateNo() {
			return getValue(Constants.ID);
		}

		public void setUpdateNo(Integer updateNo) {
			setValue(Constants.ID, updateNo);
		}

		public String getPropertyName() {
			return getValue(Constants.BULK_UPDATED_PROP_NM);
		}

		public void setPropertyName(String propertyName) {
			setValue(Constants.BULK_UPDATED_PROP_NM, propertyName);
		}

		public Object getPropertyValue() {
			return getValue(Constants.BULK_UPDATED_PROP_VALUE);
		}

		public void setPropertyValue(Object propertyValue) {
			setValue(Constants.BULK_UPDATED_PROP_VALUE, propertyValue);
		}

		public void setValue(String name, Object value) {
			if (value == null && getValue(name) == null) {
				return;
			}
			if (params == null) {
				params = new HashMap<String, Object>();
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
			return "BulkUpdatedProperty [updateNo=" + getUpdateNo() + ", propertyName=" + getPropertyName() + "]";
		}
	}
}
