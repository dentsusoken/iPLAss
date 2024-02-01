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

package org.iplass.adminconsole.client.metadata.ui.action;

import org.iplass.adminconsole.client.metadata.ui.action.result.DynamicTemplateResultEditPane;
import org.iplass.adminconsole.client.metadata.ui.action.result.RedirectResultEditPane;
import org.iplass.adminconsole.client.metadata.ui.action.result.ResultTypeEditPane;
import org.iplass.adminconsole.client.metadata.ui.action.result.StaticResourceResultEditPane;
import org.iplass.adminconsole.client.metadata.ui.action.result.StreamResultEditPane;
import org.iplass.adminconsole.client.metadata.ui.action.result.TemplateResultEditPane;
import org.iplass.mtp.web.actionmapping.definition.result.DynamicTemplateResultDefinition;
import org.iplass.mtp.web.actionmapping.definition.result.RedirectResultDefinition;
import org.iplass.mtp.web.actionmapping.definition.result.ResultDefinition;
import org.iplass.mtp.web.actionmapping.definition.result.StaticResourceResultDefinition;
import org.iplass.mtp.web.actionmapping.definition.result.StreamResultDefinition;
import org.iplass.mtp.web.actionmapping.definition.result.TemplateResultDefinition;

/**
 * Resultの種類
 *
 * @author lis70i
 *
 */
public enum ResultType {

	DYNAMICTEMPLATE("DynamicTemplate", DynamicTemplateResultDefinition.class),
	REDIRECT("Redirect", RedirectResultDefinition.class),
	STATICRESOURCE("StaticResource", StaticResourceResultDefinition.class),
	STREAM("Stream", StreamResultDefinition.class),
	TEMPLATE("Template", TemplateResultDefinition.class);

	private String displayName;
	private Class<ResultDefinition> definitionClass;

	//Classに対してClass<ResultDefinition>を指定するとコンパイルエラーになるため未指定
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private ResultType(String displayName, Class definitionClass) {
		this.displayName = displayName;
		this.definitionClass = definitionClass;
	}

	public String displayName() {
		return displayName;
	}

	public Class<ResultDefinition> definitionClass() {
		return definitionClass;
	}

	public static ResultType valueOf(ResultDefinition definition) {
		for (ResultType type : values()) {
			//if (definition.getClass().isAssignableFrom(type.definitionClass)) {
			if (definition.getClass().getName().equals(type.definitionClass().getName())) {
				return type;
			}
		}
		return null;
	}

//	public static ResultDefinition typeOfDefinition(ResultType type) throws InstantiationException, IllegalAccessException {
//		return type.definitionClass().newInstance();
//	}
	public static ResultDefinition typeOfDefinition(ResultType type) {
		if (type.definitionClass().equals(DynamicTemplateResultDefinition.class)) {
			return new DynamicTemplateResultDefinition();
		} else if (type.definitionClass().equals(RedirectResultDefinition.class)) {
			return new RedirectResultDefinition();
		} else if (type.definitionClass().equals(StaticResourceResultDefinition.class)) {
			return new StaticResourceResultDefinition();
		} else if (type.definitionClass().equals(StreamResultDefinition.class)) {
			return new StreamResultDefinition();
		} else if (type.definitionClass().equals(TemplateResultDefinition.class)) {
			return new TemplateResultDefinition();
		}
		return null;
	}

	public static ResultTypeEditPane typeOfEditPane(ResultType type) {
		if (type.definitionClass().equals(DynamicTemplateResultDefinition.class)) {
			return new DynamicTemplateResultEditPane();
		} else if (type.definitionClass().equals(RedirectResultDefinition.class)) {
			return new RedirectResultEditPane();
		} else if (type.definitionClass().equals(StaticResourceResultDefinition.class)) {
			return new StaticResourceResultEditPane();
		} else if (type.definitionClass().equals(StreamResultDefinition.class)) {
			return new StreamResultEditPane();
		} else if (type.definitionClass().equals(TemplateResultDefinition.class)) {
			return new TemplateResultEditPane();
		}
		return null;
	}
}
