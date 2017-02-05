package com.lvoutcity.core.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Rest operate
 * 
 * @author gdh
 *
 */
public class Restful {

	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface RequestMethod {
		Method[] value() default { Method.Get };
	}

	public enum Method {
		Get, Post
	}
}
