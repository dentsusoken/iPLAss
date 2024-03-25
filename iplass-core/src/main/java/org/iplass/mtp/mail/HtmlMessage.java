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

package org.iplass.mtp.mail;

import java.util.ArrayList;
import java.util.List;

import jakarta.activation.DataHandler;

import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.impl.mail.BinaryReferenceDataSource;

/**
 * <% if (doclang == "ja") {%>
 * htmlメールを送信する場合の本文を表します。
 * <%} else {%>
 * Represents the body text when sending html mail.
 * <%}%>
 * 
 * @author K.Higuchi
 *
 */
public class HtmlMessage {
	
	private List<InlineContent> inlineContents;
	
	private String content;
	private String charset;
	
	public HtmlMessage() {
	}
	
	public HtmlMessage(String content, String charset) {
		this.content = content;
		this.charset = charset;
	}

	public String getContent() {
		return content;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * html本文をセットします。
	 * <%} else {%>
	 * Set the html text.
	 * <%}%>
	 * 
	 * @param content
	 */
	public void setContent(String content) {
		this.content = content;
	}

	public String getCharset() {
		return charset;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * htmlのcharsetをセットします。
	 * <%} else {%>
	 * Set the charset of html.
	 * <%}%>
	 * 
	 * @param charset
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}
	
	/**
	 * <% if (doclang == "ja") {%>
	 * htmlへの埋め込み画像を追加します。
	 * <%} else {%>
	 * Add an embedded image to html.
	 * <%}%>
	 * 
	 * @param cid <%=doclang == 'ja' ? 'html内に埋め込む際のContent-ID': 'Content-ID of embedding image in html'%>
	 * @param bin <%=doclang == 'ja' ? '埋め込むデータを指し示すBinaryReference': 'BinaryReference to embed'%>
	 */
	public void addInlineContent(String cid, BinaryReference bin) {
		addInlineContent(cid, new DataHandler(new BinaryReferenceDataSource(bin)));
	}
	
	/**
	 * <% if (doclang == "ja") {%>
	 * htmlへの埋め込み画像を追加します。
	 * <%} else {%>
	 * Add an embedded image to html.
	 * <%}%>
	 * 
	 * @param cid <%=doclang == 'ja' ? 'html内に埋め込む際のContent-ID': 'Content-ID of embedding image in html'%>
	 * @param dataHandler <%=doclang == 'ja' ? '埋め込むデータを指し示すDataHandler': 'DataHandler to embed'%>
	 */
	public void addInlineContent(String cid, DataHandler dataHandler) {
		if (inlineContents == null) {
			inlineContents = new ArrayList<>();
		}
		inlineContents.add(new InlineContent(cid, dataHandler));
	}
	
	public List<InlineContent> getInlineContents() {
		return inlineContents;
	}

	public void setInlineContents(List<InlineContent> inlineContents) {
		this.inlineContents = inlineContents;
	}
}
