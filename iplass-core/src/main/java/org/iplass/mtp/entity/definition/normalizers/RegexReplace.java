/*
 * Copyright (C) 2021 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.entity.definition.normalizers;

import org.iplass.mtp.entity.definition.NormalizerDefinition;

/**
 * <% if (doclang == "ja") {%>
 * 正規表現による置換を行うNormalizer定義です。
 * <%} else {%>
 * Normalizer definition that replaces with a regular expression.
 * <%}%>
 * 
 * @author K.Higuchi
 *
 */
public class RegexReplace extends NormalizerDefinition {
	private static final long serialVersionUID = -394476100815750656L;

	private String regex;
	private String replacement;
	
	public RegexReplace() {
	}

	public RegexReplace(String regex, String replacement) {
		this.regex = regex;
		this.replacement = replacement;
	}

	public String getRegex() {
		return regex;
	}
	public void setRegex(String regex) {
		this.regex = regex;
	}
	public String getReplacement() {
		return replacement;
	}
	public void setReplacement(String replacement) {
		this.replacement = replacement;
	}

}
