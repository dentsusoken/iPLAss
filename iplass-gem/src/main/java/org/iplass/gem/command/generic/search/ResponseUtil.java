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

package org.iplass.gem.command.generic.search;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.impl.web.WebResponseWrapper;

public class ResponseUtil {

	public static String getIncludeJspContents(String includePath, Func beforeIncludeFunction, Func afterIncludeFunction) throws ServletException, IOException {
		return getIncludeJspContents(includePath, beforeIncludeFunction, afterIncludeFunction, new StringWriter());
	}

	public static String getIncludeJspContents(String includePath, Func beforeFunction, Func afterFunction, Writer writer) throws ServletException, IOException {
		WebRequestStack stack = WebRequestStack.getCurrent();
		HttpServletRequest req = stack.getRequest();
		WebResponseWrapper res = new WebResponseWrapper(stack.getResponse(), writer);

		if (beforeFunction != null) beforeFunction.execute(req, res);
		RequestDispatcher dispatcher = req.getRequestDispatcher(includePath);
		dispatcher.include(req, res);
		if (afterFunction != null) afterFunction.execute(req, res);
		return res.toString();
	}

	public interface Func {
		public void execute(HttpServletRequest req, HttpServletResponse res);
	}
}
