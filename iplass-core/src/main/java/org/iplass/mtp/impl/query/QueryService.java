/*
 * Copyright (C) 2018 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.query;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.iplass.mtp.entity.query.hint.Hint;
import org.iplass.mtp.entity.query.hint.HintComment;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.parser.SyntaxParser;
import org.iplass.mtp.impl.query.hint.HintCommentSyntax;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.spi.ServiceConfigrationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryService implements Service {
	private static Logger logger = LoggerFactory.getLogger(QueryService.class);
	
	private String externalHintFile;
	private Map<String, HintComment> externalHints;
	
	private SyntaxParser queryParser= new SyntaxParser(QuerySyntaxRegister.QUERY_CONTEXT);

	@Override
	public void init(Config config) {
		
		externalHints = new HashMap<>();
		
		@SuppressWarnings("unchecked")
		Map<String, String> inlineExternalHints = config.getValue("externalHints", Map.class);
		if (inlineExternalHints != null) {
			for (Map.Entry<String, String> e: inlineExternalHints.entrySet()) {
				try {
					HintComment hintComment = queryParser.parse("/*+ " + e.getValue() + " */", HintCommentSyntax.class);
					if (hintComment.getHintList() != null && hintComment.getHintList().size() > 0) {
						externalHints.put(e.getKey(), hintComment);
					}
				} catch (ParseException exp) {
					throw new ServiceConfigrationException("Can't parse externalHint:" + e.getValue(), exp);
				}
			}
		}
		externalHintFile = config.getValue("externalHintFile");
		if (externalHintFile != null) {
			Properties prop = getProperties(externalHintFile);
			for (String key: prop.stringPropertyNames()) {
				String val = prop.getProperty(key);
				if (val != null) {
					val = val.trim();
					if (!val.isEmpty()) {
						try {
							HintComment hintComment = queryParser.parse("/*+ " + val + " */", HintCommentSyntax.class);
							if (hintComment.getHintList() != null && hintComment.getHintList().size() > 0) {
								externalHints.put(key, hintComment);
							}
						} catch (ParseException e) {
							throw new ServiceConfigrationException("Can't parse externalHint:" + val, e);
						}
					}
				}
			}
		}
	}
	
	private Properties getProperties(String fileName) {
		Properties prop = new Properties();
		Path path = Paths.get(fileName);
		if (Files.exists(path)) {
			if (logger.isDebugEnabled()) {
				logger.debug("load ExternalHintFile from file path:" + fileName);
			}
			try (InputStreamReader is = new InputStreamReader(new FileInputStream(path.toFile()), "utf-8")) {
				prop.load(is);
			} catch (IOException e) {
				throw new ServiceConfigrationException(e);
			}
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("load ExternalHintFile from classpath:" + fileName);
			}
			try (InputStream is = getClass().getResourceAsStream(fileName)) {
				if (is == null) {
					logger.error("ExternalHintFile:" + fileName + " not found.Can not initialize QueryService.");
					throw new ServiceConfigrationException("ExternalHintFile:" + fileName + " Not Found.");
				}
				
				InputStreamReader isr = new InputStreamReader(is, "utf-8");
				prop.load(isr);

			} catch (IOException e) {
				throw new ServiceConfigrationException(e);
			}
		}
		return prop;
	}
	
	@Override
	public void destroy() {
	}
	
	public SyntaxParser getQueryParser() {
		return queryParser;
	}
	
	public String getExternalHintFile() {
		return externalHintFile;
	}

	public List<Hint> getExternalHint(String key) {
		if (externalHints == null) {
			return Collections.emptyList();
		}
		HintComment hc = externalHints.get(key);
		if (hc == null) {
			return Collections.emptyList();
		}
		HintComment copy = (HintComment) hc.copy();
		return copy.getHintList();
	}

}
