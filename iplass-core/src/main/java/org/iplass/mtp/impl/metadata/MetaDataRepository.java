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

package org.iplass.mtp.impl.metadata;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.SystemException;
import org.iplass.mtp.impl.metadata.MetaDataEntry.RepositoryType;
import org.iplass.mtp.impl.metadata.validation.DefinitionNameCheckValidator;
import org.iplass.mtp.impl.util.CoreResourceBundleUtil;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetaDataRepository implements Service {

	private static Logger logger = LoggerFactory.getLogger(MetaDataRepository.class);

	private MetaDataStore tenantLocalStore;
	private List<MetaDataStore> sharedStore;//indexが0に近い方が優先

	public MetaDataStore getTenantLocalStore() {
		return tenantLocalStore;
	}

	public List<MetaDataStore> getSharedStore() {
		return sharedStore;
	}

	public MetaDataEntry loadById(int tenantId, String id, boolean withShared) {
		MetaDataEntry ent = tenantLocalStore.loadById(tenantId, id);
		if (ent != null) {
			ent.setRepositryType(RepositoryType.TENANT_LOCAL);
			return ent;
		}

		if (sharedStore != null && withShared) {
			for (int i = 0; i < sharedStore.size(); i++) {
				ent = sharedStore.get(i).loadById(tenantId, id);
				if (ent != null && ent.isSharable()) {
					if (ent.isOverwritable()) {
						ent.setRepositryType(RepositoryType.TENANT_LOCAL);
					} else {
						ent.setRepositryType(RepositoryType.SHARED);
					}
					return ent;
				}
			}
		}
		return null;
	}

	public MetaDataEntry loadById(int tenantId, String id, int version, boolean withShared) {
		MetaDataEntry ent = tenantLocalStore.loadById(tenantId, id, version);
		if (ent != null) {
			ent.setRepositryType(RepositoryType.TENANT_LOCAL);
			return ent;
		}

		if (sharedStore != null && withShared) {
			for (int i = 0; i < sharedStore.size(); i++) {
				ent = sharedStore.get(i).loadById(tenantId, id, version);
				if (ent != null && ent.isSharable()) {
					if (ent.isOverwritable()) {
						ent.setRepositryType(RepositoryType.TENANT_LOCAL);
					} else {
						ent.setRepositryType(RepositoryType.SHARED);
					}
					return ent;
				}
			}
		}
		return null;
	}

	public List<MetaDataEntryInfo> definitionList(final int tenantId, final String prefixPath, boolean withShared, boolean withInvalid) {
		HashMap<String, MetaDataEntryInfo> map = new HashMap<String, MetaDataEntryInfo>();
		//indexが0の方が優先、local定義が一番優先
		if (sharedStore != null && withShared) {
			for (int i = sharedStore.size() - 1; i > -1; i--) {
				List<MetaDataEntryInfo> list = sharedStore.get(i).definitionList(tenantId, prefixPath, withInvalid);
				for (MetaDataEntryInfo definition : list) {
					if (definition.isSharable()) {
						if (definition.isOverwritable()) {
							//TODO RepositoryTypeの制御はMetaDataContext側にまとめる形にする
							definition.setRepositryType(RepositoryType.TENANT_LOCAL);
						} else {
							definition.setRepositryType(RepositoryType.SHARED);
						}
						map.put(definition.getPath(), definition);
					}
				}
			}
		}
		List<MetaDataEntryInfo> list = tenantLocalStore.definitionList(tenantId, prefixPath, withInvalid);
		for (MetaDataEntryInfo definition : list) {
			definition.setRepositryType(RepositoryType.TENANT_LOCAL);
			map.put(definition.getPath(), definition);
		}

		List<MetaDataEntryInfo> res = new ArrayList<MetaDataEntryInfo>(map.values());

		return res;

	}

	public List<MetaDataEntryInfo> definitionList(final int tenantId, final String prefixPath, boolean withShared)
			throws MetaDataRuntimeException {
		return definitionList(tenantId, prefixPath, withShared, false);
	}

	@Override
	public void destroy() {
		tenantLocalStore = null;
		sharedStore = null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void init(Config config) {

		tenantLocalStore = (MetaDataStore) config.getBean("tenantLocalStore");
		sharedStore = (List<MetaDataStore>) config.getBeans("sharedStore");
	}

	public MetaDataEntry load(int tenantId, String path, boolean withShared)
			throws MetaDataRuntimeException {
		MetaDataEntry ent = tenantLocalStore.load(tenantId, path);
		if (ent != null) {
			ent.setRepositryType(RepositoryType.TENANT_LOCAL);
			return ent;
		}

		if (sharedStore != null && withShared) {
			for (int i = 0; i < sharedStore.size(); i++) {
				ent = sharedStore.get(i).load(tenantId, path);
				if (ent != null && ent.isSharable()) {
					if (ent.getMetaData() == null) {
						throw new SystemException(tenantId + "'s " + path + " MetaData is Incompatible or not defined.");
					}
					if (ent.isOverwritable()) {
						ent.setRepositryType(RepositoryType.TENANT_LOCAL);
					} else {
						ent.setRepositryType(RepositoryType.SHARED);
					}
					return ent;
				}
			}
		}
		return null;
	}

	public MetaDataEntry load(int tenantId, String path, int version, boolean withShared)
			throws MetaDataRuntimeException {
		MetaDataEntry ent = tenantLocalStore.load(tenantId, path, version);
		if (ent != null) {
			ent.setRepositryType(RepositoryType.TENANT_LOCAL);
			return ent;
		}

		if (sharedStore != null && withShared) {
			for (int i = 0; i < sharedStore.size(); i++) {
				ent = sharedStore.get(i).load(tenantId, path, version);
				if (ent != null && ent.isSharable()) {
					if (ent.getMetaData() == null) {
						throw new SystemException(tenantId + "'s " + path + " MetaData is Incompatible or not defined.");
					}
					if (ent.isOverwritable()) {
						ent.setRepositryType(RepositoryType.TENANT_LOCAL);
					} else {
						ent.setRepositryType(RepositoryType.SHARED);
					}
					return ent;
				}
			}
		}
		return null;
	}

	public void store(int tenantId, MetaDataEntry metaDataEntry)
			throws MetaDataRuntimeException {
		this.validateDefinitionName(metaDataEntry);
		tenantLocalStore.store(tenantId, metaDataEntry);
		logger.info("store MetaData:" + metaDataEntry.getPath());
	}

	public void update(int tenantId, MetaDataEntry metaDataEntry)
			throws MetaDataRuntimeException {
		this.validateDefinitionName(metaDataEntry);
		tenantLocalStore.update(tenantId, metaDataEntry);

		logger.info("update MetaData:" + metaDataEntry.getPath());
	}

	public void remove(int tenantId, String path) throws MetaDataRuntimeException {
		tenantLocalStore.remove(tenantId, path);
		logger.info("remove MetaData:" + path);
	}

	public void updateConfigById(int tenantId, String id, MetaDataConfig config) {
		tenantLocalStore.updateConfigById(tenantId, id, config);
		logger.info("update MetaData config of id:" + id);
	}

	public List<Integer> getTenantIdsOf(String metaDataId) {
		return tenantLocalStore.getTenantIdsOf(metaDataId);
	}

	public void purgeById(int tenantId, String id) throws MetaDataRuntimeException {
		logger.info("purge meta data. tenant id = " + tenantId + ",defId = " + id);
		tenantLocalStore.purgeById(tenantId, id);
	}

	public List<MetaDataEntryInfo> getHistoryById(final int tenantId, final String definitionId)
			throws MetaDataRuntimeException {
		List<MetaDataEntryInfo> list = tenantLocalStore.getHistoryById(tenantId, definitionId);
		return list;
	}

	private void validateDefinitionName(MetaDataEntry metaDataEntry) throws MetaDataRuntimeException {
		if (Objects.isNull(metaDataEntry)) {
			return;
		}

		RootMetaData metaData = metaDataEntry.getMetaData();
		if (Objects.isNull(metaData)) {
			return;
		}

		// パスチェック
		if (!this.checkPath(metaDataEntry.getPath(), metaData.pathPrefix())) {
			throw new MetaDataRuntimeException(CoreResourceBundleUtil.resourceString("impl.metadata.validator.regularExpression.invalidPath"));
		}

		// 定義名チェック
		Optional<String> optError = this.checkDefinitionName(metaData.getName(), metaData.definitionNameCheckValidatorClass());
		if (optError.isPresent()) {
			throw new MetaDataRuntimeException(optError.get());
		}
	}

	private boolean checkPath(String path, String prefix) {
		// 必須チェックではないのでチェックに必要な情報がなかったらチェックしない（チェックOK）
		if (StringUtil.isEmpty(path) || StringUtil.isEmpty(prefix)) {
			return true;
		}

		return path.startsWith(prefix);
	}

	private Optional<String> checkDefinitionName(String definitionName, Class<? extends DefinitionNameCheckValidator> validatorType) {
		// 必須チェックではないのでチェックに必要な情報がなかったらチェックしない（チェックOK）
		if (StringUtil.isEmpty(definitionName) || Objects.isNull(validatorType)) {
			return Optional.empty();
		}

		DefinitionNameCheckValidator validator;
		try {
			validator = validatorType.getConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			// ここでのエラーはプログラムバグ
			logger.error(e.getMessage(), e);
			throw new ApplicationException(e);
		}

		return validator.validate(definitionName);
	}
}
