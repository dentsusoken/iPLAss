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

package org.iplass.mtp.impl.view.generic.editor;

import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.entity.definition.properties.DateProperty;
import org.iplass.mtp.entity.definition.properties.ExpressionProperty;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.metadata.MetaDataRuntime;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.view.generic.EntityViewRuntime;
import org.iplass.mtp.impl.view.generic.FormViewRuntime;
import org.iplass.mtp.impl.view.generic.element.property.MetaPropertyLayout;
import org.iplass.mtp.view.generic.editor.DatePropertyEditor;
import org.iplass.mtp.view.generic.editor.PropertyEditor;

/**
 * 日付型プロパティエディタのメタデータ
 * @author lis3wg
 * @author SEKIGUCHI Naoya
 */
public class MetaDatePropertyEditor extends MetaDateTimePropertyEditor {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 8195076567521958652L;

	public static MetaDatePropertyEditor createInstance(PropertyEditor editor) {
		return new MetaDatePropertyEditor();
	}

	/** 現在日付設定ボタン表示可否 */
	private boolean hideButtonPanel;

	/** 曜日を表示 */
	private boolean showWeekday;

	/** 最大日付*/
	private String minDate;

	/** 最大日付*/
	private String maxDate;

	/** テキストフィールドへの直接入力を制限する */
	private boolean restrictDirectEditing;

	/**
	 * 現在日付設定ボタン表示可否を取得します。
	 * @return 現在日付設定ボタン表示可否
	 */
	public boolean isHideButtonPanel() {
		return hideButtonPanel;
	}

	/**
	 * 現在日付設定ボタン表示可否を設定します。
	 * @param hideButtonPanel 現在日付設定ボタン表示可否
	 */
	public void setHideButtonPanel(boolean hideButtonPanel) {
		this.hideButtonPanel = hideButtonPanel;
	}

	/**
	 * @return showWeekday
	 */
	public boolean isShowWeekday() {
		return showWeekday;
	}

	/**
	 * @param showWeekday セットする showWeekday
	 */
	public void setShowWeekday(boolean showWeekday) {
		this.showWeekday = showWeekday;
	}

	/**
	 * 最小日付を取得する
	 * @return 最小日付
	 */
	public String getMinDate() {
		return minDate;
	}

	/**
	 * 最小日付を設定する
	 * @param minDate 最小日付
	 */
	public void setMinDate(String minDate) {
		this.minDate = minDate;
	}

	/**
	 * 最大日付を取得する
	 * @return 最大日付
	 */
	public String getMaxDate() {
		return maxDate;
	}

	/**
	 * 最大日付を設定する
	 * @param maxDate 最大日付
	 */
	public void setMaxDate(String maxDate) {
		this.maxDate = maxDate;
	}

	/**
	 * テキストフィールドへの直接入力を制限する値を取得する
	 * @return テキストフィールドへの直接入力を制限する（true: 直接入力不可能、false: 直接入力可能）
	 */
	public boolean isRestrictDirectEditing() {
		return restrictDirectEditing;
	}

	/**
	 * テキストフィールドへの直接入力を制限する値を設定する
	 * @param restrictDirectEditing テキストフィールドへの直接入力を制限する
	 * @see {@link #isRestrictDirectEditing()}
	 */
	public void setRestrictDirectEditing(boolean restrictDirectEditing) {
		this.restrictDirectEditing = restrictDirectEditing;
	}

	@Override
	public void applyConfig(PropertyEditor editor) {
		super.fillFrom(editor);

		DatePropertyEditor de = (DatePropertyEditor) editor;
		this.hideButtonPanel = de.isHideButtonPanel();
		this.showWeekday = de.isShowWeekday();
		this.minDate = de.getMinDate();
		this.maxDate = de.getMaxDate();
		this.restrictDirectEditing = de.isRestrictDirectEditing();
	}

	@Override
	public PropertyEditor currentConfig(String propertyName) {
		DatePropertyEditor editor = new DatePropertyEditor();
		super.fillTo(editor);

		editor.setHideButtonPanel(hideButtonPanel);
		editor.setShowWeekday(showWeekday);
		editor.setMinDate(minDate);
		editor.setMaxDate(maxDate);
		editor.setRestrictDirectEditing(restrictDirectEditing);

		return editor;
	}

	@Override
	public MetaDatePropertyEditor copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public MetaDataRuntime createRuntime(EntityViewRuntime entityView, FormViewRuntime formView,
			MetaPropertyLayout propertyLayout, EntityContext context, EntityHandler eh) {
		return new PropertyEditorRuntime(entityView, formView, propertyLayout, context, eh) {
			@Override
			protected boolean checkPropertyType(PropertyDefinition pd) {
				if (pd == null) {
					return true;
				}
				if (pd instanceof DateProperty) {
					return true;
				}
				if (pd instanceof ExpressionProperty) {
					ExpressionProperty ep = (ExpressionProperty)pd;
					if (ep.getResultType() == PropertyDefinitionType.DATE) {
						return true;
					}
				}
				return false;
			}

		};
	}
}
