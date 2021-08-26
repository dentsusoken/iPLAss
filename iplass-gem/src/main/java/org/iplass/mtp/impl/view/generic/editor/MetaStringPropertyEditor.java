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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.entity.definition.properties.ExpressionProperty;
import org.iplass.mtp.entity.definition.properties.LongTextProperty;
import org.iplass.mtp.entity.definition.properties.StringProperty;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.metadata.MetaDataRuntime;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.view.generic.EntityViewRuntime;
import org.iplass.mtp.impl.view.generic.FormViewRuntime;
import org.iplass.mtp.impl.view.generic.element.property.MetaPropertyLayout;
import org.iplass.mtp.view.generic.editor.EditorValue;
import org.iplass.mtp.view.generic.editor.LongTextPropertyEditor;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.StringPropertyEditor;
import org.iplass.mtp.view.generic.editor.StringPropertyEditor.StringDisplayType;

/**
 * 文字列型プロパティエディタのメタデータ
 * @author lis3wg
 */
@XmlSeeAlso({MetaLongTextPropertyEditor.class})
public class MetaStringPropertyEditor extends MetaPrimitivePropertyEditor {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 7084008247761525817L;

	public static MetaStringPropertyEditor createInstance(PropertyEditor editor) {
		if (editor instanceof LongTextPropertyEditor) {
			return MetaLongTextPropertyEditor.createInstance(editor);
		}
		return new MetaStringPropertyEditor();
	}

	/** 表示タイプ */
	private StringDisplayType displayType;

	/** RichText表示タグ許可設定 */
	private boolean allowedContent = false;

	/** 最大文字数 */
	private int maxlength;

	/** セレクトボックスの値 */
	private List<EditorValue> values;

	/** 検索条件完全一致設定有無 */
	private boolean searchExactMatchCondition;

	/** RickTextで表示モードの場合、ツールバーなどを表示しないか */
	private boolean hideRichtextEditorToolBar;

	/** リッチテキストエディタオプション */
	private String richtextEditorOption;

	/** Label形式の場合の登録制御 */
	private boolean insertWithLabelValue = true;

	/** Label形式の場合の更新制御 */
	private boolean updateWithLabelValue = false;

	/**
	 * 表示タイプを取得します。
	 * @return 表示タイプ
	 */
	public StringDisplayType getDisplayType() {
		return displayType;
	}

	/**
	 * 表示タイプを設定します。
	 * @param displayType 表示タイプ
	 */
	public void setDisplayType(StringDisplayType displayType) {
		this.displayType = displayType;
	}

	/**
	 * RichText表示タグ許可を設定します。
	 *
	 * @return allowedContent RichText表示タグ許可設定
	 */
	public boolean isAllowedContent() {
		return allowedContent;
	}

	/**
	 * RichText表示タグ許可設定を取得します。
	 *
	 * @param RichText表示タグ許可設定
	 */
	public void setAllowedContent(boolean allowedContent) {
		this.allowedContent = allowedContent;
	}

	/**
	 * 最大文字数を取得します。
	 * @return 最大文字数
	 */
	public int getMaxlength() {
		return maxlength;
	}

	/**
	 * 最大文字数を設定します。
	 * @param maxlength 最大文字数
	 */
	public void setMaxlength(int maxlength) {
		this.maxlength = maxlength;
	}

	/**
	 * セレクトボックスの値を取得します。
	 * @return セレクトボックスの値
	 */
	public List<EditorValue> getValues() {
		if (this.values == null) this.values = new ArrayList<>();
		return values;
	}

	/**
	 * セレクトボックスの値を設定します。
	 * @param values セレクトボックスの値
	 */
	public void setValues(List<EditorValue> values) {
		this.values = values;
	}

	public boolean isSearchExactMatchCondition() {
		return searchExactMatchCondition;
	}


	public void setSearchExactMatchCondition(boolean searchExactMatchCondition) {
		this.searchExactMatchCondition = searchExactMatchCondition;
	}

	public boolean isHideRichtextEditorToolBar() {
		return hideRichtextEditorToolBar;
	}

	public void setHideRichtextEditorToolBar(boolean hideRichtextEditorToolBar) {
		this.hideRichtextEditorToolBar = hideRichtextEditorToolBar;
	}

