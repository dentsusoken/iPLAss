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
import org.iplass.mtp.entity.GenericEntity;
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
 * @author Y.Ishida
 */
public abstract class NestTableReferenceRegistHandler extends ReferenceRegistHandlerBase {

	protected List<Entity> references;
	protected boolean isSpecifiedAsReference;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static boolean canRegist(PropertyBase property, RegistrationPropertyBaseHandler propBaseHandler) {
		// 非表示なら更新対象外
		if (!propBaseHandler.isDispProperty(property)) return false;

		// テーブルの場合のみ新規or更新
		if (!(propBaseHandler.getEditor(property) instanceof ReferencePropertyEditor)) return false;

		ReferencePropertyEditor editor = (ReferencePropertyEditor) propBaseHandler.getEditor(property);
		if (editor.getDisplayType() != ReferenceDisplayType.NESTTABLE) return false;

		// Viewモードの場合は編集画面では更新対象ではないので対象外
		if (editor.getEditPage() == EditPage.VIEW) return false;

		return true;
	}

	@SuppressWarnings({ "rawtypes" })
	public static ReferenceRegistHandler get(final RegistrationCommandContext context, final List<Entity> refs,
			EntityDefinition ed, final ReferenceProperty rp, final PropertyBase property,
			List<NestProperty> nestProperties, RegistrationPropertyBaseHandler propBaseHandler) {
		// 登録可否は呼び元でチェック済み
		return getInternal(context, refs, ed, rp, property, nestProperties, propBaseHandler, null);
	}

