/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.action;

import java.util.LinkedHashMap;

import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.action.cache.CachableResultGridPane;
import org.iplass.adminconsole.client.metadata.ui.action.cache.CacheCriteriaTypeEditPane;
import org.iplass.adminconsole.client.metadata.ui.action.cache.CacheRelatedEntityGridPane;
import org.iplass.mtp.web.actionmapping.definition.ActionMappingDefinition;
import org.iplass.mtp.web.actionmapping.definition.cache.CacheCriteriaDefinition;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.validator.IsIntegerValidator;
import com.smartgwt.client.widgets.layout.VLayout;

public class CacheCriteriaPane extends VLayout {

//	private enum FIELD_NAME {
//		NAME,
//		MAP_FROM,
//		VALUE_OBJECT,
//	}
//
//	private ParamMapGrid grid;

	/** フォーム */
	private DynamicForm typeForm;

	/** キャッシュ設定 */
	private SelectItem serverCacheTypeField;

	private CachableResultGridPane resultGridPane;
	private CacheRelatedEntityGridPane relatedEntityGridPane;
	private DynamicForm timeToLiveForm;
	private TextItem timeToLiveField;
	private CacheCriteriaTypeEditPane typeEditPane;



	public CacheCriteriaPane() {
		setMargin(5);
		setAutoHeight();

//		HLayout captionComposit = new HLayout(5);
//		captionComposit.setHeight(25);
//
//		Label caption = new Label("Server Cache :");
//		caption.setHeight(21);
//		caption.setWrap(false);
//		captionComposit.addMember(caption);
//
//		Label captionHint = new Label();
//		SmartGWTUtil.addHintToLabel(captionHint,
//				"<style type=\"text/css\"><!--"
//				+ "ul.notes{margin-top:5px;padding-left:15px;list-style-type:disc;}"
//				+ "ul.notes li{padding:5px 0px;}"
//				+ "ul.notes li span.strong {text-decoration:underline;color:red}"
//				+ "ul.subnotes {margin-top:5px;padding-left:10px;list-style-type:circle;}"
//				+ "--></style>"
//				+ "<h3>Notes</h3>"
//				+ "<ul class=\"notes\">"
//				+ "<li>アクションのパスとしてパラメータを受け取る場合は、${n}、${paths}、{n}(旧形式) の定義が必要です。</li>"
//				+ "<li>${paths} でアクション名を除いたパス全体を取得できます。</li>"
//				+ "<li>${n} でアクション名を除いたn番目のパスが取得できます (n=1,2,・・・)。</li>"
//				+ "</ul>");
//		captionComposit.addMember(captionHint);

		//入力部分
		typeForm = new DynamicForm();
		typeForm.setWidth100();
		typeForm.setNumCols(3);
		typeForm.setColWidths(120, "*", 100);

		serverCacheTypeField = new SelectItem("serverCacheType", "Cache Criteria Type");
		serverCacheTypeField.setWidth(150);
		LinkedHashMap<String, String> casheTypeMap = new LinkedHashMap<String, String>();
		for (CacheCriteriaType type : CacheCriteriaType.values()) {
			casheTypeMap.put(type.name(), type.displayName());
		}
		casheTypeMap.put("", "Not Cache");
		serverCacheTypeField.setValueMap(casheTypeMap);
		serverCacheTypeField.setDefaultValue("");
		serverCacheTypeField.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				String value = SmartGWTUtil.getStringValue(serverCacheTypeField);
				if (value != null && !value.isEmpty()) {
					typeChanged(CacheCriteriaType.valueOf(value));
				} else {
					typeChanged(null);
				}
			}
		});

		typeForm.setItems(serverCacheTypeField);

		addMember(typeForm);
	}

	public void setCacheCriteria(CacheCriteriaDefinition cacheCriteria) {
		if (cacheCriteria != null) {
			serverCacheTypeField.setValue(CacheCriteriaType.valueOf(cacheCriteria).name());
		} else {
			serverCacheTypeField.setValue("");
		}

		if (resultGridPane != null) {
			if (contains(resultGridPane)) {
				removeMember(resultGridPane);
			}
			resultGridPane = null;
		}

		if (relatedEntityGridPane != null) {
			if (contains(relatedEntityGridPane)) {
				removeMember(relatedEntityGridPane);
			}
			relatedEntityGridPane = null;
		}

		if (timeToLiveForm != null) {
			if (contains(timeToLiveForm)) {
				removeMember(timeToLiveForm);
			}
			timeToLiveForm = null;
		}

		if (typeEditPane != null) {
			if (contains(typeEditPane)) {
				removeMember(typeEditPane);
			}
			typeEditPane = null;
		}

		if (cacheCriteria != null) {

			resultGridPane = new CachableResultGridPane();
			resultGridPane.setResults(cacheCriteria.getCachableCommandResultStatus());
			addMember(resultGridPane);

			relatedEntityGridPane = new CacheRelatedEntityGridPane();
			relatedEntityGridPane.setRelatedEntities(cacheCriteria.getRelatedEntity());
			addMember(relatedEntityGridPane);

			//入力部分
			timeToLiveForm = new DynamicForm();
			timeToLiveForm.setWidth100();
			timeToLiveForm.setNumCols(3);
			timeToLiveForm.setColWidths(120, "*", 100);
			timeToLiveField = new TextItem("timeToLive", "Time to Live (ms)");
			timeToLiveField.setWidth(150);
			timeToLiveField.setValidators(new IsIntegerValidator());
			timeToLiveField.setValue(cacheCriteria.getTimeToLive());
			timeToLiveForm.setItems(timeToLiveField);
			addMember(timeToLiveForm);

			CacheCriteriaType type = CacheCriteriaType.valueOf(cacheCriteria);
			typeEditPane = CacheCriteriaType.typeOfEditPane(type);
			if (typeEditPane != null) {
				typeEditPane.setDefinition(cacheCriteria);
				addMember(typeEditPane);
			}
		}
	}

	private void typeChanged(CacheCriteriaType type) {
		CacheCriteriaDefinition newDefinition = null;
		if (type != null) {
			//タイプにあったDefinitionを取得
			newDefinition = CacheCriteriaType.typeOfDefinition(type);

			//共通属性をコピー
			if (resultGridPane != null) {
				newDefinition = resultGridPane.getEditDefinition(newDefinition);
			}
			if (relatedEntityGridPane != null) {
				newDefinition = relatedEntityGridPane.getEditDefinition(newDefinition);
			}
			if (timeToLiveForm != null) {
				if (SmartGWTUtil.getStringValue(timeToLiveField) != null
						&& !SmartGWTUtil.getStringValue(timeToLiveField).isEmpty()) {

					newDefinition.setTimeToLive(Integer.parseInt(SmartGWTUtil.getStringValue(timeToLiveField)));
				}
			}
		}

		setCacheCriteria(newDefinition);

	}

	/**
	 * 入力チェックを実行します。
	 *
	 * @return 入力チェック結果
	 */
	public boolean validate() {
		String value = SmartGWTUtil.getStringValue(serverCacheTypeField);
		if (value != null && !value.isEmpty()) {
			return resultGridPane.validate()
				&& relatedEntityGridPane.validate()
				&& timeToLiveForm.validate()
				&& typeEditPane.validate();
		}
		return true;
	}

	/**
	 * 編集されたActionMappingDefinition情報を返します。
	 *
	 * @return 編集ActionMappingDefinition情報
	 */
	public ActionMappingDefinition getEditDefinition(ActionMappingDefinition definition) {

		String value = SmartGWTUtil.getStringValue(serverCacheTypeField);
		if (value != null && !value.isEmpty()) {
			CacheCriteriaDefinition newDefinition = CacheCriteriaType.typeOfDefinition(
					CacheCriteriaType.valueOf(value));

			newDefinition = resultGridPane.getEditDefinition(newDefinition);
			newDefinition = relatedEntityGridPane.getEditDefinition(newDefinition);

			if (timeToLiveForm != null) {
				if (SmartGWTUtil.getStringValue(timeToLiveField) != null
						&& !SmartGWTUtil.getStringValue(timeToLiveField).isEmpty()) {

					newDefinition.setTimeToLive(Integer.parseInt(SmartGWTUtil.getStringValue(timeToLiveField)));
				}
			}

			if (typeEditPane != null) {
				newDefinition = typeEditPane.getEditDefinition(newDefinition);
			}

			definition.setCacheCriteria(newDefinition);
		} else {
			definition.setCacheCriteria(null);
		}
		return definition;
	}

}
