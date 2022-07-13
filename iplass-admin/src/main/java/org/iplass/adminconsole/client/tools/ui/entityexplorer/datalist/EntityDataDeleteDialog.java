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

import java.util.LinkedHashMap;
import java.util.List;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.ui.widget.MessageTabSet;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.tools.dto.entityexplorer.EntityDataCountResultInfo;
import org.iplass.adminconsole.shared.tools.rpc.entityexplorer.EntityExplorerServiceAsync;
import org.iplass.adminconsole.shared.tools.rpc.entityexplorer.EntityExplorerServiceFactory;
import org.iplass.mtp.impl.tools.entity.EntityDataDeleteResultInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class EntityDataDeleteDialog extends AbstractWindow {

	private static final String USER_ENTITY = "mtp.auth.User";

	private static final String OPERATION_VALUE_ALL_DELETE = "01";
	private static final String OPERATION_VALUE_OID_DELETE = "02";

	private String defName;
	private List<String> oids;

	private RadioGroupItem operationField;
	private TextAreaItem whereField;
	private CheckboxItem chkNotifyListenersField;
	private SelectItem commitLimitField;

	private IButton delete;
	private IButton cancel;

	private MessageTabSet messageTabSet;

	public EntityDataDeleteDialog(final String defName, final List<String> oids, final String whereClause) {
		this.defName = defName;
		this.oids = oids;

		setWidth(700);
		setMinWidth(500);
		setHeight(600);
		setMinHeight(600);
		setTitle("Delete Entity Data : " + defName);
		setCanDragResize(true);
		setShowMinimizeButton(false);
		setShowMaximizeButton(true);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();

//		Label comment = new Label("<b><font color=\"red\">" + oids.size() + "</font></b> records selected.");
//		comment.setMargin(10);
//		comment.setHeight(20);

		//------------------------
		//Input Items
		//------------------------
		operationField = new RadioGroupItem();
		operationField.setTitle("Delete Operation");

		LinkedHashMap<String, String> opeValues = new LinkedHashMap<String, String>();
		opeValues.put(OPERATION_VALUE_ALL_DELETE, "delete all records by delete condition.");
		if (oids != null && oids.size() > 0) {
			opeValues.put(OPERATION_VALUE_OID_DELETE, "delete selected records. " + "<b><font color=\"red\">" + oids.size() + "</font></b> records selected.");
			operationField.setValue(OPERATION_VALUE_OID_DELETE);
		} else {
			operationField.setValue(OPERATION_VALUE_ALL_DELETE);
		}
		operationField.setValueMap(opeValues);


		whereField = new TextAreaItem();
		whereField.setTitle("Delete Condition");
		whereField.setWidth("100%");
//		whereField.setHeight("100%");
		whereField.setHeight(150);
		whereField.setSelectOnFocus(true);
		whereField.setValue(whereClause);

		chkNotifyListenersField = new CheckboxItem();
		chkNotifyListenersField.setTitle("execute EventListneres");
		chkNotifyListenersField.setValue(true);
		SmartGWTUtil.addHoverToFormItem(chkNotifyListenersField, AdminClientMessageUtil.getString("ui_tools_entityexplorer_EntityDataDeleteDialog_runEventListenerDelet"));

		commitLimitField = new SelectItem();
		commitLimitField.setTitle("Commit Count");
		LinkedHashMap<String, String> commitValues = new LinkedHashMap<String, String>();
		commitValues.put("1", "1");
		commitValues.put("10", "10");
		commitValues.put("100", "100");
		commitValues.put("1000", "1000");
		commitValues.put("-1", "All");
		commitLimitField.setDefaultValue("100");
		commitLimitField.setValueMap(commitValues);
		SmartGWTUtil.addHoverToFormItem(commitLimitField, AdminClientMessageUtil.getString("ui_tools_entityexplorer_EntityDataDeleteDialog_speCommitUnit"));

		final DynamicForm form = new DynamicForm();
		form.setMargin(5);
		form.setHeight100();
		form.setWidth100();
		form.setItems(operationField, whereField, chkNotifyListenersField, commitLimitField);

		//------------------------
		//Buttons
		//------------------------
		delete = new IButton("Delete");
		delete.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
//				if (oids.size() == 0 && !SmartGWTUtil.getBooleanValue(chkAllDeleteField)) {
//					SC.warn("データが選択されていません。<br/>データを選択するか全件削除を選択してください。");
//					return;
//				}
				SC.ask(AdminClientMessageUtil.getString("ui_tools_entityexplorer_EntityDataDeleteDialog_confirm"),
						AdminClientMessageUtil.getString("ui_tools_entityexplorer_EntityDataDeleteDialog_startDeleteConfirm"), new BooleanCallback() {

					@Override
					public void execute(Boolean value) {
						if (value) {
							deleteData();
						}
					}
				});
			}
		});

		cancel = new IButton("Cancel");
		cancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				destroy();
			}
		});

		HLayout footer = new HLayout(5);
		footer.setMargin(5);
		footer.setHeight(20);
		footer.setWidth100();
		//footer.setAlign(Alignment.LEFT);
		footer.setAlign(VerticalAlignment.CENTER);
		footer.setMembers(delete, cancel);

		//------------------------
		//Input Layout
		//------------------------
		VLayout inputLayout = new VLayout(5);
		inputLayout.setMargin(5);
		inputLayout.setHeight(100);

