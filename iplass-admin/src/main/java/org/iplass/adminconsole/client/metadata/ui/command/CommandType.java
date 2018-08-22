/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.command;

import org.iplass.mtp.command.definition.CommandDefinition;
import org.iplass.mtp.command.definition.JavaClassCommandDefinition;
import org.iplass.mtp.command.definition.ScriptingCommandDefinition;

/**
 * Commandの種類
 * 
 * @author lis70i
 *
 */
public enum CommandType {

	JAVA("Java", JavaClassCommandDefinition.class),
	SCRIPT("Script", ScriptingCommandDefinition.class);
	
	private String displayName;
	private Class<CommandDefinition> definitionClass;

	//Classに対してClass<CommandDefinition>を指定するとコンパイルエラーになるため未指定
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private CommandType(String displayName, Class definitionClass) {
		this.displayName = displayName;
		this.definitionClass = definitionClass;
	}
	
	public String displayName() {
		return displayName;
	}
	
	public Class<CommandDefinition> definitionClass() {
		return definitionClass;
	}
	
	public static CommandType valueOf(CommandDefinition definition) {
		for (CommandType type : values()) {
			//if (definition.getClass().isAssignableFrom(type.definitionClass)) {
			if (definition.getClass().getName().equals(type.definitionClass().getName())) {
				return type;
			}
		}
		return null;
	}
	
//	public static CommandDefinition typeOfDefinition(CommandType type) throws InstantiationException, IllegalAccessException {
//		return type.definitionClass().newInstance();
//	}
	public static CommandDefinition typeOfDefinition(CommandType type) {
		if (type.definitionClass().equals(JavaClassCommandDefinition.class)) {
			return new JavaClassCommandDefinition();
		} else if (type.definitionClass().equals(ScriptingCommandDefinition.class)) {
			return new ScriptingCommandDefinition();
		}
		return null;
	}
	
	public static CommandTypeEditPane typeOfEditPane(CommandType type) {
		if (type.definitionClass().equals(JavaClassCommandDefinition.class)) {
			return new JavaClassCommandEditPane();
		} else if (type.definitionClass().equals(ScriptingCommandDefinition.class)) {
			return new ScriptingCommandEditPane();
		}
		return null;
	}
}
