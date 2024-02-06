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

package org.iplass.adminconsole.client.metadata.ui.entity.layout.item;

import org.iplass.adminconsole.client.metadata.ui.entity.layout.metafield.MetaFieldSettingDialog;
import org.iplass.adminconsole.view.annotation.Refrectable;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.view.generic.HasNestProperty;
import org.iplass.mtp.view.generic.editor.JoinPropertyEditor;

/**
 * EntityView用のプロパティ編集用View
 *
 * 起動トリガーのタイプを保持する。
 */
public class EntityViewFieldSettingDialog extends MetaFieldSettingDialog {

	private FieldReferenceType triggerType;
	private String defName;
	private String refDefName;

	//起動元
	private EntityViewFieldSettingPane prev;

	/** クラス名のタイトル */
	private String classNameTitle;
	/** プロパティの情報 */
	private PropertyInfo propertyInfo;

	public EntityViewFieldSettingDialog(String className, Refrectable value, FieldReferenceType triggerType,
			String defName) {
		this(className, value, triggerType, defName, null, null);
	}

	public EntityViewFieldSettingDialog(String className, Refrectable value, FieldReferenceType triggerType,
			String defName, String refDefName) {
		this(className, value, triggerType, defName, refDefName, null);
	}

	public EntityViewFieldSettingDialog(String className, Refrectable value, FieldReferenceType triggerType,
			String defName, String refDefName, EntityViewFieldSettingPane prev) {
		super(className, value);
		this.triggerType = triggerType;
		this.defName = defName;
		this.refDefName = refDefName;
		this.prev = prev;
		init();
	}

	@Override
	protected EntityViewFieldSettingPane createPane(String className, Refrectable value) {
		if (refDefName != null) {
			return new EntityViewFieldSettingPane(this, className, value, triggerType, defName, refDefName);
		} else {
			return new EntityViewFieldSettingPane(this, className, value, triggerType, defName);
		}
	}

	/**
	 * タイトルに説明を付け足します。
	 *
	 * @param titleDescription タイトルの説明
	 */
	public void setTitlePropertyInfo(PropertyInfo propertyInfo) {
		if (classNameTitle == null) {
			classNameTitle = getTitle();
		}
		this.propertyInfo = propertyInfo;

		if (propertyInfo != null) {
			setTitle(classNameTitle + " (" + propertyInfo.toString() + ")");
		} else {
			setTitle(classNameTitle);
		}
	}

	/**
	 * タイトルの説明を返します。
	 *
	 * @return タイトルの説明
	 */
	public PropertyInfo getTitlePropertyInfo() {
		return propertyInfo;
	}

	/**
	 * NestPropertyをサポートしているかを返します。
	 *
	 * @return NestPropertyをサポートしているか
	 */
	public boolean hasNestProperty() {
		return getValue() instanceof HasNestProperty;
	}

	/**
	 * NestPropertyの対象Entity名を返します。
	 *
	 * @return NestPropertyの対象Entity名
	 */
	public String getNestPropertyReferenceEntityName() {
		if (hasNestProperty()) {
			if (getValue() instanceof JoinPropertyEditor) {
				//JoinPropertyEditorのNestPropertyは前画面のEntityが対象
				if (prev != null) {
					return prev.getOwner().getNestPropertyReferenceEntityName();
				}
			}
		}
		return refDefName;
	}

	/**
	 * 対象のプロパティ情報
	 */
	public static class PropertyInfo {

		/** プロパティ名 */
		private final String propertyName;
		/** プロパティ表示名 */
		private final String displayName;

		public PropertyInfo(String propertyName, String displayName) {
			this.propertyName = propertyName;
			this.displayName = displayName;
		}

		public String getPropertyName() {
			return propertyName;
		}

		public String getDisplayName() {
			return displayName;
		}

		@Override
		public String toString() {
			return displayName + "[" + propertyName + "]";
		}
	}
}
