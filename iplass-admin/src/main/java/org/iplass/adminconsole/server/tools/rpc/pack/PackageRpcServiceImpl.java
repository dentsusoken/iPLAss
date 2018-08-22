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

package org.iplass.adminconsole.server.tools.rpc.pack;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.server.base.rpc.util.AuthUtil;
import org.iplass.adminconsole.shared.tools.dto.entityexplorer.EntityDataImportResultInfo;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.MetaDataImportResultInfo;
import org.iplass.adminconsole.shared.tools.dto.pack.PackageCreateInfo;
import org.iplass.adminconsole.shared.tools.dto.pack.PackageCreateResultInfo;
import org.iplass.adminconsole.shared.tools.dto.pack.PackageEntryInfo;
import org.iplass.adminconsole.shared.tools.dto.pack.PackageEntryStatusInfo;
import org.iplass.adminconsole.shared.tools.dto.pack.PackageImportCondition;
import org.iplass.adminconsole.shared.tools.rpc.pack.PackageRpcService;
import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.SearchResult;
import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.impl.entity.EntityService;
import org.iplass.mtp.impl.tools.entityport.EntityDataImportCondition;
import org.iplass.mtp.impl.tools.entityport.EntityDataImportResult;
import org.iplass.mtp.impl.tools.metaport.MetaDataImportResult;
import org.iplass.mtp.impl.tools.pack.PackageCreateCondition;
import org.iplass.mtp.impl.tools.pack.PackageCreateResult;
import org.iplass.mtp.impl.tools.pack.PackageEntity;
import org.iplass.mtp.impl.tools.pack.PackageInfo;
import org.iplass.mtp.impl.tools.pack.PackageService;
import org.iplass.mtp.impl.web.WebFrontendService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.util.StringUtil;

import com.google.gwt.user.server.rpc.XsrfProtectedServiceServlet;

public class PackageRpcServiceImpl extends XsrfProtectedServiceServlet implements PackageRpcService {

	private static final long serialVersionUID = 8928880432819501403L;

	private PackageService service = ServiceRegistry.getRegistry().getService(PackageService.class);

