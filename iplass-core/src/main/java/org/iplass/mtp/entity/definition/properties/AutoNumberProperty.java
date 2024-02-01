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

package org.iplass.mtp.entity.definition.properties;

import org.iplass.mtp.entity.definition.IndexType;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;

/**
 * <p>
 * 自動採番されるidを表すプロパティ。
 * </p>
 * <p>fixedNumberOfDigitsは、0パディングする桁数。0パディングしない場合は-1を指定。デフォルト-1。</p>
 * <p>startsWithは、採番を開始する番号。デフォルト0。</p>
 * <p>numberingTypeは、採番時、飛び番を許すか否かの設定。デフォルトALLOW_SKIPPING。</p>
 * <p>
 * formatScriptは、自動採番されるIDのフォーマット指定。<br>
 * po-${yyyy}${MM}-${nextVal()}のような形でフォーマット指定 可能。<br>
 * </p>
 * <h5>利用可能なバインド変数</h5>
 * <p>
 * nextVal()　：次の採番された番号<br>
 * yyyy　：年<br>
 * MM　：月<br>
 * dd　：日<br>
 * HH　：時、24h表記<br>
 * mm　：分<br>
 * ss　：秒<br>
 * date　：java.sql.Timestampのインスタンス<br>
 * user　：user情報<br>
 * </p>
 * 
 * @author K.Higuchi
 *
 */
public class AutoNumberProperty extends PropertyDefinition {
	private static final long serialVersionUID = -410534539656362978L;
	
	/** 桁数。桁数に満たない場合は、0埋めする。-1指定の場合は、0埋めなし。*/
	private int fixedNumberOfDigits = -1;//0埋めする桁数。-1なら、0埋めなし
	/** 自動採番されるIDのフォーマット指定。po-${yyyy}${MM}-${nextVal()}みたいな形でフォーマット指定 */
	private String formatScript;//groovyTemplate、"po-${yyyy}${MM}-${nextVal()}"みたいな
	private long startsWith = 0;
	private NumberingType numberingType = NumberingType.ALLOW_SKIPPING;
	
	public AutoNumberProperty() {
		setUpdatable(false);
		setIndexType(IndexType.UNIQUE);
	}
	
	public AutoNumberProperty(String name) {
		setName(name);
		setUpdatable(false);
	}
	
	public NumberingType getNumberingType() {
		return numberingType;
	}

	public void setNumberingType(NumberingType numberingType) {
		this.numberingType = numberingType;
	}
	
	public long getStartsWith() {
		return startsWith;
	}

	public void setStartsWith(long startsWith) {
		this.startsWith = startsWith;
	}
	
	public int getFixedNumberOfDigits() {
		return fixedNumberOfDigits;
	}

	public void setFixedNumberOfDigits(int fixedNumberOfDigits) {
		this.fixedNumberOfDigits = fixedNumberOfDigits;
	}

	public String getFormatScript() {
		return formatScript;
	}

	public void setFormatScript(String formatScript) {
		this.formatScript = formatScript;
	}

	@Override
	public Class<?> getJavaType() {
		return String.class;
	}

	@Override
	public PropertyDefinitionType getType() {
		return PropertyDefinitionType.AUTONUMBER;
	}

}
