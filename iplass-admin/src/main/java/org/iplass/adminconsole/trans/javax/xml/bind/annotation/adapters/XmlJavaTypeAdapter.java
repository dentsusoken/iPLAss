package javax.xml.bind.annotation.adapters;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

//XmlJavaTypeAdapter representation for GWT
@Retention(RUNTIME)
@Target({PACKAGE,FIELD,METHOD,TYPE,PARAMETER})
public @interface XmlJavaTypeAdapter {
	Class<? extends XmlAdapter> value();
	Class type() default DEFAULT.class;

	static final class DEFAULT {}
}
