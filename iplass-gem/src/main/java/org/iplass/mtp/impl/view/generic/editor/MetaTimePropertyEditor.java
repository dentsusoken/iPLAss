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

import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.view.generic.editor.DateTimePropertyEditor.MinIntereval;
import org.iplass.mtp.view.generic.editor.DateTimePropertyEditor.TimeDispRange;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.TimePropertyEditor;

/**
 * 時間型プロパティエディタのメタデータ
 * @author lis3wg
 */
public class MetaTimePropertyEditor extends MetaDateTimePropertyEditor {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 8195076567521958652L;

	public static MetaTimePropertyEditor createInstance(PropertyEditor editor) {
		return new MetaTimePropertyEditor();
	}

	/** 時間の表示範囲 */
	private TimeDispRange dispRange;

	/** 分の間隔 */
	private MinIntereval interval;

	/** TimePickerの利用有無 */
	private boolean useTimePicker;

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
	 * TimePickerの利用有無を取得します。
	 * @return TimePickerの利用有無
	 */
	public boolean isUseTimePicker() {
		return useTimePicker;
	}

	/**
	 * TimePickerの利用有無を設定します。
	 * @param useTimePicker TimePickerの利用有無
	 */
	public void setUseTimePicker(boolean useTimePicker) {
		this.useTimePicker = useTimePicker;
	}

	@Override
	public void applyConfig(PropertyEditor editor) {
		super.fillFrom(editor);

		TimePropertyEditor pe = (TimePropertyEditor) editor;
		this.dispRange = pe.getDispRange();
		this.interval = pe.getInterval();
		this.useTimePicker = pe.isUseTimePicker();
	}

	@Override
	public PropertyEditor currentConfig(String propertyName) {
		TimePropertyEditor editor = new TimePropertyEditor();
		super.fillTo(editor);

		editor.setDispRange(this.dispRange);
		editor.setInterval(this.interval);
		editor.setUseTimePicker(this.useTimePicker);
		return editor;
	}

	@Override
	public MetaTimePropertyEditor copy() {
		return ObjectUtil.deepCopy(this);
	}

}
