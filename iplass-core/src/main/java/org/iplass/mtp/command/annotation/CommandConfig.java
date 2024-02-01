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

package org.iplass.mtp.command.annotation;

import org.iplass.mtp.command.Command;
import org.iplass.mtp.transaction.Propagation;
import org.iplass.mtp.transaction.RollbackException;

/**
 * Commandのインスタンス（コンフィグレーションされた）の定義です。
 * 
 * @author K.Higuchi
 *
 */
public @interface CommandConfig {
	/**
	 * Commandのインスタンスの初期化設定です。<br>
	 * 設定例
	 * <pre>
	 * cmd.propA = 10
	 * cmd.propB = 'hoge'
	 * </pre>
	 * とした場合、
	 * Commandのプロパティ、propAに10、propBにhogeといったStringをセットします。<br>
	 * ※GroovyScriptで、cmdでCommandのインスタンスがバインドされています。
	 * @return
	 */
	String value() default "##default";
	
	/**
	 * Commandの実装クラスを指定します。
	 * 未指定（デフォルト）の場合は、このアノテーションが記述されているCommandクラスが指定されたとみなします。
	 * @return
	 */
	Class<? extends Command> commandClass() default Command.class;
	
	/**
	 * このCommandインスタンスを実行する際のトランザクションのPropagationの設定です。
	 * 未指定（デフォルト）の場合は、REQUIREDが指定されます。
	 * @return
	 */
	Propagation transactionPropagation() default Propagation.REQUIRED;
	
	/**
	 * Commandより例外がスローされた場合、トランザクションをロールバックするか否かの設定です。
	 * 未指定（デフォルト）の場合はtrueです。
	 * @return
	 */
	boolean rollbackWhenException() default true;
	
	/**
	 * トランザクションが本Command処理用に新規作成された際、
	 * 処理中にsetRoobackOnlyされた場合、
	 * かつ明示的に例外がスローされなかった場合、{@link RollbackException}をスローするか否かの設定です。
	 * 未指定（デフォルト）の場合はfalseです。
	 * @return
	 */
	boolean throwExceptionIfSetRollbackOnly() default false;

}
