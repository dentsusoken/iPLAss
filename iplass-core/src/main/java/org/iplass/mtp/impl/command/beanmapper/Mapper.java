/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.command.beanmapper;

import java.util.Map;
import java.util.function.Consumer;

import org.iplass.mtp.command.beanmapper.MappingError;
import org.iplass.mtp.command.beanmapper.MappingResult;

public interface Mapper {
	
	public void setTrim(boolean trim);
	public void setEmptyToNull(boolean emptyToNull);
	public void setAutoGrow(boolean autoGrow);
	public void setIndexedPropertySizeLimit(int indexedPropertySizeLimit);
	public void setTypeConversionErrorHandler(Consumer<MappingError> typeConversionErrorHandler);
	public void setTargetBean(Object bean);
	public void map(Map<String, Object> valueMap, MappingResult result);
	public Object getValue(String propPath);

}
