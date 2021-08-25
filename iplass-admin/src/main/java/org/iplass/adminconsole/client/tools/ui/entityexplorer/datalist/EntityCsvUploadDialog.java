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
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.io.upload.AdminSingleUploader;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.ui.widget.AnimationFullScreenCallback;
import org.iplass.adminconsole.client.base.ui.widget.MessageTabSet;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.tools.data.entityexplorer.UniquePropertyDS;
import org.iplass.adminconsole.client.tools.ui.entityexplorer.datalist.EntityCsvDownloadDialog.ENCODE;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.definition.EntityDefinition;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Label;
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
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

import gwtupload.client.IUploadStatus.Status;

/**
 * EntityImportダイアログ
 */
public class EntityCsvUploadDialog extends AbstractWindow {

	private static final String UPLOAD_SERVICE = "service/entityupload";

	private static final int MIN_WIDTH = 800;
	private static final int MIN_HEIGHT = 600;

	public static void showFullScreen(final EntityDefinition definition) {
		SmartGWTUtil.showAnimationFullScreen(new AnimationFullScreenCallback() {
			@Override
			public void execute(boolean earlyFinish) {
              animateOutline.hide();
              EntityCsvUploadDialog dialog = new EntityCsvUploadDialog(definition, width, height);
              dialog.show();
			}
		});
	}

	/**
	 * コンストラクタ
	 */
	private EntityCsvUploadDialog(EntityDefinition definition, int width, int height) {

		setWidth(width);
		setMinWidth(MIN_WIDTH);
		setHeight(height);
		setMinHeight(MIN_HEIGHT);
		setTitle("Import Entity Data : " + definition.getName());
		setCanDragResize(true);
		setShowMinimizeButton(false);
		setShowMaximizeButton(true);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();

		EntityImportPane entityImportPane = new EntityImportPane(definition);
		VLayout mainLayout = new VLayout();
		mainLayout.addMember(entityImportPane);

		addItem(mainLayout);
	}

	private class EntityImportPane extends VLayout {

		private CheckboxItem chkTruncateField;
		private CheckboxItem chkBulkUpdateField;
		private CheckboxItem chkNotifyListenersField;
		private CheckboxItem chkWithValidationField;
		private CheckboxItem chkUpdateDisupdatablePropertyField;
		private CheckboxItem chkInsertEnableAuditPropertySpecification;
		private CheckboxItem chkForceUpdateField;
		private CheckboxItem chkErrorSkipField;
		private CheckboxItem chkIgnoreNotExistsPropertyField;
		private TextItem prefixOidField;
		private SelectItem uniqueKeyField;
		private SelectItem commitLimitField;

		private ComboBoxItem localeField;
		private ComboBoxItem timeZoneField;

		private MessageTabSet messageTabSet;

		private IButton btnUpload;
		private IButton btnCancel;

		private String defName;

