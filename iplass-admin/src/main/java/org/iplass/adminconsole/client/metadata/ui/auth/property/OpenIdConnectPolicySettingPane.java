/*
 * Copyright (C) 2022 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.auth.property;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.ui.widget.EditablePane;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataComboBoxItem;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataComboBoxItem.ItemOption;
import org.iplass.mtp.auth.oidc.definition.OpenIdConnectDefinition;
import org.iplass.mtp.auth.policy.definition.AuthenticationPolicyDefinition;

import com.smartgwt.client.types.AutoFitWidthApproach;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.CanvasItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class OpenIdConnectPolicySettingPane extends AbstractSettingPane {

	private OpenIdConnectGridPane pnlOpenIdConnectGrid;

	public OpenIdConnectPolicySettingPane() {

		form.setGroupTitle("OpenID Connect Setting");

		pnlOpenIdConnectGrid = new OpenIdConnectGridPane();
		CanvasItem canvasOpenIdConnects = new CanvasItem();
		canvasOpenIdConnects.setTitle("OpenID Connect");
		canvasOpenIdConnects.setCanvas(pnlOpenIdConnectGrid);
		canvasOpenIdConnects.setColSpan(3);
		canvasOpenIdConnects.setStartRow(true);

		form.setItems(canvasOpenIdConnects);

		addMember(form);
	}

	@Override
	public void setDefinition(AuthenticationPolicyDefinition definition) {
		pnlOpenIdConnectGrid.setDefinition(definition);
	}
	@Override
	public AuthenticationPolicyDefinition getEditDefinition(AuthenticationPolicyDefinition definition) {
		pnlOpenIdConnectGrid.getEditDefinition(definition);
		return definition;
	}
	@Override
	public boolean validate() {
		return form.validate();
	}

	@Override
	public void clearErrors() {
		form.clearErrors(true);
	}

	private static class OpenIdConnectGridPane extends VLayout implements EditablePane<AuthenticationPolicyDefinition> {

		private ListGrid grid;

		public OpenIdConnectGridPane() {
			setWidth100();
			setHeight(1);

			grid = new ListGrid();
			grid.setWidth100();
			grid.setHeight(1);

			grid.setShowAllColumns(true);								//列を全て表示
			grid.setShowAllRecords(true);								//レコードを全て表示
			grid.setCanResizeFields(false);								//列幅変更可能
			grid.setCanSort(false);										//ソート不可
			grid.setCanPickFields(false);								//表示フィールドの選択不可
			grid.setCanGroupBy(false);									//GroupByの選択不可
			grid.setAutoFitWidthApproach(AutoFitWidthApproach.BOTH);	//AutoFit時にタイトルと値を参照
			grid.setLeaveScrollbarGap(false);							//縦スクロールバー自動表示制御
			grid.setBodyOverflow(Overflow.VISIBLE);
			grid.setOverflow(Overflow.VISIBLE);
			grid.setCanReorderRecords(true);							//Dragによる並び替えを可能にする

			grid.setCanEdit(true);
			grid.setEditEvent(ListGridEditEvent.DOUBLECLICK);

			ListGridField openIdConnectField = new ListGridField("openIdConnect", "OpenID Connect");
			// ListGridFieldに対してComboboxを設定する場合、
			// Refreshを実行するとエラーになるので除外(fetchData is not a function)
			// Tooltipを表示するとエラーになるので除外(self.getSelectedRecord is not a function)
			MetaDataComboBoxItem openIdConnectItem = new MetaDataComboBoxItem(
					OpenIdConnectDefinition.class, "OpenID Connect", new ItemOption(false, false, false, false, false));
			openIdConnectField.setEditorProperties(openIdConnectItem);
			grid.setFields(openIdConnectField);

			IButton btnAdd = new IButton("Add");
			btnAdd.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					grid.startEditingNew();
				}
			});

			IButton btnDel = new IButton("Remove");
			btnDel.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					grid.removeSelectedData();
				}
			});

			HLayout buttonPane = new HLayout(5);
			buttonPane.setMargin(5);
			buttonPane.addMember(btnAdd);
			buttonPane.addMember(btnDel);

			addMember(grid);
			addMember(buttonPane);
		}

		@Override
		public void setDefinition(AuthenticationPolicyDefinition definition) {

			if (definition.getOpenIdConnectDefinition() != null) {

				ListGridRecord[] records = new ListGridRecord[definition.getOpenIdConnectDefinition().size()];

				int cnt = 0;
				for (String openIdConnectDefinition : definition.getOpenIdConnectDefinition()) {
					ListGridRecord record = new ListGridRecord();
					record.setAttribute("openIdConnect",openIdConnectDefinition);
					records[cnt] = record;
					cnt++;
				}
				grid.setData(records);
			}
		}

		@Override
		public AuthenticationPolicyDefinition getEditDefinition(AuthenticationPolicyDefinition definition) {

			ListGridRecord[] records = grid.getRecords();
			if (records == null || records.length == 0) {
				definition.setOpenIdConnectDefinition(null);
			} else {
				List<String> openIdConnects = new ArrayList<>(records.length);
				for (ListGridRecord record : records) {
					String openIdConnect = record.getAttribute("openIdConnect");
					if (openIdConnect != null && !openIdConnect.trim().isEmpty()) {
						openIdConnects.add(openIdConnect);
					}
				}
				definition.setOpenIdConnectDefinition(openIdConnects);
			}
			return definition;
		}

		@Override
		public boolean validate() {
			return true;
		}

		@Override
		public void clearErrors() {
		}
	}

}