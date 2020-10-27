/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.view.generic;

import java.util.List;

import org.iplass.gem.AutoGenerateSetting;
import org.iplass.gem.AutoGenerateSetting.DisplayPosition;
import org.iplass.gem.GemConfigService;
import org.iplass.gem.command.Constants;
import org.iplass.gem.command.GemResourceBundleUtil;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.entity.definition.VersionControlType;
import org.iplass.mtp.entity.definition.properties.BinaryProperty;
import org.iplass.mtp.entity.definition.properties.ExpressionProperty;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.entity.definition.properties.SelectProperty;
import org.iplass.mtp.entity.definition.properties.selectvalue.SelectValueDefinition;
import org.iplass.mtp.entity.definition.properties.selectvalue.SelectValueDefinitionManager;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.TenantI18nInfo;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.editor.AutoNumberPropertyEditor;
import org.iplass.mtp.view.generic.editor.AutoNumberPropertyEditor.AutoNumberDisplayType;
import org.iplass.mtp.view.generic.editor.BinaryPropertyEditor;
import org.iplass.mtp.view.generic.editor.BinaryPropertyEditor.BinaryDisplayType;
import org.iplass.mtp.view.generic.editor.BooleanPropertyEditor;
import org.iplass.mtp.view.generic.editor.DatePropertyEditor;
import org.iplass.mtp.view.generic.editor.DateTimePropertyEditor.DateTimeDisplayType;
import org.iplass.mtp.view.generic.editor.DecimalPropertyEditor;
import org.iplass.mtp.view.generic.editor.ExpressionPropertyEditor;
import org.iplass.mtp.view.generic.editor.ExpressionPropertyEditor.ExpressionDisplayType;
import org.iplass.mtp.view.generic.editor.FloatPropertyEditor;
import org.iplass.mtp.view.generic.editor.IntegerPropertyEditor;
import org.iplass.mtp.view.generic.editor.LongTextPropertyEditor;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor;
import org.iplass.mtp.view.generic.editor.SelectPropertyEditor;
import org.iplass.mtp.view.generic.editor.StringPropertyEditor;
import org.iplass.mtp.view.generic.editor.StringPropertyEditor.StringDisplayType;
import org.iplass.mtp.view.generic.editor.TimePropertyEditor;
import org.iplass.mtp.view.generic.editor.TimestampPropertyEditor;
import org.iplass.mtp.view.generic.editor.UserPropertyEditor;
import org.iplass.mtp.view.generic.element.property.PropertyColumn;
import org.iplass.mtp.view.generic.element.property.PropertyItem;
import org.iplass.mtp.view.generic.element.section.DefaultSection;
import org.iplass.mtp.view.generic.element.section.SearchConditionSection;
import org.iplass.mtp.view.generic.element.section.SearchResultSection;
import org.iplass.mtp.view.generic.element.section.VersionSection;

/**
 * フォーム定義用ユーティリティクラス
 * @author lis3wg
 */
public class FormViewUtil {

	/**
	 * viewNameで指定した検索画面定義を取得します。
	 * 自動生成対象のViewの場合、自動生成した画面定義を返します。
	 *
	 * @param ed Entity定義
	 * @param ev 画面定義
	 * @param viewName View名
	 * @return 検索画面定義
	 */
	public static SearchFormView getSearchFormView(final EntityDefinition ed, final EntityView ev, final String viewName) {
		SearchFormView form = null;
		if (ev!= null) {
			if (ev.isAutoGenerateSearchView(viewName)) {
				//自動設定の対象の場合はViewの有無問わず自動生成
				form = createDefaultSearchFormView(ed);
				form.setName(viewName);
			} else {
				form = ev.getSearchFormView(viewName);
			}
		}
		if (form == null && StringUtil.isEmpty(viewName)) {
			//defaultの場合、何もなければ自動生成
			form = createDefaultSearchFormView(ed);
		}

		return form;
	}

	/**
	 * viewNameで指定した一括更新画面定義を取得します。
	 * 自動生成対象のViewの場合、自動生成した画面定義を返します。
	 *
	 * @param ed Entity定義
	 * @param ev 画面定義
	 * @param viewName View名
	 * @return 一括更新画面定義
	 */
	public static BulkFormView getBulkFormView(final EntityDefinition ed, final EntityView ev, final String viewName) {
		BulkFormView form = null;
		if (ev!= null) {
			if (ev.isAutoGenerateBulkView(viewName)) {
				//自動設定の対象の場合はViewの有無問わず自動生成
				form = createDefaultBulkFormView(ed);
				form.setName(viewName);
			} else {
				form = ev.getBulkFormView(viewName);
			}
		}
		if (form == null && StringUtil.isEmpty(viewName)) {
			//defaultの場合、何もなければ自動生成
			form = createDefaultBulkFormView(ed);
		}

		return form;
	}

