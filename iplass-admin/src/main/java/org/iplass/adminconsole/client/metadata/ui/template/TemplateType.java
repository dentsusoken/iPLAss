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

package org.iplass.adminconsole.client.metadata.ui.template;

import org.iplass.adminconsole.client.metadata.ui.template.report.ReportTemplateEditPane;
import org.iplass.mtp.web.template.definition.BinaryTemplateDefinition;
import org.iplass.mtp.web.template.definition.GroovyTemplateDefinition;
import org.iplass.mtp.web.template.definition.HtmlTemplateDefinition;
import org.iplass.mtp.web.template.definition.JspTemplateDefinition;
import org.iplass.mtp.web.template.definition.TemplateDefinition;
import org.iplass.mtp.web.template.report.definition.ReportTemplateDefinition;

/**
 * Templateの種類
 */
public enum TemplateType {

	JSP("Jsp", JspTemplateDefinition.class, false),
	GROOVY("GroovyTemplate", GroovyTemplateDefinition.class, false),
	HTML("Html (Text Resource)", HtmlTemplateDefinition.class, false),
	BINARY("Binary", BinaryTemplateDefinition.class, true),
	REPORT("Report", ReportTemplateDefinition.class, true);

	private final String displayName;
	private final Class<TemplateDefinition> definitionClass;
	private final boolean disableLayoutAction;

	//Classに対してClass<TemplateDefinition>を指定するとコンパイルエラーになるため未指定
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private TemplateType(String displayName, Class definitionClass, boolean disableLayoutAction) {
		this.displayName = displayName;
		this.definitionClass = definitionClass;
		this.disableLayoutAction = disableLayoutAction;
	}

	public String displayName() {
		return displayName;
	}

	public Class<TemplateDefinition> definitionClass() {
		return definitionClass;
	}

	public boolean isDisableLayoutAction() {
		return disableLayoutAction;
	}

	public static TemplateType valueOf(TemplateDefinition definition) {
		for (TemplateType type : values()) {
			//if (definition.getClass().isAssignableFrom(type.definitionClass)) {
			if (definition.getClass().getName().equals(type.definitionClass().getName())) {
				return type;
			}
		}
		return null;
	}

//	public static TemplateDefinition typeOfDefinition(TemplateType type) throws InstantiationException, IllegalAccessException {
//		return type.definitionClass().newInstance();
//	}
	public static TemplateDefinition typeOfDefinition(TemplateType type) {
		if (type.definitionClass().equals(GroovyTemplateDefinition.class)) {
			return new GroovyTemplateDefinition();
		} else if (type.definitionClass().equals(JspTemplateDefinition.class)) {
			return new JspTemplateDefinition();
		} else if (type.definitionClass().equals(HtmlTemplateDefinition.class)) {
			return new HtmlTemplateDefinition();
		} else if (type.definitionClass().equals(BinaryTemplateDefinition.class)) {
			return new BinaryTemplateDefinition();
		} else if (type.definitionClass().equals(ReportTemplateDefinition.class)) {
			return new ReportTemplateDefinition();
		}
		return null;
	}

	public static TemplateTypeEditPane typeOfEditPane(TemplateType type) {
		if (type.definitionClass().equals(GroovyTemplateDefinition.class)) {
			return new GroovyTemplateEditPane();
		} else if (type.definitionClass().equals(JspTemplateDefinition.class)) {
			return new JspTemplateEditPane();
		} else if (type.definitionClass().equals(HtmlTemplateDefinition.class)) {
			return new HtmlTemplateEditPane();
		} else if (type.definitionClass().equals(BinaryTemplateDefinition.class)) {
			return new BinaryTemplateEditPane();
		} else if (type.definitionClass().equals(ReportTemplateDefinition.class)) {
			return new ReportTemplateEditPane();
		}
		return null;
	}

}
