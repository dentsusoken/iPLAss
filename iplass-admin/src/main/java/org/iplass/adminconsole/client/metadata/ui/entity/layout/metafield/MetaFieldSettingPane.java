/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.entity.layout.metafield;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.rpc.AdminAsyncCallback;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataLangTextItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.metadata.dto.Name;
import org.iplass.adminconsole.shared.metadata.dto.refrect.AnalysisListDataResult;
import org.iplass.adminconsole.shared.metadata.dto.refrect.AnalysisResult;
import org.iplass.adminconsole.shared.metadata.dto.refrect.FieldInfo;
import org.iplass.adminconsole.shared.metadata.dto.refrect.RefrectableInfo;
import org.iplass.adminconsole.shared.metadata.rpc.refrect.RefrectionServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.refrect.RefrectionServiceFactory;
import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.Refrectable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DragStopEvent;
import com.smartgwt.client.widgets.events.DragStopHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.validator.IntegerRangeValidator;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * インターフェースクラス(org.iplass.mtpパッケージ配下)の各フィールドに値を設定するためのレイアウト。
 * {@link org.iplass.adminconsole.view.annotation.MetaFieldInfo}が設定されたフィールドのみが対象。
 *
 * 現状、EntityViewでのみ使用しているが、commonにあるので今後他のところでも利用することを想定。
 * EntityViewについては、{@link org.iplass.adminconsole.client.metadata.ui.entity.layout.item.EntityViewFieldSettingPane}として継承して利用。
 *
 */
public class MetaFieldSettingPane extends VLayout {

	protected static final String RECORD_ATTRIBUTE_TYPE = "_type";
	protected static final String RECORD_ATTRIBUTE_VALUE = "_value";
	protected static final String FIELD_ATTRIBUTE_NAME = "name";
	protected static final String FIELD_ATTRIBUTE_CLASSTYPE = "classType";
	protected static final String DEFAULT_ACTION_NAME = "#default";
	protected static final String DEFAULT_WEBAPI_NAME = "#default";

	/** Definitionの各フィールドの値を保持するマップ */
	private HashMap<String, Serializable> defValueMap = new HashMap<String, Serializable>();

	/** OKボタン押下時のイベント */
	private MetaFieldUpdateHandler okHandler = null;

	/** Cancelボタン押下時のイベント */
	private MetaFieldUpdateHandler cancelHandler = null;

	/** リフレクションを使ってインターフェースクラスを操作するためのサービス */
	private RefrectionServiceAsync service = null;

	private final MetaFieldSettingDialog owner;
	private final String className;
	private final Refrectable value;

	private MetaFieldSettingPartsController partsController = GWT.create(MetaFieldSettingPartsController.class);

	/**
	 * コンストラクタ
	 *
	 * @param className
	 * @param value
	 */
	public MetaFieldSettingPane(MetaFieldSettingDialog owner, String className, Refrectable value) {
		this.owner = owner;
		this.className = className;
		this.value = value;

		setAlign(Alignment.CENTER);
		setMargin(10);
		setMembersMargin(10);
		setWidth100();

		// サブクラスでcreatePaneをカスタマイズするために解析は明示的に実行するように変更
		// コンストラクタ内だとサブクラス側で色々準備できないので
//		service = GWT.create(RefrectionService.class);
//
//		//解析サービス呼び出し
//		service.analysis(className, value, new AsyncCallback<AnalysisResult>() {
//
//			@Override
//			public void onSuccess(AnalysisResult result) {
//				//画面生成
//				createPane(result, value);
//			}
//
//			@Override
//			public void onFailure(Throwable caught) {
//				GWT.log(caught.toString(), caught);
//			}
//		});
	}

	/**
	 * 解析の実行
	 */
	public void init() {
		service = RefrectionServiceFactory.get();

		SmartGWTUtil.showProgress();
		// 解析サービス呼び出し
		service.analysis(TenantInfoHolder.getId(), className, value, new AdminAsyncCallback<AnalysisResult>() {

			@Override
			public void onSuccess(AnalysisResult result) {
				// 画面生成
				createPane(result, value);
				SmartGWTUtil.hideProgress();
			}

			@Override
			protected void beforeFailure(Throwable caught) {
				SmartGWTUtil.hideProgress();
			};
		});
	}

