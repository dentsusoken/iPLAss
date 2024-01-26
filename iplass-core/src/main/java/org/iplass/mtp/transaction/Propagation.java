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
package org.iplass.mtp.transaction;

//TODO NESTED対応　mysqlもsavepoint使える
/**
 * トランザクションの伝搬をあらわすenum型です。
 * 
 * @author K.Higuchi
 *
 */
public enum Propagation {
	
	/** トランザクションが開始されていなかったら、開始（およびコミット/ロールバック）する。すでにトランザクションが開始されている場合は、そのトランザクションのコンテキストで実行される。 */
	REQUIRED,
	/** 新規にトランザクションを開始（およびコミット/ロールバック）する。既存のトランザクションがあった場合は、一旦サスペンドされ当該処理完了後、レジュームされる。 */
	REQUIRES_NEW,
	//NESTED,
	/** トランザクション制御をしない。既存のトランザクションが開始されている場合は、一旦そのトランザクションがサスペンドされ当該処理完了後、レジュームされる。 */
	NOT_SUPPORTED,
	/** 
	 * トランザクションが開始されていなかったら、トランザクション制御しない。既にトランザクションが開始されている場合は、そのトランザクションのコンテキストで実行される。<br>
	 */
	SUPPORTS
}