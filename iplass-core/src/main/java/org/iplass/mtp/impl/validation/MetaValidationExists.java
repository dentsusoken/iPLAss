/*
 * Copyright (C) 2020 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.ValidationContext;
import org.iplass.mtp.entity.definition.ValidationDefinition;
import org.iplass.mtp.entity.definition.VersionControlType;
import org.iplass.mtp.entity.definition.validations.ExistsValidation;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.expr.And;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.entity.property.MetaProperty;
import org.iplass.mtp.impl.entity.property.MetaReferenceProperty;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.util.StringUtil;

public class MetaValidationExists extends MetaValidation {

	private static final long serialVersionUID = -2810049312153732045L;

	//VersionControlTypeを設定するKEY値
	private static final String VERSION_TYPE_CONTEXT = "versionType";

	//エラーデータを設定するKEY値
	private static final String ERROR_DATA_CONTEXT = "errorDatas";

	//メッセージにエラーデータをバインドするKEY値
	private static final String REFERENCE_BIND_NAME = "${reference}";

	private static final Pattern valuePattern = Pattern.compile(REFERENCE_BIND_NAME, Pattern.LITERAL);

	@Override
	public void applyConfig(ValidationDefinition definition) {
		fillFrom(definition);
	}

	@Override
	public ValidationDefinition currentConfig(EntityContext context) {
		ExistsValidation def = new ExistsValidation();
		fillTo(def);
		return def;
	}

	@Override
	public MetaValidationExists copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public ValidationHandler createRuntime(final MetaEntity entity, final MetaProperty property) {

		return new ValidationHandler(this) {

			@Override
			public boolean validate(Object value, ValidationContext context) {
				if (value == null) {
					return true;
				}
				if (value instanceof Entity) {
					return checkExists((Entity) value, context);
				}
				throw new EntityRuntimeException("not support type:" + value.getClass());
			}

			@Override
			public boolean validateArray(Object[] values, ValidationContext context) {
				boolean res = true;
				if (values != null) {
					for (Object v: values) {
						//指定されているデータ分チェック
						res &= validate(v, context);
					}
				}
				return res;
			}

			@Override
			public String generateErrorMessage(Object value, ValidationContext context,
					String propertyDisplayName, String entityDisplayName) {

				String msg = super.generateErrorMessage(value, context, propertyDisplayName, entityDisplayName);
				if (msg != null) {
					//値の埋め込み
					if (msg.contains(REFERENCE_BIND_NAME)) {
						//エラーデータの取得
						@SuppressWarnings("unchecked")
						List<Entity> errors = (List<Entity>)context.getAttribute(ERROR_DATA_CONTEXT);
						if (errors != null) {
							StringBuilder strValue = new StringBuilder();
							for (int i = 0; i < errors.size(); i++) {
								Entity entity = errors.get(i);
								//複数ある場合はカンマ区切り
								if (i != 0) {
									strValue.append(", ");
								}
								strValue.append(entity.getOid());
								if (context.getAttribute(VERSION_TYPE_CONTEXT) == VersionControlType.VERSIONED) {
									//バージョンを付加
									strValue.append("." + (entity.getVersion() != null ? entity.getVersion() : "0"));
								}
							}
							msg = valuePattern.matcher(msg).replaceAll(strValue.toString());
						}
					}
				}
				return msg;
			}

			private boolean checkExists(Entity entity, ValidationContext context) {
				MetaReferenceProperty rp = ((MetaReferenceProperty)property);
				if (StringUtil.isNotEmpty(rp.getMappedByPropertyMetaDataId())) {
					//被参照は対象外
					return true;
				}

				EntityContext ectx = EntityContext.getCurrentContext();
				EntityHandler refEntity = ectx.getHandlerById(rp.getReferenceEntityMetaDataId());
				if (refEntity != null) {
					//VersionControlTypeを保持
					VersionControlType versionType = refEntity.getMetaData().getVersionControlType();
					context.setAttribute(VERSION_TYPE_CONTEXT, versionType);

					Query query = new Query().select(Entity.OID).from(refEntity.getMetaData().getName());
					Condition condition = null;
					if (versionType == VersionControlType.VERSIONED) {
						condition = new And().eq(Entity.OID, entity.getOid())
								.eq(Entity.VERSION, entity.getVersion() != null ? entity.getVersion() : 0);
					} else {
						condition = new Equals(Entity.OID, entity.getOid());
					}
					query.where(condition);

					EntityManager em = ManagerLocator.manager(EntityManager.class);
					if (em.count(query) <= 0) {
						//エラーデータを保持
						@SuppressWarnings("unchecked")
						List<Entity> errors = (List<Entity>)context.getAttribute(ERROR_DATA_CONTEXT);
						if (errors == null) {
							errors = new ArrayList<>();
							context.setAttribute(ERROR_DATA_CONTEXT, errors);
						}
						errors.add(entity);
						return false;
					}

					return true;
				}

				return true;
			}

		};
	}

}
