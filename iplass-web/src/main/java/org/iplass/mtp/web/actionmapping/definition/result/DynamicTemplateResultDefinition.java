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
 * 動的にtemplate名を指定するResult定義です。
 *
 * @author K.Higuchi
 *
 */
public class DynamicTemplateResultDefinition extends ResultDefinition {

	private static final long serialVersionUID = 6127822545341201084L;

	private String templatePathAttributeName;

	/** ContentDisposition設定フラグ */
	private boolean useContentDisposition;
	/** ContentDisposition Type(ContentDisposition設定時) */
	private ContentDispositionType contentDispositionType;
	/** ファイル名設定用Attribute名(ContentDisposition設定時) */
	private String fileNameAttributeName;

	/** LayoutAction用Attribute名 */
	private String layoutActionAttributeName;

	/**
	 * @return templatePathAttributeName
	 */
	public String getTemplatePathAttributeName() {
		return templatePathAttributeName;
	}

	/**
	 * @param templatePathAttributeName セットする templatePathAttributeName
	 */
	public void setTemplatePathAttributeName(String templatePathAttributeName) {
		this.templatePathAttributeName = templatePathAttributeName;
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

	/**
	 * @return layoutActionAttributeName
	 */
	public String getLayoutActionAttributeName() {
		return layoutActionAttributeName;
	}

	/**
	 * @param layoutActionAttributeName セットする layoutActionAttributeName
	 */
	public void setLayoutActionAttributeName(String layoutActionAttributeName) {
		this.layoutActionAttributeName = layoutActionAttributeName;
	}

	@Override
	public String summaryInfo() {
		return "path attribute = " + templatePathAttributeName;
	}
}
