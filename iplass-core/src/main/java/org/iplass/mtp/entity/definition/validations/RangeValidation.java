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

package org.iplass.mtp.entity.definition.validations;

import org.iplass.mtp.entity.definition.ValidationDefinition;

/**
 * 数値の範囲をチェックするValidation。
 * min、max、maxValueExcluded、minValueExcludedの指定により、範囲を定義。
 * その範囲外の場合、エラーとなる。
 * 
 * @author K.Higuchi
 *
 */
public class RangeValidation extends ValidationDefinition {
	private static final long serialVersionUID = -2998587323247851969L;
	
	private String max;
	private String min;
	
	private boolean maxValueExcluded;
	private boolean minValueExcluded;
	
	public RangeValidation() {
	}
	
	public RangeValidation(String max, boolean maxValueExcluded,
			String min, boolean minValueExcluded, String errorMessage) {
		this(max, maxValueExcluded, min, minValueExcluded, errorMessage, null);
	}

	public RangeValidation(String max, boolean maxValueExcluded,
			String min, boolean minValueExcluded, String errorMessage, String errorCode) {
		super();
		this.max = max;
		this.min = min;
		this.maxValueExcluded = maxValueExcluded;
		this.minValueExcluded = minValueExcluded;
		setErrorMessage(errorMessage);
		setErrorCode(errorCode);
	}
	
	public String getMax() {
		return max;
	}

	public void setMax(String max) {
		this.max = max;
	}

	public String getMin() {
		return min;
	}

	public void setMin(String min) {
		this.min = min;
	}

	public boolean isMaxValueExcluded() {
		return maxValueExcluded;
	}

	public void setMaxValueExcluded(boolean maxValueExcluded) {
		this.maxValueExcluded = maxValueExcluded;
	}

	public boolean isMinValueExcluded() {
		return minValueExcluded;
	}

	public void setMinValueExcluded(boolean minValueExcluded) {
		this.minValueExcluded = minValueExcluded;
	}

}
