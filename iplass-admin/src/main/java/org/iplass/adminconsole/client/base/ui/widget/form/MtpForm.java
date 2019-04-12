/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.base.ui.widget.form;

import org.iplass.adminconsole.client.base.ui.widget.MtpWidgetConstants;

import com.smartgwt.client.widgets.form.DynamicForm;

public class MtpForm extends DynamicForm implements MtpWidgetConstants {

	public MtpForm() {

		setWidth100();

		//間延びしないように最後に１つ余分に作成
		setNumCols(3);
		setColWidths(FORM_WIDTH_TITLE, FORM_WIDTH_ITEM, "*");

	}
}
