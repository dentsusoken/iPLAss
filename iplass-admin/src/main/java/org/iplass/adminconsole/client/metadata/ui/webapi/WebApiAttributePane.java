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

package org.iplass.adminconsole.client.metadata.ui.webapi;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.webapi.definition.CacheControlType;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.StateType;
import org.iplass.mtp.webapi.definition.WebApiDefinition;
import org.iplass.mtp.webapi.definition.WebApiTokenCheck;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;

/**
 * WebApi属性編集パネル
 *
 */
public class WebApiAttributePane extends HLayout {

	/** フォーム */
	private DynamicForm methodForm;
	private DynamicForm accessForm;
	private DynamicForm tokenForm;
	private DynamicForm cacheForm;

	/** メソッド種別（GET) */
	private CheckboxItem getMethod;
	/** メソッド種別（POST) */
	private CheckboxItem postMethod;
	/** メソッド種別（PUT) */
	private CheckboxItem putMethod;
	/** メソッド種別（DELETE) */
	private CheckboxItem deleteMethod;
	/** メソッド種別（PATCH) */
	private CheckboxItem patchMethod;

	/** 特権実行（セキュリティ制約を受けない） */
	private CheckboxItem privilegedField;

	/** 公開WebAPI */
	private CheckboxItem publicWebAPIField;

	/** XMLHttpRequestがセットされていることを確認するか。 */
	private CheckboxItem checkXRequestedWithHeaderField;

	/** sessionにて同期を行うか否か */
	private CheckboxItem synchronizeOnSessionField;

	/** WebAPIをStatelessとして呼び出すか否か */
	private SelectItem stateTypeField;

	/** TokenCheck設定 */
	private SelectItem tokenCheckField;
	private CheckboxItem useFixedTokenField;
	private CheckboxItem consumeField;
	private CheckboxItem exceptionRollbackField;

	private SelectItem cacheControlTypeField;
	private TextItem cacheControlMaxAgeField;

