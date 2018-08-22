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

package org.iplass.adminconsole.server.base.io.upload;

import static gwtupload.shared.UConsts.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import gwtupload.server.HasKey;
import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;

/**
 * gwt-uploadのUploadActionでは、レスポンスに返すSummary情報を生成する際に、
 * 文字コードを指定していないのでマルチバイト文字が化けるため、それを回避する。
 */
public class AdminUploadAction extends UploadAction {

	private static final long serialVersionUID = -5553465242497700877L;

	@Override
	protected Map<String, String> getFileItemsSummary(
			HttpServletRequest request, Map<String, String> stat) {
		if (stat == null) {
			stat = new HashMap<String, String>();
		}
		List<FileItem> s = getMyLastReceivedFileItems(request);
		if (s != null) {
			String files = "";
			String params = "";
			for (FileItem i : s) {
				if (i.isFormField()) {
					params += formFieldToXml(i);
				} else {
					files += fileFieldToXml(i);
				}
			}
			stat.put(TAG_FILES, files);
			stat.put(TAG_PARAMS, "<![CDATA[" + params + "]]>");
			stat.put(TAG_FINISHED, "ok");
		}
		return stat;
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

	private String formFieldToXml(FileItem i) {
		Map<String, String> item = new HashMap<String, String>();

		//カスタマイズ START
		//item.put(TAG_VALUE, "" + i.getString());
		try {
			item.put(TAG_VALUE, "" + i.getString("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new UploadActionException(e);
		}
		//カスタマイズ END

		item.put(TAG_FIELD, "" + i.getFieldName());

		Map<String, String> param = new HashMap<String, String>();
		param.put(TAG_PARAM, statusToString(item));
		return statusToString(param);
	}

	private String fileFieldToXml(FileItem i) {
		Map<String, String> item = new HashMap<String, String>();
		item.put(TAG_CTYPE, i.getContentType() != null ? i.getContentType()
				: "unknown");
		item.put(TAG_SIZE, "" + i.getSize());
		item.put(TAG_NAME, "" + i.getName());
		item.put(TAG_FIELD, "" + i.getFieldName());
		if (i instanceof HasKey) {
			String k = ((HasKey) i).getKeyString();
			item.put(TAG_KEY, k);
		}

		Map<String, String> file = new HashMap<String, String>();
		file.put(TAG_FILE, statusToString(item));
		return statusToString(file);
	}


	@Override
	protected FileItemFactory getFileItemFactory(long requestSize) {
		return new AdminFileItemFactory();
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
