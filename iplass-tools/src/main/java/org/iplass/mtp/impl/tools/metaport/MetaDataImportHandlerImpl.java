/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.iplass.mtp.impl.definition.DefinitionService;
import org.iplass.mtp.impl.entity.EntityService;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.impl.metadata.MetaDataRuntimeException;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.impl.tenant.MetaTenantService;
import org.iplass.mtp.impl.util.KeyGenerator;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetaDataImportHandlerImpl implements MetaDataImportHandler {

	private static Logger auditLogger = LoggerFactory.getLogger("mtp.audit.porting.metadata");

	private EntityService ehService;
	private DefinitionService definitionService;

	@Override
	public void inited(MetaDataPortingService service, Config config) {
		ehService = config.getDependentService(EntityService.class);
		definitionService = config.getDependentService(DefinitionService.class);
	}

	@Override
	public void destroyed() {
	}

	@Override
	public void storeMetaData(String path, RootMetaData importMeta, MetaDataEntry entry, boolean doAutoReload) {

		auditLogger.info("store metadata," + importMeta.getClass().getName() + ",path:" + path);

		// メタデータ定義名チェック TODO Entity以外はこのメソッド内でMetaDataContext呼び出してしまってるためチェック処理の追加が必要
		this.checkDefinitionName(path, importMeta);

		if (StringUtil.isEmpty(importMeta.getId())) {
			importMeta.setId((new KeyGenerator()).generateId());
		}

		if (importMeta instanceof MetaEntity) {
			storeMetaEntity(path, (MetaEntity)importMeta, entry, doAutoReload);
		} else {
			MetaDataConfig config = new MetaDataConfig(entry.isSharable(), entry.isOverwritable(), entry.isDataSharable(), entry.isPermissionSharable());
			MetaDataContext.getContext().store(path, importMeta, config, doAutoReload);
		}
	}

	private void storeMetaEntity(String path, MetaEntity newEntity, MetaDataEntry entry, boolean doAutoReload) {

		MetaDataConfig config = new MetaDataConfig(entry.isSharable(), entry.isOverwritable(), entry.isDataSharable(), entry.isPermissionSharable());
		ehService.createDataModelSchema(newEntity, config, doAutoReload);
	}

	@Override
	public void updateMetaData(String path, RootMetaData importMeta, MetaDataEntry entry, boolean doAutoReload) {

		auditLogger.info("update metadata," + importMeta.getClass().getName() + ",path:" + path);

		// メタデータ定義名チェック TODO Entity以外はこのメソッド内でMetaDataContext呼び出してしまってるためチェック処理の追加が必要
		this.checkDefinitionName(path, importMeta);

		if (importMeta instanceof MetaEntity) {
			updateMetaEntity(path, (MetaEntity)importMeta, entry, doAutoReload);
		} else {
			MetaDataConfig config = new MetaDataConfig(entry.isSharable(), entry.isOverwritable(), entry.isDataSharable(), entry.isPermissionSharable());
			MetaDataContext.getContext().update(path, importMeta, config, doAutoReload);
		}
	}

	private void updateMetaEntity(String path, MetaEntity newEntity, MetaDataEntry entry, boolean doAutoReload) {

		MetaDataConfig config = new MetaDataConfig(entry.isSharable(), entry.isOverwritable(), entry.isDataSharable(), entry.isPermissionSharable());

		//更新の場合、現状はdoAutoReloadモードなし

		final Future<String> result = ehService.updateDataModelSchema(newEntity, config);
		try {
			result.get();	//Thread block
		} catch (ExecutionException e) {
			throw new MetaDataPortingRuntimeException("exception occured during entity definition update:" + e.getCause().getMessage(), e);
		} catch (InterruptedException e) {
			throw new MetaDataPortingRuntimeException("execution interrrupted during entity definition update:" + e.getMessage(), e);
		}
	}

	private void checkDefinitionName(String path, RootMetaData importMeta) throws MetaDataRuntimeException {
		if (StringUtil.isEmpty(path) || Objects.isNull(importMeta)) {
			return;
		}

		Optional<String> errorMessage = definitionService.checkDefinitionName(importMeta.getClass(), path, importMeta.getName());
		if (errorMessage.isPresent()) {
			throw new MetaDataRuntimeException(errorMessage.get());
		}
	}

	@Override
	public void removeMetaData(String path, RootMetaData removeMeta, boolean doAutoReload) {

		auditLogger.info("remove metadata," + removeMeta.getClass().getName() + ",path:" + path);

		if (removeMeta instanceof MetaEntity) {
			removeMetaEntity(path, (MetaEntity)removeMeta, doAutoReload);
		} else {
			MetaDataContext.getContext().remove(path, doAutoReload);
		}
	}

	private void removeMetaEntity(String path, MetaEntity removeEntity, boolean doAutoReload) {
		//削除の場合、現状はdoAutoReloadモードなし

		final Future<String> result = ehService.removeDataModelSchema(removeEntity);
		try {
			result.get();	//Thread block
		} catch (ExecutionException e) {
			throw new MetaDataPortingRuntimeException("exception occured during entity definition remove:" + e.getCause().getMessage(), e);
		} catch (InterruptedException e) {
			throw new MetaDataPortingRuntimeException("execution interrrupted during entity definition remove:" + e.getMessage(), e);
		}
	}

	@Override
	public boolean isTenantMeta(String path) {
		return path.startsWith(MetaTenantService.TENANT_META_PATH);
	}


}
