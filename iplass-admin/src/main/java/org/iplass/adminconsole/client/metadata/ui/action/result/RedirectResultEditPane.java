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

package org.iplass.adminconsole.client.metadata.ui.action.result;

import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.web.actionmapping.definition.result.RedirectResultDefinition;
import org.iplass.mtp.web.actionmapping.definition.result.ResultDefinition;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;

public class RedirectResultEditPane extends ResultTypeEditPane {

	/** フォーム */
	private DynamicForm form;

	/** リダイレクトパス */
	private TextItem redirectPathField;

	/** AllowExternalLocationを利用するかのフラグ */
	private CheckboxItem allowExternalLocationField;

	/**
	 * コンストラクタ
	 */
	public RedirectResultEditPane() {

		setWidth100();

		//入力部分
		form = new MtpForm();
		form.setAutoHeight();

		redirectPathField = new MtpTextItem("redirectPath", "RedirectPath AttributeName");
		SmartGWTUtil.setRequired(redirectPathField);

		allowExternalLocationField = new CheckboxItem("allowExternalLocation", "Set AllowExternalLocation");

		form.setItems(redirectPathField, allowExternalLocationField);

		//配置
		addMember(form);
	}

	/**
	 * ResultDefinitionを展開します。
	 *
	 * @param definition RedirectResultDefinition
	 */
	@Override
	public void setDefinition(ResultDefinition definition) {
		RedirectResultDefinition redirect = (RedirectResultDefinition)definition;
		redirectPathField.setValue(redirect.getRedirectPath());
		allowExternalLocationField.setValue(redirect.isAllowExternalLocation());
	}

	/**
	 * 編集されたResultDefinition情報を返します。
	 *
	 * @return 編集ResultDefinition情報
	 */
	@Override
	public ResultDefinition getEditDefinition(ResultDefinition definition) {
		RedirectResultDefinition redirect = (RedirectResultDefinition)definition;
		redirect.setRedirectPath(SmartGWTUtil.getStringValue(redirectPathField));
		redirect.setAllowExternalLocation(SmartGWTUtil.getBooleanValue(allowExternalLocationField));
		return redirect;
	}

	/**
	 * 入力チェックを実行します。
	 *
	 * @return 入力チェック結果
	 */
	@Override
	public boolean validate() {
		return form.validate();
	}

}
