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

package org.iplass.mtp.command.definition.config;

public class CompositeCommandConfig extends CommandConfig {

	private static final long serialVersionUID = -8532756014954111622L;

	/** 実行コマンド */
	private CommandConfig[] commands;

	/** コマンド実行スクリプト */
	private String executeScript;//Commandの処理フローの記述。GroovyのScript。最後にステータスをreturnするように実装。

	/**
	 * 実行コマンドを返します。
	 *
	 * @return 実行コマンド
	 */
	public CommandConfig[] getCommands() {
		return commands;
	}

	/**
	 * 実行コマンドを設定します。
	 *
	 * @param commands 実行コマンド
	 */
	public void setCommands(CommandConfig[] commands) {
		this.commands = commands;
	}

	/**
	 * コマンド実行スクリプトを返します。
	 *
	 * @return コマンド実行スクリプト
	 */
	public String getExecuteScript() {
		return executeScript;
	}

	/**
	 * コマンド実行スクリプトを設定します。
	 *
	 * @param executeScript コマンド実行スクリプト
	 */
	public void setExecuteScript(String executeScript) {
		this.executeScript = executeScript;
	}

	@Override
	public CompositeCommandConfig copy() {
		CompositeCommandConfig copy = new CompositeCommandConfig();
		fillTo(copy);

		if (commands != null) {
			copy.commands = new CommandConfig[commands.length];
			int i = 0;
			for (CommandConfig config : commands) {
				copy.commands[i] = config.copy();
				i++;
			}
		}
		copy.executeScript = executeScript;
		return copy;
	}
}
