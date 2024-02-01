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

package org.iplass.adminconsole.client.base.plugin;

import com.smartgwt.client.widgets.Canvas;

/**
 * <p>WorkspaceのコンテンツCloseイベントです。</p>
 */
public class ContentClosedEvent {

	private Canvas source;

	private String defName;

	private boolean closeConfirm = false;

	public boolean isCloseConfirm() {
		return closeConfirm;
	}

	public void setCloseConfirm(boolean closeConfirm) {
		this.closeConfirm = closeConfirm;
	}

	/**
	 * コンストラクタ
	 */
	public ContentClosedEvent() {
		this(null, null);
	}

	/**
	 * コンストラクタ
	 *
	 * @param source CloseされたCanvas
	 * @param defName 定義名
	 */
	public ContentClosedEvent(Canvas source, String defName) {
		this.source = source;
		this.defName = defName;
	}

	/**
	 * <p>対象のコンテンツを返します。<p>
	 */
	public Canvas getSource() {
		return source;
	}

	/**
	 * <p>対象の定義名を返します。<p>
	 */
	public String getDefinitionName() {
		return defName;
	}
}
