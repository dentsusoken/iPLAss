/*
 * Copyright (C) 2018 DENTSU SOKEN INC. All Rights Reserved.
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

import org.iplass.mtp.transaction.Propagation;
import org.iplass.mtp.transaction.RollbackException;

/**
 * 複数のCommandを単一のCommandとみなす場合の設定です。
 * 
 * @author K.Higuchi
 *
 */
public @interface CompositeCommandConfig {
	
	/**
	 * 複合するCommandの設定を指定します。
	 * @return
	 */
	CommandConfig[] command() default {};
	
	/**
	 * 複合Commandのインスタンスの初期化設定です。<br>
	 * 設定例
	 * <pre>
	 * cmd[0].propA = 10
	 * cmd[1].propB = 'hoge'
	 * </pre>
	 * とした場合、
	 * commandで定義されるCommandのindex=0のCommandのプロパティpropAに10、
	 * index=1のCommandのプロパティpropBにhogeといったStringをセットします。<br>
	 * ※GroovyScriptで、cmdでCommandのインスタンス配列がバインドされています。
	 * @return
	 */
	String value() default "##default";
	
	/**
	 * 複合Commandを利用する場合の複合Command全体に対するトランザクションのPropagationの設定です。
	 * 未指定（デフォルト）の場合は、REQUIREDが指定されます。
	 * 個別のCommandに適用するPropagationは、CommandConfigに設定可能です。
	 * @return
	 */
	Propagation transactionPropagation() default Propagation.REQUIRED;
	
	/**
	 * 複合Commandより例外がスローされた場合、トランザクションをロールバックするか否かの設定です。
	 * 未指定（デフォルト）の場合はtrueです。
	 * @return
	 */
	boolean rollbackWhenException() default true;
	
	/**
	 * トランザクションが本複合Command処理用に新規作成された際、
	 * 処理中にsetRoobackOnlyされた場合、
	 * かつ明示的に例外がスローされなかった場合、{@link RollbackException}をスローするか否かの設定です。
	 * 未指定（デフォルト）の場合はfalseです。
	 * @return
	 */
	boolean throwExceptionIfSetRollbackOnly() default false;
	
	/**
	 * 複合Commandを利用する場合の処理ルールをGroovyScriptで設定します。
	 * ルールでは、複数のCommandの呼び出し順などを制御可能で、
	 * 最終的にCommandの実行結果文字列を返却するように実装します。<br>
	 * 設定例
	 * <pre>
	 * if (cmd[0].execute(request) == "SUCCESS") {
	 *     return cmd[1].execute(request);
	 * } else {
	 *     return cmd[2].execute(request);
	 * }
	 * </pre>
	 * 
	 * commandで指定されたインスタンス配列がcmdの名前でバインドされています。
	 * また、requestでRequestContextがバインドされています。<br>
	 * 
	 * ルールが未指定の場合は、commandで指定されたCommandを順番に実行します。
	 * Commandの実行結果は最後のCommandの実行結果が返却されます。
	 * 
	 * @return
	 */
	String rule() default "##default";
	
}
