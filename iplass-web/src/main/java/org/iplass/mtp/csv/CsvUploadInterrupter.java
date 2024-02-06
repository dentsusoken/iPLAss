/*
 * Copyright (C) 2022 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.csv;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.entity.DeleteOption;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.InsertOption;
import org.iplass.mtp.entity.UpdateOption;
import org.iplass.mtp.entity.ValidateError;
import org.iplass.mtp.entity.definition.EntityDefinition;

/**
 * カスタムでCSVアップロード処理を行わせるインターフェース
 */
public interface CsvUploadInterrupter {

	/**
	 * CSVアップロード登録処理の種類
	 */
	public enum CsvRegistrationType {

		/** 新規追加 */
		INSERT,
		/** 更新 */
		UPDATE,
		/** 削除 */
		DELETE
	}

	/**
	 * プロパティ名に対する出力CSV列名のマッピング定義を返します。
	 * keyをプロパティ名、valueを出力CSV列名として定義します。
	 * nullを返す場合は、Entity定義から決定します。
	 *
	 * @param definition Entity定義
	 * @return プロパティ名に対する出力CSV列名のマッピング定義
	 */
	default public Map<String, String> columnNameMap(EntityDefinition definition) {
		return null;
	}

	/**
	 * CSVデータから登録用のデータをマッピングします。
	 *
	 * @param row 行
	 * @param entity CSVデータ
	 * @param definition Entity定義
	 * @param registrationType 登録処理の種類
	 */
	default public void dataMapping(int row, Entity entity, EntityDefinition definition, CsvRegistrationType registrationType) {
	}

	/**
	 * 登録前処理を行います。
	 *
	 * @param row 行
	 * @param entity 登録用のデータ
	 * @param definition Entity定義
	 * @param registrationType 登録処理の種類
	 * @return 入力エラーリスト
	 */
	default public List<ValidateError> beforeRegist(int row, Entity entity, EntityDefinition definition, CsvRegistrationType registrationType) {
		return Collections.emptyList();
	}

	/**
	 * 登録後処理を行います。
	 *
	 * @param row 行
	 * @param entity 登録用のデータ
	 * @param definition Entity定義
	 * @param registrationType 登録処理の種類
	 * @return 入力エラーリスト
	 */
	default public List<ValidateError> afterRegist(int row, Entity entity, EntityDefinition definition, CsvRegistrationType registrationType) {
		return Collections.emptyList();
	}

	/**
	 * 新規追加時のオプションを返します。
	 *
	 * @param option 標準のオプション
	 * @return 実行オプション
	 */
	default public InsertOption insertOption(InsertOption option) {
		return option;
	}

	/**
	 * 更新時のオプションを返します。
	 *
	 * @param option 標準のオプション
	 * @return 実行オプション
	 */
	default public UpdateOption updateOption(UpdateOption option) {
		return option;
	}

	/**
	 * 削除時のオプションを返します。
	 *
	 * @param option 標準のオプション
	 * @return 実行オプション
	 */
	default public DeleteOption deleteOption(DeleteOption option) {
		return option;
	}

}
