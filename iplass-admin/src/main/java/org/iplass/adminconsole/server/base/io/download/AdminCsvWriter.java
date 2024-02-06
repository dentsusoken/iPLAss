/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.server.base.io.download;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.iplass.adminconsole.shared.base.dto.io.download.DownloadProperty.ENCODE;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.prefs.CsvPreference;

public abstract class AdminCsvWriter implements Closeable, Flushable {

	protected final CsvMapWriter csvWriter;
	protected final OutputStreamWriter originalWriter;

	public AdminCsvWriter(OutputStream out) throws IOException {
		this(out, ENCODE.UTF8.getValue());
	}

	public AdminCsvWriter(OutputStream out, String encode) throws IOException {

		//BOM対応
		if (isBOMApendEncode(encode)) {
			writeBOM(encode, out);
		}

		this.originalWriter = new OutputStreamWriter(out, encode);

		//Quat:「"」、Demilter：「,」、NewLine：「\n」
		this.csvWriter = new CsvMapWriter(originalWriter, new CsvPreference.Builder(CsvPreference.EXCEL_PREFERENCE).surroundingSpacesNeedQuotes(true).build());
		//Quat:「"」、Demilter：「,」、NewLine：「\r\n」
//		csvWriter = new CsvMapWriter(writer, CsvPreference.STANDARD_PREFERENCE);

	}

	@Override
	public void flush() throws IOException {
		csvWriter.flush();
	}

	@Override
	public void close() throws IOException {
		csvWriter.close();
	}

	private boolean isBOMApendEncode(String encode) {
		return "utf8".equals(encode.toLowerCase())
				|| "utf-8".equals(encode.toLowerCase());
	}

	private void writeBOM(String encode, OutputStream out) throws IOException {
		if ("utf8".equals(encode.toLowerCase())
				|| "utf-8".equals(encode.toLowerCase())) {
			out.write(0xef);
			out.write(0xbb);
			out.write(0xbf);
		}
	}
}
