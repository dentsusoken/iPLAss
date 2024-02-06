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

package org.iplass.adminconsole.client.metadata.ui.template;

import org.iplass.adminconsole.client.base.io.upload.UploadFileItem;
import org.iplass.mtp.web.template.report.definition.LocalizedReportDefinition;

public interface HasEditLocalizedReportDefinition extends HasEditFileUpload {

	/**
	 * <p>多言語用のLocalizedReportDefinitionを展開します。</p>
	 *
	 * @param definition LocalizedReportDefinition
	 * @param templateDefName テンプレート定義名(Download用)
	 * @param fileItem 既に選択済のUploadFileItem(再表示時に置き換えます)
	 */
	void setLocalizedReportDefinition(LocalizedReportDefinition definition, String templateDefName, UploadFileItem fileItem);

	/**
	 * 編集された結果を反映したLocalizedReportDefinitionを返します。
	 *
	 * @param definition 修正対象LocalizedReportDefinition
	 * @return 編集LocalizedReportDefinition
	 */
	LocalizedReportDefinition getEditLocalizedReportDefinition(LocalizedReportDefinition definition);

}
