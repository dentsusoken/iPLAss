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

package org.iplass.mtp.impl.view.top.parts;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.i18n.MetaLocalizedString;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.view.generic.editor.DateTimePropertyEditor.TimeDispRange;
import org.iplass.mtp.view.top.parts.InformationParts;
import org.iplass.mtp.view.top.parts.TopViewParts;
import org.iplass.mtp.web.template.TemplateUtil;

/**
 * お知らせ一覧パーツ
 * @author lis3wg
 */
public class MetaInformationParts extends MetaActionParts {

	/** SerialVersionUID */
	private static final long serialVersionUID = 4886385116507508802L;

	/**
	 * インスタンスを生成します。
	 * @param parts TOP画面パーツ
	 * @return インスタンス
	 */
	public static MetaInformationParts createInstance(TopViewParts parts) {
		return new MetaInformationParts();
	}

	/** タイトル */
	private String title;

	/** タイトル多言語設定情報 */
	private List<MetaLocalizedString> localizedTitleList = new ArrayList<MetaLocalizedString>();

	/** アイコンタグ */
	private String iconTag;

	/** 時間の表示範囲 */
	private TimeDispRange dispRange;

	/** パスワード残日数の警告表示 */
	private Boolean showWarningPasswordAge;

	/** パスワード警告残日数 */
	private Integer passwordWarningAge;

	/** パスワード警告メッセージ */
	private String passwordWarningMessage;

	/** パスワード警告メッセージ多言語設定情報 */
	private List<MetaLocalizedString> localizedPasswordWarningMessageList = new ArrayList<MetaLocalizedString>();;

	/** パスワード警告表示領域スタイルクラス */
	private String passwordWarnAreaStyleClass;

	/** パスワード警告マークスタイルクラス */
	private String passwordWarnMarkStyleClass;

	/** HTML出力可否 */
	private boolean availableHtmlTag = false;

	/** 一覧の表示件数 */
	private Integer numberOfDisplay;

	/**
	 * タイトルを取得します。
	 * @return タイトル
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * タイトルを設定します。
	 * @param title タイトル
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * タイトル多言語設定情報を取得します。
	 * @return タイトル多言語設定情報リスト
	 */
	public List<MetaLocalizedString> getLocalizedTitleList() {
		return localizedTitleList;
	}

	/**
	 * タイトル多言語設定情報を設定します。
	 * @param タイトル多言語設定情報リスト
	 */
	public void setLocalizedTitleList(List<MetaLocalizedString> localizedTitleList) {
		this.localizedTitleList = localizedTitleList;
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
		return dispRange;
	}

	/**
	 * 時間の表示範囲を設定します。
	 * @param dispRange 時間の表示範囲
	 */
	public void setDispRange(TimeDispRange dispRange) {
		this.dispRange = dispRange;
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
	public List<MetaLocalizedString> getLocalizedPasswordWarningMessageList() {
		return localizedPasswordWarningMessageList;
	}

	/**
	 * パスワード警告メッセージの多言語設定情報を設定します。
	 * @param パスワード警告メッセージの多言語設定情報リスト
	 */
	public void setLocalizedPasswordWarningMessageList(List<MetaLocalizedString> localizedPasswordWarningMessageList) {
		this.localizedPasswordWarningMessageList = localizedPasswordWarningMessageList;
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
	public boolean isAvailableHtmlTag() {
		return availableHtmlTag;
	}

	/**
	 * HTML出力可否を設定します。
	 * @param availableHtmlTag HTML出力可否
	 */
	public void setAvailableHtmlTag(boolean availableHtmlTag) {
		this.availableHtmlTag = availableHtmlTag;
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


	@Override
	public void applyConfig(TopViewParts parts) {
		InformationParts definition = (InformationParts)parts;
		title = definition.getTitle();
		dispRange = definition.getDispRange();

		// 言語毎の文字情報設定
		localizedTitleList = I18nUtil.toMeta(definition.getLocalizedTitleList());
		iconTag = definition.getIconTag();
		style = definition.getStyle();

		showWarningPasswordAge = definition.isShowWarningPasswordAge();
		passwordWarningAge = definition.getPasswordWarningAge();
		passwordWarningMessage = definition.getPasswordWarningMessage();
		localizedPasswordWarningMessageList = I18nUtil.toMeta(definition.getLocalizedPasswordWarningMessageList());
		passwordWarnAreaStyleClass = definition.getPasswordWarnAreaStyleClass();
		passwordWarnMarkStyleClass = definition.getPasswordWarnMarkStyleClass();
		availableHtmlTag = definition.isEnableHtmlTag();
		numberOfDisplay = definition.getNumberOfDisplay();
	}

	@Override
	public TopViewParts currentConfig() {
		InformationParts parts = new InformationParts();
		parts.setTitle(title);
		parts.setDispRange(dispRange);
		parts.setLocalizedTitleList(I18nUtil.toDef(localizedTitleList));
		parts.setIconTag(iconTag);
		parts.setShowWarningPasswordAge(showWarningPasswordAge);
		parts.setPasswordWarningAge(passwordWarningAge);
		parts.setPasswordWarningMessage(passwordWarningMessage);
		parts.setLocalizedPasswordWarningMessageList(I18nUtil.toDef(localizedPasswordWarningMessageList));
		parts.setPasswordWarnAreaStyleClass(passwordWarnAreaStyleClass);
		parts.setPasswordWarnMarkStyleClass(passwordWarnMarkStyleClass);
		parts.setEnableHtmlTag(availableHtmlTag);
		parts.setNumberOfDisplay(numberOfDisplay);
		parts.setStyle(style);

		return parts;
	}

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public ActionPartsHandler createRuntime() {
		return new ActionPartsHandler(this) {
			private static final String ACTION_NAME_GEM = "gem/information/list";

			@Override
			public boolean isParts() {
				return true;
			}

			@Override
			public boolean isWidget() {
				return false;
			}

			@Override
			public String getActionNameForParts(HttpServletRequest req) {
				return ACTION_NAME_GEM;
			}

			@Override
			public String getActionNameForWidget(HttpServletRequest req) {
				return null;
			}

			@Override
			public void setAttribute(HttpServletRequest req) {
				RequestContext request = TemplateUtil.getRequestContext();
				request.setAttribute(Constants.INFO_SETTING, currentConfig());
			}

			@Override
			public void clearAttribute(HttpServletRequest req) {
				RequestContext request = TemplateUtil.getRequestContext();
				request.setAttribute(Constants.INFO_SETTING, null);
			}
		};
	}
}
