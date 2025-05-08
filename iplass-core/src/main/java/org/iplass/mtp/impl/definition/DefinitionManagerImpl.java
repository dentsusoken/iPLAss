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

package org.iplass.mtp.impl.definition;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.iplass.mtp.definition.Definition;
import org.iplass.mtp.definition.DefinitionEntry;
import org.iplass.mtp.definition.DefinitionInfo;
import org.iplass.mtp.definition.DefinitionManager;
import org.iplass.mtp.definition.DefinitionSummary;
import org.iplass.mtp.definition.IllegalDefinitionStateException;
import org.iplass.mtp.definition.SharedConfig;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.definition.VersionHistory;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.impl.metadata.MetaDataEntry.RepositoryType;
import org.iplass.mtp.impl.metadata.MetaDataEntry.State;
import org.iplass.mtp.impl.metadata.MetaDataEntryInfo;
import org.iplass.mtp.impl.metadata.MetaDataIllegalStateException;
import org.iplass.mtp.impl.metadata.MetaDataRepository;
import org.iplass.mtp.impl.metadata.MetaDataRuntimeException;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.spi.ServiceRegistry;

public class DefinitionManagerImpl implements DefinitionManager {

	private final MetaDataRepository repository = ServiceRegistry.getRegistry().getService(MetaDataRepository.class);
	private final DefinitionService defService = ServiceRegistry.getRegistry().getService(DefinitionService.class);

	@Override
	public <D extends Definition> void setSharedConfig(Class<D> type,
			String definitionName, SharedConfig config) {
		MetaDataContext.getContext().updateConfig(DefinitionService.getInstance().getPath(type, definitionName), new MetaDataConfig(config.isSharable(), config.isOverwritable(), config.isDataSharable(), config.isPermissionSharable()));
	}

	@Override
	public <D extends Definition> DefinitionInfo getInfo(Class<D> type,
			String definitionName) {
		MetaDataEntry entry = MetaDataContext.getContext().getMetaDataEntry(DefinitionService.getInstance().getPath(type, definitionName));
		if (entry == null) {
			return null;
		}
		DefinitionInfo info = new DefinitionInfo();
		if (entry.getMetaData() != null) {
			info.setName(entry.getMetaData().getName());
			info.setDisplayName(entry.getMetaData().getDisplayName());
			info.setDescription(entry.getMetaData().getDescription());
			info.setType(type.getSimpleName());
		}
		info.setSharedConfig(new SharedConfig(entry.isSharable(), entry.isOverwritable(), entry.isDataSharable(), entry.isPermissionSharable()));
		info.setShared(entry.getRepositryType() == RepositoryType.SHARED);
		info.setSharedOverwrite(entry.getRepositryType() == RepositoryType.SHARED_OVERWRITE);
		info.setVersion(entry.getVersion());
		return info;
	}

	@Override
	public <D extends Definition> List<DefinitionSummary> listName(Class<D> type, String filterPath) {
		return listName(type, filterPath, true);
	}

	@Override
	public <D extends Definition> List<DefinitionSummary> listName(Class<D> type, String filterPath, boolean recursive) {
		String fixedPath = defService.getPrefixPath(type);
		String path;
		if (filterPath == null || filterPath.length() == 0 || "/".equals(filterPath)) {
			path = fixedPath;
		} else {
			path = defService.getPath(type, filterPath);
		}

		List<MetaDataEntryInfo> entryInfoList = MetaDataContext.getContext().definitionList(path);

		List<DefinitionSummary> ret = new ArrayList<DefinitionSummary>(entryInfoList.size());
		for (MetaDataEntryInfo definition : entryInfoList) {
			DefinitionSummary def = new DefinitionSummary(
					definition.getPath(),
					defService.getDefinitionName(type, definition.getPath()),
					definition.getDisplayName(), definition.getDescription());
			if (recursive) {
				ret.add(def);
			} else {
				if (path.endsWith("/")) {
					if (!definition.getPath().substring(path.length()).contains("/")) {
						ret.add(def);
					}
				} else {
					if (!definition.getPath().substring(path.length() + 1).contains("/")) {
						ret.add(def);
					}
				}
			}
		}

		return ret;
	}

