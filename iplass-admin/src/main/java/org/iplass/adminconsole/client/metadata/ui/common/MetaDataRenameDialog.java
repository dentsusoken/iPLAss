/*
 * Copyright (C) 2015 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.common;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.metadata.dto.AdminDefinitionModifyResult;
import org.iplass.adminconsole.shared.metadata.dto.MetaDataConstants;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.definition.DefinitionInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.validator.RegExpValidator;
import com.smartgwt.client.widgets.form.validator.Validator;

/**
 * MetaDataの名前変更用標準ダイアログクラスです。
 */
public class MetaDataRenameDialog extends MtpDialog {

	public static final int DEFAULT_HEIGHT = 160;

	protected MetaDataServiceAsync service = MetaDataServiceFactory.get();

	private TextItem fromField;
	private TextItem toField;

	/** データ変更ハンドラ */
	private List<DataChangedHandler> handlers = new ArrayList<DataChangedHandler>();

	/** 対象Definitionクラス名 */
	private String definitionClassName;

	/** 対象Definition表示名 */
	private String nodeDisplayName;


	/**
	 * コンストラクタ
	 *
	 * @param definitionClassName 対象Definitionクラス名
	 * @param nodeDisplayName Node表示名
	 * @param fromName 変更前Definition名
	 * @param pathSlash パスをスラッシュで表現する
	 * @param nameAcceptPeriod 名前にピリオドの利用を許可
	 */
	public MetaDataRenameDialog(String definitionClassName, String nodeDisplayName, String fromName, boolean pathSlash, boolean nameAcceptPeriod) {
		this.definitionClassName = definitionClassName;
		this.nodeDisplayName = nodeDisplayName;

		setTitle(rs("ui_metadata_common_MetaDataRenameDialog_renameTitle", nodeDisplayName));
		setHeight(DEFAULT_HEIGHT);
		centerInPage();

		fromField = new MtpTextItem();
		fromField.setTitle(AdminClientMessageUtil.getString("ui_metadata_common_MetaDataRenameDialog_fromName"));
		fromField.setValue(fromName);
		SmartGWTUtil.setReadOnly(fromField);

		toField = new MtpTextItem();
		toField.setTitle(AdminClientMessageUtil.getString("ui_metadata_common_MetaDataRenameDialog_toName"));
		SmartGWTUtil.setRequired(toField);
		toField.setValue(fromName);

		setNamePolicy(pathSlash, nameAcceptPeriod);

		final DynamicForm form = new MtpForm();
		form.setAutoFocus(true);
		form.setItems(fromField, toField);

		container.setMembers(form);

		IButton save = new IButton("Save");
		save.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (form.validate()){
					saveAction();
				}
			}
		});

		IButton cancel = new IButton("Cancel");
		cancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				destroy();
			}
		});

		footer.setMembers(save, cancel);
	}

	/**
	 * {@link DataChangedHandler} を追加します。
	 *
	 * @param handler {@link DataChangedHandler}
	 */
	public void addDataChangeHandler(DataChangedHandler handler) {
		handlers.add(0, handler);
	}

	/**
	 * <p>実際の名前の変更処理</p>
	 *
	 * <p>特殊な処理を必要とする場合はdoRenameをOverrideしてください。</p>
	 *
	 * @param fromName 変更前Definition名
	 * @param toName 変更後Definition名
	 */
	protected void doRename(String fromName, String toName) {

		service.renameDefinition(TenantInfoHolder.getId(), definitionClassName, fromName, toName, new RenameResultCallback());
	}

	/**
	 * 名前に対する規則を設定します。
	 *
	 * @param pathSlash パスをスラッシュで表現する
	 * @param nameAcceptPeriod 名前にピリオドの利用を許可
	 */
	private void setNamePolicy(boolean pathSlash, boolean nameAcceptPeriod) {

		//名前に対するValidatorの設定
		RegExpValidator nameRegExpValidator = new RegExpValidator();
		String nameExpression = null;
		String nameErrorMessage = null;
		if (pathSlash) {
			if (nameAcceptPeriod) {
				nameExpression = MetaDataConstants.NAME_REG_EXP_PATH_SLASH_NAME_PERIOD;
				nameErrorMessage = rs("ui_metadata_common_MetaDataRenameDialog_namePathSlashAcceptPeriodErr");
			} else {
				nameExpression = MetaDataConstants.NAME_REG_EXP_PATH_SLASH;
				nameErrorMessage = rs("ui_metadata_common_MetaDataRenameDialog_namePathSlashErr");
			}
		} else {
			nameExpression = MetaDataConstants.NAME_REG_EXP_PATH_PERIOD;
			nameErrorMessage = rs("ui_metadata_common_MetaDataRenameDialog_namePathPeriodErr");
		}
		nameRegExpValidator.setExpression(nameExpression);
		nameRegExpValidator.setErrorMessage(nameErrorMessage);

		toField.setValidators(nameRegExpValidator);
	}
	
	/**
	 * 名前に対する規則を直接カスタマイズします。
	 *
	 * 例えばEntityの名前など、標準の規則以外を指定する場合に利用します。
	 *
	 * @param validators カスタマイズしたValidator
	 */
	public void setCustomNameValidator(Validator... validators) {
		toField.setValidators(validators);
	}
	
	/**
	 * <p>保存処理です。</p>
	 *
	 * <p>formのvalidateがtrueの場合に呼び出します。</p>
	 *
	 * <p>Entityの場合など、特殊な処理を必要とする場合はdoRenameをOverrideしてください。</p>
	 */
	private void saveAction(){

		String fromName = SmartGWTUtil.getStringValue(fromField);
		String toName = SmartGWTUtil.getStringValue(toField);

		//変更チェック
		if (fromName.equals(toName)) {
			SC.warn(rs("ui_metadata_common_MetaDataRenameDialog_sameNameErr"));
			return;
		}

		//存在チェック
		checkExist(fromName, toName);
	}

	/**
	 * 対象データが既に登録されているかを確認します。
	 * 登録済の場合、エラーメッセージを表示します。
	 *
	 * @param name 対象Definition名
	 * @param callable 登録処理
	 */
	private void checkExist(final String fromName, final String toName) {
		SmartGWTUtil.showProgress("Check ....");
		service.getDefinitionInfo(TenantInfoHolder.getId(), definitionClassName, toName, new AsyncCallback<DefinitionInfo>() {

			@Override
			public void onSuccess(DefinitionInfo result) {
				SmartGWTUtil.hideProgress();
				if (result != null) {
					SC.warn(rs("ui_metadata_common_MetaDataRenameDialog_alreadyExistsErr", nodeDisplayName));
				} else {
					//変更処理
					SmartGWTUtil.showSaveProgress();
					doRename(fromName, toName);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				SmartGWTUtil.hideProgress();
			}
		});
	}

	/**
	 * データ変更通知処理
	 */
	private void fireDataChanged() {
		DataChangedEvent event = new DataChangedEvent();
		event.setValueName(SmartGWTUtil.getStringValue(toField));
		for (DataChangedHandler handler : handlers) {
			handler.onDataChanged(event);
		}
	}

	/**
	 * 標準の名前変更完了処理
	 */
	public class RenameResultCallback implements AsyncCallback<AdminDefinitionModifyResult> {

		@Override
		public void onSuccess(AdminDefinitionModifyResult result) {
			SmartGWTUtil.hideProgress();
			if (result.isSuccess()) {
				//メッセージ表示前にデータ変更通知をしてしまう(ツリーをリロード)
				fireDataChanged();

				SC.say(rs("ui_metadata_common_MetaDataRenameDialog_completeTitle")
						, rs("ui_metadata_common_MetaDataRenameDialog_renameComplete"), new BooleanCallback() {

					@Override
					public void execute(Boolean value) {

						//ダイアログ消去
						destroy();
					}
				});
			} else {
				SC.warn(rs("ui_metadata_common_MetaDataRenameDialog_renameErr", result.getMessage()));
			}
		}

		@Override
		public void onFailure(Throwable caught) {
			SmartGWTUtil.hideProgress();
			GWT.log("fail to rename " + nodeDisplayName, caught);
			SC.warn(rs("ui_metadata_common_MetaDataRenameDialog_renameErr", caught.getMessage()));
		}
	}

	private String rs(String key, Object... arguments) {
		return AdminClientMessageUtil.getString(key, arguments);
	}

}
