/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.gem.command.generic.refcombo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.iplass.gem.command.Constants;
import org.iplass.gem.command.generic.HasDisplayScriptBindings;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.RestJson;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.entity.query.PreparedQuery;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.SortSpec;
import org.iplass.mtp.entity.query.SortSpec.SortType;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.expr.And;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.EntityViewManager;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.ReferenceComboSetting;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor.RefSortType;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.RequestType;

/**
 * 参照コンボ選択リスト取得処理
 *
 * @author lis3wg
 */
@WebApi(
		name=ReferenceComboCommand.WEBAPI_NAME,
		accepts=RequestType.REST_JSON,
		methods=MethodType.POST,
		restJson=@RestJson(parameterName="param"),
		results={"selName", "data"},
		checkXRequestedWithHeader=true
	)
@CommandClass(name="gem/generic/refcombo/ReferenceComboCommand", displayName="参照コンボ選択リスト取得")
public final class ReferenceComboCommand implements Command, HasDisplayScriptBindings {

	public static final String WEBAPI_NAME = "gem/generic/refcombo/referenceCombo";

	private EntityManager em = null;
	private EntityDefinitionManager edm = null;
	private EntityViewManager evm = null;

	/**
	 * コンストラクタ
	 */
	public ReferenceComboCommand() {
		em = ManagerLocator.getInstance().getManager(EntityManager.class);
		edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
		evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
	}

	@Override
	public String execute(RequestContext request) {
		//パラメータ取得
		String defName = request.getParam(Constants.DEF_NAME);
		String viewName = request.getParam(Constants.VIEW_NAME);
		String propName = request.getParam(Constants.PROP_NAME);
		String viewType = request.getParam(Constants.VIEW_TYPE);

		Entity entity = getBindingEntity(request);
		//Editor取得
		PropertyEditor editor = evm.getPropertyEditor(defName, viewType, viewName, propName, entity);
		ReferencePropertyEditor rpe = null;
		if (editor instanceof ReferencePropertyEditor) {
			rpe = (ReferencePropertyEditor)editor;
		}

		if (rpe != null) {
			Map<String, List<SimpleEntity>> map = getData(rpe, request);

			//map内は1件のはず
			for (String key : map.keySet()) {
				request.setAttribute("selName", key);
				request.setAttribute("data", map.get(key));
				break;
			}
		}

		return Constants.CMD_EXEC_SUCCESS;
	}

	/**
	 * @param editor
	 * @param map
	 */
	private Map<String, List<SimpleEntity>> getData(ReferencePropertyEditor editor, RequestContext request) {
		Map<String, List<SimpleEntity>> map = new LinkedHashMap<String, List<SimpleEntity>>();

		//プロパティの情報
		String defName = editor.getObjectName();//参照先のdefName
		String propName = editor.getPropertyName();//このプロパティの名前
		ReferenceComboSetting setting = editor.getReferenceComboSetting();

		List<Condition> conditions = new ArrayList<Condition>();
		if (setting != null && setting.getPropertyName() != null) {
			//絞り込み条件取得
			EntityDefinition red = edm.get(defName);
			conditions.addAll(getMappedByData(propName, red, setting, map, request));
		}

		if (!conditions.isEmpty() && map.isEmpty()) {
			if (editor.getCondition() != null) {
				conditions.add(new PreparedQuery(editor.getCondition()).condition(null));
			}

			//上位の条件があって、データがなければ
			Query q = new Query();
			q.select(Entity.OID);
			if (editor.getDisplayLabelItem() != null) {
				// 表示ラベルとして扱うプロパティ
				q.select().add(editor.getDisplayLabelItem());
			} else {
				q.select().add(Entity.NAME);
			}
			q.from(defName).where(new And(conditions));

			String sortItem = editor.getSortItem();
			if (sortItem == null || sortItem.isEmpty()) {
				sortItem = Entity.OID;
			}
			SortType sortType = null;
			if (editor.getSortType() == null || editor.getSortType() == RefSortType.ASC) {
				sortType = SortType.ASC;
			} else {
				sortType = SortType.DESC;
			}
			q.order(new SortSpec(sortItem, sortType));

			final List<SimpleEntity> list = new ArrayList<SimpleEntity>();
			em.searchEntity(q, new Predicate<Entity>() {

				@Override
				public boolean test(Entity dataModel) {
					if (editor.getDisplayLabelItem() != null) {
						String displayLabelItem = editor.getDisplayLabelItem();
						// 表示ラベルとして扱うプロパティをNameに設定
						list.add(new SimpleEntity(dataModel.getOid(), dataModel.getValue(displayLabelItem)));
					} else {
						list.add(new SimpleEntity(dataModel));
					}
					return true;
				}
			});

			map.put(propName, list);
		} else {
			//上位の条件ないか、データがある場合は検索しない
		}

		return map;
	}

