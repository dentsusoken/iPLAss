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

package org.iplass.gem.command.generic.detail;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.iplass.gem.command.CommandUtil;
import org.iplass.gem.command.Constants;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.CommandConfig;
import org.iplass.mtp.command.annotation.action.ActionMapping;
import org.iplass.mtp.command.annotation.action.ActionMappings;
import org.iplass.mtp.command.annotation.action.ParamMapping;
import org.iplass.mtp.command.annotation.action.Result;
import org.iplass.mtp.command.annotation.action.Result.Type;
import org.iplass.mtp.entity.DeleteOption;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.EntityValidationException;
import org.iplass.mtp.entity.GenericEntity;
import org.iplass.mtp.entity.UpdateOption;
import org.iplass.mtp.entity.ValidateError;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.entity.definition.properties.ReferenceType;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.EntityViewManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@ActionMappings({
	@ActionMapping(name=UpdateReferencePropertyCommand.ACTION_NAME,
		displayName="参照プロパティ更新",
		command={
			@CommandConfig(commandClass=UpdateReferencePropertyCommand.class),
			@CommandConfig(commandClass=DetailViewCommand.class, value="cmd.detail=false;")
		},
		paramMapping=@ParamMapping(name=Constants.VIEW_NAME, mapFrom="${0}"),
		result={
			@Result(status=Constants.CMD_EXEC_SUCCESS, type=Type.TEMPLATE, value=Constants.TEMPLATE_VIEW),
			@Result(status=Constants.CMD_EXEC_ERROR, type=Type.TEMPLATE, value=Constants.TEMPLATE_VIEW),
			@Result(status=Constants.CMD_EXEC_ERROR_TOKEN, type=Type.TEMPLATE, value=Constants.TEMPLATE_COMMON_ERROR,
					layoutActionName=Constants.LAYOUT_NORMAL_ACTION),
			@Result(status=Constants.CMD_EXEC_ERROR_VIEW, type=Type.TEMPLATE, value=Constants.TEMPLATE_COMMON_ERROR,
					layoutActionName=Constants.LAYOUT_NORMAL_ACTION)
		}
	),
	@ActionMapping(name=UpdateReferencePropertyCommand.REF_ACTION_NAME,
		displayName="参照ダイアログ参照プロパティ更新",
		command={
			@CommandConfig(commandClass=UpdateReferencePropertyCommand.class),
			@CommandConfig(commandClass=DetailViewCommand.class, value="cmd.detail=false;")
		},
		paramMapping=@ParamMapping(name=Constants.VIEW_NAME, mapFrom="${0}"),
		result={
			@Result(status=Constants.CMD_EXEC_SUCCESS, type=Type.TEMPLATE, value=Constants.TEMPLATE_REF_VIEW),
			@Result(status=Constants.CMD_EXEC_ERROR, type=Type.TEMPLATE, value=Constants.TEMPLATE_REF_VIEW),
			@Result(status=Constants.CMD_EXEC_ERROR_TOKEN, type=Type.TEMPLATE, value=Constants.TEMPLATE_COMMON_ERROR,
					layoutActionName=Constants.LAYOUT_POPOUT_ACTION),
			@Result(status=Constants.CMD_EXEC_ERROR_VIEW, type=Type.TEMPLATE, value=Constants.TEMPLATE_COMMON_ERROR,
					layoutActionName=Constants.LAYOUT_POPOUT_ACTION)
		}
	)
})
@CommandClass(name="gem/generic/detail/UpdateReferencePropertyCommand", displayName="参照プロパティ更新")
public final class UpdateReferencePropertyCommand implements Command {

	private static Logger logger = LoggerFactory.getLogger(UpdateReferencePropertyCommand.class);

	public static final String ACTION_NAME = "gem/generic/detail/updateReferenceProperty";
	public static final String REF_ACTION_NAME = "gem/generic/detail/ref/updateReferenceProperty";

	private EntityManager em;
	private EntityDefinitionManager edm;
	private EntityViewManager evm;

	public UpdateReferencePropertyCommand() {
		em = ManagerLocator.getInstance().getManager(EntityManager.class);
		edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
		evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
	}

	@Override
	public String execute(RequestContext request) {
		DetailCommandContext context = new DetailCommandContext(request, em, edm);
		context.setEntityDefinition(edm.get(context.getDefinitionName()));
		context.setEntityView(evm.get(context.getDefinitionName()));

		String oid = request.getParam(Constants.OID);
		String updatePropertyName = request.getParam("updatePropertyName");
		String reloadUrl = request.getParam(Constants.EDITOR_REF_RELOAD_URL);

		if (StringUtil.isNotBlank(reloadUrl)) {
			request.setAttribute(Constants.EDITOR_REF_RELOAD_URL, reloadUrl);
		}

		PropertyDefinition pd = context.getProperty(updatePropertyName);
		if (pd instanceof ReferenceProperty) {
			ReferenceProperty rp = (ReferenceProperty) pd;
			EntityDefinition red = edm.get(rp.getObjectDefinitionName());
			if (rp.getMappedBy() == null || rp.getMappedBy().isEmpty()) {
				//順参照
				return updateEntity(context, oid, red, rp);
			} else {
				//逆参照
				return updateMappedbyEntity(context, oid, rp, red);
			}
		}

		return Constants.CMD_EXEC_SUCCESS;
	}

