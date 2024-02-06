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

package org.iplass.adminconsole.client.base.ui.widget;

import java.util.ArrayList;
import java.util.List;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

public class MessageTabSet extends TabSet {

	private static final String PROGRESS_ICON = "[SKINIMG]/shared/progressCursorTracker.gif";

	private String title = "Log";

	private Tab messageTab;
	private MessagePane messagePane;

	public MessageTabSet() {
		setTabBarPosition(Side.TOP);
		setWidth100();
		setHeight100();

		messageTab = new Tab();
		messageTab.setTitle(title);

		messagePane = new MessagePane();
		messageTab.setPane(messagePane);
		addTab(messageTab);
	}

	public void setTabTitle(String title) {
		this.title = title;
		setTabTitleNormal();
	}
	public void setTabTitleProgress() {
		messageTab.setTitle("<span>" + Canvas.imgHTML(PROGRESS_ICON) + "&nbsp;Execute...</span>");
	}
	public void setTabTitleNormal() {
		messageTab.setTitle(title);
	}
	public void clearMessage() {
		messagePane.clearMessage();
	}

	public void setMessage(String message) {
		messagePane.setMessage(message);
	}
	public void setMessage(List<String> messages) {
		messagePane.setMessage(messages);
	}
	public void addMessage(String message) {
		messagePane.addMessage(message);
	}
	public void addMessage(List<String> messages) {
		messagePane.addMessage(messages);
	}
	public void setErrorMessage(String message) {
		messagePane.setErrorMessage(message);
	}
	public void setErrorMessage(List<String> messages) {
		messagePane.setErrorMessage(messages);
	}
	public void addErrorMessage(String message) {
		messagePane.addErrorMessage(message);
	}
	public void addErrorMessage(List<String> messages) {
		messagePane.addErrorMessage(messages);
	}
	public void setWarnMessage(String message) {
		messagePane.setWarnMessage(message);
	}
	public void setWarnMessage(List<String> messages) {
		messagePane.setWarnMessage(messages);
	}
	public void addWarnMessage(String message) {
		messagePane.addWarnMessage(message);
	}
	public void addWarnMessage(List<String> messages) {
		messagePane.addWarnMessage(messages);
	}

	private class MessagePane extends VLayout {

		Canvas logContents;

		public MessagePane() {
			setWidth100();
			setHeight100();

			logContents = new Canvas();
			logContents.setHeight100();
			logContents.setWidth100();
			logContents.setPadding(5);
			logContents.setOverflow(Overflow.AUTO);
			logContents.setCanSelectText(true);

			addMember(logContents);
		}

		public void clearMessage() {
			logContents.setContents("");
		}

		public void setMessage(String message) {
			List<String> messages = new ArrayList<String>();
			messages.add(message);
			setMessage(messages);
		}
		public void setMessage(List<String> messages) {
			logContents.setContents(getMessageString(messages));
		}

		public void addMessage(String message) {
			List<String> messages = new ArrayList<String>();
			messages.add(message);
			addMessage(messages);
		}

		public void addMessage(List<String> messages) {
			StringBuilder builder = new StringBuilder(logContents.getContents());
			if (builder.length() > 0) {
				builder.append("<br/>");
			}
			builder.append(getMessageString(messages));
			logContents.setContents(builder.toString());
		}

		public void setErrorMessage(String message) {
			List<String> messages = new ArrayList<String>();
			messages.add(message);
			setErrorMessage(messages);
		}
		public void setErrorMessage(List<String> messages) {
			logContents.setContents("<font color=\"red\">" + getMessageString(messages) + "</font>");
		}

		public void addErrorMessage(String message) {
			List<String> messages = new ArrayList<String>();
			messages.add(message);
			addErrorMessage(messages);
		}

		public void addErrorMessage(List<String> messages) {
			StringBuilder builder = new StringBuilder(logContents.getContents());
			if (builder.length() > 0) {
				builder.append("<br/>");
			}
			builder.append("<font color=\"red\">" + getMessageString(messages) + "</font>");
			logContents.setContents(builder.toString());
		}

		public void setWarnMessage(String message) {
			List<String> messages = new ArrayList<String>();
			messages.add(message);
			setWarnMessage(messages);
		}
		public void setWarnMessage(List<String> messages) {
			logContents.setContents("<font color=\"blue\">" + getMessageString(messages) + "</font>");
		}

		public void addWarnMessage(String message) {
			List<String> messages = new ArrayList<String>();
			messages.add(message);
			addWarnMessage(messages);
		}

		public void addWarnMessage(List<String> messages) {
			StringBuilder builder = new StringBuilder(logContents.getContents());
			if (builder.length() > 0) {
				builder.append("<br/>");
			}
			builder.append("<font color=\"blue\">" + getMessageString(messages) + "</font>");
			logContents.setContents(builder.toString());
		}

		private String getMessageString(List<String> messages) {
			StringBuilder builder = new StringBuilder();
			for (String message : messages) {
				builder.append(message + "<br/>");
			}
			if (builder.length() > 0) {
				builder.delete(builder.length() - 5, builder.length());
			}
			return builder.toString();

		}
	}

}
