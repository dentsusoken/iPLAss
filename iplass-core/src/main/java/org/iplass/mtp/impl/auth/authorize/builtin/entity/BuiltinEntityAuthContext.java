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

package org.iplass.mtp.impl.auth.authorize.builtin.entity;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.iplass.mtp.auth.NoPermissionException;
import org.iplass.mtp.auth.Permission;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.permission.EntityPermission;
import org.iplass.mtp.entity.permission.EntityPropertyPermission;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.expr.And;
import org.iplass.mtp.entity.query.condition.expr.Or;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.auth.UserBinding;
import org.iplass.mtp.impl.auth.authorize.builtin.BuiltinAuthorizationContext;
import org.iplass.mtp.impl.auth.authorize.builtin.TenantAuthorizeContext;
import org.iplass.mtp.impl.auth.authorize.builtin.role.RoleContext;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.auth.EntityAuthContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class BuiltinEntityAuthContext extends BuiltinAuthorizationContext implements EntityAuthContext {

	//TODO Actionのように許可/否許可の限定条件を設定可能にするかどうか

	private static final Logger logger = LoggerFactory.getLogger(BuiltinEntityAuthContext.class);

	//動作上、参照不可とするのができない項目。これらは設定に左右されず参照可能。
	private static final HashSet<String> systemUseProperty;
	static {
		systemUseProperty = new HashSet<String>();
		systemUseProperty.add(Entity.OID);
		systemUseProperty.add(Entity.VERSION);
		systemUseProperty.add(Entity.UPDATE_DATE);
		systemUseProperty.add(Entity.STATE);
		systemUseProperty.add(Entity.LOCKED_BY);
		systemUseProperty.add(Entity.START_DATE);
		systemUseProperty.add(Entity.END_DATE);
		systemUseProperty.add(Entity.RECYCLE_BIN_ID);
	}


//	private String entityDefinitionName;
	private EnumMap<EntityPermission.Action, EntityPermissionEntry[]> entityPermissionEntry;
	private EnumMap<EntityPropertyPermission.Action, EntityPropertyPermissionEntry[]> entityPropertyPermissionEntry;

	private TenantAuthorizeContext tenantAuthContext;

	BuiltinEntityAuthContext(String entityDefinitionName, EnumMap<EntityPermission.Action, EntityPermissionEntry[]> entityPermissionEntry, EnumMap<EntityPropertyPermission.Action, EntityPropertyPermissionEntry[]> entityPropertyPermissionEntry, TenantAuthorizeContext tenantAuthContext) {
		super(entityDefinitionName);
//		this.entityDefinitionName = entityDefinitionName;
		this.tenantAuthContext = tenantAuthContext;
		this.entityPermissionEntry = entityPermissionEntry;
		this.entityPropertyPermissionEntry = entityPropertyPermissionEntry;
	}

	BuiltinEntityAuthContext(String entityDefinitionName,
			EntityPermissionEntry[] createEntityPermissionEntry,
			EntityPermissionEntry[] referenceEntityPermissionEntry,
			EntityPermissionEntry[] updateEntityPermissionEntry,
			EntityPermissionEntry[] deleteEntityPermissionEntry,
			EntityPropertyPermissionEntry[] createEntityPropertyPermissionEntry,
			EntityPropertyPermissionEntry[] referenceEntityPropertyPermissionEntry,
			EntityPropertyPermissionEntry[] updateEntityPropertyPermissionEntry,
			TenantAuthorizeContext tenantAuthContext) {
		super(entityDefinitionName);
//		this.entityDefinitionName = entityDefinitionName;
		this.tenantAuthContext = tenantAuthContext;

		entityPermissionEntry = new EnumMap<EntityPermission.Action, EntityPermissionEntry[]>(EntityPermission.Action.class);
		if (createEntityPermissionEntry != null && createEntityPermissionEntry.length != 0) {
			entityPermissionEntry.put(EntityPermission.Action.CREATE, createEntityPermissionEntry);
		}
		if (referenceEntityPermissionEntry != null && referenceEntityPermissionEntry.length != 0) {
			entityPermissionEntry.put(EntityPermission.Action.REFERENCE, referenceEntityPermissionEntry);
		}
		if (updateEntityPermissionEntry != null && updateEntityPermissionEntry.length != 0) {
			entityPermissionEntry.put(EntityPermission.Action.UPDATE, updateEntityPermissionEntry);
		}
		if (deleteEntityPermissionEntry != null && deleteEntityPermissionEntry.length != 0) {
			entityPermissionEntry.put(EntityPermission.Action.DELETE, deleteEntityPermissionEntry);
		}

		entityPropertyPermissionEntry = new EnumMap<EntityPropertyPermission.Action, EntityPropertyPermissionEntry[]>(EntityPropertyPermission.Action.class);
		if (createEntityPropertyPermissionEntry != null && createEntityPropertyPermissionEntry.length != 0) {
			entityPropertyPermissionEntry.put(EntityPropertyPermission.Action.CREATE, createEntityPropertyPermissionEntry);
		}
		if (referenceEntityPropertyPermissionEntry != null && referenceEntityPropertyPermissionEntry.length != 0) {
			entityPropertyPermissionEntry.put(EntityPropertyPermission.Action.REFERENCE, referenceEntityPropertyPermissionEntry);
		}
		if (updateEntityPropertyPermissionEntry != null && updateEntityPropertyPermissionEntry.length != 0) {
			entityPropertyPermissionEntry.put(EntityPropertyPermission.Action.UPDATE, updateEntityPropertyPermissionEntry);
		}
	}
	
	TenantAuthorizeContext getTenantAuthContext() {
		return tenantAuthContext;
	}

//	public String getEntityDefinitionName() {
//		return entityDefinitionName;
//	}

	private List<EntityPermissionEntry> listEntityTarget (EntityPermission.Action action, AuthContextHolder userAuthContext) {
		EntityPermissionEntry[] toList = entityPermissionEntry.get(action);
		if (toList == null) {
			return null;
		}
		List<EntityPermissionEntry> target = new LinkedList<EntityPermissionEntry>();
		long currentPriority = 0;
		for (int i = 0; i < toList.length; i++) {
			if (userAuthContext.userInRole(toList[i].getRole(), tenantAuthContext.getTenantContext().getTenantId())) {
				RoleContext role = tenantAuthContext.getRoleContext(toList[i].getRole());
				if (currentPriority < role.getPriority()) {
					//reset priority
					currentPriority = role.getPriority();
					target.clear();
				}
				if (currentPriority == role.getPriority()) {
					target.add(toList[i]);
				}
			}
		}
		return target;
	}
	
	private List<EntityPermissionEntry> listEntityTarget(EntityPermission.Action action, String role) {
		EntityPermissionEntry[] toList = entityPermissionEntry.get(action);
		if (toList == null) {
			return null;
		}
		List<EntityPermissionEntry> target = new LinkedList<EntityPermissionEntry>();
		
		for (int i = 0; i < toList.length; i++) {
			if (toList[i].getRole().equals(role)) {
				target.add(toList[i]);
			}
		}
		return target;
	}

	private List<EntityPropertyPermissionEntry> listEntityPropertyTarget (EntityPropertyPermission.Action action, AuthContextHolder userAuthContext) {
		EntityPropertyPermissionEntry[] toList = entityPropertyPermissionEntry.get(action);
		if (toList == null) {
			return null;
		}
		List<EntityPropertyPermissionEntry> target = new LinkedList<EntityPropertyPermissionEntry>();
		long currentPriority = 0;
		for (int i = 0; i < toList.length; i++) {
			if (userAuthContext.userInRole(toList[i].getRole(), tenantAuthContext.getTenantContext().getTenantId())) {
				RoleContext role = tenantAuthContext.getRoleContext(toList[i].getRole());
				if (currentPriority < role.getPriority()) {
					//reset priority
					currentPriority = role.getPriority();
					target.clear();
				}
				if (currentPriority == role.getPriority()) {
					target.add(toList[i]);
				}
			}
		}
		return target;
	}

	@Override
	public boolean isPermit(Permission permission, AuthContextHolder user) {
		if (permission instanceof EntityPermission) {
			return isPermit(((EntityPermission) permission).getAction(), user);
		} else {
			EntityPropertyPermission epp = (EntityPropertyPermission) permission;
			return isPermit(epp.getPropertyName(), epp.getAction(), user);
		}
	}
	
	@Override
	public boolean isResultCacheable(Permission permission) {
		return true;
	}

	boolean isPermit(EntityPermission.Action action, AuthContextHolder userAuthContext) {
		//admin（かつ、他テナントユーザでない）は全権限保持
		UserBinding user = userAuthContext.newUserBinding(tenantAuthContext);
		if (user.isGrantAllPermissions()) {
			return true;
		}

		boolean ret = false;

		List<EntityPermissionEntry> checkTargetList = listEntityTarget(action, userAuthContext);
		if (checkTargetList != null) {
			for (EntityPermissionEntry c: checkTargetList) {
				if (c.isPermit()) {
					ret = true;
					break;
				}
			}
		}

		return ret;
	}

	boolean isPermit(String propertyName, EntityPropertyPermission.Action action,
			AuthContextHolder userAuthContext) {

		//admin（かつ、他テナントユーザでない）は全権限保持
		UserBinding user = userAuthContext.newUserBinding(tenantAuthContext);
		if (user.isGrantAllPermissions()) {
			return true;
		}

		boolean ret = false;

		//システム動作上、必要となる項目は設定によらず参照可能。
		if (action == EntityPropertyPermission.Action.REFERENCE && systemUseProperty.contains(propertyName)) {
			//Entityレベルの参照権限チェックのみ
			ret = isPermit(EntityPermission.Action.REFERENCE, userAuthContext);
		} else {
			List<EntityPropertyPermissionEntry> checkTarget = listEntityPropertyTarget(action, userAuthContext);
			if (checkTarget != null) {
				for (EntityPropertyPermissionEntry c: checkTarget) {
					if (c.isPermit(propertyName)) {
						//対象roleのEntityレベルの権限チェック
						List<EntityPermissionEntry> checkEntityTarget = listEntityTarget(toEntityAction(action), c.getRole());
						if (checkEntityTarget != null) {
							for (EntityPermissionEntry e: checkEntityTarget) {
								if (e.isPermit()) {
									ret = true;
									break;
								}
							}
						}
					}
					
					if (ret == true) {
						break;
					}
				}
			}
		}

		return ret;
	}
	
	private EntityPermission.Action toEntityAction(EntityPropertyPermission.Action action) {
		switch (action) {
		case CREATE:
			return EntityPermission.Action.CREATE;
		case REFERENCE:
			return EntityPermission.Action.REFERENCE;
		case UPDATE:
			return EntityPermission.Action.UPDATE;
		default:
			return null;
		}
	}

	@Override
	public Condition addLimitingCondition(Condition orignal, EntityPermission.Action action,
			AuthContextHolder userAuthContext) {

		//admin（かつ、他テナントユーザでない）は全権限保持
		UserBinding user = userAuthContext.newUserBinding(tenantAuthContext);
		if (user.isGrantAllPermissions()) {
			return orignal;
		}

		List<EntityPermissionEntry> checkTarget = listEntityTarget(action, userAuthContext);
		if (checkTarget == null || checkTarget.size() == 0) {
			throw new NoPermissionException("access denyed:" + getContextName());
		}

		Or or = new Or();
		boolean isPermit = false;
		boolean hasNoCondition = false;
		for (EntityPermissionEntry c: checkTarget) {
			if (c.isPermit()) {
				isPermit = true;
			}
			if (c.hasLimitCondition()) {
				or.addExpression(c.getCondition(user, tenantAuthContext));
			} else {
				if (c.isPermit()) {
					hasNoCondition = true;
				}
			}
		}

		if (!isPermit) {
			throw new NoPermissionException("access denyed:" + getContextName());
		}

		if (hasNoCondition) {
			return orignal;
		}

		if (orignal == null) {
			return or.strip();
		} else {
			return new And(orignal, or.strip());
		}
	}

	@Override
	public Query modifyQuery(Query orignal, EntityPermission.Action action, AuthContextHolder userAuthContext) {
		return modifyQuery(orignal, action, null, userAuthContext);
	}

	@Override
	public Query modifyQuery(Query orignal, EntityPermission.Action action, EntityPropertyPermission.Action propAction, AuthContextHolder userAuthContext) {

//		//admin（かつ、他テナントユーザでない）は全権限保持
//		UserBinding user = new UserBinding(userAuthContext.getUserContext(), tenantAuthContext);
//		if (user.isAdmin()) {
//			return orignal;
//		}

		long time = 0;
		if (logger.isDebugEnabled()) {
			time = System.currentTimeMillis();
		}

		EntityContext entityContext = EntityContext.getCurrentContext();
		EntityHandler eh = entityContext.getHandlerByName(getContextName());
		if (eh == null) {
			throw new EntityRuntimeException(getContextName() + " is undefined.");
		}

		AuthQueryASTTransformer t = new AuthQueryASTTransformer(tenantAuthContext, this, action, propAction, userAuthContext, eh, entityContext, null);
		Query authedQuery = t.transform(orignal);

		if (logger.isDebugEnabled()) {
			logger.debug("modifyQuery " + action + "(" + propAction + ") :time=" + (System.currentTimeMillis() - time) + "ms." + " orginal=" + orignal + " : authed=" + authedQuery);
		}

		return authedQuery;
	}


	@Override
	public boolean hasLimitCondition(EntityPermission permission, AuthContextHolder userAuthContext) {

		//admin（かつ、他テナントユーザでない）は全権限保持
		UserBinding user = userAuthContext.newUserBinding(tenantAuthContext);
		if (user.isGrantAllPermissions()) {
			return false;
		}

		List<EntityPermissionEntry> checkTarget = listEntityTarget(permission.getAction(), userAuthContext);

		if (checkTarget == null || checkTarget.size() == 0) {
			throw new NoPermissionException("access denyed:" + getContextName());
		}

		for (EntityPermissionEntry c: checkTarget) {
			if (c.isPermit() && !c.hasLimitCondition()) {
				return false;
			}
		}

		return true;
	}

}
