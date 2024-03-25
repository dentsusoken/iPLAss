/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.view.generic.element.property;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.adminconsole.view.annotation.FieldOrder;
import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.adminconsole.view.annotation.generic.EntityViewField;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.view.generic.common.AutocompletionSetting;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.element.Element;

/**
 * プロパティをレイアウトする情報の基底クラス
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({PropertyItem.class, PropertyColumn.class})
@FieldOrder(manual=true)
public abstract class PropertyBase extends Element implements PropertyElement {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 76335134724542580L;

	/** プロパティの名前 */
	private String propertyName;

	/** 画面表示時のラベル */
	@MetaFieldInfo(
			displayName="表示ラベル",
			displayNameKey="generic_element_property_PropertyBase_displayLabelDisplaNameKey",
			description="画面に表示するラベルを設定します。",
			descriptionKey="generic_element_property_PropertyBase_displayLabelDescriptionKey",
			inputType=InputType.MULTI_LANG,
			multiLangField = "localizedDisplayLabelList",
			displayOrder=300
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.ALL}
	)
	@MultiLang(itemNameGetter = "getPropertyName")
	private String displayLabel;

	/** 多言語設定情報 */
	@MetaFieldInfo(
			displayName="多言語設定",
			displayNameKey="generic_element_property_PropertyBase_localizedDisplayLabelListDisplaNameKey",
			inputType=InputType.MULTI_LANG_LIST,
			displayOrder=310
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.ALL}
	)
	private List<LocalizedStringDefinition> localizedDisplayLabelList;

	/** クラス名 */
	@MetaFieldInfo(
			displayName="クラス名",
			displayNameKey="generic_element_property_PropertyBase_styleDisplaNameKey",
			displayOrder=320,
			description="スタイルシートのクラス名を指定します。" +
					"複数指定する場合は半角スペースで区切ってください。",
			descriptionKey="generic_element_property_PropertyBase_styleDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.ALL}
	)
	private String style;




	/** プロパティエディタ */
	@MetaFieldInfo(
			displayName="プロパティエディタ",
			displayNameKey="generic_element_property_PropertyBase_editorDisplaNameKey",
			required=true,
			inputType=InputType.REFERENCE,
			referenceClass=PropertyEditor.class,
			displayOrder=1000,
			description="プロパティの型にあわせたプロパティエディタが" +
					"デフォルトで選択されているので変更しないで下さい。",
			descriptionKey="generic_element_property_PropertyBase_editorDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.ALL}
	)
	@MultiLang(itemNameGetter = "getPropertyName", isMultiLangValue = false)
	private PropertyEditor editor;




	/** 自動補完設定 */
	@MetaFieldInfo(
			displayName="自動補完設定",
			displayNameKey="generic_element_property_PropertyBase_autocompletionSettingDisplaNameKey",
			inputType=InputType.REFERENCE,
			referenceClass=AutocompletionSetting.class,
			displayOrder=2000,
			description="自動補完設定を設定します。",
			descriptionKey="generic_element_property_PropertyBase_autocompletionSettingDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL, FieldReferenceType.SEARCHCONDITION, FieldReferenceType.BULK}
	)
	private AutocompletionSetting autocompletionSetting;

	/**
	 * プロパティの名前を取得します。
	 * @return プロパティの名前
	 */
	@Override
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * プロパティの名前を設定します。
	 * @param propertyName プロパティの名前
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * 画面表示時のラベルを取得します。
	 * @return 画面表示時のラベル
	 */
	public String getDisplayLabel() {
		return displayLabel;
	}

	/**
	 * 画面表示時のラベルを設定します。
	 * @param displayLabel 画面表示時のラベル
	 */
	public void setDisplayLabel(String displayLabel) {
		this.displayLabel = displayLabel;
	}

	/**
	 * プロパティエディタを取得します。
	 * @return プロパティエディタ
	 */
	@Override
	public PropertyEditor getEditor() {
		return editor;
	}

	/**
	 * プロパティエディタを設定します。
	 * @param editor プロパティエディタ
	 */
	public void setEditor(PropertyEditor editor) {
		this.editor = editor;
	}

	/**
	 * クラス名を取得します。
	 * @return クラス名
	 */
	public String getStyle() {
		return style;
	}

	/**
	 * クラス名を設定します。
	 * @param style クラス名
	 */
	public void setStyle(String style) {
		this.style = style;
	}

	/**
	 * 自動補完設定を取得します。
	 * @return autocompletionSetting 自動補完設定
	 */
	public AutocompletionSetting getAutocompletionSetting() {
		return autocompletionSetting;
	}

	/**
	 * 自動補完設定を設定します。
	 * @param autocompletionSetting 自動補完設定
	 */
	public void setAutocompletionSetting(AutocompletionSetting autocompletionSetting) {
		this.autocompletionSetting = autocompletionSetting;
	}

	/**
	 * 多言語設定情報を取得します。
	 * @return リスト
	 */
	public List<LocalizedStringDefinition> getLocalizedDisplayLabelList() {
		return localizedDisplayLabelList;
	}

	/**
	 * 多言語設定情報を設定します。
	 * @param リスト
	 */
	public void setLocalizedDisplayLabelList(List<LocalizedStringDefinition> localizedDisplayLabelList) {
		this.localizedDisplayLabelList = localizedDisplayLabelList;
	}

	/**
	 * 多言語設定情報を追加します。
	 * @param 多言語設定情報
	 */
	public void addLocalizedTitle(LocalizedStringDefinition localizedTitle) {
		if (localizedDisplayLabelList == null) {
			localizedDisplayLabelList = new ArrayList<>();
		}

		localizedDisplayLabelList.add(localizedTitle);
	}

}
