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

package org.iplass.adminconsole.client.metadata.ui.entity.layout.metafield;

import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.view.annotation.Refrectable;

import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickHandler;

/**
 * インターフェースクラス(org.iplass.mtpパッケージ配下)の各フィールドに値を設定するためのWindow。
 * {@link org.iplass.adminconsole.view.annotation.MetaFieldInfo}が設定されたフィールドのみが対象。
 *
 * 現状、EntityViewでのみ使用しているが、commonにあるので今後他のところでも利用することを想定。
 * EntityViewについては、{@link org.iplass.adminconsole.client.metadata.ui.entity.layout.item.EntityViewFieldSettingDialog}として継承して利用。
 *
 */
public class MetaFieldSettingDialog extends MtpDialog {

	private String className;
	private Refrectable value;

	private MetaFieldSettingPane panel = null;

	private IButton btnOK;
	private IButton btnCancel;

	private MetaFieldSettingDialog() {

		setHeight(600);

		btnOK = new IButton("OK");
		btnCancel = new IButton("Cancel");

		footer.setMembers(btnOK, btnCancel);
	}

	public MetaFieldSettingDialog(String className, Refrectable value) {
		this();
		this.className = className;
		this.value = value;
	}

	public void setOkHandler(MetaFieldUpdateHandler handler) {
		if (panel != null) {
			panel.setOkHandler(handler);
		}
	}

	public void setCancelHandler(MetaFieldUpdateHandler handler) {
		if (panel != null) {
			panel.setCancelHandler(handler);
		}
	}

	public void init() {

		if (className != null) {
			setTitle(className.substring(className.lastIndexOf(".") + 1) + " Setting");

			panel = createPane(className, value);
			container.addMember(panel);
		}
		centerInPage();
	}

	protected MetaFieldSettingPane createPane(String className, Refrectable value) {
		MetaFieldSettingPane pane = new MetaFieldSettingPane(this, className, value);
		pane.init();
		return pane;
	}

	protected HandlerRegistration addOKClickHandler(ClickHandler handler) {
		return btnOK.addClickHandler(handler);
	}

	protected HandlerRegistration addCancelClickHandler(ClickHandler handler) {
		return btnCancel.addClickHandler(handler);
	}

}
