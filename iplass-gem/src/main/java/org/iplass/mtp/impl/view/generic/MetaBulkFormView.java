/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.view.generic;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.i18n.MetaLocalizedString;
import org.iplass.mtp.view.generic.BulkFormView;
import org.iplass.mtp.view.generic.FormView;

@XmlAccessorType(XmlAccessType.FIELD)
public class MetaBulkFormView extends MetaFormView {

	private static final long serialVersionUID = -7141897000334457139L;

	public static MetaBulkFormView createInstance(FormView view) {
		return new MetaBulkFormView();
	}

	/** 更新アクション名 */
	private String updateActionName;
	
	/** 検索条件で更新アクション名*/
	private String updateAllActionName;

	/** 更新ボタン表示ラベル */
	private String updateDisplayLabel;

	/** 多言語設定情報 */
	private List<MetaLocalizedString> localizedUpdateDisplayLabelList = new ArrayList<MetaLocalizedString>();

	/** Javascriptコード有効可否 */
	private Boolean validJavascriptBulkPage;

	/** 親子関係の参照を物理削除するか */
	private boolean purgeCompositionedEntity;

	/** 更新時に強制的に更新処理を行う */
	private boolean forceUpadte;

	/** カスタム登録処理クラス名 */
	private String interrupterName;

	/** カスタムロード処理クラス名 */
	private String loadEntityInterrupterName;

	/** JavaScript */
	private String javaScript;

	/**
	 * 更新アクション名を取得します。
	 * @return 更新アクション名
	 */
	public String getUpdateActionName() {
		return updateActionName;
	}

	/**
	 * 更新アクション名を設定します
	 * @param updateActionName 更新アクション名
	 */
	public void setUpdateActionName(String updateActionName) {
		this.updateActionName = updateActionName;
	}

	/**
	 * 検索条件で更新アクション名を取得します。
	 * @return 検索条件で更新アクション名
	 */
	public String getUpdateAllActionName() {
		return updateAllActionName;
	}

	/**
	 * 検索条件で更新アクション名を設定します。
	 * @param updateAllActionName 検索条件で更新アクション名
	 */
	public void setUpdateAllActionName(String updateAllActionName) {
		this.updateAllActionName = updateAllActionName;
	}

	/**
	 * 更新ボタン表示ラベルを取得します。
	 * @return 更新ボタン表示ラベル
	 */
	public String getUpdateDisplayLabel() {
		return updateDisplayLabel;
	}

	/**
	 * 更新ボタン表示ラベルを設定します。
	 * @param updateDisplayLabel 更新ボタン表示ラベル
	 */
	public void setUpdateDisplayLabel(String updateDisplayLabel) {
		this.updateDisplayLabel = updateDisplayLabel;
	}

	/**
	 *  多言語設定情報を取得します。
	 * @return 多言語設定情報
	 */
	public List<MetaLocalizedString> getLocalizedUpdateDisplayLabelList() {
		return localizedUpdateDisplayLabelList;
	}

	/**
	 *  多言語設定情報を設定します。
	 * @param localizedUpdateDisplayLabelList 多言語設定情報
	 */
	public void setLocalizedUpdateDisplayLabelList(List<MetaLocalizedString> localizedUpdateDisplayLabelList) {
		this.localizedUpdateDisplayLabelList = localizedUpdateDisplayLabelList;
	}

	/**
	 * Javascriptコード有効可否を取得します。
	 * @return Javascriptコード有効可否
	 */
	public Boolean getValidJavascriptBulkPage() {
		return validJavascriptBulkPage;
	}

	/**
	 * Javascriptコード有効可否を設定します。
	 * @param validJavascriptBulkPage Javascriptコード有効可否
	 */
	public void setValidJavascriptBulkPage(Boolean validJavascriptBulkPage) {
		this.validJavascriptBulkPage = validJavascriptBulkPage;
	}

	/**
	 * 親子関係の参照を物理削除するかを取得します。
	 * @return 親子関係の参照を物理削除するか
	 */
	public boolean isPurgeCompositionedEntity() {
		return purgeCompositionedEntity;
	}

