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

package org.iplass.mtp.impl.query.value.primary;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.entity.query.value.primary.Literal;
import org.iplass.mtp.impl.parser.EvalError;
import org.iplass.mtp.impl.parser.ParseContext;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.parser.Syntax;
import org.iplass.mtp.impl.parser.SyntaxContext;
import org.iplass.mtp.impl.query.QueryConstants;
import org.iplass.mtp.impl.util.ConvertUtil;

public class LiteralSyntax implements Syntax<Literal>, QueryConstants {
	
	private static char QUOTE_CHAR = '\'';
	private static char PLUS_CHAR = '+';
	private static char MINUS_CHAR = '-';
	private static char EXP_CAHR_U = 'E';
	private static char EXP_CAHR_L = 'e';
	private static final char INTEGER_SUFFIX_U = 'I';
	private static final char INTEGER_SUFFIX_L = 'i';
	private static final char FLOAT_SUFFIX_U = 'F';
	private static final char FLOAT_SUFFIX_L = 'f';
	private static final char DECIMAL_SUFFIX_U = 'G';
	private static final char DECIMAL_SUFFIX_L = 'g';
	private static final char TIME_SUFFIX_U = 'T';
	private static final char TIME_SUFFIX_L = 't';
	private static final char DATE_SUFFIX_U = 'D';
	private static final char DATE_SUFFIX_L = 'd';
	private static final char DATETIME_SUFFIX_U = 'M';
	private static final char DATETIME_SUFFIX_L = 'm';
	private static final char SELECT_SUFFIX_U = 'S';
	private static final char SELECT_SUFFIX_L = 's';
	
	public void init(SyntaxContext context) {
	}
	
