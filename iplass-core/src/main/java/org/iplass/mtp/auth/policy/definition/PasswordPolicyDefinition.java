/*
 * Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.auth.policy.definition;

import java.io.Serializable;
import java.util.List;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.mtp.definition.LocalizedStringDefinition;

/**
 * アカウントのパスワード制御のポリシー定義。
 *
 * @author K.Higuchi
 *
 */
public class PasswordPolicyDefinition implements Serializable {
	private static final long serialVersionUID = 5571368654587947847L;

	/** パスワードが有効な最大期間 （日）。0は無限。*/
	private int maximumPasswordAge;
	/** パスワード変更に最低限必要な期間（日） */
	private int minimumPasswordAge;
	/** パスワードの複雑度チェックに利用する正規表現 */
	private String passwordPattern;
	/**アカウントIDと同一のパスワードを不許可とするか */
	private boolean denySamePasswordAsAccountId;
	/** パスワードの拒否リスト。複数指定する場合は改行で区切ってください。 */
	private String denyList;
	/** パスワードの複雑度エラーのメッセージ */
	@MultiLang(itemKey="passwordPatternErrorMessage", itemGetter="getPasswordPatternErrorMessage", itemSetter="setPasswordPatternErrorMessage", multiLangGetter="getLocalizedPasswordPatternErrorMessageList", multiLangSetter="setLocalizedPasswordPatternErrorMessageList")
	private String passwordPatternErrorMessage;
	/** パスワードの複雑度エラーのメッセージ(Localized) */
	private List<LocalizedStringDefinition> localizedPasswordPatternErrorMessageList;
	/** 過去入力したパスワードを覚えておく個数。履歴に残っているパスワードは設定できない。厳密に言うと、パスワードのハッシュを記録するので、異なるパスワードでも衝突の可能性はある。 */
	private int passwordHistoryCount;
	/** 過去入力したパスワードを覚えておく期間（日）。履歴に残っているパスワードは設定できない。厳密に言うと、パスワードのハッシュを記録するので、異なるパスワードでも衝突の可能性はある。 */
	private int passwordHistoryPeriod;

	//アカウント作成の際、初期パスワードを指定可能とするか
	private boolean createAccountWithSpecificPassword;
	//パスワードリセットの際、パスワードを指定可能とするか
	private boolean resetPasswordWithSpecificPassword;
	//自動生成パスワード設定
	/** 自動生成パスワード設定記号文字列 */
	private String randomPasswordIncludeSigns;
	/** 自動生成パスワード設定除外文字列 */
	private String randomPasswordExcludeChars;
	/** 自動生成パスワードの長さ */
	private int randomPasswordLength;
	/** 自動生成パスワードが有効な最大期間 （日）。0は無限。*/
	private int maximumRandomPasswordAge;
	/** ユーザー終了日をカスタムで設定するScript */
	private String customUserEndDate;

	/**
	 * パスワードが有効な最大期間 （日）。0は無限。
	 * @return
	 */
	public int getMaximumPasswordAge() {
		return maximumPasswordAge;
	}

	/**
	 * @see #getMaximumPasswordAge()
	 * @param maximumPasswordAge
	 */
	public void setMaximumPasswordAge(int maximumPasswordAge) {
		this.maximumPasswordAge = maximumPasswordAge;
	}

	/**
	 * パスワード変更に最低限必要な期間（日）。
	 *
	 * @return
	 */
	public int getMinimumPasswordAge() {
		return minimumPasswordAge;
	}

	/**
	 * @see #getMinimumPasswordAge()
	 * @param minimumPasswordAge
	 */
	public void setMinimumPasswordAge(int minimumPasswordAge) {
		this.minimumPasswordAge = minimumPasswordAge;
	}

	/**
	 * パスワードの複雑度チェックに利用する正規表現。
	 *
	 * @return
	 */
	public String getPasswordPattern() {
		return passwordPattern;
	}

	/**
	 * @see #getPasswordPattern()
	 * @param passwordPattern
	 */
	public void setPasswordPattern(String passwordPattern) {
		this.passwordPattern = passwordPattern;
	}

	/**
	 * アカウントIDと同一のパスワードを不許可とするか
	 *
	 * @return
	 */
	public boolean isDenySamePasswordAsAccountId() {
		return denySamePasswordAsAccountId;
	}

	/**
	 * @see #isDenySamePasswordAsAccountId()
	 * @param denySamePasswordAsAccountId
	 */
	public void setDenySamePasswordAsAccountId(boolean denySamePasswordAsAccountId) {
		this.denySamePasswordAsAccountId = denySamePasswordAsAccountId;
	}

	/**
	 * パスワードの拒否リスト。複数指定する場合は改行で区切ってください。
	 *
	 * @return
	 */
	public String getDenyList() {
		return denyList;
	}

	/**
	 * @see #getDenyList()
	 * @param denyList
	 */
	public void setDenyList(String denyList) {
		this.denyList = denyList;
	}

	/**
	 * パスワードの複雑度エラー時のメッセージ。
	 * @return
	 */
	public String getPasswordPatternErrorMessage() {
		return passwordPatternErrorMessage;
	}

	/**
	 * @see #getPasswordPatternErrorMessage()
	 * @param passwordPatternErrorMessage
	 */
	public void setPasswordPatternErrorMessage(String passwordPatternErrorMessage) {
		this.passwordPatternErrorMessage = passwordPatternErrorMessage;
	}

	/**
	 * パスワードの複雑度エラーのメッセージ(Localized)。
	 * @return
	 */
	public List<LocalizedStringDefinition> getLocalizedPasswordPatternErrorMessageList() {
		return localizedPasswordPatternErrorMessageList;
	}

