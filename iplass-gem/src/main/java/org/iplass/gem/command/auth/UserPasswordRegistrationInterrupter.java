/*
 * Copyright (C) 2015 DENTSU SOKEN INC. All Rights Reserved.
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

import org.iplass.mtp.auth.User;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.ValidateError;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.FormView;
import org.iplass.mtp.view.generic.RegistrationInterrupter;

public class UserPasswordRegistrationInterrupter implements RegistrationInterrupter {

	@Override
	public List<ValidateError> beforeRegister(Entity entity, RequestContext request, EntityDefinition definition,
			FormView view, RegistrationType registrationType) {

		if (StringUtil.isNotEmpty(request.getParam(AuthCommandConstants.PARAM_PASSWORD))) {
			entity.setValue(User.PASSWORD, request.getParam(AuthCommandConstants.PARAM_PASSWORD));
		}
		return Collections.emptyList();
	}

}
