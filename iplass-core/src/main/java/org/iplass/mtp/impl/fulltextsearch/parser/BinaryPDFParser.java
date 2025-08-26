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

import org.apache.tika.parser.Parser;
import org.apache.tika.parser.pdf.PDFParser;

/**
 * <p>
 * BinaryReferenceのコンテンツ解析Parser。
 * tikaのPDFParserを利用してPDFの解析を行う。
 * </p>
 * <p>
 * tika標準のPDF用ParserはJournalParserであるが、
 * 内部で実行されるGrobidRESTParserはメタデータ情報を取得する目的のため、
 * Index生成用コンテンツ解析には不要。直接PDFParserを利用する。
 * </p>
 * <p>
 * XFAフォームの内容は抽出しません。
 * </p>
 */
public class BinaryPDFParser extends AbstractBinaryReferenceParser {

	public BinaryPDFParser() {
	}

	@Override
	protected Parser parserInstance() {
		PDFParser parser = new PDFParser();
		parser.getPDFParserConfig().setExtractAcroFormContent(false);
		parser.getPDFParserConfig().setIfXFAExtractOnlyXFA(false);
		return parser;
	}

}
