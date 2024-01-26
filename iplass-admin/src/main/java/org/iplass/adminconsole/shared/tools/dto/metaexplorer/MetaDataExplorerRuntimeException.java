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

package org.iplass.adminconsole.shared.tools.dto.metaexplorer;

public class MetaDataExplorerRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 8118623547082185132L;

	public MetaDataExplorerRuntimeException() {
		super();
	}

	public MetaDataExplorerRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public MetaDataExplorerRuntimeException(String message) {
		super(message);
	}

	public MetaDataExplorerRuntimeException(Throwable cause) {
		super(cause);
	}


}
