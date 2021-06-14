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

package org.iplass.mtp.view.generic.element.property;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.adminconsole.view.annotation.generic.EntityViewField;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.view.generic.Jsp;
import org.iplass.mtp.view.generic.Jsps;
import org.iplass.mtp.view.generic.NullOrderType;
import org.iplass.mtp.view.generic.RequiredDisplayType;
import org.iplass.mtp.view.generic.TextAlign;
import org.iplass.mtp.view.generic.ViewConst;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.element.CsvItem;

/**
 * 検索結果一覧用のプロパティ情報
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Jsps({
	@Jsp(path="/jsp/gem/generic/element/property/PropertyColumn.jsp", key=ViewConst.DESIGN_TYPE_GEM)
})
public class PropertyColumn extends PropertyBase implements CsvItem {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -98567336076608090L;

	/** 列幅 */
	@MetaFieldInfo(
			displayName="列幅",
			displayNameKey="generic_element_property_PropertyColumn_widthDisplaNameKey",
			inputType=InputType.NUMBER,
			displayOrder=400,
			description="列幅を指定します。",
			descriptionKey="generic_element_property_PropertyColumn_widthDescriptionKey"
	)
	private int width;

	/** テキストの配置 */
	@MetaFieldInfo(
			displayName="テキストの配置",
			displayNameKey="generic_element_property_PropertyColumn_textAlignDisplaNameKey",
			inputType=InputType.ENUM,
			enumClass=TextAlign.class,
			displayOrder=410,
			description="テキストの配置を指定します。<br>" +
					"LEFT:左寄せ<br>" +
					"CENTER:中央寄せ<br>" +
					"RIGHT:右寄せ",
			descriptionKey="generic_element_property_PropertyColumn_textAlignDescriptionKey"
	)
	private TextAlign textAlign;




	/** null項目のソート順 */
	@MetaFieldInfo(
			displayName="null項目のソート順",
			displayNameKey="generic_element_property_PropertyColumn_nullOrderTypeDisplaNameKey",
			inputType=InputType.ENUM,
			enumClass=NullOrderType.class,
			displayOrder=2100,
			description="null項目のソート順を指定します。<br>" +
					"NONE:未指定、DB依存<br>" +
					"FIRST:null項目を先頭にソート<br>" +
					"LAST:null項目を後尾にソート",
			descriptionKey="generic_element_property_PropertyColumn_nullOrderTypeDescriptionKey"
	)
	private NullOrderType nullOrderType;


	/** CSVの出力 */
	@MetaFieldInfo(
			displayName="CSVに出力する",
			displayNameKey="generic_element_property_PropertyColumn_outputCsvDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=2150,
			description="CSVに出力するかを設定します。",
			descriptionKey="generic_element_property_PropertyColumn_outputCsvDescriptionKey"
	)
	private boolean outputCsv = true;


	/** 一括更新プロパティエディタ */
	@MetaFieldInfo(
			displayName="一括更新プロパティエディタ",
			displayNameKey="generic_element_property_PropertyColumn_bulkUpdateEditorDisplaNameKey",
			inputType=InputType.REFERENCE,
			referenceClass = PropertyEditor.class,
			displayOrder=2200,
			description="プロパティの型にあわせたプロパティエディタを選択してください。<br>"
					+ "未指定の場合、一括更新が無効になります。",
			descriptionKey="generic_element_property_PropertyColumn_bulkUpdateEditorDescriptionKey"
	)
	@EntityViewField(
			overrideTriggerType=FieldReferenceType.DETAIL
	)
	private PropertyEditor bulkUpdateEditor;

	/** 一括更新必須属性表示タイプ */
	@MetaFieldInfo(
			displayName="一括更新必須属性表示",
			displayNameKey="generic_element_property_PropertyColumn_bulkUpdateRequiredDisplayTypeDisplaNameKey",
			inputType=InputType.ENUM,
			enumClass=RequiredDisplayType.class,
			displayOrder=2210,
			description="一括更新画面で必須表示を行うかを設定します。<BR />" +
					"DEFAULT : プロパティ定義の必須設定に従って必須属性を表示<BR />" +
					"DISPLAY : 必須属性を表示<BR />" +
					"NONE    : 必須属性を表示しない",
			descriptionKey="generic_element_property_PropertyColumn_bulkUpdateRequiredDisplayTypeDescriptionKey"
	)
	@EntityViewField()
	private RequiredDisplayType bulkUpdateRequiredDisplayType;

	/**
	 * デフォルトコンストラクタ
	 */
	public PropertyColumn() {
	}

	/**
	 * 列幅を取得します。
	 * @return 列幅
	 */
	public int getWidth() {
	    return width;
	}

	/**
	 * 列幅を設定します。
	 * @param width 列幅
	 */
	public void setWidth(int width) {
	    this.width = width;
	}

	/**
	 * null項目のソート順を取得します。
	 * @return null項目のソート順
	 */
	public NullOrderType getNullOrderType() {
		return nullOrderType;
	}

	/**
	 * null項目のソート順を設定します。
	 * @param nullOrderType null項目のソート順
	 */
	public void setNullOrderType(NullOrderType nullOrderType) {
		this.nullOrderType = nullOrderType;
	}

	/**
	 * CSVに出力するかを取得します。
	 * @return CSVに出力するか
	 */
	@Override
	public boolean isOutputCsv() {
		return outputCsv;
	}

	/**
	 * CSVに出力するかを設定します。
	 * @param outputCsv CSVに出力するか
	 */
	public void setOutputCsv(boolean outputCsv) {
		this.outputCsv = outputCsv;
	}

	/**
	 * テキストの配置を取得します。
	 * @return テキストの配置
	 */
	public TextAlign getTextAlign() {
	    return textAlign;
	}

	/**
	 * テキストの配置を設定します。
	 * @param textAlign テキストの配置
	 */
	public void setTextAlign(TextAlign textAlign) {
	    this.textAlign = textAlign;
	}

	/**
	 * 一括更新プロパティエディタを取得します。
	 * @return 一括更新プロパティエディタ
	 */
	public PropertyEditor getBulkUpdateEditor() {
		return bulkUpdateEditor;
	}

	/**
	 * 一括更新プロパティエディタを設定します。
	 * @param bulkUpdateEditor 一括更新プロパティエディタ
	 */
	public void setBulkUpdateEditor(PropertyEditor bulkUpdateEditor) {
		this.bulkUpdateEditor = bulkUpdateEditor;
	}

	/**
	 * 一括更新必須属性表示タイプを取得します。
	 * @return 一括更新必須属性表示タイプ
	 */
	public RequiredDisplayType getBulkUpdateRequiredDisplayType() {
		return bulkUpdateRequiredDisplayType;
	}

	/**
	 * 一括更新必須属性表示タイプを設定します。
	 * @param requiredDisplayType 一括更新必須属性表示タイプ
	 */
	public void setBulkUpdateRequiredDisplayType(RequiredDisplayType bulkUpdateRequiredDisplayType) {
		this.bulkUpdateRequiredDisplayType = bulkUpdateRequiredDisplayType;
	}
}
