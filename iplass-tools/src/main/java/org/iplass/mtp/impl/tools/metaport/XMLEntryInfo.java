/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.tools.metaport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.impl.metadata.MetaDataEntry;

/**
 * インポート対象のXMLファイルに含まれるMetaDataEntry情報
 */
public class XMLEntryInfo implements Serializable {

	private static final long serialVersionUID = -430733582715056101L;

	//pathをkeyにしたEntryのMap
	private Map<String, MetaDataEntry> pathEntryMap = new HashMap<>();

	//idをkeyにしたEntryのMap
	private Map<String, MetaDataEntry> idEntryMap = new HashMap<>();

	//idが未指定のEntryList
	private List<MetaDataEntry> idBlankEntryList = new ArrayList<>();

	public XMLEntryInfo() {
	}

	public Map<String, MetaDataEntry> getPathEntryMap() {
		return pathEntryMap;
	}

	public void setPathEntryMap(Map<String, MetaDataEntry> pathEntryMap) {
		this.pathEntryMap = pathEntryMap;
	}

	public void putPathEntry(String path, MetaDataEntry entry) {
		pathEntryMap.put(path, entry);
	}

	public boolean containsPathEntry(String path) {
		return pathEntryMap.containsKey(path);
	}

	public MetaDataEntry getPathEntry(String path) {
		return pathEntryMap.get(path);
	}

	public Map<String, MetaDataEntry> getIdEntryMap() {
		return idEntryMap;
	}

	public void setIdEntryMap(Map<String, MetaDataEntry> idEntryMap) {
		this.idEntryMap = idEntryMap;
	}

	public void putIdEntry(String id, MetaDataEntry entry) {
		idEntryMap.put(id, entry);
	}

	public boolean containsIdEntry(String id) {
		return idEntryMap.containsKey(id);
	}

	public MetaDataEntry getIdEntry(String id) {
		return idEntryMap.get(id);
	}

	public List<MetaDataEntry> getIdBlankEntryList() {
		return idBlankEntryList;
	}

	public void setIdBlankEntryList(List<MetaDataEntry> idBlankEntryList) {
		this.idBlankEntryList = idBlankEntryList;
	}

	public void addIdBlankEntry(MetaDataEntry idBlankEntry) {
		idBlankEntryList.add(idBlankEntry);
	}

}
