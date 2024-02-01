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

package org.iplass.mtp.web.actionmapping.definition.result;


/**
 * 実行結果として、templateを出力するResult定義です。
 *
 * @author K.Higuchi
 *
 */
public class TemplateResultDefinition extends ResultDefinition {

	private static final long serialVersionUID = -2784701992968234641L;

	private String templateName;

	/** ContentDisposition設定フラグ */
	private boolean useContentDisposition;
	/** ContentDisposition Type(ContentDisposition設定時) */
	private ContentDispositionType contentDispositionType;
	/** ファイル名設定用Attribute名(ContentDisposition設定時) */
	private String fileNameAttributeName;

	/** LayoutAction(ActionMappingのName) */
	private String layoutActionName;

	/**
	 * @return templateName
	 */
	public String getTemplateName() {
		return templateName;
	}

	/**
	 * @param templateName セットする templateName
	 */
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
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
	 * @return layoutActionName
	 */
	public String getLayoutActionName() {
		return layoutActionName;
	}

	/**
	 * @param layoutActionName セットする layoutActionName
	 */
	public void setLayoutActionName(String layoutActionName) {
		this.layoutActionName = layoutActionName;
	}

	@Override
	public String summaryInfo() {
		return "template = " + templateName;
	}

}
