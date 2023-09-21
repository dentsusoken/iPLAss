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

package org.iplass.mtp.impl.view.generic.editor;

import java.util.regex.Pattern;

import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.BinaryProperty;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.view.generic.EntityViewRuntime;
import org.iplass.mtp.impl.view.generic.FormViewRuntime;
import org.iplass.mtp.impl.view.generic.element.property.MetaPropertyLayout;
import org.iplass.mtp.view.generic.editor.BinaryPropertyEditor;
import org.iplass.mtp.view.generic.editor.BinaryPropertyEditor.BinaryDisplayType;
import org.iplass.mtp.view.generic.editor.PropertyEditor;

/**
 * バイナリ型プロパティエディタのメタデータ
 * @author lis3wg
 */

public class MetaBinaryPropertyEditor extends MetaPrimitivePropertyEditor {
	/** シリアルバージョンUID */
	private static final long serialVersionUID = 8428866745088395378L;

	public static MetaBinaryPropertyEditor createInstance(PropertyEditor editor) {
		return new MetaBinaryPropertyEditor();
	}

	/** 表示タイプ */
	private BinaryDisplayType displayType;

	/** 画像の高さ */
	private int height;

	/** 画像の幅 */
	private int width;

	/** アップロードアクション名 */
	private String uploadActionName;

	/** ダウンロードアクション名 */
	private String downloadActionName;

	/** 新しいタブで開くか */
	private boolean openNewTab;

	/** 画像表示時に回転ボタンを表示 */
	private boolean showImageRotateButton;

	/** 画像表示時にイメージViewerを利用 */
	private boolean useImageViewer;

	/** PDF表示時にPDF.jsを利用 */
	private boolean usePdfjs;

	/** ファイル選択ボタンを非表示する */
	private boolean hideSelectButton;

	/** 削除ボタンを非表示する */
	private boolean hideDeleteButton;

	/** Label形式の場合の登録制御 */
	private boolean insertWithLabelValue = true;

	/** Label形式の場合の更新制御 */
	private boolean updateWithLabelValue = false;

	/** アップロード受け入れ可能な MIME Type 正規表現パターン */
	private String uploadAcceptMimeTypesPattern;

	/**
	 * 表示タイプを取得します。
	 * @return 表示タイプ
	 */
	public BinaryDisplayType getDisplayType() {
		return displayType;
	}

	/**
	 * 表示タイプを設定します。
	 * @param displayType 表示タイプ
	 */
	public void setDisplayType(BinaryDisplayType displayType) {
		this.displayType = displayType;
	}

	/**
	 * 画像の高さを取得します。
	 * @return 画像の高さ
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * 画像の高さを設定します。
	 * @param height 画像の高さ
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * 画像の幅を取得します。
	 * @return 画像の幅
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * 画像の幅を設定します。
	 * @param width 画像の幅
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * アップロードアクション名を取得します。
	 * @return アップロードアクション名
	 */
	public String getUploadActionName() {
		return uploadActionName;
	}

	/**
	 * アップロードアクション名を設定します。
	 * @param uploadActionName アップロードアクション名
	 */
	public void setUploadActionName(String uploadActionName) {
		this.uploadActionName = uploadActionName;
	}

	/**
	 * ダウンロードアクション名を取得します。
	 * @return ダウンロードアクション名
	 */
	public String getDownloadActionName() {
		return downloadActionName;
	}

	/**
	 * ダウンロードアクション名を設定します。
	 * @param downloadActionName ダウンロードアクション名
	 */
	public void setDownloadActionName(String downloadActionName) {
		this.downloadActionName = downloadActionName;
	}

	/**
	 * 新しいタブで開くかを取得します。
	 * @return 新しいタブで開くか
	 */
	public boolean isOpenNewTab() {
		return openNewTab;
	}

	/**
	 * 新しいタブで開くかを設定します。
	 * @param openNewTab 新しいタブで開くか
	 */
	public void setOpenNewTab(boolean openNewTab) {
		this.openNewTab = openNewTab;
	}

