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

package org.iplass.adminconsole.client.metadata.data.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.iplass.adminconsole.client.base.data.AbstractAdminDataSource;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.message.MessageCategory;
import org.iplass.mtp.message.MessageItem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.widgets.grid.ListGridRecord;


/**
 * メッセージアイテム データソース
 */
public class MessageItemDS extends AbstractAdminDataSource {

	private MetaDataServiceAsync service = MetaDataServiceFactory.get();

	private static final DataSourceField[] fields;

	static {

		DataSourceTextField key = new DataSourceTextField("key", "KEY");
		key.setPrimaryKey(true);
		DataSourceTextField id = new DataSourceTextField("id", "ID");
		DataSourceTextField message = new DataSourceTextField("message", AdminClientMessageUtil.getString("datasource_message_MessageItemDS_message"));
		DataSourceTextField shortMessage = new DataSourceTextField("shortMessage", AdminClientMessageUtil.getString("datasource_message_MessageItemDS_shortMessage"));

		fields = new DataSourceField[] {key, id, message, shortMessage};
	}

	/** MenuItemクラスごとのDS */
	private static HashMap<String, MessageItemDS> dsList = null;

    /**
     * MenuItemDSのインスタンスを返します。
     *
     * @return MenuItemDS
     */
    public static MessageItemDS getInstance(String category) {

		if (dsList == null) {
			dsList = new HashMap<String, MessageItemDS>();
		}
		if (dsList.containsKey(category)) {
			return dsList.get(category);
		}

		MessageItemDS ds = new MessageItemDS("MessageItemDS", category);
		dsList.put(category, ds);
		return ds;
    }

	/**
	 * コンストラクタ
	 *
	 * @param id ID
	 * @param category メッセージカテゴリ
	 */
    private MessageItemDS(String id, String category) {

    	String categoryId = category.replace("/", "_").replace(".", "_");

    	setID(id + categoryId);
    	setAttribute("category", category, true);

		setFields(fields);
    }

	@Override
	protected void executeFetch(final String requestId, final DSRequest request,
			final DSResponse response) {

		final String category = getAttribute("category");

		service.getDefinition(TenantInfoHolder.getId(), MessageCategory.class.getName(), category, new AsyncCallback<MessageCategory>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("error!!!", caught);

				response.setStatus(RPCResponse.STATUS_FAILURE);
				processResponse(requestId, response);
			}

			@Override
			public void onSuccess(MessageCategory msgCategory) {

				List<ListGridRecord> records = null;
				if (msgCategory == null) {
					records = new ArrayList<ListGridRecord>(0);
				} else {
					Map<String, MessageItem> items = msgCategory.getMessageItems();
					if (items == null) {
						records = new ArrayList<ListGridRecord>(0);
					} else {
						records = new ArrayList<ListGridRecord>(items.size());

						for (Iterator<Map.Entry<String, MessageItem>> it = items.entrySet().iterator(); it.hasNext();) {
							Map.Entry<String, MessageItem> entry = it.next();
							MessageItem item = entry.getValue();
							ListGridRecord record = new ListGridRecord();
							record.setAttribute("key", entry.getKey());
							record.setAttribute("id", item.getMessageId());
							record.setAttribute("message", item.getMessage());
							if (item.getMessage() != null && item.getMessage().length() > 30) {
								record.setAttribute("shortMessage", item.getMessage().substring(0, 30) + "...");
							} else {
								record.setAttribute("shortMessage", item.getMessage());
							}
							record.setAttribute("valueObject", item);
							records.add(record);
						}
					}
				}

		    	response.setData(records.toArray(new ListGridRecord[]{}));
				response.setTotalRows(records.size());
				processResponse(requestId, response);
			}
		});

	}

	@Override
	protected void executeAdd(String requestId, DSRequest request, DSResponse response) {
		ListGridRecord record = new ListGridRecord(request.getData());
		response.setData(new ListGridRecord[] {record});
		processResponse(requestId, response);
	}

	@Override
	protected void executeUpdate(String requestId, DSRequest request, DSResponse response) {
		ListGridRecord record = new ListGridRecord(request.getData());
		response.setData(new ListGridRecord[] {record});
		processResponse(requestId, response);
	}

	@Override
	protected void executeRemove(String requestId, DSRequest request, DSResponse response) {
		ListGridRecord record = new ListGridRecord(request.getData());
		response.setData(new ListGridRecord[] {record});
		processResponse(requestId, response);
	}

}
