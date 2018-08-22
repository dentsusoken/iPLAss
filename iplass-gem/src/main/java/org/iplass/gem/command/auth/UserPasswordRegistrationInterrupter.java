/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.gem.command.auth;

import java.util.Collections;
import java.util.List;

import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.ValidateError;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.DetailFormView;
import org.iplass.mtp.view.generic.RegistrationInterrupter;

public class UserPasswordRegistrationInterrupter implements RegistrationInterrupter {

	@Override
	public void dataMapping(Entity entity, RequestContext request,
			EntityDefinition definition, DetailFormView view) {
	}

	@Override
	public boolean isSpecifyAllProperties() {
		return false;
	}

	@Override
	public String[] getAdditionalProperties() {
		return null;
	}

	@Override
	public List<ValidateError> beforeRegist(Entity entity,
			RequestContext request, EntityDefinition definition,
			DetailFormView view, RegistrationType registrationType) {

		if (StringUtil.isNotEmpty(request.getParam("password"))) {
			entity.setValue("password", request.getParam("password"));
		}
		return Collections.emptyList();
	}

	@Override
	public List<ValidateError> afterRegist(Entity entity,
			RequestContext request, EntityDefinition definition,
			DetailFormView view, RegistrationType registType) {
		return Collections.emptyList();
	}

}
