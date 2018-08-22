/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.server.metadata.rpc;

import org.iplass.adminconsole.shared.metadata.dto.MetaVersionCheckException;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.definition.DefinitionEntry;
import org.iplass.mtp.definition.DefinitionManager;

public class MetaDataVersionCheckUtil {

	public static DefinitionEntry versionCheck(boolean checkVersion, @SuppressWarnings("rawtypes") Class type, String defName, int currentVersion) {
		DefinitionManager dm = ManagerLocator.getInstance().getManager(DefinitionManager.class);

		// バージョンの最新チェック
		if (checkVersion) {
			@SuppressWarnings("unchecked")
			DefinitionEntry entry = dm.getDefinitionEntry(type, defName);
			int latestVersion = entry.getDefinitionInfo().getVersion();
			if (latestVersion != currentVersion) {
				MetaVersionCheckException exception = new MetaVersionCheckException("Does not match the latest version.");
				exception.setTargetVersion(currentVersion);
				exception.setLatestVersion(latestVersion);
				exception.setLatestShared(entry.getDefinitionInfo().isShared());
				exception.setLatestSharedOverwrite(entry.getDefinitionInfo().isSharedOverwrite());
				throw exception;
			}
			return entry;
		}
		return null;
	}
}
