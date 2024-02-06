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

package org.iplass.mtp.impl.web.actionmapping;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.metadata.MetaDataRuntimeException;
import org.iplass.mtp.impl.script.Script;
import org.iplass.mtp.impl.script.ScriptContext;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.impl.util.KeyGenerator;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.web.actionmapping.definition.ParamMapDefinition;

//TODO ParamMap -> MetaParamMap
public class ParamMap implements MetaData {

	private static final long serialVersionUID = 7897422706786772704L;
	private static final String SCRIPT_PREFIX = "ParamMap_condition";

	private String name;
	private String mapFrom;
	private String condition;

	public ParamMap() {
	}

	public ParamMap(String name, String mapFrom) {
		this.name = name;
		this.mapFrom = mapFrom;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMapFrom() {
		return mapFrom;
	}

	public void setMapFrom(String mapFrom) {
		this.mapFrom = mapFrom;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	//Definition → Meta
	public void applyConfig(ParamMapDefinition definition) {
		name = definition.getName();
		mapFrom = definition.getMapFrom();
		condition = definition.getCondition();
	}

	//Meta → Definition
	public ParamMapDefinition currentConfig() {
		ParamMapDefinition definition = new ParamMapDefinition();
		definition.setName(name);
		definition.setMapFrom(mapFrom);
		definition.setCondition(condition);
		return definition;
	}

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}
	
	public ParamMapRuntime createRuntime() {
		return new ParamMapRuntime();
	}
	
	public class ParamMapRuntime {
		private Script conditionScript;
		private MapType type;
		private int depth;
		
		ParamMapRuntime() {
			if (condition != null && !condition.isEmpty()) {
				TenantContext tc = ExecuteContext.getCurrentContext().getTenantContext();
				ScriptEngine ss = tc.getScriptEngine();
				KeyGenerator keyGen = new KeyGenerator();
				conditionScript = ss.createScript(condition, SCRIPT_PREFIX + "_" + keyGen.generateId());
			}
			
			if (name == null) {
				throw new MetaDataRuntimeException("ParamMap's name must be specified.");
			}
			if (mapFrom == null) {
				throw new MetaDataRuntimeException("ParamMap's mapFrom must be specified.");
			}
			if (mapFrom.startsWith("{")) {
				//deprecated 昔の指定の仕方。いずれ消したい
				//{n}形式の場合は、fullpathの「n」番目を返す(0から)
				type = MapType.FULL_PATH;
				depth = Integer.parseInt(mapFrom.substring(1, mapFrom.length() - 1).trim());
			} else if (mapFrom.startsWith("${")) {
				String val = mapFrom.substring(2, mapFrom.length() - 1).trim();
				if (val.startsWith("subPath[")) {
					//${subPath[n]}形式、subPathのn番目
					type = MapType.SUB_PATH;
					depth = Integer.parseInt(val.substring(8, val.length() - 1).trim());
				} else if (val.startsWith("fullPath[")) {
					//${fullPath[n]}形式、fullPathのn番目
					type = MapType.FULL_PATH;
					depth = Integer.parseInt(val.substring(9, val.length() - 1).trim());
				} else if ("paths".equals(val)) {
					//deprecated 昔の指定の仕方。いずれ消したい
					//${paths}形式の場合は、PathからAction名を除いたものを返す
					type = MapType.PATHS;
				} else {
					//deprecated 昔の指定の仕方。いずれ消したい
					//${n}形式の場合は、Action名を除いたPathの「n」番目を返す(1から)
					type = MapType.SUB_PATH;
					depth = Integer.parseInt(val.trim());
				}
			} else {
				type = MapType.PARAM_ALIAS;
			}
			
		}
		
		public ParamMap getMetaData() {
			return ParamMap.this;
		}
		
		public boolean isTarget(VariableParameterValueMap paramValueMap) {
			if (conditionScript == null) {
				return true;
			}
			TenantContext tc = ExecuteContext.getCurrentContext().getTenantContext();
			ScriptEngine ss = tc.getScriptEngine();
			ScriptContext sc = ss.newScriptContext();
			sc.setAttribute("subPath", paramValueMap.getSubPaths());
			sc.setAttribute("fullPath", paramValueMap.getFullPaths());
			sc.setAttribute("paramMap", paramValueMap.getWrapped().getParamMap());
			
			Boolean ret = (Boolean) conditionScript.eval(sc);
			if (ret != null && ret.booleanValue()) {
				return true;
			} else {
				return false;
			}
		}
		
		public Object getParam(VariableParameterValueMap paramValueMap) {
			
			switch (type) {
			case PARAM_ALIAS:
				return paramValueMap.getWrapped().getParam(mapFrom);
			case FULL_PATH:
				String[] fullPaths = paramValueMap.getFullPaths();
				if (fullPaths.length > depth) {
					return fullPaths[depth];
				} else {
					return null;
				}
			case SUB_PATH:
				String[] subPaths = paramValueMap.getSubPaths();
				if (subPaths.length > depth) {
					return subPaths[depth];
				} else {
					return null;
				}
			case PATHS:
				return paramValueMap.getSubPath();
			default:
				return null;
			}
			
		}
		public Object[] getParams(VariableParameterValueMap paramValueMap) {
			switch (type) {
			case PARAM_ALIAS:
				return paramValueMap.getWrapped().getParams(mapFrom);
			case FULL_PATH:
				String[] fullPaths = paramValueMap.getFullPaths();
				if (fullPaths.length > depth) {
					return new String[]{fullPaths[depth]};
				} else {
					return null;
				}
			case SUB_PATH:
				String[] subPaths = paramValueMap.getSubPaths();
				if (subPaths.length > depth) {
					return new String[]{subPaths[depth]};
				} else {
					return null;
				}
			case PATHS:
				String paths = paramValueMap.getSubPath();
				if (paths != null) {
					return new String[]{paths};
				} else {
					return null;
				}
			default:
				return null;
			}
		}
	}
	
	private enum MapType {
		PARAM_ALIAS,
		FULL_PATH,
		SUB_PATH,
		PATHS;
	}

}
