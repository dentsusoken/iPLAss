/*
 * Copyright (C) 2015 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.server.tools.rpc.permissionexplorer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.adminconsole.server.base.rpc.util.AuthUtil;
import org.iplass.adminconsole.server.base.service.AdminEntityManager;
import org.iplass.adminconsole.shared.base.dto.entity.EntityDataTransferTypeList;
import org.iplass.adminconsole.shared.tools.dto.permissionexplorer.PermissionInfo;
import org.iplass.adminconsole.shared.tools.dto.permissionexplorer.PermissionSearchResult;
import org.iplass.adminconsole.shared.tools.dto.permissionexplorer.RolePermissionInfo;
import org.iplass.adminconsole.shared.tools.dto.permissionexplorer.UpdateRoleInfo;
import org.iplass.adminconsole.shared.tools.rpc.permissionexplorer.PermissionExplorerService;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.definition.DefinitionSummary;
import org.iplass.mtp.entity.DeleteOption;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.LoadOption;
import org.iplass.mtp.entity.SearchResult;
import org.iplass.mtp.entity.UpdateOption;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.SortSpec;
import org.iplass.mtp.entity.query.SortSpec.SortType;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.web.actionmapping.definition.ActionMappingDefinitionManager;
import org.iplass.mtp.webapi.definition.WebApiDefinitionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.server.rpc.jakarta.XsrfProtectedServiceServlet;

/**
 * PermissionExplorer用Service実装クラス
 */
public class PermissionExplorerServiceImpl extends XsrfProtectedServiceServlet implements PermissionExplorerService {

	private static final long serialVersionUID = -8009172899435114816L;

	private static final Logger logger = LoggerFactory.getLogger(PermissionExplorerServiceImpl.class);

	private EntityManager em = AdminEntityManager.getInstance();
	private EntityDefinitionManager edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
	private ActionMappingDefinitionManager adm = ManagerLocator.getInstance().getManager(ActionMappingDefinitionManager.class);
	private WebApiDefinitionManager wadm = ManagerLocator.getInstance().getManager(WebApiDefinitionManager.class);

