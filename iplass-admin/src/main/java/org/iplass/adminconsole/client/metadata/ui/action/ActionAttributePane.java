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

package org.iplass.adminconsole.client.metadata.ui.action;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.web.actionmapping.definition.ActionMappingDefinition;
import org.iplass.mtp.web.actionmapping.definition.ClientCacheType;
import org.iplass.mtp.web.actionmapping.definition.HttpMethodType;
import org.iplass.mtp.web.actionmapping.definition.TokenCheck;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;

/**
 * Action属性編集パネル
 *
 */
public class ActionAttributePane extends HLayout {

	private DynamicForm methodForm;
	/** メソッド種別（GET) */
	private CheckboxItem getMethod;
	/** メソッド種別（POST) */
	private CheckboxItem postMethod;
	/** メソッド種別（PUT) */
	private CheckboxItem putMethod;
	/** メソッド種別（DELETE) */
	private CheckboxItem deleteMethod;

	private DynamicForm accessForm;
	/** 部品（直接呼び出し不可） */
	private CheckboxItem partsField;
	/** 特権実行（セキュリティ制約を受けない） */
	private CheckboxItem privilagedField;
	/** 公開Action */
	private CheckboxItem publicActionField;
	/** sessionにて同期を行うか否か */
	private CheckboxItem synchronizeOnSessionField;
	/** パスワードで認証していない場合(RememberMe)に認証を求めるか */
	private CheckboxItem needTrustedAuthenticateField;

	private DynamicForm tokenForm;
	private SelectItem tokenCheckField;
	private CheckboxItem useFixedTokenField;
	private CheckboxItem consumeField;
	private CheckboxItem exceptionRollbackField;

	private DynamicForm cacheForm;
	private SelectItem clientCacheTypeField;
	private TextItem clientCacheMaxAgeField;

