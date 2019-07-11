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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NameValue {
	private static final Logger logger = LoggerFactory.getLogger(NameValue.class);
	
	private String name;
	private String value;
	private String textValue;
	
	private String className;
	private NameValue[] arg;
	private NameValue[] property;
	
	private boolean isEncrypted;
	private boolean isFinal;
	private boolean isAdditional;
	private boolean isInherit = true;
	private boolean isIfnone;
	
	private boolean isNull;
	
	private String ref;
	
	public NameValue() {
	}
	
	public NameValue(String name, String value) {
		this.name = name;
		this.value = value;
	}

	@XmlAttribute
	public boolean isIfnone() {
		return isIfnone;
	}
	public void setIfnone(boolean isIfnone) {
		this.isIfnone = isIfnone;
	}

	@XmlAttribute
	public boolean isEncrypted() {
		return isEncrypted;
	}

	public void setEncrypted(boolean isEncrypted) {
		this.isEncrypted = isEncrypted;
	}
	
	@XmlAttribute
	public boolean isInherit() {
		return isInherit;
	}

	public void setInherit(boolean isInherit) {
		this.isInherit = isInherit;
	}

	@XmlAttribute
	public boolean isAdditional() {
		return isAdditional;
	}

	public void setAdditional(boolean isAdditional) {
		this.isAdditional = isAdditional;
	}

	@XmlAttribute
	public boolean isFinal() {
		return isFinal;
	}
	public void setFinal(boolean isFinal) {
		this.isFinal = isFinal;
	}
	
	@XmlAttribute
	public boolean isNull() {
		return isNull;
	}

	public void setNull(boolean isNull) {
		this.isNull = isNull;
	}

	@XmlAttribute(name="class")
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public NameValue[] getArg() {
		return arg;
	}

	public void setArg(NameValue[] arg) {
		this.arg = arg;
	}

	public NameValue[] getProperty() {
		return property;
	}

	public void setProperty(NameValue[] property) {
		this.property = property;
	}

	@XmlAttribute
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@XmlAttribute
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	@XmlElement(name="value")
	public String getTextValue() {
		return textValue;
	}
	public void setTextValue(String textValue) {
		if (textValue != null) {
			textValue = textValue.trim();
		}
		this.textValue = textValue;
	}
	
	@XmlAttribute
	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}
	
	public String value() {
		if (value != null && value.length() != 0) {
			return value;
		}
		return textValue;
	}
	
	public NameValue merge(NameValue superNameValue) {
		NameValue merged = new NameValue();
		merged.name = name;
		merged.isAdditional = isAdditional;
		merged.isFinal = isFinal;
		merged.isEncrypted = isEncrypted;
		merged.isIfnone = isIfnone;
		
		if (ref != null) {
			//ref定義を優先
			merged.ref = ref;
		} else {
			merged.ref = superNameValue.ref;
			
			if (className != null) {
				merged.className = className;
			} else {
				merged.className = superNameValue.className;
			}
			
			if (textValue != null) {
				merged.textValue = textValue;
			} else {
				merged.textValue = superNameValue.textValue;
			}
			if (value != null) {
				merged.value = value;
			} else {
				merged.value = superNameValue.value;
			}
			
			merged.property = mergeNameValueArray(name, property, superNameValue.property);

			if (arg != null) {
				merged.arg = arg;
			} else {
				merged.arg = superNameValue.arg;
			}
		}
		
		return merged;
	}
	
	
	@SuppressWarnings("unchecked")
	static NameValue[] mergeNameValueArray(String targetName, NameValue[] myPropArray, NameValue[] superPropArray) {
		HashSet<String> noInheritPropList = new HashSet<String>();
		HashSet<String> finalPropList = new HashSet<String>();
		HashSet<String> superConfigPropList = new HashSet<String>();
		if (myPropArray != null) {
			
			//親の設定を継承しないプロパティ名を取得
			for (NameValue p: myPropArray) {
				if (!p.isInherit()) {
					noInheritPropList.add(p.getName());
				}
			}
			
			HashMap<String, Object> newProps = new HashMap<String, Object>();
			
			//継承元のプロパティを取得
			if (superPropArray != null) {
				for (NameValue p: superPropArray) {
					superConfigPropList.add(p.getName());
					if (p.isFinal()) {
						finalPropList.add(p.getName());
					}
					
					//継承しない設定されていない、もしくはオーバーライド禁止の場合
					if (!noInheritPropList.contains(p.getName()) || p.isFinal()) {
						Object np = newProps.get(p.getName());
						if (np == null) {
							newProps.put(p.getName(), p);
						} else {
							if (np instanceof ArrayList) {
								((ArrayList<NameValue>) np).add(p);
							} else {
								ArrayList<NameValue> pArray = new ArrayList<NameValue>();
								pArray.add((NameValue) np);
								pArray.add(p);
								newProps.put(p.getName(), pArray);
							}
						}
					}
				}
			}
			
			//自身のプロパティを取得し、オーバーライド
			HashSet<String> resetOverridePropList = new HashSet<String>();
			for (NameValue p: myPropArray) {
				Object sp = newProps.get(p.getName());
				if (finalPropList.contains(p.getName())) {
					//オーバーライドが許可されていないプロパティ
					logger.warn(targetName + "." + p.getName() + " is declared Final, so Can not override configration.");
				} else if (!superConfigPropList.contains(p.getName())
						|| noInheritPropList.contains(p.getName())) {
					//親定義では宣言されていないプロパティ、もしくはオーバーライドしないプロパティ
					if (logger.isTraceEnabled()) {
						if (noInheritPropList.contains(p.getName())) {
							logger.trace(targetName + "." + p.getName() + " is not inherit, so hole override parent's configration.");
						} else {
							logger.trace(targetName + "." + p.getName() + " is added.");
						}
					}
					if (sp == null) {
						newProps.put(p.getName(), p);
					} else if (sp instanceof ArrayList) {
						((ArrayList<NameValue>) sp).add(p);
					} else {
						ArrayList<NameValue> pArray = new ArrayList<NameValue>();
						pArray.add((NameValue) sp);
						pArray.add(p);
						newProps.put(p.getName(), pArray);
					}
				} else {
					//オーバーライドするプロパティ
					if (p.isAdditional()) {
						//additional mode
						if (logger.isTraceEnabled()) {
							logger.trace(targetName + "." + p.getName() + " is override(additional mode).");
						}
						if (sp instanceof ArrayList) {
							((ArrayList<NameValue>) sp).add(p);
						} else {
							ArrayList<NameValue> pArray = new ArrayList<NameValue>();
							pArray.add((NameValue) sp);
							pArray.add(p);
							newProps.put(p.getName(), pArray);
						}
					} else {
						if (!p.isIfnone() || !superConfigPropList.contains(p.getName())) {
							//override
							
							if (!resetOverridePropList.contains(p.getName())) {
								if (logger.isTraceEnabled()) {
									logger.trace(targetName + "." + p.getName() + " is override.");
								}
								resetOverridePropList.add(p.getName());
								if (sp instanceof ArrayList) {
									newProps.put(p.getName(), p.merge(((ArrayList<NameValue>) sp).get(0)));
								} else {
									newProps.put(p.getName(), p.merge((NameValue) sp));
								}
							} else {
								if (sp instanceof ArrayList) {
									((ArrayList<NameValue>) sp).add(p);
								} else {
									ArrayList<NameValue> pArray = new ArrayList<NameValue>();
									pArray.add((NameValue) sp);
									pArray.add(p);
									newProps.put(p.getName(), pArray);
								}
							}
						} else {
							if (logger.isTraceEnabled()) {
								logger.trace(targetName + "." + p.getName() + " is declared ifnone, so Can not override configration.");
							}
						}
					}
				}
			}
			
			//配列に変換
			ArrayList<NameValue> list = new ArrayList<NameValue>();
			for (Map.Entry<String, Object> e: newProps.entrySet()) {
				Object v = e.getValue();
				if (v instanceof ArrayList) {
					for (NameValue vv: (ArrayList<NameValue>) v) {
						list.add(vv);
					}
				} else {
					list.add((NameValue) v);
				}
			}
			return list.toArray(new NameValue[list.size()]);
		} else {
			//自身にプロパティ定義されてないので、継承元をそのまま利用
			return superPropArray;
		}
		
	}

	
}
