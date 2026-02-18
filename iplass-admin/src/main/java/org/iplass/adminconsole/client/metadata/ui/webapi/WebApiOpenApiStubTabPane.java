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
package org.iplass.adminconsole.client.metadata.ui.webapi;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogHandler;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogMode;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.MetaDataUtil;
import org.iplass.mtp.webapi.definition.WebApiDefinition;
import org.iplass.mtp.webapi.definition.WebApiStubContent;
import org.iplass.mtp.webapi.definition.openapi.OpenApiVersion;

import com.smartgwt.client.types.AutoFitWidthApproach;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * OpenAPI スタブタブ用の入力領域
 * @author SEKIGUCHI Naoya
 */
public class WebApiOpenApiStubTabPane extends VLayout {
	/** 入力フォーム */
	private DynamicForm form;
	/** 公開WebAPI */
	private CheckboxItem returnStubResponseField;
	/** スタブデフォルトコンテンツフィールド */
	private TextAreaItem stubDefaultContentField;
	/** スタブコンテンツグリッドフィールド */
	private StubContentGridField stubContentGridField;
	/** WebAPI設定アクセサ */
	private WebApiConfigurationAccessor configAccessor;

	/**
	 * コンストラクタ
	 * @param configAccessor WebAPI設定アクセサ
	 */
	public WebApiOpenApiStubTabPane(WebApiConfigurationAccessor configAccessor) {
		this.configAccessor = configAccessor;

		setMargin(5);
		setAutoHeight();
		setWidth100();
		setOverflow(Overflow.VISIBLE);

		returnStubResponseField = new CheckboxItem("returnStubResponseField", "Return Stub Response");
		returnStubResponseField.setWidth("100%");
		SmartGWTUtil.addHoverToFormItem(returnStubResponseField,
				AdminClientMessageUtil.getString("ui_metadata_webapi_WebApiOpenApiStubTabPane_returnStubResponseField_hoverMessage"));

		stubDefaultContentField = new TextAreaItem("stubDefaultContentField", "Stub Default Content");
		stubDefaultContentField.setWidth("100%");
		stubDefaultContentField.setHeight(100);
		stubDefaultContentField.setColSpan(2);
		SmartGWTUtil.addHoverToFormItem(stubDefaultContentField,
				AdminClientMessageUtil.getString("ui_metadata_webapi_WebApiOpenApiStubTabPane_stubDefaultContentField_hoverMessage"));
		SmartGWTUtil.setReadOnlyTextArea(stubDefaultContentField);

		SpacerItem stubResposeValueEditButtonSpacer = new SpacerItem();
		ButtonItem stubResposeValueEditButton = new ButtonItem("stubResponseStatusCodeEditButton", "Edit");
		stubResposeValueEditButton.setWidth(100);
		stubResposeValueEditButton.setStartRow(false);
		stubResposeValueEditButton.addClickHandler(this::onClickStubResposeValueEditButton);

		form = new DynamicForm();
		form.setColWidths(120, 300, "*");
		form.setNumCols(3);
		form.setItems(returnStubResponseField, stubDefaultContentField, stubResposeValueEditButtonSpacer,
				stubResposeValueEditButton);
		addMember(form);

		stubContentGridField = new StubContentGridField();
		stubContentGridField.setWidth100();
		stubContentGridField.setAddRecordHandler(this::onAddStubContentHandler);
		stubContentGridField.setEditRecordHandler(this::onEditStubContentHandler);

		addMember(stubContentGridField);
	}

	/**
	 * WebAPI定義の設定情報を、本画面領域に反映します。
	 * @param definition WebAPI定義
	 */
	public void setDefinition(WebApiDefinition definition) {
		returnStubResponseField.setValue(definition.isReturnStubResponse());
		stubDefaultContentField.setValue(definition.getStubDefaultContent());

		stubContentGridField.setDefinition(definition);
	}

	/**
	 * 本画面領域の設定情報を、WebAPI定義に反映します。
	 * @param definition WebAPI定義
	 * @return WebAPI定義
	 */
	public WebApiDefinition getDefinition(WebApiDefinition definition) {
		definition.setReturnStubResponse(SmartGWTUtil.getBooleanValue(returnStubResponseField));
		definition.setStubDefaultContent(SmartGWTUtil.getStringValue(stubDefaultContentField));

		stubContentGridField.getDefinition(definition);
		return definition;
	}

	@Override
	public void destroy() {
		form = null;
		returnStubResponseField = null;
		stubDefaultContentField = null;
		stubContentGridField = null;
		configAccessor = null;

		super.destroy();
	}

	/**
	 * スタブデフォルトコンテンツ編集ボタンがクリックされたときの処理
	 * @param event クリックイベント
	 */
	private void onClickStubResposeValueEditButton(ClickEvent event) {
		MetaDataUtil.showScriptEditDialog(ScriptEditorDialogMode.TEXT,
				SmartGWTUtil.getStringValue(stubDefaultContentField),
				"Edit Stub Default Content",
				"ui_metadata_webapi_WebApiOpenApiStubTabPane_stubDefaultContentField_editorHint",
				null,
				new ScriptEditorDialogHandler() {

			@Override
			public void onSave(String text) {
				stubDefaultContentField.setValue(text);
			}

			@Override
			public void onCancel() {
			}
		});
	}

