/*
 * Copyright (C) 2016 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.metadata.binary;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.spi.ServiceConfigrationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * バイナリ形式のメタデータをbyte[]でメモリ内に保持しない仕組みを提供する為のService。
 * 
 * @author K.Higuchi
 *
 */
public class BinaryMetaDataService implements Service {
	
	private static Logger logger = LoggerFactory.getLogger(BinaryMetaDataService.class);
	
	private int keepInMemoryThreshold = 64 * 1024;
	private int cacheMemoryThreshold = 1024 * 1024;
	private String tempFileDir;
	
	private int xmlBinaryChunkSize = 256 * 1024;
	
	private Path tempFileDirPath;
	
	private boolean deleteTempFileOnDestroy = true;
	
	private volatile boolean cleanedup;

	public int getXmlBinaryChunkSize() {
		return xmlBinaryChunkSize;
	}
	public int getKeepInMemoryThreshold() {
		return keepInMemoryThreshold;
	}
	public int getCacheMemoryThreshold() {
		return cacheMemoryThreshold;
	}
	public Path getTempFileDir() {
		return tempFileDirPath;
	}
	
	@Override
	public void init(Config config) {
		if (config.getValue("keepInMemoryThreshold") != null) {
			keepInMemoryThreshold = Integer.parseInt(config.getValue("keepInMemoryThreshold"));
		}
		if (config.getValue("cacheMemoryThreshold") != null) {
			cacheMemoryThreshold = Integer.parseInt(config.getValue("cacheMemoryThreshold"));
		}
		if (config.getValue("xmlBinaryChunkSize") != null) {
			xmlBinaryChunkSize = Integer.parseInt(config.getValue("xmlBinaryChunkSize"));
		}
		if (config.getValue("deleteTempFileOnDestroy") != null) {
			deleteTempFileOnDestroy = Boolean.valueOf(config.getValue("deleteTempFileOnDestroy"));
		}
		
		tempFileDir = config.getValue("tempFileDir");
		if (tempFileDir == null) {
			try {
				tempFileDirPath = Files.createTempDirectory("bmd_");
			} catch (IOException e) {
				throw new ServiceConfigrationException("can't create temporary file dir on default temp dir", e);
			}
		} else {
			try {
				tempFileDirPath = Files.createTempDirectory(FileSystems.getDefault().getPath(tempFileDir), "bmd_");
			} catch (IOException e) {
				throw new ServiceConfigrationException("can't create temporary file dir on " + tempFileDir, e);
			}
		}
		
		logger.info("create temporary dir for binary metadata:" + tempFileDirPath);
		
		if (deleteTempFileOnDestroy) {
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				if (!cleanedup) {
					synchronized (BinaryMetaDataService.this) {
						if (!cleanedup) {
							cleanupTempDir();
						}
					}
				}
			}));
		}
		
	}
	
	private void cleanupTempDir() {
		if (!cleanedup) {
			synchronized (this) {
				if (!cleanedup) {
					try {
						FileUtils.deleteDirectory(tempFileDirPath.toFile());
					} catch (IOException e) {
						logger.warn("can't delete temporary dir for binary metadata:" + tempFileDirPath);
					}
					cleanedup = true;
				}
			}
		}
	}
	
	@Override
	public void destroy() {
		if (deleteTempFileOnDestroy) {
			cleanupTempDir();
		}
	}
	
}
