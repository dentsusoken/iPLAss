/*
 * Copyright (C) 2018 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.entity.csv;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import org.apache.commons.text.StringEscapeUtils;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.entity.query.Limit;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.util.DateUtil;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <p>
 * QueryのCsvファイル出力クラス
 * </p>
 */
public class QueryCsvWriter implements AutoCloseable {

	private static final Logger logger = LoggerFactory.getLogger(QueryCsvWriter.class);

	private static final String DOUBLE_QUOT = "\"";
	private static final int BOM = '\ufeff';
	private static final String CR = "\n";

	private final Query query;
	private final QueryWriteOption option;

	private final EntityManager em;

	private Writer writer;
	private boolean isInit;

	private String dateFormat;
	private String dateTimeFormat;
	private String timeFormat;
	private ObjectMapper mapper;

	public QueryCsvWriter(OutputStream out, Query query) throws IOException {
		this(out, query, new QueryWriteOption());
	}

	public QueryCsvWriter(OutputStream out, Query query, QueryWriteOption option) throws IOException {
		this.query = query;
		this.option = option;

		em = ManagerLocator.manager(EntityManager.class);

		writer = new BufferedWriter(new OutputStreamWriter(out, option.getCharset()));
	}

	public int write() throws IOException {

		// Header出力
		writeHeader();

		// CSV レコードを出力
		return search();
	}

	public void writeError(String message) throws IOException {
		init();

		if (writer != null) {
			writer.flush();
			writer.write(message);
		}
	}

	@Override
	public void close() {
		if (writer != null) {
			try {
				writer.close();
			} catch (IOException e) {
				logger.warn("fail to close QueryCsvWriter resource. check whether resource is leak or not.", e);
			}
			writer = null;
		}
	}

	private void writeHeader() {
		init();

		List<ValueExpression> cols = query.getSelect().getSelectValues();

		IntStream.range(0, cols.size()).forEach(i -> {
			ValueExpression col = cols.get(i);
			writeText(col.toString());

			if (i < cols.size() - 1) {
				writeComma();
			}
		});

		newLine();
	}

	private void writeValues(final Object[] values) {
		init();

		IntStream.range(0, values.length).forEach(i -> {
			Object value = values[i];
			if (value != null && value.getClass().isArray()) {
				Object[] array = (Object[]) value;
				List<String> valueList = new ArrayList<>();
				for (int j = 0; j < array.length; j++) {
					if (array[j] != null) {
						if (option.isQuoteAll()) {
							valueList.add(DOUBLE_QUOT + stringValue(array[j]) + DOUBLE_QUOT);
						} else {
							valueList.add(stringValue(array[j]));
						}
					} else {
						valueList.add("");
					}
				};

				writeText(toJsonString(valueList));
			} else {
				writeText(stringValue(value));
			}


			if (i < values.length - 1) {
				writeComma();
			}
		});

		newLine();
	}

	private void init() {
		if (isInit) {
			return;
		}

		// BOM対応
		if ("UTF-8".equalsIgnoreCase(option.getCharset())) {
			try {
				writer.write(BOM);
			} catch (IOException e) {
				throw new EntityCsvException(e);
			}
		}

		isInit = true;
	}

	private int search() {

		final Query optQuery = option.getBeforeSearch().apply(query);

		if (option.getLimit() > 0) {
			optQuery.setLimit(new Limit(option.getLimit()));
		}

		final int[] count = new int[1];
		em.search(optQuery, new Predicate<Object[]>() {

			@Override
			public boolean test(Object[] values) {

				option.getAfterSearch().accept(optQuery.copy(), values);

				writeValues(values);

				count[0] = count[0] + 1;
				return true;
			}
		});

		return count[0];
	}

	private String stringValue(Object value) {

		if (value == null) {
			return "";
		}

		if (value instanceof BigDecimal) {
			BigDecimal bd = (BigDecimal) value;
			return bd.toPlainString();
		} else if (value instanceof Double) {
			BigDecimal bd = BigDecimal.valueOf((Double) value);
			return bd.toPlainString();
		} else if (value instanceof Float) {
			BigDecimal bd = BigDecimal.valueOf((Float) value);
			return bd.toPlainString();
		} else if (value instanceof Boolean) {
			Boolean b = (Boolean) value;
			return b.booleanValue() ? "1" : "0";
		} else if (value instanceof SelectValue) {
			SelectValue sv = (SelectValue) value;
			return sv.getValue();
		} else if (value instanceof BinaryReference) {
			BinaryReference br = (BinaryReference) value;
			Map<String, String> valueMap = new LinkedHashMap<>();
			valueMap.put("lobid", String.valueOf((br.getLobId())));
			valueMap.put("name", br.getName());
			valueMap.put("type", br.getType());
			return toJsonString(valueMap);
		} else if (value instanceof java.sql.Date) {
			return DateUtil.getSimpleDateFormat(getDateFormat(), false).format((java.sql.Date) value);
		} else if (value instanceof Timestamp) {
			return DateUtil.getSimpleDateFormat(getDateTimeFormat(), false).format((Timestamp) value);
		} else if (value instanceof Time) {
			return DateUtil.getSimpleDateFormat(getTimeFormat(), false).format((Time) value);
		} else {
			return value.toString();
		}
	}

	private String toJsonString(Object value) {

		if (mapper == null) {
			mapper = new ObjectMapper();
			// for backward compatibility
			mapper.configOverride(java.sql.Date.class)
					.setFormat(JsonFormat.Value.forPattern("yyyy-MM-dd").withTimeZone(TimeZone.getDefault()));
		}
		try (StringWriter writer = new StringWriter()) {
			mapper.writeValue(writer, value);
			return writer.toString();
		} catch (JsonProcessingException e) {
			throw new EntityCsvException(e);
		} catch (IOException e) {
			throw new EntityCsvException(e);
		}
	}

	private void newLine() {
		try {
			writer.write(CR);
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

	private void writeText(String text) {

		try {
			if (StringUtil.isEmpty(text)) {
				return;
			}

			String outText = StringEscapeUtils.escapeCsv(text);
			if (option.isQuoteAll()) {
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

	private String getDateFormat() {

		if (dateFormat != null) {
			return dateFormat;
		};
		if (option.getDateFormat() != null) {
			dateFormat = option.getDateFormat();
		} else {
			dateFormat = ExecuteContext.getCurrentContext().getLocaleFormat().getOutputDateFormat();
		}
		return dateFormat;

	}

	private String getDateTimeFormat() {

		if (dateTimeFormat != null) {
			return dateTimeFormat;
		};
		if (option.getDatetimeSecFormat() != null) {
			dateTimeFormat = option.getDatetimeSecFormat();
		} else {
			dateTimeFormat = ExecuteContext.getCurrentContext().getLocaleFormat().getOutputDatetimeSecFormat();
		}
		return dateTimeFormat;

	}

	private String getTimeFormat() {

		if (timeFormat != null) {
			return timeFormat;
		};
		if (option.getTimeSecFormat() != null) {
			timeFormat = option.getTimeSecFormat();
		} else {
			timeFormat = ExecuteContext.getCurrentContext().getLocaleFormat().getOutputTimeSecFormat();
		}
		return timeFormat;

	}

	public void writeFooter(String csvDownloadFooter) {
		try {
			writer.write(csvDownloadFooter);
		} catch (IOException e) {
			throw new EntityCsvException(e);
		}
		newLine();
	}

}
