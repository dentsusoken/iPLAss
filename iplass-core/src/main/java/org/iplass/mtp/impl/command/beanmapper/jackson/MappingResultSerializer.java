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
package org.iplass.mtp.impl.command.beanmapper.jackson;

import java.io.IOException;

import org.iplass.mtp.command.beanmapper.MappingResult;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class MappingResultSerializer extends StdSerializer<MappingResult> {
	private static final long serialVersionUID = -86614146437163658L;

	public static final String PROP_ERRORS = "errors";

	public MappingResultSerializer() {
		super(MappingResult.class);
	}

	public MappingResultSerializer(Class<?> t, boolean dummy) {
		super(t, dummy);
	}

	public MappingResultSerializer(Class<MappingResult> t) {
		super(t);
	}

	public MappingResultSerializer(JavaType type) {
		super(type);
	}

	public MappingResultSerializer(StdSerializer<?> src) {
		super(src);
	}

	@Override
	public void serialize(MappingResult value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeStartObject(value);
		boolean writeNull = provider.getConfig().getDefaultPropertyInclusion().getValueInclusion() == Include.ALWAYS;
		if (writeNull || value.getErrors() != null) {
			gen.writeObjectField(PROP_ERRORS, value.getErrors());
		}
		gen.writeEndObject();
	}
}
