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

package org.iplass.gem.command.generic.detail;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.iplass.gem.GemConfigService;
import org.iplass.gem.command.CommandUtil;
import org.iplass.gem.command.Constants;
import org.iplass.gem.command.ViewUtil;
import org.iplass.gem.command.generic.detail.handler.CheckPermissionLimitConditionOfButtonHandler;
import org.iplass.gem.command.generic.detail.handler.ShowDetailLayoutViewEvent;
import org.iplass.gem.command.generic.detail.handler.ShowDetailViewEventHandler;
import org.iplass.gem.command.generic.detail.handler.ShowEditViewEventHandler;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityKey;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.LoadOption;
import org.iplass.mtp.entity.ValidateError;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.BooleanProperty;
import org.iplass.mtp.entity.definition.properties.DateProperty;
import org.iplass.mtp.entity.definition.properties.DateTimeProperty;
import org.iplass.mtp.entity.definition.properties.DecimalProperty;
import org.iplass.mtp.entity.definition.properties.FloatProperty;
import org.iplass.mtp.entity.definition.properties.IntegerProperty;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.entity.definition.properties.SelectProperty;
import org.iplass.mtp.entity.definition.properties.StringProperty;
import org.iplass.mtp.entity.definition.properties.TimeProperty;
import org.iplass.mtp.impl.util.ConvertUtil;
import org.iplass.mtp.impl.view.generic.DetailFormViewRuntime;
import org.iplass.mtp.impl.view.generic.FormViewRuntimeUtil;
import org.iplass.mtp.impl.view.generic.editor.MetaBinaryPropertyEditor.BinaryPropertyEditorRuntime;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.DetailFormView;
import org.iplass.mtp.view.generic.DetailFormView.CopyTarget;
import org.iplass.mtp.view.generic.DetailFormViewHandler;
import org.iplass.mtp.view.generic.EntityViewUtil;
import org.iplass.mtp.view.generic.FormViewUtil;
import org.iplass.mtp.view.generic.OutputType;
import org.iplass.mtp.view.generic.editor.BinaryPropertyEditor;
import org.iplass.mtp.view.generic.editor.DateRangePropertyEditor;
import org.iplass.mtp.view.generic.editor.JoinPropertyEditor;
import org.iplass.mtp.view.generic.editor.LabelablePropertyEditor;
import org.iplass.mtp.view.generic.editor.NestProperty;
import org.iplass.mtp.view.generic.editor.NumericRangePropertyEditor;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.RangePropertyEditor;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor.ReferenceDisplayType;
import org.iplass.mtp.view.generic.editor.UserPropertyEditor;
import org.iplass.mtp.view.generic.element.Element;
import org.iplass.mtp.view.generic.element.VirtualPropertyItem;
import org.iplass.mtp.view.generic.element.property.PropertyElement;
import org.iplass.mtp.view.generic.element.property.PropertyItem;
import org.iplass.mtp.view.generic.element.section.DefaultSection;
import org.iplass.mtp.view.generic.element.section.ReferenceSection;
import org.iplass.mtp.view.generic.element.section.Section;
import org.iplass.mtp.web.template.TemplateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 詳細コマンド関連の情報を管理するクラス。
 * @author lis3wg
 */
