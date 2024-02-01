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
 * 正規表現によるValidation。
 * 定義された正規表現に一致しない場合、エラーとなる。
 * 
 * @see java.util.regex.Pattern
 * 
 * @author K.Higuchi
 *
 */
public class RegexValidation extends ValidationDefinition {
	private static final long serialVersionUID = 2263904604181287841L;
	
	private String pattern;
	
	public RegexValidation() {
	}
	
	public RegexValidation(String pattern, String errorMessage) {
		this(pattern, errorMessage, null);
	}

	public RegexValidation(String pattern, String errorMessage, String errorCode) {
		super();
		this.pattern = pattern;
		setErrorMessage(errorMessage);
		setErrorCode(errorCode);
	}
	
	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

}
