/*
 * Copyright (C) 2019 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.oauth;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextAreaItem;

import com.smartgwt.client.types.TextAreaWrap;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;

public final class CredentialResultDialog  extends MtpDialog {

	public CredentialResultDialog(String secret) {

		setHeight(220);
		setTitle("Credential Result");
		centerInPage();

		DynamicForm form = new MtpForm();

		TextAreaItem txaSecret = new MtpTextAreaItem();
		txaSecret.setTitle(AdminClientMessageUtil.getString("ui_metadata_oauth_CredentialResultDialog_secret"));
		txaSecret.setHeight(105);
		txaSecret.setWrap(TextAreaWrap.HARD);
		txaSecret.setValue(secret);

		form.setItems(txaSecret);

		container.setMembers(form);

		IButton btnOK = new IButton("OK");
		btnOK.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				destroy();
			}
		});

		footer.setMembers(btnOK);
	}

}
