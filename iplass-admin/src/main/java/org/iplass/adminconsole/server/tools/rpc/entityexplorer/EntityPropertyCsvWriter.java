/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.server.tools.rpc.entityexplorer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.adminconsole.server.base.i18n.AdminResourceBundleUtil;
import org.iplass.adminconsole.server.base.io.download.AdminCsvWriter;
import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.IndexType;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.ValidationDefinition;
import org.iplass.mtp.entity.definition.properties.AutoNumberProperty;
import org.iplass.mtp.entity.definition.properties.DecimalProperty;
import org.iplass.mtp.entity.definition.properties.ExpressionProperty;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.entity.definition.properties.ReferenceType;
import org.iplass.mtp.entity.definition.properties.SelectProperty;
import org.iplass.mtp.entity.definition.validations.LengthValidation;
import org.iplass.mtp.entity.definition.validations.NotNullValidation;
import org.iplass.mtp.entity.definition.validations.RangeValidation;
import org.supercsv.cellprocessor.ConvertNullTo;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvException;

public class EntityPropertyCsvWriter extends AdminCsvWriter {

	private static final String[] HEADER_KEY = new String[] { "entityName", "entityDispName", "oidPropName", "itemName", "itemDispName", "dataType",
			"numOfChara", "multiple", "required", "updatable", "indexType", "reference", "referenceRelationship", "referenced", "sort",
			"selectValueDefName", "selectValue", "expression", "numOfDecimalPlaces", "roundMode", "startValue", "numOfDigits", "format",
			"typeNumbering", "validationError" };

	private String[] header;
	private CellProcessor[] processors;

	public EntityPropertyCsvWriter(OutputStream out, String encode) throws IOException {
		super(out, encode);

		createCellHeader();
		createCellProcessor();
	}

	public void writeHeader() throws SuperCsvException, IOException {
		csvWriter.writeHeader(header);
	}

	public void writeConfig(EntityDefinition definition) throws IOException {
		if (definition == null) {
			return;
		}

		List<PropertyDefinition> properties = definition.getPropertyList();

		int row = 0;
		for (int i = 0; i < properties.size(); i++) {
			PropertyDefinition property = properties.get(i);
			if (property.isInherited()) {
				continue;
			}
			Map<String, Object> recordMap = new HashMap<String, Object>(HEADER_KEY.length);
			int col = setEntityBasicInfo(recordMap, definition, row, 0);
			col = setPropertyBasicInfo(recordMap, property, col);
			col = setReferenceInfo(recordMap, property, col);
			col = setSelectInfo(recordMap, property, col);
			col = setExpressionInfo(recordMap, property, col);
			col = setDecimalInfo(recordMap, property, col);
			col = setAutoNumberInfo(recordMap, property, col);
			col = setValidationInfo(recordMap, property, col);
			csvWriter.write(recordMap, header, processors);
			row++;
		}
	}

	private void createCellHeader() {
		header = new String[HEADER_KEY.length];
		for (int i = 0; i < HEADER_KEY.length; i++) {
			header[i] = rs("tools.entityexplorer.EntityPropertyCsvWriter." + HEADER_KEY[i]);
		}
	}

	private void createCellProcessor() {

		//Nullの場合落ちるため基本はConvertNullToを指定
		processors = new CellProcessor[HEADER_KEY.length];
		for (int i = 0; i < HEADER_KEY.length; i++) {
			processors[i] = new ConvertNullTo("");
		}
	}

	private int setEntityBasicInfo(Map<String, Object> recordMap, EntityDefinition entity, int row, int col) {
		if (row == 0) {
			recordMap.put(header[col], entity.getName());
			recordMap.put(header[++col], entity.getDisplayName());
			recordMap.put(header[++col], entity.getOidPropertyName());
		} else {
			recordMap.put(header[col], "");
			recordMap.put(header[++col], "");
			recordMap.put(header[++col], "");
		}
		return col;
	}

	private int setPropertyBasicInfo(Map<String, Object> recordMap, PropertyDefinition property, int col) {
		recordMap.put(header[++col], property.getName());
		recordMap.put(header[++col], property.getDisplayName());
		recordMap.put(header[++col], property.getClass().getSimpleName());
		recordMap.put(header[++col], getLength(property));
		recordMap.put(header[++col], property.getMultiplicity());
		recordMap.put(header[++col], (isRequired(property) ? rs("tools.entityexplorer.EntityPropertyCsvWriter.maru") : ""));
		recordMap.put(header[++col], (property.isUpdatable() ? rs("tools.entityexplorer.EntityPropertyCsvWriter.maru") : ""));
		recordMap.put(header[++col], getIndexType(property));

		return col;
	}

	private int setReferenceInfo(Map<String, Object> recordMap, PropertyDefinition property, int col) {

		if (property instanceof ReferenceProperty) {
			ReferenceProperty reference = (ReferenceProperty) property;
			recordMap.put(header[++col], reference.getObjectDefinitionName());
			recordMap.put(header[++col], getReferenceType(reference));
			recordMap.put(header[++col], reference.getMappedBy());
			recordMap.put(header[++col], reference.getOrderBy());
		} else {
			recordMap.put(header[++col], "");
			recordMap.put(header[++col], "");
			recordMap.put(header[++col], "");
			recordMap.put(header[++col], "");
		}

		return col;
	}

