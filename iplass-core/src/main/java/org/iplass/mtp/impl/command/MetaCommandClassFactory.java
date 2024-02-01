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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.definition.annotation.LocalizedString;
import org.iplass.mtp.impl.i18n.MetaLocalizedString;
import org.iplass.mtp.impl.metadata.annotation.AnnotatableMetaDataFactory;
import org.iplass.mtp.impl.metadata.annotation.AnnotateMetaDataEntry;


public class MetaCommandClassFactory implements
		AnnotatableMetaDataFactory<CommandClass, Command> {

	public static final String PATH_PREFIX = "/commandClass/";

	@Override
	public Class<Command> getAnnotatedClass() {
		return Command.class;
	}

	@Override
	public Class<CommandClass> getAnnotationClass() {
		return CommandClass.class;
	}

	@Override
	public Map<String, AnnotateMetaDataEntry> toMetaData(Class<Command> annotatedClass) {
		Map<String, AnnotateMetaDataEntry> map = new HashMap<String, AnnotateMetaDataEntry>();
		CommandClass classDef = annotatedClass.getAnnotation(CommandClass.class);
		MetaMetaJavaCommand metaMetaJavaCommand = new MetaMetaJavaCommand();
		if (DEFAULT.equals(classDef.name())) {
			metaMetaJavaCommand.setName(annotatedClass.getName().replace(".", "/"));
		} else {
			metaMetaJavaCommand.setName(classDef.name());
		}
		if (!DEFAULT.equals(classDef.displayName())) {
			metaMetaJavaCommand.setDisplayName(classDef.displayName());
		}
		if (classDef.localizedDisplayName().length > 0) {
			List<MetaLocalizedString> localizedDisplayNameList = new ArrayList<>();
			for (LocalizedString localeValue : classDef.localizedDisplayName()) {
				MetaLocalizedString metaLocaleValue = new MetaLocalizedString();
				metaLocaleValue.setLocaleName(localeValue.localeName());
				metaLocaleValue.setStringValue(localeValue.stringValue());
				localizedDisplayNameList.add(metaLocaleValue);
			}
			metaMetaJavaCommand.setLocalizedDisplayNameList(localizedDisplayNameList);
		}
		if (!DEFAULT.equals(classDef.description())) {
			metaMetaJavaCommand.setDescription(classDef.description());
		}
		
		metaMetaJavaCommand.setReadOnly(classDef.readOnly());
		metaMetaJavaCommand.setNewInstancePerRequest(classDef.newInstancePerRequest());

		metaMetaJavaCommand.setClassName(annotatedClass.getName());
//		if (classDef.resultStatus().length != 0) {
//			metaMetaJavaCommand.setResultStatus(classDef.resultStatus());
//		}
		String path = PATH_PREFIX + metaMetaJavaCommand.getName();
		metaMetaJavaCommand.setId(path);
		map.put(path, new AnnotateMetaDataEntry(metaMetaJavaCommand, classDef.overwritable(), false));

//		if (classDef.instance() != null) {
//			for (CommandDef cd: classDef.instance()) {
//				MetaSingleCommand metaCmd = new MetaSingleCommand();
//				if (DEFAULT.equals(cd.name())) {
//					metaCmd.setName(metaMetaJavaCommand.getName());
//				} else {
//					metaCmd.setName(cd.name());
//				}
//				if (DEFAULT.equals(cd.displayName())) {
//					metaCmd.setDisplayName(metaCmd.getName());
//				} else {
//					metaCmd.setDisplayName(cd.displayName());
//				}
//				metaCmd.setMetaMetaCommandId(metaMetaJavaCommand.getId());
//
//				if (!DEFAULT.equals(cd.commandConfig())) {
//					metaCmd.setCommandConfig(cd.commandConfig());
//				}
//				String instancePath = "/command/" + metaCmd.getName();
//				if (DEFAULT.equals(cd.id())) {
//					metaCmd.setId(instancePath);
//				} else {
//					metaCmd.setId(cd.id());
//				}
//				map.put(instancePath, metaCmd);
//			}
//		}
		return map;
	}

}
