package top.fols.box.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
@Target(
	value={
		ElementType.ANNOTATION_TYPE,
		ElementType.CONSTRUCTOR,
		ElementType.FIELD,
		ElementType.LOCAL_VARIABLE,
		ElementType.METHOD,
		ElementType.PACKAGE,
		ElementType.PARAMETER,
		ElementType.TYPE
	}
)

@Retention(value=RetentionPolicy.SOURCE)
public abstract @interface XAnnotations {
	String value() default "";
}
