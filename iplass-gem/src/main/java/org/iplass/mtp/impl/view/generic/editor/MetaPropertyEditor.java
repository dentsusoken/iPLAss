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

package org.iplass.mtp.impl.view.generic.editor;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.metadata.BaseMetaDataRuntime;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.metadata.MetaDataRuntime;
import org.iplass.mtp.impl.script.GroovyScriptEngine;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.impl.script.template.GroovyTemplate;
import org.iplass.mtp.impl.script.template.GroovyTemplateCompiler;
import org.iplass.mtp.impl.view.generic.EntityViewHandler;
import org.iplass.mtp.util.StringUtil;
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

	public MetaDataRuntime createRuntime(EntityViewHandler entityView) {
		return new PropertyEditorHandler();
	}

	public class PropertyEditorHandler extends BaseMetaDataRuntime {

		private static final String SCRIPT_PREFIX_OUTPUT_CUSTOM_STYLE = "PropertyEditorHandler_customStyle";
		private static final String SCRIPT_PREFIX_INPUT_CUSTOM_STYLE = "PropertyEditorHandler_inputCustomStyle";

		private GroovyTemplate outputCustomStyleScript;
		private GroovyTemplate inputCustomStyleScript;

		public PropertyEditorHandler() {

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
