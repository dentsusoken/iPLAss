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

import java.util.List;

import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.metadata.MetaDataEntryInfo;
import org.iplass.mtp.impl.metadata.MetaDataRuntime;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.impl.util.KeyGenerator;

public abstract class AbstractTypedMetaDataService<M extends RootMetaData, R extends MetaDataRuntime> implements TypedMetaDataService<M, R> {

	private KeyGenerator generator = new KeyGenerator();

	@Override
	public void createMetaData(M meta) {
		meta.setId(generator.generateId());
		MetaDataContext.getContext().store(DefinitionService.getInstance().getPathByMeta(getMetaDataType(), meta.getName()), meta);
	}

	@Override
	public void updateMetaData(M meta) {
		MetaDataContext.getContext().update(DefinitionService.getInstance().getPathByMeta(getMetaDataType(), meta.getName()), meta);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void removeMetaData(String definitionName) {
		R handler = getRuntimeByName(definitionName);
		M meta = (M) handler.getMetaData();
		MetaDataContext.getContext().remove(DefinitionService.getInstance().getPathByMeta(getMetaDataType(), meta.getName()));
	}

	@Override
	public R getRuntimeByName(String name) {
		return MetaDataContext.getContext().getMetaDataHandler(getRuntimeType(), DefinitionService.getInstance().getPathByMeta(getMetaDataType(), name));
	}

	@Override
	public R getRuntimeById(String id) {
		return MetaDataContext.getContext().getMetaDataHandlerById(getRuntimeType(), id);
	}

	@Override
	public List<String> nameList() {
		String fixedPath = getFixedPath();
		List<String> pathList = MetaDataContext.getContext().pathList(fixedPath);
//		pathList.replaceAll(path -> path.substring(fixedPath.length()));
		pathList.replaceAll(path -> toDefName(path));
		return pathList;
	}

	@Override
	public List<MetaDataEntryInfo> list() {
		return MetaDataContext.getContext().definitionList(getFixedPath());
	}

	@Override
	public List<MetaDataEntryInfo> list(String path) {
		return MetaDataContext.getContext().definitionList(DefinitionService.getInstance().getPathByMeta(getMetaDataType(), path));
	}

	private String getFixedPath() {
		DefinitionService service = DefinitionService.getInstance();
		return service.getPrefixPath(service.getDefinitionType(getMetaDataType()));
	}

	private String toDefName(String path) {
		DefinitionService service = DefinitionService.getInstance();
		return service.getDefinitionName(service.getDefinitionType(getMetaDataType()), path);
	}
}
