/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
import java.util.List;
import java.util.Map;

import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;

public class SyntaxService implements Service {
	
	//TODO 設定ファイルなどから設定値を取得する
	
	
	private Map<String, SyntaxContext> contexts;
	
	public SyntaxService() {
	}
	
	public SyntaxContext getSyntaxContext(String contextName) {
		return contexts.get(contextName);
	}

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public void init(Config config) {
		contexts = new HashMap<String, SyntaxContext>();
		
		List<String> registList = config.getValues("syntaxRegister");
		for (String r: registList) {
			SyntaxRegister sr;
			try {
				sr = (SyntaxRegister) Class.forName(r).newInstance();
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("SyntaxRegister not found.", e);
			} catch (InstantiationException e) {
				throw new RuntimeException("SyntaxRegister can not instantiate.", e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException("SyntaxRegister can not instantiate.", e);
			}
			contexts.put(sr.getContextName(), new SyntaxContext(sr.getSyntax()));
		}
	}

}
