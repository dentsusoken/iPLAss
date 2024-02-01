/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.common;

import org.iplass.mtp.definition.Definition;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.web.actionmapping.definition.ActionMappingDefinition;
import org.iplass.mtp.webapi.definition.WebApiDefinition;

public class MetaDataSharableControllerImpl implements MetaDataSharableController {

	@Override
	public boolean isDataSharableEnabled(Class<? extends Definition> defClass) {
		//TODO instanceofで判断したほうがいいいかも
		//Entityの場合はデータ共有設定可
		return (EntityDefinition.class.getName().equals(defClass.getName())
				);
	}

	@Override
	public boolean isPermissionSharableEnabled(Class<? extends Definition> defClass) {
		//TODO instanceofで判断したほうがいいいかも
		return (EntityDefinition.class.getName().equals(defClass.getName())
				|| ActionMappingDefinition.class.getName().equals(defClass.getName())
				|| WebApiDefinition.class.getName().equals(defClass.getName())
				);
	}

}
