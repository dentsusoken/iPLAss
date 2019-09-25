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

package org.iplass.adminconsole.client.base.util;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.AnimationCloseCallback;
import org.iplass.adminconsole.client.base.ui.widget.AnimationFullScreenCallback;
import org.iplass.adminconsole.client.base.ui.widget.MtpWidgetConstants;
import org.iplass.adminconsole.client.base.ui.widget.ProgressDialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Cookies;
import com.smartgwt.client.core.Rectangle;
import com.smartgwt.client.types.DateDisplayFormat;
import com.smartgwt.client.types.TimeDisplayFormat;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.util.Page;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.AnimationCallback;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.HoverEvent;
import com.smartgwt.client.widgets.events.HoverHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.CanvasItem;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.DateTimeItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.FormItemIcon;
import com.smartgwt.client.widgets.form.fields.IntegerItem;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.TimeItem;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ItemHoverEvent;
import com.smartgwt.client.widgets.form.fields.events.ItemHoverHandler;
import com.smartgwt.client.widgets.layout.HLayout;

/**
 * @author 片野　博之
 *
 */
public final class SmartGWTUtil {
	public static final String ENTITY = "ENTITY";
	/**
	 *
	 */
	private SmartGWTUtil() {
	}

	public static Map<String, Object> getAttributes(JavaScriptObject object) {
		@SuppressWarnings("unchecked")
		Map<String, Object> m = JSOHelper.convertToMap(object);
		m.remove(SC.REF);
		m.remove(ENTITY);
		return m;
	}

	/**
	 * List<String>を指定セパレータで連結したStringで返します
	 *
	 * @param values Listの文字列
	 * @param sepalator セパレータ
	 * @return セパレータで連結した文字列
	 */
	public static String convertListToString(List<String> values, String sepalator) {
		StringBuffer sb = new StringBuffer();
		if (values != null) {
			for (String value : values) {
				sb.append(value).append(sepalator);
			}
		}
		if (sb.length() > 0) {
			sb.substring(0, sb.length() - 1);
		}
		return sb.toString();
	}

	/**
	 * 配列を指定セパレータで連結したStringで返します
	 *
	 * @param values 文字列の配列
	 * @param sepalator セパレータ
	 * @return セパレータで連結した文字列
	 */
	public static String convertArrayToString(String[] values, String sepalator) {
		StringBuffer sb = new StringBuffer();
		if (values != null) {
			for (String value : values) {
				sb.append(value).append(sepalator);
			}
		}
		if (sb.length() > 0) {
			sb.substring(0, sb.length() - 1);
		}
		return sb.toString();
	}

	/**
	 * Stringを改行で分割したList<String>で返します
	 *
	 * @param value String文字列
	 * @return 改行で分けられたListの文字列
	 */
	public static List<String> convertStringToList(String value) {
		return convertStringToList(value, "\r\n|[\n\r\u2028\u2029\u0085]");
	}

	/**
	 * Stringを指定セパレータで分割したList<String>で返します
	 *
	 * @param value String文字列
	 * @param sepalator セパレータ
	 * @return セパレータで分けられたListの文字列
	 */
	public static List<String> convertStringToList(String value, String sepalator) {

		if (value == null) {
			return null;
		}

		String[] valueArray = value.split(sepalator);
		//空値の除外
		List<String> valueList = Arrays.asList(valueArray).stream()
				.filter( o -> o != null && !o.trim().isEmpty())
				.collect(Collectors.toList());
		if (valueList.isEmpty()) {
			return null;
		} else {
			return valueList;
		}
	}

	/**
	 * Stringを改行で分割した配列で返します
	 *
	 * @param value String文字列
	 * @return 改行で分けられた配列
	 */
	public static String[] convertStringToArray(String value) {
		return convertStringToArray(value, "\r\n|[\n\r\u2028\u2029\u0085]");
	}

