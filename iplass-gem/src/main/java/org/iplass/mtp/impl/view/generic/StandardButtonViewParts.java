/*
 * Copyright (C) 2026 DENTSU SOKEN INC. All Rights Reserved.
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

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.command.RequestContextBinding;
import org.iplass.mtp.impl.command.SessionBinding;
import org.iplass.mtp.impl.script.Script;
import org.iplass.mtp.impl.script.ScriptContext;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.view.generic.DetailFormViewButtonType;
import org.iplass.mtp.view.generic.OutputType;

/**
 * 詳細画面の標準ボタン表示判定を担う部品。
 *
 */
final class StandardButtonViewParts {

	private static final String SCRIPT_PREFIX_DISPLAY_TYPE_SCRIPT = "DetailFormViewRuntime_displayTypeScript";

	private final Map<DetailFormViewButtonType, Script> displayTypeScripts;
	private final ScriptEngine scriptEngine;

	/**
	 * コンストラクタ。
	 *
	 * @param displayTypeScripts コンパイル済みスクリプトの保持先
	 * @param scriptEngine スクリプトエンジン
	 */
	StandardButtonViewParts(Map<DetailFormViewButtonType, Script> displayTypeScripts, ScriptEngine scriptEngine) {
		this.displayTypeScripts = displayTypeScripts;
		this.scriptEngine = scriptEngine;
	}

	/**
	 * 画面定義に設定された標準ボタン表示判定用スクリプトをまとめてコンパイルし、部品を生成します。
	 *
	 * @param form 詳細画面定義
	 * @param scriptEngine スクリプトエンジン
	 * @param entityView エンティティ画面定義
	 * @return 標準ボタン表示判定部品
	 */
	static StandardButtonViewParts createWithCompiledScripts(MetaDetailFormView form, ScriptEngine scriptEngine,
			EntityViewRuntime entityView) {
		StandardButtonViewParts parts = new StandardButtonViewParts(
				new EnumMap<>(DetailFormViewButtonType.class), scriptEngine);
		String scriptNamePrefix = SCRIPT_PREFIX_DISPLAY_TYPE_SCRIPT + "_" + entityView.getMetaData()
				.getId() + "_"
				+ (form.getName() == null || form.getName()
						.isEmpty() ? "default" : form.getName());
		parts.compileDisplayTypeScript(DetailFormViewButtonType.EDIT, form.getEditDisplayTypeScript(), scriptNamePrefix);
		parts.compileDisplayTypeScript(DetailFormViewButtonType.LOCK, form.getLockDisplayTypeScript(), scriptNamePrefix);
		parts.compileDisplayTypeScript(DetailFormViewButtonType.COPY, form.getCopyDisplayTypeScript(), scriptNamePrefix);
		parts.compileDisplayTypeScript(DetailFormViewButtonType.VERSIONUP, form.getVersionupDisplayTypeScript(),
				scriptNamePrefix);
		parts.compileDisplayTypeScript(DetailFormViewButtonType.INSERT, form.getInsertDisplayTypeScript(), scriptNamePrefix);
		parts.compileDisplayTypeScript(DetailFormViewButtonType.UPDATE, form.getUpdateDisplayTypeScript(), scriptNamePrefix);
		parts.compileDisplayTypeScript(DetailFormViewButtonType.DELETE, form.getDeleteDisplayTypeScript(), scriptNamePrefix);
		parts.compileDisplayTypeScript(DetailFormViewButtonType.DELETE_VERSION,
				form.getDeleteSpecificVersionDisplayTypeScript(), scriptNamePrefix);
		return parts;
	}

	/**
	 * 標準ボタン表示判定用スクリプトをコンパイルして保持します。
	 *
	 * @param type ボタン種別
	 * @param script スクリプト文字列
	 * @param scriptNamePrefix スクリプト名の接頭辞
	 */
	private void compileDisplayTypeScript(DetailFormViewButtonType type, String script, String scriptNamePrefix) {
		if (script == null || script.isEmpty()) {
			return;
		}
		displayTypeScripts.put(type, scriptEngine.createScript(script, scriptNamePrefix + "_" + type.name()));
	}

	/**
	 * 標準ボタンを表示するか判定します。
	 *
	 * @param type ボタン種別
	 * @param outputType 表示タイプ
	 * @param entity 対象のエンティティ（新規登録時はnull）
	 * @return 表示する場合true
	 */
	boolean isVisible(DetailFormViewButtonType type, OutputType outputType, Entity entity) {
		Script script = displayTypeScripts.get(type);
		if (script == null) {
			//スクリプト未設定の場合は従来通り表示する
			return true;
		}

		ScriptContext sc = scriptEngine.newScriptContext();
		sc.setAttribute(DetailFormViewRuntime.REQUEST_BINDING_NAME, RequestContextBinding.newRequestContextBinding());
		sc.setAttribute(DetailFormViewRuntime.SESSION_BINDING_NAME, SessionBinding.newSessionBinding());
		sc.setAttribute(DetailFormViewRuntime.USER_BINDING_NAME, AuthContextHolder.getAuthContext()
				.newUserBinding());
		sc.setAttribute(DetailFormViewRuntime.OUTPUT_TYPE_BINDING_NAME, outputType);
		sc.setAttribute(DetailFormViewRuntime.ENTITY_BINDING_NAME, entity);

		Object val = script.eval(sc);
		if (val instanceof Boolean booleanValue) {
			return booleanValue;
		} else if (val instanceof String stringValue) {
			return Boolean.parseBoolean(stringValue);
		}
		return false;
	}

	/**
	 * 指定の画面名に対応する詳細画面定義を検索し、標準ボタンを表示するか判定します。
	 *
	 * @param formViews 検索対象の画面定義
	 * @param viewName 画面名（null または空文字の場合は無名の画面定義を対象とする）
	 * @param type ボタン種別
	 * @param outputType 表示タイプ
	 * @param entity 対象のエンティティ（新規登録時はnull）
	 * @return 表示する場合true
	 */
	static boolean isVisibleInFormViews(List<FormViewRuntime> formViews, String viewName, DetailFormViewButtonType type,
			OutputType outputType, Entity entity) {
		DetailFormViewRuntime detailView = findDetailFormView(formViews, viewName);
		if (detailView == null) {
			//画面定義が見つからない場合は従来通り表示する
			return true;
		}
		return detailView.isStandardButtonVisible(type, outputType, entity);
	}

	/**
	 * 指定の画面名に対応する詳細画面定義を検索します。
	 *
	 * @param formViews 検索対象の画面定義
	 * @param viewName 画面名（null または空文字の場合は無名の画面定義を対象とする）
	 * @return 該当する画面定義（見つからない場合はnull）
	 */
	private static DetailFormViewRuntime findDetailFormView(List<FormViewRuntime> formViews, String viewName) {
		for (FormViewRuntime formView : formViews) {
			if (!(formView instanceof DetailFormViewRuntime detailFormView)) {
				continue;
			}
			//nameが一致するhandlerを検索
			String formViewName = detailFormView.getMetaData()
					.getName();
			if (viewName == null || viewName.isEmpty()) {
				if (formViewName == null || formViewName.isEmpty()) {
					return detailFormView;
				}
			} else if (viewName.equals(formViewName)) {
				return detailFormView;
			}
		}
		return null;
	}
}
