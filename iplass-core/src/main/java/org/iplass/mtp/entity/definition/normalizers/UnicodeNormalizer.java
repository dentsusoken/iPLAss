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
 * Unicode正規化するNormalizer定義です。
 * <%} else {%>
 * Normalizer definition for Unicode normalization.
 * <%}%>
 * 
 * @author K.Higuchi
 *
 */
public class UnicodeNormalizer extends NormalizerDefinition {
	private static final long serialVersionUID = 2063092268493173467L;

	private String form;
	
	public UnicodeNormalizer() {
	}

	public UnicodeNormalizer(String form) {
		this.form = form;
	}
	
	public String getForm() {
		return form;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * NFC/NFD/NFKC/NFKDのいずれかをセット可能。
	 * <%} else {%>
	 * Either NFC/NFD/NFKC/NFKD can be set.
	 * <%}%>
	 * 
	 * @param form
	 */
	public void setForm(String form) {
		this.form = form;
	}

}
