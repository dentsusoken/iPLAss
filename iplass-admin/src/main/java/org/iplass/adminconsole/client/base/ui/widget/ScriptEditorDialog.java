/*
 * Copyright (C) 2018 DENTSU SOKEN INC. All Rights Reserved.
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Logger;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.gwt.ace.client.EditorTheme;
import org.iplass.gwt.ace.client.GwtAce;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.WidgetCanvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.events.ResizedEvent;
import com.smartgwt.client.widgets.events.ResizedHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;

public class ScriptEditorDialog extends AbstractWindow {

	private static final Logger logger = Logger.getLogger(ScriptEditorDialog.class.getName());

	private static final String THEME_COOKIE_NAME = "iplass.editor.theme";

	private SelectItem modeField;
	private SelectItem themeField;
	private TextItem maxHeightField;

	private EditorPane editorPane;
	private HintPane hintPane;

	private IButton ok;
	private IButton cancel;

	/** 表示されているエディター画面の設定 */
	private final ScriptEditorDialogSetting dialogSetting = new ScriptEditorDialogSetting();

	private boolean test = false;

	public ScriptEditorDialog(final ScriptEditorDialogCondition condition, final ScriptEditorDialogHandler handler) {
		if (!SmartGWTUtil.isEmpty(condition.getPropertyKey())) {
			setTitle(condition.getPropertyKey());
		} else {
			setTitle("ScriptEditor");
		}

		setWidth("1100px");
		setHeight("740px");

		setMargin(5);

		setIsModal(true);
		setShowModalMask(true);
		setShowMinimizeButton(false);
		setShowMaximizeButton(true);
		setCanDragResize(true);
		setCanDragReposition(true);
		centerInPage();

		addDrawHandler(new DrawHandler() {

			@Override
			public void onDraw(DrawEvent event) {
				logger.fine("ScriptEditorDialog onDraw.");

				//初期表示時に高さがズレるため調整
				adjustHeight();
			}
		});
		addResizedHandler(new ResizedHandler() {

			@Override
			public void onResized(ResizedEvent event) {
				logger.fine("ScriptEditorDialog onResized.");

				//DragでのResizeのためEditorの再表示
				editorPane.redisplay();

				adjustHeight();
			}
		});

		HLayout header = new HLayout();
		header.setWidth100();
		header.setAutoHeight();
		header.setMargin(5);

		DynamicForm optionForm = new DynamicForm();
		optionForm.setHeight(30);
		optionForm.setWidth100();
		optionForm.setNumCols(8);
		optionForm.setColWidths(70, 130, 70, 130, 90, 130, 30, "*");
		header.addMember(optionForm);

		LinkedHashMap<String, String> modeMap = new LinkedHashMap<>();
		for (ScriptEditorDialogMode mode : ScriptEditorDialogMode.values()) {
			modeMap.put(mode.name(), mode.getText());
		}
		modeField = new SelectItem();
		modeField.setTitle("Highlight");
		modeField.setWidth(130);
		modeField.setValueMap(modeMap);
		modeField.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				ScriptEditorDialogMode mode = ScriptEditorDialogMode.valueOf(SmartGWTUtil.getStringValue(modeField));
				editorPane.setMode(mode);
				dialogSetting.setMode(mode);
			}
		});

		LinkedHashMap<String, String> themeMap = new LinkedHashMap<>();
		for (EditorTheme theme : EditorTheme.values()) {
			themeMap.put(theme.name(), theme.getText());
		}
		themeField = new SelectItem();
		themeField.setTitle("Theme");
		themeField.setWidth(130);
		themeField.setValueMap(themeMap);
		themeField.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				EditorTheme theme = EditorTheme.valueOf(SmartGWTUtil.getStringValue(themeField));
				editorPane.setTheme(theme);
				dialogSetting.setTheme(theme);

				//save cookie
				SmartGWTUtil.setCookie(THEME_COOKIE_NAME, theme.name());
			}
		});

		maxHeightField = new TextItem();
		maxHeightField.setTitle("MaxHeight");
		maxHeightField.setWidth(130);
		if (condition.getMaxHeight() != null && condition.getMaxHeight() > 0) {
			maxHeightField.setValue(condition.getMaxHeight()
					.toString());
		}
		SmartGWTUtil.addHoverToFormItem(maxHeightField,
				AdminClientMessageUtil.getString("ui_metadata_top_item_TopViewContentParts_maxHeightDescriptionKey"));
		maxHeightField.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				Integer value = SmartGWTUtil.getIntegerValue(maxHeightField);
				dialogSetting.setMaxHeight(value);

				condition.setMaxHeight(value);
			}
		});

		StaticTextItem tipsField = new StaticTextItem();
		tipsField.setShowTitle(false);
		tipsField.setValue("[Ctrl]+[Space] shows snippets.");

		optionForm.setItems(modeField, themeField, maxHeightField, new SpacerItem(), tipsField);

		modeField.setValue(condition.getInitEditorMode().name());
		if (condition.getInitEditorTheme() == null) {
			EditorTheme theme = EditorTheme.ECLIPSE;

			//check cookie
			String cookieThemeName = SmartGWTUtil.getCookie(THEME_COOKIE_NAME);
			if (!SmartGWTUtil.isEmpty(cookieThemeName)) {
				try {
					EditorTheme cookieTheme = EditorTheme.valueOf(cookieThemeName);
					if (cookieTheme != null) {
						theme = cookieTheme;
					}
				} catch (Exception e) {
					//昔のもので、存在しない場合を考慮
				}
			}
			condition.setInitEditorTheme(theme);
		}
		themeField.setValue(condition.getInitEditorTheme().name());
		
		Integer height = condition.getMaxHeight();
		if (height != null) {
			maxHeightField.setValue(height);
			condition.setMaxHeight(height);
		}


		HLayout mainPane = new HLayout();
		mainPane.setMargin(10);
		mainPane.setWidth100();
		mainPane.setHeight100();

		editorPane = new EditorPane(condition);
		editorPane.setShowResizeBar(true);	//リサイズ可能
		editorPane.setResizeBarTarget("next");	//リサイズバーをダブルクリックした際、下を収縮

		hintPane = new HintPane(condition);

		mainPane.addMember(editorPane);
		mainPane.addMember(hintPane);

		HLayout footer = new HLayout(5);
		footer.setMargin(10);
		footer.setWidth100();
		footer.setAutoHeight();
		footer.setHeight(30);
		footer.setAlign(Alignment.CENTER);

		ok = new IButton("OK");
		ok.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// NOTE onSaveDialogSetting は後から追加した機能なので onSave メソッドと分けているが、本来は同時が望ましい。
				handler.onSaveDialogSetting(dialogSetting);
				handler.onSave(getText());
				destroy();
			}
		});

		cancel = new IButton("Cancel");
		cancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				handler.onCancel();
				destroy();
			}
		});

		//配置
		footer.addMember(ok);
		footer.addMember(cancel);

		addItem(header);
		addItem(mainPane);
		addItem(SmartGWTUtil.separator());
		addItem(footer);

		// 初期設定を保持
		dialogSetting.setMode(condition.getInitEditorMode());
		dialogSetting.setTheme(condition.getInitEditorTheme());
	}

	/**
	 * 編集されたテキストを取得します。
	 * @return 編集されたテキスト
	 */
	public String getText() {
		return editorPane.getText();
	}

	/**
	 * テキストを設定します。
	 * @param text テキスト
	 */
	public void setText(String text) {
		editorPane.setText(text);
	}

	private void adjustHeight() {

		if (!test) {

			Timer check = new Timer() {

				@Override
				public void run() {
					if (editorPane.getVisibleHeight() != (hintPane.getVisibleHeight() + 0)) {
						logger.fine("(editor, hint)=(" + editorPane.getVisibleHeight() + "," + (hintPane.getVisibleHeight() + 0) + ")");
						//editorPane.setHeight(hintPane.getHeight() + 1);
						Timer timer = new Timer() {

							@Override
							public void run() {
								final int height = getHeight();
								setHeight(height + 5);

								Timer timer = new Timer() {

									@Override
									public void run() {
										setHeight(height);
										test = false;
									}
								};
								timer.schedule(10);
							}
						};
						timer.schedule(10);
					} else {
						logger.fine("normal status	 (editor, hint)=(" + editorPane.getInnerContentHeight() + "," + (hintPane.getInnerContentHeight() + 1) + ")"
								+ ", (editor, hint)=(" + editorPane.getVisibleHeight() + "," + (hintPane.getVisibleHeight() + 1) + ")");
						test = false;
					}
				}
			};
			test = true;
			check.schedule(10);
		}
	}

	private static class HintPane extends SectionStack {

		public HintPane(ScriptEditorDialogCondition condition) {

			setWidth(250);
			setVisibilityMode(VisibilityMode.MUTEX);

			SectionStackSection hintSection = new SectionStackSection("Hint");
			VLayout hintPane = new VLayout();
			hintPane.setHeight100();
			hintPane.setWidth100();

			Canvas hintContents = new Canvas();
			hintContents.setHeight100();
			hintContents.setWidth100();
			hintContents.setPadding(5);
			hintContents.setOverflow(Overflow.AUTO);
			hintContents.setCanSelectText(true);

			StringBuilder contents = new StringBuilder();
			contents.append("<h3>Notes</h3>");
			if (!SmartGWTUtil.isEmpty(condition.getHintMessage())) {
				contents.append("<div>").append(condition.getHintMessage()).append("</div>");
			} else if (!SmartGWTUtil.isEmpty(condition.getHintKey())) {
				String hint = AdminClientMessageUtil.getString(condition.getHintKey());
				if (!SmartGWTUtil.isEmpty(hint)) {
					contents.append("<div>").append(hint).append("</div>");
				}
			}
			hintContents.setContents(contents.toString());

			hintPane.addMember(hintContents);
			hintSection.addItem(hintPane);
			hintSection.setExpanded(true);

			addSection(hintSection);
		}

	}

	private static class EditorPane extends VLayout {

		private WrappedEditor editor;

		public EditorPane(ScriptEditorDialogCondition condition) {

			setWidth100();
			setHeight100();

			editor = new WrappedEditor();

			//初期設定
			editor.setMode(condition.getInitEditorMode());
			EditorTheme theme = EditorTheme.ECLIPSE;
			if (condition.getInitEditorTheme() != null) {
				theme = condition.getInitEditorTheme();
			}
			editor.setTheme(theme);
			editor.setReadOnly(condition.isReadOnly());
			editor.setText(condition.getValue());

			addMember(editor);
		}

		/**
		 * 編集するテキストを設定します。
		 * @param text 編集するテキスト
		 */
		public void setText(String text) {
			editor.setText(text);
		}

		/**
		 * 編集されたテキストを取得します。
		 * @return 編集されたテキスト
		 */
		public String getText() {
			return editor.getText();
		}

		/**
		 * 編集モードを設定します。
		 * @param mode 編集モード
		 */
		public void setMode(final ScriptEditorDialogMode mode) {
			editor.setMode(mode);
		}

		/**
		 * テーマを設定します。
		 * @param theme テーマ
		 */
		public void setTheme(final EditorTheme theme) {
			editor.setTheme(theme);
		}

		public void redisplay() {
			editor.fitWrapHeight();
		}

		private static class WrappedEditor extends Canvas {

			//Widthは100%でOK
			private static final String DEFAULT_EDITOR_WIDTH  = "100%";
			private static final String DEFAULT_EDITOR_HEIGHT = "100%";

			private GwtAce aceEditor;

			private boolean isStarted = false;

			private int wrapHeight = 0;
			private WidgetCanvas wrap;
			private List<HandlerRegistration> wrapHandlers = new ArrayList<>();

			public WrappedEditor() {
				setWidth100();
				setHeight100();
				setBorder("1px solid lightgray");	//枠がほしいので

				aceEditor = new GwtAce();
				aceEditor.setWidth(DEFAULT_EDITOR_WIDTH);
				aceEditor.setHeight(DEFAULT_EDITOR_HEIGHT);

				aceEditor.addAttachHandler(new AttachEvent.Handler() {

					@Override
					public void onAttachOrDetach(AttachEvent event) {
						if (event.isAttached()) {
							GWT.log("wrapper[" + wrap.getID() + "] onAttach called.");
							isStarted = true;
						}
					}
				});

				setMode(ScriptEditorDialogMode.JSP);
				setTheme(EditorTheme.ECLIPSE);

				createWrapper();
			}

			public void setMode(final ScriptEditorDialogMode mode) {
				aceEditor.setMode(mode.getAceMode());
			}

			public void setTheme(final EditorTheme theme) {
				aceEditor.setTheme(theme);
			}

			public void setText(String text) {
				String _text = text != null ? text : "";
				aceEditor.setText(_text);
			}

			public String getText() {
				return aceEditor.getText();
			}

			/**
			 * 読み取り専用にするかを設定します。
			 * @param readOnly 読み取り専用にするか
			 */
			public void setReadOnly(final boolean readOnly) {
				aceEditor.setReadOnly(readOnly);
			}

			/**
			 * Dragによるサイズ変更時に、高さを大幅に小さくすると、
			 * WrapperのVisibleHeightとInnerContentHeightが異なる状態になってスクロールが表示されることがあるので、
			 * 再度Wrapperを作り直して回避する。
			 */
			public void fitWrapHeight() {

				if (wrap.getVisibleHeight() != wrap.getInnerContentHeight()) {
					GWT.log("Illegal state of wrapper[" + wrap.getID() + "]  height :(" + wrap.getInnerContentHeight() + "," + wrap.getVisibleHeight() + ")");
				}
				removeWrapper();
				createWrapper();

				GWT.log("rebuilded wrapper[" + wrap.getID() + "]");
			}

			private void createWrapper() {

				wrap = new WidgetCanvas(aceEditor);
				wrap.setStyleName("editor-wrap");

				wrapHandlers.add(wrap.addResizedHandler(new ResizedHandler() {

					@Override
					public void onResized(ResizedEvent event) {
						//Wrapperの高さに合わせてEditorの高さを設定する
						if (wrapHeight == wrap.getInnerContentHeight()) {
							GWT.log("wrapper[" + wrap.getID() + "] height is same. so skip editor redisplay. :(" + wrapHeight + ")");
							if (isStarted) {
								aceEditor.redisplay();
							}
						} else {
							if (isStarted) {
								fitEditorHeight();
							}
							wrapHeight = wrap.getInnerContentHeight();
						}
					}
				}));
				wrapHeight = 0;

				GWT.log("■■■ create wrapper[" + wrap.getID() + "]");

				addChild(wrap);
			}

			private void removeWrapper() {

				if (!wrapHandlers.isEmpty()) {
					for (HandlerRegistration handler : wrapHandlers) {
						handler.removeHandler();
					}
					wrapHandlers.clear();
				}

				wrap.removeFromParent();
				wrap.destroy();
				wrap = null;
			}

			private void fitEditorHeight() {

				//widthは100%なので自動で調整されるが、heightは値を設定する必要がある
				int before = aceEditor.getOffsetHeight();
				aceEditor.setHeight(WrappedEditor.this.getInnerContentHeight() + "px");
				aceEditor.redisplay();
				GWT.log("changed editor height :(" + WrappedEditor.this.getInnerContentHeight() + "),result:(" + before + "->" + aceEditor.getOffsetHeight() + ")");
			}

		}

	}

}
