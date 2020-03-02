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

package org.iplass.mtp.impl.query.hint;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.query.hint.BindHint;
import org.iplass.mtp.entity.query.hint.CacheHint;
import org.iplass.mtp.entity.query.hint.CacheHint.CacheScope;
import org.iplass.mtp.entity.query.hint.FetchSizeHint;
import org.iplass.mtp.entity.query.hint.Hint;
import org.iplass.mtp.entity.query.hint.IndexHint;
import org.iplass.mtp.entity.query.hint.NativeHint;
import org.iplass.mtp.entity.query.hint.NoBindHint;
import org.iplass.mtp.entity.query.hint.NoIndexHint;
import org.iplass.mtp.entity.query.hint.SuppressWarningsHint;
import org.iplass.mtp.entity.query.hint.TimeoutHint;
import org.iplass.mtp.impl.parser.EvalError;
import org.iplass.mtp.impl.parser.ParseContext;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.parser.Syntax;
import org.iplass.mtp.impl.parser.SyntaxContext;
import org.iplass.mtp.impl.query.QueryConstants;

public class HintSyntax implements Syntax<Hint>, QueryConstants {
	private static char[] RIGHT_PAREN_DELS = {')'};
	
	public void init(SyntaxContext context) {
	}
	
	private List<String> parseList(ParseContext str) throws ParseException {
		str.consumeChars(ParseContext.WHITE_SPACES);
		if (str.peekChar() != LEFT_PAREN_CHAR) {
			return null;
		} else {
			str.consumeChars(1);
		}
		
		str.consumeChars(ParseContext.WHITE_SPACES);
		
		List<String> list = new ArrayList<>();
		boolean isFirst = true;
		while (!str.startsWith(RIGHT_PAREN) && !str.isEnd()) {
			if (isFirst) {
				isFirst = false;
			} else {
				if (!str.consumeChars(COMMA.length())) {
					throw new ParseException(new EvalError(", expected.", this, str));
				}
				str.consumeChars(ParseContext.WHITE_SPACES);
			}
			String val = str.nextToken(ParseContext.TOKEN_DELIMITERS);
			if (val == null) {
				throw new ParseException(new EvalError("hint's argument expected.", this, str));
			}
			list.add(val);
			str.consumeChars(ParseContext.WHITE_SPACES);
		}
		if (str.popChar() != RIGHT_PAREN_CHAR) {
			throw new ParseException(new EvalError(") expected.", this, str));
		}
		return list;
	}

