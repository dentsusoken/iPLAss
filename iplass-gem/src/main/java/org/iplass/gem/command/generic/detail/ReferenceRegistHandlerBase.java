/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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

import org.iplass.gem.command.Constants;
import org.iplass.gem.command.GemResourceBundleUtil;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.EntityValidationException;
import org.iplass.mtp.entity.InsertOption;
import org.iplass.mtp.entity.LoadOption;
import org.iplass.mtp.entity.TargetVersion;
import org.iplass.mtp.entity.UpdateOption;
import org.iplass.mtp.entity.ValidateError;
import org.iplass.mtp.entity.ValidateResult;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.view.generic.LoadEntityContext;
import org.iplass.mtp.view.generic.LoadEntityInterrupter.LoadType;
import org.iplass.mtp.view.generic.editor.JoinPropertyEditor;
import org.iplass.mtp.view.generic.editor.NestProperty;
import org.iplass.mtp.view.generic.editor.RangePropertyEditor;
import org.iplass.mtp.view.generic.element.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ReferenceRegistHandlerBase implements ReferenceRegistHandler {

	private static Logger logger = LoggerFactory.getLogger(ReferenceRegistHandlerBase.class);

	protected EntityDefinitionManager edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
	protected EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);
	protected ReferenceRegistOption registOption;
	
	private boolean forceUpdate;

	/**
	 * 参照Entityをロードします。
	 * @param context コンテキスト
	 * @param entity Entity
	 * @param loadOption ロード時のオプション
	 * @param property プロパティ定義
	 * @param element エレメント
	 * @return Entity
	 */
	protected Entity loadReference(RegistrationCommandContext context, final Entity entity, LoadOption loadOption, ReferenceProperty property, Element element) {
		Entity e = null;
		if (entity.getOid() != null) {
			final LoadEntityContext leContext = context.getLoadEntityInterrupterHandler().beforeLoadReference(entity.getDefinitionName(), loadOption, property, element, LoadType.UPDATE);
			if (leContext.isDoPrivileged()) {
				//特権実行
				e = AuthContext.doPrivileged(() -> {
					//データ取得
					return em.load(entity.getOid(), entity.getVersion() ,entity.getDefinitionName(), leContext.getLoadOption());
				});
			} else {
				//データ取得
				e = em.load(entity.getOid(), entity.getVersion() ,entity.getDefinitionName(), leContext.getLoadOption());
			}
			context.getLoadEntityInterrupterHandler().afterLoadReference(e, loadOption, property, element, LoadType.UPDATE);
		}
		return e;
	}


	/**
	 * 参照型データの登録を行います。
	 * @param context コンテキスト
	 * @param entity 参照データ
	 * @param updateProperties 更新対象プロパティ
	 * @param propertyName 参照元のプロパティ名
	 * @return 入力エラーリスト
	 */
	@SuppressWarnings("unchecked")
	protected List<ValidateError> registReference(RegistrationCommandContext context, Entity entity,
			List<String> updateProperties, String propertyName) {
		List<ValidateError> errors = new ArrayList<ValidateError>();

		List<ValidateError> occurredErrors = (List<ValidateError>) context.getAttribute(Constants.VALID_ERROR_LIST);

		if (entity != null) {
			if (occurredErrors.isEmpty()) {
				try {
					if (entity.getOid() == null) {
						InsertOption option = new InsertOption();
						option.setLocalized(context.isLocalizationData());
						em.insert(entity, option);
						//参照プロパティのエンティティに権限が付いてるとうまく行かないケースがある
						//→逆参照のエンティティのプロパティが権限の条件になっている等

						//ロールバック時にIDを削除するためのフラグ
						entity.setValue(Constants.DELETE_OID_FLAG, Boolean.TRUE);
					} else {
						//Command内では常にバージョンを保持し、特定バージョンで更新する
						UpdateOption option = new UpdateOption(false, TargetVersion.SPECIFIC);
						option.setForceUpdate(forceUpdate);
						option.setUpdateProperties(updateProperties);
						option.setPurgeCompositionedEntity(context.isPurgeCompositionedEntity());
						option.setLocalized(context.isLocalizationData());
						em.update(entity, option);
					}
				} catch (EntityValidationException e) {
					if (logger.isDebugEnabled()) {
						logger.debug(e.getMessage(), e);
					}
					//プロパティ名書き換え
					if (e.getValidateResults() != null && !e.getValidateResults().isEmpty()) {
						for (ValidateError ve : e.getValidateResults()) {
							Integer refIndex = entity.getValue(Constants.REF_INDEX);
							String nestPropertyName = propertyName + "." + ve.getPropertyName();
							if (refIndex != null) {
								nestPropertyName = propertyName + "[" + refIndex + "]." + ve.getPropertyName();
							}
							ve.setPropertyName(nestPropertyName);
						}
					}
					errors.addAll(e.getValidateResults());
				}
			} else {
				//既にエラーが発生してたら入力チェックのみ実施
				ValidateResult vr = null;
				if (entity.getOid() == null) {
					vr = em.validate(entity);
				} else {
					vr = em.validate(entity, updateProperties);
				}
				if (vr != null && vr.hasError()) {
					for (ValidateError ve : vr.getErrors()) {
						Integer refIndex = entity.getValue(Constants.REF_INDEX);
						String nestPropertyName = propertyName + "." + ve.getPropertyName();
						if (refIndex != null) {
							nestPropertyName = propertyName + "[" + refIndex + "]." + ve.getPropertyName();
						}
						ve.setPropertyName(nestPropertyName);
					}
					errors.addAll(vr.getErrors());
				}
			}
		}
		return errors;
	}

	/**
	 * 被参照のEntityを参照元に設定します。
	 * @param コンテキスト
	 * @param entity 画面で入力したデータ
	 * @param mappedBy 被参照項目のプロパティ
	 * @param defName 参照先のEntity定義名
	 * @param rpd 参照プロパティ定義
	 * @param element エレメント
	 * @param refEntity 参照元Entity
	 */
	protected void setMappedByValue(RegistrationCommandContext context, Entity entity, String mappedBy, String defName,
			ReferenceProperty rpd, Element element, Entity refEntity) {
		if (rpd.getMultiplicity() != 1) {
			//参照が多重の場合
			if (refEntity.getOid() == null) {
				//新規の場合はそのまま配列で
				refEntity.setValue(mappedBy, new Entity[] { entity });
			} else {
				//追加の場合は登録済みのデータに追加する
				//本体は参照プロパティとして設定されてるはずなので、被参照のロードは不要
				Entity tmp = loadReference(context, refEntity, new LoadOption(true, false), rpd, element);
				if (tmp != null && tmp.getValue(mappedBy) != null) {
					Entity[] entities =  tmp.getValue(mappedBy);
					List<Entity> list = new ArrayList<Entity>(Arrays.asList(entities));
					boolean isAdded = false;
					for (Entity _entity : list) {
						if (_entity.getOid().equals(entity.getOid())) isAdded = true;
					}
					if (!isAdded) list.add(entity);
					refEntity.setValue(mappedBy, list.toArray(new Entity[list.size()]));
				} else {
					refEntity.setValue(mappedBy, new Entity[] { entity });
				}
			}
		} else {
			//参照が多重でなければ上書き
			refEntity.setValue(mappedBy, entity);
		}
	}

	/**
	 * 被参照のEntityを参照元から削除します。
	 * @param context コンテキスト
	 * @param entity 画面で入力したデータ
	 * @param mappedBy 被参照項目のプロパティ
	 * @param defName 参照先のEntity定義名
	 * @param rpd 参照プロパティ定義
	 * @param element エレメント
	 * @param refEntity 参照元Entity
	 */
	protected void delMappedByValue(RegistrationCommandContext context, Entity entity, String mappedBy, String defName,
			ReferenceProperty rpd, Element element, Entity refEntity) {
		if (rpd.getMultiplicity() != 1) {
			//参照が多重の場合
			//refEntityはentityをロードした時に設定されてるデータなので、一度ロードする
			//本体は参照プロパティとして設定されてるはずなので、被参照のロードは不要
			Entity tmp = loadReference(context, refEntity, new LoadOption(true, false), rpd, element);

			//参照先から本データを除く
			Entity[] entities = tmp.getValue(mappedBy);
			List<Entity> list = new ArrayList<Entity>();
			if (entities != null) {
				for (Entity e : entities) {
					if (!e.getOid().equals(entity.getOid())) {
						list.add(e);
					}
				}
			}
			refEntity.setValue(mappedBy, list.toArray(new Entity[list.size()]));
		} else {
			//参照が多重でなければnull設定
			refEntity.setValue(mappedBy, null);
		}
	}
	

	/**
	 * 更新オプションを適用して、更新対象プロパティを設定
	 * @param updateProperties
	 * @param nestProperties
	 * @param ed
	 */
	protected void getUpdatePropertiesWithOption(List<String> updateProperties, List<NestProperty> nestProperties,
			EntityDefinition ed) {
		
		// isSpecifyAllPropertiesがfalseの場合
		if (!registOption.isSpecifyAllProperties()) {
			// 標準の更新項目を追加
			updateProperties.addAll(getUpdateProperties(nestProperties, ed));
			// 指定されているプロパティを更新項目に追加。
			for (String prop : registOption.getSpecifiedUpdateNestProperties()) {
				if (!updateProperties.contains(prop)) {
					updateProperties.add(prop);
				}
			}
			return;
		}

		// Reference項目として更新可能且つ、NestされたEntityの個々のプロパティ指定がない場合
		// 既存の参照先Entityは標準の更新項目を更新可能
		if (registOption.isSpecifiedAsReference() && registOption.getSpecifiedUpdateNestProperties().isEmpty()) {
			updateProperties.addAll(getUpdateProperties(nestProperties, ed));
			return;
		}

		// その他のパターンでは、既存のEntityは指定されたプロパティのみ更新可能。
		updateProperties.addAll(registOption.getSpecifiedUpdateNestProperties());
	}

	/**
	 * ネストプロパティから更新対象プロパティ名を取得。
	 * @param nestProperties
	 * @param ed
	 * @return
	 */
	protected static List<String> getUpdateProperties(List<NestProperty> nestProperties, EntityDefinition ed) {
		List<String> updateProperties = new ArrayList<>();
		for (NestProperty nProp : nestProperties) {
			if (nProp.isHideDetail()) continue;//編集時非表示の場合は対象外
			if (nProp.getEditor() instanceof JoinPropertyEditor) {
				//組み合わせで使うプロパティをチェック
				updateProperties.addAll(checkJoinProperty(ed, (JoinPropertyEditor) nProp.getEditor()));
			}
			PropertyDefinition npd = ed.getProperty(nProp.getPropertyName());
			if (npd != null && npd.isUpdatable()) {
				updateProperties.add(nProp.getPropertyName());
			}
			if (nProp.getEditor() instanceof RangePropertyEditor) {
				RangePropertyEditor rangeTo = (RangePropertyEditor) nProp.getEditor();
				PropertyDefinition toNpd = ed.getProperty(rangeTo.getToPropertyName());
				if (toNpd != null && toNpd.isUpdatable()) {
					updateProperties.add(rangeTo.getToPropertyName());
				}
			}
		}
		return updateProperties;
	}

	private static List<String> checkJoinProperty(EntityDefinition ed, JoinPropertyEditor editor) {
		List<String> updateProperties = new ArrayList<>();
		for (NestProperty nest : editor.getProperties()) {
			PropertyDefinition pd = ed.getProperty(nest.getPropertyName());
			if (pd != null && pd.isUpdatable()) {
				updateProperties.add(nest.getPropertyName());
			}
			//JoinのJoinは不可
//			if (nest.getEditor() instanceof JoinPropertyEditor) {
//				updateProperties.addAll(checkJoinProperty(ed, (JoinPropertyEditor) nest.getEditor()));
//			}
		}
		return updateProperties;
	}

	@Override
	public void setForceUpdate(boolean forceUpdate) {
		this.forceUpdate = forceUpdate;
	}

	protected static String resourceString(String key, Object... arguments) {
		return GemResourceBundleUtil.resourceString(key, arguments);
	}
}