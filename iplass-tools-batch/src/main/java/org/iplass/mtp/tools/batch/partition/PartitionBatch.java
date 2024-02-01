/*
 * Copyright (C) 2020 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.tools.batch.partition;

import java.util.List;

import org.iplass.mtp.impl.tools.tenant.PartitionCreateParameter;
import org.iplass.mtp.impl.tools.tenant.PartitionInfo;
import org.iplass.mtp.tools.batch.MtpCuiBase.LogListener;

public interface PartitionBatch {

	/**
	 * <p>ログリスナーを追加します。</p>
	 * 
	 * @param listener ログリスナー
	 */
	void addLogListner(LogListener listener);

	/**
	 * <p>パーティション情報を返します。</p>
	 *
	 * @return パーティション情報
	 */
	List<PartitionInfo> getPartitionInfo();

	/**
	 * <p>パーティションを作成します。</p>
	 *
	 * @param param 作成条件
	 * @return 実行結果
	 */
	boolean createPartition(final PartitionCreateParameter param);

}
