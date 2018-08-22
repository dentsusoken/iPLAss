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

package org.iplass.gwt.ace.client;

import java.util.Optional;
import java.util.logging.Logger;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.SimplePanel;

public class GwtAce extends Composite implements TakesValue<String>, HasText, RequiresResize /*, HasValueChangeHandlers<String>, HasEnabled, Focusable */ {

    private static final Logger logger = Logger.getLogger(GwtAce.class.getName());

	/** default mode */
	public static final EditorMode DEFAULT_MODE = EditorMode.TEXT;

	/** default mode */
	public static final EditorTheme DEFAULT_THEME = EditorTheme.ECLIPSE;

    private static final String ELEMENT_ID_PREFIX = "ace-editor-";

    private static int counter = 0;

    private InitialOptions initialOptions = new InitialOptions();

    private boolean loadedAce = false;
    private JavaScriptObject theAce;

	public GwtAce() {
		this(DEFAULT_MODE);
	}

	public GwtAce(EditorMode mode) {
		this(mode, DEFAULT_THEME);
	}

    public GwtAce(EditorMode mode, EditorTheme theme) {
		initialOptions.setMode(mode);
		initialOptions.setTheme(theme);
        initWidget(new SimplePanel());
    }

    @Override
    protected void onLoad() {
        super.onLoad();

        if (loadedAce) {
            return;
        }

        loadedAce = true;
        Element element = getElement();
        String id = ELEMENT_ID_PREFIX + ++counter;
        element.setId(id);
        element.addClassName("gwt_ace_editor_container");
        theAce = setup(this, id, initialOptions.toJavaScriptObject());

		logger.fine("completed setup ace editor.");
    }

    /**
     * If you need to manually resize, please execute {@link GwtAce#redisplay()}.
     */
	@Override
	public void onResize() {
		redisplay();
	}

	public void redisplay() {
		if (getAceElement().isPresent()){
			logger.fine("executed redisplay.");
			resize(theAce);
		} else {
			logger.fine("redisplay skipped. because not complete setup ace editor.");
		}
	}

	public void destroy() {
		if (getAceElement().isPresent()){
			logger.fine("executed destroy.");
			destroy(theAce);
		} else {
			logger.fine("destroy skipped. because not complete setup ace editor.");
		}
	};

	@Override
	public void setValue(String value) {
		if (getAceElement().isPresent()){
			setValue(theAce, value);
		} else {
			initialOptions.setValue(value);
		}
	}

	@Override
	public String getValue() {
		return getAceElement().map(element -> {
			return getValue(theAce);
		}).orElse(initialOptions.getValue());
	}

	@Override
	public String getText() {
		return getValue();
	}

	@Override
	public void setText(String text) {
		setValue(text);
	}

    public void setMode(EditorMode mode) {
		if (getAceElement().isPresent()){
			setMode(theAce, mode.getModeName());
		} else {
			initialOptions.setMode(mode);
		}
    }

    public void setTheme(EditorTheme theme) {
		if (getAceElement().isPresent()){
			setTheme(theAce, theme.getThemeName());
		} else {
			initialOptions.setTheme(theme);
		}
    }

    public void setReadOnly(boolean readOnly) {
		if (getAceElement().isPresent()){
			setReadOnly(theAce, readOnly);
		} else {
			initialOptions.setReadOnly(readOnly);
		}
    }

	public void setHScrollBarAlwaysVisible(boolean hScrollBarAlwaysVisible) {
		if (getAceElement().isPresent()){
			setHScrollBarAlwaysVisible(theAce, hScrollBarAlwaysVisible);
		} else {
			initialOptions.setHScrollBarAlwaysVisible(hScrollBarAlwaysVisible);
		}
	}

