/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.util.ArrayList;
import java.util.List;


/**
 * Entityのload時のオプションです。
 * 
 * @author K.Higuchi
 *
 */
public class LoadOption {

	private boolean withReference = true;
	private boolean withMappedByReference = true;
	private List<String> loadReferences;
	private boolean localized = false;
	private boolean notifyListeners = true;
	private boolean versioned;

	/**
	 * withReference = true,
	 * withMappedByReference = trueで設定するコンストラクタです。
	 * 
	 */
	public LoadOption() {
	}
	
	/**
	 * 指定のオプションで、LoadOptionをnewします。
	 * 
	 * @param withReference 参照プロパティも取得するように設定
	 * @param withMappedByReference 被参照の参照プロパティも取得するように設定
	 */
	public LoadOption(boolean withReference, boolean withMappedByReference) {
		this.withReference = withReference;
		this.withMappedByReference = withMappedByReference;
	}
	
	/**
	 * 指定の参照プロパティのみをロードする形で、LoadOptionをnewします。
	 * loadReferencesが指定されている場合は、
	 * withReference、withMappedByReferenceの指定によらず、loadReferencesが優先されます。
	 * 
	 * @param loadReferences ロードする参照プロパティ名のリスト
	 */
	public LoadOption(List<String> loadReferences) {
		this.loadReferences = loadReferences;
	}
	
	/**
	 * 
	 * 指定の参照プロパティのみをロードする形で、LoadOptionをnewします。
	 * loadReferenceが指定されている場合は、
	 * withReference、withMappedByReferenceの指定によらず、loadReferenceが優先されます。
	 * 
	 * 
	 * @param loadReference ロードする参照プロパティ名の可変引数
	 */
	public LoadOption(String... loadReference) {
		if (loadReference != null) {
			loadReferences = new ArrayList<String>();
			for (String lr: loadReference) {
				loadReferences.add(lr);
			}
		}
	}
	
	public boolean isVersioned() {
		return versioned;
	}

	public void setVersioned(boolean versioned) {
		this.versioned = versioned;
	}

	public boolean isNotifyListeners() {
		return notifyListeners;
	}

	public void setNotifyListeners(boolean notifyListeners) {
		this.notifyListeners = notifyListeners;
	}

	public List<String> getLoadReferences() {
		return loadReferences;
	}

	/**
	 * Load時に指定の参照プロパティのみを読み込むように設定します。
	 * loadReferencesが指定されている場合は、
	 * withReference、withMappedByReferenceの指定によらず、loadReferencesが優先されます。
	 * 
	 * @param loadReferences 
	 */
	public void setLoadReferences(List<String> loadReferences) {
		this.loadReferences = loadReferences;
	}

	public boolean isWithReference() {
		return withReference;
	}
	
	/**
	 * Load時に参照プロパティも読み込むように設定します。
	 * 
	 * @param withReference
	 */
	public void setWithReference(boolean withReference) {
		this.withReference = withReference;
	}
	
	public boolean isWithMappedByReference() {
		return withMappedByReference;
	}
	
	/**
	 * Load時に被参照の参照プロパティも読み込むように設定します（withReference=trueとなっている前提の上で）。
	 * 
	 * @param withMappedByReference
	 */
	public void setWithMappedByReference(boolean withMappedByReference) {
		this.withMappedByReference = withMappedByReference;
	}

	public boolean isLocalized() {
		return localized;
	}

	/**
	 * localized項目をLoad対象とするか否かを設定します。
	 * 
	 * @param localized
	 */
	public void setLocalized(boolean localized) {
		this.localized = localized;
	}

	/**
	 * localized=trueに設定します。
	 * @return
	 */
	public LoadOption localized() {
		this.localized = true;
		return this;
	}

	/**
	 * 
	 * Load時に、EntityListenerに通知しないように設定します。
	 * @return
	 */
	public LoadOption unnotifyListeners() {
		this.notifyListeners = false;
		return this;
	}
	
	/**
	 * 参照先のEntityがバージョン管理されているEntityの場合、
	 * 参照先のEntityを保存時点のバージョンで取得します。
	 * Load対象のEntity自体もバージョン管理されている場合は、
	 * ロード対象Entityのversionまでを指定している場合に有効なフラグとなります。
	 * 
	 * @return
	 */
	public LoadOption versioned() {
		this.versioned = true;
		return this;
	}

	@Override
	public String toString() {
		return "LoadOption [withReference=" + withReference + ", withMappedByReference=" + withMappedByReference
				+ ", loadReferences=" + loadReferences + ", localized=" + localized + ", notifyListeners="
				+ notifyListeners + ", versioned=" + versioned + "]";
	}

}
