/*
 * Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.entity.viewcontrol;

import java.util.List;

import org.iplass.mtp.view.generic.ViewControlSetting;

import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class ViewControlSettingPane extends VLayout {

	private ViewControlSettingListGrid grid;

	private IButton delButton;

	public ViewControlSettingPane() {

		setHeight(150);
		setMembersMargin(5);
		setMargin(5);

		grid = new ViewControlSettingListGrid();
		grid.addSelectionChangedHandler(new SelectionChangedHandler() {

			@Override
			public void onSelectionChanged(SelectionEvent event) {
				ViewControlSettingListGridRecord record = (ViewControlSettingListGridRecord) event.getRecord();
				if (record.isExistDetailView() || record.isExistSearchView()) {
					delButton.disable();
				} else {
					delButton.enable();
				}
			}
		});

		// 追加ボタン
		IButton addButton = new IButton("Add");
		addButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				grid.showAddViewControlSettingDialog();
			}
		});
		// 削除ボタン
		delButton = new IButton("Remove");
		delButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				grid.removeSelectedData();
				delButton.disable();
			}
		});
		delButton.disable();

		// EventListenerボタン用レイアウト
		HLayout buttonPane = new HLayout(5);
		buttonPane.setMargin(5);
		buttonPane.addMember(addButton);
		buttonPane.addMember(delButton);

		addMember(grid);
		addMember(buttonPane);
	}

	public void setDefinition(List<ViewControlSetting> settings, List<String> detailViewNames, List<String> searchViewNames) {
		grid.setDefinition(settings, detailViewNames, searchViewNames);
	}

	public List<ViewControlSetting> getEditDefinition() {
		return grid.getEditDefinition();
	}

}
