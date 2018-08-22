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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Pattern;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.impl.auth.authorize.builtin.AuthorizationContextCacheLogic;
import org.iplass.mtp.impl.auth.authorize.builtin.BuiltinAuthorizationContext;
import org.iplass.mtp.impl.auth.authorize.builtin.TenantAuthorizeContext;
import org.iplass.mtp.impl.auth.authorize.builtin.role.RoleCacheLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class EntityAuthContextCacheLogic extends AuthorizationContextCacheLogic {

	private static final Logger logger = LoggerFactory.getLogger(EntityAuthContextCacheLogic.class);

	public static final String ENTITY_PERMISSION_DEF_NAME = "mtp.auth.EntityPermission";
	public static final String TARGET_ENTITY = "targetEntity";
	public static final String ROLE = "role";
	public static final String CAN_CREATE = "canCreate";
	public static final String CREATE_COND = "createCondition";
	public static final String CREATE_PROP_CTRL_TYPE = "createPropertyControlType";//E,D
	public static final String CREATE_PROP_LIST = "createPropertyList";
	public static final String CAN_REF = "canReference";
	public static final String REF_COND = "referenceCondition";
	public static final String REF_PROP_CTRL_TYPE = "referencePropertyControlType";
	public static final String REF_PROP_LIST = "referencePropertyList";
	public static final String CAN_UPDATE = "canUpdate";
	public static final String UPDATE_COND = "updateCondition";
	public static final String UPDATE_PROP_CTRL_LIST = "updatePropertyControlType";
	public static final String UPDATE_PROP_LIST = "updatePropertyList";
	public static final String CAN_DEL = "canDelete";
	public static final String DEL_COND = "deleteCondition";

	public static final String ENABLE = "E";
	public static final String DISABLE = "D";

	private EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);

	private Pattern spliter = Pattern.compile("\\s*,\\s*|\\s+");

	EntityAuthContextCacheLogic(TenantAuthorizeContext authorizeContext) {
		super(authorizeContext);
	}

	@Override
	public BuiltinAuthorizationContext load(String key) {
		return AuthContext.doPrivileged(() -> {
			Query q = new Query()
					.select(Entity.OID,//0
							ROLE + "." + RoleCacheLogic.ROLE_CODE,//1
							CAN_CREATE,//2
							CREATE_COND,//3
							CREATE_PROP_CTRL_TYPE,//4
							CREATE_PROP_LIST,//5
							CAN_REF,//6
							REF_COND,//7
							REF_PROP_CTRL_TYPE,//8
							REF_PROP_LIST,//9
							CAN_UPDATE,//10
							UPDATE_COND,//11
							UPDATE_PROP_CTRL_LIST,//12
							UPDATE_PROP_LIST,//13
							CAN_DEL,//14
							DEL_COND)//15
					.from(ENTITY_PERMISSION_DEF_NAME)
					.where(new Equals(TARGET_ENTITY, key));

			boolean[] isFind = {false};
			ArrayList<EntityPermissionEntry> createEntity = new ArrayList<EntityPermissionEntry>();
			ArrayList<EntityPermissionEntry> refEntity = new ArrayList<EntityPermissionEntry>();
			ArrayList<EntityPermissionEntry> updateEntity = new ArrayList<EntityPermissionEntry>();
			ArrayList<EntityPermissionEntry> delEntity = new ArrayList<EntityPermissionEntry>();
			ArrayList<EntityPropertyPermissionEntry> createProp = new ArrayList<EntityPropertyPermissionEntry>();
			ArrayList<EntityPropertyPermissionEntry> refProp = new ArrayList<EntityPropertyPermissionEntry>();
			ArrayList<EntityPropertyPermissionEntry> updateProp = new ArrayList<EntityPropertyPermissionEntry>();

			em.search(q, dataModel -> {
				if (dataModel[1] != null) {
					isFind[0] = true;
					//createEntity
					createEntity.add(new EntityPermissionEntry((String) dataModel[1], (String) dataModel[3], checkPermitFlag(dataModel[2])));
					//refEntity
					refEntity.add(new EntityPermissionEntry((String) dataModel[1], (String) dataModel[7], checkPermitFlag(dataModel[6])));
					//updateEntity
					updateEntity.add(new EntityPermissionEntry((String) dataModel[1], (String) dataModel[11], checkPermitFlag(dataModel[10])));
					//deleteEntity
					delEntity.add(new EntityPermissionEntry((String) dataModel[1], (String) dataModel[15], checkPermitFlag(dataModel[14])));

					//createProperty
					if (dataModel[4] != null) {
						createProp.add(new EntityPropertyPermissionEntry((String) dataModel[1], ((SelectValue) dataModel[4]).getValue().equals(ENABLE), toPropNameArray((String) dataModel[5])));
					} else {
						//defaultはすべて許可
						createProp.add(new EntityPropertyPermissionEntry((String) dataModel[1], false, (HashSet<String>) null));
					}
					//refProperty
					if (dataModel[8] != null) {
						refProp.add(new EntityPropertyPermissionEntry((String) dataModel[1], ((SelectValue) dataModel[8]).getValue().equals(ENABLE), toPropNameArray((String) dataModel[9])));
					} else {
						refProp.add(new EntityPropertyPermissionEntry((String) dataModel[1], false, (HashSet<String>) null));
					}
					//updateProperty
					if (dataModel[12] != null) {
						updateProp.add(new EntityPropertyPermissionEntry((String) dataModel[1], ((SelectValue) dataModel[12]).getValue().equals(ENABLE), toPropNameArray((String) dataModel[13])));
					} else {
						updateProp.add(new EntityPropertyPermissionEntry((String) dataModel[1], false, (HashSet<String>) null));
					}
				} else {
					logger.warn("role code not defined. so ignore this entry:oid=" + dataModel[0]);
				}
				return true;
			});

			if (!isFind[0]) {
				return null;
			}
			return new BuiltinEntityAuthContext(
					key,
					createEntity.toArray(new EntityPermissionEntry[createEntity.size()]),
					refEntity.toArray(new EntityPermissionEntry[refEntity.size()]),
					updateEntity.toArray(new EntityPermissionEntry[updateEntity.size()]),
					delEntity.toArray(new EntityPermissionEntry[delEntity.size()]),
					createProp.toArray(new EntityPropertyPermissionEntry[createProp.size()]),
					refProp.toArray(new EntityPropertyPermissionEntry[refProp.size()]),
					updateProp.toArray(new EntityPropertyPermissionEntry[updateProp.size()]),
					authorizeContext);
		});
	}

	private boolean checkPermitFlag(Object val) {
		if (val == null) {
			return false;
		}
		return ((Boolean) val).booleanValue();
	}

	private String[] toPropNameArray(String propListString) {
		if (propListString == null) {
			return null;
		}
		String[] splited = spliter.split(propListString);
		return splited;
	}

}