	/**
	 * デフォルトの検索画面用定義を生成します。
	 * 表示名も設定します。
	 *
	 * @param ed Entity定義
	 * @return デフォルトの検索画面用定義
	 */
	public static SearchFormView createDefaultSearchFormView(EntityDefinition ed) {
		return createDefaultSearchFormView(ed, true);
	}

	/**
	 * デフォルトの検索画面用定義を生成します。
	 *
	 * @param ed Entity定義
	 * @param isLoadDisplayLabel 表示名をセットするか
	 * @return デフォルトの検索画面用定義
	 */
	public static SearchFormView createDefaultSearchFormView(EntityDefinition ed, boolean isLoadDisplayLabel) {
		SearchFormView view = new SearchFormView();
		SearchConditionSection condSection = new SearchConditionSection();
		condSection.setColNum(1);
		condSection.setConditionDispCount(5);

		GemConfigService service = ServiceRegistry.getRegistry().getService(GemConfigService.class);
		AutoGenerateSetting setting = service.getAutoGenerateSetting();

		//システム項目（TOP）
		if (setting.getSystemPropertyDisplayPosition() == DisplayPosition.TOP) {
			createSystemSearchConditionItem(condSection, ed, isLoadDisplayLabel, setting);
		}

		//基本項目
		condSection.addElement(createNameProperty(ed.getProperty(Entity.NAME), isLoadDisplayLabel));
		if (ed.getVersionControlType() == VersionControlType.TIMEBASE) {
			condSection.addElement(createTimestampProperty(
					ed.getProperty(Entity.START_DATE), isLoadDisplayLabel));
			condSection.addElement(createTimestampProperty(
					ed.getProperty(Entity.END_DATE), isLoadDisplayLabel));
		}

		//オブジェクト情報
		for (PropertyDefinition pd : ed.getDeclaredPropertyList()) {
			//バイナリは明示的に指定した場合だけ、デフォルトは出さない
			if (pd instanceof BinaryProperty) continue;
			//多重度1以外の参照も明示的に指定した場合だけ
			else if (pd instanceof ReferenceProperty) {
				ReferenceProperty rp = (ReferenceProperty) pd;
				if (rp.getMultiplicity() != 1) continue;
			}

			PropertyItem p = new PropertyItem();
			p.setDispFlag(true);
			p.setPropertyName(pd.getName());
			if (isLoadDisplayLabel) {
				p.setDisplayLabel(pd.getDisplayName());
				p.setLocalizedDisplayLabelList(pd.getLocalizedDisplayNameList());
			}
			p.setEditor(getDefaultEditor(pd, isLoadDisplayLabel));
			condSection.addElement(p);
		}

		//システム項目（BOTTOM）
		if (setting.getSystemPropertyDisplayPosition() == DisplayPosition.BOTTOM) {
			createSystemSearchConditionItem(condSection, ed, isLoadDisplayLabel, setting);
		}

		view.addSection(condSection);

		SearchResultSection resultSection = new SearchResultSection();

		//システム項目（TOP）
		if (setting.getSystemPropertyDisplayPosition() == DisplayPosition.TOP) {
			createSystemSearchResultColumn(resultSection, ed, isLoadDisplayLabel, setting);
		}

		//基本項目
		resultSection.addElement(createNamePropertyColumn(ed.getProperty(Entity.NAME), isLoadDisplayLabel));
		if (ed.getVersionControlType() == VersionControlType.TIMEBASE) {
			resultSection.addElement(createTimestampPropertyColumn(
					ed.getProperty(Entity.START_DATE), isLoadDisplayLabel));
			resultSection.addElement(createTimestampPropertyColumn(
					ed.getProperty(Entity.END_DATE), isLoadDisplayLabel));
		}

		//オブジェクト情報
		for (PropertyDefinition pd : ed.getDeclaredPropertyList()) {
			//バイナリは明示的に指定した場合だけ、デフォルトは出さない
			if (pd instanceof BinaryProperty) continue;
			//多重度1以外の参照も明示的に指定した場合だけ
			else if (pd instanceof ReferenceProperty) {
				ReferenceProperty rp = (ReferenceProperty) pd;
				if (rp.getMultiplicity() != 1) continue;
			}
			PropertyColumn p = new PropertyColumn();
			p.setDispFlag(true);
			p.setPropertyName(pd.getName());
			if (isLoadDisplayLabel) {
				p.setDisplayLabel(pd.getDisplayName());
				p.setLocalizedDisplayLabelList(pd.getLocalizedDisplayNameList());
			}
			p.setEditor(getDefaultEditor(pd, isLoadDisplayLabel));
			resultSection.addElement(p);
		}

		//システム項目（BOTTOM）
		if (setting.getSystemPropertyDisplayPosition() == DisplayPosition.BOTTOM) {
			createSystemSearchResultColumn(resultSection, ed, isLoadDisplayLabel, setting);
		}

		view.addSection(resultSection);

		return view;

	}

