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

package org.iplass.adminconsole.shared.metadata.rpc.refrect;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.iplass.adminconsole.shared.metadata.dto.Name;
import org.iplass.adminconsole.shared.metadata.dto.refrect.AnalysisListDataResult;
import org.iplass.adminconsole.shared.metadata.dto.refrect.AnalysisResult;
import org.iplass.adminconsole.view.annotation.Refrectable;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.client.rpc.XsrfProtectedService;

/**
 * リフレクションを使用してインターフェースクラスを操作するためのサービスインターフェース
 * @author lis3wg
 *
 */
@RemoteServiceRelativePath("service/refrection")
public interface RefrectionService extends XsrfProtectedService {

	/**
	 * インターフェースクラスを解析して、フィールド情報と各フィールドの値を取得する。
	 * @param className インターフェースクラスのクラス名
	 * @param value インターフェースクラス
	 * @return
	 */
	public AnalysisResult analysis(int tenantId, String className, Refrectable value);

	/**
	 * インターフェースクラスの配列を解析して、フィールド情報と各フィールドの値を取得する。
	 * @param className
	 * @param valueList
	 * @return
	 */
	public AnalysisListDataResult analysisListData(int tenantId, String className, List<Refrectable> valueList);

	/**
	 * インターフェースクラスのインスタンスを生成する。
	 * @param className
	 * @return
	 */
	public Refrectable create(int tenantId, String className);

	/**
	 * インターフェースクラスのフィールドを更新する。
	 * @param value インターフェースクラス
	 * @param valueMap インターフェースクラスの更新データ
	 * @return
	 */
	public Refrectable update(int tenantId, Refrectable value, Map<String, Serializable> valueMap);

	public Name[] getSubClass(int tenantId, final String rootClassName);

}
