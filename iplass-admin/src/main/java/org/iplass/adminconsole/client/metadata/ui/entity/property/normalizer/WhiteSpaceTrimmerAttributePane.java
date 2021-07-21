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

package org.iplass.adminconsole.client.metadata.ui.entity.property.normalizer;

import org.iplass.mtp.entity.definition.NormalizerDefinition;
import org.iplass.mtp.entity.definition.normalizers.WhiteSpaceTrimmer;

public class WhiteSpaceTrimmerAttributePane extends NormalizerAttributePane {

	public WhiteSpaceTrimmerAttributePane() {
	}

	@Override
	public void setDefinition(NormalizerDefinition definition) {
	}

	@Override
	public NormalizerDefinition getEditDefinition(NormalizerDefinition definition) {
		return new WhiteSpaceTrimmer();
	}

	@Override
	public boolean validate() {
		return true;
	}

	@Override
	public void clearErrors() {
	}

	@Override
	public int panelHeight() {
		return 5;
	}

}
