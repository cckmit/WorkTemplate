package com.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Page
{
	public boolean tool() default true;

	public boolean search() default true;
	
	public boolean exportSinglePage() default true;
	
	public boolean isImport() default false;
	
}
