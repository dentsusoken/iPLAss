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

package org.iplass.mtp.mail;

/**
 * htmlメールを送信する場合の本文を表す。
 * 
 * @author K.Higuchi
 *
 */
public class HtmlMessage {
	
	//TODO 画像埋め込み対応
//	private List<BinaryReference> attatchment;
	
	private String content;
	private String charset;
	
	public HtmlMessage() {
	}
	
	public HtmlMessage(String content, String charset) {
		this.content = content;
		this.charset = charset;
	}

	/**
	 * html本体を取得。
	 * 
	 * @return
	 */
	public String getContent() {
		return content;
	}

	/**
	 * html本体をセット。
	 * 
	 * @param content
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * htmlのcharsetを取得。
	 * 
	 * @return
	 */
	public String getCharset() {
		return charset;
	}

	/**
	 * htmlのcharsetをセット。
	 * 
	 * @param charset
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}
	
}
