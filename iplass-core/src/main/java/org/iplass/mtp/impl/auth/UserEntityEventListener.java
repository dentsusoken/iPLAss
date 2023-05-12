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

package org.iplass.mtp.impl.auth;

import java.util.List;
import java.util.Locale;

import org.iplass.mtp.auth.User;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityEventContext;
import org.iplass.mtp.entity.EntityEventListener;
import org.iplass.mtp.entity.GenericEntity;
import org.iplass.mtp.entity.UpdateOption;
import org.iplass.mtp.impl.auth.authenticate.AccountManagementModule;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.i18n.I18nService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.TenantI18nInfo;
import org.iplass.mtp.util.StringUtil;

/**
 *
 * @author 片野　博之
 * @author K.Higuchi
 *
 */
public class UserEntityEventListener implements EntityEventListener {

	private AuthService authService = ServiceRegistry.getRegistry().getService(AuthService.class);
	private I18nService service = ServiceRegistry.getRegistry().getService(I18nService.class);

	private String makeName(String firstName, String lastName, String userLang) {
		String name = "";
		String locale = ExecuteContext.getCurrentContext().getCurrentTenant().getTenantConfig(TenantI18nInfo.class).getLocale();

		if (StringUtil.isNotEmpty(userLang)) {
			locale = Locale.forLanguageTag(userLang).toString();
		}

		if (service.getLocaleFormat(locale).isLastNameIsFirst()) {
			//lastName firstName
			name = appendName(name, lastName);
			name = appendName(name, firstName);
		} else {
			//firstName lastName
			name = appendName(name, firstName);
			name = appendName(name, lastName);
		}

		if (name.length() != 0) {
			return name;
		} else {
			return null;
		}
	}

	@Override
	public void beforeValidate(Entity entity, EntityEventContext context) {

		String userLang = entity.getValue(User.LANGUAGE);

		@SuppressWarnings("unchecked")
		List<String> validateProps = (List<String>) context.getAttribute(EntityEventContext.VALIDATE_PROPERTIES);

		if (validateProps == null) {
			//insert
			if (entity.getName() == null) {
				entity.setName(makeName((String) entity.getValue(User.FIRST_NAME), (String) entity.getValue(User.LAST_NAME), userLang));
			}
		} else {
			//update（の可能性あり）
			//nameを明示的にチェック（更新）対象としていない場合、
			//かつ、firstName, lastNameがチェック（更新）対象で、nameがnullの場合
			if ((!validateProps.contains(User.NAME))
					&& (validateProps.contains(User.FIRST_NAME) || validateProps.contains(User.LAST_NAME) || validateProps.contains(User.LANGUAGE))
					&& entity.getName() == null) {
				//暫定的にセット
				entity.setName(makeName((String) entity.getValue(User.FIRST_NAME), (String) entity.getValue(User.LAST_NAME), userLang));
			}
		}

	}

	private String appendName(String name, String appendName) {
		if (appendName != null && !appendName.isEmpty()) {
			if (name != null && !name.isEmpty()) {
				name = name + " ";
			}
			name = name + appendName;
		}
		return name;
	}

	@Override
	public boolean beforeInsert(Entity entity, EntityEventContext context) {
		AccountManagementModule amm = authService.getAccountManagementModule((String) entity.getValue(User.ACCOUNT_POLICY));
		if (amm != null && amm.canCreate()) {
			if (entity instanceof User) {
				amm.create((User) entity);
			} else {
				amm.create(((GenericEntity) entity).copyAs(User.class));
			}
		}
		return true;
	}

	@Override
	public void afterInsert(Entity entity, EntityEventContext context) {
		AccountManagementModule amm = authService.getAccountManagementModule((String) entity.getValue(User.ACCOUNT_POLICY));
		if (amm != null && amm.canCreate()) {
			if (entity instanceof User) {
				amm.afterCreate((User) entity);
			} else {
				amm.afterCreate(((GenericEntity) entity).copyAs(User.class));
			}
		}
	}

