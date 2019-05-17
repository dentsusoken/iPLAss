package org.iplass.adminconsole.view.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * メタデータ編集用の画面にて 表示順を手動で設定するかを指定します。
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FieldOrder {

	/**
	 * 手動でソートするか
	 */
	boolean manual() default false;

}
