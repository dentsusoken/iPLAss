/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.lob.lobstore.file;

import java.io.File;

import org.iplass.mtp.impl.lob.LobStoreService;
import org.iplass.mtp.impl.lob.lobstore.LobData;
import org.iplass.mtp.impl.lob.lobstore.LobStore;
import org.iplass.mtp.impl.lob.lobstore.LobValidator;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapterService;
import org.iplass.mtp.impl.transaction.TransactionService;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceConfigrationException;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.transaction.TransactionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileLobStore implements LobStore {

	//FIXME データの圧縮対応（google/snappy-javaとか）

	private static final Logger logger = LoggerFactory.getLogger(FileLobStore.class);

	private RdbAdapter rdb;

	private String rootDir;
	private boolean overwriteFile = false;
	private boolean manageLobSizeOnRdb;

	private LobValidator lobValidator;

	@Override
	public LobValidator getLobValidator() {
		return lobValidator;
	}

	public void setLobValidator(LobValidator lobValidator) {
		this.lobValidator = lobValidator;
	}

	public boolean isOverwriteFile() {
		return overwriteFile;
	}

	public void setOverwriteFile(boolean overwriteFile) {
		this.overwriteFile = overwriteFile;
	}

	public String getRootDir() {
		return rootDir;
	}

	public void setRootDir(String rootDir) {
		this.rootDir = rootDir;
	}

	@Override
	public void inited(LobStoreService service, Config config) {
		if (rootDir == null) {
			throw new ServiceConfigrationException("rootDir is undefined at FileLobStoreService");
		}
		rdb = config.getDependentService(RdbAdapterService.class).getRdbAdapter();
		manageLobSizeOnRdb = service.isManageLobSizeOnRdb();
	}

	@Override
	public void destroyed() {
	}

	@Override
	public LobData create(int tenantId, long lobDataId) {
		return new FileLobData(tenantId, lobDataId, rootDir, overwriteFile, manageLobSizeOnRdb, rdb);
	}

	@Override
	public LobData load(int tenantId, long lobDataId) {
		return new FileLobData(tenantId, lobDataId, rootDir, overwriteFile, manageLobSizeOnRdb, rdb);
	}

	@Override
	public void remove(final int tenantId, final long lobDataId) {
		Transaction t = ServiceRegistry.getRegistry().getService(TransactionService.class).getTransacitonManager().currentTransaction();
		if (t != null && t.getStatus() == TransactionStatus.ACTIVE) {
			t.afterCommit(() -> {
					FileLobData data = new FileLobData(tenantId, lobDataId, rootDir, overwriteFile, manageLobSizeOnRdb, rdb);
					File f = data.toFile(lobDataId);
					if (f.exists()) {
						if (!f.delete()) {
							logger.warn("maybe can not delete file:" + f.getAbsolutePath());
						}
					}
			});
		} else {
			FileLobData data = new FileLobData(tenantId, lobDataId, rootDir, overwriteFile, manageLobSizeOnRdb, rdb);
			File f = data.toFile(lobDataId);
			if (f.exists()) {
				if (!f.delete()) {
					logger.warn("maybe can not delete file:" + f.getAbsolutePath());
				}
			}
		}
	}

}
