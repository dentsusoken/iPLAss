/*
 * Copyright (C) 2024 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.gem.command.generic.search.fileport;

import org.iplass.gem.command.generic.search.EntityFileDownloadSearchContext;
import org.iplass.gem.command.generic.search.SearchContextBase;
import org.iplass.mtp.web.ResultStreamWriter;

public class ExcelFileDownloadSearchContext extends EntityFileDownloadSearchContext {

	public ExcelFileDownloadSearchContext(SearchContextBase context) {
		super(context);
	}

	@Override
	public String getFileContentType() {
		return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	}

	@Override
	public String getFileExtension() {
		return ".xlsx";
	}

	@Override
	public ResultStreamWriter createWriter() {
		ResultStreamWriter writer = null;
		if (isForUpload()) {
			writer = new ExcelFileDownloadUploadableWriter(this);
		} else {
			writer = new ExcelFileDownloadSearchViewWriter(this);
		}
		return writer;
	}

}
