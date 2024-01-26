/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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

import org.iplass.mtp.entity.SearchResult.ResultMode;

/**
 * 検索時のオプションを指定可能です。
 * 
 * @author K.Higuchi
 *
 */
public class SearchOption {

	private ResultMode resultMode = ResultMode.AT_ONCE;
	private boolean countTotal;
	private boolean returnStructuredEntity;
	private boolean notifyListeners = true;
	
	/**
	 * resultMode=AT_ONCE,
	 * countTotal=false,
	 * returnStructuredEntity=false,
	 * notifyListeners=true
	 * で初期化します。
	 * 
	 */
	public SearchOption() {
	}

	/**
	 * 検索結果の取得モード（ResultMode）を指定して初期化します。
	 * それ以外のオプション値は、
	 * countTotal=false,
	 * returnStructuredEntity=false,
	 * notifyListeners=true
	 * で初期化します。
	 * 
	 * @param resultMode 検索結果の取得モード
	 */
	public SearchOption(ResultMode resultMode) {
		this.resultMode = resultMode;
	}
	
	public boolean isReturnStructuredEntity() {
		return returnStructuredEntity;
	}
	public void setReturnStructuredEntity(boolean returnStructuredEntity) {
		this.returnStructuredEntity = returnStructuredEntity;
	}
	public ResultMode getResultMode() {
		return resultMode;
	}
	public void setResultMode(ResultMode resultMode) {
		this.resultMode = resultMode;
	}
	public boolean isCountTotal() {
		return countTotal;
	}
	public void setCountTotal(boolean countTotal) {
		this.countTotal = countTotal;
	}
	public boolean isNotifyListeners() {
		return notifyListeners;
	}
	public void setNotifyListeners(boolean notifyListeners) {
		this.notifyListeners = notifyListeners;
	}

	/**
	 * 検索処理時に、EntityListenerに通知しないように設定します。
	 * 
	 * @return
	 */
	public SearchOption unnotifyListeners() {
		this.notifyListeners = false;
		return this;
	}
	
	/**
	 * 検索結果を、表形式ではなく構造化された形で返却するように設定します。
	 * このフラグが有効化されるのは、{@link EntityManager#searchEntity(org.iplass.mtp.entity.query.Query, SearchOption)}にて、
	 * optionにて、{@link ResultMode#AT_ONCE}が指定されている場合のみです。
	 * ストリーム形式での読み込みの場合には、当該フラグは有効化されません。
	 * 
	 * @return
	 */
	public SearchOption returnStructuredEntity() {
		this.returnStructuredEntity = true;
		return this;
	}
	
	/**
	 * 検索時に、Limit句を指定しない形の合計件数（行数）を取得するように設定します。
	 * 
	 * @return
	 */
	public SearchOption countTotal() {
		this.countTotal = true;
		return this;
	}
	
	/**
	 * 検索結果の取得モードを指定します。
	 * 
	 * @param resultMode
	 * @return
	 */
	public SearchOption resultMode(ResultMode resultMode) {
		this.resultMode = resultMode;
		return this;
	}

}
