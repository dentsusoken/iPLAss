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

package org.iplass.adminconsole.server.metadata.rpc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.iplass.adminconsole.server.base.i18n.AdminResourceBundleUtil;
import org.iplass.adminconsole.server.base.rpc.util.AuthUtil;
import org.iplass.adminconsole.server.base.service.AdminEntityManager;
import org.iplass.adminconsole.server.base.service.auditlog.MetaDataAction;
import org.iplass.adminconsole.server.base.service.auditlog.MetaDataAuditLogger;
import org.iplass.adminconsole.server.base.service.screen.ScreenModuleBasedClassFactoryHolder;
import org.iplass.adminconsole.shared.base.dto.KeyValue;
import org.iplass.adminconsole.shared.base.dto.i18n.I18nMetaDisplayInfo;
import org.iplass.adminconsole.shared.metadata.dto.AdminDefinitionModifyResult;
import org.iplass.adminconsole.shared.metadata.dto.MetaDataInfo;
import org.iplass.adminconsole.shared.metadata.dto.MetaTreeNode;
import org.iplass.adminconsole.shared.metadata.dto.MetaVersionCheckException;
import org.iplass.adminconsole.shared.metadata.dto.Name;
import org.iplass.adminconsole.shared.metadata.dto.entity.EntitySchemaLockedException;
import org.iplass.adminconsole.shared.metadata.dto.entity.SortInfo;
import org.iplass.adminconsole.shared.metadata.dto.menu.MenuItemHolder;
import org.iplass.adminconsole.shared.metadata.dto.staticresource.FileType;
import org.iplass.adminconsole.shared.metadata.dto.staticresource.LocalizedStaticResourceInfo;
import org.iplass.adminconsole.shared.metadata.dto.staticresource.StaticResourceInfo;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataService;
import org.iplass.adminconsole.shared.tools.dto.queueexplorer.CancelResult;
import org.iplass.adminconsole.shared.tools.dto.queueexplorer.TaskCancelResultInfo;
import org.iplass.adminconsole.shared.tools.dto.queueexplorer.TaskForceDeleteResultInfo;
import org.iplass.adminconsole.shared.tools.dto.queueexplorer.TaskLoadResultInfo;
import org.iplass.adminconsole.shared.tools.dto.queueexplorer.TaskSearchResultInfo;
import org.iplass.gem.GemConfigService;
import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.async.AsyncTaskFuture;
import org.iplass.mtp.async.AsyncTaskInfo;
import org.iplass.mtp.async.AsyncTaskInfoSearchCondtion;
import org.iplass.mtp.async.AsyncTaskManager;
import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.auth.login.IdPasswordCredential;
import org.iplass.mtp.auth.oidc.definition.OpenIdConnectDefinitionManager;
import org.iplass.mtp.definition.Definition;
import org.iplass.mtp.definition.DefinitionEntry;
import org.iplass.mtp.definition.DefinitionInfo;
import org.iplass.mtp.definition.DefinitionManager;
import org.iplass.mtp.definition.DefinitionModifyResult;
import org.iplass.mtp.definition.DefinitionSummary;
import org.iplass.mtp.definition.SharedConfig;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.definition.binary.ArchiveBinaryDefinition;
import org.iplass.mtp.definition.binary.BinaryDefinition;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.SearchResult;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.definition.EntityDefinitionModifyResult;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.entity.query.OrderBy;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.QueryException;
import org.iplass.mtp.entity.query.SortSpec;
import org.iplass.mtp.entity.query.SortSpec.SortType;
import org.iplass.mtp.impl.async.rdb.RdbQueueService;
import org.iplass.mtp.impl.auth.AuthService;
import org.iplass.mtp.impl.auth.authenticate.AuthenticationProvider;
import org.iplass.mtp.impl.auth.oauth.MetaOAuthClient.OAuthClientRuntime;
import org.iplass.mtp.impl.auth.oauth.MetaOAuthResourceServer.OAuthResourceServerRuntime;
import org.iplass.mtp.impl.auth.oauth.OAuthClientService;
import org.iplass.mtp.impl.auth.oauth.OAuthResourceServerService;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.definition.DefinitionManagerImpl;
import org.iplass.mtp.impl.definition.DefinitionPath;
import org.iplass.mtp.impl.definition.DefinitionService;
import org.iplass.mtp.impl.i18n.I18nService;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.impl.metadata.MetaDataEntry.RepositoryType;
import org.iplass.mtp.impl.metadata.MetaDataIllegalStateException;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.query.OrderBySyntax;
import org.iplass.mtp.impl.query.QueryServiceHolder;
import org.iplass.mtp.impl.report.ReportingEngineService;
import org.iplass.mtp.impl.report.ReportingType;
import org.iplass.mtp.impl.web.actionmapping.cache.ContentCacheContext;
import org.iplass.mtp.impl.webhook.endpoint.WebhookEndpointService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.BulkFormView;
import org.iplass.mtp.view.generic.DetailFormView;
import org.iplass.mtp.view.generic.EntityView;
import org.iplass.mtp.view.generic.EntityViewManager;
import org.iplass.mtp.view.generic.SearchFormView;
import org.iplass.mtp.view.menu.ActionMenuItem;
import org.iplass.mtp.view.menu.EntityMenuItem;
import org.iplass.mtp.view.menu.MenuItem;
import org.iplass.mtp.view.menu.MenuItemManager;
import org.iplass.mtp.web.actionmapping.definition.ActionMappingDefinitionManager;
import org.iplass.mtp.web.staticresource.definition.LocalizedStaticResourceDefinition;
import org.iplass.mtp.web.staticresource.definition.StaticResourceDefinition;
import org.iplass.mtp.web.staticresource.definition.StaticResourceDefinitionManager;
import org.iplass.mtp.web.template.definition.BinaryTemplateDefinition;
import org.iplass.mtp.web.template.definition.LocalizedBinaryDefinition;
import org.iplass.mtp.web.template.definition.TemplateDefinition;
import org.iplass.mtp.web.template.definition.TemplateDefinitionManager;
import org.iplass.mtp.web.template.report.definition.LocalizedReportDefinition;
import org.iplass.mtp.web.template.report.definition.OutputFileType;
import org.iplass.mtp.web.template.report.definition.ReportTemplateDefinition;
import org.iplass.mtp.webapi.definition.EntityWebApiDefinition;
import org.iplass.mtp.webapi.definition.EntityWebApiDefinitionManager;
import org.iplass.mtp.webapi.definition.WebApiDefinition;
import org.iplass.mtp.webhook.endpoint.definition.WebhookEndpointDefinition;
import org.iplass.mtp.webhook.endpoint.definition.WebhookEndpointDefinitionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.server.rpc.jakarta.XsrfProtectedServiceServlet;

public class MetaDataServiceImpl extends XsrfProtectedServiceServlet implements MetaDataService {

	private static final long serialVersionUID = 460714524971929078L;

	private static final Logger logger = LoggerFactory.getLogger(MetaDataServiceImpl.class);

	private EntityDefinitionManager edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
	private TemplateDefinitionManager tdm = ManagerLocator.getInstance().getManager(TemplateDefinitionManager.class);
	private ActionMappingDefinitionManager amm = ManagerLocator.getInstance().getManager(ActionMappingDefinitionManager.class);
	private StaticResourceDefinitionManager srdm = ManagerLocator.getInstance().getManager(StaticResourceDefinitionManager.class);
	private EntityWebApiDefinitionManager ewdm = ManagerLocator.getInstance().getManager(EntityWebApiDefinitionManager.class);
	private DefinitionManager dm = ManagerLocator.getInstance().getManager(DefinitionManager.class);
	private WebhookEndpointDefinitionManager wepdm = ManagerLocator.getInstance().getManager(WebhookEndpointDefinitionManager.class);
	private WebhookEndpointService wheps = ServiceRegistry.getRegistry().getService(WebhookEndpointService.class);

