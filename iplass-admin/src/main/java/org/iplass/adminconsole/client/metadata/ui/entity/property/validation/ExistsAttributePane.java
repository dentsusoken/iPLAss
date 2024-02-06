/*
 * Copyright (C) 2020 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.entity.property.validation;

import org.iplass.adminconsole.client.metadata.ui.entity.property.ValidationListGridRecord;
import org.iplass.adminconsole.client.metadata.ui.entity.property.ValidationListGridRecord.ValidationType;

public class ExistsAttributePane extends ValidationAttributePane {

	public ExistsAttributePane() {
	}

	@Override
	public void setDefinition(ValidationListGridRecord record) {
	}

	@Override
	public ValidationListGridRecord getEditDefinition(ValidationListGridRecord record) {
		return record;
	}

	@Override
	public boolean validate() {
		return true;
	}

	@Override
	public void clearErrors() {
	}

	@Override
	public ValidationType getType() {
		return ValidationType.EXISTS;
	}

	@Override
	public int panelHeight() {
		return 5;
	}

}
