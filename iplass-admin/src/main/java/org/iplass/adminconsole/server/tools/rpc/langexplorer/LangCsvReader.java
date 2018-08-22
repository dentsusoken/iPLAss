/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.server.tools.rpc.langexplorer;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.io.input.BOMInputStream;
import org.iplass.adminconsole.server.base.i18n.AdminResourceBundleUtil;
import org.iplass.mtp.impl.tools.entityport.EntityDataPortingRuntimeException;
import org.iplass.mtp.impl.tools.lang.LangDataPortingRuntimeException;
import org.supercsv.cellprocessor.ConvertNullTo;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapReader;
import org.supercsv.prefs.CsvPreference;

public class LangCsvReader implements Iterable<Map<String, Object>>, Closeable {

	private InputStream inputStream;

	private CsvMapReader csvMapReader;
	private String[] header;
	private CellProcessor[] processors;

	private boolean isInit = false;
	private boolean isIterate = false;

	public LangCsvReader(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	protected void init() {
		if (isInit) {
			return;
		}

		//BOM対応
		InputStream is = inputStream;
		if (!(inputStream instanceof BOMInputStream)) {
			is = new BOMInputStream(inputStream, false);
		}

		//CsvMapReaderの生成
		InputStreamReader isReader;
		try {
			isReader = new InputStreamReader(is, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new EntityDataPortingRuntimeException(e);
		}
		csvMapReader = new CsvMapReader(isReader, new CsvPreference.Builder(CsvPreference.EXCEL_PREFERENCE).surroundingSpacesNeedQuotes(true).build());

		//ヘッダ読み込み
		readCsvHeader();

		isInit = true;
	}

	@Override
	public Iterator<Map<String, Object>> iterator() {
		init();

		// 一度のみ実行可能とする
		if (isIterate) {
			throw new UnsupportedOperationException("concurrent iterate not supported.");
		}
		isIterate = true;

		return new Iterator<Map<String, Object>>() {

			private Map<String, Object> currentMap;

			@Override
			public boolean hasNext() {
				read();
				if (currentMap == null) {
					return false;
				}
				return true;
			}

			@Override
			public Map<String, Object> next() {
				read();

				Map<String, Object> returnMap = currentMap;

				currentMap = null;

				return returnMap;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}

			private void read() {

				if (currentMap == null) {
					try {
						currentMap = csvMapReader.read(header, processors);
					} catch (IOException e) {
						throw new LangDataPortingRuntimeException(rs("tools.langexplorer.LangCsvReader.headReadErr"), e);
					}
				}
			}

		};
	}

	@Override
	public void close() throws IOException {
		if (csvMapReader != null) {
			csvMapReader.close();
		} else {
			inputStream.close();
		}
	}

	private void readCsvHeader() {
		// ヘッダの取得
		try {
			header = csvMapReader.getHeader(true);
		} catch (IOException e) {
			throw new LangDataPortingRuntimeException(rs("tools.langexplorer.LangCsvReader.headReadErr"), e);
		}

		// CellProcessorの取得
		createCellProcessor();
	}

	private void createCellProcessor() {
		processors = new CellProcessor[header.length];
		for (int i = 0; i < header.length; i++) {
			processors[i] = new ConvertNullTo(null,new Optional());
		}
	}

	private static String rs(String key, Object... arguments) {
		return AdminResourceBundleUtil.resourceString(key, arguments);
	}


}