	/**
	 * リッチテキストエディタオプションを取得します。
	 * @return リッチテキストエディタオプション
	 */
	public String getRichtextEditorOption() {
	    return richtextEditorOption;
	}

	/**
	 * リッチテキストエディタオプションを設定します。
	 * @param richtextEditorOption リッチテキストエディタオプション
	 */
	public void setRichtextEditorOption(String richtextEditorOption) {
	    this.richtextEditorOption = richtextEditorOption;
	}

	/**
	 * 表示タイプがLabel形式の場合に、登録時に登録対象にするかを返します。
	 *
	 * @return true：登録対象
	 */
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

	/**
	 * 表示タイプがLabel形式の場合に、更新時に更新対象にするかを返します。
	 *
	 * @return true：更新対象
	 */
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

	@Override
	public void applyConfig(PropertyEditor editor) {
		super.fillFrom(editor);

		StringPropertyEditor e = (StringPropertyEditor) editor;
		displayType = e.getDisplayType();
		allowedContent = e.isAllowedContent();
		maxlength = e.getMaxlength();
		values = e.getValues();
		searchExactMatchCondition = e.isSearchExactMatchCondition();
		hideRichtextEditorToolBar = e.isHideRichtextEditorToolBar();
		richtextEditorOption = e.getRichtextEditorOption();
		insertWithLabelValue = e.isInsertWithLabelValue();
		updateWithLabelValue = e.isUpdateWithLabelValue();
	}

	@Override
	protected void fillFrom(PropertyEditor editor) {
		super.fillFrom(editor);

		StringPropertyEditor e = (StringPropertyEditor) editor;
		displayType = e.getDisplayType();
		allowedContent = e.isAllowedContent();
		maxlength = e.getMaxlength();
		values = e.getValues();
		searchExactMatchCondition = e.isSearchExactMatchCondition();
		hideRichtextEditorToolBar = e.isHideRichtextEditorToolBar();
		richtextEditorOption = e.getRichtextEditorOption();
		insertWithLabelValue = e.isInsertWithLabelValue();
		updateWithLabelValue = e.isUpdateWithLabelValue();
	}

	@Override
	public PropertyEditor currentConfig(String propertyName) {
		StringPropertyEditor editor = new StringPropertyEditor();
		super.fillTo(editor);

		editor.setDisplayType(displayType);
		editor.setAllowedContent(allowedContent);
		editor.setMaxlength(maxlength);
		editor.setValues(values);
		editor.setSearchExactMatchCondition(searchExactMatchCondition);
		editor.setHideRichtextEditorToolBar(hideRichtextEditorToolBar);
		editor.setRichtextEditorOption(richtextEditorOption);
		editor.setInsertWithLabelValue(insertWithLabelValue);
		editor.setUpdateWithLabelValue(updateWithLabelValue);
		return editor;
	}

	@Override
	protected void fillTo(PropertyEditor editor) {
		StringPropertyEditor e = (StringPropertyEditor) editor;
		super.fillTo(editor);

		e.setDisplayType(displayType);
		e.setAllowedContent(allowedContent);
		e.setMaxlength(maxlength);
		e.setValues(values);
		e.setSearchExactMatchCondition(searchExactMatchCondition);
		e.setHideRichtextEditorToolBar(hideRichtextEditorToolBar);
		e.setRichtextEditorOption(richtextEditorOption);
		e.setInsertWithLabelValue(insertWithLabelValue);
		e.setUpdateWithLabelValue(updateWithLabelValue);
	}

	@Override
	public MetaStringPropertyEditor copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public MetaDataRuntime createRuntime(EntityViewRuntime entityView, FormViewRuntime formView,
			MetaPropertyLayout propertyLayout, EntityContext context, EntityHandler eh) {
		return new PropertyEditorRuntime(entityView, formView, propertyLayout, context, eh) {
			@Override
			protected boolean checkPropertyType(PropertyDefinition pd) {
				if (pd == null) {
					return true;
				}
				if (pd instanceof StringProperty
						|| pd instanceof LongTextProperty) {
					return true;
				}
				if (pd instanceof ExpressionProperty) {
					ExpressionProperty ep = (ExpressionProperty)pd;
					if (ep.getResultType() == PropertyDefinitionType.STRING) {
						return true;
					}
				}
				return false;
			}
		};
	}

}
