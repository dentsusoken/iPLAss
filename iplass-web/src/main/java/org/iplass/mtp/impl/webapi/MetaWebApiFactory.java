/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
import java.util.Map;

import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.impl.command.MetaCommand;
import org.iplass.mtp.impl.command.MetaCommandFactory;
import org.iplass.mtp.impl.metadata.annotation.AnnotatableMetaDataFactory;
import org.iplass.mtp.impl.metadata.annotation.AnnotateMetaDataEntry;
import org.iplass.mtp.webapi.definition.CacheControlType;


public class MetaWebApiFactory implements AnnotatableMetaDataFactory<WebApi, Command> {

	public static final String PATH_PREFIX = WebApiService.WEB_API_META_PATH;

	private MetaCommandFactory commandFactory = new MetaCommandFactory();

	@Override
	public Class<Command> getAnnotatedClass() {
		return Command.class;
	}

	@Override
	public Class<WebApi> getAnnotationClass() {
		return WebApi.class;
	}

	@Override
	public Map<String, AnnotateMetaDataEntry> toMetaData(Class<Command> annotatedClass) {
		WebApi webapi = annotatedClass.getAnnotation(WebApi.class);
		return toMetaData(webapi, annotatedClass);
	}

	Map<String, AnnotateMetaDataEntry> toMetaData(WebApi webapi, Class<Command> annotatedClass) {
		Map<String, AnnotateMetaDataEntry> map = new HashMap<String, AnnotateMetaDataEntry>();
		MetaWebApi meta = new MetaWebApi();
		meta.setName(webapi.name());

		String path = PATH_PREFIX + webapi.name();

		if (!DEFAULT.equals(webapi.id())) {
			meta.setId(webapi.id());
		} else {
			meta.setId(path);
		}

		meta.setName(webapi.name());

		if (webapi.command() != null) {
			MetaCommand metaCommand = commandFactory.toMetaCommand(webapi.command(), annotatedClass).metaCommand;
			meta.setCommand(metaCommand);
		}

		if (webapi.restJson() == null) {
			meta.setRestJsonParameterName(null);
			meta.setRestJsonParameterType(void.class);
		} else {
			meta.setRestJsonParameterName(webapi.restJson().parameterName());
			meta.setRestJsonParameterType(webapi.restJson().parameterType());
		}

		if (webapi.restXml() == null) {
			meta.setRestXmlParameterName(null);
		} else {
			meta.setRestXmlParameterName(webapi.restXml().parameterName());
		}

		if (!DEFAULT.equals(webapi.displayName())) {
			meta.setDisplayName(webapi.displayName());
		} else {
			//指定されていない場合は、Commandの定義をセット
			CommandClass classDef = annotatedClass.getAnnotation(CommandClass.class);
			if (!DEFAULT.equals(classDef.displayName())) {
				meta.setDisplayName(classDef.displayName());
			}
		}
		if (!DEFAULT.equals(webapi.description())) {
			meta.setDescription(webapi.description());
		} else {
			//指定されていない場合は、Commandの定義をセット
			CommandClass classDef = annotatedClass.getAnnotation(CommandClass.class);
			if (!DEFAULT.equals(classDef.description())) {
				meta.setDescription(classDef.description());
			}
		}

		if (webapi.cacheControlType() != null) {
			switch (webapi.cacheControlType()) {
				case CACHE:
					meta.setCacheControlType(CacheControlType.CACHE);
					break;
				case NO_CACHE:
					meta.setCacheControlType(CacheControlType.NO_CACHE);
					break;
				default:
					break;
			}
		}
		meta.setCacheControlMaxAge(webapi.cacheControlMaxAge());

		meta.setAccepts(webapi.accepts());
		meta.setMethods(webapi.methods());
		meta.setResults(webapi.results());
		meta.setState(webapi.state());
		meta.setSupportBearerToken(webapi.supportBearerToken());
		meta.setPrivilaged(webapi.privilaged());
		meta.setPublicWebApi(webapi.publicWebApi());
		meta.setCheckXRequestedWithHeader(webapi.checkXRequestedWithHeader());
		meta.setResponseType(webapi.responseType());

		//ValidateToken設定(ActionMappingからチェック)
		if (webapi.tokenCheck().executeCheck()) {
			MetaWebApiTokenCheck tokenCheck = new MetaWebApiTokenCheck();
			tokenCheck.setConsume(webapi.tokenCheck().consume());
			tokenCheck.setExceptionRollback(webapi.tokenCheck().exceptionRollback());
			tokenCheck.setUseFixedToken(webapi.tokenCheck().useFixedToken());
			meta.setTokenCheck(tokenCheck);
		}

		meta.setSynchronizeOnSession(webapi.synchronizeOnSession());
		meta.setAccessControlAllowOrigin(webapi.accessControlAllowOrign());
		meta.setAccessControlAllowCredentials(webapi.accessControlAllowCredentials());
		meta.setNeedTrustedAuthenticate(webapi.needTrustedAuthenticate());

		map.put(path, new AnnotateMetaDataEntry(meta, webapi.overwritable(), webapi.permissionSharable()));
		return map;
	}
}
