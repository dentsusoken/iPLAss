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

import java.util.List;

import org.iplass.mtp.entity.query.hint.Hint;
import org.iplass.mtp.entity.query.hint.HintComment;
import org.iplass.mtp.impl.parser.EvalError;
import org.iplass.mtp.impl.parser.ParseContext;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.parser.Syntax;
import org.iplass.mtp.impl.parser.SyntaxContext;
import org.iplass.mtp.impl.query.QueryConstants;
import org.iplass.mtp.impl.query.QueryServiceHolder;

public class HintCommentSyntax implements Syntax<HintComment>, QueryConstants {
	
	private HintSyntax hint;

	public HintComment parse(ParseContext str) throws ParseException {
		
		// /*+
		if (!str.startsWith(LEFT_HINT_COMMENT)) {
			throw new ParseException(new EvalError("/*+ expected.", this, str));
		}
		str.consumeChars(LEFT_HINT_COMMENT.length());
		str.consumeChars(ParseContext.WHITE_SPACES);
		
		HintComment hc = new HintComment();
		
		//Hint
		while (!str.startsWith(RIGHT_HINT_COMMENT)) {
			int currentIndex = str.getCurrentIndex();
			try {
				hc.add(hint.parse(str));
				str.consumeChars(ParseContext.WHITE_SPACES);
			} catch (ParseException e) {
				//check is external hint
				str.setCurrentIndex(currentIndex);
				if (!externalHint(str, hc)) {
					throw e;
				}
			}
		}
		str.consumeChars(RIGHT_HINT_COMMENT.length());
		
		return hc;
	}
	
	private boolean externalHint(ParseContext str, HintComment hc) throws ParseException {
		if (!str.equalsNextToken(EXTERNAL_HINT, ParseContext.TOKEN_DELIMITERS)) {
			return false;
		}
		str.consumeChars(EXTERNAL_HINT.length());
		str.consumeChars(ParseContext.WHITE_SPACES);
		if (str.popChar() != LEFT_PAREN_CHAR) {
			throw new ParseException(new EvalError("( expected.", this, str));
		}
		str.consumeChars(ParseContext.WHITE_SPACES);
		String key = str.nextToken(ParseContext.TOKEN_DELIMITERS);
		if (key == null) {
			throw new ParseException(new EvalError("External Hint key expected.", this, str));
		}
		List<Hint> ehs = QueryServiceHolder.getInstance().getExternalHint(key);
		
		str.consumeChars(ParseContext.WHITE_SPACES);
		if (str.popChar() != RIGHT_PAREN_CHAR) {
			throw new ParseException(new EvalError(") expected.", this, str));
		}
		str.consumeChars(ParseContext.WHITE_SPACES);
		
		for (Hint h: ehs) {
			hc.add(h);
		}
		return true;
	}

	public void init(SyntaxContext context) {
		hint = context.getSyntax(HintSyntax.class);
	}

}
