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

import org.iplass.adminconsole.shared.base.dto.i18n.I18nMetaDisplayInfo;
import org.iplass.adminconsole.shared.metadata.dto.AdminDefinitionModifyResult;
import org.iplass.adminconsole.shared.metadata.dto.MetaDataInfo;
import org.iplass.adminconsole.shared.metadata.dto.MetaTreeNode;
import org.iplass.adminconsole.shared.metadata.dto.MetaVersionCheckException;
import org.iplass.adminconsole.shared.metadata.dto.Name;
import org.iplass.adminconsole.shared.metadata.dto.entity.EntitySchemaLockedException;
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
import org.iplass.mtp.webapi.definition.EntityWebApiDefinition;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.client.rpc.XsrfProtectedService;

@RemoteServiceRelativePath("service/metaData")
public interface MetaDataService extends XsrfProtectedService {

	/* ---------------------------------------
	 * 共通
	 --------------------------------------- */

	public List<String> getAllMetaDataPath(final int tenantId);

	/**
	 * <p>
	 * 対象コンテキストパスをRootとしたMetaData情報ツリーを返します。
	 * </p>
	 *
	 * @param tenantId テナントID
	 * @param contextPath コンテキストパス
	 * @return MetaData情報ツリー
	 */
	public <D extends Definition> MetaTreeNode getMetaDataTree(int tenantId, String className);

	/**
	 * <p>
	 * MetaData情報を返します。
	 * </p>
	 *
	 * @param tenantId テナントID
	 * @param className   対象メタデータクラス
	 * @param name 定義名
	 * @return MetaData情報
	 */
	public <D extends Definition> MetaDataInfo getMetaDataInfo(int tenantId, String className, String name);

	/**
	 * <p>
	 * MetaData情報を返します。
	 * </p>
	 *
	 * @param tenantId テナントID
	 * @param path MetaDataのパス
	 * @return MetaData情報
	 */
	public <D extends Definition> MetaDataInfo getMetaDataInfo(int tenantId, String path);

	/**
	 * <p>
	 * MetaDataの表示用情報を返します。
	 * </p>
	 *
	 * @param tenantId テナントID
	 * @param className   対象メタデータクラス
	 * @param name 定義名
	 * @return {@link I18nMetaDisplayInfo MetaDataの表示情報}
	 */
	public <D extends Definition> I18nMetaDisplayInfo getMetaDisplayInfo(int tenantId, String className, String name);

	/**
	 * <p>
	 * 対象Pathに一致するMetaDataの表示用情報を返します。
	 * </p>
	 *
	 * @param tenantId テナントID
	 * @param path MetaDataのパス
	 * @return {@link I18nMetaDisplayInfo MetaDataの表示情報}
	 */
	public I18nMetaDisplayInfo getMetaDisplayInfo(int tenantId, String path);

	/**
	 * <p>
	 * 対象MetaDataのステータスチェックを実行します。
	 * </p>
	 *
	 * @param tenantId テナントID
	 * @param className MetaDataクラス名
	 * @param name MetaData定義名
	 * @return ステータスエラーメッセージ(正常時はnull)
	 */
	public <D extends Definition> String checkStatus(int tenantId, String className, String name);

	/**
	 * <p>
	 * 対象MetaDataのステータスチェックを実行します。
	 * </p>
	 *
	 * @param tenantId テナントID
	 * @param paths MetaDataのパス
	 * @return ステータスエラーメッセージ(正常時はEmpty)
	 */
	public LinkedHashMap<String, String> checkStatus(int tenantId, List<String> paths);

	/**
	 * キャッシュとして保持しているMetaData情報をクリアします。
	 *
	 * @param tenantId テナントID
	 */
	public void clearAllCache(int tenantId);

	public void updateSharedConfig(int tenantId, String className, String name, SharedConfig config);

	public AdminDefinitionModifyResult renameDefinition(int tenantId, String className, String fromName, String toName);

	public DefinitionInfo getDefinitionInfo(int tenantId, String className, String name);

	public Map<String, String> getEnableLanguages(int tenantId);

	/**
	 * メタデータの更新履歴を取得します。
	 *
	 * @param tenantId    テナントID
	 * @param className   対象メタデータクラス
	 * @param definitionId    メターデータID
	 */
	public DefinitionInfo getHistoryById(int tenantId, String className, String definitionId);

	/**
	 * Definitionの一覧を取得します。
	 *
	 * @param tenantId テナントID
	 * @param className Definitionクラス
	 * @return Definitionの一覧
	 */
	public List<Name> getDefinitionNameList(int tenantId, String className);

	/**
	 * DefinitionEntryを取得します。
	 *
	 * @param tenantId テナントID
	 * @param className Definitionクラス
	 * @param name 定義名
	 * @return DefinitionEntry
	 */
	public DefinitionEntry getDefinitionEntry(int tenantId, String className, String name);

