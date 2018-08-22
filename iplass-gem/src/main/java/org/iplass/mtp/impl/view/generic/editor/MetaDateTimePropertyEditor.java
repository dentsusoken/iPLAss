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

import javax.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.view.generic.editor.DatePropertyEditor;
import org.iplass.mtp.view.generic.editor.DateTimePropertyEditor;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.TimePropertyEditor;
import org.iplass.mtp.view.generic.editor.TimestampPropertyEditor;
import org.iplass.mtp.view.generic.editor.DateTimePropertyEditor.DateTimeDisplayType;

/**
 * 日付・時間型プロパティエディタのメタデータのスーパークラス
 * @author lis3wg
 */
@XmlSeeAlso({MetaDatePropertyEditor.class, MetaTimePropertyEditor.class, MetaTimestampPropertyEditor.class})
public abstract class MetaDateTimePropertyEditor extends MetaPrimitivePropertyEditor {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 8195076567521958652L;

	public static MetaDateTimePropertyEditor createInstance(PropertyEditor editor) {
		if (editor instanceof DatePropertyEditor) {
			return MetaDatePropertyEditor.createInstance(editor);
		} else if (editor instanceof TimePropertyEditor) {
			return MetaTimePropertyEditor.createInstance(editor);
		} else if (editor instanceof TimestampPropertyEditor) {
			return MetaTimestampPropertyEditor.createInstance(editor);
		}
		return null;
	}

	/** 表示タイプ */
	private DateTimeDisplayType displayType;

	/** 検索条件の単一日指定 */
	private boolean singleDayCondition;

	/** 検索条件From非表示設定 */
	private boolean hideSearchConditionFrom;

	/** 検索条件To非表示設定 */
	private boolean hideSearchConditionTo;

	/**
	 * 表示タイプを取得します。
	 * @return 表示タイプ
	 */
	public DateTimeDisplayType getDisplayType() {
		return displayType;
	}

	/**
	 * 表示タイプを設定します。
	 * @param displayType 表示タイプ
	 */
	public void setDisplayType(DateTimeDisplayType displayType) {
		this.displayType = displayType;
	}

	/**
	 * 検索条件の単一日指定を取得します。
	 * @return 検索条件の単一日指定
	 */
	public boolean isSingleDayCondition() {
		return singleDayCondition;
	}

	/**
	 * 検索条件の単一日指定を設定します
	 * @param singleDayCondition 検索条件の単一日指定
	 */
	public void setSingleDayCondition(boolean singleDayCondition) {
		this.singleDayCondition = singleDayCondition;
	}

	/**
	 * 検索条件From非表示設定を取得します。
	 * @return 検索条件From非表示設定
	 */
	public boolean isHideSearchConditionFrom() {
	    return hideSearchConditionFrom;
	}

	/**
	 * 検索条件From非表示設定を設定します。
	 * @param hideSearchConditionFrom 検索条件From非表示設定
	 */
	public void setHideSearchConditionFrom(boolean hideSearchConditionFrom) {
	    this.hideSearchConditionFrom = hideSearchConditionFrom;
	}

	/**
	 * 検索条件To非表示設定を取得します。
	 * @return 検索条件To非表示設定
	 */
	public boolean isHideSearchConditionTo() {
	    return hideSearchConditionTo;
	}

	/**
	 * 検索条件To非表示設定を設定します。
	 * @param hideSearchConditionTo 検索条件To非表示設定
	 */
	public void setHideSearchConditionTo(boolean hideSearchConditionTo) {
	    this.hideSearchConditionTo = hideSearchConditionTo;
	}

	@Override
	protected void fillFrom(PropertyEditor editor) {
		super.fillFrom(editor);

		DateTimePropertyEditor e = (DateTimePropertyEditor) editor;
		displayType = e.getDisplayType();
		singleDayCondition = e.isSingleDayCondition();
		hideSearchConditionFrom = e.isHideSearchConditionFrom();
		hideSearchConditionTo = e.isHideSearchConditionTo();
	}

	@Override
	protected void fillTo(PropertyEditor editor) {
		super.fillTo(editor);

		DateTimePropertyEditor pe = (DateTimePropertyEditor) editor;
		pe.setDisplayType(displayType);
		pe.setSingleDayCondition(singleDayCondition);
		pe.setHideSearchConditionFrom(hideSearchConditionFrom);
		pe.setHideSearchConditionTo(hideSearchConditionTo);
	}

}