	/**
	 * コンストラクタ
	 */
	public WebApiAttributePane() {

		//レイアウト設定
		setHeight(170);
		setMargin(5);
		setMembersMargin(10);

		methodForm = new DynamicForm();
		methodForm.setWidth(130);
		methodForm.setPadding(10);
		methodForm.setNumCols(1);
		methodForm.setColWidths("*");
		methodForm.setIsGroup(true);
		methodForm.setGroupTitle("Allow Method");

		getMethod = new CheckboxItem("get", "GET");
		getMethod.setShowTitle(false);
		postMethod = new CheckboxItem("post", "POST");
		postMethod.setShowTitle(false);
		putMethod = new CheckboxItem("put", "PUT");
		putMethod.setShowTitle(false);
		deleteMethod = new CheckboxItem("delete", "DELETE");
		deleteMethod.setShowTitle(false);
		patchMethod = new CheckboxItem("patch", "PATCH");
		patchMethod.setShowTitle(false);

		methodForm.setItems(getMethod, postMethod, putMethod, deleteMethod, patchMethod);


		accessForm = new DynamicForm();
		accessForm.setWidth100();
		accessForm.setPadding(10);
		accessForm.setNumCols(2);
		accessForm.setColWidths(80, "*");
		accessForm.setIsGroup(true);
		accessForm.setGroupTitle("Access Policy");

		privilegedField = new CheckboxItem("privileged", "Privilege execute");
		privilegedField.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_metadata_webapi_WebAPIAttributePane_privilExecution")));

		publicWebAPIField = new CheckboxItem("publicWebAPI", "Public WebAPI");
		publicWebAPIField.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_metadata_webapi_WebAPIAttributePane_publicWebAPI")));

		checkXRequestedWithHeaderField = new CheckboxItem("checkXRequestedWithHeader", "check X-Requested-With Header");
		checkXRequestedWithHeaderField.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_metadata_webapi_WebAPIAttributePane_checkXRequestedWithHeader")));

		synchronizeOnSessionField = new CheckboxItem("synchronizeOnSession", "Synchronize On Session");
		synchronizeOnSessionField.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_metadata_webapi_WebAPIAttributePane_synchronizeOnSession")));

		stateTypeField = new SelectItem();
		stateTypeField.setTitle("State Type");
		stateTypeField.setWidth(150);
		stateTypeField.setStartRow(true);

		LinkedHashMap<String, String> stateTypeMap = new LinkedHashMap<>();
		stateTypeMap.put(StateType.STATEFUL.toString(), StateType.STATEFUL.toString());
		stateTypeMap.put(StateType.STATELESS.toString(), StateType.STATELESS.toString());
		stateTypeField.setValueMap(stateTypeMap);

		accessForm.setItems(privilegedField, publicWebAPIField, checkXRequestedWithHeaderField,
				synchronizeOnSessionField, stateTypeField);

		tokenForm = new DynamicForm();
		tokenForm.setWidth100();
		tokenForm.setPadding(10);
		tokenForm.setNumCols(2);
		tokenForm.setColWidths(80, "*");
		tokenForm.setIsGroup(true);
		tokenForm.setGroupTitle("Token");

		tokenCheckField = new SelectItem("tokenCheck", "Token Check");
		tokenCheckField.setWidth(150);
		tokenCheckField.setStartRow(true);

		LinkedHashMap<String, String> tokenCheckMap = new LinkedHashMap<>();
		tokenCheckMap.put(Boolean.FALSE.toString(), "Not Check");
		tokenCheckMap.put(Boolean.TRUE.toString(), "Check");
		tokenCheckField.setValueMap(tokenCheckMap);
		tokenCheckField.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				tokenCheckChanged();
			}
		});
		useFixedTokenField = new CheckboxItem("useFixedToken", "use fixed Token");
		useFixedTokenField.setVisible(false);
		useFixedTokenField.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				consumeFieldChanged();
			}
		});
		consumeField = new CheckboxItem("consume", "consume a Token");
		consumeField.setVisible(false);
		exceptionRollbackField = new CheckboxItem("exceptionRollback", "rollback on exception");
		exceptionRollbackField.setVisible(false);

		tokenForm.setItems(tokenCheckField, useFixedTokenField, consumeField, exceptionRollbackField);

		cacheForm = new DynamicForm();
		cacheForm.setWidth100();
		cacheForm.setPadding(10);
		cacheForm.setNumCols(2);
		cacheForm.setColWidths(82, "*");
		cacheForm.setIsGroup(true);
		cacheForm.setGroupTitle("Cache Control");

		cacheControlTypeField = new SelectItem("cacheControlType", "Cache Control");
		cacheControlTypeField.setWidth(150);

		LinkedHashMap<String, String> casheTypeMap = new LinkedHashMap<>();
		casheTypeMap.put(CacheControlType.CACHE.name(), "Cache");
		casheTypeMap.put(CacheControlType.CACHE_PUBLIC.name(), "Cache Public");
		casheTypeMap.put(CacheControlType.NO_CACHE.name(), "Not Cache");
		casheTypeMap.put("", "Default");
		cacheControlTypeField.setValueMap(casheTypeMap);

		cacheControlMaxAgeField = new TextItem("cacheControlMaxAge", "Max Age");
		cacheControlMaxAgeField.setWidth(150);
		cacheControlMaxAgeField.setKeyPressFilter("[\\-0-9]");

		cacheForm.setItems(cacheControlTypeField, cacheControlMaxAgeField);

		addMember(methodForm);
		addMember(accessForm);
		addMember(tokenForm);
		addMember(cacheForm);

	}

	/**
	 * WebAPIを展開します。
	 *
	 * @param definition WebAPIDefinition
	 */
	public void setDefinition(WebApiDefinition definition) {

		getMethod.setValue(false);
		postMethod.setValue(false);
		putMethod.setValue(false);
		deleteMethod.setValue(false);
		patchMethod.setValue(false);

		if (definition.getMethods() != null) {
			for (MethodType at : definition.getMethods()) {
				if (at.equals(MethodType.GET)) {
					getMethod.setValue(true);
				}
				if (at.equals(MethodType.POST)) {
					postMethod.setValue(true);
				}
				if (at.equals(MethodType.PUT)) {
					putMethod.setValue(true);
				}
				if (at.equals(MethodType.DELETE)) {
					deleteMethod.setValue(true);
				}
				if (at.equals(MethodType.PATCH)) {
					patchMethod.setValue(true);
				}
			}
		}

		privilegedField.setValue(definition.isPrivileged());
		publicWebAPIField.setValue(definition.isPublicWebApi());
		checkXRequestedWithHeaderField.setValue(definition.isCheckXRequestedWithHeader());
		synchronizeOnSessionField.setValue(definition.isSynchronizeOnSession());
		if (definition.getState() != null) {
			stateTypeField.setValue(definition.getState().name());
		} else {
			stateTypeField.setValue(StateType.STATEFUL.name());
		}

		if (definition.getTokenCheck() != null) {
			tokenCheckField.setValue(Boolean.TRUE.toString());
			useFixedTokenField.setValue(definition.getTokenCheck().isUseFixedToken());
			if (!definition.getTokenCheck().isUseFixedToken()) {
				consumeField.setValue(definition.getTokenCheck().isConsume());
				consumeField.show();
			}
			exceptionRollbackField.setValue(definition.getTokenCheck().isExceptionRollback());
			useFixedTokenField.show();
			exceptionRollbackField.show();
		} else {
			tokenCheckField.setValue(Boolean.FALSE.toString());
			useFixedTokenField.hide();
			consumeField.hide();
			exceptionRollbackField.hide();
		}

		if (definition.getCacheControlType() != null) {
			cacheControlTypeField.setValue(definition.getCacheControlType().name());
		} else {
			cacheControlTypeField.setValue("");
		}
		cacheControlMaxAgeField.setValue(definition.getCacheControlMaxAge());

	}

	/**
	 * 編集されたWebAPIDefinition情報を返します。
	 *
	 * @return 編集WebAPIDefinition情報
	 */
	public WebApiDefinition getEditDefinition(WebApiDefinition definition) {

		List<MethodType> methodTypeList = new ArrayList<>();
		if (getMethod.getValue() != null && (Boolean)getMethod.getValue()) {
			methodTypeList.add(MethodType.GET);
		}
		if (postMethod.getValue() != null && (Boolean)postMethod.getValue()) {
			methodTypeList.add(MethodType.POST);
		}
		if (putMethod.getValue() != null && (Boolean)putMethod.getValue()) {
			methodTypeList.add(MethodType.PUT);
		}
		if (deleteMethod.getValue() != null && (Boolean)deleteMethod.getValue()) {
			methodTypeList.add(MethodType.DELETE);
		}
		if (patchMethod.getValue() != null && (Boolean) patchMethod.getValue()) {
			methodTypeList.add(MethodType.PATCH);
		}

		MethodType[] methodType = new MethodType[methodTypeList.size()];
		int i = 0;
		for (MethodType at : methodTypeList) {
			methodType[i] = at;
			i++;
		}
		definition.setMethods(methodType);
		definition.setPrivileged(SmartGWTUtil.getBooleanValue(privilegedField));
		definition.setPublicWebApi(SmartGWTUtil.getBooleanValue(publicWebAPIField));
		definition.setCheckXRequestedWithHeader(SmartGWTUtil.getBooleanValue(checkXRequestedWithHeaderField));
		definition.setSynchronizeOnSession(SmartGWTUtil.getBooleanValue(synchronizeOnSessionField));
		definition.setState(StateType.valueOf(SmartGWTUtil.getStringValue(stateTypeField)));

		if (Boolean.valueOf(SmartGWTUtil.getStringValue(tokenCheckField))) {
			WebApiTokenCheck tokenCheck = new WebApiTokenCheck();
			tokenCheck.setUseFixedToken(SmartGWTUtil.getBooleanValue(useFixedTokenField));
			tokenCheck.setConsume(SmartGWTUtil.getBooleanValue(consumeField));
			tokenCheck.setExceptionRollback(SmartGWTUtil.getBooleanValue(exceptionRollbackField));
			definition.setTokenCheck(tokenCheck);
		} else {
			definition.setTokenCheck(null);
		}

		if (cacheControlTypeField.getValue() != null && !cacheControlTypeField.getValueAsString().isEmpty()) {
			definition.setCacheControlType(CacheControlType.valueOf(SmartGWTUtil.getStringValue(cacheControlTypeField)));
		} else {
			definition.setCacheControlType(null);
		}
		if (!SmartGWTUtil.isEmpty(SmartGWTUtil.getStringValue(cacheControlMaxAgeField))) {
			definition.setCacheControlMaxAge(Long.valueOf(SmartGWTUtil.getStringValue(cacheControlMaxAgeField)));
		}

		return definition;
	}

	/**
	 * 入力チェックを実行します。
	 *
	 * @return 入力チェック結果
	 */
	public boolean validate() {
		return tokenForm.validate() && cacheForm.validate();
	}

	private void tokenCheckChanged() {
		if (Boolean.valueOf(SmartGWTUtil.getStringValue(tokenCheckField))) {
			WebApiTokenCheck tokenCheck = new WebApiTokenCheck();
			useFixedTokenField.setValue(tokenCheck.isUseFixedToken());
			consumeField.setValue(tokenCheck.isConsume());
			exceptionRollbackField.setValue(tokenCheck.isExceptionRollback());

			useFixedTokenField.show();
			consumeField.show();
			exceptionRollbackField.show();
		} else {
			useFixedTokenField.hide();
			consumeField.hide();
			exceptionRollbackField.hide();
		}
	}

	private void consumeFieldChanged() {
		if (!Boolean.valueOf(SmartGWTUtil.getStringValue(useFixedTokenField))) {
			consumeField.show();
		} else {
			consumeField.hide();
		}
	}

}