	/**
	 * コンストラクタ
	 */
	public ActionAttributePane() {
		setHeight(170);
		setMargin(5);
		setMembersMargin(10);

		methodForm = new DynamicForm();
		//methodForm.setWidth100();
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

		methodForm.setItems(getMethod, postMethod, putMethod, deleteMethod);


		accessForm = new DynamicForm();
		accessForm.setWidth100();
		accessForm.setPadding(10);
		accessForm.setNumCols(1);
		accessForm.setColWidths("*");
		accessForm.setIsGroup(true);
		accessForm.setGroupTitle("Access Policy");

		partsField = new CheckboxItem("parts", "parts. not direct access");
		partsField.setShowTitle(false);
		partsField.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_metadata_action_ActionAttributePane_errDirectAccess")));

		privilagedField = new CheckboxItem("privilaged", "privilege execute");
		privilagedField.setShowTitle(false);
		privilagedField.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_metadata_action_ActionAttributePane_privilExecution")));

		publicActionField = new CheckboxItem("publicActionField", "public action");
		publicActionField.setShowTitle(false);
		publicActionField.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_metadata_action_ActionAttributePane_publicAction")));

		synchronizeOnSessionField = new CheckboxItem("synchronizeOnSession", "synchronize on session");
		synchronizeOnSessionField.setShowTitle(false);
		synchronizeOnSessionField.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_metadata_action_ActionAttributePane_synchronizeOnSession")));

		needTrustedAuthenticateField = new CheckboxItem("needTrustedAuthenticateField", "trusted authentication required");
		needTrustedAuthenticateField.setShowTitle(false);
		needTrustedAuthenticateField.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_metadata_action_ActionAttributePane_needTrustedAuthenticate")));

		accessForm.setItems(partsField, privilagedField, publicActionField, synchronizeOnSessionField, needTrustedAuthenticateField);


		tokenForm = new DynamicForm();
		tokenForm.setWidth100();
		tokenForm.setPadding(10);
		tokenForm.setNumCols(2);
		tokenForm.setColWidths(80, "*");
		tokenForm.setIsGroup(true);
		tokenForm.setGroupTitle("Token");

		tokenCheckField = new SelectItem("tokenCheck", "Token Check");
		tokenCheckField.setWidth(150);

		LinkedHashMap<String, String> tokenCheckMap = new LinkedHashMap<String, String>();
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
		useFixedTokenField.setShowTitle(false);
		useFixedTokenField.setColSpan(2);
		useFixedTokenField.setVisible(false);
		useFixedTokenField.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_metadata_action_ActionAttributePane_useFixedToken")));
		useFixedTokenField.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				consumeFieldChanged();
			}
		});

		consumeField = new CheckboxItem("consume", "consume a Token");
		consumeField.setShowTitle(false);
		consumeField.setColSpan(2);
		consumeField.setVisible(false);
		consumeField.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_metadata_action_ActionAttributePane_consumeToken")));

		exceptionRollbackField = new CheckboxItem("exceptionRollback", "rollback on exception");
		exceptionRollbackField.setShowTitle(false);
		exceptionRollbackField.setColSpan(2);
		exceptionRollbackField.setVisible(false);
		exceptionRollbackField.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_metadata_action_ActionAttributePane_exceptionRollbackToken")));

		tokenForm.setItems(tokenCheckField, useFixedTokenField, consumeField, exceptionRollbackField);


		cacheForm = new DynamicForm();
		cacheForm.setWidth100();
		cacheForm.setPadding(10);
		cacheForm.setNumCols(2);
		cacheForm.setColWidths(80, "*");
		cacheForm.setIsGroup(true);
		cacheForm.setGroupTitle("Client Cache");

		clientCacheTypeField = new SelectItem("clientCacheType", "Client Cache");
		clientCacheTypeField.setWidth(150);

		LinkedHashMap<String, String> casheTypeMap = new LinkedHashMap<String, String>();
		casheTypeMap.put(ClientCacheType.CACHE.name(), "Cache");
		casheTypeMap.put(ClientCacheType.NO_CACHE.name(), "Not Cache");
		casheTypeMap.put("", "Default");
		clientCacheTypeField.setValueMap(casheTypeMap);

		clientCacheMaxAgeField = new TextItem("clientCacheMaxAge", "Max Age");
		clientCacheMaxAgeField.setWidth(150);
		clientCacheMaxAgeField.setKeyPressFilter("[\\-0-9]");

		cacheForm.setItems(clientCacheTypeField, clientCacheMaxAgeField);


		//配置
		addMember(methodForm);
		addMember(accessForm);
		addMember(tokenForm);
		addMember(cacheForm);
	}

	/**
	 * Actionを展開します。
	 *
	 * @param definition ActionMappingDefinition
	 */
	public void setDefinition(ActionMappingDefinition definition) {
		getMethod.setValue(false);
		postMethod.setValue(false);
		putMethod.setValue(false);
		deleteMethod.setValue(false);
		if (definition.getAllowMethod() != null) {
			for (HttpMethodType type : definition.getAllowMethod()) {
				if (type.equals(HttpMethodType.GET)) {
					getMethod.setValue(true);
				}
				if (type.equals(HttpMethodType.POST)) {
					postMethod.setValue(true);
				}
				if (type.equals(HttpMethodType.PUT)) {
					putMethod.setValue(true);
				}
				if (type.equals(HttpMethodType.DELETE)) {
					deleteMethod.setValue(true);
				}
			}
		}

		partsField.setValue(definition.isParts());
		privilagedField.setValue(definition.isPrivilaged());
		publicActionField.setValue(definition.isPublicAction());
		synchronizeOnSessionField.setValue(definition.isSynchronizeOnSession());
		needTrustedAuthenticateField.setValue(definition.isNeedTrustedAuthenticate());

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
			exceptionRollbackField.hide();
			consumeField.hide();
		}

		if (definition.getClientCacheType() != null) {
			clientCacheTypeField.setValue(definition.getClientCacheType().name());
		} else {
			clientCacheTypeField.setValue("");
		}
		clientCacheMaxAgeField.setValue(definition.getClientCacheMaxAge());
	}

	/**
	 * 編集されたActionMappingDefinition情報を返します。
	 *
	 * @return 編集ActionMappingDefinition情報
	 */
	public ActionMappingDefinition getEditDefinition(ActionMappingDefinition definition) {

		List<HttpMethodType> methodTypeList = new ArrayList<HttpMethodType>();
		if (getMethod.getValue() != null && (Boolean)getMethod.getValue()) {
			methodTypeList.add(HttpMethodType.GET);
		}
		if (postMethod.getValue() != null && (Boolean)postMethod.getValue()) {
			methodTypeList.add(HttpMethodType.POST);
		}
		if (putMethod.getValue() != null && (Boolean)putMethod.getValue()) {
			methodTypeList.add(HttpMethodType.PUT);
		}
		if (deleteMethod.getValue() != null && (Boolean)deleteMethod.getValue()) {
			methodTypeList.add(HttpMethodType.DELETE);
		}
		HttpMethodType[] methodType = new HttpMethodType[methodTypeList.size()];
		int i = 0;
		for (HttpMethodType at : methodTypeList) {
			methodType[i] = at;
			i++;
		}
		definition.setAllowMethod(methodType);

		definition.setParts(SmartGWTUtil.getBooleanValue(partsField));
		definition.setPrivilaged(SmartGWTUtil.getBooleanValue(privilagedField));
		definition.setPublicAction(SmartGWTUtil.getBooleanValue(publicActionField));
		definition.setSynchronizeOnSession(SmartGWTUtil.getBooleanValue(synchronizeOnSessionField));
		definition.setNeedTrustedAuthenticate(SmartGWTUtil.getBooleanValue(needTrustedAuthenticateField));

		if (Boolean.valueOf(SmartGWTUtil.getStringValue(tokenCheckField))) {
			TokenCheck tokenCheck = new TokenCheck();
			tokenCheck.setUseFixedToken(SmartGWTUtil.getBooleanValue(useFixedTokenField));
			tokenCheck.setConsume(SmartGWTUtil.getBooleanValue(consumeField));
			tokenCheck.setExceptionRollback(SmartGWTUtil.getBooleanValue(exceptionRollbackField));
			definition.setTokenCheck(tokenCheck);
		} else {
			definition.setTokenCheck(null);
		}

		if (clientCacheTypeField.getValue() != null && !clientCacheTypeField.getValueAsString().isEmpty()) {
			definition.setClientCacheType(ClientCacheType.valueOf(SmartGWTUtil.getStringValue(clientCacheTypeField)));
		} else {
			definition.setClientCacheType(null);
		}
		if (!SmartGWTUtil.isEmpty(SmartGWTUtil.getStringValue(clientCacheMaxAgeField))) {
			definition.setClientCacheMaxAge(Long.valueOf(SmartGWTUtil.getStringValue(clientCacheMaxAgeField)));
		}

		return definition;
	}

	/**
	 * 入力チェックを実行します。
	 *
	 * @return 入力チェック結果
	 */
	public boolean validate() {
		boolean method = methodForm.validate();
		boolean access = accessForm.validate();
		boolean token = tokenForm.validate();
		boolean cache = cacheForm.validate();
		return method && access && token && cache;
	}

	private void tokenCheckChanged() {
		if (Boolean.valueOf(SmartGWTUtil.getStringValue(tokenCheckField))) {
			TokenCheck tokenCheck = new TokenCheck();
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
