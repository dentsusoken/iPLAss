/*
 * Copyright (C) 2019 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.view.generic;

import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.mtp.definition.LocalizedStringDefinition;

@XmlAccessorType(XmlAccessType.FIELD)
public class BulkFormView extends FormView {

	private static final long serialVersionUID = 8695940686714160111L;

	/** 更新ボタン表示ラベル */
	@MetaFieldInfo(
			displayName="更新ボタン表示ラベル",
			displayNameKey="generic_BulkFormView_updateDisplayLabelDisplaNameKey",
			description="更新ボタンに表示されるラベルを設定します。",
			descriptionKey="generic_BulkFormView_updateDisplayLabelDescriptionKey",
			inputType=InputType.MULTI_LANG,
			multiLangField = "localizedUpdateDisplayLabelList",
			displayOrder=200
	)
	@MultiLang()
	private String updateDisplayLabel;

	/** 多言語設定情報 */
	@MetaFieldInfo(
			displayName="多言語設定",
			displayNameKey="generic_BulkFormView_localizedUpdateDisplayLabelListDisplaNameKey",
			inputType=InputType.MULTI_LANG_LIST,
			displayOrder=210
	)
	private List<LocalizedStringDefinition> localizedUpdateDisplayLabelList;




	/** 更新アクション名 */
	@MetaFieldInfo(
			displayName="更新アクション名",
			displayNameKey="generic_BulkFormView_updateActionNameDisplaNameKey",
			inputType=InputType.ACTION,
			displayOrder=300,
			description="更新ボタンクリックで実行されるアクションを設定します。",
			descriptionKey="generic_BulkFormView_updateActionNameDescriptionKey"
	)
	private String updateActionName;

	/** 検索条件で更新アクション名 */
	@MetaFieldInfo(
			displayName="検索条件で更新アクション名",
			displayNameKey="generic_BulkFormView_updateAllActionNameDisplaNameKey",
			inputType=InputType.ACTION,
			displayOrder=310,
			description="更新ボタンクリックで実行されるアクションを設定します。",
			descriptionKey="generic_BulkFormView_updateAllActionNameDescriptionKey"
	)
	private String updateAllActionName;




	/** JavaScriptコード */
	@MetaFieldInfo(
			displayName="JavaScriptコード",
			displayNameKey="generic_BulkFormView_javaScriptDisplaNameKey",
			inputType=InputType.SCRIPT,
			displayOrder=500,
			mode="javascript",
			description="SCRIPTタグ内に出力するJavaScriptコードを設定します。",
			descriptionKey="generic_BulkFormView_javaScriptDescriptionKey"
	)
	private String javaScript;

	/** Javascriptコード有効可否 */
	@MetaFieldInfo(
			displayName="Javascriptコードを有効化",
			displayNameKey="generic_BulkFormView_validJavascriptBulkPageDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=510,
			description="Javascriptコードに設定した内容を一括更新画面で有効にするかを設定します。",
			descriptionKey="generic_BulkFormView_validJavascriptBulkPageDescriptionKey"
	)
	private boolean validJavascriptBulkPage;




	/** 親子関係の参照を物理削除するか */
	@MetaFieldInfo(
			displayName="親子関係の参照を物理削除するか",
			displayNameKey="generic_BulkFormView_purgeCompositionedEntityDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=1600,
			description="親子関係の参照を物理削除するかを設定します",
			descriptionKey="generic_BulkFormView_purgeCompositionedEntityDescriptionKey"
	)
	private boolean purgeCompositionedEntity;

	/** 更新時に強制的に更新処理を行う */
	@MetaFieldInfo(
			displayName="更新時に強制的に更新処理を行う",
			displayNameKey="generic_BulkFormView_forceUpadteDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=1610,
			description="変更項目が一つもなくとも、強制的に更新処理（更新日時、更新者が更新される）を行います。",
			descriptionKey="generic_BulkFormView_forceUpadteDescriptionKey"
	)
	private boolean forceUpadte;

	/** カスタム登録処理クラス名 */
	@MetaFieldInfo(
			displayName="カスタム登録処理クラス名",
			displayNameKey="generic_BulkFormView_interrupterNameDisplaNameKey",
			displayOrder=1620,
			description="データ登録時に行うカスタム登録処理のクラス名を指定します。<br>" +
					"RegistrationInterrupterインターフェースを実装するクラスを指定してください。",

			descriptionKey="generic_BulkFormView_interrupterNameDescriptionKey"
	)
	private String interrupterName;

	/** カスタムロード処理クラス名 */
	@MetaFieldInfo(
			displayName="カスタムロード処理クラス名",
			displayNameKey="generic_BulkFormView_loadEntityInterrupterNameDisplaNameKey",
			displayOrder=1630,
			description="Entityロード処理実行前にロード用のオプションをカスタマイズするためのクラス名を指定します。<br>" +
					"LoadEntityInterrupterインターフェースを実装するクラスを指定してください。",

			descriptionKey="generic_BulkFormView_loadEntityInterrupterNameDescriptionKey"
	)
	private String loadEntityInterrupterName;

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
	 * 多言語設定情報を取得します。
	 * @return 多言語設定情報
	 */
	public List<LocalizedStringDefinition> getLocalizedUpdateDisplayLabelList() {
		return localizedUpdateDisplayLabelList;
	}

	/**
	 * 多言語設定情報を設定します。
	 * @param localizedUpdateDisplayLabelList 多言語設定情報
	 */
	public void setLocalizedUpdateDisplayLabelList(List<LocalizedStringDefinition> localizedUpdateDisplayLabelList) {
		this.localizedUpdateDisplayLabelList = localizedUpdateDisplayLabelList;
	}

	/**
	 * 更新アクション名を取得します。
	 * @return 更新アクション名
	 */
	public String getUpdateActionName() {
		return updateActionName;
	}

	/**
	 * 更新アクション名を設定します。
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
	 * JavaScriptコード を取得します。
	 * @return JavaScriptコード 
	 */
	public String getJavaScript() {
		return javaScript;
	}

	/**
	 * JavaScriptコードを設定します。
	 * @param javaScript JavaScriptコード
	 */
	public void setJavaScript(String javaScript) {
		this.javaScript = javaScript;
	}

	/**
	 * Javascriptコード有効可否を取得します。
	 * @return Javascriptコード有効可否
	 */
	public boolean isValidJavascriptBulkPage() {
		return validJavascriptBulkPage;
	}

	/**
	 * Javascriptコード有効可否を設定します。
	 * @param validJavascriptBulkPage Javascriptコード有効可否
	 */
	public void setValidJavascriptBulkPage(boolean validJavascriptBulkPage) {
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
}
