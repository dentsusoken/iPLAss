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
package org.iplass.mtp.impl.webhook.template;

import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.webhook.template.definition.WebHookContent;
public class MetaWebHookContent implements MetaData {
	
	private static final long serialVersionUID = -619523045085876676L;

	/** webHook 内容のタイプ */
	private String charset;
	
	/** String content container */
	private String content;
	
	public MetaWebHookContent() {
	}
	public MetaWebHookContent(String charset, String content) {
		this.charset = charset;
		this.content = content;
	}



	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public MetaWebHookContent copy() {
		return ObjectUtil.deepCopy(this);
	}
	
	//Definition → Meta
	public void applyConfig(WebHookContent definition) {
		this.content = definition.getContent();
		this.charset = definition.getCharset();
	}

	//Meta → Definition
	public WebHookContent currentConfig() {
		WebHookContent definition = new WebHookContent();
		definition.setContent(this.content);
		definition.setCharset(this.charset);
		return definition;
	}

}
