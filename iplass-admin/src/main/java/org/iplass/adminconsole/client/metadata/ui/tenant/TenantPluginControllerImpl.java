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

package org.iplass.adminconsole.client.metadata.ui.tenant;

import org.iplass.adminconsole.client.base.event.ViewMetaDataEvent;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.metadata.data.tenant.BaseTenantDS;
import org.iplass.adminconsole.client.metadata.data.tenant.TenantCategory;
import org.iplass.adminconsole.client.metadata.data.tenant.TenantDSCellFormatter;
import org.iplass.adminconsole.client.metadata.ui.MetaDataItemMenuTreeNode;
import org.iplass.adminconsole.client.metadata.ui.common.MetaCommonHeaderPane;
import org.iplass.mtp.tenant.Tenant;

import com.smartgwt.client.types.GroupStartOpen;
import com.smartgwt.client.widgets.grid.GroupNode;
import com.smartgwt.client.widgets.grid.GroupTitleRenderer;
import com.smartgwt.client.widgets.grid.GroupValueFunction;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class TenantPluginControllerImpl implements TenantPluginController {

	@Override
	public void setControllTarget(TenantMainPane owner, TenantEditPane editPane) {
	}

	@Override
	public MetaCommonHeaderPane createHeaderPane(MetaDataItemMenuTreeNode targetNode) {
		return new MetaCommonHeaderPane(targetNode);
	}

	@Override
	public ListGrid createGrid() {

		ListGrid grid = new ListGrid();
		grid.setCanEdit(false);
		grid.setWidth100();
		grid.setHeight100();
		grid.setCellHeight(22);
		grid.setShowAllRecords(true);
		grid.setLeaveScrollbarGap(false);
		grid.setShowFilterEditor(false);
		grid.setCanSort(false);
		grid.setCanAutoFitFields(false);
		grid.setCanFreezeFields(false);
		grid.setCanGroupBy(false);
		grid.setCanPickFields(false);
		grid.setCanDragRecordsOut(false);
		grid.setCanDrop(false);
		grid.setEmptyMessage("no item");
		grid.setShowAsynchGroupingPrompt(false);

		// 一覧Group設定
		grid.setGroupStartOpen(GroupStartOpen.ALL);
		grid.setGroupByField("title");
		// grid.setGroupIndentSize(100); //インデントされないのでダミーフィールド作成

		ListGridField indentField = new ListGridField("indent", " ");
		indentField.setWidth(15);

		ListGridField titleField = new ListGridField("title",
				AdminClientMessageUtil.getString("ui_metadata_tenant_TenantEditPane_property"));
		titleField.setGroupValueFunction(new GroupValueFunction() {

			@Override
			public Object getGroupValue(Object value, ListGridRecord record, ListGridField field, String fieldName,
					ListGrid grid) {
				// Group値をレコードの"category"から取得する
				return record.getAttribute("category");
			}
		});
		titleField.setGroupTitleRenderer(new GroupTitleRenderer() {

			@Override
			public String getGroupTitle(Object groupValue, GroupNode groupNode, ListGridField field, String fieldName,
					ListGrid grid) {
				// 上で返すGroup値をタイトルに指定する
				return BaseTenantDS.getTenantCategoryName(TenantCategory.valueOf((String) groupValue));
			}
		});

		ListGridField valueField = new ListGridField("displayValue",
				AdminClientMessageUtil.getString("ui_metadata_tenant_TenantEditPane_value"));
		valueField.setCellFormatter(new TenantDSCellFormatter());

		grid.setFields(indentField, titleField, valueField);

		return grid;
	}

	@Override
	public boolean isEditSupportMetaData(ViewMetaDataEvent event) {
		return Tenant.class.getName().equals(event.getDefinitionClassName());
	}

}
