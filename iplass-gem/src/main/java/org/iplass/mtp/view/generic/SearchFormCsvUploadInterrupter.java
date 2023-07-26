/*
 * Copyright (C) 2022 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.view.generic;

import java.util.List;

import org.iplass.mtp.csv.CsvUploadInterrupter;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinition;

/**
 *  汎用検索画面にカスタムでCSVアップロード処理を行わせるインターフェース
 *
 */
public interface SearchFormCsvUploadInterrupter extends CsvUploadInterrupter {

	/**
	 * 出力するサンプルCSVデータを返します。
	 * nullを返す場合、プロパティ型に応じてランダムな値を出力します。
	 *
	 * @param definition Entity定義
	 * @param properties 出力対象プロパティ定義
	 * @return サンプルCSVデータ
	 */
	default public List<Entity> sampleCsvData(EntityDefinition definition, List<PropertyDefinition> properties) {
		return null;
	}

}
