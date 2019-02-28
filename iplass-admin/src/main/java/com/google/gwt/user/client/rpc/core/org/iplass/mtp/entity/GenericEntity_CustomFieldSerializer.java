/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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


package com.google.gwt.user.client.rpc.core.org.iplass.mtp.entity;

import java.util.Set;

import org.iplass.mtp.entity.GenericEntity;

import com.google.gwt.user.client.rpc.CustomFieldSerializer;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

public class GenericEntity_CustomFieldSerializer extends CustomFieldSerializer<GenericEntity> {

	public static void deserialize(SerializationStreamReader streamReader, GenericEntity instance) throws SerializationException {

		String definitionName = streamReader.readString();
		instance.setDefinitionName(definitionName);

		int size = streamReader.readInt();
		for (int i = 0; i < size; ++i) {
			String propertyName = streamReader.readString();
			Object value = streamReader.readObject();
			instance.setValue(propertyName, value);
	    }
	}

	public static void serialize(SerializationStreamWriter streamWriter, GenericEntity instance) throws SerializationException {

		streamWriter.writeString(instance.getDefinitionName());

		Set<String> properties = instance.getPropertyNames();
		if (properties == null) {
			streamWriter.writeInt(0);
		} else {
			streamWriter.writeInt(properties.size());
			for (String property : properties) {
				streamWriter.writeString(property);
				streamWriter.writeObject(instance.getValue(property));
			}
		}
	}

	public static GenericEntity instantiate(SerializationStreamReader streamReader) {
		return new GenericEntity();
	}

	@Override
	public void deserializeInstance(SerializationStreamReader streamReader, GenericEntity instance)
			throws SerializationException {
		deserialize(streamReader, instance);
	}

	@Override
	public void serializeInstance(SerializationStreamWriter streamWriter, GenericEntity instance)
			throws SerializationException {
		serialize(streamWriter, instance);
	}

//	@Override
//	public GenericEntity instantiateInstance(SerializationStreamReader streamReader) throws SerializationException {
//		return instantiate(streamReader);
//	}

//	@Override
//	public boolean hasCustomInstantiateInstance() {
//		return false;
//	}

}
