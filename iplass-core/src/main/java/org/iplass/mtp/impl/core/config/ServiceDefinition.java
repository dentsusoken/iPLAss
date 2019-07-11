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
import java.util.Arrays;

import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement
public class ServiceDefinition {
	
	private static final Logger logger = LoggerFactory.getLogger(ServiceDefinition.class);
	
	private String[] inherits;
	private String[] includes;
	
	private ServiceConfig[] service;

	public ServiceConfig[] getService() {
		return service;
	}

	public void setService(ServiceConfig[] service) {
		this.service = service;
	}
	
	public String[] getInherits() {
		return inherits;
	}

	public void setInherits(String[] inherits) {
		this.inherits = inherits;
	}

	public String[] getIncludes() {
		return includes;
	}

	public void setIncludes(String[] includes) {
		this.includes = includes;
	}

	public ServiceConfig search(String name) {
		if (service != null) {
			for (ServiceConfig sc: service) {
				if (name.equals(sc.id())) {
					return sc;
				}
			}
		}
		return null;
	}
	
	private ServiceConfig pull(ArrayList<ServiceConfig> myConfig, String id) {
		if (myConfig.size() != 0) {
			for (ServiceConfig sc: myConfig) {
				if (id.equals(sc.id())) {
					myConfig.remove(sc);
					return sc;
				}
			}
		}
		return null;
	}
	
	public void include(ServiceDefinition mixinDefinition) {
		ArrayList<ServiceConfig> mixinConfig;
		if (mixinDefinition.service == null) {
			mixinConfig = new ArrayList<>();
		} else {
			mixinConfig = new ArrayList<ServiceConfig>(Arrays.asList(mixinDefinition.service));
		}
		
		ArrayList<ServiceConfig> newList = new ArrayList<ServiceConfig>();
		if (service != null) {
			for (ServiceConfig sc: service) {
				ServiceConfig override = pull(mixinConfig, sc.id());
				if (override != null) {
					if (sc.isFinal()) {
						logger.warn(sc.id() + " is declared Final, so can not include configration.");
						newList.add(sc);
					} else {
						if (!override.isIfnone()) {
							if (override.isInherit()) {
								if (logger.isTraceEnabled()) {
									logger.trace(sc.id() + " is include and inherit current definition.");
								}
								newList.add(override.merge(sc));
							} else {
								if (logger.isTraceEnabled()) {
									logger.trace(sc.id() + " is replaced by included definition.");
								}
								newList.add(override);
							}
						} else {
							if (logger.isTraceEnabled()) {
								logger.trace(sc.id() + " is exists and including config declared ifnone. so can not include configration.");
							}
							newList.add(sc);
						}
					}
				} else {
					newList.add(sc);
				}
			}
		}
		if (mixinConfig.size() != 0) {
			newList.addAll(mixinConfig);
		}
		service = newList.toArray(new ServiceConfig[newList.size()]);
	}
	
	public void inherit(ServiceDefinition superDefinition) {
		ArrayList<ServiceConfig> myConfig;
		if (service == null) {
			myConfig = new ArrayList<>();
		} else {
			myConfig = new ArrayList<ServiceConfig>(Arrays.asList(service));
		}
		
		ArrayList<ServiceConfig> newList = new ArrayList<ServiceConfig>();
		if (superDefinition.getService() != null) {
			for (ServiceConfig sc: superDefinition.getService()) {
				ServiceConfig override = pull(myConfig, sc.id());
				if (override != null) {
					if (sc.isFinal()) {
						logger.warn(sc.id() + " is declared Final, so can not override configration.");
						newList.add(sc);
					} else {
						if (!override.isIfnone()) {
							if (override.isInherit()) {
								if (logger.isTraceEnabled()) {
									logger.trace(sc.id() + " is override parents definition.");
								}
								newList.add(override.merge(sc));
							} else {
								if (logger.isTraceEnabled()) {
									logger.trace(sc.id() + " is replace parents definition.");
								}
								newList.add(override);
							}
						} else {
							if (logger.isTraceEnabled()) {
								logger.trace(sc.id() + " is exists in parents definition and this config declared ifnone. so can not override configration.");
							}
						}
					}
				} else {
					newList.add(sc);
				}
			}
		}
		if (myConfig.size() != 0) {
			newList.addAll(myConfig);
		}
		service = newList.toArray(new ServiceConfig[newList.size()]);
	}
}
