/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.beanvalidation.constraints;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import org.iplass.mtp.impl.beanvalidation.ValidEntityValidator;

/**
 * <p>
 * Entityに定義されるvalidation定義に従った検証を行います。
 * </p>
 * <p>
 * propertiesを指定することにより、検証対象プロパティを指定可能です。
 * properties未指定の場合は、ネストされたEntity含め、すべてのプロパティの検証を行います。
 * </p>
 * <p>
 * 例：
 * <pre>
 * public class SampleBean {
 * 	private User user;
 * 
 * 	&#064;ValidEntity(properties={"accountId", "rank.*", "groups.**"})
 * 	public User getUser() {
 * 		return user;
 * 	}
 * 	public void setUser(User user) {
 * 		this.user = user;
 * 	}
 * 	
 * 	:
 * 	:
 * }
 * </pre>
 * 
 * </p>
 * 
 * @author K.Higuchi
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {ValidEntityValidator.class})
public @interface ValidEntity {
	String message() default "";
	Class<?>[] groups() default { };
	Class<? extends Payload>[] payload() default { };
	
	/**
	 * <p>検証対象のプロパティを指定します。</p>
	 * <p>
	 * *指定は、当該のEntityのプロパティすべてをあらわします。<br>
	 * **指定は、当該のEntity以下、ネストされたEntityのプロパティも含むぷべてのプロパティをあらわします。
	 * </p>
	 * <h5>例(Userエンティティが検証対象とした場合)</h5>
	 * <code>name</code> -&gt; nameを検証対象とします<br>
	 * <code>*</code> -&gt; Userエンティティのすべてのプロパティを検証対象とします<br>
	 * <code>rank.* -&gt; Userエンティティのrankで指し示されるRankエンティティのすべてのプロパティを検証対象とします</code><br>
	 * <code>groups.** -&gt; Userエンティティのgroupsで指し示されるGroupエンティティのすべてのプロパティおよびネストされたEntity参照を検証対象とします</code><br>
	 * 
	 * @return
	 */
	String[] properties() default { };
	
	@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
	@Retention(RUNTIME)
	@Documented
	@interface List {
		ValidEntity[] value();
	}

}
