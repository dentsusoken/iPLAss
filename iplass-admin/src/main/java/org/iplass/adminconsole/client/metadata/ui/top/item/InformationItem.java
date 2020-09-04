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

package org.iplass.adminconsole.client.metadata.ui.top.item;

import java.util.LinkedHashMap;

import org.iplass.adminconsole.client.base.event.MTPEvent;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataLangTextAreaItem;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataLangTextItem;
import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogConstants;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogHandler;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogMode;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpComboBoxItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpSelectItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextAreaItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.MetaDataUtil;
import org.iplass.adminconsole.client.metadata.ui.top.PartsOperationHandler;
import org.iplass.mtp.view.generic.editor.DateTimePropertyEditor.TimeDispRange;
import org.iplass.mtp.view.top.parts.InformationParts;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.HeaderControls;
import com.smartgwt.client.widgets.HeaderControl;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.IntegerItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.SliderItem;
import com.smartgwt.client.widgets.form.fields.TextItem;

/**
 *
 * @author lis3wg
 */
public class InformationItem extends PartsItem {
	private PartsOperationHandler controler;
	private InformationParts parts;

	/**
	 * コンストラクタ
	 */
	public InformationItem(InformationParts parts, 	PartsOperationHandler controler) {
		this.parts = parts;
		this.controler = controler;

		setTitle("Information List");
		setBackgroundColor("#BBBBFF");

		setHeaderControls(HeaderControls.HEADER_LABEL, new HeaderControl(HeaderControl.SETTINGS, new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				InformationItemSettingDialog dialog = new InformationItemSettingDialog();
				dialog.show();
			}

		}), HeaderControls.CLOSE_BUTTON);
	}

	@Override
	public InformationParts getParts() {
		return parts;
	}

	@Override
	protected boolean onPreDestroy() {
		MTPEvent e = new MTPEvent();
		e.setValue("key", dropAreaType + "_" + InformationParts.class.getName() + "_");
		controler.remove(e);
		return true;
	}

	private class InformationItemSettingDialog extends MtpDialog {

		private MetaDataLangTextItem titleField;
		private TextItem iconTagField;

		private SelectItem dispRangeField;
		private IntegerItem numberOfDisplayField;
		private CheckboxItem chkEnableHtmlTag;
		private CheckboxItem chkUseRichtextEditor;
		private MtpTextAreaItem txaRichtextEditorOption;
		private MtpTextAreaItem txaDetailCustomStyle;

		private CheckboxItem showPasswordWarnField;
		private SliderItem passwordWarningAgeField;
		private MetaDataLangTextAreaItem passwordWarningMessageField;
		private ComboBoxItem passwordWarnAreaStyleField;
		private ComboBoxItem passwordWarnMarkStyleField;

		public InformationItemSettingDialog() {

			setTitle("Information List");
			setHeight(580);
			centerInPage();

			//------------------------------
			//Title
			//------------------------------
			final DynamicForm commonForm = new MtpForm();
			commonForm.setAutoFocus(true);

			titleField = new MetaDataLangTextItem();
			titleField.setTitle("Title");
			SmartGWTUtil.addHoverToFormItem(titleField, AdminClientMessageUtil.getString("ui_metadata_top_item_InformationItem_titleCustom"));

			iconTagField = new MtpTextItem("iconTag", "Icon Tag");
			SmartGWTUtil.addHoverToFormItem(iconTagField, AdminClientMessageUtil.getString("ui_metadata_top_item_InformationItem_iconTagComment"));

			commonForm.setItems(titleField, iconTagField);

			//------------------------------
			//Information List
			//------------------------------
			final DynamicForm infoListForm = new MtpForm();
			infoListForm.setIsGroup(true);
			infoListForm.setGroupTitle("Information List Settings");

			dispRangeField = new MtpSelectItem("dispRange", "Time Range");
			SmartGWTUtil.addHoverToFormItem(dispRangeField, AdminClientMessageUtil.getString("ui_metadata_top_item_InformationItem_partsTimeDispRange"));
			setDispRangeValues();

			numberOfDisplayField = new IntegerItem();
			numberOfDisplayField.setTitle("Scroll display number");
			numberOfDisplayField.setWidth("100%");
			SmartGWTUtil.addHoverToFormItem(numberOfDisplayField, AdminClientMessageUtil.getString("ui_metadata_top_item_InformationItem_numberOfDisplay"));

			chkEnableHtmlTag = new CheckboxItem();
			chkEnableHtmlTag.setTitle("Enable Html Tag");
			chkUseRichtextEditor = new CheckboxItem();
			chkUseRichtextEditor.setTitle("Use Richtext Editor");

			txaRichtextEditorOption = new MtpTextAreaItem();
			txaRichtextEditorOption.setColSpan(2);
			txaRichtextEditorOption.setTitle("Richtext Editor Option");
			txaRichtextEditorOption.setHeight(100);

			ButtonItem btnEditEetailCustomStyle = new ButtonItem();
			btnEditEetailCustomStyle.setTitle("Edit");
			btnEditEetailCustomStyle.setWidth(100);
			btnEditEetailCustomStyle.setStartRow(false);
			btnEditEetailCustomStyle.setColSpan(3);
			btnEditEetailCustomStyle.setAlign(Alignment.RIGHT);
			btnEditEetailCustomStyle.addClickHandler(event -> {
				MetaDataUtil.showScriptEditDialog(ScriptEditorDialogMode.GROOVY_SCRIPT,
						SmartGWTUtil.getStringValue(txaDetailCustomStyle),
						ScriptEditorDialogConstants.TOPVIEW_INFORMATION_CUSTOM_STYLE,
						null,
						AdminClientMessageUtil.getString("ui_metadata_top_item_InformationItem_detailCustomStyleHint"),
						new ScriptEditorDialogHandler() {

							@Override
							public void onSave(String text) {
								txaDetailCustomStyle.setValue(text);
							}
							@Override
							public void onCancel() {
							}
						});

			});

			txaDetailCustomStyle = new MtpTextAreaItem();
			txaDetailCustomStyle.setColSpan(2);
			txaDetailCustomStyle.setTitle("Detail Custom Style");
			txaDetailCustomStyle.setHeight(100);
			SmartGWTUtil.setReadOnlyTextArea(txaDetailCustomStyle);

			infoListForm.setItems(dispRangeField, numberOfDisplayField, chkEnableHtmlTag,
					chkUseRichtextEditor, txaRichtextEditorOption, btnEditEetailCustomStyle, txaDetailCustomStyle);

			//------------------------------
			//Password Warning Message
			//------------------------------

			final DynamicForm pwWarnform = new MtpForm();
			pwWarnform.setIsGroup(true);
			pwWarnform.setGroupTitle("Password Warning Message Settings");

			showPasswordWarnField = new CheckboxItem();
			showPasswordWarnField.setTitle("show warning message of the password expiration date.");
			SmartGWTUtil.addHoverToFormItem(showPasswordWarnField, AdminClientMessageUtil.getString("ui_metadata_top_item_InformationItem_showPasswordWarn"));

			passwordWarningAgeField = new SliderItem();
			passwordWarningAgeField.setTitle("Remain days threshold");
			passwordWarningAgeField.setWidth(300);
			passwordWarningAgeField.setHeight(20);
			passwordWarningAgeField.setMinValue(0.0);
			passwordWarningAgeField.setMaxValue(30.0);
			passwordWarningAgeField.setDefaultValue(5);	//初期値5
			SmartGWTUtil.addHoverToFormItem(passwordWarningAgeField, AdminClientMessageUtil.getString("ui_metadata_top_item_InformationItem_specifyPasswordWarnremainDay"));

			passwordWarningMessageField = new MetaDataLangTextAreaItem();
			passwordWarningMessageField.setTitle("Custom message");
			passwordWarningMessageField.setColSpan(2);
			passwordWarningMessageField.setHeight(100);
			SmartGWTUtil.addHoverToFormItem(passwordWarningMessageField, AdminClientMessageUtil.getString("ui_metadata_top_item_InformationItem_specifyCustomWarnMessage"));

			passwordWarnAreaStyleField = new MtpComboBoxItem();
			passwordWarnAreaStyleField.setTitle("Custom message area style class");
			SmartGWTUtil.addHoverToFormItem(passwordWarnAreaStyleField, AdminClientMessageUtil.getString("ui_metadata_top_item_InformationItem_warnMessageDispAreaStyle"));
			setPasswordWarnAreaStyleValues();

			passwordWarnMarkStyleField = new MtpComboBoxItem();
			passwordWarnMarkStyleField.setTitle("Custom message mark style class");
			SmartGWTUtil.addHoverToFormItem(passwordWarnMarkStyleField, AdminClientMessageUtil.getString("ui_metadata_top_item_InformationItem_warnMessageDispAreaIcon"));
			setPasswordWarnMarkStyleValues();

			pwWarnform.setItems(showPasswordWarnField, passwordWarningAgeField, passwordWarningMessageField, passwordWarnAreaStyleField, passwordWarnMarkStyleField);

			container.addMember(commonForm);
			container.addMember(infoListForm);
			container.addMember(pwWarnform);

			//------------------------------
			//Footer Layout
			//------------------------------
			IButton save = new IButton("OK");
			save.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if (commonForm.validate() && infoListForm.validate() && pwWarnform.validate()){
						//入力情報をパーツに
						parts.setTitle(SmartGWTUtil.getStringValue(titleField));
						parts.setLocalizedTitleList(titleField.getLocalizedList());
						parts.setIconTag(SmartGWTUtil.getStringValue(iconTagField));
						if (dispRangeField.getValue() != null && !dispRangeField.getValueAsString().isEmpty()) {
							parts.setDispRange(TimeDispRange.valueOf(SmartGWTUtil.getStringValue(dispRangeField)));
						} else {
							parts.setDispRange(null);
						}
						parts.setShowWarningPasswordAge(SmartGWTUtil.getBooleanValue(showPasswordWarnField));
						if (parts.isShowWarningPasswordAge()) {
							parts.setPasswordWarningAge(passwordWarningAgeField.getValueAsFloat().intValue());
							parts.setPasswordWarningMessage(SmartGWTUtil.getStringValue(passwordWarningMessageField));
							parts.setLocalizedPasswordWarningMessageList(passwordWarningMessageField.getLocalizedList());
							parts.setPasswordWarnAreaStyleClass(SmartGWTUtil.getStringValue(passwordWarnAreaStyleField));
							parts.setPasswordWarnMarkStyleClass(SmartGWTUtil.getStringValue(passwordWarnMarkStyleField));
						}
						parts.setNumberOfDisplay(SmartGWTUtil.getIntegerValue(numberOfDisplayField));
						parts.setEnableHtmlTag(SmartGWTUtil.getBooleanValue(chkEnableHtmlTag));
						parts.setUseRichtextEditor(SmartGWTUtil.getBooleanValue(chkUseRichtextEditor));
						parts.setRichtextEditorOption(SmartGWTUtil.getStringValue(txaRichtextEditorOption));
						parts.setDetailCustomStyle(SmartGWTUtil.getStringValue(txaDetailCustomStyle));
						destroy();
					}
				}
			});

			IButton cancel = new IButton("Cancel");
			cancel.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					destroy();
				}
			});

			footer.setMembers(save, cancel);

			setValues();
		}

		private void setDispRangeValues() {
			final LinkedHashMap<String, String> valueMap = new LinkedHashMap<>();
			valueMap.put("", "default");
			for (TimeDispRange value : TimeDispRange.values()) {
				valueMap.put(value.name(), value.name());
			}
			dispRangeField.setValueMap(valueMap);
		}

		private void setPasswordWarnAreaStyleValues() {
			final LinkedHashMap<String, String> valueMap = new LinkedHashMap<>();
			//valueMap.put("", "default");
			valueMap.put("ui-state-highlight ui-corner-all", "ui-state-highlight ui-corner-all");
			valueMap.put("ui-state-error ui-corner-all", "ui-state-error ui-corner-all");
			valueMap.put("ui-state-default ui-corner-all", "ui-state-default ui-corner-all");
			valueMap.put("ui-state-hover ui-corner-all", "ui-state-hover ui-corner-all");
			valueMap.put("ui-state-active ui-corner-all", "ui-state-active ui-corner-all");
			passwordWarnAreaStyleField.setValueMap(valueMap);
		}

		private void setPasswordWarnMarkStyleValues() {
			final LinkedHashMap<String, String> valueMap = new LinkedHashMap<>();
			//valueMap.put("", "default");
			valueMap.put("ui-icon-check", "ui-icon-check");
			valueMap.put("ui-icon-alert", "ui-icon-alert");
			valueMap.put("ui-icon-info", "ui-icon-info");
			valueMap.put("ui-icon-notice", "ui-icon-notice");
			valueMap.put("ui-icon-circle-check", "ui-icon-circle-check");
			passwordWarnMarkStyleField.setValueMap(valueMap);
		}

		private void setValues() {
			titleField.setValue(parts.getTitle());
			titleField.setLocalizedList(parts.getLocalizedTitleList());
			iconTagField.setValue(parts.getIconTag());

			if (parts.getDispRange() != null) {
				dispRangeField.setValue(parts.getDispRange().name());
			} else {
				dispRangeField.setValue("");
			}
			numberOfDisplayField.setValue(parts.getNumberOfDisplay());
			chkEnableHtmlTag.setValue(parts.isEnableHtmlTag());
			chkUseRichtextEditor.setValue(parts.isUseRichtextEditor());
			txaRichtextEditorOption.setValue(parts.getRichtextEditorOption());
			txaDetailCustomStyle.setValue(parts.getDetailCustomStyle());

			showPasswordWarnField.setValue(parts.isShowWarningPasswordAge());
			if (parts.isShowWarningPasswordAge()) {
				passwordWarningAgeField.setValue(parts.getPasswordWarningAge());
				passwordWarningMessageField.setValue(parts.getPasswordWarningMessage());
				passwordWarningMessageField.setLocalizedList(parts.getLocalizedPasswordWarningMessageList());
				passwordWarnAreaStyleField.setValue(parts.getPasswordWarnAreaStyleClass());
				passwordWarnMarkStyleField.setValue(parts.getPasswordWarnMarkStyleClass());
			}
		}

	}

}
