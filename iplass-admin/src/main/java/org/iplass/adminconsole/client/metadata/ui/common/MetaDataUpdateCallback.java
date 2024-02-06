/*
 * Copyright (C) 2015 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.common;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.metadata.dto.AdminDefinitionModifyResult;
import org.iplass.adminconsole.shared.metadata.dto.MetaVersionCheckException;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;

public abstract class MetaDataUpdateCallback implements AsyncCallback<AdminDefinitionModifyResult> {

	/**
	 * <p>更新成功後処理</p>
	 * <p>更新成功後の処理を実装してください。</p>
	 *
	 * @param result 更新結果
	 */
	protected abstract void afterUpdate(AdminDefinitionModifyResult result);

	/**
	 * <p>上書き更新処理</p>
	 * <p>上書き更新処理を実装してください。
	 * MetaVersionCheckExceptionが発生した場合に確認メッセージを表示し、
	 * OKが押された場合に呼び出します。</p>
	 */
	protected abstract void overwriteUpdate();

	@Override
	public void onFailure(Throwable caught) {
		SmartGWTUtil.hideProgress();
		doFailure(caught);
	}

	@Override
	public void onSuccess(AdminDefinitionModifyResult result) {
		SmartGWTUtil.hideProgress();
		doSuccess(result);
	}

	/**
	 * <p>更新失敗時処理</p>
	 *
	 * @param caught エラー
	 */
	protected void doFailure(final Throwable caught) {
		if (caught instanceof MetaVersionCheckException) {
			SC.ask(getRS("overwriteConfirmMsg"), new BooleanCallback() {

				@Override
				public void execute(Boolean value) {
					if (value) {
						beforeOverwriteUpdate((MetaVersionCheckException)caught);

						//上書き更新処理を実行
						overwriteUpdate();
					}
				}
			});
		} else {
			// 失敗時
			afterFailure(caught);
		}
	}

	/**
	 * <p>更新成功処理</p>
	 *
	 * @param result 更新結果
	 */
	protected void doSuccess(AdminDefinitionModifyResult result) {
		if (result.isSuccess()) {
			//更新後の処理を実行
			afterUpdate(result);
		} else {
			SC.warn(getRS("failedUpdateMetaDataMsg") + result.getMessage());
		}
	}

	/**
	 * <p>バージョンチェックエラー時の上書き更新前処理</p>
	 * <p>最新のバージョンが必要な場合は、オーバーライドしてMetaVersionCheckExceptionから取得してください。</p>
	 * @param exp バージョンエラー
	 */
	protected void beforeOverwriteUpdate(MetaVersionCheckException exp) {
	}

	/**
	 * <p>バージョンチェック以外の更新失敗時のエラー処理</p>
	 * <p>カスタマイズしたい場合はオーバーライドしてください。</p>
	 *
	 * @param caught エラー
	 */
	protected void afterFailure(Throwable caught) {
		// 失敗時
		SC.warn(getRS("failedUpdateMetaDataMsg") + caught.getMessage());
	}

	private String getRS(String key) {
		return AdminClientMessageUtil.getString("ui_metadata_common_MetaDataUpdateCallback_" + key);
	}

}
