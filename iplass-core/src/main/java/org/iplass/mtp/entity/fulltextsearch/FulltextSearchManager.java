/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.entity.fulltextsearch;

import java.sql.Timestamp;
import java.util.Map;

import org.iplass.mtp.Manager;

/**
 * <p>Entityの全文検索を管理するクラスのインタフェース。</p>
 *
 * <p>
 * Entityに対する全文検索処理は、{@link org.iplass.mtp.entity.EntityManager EntityManager} を経由する。
 * ここでは、全文検索に関する設定情報やクロール操作について定義する。
 * </p>
 */
public interface FulltextSearchManager extends Manager {

	/**
	 * 全文検索機能が利用可能かを返す。
	 *
	 * @return true:利用可能
	 */
	boolean isUseFulltextSearch();

	/**
	 * 検索時の最大検索結果件数を返す。
	 *
	 * @return 最大検索結果件数
	 */
	int getMaxRows();

	/**
	 * 検索結果が最大件数以上の場合、エラーにするかを返す。
	 *
	 *
	 * @return true: エラー
	 */
	boolean isThrowExceptionWhenOverLimit();

	/**
	 * クロール処理を実行する。
	 *
	 * @param defName Entity定義名
	 */
	void crawlEntity(String defName);

	/**
	 * 全Entityのクロール処理を実行する。
	 */
	void crawlAllEntity();

	/**
	 * 全Entityのクロール処理を実行する。
	 * クロール処理を開始する際に、作成済みのIndexを全て削除する。
	 */
	void recrawlAllEntity();

	/**
	 * リフレッシュ処理を実行する。
	 */
	void refresh();

	/**
	 * Entityの最終クロール時刻を返す。
	 *
	 * @param defNames Entity定義名
	 * @return 最終クロール時刻のMap
	 */
	Map<String, Timestamp> getLastCrawlTimestamp(String... defNames);

}