	@Override
	public <D extends Definition> List<DefinitionInfo> listInfo(Class<D> type, String filterPath) {
		String fixedPath = defService.getPrefixPath(type);
		String path;
		if (filterPath == null || "/".equals(filterPath)) {
			path = fixedPath;
		} else {
			path = DefinitionService.getInstance().getPath(type, filterPath);
		}

		List<MetaDataEntryInfo> entryInfoList = MetaDataContext.getContext().definitionList(path);
		ArrayList<DefinitionInfo> infoList = new ArrayList<DefinitionInfo>(entryInfoList.size());
		for (MetaDataEntryInfo entry: entryInfoList) {
			DefinitionInfo info = new DefinitionInfo();
			info.setName(defService.getDefinitionName(type, entry.getPath()));
			info.setDisplayName(entry.getDisplayName());
			info.setDescription(entry.getDescription());
			info.setType(type.getSimpleName());
			info.setSharedConfig(new SharedConfig(entry.isSharable(), entry.isOverwritable(), entry.isDataSharable(), entry.isPermissionSharable()));
			info.setShared(entry.getRepositryType() == RepositoryType.SHARED);
			info.setVersion(entry.getVersion());
			infoList.add(info);
		}

		return infoList;
	}

	@Override
	public <D extends Definition> void checkState(Class<D> type,
			String definitionName) throws IllegalDefinitionStateException {
		try {
			MetaDataContext.getContext().checkState(DefinitionService.getInstance().getPath(type, definitionName));
		} catch (MetaDataIllegalStateException e) {
			if (e.getCause() != e) {
				throw new IllegalDefinitionStateException("Illegal State. Check Definition on AdminConsole. Message:" + e.getCause().getMessage() + "(" + e.getCause().getClass().getName() + ")");
			} else {
				throw new IllegalDefinitionStateException("Illegal State. Check Definition on AdminConsole. Message:" + e.getMessage());
			}
		}
	}

	@Override
	public <D extends Definition> DefinitionEntry getDefinitionEntry(Class<D> type, String definitionName) {
		MetaDataEntry entry = MetaDataContext.getContext().getMetaDataEntry(DefinitionService.getInstance().getPath(type, definitionName));
		if (entry == null) {
			return null;
		}

		DefinitionInfo info = new DefinitionInfo();
		if (entry.getMetaData() != null) {
			info.setName(entry.getMetaData().getName());
			info.setDisplayName(entry.getMetaData().getDisplayName());
			info.setDescription(entry.getMetaData().getDescription());
			info.setType(type.getSimpleName());
		}
		info.setSharedConfig(new SharedConfig(entry.isSharable(), entry.isOverwritable(), entry.isDataSharable(), entry.isPermissionSharable()));
		info.setShared(entry.getRepositryType() == RepositoryType.SHARED);
		info.setSharedOverwrite(entry.getRepositryType() == RepositoryType.SHARED_OVERWRITE);
		info.setVersion(entry.getVersion());
		info.setObjDefId(entry.getMetaData().getId());

		DefinitionEntry definitionEntry = new DefinitionEntry();
		definitionEntry.setDefinitionInfo(info);

		Definition definition = defService.toDefinition(entry.getMetaData());

		definitionEntry.setDefinition(definition);

		return definitionEntry;

	}

