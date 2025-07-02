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
package org.iplass.adminconsole.server.base.io.upload;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;

import org.apache.commons.fileupload2.core.FileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CommonsFileupload FileItem 用 マルチパートリクエストパラメータ
 *
 * <p>
 * 本クラスでは各メソッドの機能は、FileItem に委譲する。
 * </p>
 *
 * @author SEKIGUCHI Naoya
 */
class CommonsFileuploadMultipartRequestParameter implements MultipartRequestParameter {
	/** logger */
	private Logger logger = LoggerFactory.getLogger(CommonsFileuploadMultipartRequestParameter.class);
	/** FileItem インスタンス */
	private FileItem<?> delegate;

	/**
	 * コンストラクタ
	 * @param fileItem FileItem インスタンス
	 */
	public CommonsFileuploadMultipartRequestParameter(FileItem<?> fileItem) {
		this.delegate = fileItem;
	}

	@Override
	public boolean isFormField() {
		return delegate.isFormField();
	}

	@Override
	public String getFieldName() {
		return delegate.getFieldName();
	}

	@Override
	public String getContentType() {
		return delegate.getContentType();
	}

	@Override
	public long getSize() {
		return delegate.getSize();
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return delegate.getInputStream();
	}

	@Override
	public String getString() {
		try {
			return delegate.getString();
		} catch (IOException e) {
			throw new UncheckedIOException("Failed to get string from FileItem.", e);
		}
	}

	@Override
	public String getString(Charset charset) {
		// org.apache.commons.fileupload.disk.DiskFileItem#getString(String charset) を参照
		try {
			return new String(delegate.get(), charset);
		} catch (IOException e) {
			throw new UncheckedIOException("Failed to get string from FileItem with charset.", e);
		}
	}

	@Override
	public String getName() {
		return delegate.getName();
	}

	@Override
	public void dispose() {
		try {
			delegate.delete();
		} catch (IOException e) {
			logger.error("failed FileItem#delete.", e);
		}
	}
}
