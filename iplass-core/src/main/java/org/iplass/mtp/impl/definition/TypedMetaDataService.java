/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.definition;

import java.util.List;

import org.iplass.mtp.impl.metadata.MetaDataEntryInfo;
import org.iplass.mtp.impl.metadata.MetaDataRuntime;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.spi.Service;

public interface TypedMetaDataService<M extends RootMetaData, R extends MetaDataRuntime> extends Service {

	/**
	 * メタデータのクラスを取得します。
	 * @return
	 */
	public Class<M> getMetaDataType();

	/**
	 * メタデータのRuntimeクラスを取得します。
	 * @return
	 */
	public Class<R> getRuntimeType();

	/**
	 * メタデータを作成します。
	 * @param meta メタデータ
	 */
	public void createMetaData(M meta);

	/**
	 * メタデータを更新します。
	 * @param meta メタデータ
	 */
	public void updateMetaData(M meta);

	/**
	 * メタデータを削除します。
	 * @param id
	 */
	public void removeMetaData(String definitionName);

	/**
	 * Runtimeクラスを取得します。
	 * @param name メタデータ名
	 * @return ランタイムクラス
	 */
	public R getRuntimeByName(String name);

	/**
	 * Runtimeクラスを取得します。
	 * @param id メタデータID
	 * @return ランタイムクラス
	 */
	public R getRuntimeById(String id);

	/**
	 * メタデータの定義名の一覧を取得します。
	 * @return メタデータの定義名の一覧
	 */
	public List<String> nameList();

	/**
	 * メタデータ情報の一覧を取得します。
	 * @return メタデータ情報の一覧
	 */
	public List<MetaDataEntryInfo> list();

	/**
	 * メタデータ情報の一覧を取得します。
	 * 指定されたパスの配下を対象にします。
	 * @param path パス
	 * @return メタデータ情報の一覧
	 */
	public List<MetaDataEntryInfo> list(String path);
}
