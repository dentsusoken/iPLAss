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

package org.iplass.mtp.view.generic.editor;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.adminconsole.view.annotation.generic.EntityViewField;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.view.generic.Jsp;
import org.iplass.mtp.view.generic.Jsps;
import org.iplass.mtp.view.generic.ViewConst;

/**
 * 真偽値型プロパティエディタ
 * @author lis3wg
 */
@Jsps({
	@Jsp(path="/jsp/gem/generic/editor/BooleanPropertyEditor.jsp", key=ViewConst.DESIGN_TYPE_GEM),
	@Jsp(path="/jsp/gem/aggregation/unit/editor/BooleanPropertyEditor.jsp", key=ViewConst.DESIGN_TYPE_GEM_AGGREGATION)
})
public class BooleanPropertyEditor extends PrimitivePropertyEditor {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -8063746392298470559L;

	/** 表示タイプ */
	@XmlType(namespace="http://mtp.iplass.org/xml/definition/view/generic")
	public enum BooleanDisplayType {
		@XmlEnumValue("Radio")RADIO,
		@XmlEnumValue("Checkbox")CHECKBOX,
		@XmlEnumValue("Select")SELECT,
		@XmlEnumValue("Label")LABEL,
		@XmlEnumValue("Hidden")HIDDEN
	}

	/** 表示タイプ */
	@MetaFieldInfo(
			displayName="表示タイプ",
			displayNameKey="generic_editor_BooleanPropertyEditor_displayTypeDisplaNameKey",
			inputType=InputType.ENUM,
			enumClass=BooleanDisplayType.class,
			required=true,
			displayOrder=100,
			description="画面に表示する方法を選択します。",
			descriptionKey="generic_editor_BooleanPropertyEditor_displayTypeDescriptionKey"
	)
	private BooleanDisplayType displayType;

	/** 真の表示ラベル */
	@MetaFieldInfo(
			displayName="真の表示ラベル",
			displayNameKey="generic_editor_BooleanPropertyEditor_trueLabelDisplaNameKey",
			description="真の選択肢に表示するラベルを設定します。",
			descriptionKey="generic_editor_BooleanPropertyEditor_trueLabelDescriptionKey",
			inputType=InputType.MULTI_LANG,
			displayOrder=110,
			multiLangField="localizedTrueLabelList"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.ALL}
	)
	@MultiLang()
	private String trueLabel;

	/** 真の表示ラベル多言語設定情報 */
	@MetaFieldInfo(
			displayName="多言語設定情報",
			displayNameKey="generic_editor_BooleanPropertyEditor_localizedTrueLabelListDisplaNameKey",
			inputType=InputType.MULTI_LANG_LIST,
			displayOrder=120
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.ALL}
	)
	private List<LocalizedStringDefinition> localizedTrueLabelList;

	/** 偽の表示ラベル */
	@MetaFieldInfo(
			displayName="偽の表示ラベル",
			displayNameKey="generic_editor_BooleanPropertyEditor_falseLabelDisplaNameKey",
			description="偽の選択肢に表示するラベルを設定します。",
			descriptionKey="generic_editor_BooleanPropertyEditor_falseLabelDescriptionKey",
			inputType=InputType.MULTI_LANG,
			displayOrder=130,
			multiLangField="localizedFalseLabelList"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.ALL}
	)
	@MultiLang()
	private String falseLabel;

	/** 偽の表示ラベル多言語設定情報 */
	@MetaFieldInfo(
			displayName="多言語設定情報",
			displayNameKey="generic_editor_BooleanPropertyEditor_localizedFalseLabelListDisplaNameKey",
			inputType=InputType.MULTI_LANG_LIST,
			displayOrder=140
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.ALL}
	)
	private List<LocalizedStringDefinition> localizedFalseLabelList;

	/** 初期値 */
	@MetaFieldInfo(
			displayName="初期値",
			displayNameKey="generic_editor_BooleanPropertyEditor_defaultValueDisplaNameKey",
			description="新規作成時の初期値を設定します。true/falseまたは1/0を指定してください。",
			descriptionKey="generic_editor_BooleanPropertyEditor_defaultValueDescriptionKey",
			displayOrder=150
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private String defaultValue;

	/**
	 * コンストラクタ
	 */
	public BooleanPropertyEditor() {
	}

	@Override
	public BooleanDisplayType getDisplayType() {
		return displayType;
	}

	/**
	 * 表示タイプを設定します。
	 * @param displayType
	 */
	public void setDisplayType(BooleanDisplayType displayType) {
		this.displayType = displayType;
	}

	@Override
	public boolean isHide() {
		return displayType == BooleanDisplayType.HIDDEN;
	}

	/**
	 * 真の表示ラベル
	 * @return 真の表示ラベル
	 */
	public String getTrueLabel() {
		return trueLabel;
	}

	/**
	 * 真の表示ラベルを設定します。
	 * @param trueLabel 真の表示ラベル
	 */
	public void setTrueLabel(String trueLabel) {
		this.trueLabel = trueLabel;
	}

	/**
	 * 偽の表示ラベル
	 * @return 偽の表示ラベル
	 */
	public String getFalseLabel() {
		return falseLabel;
	}

	/**
	 * 偽の表示ラベルを設定します。
	 * @param falseLabel 偽の表示ラベル
	 */
	public void setFalseLabel(String falseLabel) {
		this.falseLabel = falseLabel;
	}

	/**
	 * 真の表示ラベル多言語設定情報を取得します。
	 * @return リスト
	 */
	public List<LocalizedStringDefinition> getLocalizedTrueLabelList() {
		return localizedTrueLabelList;
	}

	/**
	 * 真の表示ラベル多言語設定情報を設定します。
	 * @param リスト
	 */
	public void setLocalizedTrueLabelList(List<LocalizedStringDefinition> localizedTrueLabelList) {
		this.localizedTrueLabelList = localizedTrueLabelList;
	}

	/**
	 * 真の表示ラベル多言語設定情報を追加します。
	 * @param 多言語設定情報
	 */
	public void addLocalizedTrueLabel(LocalizedStringDefinition localizedTrueLabel) {
		if (localizedTrueLabelList == null) {
			localizedTrueLabelList = new ArrayList<>();
		}

		localizedTrueLabelList.add(localizedTrueLabel);
	}

	/**
	 * 偽の表示ラベル多言語設定情報を取得します。
	 * @return リスト
	 */
	public List<LocalizedStringDefinition> getLocalizedFalseLabelList() {
		return localizedFalseLabelList;
	}

	/**
	 * 偽の表示ラベル多言語設定情報を設定します。
	 * @param リスト
	 */
	public void setLocalizedFalseLabelList(List<LocalizedStringDefinition> localizedFalseLabelList) {
		this.localizedFalseLabelList = localizedFalseLabelList;
	}

	/**
	 * 多言語設定情報を追加します。
	 * @param 多言語設定情報
	 */
	public void addLocalizedFalseLabel(LocalizedStringDefinition localizedFalseLabel) {
		if (localizedFalseLabelList == null) {
			localizedFalseLabelList = new ArrayList<>();
		}

		localizedFalseLabelList.add(localizedFalseLabel);
	}

	@Override
	public String getDefaultValue() {
		return defaultValue;
	}

	@Override
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
}
