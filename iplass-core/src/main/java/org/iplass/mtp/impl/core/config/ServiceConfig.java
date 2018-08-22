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

package org.iplass.mtp.impl.core.config;

import java.util.Arrays;
import java.util.TreeSet;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class ServiceConfig {
	private String name;
	private String interfaceName;
	private String className;
	private String[] depend;
	private NameValue[] property;
	
	private boolean isFinal;
	private boolean isInherit = true;
	private boolean isIfnone = false;
	
	@XmlAttribute
	public boolean isIfnone() {
		return isIfnone;
	}
	public void setIfnone(boolean isIfnone) {
		this.isIfnone = isIfnone;
	}
	@XmlAttribute
	public boolean isInherit() {
		return isInherit;
	}
	public void setInherit(boolean isInherit) {
		this.isInherit = isInherit;
	}
	@XmlAttribute
	public boolean isFinal() {
		return isFinal;
	}
	public void setFinal(boolean isFinal) {
		this.isFinal = isFinal;
	}
	
	@XmlAttribute
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public NameValue[] getProperty() {
		return property;
	}
	public void setProperty(NameValue[] property) {
		this.property = property;
	}
	@XmlElement(name="interface")
	public String getInterfaceName() {
		return interfaceName;
	}
	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}
	@XmlElement(name="class")
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String[] getDepend() {
		return depend;
	}
	public void setDepend(String[] depend) {
		this.depend = depend;
	}
	
	
	public String id() {
		if (name != null) {
			return name;
		} else {
			if (interfaceName == null) {
				throw new NullPointerException("can not determine ServiceConfig's id. name or interface must specified.name=" + name + ",interfaceName=" + interfaceName + ",className=" + className);
			} else {
				return interfaceName;
			}
		}
	}
	
	public ServiceConfig merge(ServiceConfig superConfig) {
		
		ServiceConfig merged = new ServiceConfig();
		merged.interfaceName = superConfig.interfaceName;
		merged.name = superConfig.name;
		merged.isFinal = this.isFinal;
		merged.isIfnone = this.isIfnone;
		if (this.className != null) {
			merged.className = this.className;
		} else {
			merged.className = superConfig.className;
		}
		if (this.depend != null) {
			TreeSet<String> set = new TreeSet<String>();
			if (superConfig.depend != null) {
				set.addAll(Arrays.asList(superConfig.depend));
			}
			set.addAll(Arrays.asList(this.depend));
			merged.depend = set.toArray(new String[set.size()]);
		} else {
			merged.depend = superConfig.depend;
		}
		
		merged.property = NameValue.mergeNameValueArray(id(), property, superConfig.property);
		
		return merged;
	}
	
	

}
