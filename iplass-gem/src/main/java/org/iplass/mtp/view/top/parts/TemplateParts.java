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

package org.iplass.mtp.view.top.parts;

import jakarta.xml.bind.annotation.XmlSeeAlso;

/**
 * テンプレート系のパーツ
 * @author lis3wg
 */
@XmlSeeAlso({CalendarParts.class, EntityListParts.class, LastLoginParts.class, TreeViewParts.class})
public class TemplateParts extends TopViewContentParts {

	/** SerialVersionUID */
	private static final long serialVersionUID = -2093081660986647075L;

	/** テンプレートパス */
	private String templatePath;

	/**
	 * テンプレートパスを取得します。
	 * @return テンプレートパス
	 */
	public String getTemplatePath() {
	    return templatePath;
	}

	/**
	 * テンプレートパスを設定します。
	 * @param templatePath テンプレートパス
	 */
	public void setTemplatePath(String templatePath) {
	    this.templatePath = templatePath;
	}
}