    private native JavaScriptObject setup(GwtAce myAce, String id, JavaScriptObject initialOptions)/*-{

	    var element = $doc.getElementById(id);

		// trigger extension
		$wnd.ace.require("ace/ext/language_tools");

		var theAce = $wnd.ace.edit(element);

		// enable autocompletion and snippets
		theAce.setOptions({
			enableBasicAutocompletion: true,
			enableSnippets: true,
			enableLiveAutocompletion: true
		});

		// This is to remove following warning message on console:
		// Automatically scrolling cursor into view after selection change this will be disabled in the next version
		// set editor.$blockScrolling = Infinity to disable this message
		theAce.$blockScrolling = Infinity;

		// setting initial options
		theAce.setTheme("ace/theme/" + initialOptions["theme"]);
		theAce.getSession().setMode("ace/mode/" + initialOptions["mode"]);
		theAce.getSession().setValue(initialOptions["value"]);
		theAce.setReadOnly(initialOptions["readOnly"]);
		theAce.renderer.setHScrollBarAlwaysVisible(initialOptions["hScrollBarAlwaysVisible"]);

	    return theAce;

	}-*/;

    private native void resize(JavaScriptObject theAce)/*-{
	    theAce.resize();
	}-*/;
	private native void destroy(JavaScriptObject theAce) /*-{
		theAce.destroy();
	}-*/;
    private native void setValue(JavaScriptObject theAce, String text)/*-{
    	theAce.getSession().setValue(text);
	}-*/;
    private native String getValue(JavaScriptObject theAce)/*-{
	    return theAce.getSession().getValue();
	}-*/;
    private native void setMode(JavaScriptObject theAce, String modeName)/*-{
		theAce.getSession().setMode("ace/mode/" + modeName);
	}-*/;
    private native void setTheme(JavaScriptObject theAce, String themeName)/*-{
		theAce.setTheme("ace/theme/" + themeName);
	}-*/;
    private native void setReadOnly(JavaScriptObject theAce, boolean readOnly)/*-{
		theAce.setReadOnly(readOnly);
	}-*/;
	private native void setHScrollBarAlwaysVisible(JavaScriptObject theAce, boolean hScrollBarAlwaysVisible) /*-{
		theAce.renderer.setHScrollBarAlwaysVisible(hScrollBarAlwaysVisible);
	}-*/;

    private Optional<com.google.gwt.dom.client.Element> getAceElement() {
        return Optional.ofNullable(getElement().getFirstChildElement());
    }

	private static class InitialOptions {

		private EditorMode mode = DEFAULT_MODE;

		private EditorTheme theme = DEFAULT_THEME;

		private String value = "";

		private boolean readOnly = false;

		private boolean hScrollBarAlwaysVisible = false;

		public EditorMode getMode() {
			return mode;
		}

		public void setMode(EditorMode mode) {
			this.mode = mode;
		}

		public EditorTheme getTheme() {
			return theme;
		}

		public void setTheme(EditorTheme theme) {
			this.theme = theme;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public void setReadOnly(boolean readOnly) {
			this.readOnly = readOnly;
		}

		public void setHScrollBarAlwaysVisible(boolean hScrollBarAlwaysVisible) {
			this.hScrollBarAlwaysVisible = hScrollBarAlwaysVisible;
		}

		public JavaScriptObject toJavaScriptObject() {
			JavaScriptObject result = JavaScriptObject.createObject();
			if (mode != null) {
				addProperty(result, "mode", mode.getModeName());
			}
			if (theme != null) {
				addProperty(result, "theme", theme.getThemeName());
			}
			addProperty(result, "value", value);
			addProperty(result, "readOnly", readOnly);
			addProperty(result, "hScrollBarAlwaysVisible", hScrollBarAlwaysVisible);
			return result;
		}

		private static native void addProperty(JavaScriptObject javaScriptObject, String property, String value)/*-{
			javaScriptObject[property] = value;
		}-*/;

		private static native void addProperty(JavaScriptObject javaScriptObject, String property, boolean value)/*-{
			javaScriptObject[property] = value;
		}-*/;
	}

}
