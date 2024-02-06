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

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.GenericEntity;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class EntitySerializer extends StdSerializer<Entity> {
	private static final long serialVersionUID = -8460188948114932009L;

	public static final String PROP_DEFINITION_NAME ="definitionName";

	public EntitySerializer() {
		super(Entity.class);
	}
	
	public EntitySerializer(Class<?> t, boolean dummy) {
		super(t, dummy);
	}

	public EntitySerializer(Class<Entity> t) {
		super(t);
	}

	public EntitySerializer(JavaType type) {
		super(type);
	}

	public EntitySerializer(StdSerializer<?> src) {
		super(src);
	}

	@Override
	public void serialize(Entity value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		boolean writeNull = provider.getConfig().getDefaultPropertyInclusion().getValueInclusion() == Include.ALWAYS;
		
		if (value instanceof GenericEntity) {
			gen.writeStartObject(value);
			
			gen.writeStringField(PROP_DEFINITION_NAME, value.getDefinitionName());
			for (String pname: ((GenericEntity) value).getPropertyNames()) {
				writeProp(pname, value, gen, writeNull);
			}
			gen.writeEndObject();
			
		} else {
			EntityContext ec = EntityContext.getCurrentContext();
			EntityHandler eh = ec.getHandlerByName(value.getDefinitionName());
			if (eh == null) {
				throw provider.mappingException("cant find Entity Defs... definitionName:" + value.getDefinitionName());
			}
			gen.writeStartObject(value);
			
			gen.writeStringField(PROP_DEFINITION_NAME, value.getDefinitionName());
			for (PropertyHandler ph: eh.getPropertyList(ec)) {
				writeProp(ph.getName(), value, gen, writeNull);
			}
			gen.writeEndObject();
		}
	}
	
	private void writeProp(String propName, Entity entity, JsonGenerator gen, boolean writeNull) throws IOException {
		Object propVal = entity.getValue(propName);
		if (writeNull || propVal != null) {
			if (propVal instanceof Object[]) {
				Object[] arrayVal = (Object[]) propVal;
				gen.writeArrayFieldStart(propName);
				for (Object v: arrayVal) {
					writeValue(v, gen);
				}
				gen.writeEndArray();
			} else {
				gen.writeFieldName(propName);
				writeValue(propVal, gen);
			}
		}
	}

	private void writeValue(Object value, JsonGenerator gen) throws IOException {
		if (value == null) {
			gen.writeNull();
		} else {
			gen.writeObject(value);
		}
	}

}
