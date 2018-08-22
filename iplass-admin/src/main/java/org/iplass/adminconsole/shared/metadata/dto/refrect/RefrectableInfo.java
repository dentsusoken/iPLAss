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

package org.iplass.adminconsole.shared.metadata.dto.refrect;

import java.io.Serializable;
import java.util.Map;

import org.iplass.adminconsole.view.annotation.Refrectable;

public class RefrectableInfo implements Serializable {
	private static final long serialVersionUID = -6530626842022603933L;
	private Refrectable value;
	private Map<String, Serializable> valueMap;
	public Refrectable getValue() {
		return value;
	}
	public void setValue(Refrectable value) {
		this.value = value;
	}
	public Map<String, Serializable> getValueMap() {
		return valueMap;
	}
	public void setValueMap(Map<String, Serializable> valueMap) {
		this.valueMap = valueMap;
	}

}
