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

package org.iplass.mtp.impl.tools.auth.builtin;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.util.DateUtil;
import org.iplass.mtp.web.template.TemplateUtil;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ConvertNullTo;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.ift.DateCellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.CsvContext;

/**
 * Builtin Auth UserのCSVファイルWriter
 */
public class BuiltinAuthUserCsvWriter implements Closeable {

	private CsvMapWriter csvWriter;

	private CellProcessor[] processors;

	private String[] header = {"OID", "Account ID", "Name", "Mail", "Policy Name", "Admin", "Error Count", "Login Error Date", "Last PW Change", "PW Remain Days", "Last Login", "Start Date", "End Date"};

	public BuiltinAuthUserCsvWriter(OutputStream out) throws IOException {

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

		createCellProcessor();
	}

	public void writeHeader() throws SuperCsvException, IOException {
		csvWriter.writeHeader(header);
	}

	public void writeUser(BuiltinAuthUser user) throws IOException {
		Map<String, Object> recordMap = new HashMap<String, Object>(header.length);
		int no = 0;
		//OID
		recordMap.put(header[no++], user.getOid());
		//Account ID
		recordMap.put(header[no++], user.getAccountId());
		//Name
		recordMap.put(header[no++], user.getName());
		//Mail
		recordMap.put(header[no++], user.getMail());
		//Policy Name
		recordMap.put(header[no++], user.getPolicyName());
		//Admin
		recordMap.put(header[no++], user.isAdmin());
		//Error Count
		recordMap.put(header[no++], user.getLoginErrorCnt());
		//Login Error Date
		recordMap.put(header[no++], user.getLoginErrorDate());
		//Last PW Change
		recordMap.put(header[no++], user.getLastPasswordChange());
		//PW Remain Days
		recordMap.put(header[no++], user.getPasswordRemainDays());
		//Last Login
		recordMap.put(header[no++], user.getLastLoginOn());
		//Start Date
		recordMap.put(header[no++], user.getStartDate());
		//End Date
		recordMap.put(header[no++], user.getEndDate());

		csvWriter.write(recordMap, header, processors);
	}

	@Override
	public void close() throws IOException {
		csvWriter.close();
	}

	private void createCellProcessor() {

		List<CellProcessor> processors = new ArrayList<>();
		//OID
		processors.add(new ConvertNullTo(""));
		//Account ID
		processors.add(new ConvertNullTo(""));
		//Name
		processors.add(new ConvertNullTo(""));
		//Mail
		processors.add(new ConvertNullTo(""));
		//Policy Name
		processors.add(new ConvertNullTo(""));
		//Admin
		processors.add(new ConvertNullTo(""));
		//Error Count
		processors.add(new ConvertNullTo(""));
		//Login Error Date
		processors.add(new ConvertNullTo("", new FmtDateTimeEx()));
		//Last PW Change
		processors.add(new ConvertNullTo("", new FmtDateEx()));
		//PW Remain Days
		processors.add(new ConvertNullTo(""));
		//Last Login
		processors.add(new ConvertNullTo("", new FmtDateTimeEx()));
		//Start Date
		processors.add(new ConvertNullTo("", new FmtDateEx()));
		//End Date
		processors.add(new ConvertNullTo("", new FmtDateEx()));

		this.processors = processors.toArray(new CellProcessor[]{});
	}

	private class FmtDateEx extends CellProcessorAdaptor implements DateCellProcessor {

		public FmtDateEx() {
			super();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public <T> T execute(final Object value, final CsvContext context) throws SuperCsvException {
			if( value == null ) {
				throw new SuperCsvCellProcessorException("Input cannot be null on line " + context.getLineNumber()
						+ " column " + context.getColumnNumber(), context, this);
			}
			if( !(value instanceof Date)) {
				throw new SuperCsvCellProcessorException("the value '" + value
						+ "' is not of type Date", context, this);
			}

			final String result = DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getOutputDateFormat(), false).format((Date) value);

			return next.execute(result, context);
		}
	}

	private class FmtDateTimeEx extends CellProcessorAdaptor implements DateCellProcessor {

		public FmtDateTimeEx() {
			super();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public <T> T execute(final Object value, final CsvContext context) throws SuperCsvException {
			if( value == null ) {
				throw new SuperCsvCellProcessorException("Input cannot be null on line " + context.getLineNumber()
						+ " column " + context.getColumnNumber(), context, this);
			}
			if( !(value instanceof Timestamp)) {
				throw new SuperCsvCellProcessorException("the value '" + value
						+ "' is not of type Timestamp", context, this);
			}

			final String result = DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getOutputDatetimeSecFormat(), true).format((Timestamp) value);

			return next.execute(result, context);
		}
	}
}
