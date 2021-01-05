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

package org.iplass.mtp.impl.command;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.CommandConfig;
import org.iplass.mtp.command.annotation.CompositeCommandConfig;
import org.iplass.mtp.impl.metadata.MetaDataRuntimeException;
import org.iplass.mtp.impl.metadata.annotation.AnnotatableMetaDataFactory;

public class MetaCommandFactory {

	public static class MetaCommandInfo {
		public MetaCommand metaCommand;
		public Class<? extends Command> cmdClass;

		public MetaCommandInfo(MetaCommand metaCommand, Class<? extends Command> cmdClass) {
			this.metaCommand = metaCommand;
			this.cmdClass = cmdClass;
		}
	}
	
	public static class MetaCommandResult {
		public List<MetaCommandInfo> metaCommandInfos;
		public MetaCommand metaCommand;
		
		public MetaCommandResult(List<MetaCommandInfo> metaCommandInfos, MetaCommand metaCommand) {
			this.metaCommandInfos = metaCommandInfos;
			this.metaCommand = metaCommand;
		}
	}
	
	public MetaCommandResult toMetaCommand(CommandConfig[] commandDefs, Class<?> annotatedClass) {
		List<MetaCommandInfo> metaCommandInfos = new ArrayList<MetaCommandInfo>();
		MetaCommand meta = null;
		if (commandDefs.length > 1) {
			MetaCompositeCommand metaComposite = new MetaCompositeCommand();
			MetaCommand[] cmds = new MetaCommand[commandDefs.length];
			for (int i = 0; i < cmds.length; i++) {
				MetaCommandInfo info = toMetaCommand(commandDefs[i], annotatedClass);
				cmds[i] = info.metaCommand;
				metaCommandInfos.add(info);
			}
			metaComposite.setCommands(cmds);
			meta = metaComposite;
		} else if (commandDefs.length == 1) {
			MetaCommandInfo info = toMetaCommand(commandDefs[0], annotatedClass);
			meta = info.metaCommand;
			metaCommandInfos.add(info);
		}
		return new MetaCommandResult(metaCommandInfos, meta);
	}
	
	public MetaCommandResult toMetaCommand(CompositeCommandConfig compositeCommandDef, Class<?> annotatedClass) {
		if (compositeCommandDef.command().length == 0) {
			return null;
		}
		
		List<MetaCommandInfo> metaCommandInfos = new ArrayList<MetaCommandInfo>();
		MetaCompositeCommand metaComposite = new MetaCompositeCommand();
		metaComposite.setTransactionPropagation(compositeCommandDef.transactionPropagation());
		metaComposite.setRollbackWhenException(compositeCommandDef.rollbackWhenException());
		metaComposite.setThrowExceptionIfSetRollbackOnly(compositeCommandDef.throwExceptionIfSetRollbackOnly());
		
		MetaCommand[] cmds = new MetaCommand[compositeCommandDef.command().length];
		for (int i = 0; i < cmds.length; i++) {
			MetaCommandInfo info = toMetaCommand(compositeCommandDef.command()[i], annotatedClass);
			cmds[i] = info.metaCommand;
			metaCommandInfos.add(info);
		}
		metaComposite.setCommands(cmds);
		if (!AnnotatableMetaDataFactory.DEFAULT.equals(compositeCommandDef.rule())) {
			metaComposite.setRule(compositeCommandDef.rule());
		}
		return new MetaCommandResult(metaCommandInfos, metaComposite);
	}
	
	@SuppressWarnings("unchecked")
	public MetaCommandInfo toMetaCommand(CommandConfig commandDef, Class<?> anotatedClass) {
		MetaSingleCommand metaCommand = new MetaSingleCommand();

		Class<? extends Command> cmdClass = null;
		if (commandDef.commandClass() == Command.class) {
			//デフォルトの場合は、Annotationを指定したCommandクラス
			if (anotatedClass != null && Command.class.isAssignableFrom(anotatedClass)) {
				cmdClass = (Class<? extends Command>) anotatedClass;
			} else {
				throw new MetaDataRuntimeException("Anotated class is not Command class, so commandClass must specify:" + anotatedClass);
			}
		} else {
			cmdClass = commandDef.commandClass();
		}
		
		metaCommand.setTransactionPropagation(commandDef.transactionPropagation());
		metaCommand.setRollbackWhenException(commandDef.rollbackWhenException());
		metaCommand.setThrowExceptionIfSetRollbackOnly(commandDef.throwExceptionIfSetRollbackOnly());

		//対象CommandClassのAnnotation定義を取得
		CommandClass ano = cmdClass.getAnnotation(CommandClass.class);
		if (AnnotatableMetaDataFactory.DEFAULT.equals(ano.name())) {
			//CommandClassにアノテーションでName指定がない場合はCommandClass名を.->/にしたもの
			metaCommand.setMetaMetaCommandId(MetaCommandClassFactory.PATH_PREFIX + cmdClass.getName().replace(".", "/"));
		} else {
			metaCommand.setMetaMetaCommandId(MetaCommandClassFactory.PATH_PREFIX + ano.name());
		}
		if (!AnnotatableMetaDataFactory.DEFAULT.equals(commandDef.value())) {
			//デフォルト出ない場合、設定
			metaCommand.setCommandConfig(commandDef.value());
		}
		
		return new MetaCommandInfo(metaCommand, cmdClass);
	}

	
}

