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

package org.iplass.mtp.impl.view.generic.element.section;

import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.view.generic.element.Element;
import org.iplass.mtp.view.generic.element.section.VersionSection;

/**
 * 別バージョン表示用セクション
 * @author lis3wg
 */
public class MetaVersionSection extends MetaSection {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -180720387357540824L;

	public static MetaVersionSection createInstance(Element element) {
		return new MetaVersionSection();
	}

	/** リンクを表示するか */
	private boolean showLink;

	/**
	 * リンクを表示するかを取得します。
	 * @return リンクを表示するか
	 */
	public boolean isShowLink() {
	    return showLink;
	}

	/**
	 * リンクを表示するかを設定します。
	 * @param showLink リンクを表示するか
	 */
	public void setShowLink(boolean showLink) {
	    this.showLink = showLink;
	}

	@Override
	public void applyConfig(Element element, String definitionId) {
		super.fillFrom(element, definitionId);

		VersionSection section = (VersionSection) element;
		showLink = section.isShowLink();
	}

	@Override
	public Element currentConfig(String definitionId) {
		VersionSection section = new VersionSection();
		super.fillTo(section, definitionId);

		section.setShowLink(showLink);
		return section;
	}

	@Override
	public MetaVersionSection copy() {
		return ObjectUtil.deepCopy(this);
	}

}
