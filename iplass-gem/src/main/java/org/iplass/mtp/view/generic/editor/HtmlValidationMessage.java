/*
 * Copyright (C) 2023 DENTSU SOKEN INC. All Rights Reserved.
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

import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.adminconsole.view.annotation.Refrectable;
import org.iplass.adminconsole.view.annotation.generic.EntityViewField;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.definition.LocalizedStringDefinition;

/**
 * HTMLのinput要素のtype、patternに対するメッセージ定義
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class HtmlValidationMessage implements Refrectable {

	private static final long serialVersionUID = -4821367998351966152L;

	/** タイプ不一致 */
	@MetaFieldInfo(
			displayName="タイプ不一致",
			displayNameKey="generic_editor_HtmlValidationMessage_typeMismatchDisplaNameKey",
			description="タイプ不一致時に表示するメッセージを設定します。",
			descriptionKey="generic_editor_HtmlValidationMessage_typeMismatchDescriptionKey",
			inputType=InputType.MULTI_LANG,
			multiLangField = "localizedTypeMismatchList"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL, FieldReferenceType.BULK}
	)
	@MultiLang()
	private String typeMismatch;

	/** タイプ不一致の多言語設定情報 */
	@MetaFieldInfo(
			displayName="タイプ不一致の多言語設定",
			displayNameKey="generic_editor_HtmlValidationMessage_localizedTypeMismatchListDisplaNameKey",
			inputType=InputType.MULTI_LANG_LIST
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL, FieldReferenceType.BULK}
	)
	private List<LocalizedStringDefinition> localizedTypeMismatchList;

	/** パターン不一致 */
	@MetaFieldInfo(
			displayName="パターン不一致",
			displayNameKey="generic_editor_HtmlValidationMessage_patternMismatchDisplaNameKey",
			description="パターン不一致時に表示するメッセージを設定します。",
			descriptionKey="generic_editor_HtmlValidationMessage_patternMismatchDescriptionKey",
			inputType=InputType.MULTI_LANG,
			multiLangField = "localizedPatternMismatchList"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL, FieldReferenceType.BULK}
	)
	@MultiLang()
	private String patternMismatch;

	/** パターン不一致の多言語設定情報 */
	@MetaFieldInfo(
			displayName="パターン不一致の多言語設定",
			displayNameKey="generic_editor_HtmlValidationMessage_localizedPatternMismatchListDisplaNameKey",
			inputType=InputType.MULTI_LANG_LIST
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL, FieldReferenceType.BULK}
	)
	private List<LocalizedStringDefinition> localizedPatternMismatchList;

	/**
	 * タイプ不一致を取得します。
	 * @return タイプ不一致
	 */
	public String getTypeMismatch() {
		return typeMismatch;
	}

	/**
	 * タイプ不一致を設定します。
	 * @param typeMismatch タイプ不一致
	 */
	public void setTypeMismatch(String typeMismatch) {
		this.typeMismatch = typeMismatch;
	}

	/**
	 * タイプ不一致多言語設定を取得します。
	 * @return タイプ不一致多言語設定
	 */
	public List<LocalizedStringDefinition> getLocalizedTypeMismatchList() {
		return localizedTypeMismatchList;
	}

	/**
	 * タイプ不一致多言語設定を設定します。
	 * @param localizedTypeMismatchList タイプ不一致多言語設定
	 */
	public void setLocalizedTypeMismatchList(List<LocalizedStringDefinition> localizedTypeMismatchList) {
		this.localizedTypeMismatchList = localizedTypeMismatchList;
	}

	/**
	 * パターン不一致を取得します。
	 * @return パターン不一致
	 */
	public String getPatternMismatch() {
		return patternMismatch;
	}

	/**
	 * パターン不一致を設定します。
	 * @param patternMismatch パターン不一致
	 */
	public void setPatternMismatch(String patternMismatch) {
		this.patternMismatch = patternMismatch;
	}

	/**
	 * パターン不一致多言語設定を取得します。
	 * @return パターン不一致多言語設定
	 */
	public List<LocalizedStringDefinition> getLocalizedPatternMismatchList() {
		return localizedPatternMismatchList;
	}

	/**
	 * パターン不一致多言語設定を設定します。
	 * @param localizedPatternMismatchList パターン不一致多言語設定
	 */
	public void setLocalizedPatternMismatchList(List<LocalizedStringDefinition> localizedPatternMismatchList) {
		this.localizedPatternMismatchList = localizedPatternMismatchList;
	}

}
