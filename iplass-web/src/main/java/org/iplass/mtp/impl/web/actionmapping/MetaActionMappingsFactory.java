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

package org.iplass.mtp.impl.web.actionmapping;

import java.util.HashMap;
import java.util.Map;

import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.annotation.action.ActionMappings;
import org.iplass.mtp.impl.metadata.annotation.AnnotatableMetaDataFactory;
import org.iplass.mtp.impl.metadata.annotation.AnnotateMetaDataEntry;


public class MetaActionMappingsFactory implements
		AnnotatableMetaDataFactory<ActionMappings, Command> {
	
	
	private MetaActionMappingFactory metaActionMappingfactory;
	
	public MetaActionMappingsFactory() {
		metaActionMappingfactory = new MetaActionMappingFactory();
	}
	
	@Override
	public Class<Command> getAnnotatedClass() {
		return Command.class;
	}

	@Override
	public Class<ActionMappings> getAnnotationClass() {
		return ActionMappings.class;
	}
	
	@Override
	public Map<String, AnnotateMetaDataEntry> toMetaData(Class<Command> annotatedClass) {
		Map<String, AnnotateMetaDataEntry> map = new HashMap<String, AnnotateMetaDataEntry>();
		ActionMappings actionMappings = annotatedClass.getAnnotation(ActionMappings.class);
		for (int i = 0; i < actionMappings.value().length; i++) {
			map.putAll(metaActionMappingfactory.toMetaData(actionMappings.value()[i], annotatedClass));
		}
		return map;
	}


}