	private List<Condition> getMappedByData(String childComboName, EntityDefinition ed, ReferenceComboSetting setting,
			Map<String, List<SimpleEntity>> map, RequestContext request) {
		List<Condition> conditions = new ArrayList<Condition>();
		//参照先のEntity情報
		String propName = setting.getPropertyName();
		ReferenceProperty rp = (ReferenceProperty) ed.getProperty(propName);
		EntityDefinition red = edm.get(rp.getObjectDefinitionName());

		//コンボの値取得
		String comboName = childComboName + "." + propName;
		String val = request.getParam(comboName);

		if (StringUtil.isEmpty(val)) {
			//コンボが未選択
			ReferenceComboSetting parent = setting.getParent();

			//親コンボが選択されてるか確認
			if (parent != null && parent.getPropertyName() != null) {
				//上位階層検索
				conditions.addAll(getMappedByData(comboName, red, parent, map, request));
			}

			//自身が最上位の場合か、親コンボが選択されてたら次は自身を選択させる
			if (parent == null || !conditions.isEmpty()) {
				//参照先のEntity検索
				Query query = new Query();
				query.select(Entity.OID);
				if (setting.getDisplayLabelItem() != null) {
					// 表示ラベルとして扱うプロパティ
					query.select().add(setting.getDisplayLabelItem());
				} else {
					query.select().add(Entity.NAME);
				}
				query.from(red.getName());

				List<Condition> condition = new ArrayList<Condition>();
				if (setting.getCondition() != null) {
					condition.add(new PreparedQuery(setting.getCondition()).condition(null));
				}
				// conditionsが空でない場合（親コンボが選択されている場合）条件を追加
				if (!conditions.isEmpty()) {
					condition.addAll(conditions);
				}
				if (!condition.isEmpty()) {
					And and = new And(condition);
					query.where(and);
				}

				String sortItem = setting.getSortItem();
				if (sortItem == null || sortItem.isEmpty()) {
					sortItem = Entity.OID;
				}
				SortType sortType = null;
				if (setting.getSortType() == null || setting.getSortType() == RefSortType.ASC) {
					sortType = SortType.ASC;
				} else {
					sortType = SortType.DESC;
				}
				query.order(new SortSpec(sortItem, sortType));

				final List<SimpleEntity> list = new ArrayList<SimpleEntity>();
				em.searchEntity(query, new Predicate<Entity>() {

					@Override
					public boolean test(Entity dataModel) {
						if (setting.getDisplayLabelItem() != null) {
							String displayLabelItem = setting.getDisplayLabelItem();
							// 表示ラベルとして扱うプロパティをNameに設定
							list.add(new SimpleEntity(dataModel.getOid(), dataModel.getValue(displayLabelItem)));
						} else {
							list.add(new SimpleEntity(dataModel));
						}
						return true;
					}
				});

				map.put(comboName, list);
			}
		} else {
			//選択済みの場合は下位のコンボの条件にする
			conditions.add(new Equals(propName + ".oid", val));
		}

		return conditions;
	}

	class SimpleEntity {
		private String oid;
		private String name;
		/**
		 * コンストラクタ
		 */
		public SimpleEntity(Entity entity) {
			this.oid = entity.getOid();
			this.name = entity.getName();
		}

		public SimpleEntity(String oid, String name) {
			this.oid = oid;
			this.name = name;
		}

		/**
		 * oidを取得します。
		 * @return oid
		 */
		public String getOid() {
			return oid;
		}
		/**
		 * oidを設定します。
		 * @param oid oid
		 */
		public void setOid(String oid) {
			this.oid = oid;
		}
		/**
		 * nameを取得します。
		 * @return name
		 */
		public String getName() {
			return name;
		}
		/**
		 * nameを設定します。
		 * @param name name
		 */
		public void setName(String name) {
			this.name = name;
		}

	}
}