		public EntityImportPane(EntityDefinition definition) {
			this.defName = definition.getName();

			setWidth100();
			setHeight100();
			setMargin(10);

			HLayout contentMainPane = new HLayout();
			contentMainPane.setWidth100();
			contentMainPane.setHeight100();

			//----------
			//Condition
			//----------

			VLayout conditionLayout = new VLayout(5);
			conditionLayout.setMargin(5);
			conditionLayout.setWidth("50%");
			conditionLayout.setHeight100();

			HLayout fileComposit = new HLayout(5);
			fileComposit.setWidth100();
			fileComposit.setHeight(25);

			Label fileLabel = new Label("File :");
			fileLabel.setWordWrap(false);
			fileLabel.setWidth("90");
			fileComposit.addMember(fileLabel);

			final AdminSingleUploader uploader = new AdminSingleUploader(UPLOAD_SERVICE);
			uploader.setValidExtensions("csv");
			uploader.addOnStartUploadHandler((result) -> {
				uploader.debugUploader("onStart");
				startExecute();
			});
			uploader.addOnStatusChangedHandler((result) -> {
				uploader.debugUploader("onStatusChanged");
			});
			uploader.addOnFinishUploadHandler((result) -> {
				uploader.debugUploader("onFinish");
				if (uploader.getStatus() == Status.SUCCESS) {
					showResult(uploader.getMessage());
				} else {
					showResultError(uploader.getErrorMessage());
				}

				//Hidden項目の削除
				uploader.removeHidden();
			});

			fileComposit.addMember(uploader);

			conditionLayout.addMember(fileComposit);

			DynamicForm entityForm = new DynamicForm();
			entityForm.setPadding(5);
			entityForm.setWidth100();
			entityForm.setAlign(Alignment.CENTER);
			entityForm.setIsGroup(true);
			entityForm.setGroupTitle("Entity Data Import Setting:");
			entityForm.setNumCols(2);
			entityForm.setColWidths(90, "*");

			chkTruncateField = new CheckboxItem();
			chkTruncateField.setTitle(rs("ui_tools_entityexplorer_EntityCsvUploadDialog_truncate"));
			chkTruncateField.setShowTitle(false);
			chkTruncateField.setColSpan(2);

			chkBulkUpdateField = new CheckboxItem();
			chkBulkUpdateField.setTitle(rs("ui_tools_entityexplorer_EntityCsvUploadDialog_bulkUpdate"));
			chkBulkUpdateField.setShowTitle(false);
			chkBulkUpdateField.setColSpan(2);
			chkBulkUpdateField.addChangedHandler((e)->{

				if (SmartGWTUtil.getBooleanValue(chkBulkUpdateField)) {
					//bulkUpdateモード
					chkNotifyListenersField.setValue(false);
					chkNotifyListenersField.setDisabled(true);
					chkWithValidationField.setValue(false);
					chkWithValidationField.setDisabled(true);
					chkForceUpdateField.setValue(false);
					chkForceUpdateField.setDisabled(true);
					chkErrorSkipField.setValue(false);
					chkErrorSkipField.setDisabled(true);
					uniqueKeyField.setValue(Entity.OID);
					uniqueKeyField.setDisabled(true);
				} else {
					chkNotifyListenersField.setDisabled(false);
					if (SmartGWTUtil.getBooleanValue(chkUpdateDisupdatablePropertyField)) {
						chkWithValidationField.setDisabled(true);
					} else {
						chkWithValidationField.setDisabled(false);
					}
					chkForceUpdateField.setDisabled(false);
					chkErrorSkipField.setDisabled(false);
					uniqueKeyField.setDisabled(false);
				}
			});

			chkNotifyListenersField = new CheckboxItem();
			chkNotifyListenersField.setTitle(rs("ui_tools_entityexplorer_EntityCsvUploadDialog_notifyListener"));
			chkNotifyListenersField.setShowTitle(false);
			chkNotifyListenersField.setColSpan(2);

			chkWithValidationField = new CheckboxItem();
			chkWithValidationField.setTitle(rs("ui_tools_entityexplorer_EntityCsvUploadDialog_withValidation"));
			chkWithValidationField.setShowTitle(false);
			chkWithValidationField.setColSpan(2);

			chkUpdateDisupdatablePropertyField = new CheckboxItem();
			chkUpdateDisupdatablePropertyField.setTitle(rs("ui_tools_entityexplorer_EntityCsvUploadDialog_updateDisupdatableProperty"));
			chkUpdateDisupdatablePropertyField.setShowTitle(false);
			chkUpdateDisupdatablePropertyField.setColSpan(2);
			chkUpdateDisupdatablePropertyField.addChangedHandler((e) -> {
				//更新不可項目も更新する場合は、Validationをfalseに設定、入力不可にする
				if (SmartGWTUtil.getBooleanValue(chkUpdateDisupdatablePropertyField)) {
					chkWithValidationField.setValue(false);
					chkWithValidationField.setDisabled(true);
				} else {
					if (SmartGWTUtil.getBooleanValue(chkBulkUpdateField)) {
						chkWithValidationField.setDisabled(true);
					} else {
						chkWithValidationField.setDisabled(false);
					}
				}
			});

			chkInsertEnableAuditPropertySpecification = new CheckboxItem();
			chkInsertEnableAuditPropertySpecification.setTitle(rs("ui_tools_entityexplorer_EntityCsvUploadDialog_insertEnableAuditPropertySpecification"));
			chkInsertEnableAuditPropertySpecification.setShowTitle(false);
			chkInsertEnableAuditPropertySpecification.setColSpan(2);

			chkForceUpdateField = new CheckboxItem();
			chkForceUpdateField.setTitle(rs("ui_tools_entityexplorer_EntityCsvUploadDialog_forceUpdate"));
			chkForceUpdateField.setShowTitle(false);
			chkForceUpdateField.setColSpan(2);

			chkErrorSkipField = new CheckboxItem();
			chkErrorSkipField.setTitle(rs("ui_tools_entityexplorer_EntityCsvUploadDialog_errorDataSkip"));
			chkErrorSkipField.setShowTitle(false);
			chkErrorSkipField.setColSpan(2);

			chkIgnoreNotExistsPropertyField = new CheckboxItem();
			chkIgnoreNotExistsPropertyField.setTitle(rs("ui_tools_entityexplorer_EntityCsvUploadDialog_ignoreNotExistsProperty"));
			chkIgnoreNotExistsPropertyField.setShowTitle(false);
			chkIgnoreNotExistsPropertyField.setColSpan(2);
			chkIgnoreNotExistsPropertyField.setValue(true);	//デフォルトtrue

			prefixOidField = new TextItem();
			prefixOidField.setTitle("OID Prefix");
			prefixOidField.setKeyPressFilter("[A-Za-z0-9]");	//英数字のみ
			prefixOidField.setHint(rs("ui_tools_entityexplorer_EntityCsvUploadDialog_preOidHint"));

			uniqueKeyField = new SelectItem();
			uniqueKeyField.setTitle("Unique Key");
			UniquePropertyDS.setDataSource(uniqueKeyField, definition);
			uniqueKeyField.setValue(Entity.OID);

			commitLimitField = new SelectItem();
			commitLimitField.setTitle(rs("ui_tools_entityexplorer_EntityCsvUploadDialog_commitUnit"));
			LinkedHashMap<String, String> commitValues = new LinkedHashMap<>();
			commitValues.put("1", rs("ui_tools_entityexplorer_EntityCsvUploadDialog_one"));
			commitValues.put("10", rs("ui_tools_entityexplorer_EntityCsvUploadDialog_ten"));
			commitValues.put("100", rs("ui_tools_entityexplorer_EntityCsvUploadDialog_hundred"));
			commitValues.put("1000", rs("ui_tools_entityexplorer_EntityCsvUploadDialog_thousand"));
			commitValues.put("-1", rs("ui_tools_entityexplorer_EntityCsvUploadDialog_all"));
			commitLimitField.setDefaultValue("100");
			commitLimitField.setValueMap(commitValues);

			VLayout hintLayout = new VLayout();
			CanvasItem hintItem = new CanvasItem();
			hintItem.setShowTitle(false);
			hintItem.setColSpan(2);
			hintItem.setCanvas(hintLayout);

			hintLayout.addMember(getLabel("ui_tools_entityexplorer_EntityCsvUploadDialog_truncateComment"));
			hintLayout.addMember(getLabel("ui_tools_entityexplorer_EntityCsvUploadDialog_bulkUpdateComment1"));
			hintLayout.addMember(getLabel("ui_tools_entityexplorer_EntityCsvUploadDialog_bulkUpdateComment2"));
			hintLayout.addMember(getLabel("ui_tools_entityexplorer_EntityCsvUploadDialog_listenerComment"));
			hintLayout.addMember(getLabel("ui_tools_entityexplorer_EntityCsvUploadDialog_updateDisupdatablePropertyComment1"));
			hintLayout.addMember(getLabel("ui_tools_entityexplorer_EntityCsvUploadDialog_insertEnableAuditPropertySpecificationComment1"));
			hintLayout.addMember(getLabel("ui_tools_entityexplorer_EntityCsvUploadDialog_preOidComment1"));
			hintLayout.addMember(getLabel("ui_tools_entityexplorer_EntityCsvUploadDialog_preOidComment2"));
			hintLayout.addMember(getLabel("ui_tools_entityexplorer_EntityCsvUploadDialog_useCtrlComment1"));
			hintLayout.addMember(getLabel("ui_tools_entityexplorer_EntityCsvUploadDialog_binaryComment"));

			entityForm.setItems(chkTruncateField, chkBulkUpdateField, chkNotifyListenersField,
					chkWithValidationField, chkUpdateDisupdatablePropertyField,
					chkInsertEnableAuditPropertySpecification,
					chkForceUpdateField, chkErrorSkipField, chkIgnoreNotExistsPropertyField,
					prefixOidField, uniqueKeyField, commitLimitField, hintItem);

			DynamicForm i18nForm = new DynamicForm();
			//i18nForm.setMargin(10);
			i18nForm.setWidth100();
			i18nForm.setPadding(5);
			i18nForm.setIsGroup(true);
			i18nForm.setGroupTitle("i18n Support Setting:");
			i18nForm.setNumCols(5);
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

			conditionLayout.addMember(entityForm);
			conditionLayout.addMember(i18nForm);

			//----------
			//Log
			//----------

			messageTabSet = new MessageTabSet();
			messageTabSet.setWidth("50%");
			messageTabSet.setHeight100();

			contentMainPane.addMember(conditionLayout);
			contentMainPane.addMember(messageTabSet);

			//----------
			//Footer
			//----------

			HLayout footer = new HLayout(5);
			footer.setMargin(5);
			footer.setHeight(20);
			footer.setWidth100();
			footer.setAlign(VerticalAlignment.CENTER);

			btnUpload = new IButton("Import");
			btnUpload.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if (uploader.getFileName() == null || uploader.getFileName().isEmpty()) {
						SC.warn(rs("ui_tools_entityexplorer_EntityCsvUploadDialog_selectImportFile"));
						return;
					}
					//if (User.DEFINITION_NAME.equals(EntityCsvUploadDialog.this.defName)
					if ("mtp.auth.User".equals(defName)
							&& SmartGWTUtil.getBooleanValue(chkTruncateField)) {
						//ユーザEntityを削除する場合は警告

						SC.ask(rs("ui_tools_entityexplorer_EntityCsvUploadDialog_confirm"), rs("ui_tools_entityexplorer_EntityCsvUploadDialog_userTruncateConfirm"), new BooleanCallback() {

							@Override
							public void execute(Boolean value) {
								if (value) {
									doCheckListener(uploader);
								}
							}
						});

					} else {
						doCheckListener(uploader);
					}
				}
			});

			btnCancel = new IButton("Cancel");
			btnCancel.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					EntityCsvUploadDialog.this.destroy();
				}
			});
			footer.setMembers(btnUpload, btnCancel);

			addMember(contentMainPane);
			addMember(SmartGWTUtil.separator());
			addMember(footer);
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
		
		private void doCheckListener(final AdminSingleUploader uploader) {

			//EntityにUserまたはPermission系が含まれていてListener実行しない場合は確認
				if (!SmartGWTUtil.getBooleanValue(chkNotifyListenersField)) {
					if ("mtp.auth.User".equals(defName)) {
						SC.ask(rs("ui_tools_entityexplorer_EntityCsvUploadDialog_confirm"), rs("ui_tools_entityexplorer_EntityCsvUploadDialog_userListenerConfirm"), new BooleanCallback() {
							@Override
							public void execute(Boolean value) {
								if (value) {
									doUpload(uploader);
								}
							}
						});
						return;
					} else if (Arrays.asList(
							"mtp.auth.ActionPermission", 
							"mtp.auth.CubePermission", 
							"mtp.auth.EntityPermission", 
							"mtp.auth.UserTaskPermission", 
							"mtp.auth.WebApiPermission", 
							"mtp.auth.WorkflowPermission").contains(defName)) {
						SC.ask(rs("ui_tools_entityexplorer_EntityCsvUploadDialog_confirm"), rs("ui_tools_entityexplorer_EntityCsvUploadDialog_permissionListenerConfirm"), new BooleanCallback() {
							@Override
							public void execute(Boolean value) {
								if (value) {
									doUpload(uploader);
								}
							}
						});
						return;
					}
				}
				
				doUpload(uploader);
		}

		private void doUpload(final AdminSingleUploader uploader) {
			SC.ask(rs("ui_tools_entityexplorer_EntityCsvUploadDialog_confirm"), rs("ui_tools_entityexplorer_EntityCsvUploadDialog_startImportConf"), new BooleanCallback() {
				@Override
				public void execute(Boolean value) {
					if (value) {
						uploader.add(new Hidden("defName", defName));
						uploader.add(new Hidden("tenantId", Integer.toString(TenantInfoHolder.getId())));
						if (SmartGWTUtil.getBooleanValue(chkTruncateField)) {
							uploader.add(new Hidden("chkTruncate", "1"));
						}
						if (SmartGWTUtil.getBooleanValue(chkBulkUpdateField)) {
							uploader.add(new Hidden("chkBulkUpdate", "1"));
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
						if (SmartGWTUtil.getBooleanValue(chkInsertEnableAuditPropertySpecification)) {
							uploader.add(new Hidden("chkInsertEnableAuditPropertySpecification", "1"));
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
			List<String> errors = new ArrayList<>(1);
			errors.add(rs("ui_tools_entityexplorer_EntityCsvUploadDialog_errorImport"));
			errors.add(message);
			messageTabSet.setErrorMessage(errors);

			disableComponent(false);
			messageTabSet.setTabTitleNormal();
		}

		private void disableComponent(boolean disabled) {
			btnUpload.setDisabled(disabled);
			btnCancel.setDisabled(disabled);
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

			List<String> logs = new ArrayList<>();

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
				return rs("ui_tools_entityexplorer_EntityCsvUploadDialog_importSuccessful");
			} else if ("WARN".equals(status)) {
				return rs("ui_tools_entityexplorer_EntityCsvUploadDialog_importWarning");
			} else if ("ERROR".equals(status)) {
				return rs("ui_tools_entityexplorer_EntityCsvUploadDialog_importErr");
			} else {
				return rs("ui_tools_entityexplorer_EntityCsvUploadDialog_couldNotRetImportResult");
			}
		}

		private boolean isStatusSuccess(String status) {
			return "SUCCESS".equals(status);
		}

		private List<String> getMessageInfo(JSONValue root) {
			List<String> messages = new ArrayList<>();
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
			String content = "<font color=\"red\">" + rs(key) + "</font>";
			com.smartgwt.client.widgets.Label label = new com.smartgwt.client.widgets.Label(content);
			label.setHeight(20);
			return label;
		}
	}

	private String rs(String key) {
		return AdminClientMessageUtil.getString(key);
	}

}
