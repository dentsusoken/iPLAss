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

import org.iplass.adminconsole.shared.base.rpc.entity.EntityDataTransferService;
import org.iplass.adminconsole.shared.metadata.dto.MetaTreeNode;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.DeleteResultInfo;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.ImportFileInfo;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.ImportMetaDataStatus;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.MetaDataCheckResultInfo;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.MetaDataImportResultInfo;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.tenant.Tenant;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.client.rpc.XsrfProtectedService;

/**
 * MetaDataExplorer用Service
 */
@RemoteServiceRelativePath("service/metaexplorer")
public interface MetaDataExplorerService extends XsrfProtectedService , EntityDataTransferService {


	public MetaTreeNode getMetaTree(final int tenantId);

	public ImportFileInfo getMetaImportFileTree(final int tenantId, final String tagOid);

	public List<ImportMetaDataStatus> checkImportStatus(final int tenantId, final String tagOid, final List<String> pathList);

	public Tenant getImportTenant(final int tenantId, final String tagOid);

	public MetaDataImportResultInfo importMetaData(final int tenantId, final String tagOid, final List<String> pathList, final Tenant importTenant);

	public void removeImportFile(final int tenantId, final String tagOid);

	public void createTag(final int tenantId, final String name, final String description);

	public List<Entity> getTagList(final int tenantId);

	public void removeTag(final int tenantId, final List<String> tagList);

	public DeleteResultInfo deleteMetaData(final int tenantId, final List<String> pathList);

	public MetaDataCheckResultInfo checkMetaData(final int tenantId, final String tagOid, final List<String> pathList);

}
