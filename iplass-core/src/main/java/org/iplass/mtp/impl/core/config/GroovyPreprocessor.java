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
package org.iplass.mtp.impl.core.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.codehaus.groovy.control.CompilationFailedException;
import org.iplass.mtp.spi.ServiceConfigrationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import groovy.text.GStringTemplateEngine;
import groovy.text.Template;

public class GroovyPreprocessor implements ConfigPreprocessor {
	
	private static Logger logger = LoggerFactory.getLogger(GroovyPreprocessor.class);
	
	private Pattern pattern = Pattern.compile("(?i)<serviceDefinition\\s*preprocess\\s*=\\s*(\"|')true(\"|')\\s*>");

	@Override
	public String preprocess(String xml, String filePath) {
		
		if (pattern.matcher(xml).find()) {
			if (logger.isDebugEnabled()) {
				logger.debug("preprocess " + filePath);
			}
			try {
				GStringTemplateEngine eng = new GStringTemplateEngine();
				Template tmpl = eng.createTemplate(xml);
				Map<String, Object> binding = new HashMap<>();
				binding.put("filePath", filePath);
				
				xml = tmpl.make(binding).toString();
				
			} catch (IOException | CompilationFailedException | ClassNotFoundException e) {
				throw new ServiceConfigrationException("preprocess failed:" + filePath, e);
			}
			
		}
		return xml;
	}
}
