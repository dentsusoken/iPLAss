/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.command;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.definition.CommandDefinition;
import org.iplass.mtp.command.definition.CommandDefinitionManager;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.impl.command.MetaMetaCommand.MetaCommandRuntime;
import org.iplass.mtp.impl.definition.AbstractTypedMetaDataService;
import org.iplass.mtp.impl.definition.DefinitionMetaDataTypeMap;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;


public class CommandService extends AbstractTypedMetaDataService<MetaMetaCommand, MetaCommandRuntime> implements Service {
	public static final String COMMAND_META_PATH = "/command/";
	public static final String COMMAND_META_META_PATH = "/commandClass/";

	public static class TypeMap extends DefinitionMetaDataTypeMap<CommandDefinition, MetaMetaCommand> {
		public TypeMap() {
			super(getFixedPath(), MetaMetaCommand.class, CommandDefinition.class);
		}
		@Override
		public TypedDefinitionManager<CommandDefinition> typedDefinitionManager() {
			return ManagerLocator.getInstance().getManager(CommandDefinitionManager.class);
		}
	}

	public void destroy() {
	}

	public void init(Config config) {
	}

	public static String getFixedPath() {
		return COMMAND_META_META_PATH;
	}

	@Override
	public Class<MetaMetaCommand> getMetaDataType() {
		return MetaMetaCommand.class;
	}

	@Override
	public Class<MetaCommandRuntime> getRuntimeType() {
		return MetaCommandRuntime.class;
	}

}
