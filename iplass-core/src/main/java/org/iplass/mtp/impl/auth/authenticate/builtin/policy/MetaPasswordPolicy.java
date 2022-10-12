/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.auth.authenticate.builtin.policy;

import java.util.List;

import org.iplass.mtp.auth.policy.definition.PasswordPolicyDefinition;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.i18n.MetaLocalizedString;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;

public class MetaPasswordPolicy implements MetaData {
	private static final long serialVersionUID = -2469096937639131962L;

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
	private String passwordPatternErrorMessage;
	/** パスワードの複雑度エラーのメッセージ(Localized) */
	private List<MetaLocalizedString> localizedPasswordPatternErrorMessageList;
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

	public int getMaximumPasswordAge() {
		return maximumPasswordAge;
	}

	public void setMaximumPasswordAge(int maximumPasswordAge) {
		this.maximumPasswordAge = maximumPasswordAge;
	}

	public int getMinimumPasswordAge() {
		return minimumPasswordAge;
	}

	public void setMinimumPasswordAge(int minimumPasswordAge) {
		this.minimumPasswordAge = minimumPasswordAge;
	}

	public String getPasswordPattern() {
		return passwordPattern;
	}

	public void setPasswordPattern(String passwordPattern) {
		this.passwordPattern = passwordPattern;
	}

	public boolean isDenySamePasswordAsAccountId() {
		return denySamePasswordAsAccountId;
	}

	public void setDenySamePasswordAsAccountId(boolean denySamePasswordAsAccountId) {
		this.denySamePasswordAsAccountId = denySamePasswordAsAccountId;
	}

	public String getDenyList() {
		return denyList;
	}

	public void setDenyList(String denyList) {
		this.denyList = denyList;
	}

	public String getPasswordPatternErrorMessage() {
		return passwordPatternErrorMessage;
	}

	public void setPasswordPatternErrorMessage(String passwordPatternErrorMessage) {
		this.passwordPatternErrorMessage = passwordPatternErrorMessage;
	}

	public List<MetaLocalizedString> getLocalizedPasswordPatternErrorMessageList() {
		return localizedPasswordPatternErrorMessageList;
	}

	public void setLocalizedPasswordPatternErrorMessageList(
			List<MetaLocalizedString> localizedPasswordPatternErrorMessageList) {
		this.localizedPasswordPatternErrorMessageList = localizedPasswordPatternErrorMessageList;
	}

	public int getPasswordHistoryCount() {
		return passwordHistoryCount;
	}

	public void setPasswordHistoryCount(int passwordHistoryCount) {
		this.passwordHistoryCount = passwordHistoryCount;
	}
	
	public int getPasswordHistoryPeriod() {
		return passwordHistoryPeriod;
	}

	public void setPasswordHistoryPeriod(int passwordHistoryPeriod) {
		this.passwordHistoryPeriod = passwordHistoryPeriod;
	}

	public boolean isCreateAccountWithSpecificPassword() {
		return createAccountWithSpecificPassword;
	}

	public void setCreateAccountWithSpecificPassword(
			boolean createAccountWithSpecificPassword) {
		this.createAccountWithSpecificPassword = createAccountWithSpecificPassword;
	}

	public boolean isResetPasswordWithSpecificPassword() {
		return resetPasswordWithSpecificPassword;
	}

	public void setResetPasswordWithSpecificPassword(
			boolean resetPasswordWithSpecificPassword) {
		this.resetPasswordWithSpecificPassword = resetPasswordWithSpecificPassword;
	}

	public String getRandomPasswordIncludeSigns() {
		return randomPasswordIncludeSigns;
	}

	public void setRandomPasswordIncludeSigns(String randomPasswordIncludeSigns) {
		this.randomPasswordIncludeSigns = randomPasswordIncludeSigns;
	}

	public String getRandomPasswordExcludeChars() {
		return randomPasswordExcludeChars;
	}

	public void setRandomPasswordExcludeChars(String randomPasswordExcludeChars) {
		this.randomPasswordExcludeChars = randomPasswordExcludeChars;
	}

	public int getRandomPasswordLength() {
		return randomPasswordLength;
	}

	public void setRandomPasswordLength(int randomPasswordLength) {
		this.randomPasswordLength = randomPasswordLength;
	}

	public int getMaximumRandomPasswordAge() {
		return maximumRandomPasswordAge;
	}

	public void setMaximumRandomPasswordAge(int maximumRandomPasswordAge) {
		this.maximumRandomPasswordAge = maximumRandomPasswordAge;
	}

	@Override
	public MetaPasswordPolicy copy() {
		return ObjectUtil.deepCopy(this);
	}

	public void applyConfig(PasswordPolicyDefinition def) {
		maximumPasswordAge = def.getMaximumPasswordAge();
		minimumPasswordAge = def.getMinimumPasswordAge();
		passwordPattern = def.getPasswordPattern();
		denySamePasswordAsAccountId = def.isDenySamePasswordAsAccountId();
		denyList = def.getDenyList();
		passwordPatternErrorMessage = def.getPasswordPatternErrorMessage();
		localizedPasswordPatternErrorMessageList = I18nUtil.toMeta(def.getLocalizedPasswordPatternErrorMessageList());
		passwordHistoryCount = def.getPasswordHistoryCount();
		passwordHistoryPeriod = def.getPasswordHistoryPeriod();
		createAccountWithSpecificPassword = def.isCreateAccountWithSpecificPassword();
		randomPasswordIncludeSigns = def.getRandomPasswordIncludeSigns();
		randomPasswordExcludeChars = def.getRandomPasswordExcludeChars();
		randomPasswordLength = def.getRandomPasswordLength();
		maximumRandomPasswordAge = def.getMaximumRandomPasswordAge();
		resetPasswordWithSpecificPassword = def.isResetPasswordWithSpecificPassword();
	}

	public PasswordPolicyDefinition currentConfig() {
		PasswordPolicyDefinition def = new PasswordPolicyDefinition();
		def.setMaximumPasswordAge(maximumPasswordAge);
		def.setMinimumPasswordAge(minimumPasswordAge);
		def.setPasswordPattern(passwordPattern);
		def.setDenySamePasswordAsAccountId(denySamePasswordAsAccountId);
		def.setDenyList(denyList);
		def.setPasswordPatternErrorMessage(passwordPatternErrorMessage);
		def.setLocalizedPasswordPatternErrorMessageList(I18nUtil.toDef(localizedPasswordPatternErrorMessageList));
		def.setPasswordHistoryCount(passwordHistoryCount);
		def.setPasswordHistoryPeriod(passwordHistoryPeriod);
		def.setCreateAccountWithSpecificPassword(createAccountWithSpecificPassword);
		def.setRandomPasswordIncludeSigns(randomPasswordIncludeSigns);
		def.setRandomPasswordExcludeChars(randomPasswordExcludeChars);
		def.setRandomPasswordLength(randomPasswordLength);
		def.setMaximumRandomPasswordAge(maximumRandomPasswordAge);
		def.setResetPasswordWithSpecificPassword(resetPasswordWithSpecificPassword);
		return def;
	}

}
