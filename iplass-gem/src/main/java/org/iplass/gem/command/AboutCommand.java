/*
 * Copyright (C) 2018 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.gem.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletContext;

import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.action.ActionMapping;
import org.iplass.mtp.command.annotation.action.ActionMapping.ClientCacheType;
import org.iplass.mtp.command.annotation.action.Result;
import org.iplass.mtp.command.annotation.action.Result.Type;
import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.util.StringUtil;

@ActionMapping(
	name=AboutCommand.ACTION_NAME,
	clientCacheType=ClientCacheType.NO_CACHE,
	displayName="About画面表示",
	result={
		@Result(status=Constants.CMD_EXEC_SUCCESS, type=Type.JSP,
				value="/jsp/gem/layout/about.jsp",
				templateName="gem/layout/about",
				layoutActionName=Constants.LAYOUT_POPOUT_ACTION),
	}
)
@CommandClass(name="gem/layout/AboutCommand", displayName="About画面表示")
public final class AboutCommand implements Command {

	public static final String ACTION_NAME = "gem/about";

	private static final String RESOUCE_NOTICE = "/META-INF/NOTICE";

	@Override
	public String execute(RequestContext request) {

		ServletContext sc = WebRequestStack.getCurrent().getServletContext();

		String appName = sc.getServletContextName();
		if (StringUtil.isEmpty(appName)) {
			appName = "iPLAss";
		}
		request.setAttribute("appName", appName);

		List<String> noticeLines = getResourceLines(sc, RESOUCE_NOTICE);
		if (noticeLines.isEmpty()) {
			noticeLines.add("not found notice resource.");
		}
		request.setAttribute("notice", noticeLines);

		return Constants.CMD_EXEC_SUCCESS;

	}

	private List<String> getResourceLines(ServletContext sc, String path) {

		URL resource;
		try {
			resource = sc.getResource(path);
		} catch (MalformedURLException e) {
			throw new ApplicationException(e);
		}

		List<String> lines = new ArrayList<>();

		if (resource != null) {
			try (InputStreamReader isr = new InputStreamReader(resource.openStream(), "UTF8");
					BufferedReader br = new BufferedReader(isr)) {
				String line = br.readLine();
				while (line != null){
					lines.add(line);
					line = br.readLine();
				}
			} catch (IOException e) {
				throw new ApplicationException(e);
			}
		}

		return lines;
	}
}
