/*
 * Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.gem.command;

import org.iplass.gem.auth.GemAuth;

/**
 * gem/generic配下のWebApiでParameterTypeを指定した場合に、{@link GemAuth} でWebApiの権限チェックを通過するために
 * Parameterクラスに設定するインターフェース
 *
 * @author lis3wg
 *
 */
public interface GemWebApiParameter {

	/**
	 * リクエストパラメータのdefNameを取得します。
	 *
	 * @return defName
	 */
	String getDefName();

	/**
	 * リクエストパラメータのviewNameを取得します。
	 *
	 * @return viewName
	 */
	String getViewName();
}
