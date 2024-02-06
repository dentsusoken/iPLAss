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
package org.iplass.mtp.impl.metadata.xmlfile.dom;

import org.w3c.dom.Node;

public abstract class MultiLangExternalRefPath extends AbstractExternalRefPath {

	/**
	 * ターゲットのタグと同じ階層にあるロケールタグの値を取得.
	 * <p>タグ名はアノテーションから取得.
	 * @return
	 */
	@Override
	public String getLocaleName(Node textNode) {
		Node parent = getLocaleParentNode(textNode);
		String localeTagName = getClass().getAnnotation(ExternalRefPathAttribute.class).localeTagName();
		return getChildNodeValue(parent, localeTagName);
	}

	/**
	 * localeNameタグの親タグを取得.
	 * <p>多くのlocalNameタグが階層のターゲットのタグと同じ階層にあるxmlに対応.
	 * @param targetTextNode
	 * @return
	 */
	protected Node getLocaleParentNode(Node targetTextNode) {
		return targetTextNode.getParentNode().getParentNode();
	}
}
