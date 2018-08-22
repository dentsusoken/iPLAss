/*
 * Copyright (C) 2016 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.base.rpc;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;

/**
 * <p>{@link AsyncCallback} のデフォルト実装クラス</p>
 * <p>エラー時の処理をデフォルトで実装しています。</p>
 *
 * @param <T> 結果のデータ型
 */
public abstract class AdminAsyncCallback<T> implements AsyncCallback<T>{

	protected void beforeFailure(Throwable caught){};

	protected void afterFailure(Throwable caught){};

	protected String failureMessage(Throwable caught){
		return rs("common_AdminAsyncCallback_failedMessage") + caught.getMessage();
	}

	public void onFailure(Throwable caught) {
		GWT.log(caught.toString(), caught);

		beforeFailure(caught);

		SC.warn(failureMessage(caught));

		afterFailure(caught);
	}

	private String rs(String key) {
		return AdminClientMessageUtil.getString(key);
	}

}
