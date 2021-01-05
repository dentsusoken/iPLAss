/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.util.HashMap;
import java.util.Map;

import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.async.AsyncCommand;
import org.iplass.mtp.impl.command.MetaCommandFactory;
import org.iplass.mtp.impl.command.MetaCommandFactory.MetaCommandResult;
import org.iplass.mtp.impl.metadata.annotation.AnnotatableMetaDataFactory;
import org.iplass.mtp.impl.metadata.annotation.AnnotateMetaDataEntry;


public class MetaAsyncCommandFactory implements
		AnnotatableMetaDataFactory<AsyncCommand, Object> {

	private MetaCommandFactory commandFactory = new MetaCommandFactory();

	public MetaAsyncCommandFactory() {
	}

	@Override
	public Class<Object> getAnnotatedClass() {
		return Object.class;
	}

	@Override
	public Class<AsyncCommand> getAnnotationClass() {
		return AsyncCommand.class;
	}

	Map<String, AnnotateMetaDataEntry> toMetaData(AsyncCommand asyncCommand, Class<Object> annotatedClass) {
		Map<String, AnnotateMetaDataEntry> map = new HashMap<String, AnnotateMetaDataEntry>();
		MetaAsyncCommand metaAsyncCommand = new MetaAsyncCommand();

		String path = AsyncCommandService.COMMAND_ASYNC_META_PATH + asyncCommand.name();

		metaAsyncCommand.setName(asyncCommand.name());
		if (!DEFAULT.equals(asyncCommand.id())) {
			metaAsyncCommand.setId(asyncCommand.id());
		} else {
			metaAsyncCommand.setId(path);
		}
		if (!DEFAULT.equals(asyncCommand.displayName())) {
			metaAsyncCommand.setDisplayName(asyncCommand.displayName());
		} else {
			//指定されていない場合は、Commandの定義をセット
			CommandClass classDef = annotatedClass.getAnnotation(CommandClass.class);
			if (classDef != null && !DEFAULT.equals(classDef.displayName())) {
				metaAsyncCommand.setDisplayName(classDef.displayName());
			}
		}
		if (!DEFAULT.equals(asyncCommand.description())) {
			metaAsyncCommand.setDescription(asyncCommand.description());
		} else {
			//指定されていない場合は、Commandの定義をセット
			CommandClass classDef = annotatedClass.getAnnotation(CommandClass.class);
			if (classDef != null && !DEFAULT.equals(classDef.description())) {
				metaAsyncCommand.setDescription(classDef.description());
			}
		}

		MetaCommandResult res = commandFactory.toMetaCommand(asyncCommand.compositeCommand(), annotatedClass);
		if (res == null) {
			res = commandFactory.toMetaCommand(asyncCommand.command(), annotatedClass);
		}
		metaAsyncCommand.setCommand(res.metaCommand);
		
		if (!DEFAULT.equals(asyncCommand.queue())) {
			metaAsyncCommand.setQueue(asyncCommand.queue());
		}
		if (!DEFAULT.equals(asyncCommand.groupingKeyAttributeName())) {
			metaAsyncCommand.setGroupingKeyAttributeName(asyncCommand.groupingKeyAttributeName());
		}
		metaAsyncCommand.setExceptionHandlingMode(asyncCommand.exceptionHandlingMode());

		map.put(path, new AnnotateMetaDataEntry(metaAsyncCommand, asyncCommand.overwritable(), false));
		return map;
	}

	@Override
	public Map<String, AnnotateMetaDataEntry> toMetaData(Class<Object> annotatedClass) {
		AsyncCommand asyncCommand = annotatedClass.getAnnotation(AsyncCommand.class);
		return toMetaData(asyncCommand, annotatedClass);
	}


}
