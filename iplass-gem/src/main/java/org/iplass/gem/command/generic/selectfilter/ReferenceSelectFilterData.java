/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.gem.command.generic.selectfilter;

import java.util.List;

import org.iplass.mtp.entity.Entity;

/**
 * 参照選択フィルターの選択値
 */
public class ReferenceSelectFilterData {

	/** 総件数 */
    private int totalCount;

	/** 選択肢の値 */
    private List<Entity> optionValues;

    /**
     * 総件数を返します。
     * @return 総件数
     */
    public int getTotalCount() {
        return totalCount;
    }

    /**
     * 総件数を設定します。
     * @param totalCount 総件数
     */
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * 選択肢を返します。
     * @return 選択肢のリスト
     */
    public List<Entity> getOptionValues() {
        return optionValues;
    }

    /**
     * 選択肢を設定します。
     * @param optionValues 選択肢のリスト
     */
    public void setOptionValues(List<Entity> optionValues) {
        this.optionValues = optionValues;
    }

    /**
     * コンストラクタ
     * @param totalCount 総件数
     * @param optionValues 選択肢のリスト
     */
	public ReferenceSelectFilterData(int totalCount, List<Entity> optionValues) {
        this.totalCount = totalCount;
        this.optionValues = optionValues;
    }
}