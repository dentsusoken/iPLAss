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

package org.iplass.mtp.entity;

import java.sql.Timestamp;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.iplass.mtp.impl.entity.jaxb.EntityXmlAdapter;

/**
 * Entity（データ）を表すインタフェース。
 * 
 * @author K.Higuchi
 *
 */
@XmlJavaTypeAdapter(EntityXmlAdapter.class)
public interface Entity {
	
	//FIXME Entityの項目の更新、参照可否（セキュリティ、ワークフロー状態による）を判断できる機能（EntityRuntimeStatus的なクラス）

	/** 作成日 */
	public static final String CREATE_DATE = "createDate";
	
	/** 作成者のID */
	public static final String CREATE_BY = "createBy";//TODO createdBy
	
	/** Entityを特定するなんらかの名称（必須） */
	public static final String NAME = "name";
	
	/** Entityを一意に識別するID */
	public static final String OID = "oid";
	
	/** EntityのバージョンNo */
	public static final String VERSION = "version";
	
	/** 更新日 */
	public static final String UPDATE_DATE = "updateDate";
	
	/** 更新者のID */
	public static final String UPDATE_BY = "updateBy";//TODO updatedBy
	
	/** Entityの説明 */
	public static final String DESCRIPTION = "description";
	
	/** Entityの状態（バージョン管理下で、有効/無効を識別する） */
	public static final String STATE = "state";
	
	/** Entityが編集ロックされている場合、ロックしているユーザーのID */
	public static final String LOCKED_BY = "lockedBy";
	
	/** Entityの有効期間開始日時 */
	public static final String START_DATE = "startDate";
	
	/** Entityの有効期間終了日時 */
	public static final String END_DATE = "endDate";
	
	/** ごみ箱に入っているデータを特定するためのID */
	public static final String RECYCLE_BIN_ID = "recycleBinId";
	
	public static final String STATE_VALID_VALUE = "V";
	public static final String STATE_INVALID_VALUE = "I";
	
	/**
	 * 指定の属性名の属性値を取得します。<br>
	 * propNameには、"."にてネストされたプロパティ、"[index]"にて配列アクセスを指定可能です。<br>
	 * 例えば、
	 * "role.condition[0].name"は、getValue("role").getValue("condition")[0].getValue("name")を示します。<br>
	 * <br>
	 * 
	 * <b>注意<br></b>
	 * propNameはクライアントからの入力値を未検証のまま適用しないでください。
	 * 改竄された場合意図しないプロパティ値が取得される可能性があります。
	 * 
	 * @param propName
	 * @return
	 */
	public <P> P getValue(String propName);//キャストの記述を必要なくすために。推奨されない使い方ではありそうだけど。。

	/**
	 * 指定の属性名の属性に値をセットします。<br>
	 * propNameには、"."にてネストされたプロパティ、"[index]"にて配列アクセスを指定可能です。<br>
	 * 例えば、
	 * "role.condition[0].name"は、getValue("role").getValue("condition")[0].getValue("name")を示します。<br>
	 * <br>
	 * 
	 * <b>注意<br></b>
	 * propNameはクライアントからの入力値を未検証のまま適用しないでください。
	 * 改竄された場合意図しないプロパティに値が設定される可能性があります。
	 * 
	 * @param propName
	 * @param value
	 */
	public void setValue(String propName, Object value);
	
	/**
	 * 指定の属性名の属性値を取得します。<br>
	 * propNameには、"."にてネストされたプロパティ、"[index]"にて配列アクセスを指定可能です。<br>
	 * 例えば、
	 * "role.condition[0].name"は、getValue("role").getValue("condition")[0].getValue("name")を示します。<br>
	 * <br>
	 * 
	 * <b>注意<br></b>
	 * propNameはクライアントからの入力値を未検証のまま適用しないでください。
	 * 改竄された場合意図しないプロパティ値が取得される可能性があります。<br>
	 * <br>
	 * ※現状の実装は、getValueと同じ（将来的には可能な限り値を変換するように実装する想定）。
	 * 
	 * @param type
	 * @param propName
	 * @return
	 */
	public <P> P getValueAs(Class<P> type, String propName);
	
	public String getOid();

	public void setOid(String oid);
	
	public Long getVersion();
	public void setVersion(Long version);

	public String getName();

	public void setName(String name);
	
	public String getDescription();
	
	public void setDescription(String description);

	public Timestamp getCreateDate();

	public void setCreateDate(Timestamp createDate);

	public Timestamp getUpdateDate();

	public void setUpdateDate(Timestamp updateDate);

	public String getCreateBy();

	public void setCreateBy(String createBy);

	public String getUpdateBy();

	public void setUpdateBy(String updateBy);

	public String getDefinitionName();

	public void setDefinitionName(String definitionName);
	
	public SelectValue getState();
	public void setState(SelectValue state);
	
	public String getLockedBy();
	public void setLockedBy(String lockedBy);
	
	public Timestamp getStartDate();
	public void setStartDate(Timestamp startDate);
	
	public Timestamp getEndDate();
	public void setEndDate(Timestamp endDate);
	
	public Long getRecycleBinId();
	public void setRecycleBinId(Long recycleBinId);
	
}
