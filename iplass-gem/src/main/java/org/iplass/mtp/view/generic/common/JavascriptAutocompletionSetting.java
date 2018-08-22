/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.view.generic.common;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.adminconsole.view.annotation.generic.EntityViewField;

/**
 * Javascriptを利用した自動補完設定
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class JavascriptAutocompletionSetting extends AutocompletionSetting {

	private static final long serialVersionUID = -8446003691620843214L;

	/** Javascript */
	@MetaFieldInfo(
		displayName="Javascript",
		displayNameKey="generic_common_JavascriptAutocompletionSetting_javascriptDisplaNameKey",
		inputType=InputType.SCRIPT,
		mode="javascript",
		description="連動元のプロパティが変更された際に実行する自動補完のJavascriptを設定します。<br>"
				+ "連動元のプロパティの値は以下の形式で変数になっています。<br>"
				+ "<pre>"
				+ "var pVal = {\r\n"
				+ "  プロパティ名1:プロパティ値,\r\n"
				+ "  プロパティ名2:[プロパティ値,...], *多重度が2以上の場合\r\n"
				+ "}"
				+ "</pre>",
		descriptionKey="generic_common_JavascriptAutocompletionSetting_javascriptDescriptionKey"
	)
	@EntityViewField()
	private String javascript;

	/**
	 * @return javascript
	 */
	public String getJavascript() {
		return javascript;
	}

	/**
	 * @param javascript セットする javascript
	 */
	public void setJavascript(String javascript) {
		this.javascript = javascript;
	}
}
