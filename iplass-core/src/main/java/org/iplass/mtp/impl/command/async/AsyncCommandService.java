/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.command.async;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.async.definition.AsyncCommandDefinition;
import org.iplass.mtp.command.async.definition.AsyncCommandDefinitionManager;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.impl.command.async.MetaAsyncCommand.AsyncCommandRuntime;
import org.iplass.mtp.impl.definition.AbstractTypedMetaDataService;
import org.iplass.mtp.impl.definition.DefinitionMetaDataTypeMap;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;

public class AsyncCommandService extends AbstractTypedMetaDataService<MetaAsyncCommand, AsyncCommandRuntime> implements Service {
	public static final String COMMAND_ASYNC_META_PATH = "/command/async/";

	public static class TypeMap extends DefinitionMetaDataTypeMap<AsyncCommandDefinition, MetaAsyncCommand> {
		public TypeMap() {
			super(getFixedPath(), MetaAsyncCommand.class, AsyncCommandDefinition.class);
		}
		@Override
		public TypedDefinitionManager<AsyncCommandDefinition> typedDefinitionManager() {
			return ManagerLocator.getInstance().getManager(AsyncCommandDefinitionManager.class);
		}
	}

	@Override
	public void init(Config config) {
	}

	@Override
	public void destroy() {
	}

	public static String getFixedPath() {
		return COMMAND_ASYNC_META_PATH;
	}

	@Override
	public Class<MetaAsyncCommand> getMetaDataType() {
		return MetaAsyncCommand.class;
	}

	@Override
	public Class<AsyncCommandRuntime> getRuntimeType() {
		return AsyncCommandRuntime.class;
	}


}
