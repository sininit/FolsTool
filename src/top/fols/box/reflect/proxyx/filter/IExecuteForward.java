package top.fols.box.reflect.proxyx.filter;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import top.fols.atri.lang.Finals;
import top.fols.box.reflect.proxyx.ProxyFactory;
import top.fols.box.reflect.proxyx.ProxyInvocationHandler;
import top.fols.box.reflect.proxyx.ProxyReflecter;
import top.fols.box.reflect.proxyx.ProxyReturn;

@SuppressWarnings({"UnnecessaryModifier", "rawtypes", "unused"})
@Retention(value= RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD, ElementType.TYPE})
public @interface IExecuteForward {
    Class<? extends ForwardReceiver>  executer() ;
	int order() default 0;

    public static final Class<IExecuteForward> TYPE = IExecuteForward.class;
    public static final ProxyFilterImpl.AnnotationExecutor<IExecuteForward> EXECUTOR = new ProxyFilterImpl.AnnotationExecutor<IExecuteForward>() {
        Integer orderCache = null;
        @Override
        public Integer order(IExecuteForward annotation) {
            // TODO: Implement this method
            if (null != orderCache && orderCache == annotation.order())
                return  orderCache;
            return orderCache = annotation.order();
        }

        @Override
        public IExecuteForward cloneAnnotation(IExecuteForward annotation) {
            Class<? extends ForwardReceiver> cls0 = null;
            try { cls0 = annotation.executer();} catch (Throwable ignored) {}
//            String name0 = null;
//            try {  name0 = annotation.name();} catch (Throwable ignored) {}

			final Integer order = annotation.order();
            final Class<? extends ForwardReceiver> type = cls0;
//            final String value = name0;
            return new IExecuteForward() {
				@Override
				public int order() {
					// TODO: Implement this method
					return order;
				}

//                @Override
//                public String name() {
//                    return value;
//                }

                @Override
                public Class<? extends ForwardReceiver> executer() {
                    return type;
                }

                @Override
                public Class<? extends Annotation> annotationType() {
                    return TYPE;
                }

                @Override
                public String toString() {
                    return "@" + TYPE.getName() 
						+ "(" 
						+ "cls=" + executer()
						//+ ", name=" + name()
						+ ", order=" + order() 
						+ ")";
                }
            };
        }


        @Override
        public void execute(ProxyInvocationHandler handler, ProxyFilterImpl filter, ProxyReflecter reflecter,
							IExecuteForward annotation, Method interceptMethod,

							Class invokeClass, Object invokeObject,
							Method annotationClassMethod,

							Object[] args,
							ProxyReturn result) throws Throwable {
            // TODO: Implement this method
            Class<? extends ForwardReceiver> cls;
            try { cls = annotation.executer(); } catch (Throwable e) {
                throw new RuntimeException("annotation must not empty class: " + annotation + " " + annotation.annotationType());
            }

			ProxyFactory factory = handler.factory();
			ForwardManager er = factory.getProperty(ForwardManager.CLASS_FPRWARD_MANAGER);

			Parameter data = new Parameter(handler, filter, reflecter,
										   annotation, interceptMethod,

										   invokeClass, invokeObject, 
										   annotationClassMethod, 

										   args, result);

//            String name = annotation.name();

			ForwardReceiver forward = er.findReceiver(cls, /*name, */ data);

			forward.before(data);
			if (data.isReturn()) {
				return;
			}
			data.setReturn(data.invokeMatchInvokeObjectClassMethod());

			forward.after(data);
        }
    };


	@SuppressWarnings("UnnecessaryModifier")
	public static class Parameter<T extends Annotation> {
		ProxyInvocationHandler handler;
		ProxyFilterImpl    	   filter;
		ProxyReflecter 		   reflecter;

		T annotation;
		Method          interceptToMethod;

		Class           invokeObjectClass;
		Object          invokeObject;
		Method          annotationInterfaceMethod;

		Object[]        args;
		ProxyReturn     result;

		public Parameter(ProxyInvocationHandler handler, ProxyFilterImpl filter, ProxyReflecter reflecter,
						 T annotation, Method interceptMethod,

						 Class invokeClass, Object invokeObject,
						 Method annotationClassMethod,

						 Object[] args,
						 ProxyReturn result) {
			this.handler   = handler;
			this.filter    = filter;
			this.reflecter = reflecter; 

			this.annotation = annotation;
			this.interceptToMethod   = interceptMethod;

			this.invokeObjectClass = invokeClass;
			this.invokeObject      = invokeObject;
			this.annotationInterfaceMethod = annotationClassMethod;

			this.args = null == args ? Finals.EMPTY_OBJECT_ARRAY: args;
			this.result = result;
		}

		public ProxyInvocationHandler handler()   { return handler; }
		public ProxyReflecter 		  reflecter() { return reflecter;}

		public T getAnnotation() {
			return annotation;
		}