	/**
	 * Stringを指定セパレータで分割した配列で返します
	 *
	 * @param value String文字列
	 * @param sepalator セパレータ
	 * @return セパレータで分けられた配列
	 */
	public static String[] convertStringToArray(String value, String sepalator) {

		List<String> valueList = convertStringToList(value, sepalator);
		if (valueList == null) {
			return null;
		}
		return valueList.toArray(new String[]{});
	}

	/**
	 * <p>{@link TextItem} などの {@link FormItem} からStringの値を取得します。</p>
	 *
	 * <p>値がnullの場合は、nullを返します。</p>
	 *
	 * @param item {@link FormItem}
	 * @return {@link FormItem} の値
	 */
	public static String getStringValue(FormItem item) {
		return getStringValue(item, false);
	}

	/**
	 * <p>{@link TextItem} などの {@link FormItem} からStringの値を取得します。</p>
	 *
	 * <p>値がnullの場合は、nullを返します。</p>
	 *
	 * @param item {@link FormItem}
	 * @param emptyToNull 空の場合にnullにするか
	 * @return {@link FormItem} の値
	 */
	public static String getStringValue(FormItem item, boolean emptyToNull) {
		if (emptyToNull) {
			return (item.getValue() != null && !item.getValue().toString().isEmpty() ? item.getValue().toString() : null);
		} else {
			return (item.getValue() != null ? item.getValue().toString() : null);
		}
	}

	/**
	 * <p>{@link IntegerItem} などの {@link FormItem} からIntegerの値を取得します。</p>
	 *
	 * <p>値がnullの場合は、nullを返します。</p>
	 *
	 * @param item {@link FormItem}
	 * @return {@link FormItem} の値
	 */
	public static Integer getIntegerValue(FormItem item) {
		String str = getStringValue(item, true);
		Integer ret = null;
		if (str != null) {
			try {
				ret = Integer.parseInt(str);
			} catch (NumberFormatException e) {
			}
		}
		return ret;
	}

	/**
	 * <p>{@link IntegerItem} などの {@link FormItem} からLongの値を取得します。</p>
	 *
	 * <p>値がnullの場合は、nullを返します。</p>
	 *
	 * @param item {@link FormItem}
	 * @return {@link FormItem} の値
	 */
	public static Long getLongValue(FormItem item) {
		String str = getStringValue(item, true);
		Long ret = null;
		if (str != null) {
			try {
				ret = Long.parseLong(str);
			} catch (NumberFormatException e) {
			}
		}
		return ret;
	}

	/**
	 * <p>{@link CheckboxItem} などの {@link FormItem} からBooleanの値を取得します。</p>
	 *
	 * <p>値がnullの場合は、falseを返します。</p>
	 *
	 * @param item {@link FormItem}
	 * @return {@link FormItem} の値
	 */
	public static boolean getBooleanValue(FormItem item) {
		Object val = item.getValue();
		if (val != null) {
			if (val instanceof Boolean) {
				return (Boolean) val;
			} else if (val instanceof String) {
				return "1".equals(val);
			} else if (val instanceof Integer) {
				return 1 == (Integer) val;
			}
		}
		return false;
	}

	/**
	 * <p>値が配列かを判定します。</p>
	 *
	 * <p>obj.getClass().isArray()がgwtのコンパイル後ではうまく動作しないため独自に実装しています。</p>
	 *
	 * @param value 対象の値
	 * @return true：配列
	 * @see http://stackoverflow.com/questions/2725533/how-to-see-if-an-object-is-an-array
	 */
	public static boolean isArray(Object value) {
		//obj.getClass().isArray()がgwtのコンパイル後ではうまく動かないため独自実装
		return value instanceof Object[]
				|| value instanceof boolean[]
				|| value instanceof byte[]
				|| value instanceof short[]
				|| value instanceof char[]
				|| value instanceof int[]
				|| value instanceof long[]
				|| value instanceof float[]
				|| value instanceof double[];
	}

	/**
	 * 必須Validationを設定します。
	 *
	 * @param item FormItem
	 */
	public static void setRequired(final FormItem item) {
		setRequired(item, AdminClientMessageUtil.getString("ui_util_SmartGWTUtil_requiredField"));
	}

