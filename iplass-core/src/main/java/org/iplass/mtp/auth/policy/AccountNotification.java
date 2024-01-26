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

package org.iplass.mtp.auth.policy;

import org.iplass.mtp.auth.policy.definition.NotificationType;

/**
 * アカウント通知クラス。
 * 
 * @see AccountNotificationListener
 * @author K.Higuchi
 *
 */
public class AccountNotification {
	private NotificationType type;
	private String userOid;
	
	public AccountNotification(NotificationType type, String userOid) {
		this.type = type;
		this.userOid = userOid;
	}
	
	/**
	 * 通知のタイプ。
	 * @return
	 */
	public NotificationType getType() {
		return type;
	}
	/**
	 * 対象のユーザーのoid
	 * 
	 * @return
	 */
	public String getUserOid() {
		return userOid;
	}
}
