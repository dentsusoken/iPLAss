/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.definition.IndexType;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.VersionControlType;
import org.iplass.mtp.entity.definition.properties.ExpressionProperty;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.l10n.MetaEachPropertyDataLocalizationStrategy.EachPropertyDataLocalizationStrategyRuntime;
import org.iplass.mtp.impl.entity.property.PrimitivePropertyHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.entity.property.ReferencePropertyHandler;
import org.iplass.mtp.impl.properties.extend.ExpressionType;


/**
 * Entity更新処理時に指定可能なオプションです。
 * 
 * @author K.Higuchi
 *
 */
public class UpdateOption {
	
	/**
	 * 全てのプロパティ（※更新不可項目を除く）を更新対象としてセットしたUpdateOptionを生成します。
	 * 
	 * 
	 * @param defName Entityの定義名
	 * @param checkTimestamp タイムスタンプによるチェックを行う場合true
	 * @return UpdateOption
	 */
	public static UpdateOption allPropertyUpdateOption(String defName, boolean checkTimestamp) {
		return allPropertyUpdateOption(defName, checkTimestamp, TargetVersion.CURRENT_VALID);
	}
	
	/**
	 * 全てのプロパティ（※更新不可項目を除く）を更新対象としてセットしたUpdateOptionを生成します。
	 * 
	 * 
	 * @param defName Entityの定義名
	 * @param checkTimestamp タイムスタンプによるチェックを行う場合true
	 * @param targetVersion バージョン管理時に更新対象のバージョンを指定するオプション
	 * @return UpdateOption
	 */
	public static UpdateOption allPropertyUpdateOption(String defName, boolean checkTimestamp, TargetVersion targetVersion) {
		
		EntityDefinition def = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class).get(defName);
		if (def == null) {
			throw new EntityRuntimeException(defName + " not defined.");
		}
		List<PropertyDefinition> props = def.getPropertyList();
		List<String> updateProps = new ArrayList<String>();
		if (props != null) {
			for (PropertyDefinition pd: props) {
				if (!(pd instanceof ExpressionProperty)
						&& pd.isUpdatable()
						&& !pd.getName().equals(Entity.UPDATE_BY)
						//バージョン管理時は、ユニークインデックスの更新不可なので
						&& (def.getVersionControlType() == VersionControlType.NONE
								|| (pd.getIndexType() != IndexType.UNIQUE && pd.getIndexType() != IndexType.UNIQUE_WITHOUT_NULL))) {
					if (!(pd instanceof ReferenceProperty)
							|| ((ReferenceProperty) pd).getMappedBy() == null) {
						updateProps.add(pd.getName());
					}
				}
			}
		}
		UpdateOption option = new UpdateOption(checkTimestamp);
		option.setUpdateProperties(updateProps);
		option.setTargetVersion(targetVersion);
		return option;
	}
	
	/**
	 * 全てのプロパティ（※更新不可項目を除く）を更新対象としてセットしたUpdateOptionを生成します。
	 * 
	 * 
	 * @param defName Entityの定義名
	 * @param checkTimestamp タイムスタンプによるチェックを行う場合true。
	 * @param localized ローカライズフラグをon/offにするか否か。trueの場合、ローカライズを意識した形でupdate項目が取得される
	 * @return UpdateOption
	 */
	public static UpdateOption allPropertyUpdateOption(String defName, boolean checkTimestamp, boolean localized) {
		if (localized) {
			ExecuteContext exec = ExecuteContext.getCurrentContext();
			EntityContext ec = EntityContext.getCurrentContext();
			EntityHandler eh = ec.getHandlerByName(defName);
			List<String> updateProps = new ArrayList<String>();
			if (eh.getDataLocalizationStrategyRuntime() != null
					&& eh.getDataLocalizationStrategyRuntime() instanceof EachPropertyDataLocalizationStrategyRuntime) {
				EachPropertyDataLocalizationStrategyRuntime dlsr = (EachPropertyDataLocalizationStrategyRuntime) eh.getDataLocalizationStrategyRuntime();
				Map<String, String> propMap = dlsr.getPropMap(exec.getLanguage());
				for (String pn: propMap.keySet()) {
					PropertyHandler ph = eh.getProperty(pn, ec);
					if (ph instanceof PrimitivePropertyHandler) {
						PrimitivePropertyHandler pph = (PrimitivePropertyHandler) ph;
						if (!(pph.getMetaData().getType() instanceof ExpressionType)
								&& pph.getMetaData().isUpdatable()
								&& !pph.getName().equals(Entity.UPDATE_BY)
								&& (eh.getMetaData().getVersionControlType() == VersionControlType.NONE
										|| pph.getMetaData().getIndexType() != IndexType.UNIQUE && pph.getMetaData().getIndexType() != IndexType.UNIQUE_WITHOUT_NULL)) {
							updateProps.add(pph.getName());
						}
					} else if (ph instanceof ReferencePropertyHandler) {
						ReferencePropertyHandler rph = (ReferencePropertyHandler) ph;
						if (rph.getMetaData().isUpdatable()
								&& rph.getMappedByPropertyHandler(ec) == null) {
							updateProps.add(rph.getName());
						}
					}
				}
				
				UpdateOption option = new UpdateOption(checkTimestamp);
				option.setUpdateProperties(updateProps);
				option.setTargetVersion(TargetVersion.CURRENT_VALID);
				option.setLocalized(localized);
				return option;
			}
		}
		
		return allPropertyUpdateOption(defName, checkTimestamp, TargetVersion.CURRENT_VALID);
	}
	
	/** 更新対象のプロパティのリスト */
	private List<String> updateProperties;
	
	/** 更新時タイムスタンプチェックを行うかどうか。デフォルトtrue */
	private boolean checkTimestamp = true;
	
	/** バージョン管理時、更新対象のバージョンの指定。デフォルトCURRENT_VALID */
	private TargetVersion targetVersion = TargetVersion.CURRENT_VALID;
	
	/** 参照関係がCOMPOSITIONとして定義されているEntityを物理削除するかどうか（falseの場合は論理削除） 。デフォルトtrue */
	private boolean purgeCompositionedEntity = true;
	
	/** 変更項目が一つもなくとも、更新処理を実行する（結果、タイムスタンプ、更新者が更新される）かどうか。デフォルトfalse */
	private boolean forceUpdate = false;
	
	/** ユーザによるデータのロックをチェックするかどうか。デフォルトtrue */
	private boolean checkLockedByUser = true;
	
	private boolean withValidation = true;
	private boolean notifyListeners = true;
	private boolean localized = false;
	
	/**
	 * コンストラクタです。
	 * 
	 */
	public UpdateOption() {
	}
	
	/**
	 * コンストラクタです。
	 * 
	 * @param checkTimestamp 更新時タイムスタンプチェックを行うかどうか。trueの場合チェックする
	 */
	public UpdateOption(boolean checkTimestamp) {
		this.checkTimestamp = checkTimestamp;
	}
	
	/**
	 * コンストラクタです。
	 * 
	 * @param checkTimestamp 更新時タイムスタンプチェックを行うかどうか。trueの場合チェックする
	 * @param targetVersion バージョン管理する場合の更新対象
	 */
	public UpdateOption(boolean checkTimestamp, TargetVersion targetVersion) {
		this.checkTimestamp = checkTimestamp;
		this.targetVersion = targetVersion;
	}
	
	public UpdateOption copy() {
		UpdateOption copy = new UpdateOption();
		if (updateProperties != null) {
			copy.updateProperties = new ArrayList<>(updateProperties);
		}
		copy.checkTimestamp = checkTimestamp;
		copy.targetVersion = TargetVersion.CURRENT_VALID;
		copy.purgeCompositionedEntity = purgeCompositionedEntity;
		copy.forceUpdate = forceUpdate;
		copy.checkLockedByUser = checkLockedByUser;
		copy.withValidation = withValidation;
		copy.notifyListeners = notifyListeners;
		copy.localized = localized;
		return copy;
	}
	
	/**
	 * @see #setTargetVersion(TargetVersion)
	 * @return
	 */
	public TargetVersion getTargetVersion() {
		return targetVersion;
	}

	/**
	 * バージョン管理時、更新対象のバージョンを指定します。
	 * デフォルト値はTargetVersion.CURRENT_VALIDです。<br>
	 * TargetVersion.SPECIFICの場合は、更新対象のEntityにversion項目のセットが必要です。<br>
	 * TargetVersion.NEWの場合、updatePropertiesに未指定の値はベースとなるEntityの値がコピーされます。ベースとなるEntityは、
	 * 更新対象のEntityにversionが指定された場合はそのバージョンがベースになります。versionが未指定の場合は、現状の有効なバージョンがベースになります。
	 * 
	 * @param targetVersion
	 */
	public void setTargetVersion(TargetVersion targetVersion) {
		this.targetVersion = targetVersion;
	}

	/**
	 * 
	 * @see #setPurgeCompositionedEntity(boolean)
	 * @return
	 */
	public boolean isPurgeCompositionedEntity() {
		return purgeCompositionedEntity;
	}

	/**
	 * 更新時、COMPOSITIONと定義されている参照先Entityが
	 * 参照から削除された場合の削除の方法を設定します。
	 * trueの場合は物理削除します。
	 * falseの場合は論理削除します。
	 * デフォルトはtrueです。
	 * 
	 * @param purgeCompositionedEntity
	 */
	public void setPurgeCompositionedEntity(boolean purgeCompositionedEntity) {
		this.purgeCompositionedEntity = purgeCompositionedEntity;
	}
	
	/**
	 * 更新時タイムスタンプチェックを行う場合はtrueを設定します。
	 * その際は、更新対象のEntityにupdateDate項目のセットが必要です。
	 * デフォルトはtrueです。
	 * 
	 * @param checkTimestamp
	 */
	public void setCheckTimestamp(boolean checkTimestamp) {
		this.checkTimestamp = checkTimestamp;
	}

	/**
	 * @see #setCheckTimestamp(boolean)
	 */
	public boolean isCheckTimestamp() {
		return checkTimestamp;
	}
	
	/**
	 * @see #setForceUpdate(boolean)
	 */
	public boolean isForceUpdate() {
		return forceUpdate;
	}

	/**
	 * 変更項目が一つもなくとも、強制的に更新処理をする（結果、タイムスタンプ、更新者が更新される）かどうかを設定します。
	 * デフォルトfalseです。
	 * 
	 * @param forceUpdate 
	 */
	public void setForceUpdate(boolean forceUpdate) {
		this.forceUpdate = forceUpdate;
	}
	
	/**
	 * @see #setCheckLockedByUser(boolean)
	 */
	public boolean isCheckLockedByUser() {
		return checkLockedByUser;
	}

	/**
	 * {@link EntityManager#lockByUser(String, String)}により、ユーザによってロックされている場合、
	 * 更新エラー(EntityLockedByUserException)とするかどうかを設定します。
	 * デフォルトはtrueです。
	 * ユーザの画面操作によらない属性をバックエンドのプログラムから更新するような場合、
	 * 当該更新オプションをfalseに指定して更新することにより、
	 * バックエンドのプログラムはユーザのロック状態によらず、属性を更新することが可能となります。
	 * 
	 * @param checkLockedByUser
	 */
	public void setCheckLockedByUser(boolean checkLockedByUser) {
		this.checkLockedByUser = checkLockedByUser;
	}
	
	
	/**
	 * @see #setWithValidation(boolean)
	 */
	public boolean isWithValidation() {
		return withValidation;
	}

	/**
	 * 更新時、バリデーションを行うか否かをセットします。
	 * デフォルトtrueです。
	 * 
	 * @param withValidation
	 */
	public void setWithValidation(boolean withValidation) {
		this.withValidation = withValidation;
	}

	/**
	 * @see #setNotifyListeners(boolean)
	 */
	public boolean isNotifyListeners() {
		return notifyListeners;
	}

	/**
	 * 更新時、 {@link EntityEventListener}に通知するか否かをセットします。
	 * デフォルトtrueです。
	 * 
	 * @param notifyListeners
	 */
	public void setNotifyListeners(boolean notifyListeners) {
		this.notifyListeners = notifyListeners;
	}

	/**
	 * @see #setLocalized(boolean)
	 */
	public boolean isLocalized() {
		return localized;
	}

	/**
	 * localized項目を更新対象とするか否かをセットします。
	 * 
	 * @param localized
	 */
	public void setLocalized(boolean localized) {
		this.localized = localized;
	}

	/**
	 * @see #setUpdateProperties(List)
	 */
	public List<String> getUpdateProperties() {
		return updateProperties;
	}

	/**
	 * 更新対象のプロパティのリストを設定します。
	 * 
	 * @param updateProperties
	 */
	public void setUpdateProperties(List<String> updateProperties) {
		this.updateProperties = updateProperties;
	}
	
	/**
	 * 更新対象のプロパティのリストを設定します。
	 * 
	 * @param updateProperty
	 */
	public void setUpdateProperties(String... updateProperty) {
		ArrayList<String> list = new ArrayList<String>();
		if (updateProperty != null) {
			for (String s: updateProperty) {
				list.add(s);
			}
		}
		this.updateProperties = list;
	}
	
	/**
	 * 更新対象のプロパティを追加します。
	 * 
	 * @param updateProperty 更新対象のプロパティ名
	 * @return UpdateOption自身
	 */
	public UpdateOption add(String updateProperty) {
		if (updateProperties == null) {
			updateProperties = new ArrayList<>();
		}
		if (!updateProperties.contains(updateProperty)) {
			updateProperties.add(updateProperty);
		}
		return this;
	}
	
	/**
	 * 更新項目がなくとも、実際に更新処理実行します。結果、更新者、更新日が変更されます。
	 * 
	 * @return
	 */
	public UpdateOption force() {
		this.forceUpdate = true;
		return this;
	}
	
	/**
	 * ユーザにより、当該Entityがロックされているか否かを確認せず更新処理します。
	 * @return
	 */
	public UpdateOption noCheckLockedByUser() {
		this.checkLockedByUser = false;
		return this;
	}
	
	/**
	 * 更新時にバリデーションを行わないように設定します。
	 * @return
	 */
	public UpdateOption withoutValidation() {
		this.withValidation = false;
		return this;
	}
	
	/**
	 * 更新時に{@link EntityEventListener}に通知しないように設定します。
	 * @return
	 */
	public UpdateOption unnotifyListeners() {
		this.notifyListeners = false;
		return this;
	}
	
	/**
	 * 更新時、COMPOSITIONと定義されている参照先Entityが参照から削除された場合に、パージしない（ゴミ箱に入る）ように設定します。
	 * @return
	 */
	public UpdateOption noPurgeCompositionedEntity() {
		this.purgeCompositionedEntity = false;
		return this;
	}
	
	/**
	 * localized=trueに設定します。
	 * @return
	 */
	public UpdateOption localized() {
		this.localized = true;
		return this;
	}

	@Override
	public String toString() {
		return "UpdateOption [updateProperties=" + updateProperties
				+ ", checkTimestamp=" + checkTimestamp + ", targetVersion="
				+ targetVersion + ", purgeCompositionedEntity="
				+ purgeCompositionedEntity + ", forceUpdate=" + forceUpdate
				+ ", checkLockedByUser=" + checkLockedByUser
				+ ", withValidation=" + withValidation + ", notifyListeners="
				+ notifyListeners + ", localized=" + localized + "]";
	}

}
