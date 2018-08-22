/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.tools.ui.entityexplorer.datalist;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.io.upload.AdminUploadStatus;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.ui.widget.MessageTabSet;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.tools.data.entityexplorer.UniquePropertyDS;
import org.iplass.adminconsole.client.tools.ui.entityexplorer.datalist.EntityCsvDownloadDialog.ENCODE;
import org.iplass.adminconsole.shared.metadata.dto.MetaDataConstants;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.definition.EntityDefinition;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.XMLParser;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CanvasItem;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.UploadedInfo;
import gwtupload.client.SingleUploader;
import gwtupload.client.Utils;

/**
 * EntityImportダイアログ
 */
public class EntityCsvUploadDialog extends AbstractWindow {

	private static final String RESOURCE_PREFIX = "ui_tools_entityexplorer_EntityCsvUploadDialog_";
	private static final String UPLOAD_SERVICE = "service/entityupload";

	private String defName;

	private VLayout mainLayout;

	private CheckboxItem chkTruncateField;
	private CheckboxItem chkForceUpdateField;
	private CheckboxItem chkErrorSkipField;
	private CheckboxItem chkIgnoreNotExistsPropertyField;
	private CheckboxItem chkNotifyListenersField;
	private CheckboxItem chkWithValidationField;
	private CheckboxItem chkUpdateDisupdatablePropertyField;
	private SelectItem uniqueKeyField;
	private SelectItem commitLimitField;
	private TextItem prefixOidField;

	private ComboBoxItem localeField;
	private ComboBoxItem timeZoneField;

	private VLayout targetLayout;
	private HLayout footer;

	private MessageTabSet messageTabSet;

	private IButton upload;
	private IButton cancel;

