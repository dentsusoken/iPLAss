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

package org.iplass.mtp.view.generic;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.mtp.definition.LocalizedStringDefinition;

/**
 * 編集画面用のFormレイアウト情報
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DetailFormView extends FormView {

	/** コピー対象 */
	@XmlType(namespace="http://mtp.iplass.org/xml/definition/view/generic")
	public enum CopyTarget {
		/** 当該エンティティのみコピー */
		@XmlEnumValue("Shallow")SHALLOW("Shallow"),
		/** 包含する（親子関係の）エンティティも一括にコピー */
		@XmlEnumValue("Deep")DEEP("Deep"),
		/** コピー時にShallowかDeepを選択 */
		@XmlEnumValue("Both")BOTH("Both"),
		/** 独自実装したコピー処理を実行 */
		@XmlEnumValue("Custom")CUSTOM("Custom");

		private final String value;

		private CopyTarget(final String value) {
			this.value = value;
		}

		public String value() {
			return this.value;
		}

		public static CopyTarget getEnum(String value) {
			for (CopyTarget v : values()) {
				if (v.value().equals(value)) {
					return v;
				}
			}
			throw new IllegalArgumentException("no such CopyTarget for the value: " + value);
		}
	}

	/** シリアルバージョンID */
	private static final long serialVersionUID = -5906085368820747139L;



	/** 編集ボタン非表示 */
	@MetaFieldInfo(
			displayName="編集ボタン非表示",
			displayNameKey="generic_DetailFormView_hideDetailDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=200,
			description="編集ボタンを非表示にするかを設定します。",
			descriptionKey="generic_DetailFormView_hideDetailDescriptionKey"
	)
	private boolean hideDetail;

	/** ロックボタン非表示設定 */
	@MetaFieldInfo(
			displayName="ロックボタンを非表示",
			displayNameKey="generic_DetailFormView_hideLockDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=210,
			description="ロックボタンを非表示にするかを設定します。",
			descriptionKey="generic_DetailFormView_hideLockDescriptionKey"
	)
	private boolean hideLock;

	/** コピーボタン非表示設定 */
	@MetaFieldInfo(
			displayName="コピーボタンを非表示",
			displayNameKey="generic_DetailFormView_isNoneDispCopyButtonDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=220,
			description="コピーボタンを非表示にするかを設定します",
			descriptionKey="generic_DetailFormView_isNoneDispCopyButtonDescriptionKey"
	)
	private boolean isNoneDispCopyButton;

	/** 削除ボタン非表示 */
	@MetaFieldInfo(
			displayName="削除ボタン非表示",
			displayNameKey="generic_DetailFormView_hideDeleteDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=230,
			description="削除ボタンを非表示にするかを設定します。",
			descriptionKey="generic_DetailFormView_hideDeleteDescriptionKey"
	)
	private boolean hideDelete;

	/** Entity権限の可能範囲条件でボタン表示を制御 */
	@MetaFieldInfo(
			displayName="Entity権限の可能範囲条件でボタン表示を制御",
			displayNameKey="generic_DetailFormView_checkEntityPermissionLimitConditionOfButtonDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=235,
			description="Entity権限の可能範囲条件をチェックし各ボタンの表示を制御します。",
			descriptionKey="generic_DetailFormView_checkEntityPermissionLimitConditionOfButtonDescriptionKey"
	)
	private boolean checkEntityPermissionLimitConditionOfButton;

	/** 編集ボタン表示ラベル */
	@MetaFieldInfo(
			displayName="編集ボタン表示ラベル",
			displayNameKey="generic_DetailFormView_editDisplayLabelDisplaNameKey",
			description="編集ボタンに表示されるラベルを設定します。",
			descriptionKey="generic_DetailFormView_editDisplayLabelDescriptionKey",
			inputType=InputType.MULTI_LANG,
			multiLangField = "localizedEditDisplayLabelList",
			displayOrder=240
	)
	@MultiLang()
	private String editDisplayLabel;

	/** 多言語設定情報 */
	@MetaFieldInfo(
			displayName="多言語設定",
			displayNameKey="generic_DetailFormView_localizedEditDisplayLabelListDisplaNameKey",
			inputType=InputType.MULTI_LANG_LIST,
			displayOrder=250
	)
	private List<LocalizedStringDefinition> localizedEditDisplayLabelList;

	/** コピーボタン表示ラベル */
	@MetaFieldInfo(
			displayName="コピーボタン表示ラベル",
			displayNameKey="generic_DetailFormView_copyDisplayLabelDisplaNameKey",
			description="コピーボタンに表示されるラベルを設定します。",
			descriptionKey="generic_DetailFormView_copyDisplayLabelDescriptionKey",
			inputType=InputType.MULTI_LANG,
			multiLangField = "localizedCopyDisplayLabelList",
			displayOrder=260
	)
	@MultiLang()
	private String copyDisplayLabel;

	/** 多言語設定情報 */
	@MetaFieldInfo(
			displayName="多言語設定",
			displayNameKey="generic_DetailFormView_localizedCopyDisplayLabelListDisplaNameKey",
			inputType=InputType.MULTI_LANG_LIST,
			displayOrder=270
	)
	private List<LocalizedStringDefinition> localizedCopyDisplayLabelList;

	/** バージョンアップボタン表示ラベル */
	@MetaFieldInfo(
			displayName="バージョンアップボタン表示ラベル",
			displayNameKey="generic_DetailFormView_versionupDisplayLabelDisplaNameKey",
			description="バージョンアップボタンに表示されるラベルを設定します。",
			descriptionKey="generic_DetailFormView_versionupDisplayLabelDescriptionKey",
			inputType=InputType.MULTI_LANG,
			multiLangField = "localizedVersionupDisplayLabelList",
			displayOrder=280
	)
	@MultiLang()
	private String versionupDisplayLabel;

	/** 多言語設定情報 */
	@MetaFieldInfo(
			displayName="多言語設定",
			displayNameKey="generic_DetailFormView_localizedVersionupDisplayLabelListDisplaNameKey",
			inputType=InputType.MULTI_LANG_LIST,
			displayOrder=290
	)
	private List<LocalizedStringDefinition> localizedVersionupDisplayLabelList;

	/** 追加ボタン表示ラベル */
	@MetaFieldInfo(
			displayName="追加ボタン表示ラベル",
			displayNameKey="generic_DetailFormView_insertDisplayLabelDisplaNameKey",
			description="追加ボタンに表示されるラベルを設定します。",
			descriptionKey="generic_DetailFormView_insertDisplayLabelDescriptionKey",
			inputType=InputType.MULTI_LANG,
			multiLangField = "localizedInsertDisplayLabelList",
			displayOrder=300
	)
	@MultiLang()
	private String insertDisplayLabel;

	/** 多言語設定情報 */
	@MetaFieldInfo(
			displayName="多言語設定",
			displayNameKey="generic_DetailFormView_localizedInsertDisplayLabelListDisplaNameKey",
			inputType=InputType.MULTI_LANG_LIST,
			displayOrder=310
	)
	private List<LocalizedStringDefinition> localizedInsertDisplayLabelList;

	/** 更新ボタン表示ラベル */
	@MetaFieldInfo(
			displayName="更新ボタン表示ラベル",
			displayNameKey="generic_DetailFormView_updateDisplayLabelDisplaNameKey",
			description="更新ボタンに表示されるラベルを設定します。",
			descriptionKey="generic_DetailFormView_updateDisplayLabelDescriptionKey",
			inputType=InputType.MULTI_LANG,
			multiLangField = "localizedUpdateDisplayLabelList",
			displayOrder=320
	)
	@MultiLang()
	private String updateDisplayLabel;

	/** 多言語設定情報 */
	@MetaFieldInfo(
			displayName="多言語設定",
			displayNameKey="generic_DetailFormView_localizedUpdateDisplayLabelListDisplaNameKey",
			inputType=InputType.MULTI_LANG_LIST,
			displayOrder=330
	)
	private List<LocalizedStringDefinition> localizedUpdateDisplayLabelList;

	/** 削除ボタン表示ラベル */
	@MetaFieldInfo(
			displayName="削除ボタン表示ラベル",
			displayNameKey="generic_DetailFormView_deleteDisplayLabelDisplaNameKey",
			description="削除ボタンに表示されるラベルを設定します。",
			descriptionKey="generic_DetailFormView_deleteDisplayLabelDescriptionKey",
			inputType=InputType.MULTI_LANG,
			multiLangField = "localizedDeleteDisplayLabelList",
			displayOrder=340
	)
	@MultiLang()
	private String deleteDisplayLabel;

	/** 多言語設定情報 */
	@MetaFieldInfo(
			displayName="多言語設定",
			displayNameKey="generic_DetailFormView_localizedDeleteDisplayLabelListDisplaNameKey",
			inputType=InputType.MULTI_LANG_LIST,
			displayOrder=350
	)
	private List<LocalizedStringDefinition> localizedDeleteDisplayLabelList;



	/** 編集アクション名 */
	@MetaFieldInfo(
			displayName="編集アクション名",
			displayNameKey="generic_DetailFormView_editActionNameDisplaNameKey",
			inputType=InputType.ACTION,
			displayOrder=500,
			description="編集ボタンクリックで実行されるアクションを設定します。",
			descriptionKey="generic_DetailFormView_editActionNameDescriptionKey"
	)
	private String editActionName;

	/** 参照時編集アクション名 */
	@MetaFieldInfo(
			displayName="参照時編集アクション名",
			displayNameKey="generic_DetailFormView_refEditActionNameDisplaNameKey",
			inputType=InputType.ACTION,
			displayOrder=510,
			description="参照ダイアログで編集ボタンクリックで実行されるアクションを設定します。",
			descriptionKey="generic_DetailFormView_refEditActionNameDescriptionKey"
	)
	private String refEditActionName;

	/** 追加アクション名 */
	@MetaFieldInfo(
			displayName="追加アクション名",
			displayNameKey="generic_DetailFormView_insertActionNameDisplaNameKey",
			inputType=InputType.ACTION,
			displayOrder=520,
			description="追加ボタンクリックで実行されるアクションを設定します。",
			descriptionKey="generic_DetailFormView_insertActionNameDescriptionKey"
	)
	private String insertActionName;

	/** 参照時追加アクション名 */
	@MetaFieldInfo(
			displayName="参照時追加アクション名",
			displayNameKey="generic_DetailFormView_refInsertActionNameDisplaNameKey",
			inputType=InputType.ACTION,
			displayOrder=530,
			description="参照ダイアログで追加ボタンクリックで実行されるアクションを設定します。",
			descriptionKey="generic_DetailFormView_refInsertActionNameDescriptionKey"
	)
	private String refInsertActionName;

	/** 更新アクション名 */
	@MetaFieldInfo(
			displayName="更新アクション名",
			displayNameKey="generic_DetailFormView_updateActionNameDisplaNameKey",
			inputType=InputType.ACTION,
			displayOrder=540,
			description="更新ボタンクリックで実行されるアクションを設定します。",
			descriptionKey="generic_DetailFormView_updateActionNameDescriptionKey"
	)
	private String updateActionName;

	/** 参照時更新アクション名 */
	@MetaFieldInfo(
			displayName="参照時更新アクション名",
			displayNameKey="generic_DetailFormView_refUpdateActionNameDisplaNameKey",
			inputType=InputType.ACTION,
			displayOrder=550,
			description="参照ダイアログで更新ボタンクリックで実行されるアクションを設定します。",
			descriptionKey="generic_DetailFormView_refUpdateActionNameDescriptionKey"
	)
	private String refUpdateActionName;

	/** 削除アクション名 */
	@MetaFieldInfo(
			displayName="削除アクション名",
			displayNameKey="generic_DetailFormView_deleteActionNameDisplaNameKey",
			inputType=InputType.ACTION,
			displayOrder=560,
			description="削除ボタンクリックで実行されるアクションを設定します。",
			descriptionKey="generic_DetailFormView_deleteActionNameDescriptionKey"
	)
	private String deleteActionName;

	/** キャンセルアクション名 */
	@MetaFieldInfo(
			displayName="キャンセルアクション名",
			displayNameKey="generic_DetailFormView_cancelActionNameDisplaNameKey",
			inputType=InputType.ACTION,
			displayOrder=570,
			description="キャンセルリンククリックで実行されるアクションを設定します。",
			descriptionKey="generic_DetailFormView_cancelActionNameDescriptionKey"
	)
	private String cancelActionName;




	/** JavaScriptコード */
	@MetaFieldInfo(
			displayName="JavaScriptコード",
			displayNameKey="generic_DetailFormView_javaScriptDisplaNameKey",
			inputType=InputType.SCRIPT,
			mode="javascript",
			displayOrder=1100,
			description="SCRIPTタグ内に出力するJavaScriptコードを設定します。",
			descriptionKey="generic_DetailFormView_javaScriptDescriptionKey"
	)
	private String javaScript;

	/** Javascriptコード有効可否(編集) */
	@MetaFieldInfo(
			displayName="編集でJavascriptコードを有効化",
			displayNameKey="generic_DetailFormView_validJavascriptDetailPageDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=1110,
			description="Javascriptコードに設定した内容を詳細編集画面で有効にするかを設定します。",
			descriptionKey="generic_DetailFormView_validJavascriptDetailPageDescriptionKey"
	)
	private boolean validJavascriptDetailPage;

	/** Javascriptコード有効可否(表示) */
	@MetaFieldInfo(
			displayName="表示でJavascriptコードを有効化",
			displayNameKey="generic_DetailFormView_validJavascriptViewPageDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=1120,
			description="Javascriptコードに設定した内容を詳細表示画面で有効にするかを設定します。",
			descriptionKey="generic_DetailFormView_validJavascriptViewPageDescriptionKey"
	)
	private boolean validJavascriptViewPage;




	/** 物理削除するかどうか */
	@MetaFieldInfo(
			displayName="物理削除するか",
			inputType=InputType.CHECKBOX,
			displayOrder=1510,
			description="物理削除を行うかを設定します。",
			displayNameKey="generic_DetailFormView_isPurgeDisplaNameKey",
			descriptionKey="generic_DetailFormView_isPurgeDescriptionKey")
	private boolean isPurge;




	/** 親子関係の参照を物理削除するか */
	@MetaFieldInfo(
			displayName="親子関係の参照を物理削除するか",
			displayNameKey="generic_DetailFormView_purgeCompositionedEntityDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=1600,
			description="親子関係の参照を物理削除するかを設定します",
			descriptionKey="generic_DetailFormView_purgeCompositionedEntityDescriptionKey"
	)
	private boolean purgeCompositionedEntity;

	/** 定義されている参照プロパティのみを取得 */
	@MetaFieldInfo(
			displayName="定義されている参照プロパティのみを取得",
			displayNameKey="generic_DetailFormView_loadDefinedReferencePropertyDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=1610,
			description="画面定義に設定された参照プロパティのみを詳細画面表示時に取得します。",
			descriptionKey="generic_DetailFormView_loadDefinedReferencePropertyDescriptionKey"
	)
	private boolean loadDefinedReferenceProperty;

	/** 更新時に強制的に更新処理を行う */
	@MetaFieldInfo(
			displayName="更新時に強制的に更新処理を行う",
			displayNameKey="generic_DetailFormView_forceUpadteDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=1620,
			description="変更項目が一つもなくとも、強制的に更新処理（更新日時、更新者が更新される）を行います。",
			descriptionKey="generic_DetailFormView_forceUpadteDescriptionKey"
	)
	private boolean forceUpadte;

	/** コピー対象 */
	@MetaFieldInfo(
			displayName="コピー対象",
			displayNameKey="generic_DetailFormView_copyTargetDisplaNameKey",
			inputType=InputType.ENUM,
			enumClass=CopyTarget.class,
			displayOrder=1630,
			description="コピーボタン押下時のコピー対象を設定します。<BR />" +
					"Shallow : 当該エンティティのみコピー<BR />" +
					"Deep    : 包含する（親子関係の）エンティティも一括にコピー<BR />" +
					"Both    : コピーボタン押下時にShallowかDeepを選択<BR />" +
					"Custom  : 独自実装したコピー処理を実行",

			descriptionKey="generic_DetailFormView_copyTargetDescriptionKey"
	)
	private CopyTarget copyTarget;

	/** カスタムコピースクリプト */
	@MetaFieldInfo(
			displayName="カスタムコピースクリプト",
			displayNameKey="generic_DetailFormView_customCopyScriptDisplaNameKey",
			inputType=InputType.SCRIPT,
			mode="groovy_script",
			displayOrder=1640,
			description="コピー対象でCustomを選択した際に実行されるGroovyScriptです。<BR />" +
					"以下のオブジェクトがバインドされています。<BR />" +
					"バインド変数名  ：内容<BR />" +
					"entity          ：コピー元のEntity<BR />" +
					"entityDefinition：Entity定義<BR />" +
					"entityManager   ：EntityManager",

			descriptionKey="generic_DetailFormView_customCopyScriptDescriptionKey"
	)
	private String customCopyScript;

	/** 初期化スクリプト */
	@MetaFieldInfo(
			displayName="初期化スクリプト ",
			displayNameKey="generic_DetailFormView_initScriptDisplaNameKey",
			inputType=InputType.SCRIPT,
			mode="groovy_script",
			displayOrder=1650,
			description="Entityを新規作成する際に実行されるGroovyScriptです。<BR />" +
					"新規作成画面表示前に呼び出され、空のEntityに対して初期値設定等を行います。<BR />" +
					"以下のオブジェクトがバインドされています。<BR />" +
					"バインド変数名  ：内容<BR />" +
					"entity          ：空のEntity",

			descriptionKey="generic_DetailFormView_initScriptDescriptionKey"
	)
	private String initScript;

	/** カスタム登録処理クラス名 */
	@MetaFieldInfo(
			displayName="カスタム登録処理クラス名",
			displayNameKey="generic_DetailFormView_interrupterNameDisplaNameKey",
			displayOrder=1660,
			description="データ登録時に行うカスタム登録処理のクラス名を指定します。<br>" +
					"RegistrationInterrupterインターフェースを実装するクラスを指定してください。",

			descriptionKey="generic_DetailFormView_interrupterNameDescriptionKey"
	)
	private String interrupterName;

	/** カスタムロード処理クラス名 */
	@MetaFieldInfo(
			displayName="カスタムロード処理クラス名",
			displayNameKey="generic_DetailFormView_loadEntityInterrupterNameDisplaNameKey",
			displayOrder=1670,
			description="Entityロード処理実行前にロード用のオプションをカスタマイズするためのクラス名を指定します。<br>" +
					"LoadEntityInterrupterインターフェースを実装するクラスを指定してください。",

			descriptionKey="generic_DetailFormView_loadEntityInterrupterNameDescriptionKey"
	)
	private String loadEntityInterrupterName;

	/** 詳細編集画面Handlerクラス名 */
	@MetaFieldInfo(
			displayName="詳細編集画面Handlerクラス名",
			displayNameKey="generic_DetailFormView_detailFormViewHandlerNameDisplaNameKey",
			inputType=InputType.MULTI_TEXT,
			displayOrder=1680,
			description="詳細編集画面の制御クラス名を指定します。<br>" +
					"DetailFormViewHandlerインターフェースを実装するクラスを指定してください。",
			descriptionKey="generic_DetailFormView_detailFormViewHandlerNameDescriptionKey"
	)
	private List<String> detailFormViewHandlerName;

	/**
	 * デフォルトコンストラクタ
	 */
	public DetailFormView() {
	}

	/**
	 * 編集アクション名を取得します。
	 * @return 編集アクション名
	 */
	public String getEditActionName() {
		return editActionName;
	}

	/**
	 * 編集アクション名を設定します。
	 * @param editActionName 編集アクション名
	 */
	public void setEditActionName(String editActionName) {
		this.editActionName = editActionName;
	}

	/**
	 * 参照時編集アクション名を取得します。
	 * @return 参照時編集アクション名
	 */
	public String getRefEditActionName() {
	    return refEditActionName;
	}

	/**
	 * 参照時編集アクション名を設定します。
	 * @param refEditActionName 参照時編集アクション名
	 */
	public void setRefEditActionName(String refEditActionName) {
	    this.refEditActionName = refEditActionName;
	}

	/**
	 * 編集ボタン表示ラベルを取得します。
	 * @return 編集ボタン表示ラベル
	 */
	public String getEditDisplayLabel() {
	    return editDisplayLabel;
	}

	/**
	 * 編集ボタン表示ラベルを設定します。
	 * @param editDisplayLabel 編集ボタン表示ラベル
	 */
	public void setEditDisplayLabel(String editDisplayLabel) {
	    this.editDisplayLabel = editDisplayLabel;
	}

	/**
	 * 多言語設定情報を取得します。
	 * @return 多言語設定情報
	 */
	public List<LocalizedStringDefinition> getLocalizedEditDisplayLabelList() {
	    return localizedEditDisplayLabelList;
	}

	/**
	 * 多言語設定情報を設定します。
	 * @param localizedEditDisplayLabelList 多言語設定情報
	 */
	public void setLocalizedEditDisplayLabelList(List<LocalizedStringDefinition> localizedEditDisplayLabelList) {
	    this.localizedEditDisplayLabelList = localizedEditDisplayLabelList;
	}

	/**
	 * コピーボタン表示ラベルを取得します。
	 * @return コピーボタン表示ラベル
	 */
	public String getCopyDisplayLabel() {
	    return copyDisplayLabel;
	}

	/**
	 * コピーボタン表示ラベルを設定します。
	 * @param copyDisplayLabel コピーボタン表示ラベル
	 */
	public void setCopyDisplayLabel(String copyDisplayLabel) {
	    this.copyDisplayLabel = copyDisplayLabel;
	}

	/**
	 * 多言語設定情報を取得します。
	 * @return 多言語設定情報
	 */
	public List<LocalizedStringDefinition> getLocalizedCopyDisplayLabelList() {
	    return localizedCopyDisplayLabelList;
	}

	/**
	 * 多言語設定情報を設定します。
	 * @param localizedCopyDisplayLabelList 多言語設定情報
	 */
	public void setLocalizedCopyDisplayLabelList(List<LocalizedStringDefinition> localizedCopyDisplayLabelList) {
	    this.localizedCopyDisplayLabelList = localizedCopyDisplayLabelList;
	}

	/**
	 * バージョンアップボタン表示ラベルを取得します。
	 * @return バージョンアップボタン表示ラベル
	 */
	public String getVersionupDisplayLabel() {
	    return versionupDisplayLabel;
	}

	/**
	 * バージョンアップボタン表示ラベルを設定します。
	 * @param versionupDisplayLabel バージョンアップボタン表示ラベル
	 */
	public void setVersionupDisplayLabel(String versionupDisplayLabel) {
	    this.versionupDisplayLabel = versionupDisplayLabel;
	}

	/**
	 * 多言語設定情報を取得します。
	 * @return 多言語設定情報
	 */
	public List<LocalizedStringDefinition> getLocalizedVersionupDisplayLabelList() {
	    return localizedVersionupDisplayLabelList;
	}

	/**
	 * 多言語設定情報を設定します。
	 * @param localizedVersionupDisplayLabelList 多言語設定情報
	 */
	public void setLocalizedVersionupDisplayLabelList(List<LocalizedStringDefinition> localizedVersionupDisplayLabelList) {
	    this.localizedVersionupDisplayLabelList = localizedVersionupDisplayLabelList;
	}

	/**
	 * 追加アクション名を取得します。
	 * @return 追加アクション名
	 */
	public String getInsertActionName() {
		return insertActionName;
	}

	/**
	 * 追加アクション名を設定します。
	 * @param insertActionName 追加アクション名
	 */
	public void setInsertActionName(String insertActionName) {
		this.insertActionName = insertActionName;
	}

	/**
	 * 参照時追加アクション名を取得します。
	 * @return 参照時追加アクション名
	 */
	public String getRefInsertActionName() {
	    return refInsertActionName;
	}

	/**
	 * 参照時追加アクション名を設定します。
	 * @param refInsertActionName 参照時追加アクション名
	 */
	public void setRefInsertActionName(String refInsertActionName) {
	    this.refInsertActionName = refInsertActionName;
	}

	/**
	 * 追加ボタン表示ラベルを取得します。
	 * @return 追加ボタン表示ラベル
	 */
	public String getInsertDisplayLabel() {
	    return insertDisplayLabel;
	}

	/**
	 * 追加ボタン表示ラベルを設定します。
	 * @param insertDisplayLabel 追加ボタン表示ラベル
	 */
	public void setInsertDisplayLabel(String insertDisplayLabel) {
	    this.insertDisplayLabel = insertDisplayLabel;
	}

	/**
	 * 多言語設定情報を取得します。
	 * @return 多言語設定情報
	 */
	public List<LocalizedStringDefinition> getLocalizedInsertDisplayLabelList() {
	    return localizedInsertDisplayLabelList;
	}

	/**
	 * 多言語設定情報を設定します。
	 * @param localizedInsertDisplayLabelList 多言語設定情報
	 */
	public void setLocalizedInsertDisplayLabelList(List<LocalizedStringDefinition> localizedInsertDisplayLabelList) {
	    this.localizedInsertDisplayLabelList = localizedInsertDisplayLabelList;
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
	 * 参照時更新アクション名を取得します。
	 * @return 参照時更新アクション名
	 */
	public String getRefUpdateActionName() {
	    return refUpdateActionName;
	}

	/**
	 * 参照時更新アクション名を設定します。
	 * @param refUpdateActionName 参照時更新アクション名
	 */
	public void setRefUpdateActionName(String refUpdateActionName) {
	    this.refUpdateActionName = refUpdateActionName;
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
	 * 削除アクション名を取得します。
	 * @return 削除アクション名
	 */
	public String getDeleteActionName() {
		return deleteActionName;
	}

	/**
	 * 削除アクション名を設定します。
	 * @param deleteActionName 削除アクション名
	 */
	public void setDeleteActionName(String deleteActionName) {
		this.deleteActionName = deleteActionName;
	}

	/**
	 * 削除ボタン表示ラベルを取得します。
	 * @return 削除ボタン表示ラベル
	 */
	public String getDeleteDisplayLabel() {
	    return deleteDisplayLabel;
	}

	/**
	 * 削除ボタン表示ラベルを設定します。
	 * @param deleteDisplayLabel 削除ボタン表示ラベル
	 */
	public void setDeleteDisplayLabel(String deleteDisplayLabel) {
	    this.deleteDisplayLabel = deleteDisplayLabel;
	}

	/**
	 * 多言語設定情報を取得します。
	 * @return 多言語設定情報
	 */
	public List<LocalizedStringDefinition> getLocalizedDeleteDisplayLabelList() {
	    return localizedDeleteDisplayLabelList;
	}

	/**
	 * 多言語設定情報を設定します。
	 * @param localizedDeleteDisplayLabelList 多言語設定情報
	 */
	public void setLocalizedDeleteDisplayLabelList(List<LocalizedStringDefinition> localizedDeleteDisplayLabelList) {
	    this.localizedDeleteDisplayLabelList = localizedDeleteDisplayLabelList;
	}

	/**
	 * キャンセルアクション名を取得します。
	 * @return キャンセルアクション名
	 */
	public String getCancelActionName() {
		return cancelActionName;
	}

	/**
	 * キャンセルアクション名を設定します。
	 * @param cancelActionName キャンセルアクション名
	 */
	public void setCancelActionName(String cancelActionName) {
		this.cancelActionName = cancelActionName;
	}

	/**
	 * Javascriptコード有効可否(編集)を取得します。
	 * @return Javascriptコード有効可否(編集)
	 */
	public boolean isValidJavascriptDetailPage() {
	    return validJavascriptDetailPage;
	}

	/**
	 * Javascriptコード有効可否(編集)を設定します。
	 * @param validJavascriptDetailPage Javascriptコード有効可否(編集)
	 */
	public void setValidJavascriptDetailPage(boolean validJavascriptDetailPage) {
	    this.validJavascriptDetailPage = validJavascriptDetailPage;
	}

	/**
	 * Javascriptコード有効可否(表示)を取得します。
	 * @return Javascriptコード有効可否(表示)
	 */
	public boolean isValidJavascriptViewPage() {
	    return validJavascriptViewPage;
	}

	/**
	 * Javascriptコード有効可否(表示)を設定します。
	 * @param validJavascriptViewPage Javascriptコード有効可否(表示)
	 */
	public void setValidJavascriptViewPage(boolean validJavascriptViewPage) {
	    this.validJavascriptViewPage = validJavascriptViewPage;
	}

	/**
	 * 編集ボタン非表示を取得します。
	 * @return 編集ボタン非表示
	 */
	public boolean isHideDetail() {
	    return hideDetail;
	}

	/**
	 * 編集ボタン非表示を設定します。
	 * @param hideDetail 編集ボタン非表示
	 */
	public void setHideDetail(boolean hideDetail) {
	    this.hideDetail = hideDetail;
	}

	/**
	 * ロックボタン非表示設定を取得します。
	 * @return ロックボタン非表示設定
	 */
	public boolean isHideLock() {
	    return hideLock;
	}

	/**
	 * ロックボタン非表示設定を設定します。
	 * @param hideLock ロックボタン非表示設定
	 */
	public void setHideLock(boolean hideLock) {
	    this.hideLock = hideLock;
	}

	/**
	 * コピーボタン非表示設定を取得します。
	 * @return コピーボタン非表示設定
	 */
	public boolean isNoneDispCopyButton() {
		return isNoneDispCopyButton;
	}

	/**
	 * コピーボタン非表示設定を設定します。
	 * @param isNoneDispCopyButton コピーボタン非表示設定
	 */
	public void setIsNoneDispCopyButton(boolean isNoneDispCopyButton) {
		this.isNoneDispCopyButton = isNoneDispCopyButton;
	}

	/**
	 * 削除ボタン非表示を取得します。
	 * @return 削除ボタン非表示
	 */
	public boolean isHideDelete() {
	    return hideDelete;
	}

	/**
	 * 削除ボタン非表示を設定します。
	 * @param hideDelete 削除ボタン非表示
	 */
	public void setHideDelete(boolean hideDelete) {
	    this.hideDelete = hideDelete;
	}

	/**
	 * Entity権限の可能範囲条件でボタン表示を制御設定を取得します。
	 * @return Entity権限の可能範囲条件でボタン表示を制御設定
	 */
	public boolean isCheckEntityPermissionLimitConditionOfButton() {
		return checkEntityPermissionLimitConditionOfButton;
	}

	/**
	 * Entity権限の可能範囲条件でボタン表示を制御設定を設定します。
	 * @param checkEntityPermissionLimitConditionOfButton Entity権限の可能範囲条件でボタン表示を制御設定
	 */
	public void setCheckEntityPermissionLimitConditionOfButton(boolean checkEntityPermissionLimitConditionOfButton) {
		this.checkEntityPermissionLimitConditionOfButton = checkEntityPermissionLimitConditionOfButton;
	}

	/**
	 * 物理削除するかどうかを取得します。
	 * @return 物理削除するかどうか
	 */
	public boolean isPurge() {
	    return isPurge;
	}

	/**
	 * 物理削除するかどうかを設定します。
	 * @param isPurge 物理削除するかどうか
	 */
	public void setPurge(boolean isPurge) {
	    this.isPurge = isPurge;
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
	 * 定義されている参照プロパティのみを取得を取得します。
	 * @return 定義されている参照プロパティのみを取得
	 */
	public boolean isLoadDefinedReferenceProperty() {
	    return loadDefinedReferenceProperty;
	}

	/**
	 * 定義されている参照プロパティのみを取得を設定します。
	 * @param loadDefinedReferenceProperty 定義されている参照プロパティのみを取得
	 */
	public void setLoadDefinedReferenceProperty(boolean loadDefinedReferenceProperty) {
	    this.loadDefinedReferenceProperty = loadDefinedReferenceProperty;
	}

	/**
	 * 更新時に強制的に更新処理を行うかを取得します。
	 * @return forceUpdate 更新時に強制的に更新処理を行うか
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
	 * コピー対象を取得します。
	 * @return コピー対象
	 */
	public CopyTarget getCopyTarget() {
	    return copyTarget;
	}

	/**
	 * コピー対象を設定します。
	 * @param copyTarget コピー対象
	 */
	public void setCopyTarget(CopyTarget copyTarget) {
	    this.copyTarget = copyTarget;
	}

	/**
	 * JavaScriptコードを取得します。
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
	 * カスタムコピースクリプトを取得します。
	 * @return カスタムコピースクリプト
	 */
	public String getCustomCopyScript() {
	    return customCopyScript;
	}

	/**
	 * カスタムコピースクリプトを設定します。
	 * @param customCopyScript カスタムコピースクリプト
	 */
	public void setCustomCopyScript(String customCopyScript) {
	    this.customCopyScript = customCopyScript;
	}

	/**
	 * 初期化スクリプトを取得します。
	 * @return 初期化スクリプト
	 */
	public String getInitScript() {
	    return initScript;
	}

	/**
	 * 初期化スクリプトを設定します。
	 * @param initScript 初期化スクリプト
	 */
	public void setInitScript(String initScript) {
	    this.initScript = initScript;
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
	 * 詳細編集画面Handlerクラス名を取得します。
	 * @return 詳細編集画面Handlerクラス名
	 */
	public List<String> getDetailFormViewHandlerName() {
		return detailFormViewHandlerName;
	}

	/**
	 * 詳細編集画面Handlerクラス名を設定します。
	 * @param detailFormViewHandlerName 詳細編集画面Handlerクラス名
	 */
	public void setDetailFormViewHandlerName(List<String> detailFormViewHandlerName) {
		this.detailFormViewHandlerName = detailFormViewHandlerName;
	}

}
