/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.tools;

import java.util.Locale;
import java.util.ResourceBundle;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.util.ResourceBundleUtil;
import org.iplass.mtp.util.StringUtil;

public class ToolsBatchResourceBundleUtil {

	/** バッチ実行言語 VM KEY */
	public static final String KEY_BAT_LANG = "batch.language";

	/** Tool用リソースファイル名 */
	public static final String RESOURCE_NAME = "mtp-tools-batch-messages";

	private ToolsBatchResourceBundleUtil(){}

	/**
	 * バッチの実行言語を返します。
	 *
	 * @return 言語
	 */
	public static String getLanguage() {

		String lang = System.getProperty(KEY_BAT_LANG);

		if (StringUtil.isEmpty(lang) || "system".equals(lang.toLowerCase())) {
			//Systemのデフォルトを取得
			lang = Locale.getDefault().getLanguage();
		}
		lang = lang.toLowerCase();

		//enまたはja以外の場合はenにする
		if (!"ja".equals(lang) && !"en".equals(lang)) {
			lang = "en";
		}

		return lang;
	}

	/**
	 * バッチのResourceBundleを返します。
	 *
	 * @return ResourceBundle
	 */
	public static ResourceBundle getResourceBundle(String lang) {

		Locale langLocale = null;
		if (StringUtil.isNotEmpty(lang)) {
			langLocale = Locale.forLanguageTag(lang);
		} else {
			langLocale = ExecuteContext.getCurrentContext().getLangLocale();
		}
		return ResourceBundleUtil.getResourceBundle(RESOURCE_NAME, langLocale);
	}

	/**
	 * バッチのメッセージを返します。
	 *
	 * @param resource ResourceBundle
	 * @param key メッセージKEY
	 * @param arguments 引数
	 * @return メッセージ
	 */
	public static String resourceString(ResourceBundle resource, String key, Object... arguments) {
		return ResourceBundleUtil.resourceString(resource, key, arguments);
	}

}
