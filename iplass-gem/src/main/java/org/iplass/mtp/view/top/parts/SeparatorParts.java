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

package org.iplass.mtp.view.top.parts;

/**
 * 2分割パーツ
 * @author lis3wg
 */
public class SeparatorParts extends TopViewParts {

	/** SerialVersionUID */
	private static final long serialVersionUID = 2637987444272245310L;

	/** 左エリアパーツ */
	private TopViewParts leftParts;

	/** 右エリアパーツ */
	private TopViewParts rightParts;

	/**
	 * 左エリアパーツを取得します。
	 * @return 左エリアパーツ
	 */
	public TopViewParts getLeftParts() {
	    return leftParts;
	}

	/**
	 * 左エリアパーツを設定します。
	 * @param leftParts 左エリアパーツ
	 */
	public void setLeftParts(TopViewParts leftParts) {
	    this.leftParts = leftParts;
	}

	/**
	 * 右エリアパーツを取得します。
	 * @return 右エリアパーツ
	 */
	public TopViewParts getRightParts() {
	    return rightParts;
	}

	/**
	 * 右エリアパーツを設定します。
	 * @param rightParts 右エリアパーツ
	 */
	public void setRightParts(TopViewParts rightParts) {
	    this.rightParts = rightParts;
	}

}