	/**
	 * メタデータの指定フィールドの値を取得。
	 *
	 * @param key
	 * @return
	 */
	public Serializable getValue(String key) {
		return defValueMap.get(key);
	}

	/**
	 * メタデータの指定フィールドの値を設定。
	 *
	 * @param key
	 * @param value
	 */
	public void setValue(String key, Serializable value) {
		defValueMap.put(key, value);
	}

	/**
	 * OKボタン押下時のイベントを設定。
	 *
	 * @param handler
	 */
	public void setOkHandler(MetaFieldUpdateHandler handler) {
		this.okHandler = handler;
	}

	/**
	 * Cancelボタン押下時のイベントを設定。
	 *
	 * @param handler
	 */
	public void setCancelHandler(MetaFieldUpdateHandler handler) {
		this.cancelHandler = handler;
	}

	/**
	 * Fieldを表示するかを判定する。
	 * 拡張クラスなどで独自の表示制御を行いたい場合はオーバーライドする
	 *
	 * @param info 対象Field
	 * @return true：表示
	 */
	protected boolean isVisileField(FieldInfo info) {
		return true;
	}

	/**
	 * サブWindowを生成する。
	 * 拡張クラスなどで独自のMetaFieldSettingWindowを生成する場合はオーバーライドする
	 *
	 * @param className 対象クラス名
	 * @param value 値
	 * @return Window
	 */
	protected MetaFieldSettingDialog createSubDialog(String className, Refrectable value, FieldInfo info) {
		MetaFieldSettingDialog dialog = new MetaFieldSettingDialog(className, value);
		dialog.init();
		return dialog;
	}

