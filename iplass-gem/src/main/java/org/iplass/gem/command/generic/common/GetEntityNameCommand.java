/*
 * Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.gem.command.generic.common;

import org.iplass.gem.command.Constants;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.RestJson;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.condition.expr.And;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.webapi.definition.RequestType;
import org.iplass.mtp.webapi.definition.MethodType;

/**
 * プロパティ値取得コマンド
 * @author lis3wg
 */
@WebApi(name=GetEntityNameCommand.WEBAPI_NAME,
	accepts=RequestType.REST_JSON,
	methods=MethodType.POST,
	restJson=@RestJson(parameterName="param"),
	results={"value"},
	checkXRequestedWithHeader=true
)
@CommandClass(name="gem/generic/common/GetEntityNameCommand", displayName="Entity名取得")
public final class GetEntityNameCommand implements Command {

	public static final String WEBAPI_NAME = "gem/generic/common/getEntityName";

	@Override
	public String execute(RequestContext request) {

		final String defName = request.getParam(Constants.DEF_NAME);
		final String oid = request.getParam(Constants.OID);
		final String version = request.getParam(Constants.VERSION);

		Object ret = null;
		if (defName != null && !defName.isEmpty()
				&& oid != null && !oid.isEmpty()
				&& version != null && !version.isEmpty()) {
			Query query = new Query();
			query.select(Entity.NAME).from(defName)
				.where(new And(new Equals(Entity.OID, oid), new Equals(Entity.VERSION, version)));

			EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);
			Entity entity = em.searchEntity(query).getFirst();
			ret = entity != null ? entity.getName() : null;
		}
		request.setAttribute("value", ret);
		return "OK";
	}
}
