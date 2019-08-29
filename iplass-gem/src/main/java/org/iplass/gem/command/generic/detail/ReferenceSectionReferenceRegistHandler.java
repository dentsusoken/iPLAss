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

package org.iplass.gem.command.generic.detail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.iplass.gem.command.generic.detail.DetailCommandContext.ReferenceSectionPropertyItem;
import org.iplass.gem.command.generic.detail.DetailCommandContext.ReferenceSectionValue;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.ValidateError;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.impl.util.ConvertUtil;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.EntityViewUtil;
import org.iplass.mtp.view.generic.OutputType;
import org.iplass.mtp.view.generic.element.property.PropertyItem;

/**
 * 参照セクション用の登録処理
 *
 * @author lis3wg
 */
public abstract class ReferenceSectionReferenceRegistHandler extends ReferenceRegistHandlerBase {

	protected List<ReferenceSectionValue> references;

	public static boolean canRegist(ReferenceSectionPropertyItem property) {
		//全てのセクションが非表示なら対象外
		if (!property.isDispFlag() || property.isHideDetail()) return false;

		//個別の登録可否は別途チェック
		return true;
	}

	public static ReferenceRegistHandler get(final DetailCommandContext context, final List<ReferenceSectionValue> refs,
			final EntityDefinition ed, final ReferenceProperty rp, final PropertyItem property) {

		if (rp.getMappedBy() == null || rp.getMappedBy().isEmpty()) {
			// 通常参照は登録前のみ
			return new ReferenceSectionReferenceRegistHandler() {
				@Override
				public void regist(ReferenceRegistHandlerFunction function, Entity inputEntity, Entity loadedEntity) {
					this.references = refs;
					List<ValidateError> errors = new ArrayList<>();
					registReference(context, inputEntity, loadedEntity, ed, property, rp, errors);
					function.execute(errors);
				}
			};
		} else {
			// 被参照は登録後のみ
			return new ReferenceSectionReferenceRegistHandler() {
				@Override
				public void registMappedby(ReferenceRegistHandlerFunction function, Entity inputEntity, Entity loadedEntity) {
					this.references = refs;
					List<ValidateError> errors = new ArrayList<>();
					registMappedbyReference(context, inputEntity, loadedEntity, ed, property, rp, errors);
					function.execute(errors);
				}
			};
		}
	}

	/**
	 * 参照データ登録処理を行います。
	 * @param context コンテキスト
	 * @param inputEntity 画面で入力したデータ
	 * @param loadedEntity ロードしたデータ
	 * @param property プロパティの画面定義
	 * @param rp 参照プロパティ定義
	 * @param updateProperties 更新対象プロパティ
	 * @param errors 入力エラーリスト
	 */
	protected void registReference(DetailCommandContext context,
			Entity inputEntity, Entity loadedEntity,
			EntityDefinition ed, PropertyItem property, ReferenceProperty rp,
			List<ValidateError> errors) {

		if (rp.getMultiplicity() != 1) {
			//画面のデータには表示されてたEntityしか設定されてないので、ロードしたデータと新規分をマージして上書き
			//→設定しなおさないと表示されてないデータが参照から外れる
			//多重度1の場合は1件だけなので、画面のデータがそのまま登録されるから問題なし
			List<Entity> usList = references.stream().filter(r -> r.getEntity().getOid() == null).map(r -> r.getEntity()).collect(Collectors.toList());
			List<Entity> loadedList = new ArrayList<>();
			if (loadedEntity.getValue(rp.getName()) != null) {
				Entity[] ary = loadedEntity.getValue(rp.getName());
				loadedList.addAll(Arrays.asList(ary));
			}
			loadedList.addAll(usList);
			inputEntity.setValue(rp.getName(), loadedList.toArray(new Entity[]{}));
		}

		for (ReferenceSectionValue val : references) {
			//非表示のものは登録しない
			if (!EntityViewUtil.isDisplayElement(context.getDefinitionName(), val.getSection().getElementRuntimeId(), OutputType.EDIT)
					|| val.getSection().isHideDetail()) {
				continue;
			}

			setIndex(ed, val);

			List<String> updateProperties = getUpdateProperties(val.getSection().getProperties(), ed);
			setForceUpdate(val.getSection().isForceUpadte());
			errors.addAll(registReference(context, val.getEntity(), updateProperties, rp.getName()));
		}
	}

	public void setIndex(EntityDefinition ed, ReferenceSectionValue val) {
		if (val.getIndex() != null && StringUtil.isNotBlank(val.getSection().getOrderPropName())) {
			//インデックスがあったらプロパティに設定
			//参照セクションではインデックスは変わらないはずなので更新対象には含めない
			PropertyDefinition pd = ed.getProperty(val.getSection().getOrderPropName());
			if (pd != null) {
				val.getEntity().setValue(val.getSection().getOrderPropName(),
						ConvertUtil.convert(pd.getJavaType(), val.getIndex()));
			}
		}
	}

