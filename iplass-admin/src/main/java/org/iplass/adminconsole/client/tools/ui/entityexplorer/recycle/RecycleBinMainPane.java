package org.iplass.adminconsole.client.tools.ui.entityexplorer.recycle;

import java.sql.Timestamp;

import org.iplass.adminconsole.client.tools.ui.entityexplorer.EntityExplorerMainPane;

import com.smartgwt.client.widgets.layout.VLayout;

public class RecycleBinMainPane extends VLayout {

	private EntityExplorerMainPane owner;
	private RecycleBinListPane recycleBinListPane;
	private RecycleBinDataListPane recycleBinDataListPane;

	public RecycleBinMainPane(EntityExplorerMainPane owner) {
		this.owner = owner;

		// レイアウト設定
		setWidth100();

		// 初期状態では一覧を生成せずに、Tabが選択されたタイミングで生成する
	}

	public void selectedPane() {

		if (recycleBinListPane == null) {
			// 初期表示にゴミ箱一覧を生成
			recycleBinListPane = new RecycleBinListPane(this);
			addMember(recycleBinListPane);
		}
	}

	public void showDataListPane(String entityName, Timestamp purgeTargetDate) {
		if (recycleBinDataListPane != null) {
			recycleBinDataListPane.hide();
			recycleBinDataListPane.destroy();
		}

		recycleBinDataListPane = new RecycleBinDataListPane(this, entityName, purgeTargetDate);
		addMember(recycleBinDataListPane);
		recycleBinDataListPane.show();
		recycleBinListPane.hide();
		owner.setWorkspaceTabName(entityName);
	}

	public void backRecycleBinListPane() {
		recycleBinListPane.show();
		if (recycleBinDataListPane != null) {
			recycleBinDataListPane.hide();
			recycleBinDataListPane.destroy();
			recycleBinDataListPane = null;
		}
		owner.setWorkspaceTabName(null);
	}
}
