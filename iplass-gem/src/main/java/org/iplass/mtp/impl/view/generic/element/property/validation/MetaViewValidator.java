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

package org.iplass.mtp.impl.view.generic.element.property.validation;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.i18n.MetaLocalizedString;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.view.generic.element.property.validation.RequiresAtLeastOneFieldValidator;
import org.iplass.mtp.view.generic.element.property.validation.ViewValidatorBase;

/**
 * 画面入力チェックのメタデータ
 * @author lis3wg
 */
@XmlSeeAlso({ MetaRequiresAtLeastOneFieldValidator.class })
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class MetaViewValidator implements MetaData {

	private static final long serialVersionUID = -3689861235173079692L;

	public static MetaViewValidator createInstance(ViewValidatorBase validator) {
		if (validator instanceof RequiresAtLeastOneFieldValidator) {
			return MetaRequiresAtLeastOneFieldValidator.createInstance(validator);
		}
		return null;
	}

	/** メッセージ */
	private String message;

	/** メッセージの多言語情報 */
	private List<MetaLocalizedString> localizedMessageList = new ArrayList<MetaLocalizedString>();

	/** 通常検索でのチェック有無 */
	private boolean validateNormal;

	/** 詳細検索でのチェック有無 */
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
	 * メッセージの多言語情報を取得します。
	 * @return メッセージの多言語情報
	 */
	public List<MetaLocalizedString> getLocalizedMessageList() {
	    return localizedMessageList;
	}

	/**
	 * メッセージの多言語情報を設定します。
	 * @param localizedMessageList メッセージの多言語情報
	 */
	public void setLocalizedMessageList(List<MetaLocalizedString> localizedMessageList) {
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

	/**
	 * 入力チェックの内容を保持。
	 * @param validator 入力チェック
	 */
	public void applyConfig(ViewValidatorBase validator, String definitionId) {
		this.message = validator.getMessage();
		this.localizedMessageList = I18nUtil.toMeta(validator.getLocalizedMessageList());
		this.validateNormal = validator.isValidateNormal();
		this.validateDetail = validator.isValidateDetail();

		fillFrom(validator, definitionId);
	}

	/**
	 * メタに保持する内容を設定する
	 * @param validator 入力チェック
	 */
	protected abstract void fillFrom(ViewValidatorBase validator, String definitionId);

	/**
	 * 設定内容を入力チェックに反映。
	 * @return 入力チェック
	 */
	public ViewValidatorBase currentConfig(String definitionId) {
		ViewValidatorBase validator = fillTo(definitionId);

		validator.setMessage(message);
		validator.setLocalizedMessageList(I18nUtil.toDef(localizedMessageList));
		validator.setValidateNormal(validateNormal);
		validator.setValidateDetail(validateDetail);

		return validator;
	}

	/**
	 * 入力チェックにメタの内容を反映。
	 * @return 入力チェック
	 */
	protected abstract ViewValidatorBase fillTo(String definitionId);

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

}
