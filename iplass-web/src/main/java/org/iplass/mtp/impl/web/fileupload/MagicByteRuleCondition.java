/*
 * Copyright (C) 2022 DENTSU SOKEN INC. All Rights Reserved.
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
	private String pattern;
	private Pattern patternCompile;

	public boolean isUseRegex() {
		return useRegex;
	}
	
	public void setUseRegex(boolean useRegex) {
		this.useRegex = useRegex;
	}
	
	public String getPattern() {
		return pattern;
	}
	
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	
	public void compilePattern() {
		if(isUseRegex()) {
			this.patternCompile = Pattern.compile(this.pattern);
		}
	}

	public boolean match(String inputValue) {
		if(isUseRegex()) {
			return this.patternCompile == null || this.patternCompile.matcher(inputValue).matches();
		}
		return this.pattern.equals(inputValue);
	}
}
