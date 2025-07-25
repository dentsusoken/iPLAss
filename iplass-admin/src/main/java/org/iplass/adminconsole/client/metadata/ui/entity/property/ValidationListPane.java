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

package org.iplass.adminconsole.client.metadata.ui.entity.property;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.entity.property.ValidationListGridRecord.ValidationType;
import org.iplass.adminconsole.client.metadata.ui.entity.property.validation.ValidationEditDialog;
import org.iplass.adminconsole.client.metadata.ui.entity.property.validation.ValidationEditDialog.ValidationEditDialogHandler;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.entity.definition.ValidationDefinition;
import org.iplass.mtp.entity.definition.validations.BinarySizeValidation;
import org.iplass.mtp.entity.definition.validations.BinaryTypeValidation;
import org.iplass.mtp.entity.definition.validations.ExistsValidation;
import org.iplass.mtp.entity.definition.validations.JavaClassValidation;
import org.iplass.mtp.entity.definition.validations.LengthValidation;
import org.iplass.mtp.entity.definition.validations.NotNullValidation;
import org.iplass.mtp.entity.definition.validations.RangeValidation;
import org.iplass.mtp.entity.definition.validations.RegexValidation;
import org.iplass.mtp.entity.definition.validations.ScriptingValidation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class ValidationListPane extends VLayout implements PropertyAttributePane {

	/** Validation用のGrid */
	private ValidationGrid validationGrid;

	private IButton addButton;
	private IButton delButton;

	private boolean readOnly;

	private ValidationListPaneHandler handler;

	public interface ValidationListPaneHandler {

		void onChangeNotNull(boolean isNotNull);

		boolean canEditNotNull();
	}

	public ValidationListPane(ValidationListPaneHandler handler) {
		this.handler = handler;

		validationGrid = new ValidationGrid();

		validationGrid.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				startValidationEdit(false, (ValidationListGridRecord) event.getRecord());
			}
		});

		addButton = new IButton("Add");
		addButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				startValidationEdit(true, new ValidationListGridRecord());
			}
		});
		delButton = new IButton("Remove");
		delButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				validationGrid.removeSelectedData();

				//Not Nullが選択されていた場合は、必須チェック解除
				checkNotNullValidation();
			}
		});

		HLayout footer = new HLayout(5);
		footer.setWidth100();
		footer.setHeight(30);
		footer.setAlign(Alignment.LEFT);
		footer.setMargin(5);
		footer.addMember(addButton);
		footer.addMember(delButton);

		addMember(SmartGWTUtil.titleLabel("Validators"));
		addMember(validationGrid);
		addMember(footer);
	}

	@Override
	public void applyFrom(String defName, PropertyListGridRecord record, PropertyAttribute typeAttribute) {

		if (record.isInherited()) {
			readOnly = true;
			addButton.setDisabled(true);
			delButton.setDisabled(true);
		}

		if (record.getValidationList() != null && record.getValidationList().size() > 0) {
			List<ValidationListGridRecord> valRecordList = new ArrayList<>();

			for (ValidationDefinition vd : record.getValidationList()) {
				ValidationListGridRecord validateRecord = new ValidationListGridRecord();
				if (vd instanceof RangeValidation) {
					RangeValidation raVd = (RangeValidation) vd;
					validateRecord.setValType(ValidationType.RANGE.name());
					validateRecord.setMax(raVd.getMax());
					validateRecord.setMin(raVd.getMin());
					validateRecord.setMaxValueExcluded(raVd.isMaxValueExcluded());
					validateRecord.setMinValueExcluded(raVd.isMinValueExcluded());
				} else if (vd instanceof RegexValidation) {
					RegexValidation reVd = (RegexValidation) vd;
					validateRecord.setValType(ValidationType.REGEX.name());
					validateRecord.setPtrn(reVd.getPattern());
				} else if (vd instanceof LengthValidation) {
					LengthValidation leVd = (LengthValidation) vd;
					validateRecord.setValType(ValidationType.LENGTH.name());
					if (leVd.getMax() != null) {
						validateRecord.setMax(String.valueOf(leVd.getMax()));
					}
					if (leVd.getMin() != null) {
						validateRecord.setMin(String.valueOf(leVd.getMin()));
					}
					validateRecord.setByteLengthCheck(leVd.isCheckBytes());
					validateRecord.setSurrogatePairAsOneChar(leVd.isSurrogatePairAsOneChar());
				} else if (vd instanceof ScriptingValidation) {
					ScriptingValidation sVd = (ScriptingValidation) vd;
					validateRecord.setValType(ValidationType.SCRIPT.name());
					validateRecord.setScripting(sVd.getScript());
					validateRecord.setAsArray(sVd.isAsArray());
				} else if (vd instanceof JavaClassValidation) {
					JavaClassValidation javaVd = (JavaClassValidation) vd;
					validateRecord.setValType(ValidationType.JAVA_CLASS.name());
					validateRecord.setJavaClassName(javaVd.getClassName());
					validateRecord.setAsArray(javaVd.isAsArray());
				} else if (vd instanceof NotNullValidation) {
					//念のため必須変更不可の場合は、一覧に出さない
					//今は入力できないが、今まで入力してしまったデータを考慮してクリーンアップ
					PropertyDefinitionType type = record.getRecordType();
					if (type == PropertyDefinitionType.EXPRESSION || type == PropertyDefinitionType.AUTONUMBER) {
						continue;
					}
					//NotNullValidation nVd = (NotNullValidation)vd;
					validateRecord.setValType(ValidationType.NOTNULL.name());
				} else if (vd instanceof BinaryTypeValidation) {
					BinaryTypeValidation btVd = (BinaryTypeValidation) vd;
					validateRecord.setValType(ValidationType.BINARYTYPE.name());
					validateRecord.setPtrn(btVd.getAcceptMimeTypesPattern());
				} else if (vd instanceof BinarySizeValidation) {
					BinarySizeValidation bsVd = (BinarySizeValidation) vd;
					validateRecord.setValType(ValidationType.BINARYSIZE.name());
					if (bsVd.getMax() != null) {
						validateRecord.setMax(String.valueOf(bsVd.getMax()));
					}
					if (bsVd.getMin() != null) {
						validateRecord.setMin(String.valueOf(bsVd.getMin()));
					}
				} else if (vd instanceof ExistsValidation) {
					validateRecord.setValType(ValidationType.EXISTS.name());
				} else {
					continue;
				}
				validateRecord.setErrorCode(vd.getErrorCode());
				validateRecord.setErrorMessage(vd.getErrorMessage());
				validateRecord.setErrorMessageMultiLang(vd.getLocalizedErrorMessageList());
				validateRecord.setMessageCategory(vd.getMessageCategory());
				validateRecord.setMessageId(vd.getMessageId());
				validateRecord.setDescription(vd.getDescription());
				validateRecord.setValidationSkipScript(vd.getValidationSkipScript());

				//一覧に表示する情報の設定
				setValidationRecordDisplayInfo(validateRecord);

				valRecordList.add(validateRecord);
			}

			ValidationListGridRecord[] records = new ValidationListGridRecord[valRecordList.size()];
			validationGrid.setData(valRecordList.toArray(records));
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public void applyTo(PropertyListGridRecord record) {

		List<ValidationDefinition> vdList = new ArrayList<>();
		for (ListGridRecord valRecord : validationGrid.getRecords()) {
			ValidationListGridRecord vRecord = (ValidationListGridRecord) valRecord;
			ValidationType valType = ValidationType.valueOf(vRecord.getValType());
			ValidationDefinition validation = null;
			if (ValidationType.RANGE.equals(valType)) {
				validation = new RangeValidation(vRecord.getMax(), vRecord.isMaxValueExcluded(),
						vRecord.getMin(), vRecord.isMinValueExcluded(),
						vRecord.getErrorMessage(), vRecord.getErrorCode());
			} else if (ValidationType.REGEX.equals(valType)) {
				validation = new RegexValidation(vRecord.getPtrn(),
						vRecord.getErrorMessage(), vRecord.getErrorCode());
			} else if (ValidationType.LENGTH.equals(valType)) {
				Integer min = vRecord.getMin() != null ? Integer.valueOf(vRecord.getMin()) : null;
				Integer max = vRecord.getMax() != null ? Integer.valueOf(vRecord.getMax()) : null;
				validation = new LengthValidation(min, max,
						vRecord.getErrorMessage(), vRecord.getErrorCode());
				((LengthValidation) validation).setCheckBytes(vRecord.isByteLengthCheck());
				((LengthValidation) validation).setSurrogatePairAsOneChar(vRecord.isSurrogatePairAsOneChar());
			} else if (ValidationType.SCRIPT.equals(valType)) {
				validation = new ScriptingValidation(vRecord.getScripting(),
						vRecord.getErrorMessage(), vRecord.getErrorCode());
				((ScriptingValidation) validation).setAsArray(vRecord.isAsArray());
			} else if (ValidationType.JAVA_CLASS.equals(valType)) {
				validation = new JavaClassValidation(vRecord.getJavaClassName(),
						vRecord.getErrorMessage(), vRecord.getErrorCode());
				((JavaClassValidation) validation).setAsArray(vRecord.isAsArray());
			} else if (ValidationType.NOTNULL.equals(valType)) {
				validation = new NotNullValidation();
			} else if (ValidationType.BINARYSIZE.equals(valType)) {
				Long min = vRecord.getMin() != null ? Long.valueOf(vRecord.getMin()) : null;
				Long max = vRecord.getMax() != null ? Long.valueOf(vRecord.getMax()) : null;
				validation = new BinarySizeValidation(min, max,
						vRecord.getErrorMessage(), vRecord.getErrorCode());
			} else if (ValidationType.BINARYTYPE.equals(valType)) {
				validation = new BinaryTypeValidation(vRecord.getPtrn(),
						vRecord.getErrorMessage(), vRecord.getErrorCode());
			} else if (ValidationType.EXISTS.equals(valType)) {
				validation = new ExistsValidation();
			} else {
				GWT.log("un support validation type. type=" + valType.name());
			}
			if (validation != null) {
				validation.setErrorMessage(vRecord.getErrorMessage());
				validation.setLocalizedErrorMessageList(
						(List<LocalizedStringDefinition>) JSOHelper.convertToJava((JavaScriptObject) vRecord.getErrorMessageMultiLang()));
				validation.setErrorCode(vRecord.getErrorCode());
				validation.setMessageCategory(vRecord.getMessageCategory());
				validation.setMessageId(vRecord.getMessageId());
				validation.setDescription(vRecord.getDescription());
				validation.setValidationSkipScript(vRecord.getValidationSkipScript());
				vdList.add(validation);
			}
		}

		record.setValidationList(vdList);

	}

	@Override
	public boolean validate() {
		return true;
	}

	@Override
	public int panelHeight() {
		return 160;
	}

	public void onChangeNotNullFromAttribute(boolean isNotNull) {

		if (isNotNull) {
			//選択された場合、NotNullValidatorに追加
			ValidationListGridRecord validator = new ValidationListGridRecord();
			validator.setValType(ValidationType.NOTNULL.name());
			validator.setErrorCode(null);
			validator.setErrorMessage(null);
			validator.setErrorMessageMultiLang(null);
			validator.setMessageCategory("mtp/validation");
			validator.setMessageId("NotNull");
			validator.setDescription(null);
			validator.setValidationSkipScript(null);

			//一覧に表示する情報の設定
			setValidationRecordDisplayInfo(validator);

			//先頭に追加
			validationGrid.getRecordList().addAt(validator, 0);
			validationGrid.refreshFields();

		} else {
			//選択解除された場合、NotNullValidatorを削除
			RecordList list = validationGrid.getRecordList();
			Record[] records = list.findAll(ValidationListGridRecord.VALTYPE, ValidationType.NOTNULL.name());

			if (records != null) {
				for (Record record : records) {
					validationGrid.removeData(record);
				}
			}
		}
	}

	private void checkNotNullValidation() {
		boolean isExistNotNull = false;
		ListGridRecord[] records = validationGrid.getRecords();
		for (ListGridRecord record : records) {
			ValidationListGridRecord validation = (ValidationListGridRecord) record;
			if (ValidationType.NOTNULL.equals(ValidationType.valueOf(validation.getValType()))) {
				isExistNotNull = true;
				break;
			}
		}

		//Not Nullの状態を通知
		handler.onChangeNotNull(isExistNotNull);
	}

	private void startValidationEdit(final boolean isNewRow, final ValidationListGridRecord target) {

		ValidationEditDialog dialog = new ValidationEditDialog(target, handler.canEditNotNull(), readOnly, new ValidationEditDialogHandler() {

			@Override
			public void onSaved(ValidationListGridRecord record) {

				if (isNewRow) {
					validationGrid.addData(record);
				}

				//一覧に表示する情報の設定
				setValidationRecordDisplayInfo(record);

				validationGrid.updateData(record);
				validationGrid.refreshFields();

				//必須Validatorが違うValidatorに変更された場合を考慮して、必須チェック
				checkNotNullValidation();
			}

		});
		dialog.show();
	}

	private void setValidationRecordDisplayInfo(ValidationListGridRecord record) {
		ValidationType validationType = ValidationType.valueOf(record.getValType());
		if (ValidationType.RANGE.equals(validationType)) {
			String purpus = "";
			if (record.getMin() != null) {
				purpus += record.getMin();
				if (record.isMinValueExcluded()) {
					purpus += " < ";
				} else {
					purpus += " <= ";
				}
			}

			purpus += " value ";

			if (record.getMax() != null) {
				if (record.isMaxValueExcluded()) {
					purpus += " < ";
				} else {
					purpus += " <= ";
				}
				purpus += record.getMax();
			}
			record.setGeneralPurpus(purpus);
		} else if (ValidationType.REGEX.equals(validationType)) {
			record.setGeneralPurpus(record.getPtrn());
		} else if (ValidationType.LENGTH.equals(validationType)) {
			String purpus = "";
			if (record.getMin() != null) {
				purpus += record.getMin();
				if (record.isByteLengthCheck()) {
					purpus += "(byte)";
				}
				purpus += " <= ";
			}

			purpus += " value ";

			if (record.getMax() != null) {
				purpus += " <= ";
				purpus += record.getMax();
				if (record.isByteLengthCheck()) {
					purpus += "(byte)";
				}
			}
			record.setGeneralPurpus(purpus);
		} else if (ValidationType.SCRIPT.equals(validationType)) {
			record.setGeneralPurpus(record.getScripting());
		} else if (ValidationType.JAVA_CLASS.equals(validationType)) {
			record.setGeneralPurpus(record.getJavaClassName());
		} else if (ValidationType.NOTNULL.equals(validationType)) {
			//特になし
		} else if (ValidationType.BINARYSIZE.equals(validationType)) {
			String purpus = "";
			if (record.getMin() != null) {
				purpus += record.getMin() + "(byte)";
				purpus += " <= ";
			}

			purpus += " size ";

			if (record.getMax() != null) {
				purpus += " <= ";
				purpus += record.getMax() + "(byte)";
			}
			record.setGeneralPurpus(purpus);
		} else if (ValidationType.BINARYTYPE.equals(validationType)) {
			record.setGeneralPurpus(record.getPtrn());
		}

		if (record.getErrorMessage() != null) {
			record.setMessageDisplayInfo(record.getErrorMessage());
		} else if (record.getMessageCategory() != null) {
			String message = "@message(" + record.getMessageCategory();
			if (record.getMessageId() != null) {
				message = message + " - " + record.getMessageId();
			} else {
				message = message + " - ?";
			}
			message = message + ")";
			record.setMessageDisplayInfo(message);
		} else {
			record.setMessageDisplayInfo("");
		}
		if (record.getValidationSkipScript() != null) {
			record.setValidationSkipScriptDisplayInfo("Set");
		} else {
			record.setValidationSkipScriptDisplayInfo("Not Set");
		}
	}

	private static class ValidationGrid extends ListGrid {

		public ValidationGrid() {

			setMargin(5);
			setHeight(1);
			setWidth100();

			setShowAllColumns(true);
			setShowAllRecords(true);
			setCanResizeFields(true);

			setCanGroupBy(false);
			setCanFreezeFields(false);
			setCanPickFields(false);
			setCanSort(false);
			setCanAutoFitFields(false);

			//grid内でのD&Dでの並べ替えを許可
			setCanDragRecordsOut(true);
			setCanAcceptDroppedRecords(true);
			setCanReorderRecords(true);

			setOverflow(Overflow.VISIBLE);
			setBodyOverflow(Overflow.VISIBLE);
			setLeaveScrollbarGap(false); //falseで縦スクロールバー領域が自動表示制御される

			ListGridField validationField = new ListGridField(ValidationListGridRecord.VALTYPE, "Type");
			validationField.setWidth(100);
			ListGridField gpField = new ListGridField(ValidationListGridRecord.GP, "Value");
			gpField.setWidth(200);
			ListGridField errorMsgField = new ListGridField(ValidationListGridRecord.MSG_DISP_INFO, "Message");
			ListGridField errorCodeField = new ListGridField(ValidationListGridRecord.ERRORCODE, "Code");
			errorCodeField.setWidth(60);
			ListGridField validationSkipScriptField = new ListGridField(ValidationListGridRecord.VALIDATION_SKIP_SCRIPT_DISP_INFO, "Skip Setting");
			validationSkipScriptField.setWidth(70);
			setFields(validationField, gpField, errorMsgField, errorCodeField, validationSkipScriptField);
		}

	}

}
