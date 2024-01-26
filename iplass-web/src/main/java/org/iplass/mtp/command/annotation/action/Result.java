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

package org.iplass.mtp.command.annotation.action;

import org.iplass.mtp.web.actionmapping.definition.result.ContentDispositionType;
import org.iplass.mtp.web.actionmapping.definition.result.ResultDefinition;

/**
 * ResultDefinitionを定義するアノテーションです。
 *
 * <p>
 * typeがJSPの場合は、Template定義も作成されます。<br/>
 * 複数のResult定義で同一のJSP(またはテンプレート名で指定した名前)を指定した場合は、
 * 解析順により最後の設定が有効になります。<br/>
 * もし複数のResult定義で同一のTemplate定義を利用したい場合は、
 * {@link org.iplass.mtp.command.annotation.template.Template}で定義を行い、
 * Result側はtypeをTEMPLATEにすることで対応可能です。
 * </p>
 *
 * @see ResultDefinition
 * @author K.Higuchi
 *
 */
public @interface Result {

	/**
	 * マッピング対象のCommandの実行結果ステータスを指定します。
	 * *指定の場合は、すべてのステータス（但し例外発生時除く）を表します。
	 * 未指定の場合（かつ、exceptionが未指定の場合）は、*が指定されたとみなされます。
	 *
	 * @return
	 */
	String status() default "##default";

	/**
	 * マッピング対象のCommand実行時の例外クラス（サブクラスも含む）を指定します。
	 * @return
	 */
	Class<? extends Throwable> exception() default UNSPECIFIED.class;

	/**
	 * ResultDefinitionのタイプを指定します。
	 * デフォルト値はType.JSPです。
	 *
	 */
	Type type() default Type.JSP;

	/**
	 * type別に次のいずれかを指定します。<br>
	 * JSP->JSPのパス。必須。<br>
	 * TEMPLATE->Templateの名前もしくはID。必須。<br>
	 * STREAM->RequestContextにセットされたInputStream or byte[] or BinaryReferenceのattribute名。未指定の場合は"streamData"<br>
	 * REDIRECT->RequestContextにセットされた、redirect先パスのattribute名。必須。
	 * DYNAMIC->RequestContextにセットされたテンプレート名を指定するattribute名。未指定の場合は"templateName"<br>
	 */
	String value() default "##default";

	/**
	 * type=TEMPLATEの場合、valueに指定する値をIDのみで解決する場合はfalseを設定します。<br>
	 * デフォルトはtrueです。<br>
	 * trueが指定された場合、テンプレートを次の順で解決します。
	 * <ol>
	 * <li>名前で検索</li>
	 * <li>名前で解決できなかった場合、IDで検索</li>
	 * </ol>
	 *
	 * @return
	 */
	boolean resolveByName() default true;

	/**
	 * type=JSPの場合のcontentType指定です。
	 */
	String contentType() default "##default";

	/**
	 * type=JSPの場合のtemplate名を指定可能です。未指定の場合、名前はjspファイルのパス（valueに設定した値）になります。
	 *
	 * @return
	 */
	String templateName() default "##default";

	/**
	 * type=STREAMの場合のみ有効です。streamのContentTypeが設定されたAttributeキーを指定します。
	 */
	String contentTypeAttributeName() default "##default";

	/**
	 * type=STREAMの場合のみ有効です。streamのContentLengthが設定されたAttributeキーを指定します。
	 */
	String contentLengthAttributeName() default "##default";

	/**
	 * type=JSP,TEMPLATE,STREAM,DYNAMICの場合に有効です。
	 * ContentDispositionを設定するかどうかのフラグです。
	 */
	boolean useContentDisposition() default false;

	/**
	 * type=JSP,TEMPLATE,STREAM,DYNAMICの場合のみ有効です。
	 * ContentDispositionのタイプを指定します。
	 */
	ContentDispositionType contentDispositionType() default ContentDispositionType.ATTACHMENT;

	/**
	 * type=JSP,TEMPLATE,STREAM,DYNAMICの場合のみ有効です。
	 * ファイル名が設定されたAttributeキーを指定します。
	 */
	String fileNameAttributeName() default "fileName";

	/**
	 * type=STREAMの場合のみ有効です。Rangeヘッダーに対応するか否かを指定します。
	 * @return
	 */
	boolean acceptRanges() default false;

	/**
	 * type=REDIRECTの場合のみ有効です。
	 * 外部サイトへのリダイレクトを許可するかどうかのフラグです。
	 * trueで外部サイトへリダイレクト許可となります。
	 * デフォルトはfalseです。
	 */
	boolean allowExternalLocation() default false;

	/**
	 * type=JSP,TEMPLATEの場合のみ有効です。LayoutAction名を指定します。
	 */
	String layoutActionName() default "";

	/**
	 * type=DYNAMICの場合のみ有効です。LayoutAction名が設定されたAttributeキーを指定します。
	 * 未指定の場合は、"layoutActionName"が適用されます。
	 */
	String layoutActionAttributeName() default "##default";

	/**
	 * ResultDefinitionの種別です。
	 *
	 * @author K.Higuchi
	 *
	 */
	public enum Type {
		REDIRECT, JSP, TEMPLATE, STREAM, DYNAMIC;
	}

	/**
	 * exceptionのマッピングで未指定を表すための例外クラスです（実際にこのクラスがスローされることはありません）
	 *
	 * @author K.Higuchi
	 *
	 */
	public class UNSPECIFIED extends Throwable {
		private static final long serialVersionUID = -7860913570648047826L;

		private UNSPECIFIED() {
		}
	}
}
