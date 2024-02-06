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

package org.iplass.mtp.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * バリデーションエラーがあった際にスローされる例外。
 * 
 * @author K.Higuchi
 *
 */
public class EntityValidationException extends EntityApplicationException {
	private static final long serialVersionUID = 742734525985667059L;
	
	private List<ValidateError> validateResults;

	public EntityValidationException() {
		super();
	}
	
	public EntityValidationException(String message) {
		super(message);
	}

	public EntityValidationException(String message, List<ValidateError> validateResults) {
		super(message + makeDetailMessage(validateResults));
		setValidateResults(validateResults);
	}
	
	@Deprecated
	public EntityValidationException(String message, ValidateError[] validateResult) {
		super(message + makeDetailMessage(new ArrayList<>(Arrays.asList(validateResult))));
		setValidateResult(validateResult);
	}
	
	private static String makeDetailMessage(List<ValidateError> validateResults) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for (ValidateError v: validateResults) {
			sb.append("\"");
			sb.append(v.getPropertyName());
			sb.append("=");
			sb.append(v.getErrorMessages());
			sb.append("\",");
		}
		sb.append("}");
		return sb.toString();
	}

	public List<ValidateError> getValidateResults() {
		return validateResults;
	}

	public void setValidateResults(List<ValidateError> validateResults) {
		this.validateResults = validateResults;
	}

	@Deprecated
	public ValidateError[] getValidateResult() {
		if (validateResults == null) {
			return null;
		} else {
			return validateResults.toArray(new ValidateError[validateResults.size()]);
		}
	}

	@Deprecated
	public void setValidateResult(ValidateError[] validateResult) {
		if (validateResult == null) {
			this.validateResults = null;
		} else {
			this.validateResults = new ArrayList<>(Arrays.asList(validateResult));
		}
	}

}