	/**
	 * viewNameで指定した詳細画面定義を取得します。
	 * 自動生成対象のViewの場合、自動生成した画面定義を返します。
	 *
	 * @param ed Entity定義
	 * @param ev 画面定義
	 * @param viewName View名
	 * @return 詳細画面定義
	 */
	public static DetailFormView getDetailFormView(final EntityDefinition ed, final EntityView ev, final String viewName) {
		DetailFormView form = null;
		if (ev!= null) {
			if (ev.isAutoGenerateDetailView(viewName)) {
				//自動設定の対象の場合はViewの有無問わず自動生成
				form = createDefaultDetailFormView(ed);
				form.setName(viewName);
			} else {
				form = ev.getDetailFormView(viewName);
			}
		}
		if (form == null && StringUtil.isEmpty(viewName)) {
			//defaultの場合、何もなければ自動生成
			form = createDefaultDetailFormView(ed);
		}

		return form;
	}

	/**
	 * デフォルトの詳細画面用定義を生成します。
	 * 表示名も設定します。
	 *
	 * @param ed Entity定義
	 * @return デフォルトの詳細画面用定義
	 */
	public static DetailFormView createDefaultDetailFormView(EntityDefinition ed) {
		return createDefaultDetailFormView(ed, true);
	}

	/**
	 * デフォルトの詳細画面用定義を生成します。
	 *
	 * @param ed Entity定義
	 * @param isLoadDisplayLabel 表示名をセットするか
	 * @return デフォルトの詳細画面用定義
	 */
	public static DetailFormView createDefaultDetailFormView(EntityDefinition ed, boolean isLoadDisplayLabel) {
		DetailFormView view = new DetailFormView();
		view.setLoadDefinedReferenceProperty(true);

		DefaultSection baseSection = createBasicSection(ed, isLoadDisplayLabel);
		if (ed.getVersionControlType() == VersionControlType.TIMEBASE) {
			baseSection.addElement(createTimestampProperty(
					ed.getProperty(Entity.START_DATE), isLoadDisplayLabel));
			baseSection.addElement(createTimestampProperty(
					ed.getProperty(Entity.END_DATE), isLoadDisplayLabel));
		}

		DefaultSection objectSection = createObjectSection(ed, isLoadDisplayLabel);

		GemConfigService service = ServiceRegistry.getRegistry().getService(GemConfigService.class);
		AutoGenerateSetting setting = service.getAutoGenerateSetting();

		DefaultSection systemSection = createSystemSection(ed, isLoadDisplayLabel, setting);

		if (systemSection != null && setting.getSystemPropertyDisplayPosition() == DisplayPosition.TOP) {
			view.addSection(systemSection);
		}
		view.addSection(baseSection);
		view.addSection(objectSection);
		if (systemSection != null && setting.getSystemPropertyDisplayPosition() == DisplayPosition.BOTTOM) {
			view.addSection(systemSection);
		}

		if (ed.getVersionControlType() != VersionControlType.NONE) {
			VersionSection vs = new VersionSection();
			vs.setDispFlag(true);
			view.addSection(vs);
		}

		return view;
	}

	/**
	 * デフォルトの一括更新画面用定義を生成します。
	 * 表示名も設定します。
	 *
	 * @param ed Entity定義
	 * @return デフォルトの一括更新画面用定義
	 */
	public static BulkFormView createDefaultBulkFormView(EntityDefinition ed) {
		return createDefaultBulkFormView(ed, true);
	}

	/**
	 * デフォルトの一括更新画面用定義を生成します。
	 *
	 * @param ed Entity定義
	 * @param isLoadDisplayLabel 表示名をセットするか
	 * @return デフォルトの一括更新画面用定義
	 */
	public static BulkFormView createDefaultBulkFormView(EntityDefinition ed, boolean isLoadDisplayLabel) {
		BulkFormView view = new BulkFormView();
//		view.setLoadDefinedReferenceProperty(true);

		DefaultSection baseSection = createBasicSection(ed, isLoadDisplayLabel);

		DefaultSection objectSection = createObjectSection(ed, isLoadDisplayLabel);

		view.addSection(baseSection);
		view.addSection(objectSection);

//		if (ed.getVersionControlType() != VersionControlType.NONE) {
//			VersionSection vs = new VersionSection();
//			vs.setDispFlag(true);
//			view.addSection(vs);
//		}

		return view;
	}

