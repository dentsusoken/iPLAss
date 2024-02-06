/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.command.annotation.async;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.iplass.mtp.async.ExceptionHandlingMode;
import org.iplass.mtp.command.annotation.CommandConfig;
import org.iplass.mtp.command.annotation.CompositeCommandConfig;

/**
 * 非同期実行Commandの定義です。
 * 
 * @author K.Higuchi
 *
 */
@Repeatable(AsyncCommands.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AsyncCommand {

	/**
	 * 当該定義のidです。
	 * 通常は未指定のまま（未指定の場合、メタデータのパスがidとなる）です。
	 * @return
	 */
	String id() default "##default";

	/**
	 * 非同期実行Commandの定義名を指定します。
	 * @return
	 */
	String name();

	/**
	 * 表示名を指定します。
	 * @return
	 */
	String displayName() default "##default";

	/**
	 * 概要説明文を指定します。
	 * @return
	 */
	String description() default "##default";

	/**
	 * ローカルテナントで上書き可能か否かを指定します。
	 * デフォルトはtrueです。
	 * @return
	 */
	boolean overwritable() default true;

	/**
	 * 非同期実行する際のキュー名を指定します。
	 * 未指定（デフォルト）の場合は、デフォルトキューを利用します。
	 * @return
	 */
	String queue() default "##default";

	/**
	 * 非同期処理実行時のgroupingKeyを利用する場合の、attribute名を指定します。
	 * @return
	 */
	String groupingKeyAttributeName() default "##default";

	/**
	 * 非同期処理実行時の例外発生時の処理方式を指定します。
	 * 未指定（デフォルト）の場合、ExceptionHandlingMode.RESTARTとなります。
	 * @return
	 */
	ExceptionHandlingMode exceptionHandlingMode() default ExceptionHandlingMode.RESTART;

	/**
	 * 非同期実行するCommandを設定します。
	 * compositeCommandが設定されている場合は、compositeCommandの設定が優先されます。
	 * 複数のCommandConnfigが設定された場合は、単純に順番に実行します。
	 * また、Command実行結果は最後のCommandの結果が返却されます。
	 * 
	 * @return
	 */
	CommandConfig[] command() default {@CommandConfig};

	/**
	 * 複合Commandを利用する場合の設定です。
	 * @return
	 */
	CompositeCommandConfig compositeCommand() default @CompositeCommandConfig;

}
