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

package org.iplass.mtp.command.async;

import java.io.Serializable;

import org.iplass.mtp.command.CommandInvoker;

/**
 * 非同期実行したCommandの処理結果により、なんらかの処理を実行したい場合に利用するインタフェース。<br>
 * ※RdbQueueを利用している場合、このhandleメソッドの呼び出しは、非同期処理のステータス更新トランザクションと
 * 同一のトランザクション内で呼び出される。
 * もし、handleメソッド内で例外が発生した場合はトランザクションがロールバックされるので、
 * ステータス更新が正常に完了しない（＝タスク実行中のままのステータスになり、結果タイムアウトとなる）点注意。
 * 
 * 
 * @see CommandInvoker#executeAsync(String, AsyncRequestContext, ResultHandler)
 * @author K.Higuchi
 *
 */
public interface ResultHandler extends Serializable {
	
	public void handle(String commandResult);

	public void handle(Throwable exception);
}
