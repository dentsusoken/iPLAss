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

package org.iplass.mtp.tools.gui;

import java.util.ResourceBundle;

import javax.swing.JFrame;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.tools.ToolsBatchResourceBundleUtil;

public abstract class MtpJFrameBase extends JFrame {

	private static final long serialVersionUID = -7811831007311276158L;

	/** 言語(locale名) */
	private String language;

	/** リソースバンドル */
	private ResourceBundle resourceBundle;

	public MtpJFrameBase() {
		setupLanguage();
	}

	public String getLanguage() {
		return language;
	}

	private void setupLanguage() {

		language = ToolsBatchResourceBundleUtil.getLanguage();
		resourceBundle = ToolsBatchResourceBundleUtil.getResourceBundle(language);

		ExecuteContext context = ExecuteContext.getCurrentContext();
		context.setLanguage(language);
	}

	protected String rs(String key, Object... args) {
		return ToolsBatchResourceBundleUtil.resourceString(resourceBundle, key, args);
	}
}
