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
import org.iplass.mtp.view.generic.element.section.ReferenceSection;

/**
 * 参照セクション用の登録処理
 *
 * @author lis3wg
 * @author Y.Ishida
 */
public abstract class ReferenceSectionReferenceRegistHandler extends ReferenceRegistHandlerBase {

	protected List<ReferenceSectionValue> references;

	public static boolean canRegist(ReferenceSectionPropertyItem property) {
		// 全てのセクションが非表示なら対象外
		if (!property.isDispFlag() || property.isHideDetail()) return false;

		// 個別の登録可否は別途チェック
		return true;
	}

	public static ReferenceRegistHandler get(final DetailCommandContext context, final List<ReferenceSectionValue> refs,
			final EntityDefinition ed, final ReferenceProperty rp, final PropertyItem property) {
		return getInternal(context, refs, ed, rp, property, null);
	}

	public static ReferenceRegistHandler get(final DetailCommandContext context, final List<ReferenceSectionValue> refs,
			final EntityDefinition ed, final ReferenceProperty rp, final PropertyItem property,
			ReferenceRegistOption option) {
		return getInternal(context, refs, ed, rp, property, option);
	}

	private static ReferenceRegistHandler getInternal(final DetailCommandContext context,
			final List<ReferenceSectionValue> refs, final EntityDefinition ed, final ReferenceProperty rp,
			final PropertyItem property, ReferenceRegistOption option) {
		if (rp.getMappedBy() == null || rp.getMappedBy().isEmpty()) {
			// 通常参照は登録前のみ
			return new ReferenceSectionReferenceRegistHandler() {
				@Override
				public void regist(ReferenceRegistHandlerFunction function, Entity inputEntity, Entity loadedEntity) {
					this.references = refs;
					this.registOption = option;
					List<ValidateError> errors = new ArrayList<>();
					registReference(context, inputEntity, loadedEntity, ed, property, rp, errors);
					function.execute(errors);
				}
			};
		} else {
			// 被参照は登録後のみ
			return new ReferenceSectionReferenceRegistHandler() {
				@Override
				public void registMappedby(ReferenceRegistHandlerFunction function, Entity inputEntity,
						Entity loadedEntity) {
					this.references = refs;
					this.registOption = option;
					List<ValidateError> errors = new ArrayList<>();
					registMappedbyReference(context, inputEntity, loadedEntity, ed, property, rp, errors);
					function.execute(errors);
				}
			};
		}
	}

	/**
	 * 参照データ登録処理を行います。
	 *
	 * @param context      コンテキスト
	 * @param inputEntity  画面で入力したデータ
	 * @param loadedEntity ロードしたデータ
	 * @param ed           エンティティ定義
	 * @param property     プロパティの画面定義
	 * @param rp           参照プロパティ定義
	 * @param errors       入力エラーリスト
	 */
	protected void registReference(DetailCommandContext context, Entity inputEntity, Entity loadedEntity,
			EntityDefinition ed, PropertyItem property, ReferenceProperty rp, List<ValidateError> errors) {

		if (rp.getMultiplicity() != 1) {
			// 画面のデータには表示されてたEntityしか設定されてないので、ロードしたデータと新規分をマージして上書き
			// →設定しなおさないと表示されてないデータが参照から外れる
			// 多重度1の場合は1件だけなので、画面のデータがそのまま登録されるから問題なし
			List<Entity> usList = references.stream().filter(r -> r.getEntity().getOid() == null).map(r -> r.getEntity()).collect(Collectors.toList());
			List<Entity> loadedList = new ArrayList<>();
			if (loadedEntity != null && loadedEntity.getValue(rp.getName()) != null) {
				Entity[] ary = loadedEntity.getValue(rp.getName());
				loadedList.addAll(Arrays.asList(ary));
			}
			loadedList.addAll(usList);
			inputEntity.setValue(rp.getName(), loadedList.toArray(new Entity[] {}));
		}

		for (ReferenceSectionValue val : references) {
			// 非表示のものは登録しない
			if (!EntityViewUtil.isDisplayElement(context.getDefinitionName(), val.getSection().getElementRuntimeId(),
					OutputType.EDIT, inputEntity) || val.getSection().isHideDetail()) {
				continue;
			}

			// セクション毎にnestpropertyが違う可能性があるので、それぞれ更新オプションを適用
			// 参照セクションの場合、registOptionがnullになるのは新規作成のみ。新規作成時は、更新制御の対象外。
			List<String> updateProperties = new ArrayList<>();
			if (registOption != null) {
				applyRegistOption(val, updateProperties, ed);
			}
			setIndex(ed, val);

			setForceUpdate(val.getSection().isForceUpadte());
			errors.addAll(registReference(context, val.getEntity(), updateProperties, rp.getName()));
		}
	}

