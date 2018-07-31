package net.globulus.easyprefs.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by gordanglavas on 29/09/16.
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface Pref {
    String key() default "";
    boolean clear() default true;
    boolean nullable() default false;
    String autoset() default "";
    Class<? extends PrefFunction> function() default PrefFunctionStub.class;
    String rawDefaultValue() default "";
    String comment() default "";
    String oldKey() default "";
}