//		inputLayout.addMember(comment);
		inputLayout.addMember(form);
		inputLayout.addMember(footer);

		//------------------------
		//MessagePane
		//------------------------
		messageTabSet = new MessageTabSet();

		//------------------------
		//Main Layout
		//------------------------
		VLayout mainLayout = new VLayout();
		mainLayout.setWidth100();
		mainLayout.setHeight100();
		mainLayout.setMargin(10);

		mainLayout.addMember(inputLayout);
		mainLayout.addMember(messageTabSet);

		addItem(mainLayout);
	}

	private void deleteData() {
		disableComponent(true);
		messageTabSet.clearMessage();
		messageTabSet.setTabTitleProgress();

		boolean isNotifyListeners = SmartGWTUtil.getBooleanValue(chkNotifyListenersField);
		if (USER_ENTITY.equals(defName) && !isNotifyListeners) {
			//削除の確認
			SC.ask(AdminClientMessageUtil.getString("ui_tools_entityexplorer_EntityDataDeleteDialog_confirm"),
					AdminClientMessageUtil.getString("ui_tools_entityexplorer_EntityDataDeleteDialog_userDeleteNotExecListenerConfirm")
					, new BooleanCallback() {

				@Override
				public void execute(Boolean value) {
					if (value) {
						executeDelete();
					} else {
						disableComponent(false);
						messageTabSet.setTabTitleNormal();
					}
				}
			});
		} else {
			executeDelete();
		}

	}

	private void executeDelete() {

		EntityExplorerServiceAsync service =  EntityExplorerServiceFactory.get();
		if (OPERATION_VALUE_ALL_DELETE.equals(SmartGWTUtil.getStringValue(operationField))) {
			//条件指定削除

			//条件に該当するデータ件数のチェック
			checkDataCount();
		} else {
			//OID指定削除

			boolean isNotifyListeners = SmartGWTUtil.getBooleanValue(chkNotifyListenersField);

			int commitLimit = -1;
			if (SmartGWTUtil.getStringValue(commitLimitField) != null) {
				commitLimit = Integer.parseInt(SmartGWTUtil.getStringValue(commitLimitField));
			}

			//oid指定削除
			service.deleteAllByOid(TenantInfoHolder.getId(), defName, oids, isNotifyListeners, commitLimit, new AsyncCallback<EntityDataDeleteResultInfo>() {
				@Override
				public void onSuccess(EntityDataDeleteResultInfo result) {
					deleteComplete(result);
				}

				@Override
				public void onFailure(Throwable caught) {
					deleteError(caught);
				}
			});
		}
	}

	private void checkDataCount() {
		EntityExplorerServiceAsync service =  EntityExplorerServiceFactory.get();
		final String whereClause = SmartGWTUtil.getStringValue(whereField);
		service.getConditionDataCount(TenantInfoHolder.getId(), defName, whereClause, new AsyncCallback<EntityDataCountResultInfo>() {

			@Override
			public void onSuccess(EntityDataCountResultInfo result) {
				checkDataCountComplete(result, whereClause);
			}

			@Override
			public void onFailure(Throwable caught) {
				deleteError(caught);
			}
		});
	}

	private void checkDataCountComplete(EntityDataCountResultInfo result, final String whereClause) {
		messageTabSet.clearMessage();

		if (result.isError()) {
			messageTabSet.addErrorMessage("Check DataCount failed.");
		} else {
			messageTabSet.addMessage("Check DataCount completed.");
		}
		messageTabSet.addMessage("------------------------------------");

		for (String message : result.getMessages()) {
			if (result.isError()) {
				messageTabSet.addErrorMessage(message);
			} else {
				messageTabSet.addMessage(message);
			}
		}

		if (!result.isError()) {

			//削除の確認
			SC.ask(AdminClientMessageUtil.getString("ui_tools_entityexplorer_EntityDataDeleteDialog_confirm"),
					AdminClientMessageUtil.getString("ui_tools_entityexplorer_EntityDataDeleteDialog_targetDataCountConf", Integer.toString(result.getTargetCount()), Integer.toString(result.getAllCount()))
					, new BooleanCallback() {

				@Override
				public void execute(Boolean value) {
					if (value) {
						executeAllDelete(whereClause);
					} else {
						disableComponent(false);
						messageTabSet.setTabTitleNormal();
					}
				}
			});
		} else {
			disableComponent(false);
			messageTabSet.setTabTitleNormal();
		}
	}

	private void executeAllDelete(String whereClause) {
		EntityExplorerServiceAsync service =  EntityExplorerServiceFactory.get();
		boolean isNotifyListeners = SmartGWTUtil.getBooleanValue(chkNotifyListenersField);
		int commitLimit = -1;
		if (SmartGWTUtil.getStringValue(commitLimitField) != null) {
			commitLimit = Integer.parseInt(SmartGWTUtil.getStringValue(commitLimitField));
		}

		service.deleteAll(TenantInfoHolder.getId(), defName, whereClause, isNotifyListeners, commitLimit, new AsyncCallback<EntityDataDeleteResultInfo>() {

			@Override
			public void onSuccess(EntityDataDeleteResultInfo result) {
				deleteComplete(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				deleteError(caught);
			}
		});
	}

	private void deleteComplete(EntityDataDeleteResultInfo result) {
		messageTabSet.clearMessage();

		if (result.isError()) {
			messageTabSet.addErrorMessage("Delete failed.");
		} else {
			messageTabSet.addMessage("Delete completed.");
		}
		messageTabSet.addMessage("------------------------------------");

		for (String message : result.getMessages()) {
			if (result.isError()) {
				messageTabSet.addErrorMessage(message);
			} else {
				messageTabSet.addMessage(message);
			}
		}

		disableComponent(false);
//		delete.setDisabled(true);	//削除ボタンは使用不可
		messageTabSet.setTabTitleNormal();
	}

	private void deleteError(Throwable caught) {
		messageTabSet.clearMessage();
		messageTabSet.addErrorMessage("Delete failed.");
		messageTabSet.addMessage("------------------------------------");

		if (caught.getMessage() != null) {
			messageTabSet.addErrorMessage(caught.getMessage());
		} else {
			messageTabSet.addErrorMessage(caught.getClass().getName());
		}

		disableComponent(false);
		messageTabSet.setTabTitleNormal();
	}

	private void disableComponent(boolean disabled) {
		delete.setDisabled(disabled);
		cancel.setDisabled(disabled);
	}
}
