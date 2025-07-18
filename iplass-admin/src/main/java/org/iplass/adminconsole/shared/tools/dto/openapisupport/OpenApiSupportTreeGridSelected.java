/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.adminconsole.shared.tools.dto.openapisupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class OpenApiSupportTreeGridSelected implements Serializable {
	private List<String> webApiList = new ArrayList<>();
	private Map<String, List<String>> entityCRUDApiMap = new HashMap<>();


	/**
	 * @return webApiList
	 */
	public List<String> getWebApiList() {
		return webApiList;
	}

	public void addWebApi(String webApi) {
		webApiList.add(webApi);
	}

	/**
	 * @return entityCRUDApiList
	 */
	public Map<String, List<String>> getEntityCRUDApiMap() {
		return entityCRUDApiMap;
	}

	/**
	 * @param entityCRUDApiList セットする entityCRUDApiList
	 */
	public void addEntityCRUDApi(String definitionName, String auth) {
		var authList = entityCRUDApiMap.get(definitionName);
		if (null == authList) {
			authList = new ArrayList<>();
			entityCRUDApiMap.put(definitionName, authList);
		}
		authList.add(auth);
	}
}
