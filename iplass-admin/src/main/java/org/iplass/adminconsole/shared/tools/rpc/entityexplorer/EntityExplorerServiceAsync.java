/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.shared.tools.rpc.entityexplorer;

import java.sql.Timestamp;
import java.util.List;

import org.iplass.adminconsole.shared.base.dto.entity.EntityDataTransferTypeList;
import org.iplass.adminconsole.shared.tools.dto.entityexplorer.CrawlEntityInfo;
import org.iplass.adminconsole.shared.tools.dto.entityexplorer.DefragEntityInfo;
import org.iplass.adminconsole.shared.tools.dto.entityexplorer.EntityDataCountResultInfo;
import org.iplass.adminconsole.shared.tools.dto.entityexplorer.EntityDataListResultInfo;
import org.iplass.adminconsole.shared.tools.dto.entityexplorer.EntityViewInfo;
import org.iplass.adminconsole.shared.tools.dto.entityexplorer.RecycleBinEntityInfo;
import org.iplass.adminconsole.shared.tools.dto.entityexplorer.SimpleEntityInfo;
import org.iplass.adminconsole.shared.tools.dto.entityexplorer.SimpleEntityTreeNode;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.impl.tools.entity.EntityDataDeleteResultInfo;
import org.iplass.mtp.impl.tools.entity.EntityUpdateAllCondition;
import org.iplass.mtp.impl.tools.entity.EntityUpdateAllResultInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Entity用AsyncService
 */
public interface EntityExplorerServiceAsync {

	/**
	 * Entityデータを送受信する場合に、値の型をGWTのホワイトリストに追加するためのメソッドです。
	 */
	void entityDataTypeWhiteList(EntityDataTransferTypeList param, AsyncCallback<EntityDataTransferTypeList> callback);

	/**
	 * <p>Entityのリストを取得します。</p>
	 *
	 * @param tenantId テナントID
	 * @param isGetDataCount データ件数取得有無
	 * @param callback  Callbackクラス
	 */
	void getSimpleEntityList(int tenantId, boolean isGetDataCount, AsyncCallback<List<SimpleEntityInfo>> callback);

	/**
	 * <p>EntityのリストをTreeNode形式で取得します。</p>
	 *
	 * @param tenantId テナントID
	 * @param isGetDataCount データ件数取得有無
	 * @param callback  Callbackクラス
	 */
	void getSimpleEntityTree(int tenantId, boolean isGetDataCount, AsyncCallback<SimpleEntityTreeNode> callback);

	/**
	 * <p>Entityの画面定義のリストを取得します。</p>
	 *
	 * @param tenantId テナントID
	 * @param isGetDataCount データ件数取得有無
	 * @param callback  Callbackクラス
	 */
	void getEntityViewList(int tenantId, boolean isGetDataCount, AsyncCallback<List<EntityViewInfo>> callback);

	/**
	 * <p>Entityデータを検索します。</p>
	 *
	 * @param tenantId テナントID
	 * @param defName 検索対象Entity定義名
	 * @param whereClause Where条件
	 * @param orderByClause Sort条件
	 * @param isSearcgAllVersion 全バージョン検索
	 * @param limit Limit
	 * @param offset Offset
	 * @param callback  Callbackクラス
	 */
	void search(int tenantId, String defName, String whereClause, String orderByClause, boolean isSearcgAllVersion, int limit, int offset, AsyncCallback<EntityDataListResultInfo> callback);

	/**
	 * <p>Entityデータの件数を取得します。</p>
	 *
	 * @param tenantId テナントID
	 * @param defName 検索対象Entity定義名
	 * @param whereClause Where条件
	 * @param isSearcgAllVersion 全バージョン検索
	 * @param callback  Callbackクラス
	 */
	void count(int tenantId, String defName, String whereClause, boolean isSearcgAllVersion, AsyncCallback<EntityDataListResultInfo> callback);

	/**
	 * <p>EQLを検証します。</p>
	 * <p>直接EQL条件指定時に、入力されている条件が正しいかを検証します。
	 * CSVダウンロードなどStreamを返すなどでエラーを通知しにくい場合に、実際のダウンロード処理の直前で実行するための機能です。</p>
	 *
	 * @param tenantId テナントID
	 * @param defName 検索対象Entity定義名
	 * @param whereClause Where条件
	 * @param orderByClause Sort条件
	 * @param isSearcgAllVersion 全バージョン検索
	 * @param callback  Callbackクラス
	 */
	void validateCriteria(int tenantId, String defName, String whereClause, String orderByClause, boolean isSearcgAllVersion, AsyncCallback<EntityDataListResultInfo> callback);

