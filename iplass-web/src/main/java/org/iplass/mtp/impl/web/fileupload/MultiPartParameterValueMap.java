/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload2.core.FileItemInput;
import org.apache.commons.fileupload2.core.FileItemInputIterator;
import org.apache.commons.fileupload2.jakarta.servlet6.JakartaServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.iplass.mtp.command.UploadFileHandle;
import org.iplass.mtp.impl.web.ParameterValueMap;
import org.iplass.mtp.impl.web.RequestParameterCountLimitException;
import org.iplass.mtp.impl.web.WebFrontendService;
import org.iplass.mtp.impl.web.WebProcessRuntimeException;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;


public class MultiPartParameterValueMap implements ParameterValueMap {

	//一旦tempディレクトリに保存。ファイルのウイルスチェックを行う。安全なファイルのみ取り出せるように。

	private static final Logger logger = LoggerFactory.getLogger(MultiPartParameterValueMap.class);

	private List<UploadFileHandleImpl> tempFiles;
	//Webからのリクエストパラメータ
	protected Map<String, Object> valueMap;

	private boolean init;
	private ServletContext servletContext;
	private HttpServletRequest req;
	private long maxFileSize = -1;
	/**
	 * リクエストパラメータ最大数
	 * @see WebFrontendService#getMaxMultipartParameterCount()
	 */
	// NOTE 変更要素があれば getter / setter を用意し使用する。現状は固定の設定とする。
	private long maxParameterCount;

	public MultiPartParameterValueMap(ServletContext servletContext, HttpServletRequest req) {
		this.servletContext = servletContext;
		this.req = req;
		// マルチパートリクエストのリクエストパラメータ数最大数を設定する
		maxParameterCount = ServiceRegistry.getRegistry().getService(WebFrontendService.class)
				.getMaxMultipartParameterCount();

	}

	public long getMaxFileSize() {
		return maxFileSize;
	}

	public void setMaxFileSize(long maxFileSize) {
		this.maxFileSize = maxFileSize;
	}

	private void init() {
		if (init) {
			return;
		}

		init = true;

		long start = 0L;
		if (logger.isDebugEnabled()) {
			start = System.currentTimeMillis();
		}
		valueMap = new HashMap<String, Object>();

		tempFiles = new ArrayList<UploadFileHandleImpl>();

		JakartaServletFileUpload<?, ?> upload = new JakartaServletFileUpload<>();
		// Parse the request
		try {
			FileItemInputIterator iter = upload.getItemIterator(req);
			long parameterCount = 0L;
			while (iter.hasNext()) {
				if (parameterCount == maxParameterCount) {
					// パラメータ数が上限を超過する場合、例外をスローする
					throw new RequestParameterCountLimitException(
							"Multipart request parameters exceeds the limit. limit: " + maxParameterCount);
				}

				FileItemInput item = iter.next();
				String name = item.getFieldName();
				if (item.isFormField()) {
					// TODO charset
					String value = IOUtils.toString(item.getInputStream(), StandardCharsets.UTF_8);

					Object oldValue = valueMap.get(name);
					if (oldValue == null) {
						valueMap.put(name, new String[]{value});
					} else if (oldValue instanceof String[]) {
						String[] newValue = new String[((String[]) oldValue).length + 1];
						System.arraycopy(oldValue, 0, newValue, 0, newValue.length - 1);
						newValue[newValue.length - 1] = value;
						valueMap.put(name, newValue);
					} else {
						//TODO エラー？上書きしちゃう？
						throw new WebProcessRuntimeException(name + " is alerady used as file upload field name.");
					}
				} else {
					UploadFileHandleImpl value = UploadFileHandleImpl.toUploadFileHandle(item.getInputStream(), item.getName(), item.getContentType(),
							servletContext, maxFileSize);
					if (value != null) {
						tempFiles.add(value);
						Object oldValue = valueMap.get(name);
						if (oldValue == null) {
							valueMap.put(name, new UploadFileHandle[]{value});
						} else if (oldValue instanceof UploadFileHandle[]) {
							UploadFileHandle[] newValue = new UploadFileHandle[((UploadFileHandle[]) oldValue).length + 1];
							System.arraycopy(oldValue, 0, newValue, 0, newValue.length - 1);
							newValue[newValue.length - 1] = value;
							valueMap.put(name, newValue);
						} else {
							//TODO エラー？上書きしちゃう？
							throw new WebProcessRuntimeException(name + " is alerady used as form field name.");
						}
					}
				}
				// パラメータ数インクリメント
				parameterCount += 1L;
			}
		} catch (IOException e) {
			cleanTempResource();
			throw new WebProcessRuntimeException(e);
		} catch (RuntimeException | Error e) {
			cleanTempResource();
			throw e;
		}

		//変更禁止
		valueMap = Collections.unmodifiableMap(valueMap);

		if (logger.isDebugEnabled()) {
			logger.debug("MultiPartRequest parsed. time:" + (System.currentTimeMillis() - start));
		}
	}

	@Override
	public void cleanTempResource() {
		if (tempFiles != null) {
			for (UploadFileHandleImpl f: tempFiles) {
				f.deleteTempFile();
			}
		}
	}

	@Override
	public Map<String, Object> getParamMap() {
		init();
		return valueMap;
	}

	@Override
	public Iterator<String> getParamNames() {
		init();
		return valueMap.keySet().iterator();
	}

	@Override
	public Object getParam(String name) {
		init();
		Object val = valueMap.get(name);
		if (val instanceof Object[]) {
			return ((Object[]) val)[0];
		}
		return val;
	}

	@Override
	public Object[] getParams(String name) {
		init();
		Object val = valueMap.get(name);
		if (val instanceof String) {
			return new String[]{(String) val};
		}
		if (val instanceof UploadFileHandle) {
			return new UploadFileHandle[]{(UploadFileHandle) val};
		}
		return (Object[]) val;
	}
}
