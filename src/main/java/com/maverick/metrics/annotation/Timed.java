package com.maverick.metrics.annotation;

import java.lang.annotation.*;

/**
 * Annotate the method you want to get metrics for.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Timed {
	/**
	 * Define a metric name. The name will appended to "metrics"
	 */
	String name() default "";
}
