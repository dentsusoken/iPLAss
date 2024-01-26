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

package org.iplass.mtp.view.generic.editor;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.adminconsole.view.annotation.Refrectable;
import org.iplass.adminconsole.view.annotation.generic.EntityViewField;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.definition.LocalizedStringDefinition;

/**
 * セレクトボックスの値
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class EditorValue implements Refrectable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 372120834366334182L;

	/** ラベル */
	@MetaFieldInfo(displayName="表示名", description="選択肢のラベルを設定します。",
			displayNameKey="generic_editor_EditorValue_labelDisplaNameKey", descriptionKey="generic_editor_EditorValue_labelDescriptionKey", 
			inputType=InputType.MULTI_LANG,
			multiLangField = "localizedLabelList"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.ALL}
	)
	@MultiLang()
	private String label;

	/** 多言語設定情報 */
	@MetaFieldInfo(
			displayName="多言語設定情報",
			displayNameKey="generic_editor_EditorValue_localizedLabelListDisplayNameKey",
			inputType=InputType.MULTI_LANG_LIST
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.ALL}
	)
	private List<LocalizedStringDefinition> localizedLabelList;

	/** 値 */
	@MetaFieldInfo(displayName="値", description="選択肢の値を設定します。",
			displayNameKey="generic_editor_EditorValue_valueDisplaNameKey", descriptionKey="generic_editor_EditorValue_valueDescriptionKey")
	@EntityViewField(
			referenceTypes={FieldReferenceType.ALL}
	)
	private String value;

	/** クラス名 */
	@MetaFieldInfo(
			displayName="クラス名",
			displayNameKey="generic_editor_EditorValue_styleDisplaNameKey",
			description="スタイルシートのクラス名を指定します。複数指定する場合は半角スペースで区切ってください。",
			descriptionKey="generic_editor_EditorValue_styleDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.ALL}
	)
	private String style;

	/**
	 * デフォルトコンストラクタ
	 */
	public EditorValue() {
	}

	/**
	 * コンストラクタ
	 * @param label ラベル
	 * @param value 値
	 */
	public EditorValue(String label, String value) {
		this.label = label;
		this.value = value;
	}

	/**
	 * ラベルを取得します。
	 * @return ラベル
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * ラベルを設定します。
	 * @param label ラベル
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * 値を取得します。
	 * @return 値
	 */
	public String getValue() {
		return value;
	}

	/**
	 * 値を設定します。
	 * @param value 値
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * クラス名を取得します。
	 * @return クラス名
	 */
	public String getStyle() {
		return style;
	}

	/**
	 *  クラス名
	 * @param style クラス名
	 */
	public void setStyle(String style) {
		this.style = style;
	}

	/**
	 * 多言語設定情報を設定します。
	 * @param リスト
	 */
	public List<LocalizedStringDefinition> getLocalizedLabelList() {
		return localizedLabelList;
	}

	/**
	 * 多言語設定情報を追加します。
	 * @param 多言語設定情報
	 */
	public void setLocalizedLabelList(List<LocalizedStringDefinition> localizedLabelList) {
		this.localizedLabelList = localizedLabelList;
	}
}