	public Hint parse(ParseContext str) throws ParseException {
		int currentIndex = str.getCurrentIndex();
		String token = str.nextToken(ParseContext.TOKEN_DELIMITERS);
		if (token == null) {
			throw new ParseException(new EvalError("hint clause expected.", this, str));
		}
		
		token = token.toUpperCase();
		switch (token) {
		case HINT_INDEX:
			IndexHint ih = new IndexHint();
			ih.setPropertyNameList(parseList(str));
			return ih;
		case HINT_NO_INDEX:
			NoIndexHint nih = new NoIndexHint();
			nih.setPropertyNameList(parseList(str));
			return nih;
		case HINT_NATIVE:
			NativeHint nh = new NativeHint();
			str.consumeChars(ParseContext.WHITE_SPACES);
			if (str.popChar() != LEFT_PAREN_CHAR) {
				throw new ParseException(new EvalError("( expected.", this, str));
			}
			str.consumeChars(ParseContext.WHITE_SPACES);
			if (str.peekChar() != QUOTE_CHAR) {
				//table name
				String tableName = str.nextToken(ParseContext.TOKEN_DELIMITERS);
				if (tableName == null) {
					throw new ParseException(new EvalError("NativeHint table argument expected.", this, str));
				}
				str.consumeChars(ParseContext.WHITE_SPACES);
				if (str.popChar() != COMMA_CHAR) {
					throw new ParseException(new EvalError(", expected.", this, str));
				}
				str.consumeChars(ParseContext.WHITE_SPACES);
				nh.setTable(tableName);
			}
			String nhExp = str.innerToken(QUOTE_CHAR, true);
			if (nhExp == null) {
				throw new ParseException(new EvalError("NativeHint requires hint expression. Ex: native('ORDERED')", this, str));
			}
			nh.setHintExpression(nhExp);
			str.consumeChars(ParseContext.WHITE_SPACES);
			if (str.popChar() != RIGHT_PAREN_CHAR) {
				throw new ParseException(new EvalError(") expected.", this, str));
			}
			
			return nh;
		case HINT_BIND:
			BindHint bh = new BindHint();
			str.consumeChars(ParseContext.WHITE_SPACES);
			return bh;
		case HINT_NO_BIND:
			NoBindHint nbh = new NoBindHint();
			str.consumeChars(ParseContext.WHITE_SPACES);
			return nbh;
		case HINT_CACHE:
			CacheHint ch = new CacheHint();
			str.consumeChars(ParseContext.WHITE_SPACES);
			if (str.peekChar() != LEFT_PAREN_CHAR) {
				return ch;
			}
			str.consumeChars(1);
			str.consumeChars(ParseContext.WHITE_SPACES);
			if (str.equalsNextToken(HINT_CACHE_TRANSACTION_LOCAL, ParseContext.TOKEN_DELIMITERS)) {
				//transaction local指定
				ch.setScope(CacheScope.TRANSACTION);
				str.consumeChars(HINT_CACHE_TRANSACTION_LOCAL.length());
				str.consumeChars(ParseContext.WHITE_SPACES);
				if (str.peekChar() != RIGHT_PAREN_CHAR) {
					throw new ParseException(new EvalError(") expected.", this, str));
				}
				str.consumeChars(1);
				str.consumeChars(ParseContext.WHITE_SPACES);
				return ch;
			} else {
				//ttl指定
				String ttlStr = str.nextToken(RIGHT_PAREN_DELS);
				if (ttlStr != null) {
					try {
						ch.setTTL(Integer.parseInt(ttlStr.trim()));
					} catch(NumberFormatException e) {
						throw new ParseException(new EvalError("invalid TTL(or TRANSACTION scope) value.", this, str));
					}
				}
				str.consumeChars(1);
				str.consumeChars(ParseContext.WHITE_SPACES);
				return ch;
			}
		case HINT_FETCH_SIZE:
			FetchSizeHint fsh = new FetchSizeHint();
			str.consumeChars(ParseContext.WHITE_SPACES);
			if (str.popChar() != LEFT_PAREN_CHAR) {
				throw new ParseException(new EvalError("( expected.", this, str));
			}
			str.consumeChars(ParseContext.WHITE_SPACES);
			String sizeStr = str.nextToken(RIGHT_PAREN_DELS);
			try {
				fsh.setSize(Integer.parseInt(sizeStr.trim()));
			} catch(NumberFormatException e) {
				throw new ParseException(new EvalError("invalid size value.", this, str));
			}
			str.consumeChars(1);
			str.consumeChars(ParseContext.WHITE_SPACES);
			return fsh;
		case HINT_TIMEOUT:
			TimeoutHint th = new TimeoutHint();
			str.consumeChars(ParseContext.WHITE_SPACES);
			if (str.popChar() != LEFT_PAREN_CHAR) {
				throw new ParseException(new EvalError("( expected.", this, str));
			}
			str.consumeChars(ParseContext.WHITE_SPACES);
			String secStr = str.nextToken(RIGHT_PAREN_DELS);
			try {
				th.setSeconds(Integer.parseInt(secStr.trim()));
			} catch(NumberFormatException e) {
				throw new ParseException(new EvalError("invalid seconds value.", this, str));
			}
			str.consumeChars(1);
			str.consumeChars(ParseContext.WHITE_SPACES);
			return th;
		case SUPPRESS_WARNINGS:
			SuppressWarningsHint swh = new SuppressWarningsHint();
			str.consumeChars(ParseContext.WHITE_SPACES);
			return swh;
		default:
			str.setCurrentIndex(currentIndex);
			throw new ParseException(new EvalError("hint clause(INDEX/NO_INDEX/NATIVE/BIND/CACHE/FETCH_SIZE/TIMEOUT/SUPPRESS_WARNINGS) expected.", this, str));
		}
	}
}