	/**
	 * 画像表示時に回転ボタンを表示を取得します。
	 * @return 画像表示時に回転ボタンを表示
	 */
	public boolean isShowImageRotateButton() {
		return showImageRotateButton;
	}

	/**
	 * 画像表示時に回転ボタンを表示を設定します。
	 * @param showImageRotateButton 画像表示時に回転ボタンを表示
	 */
	public void setShowImageRotateButton(boolean showImageRotateButton) {
		this.showImageRotateButton = showImageRotateButton;
	}

	/**
	 * 画像表示時にイメージViewerを利用を取得します。
	 * @return 画像表示時にイメージViewerを利用
	 */
	public boolean isUseImageViewer() {
		return useImageViewer;
	}

	/**
	 * 画像表示時にイメージViewerを利用を設定します。
	 * @param useImageViewer 画像表示時にイメージViewerを利用
	 */
	public void setUseImageViewer(boolean useImageViewer) {
		this.useImageViewer = useImageViewer;
	}

	/**
	 * PDF表示時にPDF.jsを利用を取得します。
	 * @return PDF表示時にPDF.jsを利用
	 */
	public boolean isUsePdfjs() {
		return usePdfjs;
	}

	/**
	 * PDF表示時にPDF.jsを利用を設定します。
	 * @param usePdfjs PDF表示時にPDF.jsを利用
	 */
	public void setUsePdfjs(boolean usePdfjs) {
		this.usePdfjs = usePdfjs;
	}

	/**
	 * ファイル選択ボタン非表示設定を取得します
	 * @return ファイル選択ボタン非表示設定
	 */
	public boolean isHideSelectButton() {
		return hideSelectButton;
	}

	/**
	 * ファイル選択ボタン非表示設定を設定します
	 * @param ファイル選択ボタン非表示設定
	 */
	public void setHideSelectButton(boolean hideSelectButton) {
		this.hideSelectButton = hideSelectButton;
	}

	/**
	 * 削除ボタン非表示設定を取得します
	 * @return ファイル選択ボタン非表示設定
	 */
	public boolean isHideDeleteButton() {
		return hideDeleteButton;
	}

	/**
	 * 削除ボタン非表示設定を設定します
	 * @param ファイル選択ボタン非表示設定
	 */
	public void setHideDeleteButton(boolean hideDeleteButton) {
		this.hideDeleteButton = hideDeleteButton;
	}

	/**
	 * 表示タイプがLabel形式の場合に、登録時に登録対象にするかを返します。
	 *
	 * @return true：登録対象
	 */
	public boolean isInsertWithLabelValue() {
		return insertWithLabelValue;
	}

	/**
	 * Label形式の場合の登録制御を設定します。
	 *
	 * @param insertWithLabelValue Label形式の場合の登録制御
	 */
	public void setInsertWithLabelValue(boolean insertWithLabelValue) {
		this.insertWithLabelValue = insertWithLabelValue;
	}

	/**
	 * 表示タイプがLabel形式の場合に、更新時に更新対象にするかを返します。
	 *
	 * @return true：更新対象
	 */
	public boolean isUpdateWithLabelValue() {
		return updateWithLabelValue;
	}

	/**
	 * Label形式の場合の更新制御を設定します。
	 *
	 * @param updateWithLabelValue Label形式の場合の更新制御
	 */
	public void setUpdateWithLabelValue(boolean updateWithLabelValue) {
		this.updateWithLabelValue = updateWithLabelValue;
	}

	/**
	 * アップロード受け入れ可能な MIME Type 正規表現パターンを取得します。
	 * @return アップロード受け入れ可能な MIME Type 正規表現パターン
	 */
	public String getUploadAcceptMimeTypesPattern() {
		return uploadAcceptMimeTypesPattern;
	}

	/**
	 * アップロード受け入れ可能な MIME Type 正規表現パターンを設定します
	 * @param uploadAcceptMimeTypesPattern アップロード受け入れ可能な MIME Type 正規表現パターン
	 */
	public void setUploadAcceptMimeTypesPattern(String uploadAcceptMimeTypesPattern) {
		this.uploadAcceptMimeTypesPattern = uploadAcceptMimeTypesPattern;
	}

