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

package org.iplass.gem.command.auth;

import java.util.Collections;
import java.util.List;

import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.ValidateError;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.view.generic.FormView;
import org.iplass.mtp.view.generic.RegistrationInterrupter;

public class UserRegistrationInterrupter implements RegistrationInterrupter {

	@Override
	public void dataMapping(Entity entity, RequestContext request,
			EntityDefinition definition, FormView view) {
	}

	@Override
	public boolean isSpecifyAllProperties() {
		return false;
	}

	@Override
	public String[] getAdditionalProperties() {
		return new String[]{"name"};
	}

	@Override
	public List<ValidateError> beforeRegist(Entity entity,
			RequestContext request, EntityDefinition definition,
			FormView view, RegistrationType registrationType) {
		return Collections.emptyList();
	}

	@Override
	public List<ValidateError> afterRegist(Entity entity,
			RequestContext request, EntityDefinition definition,
			FormView view, RegistrationType registType) {
		return Collections.emptyList();
	}

}
