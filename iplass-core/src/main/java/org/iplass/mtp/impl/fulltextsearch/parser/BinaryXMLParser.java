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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.xml.XMLParser;

/**
 * <p>
 * BinaryReferenceのコンテンツ解析Parser。
 * tikaのXMLParserを利用してPDFの解析を行う。
 * </p>
 * <p>
 * tika標準のXML用ParserはDcXMLParserであるが、
 * メタデータ情報を取得する処理が含まれているため、Index生成用コンテンツ解析には不要。
 * 直接XMLParserを利用する。
 * </p>
 */
public class BinaryXMLParser extends AbstractBinaryReferenceParser {

	private static Set<MediaType> customTypes;

	public BinaryXMLParser() {

		//text/xmlを追加
		customTypes = Collections.unmodifiableSet(new HashSet<MediaType>(
				Arrays.asList(MediaType.text("xml"))));
	}

	@Override
	protected Parser parserInstance() {
		return new XMLParser();
	}

	protected Set<MediaType> getCustomSupportTypes() {
		return customTypes;
	}

}
