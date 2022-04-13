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

package org.iplass.adminconsole.client.metadata.data.tenant;

import java.util.LinkedHashMap;
import java.util.List;

import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.shared.base.rpc.tenant.TenantServiceAsync;
import org.iplass.adminconsole.shared.base.rpc.tenant.TenantServiceFactory;
import org.iplass.adminconsole.shared.metadata.dto.tenant.TenantInfo;
import org.iplass.gem.Skin;
import org.iplass.gem.Theme;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.tenant.TenantAuthInfo;
import org.iplass.mtp.tenant.TenantI18nInfo;
import org.iplass.mtp.tenant.TenantMailInfo;
import org.iplass.mtp.tenant.gem.TenantGemInfo;
import org.iplass.mtp.tenant.web.TenantWebInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class GemTenantDS extends BaseTenantDS {

	private static GemTenantDS instance = null;

	public static GemTenantDS getInstance() {
		if (instance == null) {
			instance = new GemTenantDS();
			instance.setFields(fields);
		}
		return instance;
	}

	/**
	 * コンストラクタ
	 */
	protected GemTenantDS() {
		// setFields(fields); //継承しているため、FieldとぶつかってWARNがでるのでgetInstanceで設定
	}

	@Override
	protected void executeFetch(final String requestId, final DSRequest request, final DSResponse response) {

		doExecuteFetch(new AsyncCallback<TenantInfo>() {

			@Override
			public void onSuccess(TenantInfo result) {
				response.setData(records.values().toArray(new ListGridRecord[] {}));
				response.setTotalRows(records.size());

				response.setAttribute("valueObject", result);

				processResponse(requestId, response);
			}

			@Override
			public void onFailure(Throwable caught) {
				response.setStatus(RPCResponse.STATUS_FAILURE);

				// dataにメッセージを指定すると、そのメッセージを表示
				// 指定しないと、[Server returned FAILURE with no error message.]が表示される
				response.setAttribute("data", caught.getMessage());

				// response.setAttribute("caught", caught);
				processResponse(requestId, response);
			}
		});
	}

	protected void doExecuteFetch(final AsyncCallback<TenantInfo> callback) {

		final boolean doGetOption = (records != null ? false : true);

		TenantServiceAsync service = TenantServiceFactory.get();

		service.getTenantDefinitionEntry(TenantInfoHolder.getId(), doGetOption, new AsyncCallback<TenantInfo>() {

			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(TenantInfo result) {

				// 対象テナントの保持
				entry = result.getTenantEntry();
				curTenant = (Tenant) entry.getDefinition();

				// まだRecordが生成されていない場合は生成
				if (doGetOption) {
					createTenantRecords(result);
				}

				// テナント情報を反映
				applyToRecord(curTenant);

				callback.onSuccess(result);
			}
		});
	}

	/**
	 * <p>
	 * {@link TenantInfo} をもとに {@link ListGridRecord} を生成します。
	 * </p>
	 * <p>
	 * この時点ではまだ{@link Tenant}情報は反映しません
	 * </p>
	 *
	 * @param tenantInfo {@link TenantInfo}
	 * @return {@link ListGridRecord}
	 */
	protected void createTenantRecords(TenantInfo tenantInfo) {

		records = new LinkedHashMap<>();

		TenantCategory category = null; // work用
		LinkedHashMap<String, String> selectList = null; // work用

		GWT.log("debug");
		
		createCommonFieldRecord(category, selectList);
		
		GWT.log("debug2");
		
		createAuthFieldRecord(category, selectList);
		
		GWT.log("debug3");
		
		
		createGemFieldRecord(category, selectList, tenantInfo);
		
		
		GWT.log("debug4");
		
		createWebFieldRecord(category, selectList);
		
		GWT.log("debug5");
		createI18nFieldRecord(category, selectList, tenantInfo);
		
		GWT.log("debug6");
		createMailFieldRecord(category, selectList);
		
		GWT.log("debug7");
		createAdditionalFieldRecord(category, selectList);
		
		GWT.log("debug2");
	}

	protected void createGemFieldRecord(TenantCategory category, LinkedHashMap<String, String> selectList,
			TenantInfo tenantInfo) {
		category = TenantCategory.SCREENDISPSETTING;
		setHelpText(createRecord("tenantImageUrl", category, TenantColType.STRING), "tenantImageUrlHelpText");
		setHelpText(createRecord("tenantMiniImageUrl", category, TenantColType.STRING), "tenantMiniImageUrlHelpText");
		setHelpText(createRecord("tenantLargeImageUrl", category, TenantColType.STRING), "tenantLargeImageUrlHelpText");
		setHelpText(createRecord("iconUrl", category, TenantColType.STRING), "iconUrlHelpText");
		// TODO jsとcssを複数指定
		createRecord("javascriptFilePath", category, TenantColType.STRING);
		createRecord("stylesheetFilePath", category, TenantColType.STRING);
		selectList = getBoolList(getRS("show"), getRS("doNotShow"));
		createBoolRecord("useDisplayName", category, selectList);
		selectList = getBoolList(getRS("show"), getRS("doNotShow"));
		createBoolRecord("dispTenantName", category, selectList);
		selectList = getSkinList(tenantInfo.getSkins());
		createComboRecord("skin", category, selectList);
		selectList = getThemeList(tenantInfo.getThemes());
		createComboRecord("theme", category, selectList);
	}

	private LinkedHashMap<String, String> getThemeList(List<Theme> themes) {
		final LinkedHashMap<String, String> themeMap = new LinkedHashMap<>(2);
		themeMap.put("", "");
		if (themes != null && !themes.isEmpty()) {
			for (Theme theme : themes) {
				themeMap.put(theme.getThemeName(), theme.getDisplayName());
			}
		}
		return themeMap;
	}

	private LinkedHashMap<String, String> getSkinList(List<Skin> skins) {
		final LinkedHashMap<String, String> skinMap = new LinkedHashMap<>(2);
		skinMap.put("", "");
		if (skins != null && !skins.isEmpty()) {
			for (Skin skin : skins) {
				skinMap.put(skin.getSkinName(), skin.getDisplayName());
			}
		}
		return skinMap;
	}

	/**
	 * {@link Tenant} データをレコードに反映します。
	 *
	 * @param tenant {@link Tenant}
	 */
	protected void applyToRecord(Tenant tenant) {
		applyToRecord(tenant, "value", "displayValue");
	}

	protected void applyToRecord(Tenant tenant, String valueKey, String dispKey) {
		setCommonFieldRecord(tenant, valueKey, dispKey);
		setAuthFieldRecord(tenant, valueKey, dispKey);
		setMailFieldRecord(tenant, valueKey, dispKey);
		setWebFieldRecord(tenant, valueKey, dispKey);
		setI18nFieldRecord(tenant, valueKey, dispKey);
		setGemFieldRecord(tenant, valueKey, dispKey);
	}

	@Override
	public Tenant getUpdateData() {

		Tenant tenant = new Tenant();
		tenant.setTenantConfig(new TenantAuthInfo());
		tenant.setTenantConfig(new TenantMailInfo());
		tenant.setTenantConfig(new TenantI18nInfo());
		tenant.setTenantConfig(new TenantWebInfo());
		tenant.setTenantConfig(new TenantGemInfo());

		for (Record record : records.values()) {
			String name = record.getAttribute("name");
			applyToTenantField(tenant, name, "value");
		}

		// 共通項目のセット（特にUpdateDateは排他制御で利用しているためセット必須）
		tenant.setCreateDate(curTenant.getCreateDate());
		tenant.setCreateUser(curTenant.getCreateUser());
		tenant.setUpdateDate(curTenant.getUpdateDate());
		tenant.setUpdateUser(curTenant.getUpdateUser());

		return tenant;
	}

	@Override
	public void applyToTenantField(Tenant tenant, String name, String valueKey) {
		ListGridRecord record = getFieldRecord(name);

		if (applyToCommonField(tenant, name, valueKey, record)) {
		} else if (applyToAuthField(tenant, name, valueKey, record)) {
		} else if (applyToMailField(tenant, name, valueKey, record)) {
		} else if (applyToI18nField(tenant, name, valueKey, record)) {
		} else if (applyToWebField(tenant, name, valueKey, record)) {
		} else if (applyToGemField(tenant, name, valueKey, record)) {
		}
	}

	protected void setGemFieldRecord(Tenant tenant, String valueKey, String dispKey) {
		ListGridRecord record = null; // work用

		TenantGemInfo gem = tenant.getTenantConfig(TenantGemInfo.class);
		if (gem == null) {
			gem = new TenantGemInfo();
			tenant.setTenantConfig(gem);
		}

		record = setRecordValue("displayName", tenant.getDisplayName(), valueKey, dispKey);
		record.setAttribute("localizedStringList", tenant.getLocalizedDisplayNameList());
		setRecordValue("tenantImageUrl", gem.getTenantImageUrl(), valueKey, dispKey);
		setRecordValue("tenantMiniImageUrl", gem.getTenantMiniImageUrl(), valueKey, dispKey);
		setRecordValue("tenantLargeImageUrl", gem.getTenantLargeImageUrl(), valueKey, dispKey);
		setRecordValue("iconUrl", gem.getIconUrl(), valueKey, dispKey);
		// TODO jsとcssを複数指定
		setRecordValue("javascriptFilePath", gem.getJavascriptFilePath(), valueKey, dispKey);
		setRecordValue("stylesheetFilePath", gem.getStylesheetFilePath(), valueKey, dispKey);
		setRecordValue("useDisplayName", gem.isUseDisplayName(), valueKey, dispKey);
		setRecordValue("dispTenantName", gem.isDispTenantName(), valueKey, dispKey);
		String skinValue = gem.getSkin() != null ? gem.getSkin() : "";
		setRecordValue("skin", skinValue, valueKey, dispKey);
		String themeValue = gem.getTheme() != null ? gem.getTheme() : "";
		setRecordValue("theme", themeValue, valueKey, dispKey);
	}

	protected boolean applyToGemField(Tenant tenant, String name, String valueKey, ListGridRecord record) {
		TenantGemInfo tenantGemInfo = tenant.getTenantConfig(TenantGemInfo.class);
		boolean applied = true;

		if ("useDisplayName".equals(name)) {
			tenantGemInfo.setUseDisplayName(record.getAttributeAsBoolean(valueKey));
		} else if ("dispTenantName".equals(name)) {
			tenantGemInfo.setDispTenantName(record.getAttributeAsBoolean(valueKey));
		} else if ("skin".equals(name)) {
			tenantGemInfo.setSkin(record.getAttributeAsString(valueKey));
		} else if ("theme".equals(name)) {
			tenantGemInfo.setTheme(record.getAttributeAsString(valueKey));
		} else if ("tenantImageUrl".equals(name)) {
			tenantGemInfo.setTenantImageUrl(record.getAttributeAsString(valueKey));
		} else if ("tenantMiniImageUrl".equals(name)) {
			tenantGemInfo.setTenantMiniImageUrl(record.getAttributeAsString(valueKey));
		} else if ("tenantLargeImageUrl".equals(name)) {
			tenantGemInfo.setTenantLargeImageUrl(record.getAttributeAsString(valueKey));
		} else if ("iconUrl".equals(name)) {
			tenantGemInfo.setIconUrl(record.getAttributeAsString(valueKey));
		} else if ("javascriptFilePath".equals(name)) {
			tenantGemInfo.setJavascriptFilePath(record.getAttributeAsString(valueKey));
		} else if ("stylesheetFilePath".equals(name)) {
			tenantGemInfo.setStylesheetFilePath(record.getAttributeAsString(valueKey));
		} else {
			applied = false;
		}

		return applied;
	}

}
