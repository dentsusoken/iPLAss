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
import org.iplass.mtp.tenant.TenantI18nInfo;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.editor.AutoNumberPropertyEditor;
import org.iplass.mtp.view.generic.editor.BinaryPropertyEditor;
import org.iplass.mtp.view.generic.editor.BinaryPropertyEditor.BinaryDisplayType;
import org.iplass.mtp.view.generic.editor.BooleanPropertyEditor;
import org.iplass.mtp.view.generic.editor.DatePropertyEditor;
import org.iplass.mtp.view.generic.editor.DateTimePropertyEditor.DateTimeDisplayType;
import org.iplass.mtp.view.generic.editor.DecimalPropertyEditor;
import org.iplass.mtp.view.generic.editor.ExpressionPropertyEditor;
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
	 * viewNameが未指定、もしくは自動生成対象のViewの場合、自動生成した検索画面定義を返します。
	 *
	 * @param ed Entity定義
	 * @param ev 画面定義
	 * @param viewName View名
	 * @return 検索画面定義
	 */
	public static SearchFormView getSearchFormView(EntityDefinition ed, EntityView ev, String viewName) {
		SearchFormView form = null;
		if (StringUtil.isEmpty(viewName)) {
			//デフォルトレイアウトを利用
			if (ev!= null && ev.getSearchFormViewNames().length > 0) {
				//1件でもView定義があればその中からデフォルトレイアウトを探す
				form = ev.getDefaultSearchFormView();
			} else {
				//何もなければ自動生成
				form = createDefaultSearchFormView(ed);
			}
		} else {
			//指定レイアウトを利用
			if (ev!= null) {
				if (ev.isAutoGenerateSearchView(viewName)) {
					//自動設定の対象の場合はViewの有無問わず自動生成
					form = createDefaultSearchFormView(ed);
				} else {
					form = ev.getSearchFormView(viewName);
				}
			}
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
		condSection.addElement(createNameProperty(ed.getProperty(Entity.NAME), isLoadDisplayLabel));
		if (ed.getVersionControlType() == VersionControlType.TIMEBASE) {
			condSection.addElement(createTimestampProperty(
					ed.getProperty(Entity.START_DATE), isLoadDisplayLabel));
			condSection.addElement(createTimestampProperty(
					ed.getProperty(Entity.END_DATE), isLoadDisplayLabel));
		}
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
			if (isLoadDisplayLabel) {
				p.setDisplayLabel(pd.getDisplayName());
			}
			p.setEditor(getDefaultEditor(pd, isLoadDisplayLabel));
			p.setPropertyName(pd.getName());
			if (isLoadDisplayLabel) {
				p.setLocalizedDisplayLabelList(pd.getLocalizedDisplayNameList());
			}
			condSection.addElement(p);
		}
		view.addSection(condSection);

		SearchResultSection resultSection = new SearchResultSection();
		resultSection.setDispRowCount(10);
		resultSection.addElement(createNamePropertyColumn(ed.getProperty(Entity.NAME), isLoadDisplayLabel));
		if (ed.getVersionControlType() == VersionControlType.TIMEBASE) {
			resultSection.addElement(createTimestampPropertyColumn(
					ed.getProperty(Entity.START_DATE), isLoadDisplayLabel));
			resultSection.addElement(createTimestampPropertyColumn(
					ed.getProperty(Entity.END_DATE), isLoadDisplayLabel));
		}
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
			if (isLoadDisplayLabel) {
				p.setDisplayLabel(pd.getDisplayName());
			}
			p.setEditor(getDefaultEditor(pd, isLoadDisplayLabel));
			p.setPropertyName(pd.getName());
			if (isLoadDisplayLabel) {
				p.setLocalizedDisplayLabelList(pd.getLocalizedDisplayNameList());
			}
			resultSection.addElement(p);
		}
		view.addSection(resultSection);

		return view;

	}

	/**
	 * viewNameで指定した詳細画面定義を取得します。
	 * viewNameが未指定、もしくは自動生成対象のViewの場合、自動生成した詳細画面定義を返します。
	 *
	 * @param ed Entity定義
	 * @param ev 画面定義
	 * @param viewName View名
	 * @return 詳細画面定義
	 */
	public static DetailFormView getDetailFormView(EntityDefinition ed, EntityView ev, String viewName) {
		DetailFormView form = null;
		if (StringUtil.isEmpty(viewName)) {
			//デフォルトレイアウトを利用
			if (ev!= null && ev.getDetailFormViewNames().length > 0) {
				//1件でもView定義があればその中からデフォルトレイアウトを探す
				form = ev.getDefaultDetailFormView();
			} else {
				//何もなければ自動生成
				form = createDefaultDetailFormView(ed);
			}
		} else {
			//指定レイアウトを利用
			if (ev!= null) {
				if (ev.isAutoGenerateDetailView(viewName)) {
					//自動設定の対象の場合はViewの有無問わず自動生成
					form = createDefaultDetailFormView(ed);
				} else {
					form = ev.getDetailFormView(viewName);
				}
			}
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

		DefaultSection baseSection = new DefaultSection();
		baseSection.setDispFlag(true);
		baseSection.setColNum(1);
		baseSection.setExpandable(true);

		//タイトル(デフォルトはja)
		baseSection.setTitle(resourceString("generic.element.section.DefaultSection.basicInfo"));
		baseSection.setLocalizedTitleList(resourceList("generic.element.section.DefaultSection.basicInfo"));

		baseSection.addElement(createNameProperty(ed.getProperty(Entity.NAME), isLoadDisplayLabel));
		baseSection.addElement(createDescriptionProperty(ed.getProperty(Entity.DESCRIPTION), isLoadDisplayLabel));
		if (ed.getVersionControlType() == VersionControlType.TIMEBASE) {
			baseSection.addElement(createTimestampProperty(
					ed.getProperty(Entity.START_DATE), isLoadDisplayLabel));
			baseSection.addElement(createTimestampProperty(
					ed.getProperty(Entity.END_DATE), isLoadDisplayLabel));
		}

		DefaultSection objectSection = new DefaultSection();
		objectSection.setDispFlag(true);
		objectSection.setColNum(1);
		objectSection.setExpandable(true);

		//タイトル(デフォルトはja)
		objectSection.setTitle(resourceString("generic.element.section.DefaultSection.objectInfo"));
		objectSection.setLocalizedTitleList(resourceList("generic.element.section.DefaultSection.objectInfo"));

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
			objectSection.addElement(p);
		}

		view.addSection(baseSection);
		view.addSection(objectSection);

		if (ed.getVersionControlType() != VersionControlType.NONE) {
			VersionSection vs = new VersionSection();
			vs.setDispFlag(true);
			view.addSection(vs);
		}

		return view;
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
		PropertyEditor editor = getDefaultEditor(pd.getType(), isLoadDisplayLabel);

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
					resultEditor = getDefaultEditor(resultType, isLoadDisplayLabel);

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
					resultEditor = getDefaultEditor(resultType, isLoadDisplayLabel);
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
	 * @return プロパティエディタ
	 */
	private static PropertyEditor getDefaultEditor(PropertyDefinitionType type, boolean isLoadDisplayLabel) {
		PropertyEditor editor = null;

		switch(type) {
		case BINARY:
			BinaryPropertyEditor bpe = new BinaryPropertyEditor();
			bpe.setDisplayType(BinaryDisplayType.BINARY);
			editor = bpe;
			break;
		case BOOLEAN:
			BooleanPropertyEditor bope = new BooleanPropertyEditor();
			bope.setDisplayType(BooleanPropertyEditor.BooleanDisplayType.RADIO);
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
			dpe.setDisplayType(DatePropertyEditor.DateTimeDisplayType.DATETIME);
			editor = dpe;
			break;
		case DATETIME:
			TimestampPropertyEditor tspe = new TimestampPropertyEditor();
			tspe.setDisplayType(TimestampPropertyEditor.DateTimeDisplayType.DATETIME);
			tspe.setUseDatetimePicker(true);
			editor = tspe;
			break;
		case DECIMAL:
			DecimalPropertyEditor dcpe = new DecimalPropertyEditor();
			dcpe.setDisplayType(DecimalPropertyEditor.NumberDisplayType.TEXT);
			editor = dcpe;
			break;
		case EXPRESSION:
			ExpressionPropertyEditor epe = new ExpressionPropertyEditor();
			editor = epe;
			break;
		case FLOAT:
			FloatPropertyEditor fpe = new FloatPropertyEditor();
			fpe.setDisplayType(FloatPropertyEditor.NumberDisplayType.TEXT);
			editor = fpe;
			break;
		case INTEGER:
			IntegerPropertyEditor ipe = new IntegerPropertyEditor();
			ipe.setDisplayType(IntegerPropertyEditor.NumberDisplayType.TEXT);
			editor = ipe;
			break;
		case LONGTEXT:
			LongTextPropertyEditor ltpe = new LongTextPropertyEditor();
			ltpe.setDisplayType(LongTextPropertyEditor.StringDisplayType.TEXTAREA);
			editor = ltpe;
			break;
		case REFERENCE:
			ReferencePropertyEditor rpe = new ReferencePropertyEditor();
			rpe.setDisplayType(ReferencePropertyEditor.ReferenceDisplayType.LINK);
			editor = rpe;
			break;
		case SELECT:
			SelectPropertyEditor sepe = new SelectPropertyEditor();
			sepe.setDisplayType(SelectPropertyEditor.SelectDisplayType.RADIO);
			editor = sepe;
			break;
		case STRING:
			StringPropertyEditor spe = new StringPropertyEditor();
			spe.setDisplayType(StringPropertyEditor.StringDisplayType.TEXT);
			editor = spe;
			break;
		case TIME:
			TimePropertyEditor tpe = new TimePropertyEditor();
			tpe.setDisplayType(TimePropertyEditor.DateTimeDisplayType.DATETIME);
			tpe.setUseTimePicker(true);
			editor = tpe;
			break;
		case AUTONUMBER:
			AutoNumberPropertyEditor anpe = new AutoNumberPropertyEditor();
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
