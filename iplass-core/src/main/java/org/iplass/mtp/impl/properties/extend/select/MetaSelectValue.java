/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.properties.extend.select;

import java.util.List;

import org.iplass.mtp.entity.definition.properties.selectvalue.SelectValueDefinition;
import org.iplass.mtp.impl.definition.DefinableMetaData;
import org.iplass.mtp.impl.metadata.BaseMetaDataRuntime;
import org.iplass.mtp.impl.metadata.BaseRootMetaData;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.util.ObjectUtil;

public class MetaSelectValue extends BaseRootMetaData implements DefinableMetaData<SelectValueDefinition> {
	private static final long serialVersionUID = 7155416271666450976L;

	private List<Value> values;

	public List<Value> getValues() {
		return values;
	}

	public void setValues(List<Value> values) {
		this.values = values;
	}

	@Override
	public SelectValueRuntime createRuntime(MetaDataConfig metaDataConfig) {
		return new SelectValueRuntime();
	}

	@Override
	public MetaSelectValue copy() {
		return ObjectUtil.deepCopy(this);
	}

	public SelectValueDefinition currentConfig() {
		SelectValueDefinition def = Value.toSelectValueDefinition(values);
		def.setName(name);
		def.setDisplayName(displayName);
		def.setDescription(description);
		return def;
	}

	public void applyConfig(SelectValueDefinition definition) {
		name = definition.getName();
		displayName = definition.getDisplayName();
		description = definition.getDescription();
		values = Value.toValues(definition);
	}

	public class SelectValueRuntime extends BaseMetaDataRuntime {

		@Override
		public MetaSelectValue getMetaData() {
			return MetaSelectValue.this;
		}

	}

}