	@Override
	public boolean beforeDelete(Entity entity, EntityEventContext context) {
		//ゴミデータ削除を考慮してすべてのAccountManagementModuleに通知
		AccountManagementModule amm = authService.getAccountManagementModule();
		if (amm.canRemove()) {
			if (entity instanceof User) {
				amm.remove((User) entity);
			} else {
				amm.remove(((GenericEntity) entity).copyAs(User.class));
			}
		}
		return true;
	}

	@Override
	public boolean beforeUpdate(Entity entity, EntityEventContext context) {
		UpdateOption uo = (UpdateOption) context.getAttribute(EntityEventContext.UPDATE_OPTION);
		Entity before = (Entity) context.getAttribute(EntityEventContext.BEFORE_UPDATE_ENTITY);

		if ((!uo.getUpdateProperties().contains(Entity.NAME))
				&& (uo.getUpdateProperties().contains(User.FIRST_NAME) || uo.getUpdateProperties().contains(User.LAST_NAME) || uo.getUpdateProperties().contains(User.LANGUAGE))) {
			//nameを明示的に更新対象としていない場合、
			//かつ、firstName, lastNameが更新対象の場合更新

			String firstName = before.getValue(User.FIRST_NAME);
			String lastName = before.getValue(User.LAST_NAME);
			String language = before.getValue(User.LANGUAGE);
			if (uo.getUpdateProperties().contains(User.FIRST_NAME)) {
				firstName = entity.getValue(User.FIRST_NAME);
			}
			if (uo.getUpdateProperties().contains(User.LAST_NAME)) {
				lastName = entity.getValue(User.LAST_NAME);
			}
			if (uo.getUpdateProperties().contains(User.LANGUAGE)) {
				language = entity.getValue(User.LANGUAGE);
			}

			entity.setName(makeName(firstName, lastName, language));
			if (entity.getName() != null) {
				//生成したnameがnullの場合は、更新対象に含めない
				uo.getUpdateProperties().add(Entity.NAME);
			}
		}

		//AccountManagementModuleへ通知
		String policyName = before.getValue(User.ACCOUNT_POLICY);
		if (uo.getUpdateProperties().contains(User.ACCOUNT_POLICY)) {
			policyName = (String) entity.getValue(User.ACCOUNT_POLICY);
		}

		AccountManagementModule amm = authService.getAccountManagementModule(policyName);
		if (amm.canUpdate()) {
			if (entity instanceof User) {
				amm.update((User) entity, uo.getUpdateProperties());
			} else {
				amm.update(((GenericEntity) entity).copyAs(User.class), uo.getUpdateProperties());
			}
		}

		return true;
	}
	
	

	@Override
	public void afterUpdate(Entity entity, EntityEventContext context) {
		UpdateOption uo = (UpdateOption) context.getAttribute(EntityEventContext.UPDATE_OPTION);
		Entity before = (Entity) context.getAttribute(EntityEventContext.BEFORE_UPDATE_ENTITY);
		
		String policyName = before.getValue(User.ACCOUNT_POLICY);
		if (uo.getUpdateProperties().contains(User.ACCOUNT_POLICY)) {
			policyName = (String) entity.getValue(User.ACCOUNT_POLICY);
		}
		AccountManagementModule amm = authService.getAccountManagementModule(policyName);
		if (amm.canUpdate()) {
			if (entity instanceof User) {
				amm.afterUpdate((User) entity, policyName, uo.getUpdateProperties());
			} else {
				amm.afterUpdate(((GenericEntity) entity).copyAs(User.class), policyName, uo.getUpdateProperties());
			}
		}
	}

	@Override
	public void afterRestore(Entity entity) {
		AccountManagementModule amm = authService.getAccountManagementModule((String) entity.getValue(User.ACCOUNT_POLICY));
		if (amm != null && amm.canRestore()) {
			if (entity instanceof User) {
				amm.restore((User) entity);
			} else {
				amm.restore(((GenericEntity) entity).copyAs(User.class));
			}
		}
	}

	@Override
	public void afterPurge(Entity entity) {
		//ゴミデータ削除を考慮してすべてのAccountManagementModuleに通知
		AccountManagementModule amm = authService.getAccountManagementModule();
		if (amm != null && amm.canPurge()) {
			if (entity instanceof User) {
				amm.purge((User) entity);
			} else {
				amm.purge(((GenericEntity) entity).copyAs(User.class));
			}
		}
	}
}