	@Override
	public <D extends Definition> DefinitionEntry getDefinitionEntry(Class<D> type, String definitionName, int version) {
		MetaDataEntry temp = MetaDataContext.getContext().getMetaDataEntry(DefinitionService.getInstance().getPath(type, definitionName));
		if (temp == null) {
			return null;
		}

		MetaDataEntry entry = MetaDataContext.getContext().getMetaDataEntryById(temp.getMetaData().getId(), version);
		DefinitionInfo info = new DefinitionInfo();
		if (entry.getMetaData() != null) {
			info.setName(entry.getMetaData().getName());
			info.setDisplayName(entry.getMetaData().getDisplayName());
			info.setDescription(entry.getMetaData().getDescription());
			info.setType(type.getSimpleName());
		}
		info.setSharedConfig(new SharedConfig(entry.isSharable(), entry.isOverwritable(), entry.isDataSharable(), entry.isPermissionSharable()));
		info.setShared(entry.getRepositryType() == RepositoryType.SHARED);
		info.setSharedOverwrite(entry.getRepositryType() == RepositoryType.SHARED_OVERWRITE);
		info.setVersion(entry.getVersion());
		info.setObjDefId(entry.getMetaData().getId());

		DefinitionEntry definitionEntry = new DefinitionEntry();
		definitionEntry.setDefinitionInfo(info);

//		Definition definition = defService.toDefinition(entry.getMetaData());
		Definition definition = ((DefinableMetaData<?>) entry.getMetaData()).currentConfig();

		definitionEntry.setDefinition(definition);

		return definitionEntry;
	}

//	@Override
	public <D extends Definition> DefinitionInfo getHistoryById(Class<D> type, String definitionId) {
		int tenantId = ExecuteContext.getCurrentContext().getTenantContext().getTenantId();
		List<MetaDataEntryInfo> entryInfoList = repository.getHistoryById(tenantId, definitionId);

		DefinitionInfo info = new DefinitionInfo();
		ArrayList<VersionHistory> versionList = new ArrayList<VersionHistory>(entryInfoList.size());
		for (MetaDataEntryInfo entry: entryInfoList) {
			VersionHistory history = new VersionHistory();
			history.setUpdateBy(entry.getCreateUser());
			history.setUpdateDate(entry.getCreateDate());
			history.setVersion(entry.getVersion());
			versionList.add(history);

			if (entry.getState() == State.VALID) {
				info.setName(defService.getDefinitionName(type, entry.getPath()));
				info.setDisplayName(entry.getDisplayName());
				info.setDescription(entry.getDescription());
				info.setType(type.getSimpleName());
				info.setSharedConfig(new SharedConfig(entry.isSharable(), entry.isOverwritable(), entry.isDataSharable(), entry.isPermissionSharable()));
				info.setShared(entry.getRepositryType() == RepositoryType.SHARED);
				info.setVersion(entry.getVersion());
			}
		}
		info.setVersionHistory(versionList);

		return info;
	}

	@Override
	public <D extends Definition> void rename(Class<D> type, String oldDefinitionName, String newDefinitionName) {
		String oldPath = defService.getPath(type, oldDefinitionName);
		String newPath = defService.getPath(type, newDefinitionName);
		MetaDataContext metaContext = MetaDataContext.getContext();
		MetaDataEntry current = metaContext.getMetaDataEntry(oldPath);
		if (current == null) {
			throw new MetaDataRuntimeException(oldDefinitionName + "of " + type.getName() + " not found");
		}

		// TODO AdminConsoleからリネームする場合、AbstractTypedMetaDataServiceやAbstractTypedDefinitionManagerは経由していない
		// パスはnewPathが自動生成されるので、ここでのパスチェックは不要
		Optional<String> checkResult = defService.checkDefinitionNameByTypeWithoutPath(type, newDefinitionName);
		if (checkResult.isPresent()) {
			throw new MetaDataRuntimeException(checkResult.get());
		}

		RootMetaData renamed = current.getMetaData().copy();
		renamed.setName(newDefinitionName);
		metaContext.update(newPath, renamed);
	}

	@Override
	public <D extends Definition> TypedDefinitionManager<D> getTypedDefinitionManager(Class<D> type) {
		return defService.getTypedDefinitionManager(type);
	}

}
