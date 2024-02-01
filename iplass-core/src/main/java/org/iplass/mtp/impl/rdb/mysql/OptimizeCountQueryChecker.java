/*
 * Copyright (C) 2023 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.rdb.mysql;

import org.iplass.mtp.entity.query.GroupBy;
import org.iplass.mtp.entity.query.Having;
import org.iplass.mtp.entity.query.Limit;
import org.iplass.mtp.entity.query.QueryVisitorSupport;
import org.iplass.mtp.entity.query.Select;
import org.iplass.mtp.entity.query.SubQuery;
import org.iplass.mtp.entity.query.value.aggregate.Avg;
import org.iplass.mtp.entity.query.value.aggregate.Count;
import org.iplass.mtp.entity.query.value.aggregate.Listagg;
import org.iplass.mtp.entity.query.value.aggregate.Max;
import org.iplass.mtp.entity.query.value.aggregate.Median;
import org.iplass.mtp.entity.query.value.aggregate.Min;
import org.iplass.mtp.entity.query.value.aggregate.Mode;
import org.iplass.mtp.entity.query.value.aggregate.StdDevPop;
import org.iplass.mtp.entity.query.value.aggregate.StdDevSamp;
import org.iplass.mtp.entity.query.value.aggregate.Sum;
import org.iplass.mtp.entity.query.value.aggregate.VarPop;
import org.iplass.mtp.entity.query.value.aggregate.VarSamp;
import org.iplass.mtp.entity.query.value.window.CumeDist;
import org.iplass.mtp.entity.query.value.window.DenseRank;
import org.iplass.mtp.entity.query.value.window.PercentRank;
import org.iplass.mtp.entity.query.value.window.Rank;
import org.iplass.mtp.entity.query.value.window.RowNumber;
import org.iplass.mtp.entity.query.value.window.WindowAggregate;

/**
 * 導出テーブルのマージが行われるかをチェックするためのQueryVisitor。
 * 
 * 参考：
 * https://dev.mysql.com/doc/refman/8.0/en/derived-table-optimization.html
 * 
 * @author K.Higuchi
 *
 */
class OptimizeCountQueryChecker extends QueryVisitorSupport {
	boolean possible = true;
	
	@Override
	public boolean visit(Select select) {
		if (select .isDistinct()) {
			possible = false;
			return false;
		} else {
			return true;
		}
	}

	//Aggregate
	@Override
	public boolean visit(Count count) {
		possible = false;
		return false;
	}

	@Override
	public boolean visit(Sum sum) {
		possible = false;
		return false;
	}

	@Override
	public boolean visit(Avg avg) {
		possible = false;
		return false;
	}

	@Override
	public boolean visit(Max max) {
		possible = false;
		return false;
	}

	@Override
	public boolean visit(Min min) {
		possible = false;
		return false;
	}

	@Override
	public boolean visit(StdDevPop stdDevPop) {
		possible = false;
		return false;
	}

	@Override
	public boolean visit(StdDevSamp stdDevSamp) {
		possible = false;
		return false;
	}

	@Override
	public boolean visit(VarPop varPop) {
		possible = false;
		return false;
	}

	@Override
	public boolean visit(VarSamp varSamp) {
		possible = false;
		return false;
	}

	@Override
	public boolean visit(Mode mode) {
		possible = false;
		return false;
	}

	@Override
	public boolean visit(Median median) {
		possible = false;
		return false;
	}

	@Override
	public boolean visit(Listagg listagg) {
		possible = false;
		return false;
	}

	//Window function
	@Override
	public boolean visit(WindowAggregate windowAggregate) {
		possible = false;
		return false;
	}

	@Override
	public boolean visit(RowNumber rowNumber) {
		possible = false;
		return false;
	}

	@Override
	public boolean visit(Rank rank) {
		possible = false;
		return false;
	}

	@Override
	public boolean visit(DenseRank denseRank) {
		possible = false;
		return false;
	}

	@Override
	public boolean visit(PercentRank percentRank) {
		possible = false;
		return false;
	}

	@Override
	public boolean visit(CumeDist cumeDist) {
		possible = false;
		return false;
	}

	@Override
	public boolean visit(Limit limit) {
		possible = false;
		return false;
	}

	@Override
	public boolean visit(Having having) {
		possible = false;
		return false;
	}

	@Override
	public boolean visit(GroupBy groupBy) {
		possible = false;
		return false;
	}

	@Override
	public boolean visit(SubQuery subQuery) {
		return false;
	}

}