	/**
	 * Definitionを取得します。
	 *
	 * @param tenantId テナントID
	 * @param className Definitionクラス
	 * @param name 定義名
	 * @return Definition
	 */
	public <D extends Definition> D getDefinition(int tenantId, String className, String name);

	/**
	 * Definitionを作成します。
	 * @param tenantId テナントID
	 * @param definition 追加Definition
	 * @return 更新結果
	 */
	public <D extends Definition> AdminDefinitionModifyResult createDefinition(int tenantId, D definition);

	/**
	 * Definitionを更新します。
	 * @param tenantId テナントID
	 * @param definition 更新Definition
	 * @param currentVersion 現在のバージョン
	 * @param checkVersion バージョンチェックの有無
	 * @return 更新結果
	 * @throws MetaVersionCheckException
	 */
	public <D extends Definition> AdminDefinitionModifyResult updateDefinition(int tenantId, D definition, int currentVersion, boolean checkVersion) throws MetaVersionCheckException;

	/**
	 * Definitionを削除します。
	 * @param tenantId テナントID
	 * @param className Definitionクラス
	 * @param name 定義名
	 * @return 更新結果
	 */
	public <D extends Definition> AdminDefinitionModifyResult deleteDefinition(int tenantId, String className, String name);

	/**
	 * Definitionをコピーします。
	 * @param tenantId テナントID
	 * @param className Definitionクラス
	 * @param sourceName コピー元定義名
	 * @param newName コピー先定義名
	 * @param displayName 表示名
	 * @param description 概要
	 * @return 更新結果
	 */
	public <D extends Definition> AdminDefinitionModifyResult copyDefinition(int tenantId, String className, String sourceName, String newName, String displayName, String description);

	/* ---------------------------------------
	 * Entity
	 --------------------------------------- */

	public List<Name> getEntityDefinitionNameList(int tenantId);

	public EntityDefinition getEntityDefinition(int tenantId, String name);

	public DefinitionEntry getEntityDefinitionEntry(int tenantId, String name);

	public AdminDefinitionModifyResult createEntityDefinition(int tenantId, EntityDefinition definition);

	public AdminDefinitionModifyResult updateEntityDefinition(int tenantId, EntityDefinition definition, int currentVersion, boolean checkVersion) throws MetaVersionCheckException, EntitySchemaLockedException;

	public AdminDefinitionModifyResult updateEntityDefinition(int tenantId, EntityDefinition definition, int currentVersion, Map<String, String> renamePropertyMap, boolean checkVersion) throws MetaVersionCheckException, EntitySchemaLockedException;

	public boolean checkLockEntityDefinition(int tenantId, String defName);

	public AdminDefinitionModifyResult deleteEntityDefinition(int tenantId, String name);

	public AdminDefinitionModifyResult copyEntityDefinition(int tenantId, String sourceName,
			String newName, String displayName, String description,
			boolean isCopyEntityView, boolean isCopyEntityFilter, boolean isCopyEntityWebAPI);

	public List<Name> getPropertyDefinitionNameList(int tenantId, String name);

	public PropertyDefinition getPropertyDefinition(int tenantId, String name, String propertyName);

	public Name getEntityDefinitionName(int tenantId, String name);

	public String getPropertyDisplayName(int tenantId, String name, String propertyName);

	public List<String> getEntityStoreSpaceList(int tenantId);

	public Long getAutoNumberCurrentValue(int tenantId, String name, String propertyName);

	public void resetAutoNumberCounter(int tenantId, String name, String propertyName, long startsWith);

	public List<SortInfo> getSortInfo(int tenantId, String orderBy);

	public List<EntityDefinition> getCrawlTargetEntityList(int tenantId);

	public Map<String, List<String>> getCrawlTargetEntityViewMap(int tenantId);

	/* ---------------------------------------
	 * EntityView
	 --------------------------------------- */

	public SearchFormView createDefaultSearchFormView(int tenantId, String name);

	public DetailFormView createDefaultDetailFormView(int tenantId, String name);

	public List<Entity> getRoles(int tenantId);

	/* ---------------------------------------
	 * Menu Item
	 --------------------------------------- */

	/**
	 * メニューアイテムを全件取得します。
	 *
	 * @param tenantId  テナントID
	 * @return MenuItemHolder({@link MenuItem}のList)
	 */
	public MenuItemHolder getMenuItemList(int tenantId);

	/**
	 * メニューアイテムを作成します。
	 *
	 * @param tenantId テナントID
	 * @param definition 追加{@link MenuItem}
	 * @return 更新結果
	 */
	public AdminDefinitionModifyResult createMenuItem(int tenantId, MenuItem definition);

