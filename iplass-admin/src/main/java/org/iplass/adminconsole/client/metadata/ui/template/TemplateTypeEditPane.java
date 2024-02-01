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

package org.iplass.adminconsole.client.metadata.ui.template;

import org.iplass.mtp.web.template.definition.TemplateDefinition;

import com.smartgwt.client.widgets.layout.VLayout;

public abstract class TemplateTypeEditPane extends VLayout {

	/**
	 * Templateを展開します。
	 *
	 * @param definition TemplateDefinition
	 */
	public abstract void setDefinition(TemplateDefinition definition);

	/**
	 * 編集されたTemplateDefinition情報を返します。
	 *
	 * @return 編集TemplateDefinition情報
	 */
	public abstract TemplateDefinition getEditDefinition(TemplateDefinition definition);

	/**
	 * 入力チェックを実行します。
	 *
	 * @return 入力チェック結果
	 */
	public abstract boolean validate();

	/**
	 * <p>更新時にファイルをアップロードするかを返します。
	 * この結果がtrueの場合は、RPCでのリクエストではなくUploadAction(Servlet)経由で
	 * ファイルをアップロードします。</p>
	 *
	 * @return true：ファイルアップロードあり
	 */
	public abstract boolean isFileUpload();

	/**
	 * <p>Localeの編集ダイアログの高さを返します。
	 * 0の場合、100%になります。</p>
	 *
	 * @return Localeの編集ダイアログの高さ
	 */
	public int getLocaleDialogHeight() {
		return 0;
	}

}
