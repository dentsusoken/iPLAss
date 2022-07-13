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

import org.iplass.adminconsole.shared.base.rpc.entity.EntityDataTransferService;
import org.iplass.adminconsole.shared.tools.dto.entityexplorer.CrawlEntityInfo;
import org.iplass.adminconsole.shared.tools.dto.entityexplorer.DefragEntityInfo;
import org.iplass.adminconsole.shared.tools.dto.entityexplorer.EntityDataCountResultInfo;
import org.iplass.adminconsole.shared.tools.dto.entityexplorer.EntityDataListResultInfo;
import org.iplass.adminconsole.shared.tools.dto.entityexplorer.RecycleBinEntityInfo;
import org.iplass.adminconsole.shared.tools.dto.entityexplorer.SimpleEntityInfo;
import org.iplass.adminconsole.shared.tools.dto.entityexplorer.SimpleEntityTreeNode;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.impl.tools.entity.EntityDataDeleteResultInfo;
import org.iplass.mtp.impl.tools.entity.EntityUpdateAllCondition;
import org.iplass.mtp.impl.tools.entity.EntityUpdateAllResultInfo;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.client.rpc.XsrfProtectedService;

/**
 * Entity用Service
 */
@RemoteServiceRelativePath("service/entityexplorer")
public interface EntityExplorerService extends XsrfProtectedService , EntityDataTransferService {


	/**
	 * <p>Entityのリストを取得します。</p>
	 *
	 * @param tenantId テナントID
	 * @param isGetDataCount データ件数取得有無
	 * @return {@link SimpleEntityInfo}
	 */
	public List<SimpleEntityInfo> getSimpleEntityList(int tenantId, boolean isGetDataCount);

	/**
	 * <p>EntityのリストをTreeNode形式で取得します。</p>
	 *
	 * @param tenantId テナントID
	 * @param isGetDataCount データ件数取得有無
	 * @return {@link SimpleEntityTreeNode}
	 */
	public SimpleEntityTreeNode getSimpleEntityTree(final int tenantId, final boolean isGetDataCount);

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
	 * @return
	 */
	public EntityDataListResultInfo search(int tenantId, final String defName, final String whereClause, final String orderByClause, final boolean isSearcgAllVersion, final int limit, final int offset);

	/**
	 * <p>Entityデータの件数を取得します。</p>
	 *
	 * @param tenantId テナントID
	 * @param defName 検索対象Entity定義名
	 * @param whereClause Where条件
	 * @param isSearcgAllVersion 全バージョン検索
	 * @return
	 */
	public EntityDataListResultInfo count(int tenantId, final String defName, final String whereClause, final boolean isSearcgAllVersion);

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
	 * @return
	 */
	public EntityDataListResultInfo validateCriteria(int tenantId, final String defName, final String whereClause, final String orderByClause, final boolean isSearcgAllVersion);

	/**
	 * <p>ReferenceEntityを検索します。</p>
	 *
	 * @param tenantId テナントID
	 * @param defName 検索対象Entity定義名
	 * @param refName 検索対象ReferenceProperty名
	 * @param oid 検索対象EntityOID
	 * @param version 検索対象EntityVersion
	 * @return
	 */
	public List<Entity> searchReferenceEntity(int tenantId, final String defName, final String refName, final String oid, final Long version);

	/**
	 * <p>Entityをロードします。</p>
	 *
	 * @param tenantId テナントID
	 * @param defName 検索対象Entity定義名
	 * @param oid OID
	 * @param version Version
	 * @return {@link Entity}
	 */
	public Entity load(final int tenantId, final String defName, final String oid, final Long version);

	/**
	 * <p>Entityに対してupdateAllを実行します。</p>
	 *
	 * @param tenantId テナントID
	 * @param cond UpdateAll条件
	 * @return
	 */
	public EntityUpdateAllResultInfo updateAll(final int tenantId, final EntityUpdateAllCondition cond);

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
	 * @return
	 */
	public EntityDataDeleteResultInfo deleteAll(final int tenantId, final String defName, final String whereClause, final boolean isNotifyListeners, final int commitLimit);

	/**
	 * <p>OIDに該当するデータを一括で削除します。</p>
	 *
	 * @param tenantId テナントID
	 * @param defName 検索対象Entity定義名
	 * @param oids 削除対象OIDのリスト
	 * @param isNotifyListeners Listner通知を行うか
	 * @param commitLimit Commit単位
	 * @return
	 */
	public EntityDataDeleteResultInfo deleteAllByOid(final int tenantId, final String defName, final List<String> oids, final boolean isNotifyListeners, final int commitLimit);

	/**
	 * <p>Where条件に該当するデータ件数を返します。</p>
	 * <p>updateAll、deleteAll実行前の件数確認用処理です</p>
	 *
	 * @param tenantId テナントID
	 * @param defName 検索対象Entity定義名
	 * @param whereClause 検索条件
	 * @return
	 */
	public EntityDataCountResultInfo getConditionDataCount(final int tenantId, final String defName, final String whereClause);

	public List<DefragEntityInfo> getDefragEntityList(int tenantId, boolean isGetDataCount);

	public List<String> defragEntity(int tenantId, String defName);

	public void execCrawlEntity(final int tenantId, String[] defNames);

	public List<CrawlEntityInfo> getCrawlEntityList(int tenantId, boolean isGetDataCount);

	public void execReCrawlEntity(int tenantId);

	public void execRefresh(int tenantId);

	public boolean isUseFulltextSearch(int tenantId);

	public List<RecycleBinEntityInfo> getRecycleBinInfoList(int tenantId, Timestamp ts, boolean isGetCount);

	public List<String> cleanRecycleBin(int tenantId, String defName, Timestamp ts);

}
