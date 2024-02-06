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
package org.iplass.mtp.auth.token;

import java.sql.Timestamp;

/**
 * 認証トークンを表すインタフェースです。
 * 
 * @author K.Higuchi
 *
 */
public interface AuthTokenInfo {
	/**
	 * トークンの種別です。
	 * 
	 * @return
	 */
	public String getType();
	
	/**
	 * トークンを一意に特定するためのキーです。
	 * 
	 * @return
	 */
	public String getKey();
	
	/**
	 * トークンの内容を表現する説明文です。
	 * 
	 * @return
	 */
	public String getDescription();
	
	/**
	 * トークンの発行日です。
	 * 
	 * @return
	 */
	public Timestamp getStartDate();

}
