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
package org.iplass.mtp.impl.webapi.jackson;

import java.io.IOException;
import java.util.Map;

import org.iplass.mtp.impl.webapi.WebApiResponse;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class WebApiResponseSerializer extends StdSerializer<WebApiResponse> {
	private static final long serialVersionUID = -3028378394756086182L;
	
	public static final String PROP_STATUS = "status";
	public static final String PROP_EXCEPTION_TYPE = "exceptionType";
	public static final String PROP_EXCEPTION_MESSAGE = "exceptionMessage";
	
	
	public WebApiResponseSerializer() {
		super(WebApiResponse.class);
	}

	public WebApiResponseSerializer(Class<?> t, boolean dummy) {
		super(t, dummy);
	}

	public WebApiResponseSerializer(Class<WebApiResponse> t) {
		super(t);
	}

	public WebApiResponseSerializer(JavaType type) {
		super(type);
	}

	public WebApiResponseSerializer(StdSerializer<?> src) {
		super(src);
	}

	@Override
	public void serialize(WebApiResponse value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeStartObject(value);
		boolean writeNull = provider.getConfig().getDefaultPropertyInclusion().getValueInclusion() == Include.ALWAYS;
		
		if (writeNull || value.getStatus() != null) {
			gen.writeStringField(PROP_STATUS, value.getStatus());
		}
		if (writeNull || value.getResults() != null) {
			for (Map.Entry<String, Object> e: value.getResults().entrySet()) {
				if (e.getValue() != null) {
					gen.writeObjectField(e.getKey(), e.getValue());
				}
			}
		}
		if (writeNull || value.getExceptionType() != null) {
			gen.writeStringField(PROP_EXCEPTION_TYPE, value.getExceptionType());
		}
		if (writeNull || value.getExceptionMessage() != null) {
			gen.writeStringField(PROP_EXCEPTION_MESSAGE, value.getExceptionMessage());
		}
		gen.writeEndObject();
	}

}