	/**
	 * メニューアイテムを更新します。
	 *
	 * @param tenantId テナントID
	 * @param definition 更新{@link MenuItem}
	 * @return 更新結果
	 */
	public AdminDefinitionModifyResult updateMenuItem(int tenantId, MenuItem definition);

	/* ---------------------------------------
	 * Template
	 --------------------------------------- */

	public DefinitionEntry getTemplateDefinitionEntry(int tenantId, String name);

	/**
	 * Template定義を更新します。
	 *
	 * @param tenantId  テナントID
	 * @param definition Template定義
	 * @return 更新結果
	 */
	public AdminDefinitionModifyResult updateTemplateDefinition(int tenantId, TemplateDefinition definition, int currentVersion, boolean checkVersion) throws MetaVersionCheckException;

	public List<Name> getReportTemplateDefinitionNameList(int tenantId);

	/**
	 * ReportingEngineで使用可能な出力形式の一覧を取得します。
	 *
	 * @param tenantId テナントID
	 * @param type reportType
	 * @return List<OutputFileType>
	 */
	public List<OutputFileType> getOutputFileTypeList(int tenantId, String type);

	/**
	 * ReportingEngineでレポート出力形式の一覧を取得します。
	 *
	 * @return List<Name>
	 * @param tenantId テナントID
	 */
	public List<Name> getReportTypeList( int tenantId );

	/* ---------------------------------------
	 * StaticResource
	 --------------------------------------- */

	/**
	 * 静的リソースの名前リストを取得します。
	 *
	 * @param tenantId  テナントID
	 * @return 静的リソースの名前リスト
	 */
	public List<Name> getStaticResourceDefinitionNameList(int tenantId);

	public DefinitionEntry getStaticResourceDefinitionEntry(int tenantId, String name);

	/**
	 * StaticResource定義を作成します。
	 *
	 * @param tenantId テナントID
	 * @param definitionName 定義名称
	 * @return 更新結果
	 */
	public AdminDefinitionModifyResult createStaticResourceDefinition(int tenantId, DefinitionSummary definitionName);

	/**
	 * StaticResource定義を削除します。
	 *
	 * @param tenantId  テナントID
	 * @param name 定義名
	 * @return 更新結果
	 */
	public AdminDefinitionModifyResult deleteStaticResourceDefinition(int tenantId, String name);

	/**
	 * StaticResource定義をコピーします。
	 *
	 * @param tenantId テナントID
	 * @param sourceName コピー元定義名
	 * @param newDefinitionName 新定義名称
	 * @return 更新結果
	 */
	public AdminDefinitionModifyResult copyStaticResourceDefinition(int tenantId, String sourceName, DefinitionSummary newDefinitionName);

	/* ---------------------------------------
	 * EntityWebApi
	 --------------------------------------- */

	/**
	 * EntityWebApiDefinitionの一覧を取得します。
	 *
	 * @param tenantId テナントID
	 * @return {@link EntityWebApiDefinition}
	 */
	public List<DefinitionEntry> getEntityWebApiDefinitionEntryList(int tenantId);

	/**
	 * EntityWebApiDefinitionを登録します。
	 *
	 * @param tenantId テナントID
	 * @param ec EntityWebApiDefinition情報が格納されたリスト
	 * @return boolean
	 */
	public boolean registEntityWebApiDefinition(int tenantId, List<DefinitionEntry> ec, final boolean checkVersion) throws MetaVersionCheckException;

	/* ---------------------------------------
	 * AuthenticationPolicy
	 --------------------------------------- */

	/**
	 * AuthenticationPolicyDefinitionで選択可能なAuthProviderの名前を返します。
	 *
	 * @param tenantId    テナントID
	 * @return 選択可能なAuthProviderの名前リスト
	 */
	public List<String> getSelectableAuthProviderNameList(int tenantId);

	/* ---------------------------------------
	 * Queue
	 --------------------------------------- */

	/**
	 * ServiceConfigに設定されているQueueNameListを取得します。
	 * @param tenantId
	 * @return QueueNameList
	 */
	public List<String> getQueueNameList(int tenantId);

	public List<TaskSearchResultInfo> searchAsyncTaskInfo(int tenantId, final AsyncTaskInfoSearchCondtion cond);

	public TaskLoadResultInfo loadAsyncTaskInfo(int tenantId, final String queueName, final long taskId);

	public TaskCancelResultInfo cancelAsyncTask(int tenantId, final String queueName, final List<Long> taskIds);

	public TaskForceDeleteResultInfo forceDeleteAsyncTask(final int tenantId, final String queueName, final List<Long> taskIds);

	/* ---------------------------------------
	 * Menu item
	 --------------------------------------- */

	/**
	 * ServiceConfigに設定されているイメージカラーを取得します。
	 * @param tenantId
	 * @return imageColorList
	 */
	public List<String> getImageColorList(int tenantId);
}
