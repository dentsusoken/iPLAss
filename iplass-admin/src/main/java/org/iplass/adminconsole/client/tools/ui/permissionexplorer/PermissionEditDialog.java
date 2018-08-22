/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.tools.ui.permissionexplorer;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.entity.GenericEntity;

import com.smartgwt.client.types.Side;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

public class PermissionEditDialog extends AbstractWindow {

	private TabSet tabSet;

	/** 個別属性部分 */
	private List<PermissionEditPane> permissionEditPaneList;

	/** フッター */
	private Canvas footerLine;
	private HLayout footer;

	/** データ変更ハンドラ */
	private List<DataChangedHandler> handlers = new ArrayList<DataChangedHandler>();

	public PermissionEditDialog() {

		setTitle("Edit Permission");
		setWidth(550);
		setHeight(570);
		setMinWidth(550);
		setMinHeight(570);

		setShowMinimizeButton(false);
		setShowMaximizeButton(true);
		setCanDragResize(true);

		setIsModal(true);
		setShowModalMask(true);

		centerInPage();

		IButton save = new IButton("OK");
		save.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				boolean typeValidate = true;

				for (PermissionEditPane permissionEditPane: permissionEditPaneList) {
					typeValidate = typeValidate && permissionEditPane.validate();
				}

				if (typeValidate) {
					savePermission();
				}
			}
		});

		IButton cancel = new IButton("Cancel");
		cancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				destroy();
			}
		});

		footerLine = SmartGWTUtil.separator();
		footer = new HLayout(5);
		footer.setMargin(5);
		footer.setHeight(20);
		footer.setWidth100();
		footer.setAlign(VerticalAlignment.CENTER);
		footer.setMembers(save, cancel);

		permissionEditPaneList = new ArrayList<PermissionEditPane>();
	}

	public void setPermissionEditPane(PermissionEditPane permissionEditPane) {


		if (tabSet == null || tabSet.getTabs().length < 1) {
			permissionEditPaneList.add(permissionEditPane);

			tabSet = new TabSet();
			tabSet.setTabBarPosition(Side.TOP);

			VLayout mainLayout = new VLayout();
			mainLayout.setWidth100();
			mainLayout.setMargin(5);

			mainLayout.addMember(tabSet);

			Tab entityPermissionTab = new Tab("PermissionData");
			entityPermissionTab.setPane(permissionEditPane);
			tabSet.addTab(entityPermissionTab);

			addItem(mainLayout);
			addItem(footerLine);
			addItem(footer);
		} else {
			permissionEditPaneList.add(permissionEditPane);
			Tab entityPermissionTab = new Tab("PermissionData");
			entityPermissionTab.setPane(permissionEditPane);
			tabSet.addTab(entityPermissionTab);
		}

	}

	/**
	 * {@link DataChangedHandler} を追加します。
	 *
	 * @param handler {@link DataChangedHandler}
	 */
	public void addDataChangeHandler(DataChangedHandler handler) {
		handlers.add(0, handler);
	}

	private void savePermission() {
		//データ変更を通知
		GenericEntity[] entityArray = new GenericEntity[permissionEditPaneList.size()];

		int cnt = 0;
		for (PermissionEditPane permissionEditPane : permissionEditPaneList) {
			entityArray[cnt] = permissionEditPane.getEditEntity();
			cnt ++;
		}
		fireDataChanged(entityArray);

		//ダイアログ消去
		destroy();
	}

	/**
	 * データ変更通知処理
	 */
	private void fireDataChanged(GenericEntity[] entity) {
		DataChangedEvent event = new DataChangedEvent();
		event.setValueObject(entity);
		for (DataChangedHandler handler : handlers) {
			handler.onDataChanged(event);
		}
	}
}
