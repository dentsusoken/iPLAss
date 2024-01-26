/*
 * Copyright (C) 2016 DENTSU SOKEN INC. All Rights Reserved.
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
 * バイナリ（BinaryReference）サイズのValidation。
 * バイナリサイズでチェック。
 *
 * @author K.Higuchi
 *
 */
public class BinarySizeValidation extends ValidationDefinition {
	private static final long serialVersionUID = 5949696020925018911L;

	private Long max;
	private Long min;

	public BinarySizeValidation() {
	}

	public BinarySizeValidation(Long max, String errorMessage) {
		this(0L, max, errorMessage, null);
	}

	public BinarySizeValidation(Long max, String errorMessage, String errorCode) {
		this(0L, max, errorMessage, errorCode);
	}

	public BinarySizeValidation(Long min, Long max, String errorMessage) {
		this(min, max, errorMessage, null);
	}

	public BinarySizeValidation(Long min, Long max, String errorMessage, String errorCode) {
		setErrorMessage(errorMessage);
		setErrorCode(errorCode);
		this.min = min;
		this.max = max;
	}
	
	public Long getMax() {
		return max;
	}
	public void setMax(Long max) {
		this.max = max;
	}
	public Long getMin() {
		return min;
	}
	public void setMin(Long min) {
		this.min = min;
	}

}
