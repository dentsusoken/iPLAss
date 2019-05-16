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

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.rpc.AdminAsyncCallback;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataLangTextItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.metadata.dto.refrect.AnalysisResult;
import org.iplass.adminconsole.shared.metadata.dto.refrect.FieldInfo;
import org.iplass.adminconsole.shared.metadata.rpc.refrect.RefrectionServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.refrect.RefrectionServiceFactory;
import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.Refrectable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.validator.IntegerRangeValidator;
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

	private static final String DEFAULT_ACTION_NAME = "#default";
	private static final String DEFAULT_WEBAPI_NAME = "#default";

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

		setWidth100();

		// サブクラスでcreatePaneをカスタマイズするために解析はinitで明示的に実行するように変更
		// コンストラクタ内だとサブクラス側で色々準備できないので
	}

	/**
	 * 解析の実行
	 */
	protected void init() {
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
	protected Serializable getValue(String key) {
		return defValueMap.get(key);
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
	 * メタデータの指定フィールドの値を設定。
	 *
	 * @param key
	 * @param value
	 */
	protected void setValue(String key, Serializable value) {
		defValueMap.put(key, value);
	}

	/**
	 * OKボタン押下時のイベントを設定。
	 *
	 * @param handler
	 */
	protected void setOkHandler(MetaFieldUpdateHandler handler) {
		this.okHandler = handler;
	}

	/**
	 * Cancelボタン押下時のイベントを設定。
	 *
	 * @param handler
	 */
	protected void setCancelHandler(MetaFieldUpdateHandler handler) {
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

	protected String getSimpleName(String name) {
		return name.substring(name.lastIndexOf(".") + 1);
	}

	/**
	 * 入力アイテム生成
	 */
	protected FormItem createInputItem(FieldInfo info) {
		FormItem item = partsController.createItem(this, info);

		if (item != null) {
			String displayName = getDisplayName(info);
			String title = info.isDeprecated() ? "<del>" + displayName + "</del>" : displayName;
			//見た目だけ変更して、必須チェックは個別に実施(CanvasItemを考慮)
			if (info.isRequired()) {
				title = "<b>" + title + "</b>";
			}
			item.setTitle(title);
			item.setName(info.getName());
			String description = getDescription(info);
			if (SmartGWTUtil.isNotEmpty(description)) {
				SmartGWTUtil.addHoverToFormItem(item, description);
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
	 * 画面生成後の処理
	 *
	 * @param form 生成したForm
	 */
	protected void afterCreatePane(DynamicForm form) {
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

	/**
	 * 起動ダイアログを返します。
	 *
	 * @return 起動ダイアログ
	 */
	protected MetaFieldSettingDialog getOwner() {
		return owner;
	}

	private void createPane(AnalysisResult result, final Refrectable value) {

		for (String key : result.getValueMap().keySet()) {
			defValueMap.put(key, result.getValueMap().get(key));
		}

		// 解析結果から入力フィールドを生成
		ArrayList<FormItem> items = new ArrayList<FormItem>();
		for (FieldInfo info : result.getFields()) {
			if (!isVisileField(info)) {
				continue;
			}

			FormItem item = createInputItem(info);
			if (item != null) {
				items.add(item);
			}
		}

		// 標準のフォーム作成
		DynamicForm form = new MtpForm();
		form.setFields(items.toArray(new FormItem[items.size()]));

		afterCreatePane(form);

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
	}

	private String getString(String key) {
		return AdminClientMessageUtil.getString(key);
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
			boolean validate = form.validate();

			for (FieldInfo info : fields) {
				// 表示対象外は更新しない
				if (!isVisileField(info)) {
					continue;
				}

				// 多言語Listは対象プロパティ側でセット
				if (info.getInputType() == InputType.MULTI_LANG_LIST) {
					continue;
				}

				// CanvasItemは個別でチェック
				if (form.getItem(info.getName()) != null) {
					if (form.getItem(info.getName()) instanceof MetaFieldCanvasItem) {
						MetaFieldCanvasItem canvasItem = (MetaFieldCanvasItem)form.getItem(info.getName());
						validate = validate & canvasItem.validate();
					}
				}
			}
			if (!validate) {
				return;
			}

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

}
