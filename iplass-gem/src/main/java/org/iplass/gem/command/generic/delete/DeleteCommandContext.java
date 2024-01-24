/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.gem.command.generic.delete;


import java.util.Collections;
import java.util.List;

import org.iplass.gem.command.Constants;
import org.iplass.gem.command.GemResourceBundleUtil;
import org.iplass.gem.command.generic.GenericCommandContext;
import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.entity.DeleteTargetVersion;
import org.iplass.mtp.entity.ValidateError;
import org.iplass.mtp.entity.definition.VersionControlType;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.utilityclass.definition.UtilityClassDefinitionManager;
import org.iplass.mtp.view.generic.BulkOperationInterrupter;
import org.iplass.mtp.view.generic.DetailFormView;
import org.iplass.mtp.view.generic.FormViewUtil;
import org.iplass.mtp.view.generic.SearchFormView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteCommandContext extends GenericCommandContext {

	private static Logger logger = LoggerFactory.getLogger(DeleteCommandContext.class);

	protected UtilityClassDefinitionManager ucdm = null;

	private SearchFormView searchView;
	private DetailFormView detailView;
	private DeleteInterrupterHandler deleteInterrupterHandler = null;
	private DeleteTargetVersion deleteTargetVersion;

	protected Logger getLogger() {
		return logger;
	}

	public DeleteCommandContext(RequestContext request) {
		super(request);

		ucdm = ManagerLocator.getInstance().getManager(UtilityClassDefinitionManager.class);
	}

	@Override
	public List<ValidateError> getErrors() {
		return Collections.emptyList();
	}

	@Override
	public void addError(ValidateError error) {
	}

	@Override
	public boolean hasErrors() {
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public SearchFormView getView() {
		if (searchView == null) {
			String viewName = getViewName();
			searchView = FormViewUtil.getSearchFormView(entityDefinition, entityView, viewName);
		}
		return searchView;
	}

	public DetailFormView getDetailView() {
		if (detailView == null) {
			String viewName = getViewName();
			detailView = FormViewUtil.getDetailFormView(entityDefinition, entityView, viewName);
		}
		return detailView;
	}

	public DeleteInterrupterHandler getDeleteInterrupterHandler() {
		if (deleteInterrupterHandler == null) {
			BulkOperationInterrupter deleteInterrupter = createBulkInterrupter(getDeleteInterrupterName());
			deleteInterrupterHandler = new DeleteInterrupterHandler(request, this, deleteInterrupter);
		}
		return deleteInterrupterHandler;
	}

	protected String getDeleteInterrupterName() {
		return getView().getResultSection().getDeleteInterrupterName();
	}

	protected BulkOperationInterrupter createBulkInterrupter(String className) {
		BulkOperationInterrupter interrupter = null;
		if (StringUtil.isNotEmpty(className)) {
			getLogger().debug("set delete operation interrupter. class=" + className);
			try {
				interrupter = ucdm.createInstanceAs(BulkOperationInterrupter.class, className);
			} catch (ClassNotFoundException e) {
				getLogger().error(className + " can not instantiate.", e);
				throw new ApplicationException(resourceString("command.generic.detail.DeleteCommandContext.internalErr"));
			}
		}
		if (interrupter == null) {
			// 何もしないデフォルトInterrupter生成
			getLogger().debug("set default delete operation interrupter.");
			interrupter = new BulkOperationInterrupter() {};
		}
		return interrupter;
	}

	protected DeleteTargetVersion getSearchDeleteTargetVersion() {

		if (deleteTargetVersion == null) {
			deleteTargetVersion = DeleteTargetVersion.ALL;
			if (getEntityDefinition().getVersionControlType() != VersionControlType.NONE) {
				boolean isDeleteSpecificVersion = getView().isDeleteSpecificVersion();
				if (isDeleteSpecificVersion && isAllVersion()) {
					deleteTargetVersion = DeleteTargetVersion.SPECIFIC;
				}
			}
		}
		return deleteTargetVersion;
	}

	private boolean isAllVersion() {
		String allVer = getRequest().getParam(Constants.SEARCH_ALL_VERSION);
		return "1".equals(allVer);
	}

	protected String resourceString(String key, Object... arguments) {
		return GemResourceBundleUtil.resourceString(key, arguments);
	}

}
