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

package org.iplass.mtp.impl.auth.authenticate;

import org.iplass.mtp.auth.User;
import org.iplass.mtp.impl.auth.UserContextImpl;

/**
 *  テンポラリユーザコンテキスト
 * 
 *  一時的に作成されるユーザ用のコンテキストクラス
 * 
 * @author 藤田 義弘
 *
 */
public final class TemporaryUserContext extends UserContextImpl {
	//TODO パッケージ移動->auth直下
	
	private static final long serialVersionUID = 6545530702359622550L;
	
	public TemporaryUserContext(AccountHandle account, User userEntity) {
		super(account, userEntity);
	}

}
