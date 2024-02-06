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

package org.iplass.mtp.view.menu;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 階層フォルダ用メニューアイテム定義
 */
@XmlRootElement
public class NodeMenuItem extends MenuItem {

	private static final long serialVersionUID = -7946592191608287406L;

	@Override
	public <R> R accept(MenuItemVisitor<R> menuItemVisitor) {
		return menuItemVisitor.visit(this);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NodeMenuItem [");
		builder.append("name=");
		builder.append(getName());
		builder.append(", description=");
		builder.append(getDescription());
		builder.append(", definitionId=");
		builder.append(getDisplayName());
		builder.append(", imageUrl=");
		builder.append(getImageUrl());
		builder.append("]");
		return builder.toString();
	}
}