	private int setSelectInfo(Map<String, Object> recordMap, PropertyDefinition property, int col) {

		if (property instanceof SelectProperty) {
			SelectProperty select = (SelectProperty) property;

			recordMap.put(header[++col], (select.getSelectValueDefinitionName() != null ? select.getSelectValueDefinitionName() : ""));

			if (select.getSelectValueList() != null) {
				StringBuilder builder = new StringBuilder();
				for (SelectValue value : select.getSelectValueList()) {
					builder.append(value.getDisplayName() + "(" + value.getValue() + "),");
				}
				if (builder.length() > 0) {
					builder.deleteCharAt(builder.length() - 1);
				}
				recordMap.put(header[++col], builder.toString());
			} else {
				recordMap.put(header[++col], "");
			}
		} else {
			recordMap.put(header[++col], "");
			recordMap.put(header[++col], "");
		}
		return col;
	}

	private int setExpressionInfo(Map<String, Object> recordMap, PropertyDefinition property, int col) {

		if (property instanceof ExpressionProperty) {
			ExpressionProperty expression = (ExpressionProperty) property;
			recordMap.put(header[++col], expression.getExpression());
		} else {
			recordMap.put(header[++col], "");
		}
		return col;
	}

	private int setDecimalInfo(Map<String, Object> recordMap, PropertyDefinition property, int col) {

		if (property instanceof DecimalProperty) {
			DecimalProperty decimal = (DecimalProperty) property;
			recordMap.put(header[++col], decimal.getScale());
			recordMap.put(header[++col], decimal.getRoundingMode().name());
		} else {
			recordMap.put(header[++col], "");
			recordMap.put(header[++col], "");
		}
		return col;
	}

	private int setAutoNumberInfo(Map<String, Object> recordMap, PropertyDefinition property, int col) {

		if (property instanceof AutoNumberProperty) {
			AutoNumberProperty autoNumber = (AutoNumberProperty) property;
			recordMap.put(header[++col], autoNumber.getStartsWith());
			recordMap.put(header[++col], autoNumber.getFixedNumberOfDigits());
			recordMap.put(header[++col], autoNumber.getFormatScript());
			recordMap.put(header[++col], autoNumber.getNumberingType().name());
		} else {
			recordMap.put(header[++col], "");
			recordMap.put(header[++col], "");
			recordMap.put(header[++col], "");
			recordMap.put(header[++col], "");
		}
		return col;
	}

	private int setValidationInfo(Map<String, Object> recordMap, PropertyDefinition property, int col) {

		if (property.getValidations() != null) {
			StringBuilder builder = new StringBuilder();
			for (ValidationDefinition validation : property.getValidations()) {
				builder.append(validation.getClass().getSimpleName() + ":");
				if (validation.getErrorCode() != null) {
					builder.append("(" + validation.getErrorCode() + ")");
				}
				builder.append(validation.getErrorMessage());
				if (validation.getValidationSkipScript() != null) {
					builder.append("(ValidationSkipScript:Set)");
				} else {
					builder.append("(ValidationSkipScript:Not Set)");
				}
				builder.append(",");
			}
			if (builder.length() > 0) {
				builder.deleteCharAt(builder.length() - 1);
			}
			recordMap.put(header[++col], builder.toString());
		} else {
			recordMap.put(header[++col], "");
		}
		return col;
	}

	private String getLength(PropertyDefinition property) {
		if (property.getValidations() != null) {
			for (ValidationDefinition validation : property.getValidations()) {
				if (validation instanceof LengthValidation) {
					LengthValidation length = (LengthValidation) validation;
					return length.getMin() + "-" + length.getMax()
							+ (length.isCheckBytes() ? "byte" : "");
				} else if (validation instanceof RangeValidation) {
					RangeValidation range = (RangeValidation) validation;
					return (range.isMinValueExcluded() ? "" : range.getMin())
							+ "-"
							+ (range.isMaxValueExcluded() ? "" : range.getMax());
				}
			}
		}
		return "-";
	}

	private boolean isRequired(PropertyDefinition property) {
		if (property.getValidations() != null) {
			for (ValidationDefinition validation : property.getValidations()) {
				if (validation instanceof NotNullValidation) {
					return true;
				}
			}
		}
		return false;
	}

	private String getIndexType(PropertyDefinition property) {
		IndexType type = property.getIndexType();
		if (type == null || type.equals(IndexType.NON_INDEXED)) {
			return "";
		}
		if (type.equals(IndexType.NON_UNIQUE)) {
			return "I";
		}
		if (type.equals(IndexType.UNIQUE)) {
			return "U";
		}
		if (type.equals(IndexType.UNIQUE_WITHOUT_NULL)) {
			return "UN";
		}
		return "";
	}

	private String getReferenceType(ReferenceProperty property) {
		ReferenceType type = property.getReferenceType();
		if (type == null) {
			return "!Null";
		}
		return property.getReferenceType().name();
	}

	private static String rs(String key, Object... arguments) {
		return AdminResourceBundleUtil.resourceString(key, arguments);
	}

}