	/**
	 * コンストラクタ
	 */
	public EntityCsvUploadDialog(EntityDefinition definition) {
		this.defName = definition.getName();

		setWidth(900);
		setMinWidth(700);
		setHeight(640);
		setMinHeight(640);
		setTitle("Import Entity Data : " + defName);
		setCanDragResize(true);
		setShowMinimizeButton(false);
		setShowMaximizeButton(true);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();

		mainLayout = new VLayout();
		mainLayout.setWidth100();
		mainLayout.setHeight100();
		mainLayout.setMargin(10);

		addItem(mainLayout);

		targetLayout = new VLayout(5);
		targetLayout.setWidth100();
		targetLayout.setMargin(5);
		targetLayout.setHeight(150);

		HLayout fileComposit = new HLayout(5);
		fileComposit.setWidth100();
		fileComposit.setHeight(25);

		Label fileLabel = new Label("File :");
		fileLabel.setWordWrap(false);
		fileLabel.setWidth("90");
		fileComposit.addMember(fileLabel);

		//IButton dummy = new IButton();
		//Label dummy = new Label("&nbsp;");
		Label dummy = new Label("");
		final SingleUploader uploader = new SingleUploader(new AdminUploadStatus(), dummy);
		IUploader.UploaderConstants constants = GWT.create(CustomUploaderConstants.class);
		uploader.setI18Constants(constants);	//dummyに対してメッセージが表示されるので、空実装
		uploader.setServletPath(GWT.getModuleBaseURL() + UPLOAD_SERVICE);
		uploader.setAutoSubmit(false);
		uploader.getFileInput().asWidget().setHeight("20px");
		uploader.getFileInput().asWidget().setWidth(MetaDataConstants.DEFAULT_FORM_ITEM_WIDTH + "px");
//		uploader.setWidth("100%");
		uploader.setValidExtensions("csv");
		uploader.addOnStartUploadHandler(new IUploader.OnStartUploaderHandler() {

			@Override
			public void onStart(IUploader uploader) {
				startExecute();
			}
		});
		uploader.addOnStatusChangedHandler(new IUploader.OnStatusChangedHandler() {

			@Override
			public void onStatusChanged(IUploader uploader) {
				GWT.log("onStatusChanged: status            ->" + uploader.getStatus());
				UploadedInfo info = uploader.getServerInfo();

				if (info == null) {
					GWT.log("onStatusChanged: UploadedInfo is Null.");
				} else {
					GWT.log("onStatusChanged: File name         ->" + info.name);
					GWT.log("onStatusChanged: File content-type ->" + info.ctype);
					GWT.log("onStatusChanged: File size         ->" + info.size);
				}
				GWT.log("onStatusChanged: message           ->" + uploader.getServerMessage().getMessage());
			}
		});
		uploader.addOnFinishUploadHandler(new IUploader.OnFinishUploaderHandler() {

			@Override
			public void onFinish(IUploader uploaderResult) {
				if (uploaderResult.getStatus() == Status.SUCCESS) {
					UploadedInfo info = uploaderResult.getServerInfo();

					GWT.log("onFinish: status                   ->" + uploaderResult.getStatus());

					if (info == null) {
						GWT.log("onFinish: UploadedInfo is Null.");
					} else{
						GWT.log("onFinish: File name                ->" + info.name);
						GWT.log("onFinish: File content-type        ->" + info.ctype);
						GWT.log("onFinish: File size                ->" + info.size);
					}
					GWT.log("onFinish: message                  ->" + uploaderResult.getServerMessage().getMessage());

					// Here is the string returned in your servlet
					showResult(uploaderResult.getServerMessage().getMessage());
				} else {
					UploadedInfo info = uploaderResult.getServerInfo();
					GWT.log("onFinish: status                   ->" + uploaderResult.getStatus());
					GWT.log("onFinish: ServerResponse           ->" + uploaderResult.getServerRawResponse());
					if (info == null) {
						GWT.log("onFinish: UploadedInfo is Null.");
					} else {
						GWT.log("onFinish: File name                ->" + info.name);
						GWT.log("onFinish: File content-type        ->" + info.ctype);
						GWT.log("onFinish: File size                ->" + info.size);
					}
					GWT.log("onFinish: message                  ->" + uploaderResult.getServerMessage().getMessage());

					//ServerResponseからメッセージ作成
					showResultError(getErrorMessage(uploaderResult.getServerRawResponse()));
				}
				//Hidden項目の削除（uploader#reset、clearなどではうまくいかないため下の方法でクリア）
				Widget form = uploader.getForm().getWidget();
				if (form instanceof FlowPanel) {
					FlowPanel formPanel = (FlowPanel)form;
					for (int i = formPanel.getWidgetCount() - 1; i >= 0; i--) {
						Widget child = formPanel.getWidget(i);
						if (child instanceof Hidden) {
							formPanel.remove(child);
						}
					}
				}
			}

			private String getErrorMessage(String response) {
				Document doc = XMLParser.parse(response);
				return Utils.getXmlNodeValue(doc, "error");
			}
		});

		fileComposit.addMember(uploader);
//		csvEncodeField = new ListBox();
//		csvEncodeField.setName("csvEncodeField");
//		csvEncodeField.addItem("UTF-8", "UTF-8");
//		csvEncodeField.addItem("MS932", "MS932");
//		csvEncodeField.setSelectedIndex(0);
//		csvEncodeField.setEnabled(false);	//UTF-8固定
//		fileLayout.addMember(csvEncodeField);

		targetLayout.addMember(fileComposit);

		upload = new IButton("Import");
		upload.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (uploader.getFileName() == null || uploader.getFileName().isEmpty()) {
					SC.warn(getResourceString("selectImportFile"));
					return;
				}
				//if (User.DEFINITION_NAME.equals(EntityCsvUploadDialog.this.defName)
				if ("mtp.auth.User".equals(EntityCsvUploadDialog.this.defName)
						&& SmartGWTUtil.getBooleanValue(chkTruncateField)) {
					//ユーザEntityを削除する場合は警告

					SC.ask(getResourceString("confirm"), getResourceString("userTruncateConfirm"), new BooleanCallback() {

						@Override
						public void execute(Boolean value) {
							if (value) {
								doUpload(uploader);
							}
						}
					});

				} else {
					doUpload(uploader);
				}
			}
		});

		DynamicForm entityForm = new DynamicForm();
