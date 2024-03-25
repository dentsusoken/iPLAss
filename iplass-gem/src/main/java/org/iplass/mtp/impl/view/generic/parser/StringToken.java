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

import jakarta.servlet.ServletException;
import jakarta.servlet.jsp.PageContext;

import org.iplass.mtp.view.generic.parser.Token;

public class StringToken implements Token {

	private String token;

	public StringToken(String token) {
		this.token = token;
	}

	@Override
	public void printOut(PageContext page) throws ServletException, IOException {
		if (token != null) {
			page.getOut().write(token);
		}
	}

	@Override
	public String getKey() {
		return null;
	}
}