	@SuppressWarnings({ "rawtypes" })
	public static ReferenceRegistHandler get(final RegistrationCommandContext context, final List<Entity> refs,
			EntityDefinition ed, final ReferenceProperty rp, final PropertyBase property,
			List<NestProperty> nestProperties, RegistrationPropertyBaseHandler propBaseHandler,
			ReferenceRegistOption option) {
		// 登録可否は呼び元でチェック済み
		return getInternal(context, refs, ed, rp, property, nestProperties, propBaseHandler, option);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected static ReferenceRegistHandler getInternal(final RegistrationCommandContext context,
			final List<Entity> refs, final EntityDefinition ed, final ReferenceProperty rp, PropertyBase property,
			List<NestProperty> nestProperties, RegistrationPropertyBaseHandler propBaseHandler,
			ReferenceRegistOption option) {
		
		List<String> updateProperties = applyRegistOption(option, nestProperties, ed,
				(ReferencePropertyEditor) propBaseHandler.getEditor(property), refs);

		if (rp.getMappedBy() == null || rp.getMappedBy().isEmpty()) {
			// 通常参照は登録前のみ
			return new NestTableReferenceRegistHandler() {
				@Override
				public void regist(ReferenceRegistHandlerFunction function, Entity inputEntity, Entity loadedEntity) {
					this.references = refs;
					this.isSpecifiedAsReference = (option == null) ? true : option.isSpecifiedAsReference();
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
				public void registMappedby(ReferenceRegistHandlerFunction function, Entity inputEntity,
						Entity loadedEntity) {
					this.references = refs;
					this.isSpecifiedAsReference = (option == null) ? true : option.isSpecifiedAsReference();
					List<ValidateError> errors = new ArrayList<>();
					if (checkMultiple(rp, errors)) {
						registMappedbyReference(context, inputEntity, loadedEntity, property, rp, updateProperties,
								errors);
					}
					function.execute(errors);
				}
			};
		}
	}

	/** カスタム登録処理によるNestEntityの更新制御適用 */
	protected static List<String> applyRegistOption(ReferenceRegistOption option, List<NestProperty> nestProperties,
			EntityDefinition ed, ReferencePropertyEditor editor, List<Entity> refs) {
		List<String> updateProperties;
		
		// 新規作成、単一プロパティに対する一括更新（BulkUpdate）の場合は制御対象外
		if (option == null) {
			updateProperties = getUpdateProperties(nestProperties, ed, editor);
			return updateProperties;
		}

		// isSpecifyAllPropertiesがfalseの場合
		if (!option.isSpecifyAllProperties()) {
			updateProperties = new ArrayList<String>();
			// 標準の更新項目を追加
			updateProperties.addAll(getUpdateProperties(nestProperties, ed, editor));
			// 指定されているプロパティを更新項目に追加。
			for (String prop : option.getSpecifiedUpdateNestProperties()) {
				if (!updateProperties.contains(prop)) {
					updateProperties.add(prop);
				}
			}
			return updateProperties;
		}

		// Reference項目として更新可能且つ、NestされたEntityの個々のプロパティ指定がない場合
		// データの追加、削除は可能。既存の参照先Entityは全てのプロパティ更新可。
		if (option.isSpecifiedAsReference() && option.getSpecifiedUpdateNestProperties().isEmpty()) {
			updateProperties = getUpdateProperties(nestProperties, ed, editor);
			return updateProperties;
		}
		
		// 以下のパターンでは、既存のEntityは指定されたプロパティのみ更新可能。
		// ネストテーブルの表示順プロパティの指定があった場合、更新可能項目として必ず追加
		updateProperties = option.getSpecifiedUpdateNestProperties();
		addTableOrderProperty(updateProperties, editor);
		
		// Reference項目として更新可能且つ、NestされたEntityの個々のプロパティに対して、更新対象の指定がある場合
		// データの追加、削除は可能。新規Entityの更新不可項目はnullに設定。
		if (option.isSpecifiedAsReference()) {
			for (Entity entity : refs) {
				if (entity.getOid() == null) {
					Entity newEntity = new GenericEntity();
					newEntity.setDefinitionName(entity.getDefinitionName());
					for (String prop : option.getSpecifiedUpdateNestProperties()) {
						newEntity.setValue(prop, entity.getValue(prop));
					}
					refs.set(refs.indexOf(entity), newEntity);
				}
			}
		} else {
			// Reference項目として更新不可且つ、NestされたEntityの個々のプロパティに対して、更新対象の指定がある場合
			// データの追加、削除は不可能。
			for (Entity entity : refs.toArray(new Entity[refs.size()])) {
				if (entity.getOid() == null) {
					refs.remove(refs.indexOf(entity));
				}
			}
		}
		
		return updateProperties;
	}

	/** 標準の更新項目を返却 */
	protected static List<String> getUpdateProperties(List<NestProperty> nestProperties, EntityDefinition ed,
			ReferencePropertyEditor editor) {
		List<String> updateProperties = getUpdateProperties(nestProperties, ed);
		addTableOrderProperty(updateProperties, editor);
		return updateProperties;
	}

	/** ネストテーブルの表示順プロパティを追加 */
	protected static void addTableOrderProperty(List<String> updateProperties, ReferencePropertyEditor editor) {
		if (StringUtil.isNotBlank(editor.getTableOrderPropertyName())
				&& !updateProperties.contains(editor.getTableOrderPropertyName())) {
			updateProperties.add(editor.getTableOrderPropertyName());
		}
	}

	/**
	 * 参照データ登録処理を行います。
	 * 
	 * @param context          コンテキスト
	 * @param inputEntity      画面で入力したデータ
	 * @param loadedEntity     ロードしたデータ
	 * @param property         プロパティの画面定義
	 * @param rp               参照プロパティ定義
	 * @param updateProperties 更新対象のプロパティ
	 * @param errors           入力エラーリスト
	 */
	protected void registReference(RegistrationCommandContext context, Entity inputEntity, Entity loadedEntity,
			PropertyBase property, ReferenceProperty rp, List<String> updateProperties, List<ValidateError> errors) {
		for (Entity entity : references) {
			errors.addAll(registReference(context, entity, updateProperties, rp.getName()));
		}
	}

	/**
	 * 参照データ(被参照)登録処理を行います。
	 * 
	 * @param context          コンテキスト
	 * @param inputEntity      画面で入力したデータ
	 * @param loadedEntity     ロードしたデータ
	 * @param property         プロパティの画面定義
	 * @param rp               参照プロパティ定義
	 * @param updateProperties 更新対象のプロパティ
	 * @param errors           入力エラーリスト
	 */
	protected void registMappedbyReference(RegistrationCommandContext context, Entity inputEntity, Entity loadedEntity,
			PropertyBase property, ReferenceProperty rp, List<String> updateProperties, List<ValidateError> errors) {

		// 被参照の場合はプロパティの値の方で元データを保持する
		String mappedBy = rp.getMappedBy();
		if (!updateProperties.contains(mappedBy)) {
			updateProperties.add(mappedBy);
		}

		// 参照元のプロパティ定義
		String defName = rp.getObjectDefinitionName();
		ReferenceProperty mpd = (ReferenceProperty) edm.get(defName).getProperty(mappedBy);

		// 参照元の登録
		List<Entity> registList = new ArrayList<>();
		List<Entity> deleteList = new ArrayList<>();
		diffMappedbyReference(loadedEntity, rp, registList, deleteList);

		// 新規・更新されたものは参照プロパティにEntityを追加
		for (Entity entity : registList) {
			setMappedByValue(context, loadedEntity, mappedBy, defName, mpd, entity);
			errors.addAll(registReference(context, entity, updateProperties, rp.getName()));
		}

		// 削除されたものは参照プロパティからEntityを削除
		// カスタム登録処理でReference項目として除外された場合は、スキップ。
		if (!deleteList.isEmpty() && isSpecifiedAsReference) {
			if (ReferenceType.COMPOSITION.equals(rp.getReferenceType())) {
				// 親子関係があるものは直接削除
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
	 * 
	 * @param entity        画面で入力したデータ
	 * @param loadedEntity  ロードしたデータ
	 * @param pd            比較対象プロパティ
	 * @param registRefList 登録用Entityを保持するためのList
	 * @param deleteRefList 削除用Entityを保持するためのList
	 */
	protected void diffMappedbyReference(Entity loadedEntity, PropertyDefinition pd, List<Entity> registRefList,
			List<Entity> deleteRefList) {

		List<Entity> storedRefList = new ArrayList<>();

		if (pd.getMultiplicity() != 1) {
			// 複数
			Entity[] in = references.toArray(new Entity[] {});
			if (in != null) {
				registRefList.addAll(Arrays.asList(in));
			}

			if (loadedEntity != null) {
				Entity[] load = loadedEntity.getValue(pd.getName());
				if (load != null) {
					storedRefList.addAll(Arrays.asList(load));
				}
			}
		} else {
			// 単一
			Entity in = !references.isEmpty() ? references.get(0) : null;
			if (in != null) {
				registRefList.add(in);
			}

			if (loadedEntity != null) {
				Entity load = loadedEntity.getValue(pd.getName());
				if (load != null) {
					storedRefList.add(load);
				}
			}
		}

		for (Entity stored : storedRefList) {
			boolean match = registRefList.stream().anyMatch(regist -> {
				return regist.getOid() != null && stored.getOid().equals(regist.getOid());
			});
			if (!match) {
				// ロードしたデータにしかなければ削除
				deleteRefList.add(stored);
			}
		}
	}

	/**
	 * 多重度以上の指定がないかチェック、多重度を越えてたらfalseを返す
	 * 
	 * @param p
	 * @param errors
	 * @return
	 */
	protected boolean checkMultiple(ReferenceProperty p, List<ValidateError> errors) {
		// 画面上の操作では起きないはず、多重度を減らした場合等
		if (p.getMultiplicity() != -1) {
			if (references.size() > p.getMultiplicity()) {
				ValidateError error = new ValidateError();
				error.setPropertyName(p.getName());
//				error.addErrorMessage(TemplateUtil.getResourceString("登録可能なデータは{0}件までです。", p.getMultiplicity()));
				error.addErrorMessage("登録可能なデータは" + p.getMultiplicity() + "件までです。");
				errors.add(error);
				return false;
			}
		}
		return true;
	}
}
