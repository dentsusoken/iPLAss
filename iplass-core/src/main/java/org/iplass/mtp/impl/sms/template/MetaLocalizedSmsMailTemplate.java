/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.sms.template;

import org.iplass.mtp.impl.mail.template.MetaPlainTextBodyPart;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.sms.template.definition.LocalizedSmsMailTemplateDefinition;

/**
 * 特定ロケールを表現するSMSテンプレートのメタデータ.
 */
public class MetaLocalizedSmsMailTemplate implements MetaData {

	private static final long serialVersionUID = -4137737997417644384L;
	private String localeName;
	private MetaPlainTextBodyPart message;
	
	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}
	
	public void applyConfig(LocalizedSmsMailTemplateDefinition d) {
		this.localeName = d.getLocaleName();

		if (d.getMessage() != null) {
			message = new MetaPlainTextBodyPart();
			message.setContent(d.getMessage().getContent());
		} else {
			message = null;
		}
	}
	
	public LocalizedSmsMailTemplateDefinition currentConfig() {
		LocalizedSmsMailTemplateDefinition d = new LocalizedSmsMailTemplateDefinition();
		d.setLocaleName(getLocaleName());
		d.setMessage(message.currentConfig());
		return d;
	}
	
	public String getLocaleName() {
		return localeName;
	}

	public void setLocaleName(String localeName) {
		this.localeName = localeName;
	}

	public MetaPlainTextBodyPart getMessage() {
		return message;
	}

	public void setMessage(MetaPlainTextBodyPart message) {
		this.message = message;
	}

}
