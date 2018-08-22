/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.base.event;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;

/**
 * AdminConsole全体で管理するイベントハンドラ。
 */
public class AdminConsoleGlobalEventBus {

	/** 全体で管理するイベントハンドラの実装クラス */
	private static EventBus EVENT_BUS = GWT.create(SimpleEventBus.class);

	private AdminConsoleGlobalEventBus() {}

	public static <H extends EventHandler> HandlerRegistration addHandler(GwtEvent.Type<H> type, H handler) {
		return EVENT_BUS.addHandler(type, handler);
	}

	public static <H extends EventHandler> HandlerRegistration addHandlerToSource(GwtEvent.Type<H> type, Object source, H handler) {
		return EVENT_BUS.addHandlerToSource(type, source, handler);
	}

	public static EventBus getEventBus() {
		return EVENT_BUS;
	}

}
