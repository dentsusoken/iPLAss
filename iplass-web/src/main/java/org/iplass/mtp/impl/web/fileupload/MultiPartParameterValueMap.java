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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.iplass.mtp.command.UploadFileHandle;
import org.iplass.mtp.impl.web.ParameterValueMap;
import org.iplass.mtp.impl.web.WebProcessRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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

	public MultiPartParameterValueMap(ServletContext servletContext, HttpServletRequest req) {
		this.servletContext = servletContext;
		this.req = req;
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

		ServletFileUpload upload = new ServletFileUpload();
		// Parse the request
		try {
			FileItemIterator iter = upload.getItemIterator(req);
			while (iter.hasNext()) {
				FileItemStream item = iter.next();
				String name = item.getFieldName();
				if (item.isFormField()) {
					String value = Streams.asString(item.openStream());
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
					UploadFileHandleImpl value = UploadFileHandleImpl.toUploadFileHandle(item.openStream(), item.getName(), item.getContentType(), servletContext, maxFileSize);
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
			}
		} catch (FileUploadException | IOException e) {
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
