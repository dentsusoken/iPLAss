/*
 * Copyright (C) 2015 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.shared.base.dto.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import org.iplass.adminconsole.shared.base.rpc.entity.EntityDataTransferService;
import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.SelectValue;

/**
 * EntityDataを送受信する場合に、
 * 返す値の型をGWTのシリアライズリストに追加するためのObjectクラス。
 * 実際の送受信には利用しません。
 *
 * Serviceで、このクラスを利用するIFを定義することで自動的にホワイトリスト化される。
 *
 * Entityデータを送受信する場合は、{@link EntityDataTransferService} を実装してください。
 *
 * @see http://stackoverflow.com/questions/138099/how-do-i-add-a-type-to-gwts-serialization-policy-whitelist
 */
@SuppressWarnings("unused")
public class EntityDataTransferTypeList implements Serializable {

	private static final long serialVersionUID = -1583828292080932997L;

	private String stringType;
	private String[] stringArrayType;

	private Boolean booleanType;
	private Boolean[] booleanArrayType;

	private BinaryReference binaryType;
	private BinaryReference[] binaryArrayType;

	private Date dateType;
	private Date[] dateArrayType;

	private Timestamp timestampType;
	private Timestamp[] timestampArrayType;

	private BigDecimal bigDecimalType;
	private BigDecimal[] bigDecimalArrayType;

	private Double doubleType;
	private Double[] doubleArrayType;

	private Long longType;
	private Long[] longArrayType;

	private Entity entityType;
	private Entity[] entityArrayType;

	private Time timeType;
	private Time[] timeArrayType;

	private SelectValue selectValueType;
	private SelectValue[] selectValueArrayType;

	private EntityDataTransferTypeList() {
	}

}
