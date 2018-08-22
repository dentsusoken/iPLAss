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

package org.iplass.adminconsole.client.metadata.ui.action;

import org.iplass.adminconsole.client.metadata.ui.action.cache.CacheCriteriaTypeEditPane;
import org.iplass.adminconsole.client.metadata.ui.action.cache.JavaClassCacheCriteriaEditPane;
import org.iplass.adminconsole.client.metadata.ui.action.cache.ParameterMatchCacheCriteriaGridEditPane;
import org.iplass.adminconsole.client.metadata.ui.action.cache.ScriptingCacheCriteriaEditPane;
import org.iplass.mtp.web.actionmapping.definition.cache.CacheCriteriaDefinition;
import org.iplass.mtp.web.actionmapping.definition.cache.JavaClassCacheCriteriaDefinition;
import org.iplass.mtp.web.actionmapping.definition.cache.ParameterMatchCacheCriteriaDefinition;
import org.iplass.mtp.web.actionmapping.definition.cache.ScriptingCacheCriteriaDefinition;

/**
 * CacheCriteriaの種類
 *
 * @author lis70i
 *
 */
public enum CacheCriteriaType {

	JAVACLASS("JavaClass", JavaClassCacheCriteriaDefinition.class),
	PARAMETERMATCH("ParameterMatch", ParameterMatchCacheCriteriaDefinition.class),
	SCRIPT("Script", ScriptingCacheCriteriaDefinition.class);

	private String displayName;
	private Class<CacheCriteriaDefinition> definitionClass;

	//Classに対してClass<CacheCriteriaDefinition>を指定するとコンパイルエラーになるため未指定
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private CacheCriteriaType(String displayName, Class definitionClass) {
		this.displayName = displayName;
		this.definitionClass = definitionClass;
	}

	public String displayName() {
		return displayName;
	}

	public Class<CacheCriteriaDefinition> definitionClass() {
		return definitionClass;
	}

	public static CacheCriteriaType valueOf(CacheCriteriaDefinition definition) {
		for (CacheCriteriaType type : values()) {
			//if (definition.getClass().isAssignableFrom(type.definitionClass)) {
			if (definition.getClass().getName().equals(type.definitionClass().getName())) {
				return type;
			}
		}
		return null;
	}

//	public static CacheCriteriaDefinition typeOfDefinition(CacheCriteriaType type) throws InstantiationException, IllegalAccessException {
//		return type.definitionClass().newInstance();
//	}
	public static CacheCriteriaDefinition typeOfDefinition(CacheCriteriaType type) {
		if (type.definitionClass().equals(JavaClassCacheCriteriaDefinition.class)) {
			return new JavaClassCacheCriteriaDefinition();
		} else if (type.definitionClass().equals(ParameterMatchCacheCriteriaDefinition.class)) {
			return new ParameterMatchCacheCriteriaDefinition();
		} else if (type.definitionClass().equals(ScriptingCacheCriteriaDefinition.class)) {
			return new ScriptingCacheCriteriaDefinition();
		}
		return null;
	}

	public static CacheCriteriaTypeEditPane typeOfEditPane(CacheCriteriaType type) {
		if (type.definitionClass().equals(JavaClassCacheCriteriaDefinition.class)) {
			return new JavaClassCacheCriteriaEditPane();
		} else if (type.definitionClass().equals(ParameterMatchCacheCriteriaDefinition.class)) {
			return new ParameterMatchCacheCriteriaGridEditPane();
		} else if (type.definitionClass().equals(ScriptingCacheCriteriaDefinition.class)) {
			return new ScriptingCacheCriteriaEditPane();
		}
		return null;
	}
}
