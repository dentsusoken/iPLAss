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

package org.iplass.mtp.impl.web.fileupload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.servlet.ServletContext;

import org.apache.commons.fileupload.util.Streams;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.UploadFileHandle;
import org.iplass.mtp.command.UploadFileSizeOverException;
import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.impl.util.UploadFileUtil;
import org.iplass.mtp.impl.web.WebFrontendService;
import org.iplass.mtp.impl.web.WebProcessRuntimeException;
import org.iplass.mtp.impl.web.WebResourceBundleUtil;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UploadFileHandleImpl implements UploadFileHandle {

	private static final Logger logger = LoggerFactory.getLogger(UploadFileHandleImpl.class);
	private static WebFrontendService webFront = ServiceRegistry.getRegistry().getService(WebFrontendService.class);

	public static UploadFileHandleImpl toUploadFileHandle(InputStream is, String fileName, String type, ServletContext servletContext) throws IOException {
		try {
			UploadFileHandleImpl value = null;
			
			File tempDir = null;
			if (webFront.getTempFileDir() == null) {
				tempDir = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
			} else {
				tempDir = new File(webFront.getTempFileDir());
			}
			long maxSize = webFront.getMaxUploadFileSize();
			
	    	fileName = delPath(fileName);
			if (fileName != null && fileName.length() != 0) {
				String ext = ".tmp";
				File tempFile = null;
				long size = 0;
				try {
					tempFile = File.createTempFile("tmp", ext, tempDir);
					try (FileOutputStream fos = new FileOutputStream(tempFile)) {
						size = Streams.copy(is, fos, true);
					}
				} catch (RuntimeException | IOException e) {
					if (tempFile != null) {
						tempFile.delete();
					}
					throw e;
				}
				
				// tempFileのウィルスチェック
				FileScanner scanHandle = webFront.getUploadFileScanner();
				
				if (scanHandle != null) {
					scanHandle.scan(tempFile.getAbsolutePath());
					// ウィルスに感染していた場合はファイルを削除する設定になっている事が前提
					if (!tempFile.exists()) {
						throw new WebProcessRuntimeException(fileName + " is a file infected with the virus.");
					}
				}
		
				if (maxSize != -1 && size > maxSize) {
					value = new UploadFileHandleImpl(tempFile, fileName, type, size, true);
				} else {
					value = new UploadFileHandleImpl(tempFile, fileName, type, size, false);
				}
			}
			return value;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					logger.warn("can not close resource:" + fileName + ", mybe leak...", e);
				}
			}
		}
	}
	
	private static String delPath(String fileName) {
		if (fileName.contains("\\")) {
			fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
		}
		if (fileName.contains("/")) {
			fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
		}
		//TODO その他チェックしなければならないものは？
		if (fileName.contains("\n") || fileName.contains("\r") || fileName.contains("\0")) {
			throw new RuntimeException("contains \\n,\\r,\\0");
		}
		return fileName;
	}

	private File tempFile;
	private String fileName;
	private String type;
	private long size;

	private boolean isSizeOver;
	
	UploadFileHandleImpl(File tempFile, String fileName, String type, long size, boolean isSizeOver) {
		this.tempFile = tempFile;
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
			UploadFileUtil.checkMagicByte(tempFile, type, fileName);
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

	//TODO バイナリストリームはパブリッククラウドの場合は、使わせないほうがよいか？
	@Override
	public InputStream getInputStream() {
		if (isSizeOver) {
			throw new UploadFileSizeOverException(resourceString("impl.web.fileupload.UploadFileHandleImpl.maxFileSize"));
		}

		// マジックバイトチェックを実施
		if (webFront.isExecMagicByteCheck()) {
			UploadFileUtil.checkMagicByte(tempFile, type, fileName);
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
			UploadFileUtil.checkMagicByte(tempFile, type, fileName);
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
			UploadFileUtil.checkMagicByte(tempFile, type, fileName);
		}

		try {
			return Files.move(tempFile.toPath(), target, options);
		} catch (IOException e) {
			throw new WebProcessRuntimeException(e);
		}
	}
	
	public void deleteTempFile() {
		if (tempFile != null) {
			if (!tempFile.delete()) {
				logger.warn("maybe not delete file:" + tempFile.getName());
			}
		}
	}

	@Override
	public String toString() {
		return fileName;
	}

	private static String resourceString(String key, Object... arguments) {
		return WebResourceBundleUtil.resourceString(key, arguments);
	}
}
