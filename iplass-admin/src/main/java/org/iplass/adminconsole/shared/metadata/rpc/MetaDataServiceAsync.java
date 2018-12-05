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

package org.iplass.adminconsole.shared.metadata.rpc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.iplass.adminconsole.shared.base.dto.KeyValue;
import org.iplass.adminconsole.shared.base.dto.i18n.I18nMetaDisplayInfo;
import org.iplass.adminconsole.shared.metadata.dto.AdminDefinitionModifyResult;
import org.iplass.adminconsole.shared.metadata.dto.MetaDataInfo;
import org.iplass.adminconsole.shared.metadata.dto.MetaTreeNode;
import org.iplass.adminconsole.shared.metadata.dto.Name;
import org.iplass.adminconsole.shared.metadata.dto.entity.SortInfo;
import org.iplass.adminconsole.shared.metadata.dto.menu.MenuItemHolder;
import org.iplass.adminconsole.shared.tools.dto.queueexplorer.TaskCancelResultInfo;
import org.iplass.adminconsole.shared.tools.dto.queueexplorer.TaskForceDeleteResultInfo;
import org.iplass.adminconsole.shared.tools.dto.queueexplorer.TaskLoadResultInfo;
import org.iplass.adminconsole.shared.tools.dto.queueexplorer.TaskSearchResultInfo;
import org.iplass.mtp.async.AsyncTaskInfoSearchCondtion;
import org.iplass.mtp.definition.Definition;
import org.iplass.mtp.definition.DefinitionEntry;
import org.iplass.mtp.definition.DefinitionInfo;
import org.iplass.mtp.definition.DefinitionSummary;
import org.iplass.mtp.definition.SharedConfig;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.view.generic.DetailFormView;
import org.iplass.mtp.view.generic.SearchFormView;
import org.iplass.mtp.view.menu.MenuItem;
import org.iplass.mtp.web.template.definition.TemplateDefinition;
import org.iplass.mtp.web.template.report.definition.OutputFileType;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MetaDataServiceAsync {

	/* ---------------------------------------
	 * 共通
	 --------------------------------------- */

	void getAllMetaDataPath(final int tenantId, AsyncCallback<List<String>> callback);

	<D extends Definition> void getMetaDataTree(int tenantId, String className, AsyncCallback<MetaTreeNode> callback);

	<D extends Definition> void getMetaDataInfo(int tenantId, String className, String name, AsyncCallback<MetaDataInfo> callback);

	<D extends Definition> void getMetaDataInfo(int tenantId, String path, AsyncCallback<MetaDataInfo> callback);

	<D extends Definition> void getMetaDisplayInfo(int tenantId, String className, String name, AsyncCallback<I18nMetaDisplayInfo> callback);

	void getMetaDisplayInfo(int tenantId, String path, AsyncCallback<I18nMetaDisplayInfo> callback);

	<D extends Definition> void checkStatus(int tenantId, String className, String name, AsyncCallback<String> callback);

	void checkStatus(int tenantId, List<String> paths, AsyncCallback<LinkedHashMap<String, String>> callback);

	/**
	 * キャッシュとして保持しているMetaData情報をクリアします。
	 *
	 * @param tenantId テナントID
	 * @param callback  Callbackクラス
	 */
	void clearAllCache(int tenantId, AsyncCallback<Void> callback);

	/**
	 * キャッシュとして保持しているActionのコンテンツキャッシュをクリアします。
	 *
	 * @param tenantId テナントID
	 * @param actionName アクション名
	 */
	void clearActionCache(int tenantId, String actionName, AsyncCallback<Void> callback);

	/**
	 * キャッシュとして保持しているActionのコンテンツキャッシュをクリアします。
	 *
	 * @param tenantId テナントID
	 */
	void clearTenantActionCache(int tenantId, AsyncCallback<Void> callback);

	void updateSharedConfig(int tenantId, String className, String name, SharedConfig config, AsyncCallback<Void> callback);

	void renameDefinition(int tenantId, String className, String fromName, String toName, AsyncCallback<AdminDefinitionModifyResult> callback);

	void getDefinitionInfo(int tenantId, String className, String name, AsyncCallback<DefinitionInfo> callback);

	void getEnableLanguages(int tenantId, AsyncCallback<Map<String, String>> callback);

	/**
	 * メタデータの更新履歴を取得します。
	 *
	 * @param tenantId    テナントID
	 * @param className   対象メタデータクラス
	 * @param definitionId    メターデータID
	 * @param callback  Callbackクラス
	 */
	void getHistoryById(int tenantId, String className, String definitionId, AsyncCallback<DefinitionInfo> callback);

	/**
	 * Definitionの一覧を取得します。
	 *
	 * @param tenantId テナントID
	 * @param className Definitionクラス
	 * @param callback Callbackクラス
	 */
	void getDefinitionNameList(int tenantId, String className, AsyncCallback<List<Name>> callback);

	/**
	 * DefinitionEntryを取得します。
	 *
	 * @param tenantId テナントID
	 * @param className Definitionクラス
	 * @param name 定義名
	 * @return DefinitionEntry
	 * @param callback  Callbackクラス
	 */
	void getDefinitionEntry(int tenantId, String className, String name, AsyncCallback<DefinitionEntry> callback);

	/**
	 * Definitionを取得します。
	 *
	 * @param tenantId テナントID
	 * @param className Definitionクラス
	 * @param name 名前
	 * @param callback  Callbackクラス
	 */
	<D extends Definition> void getDefinition(int tenantId, String className, String name, AsyncCallback<D> callback);

	/**
	 * SamlDefinitionを作成します。
	 *
	 * @param tenantId テナントID
	 * @param definition 追加Definition
	 * @param callback  Callbackクラス
	 */
	<D extends Definition> void createDefinition(int tenantId, D definition, AsyncCallback<AdminDefinitionModifyResult> callback);

	/**
	 * SamlDefinitionを更新します。
	 *
	 * @param tenantId テナントID
	 * @param definition 更新Definition
	 * @param currentVersion 現在のバージョン
	 * @param checkVersion バージョンチェックの有無
	 * @param callback  Callbackクラス
	 */
	<D extends Definition> void updateDefinition(int tenantId, D definition,
			int currentVersion, boolean checkVersion, AsyncCallback<AdminDefinitionModifyResult> callback);

	/**
	 * SamlDefinitionを削除します。
	 *
	 * @param tenantId テナントID
	 * @param className Definitionクラス
	 * @param name 名前
	 * @param callback  Callbackクラス
	 */
	<D extends Definition> void deleteDefinition(int tenantId, String className, String name, AsyncCallback<AdminDefinitionModifyResult> callback);

	/**
	 * Definitionをコピーします。
	 *
	 * @param tenantId    テナントID
	 * @param className Definitionクラス
	 * @param sourceName  コピー元定義名
	 * @param newName     コピー先定義名
	 * @param displayName 表示名
	 * @param description 説明
	 * @param callback  Callbackクラス
	 */
	<D extends Definition> void copyDefinition(int tenantId, String className, String sourceName, String newName,
			String displayName, String description, AsyncCallback<AdminDefinitionModifyResult> callback);

	/* ---------------------------------------
	 * Entity
	 --------------------------------------- */

	void getEntityDefinitionNameList(int tenantId, AsyncCallback<List<Name>> callback);

	void getEntityDefinition(int tenantId, String name, AsyncCallback<EntityDefinition> callback);

	void getEntityDefinitionEntry(int tenantId, String name, AsyncCallback<DefinitionEntry> callback);

	void createEntityDefinition(int tenantId, EntityDefinition definition, AsyncCallback<AdminDefinitionModifyResult> callback);

	void updateEntityDefinition(int tenantId, EntityDefinition definition, int currentVersion, boolean checkVersion, AsyncCallback<AdminDefinitionModifyResult> callback);

	void updateEntityDefinition(int tenantId, EntityDefinition definition, int currentVersion, Map<String, String> renamePropertyMap, boolean checkVersion, AsyncCallback<AdminDefinitionModifyResult> callback);

	void checkLockEntityDefinition(int tenantId, String defName, AsyncCallback<Boolean> callback);

	void deleteEntityDefinition(int tenantId, String name, AsyncCallback<AdminDefinitionModifyResult> callback);

	void copyEntityDefinition(int tenantId, String sourceName,
			String newName, String displayName, String description,
			boolean isCopyEntityView, boolean isCopyEntityFilter, boolean isCopyEntityWebAPI,
			AsyncCallback<AdminDefinitionModifyResult> callback);

	void getEntityDefinitionName(int tenantId, String name, AsyncCallback<Name> callback);

	void getPropertyDefinitionNameList(int tenantId, String name, AsyncCallback<List<Name>> callback);

	void getPropertyDefinition(int tenantId, String name, String propertyName, AsyncCallback<PropertyDefinition> callback);

	void getPropertyDisplayName(int tenantId, String name, String propertyName, AsyncCallback<String> callback);

	void getEntityStoreSpaceList(int tenantId, AsyncCallback<List<String>> callback);

	void getAutoNumberCurrentValueList(int tenantId, String name, String propertyName, AsyncCallback<List<KeyValue<String, Long>>> callback);

	void resetAutoNumberCounterList(int tenantId, String name, String propertyName, List<KeyValue<String, Long>> values, AsyncCallback<Void> callback);

	void getSortInfo(int tenantId, String orderBy, AsyncCallback<List<SortInfo>> callback);

	void getCrawlTargetEntityList(int tenantId, AsyncCallback<List<EntityDefinition>> callback);

	void getCrawlTargetEntityViewMap(int tenantId, AsyncCallback<Map<String, List<String>>> callback);

	/* ---------------------------------------
	 * EntityView
	 --------------------------------------- */

	void createDefaultSearchFormView(int tenantId, String name, AsyncCallback<SearchFormView> callback);

	void createDefaultDetailFormView(int tenantId, String name, AsyncCallback<DetailFormView> callback);

	void getRoles(int tenantId, AsyncCallback<List<Entity>> callback);

	/* ---------------------------------------
	 * Menu Item
	 --------------------------------------- */

	/**
	 * メニューアイテムを全件取得します。
	 *
	 * @param tenantId  テナントID
	 * @param callback  Callbackクラス
	 */
	void getMenuItemList(int tenantId, AsyncCallback<MenuItemHolder> callback);

	/**
	 * メニューアイテムを作成します。
	 *
	 * @param tenantId テナントID
	 * @param definition 追加{@link MenuItem}
	 * @param callback  Callbackクラス
	 */
	void createMenuItem(int tenantId, MenuItem definition, AsyncCallback<AdminDefinitionModifyResult> callback);

	/**
	 * メニューアイテムを更新します。
	 *
	 * @param tenantId テナントID
	 * @param definition 更新{@link MenuItem}
	 * @param callback  Callbackクラス
	 */
	void updateMenuItem(int tenantId, MenuItem definition, AsyncCallback<AdminDefinitionModifyResult> callback);

	/* ---------------------------------------
	 * Template
	 --------------------------------------- */

	void getTemplateDefinitionEntry(int tenantId, String name, AsyncCallback<DefinitionEntry> callback);

	/**
	 * Template定義を更新します。
	 *
	 * @param tenantId  テナントID
	 * @param definition Template定義
	 * @param callback  Callbackクラス
	 */
	void updateTemplateDefinition(int tenantId, TemplateDefinition definition, int currentVersion, boolean checkVersion, AsyncCallback<AdminDefinitionModifyResult> callback);

	void getReportTemplateDefinitionNameList(int tenantId, AsyncCallback<List<Name>> callback);

	/**
	 * ReportingEngineで使用可能な出力形式の一覧を取得します。
	 *
	 * @return List<OutputFileType>
	 * @param tenantId テナントID
	 * @param type レポートタイプ
	 * @param callback  Callbackクラス
	 */
	void getOutputFileTypeList( int tenantId, String type, AsyncCallback<List<OutputFileType>> callback);

	/**
	 * ReportingEngineでレポート出力形式の一覧を取得します。
	 *
	 * @return List<Name>
	 * @param tenantId テナントID
	 * @param callback  Callbackクラス
	 */
	void getReportTypeList( int tenantId, AsyncCallback<List<Name>> callback);

	/* ---------------------------------------
	 * StaticResource
	 --------------------------------------- */

	/**
	 * 静的リソースの名前リストを取得します。
	 *
	 * @param tenantId  テナントID
	 * @param callback  Callbackクラス
	 */
	void getStaticResourceDefinitionNameList(int tenantId, AsyncCallback<List<Name>> callback);

	void getStaticResourceDefinitionEntry(int tenantId, String name, AsyncCallback<DefinitionEntry> callback);

	/**
	 * StaticResource定義を作成します。
	 *
	 * @param tenantId テナントID
	 * @param definitionName 定義名称
	 * @param callback Callbackクラス
	 */
	void createStaticResourceDefinition(int tenantId, DefinitionSummary definitionName, AsyncCallback<AdminDefinitionModifyResult> callback);

	/**
	 * StaticResource定義を削除します。
	 *
	 * @param tenantId  テナントID
	 * @param name 定義名
	 * @param callback  Callbackクラス
	 */
	void deleteStaticResourceDefinition(int tenantId, String name, AsyncCallback<AdminDefinitionModifyResult> callback);

	/**
	 * StaticResource定義をコピーします。
	 *
	 * @param tenantId テナントID
	 * @param sourceName コピー元定義名
	 * @param newDefinitionName 新定義名称
	 * @param callback Callbackクラス
	 */
	void copyStaticResourceDefinition(int tenantId, String sourceName, DefinitionSummary newDefinitionName, AsyncCallback<AdminDefinitionModifyResult> callback);

	/* ---------------------------------------
	 * EntityWebApi
	 --------------------------------------- */

	/**
	 * EntityWebApiDefinitionの一覧を取得します。
	 *
	 * @param tenantId テナントID
	 * @param callback  Callbackクラス
	 */
	void getEntityWebApiDefinitionEntryList(int tenantId, AsyncCallback<List<DefinitionEntry>> callback);


	/**
	 * EntityWebApiDefinitionを登録します。
	 *
	 * @param tenantId テナントID
	 * @param ec EntityWebApiDefinition情報が格納されたリスト
	 * @param callback  Callbackクラス
	 */
	void registEntityWebApiDefinition(int tenantId, List<DefinitionEntry> ec, final boolean checkVersion, AsyncCallback<Boolean> callback);


	/* ---------------------------------------
	 * AuthenticationPolicy
	 --------------------------------------- */

	/**
	 * AuthenticationPolicyDefinitionで選択可能なAuthProviderの名前を返します。
	 *
	 * @param tenantId    テナントID
	 * @param callback  Callbackクラス
	 */
	void getSelectableAuthProviderNameList(int tenantId, AsyncCallback<List<String>> callback);

	/* ---------------------------------------
	 * Queue
	 --------------------------------------- */

	/**
	 * ServiceConfigに設定されているQueueNameListを取得します。
	 *
	 * @param tenantId テナントID
	 * @param callback  Callbackクラス
	 */
	void getQueueNameList(int tenantId, AsyncCallback<List<String>> callback);

	/**
	 * Task情報を検索します。
	 *
	 * @param tenantId テナントID
	 * @param cond 検索条件
	 * @param callback Callbackクラス
	 */
	void searchAsyncTaskInfo(int tenantId, final AsyncTaskInfoSearchCondtion cond, AsyncCallback<List<TaskSearchResultInfo>> callback);

	/**
	 * Task情報をロードします。
	 *
	 * @param tenantId テナントID
	 * @param queueName Queue名
	 * @param taskId タスクID
	 * @param callback Callbackクラス
	 */
	void loadAsyncTaskInfo(int tenantId, final String queueName, final long taskId, AsyncCallback<TaskLoadResultInfo> callback);

	/**
	 * Taskをキャンセルします。
	 *
	 * @param tenantId テナントID
	 * @param queueName Queue名
	 * @param taskIds タスクID(List)
	 * @param callback Callbackクラス
	 */
	void cancelAsyncTask(int tenantId, final String queueName, final List<Long> taskIds, AsyncCallback<TaskCancelResultInfo> callback);

	/**
	 * Taskを強制削除します。
	 *
	 * @param tenantId テナントID
	 * @param queueName Queue名
	 * @param taskIds タスクID(List)
	 * @param callback Callbackクラス
	 */
	void forceDeleteAsyncTask(int tenantId, final String queueName, final List<Long> taskIds, AsyncCallback<TaskForceDeleteResultInfo> callback);

	/* ---------------------------------------
	 * Menu item
	 --------------------------------------- */

	/**
	 * ServiceConfigに設定されているイメージカラーを取得します。
	 *
	 * @param tenantId テナントID
	 * @param callback  Callbackクラス
	 */
	void getImageColorList(int tenantId, AsyncCallback<List<String>> callback);
}
