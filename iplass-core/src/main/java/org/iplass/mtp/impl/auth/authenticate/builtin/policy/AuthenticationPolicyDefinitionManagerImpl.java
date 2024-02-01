/*
 * Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.auth.authenticate.builtin.policy;

import org.iplass.mtp.auth.policy.definition.AuthenticationPolicyDefinition;
import org.iplass.mtp.auth.policy.definition.AuthenticationPolicyDefinitionManager;
import org.iplass.mtp.impl.auth.authenticate.builtin.policy.MetaAuthenticationPolicy.AuthenticationPolicyRuntime;
import org.iplass.mtp.impl.definition.AbstractTypedDefinitionManager;
import org.iplass.mtp.impl.definition.TypedMetaDataService;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.spi.ServiceRegistry;

public class AuthenticationPolicyDefinitionManagerImpl extends AbstractTypedDefinitionManager<AuthenticationPolicyDefinition> implements AuthenticationPolicyDefinitionManager {
//	private static Logger logger = LoggerFactory.getLogger(AuthenticationPolicyDefinitionManagerImpl.class);

	private AuthenticationPolicyService service = ServiceRegistry.getRegistry().getService(AuthenticationPolicyService.class);
//	private DefinitionManager dm = ManagerLocator.getInstance().getManager(DefinitionManager.class);

//	@Override
//	public DefinitionModifyResult create(AuthenticationPolicyDefinition definition) {
//		MetaAuthenticationPolicy meta = new MetaAuthenticationPolicy();
//		meta.applyConfig(definition);
//
//		try {
//			service.create(meta);
//		} catch (Exception e) {
//			setRollbackOnly();
//			logger.error("exception occured during authentication policy definition create:" + e.getMessage(), e);
//			return new DefinitionModifyResult(false, "exception occured during authentication policy definition create:" + e.getMessage());
//		}
//		return new DefinitionModifyResult(true);
//	}

//	@Override
//	public DefinitionModifyResult update(AuthenticationPolicyDefinition definition) {
//		AuthenticationPolicyRuntime handler = service.get(definition.getName());
//		if(handler == null) {
//			logger.error("exception occured during authentication policy definition update:"
//					+ "AuthenticationPolicyDefinition not found.definitionName=" + definition.getName());
//			return new DefinitionModifyResult(false, "exception occured during authentication policy definition:"
//					+ "AuthenticationPolicyDefinition not found.definitionName=" + definition.getName());
//		}
//		MetaAuthenticationPolicy meta = new MetaAuthenticationPolicy();
//		meta.setId(handler.getMetaData().getId());
//		meta.applyConfig(definition);
//
//		try {
//			service.update(meta);
//		} catch (Exception e) {
//			setRollbackOnly();
//			logger.error("exception occured during authentication policy definition update:" + e.getMessage(), e);
//			return new DefinitionModifyResult(false, "exception occured during authentication policy definition update:" + e.getMessage());
//		}
//		return new DefinitionModifyResult(true);
//	}

//	@Override
//	public DefinitionModifyResult remove(String definitionName) {
//		AuthenticationPolicyRuntime runtime = service.get(definitionName);
//		if(runtime == null) {
//			//存在しない場合は正常終了として返す
//			return new DefinitionModifyResult(true);
//		}
//
//		try {
//			service.remove(runtime.getMetaData().getName());
//		} catch (Exception e) {
//			setRollbackOnly();
//			logger.error("exception occured during authentication policy definition remove:" + e.getMessage(), e);
//			return new DefinitionModifyResult(false, "exception occured during authentication policy definition remove:" + e.getMessage());
//		}
//		return new DefinitionModifyResult(true);
//	}

//	@Override
//	public AuthenticationPolicyDefinition get(String definitionName) {
//		AuthenticationPolicyRuntime runtime =  service.get(definitionName);
//		if (runtime == null) {
//			return null;
//		}
//		return runtime.getMetaData().currentConfig();
//	}

	@Override
	public AuthenticationPolicyDefinition getOrDefault(String definitionName) {
		AuthenticationPolicyRuntime runtime =  service.getOrDefault(definitionName);
		if (runtime == null) {
			return null;
		}
		return runtime.getMetaData().currentConfig();
	}

//	@Override
//	public List<String> definitionList() {
//		return service.nameList();
//	}

//	@Override
//	public List<DefinitionName> definitionNameList() {
//		return definitionNameList("");
//	}

//	@Override
//	public List<DefinitionName> definitionNameList(String filterPath) {
//		return dm.listName(AuthenticationPolicyDefinition.class, filterPath);
//	}

//	private void setRollbackOnly() {
//		Transaction t = ServiceRegistry.getRegistry().getService(TransactionService.class).getTransacitonManager().currentTransaction();
//		if (t != null && t.getStatus() == TransactionStatus.ACTIVE) {
//			t.setRollbackOnly();
//		}
//	}

	@Override
	public Class<AuthenticationPolicyDefinition> getDefinitionType() {
		return AuthenticationPolicyDefinition.class;
	}

	@Override
	protected RootMetaData newInstance(AuthenticationPolicyDefinition definition) {
		return new MetaAuthenticationPolicy();
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected TypedMetaDataService getService() {
		return service;
	}

}
