package top.fols.box.reflect.proxyx.helper;

public @interface IInvokeClass {
	String value() default "";
	Class  valueClass();
}
