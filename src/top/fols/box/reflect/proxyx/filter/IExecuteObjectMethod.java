package top.fols.box.reflect.proxyx.filter;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import top.fols.box.reflect.proxyx.ProxyFactory;
import top.fols.box.reflect.proxyx.ProxyInvocationHandler;
import top.fols.box.reflect.proxyx.ProxyReflecter;
import top.fols.box.reflect.proxyx.ProxyReturn;

@SuppressWarnings({"UnnecessaryModifier", "rawtypes", "unused"})
@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD, ElementType.TYPE})
public @interface IExecuteObjectMethod {
	int order() default 0;

    public static final Class<IExecuteObjectMethod> TYPE = IExecuteObjectMethod.class;
    public static final ProxyFilterImpl.AnnotationExecutor<IExecuteObjectMethod> EXECUTOR = new ProxyFilterImpl.AnnotationExecutor<IExecuteObjectMethod>() {
        Integer orderCache = null;
        @Override
        public Integer order(IExecuteObjectMethod annotation) {
            // TODO: Implement this method
            if (null != orderCache && orderCache == annotation.order())
                return  orderCache;
            return orderCache = annotation.order();
        }

        @Override
        public IExecuteObjectMethod cloneAnnotation(final IExecuteObjectMethod annotation) {
			final Integer order = annotation.order();
            final int hashCode = annotation.hashCode();
            return new IExecuteObjectMethod() {

				@Override
				public int order() {
					// TODO: Implement this method
					return order;
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
                    if (!(obj instanceof IExecuteObjectMethod))
                        return false;
                    IExecuteObjectMethod other = (IExecuteObjectMethod) obj;
                    return true;
                }

                @Override
                public String toString() {
                    return "@" + TYPE.getName() + "(" + "order=" + order() + ")";
                }
            };
        }

        @Override
        public void execute(ProxyInvocationHandler handler, ProxyFilterImpl filter, ProxyReflecter reflecter,
							IExecuteObjectMethod annotation, Method interceptMethod,

							Class invokeClass, Object invokeObject,
							Method annotationClassMethod,

							Object[] args,
							ProxyReturn result) throws Throwable {
            // TODO: Implement this method
//			Class  returnType 	= annotationClassMethod.getReturnType();
			String name 		= annotationClassMethod.getName();
//			Class[] paramTypes  = annotationClassMethod.getParameterTypes();

//			System.out.println("annotationClassMethod: " + annotationClassMethod);
//			System.out.println("runMethod: " + runMethod);


			Method method = reflecter.requireMethod(handler.getInvokeObjectClass(), name, args);
            Object original = method.invoke(invokeObject, args);
            result.setReturn(original);

//          Method objectEqualsMethod = reflecter.requireMethod(invokeClass, name, args);
//			System.out.println(objectEqualsMethod);
//
//          Object original = objectEqualsMethod.invoke(invokeObject, args);
//          result.setReturn(original);
        }
    };
}