//		entityForm.setMargin(10);
		entityForm.setPadding(5);
		entityForm.setWidth100();
		entityForm.setAlign(Alignment.CENTER);
		entityForm.setIsGroup(true);
		entityForm.setGroupTitle("Entity Data Import Setting:");
		entityForm.setNumCols(1);
		entityForm.setColWidths("*");

		chkTruncateField = new CheckboxItem();
		chkTruncateField.setTitle(getResourceString("truncate"));
		chkTruncateField.setShowTitle(false);

		chkForceUpdateField = new CheckboxItem();
		chkForceUpdateField.setTitle(getResourceString("forceUpdate"));
		chkForceUpdateField.setShowTitle(false);

		chkErrorSkipField = new CheckboxItem();
		chkErrorSkipField.setTitle(getResourceString("errorDataSkip"));
		chkErrorSkipField.setShowTitle(false);

		chkIgnoreNotExistsPropertyField = new CheckboxItem();
		chkIgnoreNotExistsPropertyField.setTitle(getResourceString("ignoreNotExistsProperty"));
		chkIgnoreNotExistsPropertyField.setShowTitle(false);
		chkIgnoreNotExistsPropertyField.setValue(true);	//デフォルトtrue

		chkNotifyListenersField = new CheckboxItem();
		chkNotifyListenersField.setTitle(getResourceString("notifyListener"));
		chkNotifyListenersField.setShowTitle(false);

		chkWithValidationField = new CheckboxItem();
		chkWithValidationField.setTitle(getResourceString("withValidation"));
		chkWithValidationField.setShowTitle(false);

		chkUpdateDisupdatablePropertyField = new CheckboxItem();
		chkUpdateDisupdatablePropertyField.setTitle(getResourceString("updateDisupdatableProperty"));
		chkUpdateDisupdatablePropertyField.setShowTitle(false);
		chkUpdateDisupdatablePropertyField.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				//更新不可項目も更新する場合は、Validationをfalseに設定、入力不可にする
				if (SmartGWTUtil.getBooleanValue(chkUpdateDisupdatablePropertyField)) {
					chkWithValidationField.setValue(false);
					chkWithValidationField.setDisabled(true);
				} else {
					chkWithValidationField.setDisabled(false);
				}
			}
		});

		uniqueKeyField = new SelectItem();
		uniqueKeyField.setTitle("Unique Key");
		UniquePropertyDS.setDataSource(uniqueKeyField, definition);
		uniqueKeyField.setValue(Entity.OID);

		commitLimitField = new SelectItem();
		commitLimitField.setTitle(getResourceString("commitUnit"));
		LinkedHashMap<String, String> commitValues = new LinkedHashMap<String, String>();
		commitValues.put("1", getResourceString("one"));
		commitValues.put("10", getResourceString("ten"));
		commitValues.put("100", getResourceString("hundred"));
		commitValues.put("1000", getResourceString("thousand"));
		commitValues.put("-1", getResourceString("all"));
		commitLimitField.setDefaultValue("100");
		commitLimitField.setValueMap(commitValues);

		prefixOidField = new TextItem();
		prefixOidField.setTitle("OID Prefix");
		prefixOidField.setKeyPressFilter("[A-Za-z0-9]");	//英数字のみ
		prefixOidField.setHint(getResourceString("preOidHint"));

		HLayout settingLayout = new HLayout();
		CanvasItem settingItem = new CanvasItem();
		settingItem.setShowTitle(false);
		settingItem.setCanvas(settingLayout);
		settingItem.setCanFocus(true);

		DynamicForm leftForm = new DynamicForm();
		leftForm.setWidth100();
		leftForm.setAlign(Alignment.CENTER);
		leftForm.setNumCols(1);
		leftForm.setColWidths("*");

		DynamicForm rightForm = new DynamicForm();
		rightForm.setWidth100();
		rightForm.setAlign(Alignment.CENTER);
		rightForm.setNumCols(2);
		rightForm.setColWidths(90, "*");

		settingLayout.addMember(leftForm);
		settingLayout.addMember(rightForm);

		leftForm.setItems(chkTruncateField, chkNotifyListenersField, chkWithValidationField, chkUpdateDisupdatablePropertyField,
				chkForceUpdateField, chkErrorSkipField, chkIgnoreNotExistsPropertyField);

		rightForm.setItems(prefixOidField, uniqueKeyField, commitLimitField);

		VLayout hintLayout = new VLayout();
		CanvasItem hintItem = new CanvasItem();
		hintItem.setShowTitle(false);
		hintItem.setCanvas(hintLayout);

		hintLayout.addMember(getLabel("truncateComment"));
		hintLayout.addMember(getLabel("listenerComment"));
		hintLayout.addMember(getLabel("updateDisupdatablePropertyComment1"));
		hintLayout.addMember(getLabel("preOidComment1"));
		hintLayout.addMember(getLabel("preOidComment2"));
		hintLayout.addMember(getLabel("useCtrlComment1"));
		hintLayout.addMember(getLabel("binaryComment"));

		entityForm.setItems(settingItem, hintItem);

		DynamicForm i18nForm = new DynamicForm();
		//i18nForm.setMargin(10);
		i18nForm.setWidth100();
		i18nForm.setPadding(5);
		i18nForm.setIsGroup(true);
		i18nForm.setGroupTitle("i18n Support Setting:");
		i18nForm.setNumCols(5);
