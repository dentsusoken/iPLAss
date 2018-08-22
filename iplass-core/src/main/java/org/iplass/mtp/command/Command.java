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

/**
 * <p>
 * ロジックを実装するためのインタフェースです。
 * <p>
 * <p>
 * ロジックはこのインタフェースをimplementsしたクラスのexecuteメソッド内に記述してください。
 * Commandは、Webブラウザからの呼び出し（action）に対する処理ロジック、
 * WebApiの処理ロジック、
 * カスタムのワークフローの処理ロジックなどを実装します。
 * </p>
 * 
 * <h5>注意</h5>
 * <p>
 * デフォルトの設定（設定はアノテーション、もしくはadminConsoleからMetaDataとして設定する）ではCommandインスタンスは共有され、複数スレッドから同時に呼び出されるので、
 * デフォルト設定のまま利用する場合はクライアント依存・リクエスト依存の情報は、フィールドに保持しないようにしてください。
 * </p>
 * 
 * <h5>実装例（アノテーションにてCommand設定を定義）</h5>
 * <pre>
 * &#064;CommandClass(name="sample/SampleCmd", displayName="サンプルの処理")
 * public class SampleCmd implements Command {
 *   
 *   //Commandの処理ロジックをexecuteメソッド内に記述
 *   public String execute(RequestContext request) {
 *     String id = request.getParam("id");
 *     if (id == null) {
 *       throw new ApplicationException("id is null");
 *     }
 *     
 *     if (id.equals("valid")) {
 *       return "SUCCESS";
 *     } else {
 *       return "FAIL";
 *     }
 *   }
 *   
 * }
 * </pre>
 * 
 * @author K.Higuchi
 *
 */
public interface Command {
	
	/**
	 * 実行するロジックを記述します。
	 * 基本的にはexecute()呼び出し前にトランザクションは基盤側で自動的に開始されます。
	 * 処理実行中に例外が発生し、execute内で処理されず基盤側までスローされた場合は、
	 * execute()呼び出し前に自動開始されたトランザクションはロールバックされます。
	 * 
	 * @param request
	 * @return
	 */
	public String execute(RequestContext request);

}
