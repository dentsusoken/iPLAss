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

import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.entity.DeleteOption;
import org.iplass.mtp.entity.GenericEntity;
import org.iplass.mtp.webapi.definition.RequestType;
import org.iplass.mtp.webapi.definition.MethodType;

@WebApi(name="mtp/entity/DELETE",
		accepts={RequestType.REST_FORM},
		methods={MethodType.DELETE},
		overwritable=false)
@CommandClass(name="mtp/entity/DeleteEntityCommand", displayName="Entity Delete Web API", overwritable=false)
public final class DeleteEntityCommand extends AbstractEntityCommand {
	
	public static final String PARAM_TIMESTAMP = "timestamp";
	public static final String RESULT_OID = "oid";
	
	//entity/[defName]/[oid]?timestamp=[epochTime]
	@Override
	public String executeImpl(RequestContext request, String[] subPath) {
		if (subPath == null || subPath.length != 2) {
			throw new IllegalArgumentException("illegal path parameter:" + subPath);
		}
		checkPermission(subPath[0], def -> def.getMetaData().isDelete());
		
		GenericEntity e = new GenericEntity(subPath[0]);
		e.setOid(subPath[1]);
		DeleteOption option = new DeleteOption();
		String tsStr = request.getParam(PARAM_TIMESTAMP);
		if (tsStr != null) {
			e.setUpdateDate(new Timestamp(Long.parseLong(tsStr)));
			option.setCheckTimestamp(true);
		}
		
		em.delete(e, option);
		return CMD_EXEC_SUCCESS;
	}
}
