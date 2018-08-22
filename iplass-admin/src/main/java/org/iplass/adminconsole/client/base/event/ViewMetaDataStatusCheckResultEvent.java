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

package org.iplass.adminconsole.client.base.event;

import java.util.LinkedHashMap;

import org.iplass.adminconsole.client.base.event.ViewMetaDataStatusCheckResultEvent.ViewMetaDataStatusCheckResultHandler;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

/**
 * メタデータのステータスチェックエラー結果の表示をリクエストするイベントです。
 */
public class ViewMetaDataStatusCheckResultEvent extends GwtEvent<ViewMetaDataStatusCheckResultHandler> {

	/**
	 * メタデータのステータス結果表示を実現するクラスで実装します。
	 */
	public interface ViewMetaDataStatusCheckResultHandler extends EventHandler {

		/**
		 * メタデータのステータス結果を表示します。
		 *
		 * @param event 対象Entityイベント
		 */
		void onViewMetaDataStatusCheckResult(ViewMetaDataStatusCheckResultEvent event);
	}

	public static final Type<ViewMetaDataStatusCheckResultHandler> TYPE = new Type<>();

	/** ステータスチェック結果 */
	private final LinkedHashMap<String, String> result;

	public ViewMetaDataStatusCheckResultEvent(LinkedHashMap<String, String> result) {
		this.result = result;
	}

	/**
	 * 対象のソースに対して、イベントを発行します。
	 *
	 * @param result ステータスチェック結果
	 * @param source 対象ソース
	 */
	public static void fire(LinkedHashMap<String, String> result, HasHandlers source) {
		source.fireEvent(new ViewMetaDataStatusCheckResultEvent(result));
	}

	/**
	 * ステータスチェック結果を返します。
	 *
	 * @return ステータスチェック結果
	 */
	public LinkedHashMap<String, String> getResult() {
		return result;
	}

	@Override
	public Type<ViewMetaDataStatusCheckResultHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ViewMetaDataStatusCheckResultHandler handler) {
		handler.onViewMetaDataStatusCheckResult(this);
	}

}
