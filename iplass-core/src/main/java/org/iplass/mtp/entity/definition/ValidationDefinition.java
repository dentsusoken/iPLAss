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

package org.iplass.mtp.entity.definition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.entity.definition.validations.BinarySizeValidation;
import org.iplass.mtp.entity.definition.validations.BinaryTypeValidation;
import org.iplass.mtp.entity.definition.validations.ExistsValidation;
import org.iplass.mtp.entity.definition.validations.JavaClassValidation;
import org.iplass.mtp.entity.definition.validations.LengthValidation;
import org.iplass.mtp.entity.definition.validations.NotNullValidation;
import org.iplass.mtp.entity.definition.validations.RangeValidation;
import org.iplass.mtp.entity.definition.validations.RegexValidation;
import org.iplass.mtp.entity.definition.validations.ScriptingValidation;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.xml.bind.annotation.XmlSeeAlso;

/**
 * 検証ロジックの定義。
 *
 * @author K.Higuchi
 *
 */
@XmlSeeAlso(
		value = {
				NotNullValidation.class,
				RangeValidation.class,
				RegexValidation.class,
				LengthValidation.class,
				ScriptingValidation.class,
				JavaClassValidation.class,
				BinarySizeValidation.class,
				BinaryTypeValidation.class,
				ExistsValidation.class
		})
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public abstract class ValidationDefinition implements Serializable {
	private static final long serialVersionUID = 2424889426106372753L;

	@MultiLang(
			itemKey = "errorMessage",
			itemGetter = "getErrorMessage",
			itemSetter = "setErrorMessage",
			multiLangGetter = "getLocalizedErrorMessageList",
			multiLangSetter = "setLocalizedErrorMessageList")
	private String errorMessage;
	private String errorCode;

	private String messageCategory;
	private String messageId;

	private List<LocalizedStringDefinition> localizedErrorMessageList;

	private String validationSkipScript;

	public String getMessageCategory() {
		return messageCategory;
	}

	private String description;

	/**
	 * エラーメッセージをメッセージ定義を利用する場合、メッセージ定義のカテゴリ名を指定。
	 *
	 * @param messageCategory メッセージ定義のカテゴリ名
	 */
	public void setMessageCategory(String messageCategory) {
		this.messageCategory = messageCategory;
	}

	public String getMessageId() {
		return messageId;
	}

	/**
	 * エラーメッセージをメッセージ定義を利用する場合、メッセージ定義のメッセージIDを指定。
	 *
	 * @param messageId メッセージ定義のID
	 */
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * エラーにエラーコードを定義する場合、そのエラーコードを指定する。
	 *
	 * @param errorCode
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * エラーメッセージを指定する。
	 * エラーメッセージが定義されている場合は、メッセージ定義（messageCategory,messageId）より優先してこちらが利用される。
	 *
	 * @param errorMessage
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * 多言語設定情報を取得します。
	 * @return リスト
	 */
	public List<LocalizedStringDefinition> getLocalizedErrorMessageList() {
		return localizedErrorMessageList;
	}

	/**
	 * 多言語設定情報を設定します。
	 * @param リスト
	 */
	public void setLocalizedErrorMessageList(List<LocalizedStringDefinition> localizedErrorMessageList) {
		this.localizedErrorMessageList = localizedErrorMessageList;
	}

	/**
	 * 多言語設定情報を追加します。
	 * @param 多言語設定情報
	 */
	public void addLocalizedErrorMessage(LocalizedStringDefinition localizedErrorMessage) {
		if (localizedErrorMessageList == null) {
			localizedErrorMessageList = new ArrayList<>();
		}

		localizedErrorMessageList.add(localizedErrorMessage);
	}

	/**
	 * 説明を取得します。
	 * @return 説明
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 説明を設定します。
	 * @param description 説明
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 検証の実行をスキップするためのスクリプトを取得します。
	 * 
	 * @return スクリプト
	 */
	public String getValidationSkipScript() {
		return validationSkipScript;
	}

	/**
	 * 検証の実行をスキップするスクリプトを設定します。
	 * 
	 * @param validationSkipScript スクリプト
	 */
	public void setValidationSkipScript(String validationSkipScript) {
		this.validationSkipScript = validationSkipScript;
	}
}
