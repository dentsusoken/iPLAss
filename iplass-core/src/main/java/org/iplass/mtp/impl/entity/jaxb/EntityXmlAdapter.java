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

package org.iplass.mtp.impl.entity.jaxb;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.GenericEntity;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.util.StringUtil;

/**
 * EntityのXmlAdapter。
 *
 * @author K.Higuchi
 *
 */
public class EntityXmlAdapter extends XmlAdapter<GenericEntity, Entity> {

	@Override
	public Entity unmarshal(GenericEntity v) throws Exception {

		EntityDefinitionManager edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);

		// マッピングクラスがある場合はそのクラスで返してあげる
		String definitionName = v.getDefinitionName();
		EntityDefinition ed = edm.get(definitionName);

		if (ed != null) {
			if (ed.getMapping() != null && !StringUtil.isEmpty(ed.getMapping().getMappingModelClass())) {
				@SuppressWarnings("unchecked")
				Class<GenericEntity> retEntity = (Class<GenericEntity>) Class.forName(ed.getMapping().getMappingModelClass());
				return v.copyAs(retEntity);
			}
		}

		return v;
	}

	@Override
	public GenericEntity marshal(Entity v) throws Exception {
		return (GenericEntity) v;
	}

}
