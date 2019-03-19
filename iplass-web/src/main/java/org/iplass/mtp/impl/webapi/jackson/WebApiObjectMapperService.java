/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.spi.ServiceConfigrationException;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


public class WebApiObjectMapperService implements Service {

	private ObjectMapper mapper;

	private Map<String, Class<?>> typeMap;
	private List<MixinConfig> mixin;
	private boolean writeNullMapValues = false;
	private boolean writeNullValues = false;
	private boolean escapeNonAscii = false;

	public List<MixinConfig> getMixin() {
		return mixin;
	}

	public boolean isEscapeNonAscii() {
		return escapeNonAscii;
	}

	public boolean isWriteNullValues() {
		return writeNullValues;
	}

	public boolean isWriteNullMapValues() {
		return writeNullMapValues;
	}

	@Override
	public void init(Config config) {
		
		if (config.getValue("writeNullValues") != null) {
			writeNullValues = Boolean.valueOf(config.getValue("writeNullValues"));
		}
		if (config.getValue("writeNullMapValues") != null) {
			writeNullMapValues = Boolean.valueOf(config.getValue("writeNullMapValues"));
		}
		if (config.getValue("escapeNonAscii") != null) {
			escapeNonAscii = Boolean.valueOf(config.getValue("escapeNonAscii"));
		}
		
		mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
		//for backward compatibility
		mapper.configOverride(java.sql.Date.class).setFormat(JsonFormat.Value.forPattern("yyyy-MM-dd"));
		
		if (escapeNonAscii) {
			mapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
		}
		
		if (writeNullValues) {
			mapper.setSerializationInclusion(Include.ALWAYS);
		} else {
			mapper.setSerializationInclusion(Include.NON_NULL);
		}
		if (writeNullMapValues) {
			mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, true);
		} else {
			mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
		}
		
		mixin = config.getValues("mixin", MixinConfig.class);
		if (mixin != null && mixin.size() > 0) {
			for (MixinConfig mc: mixin) {
				mapper.addMixIn(mc.getTarget(), mc.getMixinSource());
			}
		}
		

		typeMap = new HashMap<String, Class<?>>();
		List<WebApiParameterType> typeList = (List<WebApiParameterType>) config.getValues("parameterType", WebApiParameterType.class);
		if (typeList != null && typeList.size() > 0) {
			for (WebApiParameterType t: typeList) {
				try {
					typeMap.put(t.getTypeName(), Class.forName(t.getClassName()));
				} catch (ClassNotFoundException e) {
					throw new ServiceConfigrationException("cant find Class of WebApiParameterType:" + t.getClassName(), e);
				}
			}
		}
	}

	@Override
	public void destroy() {
		mapper = null;
		typeMap = null;
	}

	public ObjectMapper getObjectMapper() {
		return mapper;
	}


	public Class<?> getMappedClass(String type) {
		return typeMap.get(type);
	}


}
