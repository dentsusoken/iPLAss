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

import org.iplass.mtp.entity.DeleteOption;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.ValidateError;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.entity.definition.properties.ReferenceType;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.editor.NestProperty;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor.EditPage;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor.ReferenceDisplayType;
import org.iplass.mtp.view.generic.element.property.PropertyBase;

/**
 * ネストテーブル用の登録処理
 *
 * @author lis3wg
 */
public abstract class NestTableReferenceRegistHandler extends ReferenceRegistHandlerBase {

	protected List<Entity> references;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static boolean canRegist(PropertyBase property, RegistrationPropertyBaseHandler propBaseHandler) {
		//非表示プロパティは対象外
		if (propBaseHandler.isHidden(property)) return false;

		//テーブルの場合のみ新規or更新
		if (!(propBaseHandler.getEditor(property) instanceof ReferencePropertyEditor)) return false;

		ReferencePropertyEditor editor = (ReferencePropertyEditor) propBaseHandler.getEditor(property);
		if (editor.getDisplayType() != ReferenceDisplayType.NESTTABLE) return false;

		//Viewモードの場合は編集画面では更新対象ではないので対象外
		if (editor.getEditPage() == EditPage.VIEW) return false;

		return true;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static ReferenceRegistHandler get(final RegistrationCommandContext context, final List<Entity> refs,
			EntityDefinition ed, final ReferenceProperty rp, final PropertyBase property, List<NestProperty> nestProperties,
			RegistrationPropertyBaseHandler propBaseHandler) {
		//登録可否は呼び元でチェック済み
		final List<String> updateProperties = getUpdateProperties(nestProperties, ed, (ReferencePropertyEditor) propBaseHandler.getEditor(property));

		if (rp.getMappedBy() == null || rp.getMappedBy().isEmpty()) {
			// 通常参照は登録前のみ
			return new NestTableReferenceRegistHandler() {
				@Override
				public void regist(ReferenceRegistHandlerFunction function, Entity inputEntity, Entity loadedEntity) {
					this.references = refs;
					List<ValidateError> errors = new ArrayList<>();
					if (checkMultiple(rp, errors)) {
						registReference(context, inputEntity, loadedEntity, property, rp, updateProperties, errors);
					}
					function.execute(errors);
				}
			};
		} else {
			// 被参照は登録後のみ
			return new NestTableReferenceRegistHandler() {
				@Override
				public void registMappedby(ReferenceRegistHandlerFunction function, Entity inputEntity, Entity loadedEntity) {
					this.references = refs;
					List<ValidateError> errors = new ArrayList<>();
					if (checkMultiple(rp, errors)) {
						registMappedbyReference(context, inputEntity, loadedEntity, property, rp, updateProperties, errors);
					}
					function.execute(errors);
				}
			};
		}
	}

	protected static List<String> getUpdateProperties(List<NestProperty> nestProperties, EntityDefinition ed, ReferencePropertyEditor editor) {
		List<String> updateProperties = getUpdateProperties(nestProperties, ed);
		if (StringUtil.isNotBlank(editor.getTableOrderPropertyName()) && !updateProperties.contains(editor.getTableOrderPropertyName())) {
			updateProperties.add(editor.getTableOrderPropertyName());
		}
		return updateProperties;
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
	protected void registReference(RegistrationCommandContext context, Entity inputEntity, Entity loadedEntity,
			PropertyBase property, ReferenceProperty rp, List<String> updateProperties, List<ValidateError> errors) {

		for (Entity entity : references) {
			errors.addAll(registReference(context, entity, updateProperties, rp.getName()));
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
	protected void registMappedbyReference(RegistrationCommandContext context, Entity inputEntity, Entity loadedEntity,
			PropertyBase property, ReferenceProperty rp, List<String> updateProperties, List<ValidateError> errors) {

		//被参照の場合はプロパティの値の方で元データを保持する
		String mappedBy = rp.getMappedBy();
		if (!updateProperties.contains(mappedBy)) {
			updateProperties.add(mappedBy);
		}

		//参照元のプロパティ定義
		String defName = rp.getObjectDefinitionName();
		ReferenceProperty mpd = (ReferenceProperty) edm.get(defName).getProperty(mappedBy);

		//参照元の登録
		List<Entity> registList = new ArrayList<>();
		List<Entity> deleteList = new ArrayList<>();
		difference(loadedEntity, rp, registList, deleteList);

		//新規・更新されたものは参照プロパティにEntityを追加
		for (Entity entity : registList) {
			setMappedByValue(context, loadedEntity, mappedBy, defName, mpd, entity);
			errors.addAll(registReference(context, entity, updateProperties, rp.getName()));
		}

		//削除されたものは参照プロパティからEntityを削除
		if (!deleteList.isEmpty()) {
			if (ReferenceType.COMPOSITION.equals(rp.getReferenceType())) {
				//親子関係があるものは直接削除
				for (Entity de : deleteList) {
					em.delete(de, new DeleteOption(false));
				}
			} else {
				for (Entity de : deleteList) {
					delMappedByValue(context, inputEntity, mappedBy, defName, mpd, de);
					List<String> delProperties = new ArrayList<>();
					delProperties.add(mappedBy);
					errors.addAll(registReference(context, de, delProperties, rp.getName()));
				}
			}
		}
	}

	/**
	 * 画面の入力データとロードしたデータから参照元の差分を取得します。
	 * @param entity 画面で入力したデータ
	 * @param loadedEntity ロードしたデータ
	 * @param pd 比較対象プロパティ
	 * @param registList 登録用Entityを保持するためのList
	 * @param deleteList 削除用Entityを保持するためのList
	 */
	protected void difference(Entity loadedEntity, PropertyDefinition pd, List<Entity> registList, List<Entity> deleteList) {
		List<Entity> list1 = new ArrayList<>();
		List<Entity> list2 = new ArrayList<>();

		if (pd.getMultiplicity() != 1) {
			//複数
			Entity[] in = references.toArray(new Entity[]{});
			if (in != null) list1.addAll(Arrays.asList(in));
			if (loadedEntity != null) {
				Entity[] load = loadedEntity.getValue(pd.getName());
				if (load != null) list2.addAll(Arrays.asList(load));
			}
		} else {
			//単一
			Entity in = !references.isEmpty() ? references.get(0) : null;
			if (in != null) list1.add(in);
			if (loadedEntity != null) {
				Entity load = loadedEntity.getValue(pd.getName());
				if (load != null) list2.add(load);
			}
		}

		for (Entity in : list1) {
			if (in.getOid() == null) {
				//OIDがないのは画面で入力されたデータ
				registList.add(in);
			}
		}
		for (Entity load : list2) {
			boolean match = false;
			Entity updEntity = null;
			for (Entity in : list1) {
				if (in.getOid() != null && load.getOid().equals(in.getOid())) {
					match = true;
					updEntity = in;
					break;
				}
			}
			if (match) {
				//画面・ロードしたデータ両方にあれば更新
				registList.add(updEntity);
			} else {
				//ロードしたデータにしかなければ削除
				deleteList.add(load);
			}
		}
	}

	/**
	 * 多重度以上の指定がないかチェック、多重度を越えてたらfalseを返す
	 * @param p
	 * @param errors
	 * @return
	 */
	protected boolean checkMultiple(ReferenceProperty p, List<ValidateError> errors) {
		//画面上の操作では起きないはず、多重度を減らした場合等
		if (p.getMultiplicity() != -1) {
			if (references.size() > p.getMultiplicity()) {
				ValidateError error = new ValidateError();
				error.setPropertyName(p.getName());
//				error.addErrorMessage(TemplateUtil.getResourceString("登録可能なデータは{0}件までです。", p.getMultiplicity()));
				error.addErrorMessage("登録可能なデータは"+ p.getMultiplicity() +"件までです。");
				errors.add(error);
				return false;
			}
		}
		return true;
	}
}
