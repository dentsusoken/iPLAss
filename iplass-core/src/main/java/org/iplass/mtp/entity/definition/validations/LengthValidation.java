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
 * 文字列の長さをチェックするValidation。
 * 文字列長が、min以上、max以下ではない場合、エラー。
 *
 * @author K.Higuchi
 *
 */
public class LengthValidation extends ValidationDefinition {
	private static final long serialVersionUID = 5741933609616633482L;

	private Integer max;
	private Integer min;

	private boolean checkBytes;
	//Count surrogate pair characters as one character
	private boolean surrogatePairAsOneChar;

	public LengthValidation() {
	}

	public LengthValidation(Integer max, String errorMessage) {
		this(0, max, errorMessage, null);
	}

	public LengthValidation(Integer max, String errorMessage, String errorCode) {
		this(0, max, errorMessage, errorCode);
	}

	public LengthValidation(Integer min, Integer max, String errorMessage) {
		this(min, max, errorMessage, null);
	}

	public LengthValidation(Integer min, Integer max, String errorMessage, String errorCode) {
		setErrorMessage(errorMessage);
		setErrorCode(errorCode);
		this.min = min;
		this.max = max;
	}

	/**
	 * サロゲートペア文字を1文字としてカウントするか否か。
	 * デフォルト値はtrue。
	 * バイト数でカウントする場合は、この設定は意味を成さない。
	 * 
	 * @return
	 */
	public boolean isSurrogatePairAsOneChar() {
		return surrogatePairAsOneChar;
	}

	/**
	 * サロゲートペア文字を1文字としてカウントする場合はtrueをセット。
	 * 
	 * 未設定の場合のデフォルト値はtrue。
	 * 
	 * @param surrogatePairAsOneChar
	 */
	public void setSurrogatePairAsOneChar(boolean surrogatePairAsOneChar) {
		this.surrogatePairAsOneChar = surrogatePairAsOneChar;
	}

	public boolean isCheckBytes() {
		return checkBytes;
	}

	/**
	 * バイト数でチェックしたい場合、trueをセット
	 *
	 * @param checkBytes
	 */
	public void setCheckBytes(boolean checkBytes) {
		this.checkBytes = checkBytes;
	}

	public Integer getMax() {
		return max;
	}
	public void setMax(Integer max) {
		this.max = max;
	}
	public Integer getMin() {
		return min;
	}
	public void setMin(Integer min) {
		this.min = min;
	}

}
