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

package org.iplass.mtp.entity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Entityのプロパティの値を検証する際のContextです。
 * 
 * 
 * @author K.Higuchi
 *
 */
public class ValidationContext {
	
	private Entity entity;
	private String propertyName;
	private HashMap<String, Object> contextValues;

	public ValidationContext(Entity entity,
			String propertyName) {
		this.entity = entity;
		this.propertyName = propertyName;
	}
	
	public Iterator<String> getAttributeNames() {
		if (contextValues == null) {
			return Collections.emptyIterator();
		}
		return contextValues.keySet().iterator();
	}
	
	/**
	 * setAttributeされた値は、${key}形式でエラーメッセージに埋め込みが可能となります。<br>
	 * 
	 * 例えば、
	 * <pre>
	 * setAttribute("type", "String");
	 * </pre>
	 * された場合に、メッセージ定義が
	 * <pre>
	 * "${type}型は許可されません。"
	 * </pre>
	 * となっていた場合、エラーメッセージの出力は
	 * <pre>
	 * "String型は許可されません。"
	 * </pre>
	 * となります。
	 * 
	 * @param key 
	 * @param value
	 */
	public void setAttribute(String key, Object value) {
		if (contextValues == null) {
			contextValues = new HashMap<String, Object>();
		}
		contextValues.put(key, value);
	}
	
	public Object getAttribute(String key) {
		if (contextValues == null) {
			return null;
		}
		return contextValues.get(key);
	}
	
	public Entity getEntity() {
		return entity;
	}
	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

}
