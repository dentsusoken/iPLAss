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
package org.iplass.mtp.impl.command.beanmapper.jackson;

import java.io.IOException;

import org.iplass.mtp.command.beanmapper.MappingError;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class MappingErrorSerializer extends StdSerializer<MappingError> {
	private static final long serialVersionUID = -4633763468134285985L;

	public static final String PROP_PROP_PATH = "propertyPath";
	public static final String PROP_ERROR_MESSAGES = "errorMessages";

	public MappingErrorSerializer() {
		super(MappingError.class);
	}

	public MappingErrorSerializer(Class<?> t, boolean dummy) {
		super(t, dummy);
	}

	public MappingErrorSerializer(Class<MappingError> t) {
		super(t);
	}

	public MappingErrorSerializer(JavaType type) {
		super(type);
	}

	public MappingErrorSerializer(StdSerializer<?> src) {
		super(src);
	}

	@Override
	public void serialize(MappingError value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeStartObject(value);
		boolean writeNull = provider.getConfig().getDefaultPropertyInclusion().getValueInclusion() == Include.ALWAYS;
		if (writeNull || value.getPropertyPath() != null) {
			gen.writeStringField(PROP_PROP_PATH, value.getPropertyPath());
		}
		if (writeNull || value.getErrorMessages() != null) {
			gen.writeObjectField(PROP_ERROR_MESSAGES, value.getErrorMessages());
		}
		gen.writeEndObject();
	}
}
