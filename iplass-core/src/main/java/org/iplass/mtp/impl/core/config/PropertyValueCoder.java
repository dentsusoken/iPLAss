/*
 * Copyright (C) 2022 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.core.config;

import java.util.Properties;

public interface PropertyValueCoder extends AutoCloseable {
	public static final String PROPERTY_VALUE_CODER = "propertyValueCoder";
	
	public default void open(Properties prop) {
	};
	
	@Override
	public default void close() {
	};

	public default String encode(String plain){
		return plain;
	};
	
	public default String decode(String encoded){
		return encoded;
	};
	
	public default void clear(){
	};
}
