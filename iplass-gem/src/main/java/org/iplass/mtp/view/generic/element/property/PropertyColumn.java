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
import org.iplass.mtp.view.generic.NullOrderType;
import org.iplass.mtp.view.generic.TextAlign;

/**
 * 検索結果一覧用のプロパティ情報
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PropertyColumn extends PropertyBase {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -98567336076608090L;

	/** 列幅 */
	@MetaFieldInfo(
			displayName="列幅",
			displayNameKey="generic_element_property_PropertyColumn_widthDisplaNameKey",
			inputType=InputType.NUMBER,
			description="列幅を指定します。",
			descriptionKey="generic_element_property_PropertyColumn_widthDescriptionKey"
	)
	private int width;

	/** null項目のソート順 */
	@MetaFieldInfo(
			displayName="null項目のソート順",
			displayNameKey="generic_element_property_PropertyColumn_nullOrderTypeDisplaNameKey",
			inputType=InputType.ENUM,
			enumClass=NullOrderType.class,
			description="null項目のソート順を指定します。<br>" +
					"NONE:未指定、DB依存<br>" +
					"FIRST:null項目を先頭にソート<br>" +
					"LAST:null項目を後尾にソート",
			descriptionKey="generic_element_property_PropertyColumn_nullOrderTypeDescriptionKey"
	)
	private NullOrderType nullOrderType;

	/** テキストの配置 */
	@MetaFieldInfo(
			displayName="テキストの配置",
			displayNameKey="generic_element_property_PropertyColumn_textAlignDisplaNameKey",
			inputType=InputType.ENUM,
			enumClass=TextAlign.class,
			description="テキストの配置を指定します。<br>" +
					"LEFT:左寄せ<br>" +
					"CENTER:中央寄せ<br>" +
					"RIGHT:右寄せ",
			descriptionKey="generic_element_property_PropertyColumn_textAlignDescriptionKey"
	)
	private TextAlign textAlign;

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
}
