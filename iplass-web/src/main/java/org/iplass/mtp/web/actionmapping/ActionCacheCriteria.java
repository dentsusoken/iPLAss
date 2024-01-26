/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.web.actionmapping;

import org.iplass.mtp.command.RequestContext;

/**
 * アクションのキャッシュに対して、一致条件を定義するためのインタフェースです。
 * 
 * @author K.Higuchi
 *
 */
public interface ActionCacheCriteria {
	
	/**
	 * キャッシュのキーとして利用する一意の文字列を返却するように実装します。<br>
	 * 
	 * 当該キーと一致するキャッシュが存在した場合、そのキャッシュがクライアントへ返却されるようになります。<br>
	 * 返却値がnullの場合、当該requestではキャッシュしません。
	 * 
	 * @param request
	 * @return
	 */
	public String createCacheKey(RequestContext request);
	

}
