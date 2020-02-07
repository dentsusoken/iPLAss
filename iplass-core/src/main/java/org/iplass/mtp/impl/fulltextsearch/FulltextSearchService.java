/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.fulltextsearch;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.FulltextSearchOption;
import org.iplass.mtp.entity.SearchResult;
import org.iplass.mtp.spi.Service;

public interface FulltextSearchService extends Service {

	/**
	 * 全文検索機能が利用可能かを返す。
	 *
	 * @return true:利用可能
	 */
	boolean isUseFulltextSearch();

	//TODO gem側でチェックしているのみ
	/**
	 * 検索時の最大検索結果件数を返す。
	 *
	 * @return 最大検索結果件数
	 */
	int getMaxRows();

	//TODO gem側でチェックしているのみ
	/**
	 * 検索結果が最大件数以上の場合、エラーにするかを返す。
	 *
	 *
	 * @return true: エラー
	 */
	boolean isThrowExceptionWhenOverLimit();

	/**
	 * クロール処理を実行する。
	 * Entity定義名が未指定の場合、全Entityを対象にする。
	 *
	 * @param tenantId テナントID
	 * @param defNames Entity定義名
	 */
	void execCrawlEntity(int tenantId, String... defNames);

	/**
	 * クロール処理を実行する。
	 * Entity定義名が未指定の場合、全Entityを対象にする。
	 *
	 * @param defNames Entity定義名
	 */
	void execCrawlEntity(String... defNames);

	/**
	 * リフレッシュ処理を実行する。
	 */
	void execRefresh();

	/**
	 * テナントの全Indexデータを削除する。
	 *
	 * @param tenantId テナントID
	 */
	void deleteAllIndex(int tenantId);

	/**
	 * テナントの全Indexデータを削除する。
	 */
	void deleteAllIndex();

	/**
	 * Entityの最終クロール時刻を返す。
	 *
	 * @param defNames Entity定義名
	 * @return 最終クロール時刻のMap
	 */
	Map<String, Timestamp> getLastCrawlTimestamp(String... defNames);

	/**
	 * 全文検索を実行し、検索結果を返す。
	 *
	 * @param defName Entity定義名
	 * @param keyword キーワード
	 * @return 検索結果のEntityデータリスト
	 */
	<T extends Entity> SearchResult<T> fulltextSearchEntity(String defName, String keyword);

	/**
	 * 全文検索を実行し、検索結果を返す。
	 *
	 * {@literal entityProperties}のkeyで指定したEntity定義名を検索対象とする。
	 * また返すEntityデータに含まれるPropertyは{@literal entityProperties}のvalueで指定したProperty名のリストを対象とする。
	 *
	 * @param entityProperties 対象Entity定義名とプロパティ名のリストをセットにしたMap
	 * @param keyword キーワード
	 * @return 検索結果のEntityデータリスト
	 */
	<T extends Entity> SearchResult<T> fulltextSearchEntity(Map<String, List<String>> entityProperties, String keyword);

	/**
	 * 全文検索を実行し、検索結果を返す。
	 *
	 * {@literal option}のkeyで指定したEntity定義名を検索対象とする。
	 * また返すEntityデータに含まれるPropertyは{@literal conditions}のpropertiesで指定したProperty名のリストを対象とする。
	 *
	 * @param keyword キーワード
	 * @param option 全文検索時のオプション
	 * @return 検索結果のEntityデータリスト
	 */
	<T extends Entity> SearchResult<T> fulltextSearchEntity(String keyword, FulltextSearchOption option);

	/**
	 * 全文検索を実行し、検索結果のOIDリストを返す。
	 *
	 * @param defName Entity定義名
	 * @param keyword キーワード
	 * @return 検索結果のOIDリスト
	 */
	List<String> fulltextSearchOidList(String defName, String keyword);


	//TODO 現状Luceneの場合、FulltextSearchResultはoidのみ
	/**
	 * 全文検索を実行し、検索結果情報を返す。
	 *
	 * @param defName Entity定義名
	 * @param keyword キーワード
	 * @return 検索結果情報
	 */
	List<FulltextSearchResult> execFulltextSearch(String defName, String keyword);

}
