/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.entity.property.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.EditablePane;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataLangTextItem;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataSelectItem;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataSelectItem.ItemOption;
import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogConstants;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogHandler;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogMode;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm2Column;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpSelectItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextAreaItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.data.message.MessageItemDS;
import org.iplass.adminconsole.client.metadata.ui.MetaDataUtil;
import org.iplass.adminconsole.client.metadata.ui.entity.property.ValidationListGridRecord;
import org.iplass.adminconsole.client.metadata.ui.entity.property.ValidationListGridRecord.ValidationType;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.message.MessageCategory;

import com.google.gwt.core.client.JavaScriptObject;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.FormItemIcon;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.VLayout;

public class ValidationEditDialog extends MtpDialog {

	private static final int BASE_HEIGHT = 330;

	private static final int MESSAGE_HEIGHT = 120;

	private SelectItem selType;

	private TextAreaItem descriptionField;

	private ValidationAttributePane typePane;

	private Map<ValidationType, ValidationAttributePane> mapTypePanes;

	private ValidationMessagePane messagePane;

	private TextAreaItem validationSkipScriptItem;

	private boolean isReadOnly = false;
	private boolean canSelectNotNull = true;

	/** 対象Propertyのレコード */
	private ValidationListGridRecord record;

	public interface ValidationEditDialogHandler {

		void onSaved(ValidationListGridRecord record);
	}

	private ValidationEditDialogHandler handler;

	public ValidationEditDialog(ValidationListGridRecord target, boolean canSelectNotNull, boolean isReadOnly, ValidationEditDialogHandler handler) {
		this.record = target;
		this.canSelectNotNull = canSelectNotNull;
		this.isReadOnly = isReadOnly;
		this.handler = handler;

		if (isReadOnly) {
			setTitle("Validator (Read Only)");
		} else {
			setTitle("Validator");
		}
		setHeight(BASE_HEIGHT);

		initialize();
		dataInitialize();
		formVisibleChange();

		centerInPage();
	}

