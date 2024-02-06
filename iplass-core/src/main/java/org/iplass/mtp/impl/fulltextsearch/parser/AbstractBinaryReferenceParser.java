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

package org.iplass.mtp.impl.fulltextsearch.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.fulltextsearch.FulltextSearchRuntimeException;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public abstract class AbstractBinaryReferenceParser implements BinaryReferenceParser {

	private static Logger logger = LoggerFactory.getLogger(AbstractBinaryReferenceParser.class);

	private Set<MediaType> supportTypes;

	public AbstractBinaryReferenceParser() {
	}

	@Override
	public BinaryReferenceParser getParser(BinaryReference br) {

		MediaType brType = MediaType.parse(br.getType());
		if (brType != null) {
			if (getSupportTypes().contains(brType)) {
				return this;
			}
			if (getCustomSupportTypes() != null && getCustomSupportTypes().contains(brType)) {
				return this;
			}
		}
		return null;
	}

	@Override
	public String parse(BinaryReference br, int writeLimit) {
		EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);

		try (InputStream is = em.getInputStream(br)) {

			Parser parser = parserInstance();
			ContentHandler handler = new BodyContentHandler(writeLimit);
			Metadata metadata = new Metadata();

			try {
				parser.parse(is, handler, metadata, new ParseContext());
			} catch (SAXException e) {
				//privateクラスのためクラス名で判定
				if (e.getClass().getName().equals("org.apache.tika.sax.WriteOutContentHandler$WriteLimitReachedException")) {
					//コンテンツのLimitに引っかかった場合はWARNログを出して、今までの結果を出力
					logger.warn(br.getName() + " contained more than " + writeLimit + " characters. so cut " + writeLimit + " characters.");
				} else {
					throw new BinaryReferenceParseException("Exception occured on index creating process.", e);
				}
			} catch (TikaException e) {
				throw new BinaryReferenceParseException("Exception occured on index creating process.", e);
			} catch (Throwable e) {
				//タイプに一致するParserで必要なClassがなかった場合は、BinaryReferenceParseExceptionをthrow
				if (e instanceof NoClassDefFoundError) {
					throw new BinaryReferenceParseException("Exception occured on index creating process.", e);
				} else {
					throw e;
				}
			}

			String content = handler.toString();
			//logger.trace(br.getName() + " tika metadata is {" + metadata.toString() + "}");
			logger.trace(br.getName() + " tika content is [" + content + "]");

			if (StringUtil.isNotEmpty(content)) {
				return br.getName() + " " + br.getType() + " " + content;
			} else {
				return null;
			}

		} catch (IOException e) {
			throw new FulltextSearchRuntimeException("Exception occured on index creating process.", e);
		}
	}

	protected Set<MediaType> getCustomSupportTypes() {
		return null;
	}

	protected Set<MediaType> getSupportTypes() {
		if (supportTypes == null) {
			supportTypes = parserInstance().getSupportedTypes(new ParseContext());
		}
		return supportTypes;

	}

	protected abstract Parser parserInstance();

}
