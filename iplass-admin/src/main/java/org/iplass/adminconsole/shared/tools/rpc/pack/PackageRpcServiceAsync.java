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

package org.iplass.adminconsole.shared.tools.rpc.pack;

import java.util.List;

import org.iplass.adminconsole.shared.tools.dto.entityexplorer.EntityDataImportResultInfo;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.MetaDataImportResultInfo;
import org.iplass.adminconsole.shared.tools.dto.pack.PackageCreateInfo;
import org.iplass.adminconsole.shared.tools.dto.pack.PackageCreateResultInfo;
import org.iplass.adminconsole.shared.tools.dto.pack.PackageEntryInfo;
import org.iplass.adminconsole.shared.tools.dto.pack.PackageEntryStatusInfo;
import org.iplass.adminconsole.shared.tools.dto.pack.PackageImportCondition;
import org.iplass.mtp.tenant.Tenant;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PackageRpcServiceAsync {

	/**
	 * Packageの一覧を取得します。
	 *
	 * @param tenantId テナントID
	 * @param callback List<PackageEntryInfo>
	 */
	void getPackageList(final int tenantId, AsyncCallback<List<PackageEntryStatusInfo>> callback);


	/**
	 * Packageを削除します。
	 *
	 * @param tenantId テナントID
	 * @param packOids 削除対象OIDのリスト
	 * @param callback Void
	 */
	void deletePackage(final int tenantId, final List<String> packOids, AsyncCallback<Void> callback);


	/**
	 * Packageを登録します。
	 *
	 * @param tenantId テナントID
	 * @param createInfo 作成情報
	 * @param callback String（登録PackageエンティティのOID）
	 */
	void storePackage(final int tenantId, final PackageCreateInfo createInfo, AsyncCallback<String> callback);

	/**
	 * Package作成を実行します。(非同期)
	 *
	 * @param tenantId テナントID
	 * @param oid 対象PackageのOID
	 * @param callback Void
	 */
	void asyncCreatePackage(final int tenantId, final String packOid, AsyncCallback<Void> callback);

	/**
	 * Package作成を実行します。(同期)
	 *
	 * @param tenantId テナントID
	 * @param oid 対象PackageのOID
	 * @param callback Void
	 */
	void syncCreatePackage(final int tenantId, final String packOid, AsyncCallback<PackageCreateResultInfo> callback);


	void getPackageEntryInfo(final int tenantId, final String packOid, AsyncCallback<PackageEntryInfo> callback);


	void importPackageMetaData(final int tenantId, final String packOid, final Tenant importTenant, AsyncCallback<MetaDataImportResultInfo> callback);

	void importPackageEntityData(final int tenantId, final String packOid, final String path, final PackageImportCondition condition, AsyncCallback<EntityDataImportResultInfo> callback);

}
