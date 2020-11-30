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

package org.iplass.mtp.view.generic.editor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.adminconsole.view.annotation.generic.EntityViewField;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.view.generic.Jsp;
import org.iplass.mtp.view.generic.Jsps;
import org.iplass.mtp.view.generic.ViewConst;

/**
 * バイナリ型プロパティエディタ
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Jsps({
	@Jsp(path="/jsp/gem/generic/editor/BinaryPropertyEditor.jsp", key=ViewConst.DESIGN_TYPE_GEM)
})
public class BinaryPropertyEditor extends PrimitivePropertyEditor {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -107014320173325123L;

	/** 表示タイプ */
	@XmlType(namespace="http://mtp.iplass.org/xml/definition/view/generic")
	public enum BinaryDisplayType {
		@XmlEnumValue("Binary")BINARY,
		@XmlEnumValue("Link")LINK,
		@XmlEnumValue("Label")LABEL,
		@XmlEnumValue("Preview")PREVIEW,
		@XmlEnumValue("Hidden")HIDDEN
	}

	@MetaFieldInfo(
			displayName="表示タイプ",
			displayNameKey="generic_editor_BinaryPropertyEditor_displayTypeDisplaNameKey",
			inputType=InputType.ENUM,
			enumClass=BinaryDisplayType.class,
			required=true,
			displayOrder=100,
			description="画面に表示する方法を選択します。",
			descriptionKey="generic_editor_BinaryPropertyEditor_displayTypeDescriptionKey"
	)
	private BinaryDisplayType displayType;

	/** 画像の高さ */
	@MetaFieldInfo(
			displayName="画像の高さ(px)",
			displayNameKey="generic_editor_BinaryPropertyEditor_heightDisplaNameKey",
			inputType=InputType.NUMBER,
			displayOrder=110,
			description="バイナリが画像の場合に表示する画像の高さを設定します。",
			descriptionKey="generic_editor_BinaryPropertyEditor_heightDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHRESULT, FieldReferenceType.DETAIL}
	)
	private int height;

	/** 画像の幅 */
	@MetaFieldInfo(
			displayName="画像の幅(px)",
			displayNameKey="generic_editor_BinaryPropertyEditor_widthDisplaNameKey",
			inputType=InputType.NUMBER,
			displayOrder=120,
			description="バイナリが画像の場合に表示する画像の幅を設定します。",
			descriptionKey="generic_editor_BinaryPropertyEditor_widthDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHRESULT, FieldReferenceType.DETAIL}
	)
	private int width;

	/** アップロードアクション名 */
	@MetaFieldInfo(
			displayName="アップロードアクション名",
			displayNameKey="generic_editor_BinaryPropertyEditor_uploadActionNameDisplaNameKey",
			inputType=InputType.ACTION,
			displayOrder=130,
			description="アップロード時に実行されるアクションを設定します。",
			descriptionKey="generic_editor_BinaryPropertyEditor_uploadActionNameDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private String uploadActionName;

	/** ダウンロードアクション名 */
	@MetaFieldInfo(
			displayName="ダウンロードアクション名",
			displayNameKey="generic_editor_BinaryPropertyEditor_downloadActionNameDisplaNameKey",
			inputType=InputType.ACTION,
			displayOrder=140,
			description="ダウンロード時に実行されるアクションを設定します。",
			descriptionKey="generic_editor_BinaryPropertyEditor_downloadActionNameDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHRESULT, FieldReferenceType.DETAIL}
	)
	private String downloadActionName;

	/** 新しいタブで開くか */
	@MetaFieldInfo(
			displayName="新しいタブで開く",
			displayNameKey="generic_editor_BinaryPropertyEditor_openNewTabDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=150,
			description="リンククリック時にバイナリの内容を新しいタブで表示します。",
			descriptionKey="generic_editor_BinaryPropertyEditor_openNewTabDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHRESULT, FieldReferenceType.DETAIL}
	)
	private boolean openNewTab;

	/** PDF表示時にPDF.jsを利用 */
	@MetaFieldInfo(
			displayName="PDF表示時にPDF.jsを利用",
			displayNameKey="generic_editor_BinaryPropertyEditor_usePdfjsNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=160,
			description="PDF.jsを利用してPDFの表示を行います。<br>"
					+ "細工されたPDFによる情報詐取(JVNTA#94087669)の対策が必要な場合に利用てください。",
			descriptionKey="generic_editor_BinaryPropertyEditor_usePdfjsDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHRESULT, FieldReferenceType.DETAIL}
	)
	private boolean usePdfjs;

	/** ファイル選択ボタンを非表示する */
	@MetaFieldInfo(
			displayName="ファイル選択ボタンを非表示",
			displayNameKey="generic_editor_BinaryPropertyEditor_hideSelectButtonDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=170,
			description="編集画面にて当バイナリファイルがいない場合、ファイル選択ボタンを非表示にします。",
			descriptionKey="generic_editor_BinaryPropertyEditor_hideSelectButtonDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private boolean hideSelectButton;

	/** 削除ボタンを非表示する */
	@MetaFieldInfo(
			displayName="削除ボタンを非表示",
			displayNameKey="generic_editor_BinaryPropertyEditor_hideDeleteButtonDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=180,
			description="編集画面にて当バイナリファイルデータを削除するボタンを非表示にします。",
			descriptionKey="generic_editor_BinaryPropertyEditor_hideDeleteButtonDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private boolean hideDeleteButton;

	/**
	 * コンストラクタ
	 */
	public BinaryPropertyEditor() {
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
	 *  アップロードアクション名を設定します。
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

	@Override
	public BinaryDisplayType getDisplayType() {
		if (displayType == null) {
			displayType = BinaryDisplayType.BINARY;
		}
		return displayType;
	}

	/**
	 * 表示タイプを設定します。
	 * @param displayType
	 */
	public void setDisplayType(BinaryDisplayType displayType) {
		this.displayType = displayType;
	}

	@Override
	public boolean isHide() {
		return displayType == BinaryDisplayType.HIDDEN;
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

	@Override
	public String getDefaultValue() {
		// デフォルト値なし、空実装
		return null;
	}

	@Override
	public void setDefaultValue(String defaultValue) {
		// デフォルト値なし、空実装
	}
}
