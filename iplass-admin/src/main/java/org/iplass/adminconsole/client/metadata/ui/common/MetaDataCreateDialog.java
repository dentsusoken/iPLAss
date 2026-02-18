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
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextAreaItem;
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
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.validator.RegExpValidator;
import com.smartgwt.client.widgets.form.validator.Validator;

/**
 * MetaDataの新規作成用標準ダイアログクラスです。
 */
public abstract class MetaDataCreateDialog extends MtpDialog {

	public static final int DEFAULT_HEIGHT = 250;

	protected MetaDataServiceAsync service = MetaDataServiceFactory.get();

	private TextItem sourceNameField;
	private TextItem nameField;
	private TextItem displayNameField;
	private TextAreaItem descriptionField;

	/** データ変更ハンドラ */
	private List<DataChangedHandler> handlers = new ArrayList<DataChangedHandler>();

	/** 対象Definitionクラス名 */
	private String definitionClassName;

	/** 対象Definition表示名 */
	private String nodeDisplayName;

	/** コピー対象定義名 */
	private String sourceName;

	/**
	 * <p>保存処理です。</p>
	 *
	 * <p>formのvalidateがtrueの場合に呼び出します。</p>
	 */
	protected abstract void saveAction(SaveInfo saveInfo, boolean isCopyMode);

	/**
	 * コンストラクタ
	 *
	 * @param definitionClassName 対象Definitionクラス名
	 * @param nodeDisplayName Node表示名
	 * @param folderPath デフォルトフォルダパス
	 */
	public MetaDataCreateDialog(String definitionClassName, String nodeDisplayName, String folderPath) {
		this(definitionClassName, nodeDisplayName, folderPath, false);
	}

