/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.data.entity;

import java.util.HashMap;

import org.iplass.adminconsole.client.base.data.AbstractAdminDataSource;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.widgets.tree.TreeNode;

public class PropertyTreeDS extends AbstractAdminDataSource {

	private static HashMap<String, PropertyTreeDS> dsList = null;

	private MetaDataServiceAsync service = MetaDataServiceFactory.get();

	private boolean showRoot;

	public static PropertyTreeDS create(String defName, boolean showRoot) {
		//同じEntityのDSを作らない
		if (dsList == null) {
			dsList = new HashMap<String, PropertyTreeDS>();
		}
		if (dsList.containsKey(defName + "_" + showRoot)) {
			return dsList.get(defName + "_" + showRoot);
		}

		PropertyTreeDS ds = new PropertyTreeDS("PropertyTreeListDS", defName, showRoot);
		dsList.put(defName + "_" + showRoot, ds);
		return ds;
	}

	private PropertyTreeDS(String id, String defName, boolean showRoot) {
		setID(convertID(id + defName + "_" + showRoot));//IDが同じだとエラーになる
		setAttribute("defName", defName, false);
		this.showRoot = showRoot;

		DataSourceField nameField = new DataSourceField("name", FieldType.TEXT, "name");
		DataSourceField displayNameField = new DataSourceField("displayName", FieldType.TEXT, "displayName");
		DataSourceField propertyDefinitionField = new DataSourceField("propertyDefinition", FieldType.TEXT, "propertyDefinition");
		DataSourceField outputDisplayNameField = new DataSourceField("outputDisplayName", FieldType.TEXT, "outputDisplayName");

		setFields(nameField, displayNameField, propertyDefinitionField, outputDisplayNameField);
	}

	private String convertID(String id) {
		//IDに「.」が含まれるとエラーになるため変換（Entityの階層は「.」のため）
		return id.replace(".", "_");
	}


	@Override
	protected void executeFetch(final String requestId, DSRequest request, final DSResponse response) {

		String defName = getAttribute("defName");

		service.getEntityDefinition(TenantInfoHolder.getId(), defName, new AsyncCallback<EntityDefinition>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("error!!!", caught);

				response.setStatus(RPCResponse.STATUS_FAILURE);
				processResponse(requestId, response);
			}

			@Override
			public void onSuccess(EntityDefinition result) {
				if (showRoot) {
					TreeNode[] list = new TreeNode[1];

					TreeNode record = new TreeNode();
					record.setAttribute("name", result.getName());
					record.setAttribute("displayName", result.getDisplayName());
					record.setAttribute("entityDefinition", result);
					record.setAttribute("outputDisplayName", result.getName() + "(" + result.getDisplayName() + ")");
					record.setChildren(getNodeList(result));
					list[0] = record;

					response.setData(list);

					response.setTotalRows(list.length);
					processResponse(requestId, response);
				} else {
					TreeNode[] list = getNodeList(result);
					response.setData(list);

					response.setTotalRows(list.length);
					processResponse(requestId, response);
				}
			}

			private TreeNode[] getNodeList(EntityDefinition result) {
				int size = result.getPropertyList().size();

				// Create list for return - it is just requested records
				TreeNode[] list = new TreeNode[size];
				if (size > 0) {
					for (int i = 0; i < size; i++) {
						PropertyDefinition pd = result.getPropertyList().get(i);

						TreeNode record = new TreeNode();
						copyValues(pd, record);
						list[i] = record;
					}
				}
				return list;
			}

			private void copyValues(PropertyDefinition pd, TreeNode node) {
				node.setAttribute("name", pd.getName());
				node.setAttribute("displayName", pd.getDisplayName());
				node.setAttribute("localizedDisplayNameList", pd.getLocalizedDisplayNameList());
				node.setAttribute("propertyDefinition", pd);
				node.setAttribute("outputDisplayName", pd.getName() + "(" + pd.getDisplayName() + ")");
				if (pd instanceof ReferenceProperty) {
					node.setIsFolder(true);
				} else {
					node.setIsFolder(false);
				}
			}

		});
	}

}
