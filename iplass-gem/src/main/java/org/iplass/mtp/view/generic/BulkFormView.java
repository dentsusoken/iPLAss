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

package org.iplass.mtp.view.generic;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.mtp.definition.LocalizedStringDefinition;

@XmlAccessorType(XmlAccessType.FIELD)
public class BulkFormView extends FormView {

	private static final long serialVersionUID = 937306616465891196L;

	/** 編集ボタン表示ラベル */
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

	/** 編集ボタン非表示 */
	@MetaFieldInfo(
			displayName="更新ボタン非表示",
			displayNameKey="generic_BulkFormView_hideUpdateDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=320,
			description="更新ボタンを非表示にするかを設定します。",
			descriptionKey="generic_BulkFormView_hideUpdateDescriptionKey"
	)
	private boolean hideUpdate;

	/** 編集アクション名 */
	@MetaFieldInfo(
			displayName="更新アクション名",
			displayNameKey="generic_BulkFormView_updateActionNameDisplaNameKey",
			inputType=InputType.ACTION,
			displayOrder=500,
			description="更新ボタンクリックで実行されるアクションを設定します。",
			descriptionKey="generic_BulkFormView_updateActionNameDescriptionKey"
	)
	private String updateActionName;

	/** JavaScriptコード */
	@MetaFieldInfo(
			displayName="JavaScriptコード",
			displayNameKey="generic_BulkFormView_javaScriptDisplaNameKey",
			inputType=InputType.SCRIPT,
			displayOrder=1100,
			mode="javascript",
			description="SCRIPTタグ内に出力するJavaScriptコードを設定します。",
			descriptionKey="generic_BulkFormView_javaScriptDescriptionKey"
	)
	private String javaScript;

