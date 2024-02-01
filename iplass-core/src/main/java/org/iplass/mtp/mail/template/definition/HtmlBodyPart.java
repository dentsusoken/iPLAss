/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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
 * メールテンプレートHTMLメッセージ定義
 */
public class HtmlBodyPart implements Serializable {

	private static final long serialVersionUID = -6882298439376235559L;

	private String content;

	private String charset;

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

	/**
	 * 文字コードを取得します。
	 * @return 文字コード
	 */
	public String getCharset() {
		return charset;
	}

	/**
	 * 文字コードを設定します。
	 * @param charset 文字コード
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}
}
