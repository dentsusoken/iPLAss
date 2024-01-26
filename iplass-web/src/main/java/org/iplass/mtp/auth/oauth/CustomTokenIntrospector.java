/*
 * Copyright (C) 2019 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.auth.oauth;

import java.util.Map;

import org.iplass.mtp.auth.User;
import org.iplass.mtp.command.RequestContext;

/**
 * ResourceServerからのintrospectionリクエストのレスポンスを
 * カスタマイズするためのインタフェースです。
 * 
 * @author K.Higuchi
 *
 */
public interface CustomTokenIntrospector {
	
	/**
	 * introspectionリクエストのレスポンスをカスタマイズするよう実装します。
	 * 
	 * @param response レスポンスを表すMap
	 * @param request RequestContext
	 * @param resourceOwner 当該AccessTokenのResource Owner
	 * @return falseを返した場合、Tokenは無効と判断し、active=falseのみをResourceServerへ返却します。
	 */
	public boolean handle(Map<String, Object> response, RequestContext request, User resourceOwner);

}
