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
	private Map<String, BulkCommandParams> bulkDetailCommandBaseParams = new HashMap<>();

	/** 更新されたプロパティリスト */
	private List<BulkUpdatedProperty> updatedProps = new ArrayList<>();

	/** パラメータパターン */
	private Pattern pattern = Pattern.compile("^(\\d+)\\_([^_]+)$");

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
		populateParams();
	}

	private void populateParams() {
		// BulkUpdateAllCommandからのChainの可能性があるので、Attributeから取得する
		String[] oid = (String[]) request.getAttribute(Constants.OID);
		if (oid == null || oid.length == 0) {
			oid = getParams(Constants.OID);
		}
		if (oid != null) {
			for (int i = 0; i < oid.length; i++) {
				// oidには先頭に「行番号_」が付加されているので分離する
				String[] params = splitRowParam(oid[i]);
				Integer targetRow = Integer.parseInt(params[0]);
				String targetOid = params[1];
				BulkCommandParams bulkParams = getBulkDetailCommandBaseParams(targetRow);
				if (bulkParams != null) {
					//TODO
					throw new ApplicationException();
				}
				bulkDetailCommandBaseParams.put(targetOid, new BulkCommandParams(targetRow, targetOid));
			}
		}

		// BulkUpdateAllCommandからのChainの可能性があるので、Attributeから取得する
		String[] version = (String[]) request.getAttribute(Constants.VERSION);
		if (version == null || version.length == 0) {
			version = getParams(Constants.VERSION);
		}
		if (version != null) {
			for (int i = 0; i < version.length; i++) {
				// versionには先頭に「行番号_」が付加されているので分離する
				String[] params = splitRowParam(version[i]);
				Integer targetRow = Integer.parseInt(params[0]);
				Long targetVersion = Long.parseLong(params[1]);
				BulkCommandParams bulkParams = getBulkDetailCommandBaseParams(targetRow);
				if (bulkParams == null) {
					// TODO
					throw new ApplicationException();
				}
				bulkParams.setVersion(targetVersion);
			}
		}

		// BulkUpdateAllCommandからのChainの可能性があるので、Attributeから取得する
		String[] timestamp = (String[]) request.getAttribute(Constants.TIMESTAMP);
		if (timestamp == null || timestamp.length == 0) {
			timestamp = getParams(Constants.TIMESTAMP);
		}
		if (timestamp != null) {
			for (int i = 0; i < timestamp.length; i++) {
				// timestampには先頭に「行番号_」が付加されているので分離する
				String[] params = splitRowParam(timestamp[i]);
				Integer targetRow = Integer.parseInt(params[0]);
				Long targetTimestamp = Long.parseLong(params[1]);
				BulkCommandParams bulkParams = getBulkDetailCommandBaseParams(targetRow);
				if (bulkParams == null) {
					// TODO
					throw new ApplicationException();
				}
				bulkParams.setUpdateDate(targetTimestamp);
			}
		}

		String[] updatedPropNm = (String[]) request.getParams(Constants.BULK_UPDATED_PROP_NM);
		if (updatedPropNm != null) {
			for (int i = 0; i < updatedPropNm.length; i++) {
				String[] params = splitRowParam(updatedPropNm[i]);
				Integer nth = Integer.parseInt(params[0]);
				String propName = params[1];
				BulkUpdatedProperty updatedProp = getBulkUpdatedProperty(nth);
				if (updatedProp != null) {
					throw new ApplicationException();
				}
				updatedProps.add(new BulkUpdatedProperty(nth, propName));
			}
		}

		String[] updatedPropVal = (String[]) request.getParams(Constants.BULK_UPDATED_PROP_VALUE);
		if (updatedPropVal != null) {
			for (int i = 0; i < updatedPropNm.length; i++) {
				String[] params = splitRowParam(updatedPropVal[i]);
				Integer nth = Integer.parseInt(params[0]);
				String propVal = params[1];
				BulkUpdatedProperty updatedProp = getBulkUpdatedProperty(nth);
				if (updatedProp == null) {
					throw new ApplicationException();
				}
				updatedProp.setPropertyValue(propVal);
			}
		}
	}

	private String[] splitRowParam(String rowParam) {
		Matcher m = pattern.matcher(rowParam);
		if (!m.matches()) {
			// TODO
			throw new ApplicationException();
		}
		String[] params = new String[] { m.group(1), m.group(2) };
		return params;
	}

	private BulkCommandParams getBulkDetailCommandBaseParams(Integer row) {
		List<BulkCommandParams> paramsList = bulkDetailCommandBaseParams.values().stream()
				.filter(p -> p.getRow().equals(row))
				.collect(Collectors.toList());
		if (paramsList.size() == 0) {
			return null;
		} else if (paramsList.size() > 1) {
			// TODO
			throw new ApplicationException("duplicate parameter: " + paramsList);
		}
		return paramsList.get(0);
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
		RegistrationPropertyBaseHandler<PropertyColumn> propBaseHandler = new RegistrationPropertyBaseHandler<PropertyColumn>() {
			@Override
			public boolean isHidden(PropertyColumn property) {
				return !property.isDispFlag() || property.getBulkUpdateEditor() == null;
			}

			@Override
			public PropertyEditor getEditor(PropertyColumn property) {
				return property.getBulkUpdateEditor();
			}
		};
		// ネストテーブルはプロパティ単位で登録可否決定
		if (!NestTableReferenceRegistHandler.canRegist(property, propBaseHandler)) return;

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

		ReferenceRegistHandler handler = NestTableReferenceRegistHandler.get(this, list, red, p, property, editor.getNestProperties(), propBaseHandler);
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

	public BulkUpdatedProperty getBulkUpdatedProperty(Integer nth) {
		List<BulkUpdatedProperty> updatedPropList = updatedProps.stream()
				.filter(p -> p.getNth().equals(nth))
				.collect(Collectors.toList());
		if (updatedPropList.size() == 0) {
			return null;
		} else if (updatedPropList.size() > 1) {
			// TODO
			throw new ApplicationException("duplicate updated property: " + updatedPropList);
		}
		return updatedPropList.get(0);
	}

	public Set<String> getOids() {
		return bulkDetailCommandBaseParams.keySet();
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
		BulkCommandParams params = bulkDetailCommandBaseParams.get(oid);
		if (params != null) {
			row = params.getRow();
		}
		return row;
	}

	public Long getVersion(String oid) {
		Long version = null;
		BulkCommandParams params = bulkDetailCommandBaseParams.get(oid);
		if (params != null) {
			version = params.getVersion();
		}
		return version;
	}

	public Timestamp getTimestamp(String oid) {
		Timestamp ts = null;
		BulkCommandParams params = bulkDetailCommandBaseParams.get(oid);
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

	public Object getBulkUpdatePropDispValue() {
		String name = getBulkUpdatePropName();
		PropertyDefinition p = entityDefinition.getProperty(name);
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

	public List<PropertyColumn> getProperty() {
//		String execType = getExecType();
		List<PropertyColumn> propList = new ArrayList<PropertyColumn>();
		String propName = getBulkUpdatePropName();
		if (StringUtil.isEmpty(propName)) {
			// TODO
			throw new ApplicationException();
		}
		PropertyDefinition pd = getEntityDefinition().getProperty(propName);
		if (pd == null) {
			// TODO
			throw new ApplicationException();
		}

		SearchResultSection section = getView().getResultSection();
		List<PropertyColumn> propertyColumns = section.getElements().stream()
				.filter(e -> e instanceof PropertyColumn)
				.map(e -> (PropertyColumn)e)
				.filter(e -> e.getBulkUpdateEditor() != null) //一括更新プロパティエディタが未設定のプロパティは対象外にする
				.collect(Collectors.toList());

		for (PropertyColumn pc : propertyColumns) {
			if(pc.getPropertyName().equals(propName)) {
				propList.add(pc);
				if (pc.getBulkUpdateEditor() instanceof DateRangePropertyEditor) {
					DateRangePropertyEditor de = (DateRangePropertyEditor) pc.getBulkUpdateEditor();
					PropertyColumn dummy = new PropertyColumn();
					dummy.setDispFlag(true);
					dummy.setPropertyName(de.getToPropertyName());
					dummy.setEditor(de.getEditor());
					propList.add(dummy);
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

	@SuppressWarnings("unused")
	private class BulkCommandParams {

		private Integer row;
		private String oid;
		private Long version;
		private Long updateDate;

		public BulkCommandParams(Integer row, String oid) {
			this.row = row;
			this.oid = oid;
		}

		public Integer getRow() {
			return row;
		}

		public void setRow(Integer row) {
			this.row = row;
		}

		public String getOid() {
			return oid;
		}

		public void setOid(String oid) {
			this.oid = oid;
		}

		public Long getVersion() {
			return version;
		}

		public void setVersion(Long version) {
			this.version = version;
		}

		public Long getUpdateDate() {
			return updateDate;
		}

		public void setUpdateDate(Long updateDate) {
			this.updateDate = updateDate;
		}

		@Override
		public String toString() {
			return "BulkDetailCommandBaseParameters [row=" + row + ", oid=" + oid + ", version=" + version + ", updateDate=" + updateDate + "]";
		}
	}


	public static class BulkUpdatedProperty {
		private Integer nth;
		private String propertyName;
		private Object propertyValue;

		public BulkUpdatedProperty(Integer nth, String propertyName) {
			this(nth, propertyName, null);
		}

		public BulkUpdatedProperty(Integer nth, String propertyName, Object propertyValue) {
			this.nth = nth;
			this.propertyName = propertyName;
			this.propertyValue = propertyValue;
		}

		public Integer getNth() {
			return nth;
		}

		public void setNth(Integer nth) {
			this.nth = nth;
		}

		public String getPropertyName() {
			return propertyName;
		}

		public void setPropertyName(String propertyName) {
			this.propertyName = propertyName;
		}

		public Object getPropertyValue() {
			return propertyValue;
		}

		public void setPropertyValue(Object propertyValue) {
			this.propertyValue = propertyValue;
		}

		@Override
		public String toString() {
			return "BulkUpdatedProperty [nth=" + nth + ", propertyName=" + propertyName + "]";
		}
	}

}
