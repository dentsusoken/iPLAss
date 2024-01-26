/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.view.generic.parser;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.view.generic.editor.NestProperty;
import org.iplass.mtp.view.generic.parser.Token;

public class Parser {

	private List<NestProperty> properties;

	public Parser(List<NestProperty> properties) {
		this.properties = properties;
	}

	public List<Token> parse(String format) throws IOException {
		List<Token> tokenList = new ArrayList<Token>();
		if (format == null || format.isEmpty()) return tokenList;

		Reader reader = new StringReader(format);
		StringBuffer stringToken = new StringBuffer();

		int c = -1;
		while (true) {
			c = reader.read();
			if (c == -1) break;
			if (c == '$') {
				if (stringToken.length() > 0) {
					StringToken token = new StringToken(stringToken.toString());
					tokenList.add(token);
					stringToken = new StringBuffer();
				}
				c = reader.read();
				if (c == '{') {
					Token token = perseProperty(reader);
					if (token != null) tokenList.add(token);
					continue;
				}
			}
			stringToken.append((char) c);
		}
		if (stringToken.length() > 0) {
			StringToken token = new StringToken(stringToken.toString());
			tokenList.add(token);
			stringToken = new StringBuffer();
		}

		return tokenList;
	}

	private Token perseProperty(Reader reader) throws IOException {
		StringBuffer propertyName = new StringBuffer();
		while (true) {
			int c = reader.read();
			if (c == -1) break;
			if (c == '}') break;
			propertyName.append((char) c);
		}

		PropertyToken token = null;
		NestProperty property = getProperty(propertyName.toString());
		if (property != null) {
			property.setPropertyName(propertyName.toString());
			token = new PropertyToken(property);
		}
		return token;
	}

	public NestProperty getProperty(String name) {
		for (NestProperty property : properties) {
			if (property.getPropertyName().equals(name)) {
				return property;
			}
		}
		return null;
	}

}
