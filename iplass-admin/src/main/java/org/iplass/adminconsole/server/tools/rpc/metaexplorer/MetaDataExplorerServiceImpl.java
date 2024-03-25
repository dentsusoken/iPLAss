/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.iplass.adminconsole.server.base.i18n.AdminResourceBundleUtil;
import org.iplass.adminconsole.server.base.rpc.util.AuthUtil;
import org.iplass.adminconsole.server.base.rpc.util.TransactionUtil;
import org.iplass.adminconsole.server.base.service.auditlog.MetaDataAction;
import org.iplass.adminconsole.server.base.service.auditlog.MetaDataAuditLogger;
import org.iplass.adminconsole.server.metadata.rpc.MetaDataTreeBuilder;
import org.iplass.adminconsole.shared.base.dto.entity.EntityDataTransferTypeList;
import org.iplass.adminconsole.shared.metadata.dto.MetaTreeNode;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.DeleteResultInfo;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.ImportFileInfo;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.ImportMetaDataStatus;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.Message;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.MetaDataImportResultInfo;
import org.iplass.adminconsole.shared.tools.rpc.metaexplorer.MetaDataExplorerService;
import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.impl.entity.EntityService;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.impl.metadata.MetaDataEntryInfo;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.impl.tenant.MetaTenant;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.server.rpc.jakarta.XsrfProtectedServiceServlet;

public class MetaDataExplorerServiceImpl extends XsrfProtectedServiceServlet implements MetaDataExplorerService {

	private static final long serialVersionUID = -5919810097403847465L;

	private static final Logger logger = LoggerFactory.getLogger(MetaDataExplorerServiceImpl.class);

	private EntityService ehService = ServiceRegistry.getRegistry().getService(EntityService.class);

	@Override
	public EntityDataTransferTypeList entityDataTypeWhiteList(EntityDataTransferTypeList param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public MetaTreeNode getMetaTree(final int tenantId) {

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<MetaTreeNode>() {

			@Override
			public MetaTreeNode call() {

				MetaTreeNode root = new MetaDataTreeBuilder().all().build();
				root.setName("MetaDataList");

				return root;
			}

		});
	}

