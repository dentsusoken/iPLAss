/*
 * Copyright (C) 2016 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.view.generic.element.property.validation;

import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.adminconsole.view.annotation.Refrectable;
import org.iplass.adminconsole.view.annotation.generic.EntityViewField;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.definition.LocalizedStringDefinition;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * 画面側入力チェック
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({RequiresAtLeastOneFieldValidator.class})
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS)
public abstract class ViewValidatorBase implements Refrectable {

	/** serialVersionUID */
	private static final long serialVersionUID = -9110629170547741670L;

	/** メッセージ */
	@MetaFieldInfo(
			displayName="メッセージ",
			displayNameKey="generic_element_property_validation_ValidatorBase_messageDisplaNameKey",
			description="入力チェックエラー時に表示するメッセージを設定します。",
			descriptionKey="generic_element_property_validation_ValidatorBase_messageDescriptionKey",
			required=true,
			inputType=InputType.MULTI_LANG,
			multiLangField = "localizedMessageList"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION}
	)
	@MultiLang()
	private String message;

	/** メッセージの多言語設定情報 */
	@MetaFieldInfo(
			displayName="メッセージの多言語設定",
			displayNameKey="generic_element_property_validation_ValidatorBase_localizedMessageListDisplaNameKey",
			inputType=InputType.MULTI_LANG_LIST
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION}
	)
	private List<LocalizedStringDefinition> localizedMessageList;

	/** 通常検索でのチェック有無 */
	@MetaFieldInfo(
			displayName="通常検索で入力チェックを行う",
			displayNameKey="generic_element_property_validation_ValidatorBase_validateNormalDisplaNameKey",
			inputType=InputType.CHECKBOX,
			description="通常検索で入力チェックを行うかを設定します。",
			descriptionKey="generic_element_property_validation_ValidatorBase_validateNormalDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION}
	)
	private boolean validateNormal;

	/** 詳細検索でのチェック有無 */
	@MetaFieldInfo(
			displayName="詳細検索で入力チェックを行う",
			displayNameKey="generic_element_property_validation_ValidatorBase_validateDetailDisplaNameKey",
			inputType=InputType.CHECKBOX,
			description="詳細検索で入力チェックを行うかを設定します。",
			descriptionKey="generic_element_property_validation_ValidatorBase_validateDetailDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION}
	)
	private boolean validateDetail;

	/**
	 * メッセージを取得します。
	 * @return メッセージ
	 */
	public String getMessage() {
	    return message;
	}

	/**
	 * メッセージを設定します。
	 * @param message メッセージ
	 */
	public void setMessage(String message) {
	    this.message = message;
	}

	/**
	 * メッセージの多言語設定情報を取得します。
	 * @return メッセージの多言語設定情報
	 */
	public List<LocalizedStringDefinition> getLocalizedMessageList() {
	    return localizedMessageList;
	}

	/**
	 * メッセージの多言語設定情報を設定します。
	 * @param localizedMessageList メッセージの多言語設定情報
	 */
	public void setLocalizedMessageList(List<LocalizedStringDefinition> localizedMessageList) {
	    this.localizedMessageList = localizedMessageList;
	}

	/**
	 * 通常検索でのチェック有無を取得します。
	 * @return 通常検索でのチェック有無
	 */
	public boolean isValidateNormal() {
	    return validateNormal;
	}

	/**
	 * 通常検索でのチェック有無を設定します。
	 * @param validateNormal 通常検索でのチェック有無
	 */
	public void setValidateNormal(boolean validateNormal) {
	    this.validateNormal = validateNormal;
	}

	/**
	 * 詳細検索でのチェック有無を取得します。
	 * @return 詳細検索でのチェック有無
	 */
	public boolean isValidateDetail() {
	    return validateDetail;
	}

	/**
	 * 詳細検索でのチェック有無を設定します。
	 * @param validateDetail 詳細検索でのチェック有無
	 */
	public void setValidateDetail(boolean validateDetail) {
	    this.validateDetail = validateDetail;
	}
}
