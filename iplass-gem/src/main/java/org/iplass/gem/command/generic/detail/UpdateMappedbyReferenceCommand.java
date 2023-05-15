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

package org.iplass.gem.command.generic.detail;

import java.util.ArrayList;
import java.util.List;

import org.iplass.gem.command.CommandUtil;
import org.iplass.gem.command.Constants;
import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.RestJson;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.command.annotation.webapi.WebApiTokenCheck;
import org.iplass.mtp.entity.DeleteOption;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.EntityValidationException;
import org.iplass.mtp.entity.UpdateOption;
import org.iplass.mtp.entity.ValidateError;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.entity.definition.properties.ReferenceType;
import org.iplass.mtp.impl.web.token.TokenStore;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.web.template.TemplateUtil;
import org.iplass.mtp.web.template.TemplateUtil.TokenOutputType;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.RequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@WebApi(
		name=UpdateMappedbyReferenceCommand.WEBAPI_NAME,
		displayName="被参照プロパティ更新",
		accepts=RequestType.REST_JSON,
		methods=MethodType.POST,
		restJson=@RestJson(parameterName="param"),
		results={Constants.ERROR_PROP, TokenStore.TOKEN_PARAM_NAME},
		tokenCheck=@WebApiTokenCheck(consume=true, useFixedToken=false),
		checkXRequestedWithHeader=true
	)
@CommandClass(name="gem/generic/detail/UpdateMappedbyReferenceCommand", displayName="被参照プロパティ更新")
public final class UpdateMappedbyReferenceCommand implements Command {

	private static Logger logger = LoggerFactory.getLogger(UpdateMappedbyReferenceCommand.class);

	public static final String WEBAPI_NAME = "gem/generic/detail/UpdateMappedbyReference";

	private EntityManager em;
	private EntityDefinitionManager edm;

	public UpdateMappedbyReferenceCommand() {
		em = ManagerLocator.getInstance().getManager(EntityManager.class);
		edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
	}

	@Override
	public String execute(RequestContext request) {

		// 参照元Entity定義名
		String defName = request.getParam(Constants.DEF_NAME);
		// 参照元OID
		String oid = request.getParam(Constants.OID);
		// 更新対象プロパティ（被参照プロパティ）
		String updatePropertyName = request.getParam("updatePropertyName");
		// 削除対象件数
		String delCountStr = request.getParam("delCount");
		// 物理削除（親子関係の場合）
		// FIXME DetailView定義から取得する
		boolean purge = Boolean.parseBoolean(request.getParam("purge"));

		int delCount = 0;
		try {
			delCount = Integer.parseInt(delCountStr);
		} catch (NumberFormatException e) {
		}

		EntityDefinition ed = edm.get(defName);
		PropertyDefinition pd = ed.getProperty(updatePropertyName);
		if (pd instanceof ReferenceProperty) {
			ReferenceProperty rp = (ReferenceProperty) pd;
			if (StringUtil.isNotEmpty(rp.getMappedBy())) {
				for (int i = 0; i < delCount; i++) {
					// 削除対象のoid、version取得
					String refEntityKey = request.getParam("refEntityKey_" + i);

					if (StringUtil.isBlank(refEntityKey)) {
						continue;
					}

					Entity refEntity = loadReference(refEntityKey, rp);
					if (refEntity == null) {
						// 既に削除されているのでスキップ
						continue;
					}

					try {
						if (rp.getReferenceType() == ReferenceType.ASSOCIATION) {
							// 被参照の削除の場合は相手を更新
							EntityDefinition red = edm.get(rp.getObjectDefinitionName());
							update(oid, refEntity, rp, red);
						} else if (rp.getReferenceType() == ReferenceType.COMPOSITION) {
							// 親子の場合は削除
							delete(refEntity, purge);
						}
					} catch (EntityValidationException e) {
						if (logger.isDebugEnabled()) {
							logger.debug(e.getMessage(), e);
						}
						List<ValidateError> errors = new ArrayList<ValidateError>();
						errors.addAll(e.getValidateResults());
						request.setAttribute(Constants.ERROR_PROP, errors);
						return Constants.CMD_EXEC_ERROR;
					} catch (ApplicationException e) {
						if (logger.isDebugEnabled()) {
							logger.debug(e.getMessage(), e);
						}
						List<String> errors = new ArrayList<>();
						errors.add(e.getMessage());
						request.setAttribute(Constants.ERROR_PROP, errors);
						return Constants.CMD_EXEC_ERROR;
					}
				}
			}
		}

		// ワンタイムトークンを新規生成して返却値にセット
		String token = TemplateUtil.outputToken(TokenOutputType.VALUE);
		request.setAttribute(TokenStore.TOKEN_PARAM_NAME, token);

		return Constants.CMD_EXEC_SUCCESS;
	}

	private void update(String oid, Entity refEntity, ReferenceProperty rp, EntityDefinition red) {
		boolean needUpdate = false;
		ReferenceProperty rrp = (ReferenceProperty) red.getProperty(rp.getMappedBy());
		if (rrp.getMultiplicity() == 1) {
			Entity entity = refEntity.getValue(rrp.getName());
			if (entity != null && entity.getOid().equals(oid)) {
				needUpdate = true;
				refEntity.setValue(rrp.getName(), null);
			}
		} else {
			Entity[] array = refEntity.getValue(rrp.getName());
			if (array != null) {
				List<Entity> entityList = new ArrayList<Entity>();
				for (Entity entity : array) {
					if (entity.getOid().equals(oid)) {
						// 除外
						needUpdate = true;
					} else {
						entityList.add(entity);
					}
				}
				refEntity.setValue(rrp.getName(), entityList.toArray(new Entity[entityList.size()]));
			}
		}
		if (needUpdate) {
			UpdateOption option = new UpdateOption(true);
			option.setUpdateProperties(rrp.getName());
			em.update(refEntity, option);
		} else {
			logger.info("mapped by reference delete target data has been updated. reference entity="
					+ rp.getObjectDefinitionName() + ", property=" + rrp.getName()
					+ ", oid=" + refEntity.getOid() + ", version=" + refEntity.getVersion()
					+ ", reference oid=" + oid);
		}
	}

	private void delete(Entity refEntity, boolean purge) {
		DeleteOption option = new DeleteOption();
		option.setCheckTimestamp(false);
		option.setPurge(purge);
		em.delete(refEntity, option);
	}

	private Entity loadReference(String refEntityKey, ReferenceProperty rp) {
		int lastIndex = refEntityKey.lastIndexOf("_");
		String oid = refEntityKey.substring(0, lastIndex);
		Long version = CommandUtil.getLong(refEntityKey.substring(lastIndex + 1));
		Entity refEntity = em.load(oid, version, rp.getObjectDefinitionName());
		if (refEntity == null) {
			logger.info("mapped by reference delete target data has been deleted. reference entity="
					+ rp.getObjectDefinitionName() + ", oid=" + oid + ", version=" + version);
		}
		return refEntity;
	}

}
