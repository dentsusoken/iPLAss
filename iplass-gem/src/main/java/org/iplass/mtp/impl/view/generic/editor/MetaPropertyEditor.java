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

package org.iplass.mtp.impl.view.generic.editor;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.metadata.BaseMetaDataRuntime;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.metadata.MetaDataRuntime;
import org.iplass.mtp.impl.script.GroovyScriptEngine;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.impl.script.template.GroovyTemplate;
import org.iplass.mtp.impl.script.template.GroovyTemplateCompiler;
import org.iplass.mtp.impl.view.generic.EntityViewRuntime;
import org.iplass.mtp.impl.view.generic.FormViewRuntime;
import org.iplass.mtp.impl.view.generic.element.property.MetaPropertyLayout;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.EntityViewRuntimeException;
import org.iplass.mtp.view.generic.editor.CustomPropertyEditor;
import org.iplass.mtp.view.generic.editor.PrimitivePropertyEditor;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor;

/**
 * プロパティエディタのメタデータ
 * @author lis3wg
 */
@XmlSeeAlso( {MetaReferencePropertyEditor.class, MetaPrimitivePropertyEditor.class,
	MetaCustomPropertyEditor.class})
public abstract class MetaPropertyEditor implements MetaData {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 897681879612390566L;

	public static MetaPropertyEditor createInstance(PropertyEditor editor) {
		if (editor instanceof PrimitivePropertyEditor) {
			return MetaPrimitivePropertyEditor.createInstance(editor);
		} else if (editor instanceof ReferencePropertyEditor) {
			return MetaReferencePropertyEditor.createInstance(editor);
		} else if (editor instanceof CustomPropertyEditor) {
			return MetaCustomPropertyEditor.createInstance(editor);
		}
		return null;
	}

	/** 表示カスタムスタイル */
	private String customStyle;

	/** 入力カスタムスタイル */
	private String inputCustomStyle;

	/** 表示カスタムスタイルスクリプトのキー */
	private String outputCustomStyleScriptKey;

	/** 入力カスタムスタイルスクリプトのキー */
	private String inputCustomStyleScriptKey;

	/**
	 * 表示カスタムスタイルを取得します。
	 * @return 表示カスタムスタイル
	 */
	public String getCustomStyle() {
		return customStyle;
	}

	/**
	 * 表示カスタムスタイルを設定します。
	 * @param customStyle 表示カスタムスタイル
	 */
	public void setCustomStyle(String customStyle) {
		this.customStyle = customStyle;
	}

	/**
	 * 入力カスタムスタイルを取得します。
	 * @return 入力カスタムスタイル
	 */
	public String getInputCustomStyle() {
		return inputCustomStyle;
	}

	/**
	 * 入力カスタムスタイルを設定します。
	 * @param inputCustomStyle 入力カスタムスタイル
	 */
	public void setInputCustomStyle(String inputCustomStyle) {
		this.inputCustomStyle = inputCustomStyle;
	}

	@XmlTransient
	public String getOutputCustomStyleScriptKey() {
		return outputCustomStyleScriptKey;
	}

	public void setOutputCustomStyleScriptKey(String outputCustomStyleScriptKey) {
		this.outputCustomStyleScriptKey = outputCustomStyleScriptKey;
	}

	@XmlTransient
	public String getInputCustomStyleScriptKey() {
		return inputCustomStyleScriptKey;
	}

	public void setInputCustomStyleScriptKey(String inputCustomStyleScriptKey) {
		this.inputCustomStyleScriptKey = inputCustomStyleScriptKey;
	}

	/**
	 * PropertyEditorの内容を自身に適用。
	 * @param editor
	 */
	public abstract void applyConfig(PropertyEditor editor);

	/**
	 * スーパークラスの属性にPropertyEditorの属性の値をセット。
	 * @param editor
	 */
	protected void fillFrom(PropertyEditor editor) {
		this.customStyle = editor.getCustomStyle();
		this.inputCustomStyle = editor.getInputCustomStyle();
	}

