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

package org.iplass.adminconsole.client.metadata.ui.entity.property.type;

import java.util.LinkedHashMap;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.rpc.AdminAsyncCallback;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinition;

import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;

public class SortInfoEditDialog extends AbstractWindow {

	private SelectItem selProperty;
	private SelectItem selSortType;

	private IButton btnOk;

	private boolean isReadOnly = false;
	private SortInfoListGridRecord record;
	private String defName;
	private SortInfoEditDialogHandler handler;

	private final MetaDataServiceAsync service = MetaDataServiceFactory.get();

	public interface SortInfoEditDialogHandler {

		ListGridRecord[] listGridRecords();

		void onSaved(SortInfoListGridRecord record);
	}

	public SortInfoEditDialog(final SortInfoListGridRecord record, String defName, boolean isReadOnly, final SortInfoEditDialogHandler handler) {
		this.record = record;
		this.defName = defName;
		this.isReadOnly = isReadOnly;
		this.handler = handler;

		setWidth(350);
		setMaxWidth(350);
		setHeight(140);
		setMaxHeight(140);

		setShowMinimizeButton(false);
		setIsModal(true);
		setShowModalMask(true);
		setCanDragResize(true);
		centerInPage();

		selProperty = new SelectItem();
		selProperty.setTitle("Property Name");
		selProperty.setWidth(200);
		SmartGWTUtil.setRequired(selProperty);

		selSortType = new SelectItem();
		selSortType.setTitle("Order");
		selSortType.setWidth(200);

		final DynamicForm form = new DynamicForm();
		form.setMargin(5);
		form.setNumCols(2);
		form.setWidth100();
		form.setHeight(25);
		form.setItems(selProperty, selSortType);

		btnOk = new IButton("OK");
		btnOk.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (!form.validate()) {
					return;
				}

				saveRecord();
			}
		});
		IButton btnCancel = new IButton("Cancel");
		btnCancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				destroy();
			}
		});

		HLayout pnlFooter = new HLayout(5);
		pnlFooter.setMargin(5);
		pnlFooter.setHeight(20);
		pnlFooter.setWidth100();
		pnlFooter.setAlign(VerticalAlignment.CENTER);
		pnlFooter.addMember(btnOk);
		pnlFooter.addMember(btnCancel);

		addItem(form);
		addItem(SmartGWTUtil.separator());
		addItem(pnlFooter);

		initialize();
	}

	private void initialize() {

		if (isReadOnly) {
			setTitle("Sort Property (Read Only)");
			btnOk.setDisabled(true);
		} else {
			setTitle("Sort Property");
		}

		//プロパティ取得
		service.getEntityDefinition(TenantInfoHolder.getId(), defName, new AdminAsyncCallback<EntityDefinition>() {

			@Override
			public void onSuccess(EntityDefinition ed) {
				LinkedHashMap<String, String> propertyMap = new LinkedHashMap<String, String>();

				for (PropertyDefinition pd : ed.getPropertyList()) {
					//TODO DataSource化(一覧はNameで、選択時はDisplayNameになっている)
					if (pd.getDisplayName() != null && !pd.getDisplayName().isEmpty()) {
						if (pd.getDisplayName().equals(pd.getName())) {
							propertyMap.put(pd.getName(), pd.getName());
						} else {
							propertyMap.put(pd.getName(),
									pd.getName() + "(" + pd.getDisplayName() + ")");
						}
					} else {
						propertyMap.put(pd.getName(), pd.getName());
					}
				}
				selProperty.setValueMap(propertyMap);

				selProperty.setValue(record.getPropertyName());
			}

		});

		LinkedHashMap<String, String> typeMap = new LinkedHashMap<String, String>();
		typeMap.put("ASC", "ASC");
		typeMap.put("DESC", "DESC");
		selSortType.setValueMap(typeMap);

		selSortType.setValue(record.getSortType());
	}

	private void saveRecord() {

		String propertyName = SmartGWTUtil.getStringValue(selProperty);

		//重複チェック
		if (handler.listGridRecords() != null) {
			for (ListGridRecord other : handler.listGridRecords()) {
				//自分は除外
				if (record == other) {
					continue;
				}
				SortInfoListGridRecord sRecord = (SortInfoListGridRecord)other;
				if (sRecord.getPropertyName().equals(propertyName)) {
					SC.say(rs("ui_metadata_entity_PropertyListGrid_propertyAlreadySelect"));
					return;
				}
			}
		}

		record.setPropertyName(SmartGWTUtil.getStringValue(selProperty));
		record.setSortType(SmartGWTUtil.getStringValue(selSortType));

		handler.onSaved(record);

		destroy();
	}

	private String rs(String key) {
		return AdminClientMessageUtil.getString(key);
	}
}
