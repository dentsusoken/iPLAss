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

package org.iplass.adminconsole.client.metadata.ui.template;

import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.web.template.definition.JspTemplateDefinition;
import org.iplass.mtp.web.template.definition.TemplateDefinition;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;

public class JspTemplateEditPane extends TemplateTypeEditPane implements HasEditLocalizedStringDefinition {

	/** フォーム */
	private DynamicForm form;

	/** パス */
	private TextItem pathField;

	/**
	 * コンストラクタ
	 */
	public JspTemplateEditPane() {

		setWidth100();

		//入力部分
		form = new MtpForm();
		form.setWidth100();

		pathField = new MtpTextItem("path", "Path");
		SmartGWTUtil.setRequired(pathField);

		form.setItems(pathField);

		//配置
		addMember(form);
	}

	@Override
	public void setDefinition(TemplateDefinition definition) {
		JspTemplateDefinition jspDefinition = (JspTemplateDefinition)definition;
		pathField.setValue(jspDefinition.getPath());
	}

	@Override
	public TemplateDefinition getEditDefinition(TemplateDefinition definition) {
		JspTemplateDefinition jspDefinition = (JspTemplateDefinition)definition;
		jspDefinition.setPath(SmartGWTUtil.getStringValue(pathField));
		return jspDefinition;
	}

	@Override
	public boolean validate() {
		return form.validate();
	}

	@Override
	public boolean isFileUpload() {
		return false;
	}

	@Override
	public void setLocalizedStringDefinition(LocalizedStringDefinition definition) {
		pathField.setValue(definition.getStringValue());
	}

	@Override
	public LocalizedStringDefinition getEditLocalizedStringDefinition(LocalizedStringDefinition definition) {
		definition.setStringValue(SmartGWTUtil.getStringValue(pathField));
		return definition;
	}

	@Override
	public int getLocaleDialogHeight() {
		return 170;
	}

}