	@Override
	public List<PackageEntryStatusInfo> getPackageList(final int tenantId) {

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<List<PackageEntryStatusInfo>>() {

			@Override
			public List<PackageEntryStatusInfo> call() {

				List<PackageEntryStatusInfo> result = new ArrayList<PackageEntryStatusInfo>();
				SearchResult<Entity> list = service.getPackageList();
				if (list.getList() != null) {
					for (Entity entity : list.getList()) {
						PackageEntryStatusInfo info = new PackageEntryStatusInfo();
						info.setOid(entity.getOid());
						info.setName(entity.getName());
						info.setDescription(entity.getDescription());
						info.setType(entity.getValueAs(SelectValue.class, PackageEntity.TYPE));
						info.setStatus(entity.getValueAs(SelectValue.class, PackageEntity.STATUS));
						info.setProgress(entity.getValueAs(Integer.class, PackageEntity.COMPLETE_TASK_COUNT)
								+ " / " + entity.getValueAs(Integer.class, PackageEntity.TASK_COUNT));
						info.setExecStartDate(entity.getValueAs(Timestamp.class, PackageEntity.EXEC_START_DATE));
						info.setExecEndDate(entity.getValueAs(Timestamp.class, PackageEntity.EXEC_END_DATE));
						info.setCreateSetting(entity.getValueAs(BinaryReference.class, PackageEntity.CREATE_SETTING));
//						info.setDetail(entity.getValueAs(BinaryReference.class, PackageEntry.DETAIL));
						info.setArchive(entity.getValueAs(BinaryReference.class, PackageEntity.ARCHIVE));
						result.add(info);
					}
				}
				return result;
			}

		});
	}

	@Override
	public void deletePackage(final int tenantId, final List<String> packOids) {

		AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<Void>() {

			@Override
			public Void call() {
				service.deletePackage(packOids);
				return null;
			}

		});
	}

	@Override
	public String storePackage(final int tenantId, final PackageCreateInfo createInfo) {

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<String>() {

			@Override
			public String call() {

				//PackageCreateInfo -> PackageCreateCondition
				PackageCreateCondition condition = new PackageCreateCondition();
				condition.setName(createInfo.getName());
				condition.setDescription(createInfo.getDescription());
				condition.setMetaDataPaths(createInfo.getMetaDataPaths());
				condition.setEntityPaths(createInfo.getEntityPaths());

				return service.storePackage(condition);
			}

		});
	}

	@Override
	public void asyncCreatePackage(final int tenantId, final String packOid) {
		AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<Void>() {

			@Override
			public Void call() {
				File tempDir = getTempDir(getContextTempDir());
				service.archivePackageAsync(packOid, tempDir);
				return null;
			}

		});
	}

	@Override
	public PackageCreateResultInfo syncCreatePackage(final int tenantId, final String packOid) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<PackageCreateResultInfo>() {

			@Override
			public PackageCreateResultInfo call() {
				File tempDir = getTempDir(getContextTempDir());
				PackageCreateResult result = service.archivePackage(packOid, tempDir);

				PackageCreateResultInfo info = new PackageCreateResultInfo();
				info.setError(result.isError());
				info.setMessages(result.getMessages());

				return info;
			}

		});
	}

	@Override
	public PackageEntryInfo getPackageEntryInfo(final int tenantId, final String packOid) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<PackageEntryInfo>() {

			@Override
			public PackageEntryInfo call() {
				File tempDir = getTempDir(getContextTempDir());
				PackageInfo info = service.getPackageInfo(packOid, tempDir);

				//PackageInfo -> PackageEntryInfo(DTO)
				PackageEntryInfo entry = new PackageEntryInfo();
				entry.setMetaDataPaths(info.getMetaDataPaths());
				entry.setTenant(info.getTenant());
				entry.setWarningTenant(info.isWarningTenant());
				entry.setEntityPaths(info.getEntityPaths());
				entry.setHasLobData(info.isHasLobData());
				return entry;
			}

		});
	}

	@Override
	public MetaDataImportResultInfo importPackageMetaData(final int tenantId, final String packOid, final Tenant importTenant) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<MetaDataImportResultInfo>() {

			@Override
			public MetaDataImportResultInfo call() {
				File tempDir = getTempDir(getContextTempDir());
				MetaDataImportResult info = service.importPackageMetaData(packOid, tempDir, importTenant);

				//MetaDataImportResult -> MetaDataImportResultInfo(DTO)
				MetaDataImportResultInfo result = new MetaDataImportResultInfo();
				result.setError(info.isError());
				result.setMessages(info.getMessages());

				return result;
			}

		});
	}

	@Override
	public EntityDataImportResultInfo importPackageEntityData(final int tenantId, final String packOid, final String path, final PackageImportCondition condition) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<EntityDataImportResultInfo>() {

			@Override
			public EntityDataImportResultInfo call() {

				//EntityImportCondition(DTO) -> EntityDataImportCondition
				EntityDataImportCondition cond = new EntityDataImportCondition();
				cond.setCommitLimit(condition.getCommitLimit());
				cond.setNotifyListeners(condition.isNotifyListeners());
				if (condition.isUpdateDisupdatableProperty()) {
					cond.setUpdateDisupdatableProperty(true);
					cond.setWithValidation(false);
				} else {
					cond.setUpdateDisupdatableProperty(false);
					cond.setWithValidation(condition.isWithValidation());
				}
				cond.setErrorSkip(condition.isErrorSkip());
				cond.setIgnoreNotExistsProperty(condition.isIgnoreNotExistsProperty());
				cond.setPrefixOid(condition.getPrefixOid());
				cond.setTruncate(condition.isTruncate());
				cond.setFourceUpdate(condition.isFourceUpdate());
				if (StringUtil.isNotEmpty(condition.getLocale())) {
					cond.setLocale(condition.getLocale());
				}
				if (StringUtil.isNotEmpty(condition.getTimezone())) {
					cond.setTimezone(condition.getTimezone());
				}

				File tempDir = getTempDir(getContextTempDir());
				String entityPath =  EntityService.ENTITY_META_PATH + path.substring(0, path.length() - 4).replace(".", "/");	//.csvを除く
				EntityDataImportResult info = service.importPackageEntityData(packOid, entityPath, cond, tempDir);

				//EntityDataImportResult -> EntityDataImportResultInfo(DTO)
				EntityDataImportResultInfo result = new EntityDataImportResultInfo();
				result.setError(info.isError());
				result.setMessages(info.getMessages());

				return result;
			}

		});
	}


	private File getContextTempDir() {
		return (File)getThreadLocalRequest().getSession().getServletContext().getAttribute("javax.servlet.context.tempdir");
	}

	private File getTempDir(final File contextTempDir) {
		WebFrontendService webFront = ServiceRegistry.getRegistry().getService(WebFrontendService.class);
		File tempDir = null;
		if (webFront.getTempFileDir() == null) {
			tempDir = contextTempDir;
		} else {
			tempDir = new File(webFront.getTempFileDir());
		}
		return tempDir;
	}
}
