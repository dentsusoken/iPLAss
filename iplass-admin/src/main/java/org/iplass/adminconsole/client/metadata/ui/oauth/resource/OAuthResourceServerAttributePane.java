/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.oauth.resource;

import org.iplass.adminconsole.client.base.ui.widget.EditablePane;
import org.iplass.mtp.auth.oauth.definition.OAuthResourceServerDefinition;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CanvasItem;
import com.smartgwt.client.widgets.layout.VLayout;

public class OAuthResourceServerAttributePane extends VLayout implements EditablePane<OAuthResourceServerDefinition> {

	private DynamicForm form;

	private CustomTokenIntrospectorGridPane gridCustomTokenIntrospector;

	public OAuthResourceServerAttributePane() {
		initialize();
	}

	@Override
	public void setDefinition(OAuthResourceServerDefinition definition) {
		gridCustomTokenIntrospector.setDefinition(definition);
	}

	@Override
	public OAuthResourceServerDefinition getEditDefinition(OAuthResourceServerDefinition definition) {
		gridCustomTokenIntrospector.getEditDefinition(definition);
		return definition;
	}

	@Override
	public boolean validate() {
		return gridCustomTokenIntrospector.validate();
	}

	private void initialize() {

		setMargin(5);
		setWidth100();
		setAutoHeight();

		//入力部分
		form = new DynamicForm();
		form.setWidth100();
		form.setNumCols(5);	//間延びしないように最後に１つ余分に作成
		form.setColWidths(100, 300, 100, 300, "*");
		form.setMargin(5);

		gridCustomTokenIntrospector = new CustomTokenIntrospectorGridPane();
		CanvasItem canvasCustomTokenIntrospector = new CanvasItem();
		canvasCustomTokenIntrospector.setTitle("Custom Token Introspector");
		canvasCustomTokenIntrospector.setCanvas(gridCustomTokenIntrospector);
		canvasCustomTokenIntrospector.setColSpan(3);
		canvasCustomTokenIntrospector.setStartRow(true);

		form.setItems(canvasCustomTokenIntrospector);

		addMember(form);
	}
}
