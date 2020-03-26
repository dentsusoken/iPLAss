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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogCondition;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogHandler;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogMode;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpSelectItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextAreaItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.data.MetaDataNameDS;
import org.iplass.adminconsole.client.metadata.data.MetaDataNameDS.MetaDataNameDSOption;
import org.iplass.adminconsole.client.metadata.ui.MetaDataUtil;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EventListenerDefinition;
import org.iplass.mtp.entity.definition.listeners.EventType;
import org.iplass.mtp.entity.definition.listeners.JavaClassEventListenerDefinition;
import org.iplass.mtp.entity.definition.listeners.ScriptingEventListenerDefinition;
import org.iplass.mtp.entity.definition.listeners.SendNotificationEventListenerDefinition;
import org.iplass.mtp.entity.definition.listeners.SendNotificationType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 *
 * @author lis2s8
 *
 */
public class EventListenerListGrid extends ListGrid {

	private final String SCRIPT = "Script";
	private final String JAVACLASS = "JavaClass";
	private final String SENDNOTIFICATION = "SendNotification";

	/**
	 * コンストラクタ
	 *
	 * @param defName 選択Entity名
	 * @param service Service
	 */
	public EventListenerListGrid() {
		// 横幅を画面の100%に
		setWidth100();
		// 縦幅
		setHeight(1);
		// 列を全て表示
		setShowAllColumns(true);
		// レコードを全て表示
		setShowAllRecords(true);
		// 列幅変更可能
		setCanResizeFields(true);
		setBodyOverflow(Overflow.VISIBLE);
		setOverflow(Overflow.VISIBLE);
		setCanSort(false);

		//grid内でのD&Dでの並べ替えを許可
		setCanDragRecordsOut(true);
		setCanAcceptDroppedRecords(true);
		setCanReorderRecords(true);

		// 各フィールド初期化
		ListGridField elNameField = new ListGridField(EventListenerListGridRecord.ELNAME, "Type");
		elNameField.setWidth(150);
		ListGridField gpField = new ListGridField(EventListenerListGridRecord.GP, "Value");

		// 各フィールドをListGridに設定
		setFields(elNameField, gpField);

		// レコードダブルクリックイベント設定
		addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				startEventListenerEdit(false, (EventListenerListGridRecord)getRecord(event.getRecordNum()));
			}
		});
	}

	public void setDefinition(EntityDefinition definition) {
		List<EventListenerDefinition> lstElDef = definition.getEventListenerList();

		if (lstElDef == null) { return; }

		int size = lstElDef.size();
		EventListenerListGridRecord[] records = new EventListenerListGridRecord[size];

		for (int i = 0; i < size; i++) {

			EventListenerListGridRecord record = new EventListenerListGridRecord();
			EventListenerDefinition elDef = lstElDef.get(i);

			setElDefData(record, elDef);
			records[i] = record;
		}

		setData(records);
	}

	public EntityDefinition getEditDefinition(EntityDefinition definition) {
		List<EventListenerDefinition> lstResult = new ArrayList<EventListenerDefinition>();

		ListGridRecord[] records = getRecords();

		for (ListGridRecord record : records) {
			EventListenerDefinition elDef = createElDef((EventListenerListGridRecord)record);
			lstResult.add(elDef);
		}
		definition.setEventListenerList(lstResult);
		return definition;
	}

	private void setElDefData(EventListenerListGridRecord record, EventListenerDefinition elDef) {
		if (elDef instanceof ScriptingEventListenerDefinition) {
			record.setElName(SCRIPT);

			ScriptingEventListenerDefinition sDef = (ScriptingEventListenerDefinition)elDef;
			// Script文字列
			record.setScript(sDef.getScript());
			record.setGeneralPurpus(sDef.getScript());

			// 実行タイミングの各チェックボックス
			List<EventType> lstEType = sDef.getListenEvent();
			if (lstEType != null) {
				for (EventType eType : lstEType) {
					switch (eType) {
						case AFTER_DELETE:
							record.setAfterD(true);
							break;
						case AFTER_INSERT:
							record.setAfterI(true);
							break;
						case AFTER_UPDATE:
							record.setAfterU(true);
							break;
						case BEFORE_DELETE:
							record.setBeforeD(true);
							break;
						case BEFORE_INSERT:
							record.setBeforeI(true);
							break;
						case BEFORE_UPDATE:
							record.setBeforeU(true);
							break;
						case AFTER_RESTORE:
							record.setAfterR(true);
							break;
						case AFTER_PURGE:
							record.setAfterP(true);
							break;
						case ON_LOAD:
							record.setOnLoad(true);
							break;
						case BEFORE_VALIDATE:
							record.setBeforeValidate(true);
						default:
							break;
					}
				}
			}
		} else if (elDef instanceof JavaClassEventListenerDefinition) {
			record.setElName(JAVACLASS);

			JavaClassEventListenerDefinition jcDef = (JavaClassEventListenerDefinition)elDef;
			record.setClassName(jcDef.getClassName());
			record.setGeneralPurpus(jcDef.getClassName());
//		} else if (elDef instanceof OutboundEventListenerDefinition) {
//			// TODO OutboundEventListenerの場合
//			record.setElName(OUTBOUND);
//			OutboundEventListenerDefinition oDef = (OutboundEventListenerDefinition)elDef;
		} else if (elDef instanceof SendNotificationEventListenerDefinition) {
			record.setElName(SENDNOTIFICATION);

			SendNotificationEventListenerDefinition snDef = (SendNotificationEventListenerDefinition)elDef;
			record.setNotificationType(snDef.getNotificationType().name());
			record.setGeneralPurpus(snDef.getNotificationType().name());
			record.setTmplDefName(snDef.getTmplDefName());
			record.setNotificationCondScript(snDef.getNotificationCondScript());

			record.setSyncrhonous(snDef.isSynchronous());
			record.setWebHookEndPointList(snDef.getEndPointDefList());
			record.setWebHookResultHandler(snDef.getResultHandler());

			List<EventType> lstEType = snDef.getListenEvent();
			if (lstEType != null) {
				for (EventType eType : lstEType) {
					switch(eType) {
						case AFTER_DELETE:
							record.setNotifyAfterD(true);
							break;
						case AFTER_INSERT:
							record.setNotifyAfterI(true);
							break;
						case AFTER_UPDATE:
							record.setNotifyAfterU(true);
							break;
						case BEFORE_DELETE:
							record.setNotifyBeforeD(true);
							break;
						case BEFORE_INSERT:
							record.setNotifyBeforeI(true);
							break;
						case BEFORE_UPDATE:
							record.setNotifyBeforeU(true);
							break;
						case AFTER_RESTORE:
							record.setNotifyAfterR(true);
							break;
						case AFTER_PURGE:
							record.setNotifyAfterP(true);
							break;
						case ON_LOAD:
							record.setNotifyOnLoad(true);
							break;
						case BEFORE_VALIDATE:
							record.setNotifyBeforeValidate(true);
						default:
							break;
					}
				}
			}

		} else {
		}
		record.setWithoutMappedByReference(elDef.isWithoutMappedByReference());
	}

	/**
	 *
	 * @param record
	 * @return
	 */
	private EventListenerDefinition createElDef(EventListenerListGridRecord record) {
		EventListenerDefinition result = null;
		String elName = record.getElName();

		if (elName != null && elName.length() > 0) {
			if (SCRIPT.equals(elName)) {
				ScriptingEventListenerDefinition sDef = new ScriptingEventListenerDefinition();
				sDef.setScript(record.getScript());

				List<EventType> lstEType = new ArrayList<EventType>();
				if (record.isAfterD()) { lstEType.add(EventType.AFTER_DELETE); }
				if (record.isAfterI()) { lstEType.add(EventType.AFTER_INSERT); }
				if (record.isAfterU()) { lstEType.add(EventType.AFTER_UPDATE); }
				if (record.isBeforeD()) { lstEType.add(EventType.BEFORE_DELETE); }
				if (record.isBeforeI()) { lstEType.add(EventType.BEFORE_INSERT); }
				if (record.isBeforeU()) { lstEType.add(EventType.BEFORE_UPDATE); }
				if (record.isAfterR()) { lstEType.add(EventType.AFTER_RESTORE); }
				if (record.isAfterP()) { lstEType.add(EventType.AFTER_PURGE); }
				if (record.isOnLoad()) { lstEType.add(EventType.ON_LOAD); }
				if (record.isBeforeValidate()) { lstEType.add(EventType.BEFORE_VALIDATE); }
				sDef.setListenEvent(lstEType);

				result = sDef;
			} else if (JAVACLASS.equals(elName)) {
				JavaClassEventListenerDefinition jDef = new JavaClassEventListenerDefinition();
				jDef.setClassName(record.getClassName());

				result = jDef;
//			} else if (OUTBOUND.equals(elName)) {
//
			} else if (SENDNOTIFICATION.equals(elName)) {
				SendNotificationEventListenerDefinition snDef = new SendNotificationEventListenerDefinition();
				snDef.setNotificationType(SendNotificationType.valueOf(record.getNotificationType()));
				snDef.setTmplDefName(record.getTmplDefName());
				snDef.setNotificationCondScript(record.getNotificationCondScript());

				snDef.setSynchronous(record.isSyncrhonous());
				snDef.setEndPointDefList(record.getWebHookEndPointList());
				snDef.setResultHandler(record.getWebHookResultHandler());

				snDef.setSynchronous(record.isSyncrhonous());
				snDef.setEndPointDefList(record.getWebHookEndPointList());
				snDef.setResultHandler(record.getWebHookResultHandler());

				List<EventType> lstEType = new ArrayList<EventType>();
				if (record.isNotifyAfterD()) { lstEType.add(EventType.AFTER_DELETE); }
				if (record.isNotifyAfterI()) { lstEType.add(EventType.AFTER_INSERT); }
				if (record.isNotifyAfterU()) { lstEType.add(EventType.AFTER_UPDATE); }
				if (record.isNotifyBeforeD()) { lstEType.add(EventType.BEFORE_DELETE); }
				if (record.isNotifyBeforeI()) { lstEType.add(EventType.BEFORE_INSERT); }
				if (record.isNotifyBeforeU()) { lstEType.add(EventType.BEFORE_UPDATE); }
				if (record.isNotifyAfterR()) { lstEType.add(EventType.AFTER_RESTORE); }
				if (record.isNotifyAfterP()) { lstEType.add(EventType.AFTER_PURGE); }
				if (record.isNotifyOnLoad()) { lstEType.add(EventType.ON_LOAD); }
				if (record.isNotifyBeforeValidate()) { lstEType.add(EventType.BEFORE_VALIDATE); }
				snDef.setListenEvent(lstEType);

				result = snDef;
			}
			if (result != null) {
				result.setWithoutMappedByReference(record.isWithoutMappedByReference());
			}
		}

		return result;
	}

	/**
	 *
	 *
	 * @param isNewRow
	 * @param record
	 */
	public void startEventListenerEdit(boolean isNewRow, EventListenerListGridRecord record) {
		EventListenerEditDialog dialog = new EventListenerEditDialog(isNewRow, record);
		dialog.show();
	}


	/**
	 * EventListener編集用ダイアログ
	 *
	 * @author lis2s8
	 *
	 */
	private class EventListenerEditDialog extends MtpDialog {

		private boolean isNewRow = false;
		private EventListenerListGridRecord target;

		//Type
		private DynamicForm typeItemForm;
		private SelectItem typeItem;

		//Main
		private VLayout typeLayout;

		//Script
		private DynamicForm scriptItemForm;
		private TextAreaItem scriptItem;

		//ScriptEvent
		private DynamicForm scriptEventItemForm;
		private CheckboxItem beforeIItem;
		private CheckboxItem afterIItem;
		private CheckboxItem beforeUItem;
		private CheckboxItem afterUItem;
		private CheckboxItem beforeDItem;
		private CheckboxItem afterDItem;
		private CheckboxItem afterRItem;
		private CheckboxItem afterPItem;
		private CheckboxItem onLoadItem;
		private CheckboxItem beforeValidateItem;

		//JavaClass
		private DynamicForm javaClassItemForm;
		private TextItem javaClassNameItem;

		//SendNotification
		private DynamicForm sendNotificationForm;
		private SelectItem notificationTypeItem;
		private SelectItem tmplDefNameItem;
		//webhook
		private VLayout webhookLayout;
		private DynamicForm webHookSettingsForm;
		private CheckboxItem webHookIsSynchronousItem;
		private TextItem notificationResultHandlerItem;
		private WebHookEndPointGrid webHookEndPointGrid;

		//NotificationCondition
		private DynamicForm notificationCondForm;
		private TextAreaItem notificationCondScriptItem;
		//NotificationEvent
		private DynamicForm notifyEventItemForm;
		private CheckboxItem notifyBeforeIItem;
		private CheckboxItem notifyAfterIItem;
		private CheckboxItem notifyBeforeUItem;
		private CheckboxItem notifyAfterUItem;
		private CheckboxItem notifyBeforeDItem;
		private CheckboxItem notifyAfterDItem;
		private CheckboxItem notifyAfterRItem;
		private CheckboxItem notifyAfterPItem;
		private CheckboxItem notifyOnLoadItem;
		private CheckboxItem notifyBeforeValidateItem;

		
		
		

		//withoutMappedByReference
		private DynamicForm withoutMappedByReferenceItemForm;
		private CheckboxItem withoutMappedByReferenceItem;


		private EventListenerEditDialog(boolean isNewRow, EventListenerListGridRecord target) {
			this.isNewRow = isNewRow;
			this.target = target;

			initialize();
			dataInitialize();
			formVisibleChange();
		}

		private void initialize() {

			setHeight(200);
			setTitle("EventListener");
			centerInPage();

			//---------------------------------
			//Type
			//---------------------------------
			typeItem = new MtpSelectItem();
			typeItem.setTitle("Type");
			SmartGWTUtil.setRequired(typeItem);
			typeItem.addChangedHandler(new ChangedHandler() {
				public void onChanged(ChangedEvent event) {
					formVisibleChange();
				}
			});

			typeItemForm = new MtpForm();
			typeItemForm.setItems(typeItem);

			container.addMember(typeItemForm);

			typeLayout = new VLayout();
			typeLayout.setWidth100();
			typeLayout.setHeight100();

			container.addMember(typeLayout);

			//---------------------------------
			//Script
			//---------------------------------
			ButtonItem editScript = new ButtonItem("editScript", "Edit");
			editScript.setWidth(100);
			editScript.setStartRow(false);
			editScript.setColSpan(3);
			editScript.setAlign(Alignment.RIGHT);
			editScript.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

				@Override
				public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
					MetaDataUtil.showScriptEditDialog(ScriptEditorDialogMode.GROOVY_SCRIPT,
							SmartGWTUtil.getStringValue(scriptItem),
							ScriptEditorDialogCondition.ENTITY_EVENT_LISTNER,
							"ui_metadata_entity_EventListenerListGrid_scriptHint",
							null,
							new ScriptEditorDialogHandler() {

								@Override
								public void onSave(String text) {
									scriptItem.setValue(text);
								}
								@Override
								public void onCancel() {
								}
							});
				}
			});

			scriptItem = new MtpTextAreaItem();
			scriptItem.setColSpan(2);
			scriptItem.setTitle("Script");
			scriptItem.setHeight("100%");
			SmartGWTUtil.setRequired(scriptItem);
			SmartGWTUtil.setReadOnlyTextArea(scriptItem);

			scriptItemForm = new MtpForm();
			scriptItemForm.setHeight100();
			scriptItemForm.setItems(editScript, scriptItem);

			beforeIItem = new CheckboxItem();
			beforeIItem.setTitle("beforeInsert");
			afterIItem = new CheckboxItem();
			afterIItem.setTitle("afterInsert");
			beforeUItem = new CheckboxItem();
			beforeUItem.setTitle("beforeUpdate");
			afterUItem = new CheckboxItem();
			afterUItem.setTitle("afterUpdate");
			beforeDItem = new CheckboxItem();
			beforeDItem.setTitle("beforeDelete");
			afterDItem = new CheckboxItem();
			afterDItem.setTitle("afterDelete");
			afterRItem = new CheckboxItem();
			afterRItem.setTitle("afterRestore");
			afterPItem = new CheckboxItem();
			afterPItem.setTitle("afterPurge");
			onLoadItem = new CheckboxItem();
			onLoadItem.setTitle("onLoad");
			beforeValidateItem = new CheckboxItem();
			beforeValidateItem.setTitle("beforeValidate");

			scriptEventItemForm = new DynamicForm();
			scriptEventItemForm.setNumCols(9);
			scriptEventItemForm.setHeight(100);
			scriptEventItemForm.setIsGroup(true);
			scriptEventItemForm.setGroupTitle("Events");
			scriptEventItemForm.setItems(beforeIItem, afterIItem, beforeUItem, afterUItem, SmartGWTUtil.createSpacer(),
					beforeDItem, afterDItem, afterRItem, afterPItem, SmartGWTUtil.createSpacer(),
					onLoadItem, beforeValidateItem);

			//---------------------------------
			//Java Class
			//---------------------------------
			javaClassNameItem = new MtpTextItem();
			javaClassNameItem.setTitle("Class Name");
			SmartGWTUtil.setRequired(javaClassNameItem);
			SmartGWTUtil.addHoverToFormItem(javaClassNameItem,
					AdminClientMessageUtil.getString("ui_metadata_entity_EventListenerListGrid_javaClassNameItemComment"));

			javaClassItemForm = new MtpForm();
			javaClassItemForm.setHeight(25);
			javaClassItemForm.setItems(javaClassNameItem);

			//---------------------------------
			//Send Notification
			//---------------------------------
			notificationTypeItem = new MtpSelectItem();
			notificationTypeItem.setTitle("Notification type");
			SmartGWTUtil.setRequired(notificationTypeItem);
			notificationTypeItem.addChangedHandler(new ChangedHandler() {
				@Override
				public void onChanged(ChangedEvent event) {
					MetaTemplateChange();
				}
			});
			notificationTypeItem.addChangedHandler(new ChangedHandler() {
				@Override
				public void onChanged(ChangedEvent event) {
					formVisibleChange();
				}
			});
			
			tmplDefNameItem = new MtpSelectItem("template", "Template");
			SmartGWTUtil.setRequired(tmplDefNameItem);
			

			sendNotificationForm = new MtpForm();
			sendNotificationForm.setHeight(50);
			sendNotificationForm.setItems(notificationTypeItem, tmplDefNameItem);
			
			//---------------------------------
			//WebHook
			//---------------------------------
			webhookLayout = new VLayout();
			webhookLayout.setWidth("100%");
			webHookSettingsForm = new MtpForm();
			webHookSettingsForm.setNumCols(2);
			notificationResultHandlerItem = new TextItem("ResultHandler","ResuleHandlerImplClassName");
			notificationResultHandlerItem.setWidth(575);
			webHookSettingsForm.setItems(notificationResultHandlerItem);
			
			webHookIsSynchronousItem = new CheckboxItem("webHookIsSynchronous","Synchronous");
			webHookIsSynchronousItem.setShowTitle(false);
			MtpForm webHookIsSynchronousForm = new MtpForm();
			webHookIsSynchronousForm.setHeight(25);
			webHookIsSynchronousForm.setItems(webHookIsSynchronousItem);
			
			VLayout editEndPointButtonLayout =  new VLayout();
			editEndPointButtonLayout.setLeft(660);
			editEndPointButtonLayout.setWidth100();
			editEndPointButtonLayout.setLayoutBottomMargin(3);
			IButton editEndPointButton = new IButton("Edit End Point");
			editEndPointButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					webHookEndPointEditMap();
				}
			});
			editEndPointButtonLayout.addMember(editEndPointButton);
			
			
			webHookEndPointGrid = new WebHookEndPointGrid();
			webHookEndPointGrid.setWidth("100%");
			webHookEndPointGrid.setHeight(300);
			webHookEndPointGrid.setTitle("End Point Address");

			webhookLayout.addMembers(webHookSettingsForm,webHookIsSynchronousForm,editEndPointButtonLayout,webHookEndPointGrid);
			
			//---------------------------------
			//Notification Condition
			//---------------------------------
			ButtonItem editNotificationCond = new ButtonItem("editNotificationCond", "Edit");
			editNotificationCond.setWidth(100);
			editNotificationCond.setStartRow(false);
			editNotificationCond.setColSpan(3);
			editNotificationCond.setAlign(Alignment.RIGHT);
			editNotificationCond.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

				@Override
				public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
					MetaDataUtil.showScriptEditDialog(ScriptEditorDialogMode.GROOVY_SCRIPT,
							SmartGWTUtil.getStringValue(notificationCondScriptItem),
							ScriptEditorDialogCondition.ENTITY_EVENT_LISTNER,
							"ui_metadata_entity_EventListenerListGrid_notificationCondScriptHint",
							null,
							new ScriptEditorDialogHandler() {

								@Override
								public void onSave(String text) {
									notificationCondScriptItem.setValue(text);
								}
								@Override
								public void onCancel() {
								}
							});
				}
			});

			notificationCondScriptItem = new MtpTextAreaItem();
			notificationCondScriptItem.setColSpan(2);
			notificationCondScriptItem.setTitle("Notification condition");
			notificationCondScriptItem.setHeight("100%");
			SmartGWTUtil.setReadOnlyTextArea(notificationCondScriptItem);

			notificationCondForm = new MtpForm();
			notificationCondForm.setHeight100();
			notificationCondForm.setItems(editNotificationCond, notificationCondScriptItem);

			notifyBeforeIItem = new CheckboxItem();
			notifyBeforeIItem.setTitle("beforeInsert");
			notifyAfterIItem = new CheckboxItem();
			notifyAfterIItem.setTitle("afterInsert");
			notifyBeforeUItem = new CheckboxItem();
			notifyBeforeUItem.setTitle("beforeUpdate");
			notifyAfterUItem = new CheckboxItem();
			notifyAfterUItem.setTitle("afterUpdate");
			notifyBeforeDItem = new CheckboxItem();
			notifyBeforeDItem.setTitle("beforeDelete");
			notifyAfterDItem = new CheckboxItem();
			notifyAfterDItem.setTitle("afterDelete");
			notifyAfterRItem = new CheckboxItem();
			notifyAfterRItem.setTitle("afterRestore");
			notifyAfterPItem = new CheckboxItem();
			notifyAfterPItem.setTitle("afterPurge");
			notifyOnLoadItem = new CheckboxItem();
			notifyOnLoadItem.setTitle("onLoad");
			notifyBeforeValidateItem = new CheckboxItem();
			notifyBeforeValidateItem.setTitle("beforeValidate");

			notifyEventItemForm = new DynamicForm();
			notifyEventItemForm.setNumCols(9);
			notifyEventItemForm.setHeight(100);
			notifyEventItemForm.setIsGroup(true);
			notifyEventItemForm.setGroupTitle("Events");
			notifyEventItemForm.setItems(notifyBeforeIItem, notifyAfterIItem, notifyBeforeUItem, notifyAfterUItem, SmartGWTUtil.createSpacer(),
					notifyBeforeDItem, notifyAfterDItem, notifyAfterRItem, notifyAfterPItem,SmartGWTUtil.createSpacer(),
					notifyOnLoadItem, notifyBeforeValidateItem);

			//---------------------------------
			//withoutMappedByReference
			//---------------------------------
			withoutMappedByReferenceItem = new CheckboxItem();
			withoutMappedByReferenceItem.setTitle("mapped by reference info is unnecessary for listner.");
			withoutMappedByReferenceItem.setShowTitle(false);
			SmartGWTUtil.addHoverToFormItem(withoutMappedByReferenceItem,
					AdminClientMessageUtil.getString("ui_metadata_entity_EventListenerListGrid_withoutMappByRefComment"));

			withoutMappedByReferenceItemForm = new MtpForm();
			withoutMappedByReferenceItemForm.setHeight(25);
			withoutMappedByReferenceItemForm.setItems(withoutMappedByReferenceItem);

			//---------------------------------
			//Footer
			//---------------------------------
			IButton ok = new IButton("OK");
			ok.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if (!validate()) {
						return;
					}
					if (isNewRow) {
						addData(target);
					}
					updateRecordData();
					destroy();
				}
			});
			IButton cancel = new IButton("Cancel");
			cancel.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					destroy();
				}
			});

			footer.setMembers(ok, cancel);
		}

		private void dataInitialize() {
			LinkedHashMap<String, String> typeMap = new LinkedHashMap<String, String>();
			typeMap.put(SCRIPT, "Script");
			typeMap.put(JAVACLASS, "JavaClass");
			typeMap.put(SENDNOTIFICATION, "SendNotification");

			typeItem.setValueMap(typeMap);

			typeItem.setValue(target.getElName());

			scriptItem.setValue(target.getScript());
			afterDItem.setValue(target.isAfterD());
			afterIItem.setValue(target.isAfterI());
			afterUItem.setValue(target.isAfterU());
			beforeDItem.setValue(target.isBeforeD());
			beforeIItem.setValue(target.isBeforeI());
			beforeUItem.setValue(target.isBeforeU());
			afterRItem.setValue(target.isAfterR());
			afterPItem.setValue(target.isAfterP());
			onLoadItem.setValue(target.isOnLoad());
			beforeValidateItem.setValue(target.isBeforeValidate());

			javaClassNameItem.setValue(target.getClassName());

			LinkedHashMap<String, String> notificationTypeMap = new LinkedHashMap<String, String>();
			notificationTypeMap.put(SendNotificationType.MAIL.name(), SendNotificationType.MAIL.displayName());
			notificationTypeMap.put(SendNotificationType.SMS.name(), SendNotificationType.SMS.displayName());
			notificationTypeMap.put(SendNotificationType.PUSH.name(), SendNotificationType.PUSH.displayName());
			notificationTypeMap.put(SendNotificationType.WEBHOOK.name(), SendNotificationType.WEBHOOK.displayName());
			notificationTypeItem.setValueMap(notificationTypeMap);

			notificationTypeItem.setValue(target.getNotificationType());
			notificationCondScriptItem.setValue(target.getNotificationCondScript());
			tmplDefNameItem.setValue(target.getTmplDefName());
			notifyAfterDItem.setValue(target.isNotifyAfterD());
			notifyAfterIItem.setValue(target.isNotifyAfterI());
			notifyAfterUItem.setValue(target.isNotifyAfterU());
			notifyBeforeDItem.setValue(target.isNotifyBeforeD());
			notifyBeforeIItem.setValue(target.isNotifyBeforeI());
			notifyBeforeUItem.setValue(target.isNotifyBeforeU());
			notifyAfterRItem.setValue(target.isNotifyAfterR());
			notifyAfterPItem.setValue(target.isNotifyAfterP());
			notifyOnLoadItem.setValue(target.isNotifyOnLoad());
			notifyBeforeValidateItem.setValue(target.isNotifyBeforeValidate());
			
			webHookIsSynchronousItem.setValue(target.isSyncrhonous());
			notificationResultHandlerItem.setValue(target.getWebHookResultHandler());
			
			webHookEndPointGrid.initializeGrid(target.getWebHookEndPointList(), false);
			withoutMappedByReferenceItem.setValue(target.isWithoutMappedByReference());
			
			//既にnotificationTypeがあったらtemplateの選択肢もロードします
			if (notificationTypeItem.getValueAsString()!=null&&!notificationTypeItem.getValueAsString().isEmpty()) {
				MetaTemplateChange();
			}
		}

		private void formVisibleChange() {
			typeItemForm.clearErrors(true);

			if (typeLayout.contains(scriptItemForm)) {
				scriptItemForm.clearErrors(true);
				typeLayout.removeMember(scriptItemForm);
			}
			if (typeLayout.contains(scriptEventItemForm)) {
				scriptEventItemForm.clearErrors(true);
				typeLayout.removeMember(scriptEventItemForm);
			}
			if (typeLayout.contains(javaClassItemForm)) {
				javaClassItemForm.clearErrors(true);
				typeLayout.removeMember(javaClassItemForm);
			}
			if (typeLayout.contains(sendNotificationForm)) {
				sendNotificationForm.clearErrors(true);
				typeLayout.removeMember(sendNotificationForm);
			}
			if (typeLayout.contains(notificationCondForm)) {
				notificationCondForm.clearErrors(true);
				typeLayout.removeMember(notificationCondForm);
			}
			if (typeLayout.contains(notifyEventItemForm)) {
				notifyEventItemForm.clearErrors(true);
				typeLayout.removeMember(notifyEventItemForm);
			}
			if (typeLayout.contains(withoutMappedByReferenceItemForm)) {
				withoutMappedByReferenceItemForm.clearErrors(true);
				typeLayout.removeMember(withoutMappedByReferenceItemForm);
			}
			if (typeLayout.contains(webhookLayout)) {
				typeLayout.removeMember(webhookLayout);
			}

			String selectValType = SmartGWTUtil.getStringValue(typeItem);
			if (SCRIPT.equals(selectValType)) {
				typeLayout.addMember(scriptItemForm);
				typeLayout.addMember(scriptEventItemForm);
				typeLayout.addMember(withoutMappedByReferenceItemForm);
				setHeight(470);
				centerInPage();
			} else if (JAVACLASS.equals(selectValType)) {
				typeLayout.addMember(javaClassItemForm);
				typeLayout.addMember(withoutMappedByReferenceItemForm);
				setHeight(190);
			} else if (SENDNOTIFICATION.equals(selectValType)) {
				setHeight(470);
				typeLayout.addMember(sendNotificationForm);
				
				if (notificationTypeItem.getValueAsString()!=null) {
					if (notificationTypeItem.getValueAsString().equals(SendNotificationType.WEBHOOK.name())) {
						typeLayout.addMember(webhookLayout);
						setHeight(800);
					}
				}
				typeLayout.addMember(notificationCondForm);
				if (notificationTypeItem.getValueAsString()!=null) {
					if (notificationTypeItem.getValueAsString().equals(SendNotificationType.WEBHOOK.name())) {
						typeLayout.addMember(webhookLayout);
						setHeight(800);
					}
				}
				typeLayout.addMember(notifyEventItemForm);
				typeLayout.addMember(withoutMappedByReferenceItemForm);
				

				centerInPage();
			} else {
				setHeight(200);
			}
			typeLayout.markForRedraw();
		}

		private void MetaTemplateChange() {
			sendNotificationForm.clearErrors(true);
			String selectValType = SmartGWTUtil.getStringValue(notificationTypeItem);
			SendNotificationType type = SendNotificationType.valueOf(selectValType);
			MetaDataNameDS.setDataSource(tmplDefNameItem, type.definitionClass(), new MetaDataNameDSOption(true, false));
		}

		private boolean validate() {
			boolean isValidate = true;
			if (!typeItemForm.validate()) {
				isValidate = false;
			}
			if (typeLayout.contains(scriptItemForm)) {
				if (!scriptItemForm.validate()) {
					isValidate = false;
				}
			}
			if (typeLayout.contains(scriptEventItemForm)) {
				if (!scriptEventItemForm.validate()) {
					isValidate = false;
				}
			}
			if (typeLayout.contains(javaClassItemForm)) {
				if (!javaClassItemForm.validate()) {
					isValidate = false;
				}
			}
			if (typeLayout.contains(sendNotificationForm)) {
				if (!sendNotificationForm.validate()) {
					isValidate = false;
				}
			}
			if (typeLayout.contains((notificationCondForm))) {
				if (!notificationCondForm.validate()) {
					isValidate = false;
				}
			}
			if (typeLayout.contains(notifyEventItemForm)) {
				if (!notifyEventItemForm.validate()) {
					isValidate = false;
				}
			}
			if (typeLayout.contains(withoutMappedByReferenceItemForm)) {
				if (!withoutMappedByReferenceItemForm.validate()) {
					isValidate = false;
				}
			}
			if (typeLayout.contains(webhookLayout)) {
				if (webhookLayout.contains(webHookSettingsForm)) {
					if(!webHookSettingsForm.validate()) {
						isValidate = false;
					}
				}
			}
			return isValidate;
		}

		private void updateRecordData() {
			String selectValType = SmartGWTUtil.getStringValue(typeItem);
			target.setElName(selectValType);
			if (SCRIPT.equals(selectValType)) {
				target.setScript(SmartGWTUtil.getStringValue(scriptItem));
				target.setGeneralPurpus(SmartGWTUtil.getStringValue(scriptItem));

				target.setAfterD(SmartGWTUtil.getBooleanValue(afterDItem));
				target.setAfterI(SmartGWTUtil.getBooleanValue(afterIItem));
				target.setAfterU(SmartGWTUtil.getBooleanValue(afterUItem));
				target.setBeforeD(SmartGWTUtil.getBooleanValue(beforeDItem));
				target.setBeforeI(SmartGWTUtil.getBooleanValue(beforeIItem));
				target.setBeforeU(SmartGWTUtil.getBooleanValue(beforeUItem));
				target.setAfterR(SmartGWTUtil.getBooleanValue(afterRItem));
				target.setAfterP(SmartGWTUtil.getBooleanValue(afterPItem));
				target.setOnLoad(SmartGWTUtil.getBooleanValue(onLoadItem));
				target.setBeforeValidate(SmartGWTUtil.getBooleanValue(beforeValidateItem));
			} else if (JAVACLASS.equals(selectValType)) {
				target.setClassName(SmartGWTUtil.getStringValue(javaClassNameItem));
				target.setGeneralPurpus(SmartGWTUtil.getStringValue(javaClassNameItem));
			} else if (SENDNOTIFICATION.equals(selectValType)) {
				target.setNotificationType(SmartGWTUtil.getStringValue(notificationTypeItem));
				target.setTmplDefName(SmartGWTUtil.getStringValue(tmplDefNameItem));
				target.setGeneralPurpus(SmartGWTUtil.getStringValue(notificationTypeItem));
				target.setNotificationCondScript(SmartGWTUtil.getStringValue(notificationCondScriptItem));
				
				target.setNotifyAfterD(SmartGWTUtil.getBooleanValue(notifyAfterDItem));
				target.setNotifyAfterI(SmartGWTUtil.getBooleanValue(notifyAfterIItem));
				target.setNotifyAfterU(SmartGWTUtil.getBooleanValue(notifyAfterUItem));
				target.setNotifyBeforeD(SmartGWTUtil.getBooleanValue(notifyBeforeDItem));
				target.setNotifyBeforeI(SmartGWTUtil.getBooleanValue(notifyBeforeIItem));
				target.setNotifyBeforeU(SmartGWTUtil.getBooleanValue(notifyBeforeUItem));
				target.setNotifyAfterR(SmartGWTUtil.getBooleanValue(notifyAfterRItem));
				target.setNotifyAfterP(SmartGWTUtil.getBooleanValue(notifyAfterPItem));
				target.setNotifyOnLoad(SmartGWTUtil.getBooleanValue(notifyOnLoadItem));
				target.setNotifyBeforeValidate(SmartGWTUtil.getBooleanValue(notifyBeforeValidateItem));
				target.setWebHookResultHandler(SmartGWTUtil.getStringValue(notificationResultHandlerItem));
				target.setSyncrhonous(SmartGWTUtil.getBooleanValue(webHookIsSynchronousItem));
			}
			target.setWithoutMappedByReference(SmartGWTUtil.getBooleanValue(withoutMappedByReferenceItem));
			updateData(target);
			refreshFields();
		}

		private void webHookEndPointEditMap() {
			List<String> tempList = target.getWebHookEndPointList();
			if (tempList==null) {
				tempList = new ArrayList<String>();
			}
			WebHookEndPointDialog endPointDialog = new WebHookEndPointDialog(tempList);

			
			endPointDialog.addDataChangeHandler(new DataChangedHandler() {
				@Override
				public void onDataChanged(DataChangedEvent event) {
					@SuppressWarnings("unchecked")
					HashMap<String, String> tempMap = (HashMap<String, String>)event.getValueObject(Object.class);
					ArrayList<String> tempList = new ArrayList<String>();
					if (tempMap ==null || tempMap.keySet()==null) {
					} else {
						tempList = new ArrayList<String>(tempMap.keySet());
					}
					target.setWebHookEndPointList(tempList);
					webHookEndPointGrid.setData(webHookEndPointGrid.createWebHookEndPointRecord(tempMap,(ArrayList<String>)target.getWebHookEndPointList(),false));
					webHookEndPointGrid.markForRedraw();
					endPointDialog.destroy();
				}
			});
			endPointDialog.show();
			endPointDialog.markForRedraw();
		}

		private class WebHookEndPointGrid extends ListGrid{
			MetaDataServiceAsync metaDataService;
			ListGridField endPointNameField;
			ListGridField endPointUrlField;
			ListGridField endPointSelectedField;
			private WebHookEndPointGrid(){
				metaDataService=MetaDataServiceFactory.get();
				setTitle("WebEndPont");
				setBodyOverflow(Overflow.SCROLL);
				endPointNameField = new ListGridField("webHookEndPoint", "EndPoint");
				endPointNameField.setCanEdit(false);
				endPointUrlField= new ListGridField("webHookUrl", "URL");
				endPointUrlField.setCanEdit(false);
				endPointSelectedField = new ListGridField("webHookendPointSelected","Selected");
				endPointSelectedField.setWidth(40);
				endPointSelectedField.setType(ListGridFieldType.BOOLEAN);
				endPointSelectedField.setCanEdit(true);
			}

			private void initializeGrid(List<String> endPontNameList, boolean isEdit) {
				metaDataService.getEndPointFullListWithUrl(TenantInfoHolder.getId(), new AsyncCallback<Map<String,String>>() {

					@Override
					public void onFailure(Throwable caught) {
						GWT.log("Failed to fetch WebHookEndPointData.", caught);
					}
					@Override
					public void onSuccess(Map<String, String> result) {
						if(isEdit) {
							WebHookEndPointGrid.this.setFields(endPointSelectedField,endPointNameField,endPointUrlField);
						} else {
							WebHookEndPointGrid.this.setFields(endPointNameField,endPointUrlField);
						}
						WebHookEndPointGrid.this.setData(createWebHookEndPointRecord(result,endPontNameList,isEdit));
						WebHookEndPointGrid.this.markForRedraw();
					}
				});
			}
			private ListGridRecord[] createWebHookEndPointRecord(Map<String,String> result, List<String> endPontNameList, boolean isEdit) {
				if (isEdit) {
					if (result!=null) {
						ListGridRecord[] temp= new ListGridRecord[result.size()];
						int i = 0;
						for(Map.Entry<String, String> entry : result.entrySet()) {
							ListGridRecord record = new ListGridRecord();
						    String key = entry.getKey();
						    String value = entry.getValue();
							record.setAttribute("webHookEndPoint", key);
							record.setAttribute("webHookUrl", value);
							if (endPontNameList==null) {
								record.setAttribute("webHookendPointSelected", false);
							} else if (endPontNameList.contains(key)) {
								record.setAttribute("webHookendPointSelected", true);
							} else {
								record.setAttribute("webHookendPointSelected", false);
							}
							temp[i] = record;
							i++;
						}
						return temp;
					} else {
						return null;
					}
				} else {
					if (result!=null) {
						ListGridRecord[] temp= new ListGridRecord[endPontNameList.size()];
						int i = 0;
						for(String endPointDefName : endPontNameList) {
						    ListGridRecord record = new ListGridRecord();
							record.setAttribute("webHookEndPoint", endPointDefName);
							record.setAttribute("webHookUrl", result.get(endPointDefName));
							temp[i] = record;
							i++;
						}
						return temp;	
					} else {
						return null;
					}
				}
			}
			private HashMap<String, String> getSelectedEndPointNameUrlPair() {
				HashMap<String, String> selectedEndPointDefName = new HashMap<String, String>();
				ListGridRecord[] result = this.getRecords();
				if (result!=null) {
					for (int i =0; i<result.length;i++) {
						if (result[i].getAttributeAsBoolean("webHookendPointSelected")) {
							selectedEndPointDefName.put(result[i].getAttributeAsString("webHookEndPoint"), result[i].getAttributeAsString("webHookUrl"));
						}
					}
				}
				return selectedEndPointDefName;
			}
		}
		private class WebHookEndPointDialog extends MtpDialog{
			WebHookEndPointGrid editGrid;

			private List<DataChangedHandler> handlers = new ArrayList<DataChangedHandler>();
			WebHookEndPointDialog(List<String>endPontNameList){
				setHeight("80%");
				setWidth("60%");
				centerInPage();
				setTitle("Edit End Point");
				editGrid = new WebHookEndPointGrid();
				editGrid.setWidth100();
				editGrid.setHeight100();
				editGrid.initializeGrid(endPontNameList, true);
				container.addMember(editGrid);
				
				IButton save = new IButton("Save");
				save.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
					public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
						HashMap<String, String> selectedEndPointDefName = editGrid.getSelectedEndPointNameUrlPair();
						fireDataChanged(selectedEndPointDefName);
						destroy();
					}


				});

				IButton cancel = new IButton("Cancel");
				cancel.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
					public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
						destroy();
					}
				});
				footer.setMembers(save, cancel);
			}
			
			public void addDataChangeHandler(DataChangedHandler handler) {
				handlers.add(0, handler);
			}
			
			private void fireDataChanged(HashMap<String, String> selectedEndPointDefNameAndUrl) {
				DataChangedEvent event = new DataChangedEvent();
				event.setValueObject(selectedEndPointDefNameAndUrl);
				for (DataChangedHandler handler : handlers) {
					handler.onDataChanged(event);
				}
			}
		}
	}
}