		public Class  getInvokeObjectClass() { return invokeObjectClass;}
		public Object getInvokeObject() 	 { return invokeObject; }


		public String getMethodName() { return annotationInterfaceMethod.getName(); }

		public Method getFinalPositioningMethod() {
			return interceptToMethod;
		}
		public boolean isFinalPositioningMethodVoid() {
			return interceptToMethod.getReturnType() == Finals.VOID_CLASS;
		}
		public Object  invokeFinalPositioningMethod() throws InvocationTargetException, IllegalAccessException {
			return interceptToMethod.invoke(invokeObject, args);
		}

		public Method  getAnnotationInterfaceMethod()    {
			return annotationInterfaceMethod;
		}
		public boolean isAnnotationInterfaceMethodVoid() {
			return annotationInterfaceMethod.getReturnType() == Finals.VOID_CLASS;
		}
		public Object  invokeObjectFromAnnotationInterfaceMethod() throws InvocationTargetException, IllegalAccessException {
			return annotationInterfaceMethod.invoke(invokeObject, args);
		}

		public Method matchInvokeObjectClassMethod() {
			return reflecter.requireMethod(handler.getInvokeObjectClass(), this.annotationInterfaceMethod.getName(), args);
		}
		public boolean isMatchInvokeObjectClassMethodVoid() {
			Method objectEqualsMethod = this.matchInvokeObjectClassMethod();
			return objectEqualsMethod.getReturnType() == Finals.VOID_CLASS;
		}
		public Object invokeMatchInvokeObjectClassMethod() throws InvocationTargetException, IllegalAccessException {
			Method objectEqualsMethod = this.matchInvokeObjectClassMethod();
			return objectEqualsMethod.invoke(invokeObject, args);
		}



		public Object[] getArguments() {
			return args;
		}


		public void setReturn(Object value) {
			result.setReturn(value);
		}
		public Object getReturn() {
			return result.getReturn();
		}
		public boolean isReturn() {
			return result.isReturn();
		}
	}

	public static abstract class ForwardReceiver {
		protected abstract void before(Parameter param) throws Throwable;
		protected abstract void after(Parameter param) throws Throwable;
	}
	public static abstract class ForwardReceiverAsReplace extends ForwardReceiver {
		protected final void before(Parameter param) throws Throwable {
			param.setReturn(execute(param));
		}
		protected final void after(Parameter param) throws Throwable {}


		protected abstract Object execute(Parameter param) throws Throwable;
	}


	public static class ForwardManager {
		static final public Class<ForwardManager>  CLASS_FPRWARD_MANAGER = ForwardManager.class;
		static final 		Class<ForwardReceiver> CLASS_FORWARD_RECEIVER  = ForwardReceiver.class;

//		public ForwardReceiver findReceiverFromClassMethodNameConvert(Class forwardTo, String name, Parameter data) {
//			final Object[] params = { data };
//			final Method find     = data.reflecter.method(forwardTo, Finals.VOID_CLASS, name, params);
//			if (Modifier.isStatic(find.getModifiers())) {
//				//noinspection unchecked
//				return new ForwardReceiver() {
//					@Override
//					protected void before(Parameter param) throws Throwable {
//						// TODO: Implement this method
//						find.invoke(null, param);
//					}
//					@Override
//					protected void after(Parameter param) throws Throwable {
//						// TODO: Implement this method
//					}
//				};
//			}
//			return null;
//		}
		public ForwardReceiver findReceiver(Class<? extends ForwardReceiver> forwardTo, /*String name, */Parameter data) {
//			boolean hasName = !(null == name || name.length() == 0);
			if (null == forwardTo) {
//				if (hasName) {
//				} else {
//					throw new RuntimeException("name='', but the class found does not belong to forward: " + forwardTo);
//				}
			} else {
//				if (hasName) {
//					throw new RuntimeException("name='', but the class found does not belong to forward: " + forwardTo);
//				}
				if (CLASS_FORWARD_RECEIVER.isAssignableFrom(forwardTo)) {
					ForwardReceiver forward = data.reflecter.newInstance(forwardTo);
					return forward;
				} 

//				ForwardReceiver exactHook = findReceiverFromClassMethodNameConvert(forwardTo, name, data);
//				if (null != exactHook)
//					return  exactHook;
//
//				Class[]   paramTypes = { Parameter.class };
//				throw new RuntimeException(ReflectMatcher.buildNoSuchMatch(forwardTo, Reflects.methods(forwardTo), Finals.VOID_CLASS, "static " +  name, paramTypes));
			}
			throw new RuntimeException("unsupported: " + 
									   Arrays.asList(data.annotation, 
													 "from=" + data.annotationInterfaceMethod, 
													 "intercept=" + data.interceptToMethod) +
									   ", matchs: " + forwardTo + " is not instanceof " + CLASS_FORWARD_RECEIVER);
		}



		ForwardManager() {}
	}


}
