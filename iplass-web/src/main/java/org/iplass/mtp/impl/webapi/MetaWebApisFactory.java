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
package org.iplass.mtp.impl.webapi;

import java.util.HashMap;
import java.util.Map;

import org.iplass.mtp.command.annotation.webapi.WebApis;
import org.iplass.mtp.impl.metadata.annotation.AnnotatableMetaDataFactory;
import org.iplass.mtp.impl.metadata.annotation.AnnotateMetaDataEntry;


public class MetaWebApisFactory implements AnnotatableMetaDataFactory<WebApis, Object> {
	
	
	private MetaWebApiFactory metaWebApiFactory;
	
	public MetaWebApisFactory() {
		metaWebApiFactory = new MetaWebApiFactory();
	}
	
	@Override
	public Class<Object> getAnnotatedClass() {
		return Object.class;
	}

	@Override
	public Class<WebApis> getAnnotationClass() {
		return WebApis.class;
	}
	
	@Override
	public Map<String, AnnotateMetaDataEntry> toMetaData(Class<Object> annotatedClass) {
		Map<String, AnnotateMetaDataEntry> map = new HashMap<String, AnnotateMetaDataEntry>();
		WebApis actionMappings = annotatedClass.getAnnotation(WebApis.class);
		for (int i = 0; i < actionMappings.value().length; i++) {
			map.putAll(metaWebApiFactory.toMetaData(actionMappings.value()[i], annotatedClass));
		}
		return map;
	}

}
