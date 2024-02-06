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

package org.iplass.mtp.impl.view.generic.editor;

import javax.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.entity.definition.properties.ExpressionProperty;
import org.iplass.mtp.entity.definition.properties.FloatProperty;
import org.iplass.mtp.entity.definition.properties.IntegerProperty;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.metadata.MetaDataRuntime;
import org.iplass.mtp.impl.view.generic.EntityViewRuntime;
import org.iplass.mtp.impl.view.generic.FormViewRuntime;
import org.iplass.mtp.impl.view.generic.element.property.MetaPropertyLayout;
import org.iplass.mtp.view.generic.editor.DecimalPropertyEditor;
import org.iplass.mtp.view.generic.editor.FloatPropertyEditor;
import org.iplass.mtp.view.generic.editor.IntegerPropertyEditor;
import org.iplass.mtp.view.generic.editor.NumberPropertyEditor;
import org.iplass.mtp.view.generic.editor.NumberPropertyEditor.NumberDisplayType;
import org.iplass.mtp.view.generic.editor.PropertyEditor;

/**
 * 数値型プロパティエディタのメタデータのスーパークラス
 * @author lis3wg
 */
@XmlSeeAlso({MetaDecimalPropertyEditor.class, MetaFloatPropertyEditor.class, MetaIntegerPropertyEditor.class})
public abstract class MetaNumberPropertyEditor extends MetaPrimitivePropertyEditor {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -9028002796797351149L;

	public static MetaNumberPropertyEditor createInstance(PropertyEditor editor) {
		if (editor instanceof DecimalPropertyEditor) {
			return MetaDecimalPropertyEditor.createInstance(editor);
		} else if (editor instanceof FloatPropertyEditor) {
			return MetaFloatPropertyEditor.createInstance(editor);
		} else if (editor instanceof IntegerPropertyEditor) {
			return MetaIntegerPropertyEditor.createInstance(editor);
		}
		return null;
	}

	/** 表示タイプ */
	private NumberDisplayType displayType;

	/** 数値のフォーマット */
	private String numberFormat;

	/** 表示内容をカンマ表示するか */
	private boolean showComma;

	/** 範囲で検索するか */
	private boolean searchInRange;

	/** 検索条件From非表示設定 */
	private boolean hideSearchConditionFrom;

	/** 検索条件To非表示設定 */
	private boolean hideSearchConditionTo;

	/** 検索条件範囲記号非表示設定 */
	private boolean hideSearchConditionRangeSymbol;

	/** 最大文字数 */
	private int maxlength;

	/** Label形式の場合の登録制御 */
	private boolean insertWithLabelValue = true;

	/** Label形式の場合の更新制御 */
	private boolean updateWithLabelValue = false;

	/**
	 * 表示タイプを取得します。
	 * @return 表示タイプ
	 */
	public NumberDisplayType getDisplayType() {
		return displayType;
	}

	/**
	 * 表示タイプを設定します。
	 * @param displayType 表示タイプ
	 */
	public void setDisplayType(NumberDisplayType displayType) {
		this.displayType = displayType;
	}

	/**
	 * 数値のフォーマットを取得します。
	 * @return 数値のフォーマット
	 */
	public String getNumberFormat() {
		return numberFormat;
	}

	/**
	 * 数値のフォーマットを設定します。
	 * @param numberFormat 数値のフォーマット
	 */
	public void setNumberFormat(String numberFormat) {
		this.numberFormat = numberFormat;
	}

	/**
	 * 表示内容をカンマ表示するかを取得します。
	 * @return 表示内容をカンマ表示するか
	 */
	public boolean isShowComma() {
		return showComma;
	}

	/**
	 * 表示内容をカンマ表示するかを設定します。
	 * @param showComma 表示内容をカンマ表示するか
	 */
	public void setShowComma(boolean showComma) {
		this.showComma = showComma;
	}

	/**
	 * 範囲で検索するかを取得します。
	 * @return 範囲で検索するか
	 */
	public boolean isSearchInRange() {
	    return searchInRange;
	}

	/**
	 * 範囲で検索するかを設定します。
	 * @param searchInRange 範囲で検索するか
	 */
	public void setSearchInRange(boolean searchInRange) {
	    this.searchInRange = searchInRange;
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

	/**
	 * 検索条件範囲記号非表示設定を取得します。
	 * @return 検索条件範囲記号非表示設定
	 */
	public boolean isHideSearchConditionRangeSymbol() {
		return hideSearchConditionRangeSymbol;
	}

	/**
	 * 検索条件範囲記号非表示設定を設定します。
	 * @param hideSearchConditionRangeSymbol 検索条件範囲記号非表示設定
	 */
	public void setHideSearchConditionRangeSymbol(boolean hideSearchConditionRangeSymbol) {
		this.hideSearchConditionRangeSymbol = hideSearchConditionRangeSymbol;
	}

	/**
	 * 最大文字数を取得します。
	 * @return 最大文字数
	 */
	public int getMaxlength() {
		return maxlength;
	}

	/**
	 * 最大文字数を設定します。
	 * @param maxlength 最大文字数
	 */
	public void setMaxlength(int maxlength) {
		this.maxlength = maxlength;
	}

	/**
	 * 表示タイプがLabel形式の場合に、登録時に登録対象にするかを返します。
	 *
	 * @return true：登録対象
	 */
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

	/**
	 * 表示タイプがLabel形式の場合に、更新時に更新対象にするかを返します。
	 *
	 * @return true：更新対象
	 */
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

	@Override
	protected void fillFrom(PropertyEditor editor) {
		super.fillFrom(editor);

		NumberPropertyEditor pe = (NumberPropertyEditor) editor;
		displayType = pe.getDisplayType();
		numberFormat = pe.getNumberFormat();
		showComma = pe.isShowComma();
		searchInRange = pe.isSearchInRange();
		hideSearchConditionFrom = pe.isHideSearchConditionFrom();
		hideSearchConditionTo = pe.isHideSearchConditionTo();
		hideSearchConditionRangeSymbol = pe.isHideSearchConditionRangeSymbol();
		maxlength = pe.getMaxlength();
		insertWithLabelValue = pe.isInsertWithLabelValue();
		updateWithLabelValue = pe.isUpdateWithLabelValue();
	}

	@Override
	protected void fillTo(PropertyEditor editor) {
		super.fillTo(editor);

		NumberPropertyEditor pe = (NumberPropertyEditor) editor;
		pe.setDisplayType(displayType);
		pe.setNumberFormat(numberFormat);
		pe.setShowComma(showComma);
		pe.setSearchInRange(searchInRange);
		pe.setHideSearchConditionFrom(hideSearchConditionFrom);
		pe.setHideSearchConditionTo(hideSearchConditionTo);
		pe.setHideSearchConditionRangeSymbol(hideSearchConditionRangeSymbol);
		pe.setInsertWithLabelValue(insertWithLabelValue);
		pe.setUpdateWithLabelValue(updateWithLabelValue);
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
				if (pd instanceof FloatProperty || pd instanceof IntegerProperty) {
					return true;
				}
				if (pd instanceof ExpressionProperty) {
					ExpressionProperty ep = (ExpressionProperty)pd;
					if (ep.getResultType() == PropertyDefinitionType.FLOAT
							|| ep.getResultType() == PropertyDefinitionType.INTEGER) {
						return true;
					}
				}
				return false;
			}

		};
	}
}