	/**
	 * 親子関係の参照を物理削除するかを設定します。
	 * @param purgeCompositionedEntity 親子関係の参照を物理削除するか
	 */
	public void setPurgeCompositionedEntity(boolean purgeCompositionedEntity) {
		this.purgeCompositionedEntity = purgeCompositionedEntity;
	}

	/**
	 * 更新時に強制的に更新処理を行うかを取得します。
	 * @return 更新時に強制的に更新処理を行うか
	 */
	public boolean isForceUpadte() {
		return forceUpadte;
	}

	/**
	 * 更新時に強制的に更新処理を行うかを設定します。
	 * @param forceUpadte 更新時に強制的に更新処理を行うか
	 */
	public void setForceUpadte(boolean forceUpadte) {
		this.forceUpadte = forceUpadte;
	}

	/**
	 * カスタム登録処理クラス名を取得します。
	 * @return カスタム登録処理クラス名
	 */
	public String getInterrupterName() {
		return interrupterName;
	}

	/**
	 * カスタム登録処理クラス名を設定します。
	 * @param interrupterName カスタム登録処理クラス名
	 */
	public void setInterrupterName(String interrupterName) {
		this.interrupterName = interrupterName;
	}

	/**
	 * カスタムロード処理クラス名を取得します。
	 * @return カスタムロード処理クラス名 
	 */
	public String getLoadEntityInterrupterName() {
		return loadEntityInterrupterName;
	}

	/**
	 * カスタムロード処理クラス名を設定します。
	 * @param loadEntityInterrupterName カスタムロード処理クラス名
	 */
	public void setLoadEntityInterrupterName(String loadEntityInterrupterName) {
		this.loadEntityInterrupterName = loadEntityInterrupterName;
	}

	/**
	 * JavaScriptを取得します。
	 * @return JavaScript
	 */
	public String getJavaScript() {
		return javaScript;
	}

	/**
	 * JavaScriptを設定します。
	 * @param javaScript JavaScript
	 */
	public void setJavaScript(String javaScript) {
		this.javaScript = javaScript;
	}

	@Override
	public void applyConfig(FormView form, String definitionId) {
		super.fillFrom(form, definitionId);

		BulkFormView bForm = (BulkFormView) form;
		updateActionName = bForm.getUpdateActionName();
		updateAllActionName = bForm.getUpdateAllActionName();
		updateDisplayLabel = bForm.getUpdateDisplayLabel();
		localizedUpdateDisplayLabelList = I18nUtil.toMeta(bForm.getLocalizedUpdateDisplayLabelList());
		purgeCompositionedEntity = bForm.isPurgeCompositionedEntity();
		forceUpadte = bForm.isForceUpadte();
		javaScript = bForm.getJavaScript();
		validJavascriptBulkPage = bForm.isValidJavascriptBulkPage();
		interrupterName = bForm.getInterrupterName();
		loadEntityInterrupterName = bForm.getLoadEntityInterrupterName();

	}

	@Override
	public FormView currentConfig(String definitionId) {
		BulkFormView form = new BulkFormView();
		super.fillTo(form, definitionId);

		form.setUpdateActionName(updateActionName);
		form.setUpdateAllActionName(updateAllActionName);
		form.setUpdateDisplayLabel(updateDisplayLabel);
		form.setLocalizedUpdateDisplayLabelList(I18nUtil.toDef(localizedUpdateDisplayLabelList));
		form.setPurgeCompositionedEntity(purgeCompositionedEntity);
		form.setForceUpadte(forceUpadte);
		form.setJavaScript(javaScript);
		form.setInterrupterName(interrupterName);
		form.setLoadEntityInterrupterName(loadEntityInterrupterName);
		if (validJavascriptBulkPage != null) {
			form.setValidJavascriptBulkPage(validJavascriptBulkPage);
		} else {
			// 未設定なら有効扱い
			form.setValidJavascriptBulkPage(true);
		}

		return form;
	}

	@Override
	public FormViewRuntime createRuntime(EntityViewRuntime entityView) {
		return new BulkFormViewHandler(this, entityView);
	}

}
