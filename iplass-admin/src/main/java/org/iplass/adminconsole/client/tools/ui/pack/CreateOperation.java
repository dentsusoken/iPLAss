/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.tools.ui.pack;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.tools.ui.pack.operation.CreatePackagePane;
import org.iplass.adminconsole.client.tools.ui.pack.operation.EntitySelectPane;
import org.iplass.adminconsole.client.tools.ui.pack.operation.MetaDataSelectPane;

import com.smartgwt.client.widgets.Canvas;

public enum CreateOperation {

//	SelectMetaData("1.Select MetaData", "Please choose the target metadata."),
//	SelectEntity("2.Select Entity", "Please choose the target entity."),
//	ExecutePack("3.Execute Pack", "Execute package creation.");
	SELECTMETADATA("1.Select MetaData", AdminClientMessageUtil.getString("ui_tools_pack_CreateOperation_selectMetaDataIncludePackage")),
	SELECTENTITY("2.Select Entity", AdminClientMessageUtil.getString("ui_tools_pack_CreateOperation_selectEntityDataIncludePackage")),
	EXECUTEPACK("3.Execute Pack", AdminClientMessageUtil.getString("ui_tools_pack_CreateOperation_setPackageInfo"));

	private String displayName;

	private String description;

	private CreateOperation(String displayName, String description) {
		this.displayName = displayName;
		this.description = description;
	}

	public String displayName() {
		return displayName;
	}

	public String description() {
		return description;
	}

	public Canvas getOperationPane(PackageCreateDialog owner) {
		Canvas canvas = null;
		if (this == SELECTMETADATA) {
			canvas = new MetaDataSelectPane(owner);
		} else if (this == SELECTENTITY) {
			canvas = new EntitySelectPane(owner);
		} else if (this == EXECUTEPACK) {
			canvas = new CreatePackagePane(owner);
		}
		return canvas;
	}

}
