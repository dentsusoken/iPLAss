/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.cluster;

import java.io.Serializable;
import java.util.HashMap;

public class Message implements Serializable {
	private static final long serialVersionUID = -7232626383853152949L;
	
	String eventName;
	HashMap<String, String> param;
	
	public Message() {
	}
	
	public Message(String eventName) {
		this.eventName = eventName;
	}
	
	public void addParameter(String name, String value) {
		if (param == null) {
			param = new HashMap<String, String>();
		}
		param.put(name, value);
	}
	
	public void removeParameter(String name) {
		if (param != null) {
			param.remove(name);
		}
	}
	
	public String getParameter(String name) {
		if (param == null) {
			return null;
		}
		return param.get(name);
	}
	
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public HashMap<String, String> getParam() {
		return param;
	}
	public void setParam(HashMap<String, String> param) {
		this.param = param;
	}

	@Override
	public String toString() {
		return "Message [eventName=" + eventName + ", param=" + param + "]";
	}
}
