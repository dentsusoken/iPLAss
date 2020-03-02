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

package org.iplass.mtp.impl.query;

public interface QueryConstants {

	public static final String SELECT = "SELECT";
	public static final String DISTINCT = "DISTINCT";
	public static final String FROM = "FROM";
	public static final String REFER = "REFER";
	public static final String WHERE = "WHERE";
	public static final String GROUP = "GROUP";
	public static final String BY = "BY";
	public static final String HAVING = "HAVING";
	public static final String ORDER = "ORDER";
	public static final String ASC = "ASC";
	public static final String DESC = "DESC";
	public static final String NULLS = "NULLS";
	public static final String FIRST = "FIRST";
	public static final String LAST = "LAST";
	public static final String LIMIT = "LIMIT";
	public static final String OFFSET = "OFFSET";
	public static final String VERSIONED = "VERSIONED";
	public static final String LOCALIZED = "LOCALIZED";

//	public static final String AS = "AS";

	public static final String ON = "ON";

	public static final String AND = "AND";
	public static final String OR = "OR";
	public static final String NOT = "NOT";

	public static final String BETWEEN = "BETWEEN";
	public static final String EQUALS = "=";
	public static final String GREATER = ">";
	public static final String GREATER_EQUAL = ">=";
	public static final String IN = "IN";
	public static final String IS = "IS";
	public static final String NULL = "NULL";
	public static final String LESSER = "<";
	public static final String LESSER_EQUAL = "<=";
	public static final String LIKE = "LIKE";
	public static final String NOT_EQUALS = "!=";

	public static final String LEFT_PAREN = "(";
	public static final String RIGHT_PAREN = ")";
	public static final char LEFT_PAREN_CHAR = '(';
	public static final char RIGHT_PAREN_CHAR = ')';

	public static final String LEFT_BRACKET = "[";
	public static final String RIGHT_BRACKET = "]";

	public static final String LEFT_BRACE = "{";
	public static final String RIGHT_BRACE = "}";

	public static final String PLUS = "+";
	public static final String MINUS = "-";
	public static final String ASTER = "*";
	public static final String SOLID = "/";

	public static final String COMMA = ",";
	public static final String QUOTE = "'";
	public static final String DOT = ".";
	public static char COMMA_CHAR = ','; 
	public static char QUOTE_CHAR = '\''; 

	public static final String COUNT = "COUNT";
	public static final String SUM = "SUM";
	public static final String AVG = "AVG";
	public static final String MAX = "MAX";
	public static final String MIN = "MIN";
	public static final String STDDEV_POP = "STDDEV_POP";
	public static final String STDDEV_SAMP = "STDDEV_SAMP";
	public static final String VAR_POP = "VAR_POP";
	public static final String VAR_SAMP = "VAR_SAMP";
	public static final String MODE = "MODE";
	public static final String MEDIAN = "MEDIAN";
	
	public static final String RANK = "RANK";
	public static final String DENSE_RANK = "DENSE_RANK";
	public static final String PERCENT_RANK = "PERCENT_RANK";
	public static final String CUME_DIST = "CUME_DIST";
	public static final String ROW_NUMBER = "ROW_NUMBER";

	public static final String OVER = "OVER";
	public static final String PARTITION = "PARTITION";
	
	public static final String BOOLEAN_TRUE = "TRUE";
	public static final String BOOLEAN_FALSE = "FALSE";

	public static final String ARRAY = "ARRAY";

	public static final String ROLLUP = "ROLLUP";
	public static final String CUBE = "CUBE";

	public static final String CONTAINS = "CONTAINS";
	
	public static final String CASE = "CASE";
	public static final String WHEN = "WHEN";
	public static final String THEN = "THEN";
	public static final String ELSE = "ELSE";
	public static final String END = "END";
	
	public static final String CS = "CS";
	public static final String CI = "CI";
	
	public static final String CAST = "CAST";
	public static final String AS = "AS";
	public static final String OF = "OF";
	public static final String NOW = "NOW";
	public static final String UPDATE = "UPDATE";
	public static final String TIME = "TIME";
	
	public static final String LEFT_HINT_COMMENT = "/*+";
	public static final String RIGHT_HINT_COMMENT = "*/";
	public static final String HINT_INDEX = "INDEX";
	public static final String HINT_NO_INDEX = "NO_INDEX";
	public static final String HINT_NATIVE = "NATIVE";
	public static final String HINT_BIND = "BIND";
	public static final String HINT_NO_BIND = "NO_BIND";
	public static final String HINT_CACHE = "CACHE";
	public static final String HINT_CACHE_TRANSACTION_LOCAL = "TRANSACTION";
	public static final String HINT_CACHE_GLOBAL = "GLOBAL";
	public static final String HINT_FETCH_SIZE = "FETCH_SIZE";
	public static final String HINT_TIMEOUT = "TIMEOUT";
	public static final String SUPPRESS_WARNINGS = "SUPPRESS_WARNINGS";
	public static final String EXTERNAL_HINT = "@HINT";

}
