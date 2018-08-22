/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.tools.ui.pack;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.ui.widget.AnimationFullScreenCallback;
import org.iplass.adminconsole.client.base.ui.widget.MessageTabSet;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.tools.ui.metaexplorer.MetaDataImportTenantPane;
import org.iplass.adminconsole.client.tools.ui.metaexplorer.MetaDataImportTenantPane.TenantSelectActionCallback;
import org.iplass.adminconsole.shared.metadata.dto.MetaDataConstants;
import org.iplass.adminconsole.shared.tools.dto.entityexplorer.EntityDataImportResultInfo;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.MetaDataImportResultInfo;
import org.iplass.adminconsole.shared.tools.dto.pack.PackageEntryInfo;
import org.iplass.adminconsole.shared.tools.dto.pack.PackageImportCondition;
import org.iplass.adminconsole.shared.tools.rpc.pack.PackageRpcServiceAsync;
import org.iplass.adminconsole.shared.tools.rpc.pack.PackageRpcServiceFactory;
import org.iplass.mtp.tenant.Tenant;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
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
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

/**
 * PackageをImportするダイアログ
 */
public class PackageImportDialog extends AbstractWindow {

	private static final String RESOURCE_PREFIX = "ui_tools_pack_PackageImportDialog_";

	private static final int MIN_WIDTH = 800;
	private static final int MIN_HEIGHT = 600;

	private String fileOid;
	@SuppressWarnings("unused")
	private PackageListPane owner;

	private PackageEntryInfo packageInfo;

	private VLayout mainLayout;

	//インポート設定画面
	private PackageImportPane packageImportPane;
	//テナントプロパティ選択画面
	private MetaDataImportTenantPane tanantSelectPane;


	public static void showFullScreen(final String fileOid, final PackageListPane owner) {
		SmartGWTUtil.showAnimationFullScreen(new AnimationFullScreenCallback() {
			@Override
			public void execute(boolean earlyFinish) {
              animateOutline.hide();
              PackageImportDialog dialog = new PackageImportDialog(fileOid, owner, width, height);
              dialog.show();
              dialog.initializeData();	//showの後で実行しないとProgressが表示されないので、ここで
			}
		});
	}

	/**
	 * コンストラクタ
	 */
	private PackageImportDialog(String fileOid, PackageListPane owner, int width, int height) {
		this.fileOid = fileOid;
		this.owner = owner;

		setWidth(width);
		setMinWidth(MIN_WIDTH);
		setHeight(height);
		setMinHeight(MIN_HEIGHT);
		setTitle("Import Package");
		setCanDragResize(true);
		setShowMinimizeButton(false);
		setShowMaximizeButton(true);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();

		packageImportPane = new PackageImportPane();

		mainLayout = new VLayout();
		mainLayout.addMember(packageImportPane);

		addItem(mainLayout);
	}

	private class PackageImportPane extends VLayout {

		private HTMLFlow description;
		private EntryTab metadataTab;
		private EntryTab entityTab;
		private EntityImportPane entityImportPane;
		private ImportResultDialog resultDialog;

		private IButton unPack;
		private IButton cancel;

		private PackageRpcServiceAsync service = PackageRpcServiceFactory.get();