	/**
	 * 参照データ(被参照)登録処理を行います。
	 *
	 * @param context      コンテキスト
	 * @param inputEntity  画面で入力したデータ
	 * @param loadedEntity ロードしたデータ
	 * @param ed           エンティティ定義
	 * @param property     プロパティの画面定義
	 * @param rp           参照プロパティ定義
	 * @param errors       入力エラーリスト
	 */
	protected void registMappedbyReference(DetailCommandContext context, Entity inputEntity, Entity loadedEntity,
			EntityDefinition ed, PropertyItem property, ReferenceProperty rp, List<ValidateError> errors) {

		// 参照元のプロパティ定義
		String defName = rp.getObjectDefinitionName();
		String mappedBy = rp.getMappedBy();
		ReferenceProperty mpd = (ReferenceProperty) edm.get(defName).getProperty(mappedBy);

		List<UpdateSet> usList = new ArrayList<>();
		for (ReferenceSectionValue val : references) {
			// 非表示のものは登録しない
			if (!EntityViewUtil.isDisplayElement(context.getDefinitionName(), val.getSection().getElementRuntimeId(),
					OutputType.EDIT, inputEntity) || val.getSection().isHideDetail()) {
				continue;
			}

			// セクション毎にnestpropertyが違う可能性があるので、それぞれ更新オプションを適用
			// 参照セクションの場合、registOptionがnullになるのは新規作成のみ。新規作成時は、更新制御の対象外。
			List<String> updateProperties = new ArrayList<>();
			if (registOption != null) {
				applyRegistOption(val, updateProperties, ed);
			}
			setIndex(ed, val);

			// 被参照の場合はプロパティの値の方で元データを保持するため更新対象に追加
			if (!updateProperties.contains(mappedBy)) {
				updateProperties.add(mappedBy);
			}
			usList.add(new UpdateSet(val.getEntity(), updateProperties, val.getSection()));
		}

		// 参照元の登録
		List<UpdateSet> registList = new ArrayList<>();
		List<Entity> deleteList = new ArrayList<>();
		difference(usList, loadedEntity, rp, registList, deleteList);

		// 新規・更新されたものは参照プロパティにEntityを追加
		for (UpdateSet us : registList) {
			setMappedByValue(context, loadedEntity, mappedBy, defName, mpd, us.section, us.entity);
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

	public void setIndex(EntityDefinition ed, ReferenceSectionValue val) {
		if (val.getIndex() != null && StringUtil.isNotBlank(val.getSection().getOrderPropName())) {
			// インデックスがあったらプロパティに設定
			// 参照セクションではインデックスは変わらないはずなので更新対象には含めない
			PropertyDefinition pd = ed.getProperty(val.getSection().getOrderPropName());
			if (pd != null && val.getEntity() != null) {
				val.getEntity().setValue(val.getSection().getOrderPropName(),
						ConvertUtil.convert(pd.getJavaType(), val.getIndex()));
			}
		}
	}

	/** カスタム登録処理によるNestEntityの更新制御適用 */
	protected void applyRegistOption(ReferenceSectionValue val, List<String> updateProperties, EntityDefinition ed) {

		// 更新対象のプロパティ設定
		getUpdatePropertiesWithOption(updateProperties, val.getSection().getProperties(), ed);

		if (registOption.isSpecifiedAsReference() && !registOption.getSpecifiedUpdateNestProperties().isEmpty()) {
			// Reference項目として更新可能且つ、NestされたEntityの個々のプロパティに対して、更新対象の指定がある場合
			// データの追加は可能。新規Entityの更新不可項目はnullに設定。
			Entity entity = val.getEntity();
			if (entity.getOid() == null) {
				for (PropertyDefinition pd : ed.getPropertyList()) {
					boolean specified = false;
					for (String specifiedProperty : registOption.getSpecifiedUpdateNestProperties()) {
						if (pd.getName().equals(specifiedProperty)) {
							specified = true;
							break;
						}
					}
					if (!specified) {
						entity.setValue(pd.getName(), null);
					}
				}
			}
		} else if (!registOption.isSpecifiedAsReference()
				&& !registOption.getSpecifiedUpdateNestProperties().isEmpty()) {
			// Reference項目として更新不可且つ、NestされたEntityの個々のプロパティに対して、更新対象の指定がある場合
			// データの追加は不可能。
			if (val.getEntity().getOid() == null) {
				val.setEntity(null);
			}
		}
	}

	/**
	 * 画面の入力データとロードしたデータから参照元の差分を取得します。
	 *
	 * @param entity       画面で入力したデータ
	 * @param loadedEntity ロードしたデータ
	 * @param pd           比較対象プロパティ
	 * @param registList   登録用Entityを保持するためのList
	 * @param deleteList   削除用Entityを保持するためのList
	 */
	protected void difference(List<UpdateSet> usList, Entity loadedEntity, PropertyDefinition pd,
			List<UpdateSet> registList, List<Entity> deleteList) {
		List<UpdateSet> list1 = new ArrayList<>();
		List<Entity> list2 = new ArrayList<>();

		if (pd.getMultiplicity() != 1) {
			// 複数
			if (usList != null) list1.addAll(usList);
			if (loadedEntity != null) {
				Entity[] load = loadedEntity.getValue(pd.getName());
				if (load != null) list2.addAll(Arrays.asList(load));
			}
		} else {
			// 単一
			if (usList != null) list1.addAll(usList);
			if (loadedEntity != null) {
				Entity load = loadedEntity.getValue(pd.getName());
				if (load != null) list2.add(load);
			}
		}

		for (UpdateSet in : list1) {
			// カスタム登録処理なし or カスタム登録処理で参照先エンティティへのデータ更新を許容する場合
			if (in.entity != null) {
				if (in.entity.getOid() == null) {
					// OIDがないのは画面で入力されたデータ
					registList.add(in);
				}
			}
		}
		for (Entity load : list2) {
			boolean match = false;
			UpdateSet usSet = null;
			for (UpdateSet in : list1) {
				// カスタム登録処理なし or カスタム登録処理で参照先エンティティへのデータ更新を許容する場合
				if (in.entity != null) {
					if (in.entity.getOid() != null && load.getOid().equals(in.entity.getOid())) {
						match = true;
						usSet = in;
						break;
					}
				}
			}
			if (match) {
				// 画面・ロードしたデータ両方にあれば更新
				registList.add(usSet);
			} else {
				// ロードしたデータにしかなければ削除
				deleteList.add(load);
			}
		}
	}

	private class UpdateSet {
		Entity entity;
		List<String> updateProperty;
		ReferenceSection section;
		public UpdateSet(Entity entity, List<String> updateProperty, ReferenceSection section) {
			this.entity = entity;
			this.updateProperty = updateProperty;
			this.section = section;
		}
	}

}
