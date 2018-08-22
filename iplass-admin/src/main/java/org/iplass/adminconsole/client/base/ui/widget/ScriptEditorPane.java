/*
 * Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import org.iplass.gwt.ace.client.EditorMode;
import org.iplass.gwt.ace.client.EditorTheme;
import org.iplass.gwt.ace.client.GwtAce;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ResizedEvent;
import com.smartgwt.client.widgets.events.ResizedHandler;

public class ScriptEditorPane extends Canvas {

	private static final String DEFAULT_EDITOR_WIDTH  = "100%";
	private static final String DEFAULT_EDITOR_HEIGHT = "300px";

	private GwtAce editor;

	public ScriptEditorPane() {
		setWidth100();
		setHeight100();
		setBorder("1px solid lightgray");	//枠がほしいので

		editor = new GwtAce();
		editor.setWidth(DEFAULT_EDITOR_WIDTH);		//Widthは100%でOK
		editor.setHeight(DEFAULT_EDITOR_HEIGHT);	//Heightは100%はNG。・・・・

		setMode(org.iplass.gwt.ace.client.EditorMode.JSP);
		setTheme(org.iplass.gwt.ace.client.EditorTheme.ECLIPSE);

		addChild(editor);

		addResizedHandler(new ResizedHandler() {

			@Override
			public void onResized(ResizedEvent event) {
				//Editorのサイズを合わせる
				redisplay();
			}
		});
	}

	public void setMode(final EditorMode mode) {
		editor.setMode(mode);
	}

	public void setTheme(final EditorTheme theme) {
		editor.setTheme(theme);
	}

	public void setText(final String text) {
		final String _text = text != null ? text : "";
		editor.setText(_text);
	}

	public String getText() {
		return editor.getText();
	}

	public void setReadOnly(final boolean readOnly) {
		editor.setReadOnly(readOnly);
	}

	public void redisplay() {
		//開始されていなくてもサイズは変更しておく
		fetchEditorSize();

		editor.redisplay();
	}

	private void fetchEditorSize() {
		editor.setHeight(getInnerContentHeight() + "px");
	}

}
