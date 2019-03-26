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

package org.iplass.adminconsole.client.metadata.ui.oauth;

import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.TextAreaWrap;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public final class CredentialResultDialog  extends AbstractWindow {

	public CredentialResultDialog(String secret) {

		setHeight(200);
		setWidth(500);
		setTitle("Credential Result");

		setShowMinimizeButton(false);
		setShowMaximizeButton(false);
		setCanDragResize(true);
		setIsModal(true);
		setShowModalMask(true);

		centerInPage();

		DynamicForm form = new DynamicForm();
		form.setWidth100();
		form.setNumCols(3);	//間延びしないように最後に１つ余分に作成
		form.setColWidths(100, 300, "*");
		form.setMargin(5);

		TextAreaItem txaSecret = new TextAreaItem();
		txaSecret.setTitle("Secret");
		txaSecret.setWidth("100%");
		txaSecret.setHeight(105);
		txaSecret.setBrowserSpellCheck(false);
		txaSecret.setWrap(TextAreaWrap.HARD);
		txaSecret.setValue(secret);

		form.setItems(txaSecret);

		VLayout contents = new VLayout(5);
		contents.setHeight100();
		contents.setOverflow(Overflow.AUTO);
		contents.setMembers(form);

		HLayout footer = new HLayout(5);
		footer.setMargin(10);
		footer.setAutoHeight();
		footer.setWidth100();
		footer.setAlign(VerticalAlignment.CENTER);
		footer.setOverflow(Overflow.VISIBLE);

		IButton btnOK = new IButton("OK");
		btnOK.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				destroy();
			}
		});

		footer.setMembers(btnOK);

		addItem(contents);
		addItem(SmartGWTUtil.separator());
		addItem(footer);

	}

}
