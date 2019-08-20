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

package org.iplass.gem.command.generic.bulk;

import org.iplass.gem.command.GemResourceBundleUtil;
import org.iplass.gem.command.generic.detail.RegistrationCommandBase;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.view.generic.element.property.PropertyColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BulkCommandBase extends RegistrationCommandBase<BulkCommandContext, PropertyColumn> {

	private static Logger logger = LoggerFactory.getLogger(BulkCommandBase.class);

	@Override
	protected Logger getLogger() {
		return logger;
	}

	@Override
	protected BulkCommandContext getContext(RequestContext request) {
		BulkCommandContext context = new BulkCommandContext(request, em, edm);
		context.setEntityDefinition(edm.get(context.getDefinitionName()));
		context.setEntityView(evm.get(context.getDefinitionName()));

		return context;
	}

	protected String resourceString(String key, Object... arguments) {
		return GemResourceBundleUtil.resourceString(key, arguments);
	}
}
