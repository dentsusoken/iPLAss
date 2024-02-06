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

package org.iplass.mtp.impl.mail.template;

import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.mail.template.definition.HtmlBodyPart;

public class MetaHtmlBodyPart extends MetaBodyPart {
	private static final long serialVersionUID = 5022389794198292558L;

	private String content;
	private String charset;

	public MetaHtmlBodyPart() {
	}

	public MetaHtmlBodyPart(String content, String charset) {
		this.content = content;
		this.charset = charset;
	}

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	//Definition → Meta
	public void applyConfig(HtmlBodyPart definition) {
		content = definition.getContent();
		charset = definition.getCharset();
	}

	//Meta → Definition
	public HtmlBodyPart currentConfig() {
		HtmlBodyPart definition = new HtmlBodyPart();
		definition.setContent(content);
		definition.setCharset(charset);
		return definition;
	}

}
