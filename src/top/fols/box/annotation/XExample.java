package top.fols.box.annotation;


public abstract @interface XExample {
	public String e() default "";
	public String v() default "";
	
	XExample[] value();
}
