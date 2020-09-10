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

package org.iplass.mtp.view.top.parts;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.view.generic.editor.DateTimePropertyEditor.TimeDispRange;

/**
 * お知らせ一覧パーツ
 * @author lis3wg
 */
public class InformationParts extends ActionParts {

	/** SerialVersionUID */
	private static final long serialVersionUID = 1563822082137750617L;

	/** 多言語設定情報 */
	private List<LocalizedStringDefinition> localizedTitleList;

	/** アイコンタグ */
	private String iconTag;

	/** パスワード残日数の警告表示 */
	private Boolean showWarningPasswordAge;

	/** パスワード警告残日数 */
	private Integer passwordWarningAge;

	/** パスワード警告メッセージ */
	@MultiLang(itemKey = "passwordWarningMessage", itemGetter = "getPasswordWarningMessage", itemSetter = "setPasswordWarningMessage", multiLangGetter = "getLocalizedPasswordWarningMessageList", multiLangSetter = "setLocalizedPasswordWarningMessageList")
	private String passwordWarningMessage;

	/** パスワード警告メッセージ多言語設定情報 */
	private List<LocalizedStringDefinition> localizedPasswordWarningMessageList;

	/** パスワード警告表示領域スタイルクラス */
	private String passwordWarnAreaStyleClass;

	/** パスワード警告マークスタイルクラス */
	private String passwordWarnMarkStyleClass;

	/** HTML出力可否 */
	private boolean enableHtmlTag;

	/** リッチテキストエディタの利用 */
	private boolean useRichtextEditor;

	/** リッチテキストエディタオプション */
	private String richtextEditorOption;

	/** 詳細画面表示カスタムスタイル */
	private String detailCustomStyle;

	/** 一覧の表示件数 */
	private Integer numberOfDisplay;

	/**
	 * タイトルを取得します。
	 * @return タイトル
	 */
	@MultiLang(itemKey = "title", itemGetter = "getTitle", itemSetter = "setTitle", multiLangGetter = "getLocalizedTitleList", multiLangSetter = "setLocalizedTitleList")
	public String getTitle() {
		return getParam("title");
	}

	/**
	 * タイトルを設定します。
	 * @param title タイトル
	 */
	public void setTitle(String title) {
		setParam("title", title);
	}

	/**
	 * タイトルの多言語設定情報を取得します。
	 * @return タイトルの多言語設定情報リスト
	 */
	public List<LocalizedStringDefinition> getLocalizedTitleList() {
		return localizedTitleList;
	}

	/**
	 * タイトルの多言語設定情報を設定します。
	 * @param タイトルの多言語設定情報リスト
	 */
	public void setLocalizedTitleList(List<LocalizedStringDefinition> localizedTitleList) {
		this.localizedTitleList = localizedTitleList;
	}

	/**
	 * タイトルの多言語設定情報を追加します。
	 * @param タイトルの多言語設定情報
	 */
	public void addLocalizedTitle(LocalizedStringDefinition localizedTitle) {
		if (localizedTitleList == null) {
			localizedTitleList = new ArrayList<>();
		}

		localizedTitleList.add(localizedTitle);
	}

	/**
	 * アイコンタグを取得します。
	 * @return アイコンタグ
	 */
	public String getIconTag() {
	    return iconTag;
	}

	/**
	 * アイコンタグを設定します。
	 * @param iconTag アイコンタグ
	 */
	public void setIconTag(String iconTag) {
	    this.iconTag = iconTag;
	}

	/**
	 * 時間の表示範囲を取得します。
	 * @return 時間の表示範囲
	 */
	public TimeDispRange getDispRange() {
		String dispRange = getParam("dispRange");
		return dispRange != null ? TimeDispRange.valueOf(dispRange) : null;
	}

	/**
	 * 時間の表示範囲を設定します。
	 * @param dispRange 時間の表示範囲
	 */
	public void setDispRange(TimeDispRange dispRange) {
		if (dispRange != null) {
			setParam("dispRange", dispRange.name());
		} else {
			setParam("dispRange", null);
		}
	}


	/**
	 * パスワード残日数の警告表示を取得します。
	 * @return パスワード残日数の警告表示
	 */
	public Boolean isShowWarningPasswordAge() {
		return showWarningPasswordAge != null ? showWarningPasswordAge : false;
	}

	/**
	 * パスワード残日数の警告表示を設定します。
	 * @param showWarningPasswordAge パスワード残日数の警告表示
	 */
	public void setShowWarningPasswordAge(Boolean showWarningPasswordAge) {
		this.showWarningPasswordAge = showWarningPasswordAge;
	}

	/**
	 * パスワード警告残日数を取得します。
	 * @return パスワード警告残日数
	 */
	public Integer getPasswordWarningAge() {
		return passwordWarningAge != null ? passwordWarningAge : 0;
	}

	/**
	 * パスワード警告残日数を設定します。
	 * @param passwordWarningAge パスワード警告残日数
	 */
	public void setPasswordWarningAge(Integer passwordWarningAge) {
		this.passwordWarningAge = passwordWarningAge;
	}

