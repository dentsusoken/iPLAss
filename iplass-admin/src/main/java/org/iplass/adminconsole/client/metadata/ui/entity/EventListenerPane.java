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

package org.iplass.adminconsole.client.metadata.ui.entity;

import org.iplass.mtp.entity.definition.EntityDefinition;

import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * EventListener用
 *
 * @author lis2s8
 *
 */
public class EventListenerPane extends VLayout {

	private EventListenerListGrid grid;

	public EventListenerPane() {

		setMembersMargin(5);
		setMargin(5);

		grid = new EventListenerListGrid();

		// 追加ボタン
		IButton addEventListener = new IButton("Add");
		addEventListener.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				grid.startEventListenerEdit(true, new EventListenerListGridRecord());
			}
		});
		// 削除ボタン
		IButton delEventListener = new IButton("Remove");
		delEventListener.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				grid.removeSelectedData();
			}
		});

		// EventListenerボタン用レイアウト
		HLayout elButtonPane = new HLayout(5);
		elButtonPane.setMargin(5);
		elButtonPane.addMember(addEventListener);
		elButtonPane.addMember(delEventListener);

		// PaneにScript用TextAreaとチェックボックス用Layoutをadd
		addMember(grid);
		addMember(elButtonPane);
	}

	public void setDefinition(EntityDefinition definition) {
		grid.setDefinition(definition);
	}

	public EntityDefinition getEditDefinition(EntityDefinition definition) {
		grid.getEditDefinition(definition);
		return definition;
	}

	/**
	 * 入力チェックを実行します。
	 *
	 * @return 入力チェック結果
	 */
	public boolean validate() {
		return true;
	}

	/**
	 * エラー表示をクリアします。
	 */
	public void clearErrors() {
	}


//	/**
//	 * データ再読込
//	 */
//	public void reloadGridData() {
//		if (grid != null) {
//			grid.reloadData();
//		}
//	}
}
