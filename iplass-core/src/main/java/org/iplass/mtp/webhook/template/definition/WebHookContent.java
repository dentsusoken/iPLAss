/*
 * Copyright (C) 2020 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.webhook.template.definition;

import java.io.Serializable;

public class WebHookContent implements Serializable {

	private static final long serialVersionUID = -344879145295664096L;

	//主にJSONが流行らしい、できれば要求された以外、JSONをおすすめします
	
	/** webHook 内容のタイプ */
	private String charset;
	
	/** String content container */
	private String content;
	
	public WebHookContent() {
		this.content = "";
	}
	

	
	public WebHookContent(String content, String charset) {
		setContent(content);
		setCharset(charset);
	}
	
	public void setContent (String content) {
		this.content = content;
	}
	
	
	public String getContent() {
		return String.valueOf(this.content);
	}
	
			
	
	

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	
	public WebHookContent copy() {
		return new WebHookContent(this.content, this.charset);
	}

}
