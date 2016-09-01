package com.zy.common.extend;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.RoundingMode;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BigDecimalBinder {

	String value() default "";

	int fraction() default 2;
	
	RoundingMode roundingMode() default RoundingMode.HALF_UP;

}