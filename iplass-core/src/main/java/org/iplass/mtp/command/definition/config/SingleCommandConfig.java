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

package org.iplass.mtp.command.definition.config;

public class SingleCommandConfig extends CommandConfig {

	private static final long serialVersionUID = 4064117722972269162L;

	/** 実行コマンド名 */
	private String commandName;

	/**
	 * 実行コマンド名を返します。
	 *
	 * @return 実行コマンド名
	 */
	public String getCommandName() {
		return commandName;
	}

	/**
	 * 実行コマンド名を設定します。
	 *
	 * @param commandName 実行コマンド名
	 */
	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}

	@Override
	public SingleCommandConfig copy() {
		SingleCommandConfig copy = new SingleCommandConfig();
		fillTo(copy);
		copy.commandName = commandName;
		return copy;
	}
}
