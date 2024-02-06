/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.gem.command;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Stack;

import org.iplass.mtp.web.ResultStreamWriter;

public abstract class XmlWriter implements ResultStreamWriter {
	// 改行コード
//	protected static final String CR = "\r\n";
	protected static final String CR = "\n";
	// タブコード
	protected static final String TAB = "\t";
	// Stack（XMLタグを閉じる時に使用）
	protected Stack<String> stackTags;
	// output stream
	protected OutputStream os = null;

//	public void write(OutputStream out) throws IOException;

	protected void startAttribute(String element) throws IOException {
		addTabs();
		add("<");
		String e = getReplacedString(element);
		add(e);
	}

	protected void endAttribute() throws IOException {
		add(" />" + CR);
	}

	protected void addAttribute(String attr, String value) throws IOException {
		String a = getReplacedString(attr);
		add(" " + a + "=\"" + value + "\"");
	}

	protected void addHeader() throws IOException {
		add("<?xml version=\"1.0\" encoding=\"utf-8\"?>" + CR);
	}

	protected void addElement(String element) throws IOException {
		addTabs();
		stackTags.push(element);
		add(getTag(element, true, false));
	}

	protected void addElement(String element, String name) throws IOException {
		addTabs();
		stackTags.push(element);
		add(getTagWithName(element, name));
	}


	protected void closeElement() throws IOException {
		String element = stackTags.pop();
		addTabs();
		add(getTag(element, true, true));
	}

	protected void closeElementOnTheLine() throws IOException {
		String element = stackTags.pop();
		add(getTag(element, true, true));
	}

	protected void addElementWithValue(String element, boolean value) throws IOException {
		addElementWithValue(element, String.valueOf(value));
	}

	protected void addElementWithValue(String element, char value) throws IOException {
		addElementWithValue(element, String.valueOf(value));
	}

	protected void addElementWithValue(String element, char[] value) throws IOException {
		addElementWithValue(element, String.valueOf(value));
	}

	protected void addElementWithValue(String element, int value) throws IOException {
		addElementWithValue(element, String.valueOf(value));
	}

	protected void addElementWithValue(String element, long value) throws IOException {
		addElementWithValue(element, String.valueOf(value));
	}

	protected void addElementWithValue(String element, float value) throws IOException {
		addElementWithValue(element, String.valueOf(value));
	}

	protected void addElementWithValue(String element, double value) throws IOException {
		addElementWithValue(element, String.valueOf(value));
	}

	protected void addElementWithValue(String element, String value) throws IOException {
		addTabs();
		add(getTag(element, false, false));
		add(value);
		add(getTag(element, false, true));
	}

	protected String getTag(String element, boolean isElement, boolean isClose) {
		StringBuffer sb = new StringBuffer();
		sb.append("<");
		if (isClose) {
			sb.append("/");
		}
		String e = getReplacedString(element);
		sb.append(e);
		sb.append(">");
		if (isClose || isElement) {
			sb.append(CR);
		}
		return sb.toString();
	}

	protected String getTagWithName(String element, String name) {
		StringBuffer sb = new StringBuffer();
		sb.append("<");
		String e = getReplacedString(element);
		sb.append(e);
		sb.append(" name = \"");
		sb.append(name);
		sb.append("\">");

		return sb.toString();
	}
	protected void addTabs() throws IOException {
		int count = stackTags.size();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < count; i++) {
			sb.append(TAB);
		}
		add(sb.toString());
	}

	protected String getEncodeValue(String value) {
//		return StringUtil.escapeXml10(value, true);
		return value;
	}

	protected void add(String text) throws IOException {
		if (text == null) {
			return;
		}
		//SRB対応-チェック済み
		os.write(text.getBytes("UTF-8"));
	}

	protected String getReplacedString(String element) {
		return element;
	}
}
