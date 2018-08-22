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
 * Entityの検証結果を表します。
 * 
 * @author K.Higuchi
 *
 */
public class ValidateResult {
	
	private List<ValidateError> errors;
	
	public ValidateResult() {
	}
	
	@Deprecated
	public ValidateResult(ValidateError[] errors) {
		setError(errors);
	}
	
	public ValidateResult(List<ValidateError> errors) {
		this.errors = errors;
	}
	
	public List<ValidateError> getErrors() {
		return errors;
	}
	
	public void setErrors(List<ValidateError> errors) {
		this.errors = errors;
	}
	
	@Deprecated
	public ValidateError[] getError() {
		if (errors == null) {
			return null;
		} else {
			return errors.toArray(new ValidateError[errors.size()]);
		}
	}

	@Deprecated
	public void setError(ValidateError[] errors) {
		if (errors != null) {
			this.errors = new ArrayList<>(errors.length);
			for (ValidateError ve: errors) {
				this.errors.add(ve);
			}
		} else {
			this.errors = null;
		}
	}
	
	public boolean hasError() {
		return (errors != null && errors.size() != 0);
	}
	
	public void addError(ValidateError error) {
		if (errors == null) {
			errors = new ArrayList<>();
		}
		
		errors.add(error);
	}

}
