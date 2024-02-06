/*
 * Copyright (C) 2023 DENTSU SOKEN INC. All Rights Reserved.
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

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.iplass.mtp.impl.view.generic.editor.MetaPropertyEditor.PropertyEditorRuntime;
import org.iplass.mtp.impl.view.generic.element.section.HasSectionPropertyRuntimeMap;
import org.iplass.mtp.impl.view.generic.element.section.MetaDefaultSection.DefaultSectionRuntime;
import org.iplass.mtp.impl.view.generic.element.section.MetaReferenceSection.ReferenceSectionRuntime;
import org.iplass.mtp.impl.view.generic.element.section.SectionRuntime;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FormViewRuntime に関連するユーティリティクラス
 *
 * @author SEKIGUCHI Naoya
 */
public class FormViewRuntimeUtil {
	/** ロガー */
	private static Logger LOG = LoggerFactory.getLogger(FormViewRuntimeUtil.class);

	// NOTE (default)のビュー名は空文字 "" になることを実行確認した
	/** ビュー名が指定されていない場合のデフォルト値 */
	private static final String VIEW_DEFAULT_NAME = "";

	/**
	 * プライベートコンストラクタ
	 */
	private FormViewRuntimeUtil() {
	}

	/**
	 * エンティティのビュー定義を取得する
	 *
	 * @param <T> FormViewRuntimeクラス
	 * @param defName エンティティ定義名
	 * @param viewName エンティティビュー名
	 * @param runtimeClass FormViewRuntimeクラス
	 * @return エンティティビュー定義
	 */
	@SuppressWarnings("unchecked")
	public static <T extends FormViewRuntime> T getFormViewRuntime(String defName, String viewName, Class<T> runtimeClass) {
		EntityViewService service = ServiceRegistry.getRegistry().getService(EntityViewService.class);
		EntityViewRuntime entityViewRuntime = service.getRuntimeByName(defName);

		if (null != entityViewRuntime) {
			// エンティティビュー名を非null化
			String nonNullViewName = Optional.ofNullable(viewName).orElse(VIEW_DEFAULT_NAME);

			for (FormViewRuntime viewRuntime : entityViewRuntime.getFormViews()) {
				if (runtimeClass.isAssignableFrom(viewRuntime.getClass())
						&& nonNullViewName.equals(viewRuntime.getMetaData().getName())) {
					return (T) viewRuntime;
				}
			}
		}

		LOG.warn("FormViewRuntime was not found. entityName = {}, viewName = {}, runtimeClass = {}", defName, viewName,
				runtimeClass);

		return null;
	}

	/**
	 * 指定されたプロパティのプロパティエディタランタイムを取得する
	 * @param <T> プロパティエディタランタイムクラス
	 * @param viewRuntime FormViewランタイム
	 * @param propName プロパティ名
	 * @param runtimeClass プロパティエディタランタイムクラス
	 * @return プロパティエディタランタイム
	 */
	public static <T extends PropertyEditorRuntime> T getPropertyEditorRuntime(DetailFormViewRuntime viewRuntime, String propName,
			Class<T> runtimeClass) {
		if (null == viewRuntime || null == propName) {
			return null;
		}

		// プロパティ名に "." が存在していた場合、ネストプロパティとして判断する
		Function<SectionRuntime, Boolean> isSectionRuntimeType = propName.contains(".")
				// ネストプロパティの場合は、リファレンスセクションの判定を実施
				? FormViewRuntimeUtil::isReferenceSectionRuntime
				// 通常プロパティの場合は、デフォルトセクションの判定を実施
				: FormViewRuntimeUtil::isDefaultSectionRuntime;

		// TODO ビュー中に複数の同一プロパティが存在していた場合、正しく取得することができない。
		List<SectionRuntime> sections = viewRuntime.getSections();
		for (SectionRuntime section : sections) {
			if (isSectionRuntimeType.apply(section)) {
				T propertyEditorRuntime = ((HasSectionPropertyRuntimeMap) section).getPropertyEditorRuntime(propName, runtimeClass);
				if (null != propertyEditorRuntime) {
					// ネストプロパティではなく、プロパティエディタランタイムが指定された型の場合は、値を返却する
					return propertyEditorRuntime;
				}
			}
		}

		// 警告ログ出力
		LOG.warn("Property does not exist or PropertyEditor is not defined. viewRuntime = {}, propName = {}, runtimeClass = {}",
				viewRuntime.getMetaData().getName(), propName, runtimeClass);

		// 定義が無い場合は null を返却
		return null;
	}

	/**
	 * 指定されたプロパティのプロパティエディタランタイムを取得する
	 * @param <T> プロパティエディタランタイムクラス
	 * @param defName エンティティ名
	 * @param viewName エンティティビュー名
	 * @param propName プロパティ名
	 * @param runtimeClass プロパティエディタランタイムクラス
	 * @return プロパティエディタランタイム
	 */
	public static <T extends PropertyEditorRuntime> T getPropertyEditorRuntime(String defName, String viewName, String propName,
			Class<T> runtimeClass) {
		DetailFormViewRuntime viewRuntime = getFormViewRuntime(defName, viewName, DetailFormViewRuntime.class);
		return getPropertyEditorRuntime(viewRuntime, propName, runtimeClass);
	}

	/**
	 * デフォルトセクションの判定を実施する。
	 *
	 * <p>
	 * 本メソッドで判定する SecrionRuntime は HasSectionPropertyRuntimeMap を実装する必要がある。
	 * </p>
	 *
	 * @param runtime SectionRuntimeインスタンス
	 * @return 想定したクラスであればtrue
	 */
	private static boolean isDefaultSectionRuntime(SectionRuntime runtime) {
		return runtime instanceof DefaultSectionRuntime;
	}

	/**
	 * リファレンスセクションの判定を実施する。
	 *
	 * <p>
	 * 本メソッドで判定する SecrionRuntime は HasSectionPropertyRuntimeMap を実装する必要がある。
	 * </p>
	 *
	 * @param runtime SectionRuntimeインスタンス
	 * @return 想定したクラスであればtrue
	 */
	private static boolean isReferenceSectionRuntime(SectionRuntime runtime) {
		return runtime instanceof ReferenceSectionRuntime;
	}
}
