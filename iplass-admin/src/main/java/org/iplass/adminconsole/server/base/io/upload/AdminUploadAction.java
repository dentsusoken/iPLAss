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

package org.iplass.adminconsole.server.base.io.upload;

import static gwtupload.shared.UConsts.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.iplass.adminconsole.server.base.service.AdminConsoleService;
import org.iplass.mtp.impl.web.WebFrontendService;
import org.iplass.mtp.spi.ServiceRegistry;

import gwtupload.server.AbstractUploadListener;

/**
 * gwt-uploadのUploadActionで解決できない問題を解決する。
 *
 * <ul>
 * <li>
 * commons-fileupload の ServletFileUpload インスタンスに、最大ファイル数（fileCountMax）を設定する。
 * </li>
 * </ul>
 *
 *
 * @author SEKIGUCHI Naoya
 */
public abstract class AdminUploadAction extends UploadActionWithoutSession {

	private static final long serialVersionUID = -5553465242497700877L;

	/** 最大パラメータ(ファイル）数 */
	protected long maxParameterCount;

	private File contextTempDir;

	private AdminConsoleService acs = ServiceRegistry.getRegistry().getService(AdminConsoleService.class);

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		WebFrontendService webFront = ServiceRegistry.getRegistry().getService(WebFrontendService.class);
		// マルチパートリクエストの最大パラメータ数を設定する。
		maxParameterCount = webFront.getMaxMultipartParameterCount();

		contextTempDir = (File)config.getServletContext().getAttribute("javax.servlet.context.tempdir");

		//サイズ制限についてパラメータで指定されていない場合、Serviceの設定値をセット
		if (getInitParameter("maxSize") == null) {
			maxSize = acs.getMaxUploadFileSize();
		}
		if (getInitParameter("maxFileSize") == null) {
			maxFileSize = acs.getMaxUploadFileSize();
		}
	}

	/**
	 * @return contextTempDir
	 */
	protected File getContextTempDir() {
		return contextTempDir;
	}

	protected final byte[] convertFileToByte(File file) {
		byte[] bytes = null;
		FileInputStream is = null;
		FileChannel channel = null;
		try {
			is = new FileInputStream(file);
			channel = is.getChannel();
			ByteBuffer buffer = ByteBuffer.allocate((int)channel.size());
			channel.read(buffer);
			buffer.clear();
			bytes = new byte[buffer.capacity()];
			buffer.get(bytes);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (channel != null) {
				try {
					channel.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}

		return bytes;
	}

	@Override
	protected FileItemFactory getFileItemFactory(long requestSize) {
		return new AdminFileItemFactory();
	}

	@Override
	protected ServletFileUpload getServletFileUpload(FileItemFactory factory, AbstractUploadListener listener) {
		ServletFileUpload uploader = super.getServletFileUpload(factory, listener);
		// EDIT - fileCountMax を設定する
		uploader.setFileCountMax(maxParameterCount);

		return uploader;
	}

	/**
	 * <p>FileItem生成用クラス</p>
	 *
	 * <p>
	 * 標準の gwtupload.server.UploadServlet#DefaultFileItemFactory では、
	 * 無条件でfieldNameにカウントが付いてしまうため、対応。
	 * </p>
	 */
	public static class AdminFileItemFactory extends DiskFileItemFactory {
		private HashMap<String, Integer> map = new HashMap<String, Integer>();

		@Override
		public FileItem createItem(String fieldName, String contentType, boolean isFormField, String fileName) {
			Integer cont = map.get(fieldName) != null ? (map.get(fieldName) + 1) : 0;
			map.put(fieldName, cont);

			// 複数の場合のみ、後ろにCountを付加
			if (cont > 0 || fieldName.contains(MULTI_SUFFIX)) {
				fieldName = fieldName.replace(MULTI_SUFFIX, "") + "-" + cont;
			}

			return super.createItem(fieldName, contentType, isFormField, fileName);
		}
	}

}
