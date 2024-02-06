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

package org.iplass.adminconsole.client.metadata.ui;

import java.util.LinkedHashMap;

import org.iplass.adminconsole.client.base.event.AdminConsoleGlobalEventBus;
import org.iplass.adminconsole.client.base.event.ViewMetaDataStatusCheckResultEvent;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.HasDestroy;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataStatusCheckDialog;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataStatusCheckDialog.StatusCheckResultHandler;

import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.SectionStackSection;

public class MetaDataPluginSection extends SectionStackSection implements HasDestroy {

	private static final String REFRESH_ICON = "[SKINIMG]/actions/refresh.png";
	private static final String STATUS_ICON = "tick.png";

	private MetaDataPluginTreeGrid metaTreeGrid;

	public MetaDataPluginSection() {

		setTitle("MetaDataSettings");

		ImgButton clearCache = new ImgButton();
		clearCache.setSrc(REFRESH_ICON);
		clearCache.setSize(16);
		clearCache.setShowFocused(false);
		clearCache.setShowRollOver(false);
		clearCache.setShowDown(false);
		clearCache.setTooltip(AdminClientMessageUtil.getString("ui_MtpAdmin_refreshMetadataList"));
		clearCache.setHoverWrap(false);
		clearCache.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				metaTreeGrid.clearMetaDataCache();
			}
		});
		ImgButton checkStatus = new ImgButton();
		checkStatus.setSrc(STATUS_ICON);
		checkStatus.setSize(16);
		checkStatus.setShowFocused(false);
		checkStatus.setShowRollOver(false);
		checkStatus.setShowDown(false);
		checkStatus.setTooltip(AdminClientMessageUtil.getString("ui_MtpAdmin_checkMetadataStatusRegist"));
		checkStatus.setHoverWrap(false);
		checkStatus.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				statusCheck();
			}
		});
		setControls(checkStatus, clearCache);

		metaTreeGrid = MetaDataPluginTreeGrid.getInstance();
		metaTreeGrid.setWidth100();
		addItem(metaTreeGrid);
	}

	private void statusCheck() {

		MetaDataStatusCheckDialog dialog = new MetaDataStatusCheckDialog(null, new StatusCheckResultHandler() {
			@Override
			public void onError(LinkedHashMap<String, String> result) {
				ViewMetaDataStatusCheckResultEvent.fire(result, AdminConsoleGlobalEventBus.getEventBus());
			}
		});
		dialog.show();
	}

	@Override
	public void destroy() {
		metaTreeGrid.destroy();
	}

}