	@Override
	public MetaBinaryPropertyEditor copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public void applyConfig(PropertyEditor editor) {
		super.fillFrom(editor);

		BinaryPropertyEditor e = (BinaryPropertyEditor) editor;
		displayType = e.getDisplayType();
		height = e.getHeight();
		width = e.getWidth();
		uploadActionName = e.getUploadActionName();
		downloadActionName = e.getDownloadActionName();
		openNewTab = e.isOpenNewTab();
		showImageRotateButton = e.isShowImageRotateButton();
		useImageViewer = e.isUseImageViewer();
		usePdfjs = e.isUsePdfjs();
		hideSelectButton = e.isHideSelectButton();
		hideDeleteButton = e.isHideDeleteButton();
		insertWithLabelValue = e.isInsertWithLabelValue();
		updateWithLabelValue = e.isUpdateWithLabelValue();
		uploadAcceptMimeTypesPattern = e.getUploadAcceptMimeTypesPattern();
	}

	@Override
	public PropertyEditor currentConfig(String propertyName) {
		BinaryPropertyEditor editor = new BinaryPropertyEditor();
		super.fillTo(editor);

		editor.setDisplayType(displayType);
		editor.setHeight(height);
		editor.setWidth(width);
		editor.setUploadActionName(uploadActionName);
		editor.setDownloadActionName(downloadActionName);
		editor.setOpenNewTab(openNewTab);
		editor.setShowImageRotateButton(showImageRotateButton);
		editor.setUseImageViewer(useImageViewer);
		editor.setUsePdfjs(usePdfjs);
		editor.setHideSelectButton(hideSelectButton);
		editor.setHideDeleteButton(hideDeleteButton);
		editor.setInsertWithLabelValue(insertWithLabelValue);
		editor.setUpdateWithLabelValue(updateWithLabelValue);
		editor.setUploadAcceptMimeTypesPattern(uploadAcceptMimeTypesPattern);

		return editor;
	}

	@Override
	public BinaryPropertyEditorRuntime createRuntime(EntityViewRuntime entityView, FormViewRuntime formView,
			MetaPropertyLayout propertyLayout, EntityContext context, EntityHandler eh) {
		return new BinaryPropertyEditorRuntime(entityView, formView, propertyLayout, context, eh);
	}

	/**
	 * バイナリプロパティエディターランタイム
	 */
	public class BinaryPropertyEditorRuntime extends PropertyEditorRuntime {
		/** コンパイル済みアップロード受け入れ可能な MIME Type 正規表現パターン */
		private Pattern uploadAcceptMimeTypesPattern;

		/**
		 * コンストラクタ
		 * @param entityView エンティティビューランタイム
		 * @param formView FormViewランタイム
		 * @param propertyLayout プロパティレイアウトメタ情報
		 * @param context エンティティコンテキスト
		 * @param eh エンティティハンドラー
		 */
		public BinaryPropertyEditorRuntime(EntityViewRuntime entityView, FormViewRuntime formView,
				MetaPropertyLayout propertyLayout, EntityContext context, EntityHandler eh) {
			super(entityView, formView, propertyLayout, context, eh);
			this.uploadAcceptMimeTypesPattern = null != MetaBinaryPropertyEditor.this.uploadAcceptMimeTypesPattern
					? Pattern.compile(MetaBinaryPropertyEditor.this.uploadAcceptMimeTypesPattern)
					: null;
		}

		@Override
		protected boolean checkPropertyType(PropertyDefinition pd) {
			return pd == null || pd instanceof BinaryProperty;
		}

		/**
		 * コンパイル済みアップロード受け入れ可能な MIME Type 正規表現パターンを取得する
		 * @return コンパイル済みアップロード受け入れ可能な MIME Type 正規表現パターン
		 */
		public Pattern getUploadAcceptMimeTypesPattern() {
			return uploadAcceptMimeTypesPattern;
		}
	}
}
