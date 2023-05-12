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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 2分割パーツ
 * @author lis3wg
 */
public class SeparatorParts extends TopViewContentParts implements HasNestParts {

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

	@Override
	public List<TopViewParts> getNestParts() {
		List<TopViewParts> nestParts = new ArrayList<>();
		if (leftParts != null) {
			nestParts.addAll(getNestParts(leftParts));
		}
		if (rightParts != null) {
			nestParts.addAll(getNestParts(rightParts));
		}
		return nestParts;
	}

	private List<TopViewParts> getNestParts(TopViewParts parts) {
		if (parts instanceof HasNestParts) {
			List<TopViewParts> nestParts = new ArrayList<>();
			nestParts.add(parts);
			nestParts.addAll(((HasNestParts)parts).getNestParts());
			return nestParts;
		} else {
			return Arrays.asList(parts);
		}
	}

}
