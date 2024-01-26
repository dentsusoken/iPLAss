/*
 * Copyright (C) 2016 DENTSU SOKEN INC. All Rights Reserved.
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

import org.iplass.mtp.definition.binary.BinaryDefinition;
import org.iplass.mtp.impl.metadata.binary.jaxb.BinaryDefinitionJaxbRepresentation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * BinaryDefinition-JSONマッピングのためのカスタムのJsonDeserializer。
 * BinaryDefinitionはname,sizeのみ出力とする。
 *
 * @author K.Higuchi
 *
 */
public class BinaryDefinitionDeserializer extends JsonDeserializer<BinaryDefinition> {
	
	private static final String NAME = "name";
	private static final String SIZE = "size";

	@Override
	public BinaryDefinition deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		
		JsonToken t = jp.getCurrentToken();
		if (t == JsonToken.START_OBJECT) {
			jp.nextToken();
		}
		
		BinaryDefinitionJaxbRepresentation ret = new BinaryDefinitionJaxbRepresentation();

		for (; jp.getCurrentToken() != JsonToken.END_OBJECT; jp.nextToken()) {
			String propName = jp.getCurrentName();
			jp.nextToken();
			if (NAME.equals(propName)) {
				ret.setName(jp.getText());
			} else if (SIZE.equals(propName)) {
				ret.setSize(jp.getLongValue());
			} else {
				jp.skipChildren();
			}
		}

		return ret;
	}

}
