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

package org.iplass.mtp.command;

import org.iplass.mtp.Manager;
import org.iplass.mtp.command.async.AsyncRequestContext;
import org.iplass.mtp.command.async.ResultHandler;
import org.iplass.mtp.transaction.TransactionOption;

/**
 * Commandをコード中より呼び出す際に利用するインタフェースです。
 * CommandInvokerを利用してCommandを呼び出す場合と、直接Commandのexecuteを呼び出す場合の違いは、
 * CommandInterceptorの処理（トランザクション制御など）が実行されるか否かです。
 * 
 * @author K.Higuchi
 *
 */
public interface CommandInvoker extends Manager {

	//TODO Commandの実行結果のキャッシュ

	/**
	 * 指定のcmdNameのCommandのインスタンスを指定のrequestで呼び出します。
	 * 
	 * @param cmdName Command定義の名前（CommandDefinitionで定義した際の名前、CommandClassアノテーションで指定したname）
	 * @param request RequestContextのインスタンス
	 * @return Commandが返却する結果ステータス
	 */
	public String execute(String cmdName, RequestContext request);
	
	/**
	 * 指定のcmdNameのCommandのインスタンスを指定のrequest、transactionOptionで呼び出します。
	 * 
	 * @param cmdName Command定義の名前（CommandDefinitionで定義した際の名前、CommandClassアノテーションで指定したname）
	 * @param request RequestContextのインスタンス
	 * @param transactionOption トランザクション制御設定
	 * @return Commandが返却する結果ステータス
	 */
	public String execute(String cmdName, RequestContext request, TransactionOption transactionOption);

	/**
	 * 指定のcmdを指定のrequestで呼び出します。
	 * 
	 * @param cmd Commandのインスタンス
	 * @param request RequestContextのインスタンス
	 * @return Commandが返却する結果ステータス
	 */
	public String execute(Command cmd, RequestContext request);

	/**
	 * 指定のcmdを指定のrequest、transactionOptionで呼び出します。
	 * 
	 * @param cmd Commandのインスタンス
	 * @param request RequestContextのインスタンス
	 * @param transactionOption トランザクション制御設定
	 * @return Commandが返却する結果ステータス
	 */
	public String execute(Command cmd, RequestContext request, TransactionOption transactionOption);
	
	/**
	 * 指定のcmdNameのCommandの新規インスタンスを取得します。
	 * 指定したCommandを{@link #execute(Command, RequestContext)}で実行することが可能です。
	 * 呼び出し前にCommandの初期化処理（メンバ変数の値の設定）を行うことが可能です。
	 * 
	 * @param cmdName Command定義の名前（CommandDefinitionで定義した際の名前、CommandClassアノテーションで指定したname）
	 * @return Commandが返却する結果ステータス
	 */
	public Command getCommandInstance(String cmdName);
	
	/**
	 * 指定のasyncCmdNameで定義される非同期Commandを指定のrequestで実行します。
	 * 非同期Commandは、{@link org.iplass.mtp.async.AsyncTaskManager}経由で実行されます。
	 * 
	 * @param asyncCmdName AsyncCommand定義の名前（AsyncCommandDefinitionで定義した際の名前、AsyncCommandアノテーションで指定したname）
	 * @param request 非同期Commandへのrequest
	 * @return 非同期タスクのタスクID
	 * @see org.iplass.mtp.async.AsyncTaskManager
	 */
	public long executeAsync(String asyncCmdName, AsyncRequestContext request);
	
	/**
	 * 指定のasyncCmdNameで定義される非同期Commandを指定のrequestで実行します。
	 * 非同期Commandは、{@link org.iplass.mtp.async.AsyncTaskManager}経由で実行されます。
	 * resultHandlerにて、Commandの実行ステータスによる処理を設定することが可能です。
	 * ResultHandlerの標準提供の実装として、{@link org.iplass.mtp.command.async.WriteToEntityHandler}があります。
	 * 
	 * @param asyncCmdName AsyncCommand定義の名前（AsyncCommandDefinitionで定義した際の名前、AsyncCommandアノテーションで指定したname）
	 * @param request 非同期Commandへのrequest
	 * @param resultHandler ResultHandlerのインスタンス（Serializbleの必要あり）
	 * @return　非同期タスクのタスクID
	 * @see org.iplass.mtp.async.AsyncTaskManager
	 */
	public long executeAsync(String asyncCmdName, AsyncRequestContext request, ResultHandler resultHandler);
	
}
