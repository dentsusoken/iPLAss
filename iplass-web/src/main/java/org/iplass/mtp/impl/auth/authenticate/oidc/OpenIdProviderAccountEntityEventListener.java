/*
 * Copyright (C) 2022 DENTSU SOKEN INC. All Rights Reserved.
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


package org.iplass.mtp.impl.auth.authenticate.oidc;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityEventContext;
import org.iplass.mtp.entity.EntityEventListener;
import org.iplass.mtp.entity.UpdateOption;

public class OpenIdProviderAccountEntityEventListener implements EntityEventListener {
	public static final String DEFINITION_NAME = "mtp.auth.oidc.OpenIdProviderAccount";
	
	/** subjectId + "@" + openIdConnectDefinitionName*/
	public static final String UNIQUE_KEY = "uniqueKey";
	
	public static final String OIDC_DEFINITION_NAME = "openIdConnectDefinitionName";
	public static final String SUBJECT_ID = "subjectId";
	public static final String SUBJECT_NAME = "subjectName";
	public static final String USER = "user";
	public static final String USER_OID = "userOid";

	@Override
	public void beforeValidate(Entity entity, EntityEventContext context) {
		UpdateOption option = (UpdateOption) context.getAttribute(EntityEventContext.UPDATE_OPTION);
		
		if (option == null) {
			//insert
			entity.setValue(UNIQUE_KEY, OIDCAccountHandle.createSubjectUniqueKey(entity.getValue(SUBJECT_ID), entity.getValue(OIDC_DEFINITION_NAME)));
			entity.setValue(USER_OID, ((Entity) entity.getValue(USER)).getOid());
		}
	}
	
	@Override
	public boolean beforeUpdate(Entity entity, EntityEventContext context) {
		UpdateOption option = (UpdateOption) context.getAttribute(EntityEventContext.UPDATE_OPTION);
		Entity before = (Entity) context.getAttribute(EntityEventContext.BEFORE_UPDATE_ENTITY);
		if (option.getUpdateProperties().contains(OIDC_DEFINITION_NAME)
				|| option.getUpdateProperties().contains(SUBJECT_ID)) {
			
			String sub = before.getValue(SUBJECT_ID);
			if (option.getUpdateProperties().contains(SUBJECT_ID)) {
				sub = entity.getValue(SUBJECT_ID);
			}
			String providerName = before.getValue(OIDC_DEFINITION_NAME);
			if (option.getUpdateProperties().contains(OIDC_DEFINITION_NAME)) {
				providerName = entity.getValue(OIDC_DEFINITION_NAME);
			}
			entity.setValue(UNIQUE_KEY, OIDCAccountHandle.createSubjectUniqueKey(sub, providerName));
			
			if (!option.getUpdateProperties().contains(UNIQUE_KEY)) {
				option.getUpdateProperties().add(UNIQUE_KEY);
			}
		}
		if (option.getUpdateProperties().contains(USER)) {
			entity.setValue(USER_OID, ((Entity) entity.getValue(USER)).getOid());
			if (!option.getUpdateProperties().contains(USER_OID)) {
				option.getUpdateProperties().add(USER_OID);
			}
		}
		
		return true;
	}
}