	/** Javascriptコード有効可否(編集) */
	@MetaFieldInfo(
			displayName="Javascriptコードを有効化",
			displayNameKey="generic_BulkFormView_validJavascriptBulkPageDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=1110,
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
	
	
	/** 定義されている参照プロパティのみを取得 */
	@MetaFieldInfo(
			displayName="定義されている参照プロパティのみを取得",
			displayNameKey="generic_BulkFormView_loadDefinedReferencePropertyDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=1610,
			description="画面定義に設定された参照プロパティのみを詳細画面表示時に取得します。",
			descriptionKey="generic_BulkFormView_loadDefinedReferencePropertyDescriptionKey"
	)
	private boolean loadDefinedReferenceProperty;
	
	/** 更新時に強制的に更新処理を行う */
	@MetaFieldInfo(
			displayName="更新時に強制的に更新処理を行う",
			displayNameKey="generic_BulkFormView_forceUpadteDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=1620,
			description="変更項目が一つもなくとも、強制的に更新処理（更新日時、更新者が更新される）を行います。",
			descriptionKey="generic_BulkFormView_forceUpadteDescriptionKey"
	)
	private boolean forceUpadte;

	/** カスタム登録処理クラス名 */
	@MetaFieldInfo(
			displayName="カスタム登録処理クラス名",
			displayNameKey="generic_BulkFormView_interrupterNameDisplaNameKey",
			displayOrder=1660,
			description="データ登録時に行うカスタム登録処理のクラス名を指定します。<br>" +
					"RegistrationInterrupterインターフェースを実装するクラスを指定してください。",

			descriptionKey="generic_BulkFormView_interrupterNameDescriptionKey"
	)
	private String interrupterName;

	/** カスタムロード処理クラス名 */
	@MetaFieldInfo(
			displayName="カスタムロード処理クラス名",
			displayNameKey="generic_BulkFormView_loadEntityInterrupterNameDisplaNameKey",
			displayOrder=1670,
			description="Entityロード処理実行前にロード用のオプションをカスタマイズするためのクラス名を指定します。<br>" +
					"LoadEntityInterrupterインターフェースを実装するクラスを指定してください。",

			descriptionKey="generic_BulkFormView_loadEntityInterrupterNameDescriptionKey"
	)
	private String loadEntityInterrupterName;

	/**
	 * @return the updateDisplayLabel
	 */
	public String getUpdateDisplayLabel() {
		return updateDisplayLabel;
	}

	/**
	 * @param updateDisplayLabel the updateDisplayLabel to set
	 */
	public void setUpdateDisplayLabel(String updateDisplayLabel) {
		this.updateDisplayLabel = updateDisplayLabel;
	}

	/**
	 * @return the localizedUpdateDisplayLabelList
	 */
	public List<LocalizedStringDefinition> getLocalizedUpdateDisplayLabelList() {
		return localizedUpdateDisplayLabelList;
	}

	/**
	 * @param localizedUpdateDisplayLabelList the localizedUpdateDisplayLabelList to set
	 */
	public void setLocalizedUpdateDisplayLabelList(List<LocalizedStringDefinition> localizedUpdateDisplayLabelList) {
		this.localizedUpdateDisplayLabelList = localizedUpdateDisplayLabelList;
	}

	/**
	 * @return the hideUpdate
	 */
	public boolean isHideUpdate() {
		return hideUpdate;
	}

	/**
	 * @param hideUpdate the hideUpdate to set
	 */
	public void setHideUpdate(boolean hideUpdate) {
		this.hideUpdate = hideUpdate;
	}

	/**
	 * @return the updateActionName
	 */
	public String getUpdateActionName() {
		return updateActionName;
	}

	/**
	 * @param updateActionName the updateActionName to set
	 */
	public void setUpdateActionName(String updateActionName) {
		this.updateActionName = updateActionName;
	}

	/**
	 * @return the javaScript
	 */
	public String getJavaScript() {
		return javaScript;
	}

	/**
	 * @param javaScript the javaScript to set
	 */
	public void setJavaScript(String javaScript) {
		this.javaScript = javaScript;
	}

	/**
	 * @return the validJavascriptBulkPage
	 */
	public boolean isValidJavascriptBulkPage() {
		return validJavascriptBulkPage;
	}

	/**
	 * @param validJavascriptBulkPage the validJavascriptBulkPage to set
	 */
	public void setValidJavascriptBulkPage(boolean validJavascriptBulkPage) {
		this.validJavascriptBulkPage = validJavascriptBulkPage;
	}

	/**
	 * @return the purgeCompositionedEntity
	 */
	public boolean isPurgeCompositionedEntity() {
		return purgeCompositionedEntity;
	}

	/**
	 * @param purgeCompositionedEntity the purgeCompositionedEntity to set
	 */
	public void setPurgeCompositionedEntity(boolean purgeCompositionedEntity) {
		this.purgeCompositionedEntity = purgeCompositionedEntity;
	}

	/**
	 * @return the loadDefinedReferenceProperty
	 */
	public boolean isLoadDefinedReferenceProperty() {
		return loadDefinedReferenceProperty;
	}

	/**
	 * @param loadDefinedReferenceProperty the loadDefinedReferenceProperty to set
	 */
	public void setLoadDefinedReferenceProperty(boolean loadDefinedReferenceProperty) {
		this.loadDefinedReferenceProperty = loadDefinedReferenceProperty;
	}

	/**
	 * @return the forceUpadte
	 */
	public boolean isForceUpadte() {
		return forceUpadte;
	}

	/**
	 * @param forceUpadte the forceUpadte to set
	 */
	public void setForceUpadte(boolean forceUpadte) {
		this.forceUpadte = forceUpadte;
	}

	/**
	 * @return the interrupterName
	 */
	public String getInterrupterName() {
		return interrupterName;
	}

	/**
	 * @param interrupterName the interrupterName to set
	 */
	public void setInterrupterName(String interrupterName) {
		this.interrupterName = interrupterName;
	}

	/**
	 * @return the loadEntityInterrupterName
	 */
	public String getLoadEntityInterrupterName() {
		return loadEntityInterrupterName;
	}

	/**
	 * @param loadEntityInterrupterName the loadEntityInterrupterName to set
	 */
	public void setLoadEntityInterrupterName(String loadEntityInterrupterName) {
		this.loadEntityInterrupterName = loadEntityInterrupterName;
	}
}
