package com.cc.manager.common.utils;

import java.lang.annotation.*;
/**
 *
 * @author cf
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Comments
{
    String name() default "";

    String option() default "";

    String formatter() default "";

    String editor() default "";
}
