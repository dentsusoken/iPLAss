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

package org.iplass.mtp.entity.definition;

import org.iplass.mtp.definition.DefinitionModifyResult;

/**
 * Entity定義更新時の結果を表すクラス。
 * 
 * Entity定義の変更は、データの変更を伴う可能性があり、
 * 定義変更処理自体は、非同期、別トランザクションで実行される。
 * 呼び出し側スレッドを同期したい場合は、isSuccess()を呼び出す。
 * isSuccess()呼び出し時に、呼び出し側スレッドが待機する形となる。
 * 
 *
 * @author K.Higuchi
 *
 */
//public abstract class EntityDefinitionModifyResult extends DefinitionModifyResult {
public class EntityDefinitionModifyResult extends DefinitionModifyResult {

	protected static final long FIXED_SERIAL_VERSION = 7145015583970134749L;
	private static final long serialVersionUID = FIXED_SERIAL_VERSION;
	
	public EntityDefinitionModifyResult() {
		super();
	}
	public EntityDefinitionModifyResult(boolean isSuccess, String message) {
		super(isSuccess, message);
	}
	public EntityDefinitionModifyResult(boolean isSuccess) {
		super(isSuccess);
	}

//	/**
//	 * 処理結果を返す。
//	 * 非同期で実行している場合、非同期処理が完了するまで、
//	 * このメソッドの呼び出しでスレッドがブロックされる。
//	 *
//	 * @return 成功の場合true
//	 */
//	public boolean isSuccess();

	//TODO 返却値の決定

	//FIXME 後からの問い合わせ用のハンドル（タスクＩＤ？）の取得メソッド

}