	/**
	 * 必須Validationを設定します。
	 *
	 * @param item FormItem
	 * @param message エラーメッセ―ジ
	 */
	public static void setRequired(final FormItem item, String message) {
		//TODO nowrap指定方法が不明
		item.setRequired(true);
		item.setRequiredMessage(message);
	}

	/**
	 * Hintを設定します。
	 *
	 * @param item FormItem
	 * @param hint 表示メッセージ
	 */
	public static void addHintToFormItem(final FormItem item, final String hint) {
		item.setIcons(getHintIcon(hint));
	}

	public static FormItemIcon getHintIcon(final String hint) {
		FormItemIcon icon = SmartGWTUtil.getIcon(MtpWidgetConstants.ICON_HELP);
		icon.addFormItemClickHandler(new FormItemClickHandler() {
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				SC.say(hint);
			}
		});
		icon.setBaseStyle("adminButtonRounded");
		return icon;
	}

	public static void addHintToLabel(final Label label, final String hint) {
		label.setIcon(MtpWidgetConstants.ICON_HELP);
		label.addIconClickHandler(new com.smartgwt.client.widgets.events.IconClickHandler() {
			@Override
			public void onIconClick(
					com.smartgwt.client.widgets.events.IconClickEvent event) {
				SC.say(hint);
			}
		});
	}

	/**
	 * Hoverを設定します。
	 *
	 * @param item FormItem
	 * @param prompt 表示メッセージ
	 */
	public static void addHoverToFormItem(final FormItem item, final String prompt) {
//		item.setHoverStyle(".formHover");	//スタイル指定は面倒くさいので直接指定
		item.addItemHoverHandler(new ItemHoverHandler() {
			@Override
			public void onItemHover(ItemHoverEvent event) {
				item.setPrompt(getHoverString(prompt));
			}
		});
	}

	/**
	 * Hoverを設定します。
	 *
	 * @param item Canvas
	 * @param prompt 表示メッセージ
	 */
	public static void addHoverToCanvas(final Canvas item, final String prompt) {
//		item.setHoverStyle(".formHover");	//スタイル指定は面倒くさいので直接指定
		item.addHoverHandler(new HoverHandler() {
			@Override
			public void onHover(HoverEvent event) {
				item.setPrompt(getHoverString(prompt));
			}
		});
		item.setCanHover(true);
	}

	/**
	 * Hover用にスタイルを設定したメッセージを返します。
	 *
	 * @param hover 表示メッセージ
	 */
	public static String getHoverString(String hover) {
		if (hover == null || hover.isEmpty()) {
			return "";
		}
		return "<div style=\"white-space:nowrap;\">"
			+ hover.replaceAll("\r\n", "<br/>").replaceAll("\n", "<br/>").replaceAll("\r", "<br/>")
			+ "</div>";
	}

	/**
	 * アイコンを生成します。
	 *
	 * @param src ソース
	 * @return アイコン
	 */
	public static FormItemIcon getIcon(String src) {
		FormItemIcon icon = new FormItemIcon();
		icon.setSrc(src);
		return icon;
	}

	/**
	 * Form上に配置するダミーのSpacerを生成します。
	 *
	 * @return Spacer
	 */
	public static SpacerItem createSpacer() {
		return createSpacer(-1);
	}

	/**
	 * Form上に配置するダミーのSpacerを生成します。
	 *
	 * @param width 幅
	 * @return Spacer
	 */
	public static SpacerItem createSpacer(int width) {
		SpacerItem spacer = new SpacerItem();
		if (width < 0) {
			spacer.setWidth("100%");
		} else {
			spacer.setWidth(width);
		}
		return spacer;
	}

	/**
	 * FormItemを編集不可にします。
	 *
	 * FormItemのsetDisabled(true)の場合、選択してコピーできないので。
	 * ※FormItemのsetDisabled(true)時の表示色は見やすいようにcssでカスタマイズしました。
	 * またReadOnlyの場合、フォーカスは取得できてしまいます(setCanFocus(false)してますが・・・)。
	 *
	 * @param item
	 */
	public static void setReadOnly(final FormItem item) {
		setReadOnly(item, true);
	}

	/**
	 * FormItemを編集不可にします。
	 *
	 * FormItemのsetDisabled(true)の場合、選択してコピーできないので。
	 * ※FormItemのsetDisabled(true)時の表示色は見やすいようにcssでカスタマイズしました。
	 * またReadOnlyの場合、フォーカスは取得できてしまいます(setCanFocus(false)してますが・・・)。
	 *
	 * @param item
	 * @param readonly 編集可/編集不可
	 */
	public static void setReadOnly(final FormItem item, boolean readonly) {
		if (readonly) {
			//FormItemにsetReadOnly(boolean)みたいなメソッドはないが、以下のAttributeを設定することで有効になる
			//item.setAttribute("readOnly", true);
			//->readOnlyをtrueにしても編集可能になってしまうので、setCanEdit(false)
			item.setCanEdit(false);

			//このままだと編集できるかできないかわからないのでStyleを設定
			//通常、文字が灰色になり見にくくなるのでmtpadmin.cssでカスタマイズ
			item.setTextBoxStyle("textItemDisabled");
			item.setCanFocus(false);	//これを設定しないとFocusを得たときに別のStyleに変わってしまうので
		} else {
			item.setCanEdit(true);
			item.setTextBoxStyle("textItem");
			item.setCanFocus(true);
		}
	}

	/**
	 * TextAreaItemを編集不可にします。
	 * @param item
	 */
	public static void setReadOnlyTextArea(final TextAreaItem item) {
		setReadOnly(item);
//		//TextAeraItemにreadOnlyとか無いので、入力キャンセルとカーソル位置固定で疑似readOnlyに
//		item.addChangeHandler(new ChangeHandler() {
//
//			@Override
//			public void onChange(ChangeEvent event) {
//				event.cancel();
//				int cursorPos = item.getSelectionRange()[0];
//				item.setAttribute("cursorPos", new Integer(cursorPos - 1));
//			}
//		});
//		item.addChangedHandler(new ChangedHandler() {
//
//			@Override
//			public void onChanged(ChangedEvent event) {
//				int cursorPos = item.getAttributeAsInt("cursorPos");
//				item.setSelectionRange(cursorPos, cursorPos);
//			}
//		});
	}

	/**
	 * 画面上を分割するコンポーネントを返します。
	 *
	 * @return 分割コンポーネント
	 */
	public static Canvas separator() {
		Label separator = new Label("<hr/>");
		separator.setWidth100();
		separator.setAutoHeight();

		return separator;
	}

	/**
	 * 画面上に表示するタイトル用ラベルを返します。
	 *
	 * @return タイトル用ラベル
	 */
	public static Label titleLabel(String title) {
		return titleLabel(title, false);
	}

	/**
	 * 画面上に表示するタイトル用ラベルを返します。
	 *
	 * @return タイトル用ラベル
	 */
	public static Label titleLabel(String title, boolean required) {
		String titleStr = title + " :";
		if (required) {
			titleStr = "<b>" + titleStr + "</b>";
		}
		Label titleLabel = new Label();
		titleLabel.setContents(titleStr);
		titleLabel.setStyleName("formTitle");
		titleLabel.setWidth100();
		titleLabel.setAutoHeight();
		titleLabel.setMargin(5);

		return titleLabel;
	}

	/**
	 * Progress画面を表示します。
	 */
	public static void showProgress() {
		ProgressDialog.showProgress();
	}

	/**
	 * Progress画面を表示します。
	 *
	 * @param contents 表示文言
	 */
	public static void showProgress(String contents) {
		ProgressDialog.showProgress(contents);
	}

	/**
	 * Save時のProgress画面を表示します。
ｓ	 */
	public static void showSaveProgress() {
		ProgressDialog.showProgress("Save...");
	}

	/**
	 * Progress画面を非表示にします。
	 */
	public static void hideProgress() {
		ProgressDialog.hideProgress();
	}

	/** Page全体からのMargin */
	private static final int MARGIN = 20;
	/**
	 * アニメーションを利用して画面を開きます。
	 * 画面を開く処理は、FullScreenCallbackのexecuteメソッドで実装してください。
	 */
	public static void showAnimationFullScreen(final AnimationFullScreenCallback callback) {
		final Rectangle rect = new Rectangle(
				(Page.getWidth() / 2) - 5, (Page.getHeight() / 2) - 5, 10, 10);

		final Canvas animateOutline = new Canvas();
		animateOutline.setBorder("2px solid black");
		animateOutline.setTop(rect.getTop());
		animateOutline.setLeft(rect.getLeft());
		animateOutline.setWidth(rect.getLeft());
		animateOutline.setHeight(rect.getHeight());

		animateOutline.show();

		final int width = Page.getWidth() - MARGIN * 2;
		final int height = Page.getHeight() - MARGIN * 2;

		//callbackに設定
		callback.setOutline(animateOutline);
		callback.setWidth(width);
		callback.setHeight(height);

		animateOutline.animateRect(MARGIN, MARGIN, width, height, callback, 500);
	}

	/**
	 * アニメーションを利用して画面を閉じます。
	 * 画面を閉じる処理は、AnimationCloseCallbackのbeforeAnimateメソッドで実装してください。
	 */
	public static void closeAnimationScreen(final Canvas target, final AnimationCloseCallback callback) {
		final Rectangle rect = new Rectangle(
				(Page.getWidth() / 2) - 5, (Page.getHeight() / 2) - 5, 10, 10);

		final Canvas animateOutline = new Canvas();
		animateOutline.setBorder("2px solid black");
		animateOutline.setRect(target.getLeft(), target.getTop(), target.getWidth(), target.getHeight());
		animateOutline.show();

		//通知
		callback.beforeAnimate();

		animateOutline.animateRect(rect.getLeft(), rect.getTop(),
				rect.getWidth(), rect.getHeight(), new AnimationCallback() {
			@Override
			public void execute(boolean earlyFinish) {
				animateOutline.hide();

				//通知
				callback.afterAnimate();
			}
		}, 500);
	}

	private static LinkedHashMap<String, String> localeMap = null;
	public static LinkedHashMap<String, String> getDefaultLocaleMap() {
		if (localeMap != null) {
			return localeMap;
		}
		localeMap = new LinkedHashMap<>();
		localeMap.put("", "");
		localeMap.put("ja_JP", "ja_JP");
		localeMap.put("en_US", "en_US");
		return localeMap;
	}

	private static LinkedHashMap<String, String> timeZoneMap = null;
	public static LinkedHashMap<String, String> getDefaultTimeZoneMap() {
		if (timeZoneMap != null) {
			return timeZoneMap;
		}
		timeZoneMap = new LinkedHashMap<>();
		timeZoneMap.put("", "");
		timeZoneMap.put("Asia/Tokyo", "Asia/Tokyo");
		timeZoneMap.put("Etc/GMT+0", "GMT");
		timeZoneMap.put("America/Los_Angeles", "America/Los_Angeles");
		timeZoneMap.put("America/Belize", "America/Belize");
		timeZoneMap.put("America/New_York", "America/New_York");
		return timeZoneMap;
	}

	public static Date getDateTimeValue(Date dateValue, Date timeValue, boolean showSecond, String defaultTimeStr, String fixedSec, String fixedMilSec) {

		if (dateValue == null) {
			return null;
		}

		//日付部分のDateをString形式に変換
		DateTimeFormat toDateStrFormatter = DateTimeFormat.getFormat("yyyy/MM/dd");
		String dateStr = toDateStrFormatter.format(dateValue);

		//時間部分のDateをString形式に変換
		String timeStr = null;
		if (timeValue != null) {
			if (showSecond) {
				DateTimeFormat toTimeStrFormatter = DateTimeFormat.getFormat("HH:mm:ss");
				timeStr = toTimeStrFormatter.format(timeValue);
			} else {
				DateTimeFormat toTimeStrFormatter = DateTimeFormat.getFormat("HH:mm");
				timeStr = toTimeStrFormatter.format(timeValue) + ":" + fixedSec;
			}
		} else {
			if (showSecond) {
				timeStr = defaultTimeStr;
			} else {
				timeStr = defaultTimeStr + ":" + fixedSec;
			}
		}
		timeStr = timeStr + ":" + fixedMilSec;

		String concatValue = dateStr + " " + timeStr;
		DateTimeFormat toValueFormatter = DateTimeFormat.getFormat("yyyy/MM/dd HH:mm:ss:SSS");

		return toValueFormatter.parseStrict(concatValue);
	}

	/**
	 * DateItemを生成します。
	 * LocaleはTenantInfoから取得します。
	 * 現状はja以外はenに合わせて出力します。
	 *
	 * @return DateItem
	 */
	public static DateItem createDateItem() {
		return createDateItem(TenantInfoHolder.getInputLocale());
	}

	/**
	 * DateItemを生成します。
	 * 指定されたLocaleに合わせたFormatを指定します。
	 * 現状はja以外はenに合わせて出力します。
	 *
	 * @param locale
	 * @return DateItem
	 */
	public static DateItem createDateItem(String locale) {
		DateItem dateField = new DateItem();
		dateField.setUseTextField(true);
		if (locale == null || locale.equalsIgnoreCase("ja_JP")) {
			dateField.setDateFormatter(DateDisplayFormat.TOJAPANSHORTDATE);
			dateField.setTooltip("YYYY/MM/DD");
		} else {
			dateField.setDateFormatter(DateDisplayFormat.TOUSSHORTDATE);
			dateField.setTooltip("MM/DD/YYYY");
			//dateField.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATE);
			//dateField.setTooltip("DD/MM/YYYY");
		}
		dateField.setWidth(100);

		return dateField;
	}

	/**
	 * DateTimeItemを生成します。
	 * LocaleはTenantInfoから取得します。
	 * 現状はja以外はenに合わせて出力します。
	 *
	 * @return DateTimeItem
	 */
	public static DateTimeItem createDateTimeItem() {
		return createDateTimeItem(TenantInfoHolder.getInputLocale());
	}

	/**
	 * DateTimeItemを生成します。
	 * 指定されたLocaleに合わせたFormatを指定します。
	 * 現状はja以外はenに合わせて出力します。
	 *
	 * @param locale
	 * @return DateTimeItem
	 */
	public static DateTimeItem createDateTimeItem(String locale) {
		DateTimeItem dateTimeField = new DateTimeItem();
		dateTimeField.setUseTextField(true);
		dateTimeField.setDateFormatter(DateDisplayFormat.TOJAPANSHORTDATETIME);
		if (locale == null || locale.equalsIgnoreCase("ja_JP")) {
			//現状HH:MM:SSを表示するFormatがないのでHH:MMまで
			dateTimeField.setDateFormatter(DateDisplayFormat.TOJAPANSHORTDATETIME);
			dateTimeField.setTooltip("YYYY/MM/DD HH:MM");
		} else {
			//現状HH:MM:SSを表示するFormatがないのでHH:MMまで
			dateTimeField.setDateFormatter(DateDisplayFormat.TOUSSHORTDATETIME);
			dateTimeField.setTooltip("MM/DD/YYYY HH:MM");
			//dateTimeField.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
			//dateTimeField.setTooltip("DD/MM/YYYY HH:MM");
		}
		dateTimeField.setWidth(120);

		return dateTimeField;
	}

	/**
	 * TimeItemを生成します。
	 *
	 * @param showSecond 秒を表示するか
	 * @return TimeItem
	 */
	public static TimeItem createTimeItem(boolean showSecond) {
		TimeItem timeField = new TimeItem();
		//timeField.setUseTextField(false);	//サポートされていない
		if (showSecond) {
			timeField.setTimeFormatter(TimeDisplayFormat.TOPADDED24HOURTIME);
			timeField.setTooltip("HH:MM:SS");
			timeField.setWidth(70);
		} else {
			timeField.setTimeFormatter(TimeDisplayFormat.TOSHORTPADDED24HOURTIME);
			timeField.setTooltip("HH:MM");
			timeField.setWidth(50);
		}
		timeField.setShowHint(false);

		return timeField;
	}


	public static DateTimeFormat createInputDateFormat() {
		return getInputLocale(TenantInfoHolder.getInputLocale());
	}

	public static DateTimeFormat getInputLocale(String locale) {
		if (locale == null || locale.equalsIgnoreCase("ja_JP")) {
			return DateTimeFormat.getFormat("yyyy/MM/dd");
		} else {
			return DateTimeFormat.getFormat("MM/dd/yyyy");
		}
	}

	/**
	 * Timestamp値をフォーマットします。
	 *
	 * @param timestamp 値
	 * @return フォーマット値
	 */
	public static String formatTimestamp(Timestamp timestamp) {
		if (timestamp == null) {
			return "";
		}
		return TenantInfoHolder.getOutputDateTimeSecFormat().format(timestamp, TenantInfoHolder.getTimeZone());
	}

	/**
	 * Date値をフォーマットします。
	 *
	 * @param date 値
	 * @return フォーマット値
	 */
	public static String formatDate(Date date) {
		if (date == null) {
			return "";
		}
		return TenantInfoHolder.getOutputDateFormat().format(date);
	}

	/**
	 * Time値をフォーマットします。
	 *
	 * @param time 値
	 * @return フォーマット値
	 */
	public static String formatTime(Time time) {
		if (time == null) {
			return "";
		}
		return TenantInfoHolder.getOutputTimeSecFormat().format(time);
	}

	/**
	 * 文字列が空(null または empty)かを判定します。
	 *
	 * @param value 値
	 * @return 判定値
	 */
	public static boolean isEmpty(String value) {
		return value == null || value.isEmpty();
	}

	/**
	 * 文字列が空(null または empty)ではないかを判定します。
	 *
	 * @param value 値
	 * @return 判定値
	 */
	public static boolean isNotEmpty(String value) {
		return value != null && !value.isEmpty();
	}

	/**
	 * Collectionが空(null または empty)かを判定します。
	 *
	 * @param collection 値
	 * @return 判定値
	 */
	public static boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

	/**
	 * Collectionが空(null または empty)ではないかを判定します。
	 *
	 * @param collection 値
	 * @return 判定値
	 */
	public static boolean isNotEmpty(Collection<?> collection) {
		return collection != null && !collection.isEmpty();
	}

	private static final int COOKIE_EXPIRE_DAYS = 30;
	private static final long MILLISECS_PER_DAY = 1000L * 60L * 60L * 24L;

	public static String getCookie(String name) {
		String value = Cookies.getCookie(name);
		if (isEmpty(value)) {
			return "";
		}
		return value;
	}

	public static void setCookie(String name, String value) {
		setCookie(name, value, COOKIE_EXPIRE_DAYS);
	}

	public static void setCookie(String name, String value, int expireDays) {
		try {
			if (isEmpty(value)) {
				Cookies.removeCookie(name);
				return;
			}

			Date d = new Date();
			d.setTime(d.getTime() + MILLISECS_PER_DAY * expireDays);
			Cookies.setCookie(name, value, d);
		} catch (Throwable e) {
			GWT.log("cookie save failed.", e);
		}
	}

	public static CanvasItem createButtonTitleItem(ButtonItem buttonItem) {
		CanvasItem buttonFormItem = new CanvasItem();

		//Button用のForm
		DynamicForm buttonForm = new DynamicForm();
		buttonForm.setNumCols(2);	//間延びしないように最後に１つ余分に作成
		buttonForm.setColWidths(100, "*");

		buttonItem.setStartRow(false);

		buttonForm.setItems(buttonItem);

		HLayout buttonPane = new HLayout();
		buttonPane.setAutoHeight();
		buttonPane.addMember(buttonForm);
		buttonFormItem.setCanvas(buttonPane);

		return buttonFormItem;
	}
}