	/**
	 * @see #getLocalizedPasswordPatternErrorMessageList()
	 * @param localizedPasswordPatternErrorMessageList
	 */
	public void setLocalizedPasswordPatternErrorMessageList(
			List<LocalizedStringDefinition> localizedPasswordPatternErrorMessageList) {
		this.localizedPasswordPatternErrorMessageList = localizedPasswordPatternErrorMessageList;
	}

	/**
	 * 過去入力したパスワードを覚えておく個数。
	 * 履歴に残っているパスワードは設定できない。
	 * （厳密に言うと、パスワードのハッシュを記録するので、異なるパスワードでも衝突の可能性はある。）
	 * @return
	 */
	public int getPasswordHistoryCount() {
		return passwordHistoryCount;
	}

	/**
	 * @see #getPasswordHistoryCount()
	 * @param passwordHistoryCount
	 */
	public void setPasswordHistoryCount(int passwordHistoryCount) {
		this.passwordHistoryCount = passwordHistoryCount;
	}
	
	/**
	 * 過去入力したパスワードを覚えておく期間（日）。
	 * 履歴に残っているパスワードは設定できない。
	 * （厳密に言うと、パスワードのハッシュを記録するので、異なるパスワードでも衝突の可能性はある。）
	 * @return
	 */
	public int getPasswordHistoryPeriod() {
		return passwordHistoryPeriod;
	}

	/**
	 * @see #getPasswordHistoryPeriod()
	 * @param passwordHistoryPeriod
	 */
	public void setPasswordHistoryPeriod(int passwordHistoryPeriod) {
		this.passwordHistoryPeriod = passwordHistoryPeriod;
	}

	/**
	 * アカウント作成の際、初期パスワードを指定可能とするか。<br>
	 * trueの場合：パスワードが指定されていれば、そのパスワードでアカウントを作成。未指定の場合は自動生成。<br>
	 * falseの場合：パスワード指定出来ない。アカウント作成時に自動生成される。
	 *
	 * @return
	 */
	public boolean isCreateAccountWithSpecificPassword() {
		return createAccountWithSpecificPassword;
	}

	/**
	 * @see #isCreateAccountWithSpecificPassword()
	 * @param createAccountWithSpecificPassword
	 */
	public void setCreateAccountWithSpecificPassword(
			boolean createAccountWithSpecificPassword) {
		this.createAccountWithSpecificPassword = createAccountWithSpecificPassword;
	}

	/**
	 * パスワードリセットの際、プログラム側からパスワード指定を許すかどうか。<br>
	 * trueの場合：パスワードが指定されていれば、そのパスワードをセット。未指定の場合は自動生成。<br>
	 * falseの場合：パスワード指定出来ない。自動生成されたパスワードでリセットされる。
	 *
	 * @return
	 */
	public boolean isResetPasswordWithSpecificPassword() {
		return resetPasswordWithSpecificPassword;
	}

	/**
	 * @see #isResetPasswordWithSpecificPassword()
	 * @param resetPasswordWithSpecificPassword
	 */
	public void setResetPasswordWithSpecificPassword(
			boolean resetPasswordWithSpecificPassword) {
		this.resetPasswordWithSpecificPassword = resetPasswordWithSpecificPassword;
	}

	/**
	 * 自動生成パスワード生成時に、利用する記号文字列。
	 *
	 * @return
	 */
	public String getRandomPasswordIncludeSigns() {
		return randomPasswordIncludeSigns;
	}

	/**
	 * @see #getRandomPasswordIncludeSigns()
	 * @param randomPasswordIncludeSigns
	 */
	public void setRandomPasswordIncludeSigns(String randomPasswordIncludeSigns) {
		this.randomPasswordIncludeSigns = randomPasswordIncludeSigns;
	}

	/**
	 * 自動生成パスワード生成時に、利用しない文字。
	 * 例えば、"0oO1l"など。判別が難しい文字など指定。
	 *
	 * @return
	 */
	public String getRandomPasswordExcludeChars() {
		return randomPasswordExcludeChars;
	}

	/**
	 * @see #getRandomPasswordExcludeChars()
	 * @param randomPasswordExcludeChars
	 */
	public void setRandomPasswordExcludeChars(String randomPasswordExcludeChars) {
		this.randomPasswordExcludeChars = randomPasswordExcludeChars;
	}

	/**
	 * 自動生成パスワード生成時のパスワードの長さ。
	 * @return
	 */
	public int getRandomPasswordLength() {
		return randomPasswordLength;
	}

	/**
	 * @see #getRandomPasswordLength()
	 * @param randomPasswordLength
	 */
	public void setRandomPasswordLength(int randomPasswordLength) {
		this.randomPasswordLength = randomPasswordLength;
	}
	
	/**
	 * 自動生成パスワードが有効な最大期間 （日）。0は無限。
	 * @return
	 */
	public int getMaximumRandomPasswordAge() {
		return maximumRandomPasswordAge;
	}

	/**
	 * @see #getMaximumRandomPasswordAge()
	 * @param maximumRandomPasswordAge
	 */
	public void setMaximumRandomPasswordAge(int maximumRandomPasswordAge) {
		this.maximumRandomPasswordAge = maximumRandomPasswordAge;
	}

	/**
	 * ユーザー終了日をカスタムで設定するScript
	 * @return
	 */
	public String getCustomUserEndDate() {
		return customUserEndDate;
	}

	/**
	 * @see #getCustomUserEndDate()
	 * @param customUserEndDate
	 */
	public void setCustomUserEndDate(String customUserEndDate) {
		this.customUserEndDate = customUserEndDate;
	}

}
