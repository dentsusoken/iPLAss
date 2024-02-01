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

package org.iplass.adminconsole.client.metadata.ui.top.item;

import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.mtp.view.top.parts.TopViewParts;

import com.smartgwt.client.types.HeaderControls;
import com.smartgwt.client.widgets.HeaderControl;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DoubleClickEvent;
import com.smartgwt.client.widgets.events.DoubleClickHandler;

/**
 *
 * @author lis3wg
 */
public abstract class PartsItem extends AbstractWindow {

	protected String dropAreaType;

	/**
	 * コンストラクタ
	 */
	public PartsItem() {
		setWidth100();
		setHeight(22);
		setCanDragReposition(true);
		setCanDrop(true);
		setShowEdges(false);
		setShowMinimizeButton(false);
		setBorder("1px solid navy");

		// ダミーで空のスタイルを指定しないとsmartget3.1からはsetBackgroundColorが有効にならない
		setStyleName("");

		setHeaderControls(HeaderControls.HEADER_LABEL, new HeaderControl(HeaderControl.SETTINGS, new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onOpen();
			}

		}), HeaderControls.CLOSE_BUTTON);

		//DoubleClickで設定画面表示
		addDoubleClickHandler(new DoubleClickHandler() {

			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				onOpen();
			}
		});
	}

	public abstract TopViewParts getParts();

	/**
	 * アイテムの設定画面を開きます。
	 */
	protected abstract void onOpen();

	public void setDropAreaType(String dropAreaType) {
		this.dropAreaType = dropAreaType;
	}

	public void doDropAction(DropActionCallback callback) {
		if (callback != null) callback.handle();
	}

	public interface DropActionCallback {
		void handle();
	}
}
