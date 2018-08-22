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

package org.iplass.mtp.impl.mail.template;

import org.iplass.mtp.impl.definition.AbstractTypedDefinitionManager;
import org.iplass.mtp.impl.definition.TypedMetaDataService;
import org.iplass.mtp.impl.mail.MailService;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.mail.template.definition.MailTemplateDefinition;
import org.iplass.mtp.mail.template.definition.MailTemplateDefinitionManager;
import org.iplass.mtp.spi.ServiceRegistry;

public class MailTemplateDefinitionManagerImpl extends AbstractTypedDefinitionManager<MailTemplateDefinition> implements
		MailTemplateDefinitionManager {

//	private static final Logger logger = LoggerFactory.getLogger(MailTemplateDefinitionManagerImpl.class);

	private MailService service;
//	private DefinitionManager dm;

	public MailTemplateDefinitionManagerImpl() {
		service = ServiceRegistry.getRegistry().getService(MailService.class);
//		dm = ManagerLocator.getInstance().getManager(DefinitionManager.class);
	}

	// AbstractTypedDefinitionManager<D>.get(String)
//	@Override
//	public MailTemplateDefinition get(String definitionName) {
//		MailTemplateRuntime handler = service.getMailTemplateByName(definitionName);
//		if (handler == null) {
//			return null;
//		}
//		return handler.getMetaData().currentConfig();
//	}

	// FIXME DefinitionModifyResult はサブクラス必要か？
//	@Override
//	public MailTemplateDefinitionModifyResult create(
//			MailTemplateDefinition definition) {
//		MetaMailTemplate metaData = new MetaMailTemplate();
//		metaData.applyConfig(definition);
//
//		try {
//			service.storeTemplate(metaData);
//		} catch (final Exception e) {
//			setRollbackOnly();
//			if (e.getCause() != null) {
//				logger.error("exception occured during mail template definition create:" + e.getCause().getMessage(), e.getCause());
//				return new MailTemplateDefinitionModifyResult(false, e.getCause().getMessage());
//			} else {
//				logger.error("exception occured during mail template definition create:" + e.getMessage(), e);
//				return new MailTemplateDefinitionModifyResult(false, e.getMessage());
//			}
//		}
//
//		return new MailTemplateDefinitionModifyResult(true);
//	}

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
//		return dm.listName(MailTemplateDefinition.class, filterPath);
//	}

//	@Override
//	public MailTemplateDefinitionModifyResult update(
//			MailTemplateDefinition definition) {
//
//		MailTemplateRuntime handler = service.getMailTemplateByName(definition.getName());
//		if(handler == null) {
//			throw new IllegalStateException("mail template not found. mail template name=" + definition.getName());
//		}
//
//		MetaMailTemplate metaData = new MetaMailTemplate();
//		metaData.applyConfig(definition);
//		metaData.setId(handler.getMetaData().getId());
//
//		try {
//			service.updateTemplate(metaData);
//		} catch (Exception e) {
//			setRollbackOnly();
//			if (e.getCause() != null) {
//				logger.error("exception occured during mail template definition update:" + e.getCause().getMessage(), e.getCause());
//				return new MailTemplateDefinitionModifyResult(false, e.getCause().getMessage());
//			} else {
//				logger.error("exception occured during mail template definition update:" + e.getMessage(), e);
//				return new MailTemplateDefinitionModifyResult(false, e.getMessage());
//			}
//		}
//
//		return new MailTemplateDefinitionModifyResult(true);
//	}

//	@Override
//	public MailTemplateDefinitionModifyResult remove(String definitionName) {
//		try {
//			service.removeTemplate(definitionName);
//		} catch (Exception e) {
//			setRollbackOnly();
//			if (e.getCause() != null) {
//				logger.error("exception occured during mail template definition remove:" + e.getCause().getMessage(), e.getCause());
//				return new MailTemplateDefinitionModifyResult(false, e.getCause().getMessage());
//			} else {
//				logger.error("exception occured during mail template definition remove:" + e.getMessage(), e);
//				return new MailTemplateDefinitionModifyResult(false, e.getMessage());
//			}
//		}
//
//		return new MailTemplateDefinitionModifyResult(true);
//	}

//	private void setRollbackOnly() {
//		Transaction t = ServiceRegistry.getRegistry().getService(TransactionService.class).getTransacitonManager().currentTransaction();
//		if (t != null && t.getStatus() == TransactionStatus.ACTIVE) {
//			t.setRollbackOnly();
//		}
//	}

	@Override
	public Class<MailTemplateDefinition> getDefinitionType() {
		return MailTemplateDefinition.class;
	}

	@Override
	protected RootMetaData newInstance(MailTemplateDefinition definition) {
		return new MetaMailTemplate();
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected TypedMetaDataService getService() {
		return service;
	}
}
