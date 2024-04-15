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

package org.iplass.mtp.impl.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

public class WebResponseWrapper extends HttpServletResponseWrapper {

	private Writer writer = null;

	public WebResponseWrapper(HttpServletResponse response) {
		super(response);
		writer = new StringWriter();
	}

	public WebResponseWrapper(HttpServletResponse response, Writer writer) {
		super(response);
		this.writer = writer;
	}

	@Override
    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(writer);
    }

	@Override
    public String toString() {
        return writer.toString();
    }
}
