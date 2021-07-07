/*
 * Copyright (C) 2021 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
 * ICU4JのTransliteratorを利用して変換を行うNormalizer定義です。
 * <%} else {%>
 * Normalizer definition that uses ICU4J's Transliterator to perform the transformation.
 * <%}%>
 * 
 * @author K.Higuchi
 *
 */
public class ICUTransliterator extends NormalizerDefinition {
	private static final long serialVersionUID = -1117304802663374067L;

	private String transliteratorId;

	public ICUTransliterator() {
	}

	public ICUTransliterator(String transliteratorId) {
		this.transliteratorId = transliteratorId;
	}

	public String getTransliteratorId() {
		return transliteratorId;
	}

	public void setTransliteratorId(String transliteratorId) {
		this.transliteratorId = transliteratorId;
	}
}
