/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.adminconsole.client.tools.data.openapisupport;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.data.AbstractAdminDataSource;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.shared.tools.dto.openapisupport.OpenApiSupportTreeGridEntry;
import org.iplass.adminconsole.shared.tools.rpc.openapisupport.OpenApiSupportRpcConstant;
import org.iplass.adminconsole.shared.tools.rpc.openapisupport.OpenApiSupportServiceAsync;
import org.iplass.adminconsole.shared.tools.rpc.openapisupport.OpenApiSupportServiceFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.widgets.tree.TreeNode;

/**
 * OpenAPI(Swagger)Support 用の TreeGrid DataSource
 * <p>
 * データソースは一度きりの利用で、再度データ取得する場合は新しいインスタンスを生成する必要がある。
 * </p>
 * @author SEKIGUCHI Naoya
 */
public class OpenApiSupportTreeGridDS extends AbstractAdminDataSource {

	/**
	 * TreeNode 属性名
	 */
	public static enum AttributeName {
		/** パス */
		PATH,
		/**
		 * TreeNode が属しているルートノード名
		 * @see org.iplass.adminconsole.shared.tools.rpc.openapisupport.OpenApiSupportRpcConstant.Service.RootNode
		 */
		ROOT_NODE,
		/** 当該ノードの定義名 */
		DEFINITION_NAME,
		/** フォルダであるか */
		IS_FOLDER
	}

	/** フェッチ処理完了後コールバック */
	private Runnable completeFetchCallback;

	/**
	 * インスタンスを生成します。
	 * @return 本インスタンス
	 */
	public static OpenApiSupportTreeGridDS getInstance() {
		return new OpenApiSupportTreeGridDS();
	}

	/**
	 * フェッチ処理完了後のコールバックを設定する
	 * <p>
	 * フェッチ処理を実行した後に、追加の処理を行いたい場合に設定する。
	 * 処理を実行したら本インスタンスの処理はクリアされます。
	 * </p>
	 * @param completeFetchCallback
	 */
	public void setCompleteFetchCallback(Runnable completeFetchCallback) {
		this.completeFetchCallback = completeFetchCallback;
	}

	@Override
	protected void executeFetch(final String requestId, final DSRequest request,
			final DSResponse response) {

		OpenApiSupportServiceAsync service = OpenApiSupportServiceFactory.get();
		service.getWebApiTreeGridEntry(TenantInfoHolder.getId(), new AsyncCallback<OpenApiSupportTreeGridEntry>() {

			@Override
			public void onSuccess(OpenApiSupportTreeGridEntry webApiEntry) {
				service.getEntityCrudApiTreeGridEntry(TenantInfoHolder.getId(), new AsyncCallback<OpenApiSupportTreeGridEntry>() {

					@Override
					public void onSuccess(OpenApiSupportTreeGridEntry entityCrudEntry) {
						TreeNode webApiRoot = createNode(OpenApiSupportRpcConstant.Service.RootNode.WEB_API, webApiEntry,
								OpenApiSupportRpcConstant.Service.RootNode.WEB_API);
						TreeNode entityCrudRoot = createNode(OpenApiSupportRpcConstant.Service.RootNode.ENTITY_CRUD_API, entityCrudEntry,
								OpenApiSupportRpcConstant.Service.RootNode.ENTITY_CRUD_API);

						response.setData(new Record[] { webApiRoot, entityCrudRoot });
						processResponse(requestId, response);

						if (completeFetchCallback != null) {
							completeFetchCallback.run();
							completeFetchCallback = null;
						}

					}

					@Override
					public void onFailure(Throwable caught) {
						completeFetchCallback = null;

						GWT.log("get entity crud api error!!!", caught);
						response.setStatus(RPCResponse.STATUS_FAILURE);
						processResponse(requestId, response);
					}
				});

			}

			@Override
			public void onFailure(Throwable caught) {
				completeFetchCallback = null;

				GWT.log("get webapi error!!!", caught);
				response.setStatus(RPCResponse.STATUS_FAILURE);
				processResponse(requestId, response);
			}
		});
	}

	/**
	 * グリッドエントリー情報を、TreeGrid のノードに展開する、
	 * @param nodeName 当該ノードの表示名
	 * @param gridEntry グリッドエントリー情報
	 * @param rootNode ルートノード名
	 * @return TreeNode インスタンス
	 */
	private TreeNode createNode(String nodeName, OpenApiSupportTreeGridEntry gridEntry, String rootNode) {
		TreeNode node = new TreeNode(nodeName);

		node.setAttribute(AttributeName.PATH.name(), gridEntry.getName());
		node.setAttribute(AttributeName.ROOT_NODE.name(), rootNode);
		node.setAttribute(AttributeName.DEFINITION_NAME.name(), gridEntry.getPath());
		node.setAttribute(AttributeName.IS_FOLDER.name(), gridEntry.isFolder());
		node.setIsFolder(gridEntry.isFolder());
		node.setChildren(createChildren(gridEntry.getChildren(), rootNode));

		return node;
	}

	/**
	 * TreeGrid の子ノードを生成します。
	 * @param gridChildren TreeGrid の子ノード情報
	 * @param rootNode ルートノード名
	 * @return TreeNode の配列
	 */
	private TreeNode[] createChildren(List<OpenApiSupportTreeGridEntry> gridChildren, String rootNode) {
		if (gridChildren == null || gridChildren.isEmpty()) {
			return new TreeNode[0];
		}

		// ソート
		gridChildren.sort((a, b) -> {
			if (a.isFolder() && !b.isFolder()) {
				// フォルダーは先頭に表示
				return -1;
			} else if (!a.isFolder() && b.isFolder()) {
				// フォルダーは先頭に表示
				return 1;
			}
			// 名前でソートする
			return a.getName().compareTo(b.getName());
		});

		List<TreeNode> children = new ArrayList<>();
		for (OpenApiSupportTreeGridEntry item : gridChildren) {
			children.add(createNode(item.getName(), item, rootNode));
		}

		return children.toArray(new TreeNode[children.size()]);
	}
}
