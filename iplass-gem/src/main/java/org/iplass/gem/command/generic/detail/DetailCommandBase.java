/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
import org.iplass.gem.command.GemResourceBundleUtil;
import org.iplass.gem.command.generic.ResultType;
import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.auth.User;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
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
import org.iplass.mtp.entity.definition.properties.AutoNumberProperty;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.condition.predicate.In;
import org.iplass.mtp.transaction.TransactionManager;
import org.iplass.mtp.view.generic.EntityViewManager;
import org.iplass.mtp.view.generic.LoadEntityContext;
import org.iplass.mtp.view.generic.LoadEntityInterrupter.LoadType;
import org.iplass.mtp.view.generic.RegistrationInterrupter.RegistrationType;
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
public abstract class DetailCommandBase implements Command {

	private static Logger logger = LoggerFactory.getLogger(DetailCommandBase.class);

	/** EntityDefinitionManager */
	private EntityDefinitionManager edm = null;

	/** EntityViewManager */
	protected EntityViewManager evm = null;

	/** EntityManager */
	protected EntityManager em = null;

	/**
	 * コンストラクタ
	 */
	public DetailCommandBase() {
		edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
		evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
		em = ManagerLocator.getInstance().getManager(EntityManager.class);
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

	/**
	 * Entityをロードします。
	 * @param context コンテキスト
	 * @param oid OID
	 * @param version データのバージョン
	 * @param defName Entity定義名
	 * @param loadOption ロード時のオプション
	 * @param type ロード処理の種類
	 * @return Entity
	 */
	protected Entity loadEntity(DetailCommandContext context, final String oid, final Long version, final String defName,
			LoadOption loadOption, LoadType type) {
		Entity e = null;
		if (oid != null) {
			final LoadEntityContext leContext = context.getLoadEntityInterrupterHandler().beforeLoadEntity(defName, loadOption, type);
			if (leContext.isDoPrivileged()) {
				//特権実行
				e = AuthContext.doPrivileged(() -> {
					//データ取得
					return em.load(oid, version ,defName, leContext.getLoadOption());
				});
			} else {
				//データ取得
				e = em.load(oid, version ,defName, leContext.getLoadOption());
			}
			context.getLoadEntityInterrupterHandler().afterLoadEntity(e, loadOption, type);
		}
		return e;
	}

	/**
	 * Entityをロードします。
	 * @param context コンテキスト
	 * @param oid OID
	 * @param version データのバージョン
	 * @param defName Entity定義名
	 * @param loadReferences ロードする参照プロパティ
	 * @return Entity
	 */
	protected Entity loadViewEntity(DetailCommandContext context, String oid, Long version, String defName, List<String> loadReferences) {
		return loadEntity(context, oid, version ,defName, new LoadOption(loadReferences), LoadType.VIEW);
	}

	/**
	 * 参照プロパティを除いてEntityをロードします。
	 * @param oid OID
	 * @param version データのバージョン
	 * @param defName Entity定義名
	 * @return Entity
	 */
	protected Entity loadEntityWithoutReference(String oid, Long version, String defName) {
		Entity e = null;
		if (oid != null) {
			//データ取得
			e = em.load(oid, version ,defName, new LoadOption(false, false));
		}
		return e;
	}

	/**
	 * Entityをロードします。
	 * @param context コンテキスト
	 * @param entity Entity
	 * @param loadOption ロード時のオプション
	 * @return Entity
	 */
	private Entity loadEntityRegistProcess(DetailCommandContext context, Entity entity, LoadOption loadOption) {
		return loadEntity(context, entity.getOid(), entity.getVersion(), entity.getDefinitionName(), loadOption, LoadType.UPDATE);
	}

	/**
	 * Entityを更新します。
	 * @param context コンテキスト
	 * @param entity 画面で入力したデータ
	 * @return 更新結果
	 */
	protected EditResult updateEntity(DetailCommandContext context, Entity entity) {
		EditResult ret = new EditResult();
		List<ValidateError> errors = new ArrayList<ValidateError>();
		context.setAttribute(Constants.VALID_ERROR_LIST, errors);
		try {
			//参照型の登録
			Entity loadEntity = loadEntityRegistProcess(context, entity, new LoadOption(true, context.hasUpdatableMappedByReference()));
			errors.addAll(beforeRegistRefEntity(context, entity, loadEntity));

			//カスタム登録前処理
			errors.addAll(context.getRegistrationInterrupterHandler().beforeRegist(entity, RegistrationType.UPDATE));

			//本データの更新
			errors.addAll(update(context, entity));

			//被参照の参照型の登録
			errors.addAll(afterRegistRefEntity(context, entity, loadEntity));

			//カスタム登録後処理
			errors.addAll(context.getRegistrationInterrupterHandler().afterRegist(entity, RegistrationType.UPDATE));

			if (errors.isEmpty()) {
				ret.setResultType(ResultType.SUCCESS);
			} else {
				ret.setResultType(ResultType.ERROR);
				ret.setErrors(errors.toArray(new ValidateError[errors.size()]));
				ret.setMessage(resourceString("command.generic.detail.DetailCommandBase.inputErr"));

				// ロールバック
				rollBack(context, entity);
			}
		} catch (ApplicationException e) {
			if (logger.isDebugEnabled()) {
				logger.debug(e.getMessage(), e);
			}

			ret.setResultType(ResultType.ERROR);
			ret.setMessage(e.getMessage());

			// ロールバック
			rollBack(context, entity);
		}
		return ret;
	}

	/**
	 * Entityを更新します。
	 * @param context コンテキスト
	 * @param entity 画面で入力したデータ
	 * @return 入力エラーリスト
	 */
	@SuppressWarnings("unchecked")
	private List<ValidateError> update(DetailCommandContext context, Entity entity) {
		List<ValidateError> errors = new ArrayList<ValidateError>();

		//画面に表示してるものだけ更新
		List<String> updatePropNames = new ArrayList<String>();
		List<PropertyItem> propList = context.getProperty();
		for (PropertyItem prop : propList) {
			if (prop.isHideDetail()) continue;//詳細編集で非表示なら更新対象外
			PropertyDefinition pd = context.getProperty(prop.getPropertyName());
			if (pd != null && pd.isUpdatable()) {
				//被参照は更新しない
				if (pd instanceof ReferenceProperty) {
					String mappedBy = ((ReferenceProperty) pd).getMappedBy();
					if (mappedBy == null || mappedBy.isEmpty()) {
						updatePropNames.add(pd.getName());
					}
				} else {
					updatePropNames.add(pd.getName());
				}
			}
		}

		TargetVersion targetVersion = TargetVersion.CURRENT_VALID;
		if (context.isVersioned()) {
			//バージョン番号更新かそのまま更新か
			if (context.isNewVersion()) {
				targetVersion = TargetVersion.NEW;
			} else {
				targetVersion = TargetVersion.SPECIFIC;
			}
		}

		List<ValidateError> occurredErrors = (List<ValidateError>) context.getAttribute(Constants.VALID_ERROR_LIST);
		if (occurredErrors.isEmpty()) {
			UpdateOption option = new UpdateOption(true, targetVersion);
			option.setUpdateProperties(context.getRegistrationInterrupterHandler().getAdditionalProperties(updatePropNames));
			option.setPurgeCompositionedEntity(context.getView().isPurgeCompositionedEntity());
			option.setLocalized(context.getView().isLocalizationData());
			try {
				em.update(entity, option);
			} catch (EntityValidationException e) {
				if (logger.isDebugEnabled()) {
					logger.debug(e.getMessage(), e);
				}
				errors.addAll(e.getValidateResults());
			}
		} else {
			//既にエラーが発生してたら入力チェックのみ実施
			ValidateResult vr = em.validate(entity, updatePropNames);
			if (vr != null && vr.hasError()) {
				errors.addAll(vr.getErrors());
			}
		}
		return errors;
	}

	/**
	 * Entityを追加します。
	 * @param context コンテキスト
	 * @param entity 画面で入力したデータ
	 * @return 追加結果
	 */
	protected EditResult insertEntity(DetailCommandContext context, Entity entity) {
		EditResult ret = new EditResult();
		List<ValidateError> errors = new ArrayList<ValidateError>();
		context.setAttribute(Constants.VALID_ERROR_LIST, errors);
		try {
			//参照型の登録
			errors.addAll(beforeRegistRefEntity(context, entity, null));

			//カスタム登録前処理
			errors.addAll(context.getRegistrationInterrupterHandler().beforeRegist(entity, RegistrationType.INSERT));

			//本データの追加
			errors.addAll(insert(context, entity));

			//被参照の参照型の登録、新規で登録したデータなので被参照はいない→ロード対象から外す
			errors.addAll(afterRegistRefEntity(context, entity, loadEntityRegistProcess(context, entity, new LoadOption(true, false))));

			//カスタム登録後処理
			errors.addAll(context.getRegistrationInterrupterHandler().afterRegist(entity, RegistrationType.INSERT));

			if (errors.isEmpty()) {
				ret.setResultType(ResultType.SUCCESS);
			} else {
				ret.setResultType(ResultType.ERROR);
				ret.setErrors(errors.toArray(new ValidateError[errors.size()]));
				ret.setMessage(resourceString("command.generic.detail.DetailCommandBase.inputErr"));

				// ロールバック
				rollBack(context, entity);
			}
		} catch (ApplicationException e) {
			if (logger.isDebugEnabled()) {
				logger.debug(e.getMessage(), e);
			}

			ret.setResultType(ResultType.ERROR);
			ret.setMessage(e.getMessage());

			// ロールバック
			rollBack(context, entity);
		}

		return ret;
	}

	/**
	 * Entityを追加します。
	 * @param entity 画面で入力したデータ
	 * @return 入力エラーリスト
	 */
	@SuppressWarnings("unchecked")
	private List<ValidateError> insert(DetailCommandContext context, Entity entity) {
		List<ValidateError> errors = new ArrayList<ValidateError>();

		List<ValidateError> occurredErrors = (List<ValidateError>) context.getAttribute(Constants.VALID_ERROR_LIST);
		if (occurredErrors.isEmpty()) {
			try {
				InsertOption option = new InsertOption();
				option.setLocalized(context.getView().isLocalizationData());
				em.insert(entity, option);
			} catch (EntityValidationException e) {
				if (logger.isDebugEnabled()) {
					logger.debug(e.getMessage(), e);
				}
				errors.addAll(e.getValidateResults());
			}
		} else {
			//既にエラーが発生してたら入力チェックのみ実施
			ValidateResult vr = em.validate(entity);
			if (vr != null && vr.hasError()) {
				errors.addAll(vr.getErrors());
			}
		}
		return errors;
	}

	/**
	 * 登録・更新失敗時のロールバックを行います。
	 * @param context コンテキスト
	 * @param entity 画面で入力したデータ
	 */
	private void rollBack(DetailCommandContext context, Entity entity) {
		// ロールバックフラグON
		ManagerLocator.getInstance().getManager(TransactionManager.class).currentTransaction().setRollbackOnly();

		// OIDリセット
		resetOid(context, entity);
	}

	/**
	 * ロールバック時に参照EntityのOIDをリセットします。
	 * @param context コンテキスト
	 * @param entity 画面で入力したデータ
	 */
	private void resetOid(DetailCommandContext context, Entity entity) {
		// 参照型で新規追加されたものからOIDを削除する
		for (PropertyDefinition pd : context.getPropertyList()) {
			if (pd instanceof ReferenceProperty) {
				Object val = entity.getValue(pd.getName());
				if (val != null) {
					if (val instanceof Entity) {
						Entity rEntity = (Entity) val;
						Boolean deleteOidFlag = rEntity.getValue(Constants.DELETE_OID_FLAG);
						if (deleteOidFlag != null && deleteOidFlag) {
							rEntity.setOid(null);
							resetAutoNumber(rEntity, (ReferenceProperty)pd);
						}
					} else if (val instanceof Entity[]) {
						Entity[] rEntities = (Entity[]) val;
						for (Entity rEntity : rEntities) {
							Boolean deleteOidFlag = rEntity.getValue(Constants.DELETE_OID_FLAG);
							if (deleteOidFlag != null && deleteOidFlag) {
								rEntity.setOid(null);
								resetAutoNumber(rEntity, (ReferenceProperty)pd);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * ロールバック時に参照EntityのAutoNumberプロパティをリセットします。
	 *
	 * @param entity 参照Entity
	 * @param rp ReferenceProperty
	 */
	private void resetAutoNumber(Entity entity, ReferenceProperty rp) {
		EntityDefinition ned = edm.get(rp.getObjectDefinitionName());
		for (PropertyDefinition pd : ned.getPropertyList()) {
			if (pd instanceof AutoNumberProperty) {
				entity.setValue(pd.getName(), null);
			}
		}
	}

	/**
	 * 本データ登録前の参照データ登録処理を行います。
	 * @param context コンテキスト
	 * @param entity 画面で入力したデータ
	 * @param loadedEntity ロードしたデータ
	 * @return 入力エラーリスト
	 */
	private List<ValidateError> beforeRegistRefEntity(DetailCommandContext context, Entity entity, Entity loadedEntity) {
		// リクエストから参照データを生成した際に登録したhandlerを使ってプロパティ単位で登録を実行
		final List<ValidateError> errors = new ArrayList<ValidateError>();
		context.regist(err -> errors.addAll(err), entity, loadedEntity);
		return errors;
	}

	/**
	 * 本データ登録後の参照データ(被参照)登録処理を行います。
	 * @param context コンテキスト
	 * @param entity 画面で入力したデータ
	 * @param loadedEntity ロードしたデータ
	 * @return 入力エラーリスト
	 */
	private List<ValidateError> afterRegistRefEntity(DetailCommandContext context, Entity entity, Entity loadedEntity) {
		// リクエストから参照データを生成した際に登録したhandlerを使ってプロパティ単位で登録を実行
		final List<ValidateError> errors = new ArrayList<ValidateError>();
		context.registMappedby(err -> errors.addAll(err), entity, loadedEntity);
		return errors;
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

		// 参照元分のユーザ情報を再取得
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

			context.setAttribute(Constants.USER_INFO_MAP, userMap);
		}

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


	/**
	 * 編集結果
	 * @author lis3wg
	 */
	protected class EditResult {

		/** 結果 */
		private ResultType resultType;

		/** 入力エラー */
		private ValidateError[] errors;

		/** エラーメッセージ */
		private String message;

		/** コンストラクタ */
		public EditResult() {
		}

		/**
		 * 結果を取得します。
		 * @return 結果
		 */
		public ResultType getResultType() {
			return resultType;
		}

		/**
		 * 結果を設定します。
		 * @param resultType 結果
		 */
		public void setResultType(ResultType resultType) {
			this.resultType = resultType;
		}

		/**
		 * 入力エラーを取得します。
		 * @return 入力エラー
		 */
		public ValidateError[] getErrors() {
			return errors;
		}

		/**
		 * 入力エラーを設定します。
		 * @param errors 入力エラー
		 */
		public void setErrors(ValidateError[] errors) {
			this.errors = errors;
		}

		/**
		 * エラーメッセージを取得します。
		 * @return エラーメッセージ
		 */
		public String getMessage() {
			return message;
		}

		/**
		 * エラーメッセージを設定します。
		 * @param message エラーメッセージ
		 */
		public void setMessage(String message) {
			this.message = message;
		}
	}

	private static String resourceString(String key, Object... arguments) {
		return GemResourceBundleUtil.resourceString(key, arguments);
	}
}
