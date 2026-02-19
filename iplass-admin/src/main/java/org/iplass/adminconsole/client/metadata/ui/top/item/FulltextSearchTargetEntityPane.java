/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.metadata.data.entity.CrawlEntityDS;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.view.top.parts.FulltextSearchViewParts;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.grid.EditorValueMapFunction;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * クロール対象エンティティTopView
 */
public class FulltextSearchTargetEntityPane extends VLayout {

	private ListGrid grid;
	private MetaDataServiceAsync service = MetaDataServiceFactory.get();
	boolean isInitDrop;

	/**
	 * コンストラクタ
	 *
	 */
	public FulltextSearchTargetEntityPane(final FulltextSearchViewParts parts, boolean isInitDrop) {

		this.isInitDrop = isInitDrop;

		//レイアウト設定
		setWidth100();
		setHeight100();

		grid = new ListGrid();
		grid.setWidth100();
		grid.setHeight100();
		grid.setShowAllRecords(true);
		grid.setCellHeight(22);
		grid.setLeaveScrollbarGap(false);	//falseで縦スクロールバー領域が自動表示制御される
		grid.setShowRowNumbers(true);		//行番号表示

		addMember(grid);

		service.getCrawlTargetEntityViewMap(TenantInfoHolder.getId(),
			new AsyncCallback<Map<String, List<String>>>() {

			public void onFailure(Throwable caught) {
				SC.say("failed","Failed to get the screen information." + caught.getMessage());
				GWT.log(caught.toString(), caught);
			}

			@Override
			public void onSuccess(Map<String, List<String>> result) {
				refreshGrid(result, parts);
			}

		});
	}

	private void refreshGrid(final Map<String,List<String>> viewsMap, FulltextSearchViewParts parts) {
		CrawlEntityDS ds = CrawlEntityDS.getInstance(parts, isInitDrop);

		grid.setDataSource(ds);

		//（参考）setFieldsは、setDataSource後に指定しないと効かない
		ListGridField errorField = new ListGridField("error", " ");
		errorField.setWidth(25);
		ListGridField nameField = new ListGridField("name", AdminClientMessageUtil.getString("ui_metadata_ui_top_item_FulltextSearchTargetEntityPane_name"));
		nameField.setHidden(true);
		ListGridField displayNameField = new ListGridField("displayName", AdminClientMessageUtil.getString("ui_metadata_ui_top_item_FulltextSearchTargetEntityPane_entityName"));
		displayNameField.setCanEdit(false);
		ListGridField entityViewField = new ListGridField("entityView", AdminClientMessageUtil.getString("ui_metadata_ui_top_item_FulltextSearchTargetEntityPane_entityView"));
		SelectItem entityView = new SelectItem();
		entityViewField.setEditorProperties(entityView);
		entityViewField.setEditorValueMapFunction(new EditorValueMapFunction() {
			public Map<String, String> getEditorValueMap(@SuppressWarnings("rawtypes") Map values, ListGridField field, ListGrid grid) {
				String defName = (String) values.get("name");

				Map<String, String> valueMap = new HashMap<String, String>();
				List<String> entityView = (List<String>) viewsMap.get(defName);
				for (String view : entityView) {
					valueMap.put(view, view);
				}

				return valueMap;
			}
		});

		ListGridField isDispEntity = new ListGridField("isDispEntity", AdminClientMessageUtil.getString("ui_metadata_ui_top_item_FulltextSearchTargetEntityPane_showInSearchList"));
		grid.setFields(nameField, displayNameField, entityViewField, isDispEntity);

		grid.setCanEdit(true);
		grid.setEditEvent(ListGridEditEvent.CLICK);
		grid.setEditByCell(true);

		grid.fetchData();

	}

}
