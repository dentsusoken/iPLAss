/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.definition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.iplass.mtp.Manager;

/**
 * Definitionの取得、更新を行うための型付けされたManagerのインタフェースです。
 *
 * @author K.Higuchi
 *
 * @param <D> このDefinitionManagerが扱うDefinitionのType
 */
public interface TypedDefinitionManager<D extends Definition> extends Manager {

	/**
	 * 指定の定義名のDefinitionを取得します。
	 *
	 * @param definitionName 定義名
	 * @return 指定の定義名で一意に特定されるDefinition
	 */
	public D get(String definitionName);

	/**
	 * 現在登録されているDefinition名のリストを返します。
	 *
	 * @return 定義名のリスト
	 */
	public default List<String> definitionList() {
		List<DefinitionSummary> nameList = definitionSummaryList();
		if (nameList == null) {
			return Collections.emptyList();
		}

		ArrayList<String> ret = new ArrayList<>(nameList.size());
		for (DefinitionSummary n: nameList) {
			ret.add(n.getName());
		}
		return ret;
	}

	/**
	 * 現在登録されているDefinitionのDefinitionSummaryのリストをすべて返します。
	 *
	 * @return DefinitionSummary(Name, DisplayName, description)のリスト
	 */
	public default List<DefinitionSummary> definitionSummaryList() {
		return definitionSummaryList(null, true);
	}

	/**
	 * 現在登録されているDefinitionのDefinitionSummaryのリストを返します。
	 * 再帰的に階層下の定義をすべて取得します。
	 * {@link #definitionSummaryList(String, boolean)}のrecursiveをtrueで呼び出します。
	 *
	 * @param filterPath "/"もしくは"."区切りのdefinitionNameのプレフィックス（実装されるDefinitionによりパス区切り文字は異なります）
	 * @return DefinitionSummary(Name, DisplayName, description)のリスト
	 */
	public default List<DefinitionSummary> definitionSummaryList(String filterPath) {
		return definitionSummaryList(filterPath, true);
	}

	/**
	 * 現在登録されているDefinitionのDefinitionSummaryのリストを返します。
	 * filterPathが指定された場合は、当該パス以下のDefinitionから取得します。
	 * filterPathがnullもしくは、"/"、""の場合は、すべてのDefinitionから取得します。
	 * recursive=trueが指定された場合は、再帰的に階層下の定義をすべて取得します。
	 *
	 * @param filterPath "/"もしくは"."区切りのdefinitionNameのプレフィックス（実装されるDefinitionによりパス区切り文字は異なります）
	 * @param recursive 再帰的に階層下の定義をすべて取得する場合はtrue
	 * @return DefinitionSummary(Name, DisplayName, description)のリスト
	 */
	public List<DefinitionSummary> definitionSummaryList(String filterPath, boolean recursive);

	/**
	 * 新規にDefinitionを作成します。
	 *
	 * @param definition　新規に作成するDefinition
	 * @return 作成結果
	 */
	public DefinitionModifyResult create(D definition);

	/**
	 * 指定のDefinitionで既存のDefinitionを更新します。
	 * （定義名の一致する定義を更新します。）
	 *
	 * @param definition 更新するDefinition
	 * @return 更新結果
	 */
	public DefinitionModifyResult update(D definition);

	/**
	 * 指定の定義名のDefinitionを削除します。
	 *
	 * @param definitionName 定義名
	 * @return 削除結果
	 */
	public DefinitionModifyResult remove(String definitionName);

	/**
	 * 指定のDefinitionのnameを変更します。
	 *
	 * @param oldDefinitionName 古いname
	 * @param newDefinitionName 新しいname
	 */
	public void rename(String oldDefinitionName, String newDefinitionName);

	/**
	 * このTypedDefinitionManagerが扱うDefinitionの型を返却します。
	 * @return
	 */
	public Class<D> getDefinitionType();

}
