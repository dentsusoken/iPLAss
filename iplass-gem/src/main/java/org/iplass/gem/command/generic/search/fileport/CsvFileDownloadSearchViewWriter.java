/*
 * Copyright (C) 2024 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.gem.command.generic.search.fileport;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.regex.Pattern;

import org.apache.commons.text.StringEscapeUtils;
import org.iplass.gem.command.generic.search.EntityFileDownloadSearchContext;
import org.iplass.gem.command.generic.search.EntityFileDownloadSearchContext.FileColumn;
import org.iplass.gem.command.generic.search.EntityFileDownloadSearchViewWriter;
import org.iplass.mtp.impl.entity.fileport.EntityCsvException;
import org.iplass.mtp.util.StringUtil;

public class CsvFileDownloadSearchViewWriter extends EntityFileDownloadSearchViewWriter {

	private static final String CR = "\n";	// 改行コード。CSV出力なので、SJIS(MS932)を想定
	private static final String DOUBLE_QUOT = "\"";
	private static final int BOM = '\ufeff';

	private Writer writer;

	public CsvFileDownloadSearchViewWriter(EntityFileDownloadSearchContext context) {
		super(context);
	}

	@Override
	protected void initWriter(OutputStream out) throws IOException {
		String charSet = context.getCsvCharacterCode();

		this.writer = new BufferedWriter(new OutputStreamWriter(out, charSet));

		//BOM対応
		if ("UTF-8".equalsIgnoreCase(charSet)) {
			try {
				writer.write(BOM);
			} catch (IOException e) {
				throw new EntityCsvException(e);
			}
		}
	}

	@Override
	protected void beforeWriteHeader() {
	}

	@Override
	protected void writeHeaderColumn(int columnIndex, String text) {
		writeText(text);
	}

	@Override
	protected String valueToUnSplitMultipleValueString(Object value) {
		String strValue = valueToString(value);
		if (StringUtil.isNotEmpty(strValue)) {
			// 連結して出力するためエスケープ
			String outText = StringEscapeUtils.escapeCsv(strValue);
			// 値にダブルクォーテーションが含まれる場合は、最後に２重にエスケープされるので戻す
			if (strValue.contains("\"")) {
				outText = outText.replaceAll(Pattern.quote("\"\""), "\"");
			}
			return outText;
		}
		return strValue;
	}

	@Override
	protected void beforeWriteData() {
	}

	@Override
	protected void startRow() {
	}

	@Override
	protected void writeValueColumn(int columnIndex, FileColumn fileColumn, Object value) {
		writeText(valueToString(value));
	}

	@Override
	protected void nextColumn() {
		writeComma();
	}

	@Override
	protected void endRow() {
		writeCR();
	}

	@Override
	protected void endData() throws IOException {
		// フッタ出力
		writeFooter();

		writer.flush();
	}

	private void writeText(String text) {

		try {
			if (StringUtil.isEmpty(text)) {
				return;
			}

			String outText = StringEscapeUtils.escapeCsv(text);
			if (gcs.isCsvDownloadQuoteAll()) {
				if (outText.startsWith("\"") && outText.endsWith("\"")) {
					writer.write(outText);
				} else {
					writer.write(DOUBLE_QUOT + outText + DOUBLE_QUOT);
				}
			} else {
				if (outText.startsWith(" ") || outText.endsWith(" ")) {
					writer.write(DOUBLE_QUOT + outText + DOUBLE_QUOT);
				} else {
					writer.write(outText);
				}
			}
		} catch (IOException e) {
			throw new EntityCsvException(e);
		}

	}

	private void writeComma() {

		try {
			writer.write(",");
		} catch (IOException e) {
			throw new EntityCsvException(e);
		}
	}

	private void writeCR() {

		try {
			writer.write(CR);
		} catch (IOException e) {
			throw new EntityCsvException(e);
		}
	}

	private void writeFooter() {
		if (gcs.isCsvDownloadWithFooter()) {
			try {
				writer.write(gcs.getCsvDownloadFooter());
			} catch (IOException e) {
				throw new EntityCsvException(e);
			}

			writeCR();
		}
	}

}