//		i18nForm.setColWidths(90, "*", 90, "*", "*");
		i18nForm.setColWidths(90, 170, 120, "*", "*");	//2つの幅を狭めたいので2番目のWidthも指定

		localeField = new ComboBoxItem();
		localeField.setTitle("File Locale");
		localeField.setValueMap(SmartGWTUtil.getDefaultLocaleMap());
		SmartGWTUtil.addHoverToFormItem(localeField, AdminClientMessageUtil.getString("ui_common_i18n_importLocaleTooltip"));
		localeField.setValue("");

		timeZoneField = new ComboBoxItem();
		timeZoneField.setTitle("File TimeZone");
		timeZoneField.setValueMap(SmartGWTUtil.getDefaultTimeZoneMap());
		SmartGWTUtil.addHoverToFormItem(timeZoneField, AdminClientMessageUtil.getString("ui_common_i18n_importTimezoneTooltip"));
		timeZoneField.setValue("");

		i18nForm.setItems(localeField, timeZoneField, SmartGWTUtil.createSpacer());

		footer = new HLayout(5);
		footer.setMargin(5);
		footer.setHeight(20);
		footer.setWidth100();
		//footer.setAlign(Alignment.LEFT);
		footer.setAlign(VerticalAlignment.CENTER);

		cancel = new IButton("Cancel");
		cancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				destroy();
			}
		});

		footer.setMembers(upload, cancel);

		targetLayout.addMember(entityForm);
		targetLayout.addMember(i18nForm);
		targetLayout.addMember(footer);

		messageTabSet = new MessageTabSet();

		mainLayout.addMember(targetLayout);
		mainLayout.addMember(messageTabSet);

	}

	private void startExecute() {
		disableComponent(true);
		messageTabSet.clearMessage();
		messageTabSet.setTabTitleProgress();
	}

	private void showResult(String message) {
		showResponse(message);

		disableComponent(false);
		messageTabSet.setTabTitleNormal();
	}

	private void doUpload(final SingleUploader uploader) {
		SC.ask(getResourceString("confirm"), getResourceString("startImportConf"), new BooleanCallback() {
			@Override
			public void execute(Boolean value) {
				if (value) {
					uploader.add(new Hidden("defName", EntityCsvUploadDialog.this.defName));
					uploader.add(new Hidden("tenantId", Integer.toString(TenantInfoHolder.getId())));
					if (SmartGWTUtil.getBooleanValue(chkTruncateField)) {
						uploader.add(new Hidden("chkTruncate", "1"));
					}
					if (SmartGWTUtil.getBooleanValue(chkForceUpdateField)) {
						uploader.add(new Hidden("chkForceUpdate", "1"));
					}
					if (SmartGWTUtil.getBooleanValue(chkErrorSkipField)) {
						uploader.add(new Hidden("chkErrorSkip", "1"));
					}
					if (SmartGWTUtil.getBooleanValue(chkIgnoreNotExistsPropertyField)) {
						uploader.add(new Hidden("chkIgnoreNotExistsProperty", "1"));
					}
					if (SmartGWTUtil.getBooleanValue(chkNotifyListenersField)) {
						uploader.add(new Hidden("chkNotifyListeners", "1"));
					}
					if (SmartGWTUtil.getBooleanValue(chkWithValidationField)) {
						uploader.add(new Hidden("chkWithValidation", "1"));
					}
					if (SmartGWTUtil.getBooleanValue(chkUpdateDisupdatablePropertyField)) {
						uploader.add(new Hidden("chkUpdateDisupdatableProperty", "1"));
					}
					uploader.add(new Hidden("locale", SmartGWTUtil.getStringValue(localeField)));
					uploader.add(new Hidden("timeZone", SmartGWTUtil.getStringValue(timeZoneField)));

					uploader.add(new Hidden("csvEncode", ENCODE.UTF8.name()));
					uploader.add(new Hidden("uniqueKey", SmartGWTUtil.getStringValue(uniqueKeyField)));
					uploader.add(new Hidden("commitLimit", SmartGWTUtil.getStringValue(commitLimitField)));
					uploader.add(new Hidden("prefixOid", SmartGWTUtil.getStringValue(prefixOidField)));

					uploader.submit();
				}
			}
		});
	}

	private void showResultError(String message) {
		List<String> errors = new ArrayList<String>(1);
		errors.add(getResourceString("errorImport"));
		errors.add(message);
		messageTabSet.setErrorMessage(errors);

		disableComponent(false);
		messageTabSet.setTabTitleNormal();
	}

	private void disableComponent(boolean disabled) {
		upload.setDisabled(disabled);
		cancel.setDisabled(disabled);
	}

	private void showResponse(String json) {
		GWT.log("ImportEntity Response:" + json);
		if (json == null) {
			return;
		}
		JSONValue rootValue = JSONParser.parseStrict(json);
		if (rootValue == null) {
			return;
		}

		List<String> logs = new ArrayList<String>();

		String status = getStatus(rootValue);
		logs.add(getStatusMessage(status));

		List<String> messages = getMessageInfo(rootValue);
		if (!messages.isEmpty()) {
			logs.add("-----------------------------------");
			logs.addAll(messages);
		}
		if (isStatusSuccess(status)) {
			messageTabSet.setMessage(logs);
		} else {
			messageTabSet.setErrorMessage(logs);
		}
	}

	private String getStatus(JSONValue root) {
		return snipQuote(getValue("status", root).toString());
	}

	private String getStatusMessage(String status) {
		if ("SUCCESS".equals(status)) {
			return getResourceString("importSuccessful");
		} else if ("WARN".equals(status)) {
			return getResourceString("importWarning");
		} else if ("ERROR".equals(status)) {
			return getResourceString("importErr");
		} else {
			return getResourceString("couldNotRetImportResult");
		}
	}

	private boolean isStatusSuccess(String status) {
		return "SUCCESS".equals(status);
	}

	private List<String> getMessageInfo(JSONValue root) {
		List<String> messages = new ArrayList<String>();
		JSONArray messageArray = getValue("messages", root).isArray();
		for (int i = 0; i < messageArray.size(); i++) {
			JSONValue child = messageArray.get(i);
			messages.add(snipQuote(child.toString()));
		}
		return messages;
	}

	private JSONValue getValue(String key, JSONValue root) {
		JSONObject jsonObject = root.isObject();
		return jsonObject.get(key);
	}

	private String snipQuote(String value) {
		if (value.startsWith("\"") && value.endsWith("\"")) {
			return value.substring(1, value.length() - 1);
		} else {
			return value;
		}
	}

	private com.smartgwt.client.widgets.Label getLabel(String key) {
		com.smartgwt.client.widgets.Label label = new com.smartgwt.client.widgets.Label(getResourceString(key));
		label.setHeight(20);
		return label;
	}

	private String getResourceString(String key) {
		return AdminClientMessageUtil.getString(RESOURCE_PREFIX + key);
	}

//	public interface CustomUploaderConstants extends IUploader.UploaderConstants {
//
//	    @DefaultStringValue("")
//	    String uploaderSend();
//
//	    @DefaultStringValue("ファイル拡張子が異なります。\n以下の拡張子のみ対応しています。:\n")
//	    String uploaderInvalidExtension();
//
//	}
}
