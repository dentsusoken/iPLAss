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

package org.iplass.mtp.command.async.definition;

import javax.xml.bind.annotation.XmlRootElement;

import org.iplass.mtp.async.AsyncTaskManager;
import org.iplass.mtp.async.AsyncTaskOption;
import org.iplass.mtp.async.ExceptionHandlingMode;
import org.iplass.mtp.command.definition.config.CommandConfig;
import org.iplass.mtp.definition.Definition;


/**
 * 非同期実行Commandの定義。
 * 定義した非同期実行CommandはCommandInvokerを利用して呼び出し可能。
 * 非同期処理自体は、{@link AsyncTaskManager}経由で実行される。
 *
 * @see org.iplass.mtp.command.CommandInvoker
 * @see AsyncTaskManager
 * @author K.Higuchi
 *
 */
@XmlRootElement
public class AsyncCommandDefinition implements Definition {
	private static final long serialVersionUID = 8751389096879390423L;

	private String name;
	private String displayName;
//	private List<LocalizedStringDefinition> localizedDisplayNameList;
	private String description;

	private String queue;
	private String groupingKeyAttributeName;
	private ExceptionHandlingMode exceptionHandlingMode = ExceptionHandlingMode.RESTART;

	private CommandConfig commandConfig;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 非同期処理用のキュー
	 *
	 * @return
	 */
	public String getQueue() {
		return queue;
	}

	/**
	 * 非同期処理用のキューを設定
	 *
	 * @param queue キュー名
	 * @see AsyncTaskOption
	 */
	public void setQueue(String queue) {
		this.queue = queue;
	}

	/**
	 * 非同期処理実行時のgroupingKeyを利用する場合、
	 * AsyncRequestContextのattributeにそのキーをセットする。
	 * その際の、attribute名。
	 *
	 * @return
	 */
	public String getGroupingKeyAttributeName() {
		return groupingKeyAttributeName;
	}

	/**
	 * 非同期処理実行時のgroupingKeyを利用する場合、
	 * AsyncRequestContextのattributeにそのキーをセットする。
	 * その際の、attribute名を指定する。
	 *
	 * @param groupingKeyAttributeName
	 * @see AsyncTaskOption
	 *
	 */
	public void setGroupingKeyAttributeName(String groupingKeyAttributeName) {
		this.groupingKeyAttributeName = groupingKeyAttributeName;
	}

	/**
	 * 非同期実行時の例外処理モード
	 *
	 * @return
	 */
	public ExceptionHandlingMode getExceptionHandlingMode() {
		return exceptionHandlingMode;
	}

	/**
	 * 非同期実行時の例外処理モードを設定。
	 *
	 * @param exceptionHandlingMode
	 * @see AsyncTaskOption
	 */
	public void setExceptionHandlingMode(ExceptionHandlingMode exceptionHandlingMode) {
		this.exceptionHandlingMode = exceptionHandlingMode;
	}

	/**
	 * 非同期実行するCommandの設定
	 *
	 * @return
	 */
	public CommandConfig getCommandConfig() {
		return commandConfig;
	}

	/**
	 * 非同期実行するCommandの設定をセット。
	 *
	 * @param commandConfig
	 */
	public void setCommandConfig(CommandConfig commandConfig) {
		this.commandConfig = commandConfig;
	}

}