	/**
	 * 編集画面の基本項目Sectionを生成します。
	 * 開始日、終了日は含みません。
	 *
	 * @return 基本項目Section
	 */
	private static DefaultSection createBasicSection(EntityDefinition ed, boolean isLoadDisplayLabel) {

		DefaultSection section = new DefaultSection();
		section.setDispFlag(true);
		section.setColNum(1);
		section.setExpandable(true);
		section.setStyle(Constants.AUTO_GENERATE_DETAIL_BASIC_SECTION_CSS_CLASS);

		//タイトル(デフォルトはja)
		section.setTitle(resourceString("generic.element.section.DefaultSection.basicInfo"));
		section.setLocalizedTitleList(resourceList("generic.element.section.DefaultSection.basicInfo"));

		section.addElement(createNameProperty(ed.getProperty(Entity.NAME), isLoadDisplayLabel));
		section.addElement(createDescriptionProperty(ed.getProperty(Entity.DESCRIPTION), isLoadDisplayLabel));

		return section;
	}


	/**
	 * 編集画面のオブジェクト情報Sectionを生成します。
	 *
	 * @return オブジェクト情報Section
	 */
	private static DefaultSection createObjectSection(EntityDefinition ed, boolean isLoadDisplayLabel) {

		DefaultSection section = new DefaultSection();
		section.setDispFlag(true);
		section.setColNum(1);
		section.setExpandable(true);
		section.setStyle(Constants.AUTO_GENERATE_DETAIL_OBJECT_SECTION_CSS_CLASS);

		section.setTitle(resourceString("generic.element.section.DefaultSection.objectInfo"));
		section.setLocalizedTitleList(resourceList("generic.element.section.DefaultSection.objectInfo"));

		for (PropertyDefinition pd : ed.getDeclaredPropertyList()) {
			PropertyItem p = new PropertyItem();
			p.setDispFlag(true);
			if (isLoadDisplayLabel) {
				p.setDisplayLabel(pd.getDisplayName());
			}
			p.setEditor(getDefaultEditor(pd, isLoadDisplayLabel));
			p.setPropertyName(pd.getName());
			if (isLoadDisplayLabel) {
				p.setLocalizedDisplayLabelList(pd.getLocalizedDisplayNameList());
			}
			section.addElement(p);
		}

		return section;
	}

	/**
	 * 編集画面のシステム項目Sectionを生成します。
	 *
	 * @param ed Entity定義
	 * @param isLoadDisplayLabel 表示名を設定するか
	 * @param setting 自動生成設定
	 * @return システム項目Section
	 */
	private static DefaultSection createSystemSection(EntityDefinition ed, boolean isLoadDisplayLabel, AutoGenerateSetting setting) {

		if (!setting.isShowSystemProperty()) {
			return null;
		}

		DefaultSection section = new DefaultSection();
		section.setDispFlag(true);
		section.setColNum(1);
		section.setExpandable(true);
		section.setStyle(Constants.AUTO_GENERATE_DETAIL_SYSTEM_SECTION_CSS_CLASS);

		section.setTitle(resourceString("generic.element.section.DefaultSection.systemInfo"));
		section.setLocalizedTitleList(resourceList("generic.element.section.DefaultSection.systemInfo"));


		for (String propName : setting.getSystemProperties()) {
			//編集画面は編集不可
			PropertyItem item = createSystemPropertyItems(propName, ed, isLoadDisplayLabel, true, setting);
			if (item != null) {
				section.addElement(item);
			}
		}

		return section;
	}

	/**
	 * 検索画面の検索条件にシステム項目を設定します。
	 *
	 * @param section 検索条件Section
	 * @param ed Entity定義
	 * @param isLoadDisplayLabel 表示名を設定するか
	 * @param setting 自動生成設定
	 */
	private static void createSystemSearchConditionItem(SearchConditionSection section, EntityDefinition ed, boolean isLoadDisplayLabel, AutoGenerateSetting setting) {

		if (!setting.isShowSystemProperty()) {
			return;
		}

		for (String propName : setting.getSystemProperties()) {
			//検索条件は編集可能
			PropertyItem item = createSystemPropertyItems(propName, ed, isLoadDisplayLabel, false, setting);
			if (item != null) {
				section.addElement(item);
			}
		}

	}