	/**
	 * スタブコンテンツの追加操作を行うハンドラ
	 * @param stubContent スタブコンテンツ（未設定）
	 * @param callback 追加完了後に実行する処理
	 */
	private void onAddStubContentHandler(WebApiStubContent stubContent, Consumer<WebApiStubContent> callback) {
		// 追加なので、stubContent は未設定
		var dialog = new WebApiOpenApiStubContentEditDialog(callback, configAccessor.getResponseType());
		dialog.show();
	}

	/**
	 * スタブコンテンツの編集操作を行うハンドラ
	 * @param stubContent スタブコンテンツ（編集対象）
	 * @param callback 編集完了後に実行する処理
	 */
	private void onEditStubContentHandler(WebApiStubContent stubContent, Consumer<WebApiStubContent> callback) {
		// 編集なので、stubContent は値あり
		var dialog = new WebApiOpenApiStubContentEditDialog(callback, configAccessor.getResponseType());
		dialog.setValue(stubContent);
		dialog.show();
	}

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
	 * グリッドフィールド名
	 */
	private static enum FieldName {
		/** コンテンツタイプ */
		CONTENT_TYPE,
		/** ラベル */
		LABEL,
		/** コンテンツ */
		CONTENT,
		/** 表示用コンテンツ */
		DISPLAY_CONTENT
	}

	/**
	 * スタブコンテンツの編集アクションを処理するインターフェース
	 */
	public static interface StubContentGridActionHandler {
		/**
		 * スタブコンテンツの編集操作を行います。
		 * @param stubContent 編集対象のスタブコンテンツ（新規の場合は null、編集の場合は設定あり）
		 * @param callback 編集完了後に実行する処理
		 */
		void onAction(WebApiStubContent stubContent, Consumer<WebApiStubContent> callback);
	}

	/**
	 * スタブコンテンツグリッドフィールド
	 * <p>
	 * グリッドおよび編集操作やボタンを含む入力フィールドです。
	 * </p>
	 */
	private static class StubContentGridField extends VLayout {
		/** グリッド本体 */
		private StubConentListGrid stubContentListGrid;
		/** レコード追加操作 */
		private StubContentGridActionHandler addRecordHandler;
		/** レコード編集操作 */
		private StubContentGridActionHandler editRecordHandler;

		/**
		 * コンストラクタ
		 */
		public StubContentGridField() {
			Label stubContentLabel = new Label("Stub Contents:");
			stubContentLabel.setHeight(21);
			stubContentLabel.setWrap(false);

			stubContentListGrid = new StubConentListGrid();
			stubContentListGrid.addRecordDoubleClickHandler(this::onDoubleClickGrid);

			IButton addButton = new IButton("Add");
			addButton.addClickHandler(this::onClickAddButton);

			IButton removeButton = new IButton("Remove");
			removeButton.addClickHandler(this::onClickRemoveButton);

			HLayout stubContentListGridButtonLayout = new HLayout();
			stubContentListGridButtonLayout.setWidth100();
			stubContentListGridButtonLayout.addMember(addButton);
			stubContentListGridButtonLayout.addMember(removeButton);

			addMember(stubContentLabel);
			addMember(stubContentListGrid);
			addMember(stubContentListGridButtonLayout);
		}

		/**
		 * スタブコンテンツの追加操作を行うハンドラを設定します。
		 * @param handler ハンドラ
		 */
		public void setAddRecordHandler(StubContentGridActionHandler handler) {
			this.addRecordHandler = handler;
		}

		/**
		 * スタブコンテンツの編集操作を行うハンドラを設定します。
		 * @param handler ハンドラ
		 */
		public void setEditRecordHandler(StubContentGridActionHandler handler) {
			this.editRecordHandler = handler;
		}

		/**
		 * WebAPI定義の設定情報を、本画面領域に反映します。
		 * @param definition WebAPI定義
		 */
		public void setDefinition(WebApiDefinition definition) {
			stubContentListGrid.setRecords(new ListGridRecord[] {});
			if (definition.getStubContents() != null) {
				for (WebApiStubContent stubContent : definition.getStubContents()) {
					var rec = convertGridRecord(stubContent, new ListGridRecord());
					stubContentListGrid.addData(rec);
				}
			}
			stubContentListGrid.refreshFields();
		}

		/**
		 * 本画面領域の設定情報を、WebAPI定義に反映します。
		 * @param definition WebAPI定義
		 * @return WebAPI定義
		 */
		public WebApiDefinition getDefinition(WebApiDefinition definition) {
			definition.setStubContents(null);
			var records = stubContentListGrid.getRecords();
			if (null != records && records.length > 0) {
				WebApiStubContent[] stubContents = new WebApiStubContent[records.length];
				for (int i = 0; i < records.length; i++) {
					stubContents[i] = convertStubContent(records[i]);
				}
				definition.setStubContents(stubContents);
			}

			return definition;
		}

