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
import org.apache.tika.parser.txt.TXTParser;

/**
 * <p>
 * BinaryReferenceのコンテンツ解析Parser。
 * tikaのTXTParserを利用してPDFの解析を行う。
 * </p>
 */
public class BinaryTextParser extends AbstractBinaryReferenceParser {


	private static Set<MediaType> customTypes;

	public BinaryTextParser() {

		//text/htmlを追加
		//HtmlParserと異なり、この場合は、タグも含めて解析される
		customTypes = Collections.unmodifiableSet(new HashSet<MediaType>(
				Arrays.asList(
						MediaType.text("html"),
						MediaType.application("xhtml+xml"),
						MediaType.application("vnd.wap.xhtml+xml"),
						MediaType.application("x-asp"))));
	}

	@Override
	protected Parser parserInstance() {
		return new TXTParser();
	}

	protected Set<MediaType> getCustomSupportTypes() {
		return customTypes;
	}

}
