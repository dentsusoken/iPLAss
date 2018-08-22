/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.entity.auth;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.iplass.mtp.entity.permission.EntityPermission;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityQueryAuthContextHolder {
	
	public static final String REFERENCE_THIS = "this";
	private static final String HOLDER_NAME = "mtp.entity.auth.entityQueryAuthContextHolder";
	private static final Logger logger = LoggerFactory.getLogger(EntityQueryAuthContextHolder.class);
	
	private static final EntityQueryAuthContextHolder DEFAULT = new EntityQueryAuthContextHolder(EntityPermission.Action.REFERENCE, null, null);

	private final EntityPermission.Action queryAction;
	private final Set<String> withoutConditionReferenceNameSet;
	private final EntityQueryAuthContextHolder previous;
	
	private EntityQueryAuthContextHolder(EntityPermission.Action queryAction, String[] withoutConditionReferenceName, EntityQueryAuthContextHolder previous) {
		if (queryAction == null) {
			this.queryAction = EntityPermission.Action.REFERENCE;
		} else {
			this.queryAction = queryAction;
		}
		if (withoutConditionReferenceName == null) {
			withoutConditionReferenceNameSet = Collections.emptySet();
		} else {
			withoutConditionReferenceNameSet = new HashSet<>();
			for (String wcrn: withoutConditionReferenceName) {
				if (REFERENCE_THIS.equalsIgnoreCase(wcrn)) {
					withoutConditionReferenceNameSet.add(REFERENCE_THIS);
				} else {
					withoutConditionReferenceNameSet.add(wcrn);
				}
			}
		}
		this.previous = previous;
	}

	public static EntityQueryAuthContextHolder getContext() {
		ExecuteContext ec = ExecuteContext.getCurrentContext();
		EntityQueryAuthContextHolder holder = (EntityQueryAuthContextHolder) ec.getAttribute(HOLDER_NAME);
		
		if (holder == null) {
			return DEFAULT;
		}
		return holder;
	}
	
	public static void set(EntityPermission.Action queryAction, String[] withoutConditionReferenceName) {
		ExecuteContext ec = ExecuteContext.getCurrentContext();
		EntityQueryAuthContextHolder pre = (EntityQueryAuthContextHolder) ec.getAttribute(HOLDER_NAME);
		ec.setAttribute(HOLDER_NAME, new EntityQueryAuthContextHolder(queryAction, withoutConditionReferenceName, pre), false);
		if (logger.isDebugEnabled()) {
			logger.debug("set EntityQueryAuthContext to " + queryAction);
		}
	}

	public static void clear() {
		ExecuteContext ec = ExecuteContext.getCurrentContext();
		EntityQueryAuthContextHolder holder = (EntityQueryAuthContextHolder) ec.getAttribute(HOLDER_NAME);
		if (holder != null) {
			if (holder.previous == null) {
				ec.removeAttribute(HOLDER_NAME);
			} else {
				ec.setAttribute(HOLDER_NAME, holder.previous, false);
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("clear EntityQueryAuthContext");
		}
	}
	
	public EntityPermission.Action getQueryAction() {
		return queryAction;
	}
	
	public boolean isWithoutConditionReferenceName(String referenceName) {
		return withoutConditionReferenceNameSet.contains(referenceName);
	}

}