	private EntityManager em = AdminEntityManager.getInstance();
	private AsyncTaskManager atm = ManagerLocator.getInstance().getManager(AsyncTaskManager.class);

	private AuthService as = ServiceRegistry.getRegistry().getService(AuthService.class);
	private MetaDataAuditLogger auditLogger = MetaDataAuditLogger.getLogger();

	private DefinitionService ds = ServiceRegistry.getRegistry().getService(DefinitionService.class);
	private RdbQueueService rqs = ServiceRegistry.getRegistry().getService(RdbQueueService.class);
	private GemConfigService gcs = ServiceRegistry.getRegistry().getService(GemConfigService.class);

	private OAuthClientService oacs = ServiceRegistry.getRegistry().getService(OAuthClientService.class);
	private OAuthResourceServerService oars = ServiceRegistry.getRegistry().getService(OAuthResourceServerService.class);
	
	private OpenIdConnectDefinitionManager oicdm = ManagerLocator.getInstance().getManager(OpenIdConnectDefinitionManager.class);


	/* ---------------------------------------
	 * 共通
	 --------------------------------------- */

	@Override
	public List<String> getAllMetaDataPath(final int tenantId) {

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<List<String>>() {

			@Override
			public List<String> call() {
				return MetaDataContext.getContext().pathList("/");
			}

		});
	}

	@Override
	public <D extends Definition> MetaTreeNode getMetaDataTree(int tenantId, String className) {

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<MetaTreeNode>() {
			@Override
			public MetaTreeNode call() {
				Class<D> type = getDefinitionClass(className);
				return new MetaDataTreeBuilder().type(type).build();
			}
		});
	}

	@Override
	public <D extends Definition> MetaDataInfo getMetaDataInfo(int tenantId, final String className, final String name) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<MetaDataInfo>() {

			@Override
			public MetaDataInfo call() {
				Class<D> type = getDefinitionClass(className);
				MetaDataEntry entry = MetaDataContext.getContext().getMetaDataEntry(ds.getPath(type, name));
				if (entry != null) {
					MetaDataInfo info = convert(entry, className);
					return info;
				}

				return null;
			}
		});
	}

	@Override
	public <D extends Definition> MetaDataInfo getMetaDataInfo(int tenantId, final String path) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<MetaDataInfo>() {

			@Override
			public MetaDataInfo call() {
				MetaDataEntry entry = MetaDataContext.getContext().getMetaDataEntry(path);
				if (entry != null) {
					MetaDataInfo info = convert(entry, null);
					return info;
				}

				return null;
			}
		});
	}

	private MetaDataInfo convert(MetaDataEntry entry, String className) {
		RootMetaData meta = entry.getMetaData();
		MetaDataInfo info = new MetaDataInfo();
		info.setPath(entry.getPath());
		info.setName(ds.getDefinitionName(entry.getPath()));
		info.setId(meta.getId());
		info.setSharable(entry.isSharable());
		info.setOverwritable(entry.isOverwritable());
		info.setDataSharable(entry.isDataSharable());
		info.setPermissionSharable(entry.isPermissionSharable());
		info.setShared(RepositoryType.SHARED == entry.getRepositryType());
		info.setSharedOverwrite(RepositoryType.SHARED_OVERWRITE == entry.getRepositryType());
		if (className != null) {
			info.setDefinitionClassName(className);
		} else {
			//パスから取得
			DefinitionPath defPath = ds.resolvePath(entry.getPath());
			if (defPath != null && defPath.getType() != null) {
				info.setDefinitionClassName(defPath.getType().getName());
			}
		}

		return info;
	}

	@Override
	public <D extends Definition> I18nMetaDisplayInfo getMetaDisplayInfo(int tenantId, final String className, final String name) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<I18nMetaDisplayInfo>() {

			@Override
			public I18nMetaDisplayInfo call() {
				Class<D> type = getDefinitionClass(className);
				MetaDataEntry entry = MetaDataContext.getContext().getMetaDataEntry(ds.getPath(type, name));
				if (entry != null) {
					RootMetaData meta = entry.getMetaData();

					I18nMetaDisplayInfo ret = new I18nMetaDisplayInfo();
					ret.setI18nDisplayName(I18nUtil.stringMeta(meta.getDisplayName(), meta.getLocalizedDisplayNameList()));
					//TODO 多言語化？
					ret.setI18nDescription(meta.getDescription());

					return ret;
				}

				return null;
			}
		});
	}

	@Override
	public I18nMetaDisplayInfo getMetaDisplayInfo(int tenantId, final String path) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<I18nMetaDisplayInfo>() {

			@Override
			public I18nMetaDisplayInfo call() {

				MetaDataEntry entry = MetaDataContext.getContext().getMetaDataEntry(path);
				if (entry != null) {
					RootMetaData meta = entry.getMetaData();

					I18nMetaDisplayInfo ret = new I18nMetaDisplayInfo();
					ret.setI18nDisplayName(I18nUtil.stringMeta(meta.getDisplayName(), meta.getLocalizedDisplayNameList()));
					//TODO 多言語化？
					ret.setI18nDescription(meta.getDescription());

					return ret;
				}

				return null;
			}
		});
	}

	@Override
	public <D extends Definition> String checkStatus(int tenantId, String className, String name) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<String>() {

			@Override
			public String call() {

				Class<D> type = getDefinitionClass(className);
				String path = ds.getPath(type, name);
				try {
					MetaDataContext.getContext().checkState(path);

					return null;
				} catch (MetaDataIllegalStateException e) {
					logger.error("meta data status check error. target=" + path + ", " + e.getMessage(), e);
					return getMetaDataIllegalStateMessage(e);
				}
			}

		});
	}

	@Override
	public LinkedHashMap<String, String> checkStatus(int tenantId, final List<String> paths) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<LinkedHashMap<String, String>>() {

			@Override
			public LinkedHashMap<String, String> call() {
				LinkedHashMap<String, String> ret = new LinkedHashMap<String, String>();
				for (String path : paths) {

					try {
						MetaDataContext.getContext().checkState(path);
					} catch (MetaDataIllegalStateException e) {
						logger.error("meta data status check error. target=" + path + ", " + e.getMessage(), e);
//						Map<String, String> entry = new HashMap<String, String>(1);
//						entry.put(path, getMetaDataIllegalStateMessage(e));
						ret.put(path, getMetaDataIllegalStateMessage(e));
					}
				}
				return ret;
			}
		});
	}

	private String getMetaDataIllegalStateMessage(MetaDataIllegalStateException e) {
		if (e.getMessage() != null) {
			return e.getMessage();
		} else if (e.getCause() != null) {
			Throwable cause = e.getCause();
			if (cause.getMessage() != null) {
				return cause.getMessage();
			} else {
				return cause.getClass().getName();
			}
		} else {
			return e.getClass().getName();
		}
	}

	@Override
	public void clearAllCache(final int tenantId) {
		AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<Void>() {

			@Override
			public Void call() {
				auditLogger.logTenant("reload tenant context", null);

				ServiceRegistry.getRegistry().getService(TenantContextService.class).reloadTenantContext(tenantId, false);
//				MetaDataContext.getContext().clearAllCache();
				return null;
			}
		});
	}

	@Override
	public void clearActionCache(int tenantId, String actionName) {
		AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<Void>() {

			@Override
			public Void call() {
				auditLogger.logTenant("clear content cache of action", "tenantId:" + tenantId + ", actionName:" + actionName);

				TenantContext tc = ServiceRegistry.getRegistry().getService(TenantContextService.class).getTenantContext(tenantId);
				ContentCacheContext ac = tc.getResource(ContentCacheContext.class);
				ac.invalidateByActionName(actionName);
				return null;
			}
		});
	}

	@Override
	public void clearTenantActionCache(int tenantId) {
		AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<Void>() {

			@Override
			public Void call() {
				auditLogger.logTenant("clear content cache of all tenant action", "tenantId:" + tenantId);

				TenantContext tc = ServiceRegistry.getRegistry().getService(TenantContextService.class).getTenantContext(tenantId);
				ContentCacheContext ac = tc.getResource(ContentCacheContext.class);
				ac.invalidateAllEntry();
				return null;
			}
		});
	}

	@Override
	public void updateSharedConfig(int tenantId, final String className, final String name, final SharedConfig config) {
		AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<Void>() {
			@Override
			public Void call() {
				Class<? extends Definition> type = getDefinitionClass(className);
				auditLogger.logMetadata(MetaDataAction.UPDATE, className, "name:" + name + " config:" + config.toString());
				dm.setSharedConfig(type, name, config);
				return null;
			}
		});
	}

	@Override
	public AdminDefinitionModifyResult renameDefinition(int tenantId, final String className, final String fromName, final String toName) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<AdminDefinitionModifyResult>() {
			@Override
			public AdminDefinitionModifyResult call() {
				try {
					Class<? extends Definition> type = getDefinitionClass(className);
					auditLogger.logMetadata(MetaDataAction.UPDATE, className, "fromName:" + fromName + " toName:" + toName);
					dm.rename(type, fromName, toName);

					if (className.equals(EntityDefinition.class.getName())) {
						//Entityの場合はEntityView、EntityFilter、EntityWebAPIも更新
						ScreenModuleBasedClassFactoryHolder.getFactory().getEntityOperationController().renameViewDefinition(fromName, toName);
					}

				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					return new AdminDefinitionModifyResult(false, e.getMessage());
				}
				return new AdminDefinitionModifyResult(true);
			}
		});
	}

	@Override
	public DefinitionInfo getDefinitionInfo(int tenantId, final String className, final String name) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<DefinitionInfo>() {
			@Override
			public DefinitionInfo call() {
				Class<? extends Definition> type = getDefinitionClass(className);
				return dm.getInfo(type, name);
			}

		});

	}

	@Override
	public Map<String, String> getEnableLanguages(int tenantId) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<Map<String, String>>() {

			@Override
			public Map<String, String> call() {
				I18nService i18n = ServiceRegistry.getRegistry().getService(I18nService.class);
				return i18n.getEnableLanguagesMap();
			}
		});
	}

	@Override
	public DefinitionInfo getHistoryById(int tenantId, final  String className, final String definitionId) {

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<DefinitionInfo>() {

			@Override
			public DefinitionInfo call() {
				Class<? extends Definition> type = getDefinitionClass(className);

				//FIXME DefinitionManagerからは、defIdでの操作は行わない
				return ((DefinitionManagerImpl) dm).getHistoryById(type, definitionId);
			}

		});
	}

	/**
	 * <p>Pathを有効な値に変換します。</p>
	 *
	 * Entityのnameは「.」区切りなので、nameをそのまま渡された場合などを考慮して変換
	 *
	 * @param path パス
	 * @return 有効なパス
	 */
	private String convertPath(String path) {
		return path.replace(".","/");
	}

	@Override
	public List<Name> getDefinitionNameList(int tenantId, String className) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<List<Name>>() {
			@Override
			public List<Name> call() {
				Class<? extends Definition> type = getDefinitionClass(className);

				TypedDefinitionManager<?> manager = ds.getTypedDefinitionManager(type);
				List<DefinitionSummary> defList = manager.definitionSummaryList();

				List<Name> res = new ArrayList<Name>(defList.size());
				for (DefinitionSummary def : defList) {
					res.add(new Name(def.getName(), def.getDisplayName()));
				}
				return res;
			}
		});
	}

	@Override
	public DefinitionEntry getDefinitionEntry(int tenantId, String className, String name) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId,
				new AuthUtil.Callable<DefinitionEntry>() {

					@Override
					public DefinitionEntry call() {
						Class<? extends Definition> type = getDefinitionClass(className);
						return dm.getDefinitionEntry(type, name);
					}
				});
	}

	@Override
	public <D extends Definition> D getDefinition(int tenantId, String className, String name) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId,
				new AuthUtil.Callable<D>() {

					@Override
					public D call() {
						Class<D> type = getDefinitionClass(className);
						TypedDefinitionManager<D> manager = ds.getTypedDefinitionManager(type);
						return manager.get(name);
					}
				});
	}

	@Override
	public <D extends Definition> AdminDefinitionModifyResult createDefinition(int tenantId, D definition) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId,
				new AuthUtil.Callable<AdminDefinitionModifyResult>() {

					@SuppressWarnings("unchecked")
					@Override
					public AdminDefinitionModifyResult call() {

						auditLogger.logMetadata(MetaDataAction.CREATE, definition.getClass().getName(), "name:" + definition.getName());
						TypedDefinitionManager<D> manager = (TypedDefinitionManager<D>) ds.getTypedDefinitionManager(definition.getClass());
						DefinitionModifyResult ret = manager.create(definition);
						return new AdminDefinitionModifyResult(ret.isSuccess(), ret.getMessage());
					}
				});
	}

	@Override
	public <D extends Definition> AdminDefinitionModifyResult updateDefinition(int tenantId, D definition, int currentVersion, boolean checkVersion) throws MetaVersionCheckException {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId,
				new AuthUtil.Callable<AdminDefinitionModifyResult>() {

					@SuppressWarnings("unchecked")
					@Override
					public AdminDefinitionModifyResult call() {

						// バージョンの最新チェック
						MetaDataVersionCheckUtil.versionCheck(checkVersion, definition.getClass(), definition.getName(), currentVersion);

						auditLogger.logMetadata(MetaDataAction.UPDATE, definition.getClass().getName(), "name:" + definition.getName());
						TypedDefinitionManager<D> manager = (TypedDefinitionManager<D>) ds.getTypedDefinitionManager(definition.getClass());
						DefinitionModifyResult ret = manager.update(definition);
						return new AdminDefinitionModifyResult(ret.isSuccess(), ret.getMessage());
					}
				});
	}

	@Override
	public <D extends Definition> AdminDefinitionModifyResult deleteDefinition(int tenantId, String className, String name) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId,
				new AuthUtil.Callable<AdminDefinitionModifyResult>() {

					@Override
					public AdminDefinitionModifyResult call() {
						Class<D> type = getDefinitionClass(className);
						auditLogger.logMetadata(MetaDataAction.DELETE, type.getName(), "name:" + name);
						TypedDefinitionManager<D> manager = ds.getTypedDefinitionManager(type);
						DefinitionModifyResult ret = manager.remove(name);
						return new AdminDefinitionModifyResult(ret.isSuccess(), ret.getMessage());
					}
				});
	}

	@Override
	public <D extends Definition> AdminDefinitionModifyResult copyDefinition(int tenantId, String className, String sourceName,
			String newName, String displayName, String description) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId,
				new AuthUtil.Callable<AdminDefinitionModifyResult>() {

					@Override
					public AdminDefinitionModifyResult call() {
						Class<D> type = getDefinitionClass(className);
						TypedDefinitionManager<D> manager = ds.getTypedDefinitionManager(type);

						D source = manager.get(sourceName);
						source.setName(newName);
						source.setDisplayName(displayName);
						source.setDescription(description);

						auditLogger.logMetadata(MetaDataAction.CREATE, type.getName(), "name:" + source.getName());
						DefinitionModifyResult ret = manager.create(source);
						return new AdminDefinitionModifyResult(ret.isSuccess(), ret.getMessage());
					}
				});
	}

	@SuppressWarnings("unchecked")
	private <D extends Definition> Class<D> getDefinitionClass(String className) {
		try {
			return (Class<D>) Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(resourceString("canNotFoundDataType", className), e);
		} catch (ClassCastException e) {
			throw new RuntimeException(resourceString("notDataTypeSupported", className), e);
		}

	}

	// 以下、共通化できないロジック、Manager側で個別に実装してるケース

	/* ---------------------------------------
	 * Entity
	 --------------------------------------- */

	@Override
	public List<Name> getEntityDefinitionNameList(int tenantId) {

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<List<Name>>() {
			@Override
			public List<Name> call() {
				List<DefinitionSummary> defList = edm.definitionNameList();

				List<Name> res = new ArrayList<Name>(defList.size());
				for (DefinitionSummary def : defList) {
					res.add(new Name(def.getName(), def.getDisplayName()));
				}
				return res;
			}
		});

	}

	@Override
	public EntityDefinition getEntityDefinition(int tenantId, final String name) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<EntityDefinition>() {
			@Override
			public EntityDefinition call() {
				return edm.get(name);
			}
		});
	}

	@Override
	public DefinitionEntry getEntityDefinitionEntry(int tenantId, final String name) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<DefinitionEntry>() {
			@Override
			public DefinitionEntry call() {
				return dm.getDefinitionEntry(EntityDefinition.class, name);
			}
		});
	}

	@Override
	public AdminDefinitionModifyResult createEntityDefinition(int tenantId, final EntityDefinition definition) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<AdminDefinitionModifyResult>() {
			@Override
			public AdminDefinitionModifyResult call() {
				try {
					//作成
					AdminDefinitionModifyResult ret = createEntity(definition);

					return ret;
				} catch (EntityRuntimeException e) {
					logger.error(e.getMessage(), e);
					return new AdminDefinitionModifyResult(false, e.getMessage());
				}
			}
		});
	}

	@Override
	public AdminDefinitionModifyResult updateEntityDefinition(int tenantId, final EntityDefinition definition, final int currentVersion, final boolean checkVersion) {
		return updateEntityDefinition(tenantId, definition, currentVersion, null, checkVersion);
	}

	@Override
	public AdminDefinitionModifyResult updateEntityDefinition(final int tenantId, final EntityDefinition definition, final int currentVersion, final Map<String, String> renamePropertyMap, final boolean checkVersion) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<AdminDefinitionModifyResult>() {
			@Override
			public AdminDefinitionModifyResult call() {
				try {

					// バージョンの最新チェック
					MetaDataVersionCheckUtil.versionCheck(checkVersion, definition.getClass(), definition.getName(), currentVersion);

					//ロックチェック
					boolean locked = doCheckLockEntityDefinition(tenantId, definition.getName());
					if (locked) {
						throw new EntitySchemaLockedException("entity definition schema is locked. name=" + definition.getName());
					}

					// メニューItemの更新
					AdminDefinitionModifyResult ret = ScreenModuleBasedClassFactoryHolder.getFactory().getEntityOperationController().updateMenuItem(definition);
					if (ret != null) {
						return ret;
					}

					//非同期で実行するため、結果を確認しないで正常を返す
					//EntityDefinitionModifyResult ret2 = edm.update(definition, renamePropertyMap);
					//return new AdminDefinitionModifyResult(ret2.isSuccess(), ret2.getMessage());
					auditLogger.logMetadata(MetaDataAction.UPDATE, EntityDefinition.class.getName(), "name:" + definition.getName());

					edm.update(definition, renamePropertyMap);
					return new AdminDefinitionModifyResult(true, null);

				} catch (EntityRuntimeException e) {
					logger.error(e.getMessage(), e);
					return new AdminDefinitionModifyResult(false, e.getMessage());
				}
			}
		});
	}

	@Override
	public boolean checkLockEntityDefinition(final int tenantId, final String defName) {

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<Boolean>() {

			@Override
			public Boolean call() {
				return doCheckLockEntityDefinition(tenantId, defName);
			}
		});
	}

	private boolean doCheckLockEntityDefinition(final int tenantId, final String defName) {
		return edm.isLockedSchema(defName);
	}

	@Override
	public AdminDefinitionModifyResult deleteEntityDefinition(int tenantId, final String name) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<AdminDefinitionModifyResult>() {
			@Override
			public AdminDefinitionModifyResult call() {
				try {

					//対象のEntity定義がSharedOverwriteの場合は削除しても、Sharedとして残るので関連メタデータは削除しない
					DefinitionInfo entity = dm.getInfo(EntityDefinition.class, name);
					if (!entity.isSharedOverwrite()) {
						AdminDefinitionModifyResult ret = ScreenModuleBasedClassFactoryHolder.getFactory().getEntityOperationController().deleteViewDefinition(name);
						if (ret != null) {
							return ret;
						}
					}

					//Entityの削除
					auditLogger.logMetadata(MetaDataAction.DELETE, EntityDefinition.class.getName(), "name:" + name);
					EntityDefinitionModifyResult ret = edm.remove(name);

					//serialize errorになるため、EntityDefinitionModifyResult -> AdminDefinitionModifyResult
					//return ret3;
					return new AdminDefinitionModifyResult(ret.isSuccess(), ret.getMessage());
				} catch (EntityRuntimeException e) {
					logger.error(e.getMessage(), e);
					return new AdminDefinitionModifyResult(false, e.getMessage());
				}
			}
		});
	}

	@Override
	public AdminDefinitionModifyResult copyEntityDefinition(int tenantId, final String sourceName,
			final String newName, final String displayName, final String description,
			final boolean isCopyEntityView, final boolean isCopyEntityFilter, final boolean isCopyEntityWebAPI) {

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<AdminDefinitionModifyResult>() {
			@Override
			public AdminDefinitionModifyResult call() {
				try {
					//コピー元の取得
					EntityDefinition ed = edm.get(sourceName);

					//値の設定
					ed.setName(newName);
					ed.setDisplayName(displayName);
					ed.setDescription(description);

					//作成
					AdminDefinitionModifyResult ret = createEntity(ed);

					if (ret.isSuccess()) {
						AdminDefinitionModifyResult ret2 = ScreenModuleBasedClassFactoryHolder.getFactory().getEntityOperationController()
								.copyViewDefinition(sourceName, newName, displayName, description, isCopyEntityView, isCopyEntityFilter, isCopyEntityWebAPI);
						if (ret2 != null) {
							return ret2;
						}
					}

					return ret;
				} catch (EntityRuntimeException e) {
					logger.error(e.getMessage(), e);
					return new AdminDefinitionModifyResult(false, e.getMessage());
				}
			}
		});
	}

	/**
	 * Entityの作成処理（CreateとCopyで利用）
	 *
	 * MenuItemの生成、DefaultMenuTreeへの追加も行う。
	 *
	 * @param definition EntityDefinition
	 * @return
	 */
	private AdminDefinitionModifyResult createEntity(EntityDefinition definition) {
		//作成
		auditLogger.logMetadata(MetaDataAction.CREATE, EntityDefinition.class.getName(), "name:" + definition.getName());
		EntityDefinitionModifyResult ret = edm.create(definition);

		if(ret.isSuccess()) {
			AdminDefinitionModifyResult ret2 = ScreenModuleBasedClassFactoryHolder.getFactory().getEntityOperationController().createMenuItem(definition);
			if (ret2 != null) {
				return ret2;
			}
		}
		return new AdminDefinitionModifyResult(ret.isSuccess(), ret.getMessage());
	}

	@Override
	public List<Name> getPropertyDefinitionNameList(int tenantId, final String name) {

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<List<Name>>() {
			@Override
			public List<Name> call() {
				List<Name> res = new ArrayList<Name>();

				EntityDefinition ed = edm.get(name);
				if (ed != null) {
					for (PropertyDefinition pd : ed.getPropertyList()) {
						res.add(new Name(pd.getName(), I18nUtil.stringDef(pd.getDisplayName(), pd.getLocalizedDisplayNameList())));
					}
				}
				return res;
			}
		});
	}

	@Override
	public PropertyDefinition getPropertyDefinition(int tenantId, final String name, final String propertyName) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<PropertyDefinition>() {

			@Override
			public PropertyDefinition call() {
				EntityDefinition ed = edm.get(name);
				return getProperty(ed, propertyName);
			}

			private PropertyDefinition getProperty(EntityDefinition ed, String propName) {
				int firstDotIndex = propName.indexOf('.');
				if (firstDotIndex > 0) {
					String topPropName = propName.substring(0, firstDotIndex);
					String subPropName = propName.substring(firstDotIndex + 1);
					PropertyDefinition topProperty = ed.getProperty(topPropName);
					if (topProperty instanceof ReferenceProperty) {
						EntityDefinition red = edm.get(((ReferenceProperty) topProperty).getObjectDefinitionName());
						if (red != null) {
							return getProperty(red, subPropName);
						}
					}
				} else {
					return ed.getProperty(propName);
				}
				return null;
			}

		});
	}

	@Override
	public Name getEntityDefinitionName(int tenantId, final String name) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<Name>() {
			@Override
			public Name call() {
				EntityDefinition def = edm.get(name);
				if(def == null) {
					return null;
				}
				return new Name(def.getName(), def.getDisplayName());
			}
		});
	}

	@Override
	public String getPropertyDisplayName(int tenantId, final String name, final String propertyName) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<String>() {

			@Override
			public String call() {
				PropertyDefinition pd = getPropertyDefinition(tenantId, name, propertyName);
				if (pd != null) {
					return pd.getDisplayName();
				}
				return null;
			}
		});
	}

	@Override
	public List<String> getEntityStoreSpaceList(int tenantId) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<List<String>>() {
			@Override
			public List<String> call() {
				return edm.getStorageSpaceList();
			}
		});
	}

	@Override
	public List<KeyValue<String, Long>> getAutoNumberCurrentValueList(int tenantId, final String name, final String propertyName) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<List<KeyValue<String, Long>>>() {
			@Override
			public List<KeyValue<String, Long>> call() {
				return edm.getAutoNumberCurrentValueList(name, propertyName).stream().map(value -> {
					return new KeyValue<String, Long>(value.getKey(), value.getValue());
				}).collect(Collectors.toList());
			}
		});
	}

	@Override
	public void resetAutoNumberCounterList(int tenantId, final String name, final String propertyName, final List<KeyValue<String, Long>> values) {
		AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<Void>() {
			@Override
			public Void call() {
				values.forEach(value -> {
					auditLogger.logMetadata(MetaDataAction.UPDATE, EntityDefinition.class.getName(), "name:" + name + " propertyName:" + propertyName + " subKey:" + value.getKey() + " autoNumber:" + value.getValue());
					//開始値を指定するため、+1する
					edm.resetAutoNumberCounter(name, propertyName, value.getKey(), value.getValue() + 1);
				});
				return null;
			}
		});
	}

	@Override
	public List<SortInfo> getSortInfo(int tenantId, final String orderBy) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<List<SortInfo>>() {
			@Override
			public List<SortInfo> call() {
				OrderBy q;
				try {
					q = QueryServiceHolder.getInstance().getQueryParser().parse("order by " + orderBy, OrderBySyntax.class);
				} catch (ParseException e) {
					throw new QueryException(e.getMessage());
				}

				List<SortInfo> list = new ArrayList<SortInfo>();
				if (q.getSortSpecList() != null && q.getSortSpecList().size() > 0) {
					for (SortSpec s : q.getSortSpecList()) {
						SortInfo i = new SortInfo();
						i.setPropertyName(s.getSortKey().toString());
						if (s.getType() != null) {
							i.setSortType(s.getType().name());
						}
						list.add(i);
					}
				}
				return list;
			}
		});
	}

	@Override
	public List<EntityDefinition> getCrawlTargetEntityList(int tenantId) {

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<List<EntityDefinition>>() {
			@Override
			public List<EntityDefinition> call() {
				List<EntityDefinition> rtnList = new ArrayList<EntityDefinition>();

				for (String defName : edm.definitionList()) {
					EntityDefinition def = edm.get(defName);
					if (def != null && def.isCrawl()) {
						rtnList.add(def);
					}
				}
				return rtnList;
			}

		});
	}

	@Override
	public Map<String, List<String>> getCrawlTargetEntityViewMap(int tenantId) {

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<Map<String, List<String>>>() {
			@Override
			public Map<String, List<String>> call() {
				EntityViewManager evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
				Map<String, List<String>> viewsMap = new HashMap<String, List<String>>();

				for (String defName : edm.definitionList()) {
					EntityDefinition def = edm.get(defName);
					if (def != null && def.isCrawl()) {

						EntityView entityView = evm.get(defName);

						if (entityView == null || entityView.getSearchFormViewNames().length == 0) {
							List<String> viewList = new ArrayList<String>();
							viewList.add("(default)");
							viewsMap.put(defName, viewList);
						} else {
							List<String> viewList = new ArrayList<String>();
							for (String viewName : entityView.getSearchFormViewNames()) {
								if (viewName.isEmpty()) {
									viewList.add("(default)");
								} else {
									viewList.add(viewName);
								}
							}
							viewsMap.put(defName, viewList);
						}
					}
				}

				return viewsMap;
			}

		});
	}

	/* ---------------------------------------
	 * EntityView
	 --------------------------------------- */

	@Override
	public SearchFormView createDefaultSearchFormView(int tenantId, final String name) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<SearchFormView>() {
			@Override
			public SearchFormView call() {
				auditLogger.logMetadata(MetaDataAction.CREATE, SearchFormView.class.getName(), "name:" + name);
				EntityViewManager evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
				return evm.createDefaultSearchFormView(name);
			}
		});
	}

	@Override
	public DetailFormView createDefaultDetailFormView(int tenantId, final String name) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<DetailFormView>() {
			@Override
			public DetailFormView call() {
				auditLogger.logMetadata(MetaDataAction.CREATE, DetailFormView.class.getName(), "name:" + name);
				EntityViewManager evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
				return evm.createDefaultDetailFormView(name);
			}
		});
	}

	@Override
	public BulkFormView createDefaultBulkFormView(int tenantId, final String name) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<BulkFormView>() {
			@Override
			public BulkFormView call() {
				auditLogger.logMetadata(MetaDataAction.CREATE, BulkFormView.class.getName(), "name:" + name);
				EntityViewManager evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
				return evm.createDefaultBulkFormView(name);
			}
		});
	}


	@Override
	public List<Entity> getRoles(int tenantId) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<List<Entity>>() {
			@Override
			public List<Entity> call() {
				Query query = new Query().select(Entity.OID, Entity.NAME, "code").from("mtp.auth.Role").order(new SortSpec(Entity.NAME, SortType.ASC));
				SearchResult<Entity> result = em.searchEntity(query);
				return result.getList();
			}
		});
	}

	/* ---------------------------------------
	 * Menu Item
	 --------------------------------------- */

	@Override
	public MenuItemHolder getMenuItemList(int tenantId) {

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId,
				new AuthUtil.Callable<MenuItemHolder>() {
			@Override
			public MenuItemHolder call() {

				MenuItemManager mm = ManagerLocator.getInstance().getManager(MenuItemManager.class);
				List<String> names = mm.definitionList();

				if (names == null) {
					return null;
				}

				MenuItemHolder holder = new MenuItemHolder();

				//TODO YK ここで一件一件取得しないとだめか
				MenuItem item = null;
				for (String name : names) {
					item = mm.get(name);
					if (item != null) {
						holder.addMenuItem(item);
					}
				}
				return holder;
			}
		});
	}

	@Override
	public AdminDefinitionModifyResult createMenuItem(int tenantId, final MenuItem definition) {

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<AdminDefinitionModifyResult>() {
			@Override
			public AdminDefinitionModifyResult call() {
				//検証
				AdminDefinitionModifyResult validate = validateMenuItem(definition);
				if (validate != null) {
					return validate;
				}

				auditLogger.logMetadata(MetaDataAction.CREATE, MenuItem.class.getName(), "name:" + definition.getName());
				MenuItemManager mm = ManagerLocator.getInstance().getManager(MenuItemManager.class);
				DefinitionModifyResult ret = mm.create(definition);
				return new AdminDefinitionModifyResult(ret.isSuccess(), ret.getMessage());
			}
		});
	}

	@Override
	public AdminDefinitionModifyResult updateMenuItem(int tenantId, final MenuItem definition) {

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<AdminDefinitionModifyResult>() {
			@Override
			public AdminDefinitionModifyResult call() {
				//検証
				AdminDefinitionModifyResult validate = validateMenuItem(definition);
				if (validate != null) {
					return validate;
				}

				auditLogger.logMetadata(MetaDataAction.UPDATE, MenuItem.class.getName(), "name:" + definition.getName());
				MenuItemManager mm = ManagerLocator.getInstance().getManager(MenuItemManager.class);
				DefinitionModifyResult ret = mm.update(definition);
				return new AdminDefinitionModifyResult(ret.isSuccess(), ret.getMessage());
			}
		});
	}

	private AdminDefinitionModifyResult validateMenuItem(final MenuItem definition) {
		if (definition instanceof EntityMenuItem) {
			EntityMenuItem emi = (EntityMenuItem)definition;
			//参照先Entity存在チェック
			if (StringUtil.isEmpty(emi.getEntityDefinitionName())
					|| edm.get(emi.getEntityDefinitionName()) == null) {
				return new AdminDefinitionModifyResult(false, resourceString("notFoundEntity"));
			}
		} else if (definition instanceof ActionMenuItem) {
			ActionMenuItem ami = (ActionMenuItem)definition;
			//参照先Action存在チェック
			if (StringUtil.isEmpty(ami.getActionName())
					|| amm.get(ami.getActionName()) == null) {
				return new AdminDefinitionModifyResult(false, resourceString("notFoundAction"));
			}
		}
		return null;
	}

	/* ---------------------------------------
	 * Template
	 --------------------------------------- */

	@Override
	public DefinitionEntry getTemplateDefinitionEntry(int tenantId, final String name) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<DefinitionEntry>() {
			@Override
			public DefinitionEntry call() {
				DefinitionEntry entry = dm.getDefinitionEntry(TemplateDefinition.class, name);
				if (entry.getDefinition() instanceof BinaryTemplateDefinition) {
					//BinaryTemplateDefinitionのBinaryは編集では不要(設定されているかのみ知りたい)なので返さない
					//(返すとRPCのシリアライズ対象になり、大きいファイルなど無駄)
					BinaryTemplateDefinition btd = (BinaryTemplateDefinition)entry.getDefinition();
					if (btd.getBinary() != null && btd.getBinary().length > 0) {
						btd.setBinary(new byte[1]);
					}
					if (btd.getLocalizedBinaryList() != null) {
						for (LocalizedBinaryDefinition local : btd.getLocalizedBinaryList()) {
							if (local.getBinaryValue() != null && local.getBinaryValue().length > 0) {
								local.setBinaryValue(new byte[1]);
							}
						}
					}
				} else if (entry.getDefinition() instanceof ReportTemplateDefinition) {
					//ReportTemplateDefinitionのBinaryは編集では不要(設定されているかのみ知りたい)なので返さない
					//(返すとRPCのシリアライズ対象になり、大きいファイルなど無駄)
					ReportTemplateDefinition rtd = (ReportTemplateDefinition)entry.getDefinition();
					if (rtd.getBinary() != null && rtd.getBinary().length > 0) {
						rtd.setBinary(new byte[1]);
					}
					if (rtd.getLocalizedReportList() != null) {
						for (LocalizedReportDefinition local : rtd.getLocalizedReportList()) {
							if (local.getBinary() != null && local.getBinary().length > 0) {
								local.setBinary(new byte[1]);
							}
						}
					}
				}
				return entry;
			}
		});
	}

	@Override
	public AdminDefinitionModifyResult updateTemplateDefinition(int tenantId, final TemplateDefinition definition, final int currentVersion, final boolean checkVersion) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<AdminDefinitionModifyResult>() {
			@Override
			public AdminDefinitionModifyResult call() {

				// バージョンの最新チェック
				MetaDataVersionCheckUtil.versionCheck(checkVersion, definition.getClass(), definition.getName(), currentVersion);

				//BinaryTemplateの場合、byteがうまく渡せないので一度検索
				//BinaryTemplateでこのupdateが呼び出されるのはFileがアップロードされない場合
				if (definition instanceof BinaryTemplateDefinition) {
					TemplateDefinition cur = tdm.get(definition.getName());
					//念のためチェック
					if (cur != null && cur instanceof BinaryTemplateDefinition) {
						((BinaryTemplateDefinition)definition).setBinary(
								((BinaryTemplateDefinition)cur).getBinary());
					}
				}else if (definition instanceof ReportTemplateDefinition) {
					TemplateDefinition cur = tdm.get(definition.getName());
					//念のためチェック
					if (cur != null && cur instanceof ReportTemplateDefinition) {
						((ReportTemplateDefinition)definition).setBinary(
								((ReportTemplateDefinition)cur).getBinary());
					}
				}
				auditLogger.logMetadata(MetaDataAction.UPDATE, TemplateDefinition.class.getName(), "name:" + definition.getName());
				DefinitionModifyResult ret = tdm.update(definition);
				return new AdminDefinitionModifyResult(ret.isSuccess(), ret.getMessage());
			}
		});
	}

	@Override
	public List<Name> getReportTemplateDefinitionNameList(int tenantId) {

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<List<Name>>() {
			@Override
			public List<Name> call() {
				List<DefinitionSummary> defList = tdm.definitionSummaryList();

				List<Name> res = new ArrayList<Name>(defList.size());
				for (DefinitionSummary def : defList) {

					if (tdm.get(def.getName()) instanceof ReportTemplateDefinition) {
						res.add(new Name(def.getName(), def.getDisplayName()));
					}
				}
				return res;
			}
		});

	}

	@Override
	public List<org.iplass.mtp.web.template.report.definition.OutputFileType> getOutputFileTypeList(
			int tenantId, final String type) {

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<List<OutputFileType>>() {

			@Override
			public List<OutputFileType> call() {
				// OutputFileTypeのList取得
				List<OutputFileType> outputFileTypeList = ServiceRegistry.getRegistry().getService(ReportingEngineService.class).getOutputFileTypeList(type);
				return outputFileTypeList;
			}

		});
	}

	@Override
	public List<Name> getReportTypeList(int tenantId) {

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<List<Name>>() {

			@Override
			public List<Name> call() {
				List<Name> nameList = new ArrayList<Name>();
				// ReportTypeのList取得
				List<ReportingType> reportTypeList = ServiceRegistry.getRegistry().getService(ReportingEngineService.class).getReportTypeList();
				for (ReportingType type : reportTypeList) {
					Name name = new Name();
					name.setName(type.getName());
					name.setDisplayName(type.getDisplayName());
					nameList.add(name);
				}

				return nameList;
			}

		});
	}

	/* ---------------------------------------
	 * StaticResource
	 --------------------------------------- */

	// TODO 共通処理を呼ぶとエラーになる、transのDefinitionが一部コメント化されてるからか？

	@Override
	public List<Name> getStaticResourceDefinitionNameList(int tenantId) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<List<Name>>() {
			@Override
			public List<Name> call() {
				List<DefinitionSummary> defList = srdm.definitionSummaryList();

				List<Name> res = new ArrayList<Name>(defList.size());
				for (DefinitionSummary def : defList) {
					res.add(new Name(def.getName(), def.getDisplayName()));
				}
				return res;
			}
		});
	}

	@Override
	public DefinitionEntry getStaticResourceDefinitionEntry(int tenantId, String name) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<DefinitionEntry>() {
			@Override
			public DefinitionEntry call() {
				DefinitionEntry entry = dm.getDefinitionEntry(StaticResourceDefinition.class, name);
				StaticResourceDefinition definition = (StaticResourceDefinition) entry.getDefinition();
				StaticResourceInfo info = StaticResourceInfo.valueOf(definition);

				// クライアント側のDefinitionはBinaryDefinitionを持たないのでBinaryDefinitionに関するマッピングはここで行う
				BinaryDefinition resource = definition.getResource();
				if (resource != null) {
					//2.1.34までパスが設定されていたので除去
					String binaryName =  FilenameUtils.getName(resource.getName());
					info.setBinaryName(binaryName);
					info.setStoredBinaryName(binaryName);
					info.setFileType(resource instanceof ArchiveBinaryDefinition ? FileType.ARCHIVE : FileType.SIMPLE);
				}
				if (definition.getLocalizedResourceList() != null) {
					List<LocalizedStaticResourceInfo> lsrInfoList = new ArrayList<LocalizedStaticResourceInfo>();
					for (LocalizedStaticResourceDefinition lsrDef : definition.getLocalizedResourceList()) {
						LocalizedStaticResourceInfo localeInfo = LocalizedStaticResourceInfo.valueOf(lsrDef);
						localeInfo.setName(definition.getName());
						BinaryDefinition localResource = lsrDef.getResource();
						if (localResource != null) {
							//2.1.34までパスが設定されていたので除去
							String binaryName =  FilenameUtils.getName(localResource.getName());
							localeInfo.setBinaryName(binaryName);
							localeInfo.setStoredBinaryName(binaryName);
							localeInfo.setFileType(localResource instanceof ArchiveBinaryDefinition ? FileType.ARCHIVE : FileType.SIMPLE);
						}
						lsrInfoList.add(localeInfo);
					}
					info.setLocalizedResourceList(lsrInfoList);
				}

				entry.setDefinition(info);
				return entry;
			}
		});
	}

	@Override
	public AdminDefinitionModifyResult createStaticResourceDefinition(int tenantId, final DefinitionSummary definitionName) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<AdminDefinitionModifyResult>() {
			@Override
			public AdminDefinitionModifyResult call() {
				StaticResourceDefinition definition = new StaticResourceDefinition();
				definition.setName(definitionName.getName());
				definition.setDisplayName(definitionName.getDisplayName());
				definition.setDescription(definitionName.getDescription());

				auditLogger.logMetadata(MetaDataAction.CREATE, StaticResourceDefinition.class.getName(), "name:" + definition.getName());
				DefinitionModifyResult ret = srdm.create(definition);
				return new AdminDefinitionModifyResult(ret.isSuccess(), ret.getMessage());
			}
		});
	}

	@Override
	public AdminDefinitionModifyResult deleteStaticResourceDefinition(int tenantId, final String name) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<AdminDefinitionModifyResult>() {
			@Override
			public AdminDefinitionModifyResult call() {
				auditLogger.logMetadata(MetaDataAction.DELETE, StaticResourceDefinition.class.getName(), "name:" + name);
				DefinitionModifyResult ret = srdm.remove(name);
				return new AdminDefinitionModifyResult(ret.isSuccess(), ret.getMessage());
			}
		});
	}

	@Override
	public AdminDefinitionModifyResult copyStaticResourceDefinition(int tenantId, final String sourceName, final DefinitionSummary newDefinitionName) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<AdminDefinitionModifyResult>() {
			@Override
			public AdminDefinitionModifyResult call() {
				StaticResourceDefinition source = srdm.get(sourceName);
				source.setName(newDefinitionName.getName());
				source.setDisplayName(newDefinitionName.getDisplayName());
				source.setDescription(newDefinitionName.getDescription());

				auditLogger.logMetadata(MetaDataAction.CREATE, StaticResourceDefinition.class.getName(), "name:" + source.getName());
				DefinitionModifyResult ret = srdm.create(source);
				return new AdminDefinitionModifyResult(ret.isSuccess(), ret.getMessage());
			}
		});
	}

	/* ---------------------------------------
	 * EntityWebApi
	 --------------------------------------- */

	@Override
	public List<DefinitionEntry> getEntityWebApiDefinitionEntryList(final int tenantId) {

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<List<DefinitionEntry>>() {

			@Override
			public List<DefinitionEntry> call() {

				// Entity定義の取得
				List<DefinitionSummary> entityNameList = edm.definitionNameList();

				List<DefinitionEntry> definitionEntryList = new ArrayList<DefinitionEntry>();
				for (DefinitionSummary entityName : entityNameList) {
					if (EntityDefinition.SYSTEM_DEFAULT_DEFINITION_NAME.equals(entityName.getName())) {
						continue;
					}

					// １つのEntity定義の不具合により取得できないことを避けるため、Catchする
					EntityDefinition definition = null;
					EntityWebApiDefinition ewDefinition = null;
					DefinitionEntry entry = null;
					try {
						// Definition取得
						definition = edm.get(entityName.getName());

						// EntityWebApiDefinition情報取得
						entry = dm.getDefinitionEntry(EntityWebApiDefinition.class, entityName.getName());

					} catch (Exception e) {
						logger.error("Entity情報の取得でエラーが発生しました。", e);
					}

					if (entry == null) {
						entry = new DefinitionEntry();
						ewDefinition = new EntityWebApiDefinition();
						ewDefinition.setName(entityName.getName());
						ewDefinition.setDisplayName(definition.getDisplayName());

						// dummy
						DefinitionInfo info = new DefinitionInfo();
						info.setObjDefId("");
						info.setVersion(-1);
						entry.setDefinitionInfo(info);

					} else {
						ewDefinition = (EntityWebApiDefinition) entry.getDefinition();
						ewDefinition.setDisplayName(definition.getDisplayName());
					}
					entry.setDefinition(ewDefinition);

					definitionEntryList.add(entry);
				}

				return definitionEntryList;
			}

		});
	}

	@Override
	public boolean registEntityWebApiDefinition(int tenantId,final List<DefinitionEntry> entryList, final boolean checkVersion) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<Boolean>() {
			@Override
			public Boolean call() {
				for (DefinitionEntry entry : entryList) {

					EntityWebApiDefinition definition = (EntityWebApiDefinition) entry.getDefinition();

					EntityWebApiDefinition existDefinition = null;
					try {
						// EntityWebApiDefinition情報取得
						existDefinition = ewdm.get(definition.getName());

					} catch (Exception e) {
						logger.error("EntityWebApiDefinition情報の取得でエラーが発生しました。", e);
					}

					if (existDefinition == null) {
						auditLogger.logMetadata(MetaDataAction.CREATE, WebApiDefinition.class.getName(), "name:" + definition.getName());
						DefinitionModifyResult result = ewdm.create(definition);
						checkResult(result);
					} else {

						// バージョンの最新チェック、2件目以降でチェックに該当した場合それまでも全件ロールバック
						MetaDataVersionCheckUtil.versionCheck(checkVersion, definition.getClass(), definition.getName(), entry.getDefinitionInfo().getVersion());

						if (existDefinition.isInsert() != definition.isInsert()
								|| existDefinition.isLoad() != definition.isLoad()
								|| existDefinition.isQuery() != definition.isQuery()
								|| existDefinition.isUpdate() != definition.isUpdate()
								|| existDefinition.isDelete() != definition.isDelete()) {
							auditLogger.logMetadata(MetaDataAction.UPDATE, WebApiDefinition.class.getName(), "name:" + definition.getName());
							DefinitionModifyResult result = ewdm.update(definition);
							checkResult(result);
						}
					}
				}
				return true;
			}
		});
	}

	private void checkResult(DefinitionModifyResult result) {
		if (!result.isSuccess()) {
			throw new ApplicationException(result.getMessage());
		}
	}


	/* ---------------------------------------
	 * AuthenticationPolicy
	 --------------------------------------- */

	@Override
	public List<String> getSelectableAuthProviderNameList(int tenantId) {

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<List<String>>() {

			@Override
			public List<String> call() {
				List<String> providers = new ArrayList<String>();
				for (AuthenticationProvider provider : as.getAuthenticationProviders()) {
					if (provider.isSelectableOnAuthPolicy()) {
						providers.add(provider.getProviderName());
					}
				}
				return providers;
			}

		});

	}

	/* ---------------------------------------
	 * Queue
	 --------------------------------------- */

	@Override
	public List<String> getQueueNameList(int tenantId) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<List<String>>() {

			@Override
			public List<String> call() {
				return rqs.getQueueNameList();
			}
		});
	}

	@Override
	public List<TaskSearchResultInfo> searchAsyncTaskInfo(int tenantId, final AsyncTaskInfoSearchCondtion cond) {

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<List<TaskSearchResultInfo>>() {

			@Override
			public List<TaskSearchResultInfo> call() {
				List<AsyncTaskInfo> list = atm.searchAsyncTaskInfo(cond);
				List<TaskSearchResultInfo> result = new ArrayList<TaskSearchResultInfo>();
				for (AsyncTaskInfo info : list) {
					TaskSearchResultInfo copy = new TaskSearchResultInfo();
					copy.setExceptionHandlingMode(info.getExceptionHandlingMode());
					copy.setGroupingKey(info.getGroupingKey());
					copy.setQueue(info.getQueue());
					copy.setResultInfo(info.getResult() != null ? info.getResult().toString() : null);
					copy.setRetryCount(info.getRetryCount());
					copy.setStatus(info.getStatus());
					copy.setTaskId(info.getTaskId());
					copy.setTaskInfo(info.getTask() != null ? info.getTask().toString() : null);
					result.add(copy);
				}
				return result;
			}

		});
	}

	@Override
	public TaskCancelResultInfo cancelAsyncTask(final int tenantId, final String queueName, final List<Long> taskIds) {

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<TaskCancelResultInfo>() {

			@Override
			public TaskCancelResultInfo call() {
				TaskCancelResultInfo info = new TaskCancelResultInfo();

				for (long taskId : taskIds) {
					CancelResult result = new CancelResult();
					result.setTaskId(taskId);
					info.addResult(result);

					final AsyncTaskFuture<?> preResult = atm.getResult(taskId, queueName);
					result.setBeforeStatus(preResult.getStatus());

					Boolean canceled = false;
//					try {
//						preResult.get();

						if (preResult != null) {
							canceled = Transaction.requiresNew(t -> {
								return preResult.cancel(true);
							});
						}
//					} catch (InterruptedException | ExecutionException e) {
//
//					}
					result.setCanceled(canceled);

					//一応ここで再度結果を取得
					AsyncTaskFuture<?> afterResult = atm.getResult(taskId, queueName);
					if (afterResult != null) {
						try {
							afterResult.get();

							result.setResultStatus(afterResult.getStatus());
						} catch (InterruptedException | ExecutionException e) {
						}
					}
				}

				return info;
			}

		});
	}

	@Override
	public TaskLoadResultInfo loadAsyncTaskInfo(int tenantId, final String queueName, final long taskId) {

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<TaskLoadResultInfo>() {

			@Override
			public TaskLoadResultInfo call() {

				TaskLoadResultInfo result = null;
				AsyncTaskInfo info = atm.loadAsyncTaskInfo(taskId, queueName);
				if (info != null) {
					result = new TaskLoadResultInfo();
					result.setExceptionHandlingMode(info.getExceptionHandlingMode());
					result.setGroupingKey(info.getGroupingKey());
					result.setQueue(info.getQueue());
					result.setResultInfo(info.getResult() != null ? info.getResult().toString() : null);
					result.setRetryCount(info.getRetryCount());
					result.setStatus(info.getStatus());
					result.setTaskId(info.getTaskId());
					result.setTaskInfo(info.getTask() != null ? info.getTask().toString() : null);
				}

				return result;
			}

		});
	}

	@Override
	public TaskForceDeleteResultInfo forceDeleteAsyncTask(final int tenantId, final String queueName, final List<Long> taskIds) {

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<TaskForceDeleteResultInfo>() {

			@Override
			public TaskForceDeleteResultInfo call() {
				TaskForceDeleteResultInfo info = new TaskForceDeleteResultInfo();

				for (long taskId : taskIds) {
					info.addTask(taskId);

					final long execTaskId = taskId;

					Transaction.requiresNew(t -> {
						atm.forceDelete(execTaskId, queueName);
					});
				}

				return info;
			}

		});
	}

	/* ---------------------------------------
	 * Menu item
	 --------------------------------------- */

	@Override
	public List<String> getImageColorList(int tenantId) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<List<String>>() {

			@Override
			public List<String> call() {
				return gcs.getImageColorNames();
			}
		});
	}

	private static String resourceString(String suffix, Object... arguments) {
		return AdminResourceBundleUtil.resourceString("MetaDataServiceImpl." + suffix, arguments);
	}

	/* ---------------------------------------
	 * OAuth
	 --------------------------------------- */

	@Override
	public String generateCredentialOAuthClient(final int tenantId, final String definitionName) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<String>() {

			@Override
			public String call() {
				OAuthClientRuntime runtime = oacs.getRuntimeByName(definitionName);
				if (runtime != null) {
					Credential credential = runtime.generateCredential();
					if (credential != null && credential instanceof IdPasswordCredential) {
						return ((IdPasswordCredential)credential).getPassword();
					}
				}
				return null;
			}
		});
	}

	@Override
	public void deleteOldCredentialOAuthClient(final int tenantId, final String definitionName) {
		AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<Void>() {

			@Override
			public Void call() {
				OAuthClientRuntime runtime = oacs.getRuntimeByName(definitionName);
				if (runtime != null) {
					runtime.deleteOldCredential();
				}
				return null;
			}
		});
	}

	@Override
	public String generateCredentialOAuthResourceServer(final int tenantId, final String definitionName) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<String>() {

			@Override
			public String call() {
				OAuthResourceServerRuntime runtime = oars.getRuntimeByName(definitionName);
				if (runtime != null) {
					Credential credential = runtime.generateCredential();
					if (credential != null && credential instanceof IdPasswordCredential) {
						return ((IdPasswordCredential)credential).getPassword();
					}
				}
				return null;
			}
		});
	}

	@Override
	public void deleteOldCredentialOAuthResourceServer(final int tenantId, final String definitionName) {
		AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<Void>() {

			@Override
			public Void call() {
				OAuthResourceServerRuntime runtime = oars.getRuntimeByName(definitionName);
				if (runtime != null) {
					runtime.deleteOldCredential();
				}
				return null;
			}
		});
	}

	/* ---------------------------------------
	 * OpenIDConnect
	 --------------------------------------- */
	@Override
	public void createClientSecret(final int tenantId, final String definitionName, final String clientSecret) {
		AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<Void>() {

			@Override
			public Void call() {
				oicdm.saveClientSecret(definitionName, clientSecret);
				return null;
			}
		});
	}

	/* ---------------------------------------
	 * Webhook Endpoint Security Info
	 --------------------------------------- */
	@Override
	public void updateWebhookEndpointSecurityInfo(final int tenantId, final String definitionName, final String secret, final String TokenType) {
		AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<Void>() {
			@Override
			public Void call() {
				wepdm.modifySecurityToken(tenantId, definitionName, secret, TokenType);
				return null;
			}
		});
	}

	@Override
	public String getWebhookEndpointSecurityInfo(final int tenantId, final String definitionName, final String TokenType) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<String>() {
			@Override
			public String call() {
				return wheps.getSecurityToken(tenantId, definitionName, TokenType);
			}
		});
	}

	@Override
	public String generateHmacTokenString() {
		return wepdm.generateHmacKey();
	}

	/**
	 *
	 * returns a map of <defName,Url>
	 * */
	public Map<String, String> getEndpointFullListWithUrl(int tenantId){
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<HashMap<String, String>>() {
			@Override
			public HashMap<String, String> call() {
				HashMap<String, String> endpointMap = new HashMap<String, String>();
				List<Name> tempNameList = getDefinitionNameList(tenantId, WebhookEndpointDefinition.class.getName());
				for (Name name : tempNameList) {
					WebhookEndpointDefinition temp = getDefinition(tenantId,WebhookEndpointDefinition.class.getName(),name.getName());
					endpointMap.put(name.getName(), temp.getUrl());
				}
				return endpointMap;
			}
		});
	}
}
