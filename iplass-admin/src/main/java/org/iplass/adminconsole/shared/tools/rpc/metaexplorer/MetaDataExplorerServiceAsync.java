/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.shared.tools.rpc.metaexplorer;

import java.util.List;

import org.iplass.adminconsole.shared.base.dto.entity.EntityDataTransferTypeList;
import org.iplass.adminconsole.shared.metadata.dto.MetaTreeNode;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.DeleteResultInfo;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.ImportFileInfo;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.ImportMetaDataStatus;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.MetaDataCheckResultInfo;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.MetaDataImportResultInfo;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.tenant.Tenant;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * MetaDataExplorer用AsyncService
 */
public interface MetaDataExplorerServiceAsync {

	/**
	 * Entityデータを送受信する場合に、値の型をGWTのホワイトリストに追加するためのメソッドです。
	 */
	void entityDataTypeWhiteList(EntityDataTransferTypeList param, AsyncCallback<EntityDataTransferTypeList> callback);

	void getMetaTree(final int tenantId, AsyncCallback<MetaTreeNode> callback);

	void getMetaImportFileTree(final int tenantId, final String tagOid, AsyncCallback<ImportFileInfo> callback);

	void checkImportStatus(final int tenantId, final String tagOid, final List<String> pathList, AsyncCallback<List<ImportMetaDataStatus>> callback);

	void getImportTenant(final int tenantId, final String tagOid, AsyncCallback<Tenant> callback);

	void importMetaData(final int tenantId, final String tagOid, final List<String> pathList, final Tenant importTenant, AsyncCallback<MetaDataImportResultInfo> callback);

	void removeImportFile(final int tenantId, final String tagOid, AsyncCallback<Void> callback);

	void createTag(final int tenantId, final String name, final String description, AsyncCallback<Void> callback);

	void getTagList(final int tenantId, AsyncCallback<List<Entity>> callback);

	void removeTag(final int tenantId, final List<String> tagList, AsyncCallback<Void> callback);

	void deleteMetaData(final int tenantId, final List<String> pathList, AsyncCallback<DeleteResultInfo> callback);

	void checkMetaData(final int tenantId, final String tagOid, final List<String> pathList, AsyncCallback<MetaDataCheckResultInfo> callback);
}