		public PackageImportPane() {
			setWidth100();
			setHeight100();
			setMargin(10);

			//------------------------
			//Operation Pane
			//------------------------
			description = new HTMLFlow(getResourceString("importPackageDesc"));
			description.setAutoHeight();
			description.setWidth100();
			description.setPadding(5);

			HLayout contentMainPane = new HLayout();
			contentMainPane.setWidth100();
			contentMainPane.setHeight100();

			VLayout entryPane = new VLayout();
			entryPane.setWidth("50%");
			entryPane.setHeight100();
			entryPane.setMargin(5);
			entryPane.setShowResizeBar(true);

			metadataTab = new EntryTab("MetaData Entry");
			metadataTab.setWidth100();
			metadataTab.setHeight("50%");
			entryPane.addMember(metadataTab);

			entityTab = new EntryTab("Entity Data Entry");
			entityTab.setWidth100();
			entityTab.setHeight("50%");
			entryPane.addMember(entityTab);

			VLayout executePane = new VLayout();
			executePane.setWidth("50%");
			executePane.setHeight100();
			executePane.setMargin(5);
			executePane.setMembersMargin(5);

			entityImportPane = new EntityImportPane();
			entityImportPane.setWidth100();
			entityImportPane.setHeight100();
			executePane.addMember(entityImportPane);

			contentMainPane.addMember(entryPane);
			contentMainPane.addMember(executePane);

			//------------------------
			//Footer Layout
			//------------------------
			unPack = new IButton("Import");
			unPack.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					executeImport();
				}
			});
			cancel = new IButton("Cancel");
			cancel.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					PackageImportDialog.this.destroy();
				}
			});

			HLayout footer = new HLayout(5);
			footer.setMargin(5);
			footer.setHeight(20);
			footer.setWidth100();
			footer.setAlign(VerticalAlignment.CENTER);
			footer.setMembers(unPack, cancel);

			//------------------------
			//Main Layout
			//------------------------

			addMember(description);
			addMember(contentMainPane);

			addMember(SmartGWTUtil.separator());
			addMember(footer);
		}

		private void disableComponent(boolean disabled) {
			unPack.setDisabled(disabled);
			cancel.setDisabled(disabled);
		}

		public void getPackageInfo() {
			SmartGWTUtil.showProgress("read package info ....");

			PackageRpcServiceAsync service = PackageRpcServiceFactory.get();
			service.getPackageEntryInfo(TenantInfoHolder.getId(), fileOid, new AsyncCallback<PackageEntryInfo>() {

				@Override
				public void onSuccess(PackageEntryInfo result) {
					packageInfo = result;

					if (SmartGWTUtil.isNotEmpty(result.getMetaDataPaths())) {
						//メタデータ取り込みあり
						metadataTab.setMessage(result.getMetaDataPaths());
					} else {
						metadataTab.setMessage("No items to show.");
					}

					if (SmartGWTUtil.isNotEmpty(result.getEntityPaths())) {
						//Entityデータ取り込みあり
						entityTab.setMessage(result.getEntityPaths());
					} else {
						entityTab.setMessage("No items to show.");
					}

					SmartGWTUtil.hideProgress();
				}

				@Override
				public void onFailure(Throwable caught) {
					SmartGWTUtil.hideProgress();

					GWT.log(caught.toString(), caught);
					SC.warn(getResourceString("failedToGetPackage") + caught.getMessage());
				}
			});
		}


		private void executeImport() {

			//テナントが含まれている場合は、プロパティ選択を行う
			if (packageInfo.getTenant() != null) {
				SmartGWTUtil.showProgress("Load Import Tenant Info...");
				tanantSelectPane = new MetaDataImportTenantPane(packageInfo.getTenant(), new TenantSelectActionCallback() {

					@Override
					public void selected(Tenant importTenant) {
						//テナント選択画面非表示
						hideTanantSelectPane();

						//インポート開始
						doCheckUserEntityImport(importTenant);
					}

					@Override
					public void canceled() {
						//テナント選択画面非表示
						hideTanantSelectPane();
					}

					private void hideTanantSelectPane() {
						packageImportPane.show();

						if (tanantSelectPane != null) {
							tanantSelectPane.hide();
							tanantSelectPane.destroy();
							tanantSelectPane = null;
						}
					}
				});
				mainLayout.addMember(tanantSelectPane);

				//画面をテナント選択画面に切り替え
				tanantSelectPane.show();
				packageImportPane.hide();

			} else {
				doCheckUserEntityImport(null);
			}
		}

		private void doCheckUserEntityImport(final Tenant importTenant) {

			//EntityにUserが含まれていてTruncateする場合は確認
			if (SmartGWTUtil.isNotEmpty(packageInfo.getEntityPaths())) {
				if (packageInfo.getEntityPaths().contains("mtp.auth.User" + ".csv")) {
					PackageImportCondition cond = entityImportPane.getCondition();
					if (cond.isTruncate()) {
						SC.ask(getResourceString("confirm"), getResourceString("userTruncateConfirm"), new BooleanCallback() {
							@Override
							public void execute(Boolean value) {
								if (value) {
									doExecuteImport(importTenant);
								}
							}
						});
						return;
					}
				}
			}

			doExecuteImport(importTenant);
		}

		private void doExecuteImport(final Tenant importTenant) {
			SC.ask(getResourceString("confirm"), getResourceString("startImportConf"), new BooleanCallback() {

				@Override
				public void execute(Boolean value) {
					if (value) {
						disableComponent(true);

						//結果表示ダイアログを表示
						resultDialog = new ImportResultDialog();
						resultDialog.clearMessage();
						resultDialog.setTabTitleProgress();
						resultDialog.show();

						if (SmartGWTUtil.isNotEmpty(packageInfo.getMetaDataPaths())) {
							//メタデータ取り込み
							importMetaData(importTenant);
						} else {
							//Entityデータ取り込み
							importEntityData();
						}
					}

				}
			});
		}

		private void importMetaData(Tenant importTenant) {

			resultDialog.addMessage(getResourceString("startImportMetaData"));

			service.importPackageMetaData(TenantInfoHolder.getId(), fileOid, importTenant, new AsyncCallback<MetaDataImportResultInfo>() {

				@Override
				public void onSuccess(MetaDataImportResultInfo result) {
					if (result.isError()) {
						resultDialog.addErrorMessage(getResourceString("failedToImportMetaData"));
						resultDialog.addErrorMessage(result.getMessages());

						disableComponent(false);
						resultDialog.finish();
					} else {
						resultDialog.addMessage(result.getMessages());
						resultDialog.addMessage(getResourceString("importMetaDataComp"));
						resultDialog.addMessage("--------------------------------------");
						if (SmartGWTUtil.isNotEmpty(packageInfo.getEntityPaths())) {
							//Entityデータ取り込み
							importEntityData();
						} else {
							disableComponent(false);
							resultDialog.finish();
						}
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					GWT.log(caught.toString(), caught);
					resultDialog.addErrorMessage(getResourceString("failedToImportMetaDataCause") + caught.getMessage());

					disableComponent(false);
					resultDialog.finish();
				}
			});
		}

		private void importEntityData() {

			//Role、RoleConditionチェック
			List<String> execList = new ArrayList<String>(packageInfo.getEntityPaths().size());
			boolean hasRole = false;
			for (String path : packageInfo.getEntityPaths()) {
				if (path.equals(MetaDataConstants.ENTITY_NAME_ROLE + ".csv")) {
					hasRole = true;
					execList.add(0, path);
				} else if (path.equals(MetaDataConstants.ENTITY_NAME_ROLE_CONDITION + ".csv")) {
					if (hasRole) {
						execList.add(1, path);
					} else {
						execList.add(0, path);
					}
				} else {
					execList.add(path);
				}
			}

			//開始メッセージ
			resultDialog.addMessage(getResourceString("startImportAllEntityData"));

			PackageImportCondition cond = entityImportPane.getCondition();
			importEntityData(execList, 0, cond);
		}

		private void importEntityData(final List<String> pathList, final int index, final PackageImportCondition cond) {

			if (pathList.size() > index) {
				final String path = pathList.get(index);

				resultDialog.addMessage("[" + path + "]" + getResourceString("startImportEntityData"));

				service.importPackageEntityData(TenantInfoHolder.getId(), fileOid, path, cond, new AsyncCallback<EntityDataImportResultInfo>() {

					@Override
					public void onSuccess(EntityDataImportResultInfo result) {
						if (result.isError()) {
							resultDialog.addErrorMessage(result.getMessages());
							resultDialog.addErrorMessage("[" + path + "]" + getResourceString("failedImportEntityData"));

							if (cond.isErrorSkip()) {
								//エラースキップの場合は次へ
								importEntityData(pathList, index + 1, cond);
							} else {
								resultDialog.addErrorMessage(getResourceString("stopImportEntityData"));

								disableComponent(false);
								resultDialog.finish();
							}

						} else {
							resultDialog.addMessage(result.getMessages());
							resultDialog.addMessage("[" + path + "]" + getResourceString("completeImportEntityData"));

							//次のEntity処理
							importEntityData(pathList, index + 1, cond);
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						GWT.log(caught.toString(), caught);
						resultDialog.addErrorMessage("[" + path + "]" + getResourceString("failedImportEntityDataCause") + caught.getMessage());

						resultDialog.addErrorMessage(getResourceString("stopImportEntityData"));

						disableComponent(false);
						resultDialog.finish();
					}
				});

			} else {
				resultDialog.addMessage(getResourceString("completeImportAllEntityData"));
				disableComponent(false);
				resultDialog.finish();
			}

		}

	}

	void initializeData() {

		packageImportPane.getPackageInfo();
	}

	private class EntryTab extends TabSet {

		private Tab entryTab;
		private EntryPane entryPane;

		public EntryTab(String title) {
			setTabBarPosition(Side.TOP);
			setWidth100();
			setHeight100();

			entryTab = new Tab();
			entryTab.setTitle(title);

			entryPane = new EntryPane();
			entryTab.setPane(entryPane);
			addTab(entryTab);
		}

//		public void clearMessage() {
//			entryPane.clearMessage();
//		}

		public void setMessage(String message) {
			entryPane.setMessage(message);
		}
		public void setMessage(List<String> messages) {
			entryPane.setMessage(messages);
		}
//		public void addMessage(String message) {
//			entryPane.addMessage(message);
//		}
//		public void addMessage(List<String> messages) {
//			entryPane.addMessage(messages);
//		}
//		public void setErrorMessage(String message) {
//			entryPane.setErrorMessage(message);
//		}
//		public void setErrorMessage(List<String> messages) {
//			entryPane.setErrorMessage(messages);
//		}
//		public void addErrorMessage(String message) {
//			entryPane.addErrorMessage(message);
//		}
//		public void addErrorMessage(List<String> messages) {
//			entryPane.addErrorMessage(messages);
//		}
//		public void setWarnMessage(String message) {
//			entryPane.setWarnMessage(message);
//		}
//		public void setWarnMessage(List<String> messages) {
//			entryPane.setWarnMessage(messages);
//		}
//		public void addWarnMessage(String message) {
//			entryPane.addWarnMessage(message);
//		}
//		public void addWarnMessage(List<String> messages) {
//			entryPane.addWarnMessage(messages);
//		}

		private class EntryPane extends VLayout {

			Canvas logContents;

			public EntryPane() {
				setWidth100();
				setHeight100();

				logContents = new Canvas();
				logContents.setHeight100();
				logContents.setWidth100();
				logContents.setPadding(5);
				logContents.setOverflow(Overflow.AUTO);
				logContents.setCanSelectText(true);

				addMember(logContents);
			}

//			public void clearMessage() {
//				logContents.setContents("");
//			}

			public void setMessage(String message) {
				List<String> messages = new ArrayList<String>();
				messages.add(message);
				setMessage(messages);
			}
			public void setMessage(List<String> messages) {
				logContents.setContents(getMessageString(messages));
			}

//			public void addMessage(String message) {
//				List<String> messages = new ArrayList<String>();
//				messages.add(message);
//				addMessage(messages);
//			}
//			public void addMessage(List<String> messages) {
//				StringBuilder builder = new StringBuilder(logContents.getContents());
//				if (builder.length() > 0) {
//					builder.append("<br/>");
//				}
//				builder.append(getMessageString(messages));
//				logContents.setContents(builder.toString());
//			}

//			public void setErrorMessage(String message) {
//				List<String> messages = new ArrayList<String>();
//				messages.add(message);
//				setErrorMessage(messages);
//			}
//			public void setErrorMessage(List<String> messages) {
//				logContents.setContents("<font color=\"red\">" + getMessageString(messages) + "</font>");
//			}

//			public void addErrorMessage(String message) {
//				List<String> messages = new ArrayList<String>();
//				messages.add(message);
//				addErrorMessage(messages);
//			}
//			public void addErrorMessage(List<String> messages) {
//				StringBuilder builder = new StringBuilder(logContents.getContents());
//				if (builder.length() > 0) {
//					builder.append("<br/>");
//				}
//				builder.append("<font color=\"red\">" + getMessageString(messages) + "</font>");
//				logContents.setContents(builder.toString());
//			}

//			public void setWarnMessage(String message) {
//				List<String> messages = new ArrayList<String>();
//				messages.add(message);
//				setWarnMessage(messages);
//			}
//			public void setWarnMessage(List<String> messages) {
//				logContents.setContents("<font color=\"blue\">" + getMessageString(messages) + "</font>");
//			}

//			public void addWarnMessage(String message) {
//				List<String> messages = new ArrayList<String>();
//				messages.add(message);
//				addWarnMessage(messages);
//			}
//
//			public void addWarnMessage(List<String> messages) {
//				StringBuilder builder = new StringBuilder(logContents.getContents());
//				if (builder.length() > 0) {
//					builder.append("<br/>");
//				}
//				builder.append("<font color=\"blue\">" + getMessageString(messages) + "</font>");
//				logContents.setContents(builder.toString());
//			}

			private String getMessageString(List<String> messages) {
				StringBuilder builder = new StringBuilder();
				for (String message : messages) {
					builder.append(message + "<br/>");
				}
				if (builder.length() > 0) {
					builder.delete(builder.length() - 5, builder.length());
				}
				return builder.toString();

			}
		}

	}

	private class EntityImportPane extends VLayout {

		private CheckboxItem chkTruncateField;
		private CheckboxItem chkForceUpdateField;
		private CheckboxItem chkErrorSkipField;
		private CheckboxItem chkIgnoreNotExistsPropertyField;
		private CheckboxItem chkNotifyListenersField;
		private CheckboxItem chkWithValidationField;
		private CheckboxItem chkUpdateDisupdatablePropertyField;
		private SelectItem commitLimitField;
		private TextItem prefixOidField;

		private ComboBoxItem localeField;
		private ComboBoxItem timeZoneField;

//		private CheckboxItem chkAsyncField;

		public EntityImportPane() {
			setOverflow(Overflow.AUTO);	//Resize可能にするため

			DynamicForm entityForm = new DynamicForm();
			//entityForm.setMargin(10);
			entityForm.setPadding(5);
			entityForm.setWidth100();
			entityForm.setAlign(Alignment.CENTER);
			entityForm.setIsGroup(true);
			entityForm.setGroupTitle("Entity Data Import Setting:");
			entityForm.setNumCols(2);
			entityForm.setColWidths(90, "*");

			CanvasItem caption = new CanvasItem();
			caption.setCanvas(new Label(getResourceString("necessarySettEntityData")));
			caption.setShowTitle(false);
			caption.setColSpan(2);
			caption.setHeight(20);

			chkTruncateField = new CheckboxItem();
			chkTruncateField.setTitle(getResourceString("truncate"));
			chkTruncateField.setShowTitle(false);
			chkTruncateField.setColSpan(2);

			chkForceUpdateField = new CheckboxItem();
			chkForceUpdateField.setTitle(getResourceString("forceUpdate"));
			chkForceUpdateField.setShowTitle(false);
			chkForceUpdateField.setColSpan(2);

			chkErrorSkipField = new CheckboxItem();
			chkErrorSkipField.setTitle(getResourceString("errorDataSkip"));
			chkErrorSkipField.setShowTitle(false);
			chkErrorSkipField.setColSpan(2);

			chkIgnoreNotExistsPropertyField = new CheckboxItem();
			chkIgnoreNotExistsPropertyField.setTitle(getResourceString("ignoreNotExistsProperty"));
			chkIgnoreNotExistsPropertyField.setShowTitle(false);
			chkIgnoreNotExistsPropertyField.setColSpan(2);
			chkIgnoreNotExistsPropertyField.setValue(true);	//デフォルトtrue

			chkNotifyListenersField = new CheckboxItem();
			chkNotifyListenersField.setTitle(getResourceString("notifyListener"));
			chkNotifyListenersField.setShowTitle(false);
			chkNotifyListenersField.setColSpan(2);

			chkWithValidationField = new CheckboxItem();
			chkWithValidationField.setTitle(getResourceString("withValidation"));
			chkWithValidationField.setShowTitle(false);
			chkWithValidationField.setColSpan(2);

			chkUpdateDisupdatablePropertyField = new CheckboxItem();
			chkUpdateDisupdatablePropertyField.setTitle(getResourceString("updateDisupdatableProperty"));
			chkUpdateDisupdatablePropertyField.setShowTitle(false);
			chkUpdateDisupdatablePropertyField.setColSpan(2);
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
//			commitLimitField.setDisabled(true);	//暫定
//			commitLimitField.setHint("（Not Released）");

			prefixOidField = new TextItem();
			prefixOidField.setTitle("OID Prefix");
			prefixOidField.setKeyPressFilter("[A-Za-z0-9]");	//英数字のみ
			prefixOidField.setHint(getResourceString("preOidHint"));

			VLayout hintLayout = new VLayout();
			CanvasItem hintItem = new CanvasItem();
			hintItem.setShowTitle(false);
			hintItem.setColSpan(2);
			hintItem.setCanvas(hintLayout);

			hintLayout.addMember(getLabel("truncateComment"));
			hintLayout.addMember(getLabel("listenerComment"));
			hintLayout.addMember(getLabel("updateDisupdatablePropertyComment1"));
			hintLayout.addMember(getLabel("preOidComment1"));
			hintLayout.addMember(getLabel("preOidComment2"));
			hintLayout.addMember(getLabel("useCtrlComment1"));

			entityForm.setItems(caption, chkTruncateField,
					chkNotifyListenersField, chkWithValidationField, chkUpdateDisupdatablePropertyField,
					chkForceUpdateField, chkErrorSkipField, chkIgnoreNotExistsPropertyField,
					prefixOidField, commitLimitField, hintItem);

			DynamicForm i18nForm = new DynamicForm();
			//i18nForm.setMargin(10);
			i18nForm.setWidth100();
			i18nForm.setPadding(5);
			i18nForm.setIsGroup(true);
			i18nForm.setGroupTitle("i18n Support Setting:");
			i18nForm.setNumCols(4);
			i18nForm.setColWidths(90, 170, 120, "*");	//レイアウト調整したのでEntityCsvUploadDialogと少し異なる

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

			i18nForm.setItems(localeField, timeZoneField);

//			DynamicForm asyncForm = new DynamicForm();
//			//asyncForm.setMargin(10);
//			asyncForm.setPadding(5);
//			asyncForm.setWidth100();
//			asyncForm.setAlign(Alignment.CENTER);
//			asyncForm.setIsGroup(true);
//			asyncForm.setGroupTitle("UnPack Execute Setting:");
//			entityForm.setNumCols(2);
//			entityForm.setColWidths(90, "*");
//
//			chkAsyncField = new CheckboxItem();
//			chkAsyncField.setTitle(getResourceString("runAsynchronously"));
//			chkAsyncField.setShowTitle(false);
//			chkAsyncField.setColSpan(2);
//			chkAsyncField.setDisabled(true);	//暫定
//
//			asyncForm.setItems(chkAsyncField);

			VLayout targetLayout = new VLayout(5);
			targetLayout.setWidth100();
			targetLayout.setMargin(5);

			targetLayout.addMember(entityForm);
			targetLayout.addMember(i18nForm);
//			targetLayout.addMember(asyncForm);

			addMember(targetLayout);
		}

		public PackageImportCondition getCondition() {
			PackageImportCondition cond = new PackageImportCondition();
			cond.setTruncate(SmartGWTUtil.getBooleanValue(chkTruncateField));
			cond.setFourceUpdate(SmartGWTUtil.getBooleanValue(chkForceUpdateField));
			cond.setErrorSkip(SmartGWTUtil.getBooleanValue(chkErrorSkipField));
			cond.setIgnoreNotExistsProperty(SmartGWTUtil.getBooleanValue(chkIgnoreNotExistsPropertyField));
			cond.setNotifyListeners(SmartGWTUtil.getBooleanValue(chkNotifyListenersField));
			cond.setWithValidation(SmartGWTUtil.getBooleanValue(chkWithValidationField));
			cond.setUpdateDisupdatableProperty(SmartGWTUtil.getBooleanValue(chkUpdateDisupdatablePropertyField));
			cond.setCommitLimit(Integer.parseInt(SmartGWTUtil.getStringValue(commitLimitField)));
			cond.setPrefixOid(SmartGWTUtil.getStringValue(prefixOidField));

			cond.setLocale(SmartGWTUtil.getStringValue(localeField));
			cond.setTimezone(SmartGWTUtil.getStringValue(timeZoneField));
			return cond;
		}

		private com.smartgwt.client.widgets.Label getLabel(String key) {
			com.smartgwt.client.widgets.Label label = new com.smartgwt.client.widgets.Label(getResourceString(key));
			label.setHeight(20);
			return label;
		}
	}

	private class ImportResultDialog extends AbstractWindow {
		private MessageTabSet messageTab;
		private IButton okButton;
		private boolean finish = false;

		public ImportResultDialog() {
			setWidth(700);
			setMinWidth(500);
			setHeight(500);
			setMinHeight(400);
			setTitle("Import Package");
			setCanDragResize(true);
			setShowMinimizeButton(false);
			setShowMaximizeButton(true);
			setIsModal(true);
			setShowModalMask(true);
			centerInPage();

			//------------------------
			//MessagePane
			//------------------------
			messageTab = new MessageTabSet();

			//------------------------
			//Buttons
			//------------------------
			okButton = new IButton("OK");
			okButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					destroy();
				}
			});
			okButton.setDisabled(true);	//初期状態は使用不可

			HLayout execButtons = new HLayout(5);
			execButtons.setMargin(5);
			execButtons.setHeight(20);
			execButtons.setWidth100();
			execButtons.setAlign(VerticalAlignment.CENTER);
			execButtons.setMembers(okButton);

			//------------------------
			//Main Layout
			//------------------------
			VLayout mainLayout = new VLayout();
			mainLayout.setWidth100();
			mainLayout.setHeight100();
			mainLayout.setMargin(10);

			mainLayout.addMember(messageTab);
			mainLayout.addMember(execButtons);

			addItem(mainLayout);
		}

		@Override
		protected boolean onPreDestroy() {
			//処理が終わっている場合のみ許可
			return finish;
		}

		private void finish() {
			this.finish = true;
			messageTab.setTabTitleNormal();
			okButton.setDisabled(false);
		}

		private void clearMessage() {
			messageTab.clearMessage();
		}
		private void setTabTitleProgress() {
			messageTab.setTabTitleProgress();
		}
		private void addMessage(String message) {
			messageTab.addMessage(message);
		}
		private void addMessage(List<String> message) {
			messageTab.addMessage(message);
		}
		private void addErrorMessage(String message) {
			messageTab.addErrorMessage(message);
		}
		private void addErrorMessage(List<String> message) {
			messageTab.addErrorMessage(message);
		}
	}

	private String getResourceString(String key) {
		return AdminClientMessageUtil.getString(RESOURCE_PREFIX + key);
	}

}