	private void createPane(AnalysisResult result, final Refrectable value) {

		for (String key : result.getValueMap().keySet()) {
			defValueMap.put(key, result.getValueMap().get(key));
		}

		// 解析結果から入力フィールドを生成
		ArrayList<FormItem> items = new ArrayList<FormItem>();
		ArrayList<Canvas> canvass = new ArrayList<Canvas>();
		int fieldCount = 0;// ダイアログの大きさをコントロールするための項目数(多言語のボタンは含めない)
		for (FieldInfo info : result.getFields()) {
			if (!isVisileField(info)) {
				continue;
			}

			if (info.getInputType() == InputType.REFERENCE) {
				// 参照系は入力を別にする
				Canvas canvas = createReferenceInputItem(info);
				if (canvas != null) {
					canvass.add(canvas);
					fieldCount++;
				}
			} else {
				FormItem item = createSingleInputItem(info);
				if (item != null) {
					items.add(item);
					fieldCount++;
				}
			}
		}

		// 標準のフォーム作成
		DynamicForm form = new MtpForm();
		form.setFields(items.toArray(new FormItem[items.size()]));

		owner.addOKClickHandler(new OkClickHandler(form, result.getFields(), value));
		owner.addCancelClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (cancelHandler != null) {
					// 親コントロールで設定された処理実行
					cancelHandler.execute(new MetaFieldUpdateEvent(defValueMap, value));
				}
			}
		});

		addMember(form);
		for (Canvas canvas : canvass) {
			addMember(canvas);
		}
	}

	/**
	 * 参照型入力アイテム生成
	 *
	 * @param info
	 * @return
	 */
	private Canvas createReferenceInputItem(final FieldInfo info) {
		if (info.isMultiple()) {
			return createReferenceMultiInputItem(info);
		} else {
			return createReferenceSingleInputItem(info);
		}
	}

	/**
	 * 複数参照型入力レイアウト生成
	 *
	 * @param info
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Canvas createReferenceMultiInputItem(FieldInfo info) {
		// 一覧表示用Grid生成
		HLayout layout = new HLayout();
		layout.setWidth100();
		layout.setMembersMargin(6);
		layout.setAlign(Alignment.CENTER);

		String displayName = getDisplayName(info);
		String title = info.isDeprecated() ? "<del>" + displayName + "</del>" : displayName;
		Label label = new Label(title + " : ");
		label.setAlign(Alignment.RIGHT);
//		label.setPrompt(info.getDescription());

		String description = getDescription(info);
		String prompt = "<div style=\"white-space: nowrap;\">" + description + "</div>";
		label.setPrompt(prompt);

		final ListGrid grid = new ListGrid();
		grid.setWidth("65%");
		grid.setHeight(120);
		grid.setCanFreezeFields(true);

		// grid内でのD&Dでの並べ替えを許可
		grid.setCanDragRecordsOut(true);
		grid.setCanAcceptDroppedRecords(true);
		grid.setCanReorderRecords(true);

		// フィールドの値がnullなら空のリストを詰めておく
		boolean isValueNull = getValue(info.getName()) == null;
		final List<Refrectable> valueList = isValueNull ? new ArrayList<Refrectable>()
				: (List<Refrectable>) getValue(info.getName());
		if (isValueNull) {
			setValue(info.getName(), (Serializable) valueList);
		}

		// カラムの構築
		grid.setFields(new ListGridField("empty"));// 仮設定
		service.analysisListData(TenantInfoHolder.getId(), info.getReferenceClassName(), valueList,
				new AnalysisListDataAsyncCallback(getSimpleName(info.getReferenceClassName()), grid));

		IButton setButton = new IButton(
				AdminClientMessageUtil.getString("ui_metadata_common_MetaFieldSettingPane_edit"));
		setButton.addClickHandler(new MultiReferenceEditClickHandler(grid, valueList, info));

		IButton addButton = new IButton(
				AdminClientMessageUtil.getString("ui_metadata_common_MetaFieldSettingPane_add"));
		addButton.addClickHandler(new MultiReferenceAddClickHandler(valueList, info, grid));

		IButton delButton = new IButton(
				AdminClientMessageUtil.getString("ui_metadata_common_MetaFieldSettingPane_delete"));
		delButton.addClickHandler(new MultiReferenceDeleteClickHandler(grid, valueList));

		VLayout buttonLayout = new VLayout();
		buttonLayout.addMember(setButton);
		buttonLayout.addMember(addButton);
		buttonLayout.addMember(delButton);

		layout.addMember(label);
		layout.addMember(grid);
		layout.addMember(buttonLayout);

		// grid内でのD&D後のデータ並べ替え
		grid.addDragStopHandler(new DragStopHandler() {

			@Override
			public void onDragStop(DragStopEvent dragstopevent) {
				valueList.clear();
				for (ListGridRecord record : grid.getRecords()) {
					Refrectable fieldValue = (Refrectable) record.getAttributeAsObject(RECORD_ATTRIBUTE_VALUE);
					valueList.add(fieldValue);
				}
			}
		});

		return layout;
	}

	/**
	 * 単一参照型入力レイアウト生成
	 *
	 * @param info
	 * @return
	 */
	private Canvas createReferenceSingleInputItem(final FieldInfo info) {
		HLayout layout = new HLayout();
		layout.setWidth100();
		layout.setMembersMargin(6);
		layout.setAlign(Alignment.LEFT);
		layout.setLayoutLeftMargin(10);

		final DynamicForm form = new DynamicForm();
		form.setWidth(260);
		Serializable s = getValue(info.getName());
		final String name = s != null ? s.getClass().getName() : "";

		// 型名を表示、詳細は設定ボタンで
		String displayName = getDisplayName(info);
		String title = info.isDeprecated() ? "<del>" + displayName + "</del>" : displayName;
		final SelectItem item = new SelectItem(FIELD_ATTRIBUTE_CLASSTYPE, title);
		String description = getDescription(info);
		String prompt = "<div style=\"white-space: nowrap;\">" + description + "</div>";
		item.setPrompt(prompt);

		item.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if (event.getOldValue().equals(event.getValue())) {
					// 変更されてなかったらキャンセル
					event.cancel();
				}
			}
		});
		item.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				String className = (String) form.getValue(FIELD_ATTRIBUTE_CLASSTYPE);
				if (className == null || className.isEmpty()) {
					setValue(info.getName(), null);
				} else {
					// 選択されたクラスのインスタンスを生成
					service.create(TenantInfoHolder.getId(), className, new AsyncCallback<Refrectable>() {
						@Override
						public void onSuccess(Refrectable result) {
							// 指定フィールドの更新用の値として保管
							setValue(info.getName(), result);
						}

						@Override
						public void onFailure(Throwable caught) {
							GWT.log(caught.toString(), caught);
						}
					});
				}
			}
		});

		// コンボの内容を取得
		if (info.getFixedReferenceClass() == null || info.getFixedReferenceClass().length == 0) {
			service.getSubClass(TenantInfoHolder.getId(), info.getReferenceClassName(), new AsyncCallback<Name[]>() {

				@Override
				public void onSuccess(Name[] names) {
					LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
					if (!info.isRequired()) {
						valueMap.put("", "");
					}
					for (Name clsName : names) {
						valueMap.put(clsName.getName(), clsName.getDisplayName());
					}
					item.setValueMap(valueMap);
				}

				@Override
				public void onFailure(Throwable caught) {
					GWT.log(caught.toString(), caught);
				}
			});
		} else {
			LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
			if (!info.isRequired()) {
				valueMap.put("", "");
			}
			for (Name clsName : info.getFixedReferenceClass()) {
				valueMap.put(clsName.getName(), clsName.getDisplayName());
			}
			item.setValueMap(valueMap);
		}

		form.setFields(item);
		form.setValue(FIELD_ATTRIBUTE_CLASSTYPE, name);

		IButton setButton = new IButton(
				AdminClientMessageUtil.getString("ui_metadata_common_MetaFieldSettingPane_edit"));
		setButton.addClickHandler(new ReferenceEditClickHandler(info));

		layout.addMember(form);
		layout.addMember(setButton);
		return layout;
	}

	/**
	 * 複数参照型のレコード更新
	 *
	 * @param record
	 * @param className
	 * @param value
	 * @param fields
	 * @param valueMap
	 */
	private void updateRecord(ListGridRecord record, String className, Refrectable value, ListGridField[] fields,
			Map<String, Serializable> valueMap) {
		record.setAttribute(RECORD_ATTRIBUTE_VALUE, value);
		record.setAttribute(RECORD_ATTRIBUTE_TYPE, getSimpleName(value.getClass().getName()));

		// Gridに表示されている項目のみ更新する
		for (ListGridField field : fields) {
			String fieldName = field.getName();
			if (!RECORD_ATTRIBUTE_TYPE.equals(fieldName)) {
				String simpleName = getSimpleName(fieldName);
				record.setAttribute(fieldName, valueMap.get(simpleName));
			}
		}
	}

	private String getSimpleName(String name) {
		return name.substring(name.lastIndexOf(".") + 1);
	}

	/**
	 * 単一入力アイテム生成
	 *
	 * @param info
	 * @return
	 */
	protected FormItem createSingleInputItem(FieldInfo info) {
		FormItem item = partsController.createItem(this, info);

		if (item != null) {
			String displayName = getDisplayName(info);
			String title = info.isDeprecated() ? "<del>" + displayName + "</del>" : displayName;
			item.setTitle(title);
			item.setAttribute(FIELD_ATTRIBUTE_NAME, info.getName());
			String description = getDescription(info);
			if (SmartGWTUtil.isNotEmpty(description)) {
				SmartGWTUtil.addHoverToFormItem(item, description);
			}
			if (info.isRequired()) {
				SmartGWTUtil.setRequired(item);
			}
			if (info.isRangeCheck()) {
				// 数値型の範囲設定
				IntegerRangeValidator ir = new IntegerRangeValidator();
				if (info.getMaxRange() > -127)
					ir.setMax(info.getMaxRange());
				if (info.getMinRange() > -127)
					ir.setMin(info.getMinRange());
				item.setValidators(ir);
			}
		}

		return item;
	}

	/**
	 * 指定の型で値を取得
	 *
	 * @param <T>
	 * @param type
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <T extends Serializable> T getValueAs(Class<T> type, String key) {
		return (T) getValue(key);
	}

	/**
	 * フィールド情報から表示名を取得します。
	 *
	 * @param info フィールド情報
	 * @return 表示名
	 */
	protected String getDisplayName(FieldInfo info) {
		if (info.getDisplayNameKey() != null && !info.getDisplayNameKey().isEmpty()) {
			return getString(info.getDisplayNameKey());
		}
		return info.getDisplayName();
	}

	/**
	 * フィールド情報から説明を取得します。
	 *
	 * @param info フィールド情報
	 * @return 説明
	 */
	protected String getDescription(FieldInfo info) {
		if (info.getDescriptionKey() != null && !info.getDescriptionKey().isEmpty()) {
			return getString(info.getDescriptionKey());
		}
		return info.getDescription();
	}

	/**
	 * フィールド情報からScriptヒントを取得します。
	 * keyは[DescriptionKey + ScriptHint]で検索します。
	 * 該当のヒントがない場合は、DescriptionKeyの値を返します。
	 *
	 * @param info フィールド情報
	 * @return Scriptヒント(説明)
	 */
	protected String getScriptHint(FieldInfo info) {
		if (info.getDescriptionKey() != null && !info.getDescriptionKey().isEmpty()) {
			String hint = getString(info.getDescriptionKey() + "ScriptHint");
			if (hint != null && !hint.isEmpty()) {
				return hint;
			}
			return getString(info.getDescriptionKey());
		}
		return info.getDescription();
	}

	private String getString(String key) {
		return AdminClientMessageUtil.getString(key);
	}

	/**
	 * 複数参照型の解析処理のコールバック
	 *
	 */
	private final class AnalysisListDataAsyncCallback implements AsyncCallback<AnalysisListDataResult> {
		private final ListGrid grid;
		private final String simpleName;

		private AnalysisListDataAsyncCallback(String simpleName, ListGrid grid) {
			this.grid = grid;
			this.simpleName = simpleName;
		}

		@Override
		public void onSuccess(AnalysisListDataResult result) {
			List<ListGridField> fieldList = new ArrayList<ListGridField>();
			// fieldList.add(new ListGridField(RECORD_ATTRIBUTE_TYPE,
			// AdminClientMessageUtil.getString("ui_metadata_common_MetaFieldSettingPane_type")));
			for (FieldInfo info : result.getFields()) {
				if (!info.isMultiple() && info.getInputType() != InputType.REFERENCE
						&& info.getInputType() != InputType.SCRIPT && info.getInputType() != InputType.MULTI_LANG_LIST) {
					// 複数型と参照型は表示しない
					String displayName = getDisplayName(info);
					fieldList.add(new ListGridField(simpleName + "." + info.getName(), displayName));
				}
			}
			if (fieldList.size() > 3) {
				for (ListGridField field : fieldList) {
					field.setWidth(80);
				}
			}
			grid.setFields(fieldList.toArray(new ListGridField[fieldList.size()]));

			for (RefrectableInfo info : result.getRefrectables()) {
				ListGridRecord record = new ListGridRecord();
				updateRecord(record, simpleName, info.getValue(), grid.getFields(), info.getValueMap());
				grid.addData(record);
			}
		}

		@Override
		public void onFailure(Throwable caught) {
			GWT.log(caught.toString(), caught);
		}

	}

	/**
	 * 参照型（複数）の追加ボタン用コールバック処理
	 */
	private final class CreateAsyncCallback implements AsyncCallback<Refrectable> {

		/** 表示用グリッド */
		private final ListGrid grid;
		/** 参照型フィールドの値 */
		private final List<Refrectable> valueList;
		/** 参照型のクラス名 */
		private final String className;
		/** 参照型オブジェクトが保持されるフィールド名 */
		private final FieldInfo info;

		/**
		 * コンストラクタ
		 *
		 * @param grid
		 * @param valueList
		 * @param className
		 * @param name
		 */
		private CreateAsyncCallback(ListGrid grid, List<Refrectable> valueList, String className, FieldInfo info) {
			this.grid = grid;
			this.valueList = valueList;
			this.className = className;
			this.info = info;
		}

		@Override
		public void onSuccess(Refrectable result) {
			// 設定画面表示
			final MetaFieldSettingDialog dialog = createSubDialog(className, result, info);
			dialog.setOkHandler(new MetaFieldUpdateHandler() {

				@Override
				public void execute(final MetaFieldUpdateEvent event) {
					// ダイアログ破棄
					dialog.destroy();

					// ValueObject再設定
					valueList.add(event.getValue());

					// レコード生成
					ListGridRecord record = new ListGridRecord();
					updateRecord(record, getSimpleName(info.getReferenceClassName()), event.getValue(),
							grid.getFields(), event.getValueMap());
					grid.addData(record);

					// 作成したObjectを保存
					setValue(info.getName(), (Serializable) valueList);
				}
			});

			dialog.setCancelHandler(new DialogClooseHandler(dialog));

			dialog.show();
		}

		@Override
		public void onFailure(Throwable caught) {
			GWT.log(caught.toString(), caught);
		}
	}

	/**
	 * OKボタンイベント
	 */
	private final class OkClickHandler implements ClickHandler {

		/** 入力フォーム */
		private final DynamicForm form;
		/** フィールド情報 */
		private final FieldInfo[] fields;
		/** 更新データ */
		private final Refrectable value;

		/**
		 * コンストラクタ
		 *
		 * @param form
		 * @param fields
		 */
		private OkClickHandler(DynamicForm form, FieldInfo[] fields, Refrectable value) {
			this.form = form;
			this.fields = fields;
			this.value = value;
		}

		@Override
		public void onClick(ClickEvent event) {
			if (!form.validate())
				return;

			for (FieldInfo info : fields) {
				// 表示対象外は更新しない
				if (!isVisileField(info)) {
					continue;
				}

				// 多言語Listは対象プロパティ側でセット
				if (info.getInputType() == InputType.MULTI_LANG_LIST) {
					continue;
				}

				// CanvasItemは個別で値設定
				if (form.getItem(info.getName()) != null) {
					if (form.getItem(info.getName()) instanceof MetaFieldCanvasItem) {
						continue;
					}
				}

				if (form.getValue(info.getName()) != null) {
					// Formの値をMapに格納
					// 参照型とテキストエリアはFormItemではないので、ダイアログでの編集時に設定
					setValue(info.getName(), getFormValue(info));
				} else {
					// 参照型かテキストエリア以外は未入力ならnull設定
					if (info.getInputType() != InputType.REFERENCE) {
						if (info.getInputType() == InputType.CHECKBOX) {
							// チェックボックス未操作＆未設定だとnullになる
							setValue(info.getName(), false);
						} else {
							setValue(info.getName(), null);
						}
					}
				}

				if (info.getInputType() == InputType.MULTI_LANG
						&& SmartGWTUtil.isNotEmpty(info.getMultiLangFieldName())) {
					if (form.getItem(info.getName()) != null) {
						MetaDataLangTextItem mdl = (MetaDataLangTextItem) form.getItem(info.getName());
						setValue(info.getMultiLangFieldName(), (Serializable) mdl.getLocalizedList());
					}
				}
			}

			// 更新サービス呼び出し
			HashMap<String, Serializable> valueMap = new HashMap<String, Serializable>();
			valueMap.putAll(defValueMap);// defValueMapのままだと値が古い？
			service.update(TenantInfoHolder.getId(), value, valueMap, new AsyncCallback<Refrectable>() {

				@Override
				public void onSuccess(Refrectable result) {
					if (okHandler != null) {
						// 親コントロールで設定された処理実行
						okHandler.execute(new MetaFieldUpdateEvent(defValueMap, result));
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					GWT.log(caught.toString(), caught);
				}
			});
		}

		/**
		 * 入力タイプに合わせて値を取得
		 *
		 * @param info
		 * @return
		 */
		private Serializable getFormValue(FieldInfo info) {
			if (info.getInputType() == InputType.TEXT) {
				return (String) form.getValue(info.getName());
//			} else if (info.getInputType() == InputType.TextArea) {
//				return (String) form.getValue(info.getName());
			} else if (info.getInputType() == InputType.NUMBER) {
				return Integer.parseInt(form.getValue(info.getName()).toString());
			} else if (info.getInputType() == InputType.CHECKBOX) {
				return Boolean.parseBoolean(form.getValue(info.getName()).toString());
			} else if (info.getInputType() == InputType.ENUM) {
				String val = (String) form.getValue(info.getName());
				if ("".equals(val))
					return null;
				return val;
			} else if (info.getInputType() == InputType.ACTION) {
				String val = (String) form.getValue(info.getName());
				if (DEFAULT_ACTION_NAME.equals(val))
					return null;
				else
					return val;
			} else if (info.getInputType() == InputType.WEBAPI) {
				String val = (String) form.getValue(info.getName());
				if (DEFAULT_WEBAPI_NAME.equals(val))
					return null;
				else
					return val;
			}
			return (Serializable) form.getValue(info.getName());
		}
	}

	/**
	 * 単一参照型の設定ボタンイベント
	 */
	private final class ReferenceEditClickHandler implements ClickHandler {

		private final FieldInfo info;

		/**
		 * コンストラクタ
		 *
		 * @param FieldInfo
		 */
		private ReferenceEditClickHandler(FieldInfo info) {
			this.info = info;
		}

		@Override
		public void onClick(ClickEvent event) {
			// 指定フィールドの値を取得
			Serializable refValue = getValue(info.getName());

			// 編集ダイアログ表示
			final MetaFieldSettingDialog dialog = createSubDialog(refValue.getClass().getName(), (Refrectable) refValue,
					info);

			dialog.setOkHandler(new MetaFieldUpdateHandler() {

				@Override
				public void execute(MetaFieldUpdateEvent event) {
					// ダイアログ破棄
					dialog.destroy();

					// 更新したObjectで上書き
					setValue(info.getName(), event.getValue());
				}
			});

			dialog.setCancelHandler(new DialogClooseHandler(dialog));

			dialog.show();
		}
	}

	/**
	 * 複数参照型の編集ボタンクリックイベント
	 *
	 */
	private final class MultiReferenceEditClickHandler implements ClickHandler {
		private final ListGrid grid;
		private final List<Refrectable> valueList;
		private final FieldInfo info;
		private final String simpleName;

		private MultiReferenceEditClickHandler(ListGrid grid, List<Refrectable> valueList, FieldInfo info) {
			this.grid = grid;
			this.valueList = valueList;
			this.info = info;
			this.simpleName = getSimpleName(info.getReferenceClassName());
		}

		@Override
		public void onClick(ClickEvent event) {
			final ListGridRecord record = grid.getSelectedRecord();
			if (record == null)
				return;

			Refrectable fieldValue = (Refrectable) record.getAttributeAsObject(RECORD_ATTRIBUTE_VALUE);
			String className = fieldValue.getClass().getName();
			final MetaFieldSettingDialog dialog = createSubDialog(className, (Refrectable) fieldValue, info);
			dialog.setOkHandler(new MetaFieldUpdateHandler() {

				@Override
				public void execute(MetaFieldUpdateEvent event) {
					// ダイアログ破棄
					dialog.destroy();

					// 表示対象更新
					updateRecord(record, simpleName, event.getValue(), grid.getFields(), event.getValueMap());
					grid.refreshRow(grid.getRecordIndex(record));

					int index = grid.getRecordIndex(record);
					valueList.remove(index);
					valueList.add(index, event.getValue());

					setValue(info.getName(), (Serializable) valueList);
				}
			});

			dialog.setCancelHandler(new DialogClooseHandler(dialog));
			dialog.show();
		}
	}

	/**
	 * 複数参照型の追加ボタンクリックイベント
	 *
	 */
	private final class MultiReferenceAddClickHandler implements ClickHandler {
		private final List<Refrectable> valueList;
		private final FieldInfo info;
		private final ListGrid grid;

		private MultiReferenceAddClickHandler(List<Refrectable> valueList, FieldInfo info, ListGrid grid) {
			this.valueList = valueList;
			this.info = info;
			this.grid = grid;
		}

		@Override
		public void onClick(ClickEvent event) {
			// 型リストを取得
			service.getSubClass(TenantInfoHolder.getId(), info.getReferenceClassName(), new AsyncCallback<Name[]>() {

				@Override
				public void onSuccess(Name[] names) {
					if (names != null && names.length > 1) {
						// 利用可能なクラスが複数ある場合、インスタンス化するクラスを選択
						Window dialog = new Window();
						final DynamicForm form = new DynamicForm();
						SelectItem item = new SelectItem(FIELD_ATTRIBUTE_CLASSTYPE,
								AdminClientMessageUtil.getString("ui_metadata_common_MetaFieldSettingPane_type"));
						LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
						for (Name clsName : names) {
							valueMap.put(clsName.getName(), clsName.getDisplayName());
						}
						item.setValueMap(valueMap);
						form.setFields(item);

						// ボタン処理
						IButton ok = new IButton("OK");
						IButton cancel = new IButton("cancel");
						ok.addClickHandler(new ClickHandler() {

							@Override
							public void onClick(ClickEvent event) {
								String className = (String) form.getValue(FIELD_ATTRIBUTE_CLASSTYPE);
								service.create(TenantInfoHolder.getId(), className,
										new CreateAsyncCallback(grid, valueList, className, info));
							}
						});
						cancel.addClickHandler(new DialogClooseHandler(dialog));

						HLayout hl = new HLayout();
						hl.setAlign(Alignment.CENTER);
						hl.setAlign(VerticalAlignment.CENTER);
						hl.addMember(ok);
						hl.addMember(cancel);

						VLayout vl = new VLayout();
						vl.setAlign(Alignment.CENTER);
						vl.setMembersMargin(10);
						vl.setWidth100();

						vl.addMember(form);
						vl.addMember(hl);

						dialog.addItem(vl);
						dialog.show();

					} else if (names != null && names.length == 1) {
						// 一つしかない場合、その型でインスタンス作成
						service.create(TenantInfoHolder.getId(), names[0].getName(),
								new CreateAsyncCallback(grid, valueList, names[0].getName(), info));
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					GWT.log(caught.toString(), caught);
				}
			});
		}
	}

	/**
	 * 複数参照型の削除ボタンクリックイベント
	 *
	 */
	private final class MultiReferenceDeleteClickHandler implements ClickHandler {
		private final ListGrid grid;
		private final List<Refrectable> valueList;

		private MultiReferenceDeleteClickHandler(ListGrid grid, List<Refrectable> valueList) {
			this.grid = grid;
			this.valueList = valueList;
		}

		@Override
		public void onClick(ClickEvent event) {
			ListGridRecord record = grid.getSelectedRecord();
			if (record == null)
				return;

			// フィールド情報から削除
			// valueList.remove(record.getAttributeAsObject(RECORD_ATTRIBUTE_VALUE));
			// レコードのインスタンスがリストのものと違うため削除できない
			int index = grid.getRecordIndex(record);
			valueList.remove(index);

			// グリッドから削除
			grid.removeData(record);
		}
	}

	/**
	 * ダイアログ破棄用のイベントハンドラ
	 */
	private final class DialogClooseHandler implements MetaFieldUpdateHandler, ClickHandler {

		/** ダイアログ */
		private final Window dialog;

		/**
		 * コンストラクタ
		 *
		 * @param dialog
		 */
		private DialogClooseHandler(Window dialog) {
			this.dialog = dialog;
		}

		@Override
		public void execute(MetaFieldUpdateEvent event) {
			destroy();
		}

		@Override
		public void onClick(ClickEvent event) {
			destroy();
		}

		private void destroy() {
			// ダイアログ破棄
			dialog.destroy();
		}
	}

}
