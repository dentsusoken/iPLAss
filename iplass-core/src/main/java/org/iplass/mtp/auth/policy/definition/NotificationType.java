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

package org.iplass.mtp.auth.policy.definition;

/**
 * アカウント関連通知のタイプ。
 * 
 * @author K.Higuchi
 *
 */
public enum NotificationType {
	/**
	 * 新規アカウントの作成通知
	 */
	CREATED,
	/**
	 * パスワードのリセット通知
	 */
	CREDENTIAL_RESET,
	/**
	 * アカウントのロックアウト通知
	 */
	ROCKEDOUT,
	/**
	 * パスワードが更新された通知
	 */
	CREDENTIAL_UPDATED,
	/**
	 * アカウントの属性が更新された通知	
	 */
	PROPERTY_UPDATED,
	/**
	 * アカウントが削除される通知
	 */
	REMOVE,
	/**
	 * ログイン成功通知
	 */
	LOGIN_SUCCESS,
	/**
	 * ログイン失敗通知
	 */
	LOGIN_FAILED
	;
}
