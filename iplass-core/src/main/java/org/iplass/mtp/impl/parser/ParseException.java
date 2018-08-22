/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

public class ParseException extends Exception {
	private static final long serialVersionUID = -6001769683742261519L;
	
	private EvalError evalError;

	public ParseException() {
	}
	
	public ParseException(EvalError evalError) {
		
		//FIXME ここHotSpot！！！
		//エラー情報をParseCOntextに保持する方向で。最終的なエラーの場合だけ、new ParseException
		
		
		super(evalError.getMessage() + " at " + evalError.getPosition() + " (..." + concat(evalError.getParsedString(), evalError.getPosition()) + "...)");
		this.evalError = evalError;
	}
	
	private static String concat(String str, int position) {
		if (str == null) {
			return "";
		}
		int start = 0;
		if (position - 20 > 0) {
			start = position - 20;
		}
		int end = str.length();
		if (position + 20 < end) {
			end = position + 20;
		}
		return str.substring(start, position) + "^" + str.substring(position, end);
	}
	

//	public ParseException(String message, Throwable cause) {
//		super(message, cause);
//	}
//
//	public ParseException(String message) {
//		super(message);
//	}
//
//	public ParseException(Throwable cause) {
//		super(cause);
//	}
	
	public EvalError getEvalError() {
		return evalError;
	}

}