	/**
	 * システム項目のPropertyItem（検索条件、編集画面用）を生成します。
	 *
	 * @param propName プロパティ名
	 * @param ed Entity定義
	 * @param isLoadDisplayLabel 表示名を設定するか
	 * @param isReadOnly 読み取り専用か
	 * @param setting 自動生成設定
	 * @return PropertyItem
	 */
	private static PropertyItem createSystemPropertyItems(String propName, EntityDefinition ed, boolean isLoadDisplayLabel, boolean isReadOnly, AutoGenerateSetting setting) {

		if (Entity.OID.equals(propName)) {
			//OIDをカスタマイズしている場合に表示するかをチェック
			if (ed.getOidPropertyName() != null && setting.isExcludeOidWhenCustomOid()) {
				return null;
			}
		}
		if (Entity.VERSION.equals(propName)) {
			//VERSIONED以外の場合は表示しない
			if (ed.getVersionControlType() != VersionControlType.VERSIONED) {
				return null;
			}
		}

		PropertyDefinition pd = ed.getProperty(propName);
		PropertyItem item = new PropertyItem();
		item.setDispFlag(true);
		item.setPropertyName(pd.getName());
		if (isLoadDisplayLabel) {
			item.setDisplayLabel(pd.getDisplayName());
			item.setLocalizedDisplayLabelList(pd.getLocalizedDisplayNameList());
		}
		if ((Entity.CREATE_BY.equals(propName) || Entity.UPDATE_BY.equals(propName) || Entity.LOCKED_BY.equals(propName))
				&& setting.isUseUserPropertyEditor()) {
			//UserPropertyEditorを利用するかを確認
			item.setEditor(new UserPropertyEditor());
		} else {
			//Editorを取得
			item.setEditor(getDefaultEditor(pd, isLoadDisplayLabel, isReadOnly));
		}

		return item;
	}

	/**
	 * 検索画面の検索結果にシステム項目を設定します。
	 *
	 * @param section 検索結果Section
	 * @param ed Entity定義
	 * @param isLoadDisplayLabel 表示名を設定するか
	 * @param setting 自動生成設定
	 */
	private static void createSystemSearchResultColumn(SearchResultSection section, EntityDefinition ed, boolean isLoadDisplayLabel, AutoGenerateSetting setting) {

		if (!setting.isShowSystemProperty()) {
			return;
		}

		for (String propName : setting.getSystemProperties()) {

			if (Entity.OID.equals(propName)) {
				//OIDをカスタマイズしている場合に表示するかをチェック
				if (ed.getOidPropertyName() != null && setting.isExcludeOidWhenCustomOid()) {
					continue;
				}
			}
			if (Entity.VERSION.equals(propName)) {
				//VERSIONED以外の場合は表示しない
				if (ed.getVersionControlType() != VersionControlType.VERSIONED) {
					continue;
				}
			}

			PropertyDefinition pd = ed.getProperty(propName);

			PropertyColumn column = new PropertyColumn();
			column.setDispFlag(true);
			column.setPropertyName(pd.getName());
			if (isLoadDisplayLabel) {
				column.setDisplayLabel(pd.getDisplayName());
				column.setLocalizedDisplayLabelList(pd.getLocalizedDisplayNameList());
			}
			if ((Entity.CREATE_BY.equals(propName) || Entity.UPDATE_BY.equals(propName) || Entity.LOCKED_BY.equals(propName))
					&& setting.isUseUserPropertyEditor()) {
				//UserPropertyEditorを利用するかを確認
				column.setEditor(new UserPropertyEditor());
			} else {
				//Editorを取得
				column.setEditor(getDefaultEditor(pd, isLoadDisplayLabel, false));
			}
			section.addElement(column);
		}
	}

	/**
	 * 検索結果用の名前プロパティの定義を生成します。
	 *
	 * @param pd プロパティ定義
	 * @param isLoadDisplayLabel 表示名をセットするか
	 * @return 検索結果用の名前プロパティの定義
	 */
	private static PropertyColumn createNamePropertyColumn(PropertyDefinition pd, boolean isLoadDisplayLabel) {
		PropertyColumn property = new PropertyColumn();
		property.setDispFlag(true);
		if (isLoadDisplayLabel) {
			property.setDisplayLabel(pd.getDisplayName());
		}
		StringPropertyEditor editor = new StringPropertyEditor();
		editor.setDisplayType(StringPropertyEditor.StringDisplayType.TEXT);
		property.setEditor(editor);
		property.setPropertyName(pd.getName());
		if (isLoadDisplayLabel) {
			property.setLocalizedDisplayLabelList(pd.getLocalizedDisplayNameList());
		}
		return property;
	}

