/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.tools.tenant.rdb;

import java.util.List;

import org.iplass.mtp.impl.tools.tenant.PartitionCreateParameter;
import org.iplass.mtp.impl.tools.tenant.PartitionInfo;
import org.iplass.mtp.impl.tools.tenant.TenantDeleteParameter;
import org.iplass.mtp.impl.tools.tenant.TenantInfo;
import org.iplass.mtp.impl.tools.tenant.log.LogHandler;

public interface TenantRdbManager {

	/**
	 * <p>テナントが存在するかを返します。</p>
	 *
	 * @param url テナントURL
	 * @return true : 存在
	 */
	boolean existsURL(String url);

	/**
	 * <p>テナント情報を返します。</p>
	 *
	 * @param url テナントURL
	 * @return テナント情報
	 */
	TenantInfo getTenantInfo(String url);

	/**
	 * <p>有効なテナント情報を返します。</p>
	 *
	 * @return 有効なテナント情報
	 */
	List<TenantInfo> getValidTenantInfoList();

	/**
	 * <p>全てのテナント情報を返します。無効になったテナントも含まれます。</p>
	 *
	 * @return 有効なテナント情報
	 */
	List<TenantInfo> getAllTenantInfoList();

	/**
	 * <p>テナントデータを削除します。</p>
	 *
	 * @param param 削除情報
	 * @param deleteAccount アカウント情報を削除するか
	 * @param logHandler ログハンドラー
	 * @return 実行結果。true : 成功
	 */
	boolean deleteTenant(TenantDeleteParameter param, boolean deleteAccount, LogHandler logHandler);

	/**
	 * <p>パーティション対応をしているかを返します。</p>
	 * @return true : パーティション対応
	 */
	boolean isSupportPartition();

	/**
	 * <p>パーティション情報を返します。</p>
	 *
	 * @return パーティション情報
	 */
	List<PartitionInfo> getPartitionInfo();

	/**
	 * <p>パーティションを作成します。</p>
	 *
	 * @param param 作成情報
	 * @param logHandler ログハンドラー
	 * @return 実行結果。true : 成功
	 */
	boolean createPartition(PartitionCreateParameter param, LogHandler logHandler);

	/**
	 * <p>パーティションを削除します。</p>
	 *
	 * @param param 削除情報
	 * @param logHandler ログハンドラー
	 * @return 実行結果。true : 成功
	 */
	boolean dropPartition(PartitionDeleteParameter param, LogHandler logHandler);

}
