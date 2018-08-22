/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.auth.listener;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.data.MetaDataNameDS;
import org.iplass.adminconsole.client.metadata.data.MetaDataNameDS.MetaDataNameDSOption;
import org.iplass.mtp.auth.policy.definition.AccountNotificationListenerDefinition;
import org.iplass.mtp.auth.policy.definition.listeners.MailAccountNotificationListenerDefinition;
import org.iplass.mtp.mail.template.definition.MailTemplateDefinition;

import com.smartgwt.client.types.AutoFitWidthApproach;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class MailAccountNotificationListenerEditPane extends AuthenticationListenerTypeEditPane {

	/** フォーム */
	private DynamicForm form;

	/** テンプレート名 */
	private SelectItem slctCreateUserMailTemplateField;
	private SelectItem slctCredentialResetMailTemplateField;
	private SelectItem slctCreateUserWithSpecifiedPasswordMailTemplateField;
	private SelectItem slctCredentialResetWithSpecifiedPasswordMailTemplateField;
	private SelectItem slctLockedoutMailTemplateField;
	private SelectItem slctCredentialUpdatedMailTemplateField;
	private SelectItem slctPropertyUpdatedMailTemplateField;
	private SelectItem slctRemovedUpdatedMailTemplateField;
	private SelectItem slctLoginSuccessUserMailTemplateField;
	private PropertiesForUpdateNotificationPane propertiesForUpdateNotificationPane;

	/**
	 * コンストラクタ
	 */
	public MailAccountNotificationListenerEditPane() {

		//レイアウト設定
		setWidth100();
		setHeight100();

		//入力部分
		form = new DynamicForm();
		form.setMargin(5);
		form.setAutoHeight();
		form.setWidth(500);

		slctCreateUserMailTemplateField = new SelectItem("createUserMailTemplate", "Create User MailTemplate");
		slctCreateUserMailTemplateField.setWidth("100%");
		MetaDataNameDS.setDataSource(slctCreateUserMailTemplateField, MailTemplateDefinition.class, new MetaDataNameDSOption(true, false));

		slctCredentialResetMailTemplateField = new SelectItem("credentialResetMailTemplate", "Credential Reset MailTemplate");
		slctCredentialResetMailTemplateField.setWidth("100%");
		MetaDataNameDS.setDataSource(slctCredentialResetMailTemplateField, MailTemplateDefinition.class, new MetaDataNameDSOption(true, false));

		slctCreateUserWithSpecifiedPasswordMailTemplateField = new SelectItem("createUserWithSpecifiedPasswordMailTemplate", "Create User With SpecifiedPassword MailTemplate");
		slctCreateUserWithSpecifiedPasswordMailTemplateField.setWidth("100%");
		MetaDataNameDS.setDataSource(slctCreateUserWithSpecifiedPasswordMailTemplateField, MailTemplateDefinition.class, new MetaDataNameDSOption(true, false));

		slctCredentialResetWithSpecifiedPasswordMailTemplateField = new SelectItem("credentialResetWithSpecifiedPasswordMailTemplate", "Credential Reset WithSpecified PasswordMailTemplate");
		slctCredentialResetWithSpecifiedPasswordMailTemplateField.setWidth("100%");
		MetaDataNameDS.setDataSource(slctCredentialResetWithSpecifiedPasswordMailTemplateField, MailTemplateDefinition.class, new MetaDataNameDSOption(true, false));

		slctLockedoutMailTemplateField = new SelectItem("lockedoutMailTemplate", "Lockedout MailTemplate");
		slctLockedoutMailTemplateField.setWidth("100%");
		MetaDataNameDS.setDataSource(slctLockedoutMailTemplateField, MailTemplateDefinition.class, new MetaDataNameDSOption(true, false));

		slctCredentialUpdatedMailTemplateField = new SelectItem("credentialUpdatedMailTemplate", "Credential Updated MailTemplate");
		slctCredentialUpdatedMailTemplateField.setWidth("100%");
		MetaDataNameDS.setDataSource(slctCredentialUpdatedMailTemplateField, MailTemplateDefinition.class, new MetaDataNameDSOption(true, false));

		slctPropertyUpdatedMailTemplateField = new SelectItem("propertyUpdatedMailTemplate", "Property Updated MailTemplate");
		slctPropertyUpdatedMailTemplateField.setWidth("100%");
		MetaDataNameDS.setDataSource(slctPropertyUpdatedMailTemplateField, MailTemplateDefinition.class, new MetaDataNameDSOption(true, false));

		slctRemovedUpdatedMailTemplateField = new SelectItem("removedUpdatedMailTemplate", "Remove User MailTemplate");
		slctRemovedUpdatedMailTemplateField.setWidth("100%");
		MetaDataNameDS.setDataSource(slctRemovedUpdatedMailTemplateField, MailTemplateDefinition.class, new MetaDataNameDSOption(true, false));

		slctLoginSuccessUserMailTemplateField = new SelectItem("loginSuccessUserMailTemplate", "Logged in User MailTemplate");
		slctLoginSuccessUserMailTemplateField.setWidth("100%");
		MetaDataNameDS.setDataSource(slctLoginSuccessUserMailTemplateField, MailTemplateDefinition.class, new MetaDataNameDSOption(true, false));

		propertiesForUpdateNotificationPane = new PropertiesForUpdateNotificationPane();

		form.setItems(slctCreateUserMailTemplateField, slctCredentialResetMailTemplateField,
				slctCreateUserWithSpecifiedPasswordMailTemplateField, slctCredentialResetWithSpecifiedPasswordMailTemplateField,
				slctLockedoutMailTemplateField, slctCredentialUpdatedMailTemplateField,
				slctPropertyUpdatedMailTemplateField, slctRemovedUpdatedMailTemplateField,
				slctLoginSuccessUserMailTemplateField);

		//配置
		addMember(form);
		addMember(propertiesForUpdateNotificationPane);
	}

	/**
	 * AccountNotificationListenerDefinitionを展開します。
	 *
	 * @param definition AccountNotificationListenerDefinition
	 */
	@Override
	public void setDefinition(AccountNotificationListenerDefinition definition) {
		MailAccountNotificationListenerDefinition listener = (MailAccountNotificationListenerDefinition)definition;
		slctCreateUserMailTemplateField.setValue(listener.getCreateUserMailTemplate());
		slctCredentialResetMailTemplateField.setValue(listener.getCredentialResetMailTemplate());
		slctCreateUserWithSpecifiedPasswordMailTemplateField.setValue(listener.getCreateUserWithSpecifiedPasswordMailTemplate());
		slctCredentialResetWithSpecifiedPasswordMailTemplateField.setValue(listener.getCredentialResetWithSpecifiedPasswordMailTemplate());
		slctLockedoutMailTemplateField.setValue(listener.getLockedoutMailTemplate());
		slctCredentialUpdatedMailTemplateField.setValue(listener.getCredentialUpdatedMailTemplate());
		slctPropertyUpdatedMailTemplateField.setValue(listener.getPropertyUpdatedMailTemplate());
		slctRemovedUpdatedMailTemplateField.setValue(listener.getRemoveUserMailTemplate());
		slctLoginSuccessUserMailTemplateField.setValue(listener.getLoginSuccessUserMailTemplate());
		propertiesForUpdateNotificationPane.setPropertiesForUpdateNotificationList(listener.getPropertiesForUpdateNotification());
	}

	/**
	 * 編集されたAccountNotificationListenerDefinition情報を返します。
	 *
	 * @return 編集AccountNotificationListenerDefinition情報
	 */
	@Override
	public AccountNotificationListenerDefinition getEditDefinition(AccountNotificationListenerDefinition definition) {
		MailAccountNotificationListenerDefinition listener = (MailAccountNotificationListenerDefinition)definition;
		listener.setCreateUserMailTemplate(SmartGWTUtil.getStringValue(slctCreateUserMailTemplateField, true));
		listener.setCredentialResetMailTemplate(SmartGWTUtil.getStringValue(slctCredentialResetMailTemplateField, true));
		listener.setCreateUserWithSpecifiedPasswordMailTemplate(SmartGWTUtil.getStringValue(slctCreateUserWithSpecifiedPasswordMailTemplateField, true));
		listener.setCredentialResetWithSpecifiedPasswordMailTemplate(SmartGWTUtil.getStringValue(slctCredentialResetWithSpecifiedPasswordMailTemplateField, true));
		listener.setLockedoutMailTemplate(SmartGWTUtil.getStringValue(slctLockedoutMailTemplateField, true));
		listener.setCredentialUpdatedMailTemplate(SmartGWTUtil.getStringValue(slctCredentialUpdatedMailTemplateField, true));
		listener.setPropertyUpdatedMailTemplate(SmartGWTUtil.getStringValue(slctPropertyUpdatedMailTemplateField, true));
		listener.setRemoveUserMailTemplate(SmartGWTUtil.getStringValue(slctRemovedUpdatedMailTemplateField, true));
		listener.setLoginSuccessUserMailTemplate(SmartGWTUtil.getStringValue(slctLoginSuccessUserMailTemplateField, true));
		listener.setPropertiesForUpdateNotification(propertiesForUpdateNotificationPane.getEditPropertiesForUpdateNotificationList());
		return listener;
	}

	/**
	 * 入力チェックを実行します。
	 *
	 * @return 入力チェック結果
	 */
	@Override
	public boolean validate() {
		return form.validate();
	}

	private class PropertiesForUpdateNotificationPane extends VLayout {

		private PropertiesForUpdateNotificationGrid grid;

		public PropertiesForUpdateNotificationPane() {
			setWidth100();
			setAutoHeight();
			setMargin(10);
			setPadding(5);

			HLayout captionComposit = new HLayout(5);
			captionComposit.setHeight(25);

			Label caption = new Label("Properties For Update Notification:");
			caption.setWrap(false);
			caption.setHeight(21);
			captionComposit.addMember(caption);

			grid = new PropertiesForUpdateNotificationGrid();
			grid.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
				public void onRecordDoubleClick(RecordDoubleClickEvent event) {
					editPropertiesForUpdateNotification((ListGridRecord)event.getRecord());
				}
			});

			IButton addPolicy = new IButton("Add");
			addPolicy.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					addPropertiesForUpdateNotification();
				}
			});

			IButton delPolicy = new IButton("Remove");
			delPolicy.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					deleteTwoStepVerificationPolicy();
				}
			});

			HLayout policyButtonPane = new HLayout(5);
			policyButtonPane.setMargin(5);
			policyButtonPane.addMember(addPolicy);
			policyButtonPane.addMember(delPolicy);

			addMember(captionComposit);
			addMember(grid);
			addMember(policyButtonPane);
		}

		public void setPropertiesForUpdateNotificationList(List<String> propertiesForUpdateNotificationList) {

			if (propertiesForUpdateNotificationList != null) {
				List<ListGridRecord> records = new ArrayList<ListGridRecord>();
				for (String propertiesForUpdateNotification : propertiesForUpdateNotificationList) {
						records.add(createRecord(propertiesForUpdateNotification, null));
				}
				grid.setData(records.toArray(new ListGridRecord[]{}));
			}
		}

		public List<String> getEditPropertiesForUpdateNotificationList() {

			ListGridRecord[] records = grid.getRecords();
			if (records == null || records.length == 0) {
				return new ArrayList<String>();
			}

			List<String> propertiesForUpdateNotificationList = new ArrayList<String>();

			for (ListGridRecord record : records) {
				String propertiesForUpdateNotification = (String) record.getAttributeAsObject("propertyName");
				propertiesForUpdateNotificationList.add(propertiesForUpdateNotification);
			}

			return propertiesForUpdateNotificationList;
		}

		private ListGridRecord createRecord(String propertiesForUpdateNotification, ListGridRecord record) {
			if (record == null) {
				record = new ListGridRecord();
			}

			record.setAttribute("propertyName", propertiesForUpdateNotification);
			return record;
		}

		private void addPropertiesForUpdateNotification() {
			editPropertiesForUpdateNotification(null);
		}

		private void editPropertiesForUpdateNotification(final ListGridRecord record) {
			final PropertiesForUpdateNotificationEditDialog dialog = new PropertiesForUpdateNotificationEditDialog();
			dialog.addDataChangeHandler(new DataChangedHandler() {

				@Override
				public void onDataChanged(DataChangedEvent event) {
					String propertiesForUpdateNotification = event.getValueObject(String.class);
					ListGridRecord newRecord = createRecord(propertiesForUpdateNotification, record);
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
				dialog.setPropertiesForUpdateNotification(
						(String)record.getAttributeAsObject("propertyName"));
			}
			dialog.show();
		}

		private void deleteTwoStepVerificationPolicy() {
			grid.removeSelectedData();
		}

		private class PropertiesForUpdateNotificationGrid extends ListGrid {

			public PropertiesForUpdateNotificationGrid() {
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

				ListGridField displayNameField = new ListGridField("propertyName", "Property Name");

				setFields(displayNameField);
			}
		}
	}

}
