/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.entity.property;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataSelectItem;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataSelectItem.ItemOption;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogConstants;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogHandler;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogMode;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.data.message.MessageItemDS;
import org.iplass.adminconsole.client.metadata.ui.MetaDataUtil;
import org.iplass.adminconsole.client.metadata.ui.common.LocalizedStringSettingDialog;
import org.iplass.adminconsole.client.metadata.ui.entity.property.ValidationListGridRecord.ValidationType;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.message.MessageCategory;

import com.google.gwt.core.client.JavaScriptObject;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class ValidationEditDialog extends AbstractWindow {

	/** 対象Propertyのレコード */
	private ValidationListGridRecord record;

	/** Validationの選択項目 */
	private SelectItem selType;

	/** 説明 */
	private TextAreaItem descriptionField;

	/** 最小値用の入力項目 */
	private TextItem _minItem;
	/** 最大値用の入力項目 */
	private TextItem _maxItem;
	/**  */
	private CheckboxItem _minExItem;
	/**  */
	private CheckboxItem _maxExItem;
	/**  */
	private CheckboxItem _byteCheckItem;
	/**  */
	private CheckboxItem _surrogatePairAsOneCharItem;

	/**  */
	private TextItem _ptrnItem;

	/**  */
	private TextItem _mimeTypePtrnItem;

	/**  */
	private TextAreaItem _scriptItem;
	/**  */
	private CheckboxItem _asArray;

	/**  */
	private TextItem _errorMsgItem;
	/**  */
	private TextItem _errorCodeItem;
	/**  */
	private SelectItem _msgCategoryItem;
	/**  */
	private SelectItem _msgIdItem;

	private DynamicForm _rangeItemForm;
	private DynamicForm _regexItemForm;
	private DynamicForm _lengthItemForm;
	private DynamicForm _scriptItemForm;
	private DynamicForm _binarySizeItemForm;
	private DynamicForm _binaryTypeItemForm;

	private DynamicForm _commonItemForm2;

	/** Form表示用領域 */
	private VLayout _formLayout;

	private boolean isReadOnly = false;
	private boolean canSelectNotNull = true;

	public List<LocalizedStringDefinition> _localizedDisplayNameList;

	public interface ValidationEditDialogHandler {

		void onSaved(ValidationListGridRecord record);
	}

	private ValidationEditDialogHandler handler;

	public ValidationEditDialog(ValidationListGridRecord target, boolean canSelectNotNull, boolean isReadOnly, ValidationEditDialogHandler handler) {
		this.record = target;
		this.canSelectNotNull = canSelectNotNull;
		this.isReadOnly = isReadOnly;
		this.handler = handler;

		initialize();
		dataInitialize();
		formVisibleChange();
	}

	/**
	 * コンポーネント初期化
	 */
	private void initialize() {
		// ダイアログ本体のプロパティ設定
		if (isReadOnly) {
			setTitle("Validator (Read Only)");
		} else {
			setTitle("Validator");
		}
		setWidth(630);
		setHeight(170);
		setShowMinimizeButton(false);
		setIsModal(true);
		setShowModalMask(true);
		setCanDragResize(true);
		centerInPage();

		_formLayout = new VLayout(10);
		_formLayout.setMargin(5);

		selType = new SelectItem();
		selType.setTitle("Type");
		selType.setWidth(200);
		SmartGWTUtil.setRequired(selType);
		selType.addChangedHandler(new ChangedHandler() {
			public void onChanged(ChangedEvent event) {

				formVisibleChange();

				resetMessage();
			}
		});
		descriptionField = new TextAreaItem("description", "Description");
		descriptionField.setWidth("100%");
		descriptionField.setHeight(40);
		descriptionField.setColSpan(4);
		descriptionField.setStartRow(true);
		_maxItem = new TextItem("maxValue");
		_maxItem.setTitle("Max");
		_maxItem.setKeyPressFilter("[0-9]");
		_minItem = new TextItem("minValue");
		_minItem.setTitle("Min");
		_minItem.setKeyPressFilter("[0-9]");
		_maxExItem = new CheckboxItem();
		_maxExItem.setTitle("less than max value");
		SmartGWTUtil.addHoverToFormItem(_maxExItem, rs("ui_metadata_entity_PropertyListGrid_maxExclude"));
		_minExItem = new CheckboxItem();
		_minExItem.setTitle("grater than min value");
		SmartGWTUtil.addHoverToFormItem(_minExItem, rs("ui_metadata_entity_PropertyListGrid_minExclude"));
		_byteCheckItem = new CheckboxItem();
		_byteCheckItem.setTitle("check byte");
		_surrogatePairAsOneCharItem = new CheckboxItem();
		_surrogatePairAsOneCharItem.setTitle("surrogate pair as one char");

		_ptrnItem = new TextItem();
		_ptrnItem.setTitle("Pattern");
		_ptrnItem.setWidth(360);

		_mimeTypePtrnItem = new TextItem();
		_mimeTypePtrnItem.setTitle("Pattern");
		_mimeTypePtrnItem.setWidth(360);

		_scriptItem = new TextAreaItem();
		_scriptItem.setColSpan(2);
		_scriptItem.setTitle("Script");
		_scriptItem.setRowSpan(6);
		_scriptItem.setWidth("100%");
		SmartGWTUtil.setReadOnlyTextArea(_scriptItem);

		ButtonItem editScript = new ButtonItem("editScript", "Edit");
		editScript.setWidth(100);
		editScript.setStartRow(false);
		editScript.setPrompt(rs("ui_metadata_entity_PropertyListGrid_displayDialogEditScript"));
		editScript.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				MetaDataUtil.showScriptEditDialog(ScriptEditorDialogMode.GROOVY_SCRIPT,
						SmartGWTUtil.getStringValue(_scriptItem),
						ScriptEditorDialogConstants.ENTITY_VALIDATION,
						null,
						rs("ui_metadata_entity_PropertyListGrid_scriptHint"),
						new ScriptEditorDialogHandler() {

							@Override
							public void onSave(String text) {
								_scriptItem.setValue(text);
							}
							@Override
							public void onCancel() {
							}
						});
			}
		});

		_asArray = new CheckboxItem();
		_asArray.setTitle("bind variable to array types");
		SmartGWTUtil.addHoverToFormItem(_asArray, rs("ui_metadata_entity_PropertyListGrid_scriptAsArray"));

		_errorMsgItem = new TextItem();
		_errorMsgItem.setTitle("Message (Direct)");
		_errorMsgItem.setWidth(400);
		SmartGWTUtil.addHoverToFormItem(_errorMsgItem, rs("ui_metadata_entity_PropertyListGrid_messageSpecifyComment"));
		//ちょっとヒントが大きいので別ダイアログで表示
		String contentsStyle = "style=\"margin-left:5px;\"";
		String tableStyle = "style=\"border: thin gray solid;padding:5px;white-space:nowrap;\"";
		SmartGWTUtil.addHintToFormItem(_errorMsgItem,
				"<br/>"
				+ rs("ui_metadata_entity_PropertyListGrid_messageDef")
				+ "<p " + contentsStyle + ">" + rs("ui_metadata_entity_PropertyListGrid_directMessageComment") + "</p>"
				+ "<p " + contentsStyle + ">" + rs("ui_metadata_entity_PropertyListGrid_exampleFormat") + "</p>"
				+ "</div>"
				+ rs("ui_metadata_entity_PropertyListGrid_availBindVariable")
				+ "<p " + contentsStyle + ">"
				+ "<table style=\"border-collapse:collapse;\">"
				+ "<tr><th " + tableStyle + rs("ui_metadata_entity_PropertyListGrid_format") + tableStyle + rs("ui_metadata_entity_PropertyListGrid_outputContent")
				+ "<tr><td " + tableStyle + ">name</td><td "+ tableStyle + rs("ui_metadata_entity_PropertyListGrid_propName")
				+ "<tr><td " + tableStyle + ">entityName</td><td "+ tableStyle + rs("ui_metadata_entity_PropertyListGrid_entityName")
				+ "<tr><td " + tableStyle + ">min</td><td "+ tableStyle + rs("ui_metadata_entity_PropertyListGrid_lengthRangeTypeMin")
				+ "<tr><td " + tableStyle + ">max</td><td "+ tableStyle + rs("ui_metadata_entity_PropertyListGrid_lengthRangeTypeMax")
				+ "</table></p></div>"
				);

		ButtonItem langBtn = new ButtonItem();
		langBtn.setTitle("");
		langBtn.setShowTitle(false);
		langBtn.setIcon("world.png");
		langBtn.setStartRow(false);	//これを指定しないとButtonの場合、先頭にくる
		langBtn.setEndRow(false);	//これを指定しないと次のFormItemが先頭にいく
		langBtn.setPrompt(rs("ui_metadata_entity_PropertyListGrid_eachLangDspName"));
		langBtn.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {

				if (_localizedDisplayNameList == null) {
					_localizedDisplayNameList = new ArrayList<LocalizedStringDefinition>();
				}

				LocalizedStringSettingDialog dialog = new LocalizedStringSettingDialog(_localizedDisplayNameList, isReadOnly);
				dialog.show();

			}
		});

		_errorCodeItem = new TextItem();
		_errorCodeItem.setTitle("Code");

		_msgCategoryItem = new MetaDataSelectItem(MessageCategory.class, new ItemOption(true, false));
		_msgCategoryItem.setTitle("Message Category");
		_msgIdItem = new SelectItem();
		_msgIdItem.setTitle("Message Id");


		final DynamicForm commonItemForm1 = new DynamicForm();
		commonItemForm1.setWrapItemTitles(false);
		commonItemForm1.setMargin(5);
		commonItemForm1.setNumCols(2);
		commonItemForm1.setWidth100();
		commonItemForm1.setHeight(25);
		commonItemForm1.setItems(selType, descriptionField);

		_rangeItemForm = new DynamicForm();
		_rangeItemForm.setWrapItemTitles(false);
		_rangeItemForm.setMargin(5);
		_rangeItemForm.setNumCols(5);
		_rangeItemForm.setWidth100();
		_rangeItemForm.setHeight(50);
		_rangeItemForm.setItems(
				_minItem, _minExItem, SmartGWTUtil.createSpacer(),
				_maxItem, _maxExItem);

		_regexItemForm = new DynamicForm();
		_regexItemForm.setWrapItemTitles(false);
		_regexItemForm.setMargin(5);
		_regexItemForm.setNumCols(2);
		_regexItemForm.setWidth100();
		_regexItemForm.setHeight(25);
		_regexItemForm.setItems(_ptrnItem);

		_lengthItemForm = new DynamicForm();
		_lengthItemForm.setWrapItemTitles(false);
		_lengthItemForm.setMargin(5);
		_lengthItemForm.setNumCols(2);
		_lengthItemForm.setWidth100();
		_lengthItemForm.setHeight(95);
		_lengthItemForm.setItems(_minItem, _maxItem, _byteCheckItem, _surrogatePairAsOneCharItem);

		_scriptItemForm = new DynamicForm();
		_scriptItemForm.setWrapItemTitles(false);
		_scriptItemForm.setMargin(5);
		_scriptItemForm.setNumCols(3);
		_scriptItemForm.setColWidths(100, "*", 100);
		_scriptItemForm.setWidth100();
		_scriptItemForm.setHeight(120);
		_scriptItemForm.setItems(new SpacerItem(), new SpacerItem(), editScript, _scriptItem, _asArray);

		_binarySizeItemForm = new DynamicForm();
		_binarySizeItemForm.setWrapItemTitles(false);
		_binarySizeItemForm.setMargin(5);
		_binarySizeItemForm.setNumCols(2);
		_binarySizeItemForm.setWidth100();
		_binarySizeItemForm.setHeight(50);
		_binarySizeItemForm.setItems(_minItem, _maxItem);

		_binaryTypeItemForm = new DynamicForm();
		_binaryTypeItemForm.setWrapItemTitles(false);
		_binaryTypeItemForm.setMargin(5);
		_binaryTypeItemForm.setNumCols(2);
		_binaryTypeItemForm.setWidth100();
		_binaryTypeItemForm.setHeight(25);
		//_binaryTypeItemForm.setItems(_ptrnItem);
		_binaryTypeItemForm.setItems(_mimeTypePtrnItem);

		_commonItemForm2 = new DynamicForm();
		_commonItemForm2.setWrapItemTitles(false);
		_commonItemForm2.setPadding(5);
		_commonItemForm2.setNumCols(5);
		_commonItemForm2.setWidth100();
		_commonItemForm2.setHeight(110);
		_commonItemForm2.setIsGroup(true);
		_commonItemForm2.setGroupTitle("Message");
		_errorCodeItem.setColSpan(4);
		_errorMsgItem.setColSpan(3);
		_commonItemForm2.setItems(
				_errorMsgItem, //SmartGWTUtil.createSpacer(), SmartGWTUtil.createSpacer(),
				langBtn,
				_msgCategoryItem, _msgIdItem, SmartGWTUtil.createSpacer(),
				_errorCodeItem
				);


		IButton ok = new IButton("OK");
		ok.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (!commonItemForm1.validate()) {
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
			public void onClick(ClickEvent event) {
				destroy();
			}
		});

		HLayout btnLayout = new HLayout(5);
		btnLayout.setMargin(5);
		btnLayout.setHeight(20);
		btnLayout.setWidth100();
		btnLayout.setAlign(VerticalAlignment.CENTER);
		btnLayout.addMember(ok);
		btnLayout.addMember(cancel);

		_formLayout.addMember(commonItemForm1);

		addItem(_formLayout);
		addItem(SmartGWTUtil.separator());
		addItem(btnLayout);
	}

	/**
	 * データ初期化
	 */
	@SuppressWarnings("unchecked")
	private void dataInitialize() {

		LinkedHashMap<String, String> validationMap = ValidationType.allTypeMap();
		//NotNullが選択できない場合、除去
		if (!canSelectNotNull) {
			validationMap.remove(ValidationType.NOTNULL.name());
		}
		selType.setValueMap(validationMap);

		selType.setValue(record.getValType());
		descriptionField.setValue(record.getDescription());
		_maxItem.setValue(record.getMax());
		_maxExItem.setValue(record.isMaxValueExcluded());
		_minItem.setValue(record.getMin());
		_minExItem.setValue(record.isMinValueExcluded());
		_byteCheckItem.setValue(record.isByteLengthCheck());
		_surrogatePairAsOneCharItem.setValue(record.isSurrogatePairAsOneChar());
		_ptrnItem.setValue(record.getPtrn());
		_mimeTypePtrnItem.setValue(record.getPtrn());
		_scriptItem.setValue(record.getScripting());
		_asArray.setValue(record.isAsArray());
		_errorMsgItem.setValue(record.getErrorMessage());
		_localizedDisplayNameList = (List<LocalizedStringDefinition>) JSOHelper.convertToJava((JavaScriptObject) record.getErrorMessageMultiLang());
		_errorCodeItem.setValue(record.getErrorCode());
		_msgCategoryItem.setValue(record.getMessageCategory());
		_msgIdItem.setValue(record.getMessageId());

		_msgCategoryItem.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				changeMessageCategory();
				_msgIdItem.clearValue();
			}
		});

		if (record.getMessageCategory() != null && !record.getMessageCategory().isEmpty()) {
			_msgIdItem.setOptionDataSource(MessageItemDS.getInstance(record.getMessageCategory()));
		}
		_msgIdItem.setValueField("id");
		_msgIdItem.setDisplayField("id");
		_msgIdItem.setPickListWidth(520);
		ListGridField itemIdField = new ListGridField("id", "ID", 150);
		ListGridField itemMsgField = new ListGridField("shortMessage", "Message", 350);
		_msgIdItem.setPickListFields(itemIdField, itemMsgField);
		_msgIdItem.setSortField("id");
	}

	private void formVisibleChange() {
		if (_formLayout.contains(_rangeItemForm)) {
			_formLayout.removeMember(_rangeItemForm);
		}
		if (_formLayout.contains(_regexItemForm)) {
			_formLayout.removeMember(_regexItemForm);
		}
		if (_formLayout.contains(_lengthItemForm)) {
			_formLayout.removeMember(_lengthItemForm);
		}
		if (_formLayout.contains(_scriptItemForm)) {
			_formLayout.removeMember(_scriptItemForm);
		}
		if (_formLayout.contains(_binarySizeItemForm)) {
			_formLayout.removeMember(_binarySizeItemForm);
		}
		if (_formLayout.contains(_binaryTypeItemForm)) {
			_formLayout.removeMember(_binaryTypeItemForm);
		}
		if (_formLayout.contains(_commonItemForm2)) {
			_formLayout.removeMember(_commonItemForm2);
		}

		if (selType.getValue() != null) {
			ValidationType validationType
					= ValidationType.valueOf(selType.getValueAsString());
			if (ValidationType.RANGE.equals(validationType)) {
				_formLayout.addMember(_rangeItemForm);
				setHeight(360);
			} else if (ValidationType.REGEX.equals(validationType)) {
				_formLayout.addMember(_regexItemForm);
				setHeight(335);
			} else if (ValidationType.LENGTH.equals(validationType)) {
				_formLayout.addMember(_lengthItemForm);
				setHeight(405);
			} else if (ValidationType.SCRIPT.equals(validationType)) {
				_formLayout.addMember(_scriptItemForm);
				setHeight(460);
			} else if (ValidationType.NOTNULL.equals(validationType)) {
				setHeight(290);
			} else if (ValidationType.BINARYSIZE.equals(validationType)) {
				_formLayout.addMember(_binarySizeItemForm);
				setHeight(385);
			} else if (ValidationType.BINARYTYPE.equals(validationType)) {
				_formLayout.addMember(_binaryTypeItemForm);
				setHeight(335);
			}
			_formLayout.addMember(_commonItemForm2);
		}
//		_formLayout.draw();
	}

	private void resetMessage() {

		_errorMsgItem.clearValue();
		if (_localizedDisplayNameList != null) {
			_localizedDisplayNameList.clear();
		}
		_errorCodeItem.clearValue();

		ValidationType validationType = ValidationType.valueOf(selType.getValueAsString());
		if (ValidationType.NOTNULL == validationType) {
			_msgCategoryItem.setValue("mtp/validation");
			_msgIdItem.setValue("NotNull");
		} else {
			_msgCategoryItem.clearValue();
			_msgIdItem.clearValue();
		}
		changeMessageCategory();
	}

	private void changeMessageCategory() {
		String category = SmartGWTUtil.getStringValue(_msgCategoryItem);
		if (SmartGWTUtil.isEmpty(category)) {
			_msgIdItem.setOptionDataSource(MessageItemDS.getInstance(""));
		} else {
			_msgIdItem.setOptionDataSource(MessageItemDS.getInstance(category));
		}
	}

	private void validateUpdateRecordData() {

		record.setValType(selType.getValueAsString());
		record.setDescription(SmartGWTUtil.getStringValue(descriptionField, true));

		ValidationType validationType
				= ValidationType.valueOf(selType.getValueAsString());
		switch (validationType) {
		case RANGE:
			Object minValue = _rangeItemForm.getValue("minValue");
			if (minValue != null) {
				record.setMin(minValue.toString());
			} else {
				//未指定だったら除外ON
				_minExItem.setValue(true);
			}
			record.setMinValueExcluded(_minExItem.getValueAsBoolean());
			Object maxValue = _rangeItemForm.getValue("maxValue");
			if (maxValue != null) {
				record.setMax(maxValue.toString());
			} else {
				//未指定だったら除外ON
				_maxExItem.setValue(true);
			}
			record.setMaxValueExcluded(_maxExItem.getValueAsBoolean());
			//record.setGeneralPurpus(record.getMin() + "～" + record.getMax());
			break;
		case REGEX:
			if (_ptrnItem.getValue() != null) {
				record.setPtrn(_ptrnItem.getValue().toString());
			}
			//record.setGeneralPurpus(record.getPtrn());
			break;
		case LENGTH:
			Object minLength = _lengthItemForm.getValue("minValue");
			if (minLength != null) {
				record.setMin(minLength.toString());
			} else {
				record.setMin(null);
			}
			Object maxLength = _lengthItemForm.getValue("maxValue");
			if (maxLength != null) {
				record.setMax(maxLength.toString());
			} else {
				record.setMax(null);
			}
			record.setByteLengthCheck(_byteCheckItem.getValueAsBoolean());
			record.setSurrogatePairAsOneChar(_surrogatePairAsOneCharItem.getValueAsBoolean());
			//String purpus = record.getMin() + "～" + record.getMax();
			//if (record.isByteLengthCheck()) {
			//	purpus = purpus + "(byte)";
			//}
			//record.setGeneralPurpus(purpus);
			break;
		case SCRIPT:
			if (_scriptItem.getValue() != null) {
				record.setScripting(_scriptItem.getValue().toString());
			}
			record.setAsArray(_asArray.getValueAsBoolean());
			//record.setGeneralPurpus(record.getScripting());
			break;
		case NOTNULL:
			//特になし
			break;
		case BINARYSIZE:
			record.setMin(SmartGWTUtil.getStringValue(_minItem));
			record.setMax(SmartGWTUtil.getStringValue(_maxItem));
			break;
		case BINARYTYPE:
//			record.setPtrn(SmartGWTUtil.getStringValue(_ptrnItem));
			record.setPtrn(SmartGWTUtil.getStringValue(_mimeTypePtrnItem));
			//record.setGeneralPurpus(record.getPtrn());
			break;
		default:
		}

		record.setErrorMessage(SmartGWTUtil.getStringValue(_errorMsgItem, true));
		record.setErrorMessageMultiLang(_localizedDisplayNameList);
		record.setErrorCode(SmartGWTUtil.getStringValue(_errorCodeItem, true));
		record.setMessageCategory(SmartGWTUtil.getStringValue(_msgCategoryItem, true));
		record.setMessageId(SmartGWTUtil.getStringValue(_msgIdItem, true));

		handler.onSaved(record);
	}

	private String rs(String key) {
		return AdminClientMessageUtil.getString(key);
	}
}
