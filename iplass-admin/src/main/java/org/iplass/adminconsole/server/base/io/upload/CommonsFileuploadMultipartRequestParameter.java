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
import java.nio.charset.Charset;

import org.apache.commons.fileupload.FileItem;

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
	/** FileItem インスタンス */
	private FileItem delegate;

	public CommonsFileuploadMultipartRequestParameter(FileItem fileItem) {
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
		//
		return delegate.getString();
	}

	@Override
	public String getString(Charset charset) {
		// org.apache.commons.fileupload.disk.DiskFileItem#getString(String charset) を参照
		return new String(delegate.get(), charset);
	}

	@Override
	public String getName() {
		return delegate.getName();
	}

	@Override
	public void dispose() {
		delegate.delete();
	}
}
