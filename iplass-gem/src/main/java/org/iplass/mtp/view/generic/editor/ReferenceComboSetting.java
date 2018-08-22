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

import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.adminconsole.view.annotation.Refrectable;
import org.iplass.adminconsole.view.annotation.generic.EntityViewField;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor.RefSortType;

/**
 * 参照コンボ設定
 * @author lis3wg
 */
public class ReferenceComboSetting implements Refrectable {

	/** SerialVersionUID */
	private static final long serialVersionUID = -4275361512123534366L;

	/** プロパティ名 */
	@MetaFieldInfo(
			displayName="プロパティ名",
			displayNameKey="generic_editor_ReferenceComboSetting_propertyNameDisplaNameKey",
			inputType=InputType.PROPERTY,
			useReferenceType=true,
			description="被参照のプロパティを指定します。",
			descriptionKey="generic_editor_ReferenceComboSetting_propertyNameDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION, FieldReferenceType.DETAIL}
	)
	private String propertyName;

	/** 検索条件 */
	@MetaFieldInfo(
			displayName="検索条件",
			displayNameKey="generic_editor_ReferenceComboSetting_conditionDisplaNameKey",
			description="表示する選択肢を検索する際に付与する検索条件を設定します。",
			descriptionKey="generic_editor_ReferenceComboSetting_conditionDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION, FieldReferenceType.DETAIL}
	)
	private String condition;

	/** 上位参照コンボ設定 */
	@MetaFieldInfo(
			displayName="参照コンボ設定",
			displayNameKey="generic_editor_ReferenceComboSetting_parentDisplaNameKey",
			inputType=InputType.REFERENCE,
			referenceClass=ReferenceComboSetting.class,
			description="コンボの内容を絞り込む条件を指定します。",
			descriptionKey="generic_editor_ReferenceComboSetting_parentDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION, FieldReferenceType.DETAIL}
	)
	private ReferenceComboSetting parent;

	/** ソートアイテム */
	@MetaFieldInfo(
			displayName="ソートアイテム",
			displayNameKey="generic_editor_ReferencePropertyEditor_sortItemDisplaNameKey",
			inputType=InputType.PROPERTY,
			description="参照データをソートする項目を指定します。",
			descriptionKey="generic_editor_ReferencePropertyEditor_sortItemDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION, FieldReferenceType.DETAIL}
	)
	private String sortItem;

	/** ソート種別 */
	@MetaFieldInfo(
			displayName="ソート種別",
			displayNameKey="generic_editor_ReferencePropertyEditor_sortTypeDisplaNameKey",
			inputType=InputType.ENUM,
			enumClass=RefSortType.class,
			description="参照データをソートする順序を指定します。",
			descriptionKey="generic_editor_ReferencePropertyEditor_sortTypeDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION, FieldReferenceType.DETAIL}
	)
	private RefSortType sortType;

	/**
	 * プロパティ名を取得します。
	 * @return プロパティ名
	 */
	public String getPropertyName() {
	    return propertyName;
	}

	/**
	 * プロパティ名を設定します。
	 * @param propertyName プロパティ名
	 */
	public void setPropertyName(String propertyName) {
	    this.propertyName = propertyName;
	}

	/**
	 * 検索条件を取得します。
	 * @return 検索条件
	 */
	public String getCondition() {
	    return condition;
	}

	/**
	 * 検索条件を設定します。
	 * @param condition 検索条件
	 */
	public void setCondition(String condition) {
	    this.condition = condition;
	}

	/**
	 * 上位参照コンボ設定を取得します。
	 * @return 上位参照コンボ設定
	 */
	public ReferenceComboSetting getParent() {
	    return parent;
	}

	/**
	 * 上位参照コンボ設定を設定します。
	 * @param parent 上位参照コンボ設定
	 */
	public void setParent(ReferenceComboSetting parent) {
	    this.parent = parent;
	}

	/**
	 * ソートアイテムを取得します。
	 * @return ソートアイテム
	 */
	public String getSortItem() {
		return sortItem;
	}

	/**
	 * ソートアイテムを設定します。
	 * @param sortItem ソートアイテム
	 */
	public void setSortItem(String sortItem) {
		this.sortItem = sortItem;
	}

	/**
	 * ソート種別を取得します。
	 * @return ソート種別
	 */
	public RefSortType getSortType() {
		return sortType;
	}

	/**
	 * ソート種別を設定します。
	 * @param sortType ソート種別
	 */
	public void setSortType(RefSortType sortType) {
		this.sortType = sortType;
	}
}
