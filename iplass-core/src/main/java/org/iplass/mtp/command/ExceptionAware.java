/*
 * Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.command;

/**
 * <p>
 * Commandの処理で発生した例外をハンドリングするためのインタフェース。
 * {@link Command#execute(RequestContext)}内で例外処理することもできるが、
 * Intercepter内で発生した例外も含めて捕捉したい場合は、Commandの実装クラスにて、
 * 当該interfaceも合わせて実装するようにする。
 * </p>
 * 
 * <h5>実装例</h5>
 * <pre>
 * public class ExpHandleCmd implements Command, ExceptionAware {
 *   
 *   //Commandの処理本体
 *   public String execute(RequestContext request) {
 *     :
 *     :
 *     throw new IllegalArgumentException("erorr");
 *   }
 *   
 *   //例外処理の実装メソッド
 *   public String handleException(RuntimeException exception, RequestContext request) throws RuntimeException {
 *     if (exception instanceof IllegalArgumentException) {
 *       //例外処理を記述
 *       :
 *       :
 *     }
 *     
 *     //Commandの処理結果としてステータスコードを返す、もしくは例外を再スロー
 *     return "ERROR";
 *   }
 * }
 * </pre>
 * 
 * 
 * @author K.Higuchi
 *
 */
public interface ExceptionAware {
	
	/**
	 * {@link Command#execute(RequestContext)}内、もしくはCommandInterceptor内で例外が発生した場合、 当メソッドが呼び出される。
	 * 返り値として、Commandのステータスを返却するか、例外を再スローするようにする。
	 * 
	 * @param exception 発生した例外
	 * @param request RequestContextのインスタンス
	 * @return Commandステータス。例外処理後、正常の結果を返したい場合に返却する
	 * @throws RuntimeException 例外処理後、framework側に例外を通知したい場合はスローする
	 */
	public String handleException(RuntimeException exception, RequestContext request) throws RuntimeException;

}
