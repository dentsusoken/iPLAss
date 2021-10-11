package org.iplass.mtp.view.generic.editor;

import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.adminconsole.view.annotation.Refrectable;
import org.iplass.adminconsole.view.annotation.generic.EntityViewField;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;

/**
 * 日付・時間型のフォーマットの多言語設定のプロパティ
 * @author ISID Shojima
 */
public class LocalizedDateTimeFormatSetting implements Refrectable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -7784224319600780734L;

	/** 日付/時刻のフォーマットの多言語設定の言語ロケール */
	@MetaFieldInfo(
			displayName="日付/時刻の言語設定",
			displayNameKey="generic_editor_LocalizedDateTimeFormatSetting_langageDisplaNameKey",
			description="日付/時刻のフォーマットの言語を設定する。",
			required=true,
			inputType=InputType.TEXT,
			displayOrder=100,
			descriptionKey="generic_editor_LocalizedDateTimeFormatSetting_langageDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.ALL}
	)
	private String langage;

	/** 日付/時刻のフォーマットの多言語設定のフォーマット */
	@MetaFieldInfo(
			displayName="日付/時刻のフォーマット設定",
			displayNameKey="generic_editor_LocalizedDateTimeFormatSetting_dateTimeFormatDisplaNameKey",
			description="多言語設定の日付/時刻のフォーマットを設定する。",
			required=true,
			inputType=InputType.TEXT,
			displayOrder=110,
			descriptionKey="generic_editor_LocalizedDateTimeFormatSetting_dateTimeDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.ALL}
	)
	private String dateTimeFormat;

	/** 日付/時刻のフォーマットの多言語設定のロケール */
	@MetaFieldInfo(
			displayName="日付/時刻のロケール設定",
			displayNameKey="generic_editor_LocalizedDateTimeFormatSetting_dateTimeFormatLocaleDisplaNameKey",
			description="多言語設定の日付/時刻のロケールを設定する。",
			required=true,
			inputType=InputType.TEXT,
			displayOrder=120,
			descriptionKey="generic_editor_LocalizedDateTimeFormatSetting_dateTimeFormatDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.ALL}
	)
	private String dateTimeFormatLocale;

	/**
	 * 日付/時刻の言語を取得します。
	 * @return locale 言語
	 */
	public String getLangage() {
		return langage;
	}

	/**
	 * 日付/時刻の言語を設定します。
	 * @param locale 言語
	 */
	public void setLangage(String langage) {
		this.langage = langage;
	}

	/**
	 * 日付/時刻のフォーマットを取得します。
	 * @return datetimeFormat フォーマット
	 */
	public String getDateTimeFormat() {
		return dateTimeFormat;
	}

	/**
	 * 日付/時刻のフォーマットを設定します。
	 * @param datetimeFormat フォーマット
	 */
	public void setDateTimeFormat(String dateTimeFormat) {
		this.dateTimeFormat = dateTimeFormat;
	}

	/**
	 * 日付/時刻のロケールを取得します。
	 * @return datetimeFormat ロケール
	 */
	public String getDateTimeFormatLocale() {
		return dateTimeFormatLocale;
	}

	/**
	 * 日付/時刻のフォーマットを設定します。
	 * @param datetimeLocale　ロケール
	 */
	public void setDateTimeFormatLocale(String dateTimeFormatLocale) {
		this.dateTimeFormatLocale = dateTimeFormatLocale;
	}

}
