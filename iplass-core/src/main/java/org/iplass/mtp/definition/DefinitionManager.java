/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

import java.util.List;

import org.iplass.mtp.Manager;

/**
 * DefinitionのManagerです。
 * Definition共通の詳細なメタデータ（共有設定、バージョン情報、登録状態のチェック）を扱うためのインタフェースを定義します。
 *
 * @author K.Higuchi
 *
 */
public interface DefinitionManager extends Manager {

	/**
	 * 指定のDefinitionの共有設定を変更します。
	 *
	 * @param type 対象のDefinitionのclassインスタンス
	 * @param definitionName Definitionの定義名
	 * @param config 設定する共有設定
	 */
	public <D extends Definition> void setSharedConfig(Class<D> type, String definitionName, SharedConfig config);

	/**
	 * 指定のDefinitionのメタデータ（DefinitionInfo）を取得します。
	 *
	 * @param type 対象のDefinitionのclassインスタンス
	 * @param definitionName Definitionの定義名
	 * @return
	 */
	public <D extends Definition> DefinitionInfo getInfo(Class<D> type, String definitionName);

	/**
	 * DefinitionInfoのリストを取得します。
	 * filterPathにて、取得するリストの範囲を絞り込みが可能です。
	 * filterPathは各Definitionのパス区切り文字（"/"もしくは"."）にて区切られたパス階層を指定可能です。
	 * パス階層が指定された場合は、その階層以下のDefinitionのDefinitionInfoをリストします。
	 * 全件取得する場合は、filterPathはnullもしくは、"/"を指定します。
	 *
	 * @param type 対象のDefinitionのclassインスタンス
	 * @param filterPath "/"区切りのdefinitionNameのプレフィックス（Entity関連の定義は.区切りでも可）
	 * @return
	 */
	public <D extends Definition> List<DefinitionInfo> listInfo(Class<D> type, String filterPath);

	/**
	 * DefinitionSummaryのリストを取得します。
	 * filterPathにて、取得するリストの範囲を絞り込みが可能です。
	 * filterPathは各Definitionのパス区切り文字（"/"もしくは"."）にて区切られたパス階層を指定可能です。
	 * パス階層が指定された場合は、その階層以下のDefinitionのDefinitionSummaryをリストします。
	 * 全件取得する場合は、filterPathはnullもしくは、"/"を指定します。
	 * このメソッドは、{@link DefinitionManager#listName(Class, String, boolean)}をrecursive=trueで呼び出した場合と同一です。
	 *
	 * @param type 対象のDefinitionのclassインスタンス
	 * @param filterPath "/"区切りのdefinitionNameのプレフィックス（Entity関連の定義は.区切りでも可）
	 * @return
	 */
	public <D extends Definition> List<DefinitionSummary> listName(Class<D> type, String filterPath);

	/**
	 * DefinitionSummaryのリストを取得します。
	 * filterPathにて、取得するリストの範囲を絞り込みが可能です。
	 * filterPathは各Definitionのパス区切り文字（"/"もしくは"."）にて区切られたパス階層を指定可能です。
	 * パス階層が指定された場合は、その階層以下のDefinitionのDefinitionSummaryをリストします。
	 * 全件取得する場合は、filterPathはnullもしくは、"/"、""を指定します。
	 * recursive=falseを指定した場合は、filterPath直下の定義のみを取得します。
	 * recursive=trueの場合は、再帰的にfilterPath階層化すべての定義を取得します。
	 *
	 * @param type 対象のDefinitionのclassインスタンス
	 * @param filterPath "/"区切りのdefinitionNameのプレフィックス（Entity関連の定義は.区切りでも可）
	 * @return
	 */
	public <D extends Definition> List<DefinitionSummary> listName(Class<D> type, String filterPath, boolean recursive);

	/**
	 * 指定のDefinitionの状態をチェックする。
	 * Definitionの状態が不正（設定が不完全などの理由で実行できない）の場合は、
	 * IllegalDefinitionStateExceptionがスローされる
	 *
	 * @param type 対象のDefinitionのclassインスタンス
	 * @param definitionName チェック対象のDefinition名
	 * @throws IllegalDefinitionStateException
	 */
	public <D extends Definition> void checkState(Class<D> type, String definitionName) throws IllegalDefinitionStateException;

	/**
	 * 指定のDefinitionのDefinition本体およびメタ情報（DefinitionInfo）を取得します。
	 *
	 * @param type 対象のDefinitionのclassインスタンス
	 * @param definitionName 取得対象のDefinition名
	 * @return
	 */
	public <D extends Definition> DefinitionEntry getDefinitionEntry(Class<D> type, String definitionName);

	/**
	 * 指定のDefinitionの指定バージョンのDefinition本体およびメタ情報（DefinitionInfo）を取得します。
	 *
	 * @param type 対象のDefinitionのclassインスタンス
	 * @param definitionName 取得対象のDefinition名
	 * @param version 対象のDefinitionのバージョン番号
	 * @return
	 */
	public <D extends Definition> DefinitionEntry getDefinitionEntry(Class<D> type, String definitionName, int version);

	/**
	 * 指定のDefinitionのnameを変更します。
	 *
	 * @param type　対象のDefinitionのclassインスタンス
	 * @param oldDefinitionName　変更前のname
	 * @param newDefinitionName 変更後のname
	 */
	public <D extends Definition> void rename(Class<D> type, String oldDefinitionName, String newDefinitionName);


	/**
	 * 型付けされたDefinitionManagerのインスタンスを返します。
	 *
	 * @param type 対象のDefinitionのclassインスタンス
	 * @return
	 */
	public <D extends Definition> TypedDefinitionManager<D> getTypedDefinitionManager(Class<D> type);

}
