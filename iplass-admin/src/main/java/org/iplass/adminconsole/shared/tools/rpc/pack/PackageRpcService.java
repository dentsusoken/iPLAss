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

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.client.rpc.XsrfProtectedService;

@RemoteServiceRelativePath("service/packageservice")
public interface PackageRpcService extends XsrfProtectedService {

	public List<PackageEntryStatusInfo> getPackageList(final int tenantId);

	public void deletePackage(final int tenantId, final List<String> packOids);

	public String storePackage(final int tenantId, final PackageCreateInfo createInfo);

	public void asyncCreatePackage(final int tenantId, final String packOid);

	public PackageCreateResultInfo syncCreatePackage(final int tenantId, final String packOid);

	public PackageEntryInfo getPackageEntryInfo(final int tenantId, final String packOid);

	public MetaDataImportResultInfo importPackageMetaData(final int tenantId, final String packOid, final Tenant importTenant);

	public EntityDataImportResultInfo importPackageEntityData(final int tenantId, final String packOid, final String path, final PackageImportCondition condition);

}
