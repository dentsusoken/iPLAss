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

package org.iplass.mtp.tenant;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.mtp.definition.Definition;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.impl.xml.jaxb.DateXmlAdapter;
import org.iplass.mtp.impl.xml.jaxb.XmlDate;
import org.iplass.mtp.impl.xml.jaxb.XmlDateTime;
import org.iplass.mtp.impl.xml.jaxb.XmlTime;

/**
 * テナント情報をあらわすオブジェクト
 * @author 片野　博之
 *
 */
@XmlSeeAlso({XmlDate.class, XmlTime.class, XmlDateTime.class})
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Tenant implements Definition {

	//TODO 削除可能？
	//createUser、updateUser、(createDate)、(updateDate)

	/** Serial Version UID	 */
	private static final long serialVersionUID = -1058356358874779414L;

	/** テナントID */
	private int id;
	/** テナント名称 */
	private String name;
	/** テナントURL */
	private String url;

	/** 有効開始日 */
	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	private Date from;
	/** 有効終了日 */
	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	private Date to;
	/** 作成者 */
	private String createUser;
	/** 作成日時 */
	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	private Timestamp createDate;
	/** 更新者 */
	private String updateUser;
	/** 更新日時 */
	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	private Timestamp updateDate;

	/** 概要 */
	private String description;

	/** 表示名 */
	@MultiLang(itemNameGetter = "getName", itemKey = "displayName", itemGetter = "getDisplayName", itemSetter = "setDisplayName", multiLangGetter = "getLocalizedDisplayNameList", multiLangSetter = "setLocalizedDisplayNameList")
	private String displayName;
	/** 表示名(Localized) */
	private List<LocalizedStringDefinition> localizedDisplayNameList;

	/** テナント設定情報 */
	private Map<String, TenantConfig> tenantConfigs;

	public Tenant() {
	}

	public Tenant(int id, String name, String url) {
		this.id = id;
		this.name = name;
		this.url = url;

		setTenantConfig(new TenantAuthInfo());
		setTenantConfig(new TenantMailInfo());
		setTenantConfig(new TenantI18nInfo());
	}

	/**
	 * テナントIDを取得します。
	 * @return テナントID
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * テナントIDを設定します。
	 * @param id テナントID
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * テナント名称を取得します。
	 * @return テナント名称
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * テナント名称を設定します。
	 * @param name テナント名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 概要を取得します。
	 * @return 概要
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * 概要を設定します。
	 * @param description 概要
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * テナント識別URLを取得します。
	 * @return テナント識別URL
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * テナント識別URLを設定します。
	 * @param url テナント識別URL
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 有効開始日を取得します。
	 * @return 有効開始日
	 */
	public Date getFrom() {
		return this.from;
	}

	/**
	 * 有効開始日を設定します。
	 * @param from 有効開始日
	 */
	public void setFrom(Date from) {
		this.from = from;
	}

	/**
	 * 有効終了日を取得します。
	 * @return 有効終了日
	 */
	public Date getTo() {
		return this.to;
	}

	/**
	 * 有効終了日を設定します。
	 * @param to 有効終了日
	 */
	public void setTo(Date to) {
		this.to = to;
	}

	/**
	 * 作成者を取得します。
	 * @return 作成者
	 */
	public String getCreateUser() {
	    return createUser;
	}

	/**
	 * 作成者を設定します。
	 * @param createUser 作成者
	 */
	public void setCreateUser(String createUser) {
	    this.createUser = createUser;
	}

	/**
	 * 作成日時を取得します。
	 * @return 作成日時
	 */
	public Timestamp getCreateDate() {
	    return createDate;
	}

	/**
	 * 作成日時を設定します。
	 * @param createDate 作成日時
	 */
	public void setCreateDate(Timestamp createDate) {
	    this.createDate = createDate;
	}

	/**
	 * 更新者を取得します。
	 * @return 更新者
	 */
	public String getUpdateUser() {
	    return updateUser;
	}

	/**
	 * 更新者を設定します。
	 * @param updateUser 更新者
	 */
	public void setUpdateUser(String updateUser) {
	    this.updateUser = updateUser;
	}

	/**
	 * 更新日時を取得します。
	 * @return 更新日時
	 */
	public Timestamp getUpdateDate() {
	    return updateDate;
	}

	/**
	 * 更新日時を設定します。
	 * @param updateDate 更新日時
	 */
	public void setUpdateDate(Timestamp updateDate) {
	    this.updateDate = updateDate;
	}

	/**
	 * 表示名を取得します。
	 * @return 表示名
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * 表示名を設定します。
	 * @param displayName
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * 多言語設定情報を取得します。
	 * @return リスト
	 */
	public List<LocalizedStringDefinition> getLocalizedDisplayNameList() {
		return localizedDisplayNameList;
	}

	/**
	 * 多言語設定情報を設定します。
	 * @param リスト
	 */
	public void setLocalizedDisplayNameList(List<LocalizedStringDefinition> localizedDisplayNameList) {
		this.localizedDisplayNameList = localizedDisplayNameList;
	}

	/**
	 * 多言語設定情報を追加します。
	 * @param 多言語設定情報
	 */
	public void addLocalizedDisplayName(LocalizedStringDefinition localizedDisplayName) {
		if (localizedDisplayNameList == null) {
			localizedDisplayNameList = new ArrayList<LocalizedStringDefinition>();
		}

		localizedDisplayNameList.add(localizedDisplayName);
	}

	public List<TenantConfig> getTenantConfigs() {
		if (tenantConfigs != null) {
			return new ArrayList<>(tenantConfigs.values());
		} else {
			return null;
		}
	}

	public void setTenantConfigs(List<TenantConfig> tenantConfigs) {
		if (tenantConfigs != null) {
			this.tenantConfigs = tenantConfigs.stream().collect(
					Collectors.toMap(config -> config.getClass().getName(), config -> config));
		} else {
			this.tenantConfigs = null;
		}
	}

	public void setTenantConfig(TenantConfig tenantConfig) {
		if (tenantConfigs == null) {
			tenantConfigs = new HashMap<>();
		}
		tenantConfigs.put(tenantConfig.getClass().getName(), tenantConfig);
	}

	@SuppressWarnings("unchecked")
	public <T extends TenantConfig> T getTenantConfig(Class<T> tenantConfigClass) {
		if (tenantConfigs != null) {
			return (T)tenantConfigs.get(tenantConfigClass.getName());
		}
		return null;
	}

	public <T extends TenantConfig> void removeTenantConfig(Class<T> tenantConfigClass) {
		if (tenantConfigs != null) {
			tenantConfigs.remove(tenantConfigClass.getName());
		}
	}

}
