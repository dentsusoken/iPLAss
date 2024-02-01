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

import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.CommandRuntimeException;
import org.iplass.mtp.command.definition.CommandDefinition;
import org.iplass.mtp.command.definition.JavaClassCommandDefinition;
import org.iplass.mtp.impl.metadata.MetaDataConfig;

public class MetaMetaJavaCommand extends MetaMetaCommand {
	
	private static final long serialVersionUID = 8509052112705251985L;
	
	private String className;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public void applyConfig(CommandDefinition definition) {
		fillFrom(definition);
		JavaClassCommandDefinition def = (JavaClassCommandDefinition) definition;
		className = def.getClassName();
	}

	@Override
	public CommandDefinition currentConfig() {
		JavaClassCommandDefinition definition = new JavaClassCommandDefinition();
		fillTo(definition);
		definition.setClassName(className);
		return definition;
	}
	
	public MetaJavaCommandRuntime createRuntime(MetaDataConfig metaDataConfig) {
		return new MetaJavaCommandRuntime();
	}
	
	public class MetaJavaCommandRuntime extends MetaCommandRuntime {

		Class<Command> clazz;
		
		@SuppressWarnings("unchecked")
		public MetaJavaCommandRuntime() {
			try {
				if (className != null) {
					try {
						Class<?> c = Class.forName(className);
						if (!Command.class.isAssignableFrom(c)) {
							throw new CommandRuntimeException(className + " must implements " + Command.class.getName());
						}
						clazz = (Class<Command>) c;
					} catch (ClassNotFoundException e) {
						throw new CommandRuntimeException(className + " class not found.", e);
					}
				}
			} catch (RuntimeException e) {
				setIllegalStateException(e);
			}
		}
		
		@Override
		public Command newCommand() {
			checkState();
			
			if (clazz != null) {
				try {
					return clazz.newInstance();
				} catch (InstantiationException e) {
					throw new CommandRuntimeException(className + " can not instantiate.", e);
				} catch (IllegalAccessException e) {
					throw new CommandRuntimeException(className + " can not instantiate.", e);
				}
			}
			return null;
		}

		public MetaMetaJavaCommand getMetaData() {
			return MetaMetaJavaCommand.this;
		}
		
	}

}
