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

package org.iplass.mtp.mail.template.definition;

import java.io.Serializable;

/**
 * メールテンプレートテキストメッセージ定義
 */
public class PlainTextBodyPart implements Serializable {

	private static final long serialVersionUID = -6817907840182695794L;

	private String content;

	/**
	 * コンテンツを取得します。
	 * @return コンテンツ
	 */
	public String getContent() {
		return content;
	}

	/**
	 * コンテンツを設定します。
	 * @param content コンテンツ
	 */
	public void setContent(String content) {
		this.content = content;
	}

}
