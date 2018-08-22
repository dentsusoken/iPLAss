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

package org.iplass.mtp.entity.definition;

import java.util.List;
import java.util.Map;

import org.iplass.mtp.definition.DefinitionSummary;
import org.iplass.mtp.definition.TypedDefinitionManager;



/**
 * Entity定義を管理するクラスのインタフェース。
 *
 * @author K.Higuchi
 *
 */
public interface EntityDefinitionManager extends TypedDefinitionManager<EntityDefinition> {

	/**
	 * 新規にEntity定義を作成する。
	 *
	 * @param definition　新規に作成するEntityの定義
	 * @return 作成結果
	 */
	@Override
	public EntityDefinitionModifyResult create(EntityDefinition definition);


	/**
	 * 指定の定義名のEntity定義を取得する。
	 *
	 * @param definitionName 定義名
	 * @return 指定の定義名で一意に特定されるEntity定義
	 */
	public EntityDefinition get(String definitionName);

	/**
	 * 現在登録されているEntity定義名のリストを返す。
	 *
	 * @return Entity定義名のリスト
	 */
	public List<String> definitionList();

	/**
	 * 現在登録されているEntity定義名のリストを返す。
	 *
	 * @return Entity定義名(Name, DisplayName, Description)のリスト
	 */
	public List<DefinitionSummary> definitionNameList();

	/**
	 * 現在登録されているEntity定義名のリストを返す。
	 *
	 * @param filterPath "/"区切りのdefinitionNameのプレフィックス（Entity関連の定義は.区切りでも可）
	 * @return Entity定義名(Name, DisplayName, Description)のリスト
	 */
	public List<DefinitionSummary> definitionNameList(String filterPath);

	/**
	 * 指定の定義名のEntityを削除する。
	 *
	 * @param definitionName 定義名
	 * @return 削除結果
	 */
	@Override
	public EntityDefinitionModifyResult remove(String definitionName);

	/**
	 * 指定のEntity定義で既存の定義を更新する。
	 * （定義名の一致する定義を更新する。）
	 *
	 * @param definition 更新するEntity定義
	 * @return 更新結果
	 */
	@Override
	public EntityDefinitionModifyResult update(EntityDefinition definition);

	/**
	 * 指定のEntity定義で既存の定義を更新する。
	 * （定義名の一致する定義を更新する。）
	 *
	 * @param definition 更新するEntity定義
	 * @param renamePropertyMap 名前を変更するプロパティのMap(from, to)
	 * @return 更新結果
	 */
	public EntityDefinitionModifyResult update(EntityDefinition definition, Map<String, String> renamePropertyMap);

	/**
	 * AutoNumberPropertyの現在のカウンター値を返す。
	 *
	 * @param definitionName Entity定義名
	 * @param propertyName AutoNumberProperty型のプロパティのプロパティ名
	 */
	public long getAutoNumberCurrentValue(String definitionName, String propertyName);

	/**
	 * AutoNumberPropertyのカウンター値を指定の値にリセットする。
	 *
	 * @param definitionName Entity定義名
	 * @param propertyName AutoNumberProperty型のプロパティのプロパティ名
	 * @param startsWith リセットするカウンター値
	 */
	public void resetAutoNumberCounter(String definitionName, String propertyName, long startsWith);

	/**
	 * Entity定義名を変更する。
	 * データ自体はそのまま保持される。
	 *
	 * @param from 変更前の定義名
	 * @param to 変更後の定義名
	 */
	public void renameEntityDefinition(String from, String to);

	/**
	 *　Entity定義のプロパティ名を変更する。
	 * データ自体はそのまま保持される。
	 *
	 * @param definitionName 対象のEntity定義名
	 * @param from 変更前のプロパティ名
	 * @param to 変更後のプロパティ名
	 */
	public void renamePropertyDefinition(String definitionName, String from, String to);

	/**
	 * 現在登録されているEntity Storage Spaceのリストを返す。
	 *
	 * @return Entity Entity Storage Spaceのリスト
	 */
	public List<String> getStorageSpaceList();

	/**
	 * <p>スキーマがロックされているかを返す。</p>
	 *
	 * @param definitionName 対象のEntity定義名
	 * @return true：ロック
	 */
	public boolean isLockedSchema(String definitionName);

}
