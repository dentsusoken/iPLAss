/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.metadata;

import java.util.List;

import org.iplass.mtp.impl.i18n.MetaLocalizedString;

/**
 * メタデータをあらわすインタフェース。
 * 実装クラスは、JAXBにより、XMLへ変換可能なJavaBeanとして作成する。
 *
 * @author K.Higuchi
 *
 */
public interface RootMetaData extends MetaData {

	/**
	 * メタデータを一意に特定可能な、不変なIDを返す。
	 * システム内でメタデータへの参照をするような場合に使用するハンドル。
	 *
	 * @return メタデータのID
	 */
	public String getId();

	/**
	 * メタデータを一意に特定可能な、不変なIDをセットする。
	 *
	 * @param id メタデータのID
	 */
	public void setId(String id);

	/**
	 * メタデータの論理的な名前を返す。
	 * メタデータ名は変更可能。
	 * メタデータ名の変更に左右されたくない場合はメタデータを指し示すポインタとしてはIDを使用する。
	 *
	 * @return メタデータ名
	 */
	public String getName();

	/**
	 * メタデータの論理的な名前をセットする。
	 * メタデータ名は変更可能。
	 * メタデータ名の変更に左右されたくない場合はメタデータを指し示すポインタとしてはIDを使用する。
	 *
	 * @param name メタデータ名
	 */
	public void setName(String name);

	/**
	 * メタデータの表示名を返す。
	 *
	 * @return メタデータの表示名
	 */
	public String getDisplayName();

	/**
	 * メタデータの表示名をセットする。
	 *
	 * @param displayName メタデータの表示名
	 */
	public void setDisplayName(String displayName);

	public List<MetaLocalizedString> getLocalizedDisplayNameList();

	public void setLocalizedDisplayNameList(List<MetaLocalizedString> localizedDisplayNameList);

	/**
	 * メタデータの記述を返す。
	 *
	 * @return メタデータの記述
	 */
	public String getDescription();

	/**
	 * メタデータの記述を設定する。
	 *
	 * @param description メタデータの記述
	 */
	public void setDescription(String description);

	/**
	 * このメタデータの情報から、何らかの処理を実際に実行するMetaDataRuntimeを取得する。
	 * メタデータ自身は、リポジトリ上に保存するデータのみを保持する形に作成し、
	 * 動的なものは、MetaDataRuntimeに保持するようにする。
	 *
	 * @return MetaDataRuntimeのインスタンス
	 */
	public MetaDataRuntime createRuntime(MetaDataConfig metaDataConfig);

	public RootMetaData copy();

}
