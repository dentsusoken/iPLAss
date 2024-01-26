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

package org.iplass.adminconsole.client.metadata.ui.webapi;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.webapi.definition.RequestType;
import org.iplass.mtp.webapi.definition.WebApiDefinition;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;

public class RequestTypePane extends HLayout {

	private DynamicForm form;

	private Map<RequestType, CheckboxItem> requestTypeItems;

	public RequestTypePane() {

		Label caption = new Label("Request Type");
		caption.setHeight(21);

		//レイアウト設定
		setWidth100();
		setHeight(20);
		setMargin(5);
		setMembersMargin(10);
		setAlign(Alignment.LEFT);

		//入力部分
		form = new DynamicForm();
		form.setWidth100();
		form.setHeight(30);
		form.setNumCols(RequestType.values().length * 2 + 1);
		form.setTitleWidth(0);	//チェックボックスを左に寄せるため。
		form.setCellPadding(0);

		requestTypeItems = new LinkedHashMap<>();
		for (RequestType type : RequestType.values()) {
			CheckboxItem chkType = new CheckboxItem();
			chkType.setTitle(type.name().replaceAll("_", " "));
			chkType.setWidth(50);
			requestTypeItems.put(type, chkType);
		}
		form.setItems(requestTypeItems.values().toArray(new CheckboxItem[0]));

		addMember(caption);
		addMember(form);
	}

	/**
	 * 編集されたWebAPIDefinition情報を返します。
	 *
	 * @return 編集WebAPIDefinition情報
	 */
	public WebApiDefinition getEditDefinition(WebApiDefinition definition) {

		List<RequestType> requestTypeList = selectedType();
		definition.setAccepts(requestTypeList.toArray(new RequestType[0]));
		return definition;
	}

	/**
	 * WebAPIを展開します。
	 *
	 * @param definition WebAPIDefinition
	 */
	public void setDefinition(WebApiDefinition definition) {

		for (CheckboxItem chkType : requestTypeItems.values()) {
			chkType.setValue(false);
		}

		if (definition.getAccepts() != null) {
			for (RequestType type : definition.getAccepts()) {
				if (requestTypeItems.containsKey(type)) {
					requestTypeItems.get(type).setValue(true);
				}
			}
		}
	}
	/**
	 * タイプ変更イベントを設定します。
	 * @param handler
	 */
	public void setTypeChangedHandler(ChangedHandler handler) {
		for (CheckboxItem chkType : requestTypeItems.values()) {
			chkType.addChangedHandler(handler);
		}

	}
	/**
	 * 選択されているタイプを返します。
	 * @return
	 */
	public List<RequestType> selectedType() {
		List<RequestType> requestTypeList = new ArrayList<>();
		for (Entry<RequestType, CheckboxItem> typeEntry : requestTypeItems.entrySet()) {
			if (SmartGWTUtil.getBooleanValue(typeEntry.getValue())) {
				requestTypeList.add(typeEntry.getKey());
			}
		}

		return requestTypeList;
	}
}