	/**
	 * パスワード警告メッセージを取得します。
	 * @return パスワード警告メッセージ
	 */
	public String getPasswordWarningMessage() {
		return passwordWarningMessage;
	}

	/**
	 * パスワード警告メッセージを設定します。
	 * @param passwordWarningMessage パスワード警告メッセージ
	 */
	public void setPasswordWarningMessage(String passwordWarningMessage) {
		this.passwordWarningMessage = passwordWarningMessage;
	}

	/**
	 * パスワード警告メッセージの多言語設定情報を取得します。
	 * @return パスワード警告メッセージの多言語設定情報リスト
	 */
	public List<LocalizedStringDefinition> getLocalizedPasswordWarningMessageList() {
		return localizedPasswordWarningMessageList;
	}

	/**
	 * パスワード警告メッセージの多言語設定情報を設定します。
	 * @param パスワード警告メッセージの多言語設定情報リスト
	 */
	public void setLocalizedPasswordWarningMessageList(List<LocalizedStringDefinition> localizedPasswordWarningMessageList) {
		this.localizedPasswordWarningMessageList = localizedPasswordWarningMessageList;
	}

	/**
	 * パスワード警告メッセージの多言語設定情報を追加します。
	 * @param パスワード警告メッセージの多言語設定情報
	 */
	public void addLocalizedPasswordWarningMessage(LocalizedStringDefinition localizedPasswordWarningMessage) {
		if (localizedPasswordWarningMessageList == null) {
			localizedPasswordWarningMessageList = new ArrayList<>();
		}

		localizedPasswordWarningMessageList.add(localizedPasswordWarningMessage);
	}

	/**
	 * パスワード警告表示領域スタイルクラスを取得します。
	 * @return パスワード警告表示領域スタイルクラス
	 */
	public String getPasswordWarnAreaStyleClass() {
		return passwordWarnAreaStyleClass;
	}

	/**
	 * パスワード警告表示領域スタイルクラスを設定します。
	 * @param passwordWarnAreaStyleClass パスワード警告表示領域スタイルクラス
	 */
	public void setPasswordWarnAreaStyleClass(String passwordWarnAreaStyleClass) {
		this.passwordWarnAreaStyleClass = passwordWarnAreaStyleClass;
	}

	/**
	 * パスワード警告マークスタイルクラスを取得します。
	 * @return パスワード警告マークスタイルクラス
	 */
	public String getPasswordWarnMarkStyleClass() {
		return passwordWarnMarkStyleClass;
	}

	/**
	 * パスワード警告マークスタイルクラスを設定します。
	 * @param passwordWarnMarkStyleClass パスワード警告マークスタイルクラス
	 */
	public void setPasswordWarnMarkStyleClass(String passwordWarnMarkStyleClass) {
		this.passwordWarnMarkStyleClass = passwordWarnMarkStyleClass;
	}

	/**
	 * HTML出力可否を取得します。
	 * @return HTML出力可否
	 */
	public boolean isEnableHtmlTag() {
		return enableHtmlTag;
	}

	/**
	 * HTML出力可否を設定します。
	 * @param enableHtmlTag HTML出力可否
	 */
	public void setEnableHtmlTag(boolean enableHtmlTag) {
		this.enableHtmlTag = enableHtmlTag;
	}

	/**
	 * リッチテキストエディタの利用を取得します。
	 * @return リッチテキストエディタの利用
	 */
	public boolean isUseRichtextEditor() {
		return useRichtextEditor;
	}

	/**
	 * リッチテキストエディタの利用を設定します。
	 * @param useRichtextEditor リッチテキストエディタの利用
	 */
	public void setUseRichtextEditor(boolean useRichtextEditor) {
		this.useRichtextEditor = useRichtextEditor;
	}

	/**
	 * リッチテキストエディタオプションを取得します。
	 * @return リッチテキストエディタオプション
	 */
	public String getRichtextEditorOption() {
		return richtextEditorOption;
	}

	/**
	 * リッチテキストエディタオプションを設定します。
	 * @param richtextEditorOption リッチテキストエディタオプション
	 */
	public void setRichtextEditorOption(String richtextEditorOption) {
		this.richtextEditorOption = richtextEditorOption;
	}

	/**
	 * 詳細画面表示カスタムスタイルを取得します。
	 * @return 詳細画面表示カスタムスタイル
	 */
	public String getDetailCustomStyle() {
		return detailCustomStyle;
	}

	/**
	 * 詳細画面表示カスタムスタイルを設定します。
	 * @param detailCustomStyle 詳細画面表示カスタムスタイル
	 */
	public void setDetailCustomStyle(String detailCustomStyle) {
		this.detailCustomStyle = detailCustomStyle;
	}

	/**
	 * 一覧の表示件数を取得します。
	 * @return 一覧の表示件数
	 */
	public Integer getNumberOfDisplay() {
	    return numberOfDisplay;
	}

	/**
	 * 一覧の表示件数を設定します。
	 * @param numberOfDisplay 一覧の表示件数
	 */
	public void setNumberOfDisplay(Integer numberOfDisplay) {
	    this.numberOfDisplay = numberOfDisplay;
	}

}
