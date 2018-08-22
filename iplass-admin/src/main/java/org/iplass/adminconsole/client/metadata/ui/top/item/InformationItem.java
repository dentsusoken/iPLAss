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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.iplass.adminconsole.client.base.event.MTPEvent;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.common.LocalizedStringSettingDialog;
import org.iplass.adminconsole.client.metadata.ui.top.PartsOperationHandler;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.view.generic.editor.DateTimePropertyEditor.TimeDispRange;
import org.iplass.mtp.view.top.parts.InformationParts;

import com.smartgwt.client.types.HeaderControls;
import com.smartgwt.client.types.VerticalAlignment;
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
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

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

	private class InformationItemSettingDialog extends AbstractWindow {

		private TextItem titleField;
		private TextItem iconTagField;
		private SelectItem dispRangeField;

		private CheckboxItem showPasswordWarnField;
		private SliderItem passwordWarningAgeField;
		private TextAreaItem passwordWarningMessageField;
		private ComboBoxItem passwordWarnAreaStyleField;
		private ComboBoxItem passwordWarnMarkStyleField;
		private CheckboxItem enableHtmlTagField;
		private IntegerItem numberOfDisplayField;

		private List<LocalizedStringDefinition> localizedTitleList;
		private List<LocalizedStringDefinition> localizedPasswordWarningMessageList;

		public InformationItemSettingDialog() {
			setTitle("Information List");
			setHeight(580);
			setWidth(700);

			setShowMinimizeButton(false);
			setIsModal(true);
			setShowModalMask(true);
			centerInPage();

			//------------------------------
			//Title
			//------------------------------
			final DynamicForm commonForm = new DynamicForm();
			commonForm.setCellPadding(5);
			commonForm.setNumCols(4);
			commonForm.setColWidths(100, 280, "*", "*");
			commonForm.setAutoFocus(true);

			titleField = new TextItem("title","Title");
			titleField.setWidth("100%");
			titleField.setColSpan(2);
			SmartGWTUtil.addHoverToFormItem(titleField, AdminClientMessageUtil.getString("ui_metadata_top_item_InformationItem_titleCustom"));

			ButtonItem titleLangBtn = new ButtonItem("addDisplayName", "Languages");
			titleLangBtn.setShowTitle(false);
			titleLangBtn.setIcon("world.png");
			titleLangBtn.setStartRow(false);	//これを指定しないとButtonの場合、先頭にくる
			titleLangBtn.setEndRow(false);	//これを指定しないと次のFormItemが先頭にいく
			SmartGWTUtil.addHoverToFormItem(titleLangBtn, AdminClientMessageUtil.getString("ui_metadata_top_item_InformationItem_titleEachLang"));
			titleLangBtn.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

				@Override
				public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
					if (localizedTitleList == null) {
						localizedTitleList = new ArrayList<LocalizedStringDefinition>();
					}
					LocalizedStringSettingDialog dialog = new LocalizedStringSettingDialog(localizedTitleList);
					dialog.show();
				}
			});

			iconTagField = new TextItem("iconTag", "Icon Tag");
			iconTagField.setWidth("100%");
			iconTagField.setColSpan(4);
			SmartGWTUtil.addHoverToFormItem(iconTagField, AdminClientMessageUtil.getString("ui_metadata_top_item_InformationItem_iconTagComment"));

			commonForm.setItems(titleField, titleLangBtn, iconTagField);

			//------------------------------
			//Information List
			//------------------------------
			final DynamicForm infoListForm = new DynamicForm();
			infoListForm.setCellPadding(5);
			infoListForm.setNumCols(4);
			infoListForm.setColWidths(100, 280, "*", "*");
			infoListForm.setIsGroup(true);
			infoListForm.setGroupTitle("Information List Settings");

			dispRangeField = new SelectItem("dispRange", "Time Range");
			dispRangeField.setWidth("100%");
			dispRangeField.setColSpan(2);
			SmartGWTUtil.addHoverToFormItem(dispRangeField, AdminClientMessageUtil.getString("ui_metadata_top_item_InformationItem_partsTimeDispRange"));
			setDispRangeValues();

			enableHtmlTagField = new CheckboxItem("enableHtmlTagField", "Enable Html Tag");
			enableHtmlTagField.setColSpan(4);

			numberOfDisplayField = new IntegerItem();
			numberOfDisplayField.setTitle("Scroll display number");
			SmartGWTUtil.addHoverToFormItem(numberOfDisplayField, AdminClientMessageUtil.getString("ui_metadata_top_item_InformationItem_numberOfDisplay"));
			numberOfDisplayField.setColSpan(4);

			infoListForm.setItems(dispRangeField, enableHtmlTagField, numberOfDisplayField);

			//------------------------------
			//Password Warning Message
			//------------------------------

			final DynamicForm pwWarnform = new DynamicForm();
			pwWarnform.setCellPadding(5);
			pwWarnform.setNumCols(4);
			pwWarnform.setColWidths(100, 280, "*", "*");
			pwWarnform.setIsGroup(true);
			pwWarnform.setGroupTitle("Password Warning Message Settings");

			showPasswordWarnField = new CheckboxItem();
			showPasswordWarnField.setTitle("show warning message of the password expiration date.");
			showPasswordWarnField.setShowTitle(false);
			showPasswordWarnField.setColSpan(3);
			SmartGWTUtil.addHoverToFormItem(showPasswordWarnField, AdminClientMessageUtil.getString("ui_metadata_top_item_InformationItem_showPasswordWarn"));

			passwordWarningAgeField = new SliderItem();
			passwordWarningAgeField.setTitle("Remain days threshold");
			passwordWarningAgeField.setWidth(300);
			passwordWarningAgeField.setHeight(20);
			passwordWarningAgeField.setMinValue(0.0);
			passwordWarningAgeField.setMaxValue(30.0);
			passwordWarningAgeField.setColSpan(2);
			passwordWarningAgeField.setStartRow(true);
			passwordWarningAgeField.setDefaultValue(5);	//初期値5
			SmartGWTUtil.addHoverToFormItem(passwordWarningAgeField, AdminClientMessageUtil.getString("ui_metadata_top_item_InformationItem_specifyPasswordWarnremainDay"));

			passwordWarningMessageField = new TextAreaItem();
			passwordWarningMessageField.setTitle("Custom message");
			passwordWarningMessageField.setColSpan(2);
			passwordWarningMessageField.setWidth("100%");
			passwordWarningMessageField.setHeight(100);
			SmartGWTUtil.addHoverToFormItem(passwordWarningMessageField, AdminClientMessageUtil.getString("ui_metadata_top_item_InformationItem_specifyCustomWarnMessage"));

			ButtonItem pwWarnMsgLangBtn = new ButtonItem();
			pwWarnMsgLangBtn.setTitle("Languages");
			pwWarnMsgLangBtn.setShowTitle(false);
			pwWarnMsgLangBtn.setIcon("world.png");
			pwWarnMsgLangBtn.setStartRow(false);	//これを指定しないとButtonの場合、先頭にくる
			pwWarnMsgLangBtn.setEndRow(false);	//これを指定しないと次のFormItemが先頭にいく
			SmartGWTUtil.addHoverToFormItem(pwWarnMsgLangBtn, AdminClientMessageUtil.getString("ui_metadata_top_item_InformationItem_warnMessageEachLang"));
			pwWarnMsgLangBtn.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

				@Override
				public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
					if (localizedPasswordWarningMessageList == null) {
						localizedPasswordWarningMessageList = new ArrayList<LocalizedStringDefinition>();
					}
					LocalizedStringSettingDialog dialog = new LocalizedStringSettingDialog(localizedPasswordWarningMessageList);
					dialog.show();
				}
			});

			passwordWarnAreaStyleField = new ComboBoxItem();
			passwordWarnAreaStyleField.setTitle("Custom message area style class");
			passwordWarnAreaStyleField.setWidth("100%");
			passwordWarnAreaStyleField.setColSpan(2);
			SmartGWTUtil.addHoverToFormItem(passwordWarnAreaStyleField, AdminClientMessageUtil.getString("ui_metadata_top_item_InformationItem_warnMessageDispAreaStyle"));
			setPasswordWarnAreaStyleValues();

			passwordWarnMarkStyleField = new ComboBoxItem();
			passwordWarnMarkStyleField.setTitle("Custom message mark style class");
			passwordWarnMarkStyleField.setWidth("100%");
			passwordWarnMarkStyleField.setColSpan(2);
			SmartGWTUtil.addHoverToFormItem(passwordWarnMarkStyleField, AdminClientMessageUtil.getString("ui_metadata_top_item_InformationItem_warnMessageDispAreaIcon"));
			setPasswordWarnMarkStyleValues();

			pwWarnform.setItems(showPasswordWarnField, passwordWarningAgeField, passwordWarningMessageField, pwWarnMsgLangBtn, passwordWarnAreaStyleField, passwordWarnMarkStyleField);

			//------------------------------
			//Main Layout
			//------------------------------
			VLayout mainLayout = new VLayout();
			mainLayout.setWidth100();
			mainLayout.setHeight100();
			mainLayout.setMargin(10);
			mainLayout.setMembersMargin(10);
			mainLayout.addMember(commonForm);
			mainLayout.addMember(infoListForm);
			mainLayout.addMember(pwWarnform);

			//------------------------------
			//Footer Layout
			//------------------------------
			IButton save = new IButton("OK");
			save.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if (commonForm.validate() && infoListForm.validate() && pwWarnform.validate()){
						//入力情報をパーツに
						parts.setTitle(SmartGWTUtil.getStringValue(titleField));
						parts.setLocalizedTitleList(localizedTitleList);
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
							parts.setLocalizedPasswordWarningMessageList(localizedPasswordWarningMessageList);
							parts.setPasswordWarnAreaStyleClass(SmartGWTUtil.getStringValue(passwordWarnAreaStyleField));
							parts.setPasswordWarnMarkStyleClass(SmartGWTUtil.getStringValue(passwordWarnMarkStyleField));
						}
						parts.setEnableHtmlTag(SmartGWTUtil.getBooleanValue(enableHtmlTagField));
						parts.setNumberOfDisplay(SmartGWTUtil.getIntegerValue(numberOfDisplayField));
						destroy();
					}
				}
			});

			IButton cancel = new IButton("Cancel");
			cancel.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					destroy();
				}
			});

			HLayout footer = new HLayout(5);
			footer.setMargin(5);
			footer.setHeight(20);
			footer.setWidth100();
			footer.setAlign(VerticalAlignment.CENTER);
			footer.setMembers(save, cancel);


			addItem(mainLayout);
			addItem(SmartGWTUtil.separator());
			addItem(footer);

			setValues();
		}

		private void setDispRangeValues() {
			final LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
			valueMap.put("", "default");
			for (TimeDispRange value : TimeDispRange.values()) {
				valueMap.put(value.name(), value.name());
			}
			dispRangeField.setValueMap(valueMap);
		}

		private void setPasswordWarnAreaStyleValues() {
			final LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
			//valueMap.put("", "default");
			valueMap.put("ui-state-highlight ui-corner-all", "ui-state-highlight ui-corner-all");
			valueMap.put("ui-state-error ui-corner-all", "ui-state-error ui-corner-all");
			valueMap.put("ui-state-default ui-corner-all", "ui-state-default ui-corner-all");
			valueMap.put("ui-state-hover ui-corner-all", "ui-state-hover ui-corner-all");
			valueMap.put("ui-state-active ui-corner-all", "ui-state-active ui-corner-all");
			passwordWarnAreaStyleField.setValueMap(valueMap);
		}

		private void setPasswordWarnMarkStyleValues() {
			final LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
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
			localizedTitleList = parts.getLocalizedTitleList();
			iconTagField.setValue(parts.getIconTag());

			if (parts.getDispRange() != null) {
				dispRangeField.setValue(parts.getDispRange().name());
			} else {
				dispRangeField.setValue("");
			}

			enableHtmlTagField.setValue(parts.isEnableHtmlTag());
			numberOfDisplayField.setValue(parts.getNumberOfDisplay());
			showPasswordWarnField.setValue(parts.isShowWarningPasswordAge());
			if (parts.isShowWarningPasswordAge()) {
				passwordWarningAgeField.setValue(parts.getPasswordWarningAge());
				passwordWarningMessageField.setValue(parts.getPasswordWarningMessage());
				localizedPasswordWarningMessageList = parts.getLocalizedPasswordWarningMessageList();
				passwordWarnAreaStyleField.setValue(parts.getPasswordWarnAreaStyleClass());
				passwordWarnMarkStyleField.setValue(parts.getPasswordWarnMarkStyleClass());
			}
		}

	}

}
