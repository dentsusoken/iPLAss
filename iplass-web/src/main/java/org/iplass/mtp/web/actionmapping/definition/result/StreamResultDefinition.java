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

package org.iplass.mtp.web.actionmapping.definition.result;

/**
 * 実行結果として、ストリーム（バイナリデータ、文字列データ）を出力するResult定義です。
 *
 * @author K.Higuchi
 *
 */
public class StreamResultDefinition extends ResultDefinition {

	private static final long serialVersionUID = -2481952719267699944L;

	private String inputStreamAttributeName;
	private String contentTypeAttributeName = "contentType";
	private String contentLengthAttributeName = "contentLength";

	/** ContentDisposition設定フラグ */
	private boolean useContentDisposition = true;
	/** ContentDisposition Type(ContentDisposition設定時) */
	private ContentDispositionType contentDispositionType;
	/** ファイル名設定用Attribute名(ContentDisposition設定時) */
	private String fileNameAttributeName = "fileName";
	/** rangeヘッダ対応するか否か */
	private boolean acceptRanges;

	/**
	 *
	 * @see #setAcceptRanges(boolean)
	 * @return
	 */
	public boolean isAcceptRanges() {
		return acceptRanges;
	}

	/**
	 * 当該のレスポンスでRangeヘッダによる分割コンテンツを可能とするか否かを設定。
	 * Range対応する場合は、同一リクエストパラメータで同一の結果（コンテンツデータ）が返却されることが前提（参照透過性が必要）。
	 * また、出力対象のコンテンツデータのサイズが既知（ストリーム対象のデータが、BinaryReference、byte[]もしくは、InputStreamかつrequestに"contentLength"のキー名でlongでサイズ指定されていること）であること。
	 *
	 * @param acceptRanges
	 */
	public void setAcceptRanges(boolean acceptRanges) {
		this.acceptRanges = acceptRanges;
	}

	/**
	 * @return inputStreamAttributeName
	 */
	public String getInputStreamAttributeName() {
		return inputStreamAttributeName;
	}

	/**
	 * @param inputStreamAttributeName セットする inputStreamAttributeName
	 */
	public void setInputStreamAttributeName(String inputStreamAttributeName) {
		this.inputStreamAttributeName = inputStreamAttributeName;
	}

	/**
	 * @return contentTypeAttributeName
	 */
	public String getContentTypeAttributeName() {
		return contentTypeAttributeName;
	}

	/**
	 * @param contentTypeAttributeName セットする contentTypeAttributeName
	 */
	public void setContentTypeAttributeName(String contentTypeAttributeName) {
		this.contentTypeAttributeName = contentTypeAttributeName;
	}

	/**
	 * @return contentLengthAttributeName
	 */
	public String getContentLengthAttributeName() {
		return contentLengthAttributeName;
	}

	/**
	 * @param contentLengthAttributeName セットする contentLengthAttributeName
	 */
	public void setContentLengthAttributeName(String contentLengthAttributeName) {
		this.contentLengthAttributeName = contentLengthAttributeName;
	}

	/**
	 * @return useContentDisposition
	 */
	public boolean isUseContentDisposition() {
		return useContentDisposition;
	}

	/**
	 * @param useContentDisposition セットする useContentDisposition
	 */
	public void setUseContentDisposition(boolean useContentDisposition) {
		this.useContentDisposition = useContentDisposition;
	}

	/**
	 * @return contentDispositionType
	 */
	public ContentDispositionType getContentDispositionType() {
		return contentDispositionType;
	}

	/**
	 * @param contentDispositionType セットする contentDispositionType
	 */
	public void setContentDispositionType(ContentDispositionType contentDispositionType) {
		this.contentDispositionType = contentDispositionType;
	}

	/**
	 * @return fileNameAttributeName
	 */
	public String getFileNameAttributeName() {
		return fileNameAttributeName;
	}

	/**
	 * @param fileNameAttributeName セットする fileNameAttributeName
	 */
	public void setFileNameAttributeName(String fileNameAttributeName) {
		this.fileNameAttributeName = fileNameAttributeName;
	}

	@Override
	public String summaryInfo() {
		return "stream attribute = " + inputStreamAttributeName;
	}
}
