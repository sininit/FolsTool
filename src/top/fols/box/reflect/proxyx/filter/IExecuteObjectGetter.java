package top.fols.box.reflect.proxyx.filter;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

import top.fols.box.reflect.ClassProperties;
import top.fols.box.reflect.proxyx.ProxyInvocationHandler;
import top.fols.box.reflect.proxyx.ProxyReflecter;
import top.fols.box.reflect.proxyx.ProxyReturn;
import top.fols.atri.lang.Finals;

@SuppressWarnings({"UnnecessaryModifier", "rawtypes", "unused"})
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface IExecuteObjectGetter {
    String value()     default "";
    Class returnType() default void.class;

	int order() default 0;

    public static final Class<IExecuteObjectGetter> TYPE = IExecuteObjectGetter.class;
    public static final ProxyFilterImpl.AnnotationExecutor<IExecuteObjectGetter> EXECUTOR = new ProxyFilterImpl.AnnotationExecutor<IExecuteObjectGetter>() {
        Integer orderCache = null;
        @Override
        public Integer order(IExecuteObjectGetter annotation) {
            // TODO: Implement this method
            if (null != orderCache && orderCache == annotation.order())
                return  orderCache;
            return orderCache = annotation.order();
        }

        @Override
        public IExecuteObjectGetter cloneAnnotation(IExecuteObjectGetter annotation) {
            Class type0 = null;
            try { type0 = annotation.returnType(); type0 = type0 == Finals.VOID_CLASS ?null: type0; } catch (Throwable ignored) {}
            String value0 = null;
            try {  value0 = annotation.value();} catch (Throwable ignored) {}

			final Integer order = annotation.order();
            final int hashCode = annotation.hashCode();
            final Class type = type0;
            final String value = value0;
            return new IExecuteObjectGetter() {

				@Override
				public int order() {
					// TODO: Implement this method
					return order;
				}

                @Override
                public String value() {
                    return value;
                }

                @Override
                public Class returnType() {
                    return type;
                }

                @Override
                public Class<? extends Annotation> annotationType() {
                    return TYPE;
                }


                @Override
                public int hashCode() {
                    return hashCode;
                }
                @Override
                public boolean equals(Object obj) {
                    if (obj == this)
                        return true;
                    if (!(obj instanceof IExecuteObjectGetter))
                        return false;
                    IExecuteObjectGetter other = (IExecuteObjectGetter) obj;
                    return Objects.equals(type, other.returnType()) && Objects.equals(value, other.value());
                }
                @Override
                public String toString() {
                    return "@" + TYPE.getName() + "(" + "tip=" + value + ", type=" + type + ", order=" + order() + ")";
                }
            };
        }

        @Override
        public void execute(ProxyInvocationHandler handler, ProxyFilterImpl filter, ProxyReflecter reflecter,
							IExecuteObjectGetter annotation, Method interceptMethod,

							Class invokeClass, Object invokeObject,
							Method annotationClassMethod,

							Object[] args,
							ProxyReturn result) throws Throwable {
            // TODO: Implement this method

            String fieldName = annotation.value();
			if (null == fieldName || fieldName.length() == 0) {
				fieldName = ClassProperties.toGetterOrSetterAsSimpleFieldName(annotationClassMethod);
			}

            Class type = null;
            try { type = annotation.returnType(); type = type == Finals.VOID_CLASS ? null: type; } catch (Throwable ignored) {}

            Field find = reflecter.requireField(invokeClass, type, fieldName);
            result.setReturn(find.get(invokeObject));
        }
    };
}
