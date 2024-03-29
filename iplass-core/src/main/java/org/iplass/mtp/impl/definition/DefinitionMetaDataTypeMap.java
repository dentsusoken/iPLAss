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
package org.iplass.mtp.impl.definition;

import org.iplass.mtp.definition.Definition;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.impl.metadata.RootMetaData;

public abstract class DefinitionMetaDataTypeMap<D extends Definition, M extends RootMetaData> {
	protected String pathPrefix;
	protected Class<M> metaType;
	protected Class<D> defType;
//	boolean replaceDot;

	protected DefinitionMetaDataTypeMap(String pathPrefix, Class<M> metaType, Class<D> defType) {
		this.pathPrefix = pathPrefix;
		this.metaType = metaType;
		this.defType = defType;
//		this.replaceDot = replaceDot;
	}

	@SuppressWarnings("unchecked")
	public D toDefinition(M metaData) {
		if (metaData instanceof DefinableMetaData<?>) {
			return ((DefinableMetaData<D>) metaData).currentConfig();
		}
		return null;
	};
	public abstract TypedDefinitionManager<D> typedDefinitionManager();

	public String toPath(String defName) {
		return pathPrefix + defName;
	}
	public String toDefName(String path) {
		return path.substring(pathPrefix.length());
	}
	public String typeName() {
		return defType.getSimpleName();
	}
}