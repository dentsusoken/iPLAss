/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.entity.property;

import java.util.Map;

public interface PropertyAttributePane {

	public static final String KEYFILTER_NUM = "[0-9]";
	public static final String KEYFILTER_NUMANDASTER = "[0-9*]";

	public abstract void applyFrom(String defName, PropertyListGridRecord record, PropertyAttribute typeAttribute);

	public abstract void applyTo(PropertyListGridRecord record);

	public abstract boolean validate();

	public abstract int panelHeight();

	public interface NeedsEnableLangMap {

		void setEnableLangMap(Map<String, String> enableLangMap);
	}

	public interface HasNotNullHandler {

		void onChangeNotNullFromList(boolean isNotNull);

		boolean canEditNotNull();
	}

}
