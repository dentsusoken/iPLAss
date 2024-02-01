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

package org.iplass.mtp.impl.view.generic.editor;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.i18n.MetaLocalizedString;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.view.generic.editor.HtmlValidationMessage;

/**
 * HTMLのinput要素のtype、patternに対するメッセージ定義
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class MetaHtmlValidationMessage implements MetaData {

	private static final long serialVersionUID = 1641593208475960896L;

	/** タイプ不一致 */
	private String typeMismatch;

	/** タイプ不一致の多言語設定情報 */
	private List<MetaLocalizedString> localizedTypeMismatchList;

	/** パターン不一致 */
	private String patternMismatch;

	/** パターン不一致の多言語設定情報 */
	private List<MetaLocalizedString> localizedPatternMismatchList;

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
	public List<MetaLocalizedString> getLocalizedTypeMismatchList() {
		return localizedTypeMismatchList;
	}

	/**
	 * タイプ不一致多言語設定を設定します。
	 * @param localizedTypeMismatchList タイプ不一致多言語設定
	 */
	public void setLocalizedTypeMismatchList(List<MetaLocalizedString> localizedTypeMismatchList) {
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
	public List<MetaLocalizedString> getLocalizedPatternMismatchList() {
		return localizedPatternMismatchList;
	}

	/**
	 * パターン不一致多言語設定を設定します。
	 * @param localizedPatternMismatchList パターン不一致多言語設定
	 */
	public void setLocalizedPatternMismatchList(List<MetaLocalizedString> localizedPatternMismatchList) {
		this.localizedPatternMismatchList = localizedPatternMismatchList;
	}

	/**
	 * 定義の内容を自身に設定します。
	 * @param definition HtmlValidationMessage
	 */
	public void applyConfig(HtmlValidationMessage definition) {
		this.typeMismatch = definition.getTypeMismatch();
		this.localizedTypeMismatchList = I18nUtil.toMeta(definition.getLocalizedTypeMismatchList());
		this.patternMismatch = definition.getPatternMismatch();
		this.localizedPatternMismatchList = I18nUtil.toMeta(definition.getLocalizedPatternMismatchList());
	}

	/**
	 * 自身の内容を定義に設定します。
	 * @return HtmlValidationMessage
	 */
	public HtmlValidationMessage currentConfig() {
		HtmlValidationMessage definition = new HtmlValidationMessage();
		definition.setTypeMismatch(typeMismatch);
		definition.setLocalizedTypeMismatchList(I18nUtil.toDef(localizedTypeMismatchList));
		definition.setPatternMismatch(patternMismatch);
		definition.setLocalizedPatternMismatchList(I18nUtil.toDef(localizedPatternMismatchList));
		return definition;
	}

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

}
