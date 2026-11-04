/*
 * Copyright (C) 2026 DENTSU SOKEN INC. All Rights Reserved.
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

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.entity.definition.VersionControlType;
import org.iplass.mtp.view.generic.DetailFormView;
import org.iplass.mtp.view.generic.DetailFormViewButtonType;
import org.iplass.mtp.view.generic.EntityViewManager;
import org.iplass.mtp.view.generic.OutputType;

/**
 * 詳細画面の標準ボタンの表示可否を判定するクラス。
 *
 */
public class StandardButtonVisibility {

	private final EntityViewManager entityViewManager;

	private final String definitionName;

	private final OutputType outputType;

	private final DetailFormViewData data;

	/** 画面定義 */
	private final DetailFormView form;

	/** バージョン管理ありの場合true */
	private final boolean versionControl;

	/**
	 * コンストラクタ
	 *
	 * @param definitionName Entity定義名
	 * @param outputType 表示タイプ
	 * @param data 詳細画面表示用データ
	 */
	public StandardButtonVisibility(String definitionName, OutputType outputType, DetailFormViewData data) {
		this.entityViewManager = ManagerLocator.getInstance()
				.getManager(EntityViewManager.class);
		this.definitionName = definitionName;
		this.outputType = outputType;
		this.data = data;
		this.form = data.getView();
		this.versionControl = data.getEntityDefinition()
				.getVersionControlType() != VersionControlType.NONE;
	}

	/**
	 * 編集ボタンを表示するかを判定します。
	 *
	 * @return 表示する場合true
	 */
	public boolean isEditButtonVisible() {
		return (data.isCanUpdate() || data.isCanDelete()) && !form.isHideDetail()
				&& isStandardButtonVisible(DetailFormViewButtonType.EDIT);
	}

	/**
	 * ロックボタンを表示するかを判定します。
	 *
	 * @return 表示する場合true
	 */
	public boolean isLockButtonVisible() {
		return (data.isCanUpdate() || data.isCanDelete()) && !form.isHideLock()
				&& isStandardButtonVisible(DetailFormViewButtonType.LOCK);
	}

	/**
	 * コピーボタンを表示するかを判定します。
	 *
	 * @return 表示する場合true
	 */
	public boolean isCopyButtonVisible() {
		return data.isCanCreate() && !form.isNoneDispCopyButton()
				&& isStandardButtonVisible(DetailFormViewButtonType.COPY);
	}

	/**
	 * バージョンアップボタンを表示するかを判定します。
	 *
	 * @return 表示する場合true
	 */
	public boolean isVersionupButtonVisible() {
		return (data.isCanUpdate() || data.isCanDelete()) && versionControl
				&& isStandardButtonVisible(DetailFormViewButtonType.VERSIONUP);
	}

	/**
	 * 更新ボタンを表示するかを判定します。
	 *
	 * @return 表示する場合true
	 */
	public boolean isUpdateButtonVisible() {
		return data.isCanUpdate() && isStandardButtonVisible(DetailFormViewButtonType.UPDATE);
	}

	/**
	 * 削除ボタンを表示するかを判定します。
	 *
	 * @return 表示する場合true
	 */
	public boolean isDeleteButtonVisible() {
		return data.isCanDelete() && !form.isHideDelete()
				&& isStandardButtonVisible(DetailFormViewButtonType.DELETE);
	}

	/**
	 * バージョン削除ボタンを表示するかを判定します。
	 *
	 * @return 表示する場合true
	 */
	public boolean isDeleteVersionButtonVisible() {
		return versionControl && data.isCanDelete() && form.isShowDeleteSpecificVersion()
				&& isStandardButtonVisible(DetailFormViewButtonType.DELETE_VERSION);
	}

	/**
	 * 登録ボタンを表示するかを判定します。
	 *
	 * @return 表示する場合true
	 */
	public boolean isInsertButtonVisible() {
		return data.isCanCreate() && isStandardButtonVisible(DetailFormViewButtonType.INSERT);
	}

	/**
	 * 更新権限があるかを判定します。
	 *
	 * @return 更新権限がある場合true
	 */
	public boolean isCanUpdate() {
		return data.isCanUpdate();
	}

	/**
	 * 削除権限があるかを判定します。
	 *
	 * @return 削除権限がある場合true
	 */
	public boolean isCanDelete() {
		return data.isCanDelete();
	}

	/**
	 * 登録権限があるかを判定します。
	 *
	 * @return 登録権限がある場合true
	 */
	public boolean isCanCreate() {
		return data.isCanCreate();
	}

	private boolean isStandardButtonVisible(DetailFormViewButtonType type) {
		return entityViewManager.isStandardButtonVisible(definitionName, form.getName(), type, outputType, data.getEntity());
	}
}
