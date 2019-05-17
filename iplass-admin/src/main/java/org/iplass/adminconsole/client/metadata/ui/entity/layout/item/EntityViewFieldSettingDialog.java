/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.metafield.MetaFieldSettingDialog;
import org.iplass.adminconsole.view.annotation.Refrectable;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;

/**
 * EntityView用のプロパティ編集用View
 *
 * 起動トリガーのタイプを保持する。
 */
public class EntityViewFieldSettingDialog extends MetaFieldSettingDialog {

	private FieldReferenceType triggerType;
	private String defName;
	private String refDefName;

	/** タイトルにつける説明 */
	private String originalTitle;
	private String titleDescription;

	public EntityViewFieldSettingDialog(String className, Refrectable value, FieldReferenceType triggerType,
			String defName) {
		super(className, value);
		this.triggerType = triggerType;
		this.defName = defName;
		init();
	}

	public EntityViewFieldSettingDialog(String className, Refrectable value, FieldReferenceType triggerType,
			String defName, String refDefName) {
		super(className, value);
		this.triggerType = triggerType;
		this.defName = defName;
		this.refDefName = refDefName;
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
	public void setTitleDescription(String titleDescription) {
		if (originalTitle == null) {
			originalTitle = getTitle();
		}
		this.titleDescription = titleDescription;

		if (SmartGWTUtil.isNotEmpty(titleDescription)) {
			setTitle(originalTitle + " - " + titleDescription + "-");
		} else {
			setTitle(originalTitle);
		}
	}

	/**
	 * タイトルの説明を返します。
	 *
	 * @return タイトルの説明
	 */
	public String getTitleDescription() {
		return titleDescription;
	}

}
