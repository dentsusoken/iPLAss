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

package org.iplass.mtp.impl.web.actionmapping;

import java.util.List;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.impl.definition.AbstractTypedMetaDataService;
import org.iplass.mtp.impl.definition.DefinitionMetaDataTypeMap;
import org.iplass.mtp.impl.definition.validation.DefinitionNameCheckValidator;
import org.iplass.mtp.impl.definition.validation.PathSlashNamePeriodCheckValidator;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.web.actionmapping.MetaActionMapping.ActionMappingRuntime;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.web.actionmapping.definition.ActionMappingDefinition;
import org.iplass.mtp.web.actionmapping.definition.ActionMappingDefinitionManager;
import org.iplass.mtp.web.interceptor.RequestInterceptor;

public class ActionMappingService extends AbstractTypedMetaDataService<MetaActionMapping, ActionMappingRuntime> implements Service {

	//TODO マッピングしているCommandの更新・削除イベントを監視し、関連するActionMappingのキャッシュをクリア
	//TODO マッピングしているTemplateの更新・削除イベントを監視し、関連するActionMappingのキャッシュをクリア

	public static final String INTERCEPTOR_NAME = "interceptor";
	public static final String COMMAND_INTERCEPTOR_NAME = "web";

	public static final String ACTION_MAPPING_META_PATH = "/action/";

	public static class TypeMap extends DefinitionMetaDataTypeMap<ActionMappingDefinition, MetaActionMapping> {
		public TypeMap() {
			super(getFixedPath(), MetaActionMapping.class, ActionMappingDefinition.class);
		}

		@Override
		public TypedDefinitionManager<ActionMappingDefinition> typedDefinitionManager() {
			return ManagerLocator.getInstance().getManager(ActionMappingDefinitionManager.class);
		}

		@Override
		protected DefinitionNameCheckValidator createDefinitionNameCheckValidator() {
			return new PathSlashNamePeriodCheckValidator();
		}
	}

	private RequestInterceptor[] interceptors;

	public RequestInterceptor[] getInterceptors() {
		return interceptors;
	}

	public void destroy() {
	}

	public void init(Config config) {
		List<RequestInterceptor> interceptorsList = config.getValues(INTERCEPTOR_NAME, RequestInterceptor.class);
		if (interceptorsList != null) {
			interceptors = interceptorsList.toArray(new RequestInterceptor[interceptorsList.size()]);
		}
	}

	public static String getFixedPath() {
		return ACTION_MAPPING_META_PATH;
	}

	public ActionMappingRuntime getByPathHierarchy(String name) {
		MetaDataContext context = MetaDataContext.getContext();
		//直接マップされるActionMapping定義がある場合
		if (context.exists(ACTION_MAPPING_META_PATH, name)) {
			return context.getMetaDataHandler(ActionMappingRuntime.class, ACTION_MAPPING_META_PATH + name);
		}

		String path = name;
		int index = -1;
		while ((index = path.lastIndexOf("/")) >= 0) {
			path = path.substring(0, index);
			if (context.exists(ACTION_MAPPING_META_PATH, path)) {
				ActionMappingRuntime ac = context.getMetaDataHandler(ActionMappingRuntime.class, ACTION_MAPPING_META_PATH + path);
				//パスのパラメータマッピング定義があるActionMappingのみ、階層の一部一致で当該ActionMappingが呼び出されたと判断
				if (ac != null && ac.getParamMapRuntimes() != null && ac.getParamMapRuntimes().size() > 0) {
					return ac;
				}
			}
		}

		return null;
	}

	public ActionMappingRuntime getByPathHierarchy(String name, List<String> welcomeActionNameList) {
		ActionMappingRuntime actionMapping = getByPathHierarchy(name);
		if (actionMapping == null
				&& welcomeActionNameList != null
				&& (name.length() == 0 || name.endsWith("/"))) {
			MetaDataContext context = MetaDataContext.getContext();
			for (String wa : welcomeActionNameList) {
				String waPath = name + wa;
				if (context.exists(ACTION_MAPPING_META_PATH, waPath)) {
					actionMapping = context.getMetaDataHandler(ActionMappingRuntime.class, ACTION_MAPPING_META_PATH + waPath);
				}
				if (actionMapping != null) {
					break;
				}
			}
		}
		return actionMapping;
	}

	@Override
	public Class<MetaActionMapping> getMetaDataType() {
		return MetaActionMapping.class;
	}

	@Override
	public Class<ActionMappingRuntime> getRuntimeType() {
		return ActionMappingRuntime.class;
	}

}
