package org.iplass.adminconsole.client.tools.ui.entityexplorer.recycle;

import org.iplass.adminconsole.client.tools.ui.entityexplorer.EntityExplorerMainPane;

import com.smartgwt.client.widgets.layout.VLayout;

public class RecycleBinMainPane extends VLayout {

	private RecycleBinListPane recycleBinListPane;

	public RecycleBinMainPane(EntityExplorerMainPane owner) {
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
}