	/**
	 * <p>ReferenceEntityを検索します。</p>
	 *
	 * @param tenantId テナントID
	 * @param defName 検索対象Entity定義名
	 * @param refName 検索対象ReferenceProperty名
	 * @param oid 検索対象EntityOID
	 * @param version 検索対象EntityVersion
	 * @param callback  Callbackクラス
	 */
	void searchReferenceEntity(int tenantId, String defName, String refName, String oid, Long version, AsyncCallback<List<Entity>> callback);

	/**
	 * <p>Entityをロードします。</p>
	 *
	 * @param tenantId テナントID
	 * @param defName 検索対象Entity定義名
	 * @param oid OID
	 * @param version Version
	 * @param callback  Callbackクラス
	 */
	void load(int tenantId, String defName, String oid, Long version, AsyncCallback<Entity> callback);

	/**
	 * <p>Entityに対してupdateAllを実行します。</p>
	 *
	 * @param tenantId テナントID
	 * @param cond UpdateAll条件
	 * @param callback Callbackクラス
	 */
	void updateAll(int tenantId, EntityUpdateAllCondition cond, AsyncCallback<EntityUpdateAllResultInfo> callback);

	/**
	 * <p>Entityに対してdeleteAllを実行します。</p>
	 * <p>Where条件に該当するデータを一括で削除します。
	 * isNotifyListenersがtrueの場合は、1件ずつ削除されます。</p>
	 *
	 * @param tenantId テナントID
	 * @param defName 検索対象Entity定義名
	 * @param whereClause 検索条件
	 * @param isNotifyListeners Listner通知を行うか
	 * @param commitLimit Commit単位
	 * @param callback  Callbackクラス
	 */
	void deleteAll(int tenantId, String defName, String whereClause, boolean isNotifyListeners, int commitLimit, AsyncCallback<EntityDataDeleteResultInfo> callback);

	/**
	 * <p>OIDに該当するデータを一括で削除します。</p>
	 *
	 * @param tenantId テナントID
	 * @param defName 検索対象Entity定義名
	 * @param oids 削除対象OIDのリスト
	 * @param isNotifyListeners Listner通知を行うか
	 * @param commitLimit Commit単位
	 * @param callback  Callbackクラス
	 */
	void deleteAllByOid(int tenantId, String defName, List<String> oids, boolean isNotifyListeners, int commitLimit, AsyncCallback<EntityDataDeleteResultInfo> callback);

	/**
	 * <p>Where条件に該当するデータ件数を返します。</p>
	 * <p>updateAll、deleteAll実行前の件数確認用処理です</p>
	 *
	 * @param tenantId テナントID
	 * @param defName 検索対象Entity定義名
	 * @param whereClause 検索条件
	 * @param callback  Callbackクラス
	 */
	void getConditionDataCount(int tenantId, String defName, String whereClause, AsyncCallback<EntityDataCountResultInfo> callback);

	void getDefragEntityList(int tenantId, boolean isGetDataCount, AsyncCallback<List<DefragEntityInfo>> callback);

	void defragEntity(int tenantId, String defName, AsyncCallback<List<String>> callback);

	void execCrawlEntity(final int tenantId, String[] defNames, AsyncCallback<Void> callback);

	void getCrawlEntityList(int tenantId, boolean isGetDataCount, AsyncCallback<List<CrawlEntityInfo>> callback);

	void execReCrawlEntity(int tenantId, AsyncCallback<Void> callback);

	void execRefresh(int tenantId, AsyncCallback<Void> callback);

	void isUseFulltextSearch(int tenantId, AsyncCallback<Boolean> callback);

	void getRecycleBinInfoList(int tenantId, Timestamp ts, boolean isGetCount, AsyncCallback<List<RecycleBinEntityInfo>> callback);

	void cleanRecycleBin(int tenantId, String defName, Timestamp ts, AsyncCallback<List<String>> callback);

}
