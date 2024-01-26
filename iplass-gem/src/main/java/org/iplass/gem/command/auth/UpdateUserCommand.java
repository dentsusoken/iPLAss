/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.gem.command.auth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.iplass.gem.command.Constants;
import org.iplass.gem.command.GemResourceBundleUtil;
import org.iplass.gem.command.ViewUtil;
import org.iplass.gem.command.generic.ResultType;
import org.iplass.gem.command.generic.detail.DetailCommandBase;
import org.iplass.gem.command.generic.detail.DetailCommandContext;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.auth.User;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.action.ActionMapping;
import org.iplass.mtp.command.annotation.action.Result;
import org.iplass.mtp.command.annotation.action.Result.Type;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.ValidateError;
import org.iplass.mtp.view.generic.DetailFormView;
import org.iplass.mtp.view.generic.EntityView;
import org.iplass.mtp.view.generic.EntityViewManager;
import org.iplass.mtp.view.generic.EntityViewUtil;
import org.iplass.mtp.view.generic.OutputType;
import org.iplass.mtp.view.generic.editor.JoinPropertyEditor;
import org.iplass.mtp.view.generic.editor.NestProperty;
import org.iplass.mtp.view.generic.element.Element;
import org.iplass.mtp.view.generic.element.property.PropertyItem;
import org.iplass.mtp.view.generic.element.section.DefaultSection;
import org.iplass.mtp.view.generic.element.section.Section;
import org.iplass.mtp.view.top.TopViewDefinition;
import org.iplass.mtp.view.top.TopViewDefinitionManager;
import org.iplass.mtp.view.top.parts.TopViewParts;
import org.iplass.mtp.view.top.parts.UserMaintenanceParts;

@ActionMapping(
	name=UpdateUserCommand.ACTION_NAME,
	displayName="更新",
	needTrustedAuthenticate=true,
	result=@Result(type=Type.TEMPLATE, value=Constants.TEMPLATE_UPDATE_PASSWORD)
)
@CommandClass(name="gem/auth/UpdateUserCommand", displayName="ユーザー情報更新")
public final class UpdateUserCommand extends DetailCommandBase {

	public static final String ACTION_NAME = "gem/auth/updateUser";

	private EntityViewManager evm;
	private TopViewDefinitionManager tdm;

	public UpdateUserCommand() {
		em = ManagerLocator.getInstance().getManager(EntityManager.class);
		evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
		tdm = ManagerLocator.getInstance().getManager(TopViewDefinitionManager.class);
	}

	@Override
	public String execute(RequestContext request) {
		String roleName = request.getParam(Constants.ROLE_NAME);
		request.setAttribute(Constants.UPDATE_USER_INFO, "true");

		EntityView ev = evm.get(User.DEFINITION_NAME);
		DetailFormView view = null;

		TopViewDefinition td = tdm.get(roleName);
		UserMaintenanceParts parts = null;
		if (td != null) {
			for (TopViewParts tmp : td.getParts()) {
				if (tmp instanceof UserMaintenanceParts) {
					parts = (UserMaintenanceParts) tmp;
					break;
				}
			}
			if (parts != null && ev != null) {
				view = ev.getDetailFormView(parts.getViewName());
			}
		}
		if (view == null) {
			request.setAttribute(Constants.MESSAGE, resourceString("command.auth.UpdateUserCommand.viewErr"));
			return Constants.CMD_EXEC_ERROR_VIEW;
		}

		if (view.getInterrupterName() == null || view.getInterrupterName().isEmpty()) {
			view.setInterrupterName(UserRegistrationInterrupter.class.getName());
		}
		DetailCommandContext context = getContext(request);
		context.setView(view);

		if (context.hasErrors()) {
			ValidateError[] errors = context.getErrors().toArray(new ValidateError[context.getErrors().size()]);
			request.setAttribute(Constants.ERROR_PROP, errors);
			request.setAttribute(Constants.MESSAGE, resourceString("command.auth.UpdateUserCommand.inputErr"));
			return Constants.CMD_EXEC_ERROR;
		}

		//nameの更新とかで表示してない項目が必要になる可能性があるので、
		//最新データを画面の入力内容で上書きし、それを更新する
		User user = AuthContext.getCurrentContext().getUser();
		Entity updateEntity = em.load(user.getOid(), User.DEFINITION_NAME);
		Entity newEntity = context.createEntity();
//		for (PropertyDefinition pd : context.getPropertyList()) {
//			Object propValue = newEntity.getValue(pd.getName());
//			if (propValue != null) updateEntity.setValue(pd.getName(), propValue);
//		}
		List<PropertyItem> properties = getProperty(view, newEntity);
		for (PropertyItem property : properties) {
			Object propValue = newEntity.getValue(property.getPropertyName());
			updateEntity.setValue(property.getPropertyName(), propValue);
		}
		EditResult ret = AuthContext.doPrivileged(() -> updateEntity(context, updateEntity));

		if (ret.getResultType() == ResultType.ERROR) {
			List<ValidateError> tmpList = new ArrayList<ValidateError>();
			if (ret.getErrors() != null) {
				tmpList.addAll(Arrays.asList(ret.getErrors()));
			}
			ValidateError[] errors = tmpList.toArray(new ValidateError[tmpList.size()]);
			request.setAttribute(Constants.ERROR_PROP, errors);
			request.setAttribute(Constants.MESSAGE, ret.getMessage());
			return Constants.CMD_EXEC_ERROR;
		}

		AuthContext.getCurrentContext().refresh();
		return Constants.CMD_EXEC_SUCCESS;
	}

	private List<PropertyItem> getProperty(DetailFormView view, Entity entity) {
		List<PropertyItem> propList = new ArrayList<PropertyItem>();
		for (Section section : view.getSections()) {
			if (section instanceof DefaultSection
					&& EntityViewUtil.isDisplayElement(User.DEFINITION_NAME, section.getElementRuntimeId(), OutputType.EDIT, entity)
					&& ViewUtil.dispElement(Constants.EXEC_TYPE_UPDATE, section)) {
				propList.addAll(getProperty((DefaultSection)section, entity));
			}
		}
		return propList;
	}

	/**
	 * セクション内のプロパティ取得を取得します。
	 * @param section セクション
	 * @return プロパティの一覧
	 */
	private List<PropertyItem> getProperty(DefaultSection section, Entity entity) {
		List<PropertyItem> propList = new ArrayList<PropertyItem>();
		for (Element elem : section.getElements()) {
			if (elem instanceof PropertyItem) {
				PropertyItem prop = (PropertyItem) elem;
				if (prop.getEditor() instanceof JoinPropertyEditor) {
					//組み合わせで使うプロパティを通常のプロパティ扱いに
					JoinPropertyEditor je = (JoinPropertyEditor) prop.getEditor();
					for (NestProperty nest : je.getProperties()) {
						PropertyItem dummy = new PropertyItem();
						dummy.setDispFlag(true);
						dummy.setPropertyName(nest.getPropertyName());
						dummy.setEditor(nest.getEditor());
						propList.add(dummy);
					}
				}
				if (EntityViewUtil.isDisplayElement(User.DEFINITION_NAME, prop.getElementRuntimeId(), OutputType.EDIT, entity)
						&& ViewUtil.dispElement(Constants.EXEC_TYPE_UPDATE, prop)) {
					propList.add(prop);
				}
			} else if (elem instanceof DefaultSection) {
				propList.addAll(getProperty((DefaultSection)elem, entity));
			}
		}
		return propList;
	}

	private static String resourceString(String key, Object... arguments) {
		return GemResourceBundleUtil.resourceString(key, arguments);
	}
}
