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

package org.iplass.mtp.impl.csv;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.impl.entity.csv.EntityCsvException;
import org.iplass.mtp.impl.entity.csv.EntityCsvReader;
import org.iplass.mtp.impl.web.WebResourceBundleUtil;

public class EntityCsvReaderForCheck extends EntityCsvReader {

	private int errorLimit;

	public EntityCsvReaderForCheck(EntityDefinition definition, InputStream inputStream, int errorLimit) throws UnsupportedEncodingException {
		super(definition, inputStream);
		this.errorLimit = errorLimit;
	}

	public EntityCsvReaderForCheck(EntityDefinition definition, InputStream inputStream, String charset, int errorLimit) throws UnsupportedEncodingException {
		super(definition, inputStream, charset);
		this.errorLimit = errorLimit;
	}

	public EntityCsvReaderForCheck(EntityDefinition definition, Reader reader, int errorLimit) {
		super(definition, reader);
		this.errorLimit = errorLimit;
	}

	public void check() {
		try {
			init();

			List<String> list;

			List<String> errorMsgList = new ArrayList<String>();

			int cnt = 0;
			while((list = readLine()) != null) {
				cnt ++;
				try {
					validateLine(list);
				} catch (EntityCsvException e) {
					throw new EntityCsvException(e.getCode(), resourceString("impl.csv.EntityCsvReaderForCheck.rowError", cnt, e.getMessage()), e);
				}

				int start = isUseCtrl() ? 1 : 0;
				for (int i = start; i < header().size(); i++) {
					String headerName = header().get(i);
					String value = list.get(i);

					try {
						validateValue(headerName, value);
					} catch (EntityCsvException e) {
						// 型変換に失敗した場合は全てのエラーを保持して最後にエクセプションをスローする。
						errorMsgList.add(resourceString("impl.csv.EntityCsvReaderForCheck.rowError", cnt, e.getMessage()));
					}
				}
			}

			if (!errorMsgList.isEmpty()) {
				String errorMsg = resourceString("impl.csv.EntityCsvReaderForCheck.errorHead");
				if (errorLimit < 0 || errorMsgList.size() <= errorLimit) {
					for (String s : errorMsgList) {
						errorMsg = errorMsg + "\n" + s;
					}
				} else {
					for (String s : errorMsgList.subList(0, errorLimit)) {
						errorMsg = errorMsg + "\n" + s;
					}
					errorMsg += "\n...";
				}

				throw new EntityCsvException("CE1001", errorMsg);
			}

		} catch (IOException e) {
			// csvの読み込みに失敗の為システムエラーとする。
			throw new ApplicationException(e);
		}
	}

	private static String resourceString(String key, Object... arguments) {
		return WebResourceBundleUtil.resourceString(key, arguments);
	}
}
