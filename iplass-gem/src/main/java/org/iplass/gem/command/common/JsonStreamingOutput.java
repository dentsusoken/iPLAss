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

package org.iplass.gem.command.common;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Objectを直接JSON文字列化してWebAPIの結果としたい場合に
 * RequestContextにこのクラスを指定する。
 */
public class JsonStreamingOutput implements StreamingOutput {

	private Object data;

	public JsonStreamingOutput(Object data) {
		this.data = data;
	}

	@Override
	public void write(OutputStream output) throws IOException, WebApplicationException {
		ObjectMapper mapper = new ObjectMapper();
		//for backward compatibility
		mapper.configOverride(java.sql.Date.class).setFormat(JsonFormat.Value.forPattern("yyyy-MM-dd"));
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(output, "UTF-8"));
			writer.write(mapper.writeValueAsString(data));
			writer.flush();
		} catch (Exception e) {
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (Exception e) {
				}
				writer = null;
			}
		}

	}

}