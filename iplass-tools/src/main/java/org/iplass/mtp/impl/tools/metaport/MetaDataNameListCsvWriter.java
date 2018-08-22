/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.tools.metaport;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.util.StringUtil;
import org.supercsv.cellprocessor.ConvertNullTo;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.prefs.CsvPreference;

/**
 * EntityのCSVファイルWriter
 */
public class MetaDataNameListCsvWriter implements Closeable {

	private CsvMapWriter csvWriter;
	private boolean removeLineFeedCode = true;

	private String[] header = {"Path", "Name", "Display Name", "Description", "ID", "Repository"};
	private CellProcessor[] processors = {
			new ConvertNullTo("")
			, new ConvertNullTo("")
			, new ConvertNullTo("")
			, new ConvertNullTo("")
			, new ConvertNullTo("")
			, new ConvertNullTo("")
			};


	public MetaDataNameListCsvWriter(OutputStream out) throws IOException {
		this(out, true);
	}

	public MetaDataNameListCsvWriter(OutputStream out, boolean removeLineFeedCode) throws IOException {

		//BOM対応
		out.write(0xef);
		out.write(0xbb);
		out.write(0xbf);

		init(new OutputStreamWriter(out, "UTF-8"));
	}

	private void init(Writer writer) {
		//Quat:「"」、Demilter：「,」、NewLine：「\n」
		csvWriter = new CsvMapWriter(writer, new CsvPreference.Builder(CsvPreference.EXCEL_PREFERENCE).surroundingSpacesNeedQuotes(true).build());
		//Quat:「"」、Demilter：「,」、NewLine：「\r\n」
//		csvWriter = new CsvMapWriter(writer, CsvPreference.STANDARD_PREFERENCE);

	}

	public void writeHeader() throws SuperCsvException, IOException {
		csvWriter.writeHeader(header);
	}

	public void writeEntry(MetaDataEntry entry) throws IOException {
		if (entry == null) {
			return;
		}

		RootMetaData meta = entry.getMetaData();

		Map<String, Object> recordMap = new HashMap<String, Object>(header.length);
		recordMap.put(header[0], entry.getPath());
		recordMap.put(header[1], meta.getName());
		recordMap.put(header[2], I18nUtil.stringMeta(meta.getDisplayName(), meta.getLocalizedDisplayNameList()));
		recordMap.put(header[3], outputString(meta.getDescription()));
		recordMap.put(header[4], meta.getId());
		String repositryType = null;
		if (entry.getRepositryType() != null) {
			repositryType = entry.getRepositryType().name();
		}
		recordMap.put(header[5], repositryType);

		csvWriter.write(recordMap, header, processors);
	}

	@Override
	public void close() throws IOException {
		csvWriter.close();
	}

	private String outputString(String value) {
		if (removeLineFeedCode) {
			return StringUtil.removeLineFeedCode(value);
		} else {
			return value;
		}
	}

}
