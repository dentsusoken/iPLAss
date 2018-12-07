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

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.action.cache.CachableResultGridPane;
import org.iplass.adminconsole.client.metadata.ui.action.cache.CacheCriteriaTypeEditPane;
import org.iplass.adminconsole.client.metadata.ui.action.cache.CacheRelatedEntityGridPane;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.web.actionmapping.definition.ActionMappingDefinition;
import org.iplass.mtp.web.actionmapping.definition.cache.CacheCriteriaDefinition;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.validator.IsIntegerValidator;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class CacheCriteriaPane extends VLayout {

//	private enum FIELD_NAME {
//		NAME,
//		MAP_FROM,
//		VALUE_OBJECT,
//	}
//
//	private ParamMapGrid grid;

	/** メタデータサービス */
	private final MetaDataServiceAsync service = MetaDataServiceFactory.get();

	/** フォーム */
	private DynamicForm typeForm;

	/** キャッシュ設定 */
	private SelectItem serverCacheTypeField;

	private DynamicForm clearForm;
	private ButtonItem clearActionCacheBtn;
	private ButtonItem clearTenantActionCacheBtn;

	private CachableResultGridPane resultGridPane;
	private CacheRelatedEntityGridPane relatedEntityGridPane;
	private DynamicForm timeToLiveForm;
	private TextItem timeToLiveField;
	private CacheCriteriaTypeEditPane typeEditPane;



	public CacheCriteriaPane(ActionEditPane actionEditPane) {
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
		typeForm.setNumCols(2);
		typeForm.setColWidths(120, "*");

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

		clearForm = new DynamicForm();
		clearForm.setAutoWidth();
		clearForm.setPadding(10);
		clearForm.setCellPadding(5);
		clearForm.setNumCols(1);
		clearForm.setColWidths("*");
		clearForm.setIsGroup(true);
		clearForm.setVisible(false);
		clearForm.setGroupTitle("Clear Cache");

		clearActionCacheBtn = new ButtonItem("clearActionCache", "Clear Action Cache");
		clearActionCacheBtn.setWidth(150);
		clearActionCacheBtn.setShowTitle(false);
		clearActionCacheBtn.setPrompt(SmartGWTUtil.getHoverString(
				AdminClientMessageUtil.getString("ui_metadata_action_cache_CacheCriteriaPane_clearActionCacheBtnHint")));
		clearActionCacheBtn.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				SC.ask(AdminClientMessageUtil.getString("ui_metadata_action_cache_CacheCriteriaPane_clearCacheConfirm"),
						AdminClientMessageUtil.getString("ui_metadata_action_cache_CacheCriteriaPane_clearActionCacheConfirmComment"), new BooleanCallback() {

					@Override
					public void execute(Boolean value) {
						if(value) {
							service.clearActionCache(TenantInfoHolder.getId(), actionEditPane.getActionName(), new AsyncCallback<Void>() {

								@Override
								public void onSuccess(Void result) {
									SC.say(AdminClientMessageUtil.getString("ui_metadata_action_cache_CacheCriteriaPane_completion"),
											AdminClientMessageUtil.getString("ui_metadata_action_cache_CacheCriteriaPane_clearActionCacheComp"));

									GWT.log("Content cache of Action has been cleared.");
								}

								@Override
								public void onFailure(Throwable caught) {
									SC.warn(AdminClientMessageUtil.getString("ui_metadata_action_cache_CacheCriteriaPane_clearActionCacheFailed") + caught.getMessage());

									GWT.log(caught.getMessage(), caught);
								}
							});
						}
					}
				});
			}
		});

		clearTenantActionCacheBtn = new ButtonItem("clearTenantActionCache", "Clear Tenant Action Cache");
		clearTenantActionCacheBtn.setWidth(150);
		clearTenantActionCacheBtn.setShowTitle(false);
		clearTenantActionCacheBtn.setPrompt(SmartGWTUtil.getHoverString(
				AdminClientMessageUtil.getString("ui_metadata_action_cache_CacheCriteriaPane_clearTenantActionCacheBtnHint")));
		clearTenantActionCacheBtn.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				SC.ask(AdminClientMessageUtil.getString("ui_metadata_action_cache_CacheCriteriaPane_clearCacheConfirm"),
						AdminClientMessageUtil.getString("ui_metadata_action_cache_CacheCriteriaPane_clearTenantActionCacheConfirmComment"), new BooleanCallback() {

					@Override
					public void execute(Boolean value) {
						if(value) {
							service.clearTenantActionCache(TenantInfoHolder.getId(), new AsyncCallback<Void>() {

								@Override
								public void onSuccess(Void result) {
									SC.say(AdminClientMessageUtil.getString("ui_metadata_action_cache_CacheCriteriaPane_completion"),
											AdminClientMessageUtil.getString("ui_metadata_action_cache_CacheCriteriaPane_clearTenantActionCacheComp"));

									GWT.log("Content cache of all tenant Action has been cleared.");
								}

								@Override
								public void onFailure(Throwable caught) {
									SC.warn(AdminClientMessageUtil.getString("ui_metadata_action_cache_CacheCriteriaPane_clearTenantActionCacheFailed") + caught.getMessage());

									GWT.log(caught.getMessage(), caught);
								}
							});
						}
					}
				});
			}
		});

		clearForm.setItems(clearActionCacheBtn, clearTenantActionCacheBtn);

		HLayout selectCachePane = new HLayout();
		selectCachePane.setWidth100();
		selectCachePane.setAutoHeight();
		selectCachePane.addMember(typeForm);
		selectCachePane.addMember(clearForm);

		addMember(selectCachePane);
	}

	public void setCacheCriteria(CacheCriteriaDefinition cacheCriteria) {
		if (cacheCriteria != null) {
			serverCacheTypeField.setValue(CacheCriteriaType.valueOf(cacheCriteria).name());
			clearForm.setVisible(true);
		} else {
			serverCacheTypeField.setValue("");
			clearForm.setVisible(false);
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
