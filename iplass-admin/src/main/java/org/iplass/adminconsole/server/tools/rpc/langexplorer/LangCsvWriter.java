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

package org.iplass.adminconsole.server.tools.rpc.langexplorer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.adminconsole.server.base.io.download.AdminCsvWriter;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.impl.i18n.EnableLanguages;
import org.iplass.mtp.impl.i18n.I18nService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.supercsv.cellprocessor.ConvertNullTo;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvException;

/**
 * 多言語設定のCSVファイルWriter
 */
public class LangCsvWriter extends AdminCsvWriter {

	private String[] header;
	private CellProcessor[] processors;

	private I18nService i18nService = ServiceRegistry.getRegistry().getService(I18nService.class);

	public LangCsvWriter(OutputStream out) throws IOException {
		super(out);

		createCellHeader();
		createCellProcessor();
	}

	public void writeError(String message) throws IOException {
		//original側に出力
		flush();
		if (originalWriter != null) {
			originalWriter.write(message);
		}
	}

	public void writeHeader() throws SuperCsvException, IOException {
		csvWriter.writeHeader(header);
	}

	public void writeRecord(Map<String, List<LocalizedStringDefinition>> localizedStringMap, String definitionPath) throws IOException {

		for(Map.Entry<String, List<LocalizedStringDefinition>> entry : localizedStringMap.entrySet()) {
			Map<String, String> recordMap = new HashMap<String, String>();

			recordMap.put(header[0], definitionPath);
			recordMap.put(header[1], entry.getKey());

			for (LocalizedStringDefinition lsd: entry.getValue()) {
				String localeName = lsd.getLocaleName();
				String localeValue = lsd.getStringValue();

				int cnt = 0;
				for (String key : header) {
					if (key.equals(localeName)) {
						recordMap.put(header[cnt],localeValue);
						break;
					}
					cnt ++;
				}
			}

			csvWriter.write(recordMap, header, processors);
		}

	}

	private void createCellHeader() {

		//ヘッダ取得
		List<EnableLanguages> enableLanguages = i18nService.getEnableLanguages();

		header = new String[enableLanguages.size() + 3];
		header[0] = "definitionPath";
		header[1] = "item";
		header[2] = "defaultLang";

		for (int cnt = 0; cnt < enableLanguages.size(); cnt ++) {
			header[cnt + 3] = enableLanguages.get(cnt).getLanguageKey();
		}

	}

	private void createCellProcessor() {

		processors = new CellProcessor[header.length];
		for (int i = 0; i < header.length; i++) {
			processors[i] = new ConvertNullTo("");
		}
	}

}
