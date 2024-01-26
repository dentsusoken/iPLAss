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

package org.iplass.gem.command.generic.detail;

import org.iplass.gem.command.Constants;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.RestJson;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.command.annotation.webapi.WebApiTokenCheck;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.UpdateOption;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.impl.util.ConvertUtil;
import org.iplass.mtp.impl.web.token.TokenStore;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.RequestType;

@WebApi(
		name=UpdateTableOrderCommand.WEBAPI_NAME,
		displayName="テーブル表示順更新",
		accepts=RequestType.REST_JSON,
		methods=MethodType.POST,
		restJson=@RestJson(parameterName="param"),
		results={Constants.ERROR_PROP, TokenStore.TOKEN_PARAM_NAME},
		tokenCheck=@WebApiTokenCheck(consume=true, useFixedToken=false),
		checkXRequestedWithHeader=true
	)
@CommandClass(name="gem/generic/detail/UpdateTableOrderCommand", displayName="テーブル表示順更新")
public final class UpdateTableOrderCommand implements Command {

	public static final String WEBAPI_NAME = "gem/generic/detail/UpdateTableOrder";

	private EntityDefinitionManager edm;
	private EntityManager em;

	public UpdateTableOrderCommand() {
		edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
		em = ManagerLocator.getInstance().getManager(EntityManager.class);
	}

	@Override
	public String execute(RequestContext request) {
		String defName = request.getParam("refDefName");
		String targetKey = request.getParam("targetKey");
		String shiftKey = request.getParam("shiftKey");
		String orderPropName = request.getParam("orderPropName");
		boolean shiftUp = request.getParamAsBoolean("shiftUp");
		Long orderPropValue = request.getParamAsLong("orderPropValue");

		//移動対象取得
		String[] targetParam = targetKey.split("_");
		String targetOid = targetParam[0];
		Long targetVersion = ConvertUtil.convert(Long.class, targetParam[1]);
		Entity target = em.load(targetOid, targetVersion, defName);

		//移動先取得
		String[] shiftParam = shiftKey.split("_");
		String shiftOid = shiftParam[0];
		Long shiftVersion = ConvertUtil.convert(Long.class, shiftParam[1]);
		Entity shift = em.load(shiftOid, shiftVersion, defName);

		if (target != null && shift != null) {
			if (shiftUp) {
				shiftToUp(target, shift, orderPropName);
			} else {
				shiftToDown(target, shift, orderPropName);
			}

			UpdateOption option = new UpdateOption();
			option.add(orderPropName);
			em.update(target, option);
			em.update(shift, option);
		} else if (target != null && orderPropValue != null) {
			target.setValue(orderPropName, orderPropValue);
			UpdateOption option = new UpdateOption();
			option.add(orderPropName);
			em.update(target, option);
		}

		return null;
	}

	/**
	 * 対象を上の行と入れ替え
	 * @param target
	 * @param shift
	 * @param orderPropName
	 */
	private void shiftToUp(Entity target, Entity shift, String orderPropName) {
		Object targetOrderValue = target.getValue(orderPropName);
		Object shiftOrderValue = shift.getValue(orderPropName);

		EntityDefinition ed = edm.get(target.getDefinitionName());
		PropertyDefinition pd = ed.getProperty(orderPropName);

		//入れ替え元と対象が同値の可能性があるので、どちらかを基準に+-1する
		if (targetOrderValue != null) {
			//入れ替え元を基準に-1
			Integer order = ConvertUtil.convert(Integer.class, targetOrderValue);
			target.setValue(orderPropName, ConvertUtil.convert(pd.getJavaType(), order - 1));
			shift.setValue(orderPropName, targetOrderValue);
		} else if (shiftOrderValue != null) {
			//入れ替え先を基準に+1
			Integer order = ConvertUtil.convert(Integer.class, shiftOrderValue);
			target.setValue(orderPropName, shiftOrderValue);
			shift.setValue(orderPropName, ConvertUtil.convert(pd.getJavaType(), order + 1));
		} else {
			//どちらもなし、0/1で
			target.setValue(orderPropName, ConvertUtil.convert(pd.getJavaType(), 0));
			shift.setValue(orderPropName, ConvertUtil.convert(pd.getJavaType(), 1));
		}
	}

	/**
	 * 対象を下の行と入れ替え
	 * @param target
	 * @param shift
	 * @param orderPropName
	 */
	private void shiftToDown(Entity target, Entity shift, String orderPropName) {
		Object targetOrderValue = target.getValue(orderPropName);
		Object shiftOrderValue = shift.getValue(orderPropName);

		EntityDefinition ed = edm.get(target.getDefinitionName());
		PropertyDefinition pd = ed.getProperty(orderPropName);

		//入れ替え元と対象が同値の可能性があるので、どちらかを基準に+-1する
		if (targetOrderValue != null) {
			//入れ替え元を基準に+1
			Integer order = ConvertUtil.convert(Integer.class, targetOrderValue);
			target.setValue(orderPropName, ConvertUtil.convert(pd.getJavaType(), order + 1));
			shift.setValue(orderPropName, targetOrderValue);
		} else if (shiftOrderValue != null)  {
			//入れ替え先を基準に-1
			Integer order = ConvertUtil.convert(Integer.class, shiftOrderValue);
			target.setValue(orderPropName, shiftOrderValue);
			shift.setValue(orderPropName, ConvertUtil.convert(pd.getJavaType(), order - 1));
		} else {
			//どちらもなし、0/1で
			target.setValue(orderPropName, ConvertUtil.convert(pd.getJavaType(), 1));
			shift.setValue(orderPropName, ConvertUtil.convert(pd.getJavaType(), 0));
		}
	}

}
