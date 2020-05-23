package com.cc.manager.common.utils;

import java.lang.annotation.*;

/**
 * 不导出Excel的列
 *
 * @author cf
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NonExcel
{

}