	@Override
	public ImportFileInfo getMetaImportFileTree(final int tenantId, final String tagOid) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<ImportFileInfo>() {

			@Override
			public ImportFileInfo call() {

				MetaDataPortingLogic logic = MetaDataPortingLogic.getInstance();
				Map<String, Object> fileInfo = logic.tagTree(tagOid);

				MetaTreeNode pathRoot = (MetaTreeNode)fileInfo.get(MetaDataPortingLogic.KEY_ROOT_NODE);
				int entryCount = (Integer)fileInfo.get(MetaDataPortingLogic.KEY_ENTRY_COUNT);
				String fileName = (String)fileInfo.get(MetaDataPortingLogic.KEY_FILE_NAME);

				MetaTreeNode root = new MetaTreeNode();
				root.setName("metaDataList");
				root.setChildren(pathRoot.getChildren());
				root.setItems(pathRoot.getItems());

				ImportFileInfo result = new ImportFileInfo();
				result.setTagOid(tagOid);
				result.setFileName(fileName);
				result.setCount(entryCount);
				result.setRootNode(root);

				return result;
			}

		});
	}

	@Override
	public List<ImportMetaDataStatus> checkImportStatus(final int tenantId, final String tagOid, final List<String> pathList) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<List<ImportMetaDataStatus>>() {

			@Override
			public List<ImportMetaDataStatus> call() {

				MetaDataPortingLogic logic = MetaDataPortingLogic.getInstance();
				return logic.checkImportStatus(tagOid, pathList);

			}
		});
	}

	@Override
	public Tenant getImportTenant(final int tenantId, final String tagOid) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<Tenant>() {

			@Override
			public Tenant call() {

				MetaDataPortingLogic logic = MetaDataPortingLogic.getInstance();
				return logic.getImportTenant(tagOid);
			}
		});
	}

	@Override
	public MetaDataImportResultInfo importMetaData(final int tenantId, final String tagOid, final List<String> pathList, final Tenant importTenant) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<MetaDataImportResultInfo>() {

			@Override
			public MetaDataImportResultInfo call() {

				MetaDataPortingLogic logic = MetaDataPortingLogic.getInstance();
				return logic.importMetaData(tagOid, pathList, importTenant);
			}
		});
	}

	@Override
	public void removeImportFile(final int tenantId, final String tagOid) {
		AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<Void>() {

			@Override
			public Void call() {

				MetaDataPortingLogic logic = MetaDataPortingLogic.getInstance();
				logic.removeImportFile(tagOid);
				return null;
			}
		});
	}

	@Override
	public void createTag(final int tenantId, final String name, final String description) {
		AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<Void>() {

			@Override
			public Void call() {
				MetaDataPortingLogic logic = MetaDataPortingLogic.getInstance();
				logic.createTag(tenantId, name, description);
				return null;
			}

		});
	}

	@Override
	public List<Entity> getTagList(final int tenantId) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<List<Entity>>() {

			@Override
			public List<Entity> call() {
				MetaDataPortingLogic logic = MetaDataPortingLogic.getInstance();
				return logic.getTagList();
			}

		});
	}

	@Override
	public void removeTag(final int tenantId, final List<String> tagList) {
		AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<Void>() {

			@Override
			public Void call() {
				MetaDataPortingLogic logic = MetaDataPortingLogic.getInstance();
				logic.removeTag(tagList);
				return null;
			}

		});
	}

	@Override
	public DeleteResultInfo deleteMetaData(final int tenantId, final List<String> pathList) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<DeleteResultInfo>() {

			@Override
			public DeleteResultInfo call() {
				DeleteResultInfo result = new DeleteResultInfo();

				String curPath = null;
				try {
					List<MetaDataEntry> entries = new ArrayList<MetaDataEntry>();
					for (String path : pathList) {
						curPath = path;
						List<String> entryPaths = null;
						if (path.endsWith("*")) {
							//ContextPathの全体選択の場合
							String contextPath = path.substring(0, path.length() - 1);

							List<MetaDataEntryInfo> nodes = MetaDataContext.getContext().definitionList(contextPath);
							entryPaths = new ArrayList<String>();
							for (MetaDataEntryInfo node : nodes) {
								entryPaths.add(node.getPath());
							}
						} else {
							//Entryの選択の場合
							String entryPath = path;
							entryPaths = new ArrayList<String>(1);
							entryPaths.add(entryPath);
						}

						for (String entryPath : entryPaths) {
							//MetaDataEntryの取得
							MetaDataEntry entry = MetaDataContext.getContext().getMetaDataEntry(entryPath);
							if (entry == null) {

								logger.error(resourceString("deleteMetaDataErr", tenantId));

								result.addErrorMessage(resourceString("canNotGetMetaDataDefInfo", entryPath));
								result.addErrorCount();
								continue;
							}
							if (org.iplass.mtp.impl.metadata.MetaDataEntry.RepositoryType.SHARED.equals(entry.getRepositryType())) {

								result.addWarnMessage(resourceString("deleteMetaDataErr", entryPath));
								result.addWarnCount();
								continue;
							}
							if (entry.getMetaData() instanceof MetaTenant) {

								result.addWarnMessage(resourceString("skipTenantMetaData", entryPath));
								result.addWarnCount();
								continue;
							}
							entries.add(entry);
						}
					}
					curPath = null;

					//削除
					if (!entries.isEmpty()) {
						doDeleteMetaData(entries, result);
					}

				} catch (Exception e) {
					TransactionUtil.setRollback();

					logger.error("meta data delete failed.", e);

					result.clearMessages();
					result.addErrorMessage(resourceString("deleteMetaDataProcErr", e.getMessage()));
					if (curPath != null) {
						result.addErrorMessage(resourceString("targetPath", curPath));
					}
				}

				return result;
			}

		});
	}


	/**
	 * MetaDataEntryを削除します。
	 *
	 * @param entries 削除対象が定義されたMetaDataのList
	 */
	private void doDeleteMetaData(final List<MetaDataEntry> entries, final DeleteResultInfo result) {

		Transaction.requiresNew(t -> {

			String curPath = null;
			try {
				for (MetaDataEntry entry : entries) {
					curPath = entry.getPath();
					removeMetaDataEntry(entry, result);
				}
			} catch (Exception e) {
				TransactionUtil.setRollback();

				logger.error("meta data remove failed. target path=" + curPath, e);

				List<Message> msgStack = result.getMessages();
				result.clearMessages();
				result.addErrorMessage(resourceString("deleteMetaDataProcErr", e.getMessage()));
				if (curPath != null) {
					result.addErrorMessage(resourceString("targetPath", curPath));
				}

				if (msgStack != null) {
					result.addErrorMessage(resourceString("logTitle"));
					result.addErrorMessage("-----------------------------------------");
					for (Message message : msgStack) {
						result.addErrorMessage(message.getMessage());
					}
					result.addErrorMessage("-----------------------------------------");
				}
			}
		});

		//★★★暫定対応 全キャッシュクリア
		MetaDataContext.getContext().clearAllCache();
		MetaDataContext.getContext().traceCache();
	}

	private void removeMetaDataEntry(MetaDataEntry entry, DeleteResultInfo result) {

		logger.debug("start meta data remove. path=" + entry.getPath());

		MetaDataAuditLogger.getLogger().logMetadata(MetaDataAction.DELETE, "*", "path:" + entry.getPath());

		String path = entry.getPath();
		RootMetaData removeMeta = entry.getMetaData();

		if (removeMeta instanceof MetaEntity) {
			removeMetaEntity(path, (MetaEntity)removeMeta);
		} else {
			MetaDataContext.getContext().remove(path);
		}

		logger.debug("meta data removed. path=" + entry.getPath());

		result.addInfoMessage(resourceString("removeMeta", entry.getPath()));
	}

	private void removeMetaEntity(String path, MetaEntity removeEntity) {
		try {
			Future<String> result = ehService.removeDataModelSchema(removeEntity);
			result.get();	//Thread block
		} catch (ExecutionException e) {
			throw new ApplicationException("exception occured during entity definition remove:" + e.getCause().getMessage(), e);
		} catch (InterruptedException e) {
			throw new ApplicationException("execution interrrupted during entity definition remove:" + e.getMessage(), e);
		}
	}

	private static String resourceString(String suffix, Object... arguments) {
		return AdminResourceBundleUtil.resourceString("tools.metaexplorer.MetaDataExplorerServiceImpl." + suffix, arguments);
	}

}
