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

package org.iplass.adminconsole.server.tools.rpc.metaexplorer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.adminconsole.server.base.i18n.AdminResourceBundleUtil;
import org.iplass.adminconsole.server.base.io.upload.UploadRuntimeException;
import org.iplass.adminconsole.server.base.rpc.util.TransactionUtil;
import org.iplass.adminconsole.server.base.service.AdminEntityManager;
import org.iplass.adminconsole.server.metadata.rpc.MetaDataTreeBuilder;
import org.iplass.adminconsole.shared.metadata.dto.MetaTreeNode;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.ImportMetaDataStatus;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.MetaDataCheckResultInfo;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.MetaDataExplorerRuntimeException;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.MetaDataImportResultInfo;
import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.entity.DeleteOption;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.GenericEntity;
import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.SortSpec;
import org.iplass.mtp.entity.query.SortSpec.SortType;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.impl.metadata.MetaDataEntryInfo;
import org.iplass.mtp.impl.metadata.MetaDataRepository;
import org.iplass.mtp.impl.metadata.MetaDataRepositoryKind;
import org.iplass.mtp.impl.metadata.MetaDataRuntimeException;
import org.iplass.mtp.impl.metadata.MetaDataStore;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.impl.tenant.MetaTenant;
import org.iplass.mtp.impl.tools.metaport.MetaDataCheckResult;
import org.iplass.mtp.impl.tools.metaport.MetaDataImportResult;
import org.iplass.mtp.impl.tools.metaport.MetaDataImportStatus;
import org.iplass.mtp.impl.tools.metaport.MetaDataPortingRuntimeException;
import org.iplass.mtp.impl.tools.metaport.MetaDataPortingService;
import org.iplass.mtp.impl.tools.metaport.MetaDataTagEntity;
import org.iplass.mtp.impl.tools.metaport.XMLEntryInfo;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.Tenant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetaDataPortingLogic {

	private static final Logger logger = LoggerFactory.getLogger(MetaDataPortingLogic.class);

	public static final String KEY_ROOT_NODE = "KEY_ROOT_NODE";
	public static final String KEY_FILE_NAME = "KEY_FILE_NAME";
	public static final String KEY_ENTRY_COUNT = "KEY_ENTRY_COUNT";

	private MetaDataPortingService metaService;
	private EntityManager em;

	public static MetaDataPortingLogic getInstance() {
		return new MetaDataPortingLogic();
	}

	private MetaDataPortingLogic() {
		metaService = ServiceRegistry.getRegistry().getService(MetaDataPortingService.class);
		em = AdminEntityManager.getInstance();
	}

	/**
	 * インポートファイルを保存します。
	 *
	 * @param file インポートファイル
	 * @param name 名前
	 * @param description 説明
	 * @param type タイプ
	 * @return 登録後Entity
	 */
	public Entity saveImportFile(File file, String name, String description, String type) {

		//Entityの作成
		Entity entity = new GenericEntity(MetaDataTagEntity.ENTITY_DEFINITION_NAME);
		entity.setName(name);
		entity.setDescription(description);
		entity.setValue(MetaDataTagEntity.TYPE, new SelectValue(type));
		BinaryReference br = toBinaryReference(file);
		entity.setValue(MetaDataTagEntity.METADATA, br);

		//Entityに登録
		String oid = em.insert(entity);
		entity.setOid(oid);

		return entity;
	}

	/**
	 * インポートファイルを削除します。
	 *
	 * @param tagOid OID
	 */
	public void removeImportFile(final String tagOid) {
		Entity entity = em.load(tagOid, MetaDataTagEntity.ENTITY_DEFINITION_NAME);
		if (entity != null) {
			if (MetaDataTagEntity.TYPE_IMPORT_FILE.equals(
					entity.getValueAs(SelectValue.class, MetaDataTagEntity.TYPE).getValue())) {
				em.delete(entity, new DeleteOption(false));
			}
		}
	}

	/**
	 * TenantLocalStoreのメタデータをTagとして保存します。
	 *
	 * @param tenantId    テナントID
	 * @param name        タグ名
	 * @param description コメント
	 * @return 登録後のOID
	 */
	public String createTag(final int tenantId, final String name, final String description) {

		Entity entity = null;
		try {

			//一時出力ファイル生成
			File exportFile = File.createTempFile("tmp", ".tmp");
			PrintWriter writer = new PrintWriter(exportFile, "UTF-8");

			try {
				//LocalStoreのMetaDataのパスを取得
				MetaDataRepository repos = ServiceRegistry.getRegistry().getService(MetaDataRepository.class);
				MetaDataStore rdb = repos.getTenantLocalStore();
				List<MetaDataEntryInfo> list = rdb.definitionList(tenantId, "/");
				List<String> paths = new ArrayList<String>(list.size());
				for (MetaDataEntryInfo info: list) {
					paths.add(info.getPath());
				}

				//Export
				metaService.write(writer, paths);

			} finally {
				if (writer != null) {
					writer.close();
				}
			}

			//File->Entity
			entity = saveImportFile(exportFile, name, description, MetaDataTagEntity.TYPE_SNAPSHOT);

			//File delete
			exportFile.delete();

		} catch (IOException e) {
			TransactionUtil.setRollback();
			logger.error(e.getMessage(), e);
		}

		if (entity != null) {
			return entity.getOid();
		}
		return null;
	}

	/**
	 * インポートデータのツリーを返します。
	 *
	 * @param tagOid OID
	 * @return インポートデータ情報
	 */
	public Map<String, Object> tagTree(final String tagOid) {

		Map<String, Object> ret = new HashMap<String, Object>();

		//MetaDataEntryの取得
		TagEntryInfo entryInfo = getTagMetaDataEntry(tagOid);

		//MetaDataEntry->MetaDataEntryInfoの変換
		List<MetaDataEntryInfo> definitionList = convertMetaDataEntryInfo(entryInfo.getEntryInfo());

		//MetaDataEntryInfo->MetaTreeNodeの変換
		MetaTreeNode pathRoot = new MetaDataTreeBuilder().items(definitionList).build();

		ret.put(KEY_ROOT_NODE, pathRoot);
		ret.put(KEY_FILE_NAME, entryInfo.getTagName());
		ret.put(KEY_ENTRY_COUNT, definitionList.size());

		return ret;
	}

	/**
	 * インポートデータに対する登録済データのステータスをチェックします。
	 *
	 * @param tagOid インポート対象OID
	 * @param pathList 対象のメタデータパス
	 * @return
	 */
	public List<ImportMetaDataStatus> checkImportStatus(final String tagOid, final List<String> pathList) {

		//タグに含まれるEntry情報を取得
		TagEntryInfo entryInfo = getTagMetaDataEntry(tagOid);
		XMLEntryInfo xmlInfo = entryInfo.getEntryInfo();

		List<ImportMetaDataStatus> result = new ArrayList<ImportMetaDataStatus>(pathList.size());
		for (String path : pathList) {
			MetaDataImportStatus storedStatus = metaService.checkStatus(xmlInfo, path);

			ImportMetaDataStatus status = new ImportMetaDataStatus();

			MetaDataEntry entry = xmlInfo.getPathEntry(path);
			RootMetaData metaData = entry.getMetaData();
			status.setId(metaData.getId());
			status.setPath(path);

			//ImportStatus -> ImportMetaDataStatus
			status.setImportActionName(resourceString("ImportAction." + storedStatus.getAction().displayName()));	//表示名に変換
			status.setError(storedStatus.isError());
			status.setWarn(storedStatus.isWarn());
			status.setInfo(storedStatus.isInfo());
			status.setMessage(storedStatus.getMessage());
			status.setMessageDetail(storedStatus.getMessageDetail());

			result.add(status);
		}
		return result;
	}

	/**
	 * インポートファイルに含まれるテナントを返します。
	 *
	 * @param tagOid インポート対象OID
	 * @return Tenant
	 */
	public Tenant getImportTenant(final String tagOid) {

		//MetaDataEntryの取得
		TagEntryInfo entryInfo = getTagMetaDataEntry(tagOid);

		for (Map.Entry<String, MetaDataEntry> entry : entryInfo.getEntryInfo().getPathEntryMap().entrySet()) {
			if (metaService.isTenantMeta(entry.getKey())) {
				MetaTenant meta = (MetaTenant)entry.getValue().getMetaData();
				Tenant tenant = new Tenant();
				meta.applyToTenant(tenant);

				tenant.setId(-1);								//IDはセットされない（不明なので未セット）
				tenant.setName(meta.getName());					//nameはapplyToTenantでセットされないのでセット(DB側優先)
				tenant.setDescription(meta.getDescription());	//descriptionはapplyToTenantでセットされないのでセット(DB側優先)

				return tenant;
			}
		}
		return null;
	}

	/**
	 * インポートを実行します。
	 *
	 * @param tagOid インポート対象OID
	 * @param pathList インポート対象メタデータパス
	 * @param importTenant インポート対象Tenant
	 * @return インポート結果
	 */
	public MetaDataImportResultInfo importMetaData(final String tagOid, final List<String> pathList, final Tenant importTenant) {

		MetaDataImportResultInfo result = new MetaDataImportResultInfo();

		TagEntryInfo entryInfo = null;
		try {
			entryInfo = getTagMetaDataEntry(tagOid);
		} catch (Exception e) {
			TransactionUtil.setRollback();

			logger.error("meta data import failed.", e);

			result.setError(true);
			result.clearMessages();
			result.addMessages(resourceString("errImportTargetGet",  e.getMessage()));
			return result;
		}

		MetaDataImportResult metaResult = metaService.importMetaData(entryInfo.getTagName(), entryInfo.getEntryInfo(), pathList, importTenant);

		//MetaDataImportResult -> MetaDataImportResultInfo
		result.setError(metaResult.isError());
		result.setMessages(metaResult.getMessages());

		return result;
	}

	/**
	 * タグのリストを返します。
	 *
	 * @return タグ一覧
	 */
	public List<Entity> getTagList() {
		return em.searchEntity(
				new Query()
				.selectAll(MetaDataTagEntity.ENTITY_DEFINITION_NAME, false, false)
				.where(new Equals(MetaDataTagEntity.TYPE, MetaDataTagEntity.TYPE_SNAPSHOT))
				.order(new SortSpec(Entity.CREATE_DATE, SortType.DESC), new SortSpec(Entity.OID, SortType.DESC)))
				.getList();
	}

	/**
	 * タグを削除します。
	 *
	 * @param tagList 削除対象のタグリスト
	 */
	public void removeTag(final List<String> tagList) {
		for (String oid : tagList) {
			Entity entity = em.load(oid, MetaDataTagEntity.ENTITY_DEFINITION_NAME);
			if (entity != null) {
				em.delete(entity, new DeleteOption(false));
			}
		}
	}

	/**
	 * タグに含まれるMetaDataEntryを返します。
	 *
	 * @param tagOid インポート対象OID
	 * @return
	 */
	public TagEntryInfo getTagMetaDataEntry(String tagOid) {

		Entity tagEntity = em.load(tagOid, MetaDataTagEntity.ENTITY_DEFINITION_NAME);

		if (tagEntity == null) {
			throw new MetaDataExplorerRuntimeException(resourceString("canNotGetUploadFileInfo", tagOid));
		}

		TagEntryInfo tagInfo = new TagEntryInfo();
		tagInfo.setTagName(tagEntity.getName());

		BinaryReference binaryReference = tagEntity.getValue(MetaDataTagEntity.METADATA);

		InputStream metaXml = null;
		try {
			metaXml = em.getInputStream(binaryReference);

			if (metaXml == null) {
				throw new MetaDataExplorerRuntimeException(resourceString("canNotGetMetaDataInfo", tagEntity.getOid()));
			}

			XMLEntryInfo entryInfo = metaService.getXMLMetaDataEntryInfo(metaXml);
			tagInfo.setEntryInfo(entryInfo);

			return tagInfo;
		} catch (MetaDataPortingRuntimeException e) {
			throw new MetaDataExplorerRuntimeException(resourceString("canNotAnalysisMetaData", tagEntity.getOid()), e);
		} finally {
			try {
				if (metaXml != null) {
					metaXml.close();
					metaXml = null;
				}
			} catch (IOException e) {
				throw new MetaDataExplorerRuntimeException(resourceString("canNotAnalysisMetaData", tagEntity.getOid()), e);
			}
		}
	}

	/**
	 * インポートデータに対するメタデータ整合性をチェックします。
	 * 
	 * @param tagOid インポート対象OID
	 * @param pathList インポート対象メタデータパス
	 * @return チェック結果
	 */
	public MetaDataCheckResultInfo checkMetaData(final String tagOid, final List<String> pathList) {
		// インポート対象のメタデータファイル取得
		TagEntryInfo entryInfo = null;
		try {
			entryInfo = getTagMetaDataEntry(tagOid);
		} catch (Exception e) {
			TransactionUtil.setRollback();

			logger.error("meta data check failed.", e);

			MetaDataCheckResultInfo result = new MetaDataCheckResultInfo(true);
			result.setMessage(resourceString("errImportTargetGet", e.getMessage()));

			return result;
		}

		// メタデータ整合性チェック
		MetaDataCheckResult metaResult = metaService.checkMetaData(entryInfo.getTagName(), entryInfo.getEntryInfo(), pathList);

		// MetaDataCheckResult -> MetaDataCheckResultInfo
		MetaDataCheckResultInfo result = new MetaDataCheckResultInfo(metaResult.isError());
		result.setWarn(metaResult.isWarn());
		result.setMessage(metaResult.getMessage());
		result.setMetaDataPaths(metaResult.getMetaDataPaths());

		return result;
	}

	private List<MetaDataEntryInfo> convertMetaDataEntryInfo(XMLEntryInfo entryInfo) throws MetaDataRuntimeException {

		String path = "/";

		ArrayList<MetaDataEntryInfo> res = new ArrayList<MetaDataEntryInfo>();
		for (Map.Entry<String, MetaDataEntry> e: entryInfo.getPathEntryMap().entrySet()) {
			if (e.getKey().startsWith(path)) {
				RootMetaData meta = e.getValue().getMetaData();
				MetaDataEntryInfo node = new MetaDataEntryInfo();
				node.setPath(e.getKey());
				node.setId(meta.getId());
				node.setDisplayName(meta.getDisplayName());
				node.setDescription(meta.getDescription());
				node.setRepository(MetaDataRepositoryKind.XMLRESOURCE.getDisplayName());
				res.add(node);
			}
		}

		//ソート
		Collections.sort(res, new Comparator<MetaDataEntryInfo>() {
			@Override
			public int compare(MetaDataEntryInfo o1, MetaDataEntryInfo o2) {
				return o1.getPath().toLowerCase().compareTo(o2.getPath().toLowerCase());
			}
		});
		return res;
	}

	private BinaryReference toBinaryReference(File file) {
		// マジックバイトチェックは特にしない

		FileInputStream is = null;
		try {
			is = new FileInputStream(file);
			return em.createBinaryReference(file.getName(), "text/xml", is);
		} catch (FileNotFoundException e) {
			throw new UploadRuntimeException(resourceString("canNotGetImportTargetFile"));
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					logger.warn("can not close resource:" + file.getName(), e);
				}
			}
		}
	}

	private static String resourceString(String key, Object... arguments) {
		return AdminResourceBundleUtil.resourceString("tools.metaexplorer.MetaDataPortingLogic." + key, arguments);
	}

}
