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

package org.iplass.mtp.view.generic.editor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.iplass.adminconsole.view.annotation.FieldOrder;
import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.adminconsole.view.annotation.Refrectable;
import org.iplass.adminconsole.view.annotation.generic.EntityViewField;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * プロパティを表示・編集するためのエディタ
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({PrimitivePropertyEditor.class, ReferencePropertyEditor.class, CustomPropertyEditor.class})
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS)
@FieldOrder(manual=true)
public abstract class PropertyEditor implements Refrectable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 8567197962881627914L;

	/** プロパティ名 */
	protected String propertyName;

	/** 表示カスタムスタイル */
	@MetaFieldInfo(
			displayName="表示カスタムスタイル",
			displayNameKey="generic_editor_PropertyEditor_customStyleDisplayNameKey",
			displayOrder=10000,
			description="検索結果一覧表示用のスタイルを指定します。GroovyScriptで記述して下さい。",
			descriptionKey="generic_editor_PropertyEditor_customStyleDescriptionKey",
			inputType=InputType.SCRIPT
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHRESULT, FieldReferenceType.DETAIL}
	)
	private String customStyle;

	/** 入力カスタムスタイル */
	@MetaFieldInfo(
			displayName="入力カスタムスタイル",
			displayNameKey="generic_editor_PropertyEditor_inputCustomStyleDisplayNameKey",
			displayOrder=10010,
			description="編集画面のinput要素に対するスタイルを指定します。(例)width:100px;",
			descriptionKey="generic_editor_PropertyEditor_inputCustomStyleDescriptionKey",
			inputType=InputType.SCRIPT,
			mode="CSS"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION, FieldReferenceType.DETAIL, FieldReferenceType.BULK}
	)
	private String inputCustomStyle;

	/** 表示カスタムスタイルキー */
	private String outputCustomStyleScriptKey;

	/** 入力カスタムスタイルキー */
	private String inputCustomStyleScriptKey;

	/**
	 * プロパティ名を取得します。
	 * @return プロパティ名
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * プロパティ名を設定します。
	 * @param propertyName プロパティ名
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * 表示タイプを取得します。
	 * @return 表示タイプ
	 */
	public abstract Enum<?> getDisplayType();

	/**
	 * 非表示かを取得します。
	 * @return true：非表示
	 */
	public abstract boolean isHide();

	/**
	 * 表示カスタムスタイルを取得します。
	 * @return 表示カスタムスタイル
	 */
	public String getCustomStyle() {
		return customStyle;
	}

	/**
	 * 表示カスタムスタイルを設定します。
	 * @param customStyle 表示カスタムスタイル
	 */
	public void setCustomStyle(String customStyle) {
		this.customStyle = customStyle;
	}


	/**
	 * 入力カスタムスタイルを取得します。
	 * @return 入力カスタムスタイル
	 */
	public String getInputCustomStyle() {
		return inputCustomStyle;
	}

	/**
	 * 入力カスタムスタイルを設定します。
	 * @param inputCustomStyle 入力カスタムスタイル
	 */
	public void setInputCustomStyle(String inputCustomStyle) {
		this.inputCustomStyle = inputCustomStyle;
	}

	/**
	 * 表示カスタムスタイルキー
	 * @return 表示カスタムスタイルキー
	 */
	public String getOutputCustomStyleScriptKey() {
		return outputCustomStyleScriptKey;
	}

	/**
	 * 表示カスタムスタイルキーを設定します。
	 * @param outputCustomStyleScriptKey 表示カスタムスタイルキー
	 */
	public void setOutputCustomStyleScriptKey(String outputCustomStyleScriptKey) {
		this.outputCustomStyleScriptKey = outputCustomStyleScriptKey;
	}

	/**
	 * 入力カスタムスタイルキー
	 * @return 入力カスタムスタイルキー
	 */
	public String getInputCustomStyleScriptKey() {
		return inputCustomStyleScriptKey;
	}

	/**
	 * 入力カスタムスタイルキーを設定します。
	 * @param inputCustomStyleScriptKey 入力カスタムスタイルキー
	 */
	public void setInputCustomStyleScriptKey(String inputCustomStyleScriptKey) {
		this.inputCustomStyleScriptKey = inputCustomStyleScriptKey;
	}

}
