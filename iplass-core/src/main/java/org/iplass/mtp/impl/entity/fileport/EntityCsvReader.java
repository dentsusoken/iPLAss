/*
 * Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.entity.fileport;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.impl.util.CoreResourceBundleUtil;
import org.iplass.mtp.util.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;

/**
 * EntityのCSVファイルのリーダークラスです。
 */
public class EntityCsvReader extends EntityFileReader<EntityCsvReader> {

	private static final Logger logger = LoggerFactory.getLogger(EntityCsvReader.class);

	private Reader reader;
	private CsvListReader csvListReader;

	public EntityCsvReader(EntityDefinition definition, InputStream inputStream) throws UnsupportedEncodingException {
		this(definition, inputStream, "UTF-8");
	}

	public EntityCsvReader(EntityDefinition definition, InputStream inputStream, String charset)
			throws UnsupportedEncodingException {
		super(definition);
		Reader reader = new InputStreamReader(inputStream, charset);
		BufferedReader buffered = new BufferedReader(reader);
		this.reader = buffered;
	}

	public EntityCsvReader(EntityDefinition definition, Reader reader) {
		super(definition);
		BufferedReader buffered = null;
		if (reader instanceof BufferedReader) {
			buffered = (BufferedReader) reader;
		} else {
			buffered = new BufferedReader(reader);
		}
		this.reader = buffered;
	}

	@Override
	protected List<String> readHeader() {

		csvListReader = new CsvListReader(reader,
				new CsvPreference.Builder(CsvPreference.STANDARD_PREFERENCE).surroundingSpacesNeedQuotes(true).build());

		List<String> headerLine = null;
		try {
			headerLine = csvListReader.read();
		} catch (IOException e) {
			throw new EntityCsvException(e);
		}
		if (CollectionUtil.isEmpty(headerLine)) {
			throw new EntityCsvException("CE2000", rs("impl.csv.EntityCsvReader.emptyFile"));
		}

		// UTF-8 BOM対応
		headerLine.set(0, excludeBOM(headerLine.get(0)));

		return headerLine;
	}

	@Override
	protected List<String> readData() {
		try {
			List<String> currentLine = csvListReader.read();
			return currentLine;
		} catch (IOException e) {
			throw new EntityCsvException(e);
		}
	}

	@Override
	public void close() {
		Closeable closable = csvListReader != null ? csvListReader : reader;
		if (closable != null) {
			try {
				closable.close();
			} catch (IOException e) {
				logger.warn("fail to close EntityCsvReader resource. check whether resource is leak or not.", e);
			}
			csvListReader = null;
			reader = null;
		}
	}

	private String excludeBOM(String value) {
		if (value.charAt(0) == 0xFEFF) {
			return String.copyValueOf(value.toCharArray(), 1, value.toCharArray().length - 1);
		}
		return value;
	}

	private static String rs(String key, Object... arguments) {
		return CoreResourceBundleUtil.resourceString(key, arguments);
	}

}
