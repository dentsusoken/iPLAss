/*
 * Copyright (C) 2022 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
import java.util.Map;

import org.iplass.adminconsole.client.base.data.AbstractAdminDataSource;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.metadata.dto.tenant.TenantInfo;
import org.iplass.mtp.definition.DefinitionEntry;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.tenant.TenantAuthInfo;
import org.iplass.mtp.tenant.TenantI18nInfo;
import org.iplass.mtp.tenant.TenantMailInfo;
import org.iplass.mtp.tenant.web.TenantWebInfo;

import com.google.gwt.core.client.JavaScriptObject;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public abstract class BaseTenantDS extends AbstractAdminDataSource {

	protected static final String RESOURCE_PREFIX = "datasource_tenant_TenantDS_";

	protected static final DataSourceField[] fields;
	static {
		DataSourceField name = new DataSourceField("name", FieldType.TEXT);
		name.setPrimaryKey(true);
		DataSourceField title = new DataSourceField("title", FieldType.TEXT);
		DataSourceField value = new DataSourceField("value", FieldType.TEXT);
		DataSourceField displayValue = new DataSourceField("displayValue", FieldType.TEXT);
		DataSourceField category = new DataSourceField("category", FieldType.TEXT);

		fields = new DataSourceField[] { name, title, value, displayValue, category };
	}

	/** 対象テナントデータ */
	protected Tenant curTenant;

	/**
	 * Editorなどの設定が完了したレコード。 高速化のため初回Fetch時のみ生成する。
	 */
	protected LinkedHashMap<String, ListGridRecord> records;

	/** DefinitionEntry */
	protected DefinitionEntry entry;

	/**
	 * カテゴリ名を返します。
	 *
	 * @param category カテゴリ
	 * @return カテゴリ名
	 */
	public static String getTenantCategoryName(TenantCategory category) {
		return getRS(category.getDisplayName());
	}

	/**
	 * カテゴリ名を返します。
	 *
	 * @param category カテゴリ
	 * @return カテゴリ名
	 */
	public static String getTenantCategoryName(String strCategory) {
		return getRS(TenantCategory.valueOf(strCategory).getDisplayName());
	}

	/**
	 * 更新対象の{@link Tenant} データを返します。
	 *
	 * <p>
	 * {@link ListGridRecord} の値を{@link Tenant} に反映します。
	 * </p>
	 *
	 * @return 更新対象の{@link Tenant}
	 */
	public abstract Tenant getUpdateData();

	protected static String getRS(String key) {
		return AdminClientMessageUtil.getString(RESOURCE_PREFIX + key);
	}

	protected ListGridRecord getFieldRecord(String fieldKey) {
		return records.get(fieldKey);
	}

	public DefinitionEntry getTenantEntry() {
		return entry;
	}

	protected void createCommonFieldRecord(TenantCategory category, LinkedHashMap<String, String> selectList) {
		category = TenantCategory.BASICINFO;
		setCanEdit(createRecord("id", category, TenantColType.INTEGER), false);
		setCanEdit(createRecord("name", category, TenantColType.STRING), false);
		setCanEdit(createRecord("url", category, TenantColType.STRING), false);

		createRecord("displayName", category, TenantColType.STRING);
		createRecord("description", category, TenantColType.STRING);
		createRecord("from", category, TenantColType.DATE);
		createRecord("to", category, TenantColType.DATE);
	}

	protected void createAuthFieldRecord(TenantCategory category, LinkedHashMap<String, String> selectList) {
		category = TenantCategory.AUTHSETTING;
		selectList = getBoolList(getRS("toUse"), getRS("doseNotUse"));
		createBoolRecord("useRememberMe", category, selectList);
		createRecord("userAdminRoles", category, TenantColType.STRING);
		selectList = getBoolList(getRS("passResetAllow"), getRS("passResetDoseNotAllow"));
	}

	protected void createWebFieldRecord(TenantCategory category, LinkedHashMap<String, String> selectList) {
		category = TenantCategory.SCREENTRANSETTING;
		createRecord("loginUrlSelector", category, TenantColType.SCRIPT);
		createRecord("reAuthUrlSelector", category, TenantColType.SCRIPT);
		createRecord("errorUrlSelector", category, TenantColType.SCRIPT);
		createRecord("menuUrl", category, TenantColType.STRING);
		createRecord("urlForRequest", category, TenantColType.STRING);
	}

	protected void createI18nFieldRecord(TenantCategory category, LinkedHashMap<String, String> selectList,
			TenantInfo tenantInfo) {
		category = TenantCategory.MULTILINGUALSETTING;
		selectList = getBoolList(getRS("toUse"), getRS("doseNotUse"));
		createBoolRecord("useMultilingual", category, selectList);
		createRecord("useLanguages", category, TenantColType.SELECTCHECKBOX, tenantInfo.getEnableLanguageMap());
		createRecord("locale", category, TenantColType.STRING);
		createRecord("timezone", category, TenantColType.STRING);
		createRecord("outputDateFormat", category, TenantColType.STRING);
		createRecord("browserInputDateFormat", category, TenantColType.STRING);
	}

	protected void createMailFieldRecord(TenantCategory category, LinkedHashMap<String, String> selectList) {
		category = TenantCategory.MAILSENDSETTING;
		selectList = getBoolList(getRS("send"), getRS("doNotSend"));
		createBoolRecord("sendMailEnable", category, selectList);
		createRecord("mailFrom", category, TenantColType.STRING);
		createRecord("mailFromName", category, TenantColType.STRING);
		createRecord("mailReply", category, TenantColType.STRING);
		createRecord("mailReplyName", category, TenantColType.STRING);
	}

	protected void createAdditionalFieldRecord(TenantCategory category, LinkedHashMap<String, String> selectList) {
		category = TenantCategory.EXTENDSETTING;
		selectList = getBoolList(getRS("toUse"), getRS("doseNotUse"));
		createBoolRecord("usePreview", category, selectList);
	}

	@SuppressWarnings("unchecked")
	protected boolean applyToCommonField(Tenant tenant, String name, String valueKey, ListGridRecord record) {
		boolean applied = true;

		if ("id".equals(name)) {
			tenant.setId(record.getAttributeAsInt(valueKey));
		} else if ("name".equals(name)) {
			tenant.setName(record.getAttributeAsString(valueKey));
		} else if ("description".equals(name)) {
			tenant.setDescription(record.getAttributeAsString(valueKey));
		} else if ("displayName".equals(name)) {
			tenant.setDisplayName(record.getAttributeAsString(valueKey));
			Object value = record.getAttributeAsObject("localizedStringList");
			Object localizedStringDefinition = JSOHelper.convertToJava((JavaScriptObject) value);
			tenant.setLocalizedDisplayNameList((List<LocalizedStringDefinition>) localizedStringDefinition);
		} else if ("url".equals(name)) {
			tenant.setUrl(record.getAttributeAsString(valueKey));
		} else if ("from".equals(name)) {
			tenant.setFrom(new java.sql.Date(record.getAttributeAsDate(valueKey).getTime()));
		} else if ("to".equals(name)) {
			tenant.setTo(new java.sql.Date(record.getAttributeAsDate(valueKey).getTime()));
		} else {
			applied = false;
		}

		return applied;
	}

	protected boolean applyToAuthField(Tenant tenant, String name, String valueKey, ListGridRecord record) {
		TenantAuthInfo tenantAuthInfo = tenant.getTenantConfig(TenantAuthInfo.class);

		boolean applied = true;

		if ("useRememberMe".equals(name)) {
			tenantAuthInfo.setUseRememberMe(record.getAttributeAsBoolean(valueKey));
		} else if ("userAdminRoles".equals(name)) {
			List<String> adminRoles = SmartGWTUtil.convertStringToList(record.getAttributeAsString(valueKey), ",");
			if (adminRoles != null && adminRoles.isEmpty())
				adminRoles = null;
			tenantAuthInfo.setUserAdminRoles(adminRoles);
		} else {
			applied = false;
		}
		return applied;
	}

	protected boolean applyToMailField(Tenant tenant, String name, String valueKey, ListGridRecord record) {
		TenantMailInfo tenantMailInfo = tenant.getTenantConfig(TenantMailInfo.class);

		boolean applied = true;

		if ("sendMailEnable".equals(name)) {
			tenantMailInfo.setSendMailEnable(record.getAttributeAsBoolean(valueKey));
		} else if ("mailFrom".equals(name)) {
			tenantMailInfo.setMailFrom(record.getAttributeAsString(valueKey));
		} else if ("mailFromName".equals(name)) {
			tenantMailInfo.setMailFromName(record.getAttributeAsString(valueKey));
		} else if ("mailReply".equals(name)) {
			tenantMailInfo.setMailReply(record.getAttributeAsString(valueKey));
		} else if ("mailReplyName".equals(name)) {
			tenantMailInfo.setMailReplyName(record.getAttributeAsString(valueKey));
		} else {
			applied = false;
		}

		return applied;
	}

	@SuppressWarnings("unchecked")
	protected boolean applyToI18nField(Tenant tenant, String name, String valueKey, ListGridRecord record) {
		TenantI18nInfo tenantI18nInfo = tenant.getTenantConfig(TenantI18nInfo.class);

		boolean applied = true;

		if ("useMultilingual".equals(name)) {
			tenantI18nInfo.setUseMultilingual(record.getAttributeAsBoolean(valueKey));
		} else if ("useLanguages".equals(name)) {
			Object value = record.getAttributeAsObject(valueKey);
			Object useLanguages = JSOHelper.convertToJava((JavaScriptObject) value);
			tenantI18nInfo.setUseLanguageList((List<String>) useLanguages);
		} else if ("locale".equals(name)) {
			tenantI18nInfo.setLocale(record.getAttributeAsString(valueKey));
		} else if ("outputDateFormat".equals(name)) {
			tenantI18nInfo.setOutputDateFormat(record.getAttributeAsString(valueKey));
		} else if ("browserInputDateFormat".equals(name)) {
			tenantI18nInfo.setBrowserInputDateFormat(record.getAttributeAsString(valueKey));
		} else if ("timezone".equals(name)) {
			tenantI18nInfo.setTimezone(record.getAttributeAsString(valueKey));
		} else {
			applied = false;
		}

		return applied;
	}

	protected boolean applyToWebField(Tenant tenant, String name, String valueKey, ListGridRecord record) {
		TenantWebInfo tenantWebInfo = tenant.getTenantConfig(TenantWebInfo.class);
		boolean applied = true;

		if ("usePreview".equals(name)) {
			tenantWebInfo.setUsePreview(record.getAttributeAsBoolean(valueKey));
		} else if ("loginUrlSelector".equals(name)) {
			tenantWebInfo.setLoginUrlSelector(record.getAttributeAsString(valueKey));
		} else if ("reAuthUrlSelector".equals(name)) {
			tenantWebInfo.setReAuthUrlSelector(record.getAttributeAsString(valueKey));
		} else if ("errorUrlSelector".equals(name)) {
			tenantWebInfo.setErrorUrlSelector(record.getAttributeAsString(valueKey));
		} else if ("menuUrl".equals(name)) {
			tenantWebInfo.setHomeUrl(record.getAttributeAsString(valueKey));
		} else if ("urlForRequest".equals(name)) {
			tenantWebInfo.setUrlForRequest(record.getAttributeAsString(valueKey));
		} else {
			applied = false;
		}

		return applied;
	}

	protected LinkedHashMap<String, String> getBoolList(String trueDisplayName, String falseDisplayName) {
		LinkedHashMap<String, String> ret = new LinkedHashMap<>(2);

		// Recordに対してセットするMapのKEYはStringでないとエラーになるため、String
		ret.put(Boolean.TRUE.toString(), trueDisplayName);
		ret.put(Boolean.FALSE.toString(), falseDisplayName);

		return ret;
	}

	protected ListGridRecord createRecord(String name, TenantCategory category, TenantColType colType) {
		return createRecord(name, category, colType, null);
	}

	protected ListGridRecord createBoolRecord(String name, TenantCategory category,
			LinkedHashMap<String, String> selectItem) {
		return createRecord(name, category, TenantColType.BOOLEAN, selectItem);
	}

	protected ListGridRecord createComboRecord(String name, TenantCategory category,
			LinkedHashMap<String, String> selectItem) {
		return createRecord(name, category, TenantColType.SELECTCOMBO, selectItem);
	}

	private ListGridRecord createRecord(String name, TenantCategory category, TenantColType colType,
			LinkedHashMap<String, String> selectItem) {

		ListGridRecord record = new ListGridRecord();
		records.put(name, record);

		record.setAttribute("name", name);
		record.setAttribute("title", getRS(name)); // タイトルはnameをKEYにResourceから取得
		record.setAttribute("category", category.name()); // EnumをセットするとGroupHeaderでエラーになるのでStringに変更
		record.setAttribute("colType", colType);
		record.setAttribute("selectItem", selectItem);

		setCanEdit(record, true); // デフォルトは編集可

		return record;
	}

	private void setCanEdit(ListGridRecord record, boolean canEdit) {
		record.setAttribute("canEdit", canEdit);
	}

	protected void setHelpText(ListGridRecord record, String name) {
		record.setAttribute("helpText", getRS(name));
	}

	protected void setCommonFieldRecord(Tenant tenant, String valueKey, String dispKey) {
		setRecordValue("id", tenant.getId(), valueKey, dispKey);
		setRecordValue("name", tenant.getName(), valueKey, dispKey);
		setRecordValue("url", tenant.getUrl(), valueKey, dispKey);

		setRecordValue("description", tenant.getDescription(), valueKey, dispKey);
		setRecordValue("from", tenant.getFrom(), valueKey, dispKey);
		setRecordValue("to", tenant.getTo(), valueKey, dispKey);
	}

	protected void setAuthFieldRecord(Tenant tenant, String valueKey, String dispKey) {
		TenantAuthInfo auth = tenant.getTenantConfig(TenantAuthInfo.class);
		if (auth == null) {
			auth = new TenantAuthInfo();
			tenant.setTenantConfig(auth);
		}

		setRecordValue("useRememberMe", auth.isUseRememberMe(), valueKey, dispKey);
		setRecordValue("userAdminRoles", SmartGWTUtil.convertListToString(auth.getUserAdminRoles(), ","), valueKey,
				dispKey);
	}

	protected void setMailFieldRecord(Tenant tenant, String valueKey, String dispKey) {
		TenantMailInfo mail = tenant.getTenantConfig(TenantMailInfo.class);
		if (mail == null) {
			mail = new TenantMailInfo();
			tenant.setTenantConfig(mail);
		}

		setRecordValue("sendMailEnable", mail.isSendMailEnable(), valueKey, dispKey);
		setRecordValue("mailFrom", mail.getMailFrom(), valueKey, dispKey);
		setRecordValue("mailFromName", mail.getMailFromName(), valueKey, dispKey);
		setRecordValue("mailReply", mail.getMailReply(), valueKey, dispKey);
		setRecordValue("mailReplyName", mail.getMailReplyName(), valueKey, dispKey);
	}

	protected void setWebFieldRecord(Tenant tenant, String valueKey, String dispKey) {
		TenantWebInfo web = tenant.getTenantConfig(TenantWebInfo.class);
		if (web == null) {
			web = new TenantWebInfo();
			tenant.setTenantConfig(web);
		}
		setRecordValue("loginUrlSelector", web.getLoginUrlSelector(), valueKey, dispKey);
		setRecordValue("reAuthUrlSelector", web.getReAuthUrlSelector(), valueKey, dispKey);
		setRecordValue("errorUrlSelector", web.getErrorUrlSelector(), valueKey, dispKey);
		setRecordValue("menuUrl", web.getHomeUrl(), valueKey, dispKey);
		setRecordValue("urlForRequest", web.getUrlForRequest(), valueKey, dispKey);
		setRecordValue("usePreview", web.isUsePreview(), valueKey, dispKey);
	}

	protected void setI18nFieldRecord(Tenant tenant, String valueKey, String dispKey) {
		TenantI18nInfo i18n = tenant.getTenantConfig(TenantI18nInfo.class);
		if (i18n == null) {
			i18n = new TenantI18nInfo();
			tenant.setTenantConfig(i18n);
		}

		setRecordValue("useMultilingual", i18n.isUseMultilingual(), valueKey, dispKey);
		setRecordValue("useLanguages", i18n.getUseLanguageList(), valueKey, dispKey);
		setRecordValue("locale", i18n.getLocale(), valueKey, dispKey);
		setRecordValue("timezone", i18n.getTimezone(), valueKey, dispKey);
		setRecordValue("outputDateFormat", i18n.getOutputDateFormat(), valueKey, dispKey);
		setRecordValue("browserInputDateFormat", i18n.getBrowserInputDateFormat(), valueKey, dispKey);

	}

	@SuppressWarnings("unchecked")
	protected ListGridRecord setRecordValue(String fieldKey, Object value, String valueKey, String dispValueKey) {
		ListGridRecord record = getFieldRecord(fieldKey);
		if (valueKey != null) {
			record.setAttribute(valueKey, value);
		}
		if (dispValueKey != null) {
			TenantColType colType = (TenantColType) record.getAttributeAsObject("colType");
			Object dispValue = null;
			if (colType == TenantColType.BOOLEAN) {
				Map<String, String> selectItem = record.getAttributeAsMap("selectItem");
				dispValue = getSelectDispValue(selectItem, (value == null ? "" : value.toString().toLowerCase()));
			} else if (colType == TenantColType.SELECTCOMBO) {
				Map<String, String> selectItem = record.getAttributeAsMap("selectItem");
				dispValue = getSelectDispValue(selectItem, (value == null ? "" : value.toString()));
			} else if (colType == TenantColType.SELECTCHECKBOX) {
				Map<String, String> selectItem = record.getAttributeAsMap("selectItem");
				// 現状、valueはString固定
				dispValue = getMultiSelectDispValue(selectItem, (List<String>) value);
			} else if (colType == TenantColType.SCRIPT || colType == TenantColType.GROOVYTEMPLATE) {
				dispValue = getStatusDispValue((value == null ? "" : value.toString()));
			} else {
				dispValue = value;
			}
			record.setAttribute(dispValueKey, dispValue);
		}

		// 利用可否をtrueに設定(指定しないとgetEnabledする際にnullが返るので念のため)
		record.setEnabled(true);

		return record;
	}

	protected String getSelectDispValue(Map<String, String> selectItem, String value) {

		if (selectItem.containsKey(value)) {
			return selectItem.get(value.toString());
		}
		return "";
	}

	protected String getMultiSelectDispValue(Map<String, String> selectItem, List<String> values) {

		StringBuilder sb = new StringBuilder();
		int cnt = 0;
		for (String value : values) {
			if (selectItem.containsKey(value)) {
				if (cnt > 0) {
					sb.append(", ");
				}
				sb.append(selectItem.get(value));
				cnt++;
			}
		}
		return sb.toString();
	}

	protected String getStatusDispValue(String value) {
		if (value == null || value.equals("")) {
			return getRS("noSetting");
		} else {
			return getRS("setting");
		}
	}

	@SuppressWarnings("unchecked")
	protected Object getValueTypeData(Record record, String valueKey) {
		TenantColType colType = TenantColType.valueOf(record.getAttribute("colType"));
		switch (colType) {
		case STRING:
		case SCRIPT:
		case GROOVYTEMPLATE:
			return record.getAttributeAsString(valueKey);
		case INTEGER:
			return record.getAttributeAsInt(valueKey);
		case DATE:
			return record.getAttributeAsDate(valueKey);
		case BOOLEAN:
			return record.getAttributeAsBoolean(valueKey);
		case SELECTCHECKBOX:
			// 現状、useLanguages
			Object value = record.getAttributeAsObject(valueKey);
			List<String> listValue = (List<String>) JSOHelper.convertToJava((JavaScriptObject) value);
			return listValue;
		case SELECTRADIO:
		case PASSWORD:
		case SELECTCOMBO:
			// 現時点で未使用
			return record.getAttributeAsString(valueKey);
		default:
			return record.getAttributeAsString(valueKey);
		}
	}
	
	public abstract void applyToTenantField(Tenant tenant, String name, String valueKey);
}
