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

package org.iplass.gem.command.generic.detail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.iplass.gem.command.Constants;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.auth.User;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.condition.predicate.In;
import org.iplass.mtp.view.generic.element.property.PropertyItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 詳細画面用コマンドのスーパークラス
 * <pre>
 * 本クラスはマルチテナント基盤用の基底コマンドです。
 * 予告なくインターフェースが変わる可能性があるため、
 * 継承は出来る限り行わないでください。
 * </pre>
 * @author lis3wg
 */
public abstract class DetailCommandBase extends RegistrationCommandBase<DetailCommandContext, PropertyItem> {

	private static Logger logger = LoggerFactory.getLogger(DetailCommandBase.class);

	@Override
	protected Logger getLogger() {
		return logger;
	}

	/**
	 * コンテキストを取得します。
	 * @param request リクエスト
	 * @return コンテキスト
	 */
	protected DetailCommandContext getContext(RequestContext request) {
		DetailCommandContext context = new DetailCommandContext(request, em, edm);
		context.setEntityDefinition(edm.get(context.getDefinitionName()));
		context.setEntityView(evm.get(context.getDefinitionName()));

		return context;
	}

	protected void setUserInfoMap(DetailCommandContext context, Entity entity, boolean isDetail) {
		if (!context.isUseUserPropertyEditor(isDetail)) return;

		List<String> userOidList = new ArrayList<>();
		Map<String, UserRefData> datas = new HashMap<String, UserRefData>();

		for (String propertyName : context.getUseUserPropertyEditorPropertyName(isDetail)) {
			int firstDotIndex = propertyName.indexOf(".");
			// refプロパティの場合
			// 対象Entity、OID、取得対象プロパティを保持し、本ループ終了後Entity単位で再取得を行う
			if (firstDotIndex > -1) {
				String topPropName = propertyName.substring(0, firstDotIndex);
				String subPropName = propertyName.substring(firstDotIndex + 1);
				Object entityTemp = entity.getValue(topPropName);
				if (entityTemp != null) {
					// 単一プロパティの場合
					if (entityTemp instanceof Entity) {
						Entity refEntity = (Entity) entityTemp;
						String defName = refEntity.getDefinitionName();
						UserRefData data = null;
						if (datas.containsKey(defName)) {
							data = datas.get(defName);
						} else {
							data = new UserRefData();
						}
						data.set(refEntity.getOid(), subPropName);
						datas.put(defName, data);
					// 多重プロパティの場合
					} else if (entityTemp instanceof Entity[]){
						Entity[] refEntities = (Entity[]) entityTemp;
						for (Entity refEntity : refEntities) {
							String defName = refEntity.getDefinitionName();
							UserRefData data = null;
							if (datas.containsKey(defName)) {
								data = datas.get(defName);
							} else {
								data = new UserRefData();
							}
							data.set(refEntity.getOid(), subPropName);
							datas.put(defName, data);
						}
					}
				}

			// 通常プロパティの場合
			} else {
				String oid = entity.getValue(propertyName);
				if (oid != null && !userOidList.contains(oid)) {
					userOidList.add(oid);
				}
			}
		}

		// 参照元分のユーザー情報を再取得
		if (!datas.isEmpty()) {
			for (Map.Entry<String, UserRefData> data : datas.entrySet()) {
				UserRefData userRef = data.getValue();
				Query q = new Query().select(userRef.getProps().toArray())
						 .from(data.getKey())
						 .where(new In(Entity.OID, userRef.getOids().toArray()));
				em.searchEntity(q, new Predicate<Entity>() {

					@Override
					public boolean test(Entity entity) {
						Map<String, List<String>> mapping = userRef.getMapping();
						if (mapping.containsKey(entity.getOid())) {
							List<String> props = mapping.get(entity.getOid());
							for (String prop : props) {
								if (!userOidList.contains(entity.getValue(prop))) {
									userOidList.add(entity.getValue(prop));
								}
							}
						}
						return true;
					}
				});
			}
		}

		if (!userOidList.isEmpty()) {
			//UserEntityを検索してリクエストに格納
			final Map<String, Entity> userMap = new HashMap<String, Entity>();

			if (context.getView().isShowUserNameWithPrivilegedValue()) {
				AuthContext.doPrivileged(() -> {
					searchUserMap(userMap, userOidList);
				});
			} else {
				searchUserMap(userMap, userOidList);
			}

			context.setAttribute(Constants.USER_INFO_MAP, userMap);
		}

	}

	private void searchUserMap(Map<String, Entity> userMap, final List<String> userOidList) {
		Query q = new Query().select(Entity.OID, Entity.NAME)
							 .from(User.DEFINITION_NAME)
							 .where(new In(Entity.OID, userOidList.toArray()));
		
		em.searchEntity(q, new Predicate<Entity>() {

			@Override
			public boolean test(Entity dataModel) {
				if (!userMap.containsKey(dataModel.getOid())) {
					userMap.put(dataModel.getOid(), dataModel);
				}
				return true;
			}
		});
	}

	private class UserRefData {
		private List<String> oids;
		private List<String> props;
		private Map<String, List<String>> mapping;

		UserRefData () {
			oids = new ArrayList<String>();
			props = new ArrayList<String>();
			props.add(Entity.OID);
			mapping = new HashMap<String, List<String>>();
		}

		void set(String oid, String property) {
			if (!oids.contains(oid)) {
				oids.add(oid);
			}
			if (!props.contains(property)) {
				props.add(property);
			}
			if (mapping.containsKey(oid)) {
				List<String> list = mapping.get(oid);
				if(!list.contains(property)) {
					list.add(property);
					mapping.put(oid, list);
				}
			} else {
				List<String> list = new ArrayList<String>();
				list.add(property);
				mapping.put(oid, list);
			}
		}

		List<String> getOids() {
			return oids;
		}
		List<String> getProps() {
			return props;
		}
		Map<String, List<String>> getMapping() {
			return mapping;
		}
	}

}
