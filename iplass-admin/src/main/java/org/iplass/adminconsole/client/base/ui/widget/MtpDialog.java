/*
 * Copyright (C) 2019 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.base.ui.widget;

import org.iplass.adminconsole.client.base.util.SmartGWTUtil;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * ダイアログのベースクラス。
 *
 * 幅の設定、コンテンツ部分、フッタ部分（下に固定表示）を定義しています。
 * タイトル設定、高さの設定、中央表示はそれぞれで行ってください。
 *
 */
public abstract class MtpDialog extends AbstractWindow {

	/** コンテンツ部分 */
	protected VLayout container;

	/** フッタ部分 */
	protected HLayout footer;

	public MtpDialog() {

		//標準的な設定

		setWidth(MtpWidgetConstants.DIALOG_WIDTH);

		setShowMinimizeButton(false);
		setShowMaximizeButton(false);

		setCanDragReposition(true);
		setCanDragResize(true);

		setIsModal(true);
		setShowModalMask(true);

		//コンテンツ(スクロール可)
		container = new VLayout(5);
		container.setHeight100();
		container.setMargin(10);
		container.setOverflow(Overflow.AUTO);

		//フッタ
		footer = new HLayout(5);
		footer.setMargin(10);
		footer.setAutoHeight();
		footer.setWidth100();
		footer.setAlign(VerticalAlignment.CENTER);
		footer.setOverflow(Overflow.VISIBLE);

		addItem(container);
		addItem(SmartGWTUtil.separator());
		addItem(footer);
	}

}
