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

import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.view.generic.editor.BinaryPropertyEditor;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.BinaryPropertyEditor.BinaryDisplayType;

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

	/** PDF表示時にPDF.jsを利用 */
	private boolean usePdfjs;

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
		usePdfjs = e.isUsePdfjs();
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
		editor.setUsePdfjs(usePdfjs);
		return editor;
	}

}