	/**
	 * コンストラクタ
	 *
	 * @param definitionClassName 対象Definitionクラス名
	 * @param nodeDisplayName Node表示名
	 * @param folderPath デフォルトフォルダパス
	 * @param isCopyMode コピーモードか
	 */
	public MetaDataCreateDialog(String definitionClassName, final String nodeDisplayName, final String folderPath, final boolean isCopyMode) {
		this.definitionClassName = definitionClassName;
		this.nodeDisplayName = nodeDisplayName;

		if (isCopyMode) {
			setTitle(getString("copyTitle", nodeDisplayName));
			setHeight(DEFAULT_HEIGHT + 20);	//項目を追加したい場合は調整すること
		} else {
			setTitle(getString("createTitle", nodeDisplayName));
			setHeight(DEFAULT_HEIGHT);	//項目を追加したい場合は調整すること
		}

		centerInPage();

		sourceNameField = new MtpTextItem();
		sourceNameField.setTitle(AdminClientMessageUtil.getString("ui_metadata_common_MetaDataCreateDialog_sourceName"));
		SmartGWTUtil.setReadOnly(sourceNameField);
		if (!isCopyMode) {
			sourceNameField.hide();
		}

		nameField = new MtpTextItem();
		nameField.setTitle(AdminClientMessageUtil.getString("ui_metadata_common_MetaDataCreateDialog_name"));
		SmartGWTUtil.setRequired(nameField);
		nameField.setValue(folderPath);

		setNamePolicy(true, false);

		displayNameField = new MtpTextItem();
		displayNameField.setTitle(AdminClientMessageUtil.getString("ui_metadata_common_MetaDataCreateDialog_displayName"));

		descriptionField = new MtpTextAreaItem();
		descriptionField.setTitle(AdminClientMessageUtil.getString("ui_metadata_common_MetaDataCreateDialog_description"));
		descriptionField.setColSpan(2);
		descriptionField.setHeight(60);

		final DynamicForm form = new MtpForm();
		form.setAutoFocus(true);
		form.setItems(sourceNameField, nameField, displayNameField, descriptionField);

		container.addMember(form);

		IButton save = new IButton("Save");
		save.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (form.validate()){
					SaveInfo info = new SaveInfo(
							SmartGWTUtil.getStringValue(nameField),
							SmartGWTUtil.getStringValue(displayNameField),
							SmartGWTUtil.getStringValue(descriptionField));
					saveAction(info, isCopyMode);
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
	 * 名前に対する規則を設定します。
	 *
	 * @param pathSlash パスをスラッシュで表現する
	 * @param nameAcceptPeriod 名前にピリオドの利用を許可
	 */
	public void setNamePolicy(boolean pathSlash, boolean nameAcceptPeriod) {

		//名前に対するValidatorの設定
		RegExpValidator nameRegExpValidator = new RegExpValidator();
		String nameExpression = null;
		String nameErrorMessage = null;
		if (pathSlash) {
			if (nameAcceptPeriod) {
				nameExpression = MetaDataConstants.NAME_REG_EXP_PATH_SLASH_NAME_PERIOD;
				nameErrorMessage = getString("namePathSlashAcceptPeriodErr");
			} else {
				nameExpression = MetaDataConstants.NAME_REG_EXP_PATH_SLASH;
				nameErrorMessage = getString("namePathSlashErr");
			}
		} else {
			nameExpression = MetaDataConstants.NAME_REG_EXP_PATH_PERIOD;
			nameErrorMessage = getString("namePathPeriodErr");
		}
		nameRegExpValidator.setExpression(nameExpression);
		nameRegExpValidator.setErrorMessage(nameErrorMessage);

		nameField.setValidators(nameRegExpValidator);
	}

	/**
	 * 名前に対する規則を直接カスタマイズします。
	 *
	 * 例えばEntityの名前など、標準の規則以外を指定する場合に利用します。
	 *
	 * @param validators カスタマイズしたValidator
	 */
	public void setCustomNameValidator(Validator... validators) {
		nameField.setValidators(validators);
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
	 * コピー元のDefinition名を設定します。
	 *
	 * <p>指定されたコピー情報を検索して、デフォルト値を設定します。</p>
	 *
	 * @param sourceName コピー対象Definition名
	 */
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
		sourceNameField.setValue(sourceName);
		SmartGWTUtil.showProgress("Loading ....");
		service.getDefinitionInfo(TenantInfoHolder.getId(), definitionClassName, sourceName, new AsyncCallback<DefinitionInfo>() {

			@Override
			public void onSuccess(DefinitionInfo result) {
				if (result == null) {
					SC.warn(getString("copyTargetNotFoundErr"));
					destroy();
					return;
				}
				nameField.setValue(result.getName() + "_Copy");
				displayNameField.setValue(result.getDisplayName());
				descriptionField.setValue(result.getDescription());
				SmartGWTUtil.hideProgress();
			}

			@Override
			public void onFailure(Throwable caught) {
				SmartGWTUtil.hideProgress();
				SC.warn(getString("copyTargetErr", caught.getMessage()));
			}
		});
	}

	/**
	 * コピー元のDefinition名を返します。
	 *
	 * @return コピー対象Definition名
	 */
	public String getSourceName() {
		return sourceName;
	}

	protected String getDefinitionClassName() {
		return definitionClassName;
	}

	/**
	 * 対象データが既に登録されているかを確認します。
	 * 登録済の場合、エラーメッセージを表示します。
	 *
	 * @param name 対象Definition名
	 * @param callable 登録処理
	 */
	public void checkExist(final String name, final Callable<Void> callable) {
		SmartGWTUtil.showProgress("Check ....");
		service.getDefinitionInfo(TenantInfoHolder.getId(), definitionClassName, name, new AsyncCallback<DefinitionInfo>() {

			@Override
			public void onSuccess(DefinitionInfo result) {
				SmartGWTUtil.hideProgress();
				if (result != null) {
					SC.warn(getString("alreadyExistsErr", nodeDisplayName));
				} else {
					SmartGWTUtil.showSaveProgress();
					callable.call();
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				SmartGWTUtil.hideProgress();
			}
		});
	}

	/**
	 * 各メタデータでカスタマイズした部品を追加します。
	 *
	 * 例えばEntityのコピー時など、標準の項目以外を画面に表示したい場合に利用します。
	 *
	 * @param customParts カスタマイズ部品
	 */
	protected void addCustomParts(Canvas customParts) {
		container.addMember(customParts);
	}

	/**
	 * カスタムParts生成時などに利用する目的で、標準のDynamicFormを生成します。
	 *
	 * @return 標準のDynamicForm
	 */
	@Deprecated
	protected DynamicForm createDefaultForm() {
		return new MtpForm();
	}

	/**
	 * データ変更通知処理
	 */
	protected void fireDataChanged() {
		DataChangedEvent event = new DataChangedEvent();
		event.setValueName(SmartGWTUtil.getStringValue(nameField));

		for (DataChangedHandler handler : handlers) {
			handler.onDataChanged(event);
		}
	}

	/**
	 * 入力情報
	 */
	public static class SaveInfo {

		private String name;
		private String displayName;
		private String description;

		public SaveInfo(String name, String displayName, String description) {
			this.setName(name);
			this.setDisplayName(displayName);
			this.setDescription(description);
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDisplayName() {
			return displayName;
		}

		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}
	}

	/**
	 * 標準の保存完了処理
	 */
	public class SaveResultCallback implements AsyncCallback<AdminDefinitionModifyResult> {

		@Override
		public void onSuccess(AdminDefinitionModifyResult result) {
			SmartGWTUtil.hideProgress();
			if (result.isSuccess()) {
				//メッセージ表示前にデータ変更通知をしてしまう(ツリーをリロード)
				fireDataChanged();

				SC.say(getString("completeTitle"), getString("createComplete", nodeDisplayName), new BooleanCallback() {

					@Override
					public void execute(Boolean value) {

						//ダイアログ消去
						destroy();
					}
				});
			} else {
				SC.warn(getString("createErr", nodeDisplayName, result.getMessage()));
			}
		}

		@Override
		public void onFailure(Throwable caught) {
			SmartGWTUtil.hideProgress();
			GWT.log("fail to create " + nodeDisplayName, caught);
			SC.warn(getString("createErr", nodeDisplayName, caught.getMessage()));
		}
	}

	private String getString(String key, Object... arguments) {
		return AdminClientMessageUtil.getString("ui_metadata_common_MetaDataCreateDialog_" + key, arguments);
	}
}
