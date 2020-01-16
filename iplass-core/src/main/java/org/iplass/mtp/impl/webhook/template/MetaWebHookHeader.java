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
import org.iplass.mtp.webhook.template.definition.WebHookHeader;

/**
 * @author lisf06
 *
 */
public class MetaWebHookHeader implements MetaData {

	private static final long serialVersionUID = 1113045625739189908L;
	
	private String key;
	private String Value;
	
	public MetaWebHookHeader() {
		
	}
	public MetaWebHookHeader(String key, String value) {
		this.key = key;
		this.Value = value;
	}	
	
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getValue() {
		return Value;
	}
	
	public void setValue(String value) {
		Value = value;
	}
	
	@Override
	public MetaWebHookHeader copy() {
		return ObjectUtil.deepCopy(this);
	}
	
	//Definition → Meta
	public void applyConfig (WebHookHeader definition) {
		this.key = definition.getKey();
		this.Value = definition.getValue();
	}
	//Meta → Definition
	public WebHookHeader currentConfig() {
		WebHookHeader definition = new WebHookHeader();
		definition.setKey(this.key);
		definition.setValue(this.Value);
		return definition;
	}

}
