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

package org.iplass.adminconsole.shared.metadata.dto.template;

import org.iplass.adminconsole.shared.base.dto.io.download.DownloadProperty;

public interface BinaryTemplateDownloadProperty extends DownloadProperty {

	public static final String ACTION_URL = "service/binarytemplatedownload";

	public static final String DEFINITION_NAME = "defName";

	public static final String LANG = "language";

	public static final String CONTENT_DISPOSITION_TYPE = "contentDispositionType";

}
