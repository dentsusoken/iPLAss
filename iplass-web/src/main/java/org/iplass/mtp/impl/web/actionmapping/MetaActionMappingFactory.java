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

package org.iplass.mtp.impl.web.actionmapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.action.ActionMapping;
import org.iplass.mtp.command.annotation.action.ParamMapping;
import org.iplass.mtp.command.annotation.action.Result.UNSPECIFIED;
import org.iplass.mtp.command.annotation.action.cache.CacheCriteria;
import org.iplass.mtp.command.annotation.action.cache.CacheCriteria.Type;
import org.iplass.mtp.command.annotation.action.cache.CacheRelatedEntity;
import org.iplass.mtp.definition.annotation.LocalizedString;
import org.iplass.mtp.impl.command.MetaCommandFactory;
import org.iplass.mtp.impl.command.MetaCommandFactory.MetaCommandResult;
import org.iplass.mtp.impl.i18n.MetaLocalizedString;
import org.iplass.mtp.impl.metadata.annotation.AnnotatableMetaDataFactory;
import org.iplass.mtp.impl.metadata.annotation.AnnotateMetaDataEntry;
import org.iplass.mtp.impl.web.WebFrontendService;
import org.iplass.mtp.impl.web.actionmapping.cache.MetaCacheCriteria;
import org.iplass.mtp.impl.web.actionmapping.cache.MetaCacheRelatedEntity;
import org.iplass.mtp.impl.web.actionmapping.cache.MetaJavaClassCacheCriteria;
import org.iplass.mtp.impl.web.actionmapping.cache.MetaParameterMatchCacheCriteria;
import org.iplass.mtp.impl.web.actionmapping.cache.MetaScriptingCacheCriteria;
import org.iplass.mtp.impl.web.template.MetaJspTemplate;
import org.iplass.mtp.impl.web.template.TemplateService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.web.actionmapping.ActionCacheCriteria;
import org.iplass.mtp.web.actionmapping.definition.HttpMethodType;