		@Override
		public void destroy() {
			stubContentListGrid = null;
			addRecordHandler = null;
			editRecordHandler = null;

			super.destroy();
		}

		/**
		 * グリッドのレコードダブルクリック時処理
		 * @param event ダブルクリックイベント
		 */
		private void onDoubleClickGrid(RecordDoubleClickEvent event) {
			if (null != editRecordHandler) {
				var clicked = event.getRecord();
				var stubContent = convertStubContent(clicked);
				editRecordHandler.onAction(stubContent, afterContent -> {
					if (null != afterContent) {
						var rec = convertGridRecord(afterContent, clicked);
						stubContentListGrid.updateData(rec);
						stubContentListGrid.refreshFields();
					}
				});
			}
		}

		/**
		 * 追加ボタンクリック時処理
		 * @param event クリックイベント
		 */
		private void onClickAddButton(com.smartgwt.client.widgets.events.ClickEvent event) {
			if (null != addRecordHandler) {
				addRecordHandler.onAction(null, content -> {
					if (null != content) {
						var rec = convertGridRecord(content, new ListGridRecord());
						stubContentListGrid.addData(rec);
						stubContentListGrid.refreshFields();
					}
				});
			}
		}

		/**
		 * 削除ボタンクリック時操作
		 * @param event
		 */
		private void onClickRemoveButton(com.smartgwt.client.widgets.events.ClickEvent event) {
			var selected = stubContentListGrid.getSelectedRecord();
			stubContentListGrid.removeData(selected);
		}

		/**
		 * スタブコンテンツデータオブジェクトからグリッドレコードに変換します。
		 * @param stubContent スタブコンテンツ
		 * @param origin 変換元のグリッドレコード
		 * @return 変換後のグリッドレコード
		 */
		private ListGridRecord convertGridRecord(WebApiStubContent stubContent, ListGridRecord origin) {
			origin.setAttribute(FieldName.CONTENT_TYPE.name(), stubContent.getContentType());
			origin.setAttribute(FieldName.LABEL.name(), stubContent.getLabel());
			origin.setAttribute(FieldName.CONTENT.name(), stubContent.getContent());
			// HTMLエスケープしないと画面に表示されない
			var sitized = sitize(stubContent.getContent());
			origin.setAttribute(FieldName.DISPLAY_CONTENT.name(), sitized);
			return origin;
		}

		/**
		 * グリッドレコードからスタブコンテンツデータオブジェクトに変換します。
		 * @param origin 変換元のグリッドレコード
		 * @return 変換後のスタブコンテンツデータオブジェクト
		 */
		private WebApiStubContent convertStubContent(ListGridRecord origin) {
			WebApiStubContent stubContent = new WebApiStubContent();
			stubContent.setContentType(origin.getAttribute(FieldName.CONTENT_TYPE.name()));
			stubContent.setLabel(origin.getAttribute(FieldName.LABEL.name()));
			stubContent.setContent(origin.getAttribute(FieldName.CONTENT.name()));
			return stubContent;
		}

		/**
		 * HTMLエスケープを行います。
		 * @param v 対象の値
		 * @return エスケープ後の値
		 */
		private String sitize(String v) {
			if (null == v) {
				return null;
			}

			String r = v
					// & - ampersand
					.replaceAll("&", "&amp;")
					// " - double-quote
					.replaceAll("\"", "&quot;")
					// < - less-than
					.replaceAll("<", "&lt;")
					// > - greater-than
					.replaceAll(">", "&gt;")
					// ' - single-quote
					.replaceAll("'", "&#039;");

			return r;
		}
	}

	/**
	 * スタブコンテンツグリッド
	 */
	private static class StubConentListGrid extends ListGrid {
		/**
		 * コンストラクタ
		 */
		public StubConentListGrid() {
			setWidth100();
			setHeight(1);

			setShowAllColumns(true);							//列を全て表示
			setShowAllRecords(true);							//レコードを全て表示
			setCanResizeFields(true);							//列幅変更可能
			setCanSort(false);									//ソート不可
			setCanPickFields(false);							//表示フィールドの選択不可
			setCanGroupBy(false);								//GroupByの選択不可
			setAutoFitWidthApproach(AutoFitWidthApproach.BOTH);	//AutoFit時にタイトルと値を参照
			setLeaveScrollbarGap(false);						//縦スクロールバー自動表示制御
			setBodyOverflow(Overflow.VISIBLE);
			setOverflow(Overflow.VISIBLE);

			ListGridField contentTypeField = new ListGridField(FieldName.CONTENT_TYPE.name(), AdminClientMessageUtil.getString("ui_metadata_ui_webapi_WebApiOpenApiStubTabPane_contentType"));
			ListGridField labelField = new ListGridField(FieldName.LABEL.name(), AdminClientMessageUtil.getString("ui_metadata_ui_webapi_WebApiOpenApiStubTabPane_label"));
			ListGridField contentField = new ListGridField(FieldName.DISPLAY_CONTENT.name(), AdminClientMessageUtil.getString("ui_metadata_ui_webapi_WebApiOpenApiStubTabPane_content"));

			setFields(contentTypeField, labelField, contentField);
		}
	}
}

