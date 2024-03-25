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

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlEnumValue;

import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.adminconsole.view.annotation.generic.EntityViewField;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.view.generic.Jsp;
import org.iplass.mtp.view.generic.Jsps;
import org.iplass.mtp.view.generic.ViewConst;

/**
 * AutoNumber型プロパティエディタ
 * @author Y.Kazama
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Jsps({
	@Jsp(path="/jsp/gem/generic/editor/AutoNumberPropertyEditor.jsp", key=ViewConst.DESIGN_TYPE_GEM),
	@Jsp(path="/jsp/gem/aggregation/unit/editor/AutoNumberPropertyEditor.jsp", key=ViewConst.DESIGN_TYPE_GEM_AGGREGATION)
})
public class AutoNumberPropertyEditor extends PrimitivePropertyEditor implements LabelablePropertyEditor {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 6809027984079078798L;

	/** 表示タイプ */
	public enum AutoNumberDisplayType {
		@XmlEnumValue("Text")TEXT,
		@XmlEnumValue("Label")LABEL,
		@XmlEnumValue("Hidden")HIDDEN
	}

	@MetaFieldInfo(
			displayName="表示タイプ",
			displayNameKey="generic_editor_AutoNumberPropertyEditor_displayTypeDisplaNameKey",
			inputType=InputType.ENUM,
			enumClass=AutoNumberDisplayType.class,
			required=true,
			displayOrder=100,
			description="画面に表示する方法を選択します。",
			descriptionKey="generic_editor_AutoNumberPropertyEditor_displayTypeDescriptionKey"
	)
	private AutoNumberDisplayType displayType;

	/** 検索条件完全一致設定 */
	@MetaFieldInfo(
			displayName="検索完全一致設定",
			displayNameKey="generic_editor_AutoNumberPropertyEditor_searchExactMatchConditionDisplaNameKey",
			description="チェック時は完全一致検索します。<br>未チェック時はLike検索します。",
			inputType=InputType.CHECKBOX,
			displayOrder=110,
			descriptionKey="generic_editor_AutoNumberPropertyEditor_searchExactMatchConditionDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION}
	)
	private boolean searchExactMatchCondition;

	/** Label形式の場合の登録制御 */
	@MetaFieldInfo(
			displayName="Label形式の場合に表示値を登録する",
			displayNameKey="generic_editor_LabelablePropertyEditor_insertWithLabelValueDisplaNameKey",
			description="表示タイプがLabel形式の場合に表示値をそのまま登録するかを指定します。",
			inputType=InputType.CHECKBOX,
			displayOrder=200,
			descriptionKey="generic_editor_LabelablePropertyEditor_insertWithLabelValueDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private boolean insertWithLabelValue = true;

	/** Label形式の場合の更新制御 */
	@MetaFieldInfo(
			displayName="Label形式の場合に表示値で更新する",
			displayNameKey="generic_editor_LabelablePropertyEditor_updateWithLabelValueDisplaNameKey",
			description="表示タイプがLabel形式の場合に表示値で更新するかを指定します。",
			inputType=InputType.CHECKBOX,
			displayOrder=210,
			descriptionKey="generic_editor_LabelablePropertyEditor_updateWithLabelValueDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private boolean updateWithLabelValue = false;

	/**
	 * デフォルトコンストラクタ
	 */
	public AutoNumberPropertyEditor() {
	}

	@Override
	public AutoNumberDisplayType getDisplayType() {
		if (displayType == null) {
			displayType = AutoNumberDisplayType.TEXT;
		}
		return displayType;
	}

	/**
	 * 表示タイプを設定します。
	 * @param displayType
	 */
	public void setDisplayType(AutoNumberDisplayType displayType) {
		this.displayType = displayType;
	}

	@Override
	public boolean isHide() {
		return displayType == AutoNumberDisplayType.HIDDEN;
	}

	@Override
	public String getDefaultValue() {
		// デフォルト値なし、空実装
		return null;
	}

	@Override
	public void setDefaultValue(String defaultValue) {
		// デフォルト値なし、空実装
	}

	/**
	 * 検索条件完全一致設定を取得します。
	 *
	 * @return 検索条件完全一致設定
	 */
	public boolean isSearchExactMatchCondition() {
		return searchExactMatchCondition;
	}

	/**
	 * 検索条件完全一致を設定します。
	 *
	 * @param searchExactMatchCondition 検索条件完全一致設定
	 */
	public void setSearchExactMatchCondition(boolean searchExactMatchCondition) {
		this.searchExactMatchCondition = searchExactMatchCondition;
	}

	@Override
	public boolean isLabel() {
		return displayType == AutoNumberDisplayType.LABEL;
	}

	@Override
	public boolean isInsertWithLabelValue() {
		return insertWithLabelValue;
	}

	/**
	 * Label形式の場合の登録制御を設定します。
	 *
	 * @param insertWithLabelValue Label形式の場合の登録制御
	 */
	public void setInsertWithLabelValue(boolean insertWithLabelValue) {
		this.insertWithLabelValue = insertWithLabelValue;
	}

	@Override
	public boolean isUpdateWithLabelValue() {
		return updateWithLabelValue;
	}

	/**
	 * Label形式の場合の更新制御を設定します。
	 *
	 * @param updateWithLabelValue Label形式の場合の更新制御
	 */
	public void setUpdateWithLabelValue(boolean updateWithLabelValue) {
		this.updateWithLabelValue = updateWithLabelValue;
	}

}