	/**
	 * 参照データ(被参照)登録処理を行います。
	 * @param context コンテキスト
	 * @param inputEntity 画面で入力したデータ
	 * @param loadedEntity ロードしたデータ
	 * @param property プロパティの画面定義
	 * @param rp 参照プロパティ定義
	 * @param updateProperties 更新対象プロパティ
	 * @param errors 入力エラーリスト
	 */
	protected void registMappedbyReference(DetailCommandContext context,
			Entity inputEntity, Entity loadedEntity,
			EntityDefinition ed, PropertyItem property, ReferenceProperty rp,
			List<ValidateError> errors) {

		//参照元のプロパティ定義
		String defName = rp.getObjectDefinitionName();
		String mappedBy = rp.getMappedBy();
		ReferenceProperty mpd = (ReferenceProperty) edm.get(defName).getProperty(mappedBy);

		List<UpdateSet> usList = new ArrayList<>();
		for (ReferenceSectionValue val : references) {
			//非表示のものは登録しない
			if (!EntityViewUtil.isDisplayElement(context.getDefinitionName(), val.getSection().getElementRuntimeId(), OutputType.EDIT)
					|| val.getSection().isHideDetail()) {
				continue;
			}

			setIndex(ed, val);

			//セクション毎にnestpropertyが違う可能性があるので、それぞれ更新対象を生成する
			List<String> updateProperties = getUpdateProperties(val.getSection().getProperties(), ed);

			//被参照の場合はプロパティの値の方で元データを保持するため更新対象に追加
			if (!updateProperties.contains(mappedBy)) {
				updateProperties.add(mappedBy);
			}
			usList.add(new UpdateSet(val.getEntity(), updateProperties));
		}

		//参照元の登録
		List<UpdateSet> registList = new ArrayList<>();
		List<Entity> deleteList = new ArrayList<>();
		difference(usList, loadedEntity, rp, registList, deleteList);

		//新規・更新されたものは参照プロパティにEntityを追加
		for (UpdateSet us : registList) {
			setMappedByValue(context, loadedEntity, mappedBy, defName, mpd, us.entity);
			errors.addAll(registReference(context, us.entity, us.updateProperty, rp.getName()));
		}

		// 参照セクションでは削除しない
		// →表示されない参照もあり、そこが削除の差分として出てしまう

//		//削除されたものは参照プロパティからEntityを削除
//		Entity[] deleteEntity = deleteList.toArray(new Entity[deleteList.size()]);
//		if (deleteEntity.length > 0) {
//			if (ReferenceType.COMPOSITION.equals(rp.getReferenceType())) {
//				//親子関係があるものは直接削除
//				for (Entity de : deleteList) {
//					em.delete(de, new DeleteOption(false));
//				}
//			} else {
//				for (Entity de : deleteList) {
//					delMappedByValue(context, inputEntity, mappedBy, defName, mpd, de);
//					List<String> delProperties = new ArrayList<String>();
//					delProperties.add(mappedBy);
//					errors.addAll(registRefEntity(context, de, delProperties, rp.getName()));
//				}
//			}
//		}
	}

	/**
	 * 画面の入力データとロードしたデータから参照元の差分を取得します。
	 * @param entity 画面で入力したデータ
	 * @param loadedEntity ロードしたデータ
	 * @param pd 比較対象プロパティ
	 * @param registList 登録用Entityを保持するためのList
	 * @param deleteList 削除用Entityを保持するためのList
	 */
	protected void difference(List<UpdateSet> usList, Entity loadedEntity, PropertyDefinition pd,
			List<UpdateSet> registList, List<Entity> deleteList) {
		List<UpdateSet> list1 = new ArrayList<>();
		List<Entity> list2 = new ArrayList<>();

		if (pd.getMultiplicity() != 1) {
			//複数
			if (usList != null) list1.addAll(usList);
			if (loadedEntity != null) {
				Entity[] load = loadedEntity.getValue(pd.getName());
				if (load != null) list2.addAll(Arrays.asList(load));
			}
		} else {
			//単一
			if (usList != null) list1.addAll(usList);
			if (loadedEntity != null) {
				Entity load = loadedEntity.getValue(pd.getName());
				if (load != null) list2.add(load);
			}
		}

		for (UpdateSet in : list1) {
			if (in.entity.getOid() == null) {
				//OIDがないのは画面で入力されたデータ
				registList.add(in);
			}
		}
		for (Entity load : list2) {
			boolean match = false;
			UpdateSet usSet = null;
			for (UpdateSet in : list1) {
				if (in.entity.getOid() != null && load.getOid().equals(in.entity.getOid())) {
					match = true;
					usSet = in;
					break;
				}
			}
			if (match) {
				//画面・ロードしたデータ両方にあれば更新
				registList.add(usSet);
			} else {
				//ロードしたデータにしかなければ削除
				deleteList.add(load);
			}
		}
	}

	private class UpdateSet {
		Entity entity;
		List<String> updateProperty;
		public UpdateSet(Entity entity, List<String> updateProperty) {
			this.entity = entity;
			this.updateProperty = updateProperty;
		}
	}
}
