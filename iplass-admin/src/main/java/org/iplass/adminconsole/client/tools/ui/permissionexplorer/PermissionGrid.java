/*
 * Copyright (C) 2015 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.tools.ui.permissionexplorer;

public interface PermissionGrid {

	static final String CELL_STYLE_DEFAULT = "permissionExplorerGridRow";
	static final String CELL_STYLE_CONFIGURED = "permissionExplorerGridConfiguredRow";
	static final String CELL_STYLE_EDITING = "permissionExplorerGridEditingRow";
	static final String CELL_STYLE_DELETING = "permissionExplorerGridDeletingRow";

	static final String CELL_STYLE_ROLE_EDITING = "permissionExplorerRoleGridEditingRow";
	static final String CELL_STYLE_ROLE_DELETING = "permissionExplorerRoleGridDeletingRow";

}
