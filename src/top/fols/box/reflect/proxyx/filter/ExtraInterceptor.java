package top.fols.box.reflect.proxyx.filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * All methods that match the name are executed by the method annotated by this interceptor
 * Only those without the executor annotation can be intercepted
 *
 * 只能拦截没有注解 AnnotationExecutor注解 的
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ExtraInterceptor {
    String namePattern() default "";

    int interceptOrder() default 0; //default order is 0, represents no sorting
}
