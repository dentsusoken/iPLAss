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

package org.iplass.adminconsole.client.base.ui.widget;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Label;

/**
 * 処理が長い場合に表示するProgress表示画面
 *
 * モーダルで処理中の画面が表示されます。
 *
 */
public class ProgressDialog extends AbstractWindow {

	private static final String PROGRESS_ICON = "[SKINIMG]/shared/progressCursorTracker.gif";

	private static final String PROGRESS_BGCOLOR = "#DDFFFF";
	private static final String PROGRESS_BORDER = "1px solid #B5B5B5;";

	private static ProgressDialog instance = null;

	public static void showProgress() {
		showProgress(null);
	}

	public static void showProgress(String contents) {
		if (instance == null) {
			instance = new ProgressDialog();
		}
		instance.setLabelContents(contents);

		//表示時に再度Center化
		instance.centerInPage();
		instance.show();
	}

	public static void hideProgress() {
		if (instance != null) {
			instance.hide();
		}
	}

	private Label label;

	private ProgressDialog() {
		this(null);
	}

	private ProgressDialog(String contents) {
		setWidth(300);
		setHeight(50);

		setIsModal(true);
		setShowModalMask(true);
		setShowTitle(false);
		setShowMinimizeButton(false);
		setShowMaximizeButton(false);
		setShowCloseButton(false);
		setCanDragReposition(false);
		setCanDragResize(false);
		setShowEdges(false);
		setShowHeader(false);

		//空のスタイルを指定して外枠を消す(windowBackgroundを解除)
		setStyleName("");
		//空のスタイルを指定してボーダを消す(windowBodyを解除。シャドウが嫌だったので)
		setBodyStyle("");

		centerInPage();

		label = new Label();
		label.setWidth100();
		label.setHeight100();
		label.setIcon(PROGRESS_ICON);
		label.setAlign(Alignment.CENTER);
		label.setValign(VerticalAlignment.CENTER);
		label.setWrap(false);
		label.setBorder(PROGRESS_BORDER);
		label.setBackgroundColor(PROGRESS_BGCOLOR);
		addItem(label);

		setLabelContents(contents);
	}

	private void setLabelContents(String contents) {
		if (contents == null) {
			label.setContents(AdminClientMessageUtil.getString("ui_common_ProgressDialog_processing"));
		} else {
			label.setContents(contents);
		}
	}

}
