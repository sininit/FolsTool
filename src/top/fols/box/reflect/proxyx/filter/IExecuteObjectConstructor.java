package top.fols.box.reflect.proxyx.filter;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import top.fols.box.reflect.proxyx.ProxyFactory;
import top.fols.box.reflect.proxyx.ProxyInvocationHandler;
import top.fols.box.reflect.proxyx.ProxyReflecter;
import top.fols.box.reflect.proxyx.ProxyReturn;

@SuppressWarnings({"UnnecessaryModifier", "rawtypes", "unused"})
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface IExecuteObjectConstructor {
//    Class[]  params() default {};



	int order() default 0;

    public static final Class<IExecuteObjectConstructor> TYPE = IExecuteObjectConstructor.class;
    public static final ProxyFilterImpl.AnnotationExecutor<IExecuteObjectConstructor> EXECUTOR = new ProxyFilterImpl.AnnotationExecutor<IExecuteObjectConstructor>() {
        Integer orderCache = null;
        @Override
        public Integer order(IExecuteObjectConstructor annotation) {
            // TODO: Implement this method
            if (null != orderCache && orderCache == annotation.order())
                return  orderCache;
            return orderCache = annotation.order();
        }

        @Override
        public IExecuteObjectConstructor cloneAnnotation(final IExecuteObjectConstructor annotation) {
//            Class[] params0 = null;
//            try {   params0 = annotation.params().clone(); } catch (Throwable ignored) {}

			final Integer order = annotation.order();
            final int hashCode = annotation.hashCode();
//            final Class[] value = params0;
            return new IExecuteObjectConstructor() {

				@Override
				public int order() {
					// TODO: Implement this method
					return order;
				}

//                @Override
//                public Class[] params() {
//                    return value;
//                }

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
                    if (!(obj instanceof IExecuteObjectConstructor))
                        return false;
                    IExecuteObjectConstructor other = (IExecuteObjectConstructor) obj;
//                    return Arrays.equals(params(), other.params());
					return true;
                }
                @Override
                public String toString() {
//                    return "@" + TYPE.getName() + "(" + "params=" + Arrays.toString(params()) + ", order=" + order() + ")";
					
					return "@" + TYPE.getName() + "(" + "order=" + order() + ")";
                }
            };
        }

        @Override
        public void execute(ProxyInvocationHandler handler, ProxyFilterImpl filter, ProxyReflecter reflecter,
							IExecuteObjectConstructor annotation, Method interceptMethod,
							
							Class invokeClass, Object invokeObject,
							Method annotationClassMethod,
							
							Object[] args,
							ProxyReturn result) throws Throwable {
            // TODO: Implement this method
//            Class[] paramTypes = annotation.params();
			
            Constructor constructor = reflecter.requireConstructor(invokeClass, args);
            result.setReturn(constructor.newInstance(args));
        }
    };
}
