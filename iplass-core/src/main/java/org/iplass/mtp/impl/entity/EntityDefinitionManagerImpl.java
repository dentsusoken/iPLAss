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

package org.iplass.mtp.impl.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.definition.DefinitionManager;
import org.iplass.mtp.definition.DefinitionSummary;
import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.definition.EntityDefinitionModifyResult;
import org.iplass.mtp.impl.datastore.StoreService;
import org.iplass.mtp.impl.definition.DefinitionService;
import org.iplass.mtp.impl.entity.property.PrimitivePropertyHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.metadata.MetaDataDuplicatePathException;
import org.iplass.mtp.impl.metadata.MetaDataEntryInfo;
import org.iplass.mtp.impl.properties.extend.AutoNumberType;
import org.iplass.mtp.impl.properties.extend.AutoNumberType.AutoNumberTypeRuntime;
import org.iplass.mtp.impl.util.CoreResourceBundleUtil;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class EntityDefinitionManagerImpl implements
		EntityDefinitionManager {

	private static final Logger logger = LoggerFactory.getLogger(EntityDefinitionManagerImpl.class);

	private EntityService dmHandlerService;
	private DefinitionService dm;

	//TODO メタデータの整合性について、以下のようなことを検討して実現する
	//・メタデータの変更前と変更後で実データの整合性を保つ必要がある
	//・可能な限り、オンラインのままメタデータの変更を可能とする
	// ・インデックスの変更は、オンラインのままで変更可能でかつ整合性を確保したい
	// ・論理的なロックの概念の導入検討が必要。行レベルロックではインサートは防げない。全テナント同一テーブルなので、テーブルロックできない。
	// ・ロックを導入したとしても、ロック前から実行中のトランザクションをとめることは難しい。
	// ・ロック前から実行中のトランザクションによる不整合が起こったデータを修正できるように、
	//  メタデータの変更履歴、実データにメタデータのバージョンを保持し、
	//  古いバージョンのメタデータ情報を元に登録された実データを（バッチ等により）自動修正可能とする。
	// ・項目の追加・削除のみＯＫとして、実データに影響するような更新（データ型の変更）はアプリケーション停止を前提とするか。（かなり機能性は落ちるが。。。）
	//・可能であれば、メタデータの変更をロールバック（Undo）できるようにする
	//・テスト的に何度か試行錯誤して、これでＯＫっといったものを反映できるように
	//・いくつかの変更をまとめて、後々どこかのタイミングで一括で適用、といったことを可能にする

	public EntityDefinitionManagerImpl() {
		dmHandlerService = ServiceRegistry.getRegistry().getService(EntityService.class);
		dm = DefinitionService.getInstance();
	}

	@Override
	public EntityDefinitionModifyResult create(EntityDefinition definition) {

		//FIXME 自身への参照（階層的な参照定義）が定義されていた場合の対応

		//1.同名の定義がすでに存在しないかどうかをチェック
		//2.定義内容に間違いがないかをチェック
		//3.DataModelDefinitionを元に、必要なStore固有の定義を自動生成
		//4.定義をStoreに保存

		EntityDefinition checked = checkAndModify(definition);
		final Future<String> result = dmHandlerService.createDataModelSchema(checked);

		return new EntityDefinitionModifyResult() {
			private static final long serialVersionUID = FIXED_SERIAL_VERSION;
//			private String message;

			@Override
			public boolean isSuccess() {
				String id = null;
				try {
					id = result.get();
				} catch (ExecutionException e) {
					//TODO 本格的な非同期実行サービス実装後は、そちらに処理結果を保存して、後から確認できるようにする。
					message = "exception occured during entity definition create:" + e.getCause().getMessage();
					logger.error(message, e.getCause());
					if (e.getCause() instanceof MetaDataDuplicatePathException) {
						//重複エラーは起こりやすいのでメッセージを親切にする
						message = resourceString("impl.metadata.duplicatePath");
					}
					return false;
				} catch (InterruptedException e) {
					//TODO 本格的な非同期実行サービス実装後は、そちらに処理結果を保存して、後から確認できるようにする。
					message = "execution interrrupted during entity definition create:" + e.getMessage();
					logger.error(message, e);
					return false;
				}
				if (id == null) {
					return false;
				}
				//呼び出し元のスレッドローカルなメタデータキャッシュもクリアする
				EntityContext.getCurrentContext().refreshTransactionLocalCache(id);
				return true;
			}

//			@Override
//			public String getMessage() {
//				return message;
//			}

		};

	}

	private EntityDefinition checkAndModify(EntityDefinition definition) {

		//FIXME チェック処理

		return definition;
	}

	@Override
	public List<String> definitionList() {
		return dmHandlerService.nameList();
	}

	@Override
	public List<DefinitionSummary> definitionNameList() {
		return definitionNameList("");
	}

	@Override
	public List<DefinitionSummary> definitionNameList(String filterPath) {
		//EntityについてはEntityを除外するためservice経由で作成
		//return dm.listName(EntityDefinition.class, filterPath);

		List<MetaDataEntryInfo> entryInfoList = dmHandlerService.list(filterPath);

		List<DefinitionSummary> ret = new ArrayList<DefinitionSummary>(entryInfoList.size());
		for (MetaDataEntryInfo definition : entryInfoList) {
			DefinitionSummary def = new DefinitionSummary(
					definition.getPath(),
					dm.getDefinitionName(EntityDefinition.class, definition.getPath()),
					definition.getDisplayName(), definition.getDescription());
			ret.add(def);
		}

		return ret;
	}

	@Override
	public EntityDefinition get(String definitionName) {

//		ExecuteContext context = ExecuteContext.getCurrentContext();
		EntityContext ctx = EntityContext.getCurrentContext();
		EntityHandler eh = ctx.getHandlerByName(definitionName);
		if (eh == null) {
			return null;
//			throw new EntityRuntimeException(definitionName + " is not defined.");
		}
		return eh.getMetaData().currentConfig(ctx);
	}

	@Override
	public EntityDefinitionModifyResult remove(String definitionName) {
//		final ExecuteContext context = ExecuteContext.getCurrentContext();
		EntityDefinition definition = get(definitionName);

		if (definition == null) {
			throw new EntityRuntimeException(definitionName + " not found.");
		}
		EntityDefinition checked = checkAndModify(definition);
//		EntityHandler eh = context.getEntityContext().getHandlerByName(checked.getName());
		final Future<String> result = dmHandlerService.removeDataModelSchema(checked);

		return new EntityDefinitionModifyResult() {

			private static final long serialVersionUID = FIXED_SERIAL_VERSION;
//			private String message;

			@Override
			public boolean isSuccess() {
				String id = null;
				try {
					id = result.get();
				} catch (ExecutionException e) {
					//TODO 本格的な非同期実行サービス実装後は、そちらに処理結果を保存して、後から確認できるようにする。
					message = "exception occured during entity definition remove:" + e.getCause().getMessage();
					logger.error(message, e.getCause());
					return false;
				} catch (InterruptedException e) {
					//TODO 本格的な非同期実行サービス実装後は、そちらに処理結果を保存して、後から確認できるようにする。
					message = "execution interrrupted during entity definition remove:" + e.getMessage();
					logger.error(message, e);
					return false;
				}
				if (id == null) {
					return false;
				}
				//呼び出し元のスレッドローカルなメタデータキャッシュもクリアする
				EntityContext.getCurrentContext().refreshTransactionLocalCache(id);
				return true;
			}

//			@Override
//			public String getMessage() {
//				return message;
//			}

		};
	}

	@Override
	public EntityDefinitionModifyResult update(EntityDefinition definition) {
		return update(definition, null);
	}

	@Override
	public EntityDefinitionModifyResult update(EntityDefinition definition, Map<String, String> renamePropertyMap) {


		//FIXME MappedByで定義されている定義名、プロパティ名の更新はどのように行うか？？メタデータをXmlの中に入れてしまうと、大変か。MappedByされたプロパティのみ別テーブル上で管理するか？それとも、MappedByの更新せずとも整合性を問題なくできるか？

		//1.定義が存在するかチェック
		//2.更新する定義内容に間違いがないかをチェック
		//3.更新内容の差分をチェック
		//4.オンラインで（ほかのユーザが更新中に実行しても）更新可能かチェック
		//4.1.[オンラインで更新不可の場合]リードオンリーモードになっているかを確認。なっていない場合、例外をスロー（もしくは自動的にロードオンリーモードに変更）
		//  ※FIXME リードオンリーにした方がよい更新ってなに？
		//          ⇒結局のところ、データの洗い換えが行われない（追加のみ）ような仕組みにできるかどうか。要検討。
		//5.差分内容に応じて、Store固有のデータ定義を自動修正
		//6.実データの変更が必要な場合、実データを自動修正（非同期か。しかも何回か実行する必要ありか。）
		//  ※FIXME ⇒結局のところ、データの洗い換えが行われない（追加のみ）ような仕組みにできるかどうか。要検討。
		//7.データ定義のバージョンを更新（バージョンチェックを行いながら）し、Storeに保存
		//8.後々、Undoできるよう、修正履歴をStoreへ保存（実データ修正を非同期で行う場合は、この情報を元に実行）
		//9.メタデータキャッシュの更新（DBへのCommit後に行う。ほかのAPPサーバ上の再取得がCommitより速い可能性がある）

//		final ExecuteContext context = ExecuteContext.getCurrentContext();

		if (get(definition.getName()) == null) {
			throw new EntityRuntimeException(definition.getName() + " not found.");
		}
		EntityDefinition checked = checkAndModify(definition);
//		EntityHandler eh = context.getEntityContext().getHandlerByName(checked.getName());
//		final String ehid = eh.getMetaData().getId();
		final Future<String> result = dmHandlerService.updateDataModelSchema(checked, renamePropertyMap);

		return new EntityDefinitionModifyResult() {

			private static final long serialVersionUID = FIXED_SERIAL_VERSION;
//			private String message;

			@Override
			public boolean isSuccess() {
				String id = null;
				try {
					id = result.get();
				} catch (ExecutionException e) {
					//TODO 本格的な非同期実行サービス実装後は、そちらに処理結果を保存して、後から確認できるようにする。
					message = "exception occured during entity definition update:" + e.getCause().getMessage();
					logger.error(message, e.getCause());
					return false;
				} catch (InterruptedException e) {
					//TODO 本格的な非同期実行サービス実装後は、そちらに処理結果を保存して、後から確認できるようにする。
					message = "execution interrrupted during entity definition update:" + e.getMessage();
					logger.error(message, e);
					return false;
				}
				if (id == null) {
					return false;
				}
				//呼び出し元のスレッドローカルなメタデータキャッシュもクリアする
				EntityContext.getCurrentContext().refreshTransactionLocalCache(id);
				return true;
			}

//			@Override
//			public String getMessage() {
//				return message;
//			}
		};
	}

	@Override
	public void renameEntityDefinition(String from, String to) {
		ManagerLocator.getInstance().getManager(DefinitionManager.class).rename(EntityDefinition.class, from, to);
	}

	@Override
	public void renamePropertyDefinition(String defName, String from, String to) {
		dmHandlerService.renameProperty(defName, from, to);
	}

	@Override
	public long getAutoNumberCurrentValue(String definitionName, String propertyName) {
		return getAutoNumberCurrentValue(definitionName, propertyName, null);
	}

	@Override
	public long getAutoNumberCurrentValue(String definitionName, String propertyName, String subUnitKey) {
		AutoNumberTypeRuntime typeRuntime = getAutoNumberRuntime(definitionName, propertyName);
		return typeRuntime.currentValue(subUnitKey);
	}

	@Override
	public List<Pair<String, Long>> getAutoNumberCurrentValueList(String definitionName, String propertyName) {
		final AutoNumberTypeRuntime typeRuntime = getAutoNumberRuntime(definitionName, propertyName);
		return typeRuntime.keySet().stream().map(key->{
			long value = typeRuntime.currentValue(key);
			return new ImmutablePair<String, Long>(key, value);
		}).collect(Collectors.toList());
	}

	@Override
	public void resetAutoNumberCounter(String definitionName, String propertyName, long startsWith) {
		resetAutoNumberCounter(definitionName, propertyName, null, startsWith);
	}

	@Override
	public void resetAutoNumberCounter(String definitionName, String propertyName, String subUnitKey, long startsWith) {
		AutoNumberTypeRuntime typeRuntime = getAutoNumberRuntime(definitionName, propertyName);
		typeRuntime.resetCounter(subUnitKey, startsWith);
	}

	private AutoNumberTypeRuntime getAutoNumberRuntime(String definitionName, String propertyName) {

		EntityContext eContext = EntityContext.getCurrentContext();

		EntityHandler eh = eContext.getHandlerByName(definitionName);
		PropertyHandler ph = eh.getProperty(propertyName, eContext);
		if (!(ph instanceof PrimitivePropertyHandler)) {
			throw new EntityRuntimeException(definitionName + "." + propertyName + " is not AutoNumberProperty");
		}
		if (!(((PrimitivePropertyHandler) ph).getMetaData().getType() instanceof AutoNumberType)) {
			throw new EntityRuntimeException(definitionName + "." + propertyName + " is not AutoNumberProperty");
		}
		return (AutoNumberTypeRuntime) ((PrimitivePropertyHandler) ph).getTypeSpecificRuntime();
	}

	@Override
	public List<String> getStorageSpaceList() {
		StoreService storeService = ServiceRegistry.getRegistry().getService(StoreService.class);
		return storeService.getStorageSpaceList();
	}

	@Override
	public boolean isLockedSchema(String definitionName) {
		return dmHandlerService.isLockedSchema(definitionName);
	}

	// CRUD系はオーバーライド

	@Override
	public void rename(String oldDefinitionName, String newDefinitionName) {
		renameEntityDefinition(oldDefinitionName, newDefinitionName);
	}

	@Override
	public Class<EntityDefinition> getDefinitionType() {
		return EntityDefinition.class;
	}

	@Override
	public List<DefinitionSummary> definitionSummaryList(String filterPath, boolean recursive) {
		DefinitionManager manager = ManagerLocator.getInstance().getManager(DefinitionManager.class);
		return manager.listName(getDefinitionType(), filterPath, recursive);
	}

	private static String resourceString(String key, Object... arguments) {
		return CoreResourceBundleUtil.resourceString(key, arguments);
	}
}
