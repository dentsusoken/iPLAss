/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.entity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.entity.definition.DataLocalizationStrategy;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.l10n.EachInstanceDataLocalizationStrategy;

import com.smartgwt.client.types.AutoFitWidthApproach;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;

public class DataLocalizationPane extends VLayout {

	/** フォーム */
	private DynamicForm typeForm;

	/** タイプ設定 */
	private SelectItem dataLocalizationTypeField;

	private LangSelectPane langSelectPane;
	private EachInstanceStrategyPane eachInstancePane;
	private EachPropertyStrategyPane eachPropertyPane;

	public DataLocalizationPane() {

		setWidth("50%");
		setMembersMargin(5);
		setMargin(5);

		//入力部分
		typeForm = new DynamicForm();
		typeForm.setWidth100();
		typeForm.setNumCols(3);
		typeForm.setColWidths(120, "*", 100);

		dataLocalizationTypeField = new SelectItem();
		dataLocalizationTypeField.setTitle("Data Localize Type");
		dataLocalizationTypeField.setWidth(150);
		LinkedHashMap<String, String> typeMap = new LinkedHashMap<String, String>();
		typeMap.put("", "Not Localize");
		for (DataLocalizationStrategyType type : DataLocalizationStrategyType.values()) {
			typeMap.put(type.name(), type.displayName());
		}
		dataLocalizationTypeField.setValueMap(typeMap);
		dataLocalizationTypeField.setDefaultValue("");
		dataLocalizationTypeField.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				String value = SmartGWTUtil.getStringValue(dataLocalizationTypeField);
				if (SmartGWTUtil.isEmpty(value)) {
					typeChanged(null);
				} else {
					typeChanged(DataLocalizationStrategyType.valueOf(value));
				}
			}
		});

		typeForm.setItems(dataLocalizationTypeField);

		//言語選択
		langSelectPane = new LangSelectPane();

		eachInstancePane = new EachInstanceStrategyPane();
		eachPropertyPane = new EachPropertyStrategyPane();

		addMember(typeForm);
		addMember(eachInstancePane);
		addMember(eachPropertyPane);
		addMember(langSelectPane);
	}

	public void setEnableLangMap(Map<String, String> enableLangMap) {
		langSelectPane.setEnableLangMap(enableLangMap);
	}

	public void setDefinition(EntityDefinition definition) {
		setDataLocalizationStrategy(definition.getDataLocalizationStrategy());
	}

	public EntityDefinition getEditDefinition(EntityDefinition definition) {
		definition.setDataLocalizationStrategy(getEditDataLocalizationStrategy());
		return definition;
	}

	/**
	 * 入力チェックを実行します。
	 *
	 * @return 入力チェック結果
	 */
	public boolean validate() {
		boolean typeValidate = typeForm.validate();
		boolean langValidate = langSelectPane.validate();
		boolean instanceValidate = eachInstancePane.validate();
		boolean propertyValidate = eachPropertyPane.validate();

		return typeValidate && langValidate && instanceValidate && propertyValidate;
	}

	/**
	 * エラー表示をクリアします。
	 */
	public void clearErrors() {
		typeForm.clearErrors(true);
		langSelectPane.clearErrors();
		eachInstancePane.clearErrors();
		eachPropertyPane.clearErrors();
	}

	private void setDataLocalizationStrategy(DataLocalizationStrategy strategy) {
		if (strategy == null) {
			dataLocalizationTypeField.setValue("");
		} else {
			DataLocalizationStrategyType type = DataLocalizationStrategyType.valueOf(strategy);
			if (type != null) {
				dataLocalizationTypeField.setValue(type.name());
			} else {
				dataLocalizationTypeField.setValue("");
			}
		}

		langSelectPane.setDataLocalizationStrategy(strategy);
		eachInstancePane.setDataLocalizationStrategy(strategy);
		eachPropertyPane.setDataLocalizationStrategy(strategy);
	}

	private DataLocalizationStrategy getEditDataLocalizationStrategy() {

		DataLocalizationStrategy newStrategy = null;

		String typeValue = SmartGWTUtil.getStringValue(dataLocalizationTypeField);
		if (!SmartGWTUtil.isEmpty(typeValue)) {
			DataLocalizationStrategyType type = DataLocalizationStrategyType.valueOf(typeValue);
			newStrategy = DataLocalizationStrategyType.typeOfDefinition(type);

			langSelectPane.getEditDataLocalizationStrategy(newStrategy);
			eachInstancePane.getEditDataLocalizationStrategy(newStrategy);
			eachPropertyPane.getEditDataLocalizationStrategy(newStrategy);
		}

		return newStrategy;
	}


	private void typeChanged(DataLocalizationStrategyType type) {
		DataLocalizationStrategy newStrategy = null;
		if (type != null) {
			newStrategy = DataLocalizationStrategyType.typeOfDefinition(type);

			//共通属性をコピー
			langSelectPane.getEditDataLocalizationStrategy(newStrategy);
		}

		setDataLocalizationStrategy(newStrategy);
	}

	private class LangSelectPane extends VLayout {

		private ListGrid grid = null;

		public LangSelectPane() {
			setWidth100();

			Label caption = new Label("Support Languages:");
			caption.setHeight(21);
			caption.setWrap(false);

			grid = new EnableLangGrid();

			addMember(caption);
			addMember(grid);
		}

		public void setEnableLangMap(Map<String, String> enableLangMap) {
			grid.setData(new ListGridRecord[]{});
			if (enableLangMap != null) {
				List<ListGridRecord> records = new ArrayList<ListGridRecord>();
				for (Entry<String, String> enableLang : enableLangMap.entrySet()) {
					records.add(createRecord(enableLang));
				}
				grid.setData(records.toArray(new ListGridRecord[]{}));
			}
		}

		public void setDataLocalizationStrategy(DataLocalizationStrategy strategy) {

			//選択状態をクリア
			grid.deselectAllRecords();

			if (strategy != null) {
				//選択
				if (strategy.getLanguages() != null) {
					for (String language : strategy.getLanguages()) {
						for (ListGridRecord record : grid.getRecords()) {
							if (language.equals(record.getAttribute("value"))) {
								grid.selectRecord(record);
								break;
							}
						}
					}
				}
				//表示
				show();
			} else {
				//非表示
				hide();
			}
		}

		public void getEditDataLocalizationStrategy(DataLocalizationStrategy strategy) {

			List<String> languages = null;
			if (grid.getSelectedRecords() != null) {
				languages = new ArrayList<String>();
				for (ListGridRecord record : grid.getSelectedRecords()) {
					languages.add(record.getAttribute("value"));
				}
			}
			strategy.setLanguages(languages);
		}

		public boolean validate() {
			return true;
		}

		public void clearErrors() {
		}

		private ListGridRecord createRecord(Entry<String, String> entry) {
			ListGridRecord record = new ListGridRecord();
			record.setAttribute("displayName", entry.getValue());
			record.setAttribute("value", entry.getKey());
			return record;
		}


		private class EnableLangGrid extends ListGrid {

			public EnableLangGrid() {
				setWidth100();
				setHeight(1);

				setShowAllColumns(true);							//列を全て表示
				setShowAllRecords(true);							//レコードを全て表示
				//setCanResizeFields(true);							//列幅変更可能
				setCanSort(false);									//ソート不可
				setCanPickFields(false);							//表示フィールドの選択不可
				setCanGroupBy(false);								//GroupByの選択不可
				setCanAutoFitFields(false);
				setAutoFitWidthApproach(AutoFitWidthApproach.BOTH);	//AutoFit時にタイトルと値を参照
				setLeaveScrollbarGap(false);						//縦スクロールバー自動表示制御
				setBodyOverflow(Overflow.VISIBLE);
				setOverflow(Overflow.VISIBLE);

				//CheckBox選択設定
				setSelectionType(SelectionStyle.SIMPLE);
				setSelectionAppearance(SelectionAppearance.CHECKBOX);

				ListGridField displayField = new ListGridField("displayName", "Language");

				setFields(displayField);
			}
		}
	}

	private class EachInstanceStrategyPane extends VLayout {

		/** フォーム */
		private DynamicForm form;

		/** 言語プロパティ名(TODO SelectItem化。ただし同じPaneでPropertyの増減が発生するのでListner化して同期すること) */
		private TextItem languagePropertyNameField;

		public EachInstanceStrategyPane() {
			setWidth100();
			setAutoHeight();

			//入力部分
			form = new DynamicForm();
			form.setWidth100();
			form.setNumCols(3);
			form.setColWidths(120, "*", 100);

			languagePropertyNameField = new TextItem();
			languagePropertyNameField.setTitle("Language Property");
			languagePropertyNameField.setWidth(150);

			form.setItems(languagePropertyNameField);

			addMember(form);
		}

		public void setDataLocalizationStrategy(DataLocalizationStrategy strategy) {

			//クリア
			languagePropertyNameField.clearValue();

			if (strategy != null && strategy instanceof EachInstanceDataLocalizationStrategy) {
				EachInstanceDataLocalizationStrategy instanceStrategy = (EachInstanceDataLocalizationStrategy)strategy;
				languagePropertyNameField.setValue(instanceStrategy.getLanguagePropertyName());
				//表示
				show();
			} else {
				//非表示
				hide();
			}
		}

		public void getEditDataLocalizationStrategy(DataLocalizationStrategy strategy) {

			if (strategy != null && strategy instanceof EachInstanceDataLocalizationStrategy) {
				EachInstanceDataLocalizationStrategy instanceStrategy = (EachInstanceDataLocalizationStrategy)strategy;

				instanceStrategy.setLanguagePropertyName(SmartGWTUtil.getStringValue(languagePropertyNameField));
			}
		}

		public boolean validate() {
			return form.validate();
		}

		public void clearErrors() {
			form.clearErrors(true);
		}
	}


	private class EachPropertyStrategyPane extends VLayout {

		public EachPropertyStrategyPane() {
			setWidth100();
		}

		public void setDataLocalizationStrategy(DataLocalizationStrategy strategy) {
			//非表示
			hide();
		}

		public void getEditDataLocalizationStrategy(DataLocalizationStrategy strategy) {
		}

		public boolean validate() {
			return true;
		}

		public void clearErrors() {
		}
	}
}
