/*
 * Copyright (C) 2024 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.adminconsole.client.base.io.upload;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.shared.base.io.XsrfProtectedMultipartConstant;
import org.iplass.adminconsole.shared.base.rpc.AdminXsrfTokenHolder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Widget;

/**
 * XsrfProtected Form (Multpartリクエスト用）
 *
 * <p>
 * XsrfToken を含めたマルチパートリクエストを実行する為の Form クラス。
 * 本 Form に対して submit を行うと、XsrfToken を含めてリクエストを行う。
 * </p>
 *
 * <p>
 * 構成ノードイメージ
 *
 * <pre>
 * FormPanel
 *  |
 *  +-- FloaPanel (childWidgetPanel)
 *       |
 *       +-- add widget
 * </pre>
 * </p>
 *
 * @see org.iplass.adminconsole.server.base.io.upload.XsrfProtectedMultipartServlet
 * @author SEKIGUCHI Naoya
 */
public class XsrfProtectedMultipartForm extends Composite {
	/** FormPanel */
	private FormPanel formPanel = new FormPanel();
	/** 子ウィジェット管理パネル */
	private FlowPanel childWidgetPanel = new FlowPanel();
	/** XsrfToken ウィジェット */
	private Hidden xsrfTokenWidget = new Hidden(XsrfProtectedMultipartConstant.RequestParameterName.XSRF_TOKEN_KEY);
	/** XSRFトークン削除 submit complete HandlerRegistration */
	private HandlerRegistration xsrfTokenRemoveSubmitCompleteHandlerRegistration;
	/** submit キャンセル時　XSRFトークン削除 submit HandlerRegistration */
	private HandlerRegistration xsrfTokenRemoveSubmitHandlerRegistration;

	/**
	 * デフォルトコンストラクタ
	 */
	public XsrfProtectedMultipartForm() {
		// ノードを構成する

		// POST, multipart 固定
		formPanel.setMethod(FormPanel.METHOD_POST);
		formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
		// UTF-8 固定
		formPanel.getElement().setAttribute("accept-charset", StandardCharsets.UTF_8.name());
		// submit 完了時イベント
		xsrfTokenRemoveSubmitCompleteHandlerRegistration = formPanel.addSubmitCompleteHandler(event -> {
			// トークンを削除
			remove(xsrfTokenWidget);
		});

		formPanel.add(childWidgetPanel);

		initWidget(formPanel);
	}

	@Override
	protected void onUnload() {
		super.onUnload();
		// イベント破棄
		xsrfTokenRemoveSubmitCompleteHandlerRegistration.removeHandler();
		removeXsrfTokenRemoveSubmitHandlerRegistration();
	}

	/**
	 * サーバーサービスを設定する。
	 *
	 * <p>
	 * 本メソッドを実行することで、form 要素の action 属性を設定する。
	 * </p>
	 *
	 * @param service サーバーサービス名
	 */
	public void setService(String service) {
		formPanel.setAction(GWT.getModuleBaseURL() + service);
	}

	/**
	 * サーバーサービスを取得する。
	 *
	 * @return サーバーサービス名
	 */
	public String getService() {
		return formPanel.getAction().substring(GWT.getModuleBaseURL().length());
	}

	/**
	 * class 属性を設定する。
	 *
	 * <p>
	 * form 要素の class 属性にパラメータの値を設定する。
	 * パラメータが未設定の場合は、class 属性を削除する。
	 * </p>
	 *
	 * @param classAttributes クラス属性値
	 */
	public void setClassAttribute(String... classAttributes) {
		StringBuilder classValue = new StringBuilder();
		for (String attr : classAttributes) {
			classValue.append(' ').append(attr);
		}
		if (0 < classValue.length()) {
			formPanel.getElement().setAttribute("class", classValue.toString());

		} else if (formPanel.getElement().hasAttribute("class")) {
			formPanel.getElement().removeAttribute("class");

		}
	}

	/**
	 * 本ウィジェットの子要素の最後に追加する。
	 *
	 * @param widget 追加対象ウィジェット
	 */
	public void insertAfter(Widget widget) {
		childWidgetPanel.insert(widget, childWidgetPanel.getWidgetCount());
	}

	/**
	 * フォームに追加されたウィジェットのリストを取得する。
	 *
	 * @return ウィジェットリスト
	 */
	public List<Widget> getWidgetList() {
		List<Widget> result = new ArrayList<Widget>();
		for (int i = 0; i < childWidgetPanel.getWidgetCount(); i++) {
			result.add(childWidgetPanel.getWidget(i));
		}
		return result;
	}

	/**
	 * 本ウィジェットの子要素を削除する。
	 * @param widget 削除対象ウィジェット
	 */
	public void remove(Widget widget) {
		childWidgetPanel.remove(widget);
	}

	/**
	 * submit を実行する。
	 */
	public void submit() {
		// イベントが存在していれば削除（残らないはずだが、想定外動作を考慮）
		removeXsrfTokenRemoveSubmitHandlerRegistration();
		// 想定外例外でトークンが残っていることを考慮し、先に削除する
		remove(xsrfTokenWidget);

		// イベントがキャンセルされていた場合にトークンフィールドを削除。最後のイベント発火となるように毎回登録・削除する
		xsrfTokenRemoveSubmitHandlerRegistration = addSubmitHandler(event -> {
			if (event.isCanceled()) {
				remove(xsrfTokenWidget);
			}
			removeXsrfTokenRemoveSubmitHandlerRegistration();
		});

		// XsrfToken を追加
		xsrfTokenWidget.setValue(AdminXsrfTokenHolder.token().getToken());
		insertAfter(xsrfTokenWidget);
		formPanel.submit();
	}

	/**
	 * submit 開始時処理を追加する
	 * @param handler ハンドラ
	 * @return HandlerRegistration
	 */
	public HandlerRegistration addSubmitHandler(SubmitHandler handler) {
		return formPanel.addSubmitHandler(handler);
	}

	/**
	 * submit 完了時処理を追加する
	 * @param handler ハンドラ
	 * @return HandlerRegistration
	 */
	public HandlerRegistration addSubmitCompleteHandler(SubmitCompleteHandler handler) {
		return formPanel.addSubmitCompleteHandler(handler);
	}

	private void removeXsrfTokenRemoveSubmitHandlerRegistration() {
		if (null != xsrfTokenRemoveSubmitHandlerRegistration) {
			xsrfTokenRemoveSubmitHandlerRegistration.removeHandler();
			xsrfTokenRemoveSubmitHandlerRegistration = null;
		}
	}
}
