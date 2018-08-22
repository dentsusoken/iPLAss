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

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.metadata.ui.auth.listener.AuthenticationListenerEditDialog;
import org.iplass.mtp.auth.policy.definition.AccountNotificationListenerDefinition;
import org.iplass.mtp.auth.policy.definition.AuthenticationPolicyDefinition;
import org.iplass.mtp.auth.policy.definition.listeners.JavaClassAccountNotificationListenerDefinition;
import org.iplass.mtp.auth.policy.definition.listeners.MailAccountNotificationListenerDefinition;
import org.iplass.mtp.auth.policy.definition.listeners.ScriptingAccountNotificationListenerDefinition;

import com.smartgwt.client.types.AutoFitWidthApproach;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;

public class AccountNotificationListenerSettingPane extends AbstractSettingPane {

	private ListenerGrid grid;

	public AccountNotificationListenerSettingPane() {

		HLayout captionComposit = new HLayout(5);
		captionComposit.setHeight(25);

		Label caption = new Label("Account Notification Listener Setting:");
		caption.setWrap(false);
		caption.setHeight(21);
		captionComposit.addMember(caption);

		grid = new ListenerGrid();
		grid.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				editListener((ListGridRecord)event.getRecord());
			}
		});

		IButton addListener = new IButton("Add");
		addListener.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				addListener();
			}
		});

		IButton delListener = new IButton("Remove");
		delListener.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				deleteListener();
			}
		});

		HLayout listenerButtonPane = new HLayout(5);
		listenerButtonPane.setMargin(5);
		listenerButtonPane.addMember(addListener);
		listenerButtonPane.addMember(delListener);

		addMember(captionComposit);
		addMember(grid);
		addMember(listenerButtonPane);
	}

	@Override
	public void setDefinition(AuthenticationPolicyDefinition definition) {

		List<AccountNotificationListenerDefinition> nortificationListnerList = definition.getNotificationListener();
		if (nortificationListnerList != null) {
			List<ListGridRecord> records = new ArrayList<ListGridRecord>();
			for (AccountNotificationListenerDefinition listener : nortificationListnerList) {
					records.add(createRecord(listener, null));
			}
			grid.setData(records.toArray(new ListGridRecord[]{}));
		}
	}

	/**
	 * 編集されたAuthenticationPolicyDefinition情報を返します。
	 *
	 * @return 編集AuthenticationPolicyDefinition情報
	 */
	@Override
	public AuthenticationPolicyDefinition getEditDefinition(AuthenticationPolicyDefinition definition) {

		ListGridRecord[] records = grid.getRecords();
		if (records == null || records.length == 0) {
			definition.setNotificationListener(null);
			return definition;
		}

		List<AccountNotificationListenerDefinition> nortificationListnerList = new ArrayList<AccountNotificationListenerDefinition>();

		for (ListGridRecord record : records) {
			AccountNotificationListenerDefinition accountNotificationListenerDefinition = (AccountNotificationListenerDefinition)record.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name());
			nortificationListnerList.add(accountNotificationListenerDefinition);
		}
		definition.setNotificationListener(nortificationListnerList);

		return definition;
	}

	@Override
	public boolean validate() {
		return true;
	}

	private ListGridRecord createRecord(AccountNotificationListenerDefinition definition, ListGridRecord record) {
		if (record == null) {
			record = new ListGridRecord();
		}

		String typeName = "";
		if (definition instanceof JavaClassAccountNotificationListenerDefinition) {
			typeName = "JavaClassAccountNotificationListener";
		}

		if (definition instanceof MailAccountNotificationListenerDefinition) {
			typeName = "MailAccountNotificationListener";
		}

		if (definition instanceof ScriptingAccountNotificationListenerDefinition) {
			typeName = "ScriptingAccountNotificationListener";
		}
		record.setAttribute(FIELD_NAME.LISTENER_TYPE.name(), typeName);
		record.setAttribute(FIELD_NAME.VALUE_OBJECT.name(), definition);
		return record;
	}

	private void addListener() {
		editListener(null);
	}

	private void editListener(final ListGridRecord record) {
		final AuthenticationListenerEditDialog dialog = new AuthenticationListenerEditDialog();
		dialog.addDataChangeHandler(new DataChangedHandler() {

			@Override
			public void onDataChanged(DataChangedEvent event) {
				AccountNotificationListenerDefinition param = event.getValueObject(AccountNotificationListenerDefinition.class);
				ListGridRecord newRecord = createRecord(param, record);
				if (record != null) {
					grid.updateData(newRecord);
				} else {
					//追加
					grid.addData(newRecord);
				}
				grid.refreshFields();
			}
		});

		if (record != null) {
			dialog.setDefinition(
					(AccountNotificationListenerDefinition)record.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name()));
		}
		dialog.show();
	}

	private void deleteListener() {
		grid.removeSelectedData();
	}

	private class ListenerGrid extends ListGrid {

		public ListenerGrid() {
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

			ListGridField listenerTypeField = new ListGridField(FIELD_NAME.LISTENER_TYPE.name(), "Listener Type");

			setFields(listenerTypeField);
		}
	}

}