	/**
	 * 詳細結果用の名前プロパティの定義を生成します。
	 *
	 * @param pd プロパティ定義
	 * @param isLoadDisplayLabel 表示名をセットするか
	 * @return 詳細結果用の名前プロパティの定義
	 */
	private static PropertyItem createNameProperty(PropertyDefinition pd, boolean isLoadDisplayLabel) {
		PropertyItem property = new PropertyItem();
		property.setDispFlag(true);
		if (isLoadDisplayLabel) {
			property.setDisplayLabel(pd.getDisplayName());
		}
		StringPropertyEditor editor = new StringPropertyEditor();
		editor.setDisplayType(StringDisplayType.TEXT);
		property.setEditor(editor);
		property.setPropertyName(pd.getName());
		if (isLoadDisplayLabel) {
			property.setLocalizedDisplayLabelList(pd.getLocalizedDisplayNameList());
		}
		return property;
	}

	/**
	 * 詳細結果用の概要プロパティの定義を生成します。
	 *
	 * @param pd プロパティ定義
	 * @param isLoadDisplayLabel 表示名をセットするか
	 * @return 詳細結果用の概要プロパティの定義
	 */
	private static PropertyItem createDescriptionProperty(PropertyDefinition pd, boolean isLoadDisplayLabel) {
		PropertyItem property = new PropertyItem();
		property.setDispFlag(true);
		if (isLoadDisplayLabel) {
			property.setDisplayLabel(pd.getDisplayName());
		}
		StringPropertyEditor editor = new StringPropertyEditor();
		editor.setDisplayType(StringDisplayType.TEXTAREA);
		property.setEditor(editor);
		property.setPropertyName(pd.getName());
		if (isLoadDisplayLabel) {
			property.setLocalizedDisplayLabelList(pd.getLocalizedDisplayNameList());
		}
		return property;
	}

	/**
	 * 詳細結果用の有効期間プロパティの定義を生成します。
	 *
	 * @param pd プロパティ定義
	 * @param isLoadDisplayLabel 表示名をセットするか
	 * @return 詳細結果用の有効期間プロパティの定義
	 */
	private static PropertyColumn createTimestampPropertyColumn(PropertyDefinition pd, boolean isLoadDisplayLabel) {
		PropertyColumn property = new PropertyColumn();
		property.setDispFlag(true);
		if (isLoadDisplayLabel) {
			property.setDisplayLabel(pd.getDisplayName());
		}
		TimestampPropertyEditor editor = new TimestampPropertyEditor();
		editor.setDisplayType(DateTimeDisplayType.DATETIME);
		editor.setUseDatetimePicker(true);
		property.setEditor(editor);
		property.setPropertyName(pd.getName());
		if (isLoadDisplayLabel) {
			property.setLocalizedDisplayLabelList(pd.getLocalizedDisplayNameList());
		}
		return property;
	}

	/**
	 * 詳細結果用の有効期間プロパティの定義を生成します。
	 *
	 * @param pd プロパティ定義
	 * @param isLoadDisplayLabel 表示名をセットするか
	 * @return 詳細結果用の有効期間プロパティの定義
	 */
	private static PropertyItem createTimestampProperty(PropertyDefinition pd, boolean isLoadDisplayLabel) {
		PropertyItem property = new PropertyItem();
		property.setDispFlag(true);
		if (isLoadDisplayLabel) {
			property.setDisplayLabel(pd.getDisplayName());
		}
		TimestampPropertyEditor editor = new TimestampPropertyEditor();
		editor.setDisplayType(DateTimeDisplayType.DATETIME);
		editor.setUseDatetimePicker(true);
		property.setEditor(editor);
		property.setPropertyName(pd.getName());
		if (isLoadDisplayLabel) {
			property.setLocalizedDisplayLabelList(pd.getLocalizedDisplayNameList());
		}
		return property;
	}

	/**
	 * プロパティ定義の型に合わせたプロパティエディタを取得します。
	 * 表示名も設定します。
	 *
	 * @param pd プロパティ定義
	 * @return プロパティエディタ
	 */
	public static PropertyEditor getDefaultEditor(PropertyDefinition pd) {
		return getDefaultEditor(pd, true);
	}

	/**
	 * プロパティ定義の型に合わせたプロパティエディタを取得します。
	 *
	 * @param pd プロパティ定義
	 * @param isLoadDisplayLabel 表示名をセットするか
	 * @return プロパティエディタ
	 */
	public static PropertyEditor getDefaultEditor(PropertyDefinition pd, boolean isLoadDisplayLabel) {
		return getDefaultEditor(pd, isLoadDisplayLabel, false);
	}

