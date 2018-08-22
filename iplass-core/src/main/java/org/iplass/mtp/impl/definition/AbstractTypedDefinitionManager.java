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
package org.iplass.mtp.impl.definition;

import java.util.List;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.definition.Definition;
import org.iplass.mtp.definition.DefinitionManager;
import org.iplass.mtp.definition.DefinitionModifyResult;
import org.iplass.mtp.definition.DefinitionSummary;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.impl.metadata.MetaDataRuntime;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.transaction.TransactionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Definitionの取得、更新を行うための型付けされたManagerのスーパークラス。
 *
 * @author K.Higuchi
 *
 * @param <D> このDefinitionManagerが扱うDefinitionのType
 */
public abstract class AbstractTypedDefinitionManager<D extends Definition> implements TypedDefinitionManager<D> {
	private DefinitionManager dm = ManagerLocator.getInstance().getManager(DefinitionManager.class);
	private static final Logger logger = LoggerFactory.getLogger(AbstractTypedDefinitionManager.class);

	@SuppressWarnings("unchecked")
	@Override
	public D get(String definitionName) {
		MetaDataEntry entry = MetaDataContext.getContext().getMetaDataEntry(DefinitionService.getInstance().getPath(getDefinitionType(), definitionName));
		if (entry == null) {
			return null;
		}
		D definition = null;
		if (entry.getMetaData() instanceof DefinableMetaData<?>) {
			definition = (D) ((DefinableMetaData<D>) entry.getMetaData()).currentConfig();
		} else {
			logger.error("metadata is not definable. type=" + entry.getMetaData().getClass().getName());
		}

		return definition;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public DefinitionModifyResult create(D definition) {
		TypedMetaDataService service = getService();
		RootMetaData meta = newInstance(definition);
		if (meta instanceof DefinableMetaData<?>) {
			((DefinableMetaData<D>) meta).applyConfig(definition);
		} else {
			logger.error("metadata is not definable. type=" + meta.getClass().getName());
			return new DefinitionModifyResult(false, "metadata is not definable. type=" + meta.getClass().getName());
		}

		try {
			service.createMetaData(meta);
		} catch (Exception e) {
			setRollbackOnly();
			String type = meta.getClass().getSimpleName();
			if (e.getCause() != null) {
				logger.error("exception occured during " + type + " create:" + e.getCause().getMessage(), e.getCause());
				return new DefinitionModifyResult(false, "exception occured during " + type + " create:" + e.getCause().getMessage());
			} else {
				logger.error("exception occured during " + type + " create:" + e.getMessage(), e);
				return new DefinitionModifyResult(false, "exception occured during " + type + " create:" + e.getMessage());
			}
		}
		return new DefinitionModifyResult(true);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public DefinitionModifyResult update(D definition) {
		TypedMetaDataService service = getService();
		MetaDataRuntime handler = getService().getRuntimeByName(definition.getName());
		if(handler == null) {
			String type = definition.getClass().getSimpleName();
			logger.error("exception occured during " + type + " update:"
					+ type + " not found.definitionName=" + definition.getName());
			return new DefinitionModifyResult(false, "exception occured during " + type + " update:"
					+ type + " not found.definitionName=" + definition.getName());
		}
		RootMetaData meta = newInstance(definition);
		meta.setId(((RootMetaData) handler.getMetaData()).getId());
		if (meta instanceof DefinableMetaData<?>) {
			((DefinableMetaData<D>) meta).applyConfig(definition);
		} else {
			logger.error("metadata is not definable. type=" + meta.getClass().getName());
			return new DefinitionModifyResult(false, "metadata is not definable. type=" + meta.getClass().getName());
		}

		try {
			service.updateMetaData(meta);
		} catch (Exception e) {
			setRollbackOnly();
			String type = meta.getClass().getSimpleName();
			if (e.getCause() != null) {
				logger.error("exception occured during " + type + " update:" + e.getCause().getMessage(), e.getCause());
				return new DefinitionModifyResult(false, "exception occured during " + type + " update:" + e.getCause().getMessage());
			} else {
				logger.error("exception occured during " + type + " update:" + e.getMessage(), e);
				return new DefinitionModifyResult(false, "exception occured during " + type + " update:" + e.getMessage());
			}
		}
		return new DefinitionModifyResult(true);
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public DefinitionModifyResult remove(String definitionName) {
		TypedMetaDataService service = getService();
		MetaDataRuntime handler =  service.getRuntimeByName(definitionName);
		if(handler == null) {
			//該当がない場合は正常とみなす
			return new DefinitionModifyResult(true);
		}
		RootMetaData meta = (RootMetaData) handler.getMetaData();
		try {
			service.removeMetaData(meta.getName());
		} catch (Exception e) {
			setRollbackOnly();
			String type = meta.getClass().getSimpleName();
			if (e.getCause() != null) {
				logger.error("exception occured during " + type + " remove:" + e.getCause().getMessage(), e.getCause());
				return new DefinitionModifyResult(false, "exception occured during " + type + " remove:" + e.getCause().getMessage());
			} else {
				logger.error("exception occured during " + type + " remove:" + e.getMessage(), e);
				return new DefinitionModifyResult(false, "exception occured during " + type + " remove:" + e.getMessage());
			}
		}
		return new DefinitionModifyResult(true);
	}


	protected void setRollbackOnly() {
		Transaction t = Transaction.getCurrent();
		if (t != null && t.getStatus() == TransactionStatus.ACTIVE) {
			t.setRollbackOnly();
		}
	}

	@Override
	public List<DefinitionSummary> definitionSummaryList(String filterPath, boolean recursive) {
		return dm.listName(getDefinitionType(), filterPath, recursive);
	}

	@Override
	public void rename(String oldDefinitionName, String newDefinitionName) {
		dm.rename(getDefinitionType(), oldDefinitionName, newDefinitionName);
	}

	protected abstract RootMetaData newInstance(D definition);

	@SuppressWarnings("rawtypes")
	protected abstract TypedMetaDataService getService();
}
