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

package org.iplass.mtp.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Entityの検証結果の詳細。
 * 項目別のエラーメッセージ、コードを保持。
 * 
 * @author K.Higuchi
 *
 */
public class ValidateError {
	
	private String propertyName;
	private String propertyDisplayName;
	private List<String> errorMessages;
	private List<String> errorCodes;
	
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public String getPropertyDisplayName() {
		return propertyDisplayName;
	}
	public void setPropertyDisplayName(String propertyDisplayName) {
		this.propertyDisplayName = propertyDisplayName;
	}
	public List<String> getErrorCodes() {
		return errorCodes;
	}
	public void setErrorCodes(List<String> errorCodes) {
		this.errorCodes = errorCodes;
	}
	public List<String> getErrorMessages() {
		return errorMessages;
	}
	public void setErrorMessages(List<String> errorMessages) {
		this.errorMessages = errorMessages;
	}
	
	public void addErrorMessage(String errorMessage) {
		addErrorMessage(errorMessage, "");
	}
	
	public void addErrorMessage(String errorMessage, String errorCode) {
		if (errorMessages == null) {
			errorMessages = new ArrayList<String>();
		}
		if (errorCodes == null) {
			errorCodes = new ArrayList<String>();
		}
		errorMessages.add(errorMessage);
		errorCodes.add(errorCode);
	}

}
