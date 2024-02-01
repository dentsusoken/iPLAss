/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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


public class LocalizedMailTemplateDefinition implements Serializable {

	private static final long serialVersionUID = 1385150091910302424L;

	private String localeName;
	private String charset;
	private String subject;
	private PlainTextBodyPart plainMessage;
	private HtmlBodyPart htmlMessage;
	public String getLocaleName() {
		return localeName;
	}
	public void setLocaleName(String localeName) {
		this.localeName = localeName;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public PlainTextBodyPart getPlainMessage() {
		return plainMessage;
	}
	public void setPlainMessage(PlainTextBodyPart plainMessage) {
		this.plainMessage = plainMessage;
	}
	public HtmlBodyPart getHtmlMessage() {
		return htmlMessage;
	}
	public void setHtmlMessage(HtmlBodyPart htmlMessage) {
		this.htmlMessage = htmlMessage;
	}

}
