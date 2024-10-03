/*
 * Copyright (C) 2020 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.webapi.command.entity;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.function.Predicate;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.SystemException;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.query.Limit;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.impl.entity.fileport.QueryCsvWriteOption;
import org.iplass.mtp.impl.webapi.command.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QueryJsonWriter implements AutoCloseable, Constants {

	private static final Logger logger = LoggerFactory.getLogger(QueryJsonWriter.class);

	private final Query query;
	private final QueryCsvWriteOption option;
	private final EntityManager em;
	private final boolean isCountTotal;
	
	private ObjectMapper mapper;
	private JsonGenerator gen;
	
	public QueryJsonWriter(OutputStream out, Query query, boolean isCountTotal, ObjectMapper mapper,
			JsonFactory jsonFactory) throws IOException {
		this(out, query, isCountTotal, new QueryCsvWriteOption(), mapper, jsonFactory);
	}

	public QueryJsonWriter(OutputStream out, Query query, boolean isCountTotal, QueryCsvWriteOption option,
			ObjectMapper mapper, JsonFactory jsonFactory) throws IOException {
		this.query = query;
		this.option = option;
		this.isCountTotal = isCountTotal;
		this.mapper = mapper;
		em = ManagerLocator.manager(EntityManager.class);
		gen = jsonFactory.createGenerator(new BufferedWriter(new OutputStreamWriter(out, option.getCharset())));
	}

	public void write() throws IOException {
		
		gen.writeStartObject();
		gen.writeStringField("status", CMD_EXEC_SUCCESS);
		
		Query optQuery = option.getBeforeSearch().apply(query);

		//header
		gen.writeArrayFieldStart("listHeader");
		for (ValueExpression ve: optQuery.getSelect().getSelectValues()) {
			gen.writeString(ve.toString());
		}
		gen.writeEndArray();
		
		gen.writeFieldName("list");
		gen.writeStartArray();

		// 検索結果のJSONレコードを出力
		int countTotal = search(optQuery);
		
		gen.writeEndArray();
		
		if (isCountTotal) {
			gen.writeNumberField("count", countTotal);
		}

		gen.writeEndObject();
	}

	@Override
	public void close() {
		if (gen != null) {
			try {
				gen.close();
			} catch (IOException e) {
				logger.warn("fail to close QueryJsonWriter resource. check whether resource is leak or not.", e);
			}
			gen = null;
		}
	}

	private void writeValues(final Object[] values) {

		try {
			mapper.writeValue(gen, values);
		} catch (IOException e) {
			throw new SystemException(e);
		} 
	}

	private int search(Query optQuery) throws IOException {

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
}
