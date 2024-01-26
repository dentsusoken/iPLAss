/*
 * Copyright (C) 2015 DENTSU SOKEN INC. All Rights Reserved.
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

/**
 * Entity内に同一Entityの参照を持つ再帰構造のEntityをツリー表示する設定
 */
public class ReferenceRecursiveTreeSetting implements Refrectable {

	private static final long serialVersionUID = -6738046752926240015L;

	/** ツリーのルートに表示するデータの検索条件 */
	@MetaFieldInfo(
			displayName="最上位階層の検索条件",
			displayNameKey="generic_editor_ReferenceRecursiveTreeSetting_rootConditionDisplaNameKey",
			description="<b>表示タイプ:Tree</b><br>" +
					"ツリーの初期表示時に検索する最上位階層のデータの検索条件を設定します。<br>" +
					"未指定の場合全データが最上位階層に表示されます。",
			descriptionKey="generic_editor_ReferenceRecursiveTreeSetting_rootConditionDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.ALL}
	)
	private String rootCondition;

	/** 子階層のプロパティ名 */
	@MetaFieldInfo(
			displayName="子階層のプロパティ名",
			displayNameKey="generic_editor_ReferenceRecursiveTreeSetting_childPropertyNameDisplaNameKey",
			inputType=InputType.PROPERTY,
			description="<b>表示タイプ:Tree</b><br>" +
					"再帰構造のEntityが持つ子階層のプロパティ名を指定します。",
			descriptionKey="generic_editor_ReferenceRecursiveTreeSetting_childPropertyNameDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.ALL}
	)
	private String childPropertyName;

	/**
	 * ツリーのルートに表示するデータの検索条件を取得します。
	 * @return ツリーのルートに表示するデータの検索条件
	 */
	public String getRootCondition() {
	    return rootCondition;
	}

	/**
	 * ツリーのルートに表示するデータの検索条件を設定します。
	 * @param rootCondition ツリーのルートに表示するデータの検索条件
	 */
	public void setRootCondition(String rootCondition) {
	    this.rootCondition = rootCondition;
	}

	/**
	 * 子階層のプロパティ名を取得します。
	 * @return 子階層のプロパティ名
	 */
	public String getChildPropertyName() {
	    return childPropertyName;
	}

	/**
	 * 子階層のプロパティ名を設定します。
	 * @param childPropertyName 子階層のプロパティ名
	 */
	public void setChildPropertyName(String childPropertyName) {
	    this.childPropertyName = childPropertyName;
	}

}