	public Literal parse(ParseContext str) throws ParseException {
		
		Object val = null;
		
		boolean isBind = true;
		if (str.startsWith(LEFT_HINT_COMMENT)) {
			str.consumeChars(LEFT_HINT_COMMENT.length());
			str.consumeChars(ParseContext.WHITE_SPACES);
			if (str.equalsNextToken(HINT_NO_BIND, ParseContext.TOKEN_DELIMITERS)) {
				str.consumeChars(HINT_NO_BIND.length());
				str.consumeChars(ParseContext.WHITE_SPACES);
				isBind = false;
			} else {
				throw new ParseException(new EvalError("only no_bind hint is valid.", this, str));
			}
			if (str.startsWith(RIGHT_HINT_COMMENT)) {
				str.consumeChars(RIGHT_HINT_COMMENT.length());
				str.consumeChars(ParseContext.WHITE_SPACES);
			} else {
				throw new ParseException(new EvalError("hint clause not terminated.", this, str));
			}
		}
		
		//null
		if (str.equalsNextToken(NULL, ParseContext.TOKEN_DELIMITERS)) {
			str.consumeChars(NULL.length());
			val = null;
		} else {
			char peekChar = str.peekChar();
			if (peekChar == QUOTE_CHAR) {
				//String
				val = str.innerToken(QUOTE_CHAR, true);
				if (val == null) {
					throw new ParseException(new EvalError("quoted string not terminated.", this, str));
				}
				//check suffix
				char nextChar = str.peekChar();
				if (nextChar == TIME_SUFFIX_U || nextChar == TIME_SUFFIX_L) {
					val = ConvertUtil.convertFromString(Time.class, (String) val);
					str.popChar();
				} else if (nextChar == DATE_SUFFIX_U || nextChar == DATE_SUFFIX_L) {
					val = ConvertUtil.convertFromString(Date.class, (String) val);
					str.popChar();
				} else if (nextChar == DATETIME_SUFFIX_U || nextChar == DATETIME_SUFFIX_L) {
					val = ConvertUtil.convertFromString(Timestamp.class, (String) val);
					str.popChar();
				} else if (nextChar == SELECT_SUFFIX_U || nextChar == SELECT_SUFFIX_L) {
					val = new SelectValue((String) val);
					str.popChar();
				} else if (nextChar == DECIMAL_SUFFIX_U || nextChar == DECIMAL_SUFFIX_L) {
					//Decimal
					int index = str.getCurrentIndex();
					try {
						val = new BigDecimal((String) val);
					} catch (NumberFormatException e) {
						str.setCurrentIndex(index);
						throw new ParseException(new EvalError("can not parse decimal(fixed-point) value.", this, str));
					}
					str.popChar();
				}
			} else if (peekChar == PLUS_CHAR || peekChar == MINUS_CHAR) {
				//check numeric case
				int index = str.getCurrentIndex();
				str.consumeChars(1);
				String literalExp = str.nextToken(ParseContext.TOKEN_DELIMITERS);
				if (literalExp == null) {
					str.setCurrentIndex(index);
					throw new ParseException(new EvalError("literal value expected.", this, str));
				}
				//number
				char suffix = literalExp.charAt(literalExp.length() - 1);
				if (suffix == DECIMAL_SUFFIX_U || suffix == DECIMAL_SUFFIX_L) {
					//Decimal
					try {
						if (peekChar == MINUS_CHAR) {
							val = new BigDecimal(MINUS + literalExp.substring(0, literalExp.length() - 1));
						} else {
							val = new BigDecimal(literalExp.substring(0, literalExp.length() - 1));
						}
					} catch (NumberFormatException e) {
						str.setCurrentIndex(index);
						throw new ParseException(new EvalError("can not parse decimal(fixed-point) value.", this, str));
					}
				} else if (suffix == FLOAT_SUFFIX_U || suffix == FLOAT_SUFFIX_L) {
					//Float
					try {
						if (peekChar == MINUS_CHAR) {
							val = Double.parseDouble(MINUS + literalExp.substring(0, literalExp.length() - 1));
						} else {
							val = Double.parseDouble(literalExp.substring(0, literalExp.length() - 1));
						}
					} catch (NumberFormatException e) {
						str.setCurrentIndex(index);
						throw new ParseException(new EvalError("can not parse floating value.", this, str));
					}
				} else if (suffix == INTEGER_SUFFIX_U || suffix == INTEGER_SUFFIX_L) {
					//Integer
					try {
						if (peekChar == MINUS_CHAR) {
							val = Long.parseLong(MINUS + literalExp.substring(0, literalExp.length() - 1));
						} else {
							val = Long.parseLong(literalExp.substring(0, literalExp.length() - 1));
						}
					} catch (NumberFormatException e) {
						str.setCurrentIndex(index);
						throw new ParseException(new EvalError("can not parse integer value.", this, str));
					}
				} else if (suffix == EXP_CAHR_U || suffix == EXP_CAHR_L) {
					//check exponent case. ( 1.5e-5 )
					if (str.peekChar() == MINUS_CHAR) {
						str.consumeChars(1);
						String expStr = str.nextToken(ParseContext.TOKEN_DELIMITERS);
						if (expStr == null) {
							str.setCurrentIndex(index);
							throw new ParseException(new EvalError("can not parse exponential value.", this, str));
						}
						char expSuffix = expStr.charAt(expStr.length() - 1);
						if (expSuffix == DECIMAL_SUFFIX_U || expSuffix == DECIMAL_SUFFIX_L) {
							//decimal
							try {
								if (peekChar == MINUS_CHAR) {
									val = new BigDecimal(MINUS + literalExp.substring(0, literalExp.length()) + MINUS + expStr.substring(0, expStr.length() - 1));
								} else {
									val = new BigDecimal(literalExp.substring(0, literalExp.length()) + MINUS + expStr.substring(0, expStr.length() - 1));
								}
							} catch (NumberFormatException e) {
								str.setCurrentIndex(index);
								throw new ParseException(new EvalError("can not parse exponential value.", this, str));
							}
						} else if (expSuffix == FLOAT_SUFFIX_U || expSuffix == FLOAT_SUFFIX_L) {
							//double
							try {
								if (peekChar == MINUS_CHAR) {
									val = Double.parseDouble(MINUS + literalExp.substring(0, literalExp.length()) + MINUS + expStr.substring(0, expStr.length() - 1));
								} else {
									val = Double.parseDouble(literalExp.substring(0, literalExp.length()) + MINUS + expStr.substring(0, expStr.length() - 1));
								}
							} catch (NumberFormatException e) {
								str.setCurrentIndex(index);
								throw new ParseException(new EvalError("can not parse exponential value.", this, str));
							}
						} else {
							//double
							try {
								if (peekChar == MINUS_CHAR) {
									val = Double.parseDouble(MINUS + literalExp.substring(0, literalExp.length()) + MINUS + expStr.substring(0, expStr.length()));
								} else {
									val = Double.parseDouble(literalExp.substring(0, literalExp.length()) + MINUS + expStr.substring(0, expStr.length()));
								}
							} catch (NumberFormatException e) {
								str.setCurrentIndex(index);
								throw new ParseException(new EvalError("can not parse exponential value.", this, str));
							}
						}
					} else {
						str.setCurrentIndex(index);
						throw new ParseException(new EvalError("can not parse exponential value.", this, str));
					}
					
				} else if (literalExp.contains(".")) {
					//Float
					try {
						if (peekChar == MINUS_CHAR) {
							val = Double.parseDouble(MINUS + literalExp);
						} else {
							val = Double.parseDouble(literalExp);
						}
					} catch (NumberFormatException e) {
						str.setCurrentIndex(index);
						throw new ParseException(new EvalError("can not parse floating value.", this, str));
					}
				} else {
					//Integer
					try {
						if (peekChar == MINUS_CHAR) {
							val = Long.parseLong(MINUS + literalExp);
						} else {
							val = Long.parseLong(literalExp);
						}
					} catch (NumberFormatException e) {
						str.setCurrentIndex(index);
						throw new ParseException(new EvalError("can not parse integer value.", this, str));
					}
				}
				
			} else {
				int index = str.getCurrentIndex();
				String literalExp = str.nextToken(ParseContext.TOKEN_DELIMITERS);
				if (literalExp == null) {
					str.setCurrentIndex(index);
					throw new ParseException(new EvalError("literal value expected.", this, str));
				}
				//Boolean
				if (literalExp.equalsIgnoreCase(BOOLEAN_TRUE)) {
					val = Boolean.TRUE;
				} else if (literalExp.equalsIgnoreCase(BOOLEAN_FALSE)) {
					val = Boolean.FALSE;
				} else {
					//number
					char suffix = literalExp.charAt(literalExp.length() - 1);
					if (suffix == DECIMAL_SUFFIX_U || suffix == DECIMAL_SUFFIX_L) {
						//Decimal
						try {
							val = new BigDecimal(literalExp.substring(0, literalExp.length() - 1));
						} catch (NumberFormatException e) {
							str.setCurrentIndex(index);
							throw new ParseException(new EvalError("can not parse decimal(fixed-point) value.", this, str));
						}
					} else if (suffix == FLOAT_SUFFIX_U || suffix == FLOAT_SUFFIX_L) {
						//Float
						try {
							val = Double.parseDouble(literalExp.substring(0, literalExp.length() - 1));
						} catch (NumberFormatException e) {
							str.setCurrentIndex(index);
							throw new ParseException(new EvalError("can not parse floating value.", this, str));
						}
					} else if (suffix == INTEGER_SUFFIX_U || suffix == INTEGER_SUFFIX_L) {
						//Integer
						try {
							val = Long.parseLong(literalExp.substring(0, literalExp.length() - 1));
						} catch (NumberFormatException e) {
							str.setCurrentIndex(index);
							throw new ParseException(new EvalError("can not parse integer value.", this, str));
						}
					} else if (suffix == EXP_CAHR_U || suffix == EXP_CAHR_L) {
						//check exponent case. ( 1.5e-5 )
						if (str.peekChar() == MINUS_CHAR) {
							str.consumeChars(1);
							String expStr = str.nextToken(ParseContext.TOKEN_DELIMITERS);
							if (expStr == null) {
								str.setCurrentIndex(index);
								throw new ParseException(new EvalError("can not parse exponential value.", this, str));
							}
							char expSuffix = expStr.charAt(expStr.length() - 1);
							if (expSuffix == DECIMAL_SUFFIX_U || expSuffix == DECIMAL_SUFFIX_L) {
								//decimal
								try {
									val = new BigDecimal(literalExp.substring(0, literalExp.length()) + MINUS + expStr.substring(0, expStr.length() - 1));
								} catch (NumberFormatException e) {
									str.setCurrentIndex(index);
									throw new ParseException(new EvalError("can not parse exponential value.", this, str));
								}
							} else if (expSuffix == FLOAT_SUFFIX_U || expSuffix == FLOAT_SUFFIX_L) {
								//double
								try {
									val = Double.parseDouble(literalExp.substring(0, literalExp.length()) + MINUS + expStr.substring(0, expStr.length() - 1));
								} catch (NumberFormatException e) {
									str.setCurrentIndex(index);
									throw new ParseException(new EvalError("can not parse exponential value.", this, str));
								}
							} else {
								//double
								try {
									val = Double.parseDouble(literalExp.substring(0, literalExp.length()) + MINUS + expStr.substring(0, expStr.length()));
								} catch (NumberFormatException e) {
									str.setCurrentIndex(index);
									throw new ParseException(new EvalError("can not parse exponential value.", this, str));
								}
							}
						} else {
							str.setCurrentIndex(index);
							throw new ParseException(new EvalError("can not parse exponential value.", this, str));
						}
						
					} else if (literalExp.contains(".")) {
						//Float
						try {
							val = Double.parseDouble(literalExp);
						} catch (NumberFormatException e) {
							str.setCurrentIndex(index);
							throw new ParseException(new EvalError("can not parse floating value.", this, str));
						}
					} else {
						//Integer
						try {
							val = Long.parseLong(literalExp);
						} catch (NumberFormatException e) {
							str.setCurrentIndex(index);
							throw new ParseException(new EvalError("can not parse integer value.", this, str));
						}
					}
				}
			}
		}
		
		str.consumeChars(ParseContext.WHITE_SPACES);
		
		return new Literal(val, isBind);

	}

}
