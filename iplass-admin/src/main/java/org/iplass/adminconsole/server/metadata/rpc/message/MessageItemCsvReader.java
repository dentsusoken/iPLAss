/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.server.metadata.rpc.message;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.commons.io.input.BOMInputStream;
import org.iplass.adminconsole.server.base.i18n.AdminResourceBundleUtil;
import org.iplass.adminconsole.server.base.io.upload.UploadRuntimeException;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.impl.tools.entityport.EntityDataPortingRuntimeException;
import org.iplass.mtp.message.MessageItem;
import org.iplass.mtp.util.StringUtil;
import org.supercsv.cellprocessor.ConvertNullTo;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.io.CsvMapReader;
import org.supercsv.prefs.CsvPreference;

public class MessageItemCsvReader implements Iterable<MessageItem>, Closeable {

	/** CSVの固定ヘッダ(メッセージID) */
	private static final String FIXED_HEADER_MESSAGE_ID = "id";
	/** CSVの固定ヘッダ(デフォルトメッセージ) */
	private static final String FIXED_HEADER_DEFAULT_MESSAGE = "defaultMessage";

	private InputStream inputStream;

	private CsvMapReader csvMapReader;
	private String[] header;
	private CellProcessor[] processors;

	private boolean isInit = false;
	private boolean isIterate = false;

	public MessageItemCsvReader(InputStream inputStream) {
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
	public Iterator<MessageItem> iterator() {
		init();

		// 一度のみ実行可能とする
		if (isIterate) {
			throw new UnsupportedOperationException("concurrent iterate not supported.");
		}
		isIterate = true;

		return new Iterator<MessageItem>() {

			private Map<String, Object> currentMap;
			private UploadRuntimeException rowException;

			@Override
			public boolean hasNext() {
				read();
				if (currentMap == null && rowException == null) {
					return false;
				}
				return true;
			}

			@Override
			public MessageItem next() {
				read();

				if (currentMap == null && rowException == null) {
					throw new NoSuchElementException();
				}

				if (rowException != null) {
					UploadRuntimeException exception = rowException;
					currentMap = null;
					rowException = null;
					throw exception;
				}

				MessageItem item = new MessageItem();

				item.setMessageId((String)currentMap.get(FIXED_HEADER_MESSAGE_ID));
				item.setMessage((String)currentMap.get(FIXED_HEADER_DEFAULT_MESSAGE));

				List<LocalizedStringDefinition> localizedMessageList = new ArrayList<>();
				for (Map.Entry<String, Object> entry : currentMap.entrySet()) {
					//固定部分は入れない
					String key = entry.getKey();
					if (FIXED_HEADER_MESSAGE_ID.equals(key)
							|| FIXED_HEADER_DEFAULT_MESSAGE.equals(key)) {
						continue;
					}

					String value = (String)entry.getValue();
					if (StringUtil.isEmpty(value)) {
						continue;//未設定の場合は多言語定義作らない
					}

					LocalizedStringDefinition definition = new LocalizedStringDefinition();
					definition.setLocaleName(key);
					definition.setStringValue(value);
					localizedMessageList.add(definition);
				}
				if (!localizedMessageList.isEmpty()) {
					item.setLocalizedMessageList(localizedMessageList);
				}

				currentMap = null;
				rowException = null;
				return item;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}

			private void read() {

				//エラー発生時はnext()メソッド実行時にthrowさせるため、rowExceptionに保持

				if (currentMap == null && rowException == null) {
					try {
						currentMap = csvMapReader.read(header, processors);
						rowException = null;
					} catch (IOException e) {
						rowException = new UploadRuntimeException(
								rs("metadata.message.MessageItemCsvUploadServiceImpl.readErr",
										csvMapReader.getLineNumber(), ",detail:" + (e.getLocalizedMessage() != null ? e.getLocalizedMessage() : "")), e);
					} catch (ArrayIndexOutOfBoundsException e) {
						rowException = new UploadRuntimeException(
								rs("metadata.message.MessageItemCsvUploadServiceImpl.readErr",
										csvMapReader.getLineNumber(), ",detail:" + (e.getLocalizedMessage() != null ? e.getLocalizedMessage() : "")), e);
					} catch (SuperCsvException e) {
						rowException = new UploadRuntimeException(
								rs("metadata.message.MessageItemCsvUploadServiceImpl.readErr",
										csvMapReader.getLineNumber(), ",detail:" + (e.getLocalizedMessage() != null ? e.getLocalizedMessage() : "")), e);
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
			throw new UploadRuntimeException(rs("metadata.message.MessageItemCsvUploadServiceImpl.headReadErr"), e);
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

	private String rs(String key, Object... arguments) {
		return AdminResourceBundleUtil.resourceString(key, arguments);
	}

}
