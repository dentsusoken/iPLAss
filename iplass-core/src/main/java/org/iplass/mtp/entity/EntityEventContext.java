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

package org.iplass.mtp.entity;

import java.util.HashMap;

/**
 * EntityEventLintenerの呼び出しの際、
 * before～と、after～の呼び出しの間で保持されるContextです。
 * （他のListenerともContextは共有されるので、キー名は重複し得ないものを利用するようにしてください）
 * 
 * @author K.Higuchi
 *
 */
public class EntityEventContext {
	
	/** 追加時（および、追加に伴うバリデーション時）に、当該キー名で指定されたInsertOptionを取得可能。 */
	public static final String INSERT_OPTION = "insertOption";
	/** 更新時（および、更新に伴うバリデーション時）に、当該キー名で指定されたUpdateOptionを取得可能。 */
	public static final String UPDATE_OPTION = "updateOption";
	/** 削除時に、当該キー名で指定されたDeleteOptionを取得可能。 */
	public static final String DELETE_OPTION = "deleteOption";
	/** バリデーション時にバリデーション対象のプロパティのList<String>を取得可能。 */
	public static final String VALIDATE_PROPERTIES = "validateProperties";
	/** 更新時に、当該キー名で更新前のEntityを取得可能。 */
	public static final String BEFORE_UPDATE_ENTITY = "beforeUpdateEntity";
	
	private HashMap<String, Object> contextValues;
	
	public EntityEventContext() {
	}
	
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

}
