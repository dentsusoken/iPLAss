/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.poi.util.StringUtil;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.impl.definition.AbstractTypedMetaDataService;
import org.iplass.mtp.impl.definition.DefinitionMetaDataTypeMap;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.webapi.MetaWebApi.WebApiRuntime;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.webapi.definition.WebApiDefinition;
import org.iplass.mtp.webapi.definition.WebApiDefinitionManager;

public class WebApiService extends AbstractTypedMetaDataService<MetaWebApi, WebApiRuntime> implements Service {

	// TODO マッピングしているCommandの更新・削除イベントを監視し、関連するActionMappingのキャッシュをクリア

	public static final String WEB_API_META_PATH = "/webapi/";

	public static class TypeMap extends DefinitionMetaDataTypeMap<WebApiDefinition, MetaWebApi> {
		public TypeMap() {
			super(getFixedPath(), MetaWebApi.class, WebApiDefinition.class);
		}

		@Override
		public TypedDefinitionManager<WebApiDefinition> typedDefinitionManager() {
			return ManagerLocator.getInstance().getManager(WebApiDefinitionManager.class);
		}
	}

	private Map<Class<? extends Throwable>, Integer> statusMap;
	private Map<String, String> xRequestedWithMap;
	@Deprecated
	private CorsConfig cors;

	private boolean enableDefinitionApi;
	private boolean enableBinaryApi;
	private boolean writeEncodedFilenameInBinaryApi;
	private String unescapeFilenameCharacterInBinaryApi;
	/** バイナリファイルアップロード時に受け入れ可能な MIME Type 正規表現パターン */
	private Pattern acceptMimeTypesPatternInBinaryApi;

	public static String getFixedPath() {
		return WEB_API_META_PATH;
	}

	@Override
	public void destroy() {

	}

	@Override
	@SuppressWarnings("unchecked")
	public void init(Config config) {
		statusMap = new HashMap<>();
		List<StatusMap> smList = config.getValues("statusMap", StatusMap.class);
		if (smList != null) {
			for (StatusMap sm : smList) {
				statusMap.put(sm.getException(), sm.getStatus());
			}
		}

		xRequestedWithMap = config.getValue("xRequestedWithMap", Map.class);

		cors = config.getValue("cors", CorsConfig.class);
		enableDefinitionApi = config.getValue("enableDefinitionApi", Boolean.class, Boolean.FALSE);
		enableBinaryApi = config.getValue("enableBinaryApi", Boolean.class, Boolean.FALSE);
		writeEncodedFilenameInBinaryApi = config.getValue("writeEncodedFilenameInBinaryApi", Boolean.class,
				Boolean.FALSE);
		unescapeFilenameCharacterInBinaryApi = config.getValue("unescapeFilenameCharacterInBinaryApi");

		String acceptMimeTypesPatternInBinaryApi = config.getValue("acceptMimeTypesPatternInBinaryApi");
		this.acceptMimeTypesPatternInBinaryApi = StringUtil.isNotBlank(acceptMimeTypesPatternInBinaryApi)
				? Pattern.compile(acceptMimeTypesPatternInBinaryApi)
				: null;
	}

	public boolean isEnableDefinitionApi() {
		return enableDefinitionApi;
	}

	public boolean isEnableBinaryApi() {
		return enableBinaryApi;
	}

	public boolean isWriteEncodedFilenameInBinaryApi() {
		return writeEncodedFilenameInBinaryApi;
	}

	public String getUnescapeFilenameCharacterInBinaryApi() {
		return unescapeFilenameCharacterInBinaryApi;
	}

	/**
	 * バイナリファイルアップロード時に受け入れ可能な MIME Type 正規表現パターンを取得する
	 * @return バイナリファイルアップロード時に受け入れ可能な MIME Type 正規表現パターン
	 */
	public Pattern getAcceptMimeTypesPatternInBinaryApi() {
		return acceptMimeTypesPatternInBinaryApi;
	}

	@Deprecated
	public CorsConfig getCors() {
		return cors;
	}

	public Map<String, String> getXRequestedWithMap() {
		return xRequestedWithMap;
	}

	private String withHttpMethod(String path, String httpMethod) {
		if (path.endsWith("/")) {
			return path + httpMethod;
		} else {
			return path + "/" + httpMethod;
		}
	}

	public WebApiRuntime getByPathHierarchy(String name, String httpMethod) {

		MetaDataContext context = MetaDataContext.getContext();

		if (httpMethod != null) {
			String withMethod = withHttpMethod(name, httpMethod);
			if (context.exists(WEB_API_META_PATH, withMethod)) {
				return context.getMetaDataHandler(WebApiRuntime.class, WEB_API_META_PATH + withMethod);
			}
		}
		if (context.exists(WEB_API_META_PATH, name)) {
			return context.getMetaDataHandler(WebApiRuntime.class, WEB_API_META_PATH + name);
		}

		String path = name;
		int index = -1;
		while ((index = path.lastIndexOf("/")) >= 0) {
			path = path.substring(0, index);
			if (httpMethod != null) {
				String pathWithMethod = withHttpMethod(path, httpMethod);
				if (context.exists(WEB_API_META_PATH, pathWithMethod)) {
					return context.getMetaDataHandler(WebApiRuntime.class, WEB_API_META_PATH + pathWithMethod);
				}
			}
			if (context.exists(WEB_API_META_PATH, path)) {
				return context.getMetaDataHandler(WebApiRuntime.class, WEB_API_META_PATH + path);
			}
		}
		return null;
	}

	@Override
	public Class<MetaWebApi> getMetaDataType() {
		return MetaWebApi.class;
	}

	@Override
	public Class<WebApiRuntime> getRuntimeType() {
		return WebApiRuntime.class;
	}

	@SuppressWarnings("unchecked")
	public int mapStatus(Throwable e) {

		for (Class<? extends Throwable> type = e
				.getClass(); type != Throwable.class; type = (Class<? extends Throwable>) type.getSuperclass()) {
			Integer status = statusMap.get(type);
			if (status != null) {
				return status;
			}
		}

		return 500;
	}

}
