/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

import java.util.LinkedHashMap;
import java.util.List;

import org.iplass.mtp.definition.LocalizedStringDefinition;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class ValidationListGridRecord extends ListGridRecord {

	public enum ValidationType {

		NOTNULL("Not Null"), LENGTH("Length (for String)"), RANGE("Range (for Number)"), REGEX("Regex (for String, Number)"), BINARYSIZE(
				"Binary Size (for Binary)"), BINARYTYPE(
						"Binary Type (for Binary)"), EXISTS("Exists (for Reference)"), SCRIPT("Script"), JAVA_CLASS("Java Class");

		private String displayName;

		private ValidationType(String displayName) {
			this.displayName = displayName;
		}

		public String displayName() {
			return displayName;
		}

		public static LinkedHashMap<String, String> allTypeMap() {
			LinkedHashMap<String, String> typeMap = new LinkedHashMap<>();
			for (ValidationType type : values()) {
				typeMap.put(type.name(), type.displayName());
			}
			return typeMap;
		}
	}

	public static final String MAX = "max";
	public static final String MIN = "min";
	public static final String PTRN = "pattern";
	public static final String VALTYPE = "val";
	public static final String GP = "gp";
	public static final String MAXVALEX = "maxValEx";
	public static final String MINVALEX = "minvalEx";
	public static final String SCRIPTING = "scripting";
	public static final String JAVA_CLASS_NAME = "javaClassName";
	public static final String AS_ARRAY = "asArray";
	public static final String ERRORMSG = "errorMsg";
	public static final String ERRORMSGMULTILANG = "errorMsgMultiLang";
	public static final String ERRORCODE = "errorCode";
	public static final String BYTELENGTH = "byteCheck";
	public static final String SURROGATE_PAIR = "surrogatePairAsOneChar";
	public static final String MSG_CATEGORY = "msgCategory";
	public static final String MSG_ID = "msgId";
	public static final String MSG_DISP_INFO = "msgDispInfo";
	public static final String DESCRIPTION = "description";
	public static final String VALIDATION_SKIP_SCRIPT = "validationSkipScript";
	public static final String VALIDATION_SKIP_SCRIPT_DISP_INFO = "validationSkipScriptDispInfo";

	public ValidationListGridRecord() {
		setMaxValueExcluded(false);
		setMinValueExcluded(false);
		setSurrogatePairAsOneChar(true);
	}

	public String getMax() {
		return getAttribute(MAX);
	}

	public void setMax(String value) {
		setAttribute(MAX, value);
	}

	public String getMin() {
		return getAttribute(MIN);
	}

	public void setMin(String value) {
		setAttribute(MIN, value);
	}

	public String getPtrn() {
		return getAttribute(PTRN);
	}

	public void setPtrn(String value) {
		setAttribute(PTRN, value);
	}

	public String getValType() {
		return getAttribute(VALTYPE);
	}

	public void setValType(String value) {
		setAttribute(VALTYPE, value);
	}

	public String getGeneralPurpus() {
		return getAttribute(GP);
	}

	public void setGeneralPurpus(String value) {
		setAttribute(GP, value);
	}

	public boolean isMaxValueExcluded() {
		return getAttributeAsBoolean(MAXVALEX);
	}

	public void setMaxValueExcluded(boolean value) {
		setAttribute(MAXVALEX, value);
	}

	public boolean isMinValueExcluded() {
		return getAttributeAsBoolean(MINVALEX);
	}

	public void setMinValueExcluded(boolean value) {
		setAttribute(MINVALEX, value);
	}

	public String getScripting() {
		return getAttribute(SCRIPTING);
	}

	public void setScripting(String value) {
		setAttribute(SCRIPTING, value);
	}

	public String getJavaClassName() {
		return getAttribute(JAVA_CLASS_NAME);
	}

	public void setJavaClassName(String className) {
		setAttribute(JAVA_CLASS_NAME, className);
	}

	public boolean isAsArray() {
		return getAttributeAsBoolean(AS_ARRAY);
	}

	public void setAsArray(boolean value) {
		setAttribute(AS_ARRAY, value);
	}

	public String getErrorMessage() {
		return getAttribute(ERRORMSG);
	}

	public void setErrorMessage(String value) {
		setAttribute(ERRORMSG, value);
	}

	public Object getErrorMessageMultiLang() {
		return getAttributeAsObject(ERRORMSGMULTILANG);
	}

	public void setErrorMessageMultiLang(List<LocalizedStringDefinition> value) {
		setAttribute(ERRORMSGMULTILANG, value);
	}

	public String getErrorCode() {
		return getAttribute(ERRORCODE);
	}

	public void setErrorCode(String value) {
		setAttribute(ERRORCODE, value);
	}

	public boolean isByteLengthCheck() {
		return getAttributeAsBoolean(BYTELENGTH);
	}

	public void setByteLengthCheck(boolean value) {
		setAttribute(BYTELENGTH, value);
	}

	public boolean isSurrogatePairAsOneChar() {
		return getAttributeAsBoolean(SURROGATE_PAIR);
	}

	public void setSurrogatePairAsOneChar(boolean value) {
		setAttribute(SURROGATE_PAIR, value);
	}

	public String getMessageCategory() {
		return getAttribute(MSG_CATEGORY);
	}

	public void setMessageCategory(String value) {
		setAttribute(MSG_CATEGORY, value);
	}

	public String getMessageId() {
		return getAttribute(MSG_ID);
	}

	public void setMessageId(String value) {
		setAttribute(MSG_ID, value);
	}

	public String getMessageDisplayInfo() {
		return getAttribute(MSG_DISP_INFO);
	}

	public void setMessageDisplayInfo(String value) {
		setAttribute(MSG_DISP_INFO, value);
	}

	public String getDescription() {
		return getAttribute(DESCRIPTION);
	}

	public void setDescription(String value) {
		setAttribute(DESCRIPTION, value);
	}

	public String getValidationSkipScript() {
		return getAttribute(VALIDATION_SKIP_SCRIPT);
	}

	public void setValidationSkipScript(String value) {
		setAttribute(VALIDATION_SKIP_SCRIPT, value);
	}

	public String getValidationSkipScriptDisplayInfo() {
		return getAttribute(VALIDATION_SKIP_SCRIPT_DISP_INFO);
	}

	public void setValidationSkipScriptDisplayInfo(String value) {
		setAttribute(VALIDATION_SKIP_SCRIPT_DISP_INFO, value);
	}
}
