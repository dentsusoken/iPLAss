/*
 * Copyright (C) 2022 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.web.fileupload;

import java.util.regex.Pattern;

public class MagicByteRuleCondition {

	private boolean useRegex;
	private String value;
	private Pattern pattern;

	public boolean isUseRegex() {
		return useRegex;
	}
	
	public void setUseRegex(boolean useRegex) {
		this.useRegex = useRegex;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
		if(useRegex) {
			this.pattern = Pattern.compile(value);
		}
	}
	
	public void compilePattern() {
		if(isUseRegex()) {
			this.pattern = Pattern.compile(value);
		}
	}

	public boolean match(String inputValue) {
		if(isUseRegex()) {
			return this.pattern == null || this.pattern.matcher(inputValue).matches();
		}
		return value.equals(inputValue);
	}
}