	/**
	 * プロパティ定義の型に合わせたプロパティエディタを取得します。
	 *
	 * @param pd プロパティ定義
	 * @param isLoadDisplayLabel 表示名をセットするか
	 * @param isReadOnly 読み取り専用
	 * @return プロパティエディタ
	 */
	public static PropertyEditor getDefaultEditor(PropertyDefinition pd, boolean isLoadDisplayLabel, boolean isReadOnly) {
		PropertyEditor editor = getDefaultEditor(pd.getType(), isLoadDisplayLabel, isReadOnly);

		if (editor instanceof ExpressionPropertyEditor) {
			ExpressionPropertyEditor epe = (ExpressionPropertyEditor)editor;
			ExpressionProperty ep = (ExpressionProperty)pd;

			//ResultTypeが指定されている場合は内部Editorを取得
			if (ep.getResultType() != null) {
				PropertyDefinitionType resultType = ep.getResultType();

				PropertyEditor resultEditor = null;

				switch (resultType) {
				case REFERENCE:
					//REFERENCEは対象外
					break;
				case EXPRESSION:
					//EXPRESSIONは対象外
					break;
				case SELECT:
					resultEditor = getDefaultEditor(resultType, isLoadDisplayLabel, isReadOnly);

					//SELECTの場合はResultTypeSpecが設定されている場合のみEditorValueを取得
					if (ep.getResultTypeSpec() != null
							&& ep.getResultTypeSpec() instanceof SelectProperty) {

						SelectProperty resultTypeSpec = (SelectProperty)ep.getResultTypeSpec();
						if (StringUtil.isEmpty(resultTypeSpec.getSelectValueDefinitionName())) {
							//Globalが未指定の場合、未設定
							break;
						}

						if (isLoadDisplayLabel) {
							SelectPropertyEditor sepe = (SelectPropertyEditor)resultEditor;

							SelectValueDefinitionManager svdm = ManagerLocator.getInstance().getManager(SelectValueDefinitionManager.class);
							SelectValueDefinition svd = svdm.get(resultTypeSpec.getSelectValueDefinitionName());
							sepe.setValues(EntityViewUtil.createEditorValueList(svd, null));
						}
					}
					break;
				default:
					//ResultTypeに合わせたEditorを取得
					resultEditor = getDefaultEditor(resultType, isLoadDisplayLabel, isReadOnly);
				}

				//内部Editorにセット
				epe.setEditor(resultEditor);
			}
		} else if (editor instanceof ReferencePropertyEditor) {
			ReferencePropertyEditor rpe = (ReferencePropertyEditor)editor;
			ReferenceProperty rp = (ReferenceProperty)pd;
			rpe.setObjectName(rp.getObjectDefinitionName());
		} else if (editor instanceof SelectPropertyEditor) {
			if (isLoadDisplayLabel) {
				SelectPropertyEditor sepe = (SelectPropertyEditor)editor;
				SelectProperty sp = (SelectProperty) pd;
				sepe.setValues(EntityViewUtil.createEditorValueList(sp, null));
			}
		}

		return editor;
	}

