/*
 * Copyright (C) 2020 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.gem;

/**
 * 自動生成設定
 */
public class AutoGenerateSetting {

	/**
	 * システムプロパティの表示位置
	 */
	public enum DisplayPosition {
		/** 先頭 */
		TOP,
		/** 最後 */
		BOTTOM
	}

	/** システムプロパティを出力するか */
	private boolean showSystemProperty;

	/**
	 * 出力するシステムプロパティ。
	 * 基本項目であるnameとバージョン管理項目(startDate、endDate)は対象外。
	 */
	private String[] systemProperties;

	/** システムプロパティの表示位置 */
	private DisplayPosition systemPropertyDisplayPosition = DisplayPosition.TOP;

	/** OIDをカスタマイズしている場合にOIDを表示しない */
	private boolean excludeOidWhenCustomOid = true;

	/** createBy、updateBy、lockedByにUserPropertyEditorを利用するか */
	private boolean useUserPropertyEditor = true;

	/**
	 * システムプロパティを出力するかを返します。
	 * @return true システムプロパティを出力する
	 */
	public boolean isShowSystemProperty() {
		return showSystemProperty;
	}

	/**
	 * システムプロパティを出力するかを設定します。
	 * @param showSystemProperty システムプロパティを出力するか
	 */
	public void setShowSystemProperty(boolean showSystemProperty) {
		this.showSystemProperty = showSystemProperty;
	}

	/**
	 * 出力するシステムプロパティを返します。
	 *
	 * @return 出力するシステムプロパティ
	 */
	public String[] getSystemProperties() {
		return systemProperties;
	}

	/**
	 * 出力するシステムプロパティを設定します。
	 * 基本項目であるnameとバージョン管理項目(startDate、endDate)は対象外。
	 *
	 * @param systemProperties 出力するシステムプロパティ
	 */
	public void setSystemProperties(String[] systemProperties) {
		this.systemProperties = systemProperties;
	}

	/**
	 * システムプロパティの表示位置を返します。
	 * @return システムプロパティの表示位置
	 */
	public DisplayPosition getSystemPropertyDisplayPosition() {
		return systemPropertyDisplayPosition;
	}

	/**
	 * システムプロパティの表示位置を設定します。
	 * @param systemPropertyDisplayPosition システムプロパティの表示位置
	 */
	public void setSystemPropertyDisplayPosition(DisplayPosition systemPropertyDisplayPosition) {
		this.systemPropertyDisplayPosition = systemPropertyDisplayPosition;
	}

	/**
	 * OIDをカスタマイズしている場合にOIDを表示しないかを返します。
	 * @return true OIDをカスタマイズしている場合にOIDを表示しない
	 */
	public boolean isExcludeOidWhenCustomOid() {
		return excludeOidWhenCustomOid;
	}

	/**
	 * OIDをカスタマイズしている場合にOIDを表示しないかを設定します。
	 * @param excludeOidWhenCustomOid OIDをカスタマイズしている場合にOIDを表示しないか
	 */
	public void setExcludeOidWhenCustomOid(boolean excludeOidWhenCustomOid) {
		this.excludeOidWhenCustomOid = excludeOidWhenCustomOid;
	}

	/**
	 * createBy、updateBy、lockedByにUserPropertyEditorを利用するかを返します。
	 * @return true UserPropertyEditorを利用する
	 */
	public boolean isUseUserPropertyEditor() {
		return useUserPropertyEditor;
	}

	/**
	 * createBy、updateBy、lockedByにUserPropertyEditorを利用するかを設定します。
	 * @param useUserPropertyEditor UserPropertyEditorを利用するか
	 */
	public void setUseUserPropertyEditor(boolean useUserPropertyEditor) {
		this.useUserPropertyEditor = useUserPropertyEditor;
	}

}