	/**
	 * 自身の内容をPropertyEditorに適用。
	 * @return editor
	 */
	public abstract PropertyEditor currentConfig(String propertyName);

	/**
	 * PropertyEditorの属性にスーパークラスの属性の値をセット。
	 * @param editor
	 */
	protected void fillTo(PropertyEditor editor) {
		editor.setCustomStyle(customStyle);
		editor.setInputCustomStyle(inputCustomStyle);
		editor.setOutputCustomStyleScriptKey(outputCustomStyleScriptKey);
		editor.setInputCustomStyleScriptKey(inputCustomStyleScriptKey);
	}

	public abstract MetaDataRuntime createRuntime(EntityViewRuntime entityView, FormViewRuntime formView,
			MetaPropertyLayout propertyLayout, EntityContext context, EntityHandler eh);

	public abstract class PropertyEditorRuntime extends BaseMetaDataRuntime {

		private static final String SCRIPT_PREFIX_OUTPUT_CUSTOM_STYLE = "PropertyEditorHandler_customStyle";
		private static final String SCRIPT_PREFIX_INPUT_CUSTOM_STYLE = "PropertyEditorHandler_inputCustomStyle";

		private GroovyTemplate outputCustomStyleScript;
		private GroovyTemplate inputCustomStyleScript;

		protected abstract boolean checkPropertyType(PropertyDefinition pd);

		public PropertyEditorRuntime(EntityViewRuntime entityView, FormViewRuntime formView,
				MetaPropertyLayout propertyLayout, EntityContext context, EntityHandler eh) {

			if (propertyLayout != null) {
				PropertyDefinition pd = propertyLayout.getProperty(propertyLayout.getPropertyId(), context, eh);
				//プロパティが取得できない場合はチェック除外(プロパティ削除などで無視されるため)
				if (pd != null && !checkPropertyType(pd)) {
					String entityName = entityView.getMetaData().getName();
					String layoutType = formView != null ? formView.getMetaData().getClass().getSimpleName() : "unknown";
					String viewName = null;
					if (formView != null && StringUtil.isNotEmpty(formView.getMetaData().getName())) {
						viewName = formView.getMetaData().getName();
					} else {
						viewName = "(default)";
					}
					throw new EntityViewRuntimeException("on " + entityName + " [" + viewName + "] view of " + layoutType + ", "
							+ "[" + pd.getName() + "] 's " + getMetaData().getClass().getSimpleName()
							+ " is unsupport " + pd.getClass().getSimpleName() + " type.");
				}
			}

			outputCustomStyleScriptKey = "PropertyEditor_OutputStyle_" + GroovyTemplateCompiler.randomName().replace("-", "_");
			if (StringUtil.isNotEmpty(customStyle)) {
				ScriptEngine scriptEngine = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();
				outputCustomStyleScript = GroovyTemplateCompiler.compile(customStyle,
						outputCustomStyleScriptKey + "_" + SCRIPT_PREFIX_OUTPUT_CUSTOM_STYLE,
						(GroovyScriptEngine) scriptEngine);
			}

			inputCustomStyleScriptKey = "PropertyEditor_InputStyle_" + GroovyTemplateCompiler.randomName().replace("-", "_");
			if (StringUtil.isNotEmpty(inputCustomStyle)) {
				ScriptEngine scriptEngine = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();
				inputCustomStyleScript = GroovyTemplateCompiler.compile(inputCustomStyle,
						inputCustomStyleScriptKey + "_" + SCRIPT_PREFIX_INPUT_CUSTOM_STYLE,
						(GroovyScriptEngine) scriptEngine);
			}

		}

		public GroovyTemplate getOutputCustomStyleScript() {
			return outputCustomStyleScript;
		}

		public GroovyTemplate getInputCustomStyleScript() {
			return inputCustomStyleScript;
		}

		@Override
		public MetaPropertyEditor getMetaData() {
			return MetaPropertyEditor.this;
		}
	}
}
