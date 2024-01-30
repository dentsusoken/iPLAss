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
package org.iplass.mtp.impl.webapi.command.entity;

import java.sql.Timestamp;
import java.util.Arrays;

import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.RestJson;
import org.iplass.mtp.command.annotation.webapi.RestXml;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.GenericEntity;
import org.iplass.mtp.entity.TargetVersion;
import org.iplass.mtp.entity.UpdateOption;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.webapi.EntityWebApiService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.RequestType;

@WebApi(name="mtp/entity/PUT",
		accepts={RequestType.REST_JSON, RequestType.REST_XML},
		methods={MethodType.PUT},
		restJson=@RestJson(parameterName=UpdateEntityCommand.PARAM_ENTITY, parameterType=Entity.class),
		restXml=@RestXml(parameterName=UpdateEntityCommand.PARAM_ENTITY),
		results={UpdateEntityCommand.RESULT_OID, UpdateEntityCommand.RESULT_OID_LIST},
		supportBearerToken = true,
		overwritable=false)
@CommandClass(name="mtp/entity/UpdateEntityCommand", displayName="Entity Update Web API", overwritable=false)
public final class UpdateEntityCommand extends AbstractEntityCommand {

	//entity/[defName]/[oid]
	//entity/[defName]/[oid]/[version]

	public static final String PARAM_ENTITY = "entity";

	// PUTのオプションパラメータ
	public static final String PARAM_PUT_WITH_VALIDATION = "withValidation";
	public static final String PARAM_PUT_NOTIFY_LISTENERS = "notifyListeners";
	public static final String PARAM_PUT_LOCALIZED = "localized";
	public static final String PARAM_PUT_FORCE_UPDATE = "forceUpdate";
	public static final String PARAM_PUT_UPDATE_PROPERTIES = "updateProperties";
	public static final String PARAM_PUT_CHECK_TIMESTAMP = "checkTimestamp";
	public static final String PARAM_PUT_TARGET_VERSION = "targetVersion";
	public static final String PARAM_PUT_PURGE_COMPOSITIONED_ENTITY = "purgeCompositionedEntity";
	public static final String PARAM_PUT_CHECK_LOCKED_BY_USER = "checkLockedByUser";

	public static final String RESULT_OID_LIST = "oids";
	public static final String RESULT_OID = "oid";
	public static final String RESULT_VERSION = "version";

	public static final long VERSION_NEW = -1;

	private void doUpdate(RequestContext request, String defName, String oid, String ver) {

		Entity e = (Entity) request.getAttribute(PARAM_ENTITY);
		e.setDefinitionName(defName);
		e.setOid(oid);

		UpdateOption option = null;
		if (e instanceof GenericEntity) {
			option = new UpdateOption(false);
			for (String pn: ((GenericEntity) e).getPropertyNames()) {
				if (!pn.equals(Entity.OID)
						&& !pn.equals(Entity.UPDATE_DATE)) {
					option.add(pn);
				}
			}
		} else {
			option = UpdateOption.allPropertyUpdateOption(defName, false);
		}
		Timestamp updateDate = e.getUpdateDate();
		if (updateDate != null) {
			option.setCheckTimestamp(true);
		}
		setUpdateOptionWithParam(request, option);

		EntityHandler eh = EntityContext.getCurrentContext().getHandlerByName(defName);
		if (ver != null && eh.isVersioned()) {
			long version = Long.parseLong(ver);
			if (version == VERSION_NEW) {
				option.setTargetVersion(TargetVersion.NEW);
			} else {
				option.setTargetVersion(TargetVersion.SPECIFIC);
				e.setVersion(version);
			}
		}

		em.update(e, option);

		request.setAttribute(RESULT_OID, oid);
		if (eh.isVersioned()) {
			request.setAttribute(RESULT_VERSION, e.getVersion());
		}
	}

	/**
	 * リクエストパラメータに設定されたオプションをUpdateOptionに設定
	 *
	 * @param request リクエスト
	 * @param option UpdateOption
	 */
	private void setUpdateOptionWithParam(RequestContext request, UpdateOption option) {
		EntityWebApiService service = ServiceRegistry.getRegistry().getService(EntityWebApiService.class);

		if (service.isPermitRolesToSpecifyOptions()) {
			String withValidation = request.getParam(PARAM_PUT_WITH_VALIDATION);
			if (StringUtil.isNotBlank(withValidation)) {
				option.setWithValidation(Boolean.parseBoolean(withValidation));
			}
			String notifyListeners = request.getParam(PARAM_PUT_NOTIFY_LISTENERS);
			if (StringUtil.isNotBlank(notifyListeners)) {
				option.setNotifyListeners(Boolean.parseBoolean(notifyListeners));
			}
			String localized = request.getParam(PARAM_PUT_LOCALIZED);
			if (StringUtil.isNotBlank(localized)) {
				option.setLocalized(Boolean.parseBoolean(localized));
			}
			String forceUpdate = request.getParam(PARAM_PUT_FORCE_UPDATE);
			if (StringUtil.isNotBlank(forceUpdate)) {
				option.setForceUpdate(Boolean.parseBoolean(forceUpdate));
			}
			String updateProperties = request.getParam(PARAM_PUT_UPDATE_PROPERTIES);
			if (StringUtil.isNotBlank(updateProperties)) {
				//デフォルトで設定した更新対象を上書きする
				option.setUpdateProperties(Arrays.asList(updateProperties.split(",")));
			}
			String checkTimestamp = request.getParam(PARAM_PUT_CHECK_TIMESTAMP);
			if (StringUtil.isNotBlank(checkTimestamp)) {
				option.setCheckTimestamp(Boolean.parseBoolean(checkTimestamp));
			}
			String targetVersion = request.getParam(PARAM_PUT_TARGET_VERSION);
			if (StringUtil.isNotBlank(targetVersion)) {
				try {
					option.setTargetVersion(TargetVersion.valueOf(targetVersion));
				} catch (Exception e) {
					throw new IllegalArgumentException("targetVersion is invalid.");
				}
			}
			String purgeCompositionedEntity = request.getParam(PARAM_PUT_PURGE_COMPOSITIONED_ENTITY);
			if (StringUtil.isNotBlank(purgeCompositionedEntity)) {
				option.setPurgeCompositionedEntity(Boolean.parseBoolean(purgeCompositionedEntity));
			}
			String checkLockedByUser = request.getParam(PARAM_PUT_CHECK_LOCKED_BY_USER);
			if (StringUtil.isNotBlank(checkLockedByUser)) {
				option.setCheckLockedByUser(Boolean.parseBoolean(checkLockedByUser));
			}
		}
	}

	@Override
	public String executeImpl(RequestContext request, String[] subPath) {
		if (subPath == null || subPath.length < 2 || subPath.length > 3) {
			throw new IllegalArgumentException("illegal path parameter:" + subPath);
		}
		checkPermission(subPath[0], def -> def.getMetaData().isUpdate());

		switch (subPath.length) {
		case 2:
			doUpdate(request, subPath[0], subPath[1], null);
			break;
		case 3:
			doUpdate(request, subPath[0], subPath[1], subPath[2]);
			break;
		default:
			break;
		}

		return CMD_EXEC_SUCCESS;
	}
}
