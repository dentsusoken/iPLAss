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
 * <p>各言語毎の値を、別プロパティとして保存するDataLocalizationStrategy。</p>
 * <p>
 * 例えば、itemNameという文字列型プロパティを保持するEntityの場合、
 * itemName_ja、itemName_en、item_zh_CNの命名ルールで別プロパティとして定義することにより、
 * それぞれの言語の際の値とすることが可能。対応する言語のプロパティが存在しない場合は、デフォルト値としてitemNameが利用される。<br>
 * 命名ルールは以下。<br>
 * </p>
 * <p>
 * <b>[プロパティ名]_[言語*1]</b><br>
 * *1 IETF BCP 47(RFC5646,RFC4647)形式の言語表現の"-"を"_"にリプレースしたもの
 * </p>
 * <p>
 * 追加・更新時のバリデーション、EventListener呼び出しの際は、言語サフィックスが付与される前の状態で実行される。
 * 上記の例でいくと、itemNameに定義されるバリデーションによりチェックされ、EventListener呼び出し時にも、itemNameに値が設定された形で呼び出される。
 * </p>
 * 
 * @author K.Higuchi
 *
 */
public class EachPropertyDataLocalizationStrategy extends DataLocalizationStrategy {
	private static final long serialVersionUID = 7797384339194519479L;

}
