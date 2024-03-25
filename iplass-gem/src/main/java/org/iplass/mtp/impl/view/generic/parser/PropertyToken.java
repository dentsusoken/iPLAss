/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.view.generic.parser;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.jsp.PageContext;

import org.iplass.gem.command.Constants;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.view.generic.EntityViewUtil;
import org.iplass.mtp.view.generic.ViewConst;
import org.iplass.mtp.view.generic.editor.NestProperty;
import org.iplass.mtp.view.generic.parser.Token;

public class PropertyToken implements Token {

	private NestProperty property;

	public PropertyToken(NestProperty property) {
		this.property = property;
	}

	@Override
	public void printOut(PageContext page) throws ServletException, IOException {
		if (property.getEditor() == null) return;

		ServletRequest request = page.getRequest();

		String designType = (String) request.getAttribute(ViewConst.DESIGN_TYPE);
		if (designType == null) designType = "";

		String path = EntityViewUtil.getJspPath(property.getEditor(), designType);
		if (path != null) {
			Boolean nest = (Boolean) request.getAttribute(Constants.EDITOR_REF_NEST);
			EntityDefinition ed = (EntityDefinition) request.getAttribute(Constants.ENTITY_DEFINITION);

			Entity entity = null;
			//ネストテーブルからの出力かどうかでデータを変える
			if (nest == null || !nest) {
				entity = (Entity) request.getAttribute(Constants.ENTITY_DATA);
				property.getEditor().setPropertyName(property.getPropertyName());
			} else {
				//NestTable
				entity = (Entity) request.getAttribute(Constants.EDITOR_REF_NEST_VALUE);
				String prefix = (String) request.getAttribute(Constants.EDITOR_JOIN_NEST_PREFIX);
				property.getEditor().setPropertyName(prefix + "." + property.getPropertyName());
			}

			PropertyDefinition pd = ed.getProperty(property.getPropertyName());

			Object propValue = null;
			if (entity != null) {
				propValue = entity.getValue(property.getPropertyName());
			}

			request.setAttribute(Constants.EDITOR_EDITOR, property.getEditor());
			request.setAttribute(Constants.EDITOR_PROP_VALUE, propValue);
			request.setAttribute(Constants.EDITOR_PROPERTY_DEFINITION, pd);

			page.include(path);
		}
	}

	@Override
	public String getKey() {
		return property.getPropertyName();
	}
}
