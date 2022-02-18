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
package org.iplass.adminconsole.server.base.rpc.screen;

import org.iplass.adminconsole.shared.base.rpc.screen.ScreenModuleService;
import org.iplass.adminconsole.shared.base.rpc.screen.ScreenModuleType;

import com.google.gwt.user.server.rpc.XsrfProtectedServiceServlet;

/**
 * 画面モジュール用Service実装
 * 
 * @author Y.Ishida
 *
 */
public class ScreenModuleServiceImpl extends XsrfProtectedServiceServlet implements ScreenModuleService {

	private static final long serialVersionUID = -8773186849009881578L;

	@Override
	public ScreenModuleType getScreenModuleType() {

		boolean existsGem;
		try {
			Class.forName("org.iplass.gem.GemConfigService");
			existsGem = true;
		} catch (ClassNotFoundException e) {
			existsGem = false;
		}

		return existsGem ? ScreenModuleType.GEM : null;
	}

}
