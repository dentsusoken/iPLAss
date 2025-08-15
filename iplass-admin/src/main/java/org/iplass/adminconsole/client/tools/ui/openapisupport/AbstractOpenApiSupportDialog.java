/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.adminconsole.client.tools.ui.openapisupport;

import java.util.HashMap;
import java.util.Map;

import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.mtp.webapi.definition.openapi.OpenApiFileType;
import org.iplass.mtp.webapi.definition.openapi.OpenApiVersion;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * OpenAPI(Swagger)Support 用のダイアログの抽象クラス
 *
 * @author SEKIGUCHI Naoya
 */
public abstract class AbstractOpenApiSupportDialog extends AbstractWindow {
	private static final int DIALOG_WIDTH = 800;

	/** ダイアログコンテナ */
	private VLayout container;
	/** ダイアログ入力フォーム */
	private DynamicForm form;
	/** フッター */
	private HLayout footer;

	/**
	 * コンストラクタ
	 * <p>
	 * ダイアログ共通の画面構成を設定します。
	 * </p>
	 */
	public AbstractOpenApiSupportDialog() {
		super();

		setWidth(DIALOG_WIDTH);

		setShowMinimizeButton(false);
		setShowMaximizeButton(false);

		setCanDragReposition(true);
		setCanDragResize(true);

		setIsModal(true);
		setShowModalMask(true);

		container = new VLayout(5);
		container.setWidth100();
		container.setHeight100();
		container.setMargin(10);
		addItem(container);


		// --------------------------------------
		// タイトル設定
		setTitle(getDialogTitle());

		onBeforeCreateForm(container);

		// フォーム作成
		this.form = new MtpForm();
		form.setWidth100();
		onCreateForm(form);

		container.addMember(form);

		onAfterCreateForm(container);

		footer = createFooter();
		container.addMember(footer);

		onAfterCreateContainer(container);

		// 中央寄せ
		centerInPage();
		// 自動サイズ調整
		setAutoSize(true);
	}

	/**
	 * ダイアログのタイトルを取得します。
	 * @return ダイアログのタイトル
	 */
	abstract protected String getDialogTitle();

	/**
	 * アクションボタンのラベルを設定します。
	 * <p>
	 * アクションボタンは、OKに該当するボタンです。
	 * </p>
	 * @return アクションボタンのラベル
	 */
	abstract protected String getActionButtonLabel();

	/**
	 * キャンセルボタンのラベルを設定します。
	 * @return キャンセルボタンのラベル
	 */
	protected String getCancelButtonLabel() {
		return "Cancel";
	}

	/**
	 * ダイアログのフォームを作成する前に呼び出されます。
	 * <p>
	 * form 作成前にコンテナ操作を行いたい場合に利用します。
	 * </p>
	 * @param container コンテナ
	 */
	protected void onBeforeCreateForm(VLayout container) {
	}

	/**
	 * ダイアログのフォームを作成します。
	 * <p>
	 * このメソッドは、ダイアログのコンストラクタで呼び出されます。
	 * ダイアログの入力項目を form に定義します。
	 * </p>
	 * @param form ダイアログのフォーム
	 */
	abstract protected void onCreateForm(DynamicForm form);

	/**
	 * ダイアログのフォームを作成した後に呼び出されます。
	 * <p>
	 * form 作成後、フッター作成前にコンテナ操作を行いたい場合に利用します。
	 * </p>
	 * @param container コンテナ
	 */
	protected void onAfterCreateForm(VLayout container) {
	}

	/**
	 * フッターを作成します
	 * @return フッターレイアウト
	 */
	protected HLayout createFooter() {
		//フッタ
		HLayout footer = new HLayout(5);
		footer.setMargin(10);
		footer.setAutoHeight();
		footer.setWidth100();
		footer.setAlign(Alignment.CENTER);

		// ボタン
		IButton action = new IButton(getActionButtonLabel());
		action.addClickHandler(event -> onClickAction(event, form));
		IButton cancel = new IButton(getCancelButtonLabel());
		cancel.addClickHandler(this::onClickCacnel);
		footer.setMembers(action, cancel);

		return footer;
	}

	/**
	 * アクションボタンがクリックされた時の処理を実装します。
	 * @param event クリックイベント
	 * @param form ダイアログのフォーム
	 */
	abstract protected void onClickAction(ClickEvent event, DynamicForm form);

	/**
	 * キャンセルボタンがクリックされた時の処理を実装します。
	 * @param event クリックイベント
	 */
	protected void onClickCacnel(ClickEvent event) {
		destroy();
	}

	/**
	 * ダイアログのコンテナを作成した後に呼び出されます。
	 * <p>
	 * コンテナ作成完了後にコンテナ操作したい場合に実装します。
	 * </p>
	 * @param container コンテナ
	 */
	protected void onAfterCreateContainer(VLayout container) {
	}

	protected void disableFooter() {
		footer.disable();
	}

	protected void enableFooter() {
		footer.enable();
	}

	// ----- ヘルパーメソッド

	/**
	 * OpenAPIのバージョンと値のマップを作成します。
	 * <p>
	 * SelectItem の valueMap に設定するためのマップを作成します。
	 * </p>
	 * @return バージョンと値のマップ
	 */
	protected Map<String, String> createVersionValueMap() {
		Map<String, String> versionValueMap = new HashMap<>();
		for (OpenApiVersion version : OpenApiVersion.values()) {
			versionValueMap.put(version.getSeriesVersion(), version.getSeriesVersion());
		}
		return versionValueMap;
	}

	/**
	 * OpenAPIのバージョンを選択するための SelectItem を作成します。
	 * @return OpenAPIのバージョン選択アイテム
	 */
	protected SelectItem createVersionSelectItem() {
		var valueMap = createVersionValueMap();
		SelectItem version = new SelectItem("versionField", "Version");
		version.setValueMap(valueMap);
		version.setDefaultValue(OpenApiVersion.V31.getSeriesVersion());
		return version;
	}

	/**
	 * ファイルタイプと値のマップを作成します。
	 * <p>
	 * SelectItem の valueMap に設定するためのマップを作成します。
	 * </p>
	 * @return ファイルタイプと値のマップ
	 */
	protected Map<String, String> createFileTypeValueMap() {
		Map<String, String> fileTypeValueMap = new HashMap<>();
		for (OpenApiFileType fileType : OpenApiFileType.values()) {
			fileTypeValueMap.put(fileType.getDisplayName(), fileType.getDisplayName());
		}
		return fileTypeValueMap;
	}

	/**
	 * ファイルタイプを選択するための SelectItem を作成します。
	 * @return ファイルタイプ選択アイテム
	 */
	protected SelectItem createFileTypeSelectItem() {
		SelectItem fileType = new SelectItem("fileTypeField", "File Type");
		fileType.setValueMap(createFileTypeValueMap());
		fileType.setDefaultValue(OpenApiFileType.YAML.getDisplayName());
		return fileType;
	}

	@Override
	protected boolean onPreDestroy() {
		container = null;
		form = null;
		footer = null;
		return super.onPreDestroy();
	}
}
