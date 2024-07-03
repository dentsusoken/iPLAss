/*
 * Copyright (C) 2018 DENTSU SOKEN INC. All Rights Reserved.
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
import java.util.List;

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
import org.iplass.mtp.transaction.TransactionManager;
import org.iplass.mtp.view.generic.EntityViewManager;
import org.iplass.mtp.view.generic.LoadEntityContext;
import org.iplass.mtp.view.generic.LoadEntityInterrupter.LoadType;
import org.iplass.mtp.view.generic.RegistrationInterrupter.RegistrationType;
import org.iplass.mtp.view.generic.element.property.PropertyBase;
import org.slf4j.Logger;

/**
 * 詳細画面用コマンドのスーパークラス
 * <pre>
 * 本クラスはマルチテナント基盤用の基底コマンドです。
 * 予告なくインターフェースが変わる可能性があるため、
 * 継承は出来る限り行わないでください。
 * </pre>
 * @author lis3wg
 */
public abstract class RegistrationCommandBase<T extends RegistrationCommandContext, V extends PropertyBase> implements Command {

	/** EntityDefinitionManager */
	protected EntityDefinitionManager edm = null;

	/** EntityViewManager */
	protected EntityViewManager evm = null;

	/** EntityManager */
	protected EntityManager em = null;

	protected abstract Logger getLogger();

	/**
	 * コンストラクタ
	 */
	public RegistrationCommandBase() {
		edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
		evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
		em = ManagerLocator.getInstance().getManager(EntityManager.class);
	}