	/**
	 * プロパティ定義の型に合わせたプロパティエディタを取得します。
	 *
	 * @param type プロパティ型
	 * @param isLoadDisplayLabel 表示名をセットするか
	 * @param isReadOnly 読み取り専用
	 * @return プロパティエディタ
	 */
	private static PropertyEditor getDefaultEditor(PropertyDefinitionType type, boolean isLoadDisplayLabel, boolean isReadOnly) {
		PropertyEditor editor = null;

		switch(type) {
		case BINARY:
			BinaryPropertyEditor bpe = new BinaryPropertyEditor();
			if (isReadOnly) {
				//TODO LABEL
				bpe.setDisplayType(BinaryDisplayType.BINARY);
			} else {
				bpe.setDisplayType(BinaryDisplayType.BINARY);
			}
			editor = bpe;
			break;
		case BOOLEAN:
			BooleanPropertyEditor bope = new BooleanPropertyEditor();
			if (isReadOnly) {
				bope.setDisplayType(BooleanPropertyEditor.BooleanDisplayType.LABEL);
			} else {
				bope.setDisplayType(BooleanPropertyEditor.BooleanDisplayType.RADIO);
			}
			if (isLoadDisplayLabel) {
				//デフォルトはja
				bope.setTrueLabel(resourceString("generic.editor.boolean.BooleanPropertyEditor_Condition.enable"));
				bope.setFalseLabel(resourceString("generic.editor.boolean.BooleanPropertyEditor_Condition.invalid"));

				if (ExecuteContext.getCurrentContext().getCurrentTenant().getTenantConfig(TenantI18nInfo.class).isUseMultilingual()) {
					bope.setLocalizedTrueLabelList(resourceList("generic.editor.boolean.BooleanPropertyEditor_Condition.enable"));
					bope.setLocalizedFalseLabelList(resourceList("generic.editor.boolean.BooleanPropertyEditor_Condition.invalid"));
				}
			}
			editor = bope;
			break;
		case DATE:
			DatePropertyEditor dpe = new DatePropertyEditor();
			if (isReadOnly) {
				dpe.setDisplayType(DatePropertyEditor.DateTimeDisplayType.LABEL);
			} else {
				dpe.setDisplayType(DatePropertyEditor.DateTimeDisplayType.DATETIME);
			}
			editor = dpe;
			break;
		case DATETIME:
			TimestampPropertyEditor tspe = new TimestampPropertyEditor();
			if (isReadOnly) {
				tspe.setDisplayType(TimestampPropertyEditor.DateTimeDisplayType.LABEL);
			} else {
				tspe.setDisplayType(TimestampPropertyEditor.DateTimeDisplayType.DATETIME);
				tspe.setUseDatetimePicker(true);
			}
			editor = tspe;
			break;
		case DECIMAL:
			DecimalPropertyEditor dcpe = new DecimalPropertyEditor();
			if (isReadOnly) {
				dcpe.setDisplayType(DecimalPropertyEditor.NumberDisplayType.LABEL);
			} else {
				dcpe.setDisplayType(DecimalPropertyEditor.NumberDisplayType.TEXT);
			}
			editor = dcpe;
			break;
		case EXPRESSION:
			ExpressionPropertyEditor epe = new ExpressionPropertyEditor();
			epe.setDisplayType(ExpressionDisplayType.LABEL);
			editor = epe;
			break;
		case FLOAT:
			FloatPropertyEditor fpe = new FloatPropertyEditor();
			if (isReadOnly) {
				fpe.setDisplayType(FloatPropertyEditor.NumberDisplayType.LABEL);
			} else {
				fpe.setDisplayType(FloatPropertyEditor.NumberDisplayType.TEXT);
			}
			editor = fpe;
			break;
		case INTEGER:
			IntegerPropertyEditor ipe = new IntegerPropertyEditor();
			if (isReadOnly) {
				ipe.setDisplayType(IntegerPropertyEditor.NumberDisplayType.LABEL);
			} else {
				ipe.setDisplayType(IntegerPropertyEditor.NumberDisplayType.TEXT);
			}
			editor = ipe;
			break;
		case LONGTEXT:
			LongTextPropertyEditor ltpe = new LongTextPropertyEditor();
			if (isReadOnly) {
				ltpe.setDisplayType(LongTextPropertyEditor.StringDisplayType.LABEL);
			} else {
				ltpe.setDisplayType(LongTextPropertyEditor.StringDisplayType.TEXTAREA);
			}
			editor = ltpe;
			break;
		case REFERENCE:
			ReferencePropertyEditor rpe = new ReferencePropertyEditor();
			rpe.setDisplayType(ReferencePropertyEditor.ReferenceDisplayType.LINK);
			if (isReadOnly) {
				rpe.setHideSelectButton(true);
				rpe.setHideRegistButton(true);
				rpe.setHideDeleteButton(true);
				rpe.setEditableReference(false);
			}
			editor = rpe;
			break;
		case SELECT:
			SelectPropertyEditor sepe = new SelectPropertyEditor();
			if (isReadOnly) {
				sepe.setDisplayType(SelectPropertyEditor.SelectDisplayType.LABEL);
			} else {
				sepe.setDisplayType(SelectPropertyEditor.SelectDisplayType.RADIO);
			}
			editor = sepe;
			break;
		case STRING:
			StringPropertyEditor spe = new StringPropertyEditor();
			if (isReadOnly) {
				spe.setDisplayType(StringPropertyEditor.StringDisplayType.LABEL);
			} else {
				spe.setDisplayType(StringPropertyEditor.StringDisplayType.TEXT);
			}
			editor = spe;
			break;
		case TIME:
			TimePropertyEditor tpe = new TimePropertyEditor();
			if (isReadOnly) {
				tpe.setDisplayType(TimePropertyEditor.DateTimeDisplayType.LABEL);
			} else {
				tpe.setDisplayType(TimePropertyEditor.DateTimeDisplayType.DATETIME);
			}
			tpe.setUseTimePicker(true);
			editor = tpe;
			break;
		case AUTONUMBER:
			AutoNumberPropertyEditor anpe = new AutoNumberPropertyEditor();
			anpe.setDisplayType(AutoNumberDisplayType.LABEL);
			editor = anpe;
			break;
		}

		return editor;
	}

	private static String resourceString(String key, Object... arguments) {
		return GemResourceBundleUtil.resourceString(key, arguments);
	}

	private static List<LocalizedStringDefinition> resourceList(String key, Object... arguments) {
		return GemResourceBundleUtil.resourceList(key, arguments);
	}
}
