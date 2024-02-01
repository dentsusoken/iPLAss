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

package org.iplass.mtp.entity.interceptor;

import java.util.Iterator;

import org.iplass.mtp.entity.definition.EntityDefinition;


/**
 * Entity操作の呼び出しを表すインタフェース。
 * proceed()呼び出しで、後続処理（次のInterceptor呼び出しor実際のEntity操作）を呼び出す。
 * 
 * @author K.Higuchi
 *
 * @param <R>
 * @see EntityInterceptor
 */
public interface EntityInvocation<R> {
	
	EntityDefinition getEntityDefinition();
	
	/**
	 * 後続処理呼び出し。
	 * 
	 * @return 後続処理からのリターン値。
	 */
	public R proceed();
	
	
	/**
	 * 一連のInterceptの間、保持したいオブジェクトをセット可能。
	 * 
	 * @param name
	 * @param value
	 */
	public void setAttribute(String name, Object value);
	
	/**
	 * 一連のInterceptの間にsetAttribute()したオブジェクトを取得可能。
	 * 
	 * @param name
	 * @return
	 */
	public Object getAttribute(String name);
	
	/**
	 * 一連のInterceptの間にsetAttribute()された属性名の取得。
	 * 
	 * @return
	 */
	public Iterator<String> getAttributeNames();
	
	/**
	 * Entity操作の種類を判別可能。
	 * 
	 * @return
	 */
	public InvocationType getType();


	
}
