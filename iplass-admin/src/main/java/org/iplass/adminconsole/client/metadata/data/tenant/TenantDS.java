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
import java.util.Map;

import org.iplass.adminconsole.client.base.data.AbstractAdminDataSource;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.base.rpc.tenant.TenantServiceAsync;
import org.iplass.adminconsole.shared.base.rpc.tenant.TenantServiceFactory;
import org.iplass.adminconsole.shared.metadata.dto.tenant.TenantInfo;
import org.iplass.gem.Skin;
import org.iplass.gem.Theme;
import org.iplass.mtp.definition.DefinitionEntry;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.tenant.TenantAuthInfo;
import org.iplass.mtp.tenant.TenantI18nInfo;
import org.iplass.mtp.tenant.TenantMailInfo;
import org.iplass.mtp.tenant.gem.TenantGemInfo;
import org.iplass.mtp.tenant.web.TenantWebInfo;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class TenantDS extends AbstractAdminDataSource {

	public enum Category {
		BASICINFO("BasicInfo")
		, AUTHSETTING("AuthSetting")
		, AUTHPASSSETTING("AuthPassSetting")
		, SCREENDISPSETTING("ScreenDispSetting")
		, SCREENTRANSETTING("ScreenTranSetting")
		, MULTILINGUALSETTING("MultilingualSetting")
		, MAILSENDSETTING("MailSendSetting")
		, EXTENDSETTING("ExtendSetting");

		private String displayName;

		private Category(String displayName) {
			this.displayName = displayName;
		}

		public String getDisplayName() {
			return displayName;
		}
	}

	public enum ColType {
		STRING,
		INTEGER,
		DATE,
		BOOLEAN,
		PASSWORD, //現時点で未使用
		SELECTRADIO, //現時点で未使用
		SELECTCHECKBOX,
		SELECTCOMBO,	//現時点で未使用
		SCRIPT,
		GROOVYTEMPLATE
	}

	private static final String RESOURCE_PREFIX = "datasource_tenant_TenantDS_";

	private static TenantDS instance = null;
	private static final DataSourceField[] fields;
	static {
		DataSourceField name = new DataSourceField("name", FieldType.TEXT);
		name.setPrimaryKey(true);
		DataSourceField title = new DataSourceField("title", FieldType.TEXT);
		DataSourceField value = new DataSourceField("value", FieldType.TEXT);
		DataSourceField displayValue = new DataSourceField("displayValue", FieldType.TEXT);
		DataSourceField category = new DataSourceField("category", FieldType.TEXT);

		fields = new DataSourceField[] { name, title, value, displayValue, category };
	}

	public static TenantDS getInstance() {
		if (instance == null) {
			instance = new TenantDS();
			instance.setFields(fields);
		}
		return instance;
	}

	/**
	 * カテゴリ名を返します。
	 *
	 * @param category カテゴリ
	 * @return カテゴリ名
	 */
	public static String getCategoryName(Category category) {
		return getRS(category.getDisplayName());
	}

	/** 対象テナントデータ */
	protected Tenant curTenant;

	/**
	 * Editorなどの設定が完了したレコード。
	 * 高速化のため初回Fetch時のみ生成する。
	 */
	protected LinkedHashMap<String, ListGridRecord> records;

	protected DefinitionEntry entry;

	/**
	 * コンストラクタ
	 */
	protected TenantDS() {
		//setFields(fields);	//継承しているため、FieldとぶつかってWARNがでるのでgetInstanceで設定
	}

	@Override
	protected void executeFetch(final String requestId, final DSRequest request,
			final DSResponse response) {

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

				//dataにメッセージを指定すると、そのメッセージを表示
				//指定しないと、[Server returned FAILURE with no error message.]が表示される
				response.setAttribute("data", caught.getMessage());

				//				response.setAttribute("caught", caught);
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

				//対象テナントの保持
				entry = result.getTenantEntry();
				curTenant = (Tenant) entry.getDefinition();

				//まだRecordが生成されていない場合は生成
				if (doGetOption) {
					createTenantRecords(result);
				}

				//テナント情報を反映
				applyToRecord(curTenant);

				callback.onSuccess(result);
			}
		});
	}

	public DefinitionEntry getTenantEntry() {
		return entry;
	}

	/**
	 * <p>{@link TenantInfo} をもとに {@link ListGridRecord} を生成します。</p>
	 * <p>この時点ではまだ{@link Tenant}情報は反映しません</p>
	 *
	 * @param tenantInfo {@link TenantInfo}
	 * @return {@link ListGridRecord}
	 */
	private void createTenantRecords(TenantInfo tenantInfo) {

		records = new LinkedHashMap<>();

		Category category = null; //work用
		LinkedHashMap<String, String> selectList = null; //work用

		category = Category.BASICINFO;
		setCanEdit(createRecord("id", category, ColType.INTEGER), false);
		setCanEdit(createRecord("name", category, ColType.STRING), false);
		setCanEdit(createRecord("url", category, ColType.STRING), false);

		createRecord("displayName", category, ColType.STRING);
		createRecord("description", category, ColType.STRING);
		createRecord("from", category, ColType.DATE);
		createRecord("to", category, ColType.DATE);

		category = Category.AUTHSETTING;
		selectList = getBoolList(getRS("toUse"), getRS("doseNotUse"));
		createBoolRecord("useRememberMe", category, selectList);
		createRecord("userAdminRoles", category, ColType.STRING);
		selectList = getBoolList(getRS("passResetAllow"), getRS("passResetDoseNotAllow"));

		category = Category.SCREENDISPSETTING;
		setHelpText(createRecord("tenantImageUrl", category, ColType.STRING), "tenantImageUrlHelpText");
		setHelpText(createRecord("tenantMiniImageUrl", category, ColType.STRING), "tenantMiniImageUrlHelpText");
		setHelpText(createRecord("tenantLargeImageUrl", category, ColType.STRING), "tenantLargeImageUrlHelpText");
		setHelpText(createRecord("iconUrl", category, ColType.STRING), "iconUrlHelpText");
		//TODO jsとcssを複数指定
		createRecord("javascriptFilePath", category, ColType.STRING);
		createRecord("stylesheetFilePath", category, ColType.STRING);
		selectList = getBoolList(getRS("show"), getRS("doNotShow"));
		createBoolRecord("useDisplayName", category, selectList);
		selectList = getBoolList(getRS("show"), getRS("doNotShow"));
		createBoolRecord("dispTenantName", category, selectList);
		createRecord("tenantNameSelector", category, ColType.GROOVYTEMPLATE);
		selectList = getSkinList(tenantInfo.getSkins());
		createComboRecord("skin", category, selectList);
		selectList = getThemeList(tenantInfo.getThemes());
		createComboRecord("theme", category, selectList);

		category = Category.SCREENTRANSETTING;
		createRecord("loginUrlSelector", category, ColType.SCRIPT);
		createRecord("reAuthUrlSelector", category, ColType.SCRIPT);
		createRecord("errorUrlSelector", category, ColType.SCRIPT);
		createRecord("menuUrl", category, ColType.STRING);
		createRecord("urlForRequest", category, ColType.STRING);

		category = Category.MULTILINGUALSETTING;
		selectList = getBoolList(getRS("toUse"), getRS("doseNotUse"));
		createBoolRecord("useMultilingual", category, selectList);
		createRecord("useLanguages", category, ColType.SELECTCHECKBOX, tenantInfo.getEnableLanguageMap());
		createRecord("locale", category, ColType.STRING);
		createRecord("timezone", category, ColType.STRING);
		createRecord("outputDateFormat", category, ColType.STRING);
		createRecord("browserInputDateFormat", category, ColType.STRING);

		category = Category.MAILSENDSETTING;
		selectList = getBoolList(getRS("send"), getRS("doNotSend"));
		createBoolRecord("sendMailEnable", category, selectList);
		createRecord("mailFrom", category, ColType.STRING);
		createRecord("mailFromName", category, ColType.STRING);
		createRecord("mailReply", category, ColType.STRING);
		createRecord("mailReplyName", category, ColType.STRING);

		category = Category.EXTENDSETTING;
		selectList = getBoolList(getRS("toUse"), getRS("doseNotUse"));
		createBoolRecord("usePreview", category, selectList);
	}

	private ListGridRecord createRecord(String name, Category category, ColType colType) {
		return createRecord(name, category, colType, null);
	}

	private ListGridRecord createBoolRecord(String name, Category category, LinkedHashMap<String, String> selectItem) {
		return createRecord(name, category, ColType.BOOLEAN, selectItem);
	}

	private ListGridRecord createComboRecord(String name, Category category, LinkedHashMap<String, String> selectItem) {
		return createRecord(name, category, ColType.SELECTCOMBO, selectItem);
	}

	private ListGridRecord createRecord(String name, Category category, ColType colType,
			LinkedHashMap<String, String> selectItem) {

		ListGridRecord record = new ListGridRecord();
		records.put(name, record);

		record.setAttribute("name", name);
		record.setAttribute("title", getRS(name)); //タイトルはnameをKEYにResourceから取得
		record.setAttribute("category", category.name()); //EnumをセットするとGroupHeaderでエラーになるのでStringに変更
		record.setAttribute("colType", colType);
		record.setAttribute("selectItem", selectItem);

		setCanEdit(record, true); //デフォルトは編集可

		return record;
	}

	private void setCanEdit(ListGridRecord record, boolean canEdit) {
		record.setAttribute("canEdit", canEdit);
	}

	private void setHelpText(ListGridRecord record, String name) {
		record.setAttribute("helpText", getRS(name));
	}

	private LinkedHashMap<String, String> getBoolList(String trueDisplayName, String falseDisplayName) {
		LinkedHashMap<String, String> ret = new LinkedHashMap<>(2);

		//Recordに対してセットするMapのKEYはStringでないとエラーになるため、String
		ret.put(Boolean.TRUE.toString(), trueDisplayName);
		ret.put(Boolean.FALSE.toString(), falseDisplayName);

		return ret;
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
	private void applyToRecord(Tenant tenant) {
		applyToRecord(tenant, "value", "displayValue");
	}

	@SuppressWarnings("unchecked")
	protected Object getValueTypeData(Record record, String valueKey) {
		ColType colType = ColType.valueOf(record.getAttribute("colType"));
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
			//現状、useLanguages
			Object value = record.getAttributeAsObject(valueKey);
			List<String> listValue = (List<String>)JSOHelper.convertToJava((JavaScriptObject) value);
			return listValue;
		case SELECTRADIO:
		case PASSWORD:
		case SELECTCOMBO:
			//現時点で未使用
			return record.getAttributeAsString(valueKey);
		default:
			return record.getAttributeAsString(valueKey);
		}

	}

	/**
	 * <p>{@link Tenant} データをレコードの指定KEYに反映します。</p>
	 * <p>
	 * テナント情報を他のFieldに設定したい場合(例えば2つのテナント情報を比較表示する場合)に、
	 * 設定するFieldのKEYを指定することでレコードに値を設定します。
	 * (例えばvalueKeyはnullにしてdispKeyを指定するすると違うKEYとしてレコードに値が設定される)
	 * </p>
	 *
	 * @param tenant {@link Tenant}
	 * @param recordKey レコードのKEY
	 */
	protected void applyToRecord(Tenant tenant, String valueKey, String dispKey) {

		ListGridRecord record = null; //work用

		TenantAuthInfo auth = tenant.getTenantConfig(TenantAuthInfo.class);
		TenantMailInfo mail = tenant.getTenantConfig(TenantMailInfo.class);
		TenantI18nInfo i18n = tenant.getTenantConfig(TenantI18nInfo.class);
		TenantWebInfo web = tenant.getTenantConfig(TenantWebInfo.class);
		TenantGemInfo gem = tenant.getTenantConfig(TenantGemInfo.class);

		if (auth == null) {
			auth = new TenantAuthInfo();
			tenant.setTenantConfig(auth);
		}
		if (mail == null) {
			mail = new TenantMailInfo();
			tenant.setTenantConfig(mail);
		}
		if (i18n == null) {
			i18n = new TenantI18nInfo();
			tenant.setTenantConfig(i18n);
		}
		if (web == null) {
			web = new TenantWebInfo();
			tenant.setTenantConfig(web);
		}
		if (gem == null) {
			gem = new TenantGemInfo();
			tenant.setTenantConfig(gem);
		}

		setRecordValue("id", tenant.getId(), valueKey, dispKey);
		setRecordValue("name", tenant.getName(), valueKey, dispKey);
		setRecordValue("url", tenant.getUrl(), valueKey, dispKey);

		setRecordValue("description", tenant.getDescription(), valueKey, dispKey);
		setRecordValue("from", tenant.getFrom(), valueKey, dispKey);
		setRecordValue("to", tenant.getTo(), valueKey, dispKey);

		setRecordValue("useRememberMe", auth.isUseRememberMe(), valueKey, dispKey);
		setRecordValue("userAdminRoles", SmartGWTUtil.convertListToString(auth.getUserAdminRoles(), ","), valueKey, dispKey);

		record = setRecordValue("displayName", tenant.getDisplayName(), valueKey, dispKey);
		record.setAttribute("localizedStringList", tenant.getLocalizedDisplayNameList());
		setRecordValue("tenantImageUrl", gem.getTenantImageUrl(), valueKey, dispKey);
		setRecordValue("tenantMiniImageUrl", gem.getTenantMiniImageUrl(), valueKey, dispKey);
		setRecordValue("tenantLargeImageUrl", gem.getTenantLargeImageUrl(), valueKey, dispKey);
		setRecordValue("iconUrl", gem.getIconUrl(), valueKey, dispKey);
		//TODO jsとcssを複数指定
		setRecordValue("javascriptFilePath", gem.getJavascriptFilePath(), valueKey, dispKey);
		setRecordValue("stylesheetFilePath", gem.getStylesheetFilePath(), valueKey, dispKey);
		setRecordValue("useDisplayName", gem.isUseDisplayName(), valueKey, dispKey);
		setRecordValue("dispTenantName", gem.isDispTenantName(), valueKey, dispKey);
		record = setRecordValue("tenantNameSelector", gem.getTenantNameSelector(), valueKey, dispKey);
		record.setAttribute("localizedTenantNameSelector", gem.getLocalizedTenantNameSelector());
		String skinValue = gem.getSkin() != null ? gem.getSkin() : "";
		setRecordValue("skin", skinValue, valueKey, dispKey);
		String themeValue = gem.getTheme() != null ? gem.getTheme() : "";
		setRecordValue("theme", themeValue, valueKey, dispKey);

		setRecordValue("loginUrlSelector", web.getLoginUrlSelector(), valueKey, dispKey);
		setRecordValue("reAuthUrlSelector", web.getReAuthUrlSelector(), valueKey, dispKey);
		setRecordValue("errorUrlSelector", web.getErrorUrlSelector(), valueKey, dispKey);
		setRecordValue("menuUrl", web.getHomeUrl(), valueKey, dispKey);
		setRecordValue("urlForRequest", web.getUrlForRequest(), valueKey, dispKey);

		setRecordValue("useMultilingual", i18n.isUseMultilingual(), valueKey, dispKey);
		setRecordValue("useLanguages", i18n.getUseLanguageList(), valueKey, dispKey);
		setRecordValue("locale", i18n.getLocale(), valueKey, dispKey);
		setRecordValue("timezone", i18n.getTimezone(), valueKey, dispKey);
		setRecordValue("outputDateFormat", i18n.getOutputDateFormat(), valueKey, dispKey);
		setRecordValue("browserInputDateFormat", i18n.getBrowserInputDateFormat(), valueKey, dispKey);

		setRecordValue("sendMailEnable", mail.isSendMailEnable(), valueKey, dispKey);
		setRecordValue("mailFrom", mail.getMailFrom(), valueKey, dispKey);
		setRecordValue("mailFromName", mail.getMailFromName(), valueKey, dispKey);
		setRecordValue("mailReply", mail.getMailReply(), valueKey, dispKey);
		setRecordValue("mailReplyName", mail.getMailReplyName(), valueKey, dispKey);

		setRecordValue("usePreview", web.isUsePreview(), valueKey, dispKey);

	}

	@SuppressWarnings("unchecked")
	private ListGridRecord setRecordValue(String fieldKey, Object value, String valueKey, String dispValueKey) {
		ListGridRecord record = getFieldRecord(fieldKey);
		if (valueKey != null) {
			record.setAttribute(valueKey, value);
		}
		if (dispValueKey != null) {
			ColType colType = (ColType) record.getAttributeAsObject("colType");
			Object dispValue = null;
			if (colType == ColType.BOOLEAN) {
				Map<String, String> selectItem = record.getAttributeAsMap("selectItem");
				dispValue = getSelectDispValue(selectItem, (value == null ? "" : value.toString().toLowerCase()));
			} else if (colType == ColType.SELECTCOMBO) {
				Map<String, String> selectItem = record.getAttributeAsMap("selectItem");
				dispValue = getSelectDispValue(selectItem, (value == null ? "" : value.toString()));
			} else if (colType == ColType.SELECTCHECKBOX) {
				Map<String, String> selectItem = record.getAttributeAsMap("selectItem");
				//現状、valueはString固定
				dispValue = getMultiSelectDispValue(selectItem, (List<String>) value);
			} else if (colType == ColType.SCRIPT || colType == ColType.GROOVYTEMPLATE) {
				dispValue = getStatusDispValue((value == null ? "" : value.toString()));
			} else {
				dispValue = value;
			}
			record.setAttribute(dispValueKey, dispValue);
		}

		//利用可否をtrueに設定(指定しないとgetEnabledする際にnullが返るので念のため)
		record.setEnabled(true);

		return record;
	}

	private String getSelectDispValue(Map<String, String> selectItem, String value) {

		if (selectItem.containsKey(value)) {
			return selectItem.get(value.toString());
		}
		return "";
	}

	private String getMultiSelectDispValue(Map<String, String> selectItem, List<String> values) {

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

	private String getStatusDispValue(String value) {
		if (value == null || value.equals("")) {
			return getRS("noSetting");
		} else {
			return getRS("setting");
		}
	}

	/**
	 * 更新対象の{@link Tenant} データを返します。
	 *
	 * <p>{@link ListGridRecord} の値を{@link Tenant} に反映します。</p>
	 *
	 * @return 更新対象の{@link Tenant}
	 */
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

		//共通項目のセット（特にUpdateDateは排他制御で利用しているためセット必須）
		tenant.setCreateDate(curTenant.getCreateDate());
		tenant.setCreateUser(curTenant.getCreateUser());
		tenant.setUpdateDate(curTenant.getUpdateDate());
		tenant.setUpdateUser(curTenant.getUpdateUser());

		return tenant;
	}

	/**
	 * Tenantにレコードの値を反映します。
	 *
	 * @param tenant 更新対象のTenant
	 * @param name   更新フィールドのname値
	 * @param valueKey 更新値が格納されているフィールドKEY
	 */
	@SuppressWarnings("unchecked")
	protected void applyToTenantField(Tenant tenant, String name, String valueKey) {

		TenantMailInfo tenantMailInfo = tenant.getTenantConfig(TenantMailInfo.class);
		TenantAuthInfo tenantAuthInfo = tenant.getTenantConfig(TenantAuthInfo.class);
		TenantI18nInfo tenantI18nInfo = tenant.getTenantConfig(TenantI18nInfo.class);
		TenantWebInfo tenantWebInfo = tenant.getTenantConfig(TenantWebInfo.class);
		TenantGemInfo tenantGemInfo = tenant.getTenantConfig(TenantGemInfo.class);

		ListGridRecord record = getFieldRecord(name);

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
			tenant.setLocalizedDisplayNameList((List<LocalizedStringDefinition>)localizedStringDefinition);
		} else if ("url".equals(name)) {
			tenant.setUrl(record.getAttributeAsString(valueKey));
		} else if ("from".equals(name)) {
			//TODO java.sql.Date
			tenant.setFrom(new java.sql.Date(record.getAttributeAsDate(valueKey).getTime()));
		} else if ("to".equals(name)) {
			//TODO java.sql.Date
			tenant.setTo(new java.sql.Date(record.getAttributeAsDate(valueKey).getTime()));
		} else if ("useRememberMe".equals(name)) {
			tenantAuthInfo.setUseRememberMe(record.getAttributeAsBoolean(valueKey));
		} else if ("userAdminRoles".equals(name)) {
			List<String> adminRoles = SmartGWTUtil.convertStringToList(record.getAttributeAsString(valueKey), ",");
			if (adminRoles != null && adminRoles.isEmpty()) adminRoles = null;
			tenantAuthInfo.setUserAdminRoles(adminRoles);
		} else if ("sendMailEnable".equals(name)) {
			tenantMailInfo.setSendMailEnable(record.getAttributeAsBoolean(valueKey));
		} else if ("mailFrom".equals(name)) {
			tenantMailInfo.setMailFrom(record.getAttributeAsString(valueKey));
		} else if ("mailFromName".equals(name)) {
			tenantMailInfo.setMailFromName(record.getAttributeAsString(valueKey));
		} else if ("mailReply".equals(name)) {
			tenantMailInfo.setMailReply(record.getAttributeAsString(valueKey));
		} else if ("mailReplyName".equals(name)) {
			tenantMailInfo.setMailReplyName(record.getAttributeAsString(valueKey));
		} else if ("usePreview".equals(name)) {
			tenantWebInfo.setUsePreview(record.getAttributeAsBoolean(valueKey));
		} else if ("useMultilingual".equals(name)) {
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
		} else if ("useDisplayName".equals(name)) {
			tenantGemInfo.setUseDisplayName(record.getAttributeAsBoolean(valueKey));
		} else if ("dispTenantName".equals(name)) {
			tenantGemInfo.setDispTenantName(record.getAttributeAsBoolean(valueKey));
		} else if ("tenantNameSelector".equals(name)) {
			tenantGemInfo.setTenantNameSelector(record.getAttributeAsString(valueKey));
			Object value = record.getAttributeAsObject("localizedTenantNameSelector");
			Object localizedTenantNameSelector = JSOHelper.convertToJava((JavaScriptObject) value);
			tenantGemInfo.setLocalizedTenantNameSelector((List<LocalizedStringDefinition>) localizedTenantNameSelector);
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
		}
	}

	private ListGridRecord getFieldRecord(String fieldKey) {
		return records.get(fieldKey);
	}

	private static String getRS(String key) {
		return AdminClientMessageUtil.getString(RESOURCE_PREFIX + key);
	}

}
