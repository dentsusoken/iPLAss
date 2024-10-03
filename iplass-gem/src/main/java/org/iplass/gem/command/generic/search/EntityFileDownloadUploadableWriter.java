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

package org.iplass.gem.command.generic.search;

import org.iplass.gem.GemConfigService;
import org.iplass.mtp.impl.fileport.EntityFileUploadService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.web.ResultStreamWriter;

/**
 * <p>Entityのファイル出力クラス。Upload可能な形式で出力を行います。</p>
 */
public abstract class EntityFileDownloadUploadableWriter implements ResultStreamWriter {

	protected final EntityFileDownloadSearchContext context;

	protected final GemConfigService gcs;
	protected final EntityFileUploadService efus;

	public EntityFileDownloadUploadableWriter(final EntityFileDownloadSearchContext context) {
		this.context = context;

		gcs = ServiceRegistry.getRegistry().getService(GemConfigService.class);
		efus = ServiceRegistry.getRegistry().getService(EntityFileUploadService.class);
	}

}
