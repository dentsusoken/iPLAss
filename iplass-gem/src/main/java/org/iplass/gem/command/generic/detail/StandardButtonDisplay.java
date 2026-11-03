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
 * 詳細画面の標準ボタンの表示可否を判定するサポートクラス。
 *
 */
public class StandardButtonDisplay {

	private EntityViewManager entityViewManager;

	private String definitionName;

	private OutputType outputType;

	private DetailFormViewData data;

	/**
	 * コンストラクタ
	 *
	 * @param definitionName Entity定義名
	 * @param outputType 表示タイプ
	 * @param data 詳細画面表示用データ
	 */
	public StandardButtonDisplay(String definitionName, OutputType outputType, DetailFormViewData data) {
		this.entityViewManager = ManagerLocator.getInstance()
				.getManager(EntityViewManager.class);
		this.definitionName = definitionName;
		this.outputType = outputType;
		this.data = data;
	}

	/**
	 * 編集ボタンの表示可否を判定します。
	 *
	 * @return 表示する場合true
	 */
	public boolean isDisplayEditButton() {
		return (data.isCanUpdate() || data.isCanDelete()) && !getForm().isHideDetail()
				&& isStandardButtonVisible(DetailFormViewButtonType.EDIT);
	}

	/**
	 * ロックボタンの表示可否を判定します。
	 *
	 * @return 表示する場合true
	 */
	public boolean isDisplayLockButton() {
		return (data.isCanUpdate() || data.isCanDelete()) && !getForm().isHideLock()
				&& isStandardButtonVisible(DetailFormViewButtonType.LOCK);
	}

	/**
	 * コピーボタンの表示可否を判定します。
	 *
	 * @return 表示する場合true
	 */
	public boolean isDisplayCopyButton() {
		return data.isCanCreate() && !getForm().isNoneDispCopyButton()
				&& isStandardButtonVisible(DetailFormViewButtonType.COPY);
	}

	/**
	 * バージョンアップボタンの表示可否を判定します。
	 *
	 * @return 表示する場合true
	 */
	public boolean isDisplayVersionupButton() {
		return (data.isCanUpdate() || data.isCanDelete()) && isVersionControl()
				&& isStandardButtonVisible(DetailFormViewButtonType.VERSIONUP);
	}

	/**
	 * 更新ボタンの表示可否を判定します。
	 *
	 * @return 表示する場合true
	 */
	public boolean isDisplayUpdateButton() {
		return data.isCanUpdate() && isStandardButtonVisible(DetailFormViewButtonType.UPDATE);
	}

	/**
	 * 削除ボタンの表示可否を判定します。
	 *
	 * @return 表示する場合true
	 */
	public boolean isDisplayDeleteButton() {
		return data.isCanDelete() && !getForm().isHideDelete()
				&& isStandardButtonVisible(DetailFormViewButtonType.DELETE);
	}

	/**
	 * バージョン削除ボタンの表示可否を判定します。
	 *
	 * @return 表示する場合true
	 */
	public boolean isDisplayDeleteVersionButton() {
		return isVersionControl() && data.isCanDelete() && getForm().isShowDeleteSpecificVersion()
				&& isStandardButtonVisible(DetailFormViewButtonType.DELETE_VERSION);
	}

	/**
	 * 登録ボタンの表示可否を判定します。
	 *
	 * @return 表示する場合true
	 */
	public boolean isDisplayInsertButton() {
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

	private DetailFormView getForm() {
		return data.getView();
	}

	private boolean isVersionControl() {
		return data.getEntityDefinition()
				.getVersionControlType() != VersionControlType.NONE;
	}

	private boolean isStandardButtonVisible(DetailFormViewButtonType type) {
		return entityViewManager.isStandardButtonVisible(definitionName, getForm().getName(), type, outputType, data.getEntity());
	}
}
