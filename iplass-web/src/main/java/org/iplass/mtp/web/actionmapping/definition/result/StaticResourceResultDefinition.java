/*
 * Copyright (C) 2016 DENTSU SOKEN INC. All Rights Reserved.
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
 * 静的リソース定義を返却するResult定義です。
 * 
 * @author K.Higuchi
 *
 */
public class StaticResourceResultDefinition extends ResultDefinition {
	private static final long serialVersionUID = 4481823608566637666L;
	
	/**
	 * entryPathAttributeName未指定時のデフォルト名：entryPath
	 */
	public static final String DEFAULT_ENTRY_PATH_ATTRIBUTE_NAME = "entryPath";

	private String staticResourceName;
	
	private boolean useContentDisposition;
	private ContentDispositionType contentDispositionType;
	private String entryPathAttributeName = DEFAULT_ENTRY_PATH_ATTRIBUTE_NAME;

	/**
	 * StaticResourceがアーカイブ（StaticResourceDefinitionのresourceのインスタンスがArchiveBinaryDefinition）の場合、
	 * アーカイブ内エントリのパスを指し示すattribute名（RequestContext#getAttribute()利用時の名前）、
	 * もしくはparameter名(RequestContext#getParam()利用時の名前)。
	 * 
	 * @return
	 */
	public String getEntryPathAttributeName() {
		return entryPathAttributeName;
	}
	/**
	 * StaticResourceがアーカイブ（StaticResourceDefinitionのresourceのインスタンスがArchiveBinaryDefinition）の場合に利用する、
	 * アーカイブ内エントリのパスを指し示すattribute名（RequestContext#getAttribute()利用時の名前）、
	 * もしくはparameter名(RequestContext#getParam()利用時の名前)を指定。
	 * 
	 * @param entryPathAttributeName
	 */
	public void setEntryPathAttributeName(String entryPathAttributeName) {
		this.entryPathAttributeName = entryPathAttributeName;
	}
	
	/**
	 * 静的リソース定義名を取得
	 * @return
	 */
	public String getStaticResourceName() {
		return staticResourceName;
	}

	/**
	 * 静的リソース定義名を指定
	 * 
	 * @param staticResourceName
	 */
	public void setStaticResourceName(String staticResourceName) {
		this.staticResourceName = staticResourceName;
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

	@Override
	public String summaryInfo() {
		return "static resource = " + staticResourceName;
	}
}