public class DetailCommandContext extends RegistrationCommandContext
		implements ShowDetailViewEventHandler, ShowEditViewEventHandler {

	private static Logger logger = LoggerFactory.getLogger(DetailCommandContext.class);

	/** 編集画面用のFormレイアウト情報 */
	private DetailFormView view;

	private List<DetailFormViewHandler> detailFormViewHandlers;

	private GemConfigService gemConfig = null;

	private Set<String> useUserPropertyEditorPropertyNameList;

	/** Label形式の除外プロパティ */
	private List<PropertyElement> excludeLabelableProperties;

	/** 更新対象ロードエンティティ*/
	private Entity currentEntity;
	/** 編集エンティティ*/
	private Entity editedEntity;

	@Override
	protected Logger getLogger() {
		return logger;
	}

	/**
	 * コンストラクタ
	 * @param request リクエスト
	 */
	public DetailCommandContext(RequestContext request, EntityManager entityLoader,
			EntityDefinitionManager definitionLoader) {
		super(request, entityLoader, definitionLoader);

		init();
	}

	public DetailCommandContext(RequestContext request, String defName, String viewName, EntityManager entityLoader,
			EntityDefinitionManager definitionLoader) {
		super(request, defName, viewName, entityLoader, definitionLoader);

		init();
	}

	private void init() {
		gemConfig = ServiceRegistry.getRegistry().getService(GemConfigService.class);
	}

	/**
	 * 編集画面用のFormレイアウト情報を取得します。
	 * @return 編集画面用のFormレイアウト情報
	 */
	@SuppressWarnings("unchecked")
	@Override
	public DetailFormView getView() {
		String viewName = getViewName();
		if (view == null) {
			view = FormViewUtil.getDetailFormView(entityDefinition, entityView, viewName);
		}
		return view;
	}

	/**
	 * 編集画面用のFormレイアウト情報を設定します。
	 * @param view 編集画面用のFormレイアウト情報
	 */
	public void setView(DetailFormView view) {
		this.view = view;
	}

	/*
	 * 表示判定用Bindエンティティを取得します。
	 */
	public Entity getDispControlBindEntity() {
		if (logger.isTraceEnabled()) {
			logger.trace("Bind display control entity is :" + (editedEntity != null ? "edited" : "loaded"));
		}
		return editedEntity != null ? editedEntity : currentEntity;
	}

	/**
	 * 更新対象ロードエンティティを設定します。
	 * @param currentEntity 対象エンティティ
	 */
	public void setCurrentEntity(Entity currentEntity) {
		this.currentEntity = currentEntity;
	}

	/**
	 * 編集エンティティを設定します。
	 * @param editedEntity 編集エンティティ
	 */
	protected void setEditedEntity(Entity editedEntity) {
		this.editedEntity = editedEntity;
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
				//詳細編集で非表示なら更新対象外
				return EntityViewUtil.isDisplayElement(entityDefinition.getName(), property.getElementRuntimeId(), OutputType.EDIT,
						getDispControlBindEntity())
						&& !property.isHideDetail();
			}

			@Override
			public PropertyEditor getEditor(PropertyItem property) {
				return property.getEditor();
			}
		};
	}

	/**
	 * フォーム内のプロパティを取得します。
	 * @param view 画面定義
	 * @return プロパティの一覧
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PropertyItem> getProperty() {
		String execType = getExecType();
		List<PropertyItem> propList = new ArrayList<>();
		for (Section section : getView().getSections()) {
			if (section instanceof DefaultSection) {
				if (EntityViewUtil.isDisplayElement(getDefinitionName(), section.getElementRuntimeId(), OutputType.EDIT, getDispControlBindEntity())
						&& !((DefaultSection) section).isHideDetail() && ViewUtil.dispElement(execType, section)) {
					propList.addAll(getProperty((DefaultSection) section));
				}
			} else if (section instanceof ReferenceSection) {
				// 参照セクションは同一名の定義が複数の場合があるのでまとめる
				ReferenceSection rs = (ReferenceSection) section;
				if (EntityViewUtil.isDisplayElement(getDefinitionName(), section.getElementRuntimeId(), OutputType.EDIT, getDispControlBindEntity())
						&& !rs.isHideDetail() && ViewUtil.dispElement(execType, rs)) {
					Optional<ReferenceSectionPropertyItem> ret = propList.stream().filter(p -> p instanceof ReferenceSectionPropertyItem)
							.map(p -> (ReferenceSectionPropertyItem) p)
							.filter(p -> p.getPropertyName().equals(rs.getPropertyName()))
							.findFirst();

					if (ret.isPresent()) {
						ret.get().getSections().add(rs);
					} else {
						propList.add(createReferenceSectionPropertyItem(rs));
					}
				}
			}
		}
		return propList;
	}

	/**
	 * フォーム内の更新対象プロパティを取得します。
	 * @param view 画面定義
	 * @return 更新対象プロパティの一覧
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PropertyItem> getUpdateProperty() {
		List<PropertyItem> propList = getProperty();

		//EditorのLabel形式のチェック
		List<PropertyItem> updateList = propList.stream().filter(property -> {
			if (property.getEditor() instanceof LabelablePropertyEditor) {
				LabelablePropertyEditor editor = (LabelablePropertyEditor) property.getEditor();
				if (editor.isLabel() && !editor.isUpdateWithLabelValue()) {
					return false;
				}
			}
			return true;
		}).collect(Collectors.toList());

		return updateList;
	}

	/**
	 * セクション内のプロパティ取得を取得します。
	 * @param section セクション
	 * @return プロパティの一覧
	 */
	@SuppressWarnings("unchecked")
	private List<PropertyItem> getProperty(DefaultSection section) {
		String execType = getExecType();
		List<PropertyItem> propList = new ArrayList<>();
		for (Element elem : section.getElements()) {
			if (elem instanceof PropertyItem) {
				PropertyItem prop = (PropertyItem) elem;
				if (getRegistrationPropertyBaseHandler().isDispProperty(prop) && ViewUtil.dispElement(execType, prop)) {
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
					} else if (prop.getEditor() instanceof RangePropertyEditor) {
						//組み合わせで使うプロパティを通常のプロパティ扱いに
						RangePropertyEditor de = (RangePropertyEditor) prop.getEditor();
						PropertyItem dummy = new PropertyItem();
						dummy.setDispFlag(true);
						dummy.setPropertyName(de.getToPropertyName());
						dummy.setEditor(de.getEditor());
						propList.add(dummy);
					}
					propList.add(prop);
				}
			} else if (elem instanceof DefaultSection) {
				if (EntityViewUtil.isDisplayElement(getDefinitionName(), elem.getElementRuntimeId(), OutputType.EDIT, getDispControlBindEntity())
						&& !((DefaultSection) elem).isHideDetail() && ViewUtil.dispElement(execType, elem)) {
					propList.addAll(getProperty((DefaultSection) elem));
				}
			} else if (elem instanceof ReferenceSection) {
				// 参照セクションは同一名の定義が複数の場合があるのでまとめる
				ReferenceSection rs = (ReferenceSection) elem;
				if (EntityViewUtil.isDisplayElement(getDefinitionName(), elem.getElementRuntimeId(), OutputType.EDIT, getDispControlBindEntity())
						&& !rs.isHideDetail() && ViewUtil.dispElement(execType, rs)) {
					Optional<ReferenceSectionPropertyItem> ret = propList.stream().filter(p -> p instanceof ReferenceSectionPropertyItem)
							.map(p -> (ReferenceSectionPropertyItem) p)
							.filter(p -> p.getPropertyName().equals(rs.getPropertyName()))
							.findFirst();

					if (ret.isPresent()) {
						ret.get().getSections().add(rs);
					} else {
						propList.add(createReferenceSectionPropertyItem(rs));
					}
				}
			}
		}
		return propList;
	}

	/**
	 * 参照セクションからプロパティを取得します。
	 * @param section セクション
	 * @return プロパティ
	 */
	private ReferenceSectionPropertyItem createReferenceSectionPropertyItem(ReferenceSection section) {
		ReferenceSectionPropertyItem property = new ReferenceSectionPropertyItem();
		property.setPropertyName(section.getPropertyName());
		//		property.setDispFlag(section.getDispFlag());
		property.getSections().add(section);
		return property;
	}

	/**
	 * 表示対象の参照プロパティ名を取得します
	 * @return
	 */
	public List<String> getReferencePropertyName() {
		//表示しているプロパティだけとりだす。表示/編集は分からないのでとりあえず両方。
		Set<String> loadReferences = new HashSet<>();
		for (Section section : getView().getSections()) {
			if (section instanceof DefaultSection) {
				getReferencePropertyName((DefaultSection) section, loadReferences);
			} else if (section instanceof ReferenceSection) {
				ReferenceSection rs = (ReferenceSection) section;
				loadReferences.add(rs.getPropertyName());
			}
		}
		return new ArrayList<>(loadReferences);
	}

	private void getReferencePropertyName(DefaultSection section, Set<String> loadReferences) {
		for (Element element : section.getElements()) {
			if (element instanceof PropertyItem) {
				PropertyItem property = (PropertyItem) element;
				PropertyDefinition pd = getProperty(property.getPropertyName());
				if (pd instanceof ReferenceProperty) {
					//大量データ用のPropertyEditorを使わない参照プロパティのみ
					loadReferences.add(property.getPropertyName());
				}
				if (property.getEditor() instanceof JoinPropertyEditor) {
					JoinPropertyEditor jpe = (JoinPropertyEditor) property.getEditor();
					for (NestProperty nest : jpe.getProperties()) {
						//JoinPropertyエディターのネストプロパティでの参照プロパティ
						if (nest.getEditor() instanceof ReferencePropertyEditor) {
							loadReferences.add(nest.getPropertyName());
						}
					}
				}
			} else if (element instanceof DefaultSection) {
				getReferencePropertyName((DefaultSection) element, loadReferences);
			} else if (element instanceof ReferenceSection) {
				ReferenceSection rs = (ReferenceSection) element;
				loadReferences.add(rs.getPropertyName());
			}
		}
	}

	// 同一プロパティの参照セクションをまとめておくPropertyItem
	public class ReferenceSectionPropertyItem extends PropertyItem {

		private static final long serialVersionUID = -2527968770756351039L;

		private List<ReferenceSection> sections = new ArrayList<>();

		public List<ReferenceSection> getSections() {
			return sections;
		}

		public ReferenceSection getFirstSection() {
			return sections.size() > 0 ? sections.get(0) : null;
		}

		public ReferenceSection getSection(int index) {
			Optional<ReferenceSection> ret = sections.stream().filter(s -> s.getIndex() == index).findFirst();
			if (ret.isPresent()) {
				return ret.get();
			}
			return null;
		}

		@Override
		public boolean isDispFlag() {
			// dispFlagがfalseのものがあればfalse扱い
			for (ReferenceSection section : sections) {
				if (!EntityViewUtil.isDisplayElement(getDefinitionName(), section.getElementRuntimeId(), OutputType.EDIT, getDispControlBindEntity())) {
					return false;
				}
			}
			return true;
		}

		@Override
		public boolean isHideDetail() {
			// hideDetailがfalseのものがあればfalse扱い
			for (ReferenceSection section : sections) {
				if (!section.isHideDetail()) {
					return false;
				}
			}
			return true;
		}
	}

	/**
	 * リクエストからOIDを取得します。
	 * @return OID
	 */
	public String getOid() {
		return getParam(Constants.OID);
	}

	/**
	 * リクエストからバージョンを取得します。
	 * @return バージョン
	 */
	public Long getVersion() {
		return getLongValue(Constants.VERSION);
	}

	/**
	 * リクエストからフィルタ名を取得します。
	 * @return フィルタ名
	 */
	public String getFilterName() {
		return getParam(Constants.FILTER_NAME);
	}

	/**
	 * コピーを行うかを取得します。
	 * @return コピーを行うか
	 */
	public boolean isCopy() {
		String copy = getParam(Constants.COPY);
		return copy != null && "true".equals(copy);
	}

	public CopyTarget getCopyTarget() {
		String copyTarget = getParam(Constants.COPY_TARGET);
		if (copyTarget == null || copyTarget.isEmpty()) {
			return CopyTarget.SHALLOW;
		}
		return CopyTarget.getEnum(copyTarget);
	}

	@Override
	public boolean isNewVersion() {
		String newVersion = getParam(Constants.NEWVERSION);
		return newVersion != null && "true".equals(newVersion);
	}

	@Override
	public boolean isLoadVersioned() {
		if (isVersioned()) {
			// バージョンロードが無効かをチェック（下位互換対応）
			if (isLoadLatestVersionedEntity()) {
				return false;
			}
			// 検索時に全バージョンを検索しているかを確認
			String searchCond = getSearchCond();
			if (StringUtil.isNotEmpty(searchCond)) {
				for (String condition : searchCond.split("&")) {
					if (condition.startsWith(Constants.SEARCH_ALL_VERSION + "=")
							|| condition.startsWith(Constants.SEARCH_ALL_VERSION_DETAIL + "=")) {
						String[] value = condition.split("=");
						if (value.length > 1) {
							return "1".equals(value[1]);
						}
					}
				}
			}
		} else {
			// 検索時に保存時データを検索しているかを確認
			String searchCond = getSearchCond();
			if (StringUtil.isNotEmpty(searchCond)) {
				for (String condition : searchCond.split("&")) {
					if (condition.startsWith(Constants.SEARCH_SAVED_VERSION + "=")
							|| condition.startsWith(Constants.SEARCH_SAVED_VERSION_DETAIL + "=")) {
						String[] value = condition.split("=");
						if (value.length > 1) {
							return "1".equals(value[1]);
						}
					}
				}
			}
		}
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
	 * 更新可能な被参照（ネストテーブル、参照セクション）を定義内に保持しているかを取得します。
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

				if (property instanceof ReferenceSectionPropertyItem) {
					return true;
				} else if (property.getEditor() instanceof ReferencePropertyEditor) {
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

	/**
	 * リクエストから処理タイプを取得します。
	 * @return
	 */
	public String getExecType() {
		return getParam(Constants.EXEC_TYPE);
	}

	/**
	 * リクエストからキャンセル時の遷移パスを取得します。
	 * @return 遷移パス
	 */
	public String getBackPath() {
		return getParam(Constants.BACK_PATH);
	}

	/**
	 * ロードしたデータをリクエストパラメータの値で上書きするかを取得します。
	 * @return
	 */
	public boolean isUpdateByParam() {
		String updateByParam = getParam("updateByParam");
		return updateByParam != null && "true".equals(updateByParam);
	}

	/**
	 * リクエストのパラメータからEntiyを作成します。
	 * @return 画面で入力したデータ
	 */
	public Entity createEntity() {
		Entity entity = createEntity("", null);
		entity.setUpdateDate(getTimestamp());
		if (isVersioned()) {
			entity.setVersion(getVersion());
		}

		//仮想プロパティ値の反映
		setVirtualPropertyValue(entity);

		//Label形式のEditorが設定されているプロパティ値の反映
		setLabelablePropertyValue(entity);

		getRegistrationInterrupterHandler().dataMapping(entity);

		validate(entity);

		//FIXME 更新の時のみ対象エンティティを設定します。
		setEditedEntity(entity);
		return entity;
	}

	/**
	 * リクエストのパラメータからEntiyを作成します。
	 * @param paramPrefix 参照型のプロパティのリクエストパラメータに設定されているプレフィックス
	 * @param errorPrefix エラーが発生した場合にValidateErrorにセットするプレフィックス
	 * @return 画面で入力したデータ
	 */
	private Entity createEntity(String paramPrefix, String errorPrefix) {
		Entity entity = newEntity();
		for (PropertyDefinition p : getPropertyList()) {
			Object value = getPropValue(p, paramPrefix);
			entity.setValue(p.getName(), value);

			if (errorPrefix != null) {
				String name = paramPrefix + p.getName();
				//Entity生成時にエラーが発生していないかチェックして置き換え
				String errorName = errorPrefix + p.getName();
				getErrors().stream()
						.filter(error -> error.getPropertyName().equals(name))
						.forEach(error -> error.setPropertyName(errorName));
			}
		}
		return entity;
	}

	private Timestamp getTimestamp() {
		Timestamp ts = null;
		Long l = getLongValue(Constants.TIMESTAMP);
		if (l != null) {
			ts = new Timestamp(l);
		}
		return ts;
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

		//NestTable、ReferenceSectionの場合の件数取得
		//prefixが付くケース=NestTable内の多重参照なのであり得ない
		//→件数取れないため通常の参照扱いで処理が終わる
		Long count = getLongValue(prefix + p.getName() + "_count");
		String isReferenceSection = getParam("isReferenceSection_" + prefix + p.getName());
		if (p.getMultiplicity() == 1) {
			List<Entity> list = null;
			if (count == null) {
				String key = getParam(prefix + p.getName());
				list = getRefEntities(rp.getObjectDefinitionName(), new String[] { key });
			} else {
				if (isReferenceSection != null && "true".equals(isReferenceSection)) {
					list = getRefSectionValues(rp, defName, count, prefix);
				} else {
					list = getRefTableValues(rp, defName, count, prefix);
				}
			}
			if (list.size() > 0) {
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
				if (isReferenceSection != null && "true".equals(isReferenceSection)) {
					list = getRefSectionValues(rp, defName, count, prefix);
				} else {
					list = getRefTableValues(rp, defName, count, prefix);
				}
			}

			if (list != null && !list.isEmpty()) {
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
	 * NestTable、ReferenceSection以外の参照先編集データを返します。
	 *
	 * @param definitionName 参照先Entity定義名
	 * @param keyParameters oidとversionのパラメータ情報
	 * @return 編集データ
	 */
	private List<Entity> getRefEntities(String definitionName, String[] keyParameters) {

		List<EntityKey> keys = Arrays.stream(keyParameters)
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
		Optional<PropertyItem> ret = getProperty().stream().filter(pi -> pi.getPropertyName().equals(p.getName())).findFirst();
		if (ret.isPresent()) {
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
		ReferenceRegistOption option = null;
		if (currentEntity != null) {
			option = getRegistrationInterrupterHandler().getNestTableRegistOption(red, p.getName());

			// isSpecifyAllPropertiesがtrue、且つReference項目が除外、且つNestされたEntityの個々プロパティも全て指定なしの場合
			// 参照先EntityにおけるEntityのデータ追加、更新不可
			if (option.isSpecifyAllProperties() && !option.isSpecifiedAsReference()
					&& option.getSpecifiedUpdateNestProperties().isEmpty()) {
				return;
			}
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
	 * ReferenceSectionの編集データを返します。
	 *
	 * @param p 対象ReferenceProperty定義
	 * @param defName 参照先Entity定義名
	 * @param count 件数
	 * @param prefix 参照型のプロパティのリクエストパラメータに設定されているプレフィックス
	 * @return ReferenceSectionの編集データ
	 */
	private List<Entity> getRefSectionValues(ReferenceProperty p, String defName, Long count, String prefix) {
		List<Entity> list = new ArrayList<>();
		EntityDefinition ed = getEntityDefinition();
		EntityDefinition red = definitionManager.get(defName);
		setEntityDefinition(red);//参照先の定義に詰め替える

		List<PropertyItem> properties = getProperty();
		List<ReferenceSectionValue> valList = new ArrayList<>();

		Optional<ReferenceSectionPropertyItem> ret = properties.stream()
				.filter(pi -> pi instanceof ReferenceSectionPropertyItem)
				.map(pi -> (ReferenceSectionPropertyItem) pi)
				.filter(pi -> pi.getPropertyName().equals(p.getName())).findFirst();
		ReferenceSectionPropertyItem rsProperty = ret.isPresent() ? ret.get() : null;

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

				if (rsProperty != null) {
					// entity,sectionでまとめる
					if (p.getMultiplicity() == 1) {
						if (rsProperty.getSections().size() > 0) {
							ReferenceSection section = rsProperty.getSections().get(0);
							ReferenceSectionValue rsv = new ReferenceSectionValue();
							rsv.entity = entity;
							rsv.section = section;
							valList.add(rsv);
						}
					} else {
						// 多重度1以外は表示順プロパティとデータのインデックスを考慮
						String idx = getParam("referenceSectionIndex_" + prefix + p.getName() + "[" + Integer.toString(i) + "]");
						int index = CommandUtil.getInteger(idx);
						ReferenceSection section = rsProperty.getSection(index);
						if (section != null) {
							ReferenceSectionValue rsv = new ReferenceSectionValue();
							rsv.index = index;
							rsv.entity = entity;
							rsv.section = section;
							valList.add(rsv);
						}
					}
				}

				list.add(entity);
			}
		}
		setEntityDefinition(ed);//元の定義に詰め替える

		// 参照セクション用の登録処理を追加
		if (rsProperty != null) {
			addReferenceSectionRegistHandler(p, valList, red, rsProperty);
		}

		return list;
	}

	private void addReferenceSectionRegistHandler(final ReferenceProperty p, final List<ReferenceSectionValue> list, EntityDefinition red,
			ReferenceSectionPropertyItem property) {
		// 参照セクションはプロパティ単位で登録可否決定
		if (!ReferenceSectionReferenceRegistHandler.canRegist(property)) {
			return;
		}

		// カスタム登録処理による参照セクションの更新制御
		ReferenceRegistOption option = null;
		if (currentEntity != null) {
			option = getRegistrationInterrupterHandler().getNestTableRegistOption(red, p.getName());

			// isSpecifyAllPropertiesがtrue、且つReference項目が除外、且つNestされたEntityの個々プロパティも全て指定なしの場合
			// 参照先EntityにおけるEntityのデータ更新不可
			if (option.isSpecifyAllProperties() && !option.isSpecifiedAsReference()
					&& option.getSpecifiedUpdateNestProperties().isEmpty()) {
				return;
			}
		}

		ReferenceRegistHandler handler = ReferenceSectionReferenceRegistHandler.get(this, list, red, p, property, option);
		if (handler != null) {
			//handler.setForceUpdate(forceUpadte); //参照セクションはSection毎に個別設定になるので、Handler内で設定
			getReferenceRegistHandlers().add(handler);
		}
	}

	public class ReferenceSectionValue {
		private Integer index;
		private Entity entity;
		private ReferenceSection section;

		public Integer getIndex() {
			return index;
		}

		public Entity getEntity() {
			return entity;
		}

		public void setEntity(Entity entity) {
			this.entity = entity;
		}

		public ReferenceSection getSection() {
			return section;
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

	/**
	 * 仮想プロパティが設定されたプロパティの値を制御します。
	 *
	 * @param entity Entityデータ
	 */
	private void setVirtualPropertyValue(Entity entity) {
		List<VirtualPropertyItem> virtualProperties = getVirtualProperty();
		for (VirtualPropertyItem property : virtualProperties) {
			PropertyDefinition p = EntityViewUtil.getPropertyDefinition(property);
			Object value = null;
			boolean isMultiple = p.getMultiplicity() != 1;//現状必ず1
			String name = p.getName();
			if (p instanceof BooleanProperty) {
				value = isMultiple ? getBooleanValues(name, p.getMultiplicity()) : getBooleanValue(name);
			} else if (p instanceof DateProperty) {
				value = isMultiple ? getDateValues(name) : getDateValue(name);
			} else if (p instanceof DateTimeProperty) {
				value = isMultiple ? getTimestampValues(name) : getTimestampValue(name);
			} else if (p instanceof DecimalProperty) {
				value = isMultiple ? getDecimalValues(name) : getDecimalValue(name);
			} else if (p instanceof FloatProperty) {
				value = isMultiple ? getDoubleValues(name) : getDoubleValue(name);
			} else if (p instanceof IntegerProperty) {
				value = isMultiple ? getLongValues(name) : getLongValue(name);
			} else if (p instanceof SelectProperty) {
				value = isMultiple ? getSelectValues(name) : getSelectValue(name);
			} else if (p instanceof StringProperty) {
				value = isMultiple ? getStringValues(name) : getStringValue(name);
			} else if (p instanceof TimeProperty) {
				value = isMultiple ? getTimeValues(name) : getTimeValue(name);
			}

			entity.setValue(p.getName(), value);
		}
	}

	/**
	 * フォーム内の仮想プロパティを取得します。
	 * @param view 画面定義
	 * @return 仮想プロパティの一覧
	 */
	private List<VirtualPropertyItem> getVirtualProperty() {
		String execType = getExecType();
		List<VirtualPropertyItem> propList = new ArrayList<>();
		for (Section section : getView().getSections()) {
			if (section instanceof DefaultSection) {
				if (EntityViewUtil.isDisplayElement(getDefinitionName(), section.getElementRuntimeId(), OutputType.EDIT, getDispControlBindEntity())
						&& !((DefaultSection) section).isHideDetail() && ViewUtil.dispElement(execType, section)) {
					propList.addAll(getVirtualProperty((DefaultSection) section));
				}
			}
		}
		return propList;
	}

	/**
	 * セクション内の仮想プロパティ取得を取得します。
	 * @param section セクション
	 * @return 仮想プロパティの一覧
	 */
	private List<VirtualPropertyItem> getVirtualProperty(DefaultSection section) {
		String execType = getExecType();
		List<VirtualPropertyItem> propList = new ArrayList<>();
		for (Element elem : section.getElements()) {
			if (elem instanceof VirtualPropertyItem) {
				VirtualPropertyItem prop = (VirtualPropertyItem) elem;
				if (EntityViewUtil.isDisplayElement(getDefinitionName(), prop.getElementRuntimeId(), OutputType.EDIT, getDispControlBindEntity())
						&& !prop.isHideDetail() && ViewUtil.dispElement(execType, prop)) {
					propList.add(prop);
				}
			} else if (elem instanceof DefaultSection) {
				if (EntityViewUtil.isDisplayElement(getDefinitionName(), elem.getElementRuntimeId(), OutputType.EDIT, getDispControlBindEntity())
						&& !((DefaultSection) elem).isHideDetail() && ViewUtil.dispElement(execType, elem)) {
					propList.addAll(getVirtualProperty((DefaultSection) elem));
				}
			}
		}
		return propList;
	}

	/**
	 * Label形式のPropertyEditorが設定されたプロパティの値を制御します。
	 *
	 * @param entity Entityデータ
	 */
	private void setLabelablePropertyValue(Entity entity) {
		List<PropertyElement> excludeProperties = getExcludeLabelableProperty();
		for (PropertyElement element : excludeProperties) {
			//登録エラー時の値を保持
			entity.setValue(Constants.LABELABLE_EDITOR_VALUE + element.getPropertyName(), entity.getValue(element.getPropertyName()));
			entity.setValue(element.getPropertyName(), null);
		}
	}

	/**
	 * 更新失敗時のEntityデータ制御処理
	 *
	 * @param entity Entityデータ
	 */
	public void rollbackEntity(Entity entity) {
		List<PropertyElement> excludeProperties = getExcludeLabelableProperty();
		for (PropertyElement element : excludeProperties) {
			//Label値を復元
			entity.setValue(element.getPropertyName(), entity.getValue(Constants.LABELABLE_EDITOR_VALUE + element.getPropertyName()));
			entity.setValue(Constants.LABELABLE_EDITOR_VALUE + element.getPropertyName(), null);
		}
	}

	/**
	 * View内のLabel除外プロパティを取得します。
	 * @return Label除外プロパティのリスト
	 */
	private List<PropertyElement> getExcludeLabelableProperty() {
		if (excludeLabelableProperties != null) {
			return excludeLabelableProperties;
		}
		List<PropertyElement> excludeList = new ArrayList<>();
		for (Section section : getView().getSections()) {
			if (section instanceof DefaultSection) {
				excludeList.addAll(getExcludeLabelableProperty((DefaultSection) section));
			}
		}
		this.excludeLabelableProperties = excludeList;
		return excludeLabelableProperties;
	}

	/**
	 * DefaultSection内のLabel除外プロパティを取得します。
	 *
	 * @param section DefaultSection
	 * @return Label除外プロパティのリスト
	 */
	private List<PropertyElement> getExcludeLabelableProperty(DefaultSection section) {
		String execType = getExecType();
		List<PropertyElement> excludeList = new ArrayList<>();
		if (EntityViewUtil.isDisplayElement(getDefinitionName(), section.getElementRuntimeId(), OutputType.EDIT, getDispControlBindEntity())
				&& !section.isHideDetail() && ViewUtil.dispElement(execType, section)) {

			boolean isInsert = (currentEntity == null);
			for (Element element : section.getElements()) {
				if (element instanceof DefaultSection) {
					excludeList.addAll(getExcludeLabelableProperty((DefaultSection) element));
				} else if (element instanceof PropertyItem) {
					PropertyItem prop = (PropertyItem) element;
					if (prop.getEditor() instanceof LabelablePropertyEditor) {
						LabelablePropertyEditor editor = (LabelablePropertyEditor) prop.getEditor();
						//更新時は値セットは除外しないため追加時のみチェック
						if (isInsert && editor.isLabel()) {
							if (EntityViewUtil.isDisplayElement(getDefinitionName(), prop.getElementRuntimeId(), OutputType.EDIT,
									getDispControlBindEntity())
									&& !prop.isHideDetail() && ViewUtil.dispElement(execType, prop)) {
								if (!editor.isInsertWithLabelValue()) {
									excludeList.add(prop);
								}
							}
						}
					}
				} else if (element instanceof VirtualPropertyItem) {
					VirtualPropertyItem prop = (VirtualPropertyItem) element;
					if (prop.getEditor() instanceof LabelablePropertyEditor) {
						LabelablePropertyEditor editor = (LabelablePropertyEditor) prop.getEditor();
						//更新時は値セットは除外しないため追加時のみチェック
						if (isInsert && editor.isLabel()) {
							if (EntityViewUtil.isDisplayElement(getDefinitionName(), prop.getElementRuntimeId(), OutputType.EDIT,
									getDispControlBindEntity())
									&& !prop.isHideDetail() && ViewUtil.dispElement(execType, prop)) {
								if (!editor.isInsertWithLabelValue()) {
									excludeList.add(prop);
								}
							}
						}
					}
				}
			}
		}
		return excludeList;
	}

	/**
	 * UserPropertyEditorを利用しているか
	 * @param isDetail true:詳細編集、false:詳細表示
	 * @return UserPropertyEditorを利用しているか
	 */
	public boolean isUseUserPropertyEditor(boolean isDetail) {
		Set<String> propNameList = getUseUserPropertyEditorPropertyName(isDetail);
		return !propNameList.isEmpty();
	}

	/**
	 * UserPropertyEditorを利用しているプロパティ名の一覧を取得します。
	 * @param isDetail true:詳細編集、false:詳細表示
	 * @return UserPropertyEditorを利用しているプロパティ名の一覧
	 */
	public Set<String> getUseUserPropertyEditorPropertyName(boolean isDetail) {

		if (useUserPropertyEditorPropertyNameList != null) {
			return useUserPropertyEditorPropertyNameList;
		}

		useUserPropertyEditorPropertyNameList = new HashSet<>();
		for (PropertyItem property : getDisplayProperty(isDetail)) {
			String propertyName = property.getPropertyName();

			if (property.getEditor() instanceof ReferencePropertyEditor) {
				// ネストの項目を確認
				ReferencePropertyEditor editor = (ReferencePropertyEditor) property.getEditor();
				if (!editor.getNestProperties().isEmpty()) {
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
				if (!nestEditor.getNestProperties().isEmpty()) {
					Set<String> nest = getUseUserPropertyEditorNestPropertyName(nestEditor);
					for (String nestPropertyName : nest) {
						String _nestPropertyName = property.getPropertyName() + "." + nestPropertyName;
						ret.add(_nestPropertyName);
					}
				}
			} else if (property.getEditor() instanceof UserPropertyEditor) {
				ret.add(property.getPropertyName());
			}
		}
		return ret;
	}

	/**
	 * 標準の入力チェック以外のチェック、PropertyEditor絡みのもの
	 * @param entity
	 */
	protected void validate(Entity entity) {
		List<PropertyItem> properties = getDisplayProperty(true);
		DetailFormViewRuntime formViewRuntime = FormViewRuntimeUtil.getFormViewRuntime(getDefinitionName(), getViewName(),
				DetailFormViewRuntime.class);
		for (PropertyItem property : properties) {
			if (property.getEditor() instanceof DateRangePropertyEditor) {
				//日付の逆転チェック
				DateRangePropertyEditor editor = (DateRangePropertyEditor) property.getEditor();
				checkDateRange(editor, entity, property.getPropertyName(), editor.getToPropertyName(), "");
			} else if (property.getEditor() instanceof NumericRangePropertyEditor) {
				//数値の逆転チェック
				NumericRangePropertyEditor editor = (NumericRangePropertyEditor) property.getEditor();
				checkNumericRange(editor, entity, property.getPropertyName(), editor.getToPropertyName(), "");
			} else if (property.getEditor() instanceof BinaryPropertyEditor) {
				checkBinary(formViewRuntime, property.getPropertyName(), entity, "");
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
							} else if (np.getEditor() instanceof BinaryPropertyEditor) {
								checkBinary(formViewRuntime, np.getPropertyName(), ary[i], errorPrefix);
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
				setValidateErrorMessage(editor, fromName, errorPrefix, "command.generic.detail.DetailCommandContext.inputNumericRangeErr");
			}
		} else if (from_tmp != null && to_tmp == null) {
			if (!editor.isInputNullTo()) {
				setValidateErrorMessage(editor, fromName, errorPrefix, "command.generic.detail.DetailCommandContext.inputNumericRangeErr");
			}
		} else if (from_tmp != null && to_tmp != null) {
			boolean result = false;

			if (editor.isEquivalentInput()) {
				result = (from_tmp.compareTo(to_tmp) > 0) ? true : false;
			} else {
				result = (from_tmp.compareTo(to_tmp) >= 0) ? true : false;
			}
			if (result) {
				setValidateErrorMessage(editor, fromName, errorPrefix, "command.generic.detail.DetailCommandContext.invalidNumericRange");
			}
		}
	}

	/**
	 * バイナリプロパティのチェック
	 *
	 * <p>
	 * チェック内容は以下の通り
	 *
	 * <ul>
	 * <li>ファイルタイプについて、プロパティエディタもしくは、GemConfigService の受け入れ MIME Type パターンのチェック</li>
	 * </ul>
	 * </p>
	 * @param editor プロパティエディタ
	 * @param propertyName プロパティ名
	 * @param entity エンティティ
	 * @param propertyPrefix ネストプロパティの上位プロパティ名
	 */
	private void checkBinary(DetailFormViewRuntime viewRuntime, String propertyName, Entity entity, String propertyPrefix) {
		Object propertyValue = entity.getValue(propertyName);
		if (null == propertyValue) {
			// 値が null であれば何もしない
			return;
		}

		GemConfigService service = ServiceRegistry.getRegistry().getService(GemConfigService.class);

		// SectionRuntime のプロパティはネストプロパティを意識した形で情報を保管している。
		// そのため、ネストプロパティの場合は、parent[0].child というプロパティ名でプロパティエディタを取得する
		String fullPropertyName = propertyPrefix + propertyName;
		BinaryPropertyEditorRuntime editorRuntime = FormViewRuntimeUtil.getPropertyEditorRuntime(viewRuntime, fullPropertyName,
				BinaryPropertyEditorRuntime.class);

		List<BinaryReference> errorBinaryList = resolveValue(entity, propertyName, (BinaryReference value) -> {
			// MIME Type パターンのチェック
			boolean isAccept = true;
			// ファイル MIME Type の検査
			if (null != editorRuntime && null != editorRuntime.getUploadAcceptMimeTypesPattern()) {
				// プロパティエディタに許可する MIME Type が指定されている場合は、プロパティエディタを優先する
				isAccept = editorRuntime.getUploadAcceptMimeTypesPattern().matcher(value.getType()).matches();

			} else if (null != service.getBinaryUploadAcceptMimeTypesPattern()) {
				// プロパティエディタに設定が無く、GemConfigServiceの受け入れ許可設定が存在する場合は、GemConfigService の設定で許可設定を行う
				isAccept = service.getBinaryUploadAcceptMimeTypesPattern().matcher(value.getType()).matches();
			}
			// else {
			// // プロパティエディタ、GemServiceConfig に設定が無い場合はチェックしない
			// }

			return isAccept;
		});

		if (!errorBinaryList.isEmpty()) {
			// 許可されていない（パターンマッチしない）ファイルタイプの場合は、エラーメッセージ設定
			String errorMessage = resourceString("command.generic.detail.DetailCommandContext.unacceptedFileType",
					String.join(",", errorBinaryList.stream().map(v -> v.getName()).collect(Collectors.toList())));
			ValidateError e = new ValidateError();
			e.setPropertyName(fullPropertyName);
			e.addErrorMessage(errorMessage);
			getErrors().add(e);
		}
	}

	/**
	 * エンティティの値を解決（単一、配列）し検証処理を実行する
	 *
	 * @param <R> プロパティデータ型
	 * @param entity エンティティデータ
	 * @param propertyName 対象プロパティ名
	 * @param logic 検証処理。検証処理の返却値は次の通り（正常終了: true, 問題あり: false）。
	 * @return 問題のあったデータリスト
	 */
	@SuppressWarnings("unchecked")
	protected <R> List<R> resolveValue(Entity entity, String propertyName, Function<R, Boolean> logic) {
		Object value = entity.getValue(propertyName);

		List<R> result = new ArrayList<>();

		if (null != value && value.getClass().isArray()) {
			// 値が配列の場合
			for (R o : (R[]) value) {
				if (!logic.apply(o)) {
					result.add(o);
				}
			}

		} else {
			// 値が単一値。値が null の場合も単一値として検証を実施する
			if (!logic.apply((R) value)) {
				result.add((R) value);
			}
		}

		return result;
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
	 * 表示プロパティを取得します。
	 * @param isDetail true:詳細編集、false:詳細表示
	 * @return プロパティの一覧
	 */
	private List<PropertyItem> getDisplayProperty(boolean isDetail) {
		List<PropertyItem> propList = new ArrayList<>();

		String execType = getExecType();
		OutputType outputType = isDetail ? OutputType.EDIT : OutputType.VIEW;
		for (Section section : getView().getSections()) {
			if (!EntityViewUtil.isDisplayElement(getDefinitionName(), section.getElementRuntimeId(), outputType, getDispControlBindEntity())) {
				continue;
			}

			if (section instanceof DefaultSection) {
				DefaultSection ds = (DefaultSection) section;
				if ((isDetail && !ds.isHideDetail() && ViewUtil.dispElement(execType, ds)) || (!isDetail && !ds.isHideView())) {
					propList.addAll(getDisplayProperty(ds, isDetail));
				}
			} else if (section instanceof ReferenceSection) {
				ReferenceSection rs = (ReferenceSection) section;
				if ((isDetail && !rs.isHideDetail() && ViewUtil.dispElement(execType, rs)) || (!isDetail && !rs.isHideView())) {
					propList.add(getDisplayProperty(rs, outputType));
				}
			}
		}
		return propList;
	}

	/**
	 * セクション内の表示プロパティ取得を取得します。
	 * @param section セクション
	 * @param isDetail true:詳細編集、false:詳細表示
	 * @return プロパティの一覧
	 */
	private List<PropertyItem> getDisplayProperty(DefaultSection section, boolean isDetail) {
		List<PropertyItem> propList = new ArrayList<>();

		String execType = getExecType();
		OutputType outputType = isDetail ? OutputType.EDIT : OutputType.VIEW;
		for (Element elem : section.getElements()) {
			if (!EntityViewUtil.isDisplayElement(getDefinitionName(), elem.getElementRuntimeId(), outputType, getDispControlBindEntity())) {
				continue;
			}

			if (elem instanceof PropertyItem) {
				PropertyItem prop = (PropertyItem) elem;
				if ((isDetail && !prop.isHideDetail() && ViewUtil.dispElement(execType, prop)) || (!isDetail && !prop.isHideView())) {
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
				}
			} else if (elem instanceof DefaultSection) {
				DefaultSection ds = (DefaultSection) elem;
				if ((isDetail && !ds.isHideDetail() && ViewUtil.dispElement(execType, ds)) || (!isDetail && !ds.isHideView())) {
					propList.addAll(getDisplayProperty(ds, isDetail));
				}
			} else if (elem instanceof ReferenceSection) {
				ReferenceSection rs = (ReferenceSection) elem;
				if ((isDetail && !rs.isHideDetail() && ViewUtil.dispElement(execType, rs)) || (!isDetail && !rs.isHideView())) {
					propList.add(getDisplayProperty(rs, outputType));
				}
			}
		}
		return propList;
	}

	/**
	 * 参照セクションから表示プロパティを取得します。
	 * @param section セクション
	 * @param isDetail true:詳細編集、false:詳細表示
	 * @return プロパティ
	 */
	private PropertyItem getDisplayProperty(ReferenceSection section, OutputType outputType) {

		PropertyItem property = new PropertyItem();
		property.setPropertyName(section.getPropertyName());
		property.setDispFlag(
				EntityViewUtil.isDisplayElement(getDefinitionName(), section.getElementRuntimeId(), outputType, getDispControlBindEntity()));

		ReferencePropertyEditor editor = new ReferencePropertyEditor();
		editor.setDisplayType(ReferenceDisplayType.NESTTABLE);
		editor.setObjectName(section.getDefintionName());
		editor.setNestProperties(section.getProperties());
		property.setEditor(editor);
		return property;
	}

	public boolean isShallowCopyLobData() {
		return gemConfig.isShallowCopyLobData();
	}

	@Override
	public void fireShowDetailViewEvent(DetailFormViewData detailFormViewData) {

		ShowDetailLayoutViewEvent event = new ShowDetailLayoutViewEvent(getRequest(), getDefinitionName(), getViewName(), detailFormViewData);
		for (DetailFormViewHandler handler : getDetailFormViewHandlers()) {
			handler.onShowDetailView(event);
		}
	}

	@Override
	public void fireShowEditViewEvent(DetailFormViewData detailFormViewData) {

		ShowDetailLayoutViewEvent event = new ShowDetailLayoutViewEvent(getRequest(), getDefinitionName(), getViewName(), detailFormViewData);
		for (DetailFormViewHandler handler : getDetailFormViewHandlers()) {
			handler.onShowEditView(event);
		}
	}

	private List<DetailFormViewHandler> getDetailFormViewHandlers() {
		if (detailFormViewHandlers == null) {
			detailFormViewHandlers = new ArrayList<>();
			if (getView().getDetailFormViewHandlerName() != null) {
				for (String handlerClassName : getView().getDetailFormViewHandlerName()) {
					detailFormViewHandlers.add(createDetailFormViewHandler(handlerClassName));
				}
			}
			//ボタンの範囲権限チェックを行う場合は、先頭にHandlerを追加
			if (getView().isCheckEntityPermissionLimitConditionOfButton()) {
				if (logger.isDebugEnabled()) {
					logger.debug("add CheckPermissionLimitConditionOfButtonHandler to the first of detail form view handler.");
				}
				detailFormViewHandlers.add(0, new CheckPermissionLimitConditionOfButtonHandler());
			}
		}
		return detailFormViewHandlers;
	}

	private DetailFormViewHandler createDetailFormViewHandler(String handlerClassName) {
		DetailFormViewHandler handler = null;
		if (StringUtil.isNotEmpty(handlerClassName)) {
			if (logger.isDebugEnabled()) {
				logger.debug("create detail form view handler. class=" + handlerClassName);
			}
			try {
				handler = ucdm.createInstanceAs(DetailFormViewHandler.class, handlerClassName);
			} catch (ClassNotFoundException e) {
				logger.error(handlerClassName + " can not instantiate.", e);
				throw new EntityRuntimeException(resourceString("command.generic.detail.DetailCommandContext.internalErr"));
			}
		}
		return handler;
	}
}
