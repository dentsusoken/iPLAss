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

package org.iplass.mtp.impl.parser;

import java.util.HashMap;
import java.util.Map;

public class SyntaxContext {
	
	private Map<Class<?>, Syntax<?>> map = new HashMap<>();
	
	public SyntaxContext(Syntax<?>... syntax) {
		if (syntax != null) {
			for (Syntax<?> s: syntax) {
				map.put(s.getClass(), s);
			}
			
			for (Syntax<?> s: syntax) {
				s.init(this);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Syntax<?>> T getSyntax(Class<T> type) {
		return (T) map.get(type);
	}
	
	
}
