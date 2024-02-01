/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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


public class ParseContext {
	
	//TODO パース処理時のParseExceptionの共用
	
	
	/**
	 * 空白として認識するchar。
	 * ' ', '\t', '\n', '\r', '\f', '\b'
	 */
	public static char[] WHITE_SPACES = {' ', '\t', '\n', '\r', '\f', '\b'};//TODO 外部（SytaxContext）から設定可能に
	public static char[] TOKEN_DELIMITERS = {' ', '\t', '\n', '\r', '\f', '\b', '(', ')', '[', ']', ',', '+', '-', '*', '/','=','!','>','<'};//TODO 外部（SytaxContext）から設定可能に
	
	private String stream;
	private int currentIndex;
//	private EntityContext entityContext;
	
	private int offset;
	
	public ParseContext() {
	}
	
	public ParseContext(String stream/*, EntityContext entityContext*/) {
		this.stream = stream;
//		this.entityContext = entityContext;
	}
	
	ParseContext(String stream, /*EntityContext entityContext,*/ int offset) {
		this(stream/*, entityContext*/);
		this.offset = offset;
	}
	
//	public EntityContext getContext() {
//		return entityContext;
//	}

	public int totalCurrentIndex() {
		return currentIndex + offset;
	}
	
	/**
	 * 指定の文字列でリセットする。
	 * このインスタンスを再利用する際に使用する。
	 *
	 * @param stream
	 */
	public void reset(String stream) {
		this.stream = stream;
		currentIndex = 0;
		offset = 0;
	}

	/**
	 *
	 * @param currentIndex
	 * @throws ParseException
	 */
	public void setCurrentIndex(int currentIndex) throws ParseException {
		if (currentIndex < 0 || currentIndex > stream.length()) {
			throw new ParseException(new EvalError("stream is end.", null, this));
			
		}
		this.currentIndex = currentIndex;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public int getLength() {
		return stream.length();
	}

	/**
	 * 
	 * @return
	 */
	public char peekChar() {
		if (currentIndex >= stream.length()) {
			return 0;
		}
		return stream.charAt(currentIndex);
	}

	/**
	 *
	 * @return
	 */
	public char popChar() {
		if (currentIndex == stream.length()) {
			return 0;
		}
		char res = stream.charAt(currentIndex);
		currentIndex++;
		return res;
	}
	
	/**
	 *
	 * @throws IndexOutOfBoundsException すでに先頭である場合
	 */
	public void pushBack() throws IndexOutOfBoundsException {
		if (currentIndex == 0) {
			throw new StringIndexOutOfBoundsException("already top of stream");
		}
		currentIndex--;
	}

	public boolean isEnd() {
		return currentIndex == stream.length();
	}

	/**
	 * 現在の文字列シーケンスから、指定のbegin、endで文字列を切り出す。
	 *
	 * @param begin
	 * @param end
	 * @return
	 * @throws IndexOutOfBoundsException
	 */
	public ParseContext subContext(int begin, int end) throws IndexOutOfBoundsException {
		return new ParseContext(stream.substring(begin,end)/*, entityContext*/, offset + begin);
	}


	/**
	 * 現在のindex以降で最初に指定の文字が現れるindexを返す。
	 * 見つからない場合は-1。
	 *
	 * @param c
	 * @return
	 */
	public int indexOf(char c) {
		for (int i = currentIndex; i < stream.length(); i++) {
			if (c == stream.charAt(i)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 現在のindex以降で、最後に指定の文字が現れるindexを返す。
	 * 見つからない場合は-1。
	 *
	 * @param c
	 * @return
	 */
	public int lastIndexOf(char c) {
		for (int i = stream.length() - 1 ; i >= currentIndex; i--) {
			if (c == stream.charAt(i)) {
				return i;
			}
		}
		return -1;

	}

	/**
	 * 現在のindexから、指定されたignore文字以外の文字が現れるまで、
	 * popChar()する。
	 * もし、ひとつもpopChar()しなかった場合は、falseがかえる。
	 *
	 * @param ignores 消費文字列
	 * @return
	 */
	public boolean consumeChars(char[] ignores) {
		boolean isConsume = false;
		while (isEnd() == false) {
			char c = peekChar();
			if (isContains(c, ignores)) {
				popChar();
				isConsume = true;
			} else {
				break;
			}
		}
		return isConsume;
	}
	
	public boolean consumeChars(int length) throws ParseException {
//		if (currentIndex + length < 0 || currentIndex + length >= stream.length()) {
//			return false;
//		}
		setCurrentIndex(getCurrentIndex() + length);
		return true;
	}

	private boolean isContains(char c, char[] checkChars) {
		for (int i = 0; i < checkChars.length; i++) {
			if (c == checkChars[i]) {
				return true;
			}
		}
		return false;

	}

	/**
	 * delimitersで指定されている文字が現れるか、stream最後になるまで次のTokenとして切り出す。
	 * いきなり、delimitersがきたり、いきなりstream最後の場合は、null。
	 *
	 * @param delimiters
	 * @return
	 */
	public String nextToken(char[] delimiters) {
		//consumeChars(delimiters);
		int beginIndex = getCurrentIndex();

		while (isEnd() == false) {
			char c = popChar();
			if (isContains(c, delimiters)) {
				pushBack();
				break;
			}
		}

		int endIndex = getCurrentIndex();
		if (beginIndex < endIndex) {
			return stream.substring(beginIndex, endIndex);
		} else {
			return null;
		}
	}
	
	/**
	 * delimiterで指定されている文字で囲まれている文字を切り出す。
	 * いきなり、delimiterで囲まれていなかったり、いきなりstream最後の場合は、null。
	 * 切り出した後の、indexのポジションは、最後のdelimiterの後。
	 *
	 * @param delimiter
	 * @param withDoubleDelimierEscape delimiterで指定していた文字が重ねられていた場合エスケープとして扱う場合true
	 * @param escapeChar
	 * @return
	 */
	public String innerToken(char delimiter, boolean withDoubleDelimierEscape) {
		//先頭のdelimiterチェック
		char del1 = popChar();
		if (del1 != delimiter) {
			return null;
		}
		StringBuilder sb = new StringBuilder();

		while (true) {
			if (isEnd()) {
				return null;
			}
			
			char c = popChar();
			if (c == delimiter) {
				if (!withDoubleDelimierEscape) {
					break;
				} else if (peekChar() != delimiter) {
					break;
				} else {
					popChar();//重ねられている'を消費
				}
			}
			sb.append(c);
		}
		
		return sb.toString();
		
	}

	/**
	 * 現在のインデックス以降が指定のprefixで始まるかどうかを返す。
	 *
	 * @param prefix
	 * @return
	 */
	public boolean startsWith(String prefix) {
		return stream.startsWith(prefix, currentIndex);
	}
	
	public boolean equalsNextToken(String expectedToken, char[] delimiters) throws ParseException {
		int i = getCurrentIndex();
		String token = nextToken(delimiters);
		setCurrentIndex(i);
		return expectedToken.equalsIgnoreCase(token);
//
//		return stream.regionMatches(true, currentIndex, token, 0, token.length());
	}

	public String toString() {
		return stream.toString();
	}

}
