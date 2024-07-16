/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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
import java.util.ArrayList;

import org.iplass.mtp.impl.webapi.WebApiParameter;
import org.iplass.mtp.spi.ServiceRegistry;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;

public class WebApiParameterDeserializer extends JsonDeserializer<WebApiParameter> {

	public static final String NAME_PROPERTY_NAME ="name";
	public static final String VALUE_PROPERTY_NAME ="value";
	public static final String VALUE_TYPE_PROPERTY_NAME ="valueType";

	private WebApiObjectMapperService mapperService;

	WebApiParameterDeserializer () {
		mapperService = ServiceRegistry.getRegistry().getService(WebApiObjectMapperService.class);
	}

	@Override
	public WebApiParameter deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {

		JsonToken t = jp.getCurrentToken();
		if (t == JsonToken.START_OBJECT) {
			jp.nextToken();
		}

		WebApiParameter param = new WebApiParameter();
		JsonNode valueNode = null;
		for (; jp.getCurrentToken() != JsonToken.END_OBJECT; jp.nextToken()) {
			String propName = jp.currentName();
			jp.nextToken();
			if (NAME_PROPERTY_NAME.equals(propName)) {
				String name = jp.getText();
				if (JsonToken.VALUE_NULL == jp.currentToken()) {
					throw JsonMappingException.from(ctxt, "name is null.");
				}
				param.setName(name);
			} else if (VALUE_TYPE_PROPERTY_NAME.equals(propName)) {
				String valType = jp.getText();
				if (JsonToken.VALUE_NULL == jp.currentToken()) {
					throw JsonMappingException.from(ctxt, "valueType is null.");
				}
				param.setValueType(valType);
			} else if (VALUE_PROPERTY_NAME.equals(propName)) {
				if (param.getValueType() != null) {
					//指定の型にマッピング
					param.setValue(readValue(param.getValueType(), jp, ctxt));

				} else {
					//この後に、valueTypeが指定されている可能性もあるので、一旦JsonNodeとして保持。
					valueNode = jp.readValueAsTree();
				}
			} else {
				//意味ない項目...
				jp.skipChildren();
			}
		}

		if (valueNode != null) {
			TreeTraversingParser ttp = new TreeTraversingParser(valueNode, jp.getCodec());
			if (param.getValueType() != null) {
				//指定の型にマッピング
				param.setValue(readValue(param.getValueType(), ttp, ctxt));
			} else {
				//Objectとしてマッピング（デフォルトのマッピング）
				param.setValue(ttp.readValueAs(Object.class));
			}
		}

		return param;
	}

	private Object readValue(String valueType, JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {

		Class<?> cl = mapperService.getMappedClass(valueType);
		if (cl == null) {
			throw JsonMappingException.from(ctxt, "unknown valueType:" + valueType);
		}

		switch (jp.getCurrentToken()) {
		case START_ARRAY:
			jp.nextToken();
			ArrayList<Object> array = new ArrayList<Object>();
			for (; jp.getCurrentToken() != JsonToken.END_ARRAY; jp.nextToken()) {
				if (jp.getCurrentToken() == JsonToken.VALUE_NULL) {
					array.add(null);
				} else {
					array.add(jp.readValueAs(cl));
				}
			}
			return array;
		default:
			return jp.readValueAs(cl);
		}
	}
}
