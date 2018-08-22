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

package org.iplass.adminconsole.client.tools.ui.eql;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.data.DataSourceConstants;
import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.data.entity.EntityDS;
import org.iplass.mtp.entity.Entity;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;

public class EntitySelectDialog extends AbstractWindow {

	public static final String IS_EXCLUDE_REFERENCE_KEY = "isExcludeReference";
	public static final String IS_EXCLUDE_INHERITED_KEY = "isExcludeInherited";
	public static final String IS_FORMAT_EQL_KEY = "isFormatEql";

	private List<DataChangedHandler> handlers = new ArrayList<DataChangedHandler>();

	private CheckboxItem excludeRefItem;
	private CheckboxItem excludeInheritedItem;
	private CheckboxItem formatEqlItem;

	public EntitySelectDialog() {
		setTitle("Select Entity");
		setWidth(630);
		setHeight(310);
		setShowMinimizeButton(false);
		setIsModal(true);
		setShowModalMask(true);
		setLeaveScrollbarGap(false);
		centerInPage();

		Label caption = new Label();
		caption.setHeight(30);
		caption.setMargin(5);
		caption.setContents("select target entity.");

		final ListGrid grid = new ListGrid();
		grid.setMargin(5);
		grid.setHeight100();
		grid.setWidth100();
		grid.setShowAllColumns(true);
		grid.setShowAllRecords(true);
		grid.setCanResizeFields(true);

		EntityDS.setDataSource(grid);
		grid.setAutoFetchData(true);

		grid.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				String name = event.getRecord().getAttribute(DataSourceConstants.FIELD_NAME);
				selectEntity(name);
			}
		});


		DynamicForm form = new DynamicForm();
		form.setWidth100();
		form.setHeight(30);
		form.setTitleWidth(0);	//チェックボックスを左に寄せるため。
		form.setCellPadding(0);

		excludeRefItem = new CheckboxItem("excludeRefItem", "exclude reference properties");
		SmartGWTUtil.addHoverToFormItem(excludeRefItem, AdminClientMessageUtil.getString("ui_tools_eql_EntitySelectDialog_excludeSelectRefProp"));
		excludeRefItem.setWidth(200);	//Widthを指定しないと選択時に下にスクロールが表示されるため

		excludeInheritedItem = new CheckboxItem("excludeInheritedItem", "exclude inherited properties(ignore "
				+ Entity.OID + "," + Entity.NAME + "," + Entity.VERSION + ")");
		SmartGWTUtil.addHoverToFormItem(excludeInheritedItem, AdminClientMessageUtil.getString("ui_tools_eql_EntitySelectDialog_excludeSelectInheriProp"));
		excludeInheritedItem.setWidth(200);	//Widthを指定しないと選択時に下にスクロールが表示されるため

		formatEqlItem = new CheckboxItem("formatedEqlItem", "format output eql");
		SmartGWTUtil.addHoverToFormItem(formatEqlItem, AdminClientMessageUtil.getString("ui_tools_eql_EntitySelectDialog_formatOutputEql"));
		formatEqlItem.setWidth(200);	//Widthを指定しないと選択時に下にスクロールが表示されるため

		form.setFields(excludeRefItem, excludeInheritedItem, formatEqlItem);


		HLayout footerLayout = new HLayout(5);
		footerLayout.setWidth100();
		footerLayout.setHeight(30);
		footerLayout.setAlign(Alignment.CENTER);
		footerLayout.setMargin(5);

		IButton selectBtn = new IButton();
		selectBtn.setWidth(100);
		selectBtn.setTitle("Select");
		selectBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (grid.getSelectedRecord() == null) {
					SC.warn(AdminClientMessageUtil.getString("ui_tools_eql_EntitySelectDialog_pleaseSelectEntity"));
				} else {
					String name = grid.getSelectedRecord().getAttribute("name");
					selectEntity(name);
				}
			}
		});
		IButton cancelBtn = new IButton();
		cancelBtn.setWidth(100);
		cancelBtn.setTitle("Cancel");
		cancelBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				destroy();
			}
		});

		footerLayout.addMember(selectBtn);
		footerLayout.addMember(cancelBtn);

		addItem(caption);
		addItem(grid);
		addItem(form);
		addItem(footerLayout);
	}

	/**
	 * <p>{@link DataChangedHandler} を追加します。</p>
	 *
	 * <p>データ選択時は、 {@link DataChangedEvent#getValueObject(String.class)} に
	 * Entity名がセットされます。<br/>
	 * また被参照属性の除外選択は、{@link DataChangedEvent#getValue(Boolean.class, EntitySelectDialog.IS_EXCLUDE_REFERENCE_KEY)} に
	 * 選択状態がセットされます。
	 *
	 *
	 * @param handle {@link DataChangedHandler}
	 */
	public void addDataChangedHandler(DataChangedHandler handle) {
		handlers.add(0, handle);
	}

	private void selectEntity(String name) {

		DataChangedEvent event = new DataChangedEvent();
		event.setValueObject(name);
		event.setValue(IS_EXCLUDE_REFERENCE_KEY, SmartGWTUtil.getBooleanValue(excludeRefItem));
		event.setValue(IS_EXCLUDE_INHERITED_KEY, SmartGWTUtil.getBooleanValue(excludeInheritedItem));
		event.setValue(IS_FORMAT_EQL_KEY, SmartGWTUtil.getBooleanValue(formatEqlItem));

		fireDataChanged(event);
		destroy();
	}

	private void fireDataChanged(DataChangedEvent event) {
		for (DataChangedHandler handle : handlers) {
			handle.onDataChanged(event);
		}
	}
}
