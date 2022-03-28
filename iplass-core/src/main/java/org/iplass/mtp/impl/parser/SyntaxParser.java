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

package org.iplass.mtp.impl.parser;

import org.iplass.mtp.entity.query.ASTNode;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SyntaxParser {
	
	private static Logger logger = LoggerFactory.getLogger(SyntaxParser.class);
	
	private SyntaxContext sc;

	public SyntaxParser(String contextName) {
		SyntaxService service = ServiceRegistry.getRegistry().getService(SyntaxService.class);
		sc = service.getSyntaxContext(contextName);
	}
	
	/**
	 * パース処理を行います。パース処理が終わっていない場合はエラーとします。
	 * 
	 * @param src 文字列
	 * @param parseAs 構文
	 * @return ASTNode
	 * @throws ParseException
	 */
	public <T extends ASTNode> T parse(String src, Class<? extends Syntax<T>> parseAs) throws ParseException {
		ParseContext ctx = new ParseContext(src);
		return parseContext(ctx, parseAs, false);
		
	}
	
	/**
	 * パース処理を行います。パース処理が終わっていない場合もエラーとはしません。
	 * 
	 * @param src 文字列
	 * @param parseAs 構文
	 * @return ASTNode
	 * @throws ParseException
	 */
	public <T extends ASTNode> T parse(ParseContext ctx, Class<? extends Syntax<T>> parseAs) throws ParseException {
		return parseContext(ctx, parseAs, true);
		
	}
	
	private <T extends ASTNode> T parseContext(ParseContext ctx, Class<? extends Syntax<T>> parseAs, boolean isContinueParse) throws ParseException {
		long time = 0;
		if (logger.isTraceEnabled()) {
			time = System.nanoTime();
		}
		
		Syntax<T> syntax = sc.getSyntax(parseAs);
		
		ctx.consumeChars(ParseContext.WHITE_SPACES);
		
		T node = syntax.parse(ctx);
		
		ctx.consumeChars(ParseContext.WHITE_SPACES);
		
		if (logger.isTraceEnabled()) {
			logger.trace("parse query:time=" + ((double) (System.nanoTime() - time)/1000000) + "ms.");
		}
		
		if (!ctx.isEnd() && !isContinueParse) {
			throw new ParseException(new EvalError("Cant handle next token.", syntax, ctx));
		}
		return node;
	}

}
