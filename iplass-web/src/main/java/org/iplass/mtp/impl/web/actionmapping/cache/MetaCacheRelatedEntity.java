/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.web.actionmapping.cache;

import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.web.actionmapping.definition.cache.CacheRelatedEntityDefinition;
import org.iplass.mtp.web.actionmapping.definition.cache.RelatedEntityType;

/**
 * キャッシュに関連するEntityの定義。
 * 当該Entityが更新された際、キャッシュはクリアされる。
 *
 * @author K.Higuchi
 *
 */
public class MetaCacheRelatedEntity implements MetaData {
	private static final long serialVersionUID = -2239851206689032015L;

	//TODO Id or Name??あえてnameか？*指定を可能に。
	private String definitionName;
	private RelatedEntityType type;

	public String getDefinitionName() {
		return definitionName;
	}
	public void setDefinitionName(String definitionName) {
		this.definitionName = definitionName;
	}
	public RelatedEntityType getType() {
		return type;
	}
	public void setType(RelatedEntityType type) {
		this.type = type;
	}

	//Definition → Meta
	public void applyConfig(CacheRelatedEntityDefinition definition) {
		definitionName = definition.getDefinitionName();
		type = definition.getType();
	}

	//Meta → Definition
	public CacheRelatedEntityDefinition currentConfig() {
		CacheRelatedEntityDefinition definition = new CacheRelatedEntityDefinition();
		definition.setDefinitionName(definitionName);
		definition.setType(type);
		return definition;
	}

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

}
