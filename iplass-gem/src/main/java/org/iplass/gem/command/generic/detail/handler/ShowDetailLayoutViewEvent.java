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

package org.iplass.gem.command.generic.detail.handler;

import org.iplass.gem.command.generic.detail.DetailFormViewData;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.view.generic.AbstractFormViewEvent;

/**
 * 汎用詳細・編集画面の表示イベント
 *
 */
public class ShowDetailLayoutViewEvent extends AbstractFormViewEvent {

	/** 詳細編集画面表示用データ */
	private DetailFormViewData detailFormViewData;

	public ShowDetailLayoutViewEvent(RequestContext request, String entityName, String viewName,
			DetailFormViewData detailFormViewData) {
		super(request, entityName, viewName);

		this.detailFormViewData = detailFormViewData;
	}

	/**
	 * 詳細編集画面表示用データを返します。
	 *
	 * @return 詳細編集画面表示用データ
	 */
	public DetailFormViewData getDetailFormViewData() {
		return detailFormViewData;
	}

}
