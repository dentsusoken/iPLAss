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

package org.iplass.mtp.impl.webapi;

import java.util.List;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.impl.definition.AbstractTypedMetaDataService;
import org.iplass.mtp.impl.definition.DefinitionMetaDataTypeMap;
import org.iplass.mtp.impl.webapi.MetaEntityWebApi.EntityWebApiHandler;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.webapi.definition.EntityWebApiDefinition;
import org.iplass.mtp.webapi.definition.EntityWebApiDefinitionManager;

/**
 * EntityWebApi定義を扱うサービス
 * @author lis7gv
 */
public class EntityWebApiService extends AbstractTypedMetaDataService<MetaEntityWebApi, EntityWebApiHandler> implements Service {

	/** メタデータのパス */
	public static final String META_PATH = "/entityWebapi/";

	public static class TypeMap extends DefinitionMetaDataTypeMap<EntityWebApiDefinition, MetaEntityWebApi> {
		public TypeMap() {
			super(getFixedPath(), MetaEntityWebApi.class, EntityWebApiDefinition.class);
		}
		@Override
		public TypedDefinitionManager<EntityWebApiDefinition> typedDefinitionManager() {
			return ManagerLocator.getInstance().getManager(EntityWebApiDefinitionManager.class);
		}
		@Override
		public String toPath(String defName) {
			return pathPrefix + defName.replace('.', '/');
		}
		@Override
		public String toDefName(String path) {
			return path.substring(pathPrefix.length()).replace("/", ".");
		}
	}

	private int maxLimit;
	private String csvDateTimeFormat;
	private String csvDateFormat;
	private String csvTimeFormat;
	private String csvDownloadFooter;
	
	private boolean enableNativeHint;

	//for backward compatibility
	private boolean csvListWithMappedByReference;
	private boolean listWithMappedByReference;
	private boolean loadWithMappedByReference;

	private boolean throwSearchResultLimitExceededException;

	/** EntityCRUD API 制御オプション許可ロール */
	private List<String> permitRolesToSpecifyOptions;

	public boolean isEnableNativeHint() {
		return enableNativeHint;
	}

	public boolean isThrowSearchResultLimitExceededException() {
		return throwSearchResultLimitExceededException;
	}

	public boolean isLoadWithMappedByReference() {
		return loadWithMappedByReference;
	}

	//for backward compatibility
	public boolean isCsvListWithMappedByReference() {
		return csvListWithMappedByReference;
	}

	public boolean isListWithMappedByReference() {
		return listWithMappedByReference;
	}

	public int getMaxLimit() {
		return maxLimit;
	}

	public String getCsvDateTimeFormat() {
		return csvDateTimeFormat;
	}

	public String getCsvDateFormat() {
		return csvDateFormat;
	}

	public String getCsvTimeFormat() {
		return csvTimeFormat;
	}
	
	public String getCsvDownloadFooter() {
		return csvDownloadFooter;
	}

	/**
	 * EntityCRUD APIで制御オプションが許可されたロールか
	 * @return 許可されたロールの場合true
	 */
	public boolean isPermitRolesToSpecifyOptions() {
		AuthContext auth = AuthContext.getCurrentContext();
		if (auth.getUser().isAdmin()) {
			// adminなら許可
			return true;
		}

		if (permitRolesToSpecifyOptions != null && !permitRolesToSpecifyOptions.isEmpty()) {
			for (String role : permitRolesToSpecifyOptions) {
				if (auth.userInRole(role)) {
					//指定ロールなら許可
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void destroy() {
	}

	@Override
	public void init(Config config) {
		enableNativeHint = config.getValue("enableNativeHint", Boolean.TYPE, Boolean.FALSE);
		maxLimit = config.getValue("maxLimit", Integer.class, 1000);
		csvDateTimeFormat = config.getValue("csvDateTimeFormat", String.class, null);
		csvDateFormat = config.getValue("csvDateFormat", String.class, null);
		csvTimeFormat = config.getValue("csvTimeFormat", String.class, null);
		csvDownloadFooter = config.getValue("csvDownloadFooter", String.class, "");
		
		listWithMappedByReference = config.getValue("listWithMappedByReference", Boolean.TYPE, Boolean.FALSE).booleanValue();
		
		//for backward compatibility
		//基本的には、listWithMappedByReferenceと同じ設定値を参照。csvListWithMappedByReferenceが明示的に指定された場合のみ適用
		Boolean valOfCsvList = config.getValue("csvListWithMappedByReference", Boolean.TYPE);
		if (valOfCsvList != null) {
			csvListWithMappedByReference = valOfCsvList.booleanValue();
		} else {
			csvListWithMappedByReference = listWithMappedByReference;
		}
		loadWithMappedByReference = config.getValue("loadWithMappedByReference", Boolean.TYPE, Boolean.FALSE);

		throwSearchResultLimitExceededException = config.getValue("throwSearchResultLimitExceededException", Boolean.TYPE, Boolean.FALSE);

		permitRolesToSpecifyOptions = config.getValues("permitRolesToSpecifyOptions");
	}

	public static String getFixedPath() {
		return META_PATH;
	}

	@Override
	public Class<MetaEntityWebApi> getMetaDataType() {
		return MetaEntityWebApi.class;
	}

	@Override
	public Class<EntityWebApiHandler> getRuntimeType() {
		return EntityWebApiHandler.class;
	}
}
