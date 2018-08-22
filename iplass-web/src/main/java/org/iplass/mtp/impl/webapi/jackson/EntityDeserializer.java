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
package org.iplass.mtp.impl.webapi.jackson;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;

import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.property.PrimitivePropertyHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.entity.property.ReferencePropertyHandler;
import org.iplass.mtp.impl.properties.extend.ExpressionType;
import org.iplass.mtp.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.ibm.icu.math.BigDecimal;

/**
 * Entity-JSONマッピングのためのカスタムのJsonDeserializer。
 *
 * @author K.Higuchi
 *
 */
public class EntityDeserializer extends StdDeserializer<Entity> {
	private static final long serialVersionUID = 8180920461950919037L;

	private static Logger logger = LoggerFactory.getLogger(EntityDeserializer.class);

	public EntityDeserializer() {
		super(Entity.class);
	}
	public EntityDeserializer(Class<?> vc) {
		super(vc);
	}

	public EntityDeserializer(JavaType valueType) {
		super(valueType);
	}

	public EntityDeserializer(StdDeserializer<?> src) {
		super(src);
	}
	
	private Object getValue(EntityContext ec, EntityHandler eh, PropertyHandler ph, JsonNode node, DeserializationContext ctxt) throws IOException {
		
		if (node.isNull()) {
			return null;
		}

		PropertyDefinitionType pdType = ph.getEnumType();
		if (pdType == PropertyDefinitionType.EXPRESSION) {
			ExpressionType et = (ExpressionType) ((PrimitivePropertyHandler) ph).getMetaData().getType();
			if (et.getResultType() != null) {
				pdType = et.getResultType();
			} else {
				pdType = PropertyDefinitionType.STRING;
			}
		}
		
		switch (pdType) {
		case AUTONUMBER:
		case LONGTEXT:
		case STRING:
			return node.asText();
		case BINARY:
			if (node.isObject()) {
				return node.traverse(ctxt.getParser().getCodec()).readValueAs(BinaryReference.class);
			} else {
				BinaryReference br = new BinaryReference();
				br.setLobId(node.asLong());
				return br;
			}
		case BOOLEAN:
			return node.asBoolean();
		case DATE:
			//yyyy-MM-dd
			String dateStr = node.asText();
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//TODO ThradLocalCache?
			SimpleDateFormat sdf = DateUtil.getSimpleDateFormat("yyyy-MM-dd", false);
			try {
				return new java.sql.Date(sdf.parse(dateStr).getTime());
			} catch (ParseException e) {
				throw ctxt.mappingException("cant parse to Date. date:" + dateStr);
			}
		case DATETIME:
			//timestamp (long)
			return new java.sql.Timestamp(ctxt.parseDate(node.asText()).getTime());

		case TIME:
			//HH:mm:ss
			String timeStr = node.asText();
//			SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");//TODO ThradLocalCache?
			SimpleDateFormat sdfTime = DateUtil.getSimpleDateFormat("HH:mm:ss", false);
			try {
				return sdfTime.parse(timeStr);
			} catch (ParseException e) {
				throw ctxt.mappingException("cant parse to Date. date:" + timeStr);
			}
		case DECIMAL:
			String decStr = node.asText();
			return new BigDecimal(decStr);
		case FLOAT:
			return node.asDouble();
		case INTEGER:
			return node.asLong();
		case REFERENCE:
			EntityHandler refEh = ((ReferencePropertyHandler) ph).getReferenceEntityHandler(ec);
			return toEntity(ec, refEh, node, ctxt);
		case SELECT:
			if (node.isObject()) {
				return node.traverse(ctxt.getParser().getCodec()).readValueAs(SelectValue.class);
			} else {
				SelectValue sv = new SelectValue();
				sv.setValue(node.asText());
				return sv;
			}
		default:
			return null;
		}
	}
	
	private Entity toEntity(EntityContext ec, EntityHandler eh, JsonNode node, DeserializationContext ctxt) throws IOException {
		Entity entity = eh.newInstance();
		
		for (Iterator<Map.Entry<String, JsonNode>> it = node.fields(); it.hasNext();) {
			Map.Entry<String, JsonNode> e = it.next();
			PropertyHandler ph = eh.getProperty(e.getKey(), ec);
			if (ph == null) {
				if (!EntitySerializer.PROP_DEFINITION_NAME.equals(e.getKey())) {
					//unknown property...
					logger.warn("unknown property name:" + e.getKey() + " of Entity:" + eh.getMetaData().getName() + ", so ignore value");
				}
			} else {
				Object propVal = null;
				Object[] arrayVal = null;
				JsonNode value = e.getValue();
				if (value != null && !value.isNull()) {
					if (value.isArray()) {
						if (ph.getMetaData().getMultiplicity() == 1) {
							if (value.size() > 0) {
								propVal = getValue(ec, eh, ph, value.get(0), ctxt);
							}
						} else {
							arrayVal = ph.newArrayInstance(value.size(), ec);
							for (int i = 0; i < arrayVal.length; i++) {
								arrayVal[i] = getValue(ec, eh, ph, value.get(i), ctxt);
							}
						}
					} else {
						if (ph.getMetaData().getMultiplicity() == 1) {
							propVal = getValue(ec, eh, ph, value, ctxt);
						} else {
							arrayVal = ph.newArrayInstance(1, ec);
							arrayVal[0] = getValue(ec, eh, ph, value, ctxt);
						}
					}
				}
				
				if (propVal != null) {
					entity.setValue(e.getKey(), propVal);
				}
				if (arrayVal != null) {
					entity.setValue(e.getKey(), arrayVal);
				}
			}
		}
		
		return entity;
	}

	@Override
	public Entity deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		JsonNode buf = jp.getCodec().readTree(jp);
		EntityContext ec = EntityContext.getCurrentContext();
		String defName = null;
		JsonNode defNode = buf.get(EntitySerializer.PROP_DEFINITION_NAME);
		if (defNode != null) {
			defName = defNode.asText();
		}
		EntityHandler eh = ec.getHandlerByName(defName);
		if (eh == null) {
			throw ctxt.mappingException("cant find Entity Defs... definitionName:" + defName);
		}
		return toEntity(ec, eh, buf, ctxt);
	}

}
