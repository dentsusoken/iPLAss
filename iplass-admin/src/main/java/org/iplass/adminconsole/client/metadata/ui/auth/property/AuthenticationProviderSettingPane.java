/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import org.iplass.adminconsole.client.base.ui.widget.form.MtpCanvasItem;
import org.iplass.adminconsole.client.metadata.data.auth.AuthProviderDS;
import org.iplass.mtp.auth.policy.definition.AuthenticationPolicyDefinition;

import com.smartgwt.client.types.AutoFitWidthApproach;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.form.fields.CanvasItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.DataArrivedEvent;
import com.smartgwt.client.widgets.grid.events.DataArrivedHandler;
import com.smartgwt.client.widgets.layout.VLayout;

public class AuthenticationProviderSettingPane extends AbstractSettingPane {

	private ProviderGrid grid;

	private boolean isDataArrived = false;
	private List<String> initProviders = null;

	public AuthenticationProviderSettingPane() {

		form.setGroupTitle("Authentication Provider Setting");

		grid = new ProviderGrid();

		VLayout gridLayout = new VLayout();
		gridLayout.addMember(grid);

		CanvasItem canvasGrid = new MtpCanvasItem();
		canvasGrid.setTitle("Provider");
		canvasGrid.setCanvas(gridLayout);
		canvasGrid.setColSpan(3);
		canvasGrid.setStartRow(true);

		form.setItems(canvasGrid);

		addMember(form);
	}

	@Override
	public void setDefinition(AuthenticationPolicyDefinition definition) {
		if (!isDataArrived) {
			//まだFetchが完了していなかったらKEEP(onDataArrivedで反映)
			initProviders = definition.getAuthenticationProvider();
		} else {
			//すでにFetchが完了していたら反映
			setInitSelect(definition.getAuthenticationProvider());
		}
	}

	@Override
	public AuthenticationPolicyDefinition getEditDefinition(AuthenticationPolicyDefinition definition) {
		if (grid.getSelectedRecords() != null) {
			List<String> providers = new ArrayList<>();
			for (ListGridRecord record : grid.getSelectedRecords()) {
				providers.add(record.getAttribute("name"));
			}
			definition.setAuthenticationProvider(providers);
		} else {
			definition.setAuthenticationProvider(null);
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

	private class ProviderGrid extends ListGrid {

		public ProviderGrid() {
			setWidth100();
			setHeight(1);

			setShowAllColumns(true);							//列を全て表示
			setShowAllRecords(true);							//レコードを全て表示
			setCanResizeFields(true);							//列幅変更可能
			setCanSort(false);									//ソート不可
			setCanPickFields(false);							//表示フィールドの選択不可
			setCanGroupBy(false);								//GroupByの選択不可
			setAutoFitWidthApproach(AutoFitWidthApproach.BOTH);	//AutoFit時にタイトルと値を参照
			setLeaveScrollbarGap(false);						//縦スクロールバー自動表示制御
			setBodyOverflow(Overflow.VISIBLE);
			setOverflow(Overflow.VISIBLE);

			//この２つを指定することでcreateRecordComponentが有効
			setShowRecordComponents(true);
			setShowRecordComponentsByCell(true);

			//CheckBox選択設定
			setSelectionType(SelectionStyle.SIMPLE);
			setSelectionAppearance(SelectionAppearance.CHECKBOX);

			//DataSource設定
			AuthProviderDS.setDataSource(this);
			addDataArrivedHandler(new DataArrivedHandler() {

				@Override
				public void onDataArrived(DataArrivedEvent event) {
					isDataArrived = true;
					//すでに初期値が設定されていたら反映
					if (initProviders != null) {
						setInitSelect(initProviders);
						initProviders = null;
					}
				}
			});
			fetchData();
		}
	}

	private void setInitSelect(List<String> providers) {

		//一旦全選択解除(キャンセル対応)
		grid.deselectAllRecords();
		if (providers != null && !providers.isEmpty()) {
			for (String provider : providers) {
				for (ListGridRecord record : grid.getRecords()) {
					if (record.getAttribute("name").equals(provider)) {
						grid.selectRecord(record);
						break;
					}
				}
			}
		}
	}

}
