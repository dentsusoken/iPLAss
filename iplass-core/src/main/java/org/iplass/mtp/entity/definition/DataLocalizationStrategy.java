/*
 * Copyright (C) 2015 DENTSU SOKEN INC. All Rights Reserved.
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
import java.util.List;

import jakarta.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.entity.definition.l10n.EachInstanceDataLocalizationStrategy;
import org.iplass.mtp.entity.definition.l10n.EachPropertyDataLocalizationStrategy;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * <p>Entityのデータを多言語対応する戦略の定義。</p>
 * <p>
 * 本設定を有効化することにより、
 * EQLでの検索時、登録、更新時に
 * 実行ユーザーに紐づく形で、自動的に適切な言語のデータを取得、更新することが可能となる（それぞれの処理実行時にlocalizedオプションをtrueに指定した場合）。
 * </p>
 * 
 * @author K.Higuchi
 *
 */
@XmlSeeAlso(value = {EachInstanceDataLocalizationStrategy.class,
		EachPropertyDataLocalizationStrategy.class})
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS)
public class DataLocalizationStrategy implements Serializable {
	private static final long serialVersionUID = -7453476741431507201L;

	private List<String> languages;

	/**
	 * 対応する言語を取得。
	 * 
	 * @return
	 */
	public List<String> getLanguages() {
		return languages;
	}

	/**
	 * 対応する言語をセット。
	 * セット可能な言語は、I18nServiceに定義されるenableLanguagesのlanguageKey。
	 * 
	 * @param languages
	 */
	public void setLanguages(List<String> languages) {
		this.languages = languages;
	}

}
