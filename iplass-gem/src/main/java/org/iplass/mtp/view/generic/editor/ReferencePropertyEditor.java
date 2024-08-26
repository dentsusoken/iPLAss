/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.adminconsole.view.annotation.generic.EntityViewField;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.view.generic.HasNestProperty;
import org.iplass.mtp.view.generic.Jsp;
import org.iplass.mtp.view.generic.Jsps;
import org.iplass.mtp.view.generic.ViewConst;

/**
 * 参照型プロパティエディタ
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Jsps({
	@Jsp(path="/jsp/gem/generic/editor/ReferencePropertyEditor.jsp", key=ViewConst.DESIGN_TYPE_GEM),
	@Jsp(path="/jsp/gem/aggregation/unit/editor/ReferencePropertyEditor.jsp", key=ViewConst.DESIGN_TYPE_GEM_AGGREGATION)
})
public class ReferencePropertyEditor extends PropertyEditor implements HasNestProperty, LabelablePropertyEditor {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 4691895224010210421L;

	/** 表示タイプ */
	@XmlType(namespace="http://mtp.iplass.org/xml/definition/view/generic")
	public enum ReferenceDisplayType {
		@XmlEnumValue("Link")LINK,
		@XmlEnumValue("Select")SELECT,
		@XmlEnumValue("Checkbox")CHECKBOX,
		@XmlEnumValue("RefCombo")REFCOMBO,
		@XmlEnumValue("Tree")TREE,
		@XmlEnumValue("NestTable")NESTTABLE,
		@XmlEnumValue("Label")LABEL,
		@XmlEnumValue("Unique")UNIQUE,
		@XmlEnumValue("Hidden")HIDDEN
	}

	/** 編集ページ */
	@XmlType(namespace="http://mtp.iplass.org/xml/definition/view/generic")
	public enum EditPage {
		@XmlEnumValue("Detail")DETAIL,
		@XmlEnumValue("View")VIEW
	}

	/** 追加ボタン表示位置 */
	@XmlType(namespace="http://mtp.iplass.org/xml/definition/view/generic")
	public enum InsertType {
		@XmlEnumValue("Top")TOP,
		@XmlEnumValue("Bottom")BOTTOM
	}

	/** 参照配列のソートタイプ */
	@XmlType(namespace="http://mtp.iplass.org/xml/definition/view/generic")
	public enum RefSortType {
		ASC, DESC;
	}

	/** 参照コンボの検索タイプ */
	@XmlType(namespace="http://mtp.iplass.org/xml/definition/view/generic")
	public enum RefComboSearchType {
		NONE,UPPER,ALERT
	}

	/** URLパラメータのActionタイプ */
	@XmlType(namespace="http://mtp.iplass.org/xml/definition/view/generic")
	public enum UrlParameterActionType {
		SELECT,ADD,VIEW
	}

	/** 表示タイプ */
	@MetaFieldInfo(displayName="表示タイプ",
			displayNameKey="generic_editor_ReferencePropertyEditor_displayTypeDisplaNameKey",
			inputType=InputType.ENUM,
			enumClass=ReferenceDisplayType.class,
			required=true,
			displayOrder=100,
			description="画面に表示する方法を選択します。",
			descriptionKey="generic_editor_ReferencePropertyEditor_displayTypeDescriptionKey"
	)
	private ReferenceDisplayType displayType;




	@MetaFieldInfo(displayName="表示ラベルとして扱うプロパティ",
			displayNameKey="generic_editor_ReferencePropertyEditor_displayLabelItemDisplaNameKey",
			inputType=InputType.PROPERTY,
			displayOrder=200,
			description="<b>表示タイプ:Link、Select</b><br>" +
					"表示ラベルとして扱うプロパティを指定します。",
			descriptionKey="generic_editor_ReferencePropertyEditor_displayLabelItemDescriptionKey"
	)
	@EntityViewField(
			referenceTypes = {FieldReferenceType.ALL}
	)
	private String displayLabelItem;

	/** 参照型の表示プロパティ */
	@MetaFieldInfo(displayName="参照型の表示プロパティ",
			displayNameKey="generic_editor_ReferencePropertyEditor_nestPropertiesDisplaNameKey",
			inputType=InputType.REFERENCE,
			referenceClass=NestProperty.class,
			multiple=true,
			displayOrder=210,
			description="<b>表示タイプ:NestTable</b><br>" +
					"テーブルに表示するプロパティを指定します。",
			descriptionKey="generic_editor_ReferencePropertyEditor_nestPropertiesDescriptionKey"
	)
	@MultiLang(isMultiLangValue = false)
	@EntityViewField(
			referenceTypes={FieldReferenceType.ALL}
	)
	private List<NestProperty> nestProperties;

	/** ネストテーブルの表示順プロパティ */
	@MetaFieldInfo(
			displayName="ネストテーブルの表示順プロパティ",
			displayNameKey="generic_editor_ReferencePropertyEditor_tableOrderPropertyNameDisplaNameKey",
			inputType=InputType.PROPERTY,
			displayOrder=220,
			description="<b>表示タイプ:NestTable</b><br>" +
					"ネストテーブルで表示する際の表示順を示すプロパティを設定します。",
			descriptionKey="generic_editor_ReferencePropertyEditor_tableOrderPropertyNameDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private String tableOrderPropertyName;

	/** プロパティと同時にネスト項目を条件に利用 */
	@MetaFieldInfo(displayName="プロパティと同時にネスト項目を条件に表示",
			displayNameKey="generic_editor_ReferencePropertyEditor_useNestConditionWithPropertyDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=230,
			description="ネスト項目が指定されていた場合、プロパティと同時に表示するかを指定します。。",
			descriptionKey="generic_editor_ReferencePropertyEditor_useNestConditionWithPropertyDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION}
	)
	private boolean useNestConditionWithProperty;




	/** 選択ダイアログ利用可否 */
	@MetaFieldInfo(displayName="選択ダイアログ利用可否",
			displayNameKey="generic_editor_ReferencePropertyEditor_useSearchDialogDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=300,
			description="<b>表示タイプ:Link、Unique</b><br>" +
			"検索画面での条件指定方法をテキストでの名前指定から<br>" +
			"選択ダイアログからのレコード選択に変更します。",

			descriptionKey="generic_editor_ReferencePropertyEditor_useSearchDialogDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION}
	)
	private boolean useSearchDialog;

	/** 検索条件で単一選択 */
	@MetaFieldInfo(displayName="検索条件で単一選択",
			displayNameKey="generic_editor_ReferencePropertyEditor_singleSelectDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=310,
			description="<b>表示タイプ:Link</b><br>" +
			"検索画面での条件指定時に、選択ダイアログでのレコード選択方法を複数選択から単一選択に変更します。",
			descriptionKey="generic_editor_ReferencePropertyEditor_singleSelectDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION}
	)
	private boolean singleSelect;

	/** 検索条件での全選択を許可 */
	@MetaFieldInfo(displayName="検索条件での全選択を許可",
			displayNameKey="generic_editor_ReferencePropertyEditor_permitConditionSelectAllDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=320,
			description="<b>表示タイプ:Link</b><br>" +
					"参照先の選択画面で複数選択が可能な場合、全選択時の範囲を選択します。<br>" +
					"ただし多重度以上の選択は出来ず、先頭から順に選択されます。<br>" +
					"<br>" +
					"チェックあり : 検索条件に一致する全てのデータ(前後のページ含む)が対象<br>" +
					"チェックなし : 現在のページの全てのデータが対象",
			descriptionKey="generic_editor_ReferencePropertyEditor_permitConditionSelectAllDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION, FieldReferenceType.DETAIL}
	)
	private boolean permitConditionSelectAll = true; //デフォルトtrue

	/** 選択画面でバージョン検索を許可 */
	@MetaFieldInfo(displayName="選択画面でバージョン検索を許可",
			displayNameKey="generic_editor_ReferencePropertyEditor_permitVersionedSelectDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=325,
			description="<b>表示タイプ:Link</b><br>" +
					"バージョン管理している参照先の選択画面で、バージョン検索を可能にします。",
			descriptionKey="generic_editor_ReferencePropertyEditor_permitVersionedSelectDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private boolean permitVersionedSelect;

	/** 参照リンク編集可否 */
	@MetaFieldInfo(
			displayName="参照リンク編集可否",
			displayNameKey="generic_editor_ReferencePropertyEditor_editableReferenceDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=330,
			description="<b>表示タイプ:Link</b><br>" +
					"リンク先のページで編集を許可します。<br>" +
					"<b>表示タイプ:NestTable</b><br>" +
					"テーブルに編集用のリンクを表示します。",
			descriptionKey="generic_editor_ReferencePropertyEditor_editableReferenceDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHRESULT, FieldReferenceType.DETAIL}
	)
	private boolean editableReference;
	
	/** CHECKBOX形式の場合のアイテムを縦に並べるような表示するか */
	@MetaFieldInfo(
			displayName="CHECKBOX形式の場合にアイテムを縦に並べる",
			displayNameKey="generic_editor_ReferencePropertyEditor_itemDirectionColumnDisplayNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=340,
			description="CHECKBOX形式の場合のアイテムを縦に並べるような表示するかを設定します。",
			descriptionKey="generic_editor_ReferencePropertyEditor_itemDirectionColumnDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION, FieldReferenceType.DETAIL}
	)
	private boolean itemDirectionColumn;
	
	/** 「値なし」を検索条件の選択肢に追加するか */
	@MetaFieldInfo(
			displayName="「値なし」を検索条件の選択肢に追加するか",
			displayNameKey="generic_editor_ReferencePropertyEditor_isNullSearchEnabledDisplayNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=350,
			description="「値なしを検索条件の選択肢に追加するかを指定します。値なしが選択された場合、IS NULLを検索条件として指定します。",
			descriptionKey="generic_editor_ReferencePropertyEditor_isNullSearchEnabledDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION}
	)
	private boolean isNullSearchEnabled;




	/** 編集ページ */
	@MetaFieldInfo(
			displayName="編集ページ",
			displayNameKey="generic_editor_ReferencePropertyEditor_editPageDisplaNameKey",
			inputType=InputType.ENUM,
			enumClass=EditPage.class,
			displayOrder=400,
			description="<b>表示タイプ:Link、NestTable</b><br>" +
					"参照型の編集を行うページを指定します。",

			descriptionKey="generic_editor_ReferencePropertyEditor_editPageDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private EditPage editPage;

	/** 新規ボタン非表示設定 */
	@MetaFieldInfo(
			displayName="新規ボタン非表示",
			displayNameKey="generic_editor_ReferencePropertyEditor_hideRegistButtonDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=410,
			description="<b>表示タイプ:Link、NestTable、Unique</b><br>" +
					"データを追加するボタンを非表示にします。",
			descriptionKey="generic_editor_ReferencePropertyEditor_hideRegistButtonDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL, FieldReferenceType.BULK}
	)
	private boolean hideRegistButton;

	/** 選択ボタン非表示設定 */
	@MetaFieldInfo(
			displayName="選択ボタン非表示",
			displayNameKey="generic_editor_ReferencePropertyEditor_hideSelectButtonDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=420,
			description="<b>表示タイプ:Link、NestTable、Unique</b><br>" +
					"データを選択するボタンを非表示にします。",
			descriptionKey="generic_editor_ReferencePropertyEditor_hideSelectButtonDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL, FieldReferenceType.BULK}
	)
	private boolean hideSelectButton;

	/** 削除ボタン非表示設定 */
	@MetaFieldInfo(
			displayName="削除ボタン非表示",
			displayNameKey="generic_editor_ReferencePropertyEditor_hideDeleteButtonDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=430,
			description="<b>表示タイプ:Link、NestTable、Unique</b><br>" +
					"データを削除するボタンを非表示にします。",
			descriptionKey="generic_editor_ReferencePropertyEditor_hideDeleteButtonDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL, FieldReferenceType.BULK}
	)
	private boolean hideDeleteButton;

	/** 行追加方法 */
	@MetaFieldInfo(displayName="行追加方法",
			displayNameKey="generic_editor_ReferencePropertyEditor_insertTypeDisplaNameKey",
			inputType=InputType.ENUM,
			enumClass=InsertType.class,
			displayOrder=440,
			description="<b>表示タイプ:NestTable</b><br>" +
					"追加ボタンを押した時に追加される行の位置を設定します。",
			descriptionKey="generic_editor_ReferencePropertyEditor_insertTypeDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private InsertType insertType;




	/** ダイアログ表示アクション名 */
	@MetaFieldInfo(
			displayName="ダイアログ表示アクション名",
			displayNameKey="generic_editor_ReferencePropertyEditor_viewrefActionNameDisplaNameKey",
			inputType=InputType.ACTION,
			displayOrder=500,
			description="<b>表示タイプ:Link</b><br>" +
					"リンククリックで実行されるアクションを設定します。<br>" +
					"<b>表示タイプ:NestTable</b><br>" +
					"詳細表示画面での編集リンククリックで実行されるアクションを設定します。",
			descriptionKey="generic_editor_ReferencePropertyEditor_viewrefActionNameDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.ALL}
	)
	private String viewrefActionName;

	/** ダイアログ編集アクション名 */
	@MetaFieldInfo(
			displayName="ダイアログ編集アクション名",
			displayNameKey="generic_editor_ReferencePropertyEditor_detailrefActionNameDisplaNameKey",
			inputType=InputType.ACTION,
			displayOrder=510,
			description="<b>表示タイプ:NestTable</b><br>" +
					"詳細編集画面での編集リンククリックで実行されるアクションを設定します。",
			descriptionKey="generic_editor_ReferencePropertyEditor_detailrefActionNameDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private String detailrefActionName;

	/** 追加アクション名 */
	@MetaFieldInfo(
			displayName="追加アクション名",
			displayNameKey="generic_editor_ReferencePropertyEditor_addActionNameDisplaNameKey",
			inputType=InputType.ACTION,
			displayOrder=520,
			description="<b>表示タイプがLink</b><br>" +
					"追加ボタン押下で実行されるアクションを設定します。",
			descriptionKey="generic_editor_ReferencePropertyEditor_addActionNameDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private String addActionName;

	/** 選択アクション名 */
	@MetaFieldInfo(
			displayName="選択アクション名",
			displayNameKey="generic_editor_ReferencePropertyEditor_selectActionNameDisplaNameKey",
			inputType=InputType.ACTION,
			displayOrder=530,
			description="<b>表示タイプ:Link</b><br>" +
					"選択ボタン押下で実行されるアクションを設定します。",
			descriptionKey="generic_editor_ReferencePropertyEditor_selectActionNameDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION, FieldReferenceType.DETAIL}
	)
	private String selectActionName;

	/** 更新時に強制的に更新処理を行う */
	@MetaFieldInfo(
			displayName="更新時に強制的に更新処理を行う",
			displayNameKey="generic_editor_ReferencePropertyEditor_forceUpadteDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=540,
			description="<b>表示タイプ:NestTable</b><br>" +
					"変更項目が一つもなくとも、強制的に更新処理（更新日時、更新者が更新される）を行います。",
			descriptionKey="generic_editor_ReferencePropertyEditor_forceUpadteDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private boolean forceUpadte;



	/** ビュー定義名 */
	@MetaFieldInfo(
			displayName="ビュー定義名",
			displayNameKey="generic_editor_ReferencePropertyEditor_viewNameDisplaNameKey",
			displayOrder=600,
			description="<b>表示タイプ:Link、NestTable</b><br>" +
					"選択・追加ボタン、編集リンク押下で表示する画面のView定義名を設定します。<br>" +
					"未指定の場合はデフォルトのView定義を使用します。",
			descriptionKey="generic_editor_ReferencePropertyEditor_viewNameDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.ALL}
	)
	private String viewName;

	/** URLパラメータ */
	@MetaFieldInfo(
			displayName="URLパラメータ",
			displayNameKey="generic_editor_ReferencePropertyEditor_urlParameterDisplaNameKey",
			description="<b>表示タイプ:Link、NestTable</b><br>" +
					"選択ボタン、新規ボタン押下で表示する画面に渡すパラメータを設定します。<br>" +
					"NestTableの場合、編集ページがViewの場合に有効になります。<br>" +
					"以下の変数がバインドされています。<br>" +
					"request:リクエスト<br>" +
					"session:セッション<br>" +
					"parent:参照元のEntity(編集画面のみ)<br>" +
					"<br>" +
					"新規ボタン押下時にはこの項目で定義したパラメータと一緒に以下のパラメータが渡されます<br>" +
					"parentOid:親EntityのOID(新規登録時はnull)<br>" +
					"parentVersion:親Entityのバージョン(新規登録時はnull)<br>" +
					"parentDefName:親EntityのEntity定義名",
			descriptionKey="generic_editor_ReferencePropertyEditor_urlParameterDescriptionKey",
			inputType=InputType.SCRIPT,
			mode="groovytemplate",
			displayOrder=610
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION, FieldReferenceType.DETAIL, FieldReferenceType.BULK}
	)
	private String urlParameter;

	/** URLパラメータAction */
	@MetaFieldInfo(
			displayName="URLパラメータAction",
			displayNameKey="generic_editor_ReferencePropertyEditor_urlParameterActionDisplaNameKey",
			descriptionKey="generic_editor_ReferencePropertyEditor_urlParameterActionDescriptionKey",
			inputType=InputType.ENUM,
			enumClass=UrlParameterActionType.class,
			multiple = true,
			displayOrder=615
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION, FieldReferenceType.DETAIL, FieldReferenceType.BULK}
	)
	private List<UrlParameterActionType> urlParameterAction;

	/** 動的URLパラメータ */
	@MetaFieldInfo(
			displayName="動的URLパラメータ",
			displayNameKey="generic_editor_ReferencePropertyEditor_dynamicUrlParameterDisplayNameKey",
			description="<b>表示タイプ:Link、NestTable</b><br>" +
					"選択ボタン、新規ボタン押下で表示する画面に渡すパラメータを作成するためのJavascriptを設定します。<br>" +
					"NestTableの場合、編集ページがViewの場合に有効になります。<br>" +
					"<br>" +
					"URLパラメータに設定した値が変数のurlParamに格納されます。<br>" +
					"urlParamを含めてURLパラメータの文字列をreturnしてください。",
			descriptionKey="generic_editor_ReferencePropertyEditor_dynamicUrlParameterDescriptionKey",
			inputType=InputType.SCRIPT,
			mode="javascript",
			displayOrder=616
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION, FieldReferenceType.DETAIL, FieldReferenceType.BULK}
	)
	private String dynamicUrlParameter;

	/** 動的URLパラメータAction */
	@MetaFieldInfo(
			displayName="動的URLパラメータAction",
			displayNameKey="generic_editor_ReferencePropertyEditor_dynamicUrlParameterActionDisplayNameKey",
			descriptionKey="generic_editor_ReferencePropertyEditor_dynamicUrlParameterActionDescriptionKey",
			inputType=InputType.ENUM,
			enumClass=UrlParameterActionType.class,
			multiple = true,
			displayOrder=617
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION, FieldReferenceType.DETAIL, FieldReferenceType.BULK}
	)
	private List<UrlParameterActionType> dynamicUrlParameterAction;

	/** 新規アクションコールバックスクリプト */
	@MetaFieldInfo(
			displayName="新規アクションコールバックスクリプト",
			displayNameKey="generic_editor_ReferencePropertyEditor_insertActionCallbackScriptDisplaNameKey",
			inputType=InputType.SCRIPT,
			mode="javascript",
			displayOrder=620,
			description="<b>表示タイプ:Link、Unique</b><br>" +
					"新規ダイアログで追加した後に実行するスクリプトを記述します。<br>" +
					"Entityの情報(OID、Version、名前)を持ったObject(entity)が引数になります。",
			descriptionKey="generic_editor_ReferencePropertyEditor_insertActionCallbackScriptDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL, FieldReferenceType.BULK}
	)
	private String insertActionCallbackScript;

	/** 選択アクションコールバックスクリプト */
	@MetaFieldInfo(
			displayName="選択アクションコールバックスクリプト",
			displayNameKey="generic_editor_ReferencePropertyEditor_selectActionCallbackScriptDisplaNameKey",
			inputType=InputType.SCRIPT,
			mode="javascript",
			displayOrder=630,
			description="<b>表示タイプ:Link、Unique</b><br>" +
					"選択ダイアログで選択した後に実行するスクリプトを記述します。<br>" +
					"Entityの情報(OID、Version、名前)を持ったObjectの配列(entityList)が引数になります。",
			descriptionKey="generic_editor_ReferencePropertyEditor_selectActionCallbackScriptDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL, FieldReferenceType.BULK}
	)
	private String selectActionCallbackScript;

	/** 行追加コールバックスクリプト */
	@MetaFieldInfo(
			displayName="行追加コールバックスクリプト",
			displayNameKey="generic_editor_ReferencePropertyEditor_addRowCallbackScriptDisplaNameKey",
			inputType=InputType.SCRIPT,
			mode="javascript",
			displayOrder=640,
			description="<b>表示タイプ:NestTable</b><br>" +
					"NestTableで行追加した後に実行するスクリプトを記述します。<br>" +
					"行のDOMオブジェクト(row)とインデックス(index)が引数になります。",
			descriptionKey="generic_editor_ReferencePropertyEditor_addRowCallbackScriptDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private String addRowCallbackScript;




	/** 特定バージョンの基準となるプロパティ */
	@MetaFieldInfo(
			displayName="特定バージョンの基準となるプロパティ",
			displayNameKey="generic_editor_ReferencePropertyEditor_specificVersionPropertyNameDisplaNameKey",
			displayOrder=700,
			description="<b>表示タイプ:Link</b><br>",
			descriptionKey="generic_editor_ReferencePropertyEditor_specificVersionPropertyNameDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private String specificVersionPropertyName;

	/** 検索条件 */
	@MetaFieldInfo(
			displayName="検索条件",
			displayNameKey="generic_editor_ReferencePropertyEditor_conditionDisplaNameKey",
			inputType=InputType.SCRIPT,
			mode="groovytemplate",
			displayOrder=710,
			description="<b>表示タイプ:Select、Checkbox、RefCombo</b><br>" +
					"表示する選択肢を検索する際に付与する検索条件を設定します。",
			descriptionKey="generic_editor_ReferencePropertyEditor_conditionDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION, FieldReferenceType.DETAIL}
	)
	private String condition;

	/** ソートアイテム */
	@MetaFieldInfo(
			displayName="ソートアイテム",
			displayNameKey="generic_editor_ReferencePropertyEditor_sortItemDisplaNameKey",
			inputType=InputType.PROPERTY,
			displayOrder=720,
			description="<b>表示タイプ:Select</b><br>" +
					"プルダウンの参照データをソートする項目を指定します。",
			descriptionKey="generic_editor_ReferencePropertyEditor_sortItemDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION, FieldReferenceType.DETAIL}
	)
	private String sortItem;

	/** ソート種別 */
	@MetaFieldInfo(
			displayName="ソート種別",
			displayNameKey="generic_editor_ReferencePropertyEditor_sortTypeDisplaNameKey",
			inputType=InputType.ENUM,
			displayOrder=730,
			enumClass=RefSortType.class,
			description="<b>表示タイプ:Select</b><br>" +
					"プルダウンの参照データをソートする順序を指定します。",
			descriptionKey="generic_editor_ReferencePropertyEditor_sortTypeDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION, FieldReferenceType.DETAIL}
	)
	private RefSortType sortType;





	/** 参照コンボ設定 */
	@MetaFieldInfo(
			displayName="参照コンボ設定",
			displayNameKey="generic_editor_ReferencePropertyEditor_referenceComboSettingDisplaNameKey",
			inputType=InputType.REFERENCE,
			referenceClass=ReferenceComboSetting.class,
			displayOrder=800,
			description="<b>表示タイプ:RefCombo</b><br>" +
					"コンボの内容を絞り込む条件を指定します。",
			descriptionKey="generic_editor_ReferencePropertyEditor_referenceComboSettingDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION, FieldReferenceType.DETAIL}
	)
	private ReferenceComboSetting referenceComboSetting;

	/** 参照コンボの検索方法 */
	@MetaFieldInfo(displayName="参照コンボの検索方法",
			displayNameKey="generic_editor_ReferencePropertyEditor_searchTypeDisplaNameKey",
			inputType=InputType.ENUM,
			enumClass=RefComboSearchType.class,
			displayOrder=810,
			description="検索条件に参照コンボを利用する場合で、最下層を選択しなかった時の動作を設定します。<BR />" +
					"NONE  : 検索条件に利用しない<BR />" +
					"UPPER : 上位の階層で選択されているものがあればそれを利用する<BR />" +
					"ALERT : 上位の階層が選択されていたら検索せずにアラートを表示",
			descriptionKey="generic_editor_ReferencePropertyEditor_searchTypeDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION}
	)
	private RefComboSearchType searchType;

	/** 参照コンボの親を表示するか */
	@MetaFieldInfo(
			displayName="参照コンボの親階層を表示するか",
			displayNameKey="generic_editor_ReferencePropertyEditor_showRefComboParentDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=820,
			description="<b>表示タイプ:RefCombo</b><br>" +
					"詳細画面で参照コンボの親階層を表示するかを指定します。",
			descriptionKey="generic_editor_ReferencePropertyEditor_showRefComboParentDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private boolean showRefComboParent;

	/** 再帰構造Entityのツリー設定 */
	@MetaFieldInfo(
			displayName="ツリー表示設定",
			displayNameKey="generic_editor_ReferencePropertyEditor_referenceRecursiveTreeSettingDisplaNameKey",
			inputType=InputType.REFERENCE,
			referenceClass=ReferenceRecursiveTreeSetting.class,
			displayOrder=830,
			description="<b>表示タイプ:Tree</b><br>" +
					"Entity内に同一Entityの参照を持つ、再帰構造のEntityをツリー表示するための条件を設定します。",
			descriptionKey="generic_editor_ReferencePropertyEditor_referenceRecursiveTreeSettingDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION, FieldReferenceType.DETAIL}
	)
	private ReferenceRecursiveTreeSetting referenceRecursiveTreeSetting;

	/** 連動プロパティ設定 */
	@MetaFieldInfo(
			displayName="連動プロパティ設定",
			displayNameKey="generic_editor_ReferencePropertyEditor_linkPropertyDisplaNameKey",
			inputType=InputType.REFERENCE,
			referenceClass=LinkProperty.class,
			displayOrder=840,
			description="<b>表示タイプ:Select、Checkbox、RefCombo</b><br>" +
					"選択可能値を連動するプロパティ情報を設定します。",
			descriptionKey="generic_editor_ReferencePropertyEditor_linkPropertyDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION, FieldReferenceType.DETAIL}
	)
	private LinkProperty linkProperty;





	@MetaFieldInfo(displayName="ユニークプロパティ",
			displayNameKey="generic_editor_ReferencePropertyEditor_uniqueItemDisplaNameKey",
			inputType=InputType.PROPERTY,
			displayOrder=900,
			description="<b>表示タイプ:UniqueKey</b><br>" +
					"ユニークプロパティを指定します。",
			descriptionKey="generic_editor_ReferencePropertyEditor_uniqueItemDescriptionKey"
	)
	@EntityViewField(
			referenceTypes = {FieldReferenceType.ALL}
	)
	private String uniqueItem;





	/** Label形式の場合の登録制御 */
	@MetaFieldInfo(
			displayName="Label形式の場合に表示値を登録する",
			displayNameKey="generic_editor_LabelablePropertyEditor_insertWithLabelValueDisplaNameKey",
			description="表示タイプがLabel形式の場合に表示値をそのまま登録するかを指定します。",
			inputType=InputType.CHECKBOX,
			displayOrder=1000,
			descriptionKey="generic_editor_LabelablePropertyEditor_insertWithLabelValueDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private boolean insertWithLabelValue = true;

	/** Label形式の場合の更新制御 */
	@MetaFieldInfo(
			displayName="Label形式の場合に表示値で更新する",
			displayNameKey="generic_editor_LabelablePropertyEditor_updateWithLabelValueDisplaNameKey",
			description="表示タイプがLabel形式の場合に表示値で更新するかを指定します。",
			inputType=InputType.CHECKBOX,
			displayOrder=1010,
			descriptionKey="generic_editor_LabelablePropertyEditor_updateWithLabelValueDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private boolean updateWithLabelValue = false;





	/** 参照先オブジェクト名 */
	private String objectName;

	/** 参照元オブジェクト名 */
	private String referenceFromObjectName;

	/** ルートオブジェクト名、NestPropertyの場合に利用 */
	private String rootObjectName;

	/** URLパラメータをコンパイルした際に生成したキー */
	private String urlParameterScriptKey;

	/**
	 * コンストラクタ
	 */
	public ReferencePropertyEditor() {
	}

	/**
	 * 表示タイプを取得します。
	 * @return 表示タイプ
	 */
	@Override
	public ReferenceDisplayType getDisplayType() {
		return displayType;
	}

	/**
	 * 表示タイプを設定します。
	 * @param displayType 表示タイプ
	 */
	public void setDisplayType(ReferenceDisplayType displayType) {
		this.displayType = displayType;
	}

	@Override
	public boolean isHide() {
		return displayType == ReferenceDisplayType.HIDDEN;
	}

	/**
	 * 検索条件を取得します。
	 * @return 検索条件
	 */
	public String getCondition() {
	    return condition;
	}

	/**
	 * 検索条件を設定します。
	 * @param condition 検索条件
	 */
	public void setCondition(String condition) {
	    this.condition = condition;
	}

	/**
	 * 選択ダイアログ利用可否を取得します。
	 * @return 選択ダイアログ利用可否
	 */
	public boolean isUseSearchDialog() {
	    return useSearchDialog;
	}

	/**
	 * 選択ダイアログ利用可否を設定します。
	 * @param useSearchDialog 選択ダイアログ利用可否
	 */
	public void setUseSearchDialog(boolean useSearchDialog) {
	    this.useSearchDialog = useSearchDialog;
	}

	/**
	 * 検索条件で単一選択を取得します。
	 * @return 検索条件で単一選択
	 */
	public boolean isSingleSelect() {
	    return singleSelect;
	}

	/**
	 * 検索条件で単一選択を設定します。
	 * @param singleSelect 検索条件で単一選択
	 */
	public void setSingleSelect(boolean singleSelect) {
	    this.singleSelect = singleSelect;
	}

	/**
	 * プロパティと一緒にネスト項目を条件に利用するかを取得します。
	 * @return プロパティと一緒にネスト項目を条件に利用するか
	 */
	public boolean isUseNestConditionWithProperty() {
		return useNestConditionWithProperty;
	}

	/**
	 * プロパティと一緒にネスト項目を条件に利用するかを設定します。
	 * @param useNestConditionWithProperty プロパティと一緒にネスト項目を条件に利用するか
	 */
	public void setUseNestConditionWithProperty(boolean useNestConditionWithProperty) {
		this.useNestConditionWithProperty = useNestConditionWithProperty;
	}

	/**
	 * 参照型の表示プロパティを取得します。
	 * @return 参照型の表示プロパティ
	 */
	public List<NestProperty> getNestProperties() {
		if (nestProperties == null) nestProperties = new ArrayList<>();
		return nestProperties;
	}

	/**
	 * 参照型の表示プロパティを設定します。
	 * @param nestProperties 参照型の表示プロパティ
	 */
	public void setNestProperties(List<NestProperty> nestProperties) {
		this.nestProperties = nestProperties;
	}

	/**
	 * 削除ボタン非表示設定を取得します。
	 * @return 削除ボタン非表示設定
	 */
	public boolean isHideDeleteButton() {
		return hideDeleteButton;
	}

	/**
	 * 削除ボタン非表示設定を設定します。
	 * @param dispDeleteButton 削除ボタン非表示設定
	 */
	public void setHideDeleteButton(boolean dispDeleteButton) {
		this.hideDeleteButton = dispDeleteButton;
	}

	/**
	 * 新規ボタン非表示設定を取得します。
	 * @return 新規ボタン非表示設定
	 */
	public boolean isHideRegistButton() {
		return hideRegistButton;
	}

	/**
	 * 新規ボタン非表示設定を設定します。
	 * @param dispRegistButton 新規ボタン非表示設定
	 */
	public void setHideRegistButton(boolean dispRegistButton) {
		this.hideRegistButton = dispRegistButton;
	}

	/**
	 * 選択ボタン非表示設定を取得します。
	 * @return 選択ボタン非表示設定
	 */
	public boolean isHideSelectButton() {
	    return hideSelectButton;
	}

	/**
	 * 選択ボタン非表示設定を設定します。
	 * @param hideSelectButton 選択ボタン非表示設定
	 */
	public void setHideSelectButton(boolean hideSelectButton) {
	    this.hideSelectButton = hideSelectButton;
	}

	/**
	 * 参照リンク編集可否を取得します。
	 * @return 参照リンク編集可否
	 */
	public boolean isEditableReference() {
	    return editableReference;
	}

	/**
	 * 参照リンク編集可否を設定します。
	 * @param editableReference 参照リンク編集可否
	 */
	public void setEditableReference(boolean editableReference) {
	    this.editableReference = editableReference;
	}

	/**
	 * 行追加方法を取得します。
	 * @return 行追加方法
	 */
	public InsertType getInsertType() {
		return insertType;
	}

	/**
	 * 行追加方法を設定します。
	 * @param insertType 行追加方法
	 */
	public void setInsertType(InsertType insertType) {
		this.insertType = insertType;
	}

	/**
	 * ダイアログ編集アクション名を取得します。
	 * @return ダイアログ編集アクション名
	 */
	public String getDetailrefActionName() {
	    return detailrefActionName;
	}

	/**
	 * ダイアログ編集アクション名を設定します。
	 * @param detailrefActionName ダイアログ編集アクション名
	 */
	public void setDetailrefActionName(String detailrefActionName) {
	    this.detailrefActionName = detailrefActionName;
	}

	/**
	 * 選択アクション名を取得します。
	 * @return 選択アクション名
	 */
	public String getSelectActionName() {
		return selectActionName;
	}

	/**
	 * 選択アクション名を設定します。
	 * @param selectActionName 選択アクション名
	 */
	public void setSelectActionName(String selectActionName) {
		this.selectActionName = selectActionName;
	}

	/**
	 * ダイアログ表示アクション名を取得します。
	 * @return ダイアログ表示アクション名
	 */
	public String getViewrefActionName() {
		return viewrefActionName;
	}

	/**
	 * ダイアログ表示アクション名を設定します。
	 * @param viewrefActionName ダイアログ表示アクション名
	 */
	public void setViewrefActionName(String viewrefActionName) {
		this.viewrefActionName = viewrefActionName;
	}

	/**
	 * 追加アクション名を取得します。
	 * @return 追加アクション名
	 */
	public String getAddActionName() {
		return addActionName;
	}

	/**
	 * 追加アクション名を設定します。
	 * @param addActionName 追加アクション名
	 */
	public void setAddActionName(String addActionName) {
		this.addActionName = addActionName;
	}

	/**
	 * ビュー定義名を取得します。
	 * @return ビュー定義名
	 */
	public String getViewName() {
		return viewName;
	}

	/**
	 * ビュー定義名を設定します。
	 * @param viewName ビュー定義名
	 */
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	/**
	 * URLパラメータを取得します。
	 * @return URLパラメータ
	 */
	public String getUrlParameter() {
		return urlParameter;
	}

	/**
	 * URLパラメータを設定します。
	 * @param urlParameter URLパラメータ
	 */
	public void setUrlParameter(String urlParameter) {
		this.urlParameter = urlParameter;
	}

	/**
	 * URLパラメータActionを取得します。
	 * @return URLパラメータAction
	 */
	public List<UrlParameterActionType> getUrlParameterAction() {
		return urlParameterAction;
	}

	/**
	 * URLパラメータActionを設定します。
	 * @param urlParameterAction URLパラメータAction
	 */
	public void setUrlParameterAction(List<UrlParameterActionType> urlParameterAction) {
		this.urlParameterAction = urlParameterAction;
	}

	/**
	 * 動的URLパラメータを取得します。
	 * @return 動的URLパラメータ
	 */
	public String getDynamicUrlParameter() {
		return dynamicUrlParameter;
	}

	/**
	 * 動的URLパラメータを設定します。
	 * @param dynamicUrlParameter 動的URLパラメータ
	 */
	public void setDynamicUrlParameter(String dynamicUrlParameter) {
		this.dynamicUrlParameter = dynamicUrlParameter;
	}

	/**
	 * 動的URLパラメータActionを取得します。
	 * @return 動的URLパラメータAction
	 */
	public List<UrlParameterActionType> getDynamicUrlParameterAction() {
		return dynamicUrlParameterAction;
	}

	/**
	 * 動的URLパラメータActionを設定します。
	 * @param dynamicUrlParameterAction 動的URLパラメータAction
	 */
	public void setDynamicUrlParameterAction(List<UrlParameterActionType> dynamicUrlParameterAction) {
		this.dynamicUrlParameterAction = dynamicUrlParameterAction;
	}

	/**
	 * ソートアイテムを取得します。
	 * @return ソートアイテム
	 */
	public String getSortItem() {
		return sortItem;
	}

	/**
	 * ソートアイテムを設定します。
	 * @param sortItem ソートアイテム
	 */
	public void setSortItem(String sortItem) {
		this.sortItem = sortItem;
	}

	/**
	 * ソート種別を取得します。
	 * @return ソート種別
	 */
	public RefSortType getSortType() {
		return sortType;
	}

	/**
	 * ソート種別を設定します。
	 * @param sortType ソート種別
	 */
	public void setSortType(RefSortType sortType) {
		this.sortType = sortType;
	}

	/**
	 * 編集ページを取得します。
	 * @return 編集ページ
	 */
	public EditPage getEditPage() {
		return editPage;
	}

	/**
	 * 編集ページを設定します。
	 * @param editPage 編集ページ
	 */
	public void setEditPage(EditPage editPage) {
		this.editPage = editPage;
	}

	/**
	 * 参照コンボ設定を取得します。
	 * @return 参照コンボ設定
	 */
	public ReferenceComboSetting getReferenceComboSetting() {
		return referenceComboSetting;
	}

	/**
	 * 参照コンボ設定を設定します。
	 * @param referenceComboSetting 参照コンボ設定
	 */
	public void setReferenceComboSetting(ReferenceComboSetting referenceComboSetting) {
		this.referenceComboSetting = referenceComboSetting;
	}

	/**
	 * 選択アクションコールバックスクリプトを取得します。
	 * @return 選択アクションコールバックスクリプト
	 */
	public String getSelectActionCallbackScript() {
		return selectActionCallbackScript;
	}

	/**
	 * 選択アクションコールバックスクリプトを設定します。
	 * @param selectActionCallbackScript 選択アクションコールバックスクリプト
	 */
	public void setSelectActionCallbackScript(String selectActionCallbackScript) {
		this.selectActionCallbackScript = selectActionCallbackScript;
	}

	/**
	 * 新規アクションコールバックスクリプトを取得します。
	 * @return 新規アクションコールバックスクリプト
	 */
	public String getInsertActionCallbackScript() {
		return insertActionCallbackScript;
	}

	/**
	 * 新規アクションコールバックスクリプトを設定します。
	 * @param insertActionCallbackScript 新規アクションコールバックスクリプト
	 */
	public void setInsertActionCallbackScript(String insertActionCallbackScript) {
		this.insertActionCallbackScript = insertActionCallbackScript;
	}

	/**
	 * 行追加コールバックスクリプトを取得します。
	 * @return 行追加コールバックスクリプト
	 */
	public String getAddRowCallbackScript() {
		return addRowCallbackScript;
	}

	/**
	 * 行追加コールバックスクリプトを設定します。
	 * @param addRowCallbackScript 行追加コールバックスクリプト
	 */
	public void setAddRowCallbackScript(String addRowCallbackScript) {
		this.addRowCallbackScript = addRowCallbackScript;
	}

	/**
	 * 参照コンボの検索方法を取得します。
	 * @return 参照コンボの検索方法
	 */
	public RefComboSearchType getSearchType() {
		return searchType;
	}

	/**
	 * 参照コンボの検索方法を設定します。
	 * @param searchType 参照コンボの検索方法
	 */
	public void setSearchType(RefComboSearchType searchType) {
		this.searchType = searchType;
	}

	/**
	 * 参照コンボの親を表示するかを取得します。
	 * @return 参照コンボの親を表示するか
	 */
	public boolean isShowRefComboParent() {
	    return showRefComboParent;
	}

	/**
	 * 参照コンボの親を表示するかを設定します。
	 * @param showRefComboParent 参照コンボの親を表示するか
	 */
	public void setShowRefComboParent(boolean showRefComboParent) {
	    this.showRefComboParent = showRefComboParent;
	}

	/**
	 *参照型の表示プロパティを追加します。
	 * @param property 参照型の表示プロパティ
	 */
	public void addNestProperty(NestProperty property) {
		getNestProperties().add(property);
	}

	/**
	 * 再帰構造Entityのツリー設定を取得します。
	 * @return 再帰構造Entityのツリー設定
	 */
	public ReferenceRecursiveTreeSetting getReferenceRecursiveTreeSetting() {
	    return referenceRecursiveTreeSetting;
	}

	/**
	 * 再帰構造Entityのツリー設定を設定します。
	 * @param referenceRecursiveTreeSetting 再帰構造Entityのツリー設定
	 */
	public void setReferenceRecursiveTreeSetting(ReferenceRecursiveTreeSetting referenceRecursiveTreeSetting) {
	    this.referenceRecursiveTreeSetting = referenceRecursiveTreeSetting;
	}

	/**
	 * 連動プロパティ設定を取得します。
	 * @return 連動プロパティ設定
	 */
	public LinkProperty getLinkProperty() {
		return linkProperty;
	}

	/**
	 * 連動プロパティ設定を設定します。
	 * @param linkProperty 連動プロパティ設定
	 */
	public void setLinkProperty(LinkProperty linkProperty) {
		this.linkProperty = linkProperty;
	}

	/**
	 * 特定バージョンの基準となるプロパティを取得します。
	 * @return 特定バージョンの基準となるプロパティ
	 */
	public String getSpecificVersionPropertyName() {
	    return specificVersionPropertyName;
	}

	/**
	 * 特定バージョンの基準となるプロパティを設定します。
	 * @param specificVersionPropertyName 特定バージョンの基準となるプロパティ
	 */
	public void setSpecificVersionPropertyName(String specificVersionPropertyName) {
	    this.specificVersionPropertyName = specificVersionPropertyName;
	}

	/**
	 * ネストテーブルの表示順プロパティを取得します。
	 * @return ネストテーブルの表示順プロパティ
	 */
	public String getTableOrderPropertyName() {
	    return tableOrderPropertyName;
	}

	/**
	 * ネストテーブルの表示順プロパティを設定します。
	 * @param tableOrderPropertyName ネストテーブルの表示順プロパティ
	 */
	public void setTableOrderPropertyName(String tableOrderPropertyName) {
	    this.tableOrderPropertyName = tableOrderPropertyName;
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
	 * 検索条件での全選択を許可を取得します。
	 * @return 検索条件での全選択を許可
	 */
	public boolean isPermitConditionSelectAll() {
	    return permitConditionSelectAll;
	}

	/**
	 * 検索条件での全選択を許可を設定します。
	 * @param permitConditionSelectAll 検索条件での全選択を許可
	 */
	public void setPermitConditionSelectAll(boolean permitConditionSelectAll) {
	    this.permitConditionSelectAll = permitConditionSelectAll;
	}

	/**
	 * 選択画面でのバージョン検索の許可を取得します。
	 * @return 選択画面でバージョン検索を許可
	 */
	public boolean isPermitVersionedSelect() {
		return permitVersionedSelect;
	}

	/**
	 * 選択画面でのバージョン検索の許可を設定します。
	 * @param permitVersionedSelect 選択画面でバージョン検索を許可
	 */
	public void setPermitVersionedSelect(boolean permitVersionedSelect) {
		this.permitVersionedSelect = permitVersionedSelect;
	}

	public String getDisplayLabelItem() {
		return displayLabelItem;
	}

	public void setDisplayLabelItem(String displayLabelItem) {
		this.displayLabelItem = displayLabelItem;
	}

	/**
	 * ユニークプロパティを取得します。
	 * @return ユニークプロパティ
	 */
	public String getUniqueItem() {
		return uniqueItem;
	}

	/**
	 * ユニークプロパティを設定します。
	 * @param uniqueKeyItem ユニークプロパティ
	 */
	public void setUniqueItem(String uniqueItem) {
		this.uniqueItem = uniqueItem;
	}

	@Override
	public boolean isLabel() {
		return displayType == ReferenceDisplayType.LABEL;
	}

	@Override
	public boolean isInsertWithLabelValue() {
		return insertWithLabelValue;
	}

	/**
	 * Label形式の場合の登録制御を設定します。
	 *
	 * @param insertWithLabelValue Label形式の場合の登録制御
	 */
	public void setInsertWithLabelValue(boolean insertWithLabelValue) {
		this.insertWithLabelValue = insertWithLabelValue;
	}

	@Override
	public boolean isUpdateWithLabelValue() {
		return updateWithLabelValue;
	}

	/**
	 * Label形式の場合の更新制御を設定します。
	 *
	 * @param updateWithLabelValue Label形式の場合の更新制御
	 */
	public void setUpdateWithLabelValue(boolean updateWithLabelValue) {
		this.updateWithLabelValue = updateWithLabelValue;
	}

	/**
	 * 参照先オブジェクト名を取得します。
	 * @return 参照先オブジェクト名
	 */
	public String getObjectName() {
		return objectName;
	}

	/**
	 * 参照先オブジェクト名を設定します。
	 * @param objectName 参照先オブジェクト名
	 */
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	/**
	 * 参照元オブジェクト名を取得します。
	 * @return 参照元オブジェクト名
	 */
	public String getReferenceFromObjectName() {
		return referenceFromObjectName;
	}

	/**
	 * 参照元オブジェクト名を設定します。
	 * @param referenceFromObjectName 参照元オブジェクト名
	 */
	public void setReferenceFromObjectName(String referenceFromObjectName) {
		this.referenceFromObjectName = referenceFromObjectName;
	}

	/**
	 * ルートオブジェクト名を取得します。
	 * NestProperty上のEditorで設定されます。
	 * @return ルートオブジェクト名
	 */
	public String getRootObjectName() {
		return rootObjectName;
	}

	/**
	 * ルートオブジェクト名を設定します。
	 * @param rootObjectName ルートオブジェクト名
	 */
	public void setRootObjectName(String rootObjectName) {
		this.rootObjectName = rootObjectName;
	}

	/**
	 * URLパラメータをコンパイルした際に生成したキーを取得します。
	 * @return URLパラメータをコンパイルした際に生成したキー
	 */
	public String getUrlParameterScriptKey() {
	    return urlParameterScriptKey;
	}

	/**
	 * URLパラメータをコンパイルした際に生成したキーを設定します。
	 * @param urlParameterScriptKey URLパラメータをコンパイルした際に生成したキー
	 */
	public void setUrlParameterScriptKey(String urlParameterScriptKey) {
	    this.urlParameterScriptKey = urlParameterScriptKey;
	}
	
	/**
	 * CHECKBOX形式の場合のアイテムを縦に並べるような表示するかを取得します。
	 * @return CHECKBOX形式の場合のアイテムを縦に並べるような表示するか
	 */
	public boolean isItemDirectionColumn() {
		return itemDirectionColumn;
	}

	/**
	 * CHECKBOX形式の場合のアイテムを縦に並べるような表示するかを設定します。
	 * @param itemDirectionColumn CHECKBOX形式の場合のアイテムを縦に並べるような表示するか
	 */
	public void setItemDirectionColumn(boolean itemDirectionColumn) {
		this.itemDirectionColumn = itemDirectionColumn;
	}
	
	/**
	 * 「値なし」を検索条件の選択肢に追加するかを取得します。
	 * @return 「値なし」を検索条件の選択肢に追加するか
	 */
	public boolean isIsNullSearchEnabled() {
		return isNullSearchEnabled;
	}

	/**
	 * 「値なし」を検索条件の選択肢に追加するかを設定します。
	 * @param isNullSearchEnabled 「値なし」を検索条件の選択肢に追加するか
	 */
	public void setIsNullSearchEnabled(boolean isNullSearchEnabled) {
		this.isNullSearchEnabled = isNullSearchEnabled;
	}

}