	/**
	 * コンテキストを取得します。
	 * @param request リクエスト
	 * @return コンテキスト
	 */
	protected abstract T getContext(RequestContext request);

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
	protected Entity loadEntity(T context, final String oid, final Long version, final String defName,
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
	 * 画面表示用のEntityをロードします。
	 *
	 * @param context コンテキスト
	 * @param oid OID
	 * @param version データのバージョン
	 * @param defName Entity定義名
	 * @param loadReferences ロードする参照プロパティ
	 * @param loadVersioned バージョン検索するか
	 * @return Entity
	 */
	protected Entity loadViewEntity(T context, String oid, Long version, String defName, List<String> loadReferences, boolean loadVersioned) {
		LoadOption option = new LoadOption(loadReferences);
		option.setVersioned(loadVersioned);
		return loadEntity(context, oid, version, defName, option, LoadType.VIEW);
	}

	/**
	 * 更新処理時の対象Entityをロードします。
	 * 更新Entity生成時に表示判定のためのバインド用です。
	 *
	 * @param context コンテキスト
	 * @param oid OID
	 * @param version データのバージョン
	 * @param defName Entity定義名
	 * @param loadReferences ロードする参照プロパティ
	 * @param loadVersioned バージョン検索するか
	 * @return Entity
	 */
	protected Entity loadBeforeUpdateEntity(T context, String oid, Long version, String defName, List<String> loadReferences, boolean loadVersioned) {
		LoadOption option = new LoadOption(loadReferences);
		option.setVersioned(loadVersioned);
		return loadEntity(context, oid, version, defName, option, LoadType.BEFORE_UPDATE);
	}

	/**
	 * ロックユーザーを取得します。
	 *
	 * @param target ロック対象Entity
	 * @return ロックユーザー
	 */
	protected Entity getLockedByUser(Entity target) {
		if (target.getLockedBy() != null) {
			Entity user = em.load(target.getLockedBy(), null ,User.DEFINITION_NAME, new LoadOption(false, false));
			return user;
		}
		return null;
	}

	/**
	 * 更新処理のEntityをロードします。
	 * @param context コンテキスト
	 * @param entity Entity
	 * @param loadOption ロード時のオプション
	 * @return Entity
	 */
	private Entity loadUpdateEntity(T context, Entity entity, LoadOption loadOption) {
		return loadEntity(context, entity.getOid(), entity.getVersion(), entity.getDefinitionName(), loadOption, LoadType.UPDATE);
	}

	/**
	 * Entityを更新します。
	 * @param context コンテキスト
	 * @param entity 画面で入力したデータ
	 * @return 更新結果
	 */
	protected EditResult updateEntity(T context, Entity entity) {
		EditResult ret = new EditResult();
		List<ValidateError> errors = new ArrayList<>();
		context.setAttribute(Constants.VALID_ERROR_LIST, errors);
		try {
			//参照型の登録
			Entity loadEntity = loadUpdateEntity(context, entity, new LoadOption(true, context.hasUpdatableMappedByReference()));
			errors.addAll(beforeRegistRefEntity(context, entity, loadEntity));

			//カスタム登録前処理
			errors.addAll(context.getRegistrationInterrupterHandler().beforeRegister(entity, RegistrationType.UPDATE));

			//本データの更新
			errors.addAll(update(context, entity));

			//被参照の参照型の登録
			errors.addAll(afterRegistRefEntity(context, entity, loadEntity));

			//カスタム登録後処理
			errors.addAll(context.getRegistrationInterrupterHandler().afterRegister(entity, RegistrationType.UPDATE));

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
			if (getLogger().isDebugEnabled()) {
				getLogger().debug(e.getMessage(), e);
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
	private List<ValidateError> update(T context, Entity entity) {
		List<ValidateError> errors = new ArrayList<>();

		//画面に表示してるものだけ更新
		List<String> updatePropNames = new ArrayList<>();
		List<V> propList = context.getUpdateProperty();
		for (V prop : propList) {
			if (!context.getRegistrationPropertyBaseHandler().isDispProperty(prop)) continue;
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

		TargetVersion targetVersion = null;
		if (context.isVersioned()) {
			//バージョン番号更新かそのまま更新か
			if (context.isNewVersion()) {
				targetVersion = TargetVersion.NEW;
			} else {
				// 強制的に最新バージョンを指定するかをチェック（下位互換対応）
				if (context.isLoadLatestVersionedEntity()) {
					// 旧型式のSPECIFICを指定
					targetVersion = TargetVersion.SPECIFIC;
				} else {
					// バージョン指定か
					if (context.isLoadVersioned()) {
						targetVersion = TargetVersion.SPECIFIC;
					} else {
						targetVersion = TargetVersion.CURRENT_VALID;
					}
				}
			}
		} else {
			// 保存時バージョン指定か
			if (context.isLoadVersioned()) {
				targetVersion = TargetVersion.SPECIFIC;
			} else {
				targetVersion = TargetVersion.CURRENT_VALID;
			}
		}

		List<ValidateError> occurredErrors = (List<ValidateError>) context.getAttribute(Constants.VALID_ERROR_LIST);
		if (occurredErrors.isEmpty()) {
			UpdateOption option = new UpdateOption(true, targetVersion);
			option.setUpdateProperties(context.getRegistrationInterrupterHandler().getAdditionalProperties(updatePropNames));
			option.setPurgeCompositionedEntity(context.isPurgeCompositionedEntity());
			option.setLocalized(context.isLocalizationData());
			option.setForceUpdate(context.isForceUpadte());
			checkUpdateOption(context, option);
			try {
				em.update(entity, option);
			} catch (EntityValidationException e) {
				if (getLogger().isDebugEnabled()) {
					getLogger().debug(e.getMessage(), e);
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
	 * UpdateOptionのチェックを行う。
	 * @param context コンテキスト
	 * @param option 更新する際のオプション
	 */
	protected void checkUpdateOption(T context, UpdateOption option){
		// デフォルトでは何もしない。必要に応じてサブクラスでオーバーライドする

	}

	/**
	 * Entityを追加します。
	 * @param context コンテキスト
	 * @param entity 画面で入力したデータ
	 * @return 追加結果
	 */
	protected EditResult insertEntity(T context, Entity entity) {
		EditResult ret = new EditResult();
		List<ValidateError> errors = new ArrayList<>();
		context.setAttribute(Constants.VALID_ERROR_LIST, errors);
		try {
			//参照型の登録
			errors.addAll(beforeRegistRefEntity(context, entity, null));

			//カスタム登録前処理
			errors.addAll(context.getRegistrationInterrupterHandler().beforeRegister(entity, RegistrationType.INSERT));

			//本データの追加
			errors.addAll(insert(context, entity));

			//被参照の参照型の登録、新規で登録したデータなので被参照はいない→ロード対象から外す
			errors.addAll(afterRegistRefEntity(context, entity, loadUpdateEntity(context, entity, new LoadOption(true, false))));

			//カスタム登録後処理
			errors.addAll(context.getRegistrationInterrupterHandler().afterRegister(entity, RegistrationType.INSERT));

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
			if (getLogger().isDebugEnabled()) {
				getLogger().debug(e.getMessage(), e);
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
	private List<ValidateError> insert(T context, Entity entity) {
		List<ValidateError> errors = new ArrayList<>();

		List<ValidateError> occurredErrors = (List<ValidateError>) context.getAttribute(Constants.VALID_ERROR_LIST);
		if (occurredErrors.isEmpty()) {
			try {
				InsertOption option = new InsertOption();
				option.setLocalized(context.getView().isLocalizationData());
				em.insert(entity, option);
			} catch (EntityValidationException e) {
				if (getLogger().isDebugEnabled()) {
					getLogger().debug(e.getMessage(), e);
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
	private void rollBack(T context, Entity entity) {
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
	private void resetOid(T context, Entity entity) {
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
	private List<ValidateError> beforeRegistRefEntity(T context, Entity entity, Entity loadedEntity) {
		// リクエストから参照データを生成した際に登録したhandlerを使ってプロパティ単位で登録を実行
		final List<ValidateError> errors = new ArrayList<>();
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
	private List<ValidateError> afterRegistRefEntity(T context, Entity entity, Entity loadedEntity) {
		// リクエストから参照データを生成した際に登録したhandlerを使ってプロパティ単位で登録を実行
		final List<ValidateError> errors = new ArrayList<>();
		context.registMappedby(err -> errors.addAll(err), entity, loadedEntity);
		return errors;
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
