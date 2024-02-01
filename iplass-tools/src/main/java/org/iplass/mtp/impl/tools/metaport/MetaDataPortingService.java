/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.tools.metaport;

import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;

import org.iplass.mtp.spi.Service;
import org.iplass.mtp.tenant.Tenant;

/**
 * MetaDataのExport/Import用Service
 */
public interface MetaDataPortingService extends Service {

	void write(PrintWriter writer, List<String> paths);

	void write(PrintWriter writer, List<String> paths, MetaDataWriteCallback callback);

	/**
	 * InputStreamに定義されたMetaDataのMapを返します。
	 *
	 * @param is 定義XMLファイル(Stream)
	 * @return XMLに含まれるMetaDataEntry情報
	 */
	XMLEntryInfo getXMLMetaDataEntryInfo(InputStream is);

	void writeHistory(PrintWriter writer, String definitionId, String[] versions);

	void writeHistory(PrintWriter writer, String definitionId, String[] versions, MetaDataWriteCallback callback);

	/**
	 * <p>MetaDataEntryを取り込みます(全件)。</p>
	 * <p>
	 * Packageで利用しています。
	 * </p>
	 *
	 * @param targetName インポート対象の名前(Package名、XMLファイル名、Tag名)
	 * @param entryInfo 取り込み対象が定義されたMetaData情報
	 * @param importTenant ワーニング時のインポート対象Tenant
	 */
	MetaDataImportResult importMetaData(String targetName, XMLEntryInfo entryInfo, final Tenant importTenant);

	/**
	 * <p>MetaDataEntryを取り込みます(選択Pathのみ)。</p>
	 * <p>
	 * MetaDataExplorerで利用しています。
	 * </p>
	 *
	 * @param targetName インポート対象の名前(Package名、XMLファイル名、Tag名)
	 * @param entryInfo 取り込み対象が含まれたMetaData情報
	 * @param selectedPaths 取り込み対象のList
	 * @param importTenant ワーニング時のインポート対象Tenant
	 */
	MetaDataImportResult importMetaData(String targetName, XMLEntryInfo entryInfo, final List<String> selectedPaths, final Tenant importTenant);

	/**
	 * 取り込むMetaDataの整合性をチェックします。
	 *
	 * @param xmlInfo 取り込み対象が定義されたMetaData情報
	 * @param importPath ImportするMetaDataのPath
	 * @return 整合性チェック結果
	 */
	MetaDataImportStatus checkStatus(XMLEntryInfo xmlInfo, String importPath);

	/**
	 * 取り込むMetaDataがテナントメタデータかを判定します。
	 *
	 * @param importPath ImportするMetaDataのPath
	 * @return true : テナント
	 */
	boolean isTenantMeta(String importPath);

	/**
	 * Entityデータのパッチ処理を行います。
	 * 
	 * @param param MetaEntityDataパッチパラメータ
	 */
	void patchEntityData(PatchEntityDataParameter param);

	/**
	 * Entityデータのパッチ処理を特権モードで行います。
	 * 
	 * @param param EntityDataパッチパラメータ
	 */
	void patchEntityDataWithPrivilegedAuth(PatchEntityDataParameter param);

	/**
	 * Entityデータのパッチ処理をユーザー認証モードで行います。
	 * 
	 * @param param EntityDataパッチパラメータ
	 */
	void patchEntityDataWithUserAuth(PatchEntityDataParameter param, String userId, String password);
}
