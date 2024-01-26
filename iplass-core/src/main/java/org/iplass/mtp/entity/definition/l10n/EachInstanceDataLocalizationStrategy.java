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
package org.iplass.mtp.entity.definition.l10n;

import org.iplass.mtp.entity.definition.DataLocalizationStrategy;

/**
 * <p>各言語毎の値を、別インスタンス（レコード）として保存するDataLocalizationStrategy。</p>
 * <p>
 * この戦略を指定されたEntityは、言語判別用の文字列型プロパティが定義されている必要がある。
 * 各言語毎の値は、その"言語判別用プロパティ"にそれぞれの言語名が設定された形で別インスタンス（レコード）として作成、検索される。
 * EQL実行時には、自動的にその"言語判別用プロパティ"が条件に設定される。
 * "言語判別用プロパティ"の値には、IETF BCP 47(RFC5646,RFC4647)形式での言語表現が格納される。
 * DataLocalizationStrategyのlanguagesに設定されてない言語の場合は、"言語判別用プロパティ" is nullが条件として設定される。<br>
 * 言語単位で、データの件数が異なる場合などに利用することを想定。<br>
 * この戦略を利用する場合の注意点として、UniqueIndexが利用しづらくなる点。言語単位に複数のレコードが追加されるため。
 * また、AutoNumber型プロパティには言語単位で別のIDが付与される。
 * </p>
 * 
 * @author K.Higuchi
 *
 */
public class EachInstanceDataLocalizationStrategy extends DataLocalizationStrategy {
	private static final long serialVersionUID = -3033750393974140063L;

	private String languagePropertyName;

	public String getLanguagePropertyName() {
		return languagePropertyName;
	}

	/**
	 * Entityに定義される"言語判別用プロパティ"（文字列型）のプロパティ名を設定。　
	 * @param languagePropertyName
	 */
	public void setLanguagePropertyName(String languagePropertyName) {
		this.languagePropertyName = languagePropertyName;
	}

}
