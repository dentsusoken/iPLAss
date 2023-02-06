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
import org.iplass.mtp.entity.definition.properties.DateTimeProperty;
import org.iplass.mtp.entity.definition.properties.ExpressionProperty;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.metadata.MetaDataRuntime;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.view.generic.EntityViewRuntime;
import org.iplass.mtp.impl.view.generic.FormViewRuntime;
import org.iplass.mtp.impl.view.generic.element.property.MetaPropertyLayout;
import org.iplass.mtp.view.generic.editor.DateTimePropertyEditor.MinIntereval;
import org.iplass.mtp.view.generic.editor.DateTimePropertyEditor.TimeDispRange;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.TimestampPropertyEditor;

/**
 * 日時型プロパティエディタのメタデータ
 * @author lis3wg
 * @author SEKIGUCHI Naoya
 */
public class MetaTimestampPropertyEditor extends MetaDateTimePropertyEditor {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1640054951421530441L;

	public static MetaTimestampPropertyEditor createInstance(PropertyEditor editor) {
		return new MetaTimestampPropertyEditor();
	}

	/** 時間の表示範囲 */
	private TimeDispRange dispRange;

	/** 分の間隔 */
	private MinIntereval interval;

	/** 現在日付設定ボタン表示可否 */
	private boolean hideButtonPanel;

	/** 時間のデフォルト値補完を行わない */
	private boolean notFillTime;

	/** DatetimePickerの利用有無 */
	private boolean useDatetimePicker;

	/** 曜日を表示 */
	private boolean showWeekday;

	/** 最小日付 */
	private String minDate;

	/** 最大日付 */
	private String maxDate;

	/** テキストフィールドへの直接入力を制限する */
	private boolean restrictDirectEditing;
	/**
	 * 時間の表示範囲を取得します。
	 * @return 時間の表示範囲
	 */
	public TimeDispRange getDispRange() {
		return dispRange;
	}

	/**
	 * 時間の表示範囲を設定します。
	 * @param dispRange 時間の表示範囲
	 */
	public void setDispRange(TimeDispRange dispRange) {
		this.dispRange = dispRange;
	}

	/**
	 * 分の間隔を取得します。
	 * @return 分の間隔
	 */
	public MinIntereval getInterval() {
		return interval;
	}

	/**
	 * 分の間隔を設定します。
	 * @param interval 分の間隔
	 */
	public void setInterval(MinIntereval interval) {
		this.interval = interval;
	}

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
	 * 時間のデフォルト値補完を行わないを取得します。
	 * @return 時間のデフォルト値補完を行わない
	 */
	public boolean isNotFillTime() {
		return notFillTime;
	}

	/**
	 * 時間のデフォルト値補完を行わないを設定します。
	 * @param notFillTime 時間のデフォルト値補完を行わない
	 */
	public void setNotFillTime(boolean notFillTime) {
		this.notFillTime = notFillTime;
	}

	/**
	 * DatetimePickerの利用有無を取得します。
	 * @return DatetimePickerの利用有無
	 */
	public boolean isUseDatetimePicker() {
		return useDatetimePicker;
	}

	/**
	 * DatetimePickerの利用有無を設定します。
	 * @param displayDateTimePicker DatetimePickerの利用有無
	 */
	public void setUseDatetimePicker(boolean useDatetimePicker) {
		this.useDatetimePicker = useDatetimePicker;
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

		TimestampPropertyEditor pe = (TimestampPropertyEditor) editor;
		this.dispRange = pe.getDispRange();
		this.interval = pe.getInterval();
		this.hideButtonPanel = pe.isHideButtonPanel();
		this.notFillTime = pe.isNotFillTime();
		this.useDatetimePicker = pe.isUseDatetimePicker();
		this.showWeekday = pe.isShowWeekday();
		this.minDate = pe.getMinDate();
		this.maxDate = pe.getMaxDate();
		this.restrictDirectEditing = pe.isRestrictDirectEditing();
	}

	@Override
	public PropertyEditor currentConfig(String propertyName) {
		TimestampPropertyEditor editor = new TimestampPropertyEditor();
		super.fillTo(editor);

		editor.setDispRange(this.dispRange);
		editor.setInterval(this.interval);
		editor.setHideButtonPanel(hideButtonPanel);
		editor.setNotFillTime(this.notFillTime);
		editor.setUseDatetimePicker(this.useDatetimePicker);
		editor.setShowWeekday(this.showWeekday);
		editor.setMinDate(minDate);
		editor.setMaxDate(maxDate);
		editor.setRestrictDirectEditing(restrictDirectEditing);

		return editor;
	}

	@Override
	public MetaTimestampPropertyEditor copy() {
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
				if (pd instanceof DateTimeProperty) {
					return true;
				}
				if (pd instanceof ExpressionProperty) {
					ExpressionProperty ep = (ExpressionProperty)pd;
					if (ep.getResultType() == PropertyDefinitionType.DATETIME) {
						return true;
					}
				}
				return false;
			}
		};
	}

}