	/**
	 * コンポーネント初期化
	 */
	private void initialize() {

		selType = new MtpSelectItem();
		selType.setTitle("Type");
		SmartGWTUtil.setRequired(selType);
		selType.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {

				formVisibleChange();

				ValidationType validationType = ValidationType.valueOf(SmartGWTUtil.getStringValue(selType));
				messagePane.resetMessage(validationType);
			}
		});

		descriptionField = new MtpTextAreaItem("description", "Description");
		descriptionField.setHeight(60);
		descriptionField.setColSpan(2);

		ButtonItem editScript = new ButtonItem("editScript", "Edit");
		editScript.setWidth(100);
		editScript.setStartRow(false);
		editScript.setColSpan(3);
		editScript.setAlign(Alignment.RIGHT);
		editScript.setPrompt(rs("ui_metadata_entity_ValidationEditDialog_displayDialogEditScript"));
		editScript.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				MetaDataUtil.showScriptEditDialog(ScriptEditorDialogMode.GROOVY_SCRIPT,
						SmartGWTUtil.getStringValue(validationSkipScriptItem),
						ScriptEditorDialogConstants.ENTITY_VALIDATION_SKIP_SCRIPT,
						null,
						rs("ui_metadata_entity_ValidationEditDialog_scriptHint"),
						new ScriptEditorDialogHandler() {

							@Override
							public void onSave(String text) {
								validationSkipScriptItem.setValue(text);
							}

							@Override
							public void onCancel() {
							}
						});
			}
		});

		validationSkipScriptItem = new MtpTextAreaItem();
		validationSkipScriptItem.setColSpan(2);
		validationSkipScriptItem.setTitle("Validation Skip Script");
		validationSkipScriptItem.setHeight(100);
		SmartGWTUtil.setReadOnlyTextArea(validationSkipScriptItem);

		final DynamicForm form = new MtpForm();
		form.setItems(selType, descriptionField, editScript, validationSkipScriptItem);

		container.addMember(form);

		//FIXME ここで追加したくない。ValidationTypeに寄せたい
		List<ValidationAttributePane> typePaneList = new ArrayList<>();
		typePaneList.add(new BinarySizeAttributePane());
		typePaneList.add(new BinaryTypeAttributePane());
		typePaneList.add(new ExistsAttributePane());
		typePaneList.add(new LengthAttributePane());
		typePaneList.add(new NotNullAttributePane());
		typePaneList.add(new RangeAttributePane());
		typePaneList.add(new RegexAttributePane());
		typePaneList.add(new ScriptAttributePane());
		typePaneList.add(new JavaClassAttributePane());

		mapTypePanes = new HashMap<>();
		for (ValidationAttributePane typePane : typePaneList) {
			mapTypePanes.put(typePane.getType(), typePane);
		}

		messagePane = new ValidationMessagePane();

		IButton ok = new IButton("OK");
		ok.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (!form.validate()) {
					return;
				}
				validateUpdateRecordData();
				destroy();
			}
		});
		if (isReadOnly) {
			ok.setDisabled(true);
		}
		IButton cancel = new IButton("Cancel");
		cancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				destroy();
			}
		});

		footer.setMembers(ok, cancel);
	}

	/**
	 * データ初期化
	 */
	private void dataInitialize() {

		LinkedHashMap<String, String> validationMap = ValidationType.allTypeMap();
		//NotNullが選択できない場合、除去
		if (!canSelectNotNull) {
			validationMap.remove(ValidationType.NOTNULL.name());
		}
		selType.setValueMap(validationMap);

		selType.setValue(record.getValType());
		descriptionField.setValue(record.getDescription());
		validationSkipScriptItem.setValue(record.getValidationSkipScript());

		messagePane.setDefinition(record);
	}

	private void formVisibleChange() {

		if (typePane != null) {
			container.removeMember(typePane);
			typePane = null;
		}

		if (container.contains(messagePane)) {
			container.removeMember(messagePane);
		}

		if (selType.getValue() != null) {
			ValidationType validationType = ValidationType.valueOf(SmartGWTUtil.getStringValue(selType));

			typePane = mapTypePanes.get(validationType);
			if (typePane != null) {
				typePane.setDefinition(record);
				container.addMember(typePane);
				setHeight(BASE_HEIGHT + typePane.panelHeight() + MESSAGE_HEIGHT);
			}
			container.addMember(messagePane);
		}
	}

	private void validateUpdateRecordData() {

		record.setValType(SmartGWTUtil.getStringValue(selType, true));
		record.setDescription(SmartGWTUtil.getStringValue(descriptionField, true));
		record.setValidationSkipScript(SmartGWTUtil.getStringValue(validationSkipScriptItem, true));

		if (typePane != null) {
			typePane.getEditDefinition(record);
		}

		if (messagePane != null) {
			messagePane.getEditDefinition(record);
		}

		handler.onSaved(record);
	}

	private static class ValidationMessagePane extends VLayout implements EditablePane<ValidationListGridRecord> {

		private MetaDataLangTextItem errorMessageItem;

		private SelectItem messageCategoryItem;
		private SelectItem messageIdItem;

		private TextItem errorCodeItem;

		private DynamicForm form;

		public ValidationMessagePane() {

			setHeight(MESSAGE_HEIGHT);

			//ちょっとヒントが大きいので別ダイアログで表示
			String contentsStyle = "style=\"margin-left:5px;\"";
			String tableStyle = "style=\"border: thin gray solid;padding:5px;white-space:nowrap;\"";
			FormItemIcon hintIcon = SmartGWTUtil.getHintIcon(
					"<br/>"
							+ "<div>"
							+ "<p><b>" + rs("ui_metadata_entity_PropertyListGrid_messageDef") + "</b></p>"
							+ "<div " + contentsStyle + ">"
							+ "<p>" + rs("ui_metadata_entity_PropertyListGrid_directMessageComment") + "</p>"
							+ "<p>" + rs("ui_metadata_entity_PropertyListGrid_exampleFormat") + "</p>"
							+ "</div></div>"
							+ "<div>"
							+ "<p><b>" + rs("ui_metadata_entity_PropertyListGrid_availBindVariable") + "</b></p>"
							+ "<div " + contentsStyle + ">"
							+ "<table style=\"border-collapse:collapse;\">"
							+ "<tr><th " + tableStyle + ">" + rs("ui_metadata_entity_PropertyListGrid_format") + "</th><th " + tableStyle + ">"
							+ rs("ui_metadata_entity_PropertyListGrid_outputContent") + "</th></tr>"
							+ "<tr><td " + tableStyle + ">name</td><td " + tableStyle + ">" + rs("ui_metadata_entity_PropertyListGrid_propName")
							+ "</td></tr>"
							+ "<tr><td " + tableStyle + ">entityName</td><td " + tableStyle + ">" + rs("ui_metadata_entity_PropertyListGrid_entityName")
							+ "</td></tr>"
							+ "<tr><td " + tableStyle + ">min</td><td " + tableStyle + ">"
							+ rs("ui_metadata_entity_PropertyListGrid_lengthRangeTypeMin") + "</td></tr>"
							+ "<tr><td " + tableStyle + ">max</td><td " + tableStyle + ">"
							+ rs("ui_metadata_entity_PropertyListGrid_lengthRangeTypeMax") + "</td></tr>"
							+ "<tr><td " + tableStyle + ">reference</td><td " + tableStyle + ">"
							+ rs("ui_metadata_entity_PropertyListGrid_referenceTypeExists") + "</td></tr>"
							+ "</table></div></div>");
			errorMessageItem = new MetaDataLangTextItem(hintIcon);
			errorMessageItem.setTitle("Message (Direct)");
			errorMessageItem.setColSpan(3);
			SmartGWTUtil.addHoverToFormItem(errorMessageItem, rs("ui_metadata_entity_PropertyListGrid_messageSpecifyComment"));

			messageCategoryItem = new MetaDataSelectItem(MessageCategory.class, new ItemOption(true, false));
			messageCategoryItem.setTitle("Message Category");
			messageCategoryItem.addChangedHandler(new ChangedHandler() {
				@Override
				public void onChanged(ChangedEvent event) {
					changeMessageCategory();
					messageIdItem.clearValue();
				}
			});

			messageIdItem = new MtpSelectItem();
			messageIdItem.setTitle("Message Id");
			messageIdItem.setValueField("id");
			messageIdItem.setDisplayField("id");
			messageIdItem.setPickListWidth(520);
			ListGridField itemIdField = new ListGridField("id", "ID", 150);
			ListGridField itemMsgField = new ListGridField("shortMessage", "Message", 350);
			messageIdItem.setPickListFields(itemIdField, itemMsgField);
			messageIdItem.setSortField("id");

			errorCodeItem = new MtpTextItem();
			errorCodeItem.setTitle("Code");

			form = new MtpForm2Column();
			form.setIsGroup(true);
			form.setGroupTitle("Message");
			form.setItems(errorMessageItem, messageCategoryItem, messageIdItem, errorCodeItem);

			addMember(form);
		}

		@SuppressWarnings("unchecked")
		@Override
		public void setDefinition(ValidationListGridRecord record) {

			errorMessageItem.setValue(record.getErrorMessage());
			errorMessageItem
					.setLocalizedList((List<LocalizedStringDefinition>) JSOHelper.convertToJava((JavaScriptObject) record.getErrorMessageMultiLang()));
			messageCategoryItem.setValue(record.getMessageCategory());
			messageIdItem.setValue(record.getMessageId());
			errorCodeItem.setValue(record.getErrorCode());

			if (record.getMessageCategory() != null && !record.getMessageCategory().isEmpty()) {
				messageIdItem.setOptionDataSource(MessageItemDS.getInstance(record.getMessageCategory()));
			}
		}

		@Override
		public ValidationListGridRecord getEditDefinition(ValidationListGridRecord record) {

			record.setErrorMessage(SmartGWTUtil.getStringValue(errorMessageItem, true));
			record.setErrorMessageMultiLang(errorMessageItem.getLocalizedList());
			record.setErrorCode(SmartGWTUtil.getStringValue(errorCodeItem, true));
			record.setMessageCategory(SmartGWTUtil.getStringValue(messageCategoryItem, true));
			record.setMessageId(SmartGWTUtil.getStringValue(messageIdItem, true));

			return record;
		}

		@Override
		public boolean validate() {
			return form.validate();
		}

		@Override
		public void clearErrors() {
			form.clearErrors(true);
		}

		public void resetMessage(ValidationType validationType) {

			errorMessageItem.clearValue();
			errorMessageItem.setLocalizedList(null);
			errorCodeItem.clearValue();

			if (ValidationType.NOTNULL == validationType) {
				messageCategoryItem.setValue("mtp/validation");
				messageIdItem.setValue("NotNull");
			} else {
				messageCategoryItem.clearValue();
				messageIdItem.clearValue();
			}
			changeMessageCategory();
		}

		private void changeMessageCategory() {
			String category = SmartGWTUtil.getStringValue(messageCategoryItem);
			if (SmartGWTUtil.isEmpty(category)) {
				messageIdItem.setOptionDataSource(MessageItemDS.getInstance(""));
			} else {
				messageIdItem.setOptionDataSource(MessageItemDS.getInstance(category));
			}
		}
	}

	private static String rs(String key) {
		return AdminClientMessageUtil.getString(key);
	}
}
