/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.xml.jaxb;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.HandlerBase;
import org.xml.sax.InputSource;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 外部参照をできなくしたSaxParserFactory。
 *
 * @author K.Higuchi
 *
 */
@SuppressWarnings("deprecation")
public final class SecureSAXParserFactory extends SAXParserFactory {
	//jerseyのSecureSAXParserFactoryを参考に

	private static Logger logger = LoggerFactory.getLogger(SecureSAXParserFactory.class);

	private static final EntityResolver EMPTY_ENTITY_RESOLVER = new EntityResolver() {
	    public InputSource resolveEntity(String publicId, String systemId) {
	        return new InputSource(new ByteArrayInputStream(new byte[0]));
	    }
	};

    private final SAXParserFactory spf;

    public SecureSAXParserFactory(SAXParserFactory spf) {
		this.spf = spf;
		try {
			spf.setFeature("http://xml.org/sax/features/external-general-entities", Boolean.FALSE);
		} catch (Exception e) {
			logger.warn("cant set SAXParserFactory future:" + "http://xml.org/sax/features/external-general-entities", e);
		}
		try {
			spf.setFeature("http://xml.org/sax/features/external-parameter-entities", Boolean.FALSE);
		} catch (Exception e) {
			logger.warn("cant set SAXParserFactory future:" + "http://xml.org/sax/features/external-parameter-entities", e);
		}
		try {
			spf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, Boolean.TRUE);
		} catch (Exception e) {
			logger.warn("cant set SAXParserFactory future:" + XMLConstants.FEATURE_SECURE_PROCESSING, e);
		}
	}

	@Override
	public void setNamespaceAware(boolean b) {
		spf.setNamespaceAware(b);
	}

	@Override
	public void setValidating(boolean b) {
		spf.setValidating(b);
	}

	@Override
	public boolean isNamespaceAware() {
		return spf.isNamespaceAware();
	}

	@Override
	public boolean isValidating() {
		return spf.isValidating();
	}

	@Override
	public Schema getSchema() {
		return spf.getSchema();
	}

	@Override
	public void setSchema(Schema schema) {
		spf.setSchema(schema);
	}

	@Override
	public void setXIncludeAware(boolean b) {
		spf.setXIncludeAware(b);
	}

	@Override
	public boolean isXIncludeAware() {
		return spf.isXIncludeAware();
	}

	@Override
	public SAXParser newSAXParser() throws ParserConfigurationException,
			SAXException {
		return new WrappingSAXParser(spf.newSAXParser());
	}

	@Override
	public void setFeature(String s, boolean b)
			throws ParserConfigurationException, SAXNotRecognizedException,
			SAXNotSupportedException {
		spf.setFeature(s, b);
	}

	@Override
	public boolean getFeature(String s) throws ParserConfigurationException,
			SAXNotRecognizedException, SAXNotSupportedException {
		return spf.getFeature(s);
	}

	private static final class WrappingSAXParser extends SAXParser {
		private final SAXParser sp;

		protected WrappingSAXParser(SAXParser sp) {
			this.sp = sp;
		}

		@Override
		public void reset() {
			sp.reset();
		}

		@Override
		public void parse(InputStream inputStream, HandlerBase handlerBase)
				throws SAXException, IOException {
			sp.parse(inputStream, handlerBase);
		}

		@Override
		public void parse(InputStream inputStream, HandlerBase handlerBase,
				String s) throws SAXException, IOException {
			sp.parse(inputStream, handlerBase, s);
		}

		@Override
		public void parse(InputStream inputStream, DefaultHandler defaultHandler)
				throws SAXException, IOException {
			sp.parse(inputStream, defaultHandler);
		}

		@Override
		public void parse(InputStream inputStream,
				DefaultHandler defaultHandler, String s) throws SAXException,
				IOException {
			sp.parse(inputStream, defaultHandler, s);
		}

		@Override
		public void parse(String s, HandlerBase handlerBase)
				throws SAXException, IOException {
			sp.parse(s, handlerBase);
		}

		@Override
		public void parse(String s, DefaultHandler defaultHandler)
				throws SAXException, IOException {
			sp.parse(s, defaultHandler);
		}

		@Override
		public void parse(File file, HandlerBase handlerBase)
				throws SAXException, IOException {
			sp.parse(file, handlerBase);
		}

		@Override
		public void parse(File file, DefaultHandler defaultHandler)
				throws SAXException, IOException {
			sp.parse(file, defaultHandler);
		}

		@Override
		public void parse(InputSource inputSource, HandlerBase handlerBase)
				throws SAXException, IOException {
			sp.parse(inputSource, handlerBase);
		}

		@Override
		public void parse(InputSource inputSource, DefaultHandler defaultHandler)
				throws SAXException, IOException {
			sp.parse(inputSource, defaultHandler);
		}

		@Override
		public Parser getParser() throws SAXException {
			return sp.getParser();
		}

		@Override
		public XMLReader getXMLReader() throws SAXException {
			XMLReader r = sp.getXMLReader();
			r.setEntityResolver(EMPTY_ENTITY_RESOLVER);
			return r;
		}

		@Override
		public boolean isNamespaceAware() {
			return sp.isNamespaceAware();
		}

		@Override
		public boolean isValidating() {
			return sp.isValidating();
		}

		@Override
		public void setProperty(String s, Object o)
				throws SAXNotRecognizedException, SAXNotSupportedException {
			sp.setProperty(s, o);
		}

		@Override
		public Object getProperty(String s) throws SAXNotRecognizedException,
				SAXNotSupportedException {
			return sp.getProperty(s);
		}

		@Override
		public Schema getSchema() {
			return sp.getSchema();
		}

		@Override
		public boolean isXIncludeAware() {
			return sp.isXIncludeAware();
		}
	}
}
