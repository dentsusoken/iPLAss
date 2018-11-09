package org.iplass.gem.command.generic.detail;

import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.element.property.PropertyBase;

/**
 * プロパティ判断処理ハンドラ
 */
public interface RegistrationPropertyBaseHandler<T extends PropertyBase> {

	/**
	 * プロパティが表示対象になるか
	 * @param property プロパティ
	 * @return プロパティが表示対象になるか
	 */
	public boolean isDispProperty(T property);

	/**
	 * プロパティ編集するためのエディタを取得します。
	 * @param property プロパティ
	 * @return プロパティ編集するためのエディタ
	 */
	public PropertyEditor getEditor(T property);
}