public class MetaActionMappingFactory implements
		AnnotatableMetaDataFactory<ActionMapping, Command> {

	private MetaCommandFactory commandFactory = new MetaCommandFactory();

	public MetaActionMappingFactory() {
	}

	@Override
	public Class<Command> getAnnotatedClass() {
		return Command.class;
	}

	@Override
	public Class<ActionMapping> getAnnotationClass() {
		return ActionMapping.class;
	}

	private Result toMetaResult(String actionMappingPath, org.iplass.mtp.command.annotation.action.Result result, Map<String, AnnotateMetaDataEntry> map, boolean overwritable) {
		Result r = null;
		switch (result.type()) {
		case JSP:
			MetaJspTemplate template = new MetaJspTemplate();
			String jspPath = result.value();
			template.setPath(jspPath);
			if (DEFAULT.equals(result.templateName())) {
				if (jspPath.startsWith("/")) {
					template.setName(jspPath.substring(1));
				} else {
					template.setName(jspPath);
				}
			} else {
				template.setName(result.templateName());
			}
			String metaPath = TemplateService.TEMPLATE_META_PATH + template.getName();
			template.setId(metaPath);
			if (DEFAULT.equals(result.contentType())) {
				template.setContentType(ServiceRegistry.getRegistry().getService(WebFrontendService.class).getDefaultContentType());
			} else {
				template.setContentType(result.contentType());
			}
			map.put(metaPath, new AnnotateMetaDataEntry(template, overwritable, false));

			TemplateResult tr = new TemplateResult(null, metaPath);
			tr.setUseContentDisposition(result.useContentDisposition());
			tr.setContentDispositionType(result.contentDispositionType());
			tr.setFileNameAttributeName(result.fileNameAttributeName());

			if (StringUtil.isNotEmpty(result.layoutActionName())) {
				tr.setLayoutName(result.layoutActionName());
				tr.setLayoutResolveByName(true);
			}
			r = tr;
			break;
		case TEMPLATE:
			tr = new TemplateResult(null, result.resolveByName(), result.value());
			tr.setUseContentDisposition(result.useContentDisposition());
			tr.setContentDispositionType(result.contentDispositionType());
			tr.setFileNameAttributeName(result.fileNameAttributeName());
			if (StringUtil.isNotEmpty(result.layoutActionName())) {
				tr.setLayoutName(result.layoutActionName());
				tr.setLayoutResolveByName(true);
			}
			r = tr;
			break;
		case DYNAMIC:
			String templatePathAttributeName = DEFAULT.equals(result.value()) ? "templateName" : result.value();
			DynamicTemplateResult dtr = new DynamicTemplateResult(null, templatePathAttributeName);
			dtr.setUseContentDisposition(result.useContentDisposition());
			dtr.setContentDispositionType(result.contentDispositionType());
			dtr.setFileNameAttributeName(result.fileNameAttributeName());

			String layoutActionAttributeName = DEFAULT.equals(result.layoutActionAttributeName()) ? "layoutActionName" : result.layoutActionAttributeName();
			dtr.setLayoutActionAttributeName(layoutActionAttributeName);
			r = dtr;
			break;
		case REDIRECT:
			r = new RedirectResult(null, result.value(), result.allowExternalLocation());
			break;
		case STREAM:
			String inputStreamAttributeName = DEFAULT.equals(result.value()) ? "streamData" : result.value();
			StreamResult sr = new StreamResult(null, inputStreamAttributeName);
			if (!DEFAULT.equals(result.contentTypeAttributeName())) {
				sr.setContentTypeAttributeName(result.contentTypeAttributeName());
			}
			if (!DEFAULT.equals(result.contentLengthAttributeName())) {
				sr.setContentLengthAttributeName(result.contentLengthAttributeName());
			}
			sr.setUseContentDisposition(result.useContentDisposition());
			sr.setContentDispositionType(result.contentDispositionType());
			sr.setFileNameAttributeName(result.fileNameAttributeName());
			sr.setAcceptRanges(result.acceptRanges());
			r = sr;
			break;
		default:
			r = null;
		}

		if (r != null) {
			if (result.exception() != UNSPECIFIED.class) {
				r.setExceptionClassName(result.exception().getName());
			}

			if (DEFAULT.equals(result.status())) {
				if (result.exception() == UNSPECIFIED.class) {
					r.setCommandResultStatus("*");
				}
			} else {
				r.setCommandResultStatus(result.status());
			}
		}
		return r;
	}

	private MetaCacheCriteria toMetaCacheCriteria(CacheCriteria anoCacheCriteria) {

		MetaCacheCriteria criteria = null;
		switch(anoCacheCriteria.type()) {
		case JAVA_CLASS:
			if (anoCacheCriteria.javaCriteriaClass() != ActionCacheCriteria.class) {
				MetaJavaClassCacheCriteria javaCriteria = new MetaJavaClassCacheCriteria();
				javaCriteria.setClassName(anoCacheCriteria.javaCriteriaClass().getName());
				criteria = javaCriteria;
				break;
			} else {
				return null;
			}
		case PARAMETER_MATCH:
			MetaParameterMatchCacheCriteria paramCriteria = new MetaParameterMatchCacheCriteria();
			paramCriteria.setMatchingParameterName(Arrays.asList(anoCacheCriteria.matchingParameterName()));
			criteria = paramCriteria;
			break;
		case SCRIPTING:
			MetaScriptingCacheCriteria scriptCriteria = new MetaScriptingCacheCriteria();
			scriptCriteria.setScript(anoCacheCriteria.scriptCriteria());
			criteria = scriptCriteria;
			break;
		default:
			return null;
		}

		if (anoCacheCriteria.cachableResultStatus().length > 0) {
			criteria.setCachableCommandResultStatus(Arrays.asList(anoCacheCriteria.cachableResultStatus()));
		}
		if (anoCacheCriteria.cacheRelatedEntity().length > 0) {
			List<MetaCacheRelatedEntity> entities = new ArrayList<MetaCacheRelatedEntity>();
			for (CacheRelatedEntity anoEntity : anoCacheCriteria.cacheRelatedEntity()) {
				MetaCacheRelatedEntity entity = new MetaCacheRelatedEntity();
				entity.setDefinitionName(anoEntity.definitionName());
				entity.setType(anoEntity.type());
				entities.add(entity);
			}
			criteria.setRelatedEntity(entities);
		}
		if (anoCacheCriteria.timeToLive() >= 0) {
			criteria.setTimeToLive(anoCacheCriteria.timeToLive());
		}
		return criteria;
	}

	Map<String, AnnotateMetaDataEntry> toMetaData(ActionMapping actionMapping, Class<Command> annotatedClass) {
		Map<String, AnnotateMetaDataEntry> map = new HashMap<String, AnnotateMetaDataEntry>();
		MetaActionMapping metaActionMapping = new MetaActionMapping();

		String path = ActionMappingService.ACTION_MAPPING_META_PATH + actionMapping.name();

		metaActionMapping.setName(actionMapping.name());
		if (!DEFAULT.equals(actionMapping.id())) {
			metaActionMapping.setId(actionMapping.id());
		} else {
			metaActionMapping.setId(path);
		}
		if (!DEFAULT.equals(actionMapping.displayName())) {
			metaActionMapping.setDisplayName(actionMapping.displayName());
		} else {
			//指定されていない場合は、Commandの定義をセット
			CommandClass classDef = annotatedClass.getAnnotation(CommandClass.class);
			if (!DEFAULT.equals(classDef.displayName())) {
				metaActionMapping.setDisplayName(classDef.displayName());
			}
		}
		if (actionMapping.localizedDisplayName().length > 0) {
			List<MetaLocalizedString> localizedDisplayNameList = new ArrayList<>();
			for (LocalizedString localeValue : actionMapping.localizedDisplayName()) {
				MetaLocalizedString metaLocaleValue = new MetaLocalizedString();
				metaLocaleValue.setLocaleName(localeValue.localeName());
				metaLocaleValue.setStringValue(localeValue.stringValue());
				localizedDisplayNameList.add(metaLocaleValue);
			}
			metaActionMapping.setLocalizedDisplayNameList(localizedDisplayNameList);
		} else {
			//指定されていない場合は、Commandの定義をセット
			CommandClass classDef = annotatedClass.getAnnotation(CommandClass.class);
			if (classDef.localizedDisplayName().length > 0) {
				List<MetaLocalizedString> localizedDisplayNameList = new ArrayList<>();
				for (LocalizedString localeValue : classDef.localizedDisplayName()) {
					MetaLocalizedString metaLocaleValue = new MetaLocalizedString();
					metaLocaleValue.setLocaleName(localeValue.localeName());
					metaLocaleValue.setStringValue(localeValue.stringValue());
					localizedDisplayNameList.add(metaLocaleValue);
				}
				metaActionMapping.setLocalizedDisplayNameList(localizedDisplayNameList);
			}
		}
		if (!DEFAULT.equals(actionMapping.description())) {
			metaActionMapping.setDescription(actionMapping.description());
		} else {
			//指定されていない場合は、Commandの定義をセット
			CommandClass classDef = annotatedClass.getAnnotation(CommandClass.class);
			if (!DEFAULT.equals(classDef.description())) {
				metaActionMapping.setDescription(classDef.description());
			}
		}

		if (actionMapping.clientCacheType() != null) {
			switch (actionMapping.clientCacheType()) {
				case CACHE:
					metaActionMapping.setClientCacheType(org.iplass.mtp.web.actionmapping.definition.ClientCacheType.CACHE);
					break;
				case CACHE_PUBLIC:
					metaActionMapping.setClientCacheType(org.iplass.mtp.web.actionmapping.definition.ClientCacheType.CACHE_PUBLIC);
					break;
				case NO_CACHE:
					metaActionMapping.setClientCacheType(org.iplass.mtp.web.actionmapping.definition.ClientCacheType.NO_CACHE);
					break;
				default:
					break;
			}
		}
		metaActionMapping.setClientCacheMaxAge(actionMapping.clientCacheMaxAge());

		if (actionMapping.allowMethod().length > 0) {
			HttpMethodType[] allowMethods = new HttpMethodType[actionMapping.allowMethod().length];
			System.arraycopy(actionMapping.allowMethod(), 0, allowMethods, 0, allowMethods.length);
			metaActionMapping.setAllowMethod(allowMethods);
		}

		if (actionMapping.allowRequestContentTypes().length > 0) {
			String[] allowRequestContentTypes = new String[actionMapping.allowRequestContentTypes().length];
			System.arraycopy(actionMapping.allowRequestContentTypes(), 0, allowRequestContentTypes, 0, allowRequestContentTypes.length);
			metaActionMapping.setAllowRequestContentTypes(allowRequestContentTypes);
		}
		if (actionMapping.maxRequestBodySize() != Long.MIN_VALUE) {
			metaActionMapping.setMaxRequestBodySize(actionMapping.maxRequestBodySize());
		}
		if (actionMapping.maxFileSize() != Long.MIN_VALUE) {
			metaActionMapping.setMaxFileSize(actionMapping.maxFileSize());
		}
		
		metaActionMapping.setNeedTrustedAuthenticate(actionMapping.needTrustedAuthenticate());
		metaActionMapping.setParts(actionMapping.parts());
		metaActionMapping.setPrivilaged(actionMapping.privilaged());
		metaActionMapping.setPublicAction(actionMapping.publicAction());

		if (actionMapping.paramMapping().length > 0) {
			ParamMap[] paramMap = new ParamMap[actionMapping.paramMapping().length];
			for (int i = 0; i < paramMap.length; i++) {
				ParamMapping anoParamMap = actionMapping.paramMapping()[i];
				paramMap[i] = new ParamMap(anoParamMap.name(), anoParamMap.mapFrom());
				if (!DEFAULT.equals(anoParamMap.condition())) {
					paramMap[i].setCondition(anoParamMap.condition());
				}
			}
			metaActionMapping.setParamMap(paramMap);
		}

		MetaCommandResult res = commandFactory.toMetaCommand(actionMapping.compositeCommand(), annotatedClass);
		if (res == null) {
			res = commandFactory.toMetaCommand(actionMapping.command(), annotatedClass);
		}
		metaActionMapping.setCommand(res.metaCommand);

		Result[] metaResult = new Result[actionMapping.result().length];
		for (int i = 0; i < metaResult.length; i++) {
			metaResult[i] = toMetaResult(actionMapping.name(), actionMapping.result()[i], map, actionMapping.overwritable());
		}
		metaActionMapping.setResult(metaResult);

		//ValidateToken設定(ActionMappingからチェック)
		if (actionMapping.tokenCheck().executeCheck()) {
			MetaTokenCheck tokenCheck = new MetaTokenCheck();
			tokenCheck.setConsume(actionMapping.tokenCheck().consume());
			tokenCheck.setExceptionRollback(actionMapping.tokenCheck().exceptionRollback());
			tokenCheck.setUseFixedToken(actionMapping.tokenCheck().useFixedToken());
			metaActionMapping.setTokenCheck(tokenCheck);
		}

		if (!Type.NO_CACHE.equals(actionMapping.cacheCriteria().type())) {
			MetaCacheCriteria cacheCriteria = toMetaCacheCriteria(actionMapping.cacheCriteria());
			metaActionMapping.setCacheCriteria(cacheCriteria);
		}

		metaActionMapping.setSynchronizeOnSession(actionMapping.synchronizeOnSession());

		map.put(path, new AnnotateMetaDataEntry(metaActionMapping, actionMapping.overwritable(), actionMapping.permissionSharable()));
		return map;
	}

	@Override
	public Map<String, AnnotateMetaDataEntry> toMetaData(Class<Command> annotatedClass) {
		ActionMapping actionMapping = annotatedClass.getAnnotation(ActionMapping.class);
		return toMetaData(actionMapping, annotatedClass);
	}


}
