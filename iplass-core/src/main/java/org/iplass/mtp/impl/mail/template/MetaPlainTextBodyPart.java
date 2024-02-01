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
import org.iplass.mtp.mail.template.definition.PlainTextBodyPart;

public class MetaPlainTextBodyPart extends MetaBodyPart {
	private static final long serialVersionUID = -4845912474240459353L;

	private String content;

	public MetaPlainTextBodyPart() {
	}

	public MetaPlainTextBodyPart(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

	//Definition → Meta
	public void applyConfig(PlainTextBodyPart definition) {
		content = definition.getContent();
	}

	//Meta → Definition
	public PlainTextBodyPart currentConfig() {
		PlainTextBodyPart definition = new PlainTextBodyPart();
		definition.setContent(content);
		return definition;
	}

}