	@Override
	public EntityDataTransferTypeList entityDataTypeWhiteList(EntityDataTransferTypeList param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Entity> getRoleList(int tenantId) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<List<Entity>>() {

			@Override
			public List<Entity> call() {

				//参照Propertyは取得しない
				Query query = new Query();
				query.selectAll("mtp.auth.Role", true, false, false, false).order(new SortSpec("code", SortType.ASC));
				SearchResult<Entity> searchResult = em.searchEntity(query);
				return searchResult.getList();
			}
		});
	}

	@Override
	public Entity loadRoleData(int tenantId, final String oid) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<Entity>() {

			@Override
			public Entity call() {
				//参照Propertyは取得、非参照Propertyは取得しない
				Entity role = em.load(oid, "mtp.auth.Role", new LoadOption(true, false));

				if (role != null) {
					//登録済のRoleConditionの取得
					List<Entity> storedCondition = getRoleCondition(oid);
					List<Entity> conditionList = new ArrayList<>();
					for (Entity conditionOid : storedCondition) {
						//ロードしてRoleに追加
						Entity condition = em.load(conditionOid.getOid(), "mtp.auth.RoleCondition", new LoadOption(false, false));
						if (condition != null) {
							conditionList.add(condition);
						}
					}
					if (!conditionList.isEmpty()) {
						role.setValue("condition", conditionList.toArray(new Entity[]{}));
					}
				}
				return role;
			}
		});
	}

	@Override
	public void updateRoleData(int tenantId, final UpdateRoleInfo storeInfo) {

		AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<Void>() {

			@Override
			public Void call() {

				int insertCount = 0;
				int updateCount = 0;
				int deleteCount = 0;

				//Roleの追加 or 更新
				UpdateOption roleOption = null;
				for (Entity role : storeInfo.getStoreEntityList()) {
					if (StringUtil.isEmpty(role.getOid())) {
						//Roleの追加
						String oid = em.insert(role);
						insertCount++;
						role.setOid(oid);

						//Roleを追加時はConditionも無条件で追加
						Entity[] insertCondition = role.getValue("condition");
						if (insertCondition != null) {
							for (Entity condition : insertCondition) {
								condition.setValue("role", role);
								em.insert(condition);
							}
						}
					} else {
						//Roleの更新
						Entity storedRole = em.load(role.getOid(), "mtp.auth.Role", new LoadOption(false, false));
						if (storedRole != null) {
							role.setVersion(storedRole.getVersion());
						}
						if (roleOption == null) {
							roleOption = UpdateOption.allPropertyUpdateOption("mtp.auth.Role", false);
						}
						em.update(role, roleOption);
						updateCount++;

						//登録済のRoleConditionの取得
						List<Entity> storedConditionList = getRoleCondition(role.getOid());
						//更新対象のRoleConditionの取得
						Entity[] updateConditionList = role.getValue("condition");

						//RoleConditionの追加または更新処理
						if (updateConditionList != null) {
							for (Entity condition : updateConditionList) {
								condition.setValue("role", role);
								if (condition.getOid() == null) {
									//追加
									em.insert(condition);
								} else {
									boolean isExists = false;
									UpdateOption conditionOption = null;
									for (Entity storedCondition : storedConditionList) {
										if (condition.getOid().equals(storedCondition.getOid())) {
											//更新
											isExists = true;
											condition.setVersion(storedCondition.getVersion());
											if (conditionOption == null) {
												conditionOption = UpdateOption.allPropertyUpdateOption("mtp.auth.RoleCondition", false);
											}
											em.update(condition, conditionOption);
											break;
										}
									}
									if (!isExists) {
										//念のため追加
										em.insert(condition);
									}
								}
							}
						}

						//RoleConditionの削除処理
						for (Entity storedCondition : storedConditionList) {
							boolean isExists = false;
							if (updateConditionList != null) {
								for (Entity condition : updateConditionList) {
									if (storedCondition.getOid().equals(condition.getOid())) {
										isExists = true;
										break;
									}
								}
							}
							if (!isExists) {
								em.delete(storedCondition, new DeleteOption(false));
							}
						}
					}
				}

				//Roleの削除
				for (String oid: storeInfo.getRemoveOidList()) {
					Entity delRole = em.load(oid, "mtp.auth.Role");
					if (delRole != null) {
						em.delete(delRole, new DeleteOption(false));
						deleteCount++;
					}
					//FIXME RoleCondition、各種RolePermissionは削除必要？
				}

				logger.debug("role update completed. insert:" + insertCount + ", update:" + updateCount + ", delete:" + deleteCount);
				return null;
			}

		});

	}

	/**
	 * Roleを参照するRoleConditionを取得します。
	 *
	 * @param roleOid RoleのOID
	 * @return RoleCondition(OIDとVERSIONのみ)
	 */
	private List<Entity> getRoleCondition(String roleOid) {
		Query query = new Query();
		query.select(Entity.OID, Entity.VERSION)
			.from("mtp.auth.RoleCondition")
			.where(new Equals("role.oid", roleOid));
		return em.searchEntity(query).getList();
	}

	@Override
	public PermissionSearchResult getAllEntityPermissionData(int tenantId) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<PermissionSearchResult>() {

			@Override
			public PermissionSearchResult call() {

				// EntityDefinitionデータ
				List<DefinitionSummary> definitionNmaeList = edm.definitionNameList();

				// EntityPermissionデータ
				Query query = new Query();
				query.select("oid", "name", "description", "version", "updateDate",
						"role.code", "role.name",
						"targetEntity",
						"canCreate", "createCondition", "createPropertyControlType", "createPropertyList",
						"canReference", "referenceCondition", "referencePropertyControlType", "referencePropertyList",
						"canUpdate", "updateCondition", "updatePropertyControlType", "updatePropertyList",
						"canDelete", "deleteCondition")
						.from("mtp.auth.EntityPermission")
						.order(new SortSpec("targetEntity", SortType.ASC),
								new SortSpec("role.code", SortType.ASC));

				SearchResult<Entity> searchResult = em.searchEntity(query);

				List<PermissionInfo> permissionList = new ArrayList<PermissionInfo>();

				for (DefinitionSummary definitionNmae : definitionNmaeList) {
					PermissionInfo permissionInfo = new PermissionInfo();
					String definitionName = definitionNmae.getName();

					permissionInfo.setDefinitionName(definitionName);
					permissionInfo.setDisplayName(definitionNmae.getDisplayName());

					List<RolePermissionInfo> rolePermissionList = new ArrayList<>();
					for (Entity permission : searchResult.getList()) {
						if (definitionName.equals(permission.getValue("targetEntity"))) {
							RolePermissionInfo rolePermissionInfo = new RolePermissionInfo();
							rolePermissionInfo.setPermission(permission);
							rolePermissionList.add(rolePermissionInfo);
						}
					}
					permissionInfo.setRolePermissionList(rolePermissionList);

					permissionList.add(permissionInfo);
				}

				PermissionSearchResult result = new PermissionSearchResult();
				result.setPermissionList(permissionList);
				return result;
			}
		});
	}

	@Override
	public void updateEntityPermissionData(int tenantId, final List<PermissionInfo> permissionList) {

		AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<Void>() {

			@Override
			public Void call() {

				int insertCount = 0;
				int updateCount = 0;
				int deleteCount = 0;

				UpdateOption updateOption = null;
				DeleteOption deleteOption = null;
				for (PermissionInfo permissionInfo : permissionList) {
					if (permissionInfo.getRolePermissionList() != null) {
						for (RolePermissionInfo rolePermissionInfo : permissionInfo.getRolePermissionList()) {
							if (RolePermissionInfo.INSERT.equals(rolePermissionInfo.getStatus())) {
								em.insert(rolePermissionInfo.getPermission());
								insertCount++;
							} else if (RolePermissionInfo.UPDATE.equals(rolePermissionInfo.getStatus())) {
								if (updateOption == null) {
									updateOption = new UpdateOption();
									updateOption.setUpdateProperties(
											"name", "description",
											"canReference", "referenceCondition", "referencePropertyControlType", "referencePropertyList",
											"canCreate", "createCondition", "createPropertyControlType", "createPropertyList",
											"canUpdate", "updateCondition", "updatePropertyControlType", "updatePropertyList",
											"canDelete", "deleteCondition"
											);
								}
								em.update(rolePermissionInfo.getPermission(), updateOption);
								updateCount++;
							} else if (RolePermissionInfo.DELETE.equals(rolePermissionInfo.getStatus())) {
								if (deleteOption == null) {
									deleteOption = new DeleteOption(true);
								}
								em.delete(rolePermissionInfo.getPermission(), deleteOption);
								deleteCount++;
							}
						}
					}
				}

				logger.debug("entity permission update completed. insert:" + insertCount + ", update:" + updateCount + ", delete:" + deleteCount);
				return null;
			}
		});
	}

	@Override
	public PermissionSearchResult getAllActionPermissionData(int tenantId) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<PermissionSearchResult>() {

			@Override
			public PermissionSearchResult call() {

				// WorkflowDefinitionデータ
				List<DefinitionSummary> definitionNmaeList = adm.definitionSummaryList();

				// WorkflowPermissionデータ
				Query query = new Query();
				query.select("oid", "name", "description", "version", "updateDate",
						"role.code", "role.name",
						"targetAction",
						"conditionExpression")
					.from("mtp.auth.ActionPermission")
					.order(new SortSpec("targetAction", SortType.ASC), new SortSpec("role.code", SortType.ASC));

				SearchResult<Entity> searchResult = em.searchEntity(query);

				List<PermissionInfo> permissionList = new ArrayList<PermissionInfo>();

				for (DefinitionSummary definitionNmae : definitionNmaeList) {
					PermissionInfo permissionInfo = new PermissionInfo();
					String definitionName = definitionNmae.getName();

					permissionInfo.setDefinitionName(definitionName);
					permissionInfo.setDisplayName(definitionNmae.getDisplayName());

					List<RolePermissionInfo> rolePermissionList = new ArrayList<>();
					for (Entity permission : searchResult.getList()) {
						if (definitionName.equals(permission.getValue("targetAction"))) {
							RolePermissionInfo rolePermissionInfo = new RolePermissionInfo();
							rolePermissionInfo.setPermission(permission);
							rolePermissionList.add(rolePermissionInfo);
						}
					}
					permissionInfo.setRolePermissionList(rolePermissionList);
					permissionList.add(permissionInfo);
				}

				Map<String, PermissionInfo> wildCardPermissionMap = new HashMap<>();

				for (Entity permission : searchResult.getList()) {
					String targetAction = permission.getValue("targetAction");

					if (targetAction.endsWith("*")) {
						PermissionInfo permissionInfo = wildCardPermissionMap.get(targetAction);
						if (permissionInfo == null) {
							permissionInfo = new PermissionInfo();
							permissionInfo.setDefinitionName(targetAction);
							permissionInfo.setDisplayName(targetAction);
							wildCardPermissionMap.put(targetAction, permissionInfo);
						}
						RolePermissionInfo rolePermissionInfo = new RolePermissionInfo();
						rolePermissionInfo.setPermission(permission);
						permissionInfo.addRolePermission(rolePermissionInfo);
					}
				}

				PermissionSearchResult result = new PermissionSearchResult();
				result.setPermissionList(permissionList);
				if (wildCardPermissionMap.values() != null) {
					result.setWildCardPermissionList(new ArrayList<>(wildCardPermissionMap.values()));
				}
				return result;
			}
		});
	}

	@Override
	public void updateActionPermissionData(int tenantId, final List<PermissionInfo> permissionList) {

		AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<Void>() {

			@Override
			public Void call() {

				int insertCount = 0;
				int updateCount = 0;
				int deleteCount = 0;

				UpdateOption updateOption = null;
				DeleteOption deleteOption = null;
				for (PermissionInfo permissionInfo : permissionList) {
					if (permissionInfo.getRolePermissionList() != null) {
						for (RolePermissionInfo rolePermissionInfo : permissionInfo.getRolePermissionList()) {
							if (RolePermissionInfo.INSERT.equals(rolePermissionInfo.getStatus())) {
								em.insert(rolePermissionInfo.getPermission());
								insertCount++;
							} else if (RolePermissionInfo.UPDATE.equals(rolePermissionInfo.getStatus())) {
								if (updateOption == null) {
									updateOption = new UpdateOption();
									updateOption.setUpdateProperties(
											"name", "description",
											"conditionExpression"
											);
								}
								em.update(rolePermissionInfo.getPermission(), updateOption);
								updateCount++;
							} else if (RolePermissionInfo.DELETE.equals(rolePermissionInfo.getStatus())) {
								if (deleteOption == null) {
									deleteOption = new DeleteOption(true);
								}
								em.delete(rolePermissionInfo.getPermission(), deleteOption);
								deleteCount++;
							}
						}
					}
				}

				logger.debug("action permission update completed. insert:" + insertCount + ", update:" + updateCount + ", delete:" + deleteCount);
				return null;
			}
		});
	}

	@Override
	public PermissionSearchResult getAllWebApiPermissionData(int tenantId) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<PermissionSearchResult>() {

			@Override
			public PermissionSearchResult call() {

				// WorkflowDefinitionデータ
				List<DefinitionSummary> definitionNmaeList = wadm.definitionSummaryList();

				// WorkflowPermissionデータ
				Query query = new Query();
				query.select("oid", "name", "description", "version", "updateDate",
						"role.code", "role.name",
						"targetWebApi",
						"conditionExpression")
					.from("mtp.auth.WebApiPermission")
					.order(new SortSpec("targetWebApi", SortType.ASC), new SortSpec("role.code", SortType.ASC));

				SearchResult<Entity> searchResult = em.searchEntity(query);

				List<PermissionInfo> permissionList = new ArrayList<PermissionInfo>();

				for (DefinitionSummary definitionNmae : definitionNmaeList) {
					PermissionInfo permissionInfo = new PermissionInfo();
					String definitionName = definitionNmae.getName();

					permissionInfo.setDefinitionName(definitionName);
					permissionInfo.setDisplayName(definitionNmae.getDisplayName());

					List<RolePermissionInfo> rolePermissionList = new ArrayList<>();
					for (Entity permission : searchResult.getList()) {
						if (definitionName.equals(permission.getValue("targetWebApi"))) {
							RolePermissionInfo rolePermissionInfo = new RolePermissionInfo();
							rolePermissionInfo.setPermission(permission);
							rolePermissionList.add(rolePermissionInfo);
						}
					}
					permissionInfo.setRolePermissionList(rolePermissionList);
					permissionList.add(permissionInfo);
				}

				Map<String, PermissionInfo> wildCardPermissionMap = new HashMap<>();

				for (Entity permission : searchResult.getList()) {
					String targetAction = permission.getValue("targetWebApi");

					if (targetAction.endsWith("*")) {
						PermissionInfo permissionInfo = wildCardPermissionMap.get(targetAction);
						if (permissionInfo == null) {
							permissionInfo = new PermissionInfo();
							permissionInfo.setDefinitionName(targetAction);
							permissionInfo.setDisplayName(targetAction);
							wildCardPermissionMap.put(targetAction, permissionInfo);
						}
						RolePermissionInfo rolePermissionInfo = new RolePermissionInfo();
						rolePermissionInfo.setPermission(permission);
						permissionInfo.addRolePermission(rolePermissionInfo);
					}
				}

				PermissionSearchResult result = new PermissionSearchResult();
				result.setPermissionList(permissionList);
				if (wildCardPermissionMap.values() != null) {
					result.setWildCardPermissionList(new ArrayList<>(wildCardPermissionMap.values()));
				}
				return result;
			}
		});
	}

	@Override
	public void updateWebApiPermissionData(int tenantId, final List<PermissionInfo> permissionList) {

		AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<Void>() {

			@Override
			public Void call() {

				int insertCount = 0;
				int updateCount = 0;
				int deleteCount = 0;

				UpdateOption updateOption = null;
				DeleteOption deleteOption = null;
				for (PermissionInfo permissionInfo : permissionList) {
					if (permissionInfo.getRolePermissionList() != null) {
						for (RolePermissionInfo rolePermissionInfo : permissionInfo.getRolePermissionList()) {
							if (RolePermissionInfo.INSERT.equals(rolePermissionInfo.getStatus())) {
								em.insert(rolePermissionInfo.getPermission());
								insertCount++;
							} else if (RolePermissionInfo.UPDATE.equals(rolePermissionInfo.getStatus())) {
								if (updateOption == null) {
									updateOption = new UpdateOption();
									updateOption.setUpdateProperties(
											"name", "description",
											"conditionExpression"
											);
								}
								em.update(rolePermissionInfo.getPermission(), updateOption);
								updateCount++;
							} else if (RolePermissionInfo.DELETE.equals(rolePermissionInfo.getStatus())) {
								if (deleteOption == null) {
									deleteOption = new DeleteOption(true);
								}
								em.delete(rolePermissionInfo.getPermission(), deleteOption);
								deleteCount++;
							}
						}
					}
				}

				logger.debug("webapi permission update completed. insert:" + insertCount + ", update:" + updateCount + ", delete:" + deleteCount);
				return null;
			}
		});
	}

	@Override
	public void dummyConnect(int tenantId) {
		AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<Void>() {

			@Override
			public Void call() {
				return null;
			}
		});
	}

}