	/**
	 * 順参照のEntity(親)更新
	 * @param request
	 * @param oid
	 * @param ed
	 * @param red
	 * @param rp
	 * @return
	 */
	private String updateEntity(DetailCommandContext context, String oid, EntityDefinition red, ReferenceProperty rp) {
		//被参照じゃない場合は自身の参照プロパティだけを更新

		Long version = context.getLongValue(Constants.VERSION);
		Long updateDate = context.getLongValue(Constants.TIMESTAMP);
		Entity entity = createEntity(context.getEntityDefinition(), oid, version, updateDate);

		//値取得
		String[] values = context.getParams(rp.getName());
		List<Entity> list = new ArrayList<Entity>();
		if (values != null) {
			for (String value : values) {
				String roid = null;
				Long rversion = null;
				int lastIndex = value.lastIndexOf("_");

				if (lastIndex < 0) {
					roid = value;
				} else {
					roid = value.substring(0, lastIndex);
					rversion = CommandUtil.getLong(value.substring(lastIndex + 1));
				}
				Entity refEntity = createEntity(red, roid, rversion, null);
				list.add(refEntity);
			}
		}

		if (rp.getMultiplicity() == 1) {
			entity.setValue(rp.getName(), list.size() > 0 ? list.get(0) : null);
		} else {
			entity.setValue(rp.getName(), list.toArray(new Entity[list.size()]));
		}
		UpdateOption option = new UpdateOption(true);
		option.setUpdateProperties(rp.getName());
		option.setPurgeCompositionedEntity(context.getView().isPurgeCompositionedEntity());
		try {
			em.update(entity, option);
		} catch (EntityValidationException e) {
			if (logger.isDebugEnabled()) {
				logger.debug(e.getMessage(), e);
			}
			List<ValidateError> errors = new ArrayList<ValidateError>();
			errors.addAll(e.getValidateResults());
			context.setAttribute(Constants.ERROR_PROP, errors);
			return Constants.CMD_EXEC_ERROR;
		}

		return Constants.CMD_EXEC_SUCCESS;
	}

	/**
	 * 逆参照のEntity(子)更新
	 * @param request
	 * @param oid
	 * @param rp
	 * @param red
	 * @return
	 */
	private String updateMappedbyEntity(DetailCommandContext context, String oid, ReferenceProperty rp, EntityDefinition red) {
		//逆参照の場合は子を更新
		//ただし親画面からは子を消すパターンしかない(参照を追加するのは子の更新画面から)

		String refEntityKey = context.getParam("refEntityKey");
		if (refEntityKey != null && !refEntityKey.isEmpty()) {
			String roid = null;
			Long rversion = null;
			int lastIndex = refEntityKey.lastIndexOf("_");

			if (lastIndex < 0) {
				roid = refEntityKey;
			} else {
				roid = refEntityKey.substring(0, lastIndex);
				rversion = CommandUtil.getLong(refEntityKey.substring(lastIndex + 1));
			}
			Entity refEntity = em.load(roid, rversion, rp.getObjectDefinitionName());
			if (rp.getReferenceType() == ReferenceType.ASSOCIATION) {
				//通常参照
				ReferenceProperty rrp = (ReferenceProperty) red.getProperty(rp.getMappedBy());
				if (rrp.getMultiplicity() == 1) {
					Entity entity = refEntity.getValue(rrp.getName());
					if (entity != null && entity.getOid().equals(oid)) {
						refEntity.setValue(rrp.getName(), null);
					}
				} else {
					Entity[] array = refEntity.getValue(rrp.getName());
					if (array != null) {
						List<Entity> entityList = new ArrayList<Entity>();
						for (Entity entity : array) {
							if (entity != null && !entity.getOid().equals(oid)) {
								entityList.add(entity);
							}
						}
						refEntity.setValue(rrp.getName(), entityList.toArray(new Entity[entityList.size()]));
					}
				}
				UpdateOption option = new UpdateOption(true);
				option.setUpdateProperties(rrp.getName());
				option.setPurgeCompositionedEntity(context.getView().isPurgeCompositionedEntity());
				try {
					em.update(refEntity, option);
				} catch (EntityValidationException e) {
					if (logger.isDebugEnabled()) {
						logger.debug(e.getMessage(), e);
					}
					List<ValidateError> errors = new ArrayList<ValidateError>();
					errors.addAll(e.getValidateResults());
					context.setAttribute(Constants.ERROR_PROP, errors);
					return Constants.CMD_EXEC_ERROR;
				}
			} else {
				//親子関係
				//親子の場合、親が複数になることはない
				//また親から削除された時点で子は削除対処となる
				DeleteOption option = new DeleteOption(false);
				option.setPurge(context.getView().isPurgeCompositionedEntity());
				em.delete(refEntity, option);
			}
		}

		return Constants.CMD_EXEC_SUCCESS;
	}

	private Entity createEntity(EntityDefinition ed, String oid, Long version, Long updateDate) {
		Entity entity = null;
		if (ed.getMapping() != null && ed.getMapping().getMappingModelClass() != null) {
			try {
				entity = (Entity) Class.forName(ed.getMapping().getMappingModelClass()).newInstance();
			} catch (InstantiationException e) {
				throw new EntityRuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new EntityRuntimeException(e);
			} catch (ClassNotFoundException e) {
				throw new EntityRuntimeException(e);
			}
		} else {
			entity = new GenericEntity();
		}

		entity.setDefinitionName(ed.getName());
		entity.setOid(oid);
		entity.setVersion(version);
		if (updateDate != null) {
			entity.setUpdateDate(new Timestamp(updateDate));
		}

		return entity;
	}

}
