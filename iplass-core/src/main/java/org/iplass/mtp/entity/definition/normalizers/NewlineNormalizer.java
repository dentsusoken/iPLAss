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
 * 改行コードのNormalizer定義です。
 * <%} else {%>
 * Normalizer definition of the newline character.
 * <%}%>
 * 
 * @author K.Higuchi
 *
 */
public class NewlineNormalizer extends NormalizerDefinition {
	private static final long serialVersionUID = -4056785014337074495L;

	private NewlineType type;

	public NewlineNormalizer() {
	}

	public NewlineNormalizer(NewlineType type) {
		this.type = type;
	}

	public NewlineType getType() {
		return type;
	}

	public void setType(NewlineType type) {
		this.type = type;
	}
}
