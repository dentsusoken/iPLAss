/*
 * Copyright (C) 2016 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.UploadFileHandle;
import org.iplass.mtp.command.UploadFileSizeOverException;
import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.impl.web.WebFrontendService;
import org.iplass.mtp.impl.web.WebProcessRuntimeException;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * テスト時に利用可能なUploadFileHandleの実装です。
 * アップロードファイルを伴うテストの場合に、 {@link TestRequestContext#setParam(String, Object)}にてセットします。
 *
 * @author K.Higuchi
 *
 */
public class TestUploadFileHandle implements UploadFileHandle {
	private static final Logger logger = LoggerFactory.getLogger(TestUploadFileHandle.class);

	private WebFrontendService webFront = ServiceRegistry.getRegistry().getService(WebFrontendService.class);

	private File tempFile;
	private String fileName;
	private String type;
	private long size;

	private boolean isSizeOver;

	/**
	 * 指定のtempFileで指定されるファイルにてTestUploadFileHandleを生成します。
	 *
	 * @param tempFile
	 * @param fileName
	 * @param type
	 */
	public TestUploadFileHandle(File tempFile, String fileName, String type) {
		this.tempFile = tempFile;
		this.fileName = fileName;
		this.type = type;
		this.size = tempFile.length();
	}

	/**
	 * アップロードされたファイルがサイズオーバーであった場合を想定したテストの場合、当コンストラクタを利用し、
	 * isSizeOverがtrueの状態のUploadFileHandleを生成することが可能です。
	 *
	 * @param fileName
	 * @param type
	 * @param size
	 * @param isSizeOver
	 */
	public TestUploadFileHandle(String fileName, String type, long size, boolean isSizeOver) {
		this.fileName = fileName;
		this.type = type;
		this.size = size;
		this.isSizeOver = isSizeOver;
	}

	@Override
	public String getFileName() {
		return fileName;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public BinaryReference toBinaryReference() {
		if (isSizeOver) {
			throw new UploadFileSizeOverException(resourceString("impl.web.fileupload.UploadFileHandleImpl.maxFileSize"));
		}

		// マジックバイトチェックを実施
		if (webFront.isExecMagicByteCheck()) {
			webFront.getMagicByteCheaker().checkMagicByte(tempFile, type, fileName);
		}

		FileInputStream is = null;
		try {
			is = new FileInputStream(tempFile);
			EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);
			return em.createBinaryReference(fileName, type, is);
		} catch (FileNotFoundException e) {
			logger.warn("upload file is externally deleted. maybe contains virus." , e);
			return null;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					logger.warn("can not close resource:" + tempFile.getName(), e);
				}
			}
		}
	}

	@Override
	public long getSize() {
		return size;
	}

	@Override
	public InputStream getInputStream() {
		if (isSizeOver) {
			throw new UploadFileSizeOverException(resourceString("impl.web.fileupload.UploadFileHandleImpl.maxFileSize"));
		}

		// マジックバイトチェックを実施
		if (webFront.isExecMagicByteCheck()) {
			webFront.getMagicByteCheaker().checkMagicByte(tempFile, type, fileName);
		}

		try {
			return new FileInputStream(tempFile);
		} catch (FileNotFoundException e) {
			logger.warn("upload file is externally deleted. maybe contains virus." , e);
			return null;
		}
	}

	@Override
	public boolean isSizeOver() {
		return isSizeOver;
	}

	@Override
	public Path copyTo(Path target, CopyOption... options) {
		if (isSizeOver) {
			throw new UploadFileSizeOverException(resourceString("impl.web.fileupload.UploadFileHandleImpl.maxFileSize"));
		}

		// マジックバイトチェックを実施
		if (webFront.isExecMagicByteCheck()) {
			webFront.getMagicByteCheaker().checkMagicByte(tempFile, type, fileName);
		}

		try {
			return Files.copy(tempFile.toPath(), target, options);
		} catch (IOException e) {
			throw new WebProcessRuntimeException(e);
		}
	}

	@Override
	public Path moveTo(Path target, CopyOption... options) {
		if (isSizeOver) {
			throw new UploadFileSizeOverException(resourceString("impl.web.fileupload.UploadFileHandleImpl.maxFileSize"));
		}

		// マジックバイトチェックを実施
		if (webFront.isExecMagicByteCheck()) {
			webFront.getMagicByteCheaker().checkMagicByte(tempFile, type, fileName);
		}

		try {
			return Files.move(tempFile.toPath(), target, options);
		} catch (IOException e) {
			throw new WebProcessRuntimeException(e);
		}

	}

	private static String resourceString(String key, Object... arguments) {
		return TestResourceBundleUtil.resourceString(key, arguments);
	}
}
