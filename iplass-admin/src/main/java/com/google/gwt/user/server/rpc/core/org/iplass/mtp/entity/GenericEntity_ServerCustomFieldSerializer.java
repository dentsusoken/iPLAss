/*
 * Copyright (C) 2019 DENTSU SOKEN INC. All Rights Reserved.
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

package com.google.gwt.user.server.rpc.core.org.iplass.mtp.entity;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import org.iplass.mtp.entity.GenericEntity;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.core.org.iplass.mtp.entity.GenericEntity_CustomFieldSerializer;
import com.google.gwt.user.server.rpc.ServerCustomFieldSerializer;
import com.google.gwt.user.server.rpc.impl.DequeMap;
import com.google.gwt.user.server.rpc.impl.ServerSerializationStreamReader;

public class GenericEntity_ServerCustomFieldSerializer extends ServerCustomFieldSerializer<GenericEntity> {

	public static GenericEntity instantiate(SerializationStreamReader streamReader) {
		return new GenericEntity();
	}

	@Override
	public void deserializeInstance(ServerSerializationStreamReader streamReader, GenericEntity instance,
			Type[] expectedParameterTypes, DequeMap<TypeVariable<?>, Type> resolvedTypes)
			throws SerializationException {
		GenericEntity_CustomFieldSerializer.deserialize(streamReader, instance);
	}

	@Override
	public void deserializeInstance(SerializationStreamReader streamReader, GenericEntity instance)
			throws SerializationException {
		GenericEntity_CustomFieldSerializer.deserialize(streamReader, instance);

	}

	@Override
	public void serializeInstance(SerializationStreamWriter streamWriter, GenericEntity instance)
			throws SerializationException {
		GenericEntity_CustomFieldSerializer.serialize(streamWriter, instance);

	}

}
