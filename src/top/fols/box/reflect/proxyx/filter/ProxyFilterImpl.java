package top.fols.box.reflect.proxyx.filter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;
import top.fols.atri.assist.json.JSONObject;
import top.fols.atri.lang.Arrayz;
import top.fols.atri.lang.Objects;
import top.fols.atri.reflect.Reflects;
import top.fols.box.reflect.proxyx.*;

public class ProxyFilterImpl implements ProxyFilter {
	private ProxyFactory vFactory;
	public ProxyFilterImpl(ProxyFactory vFactory) {
		this.vFactory = vFactory;
	}

	public ProxyFactory factory() { return vFactory; }
	protected AnnotationInterfaceMapping newAnnotationInterfaceMapping(Class clazz) { return new AnnotationInterfaceMapping(clazz); }




	@SuppressWarnings("UnnecessaryModifier")
	public static interface AnnotationExecutor<T extends Annotation> {
		public T cloneAnnotation(T annotation);
		public Integer order(T annotation);

		public void  execute(ProxyInvocationHandler handler, ProxyFilterImpl filter, ProxyReflecter reflecter,
							 T annotation, Method runMethod,

							 Class invokeClass, Object invokeObject,
							 Method annotationClassMethod,

							 Object[] args,
							 ProxyReturn result) throws Throwable;
	}

	protected AnnotationExecutor parseAnnotationExecutorValue(Class<? extends Annotation> annotationClass) {
		Field[] fields = annotationClass.getFields();
		for (Field field : fields) {
			if (field.getType() == AnnotationExecutor.class && Modifier.isStatic(field.getModifiers())) {
				try {
					return (AnnotationExecutor) field.get(null);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return null;
	}



	//<Class, One-AnnotationExecutor>
	Map<Class, AnnotationExecutor[]> annotationExecutorHashMap = new HashMap<>();
	public AnnotationExecutor findAnnotationExecutor(Class<? extends Annotation> annotationClass) {
		AnnotationExecutor[] annotationExecutor = annotationExecutorHashMap.get(annotationClass);
		if (null == annotationExecutor) {
			AnnotationExecutor executor = parseAnnotationExecutorValue(annotationClass);
			if (null == executor) {
				annotationExecutorHashMap.put(annotationClass, annotationExecutor = new AnnotationExecutor[]{});//put null
			} else {
				annotationExecutorHashMap.put(annotationClass, annotationExecutor = new AnnotationExecutor[]{executor});
			}
		}
		return Objects.first(annotationExecutor);
	}
	public void addAnnotationExecutor(Class<Annotation> annotationClass, AnnotationExecutor executor) {
		annotationExecutorHashMap.put(annotationClass, new AnnotationExecutor[]{executor});
	}
	public void removeAnnotationExecutor(Class<Annotation> annotationClass, AnnotationExecutor executor) {
		annotationExecutorHashMap.remove(annotationClass);
	}
	public boolean hasAnnotationExecutor(Class<Annotation> annotationClass) {
		return annotationExecutorHashMap.containsKey(annotationClass);
	}
	
	
	public Method[] getAnnotationInterfaceMethods(Class myInterface) {
		Method[] methods = Reflects.methods(myInterface);
		return   methods;
	}





	final Map<Class, AnnotationInterfaceMapping> annotationInterfaceMappingMap = new IdentityHashMap<>();
	public AnnotationInterfaceMapping getAnnotationInterfaceMapping(Class clazz) {
		AnnotationInterfaceMapping mapping = annotationInterfaceMappingMap.get(clazz);
		if (null == mapping) {
			synchronized (annotationInterfaceMappingMap) {
				annotationInterfaceMappingMap.put(clazz, mapping = createAnnotationInterfaceMapping(this, clazz));
			}
		}
		return mapping;
	}
	AnnotationInterfaceMapping createAnnotationInterfaceMapping(ProxyFilterImpl proxyx, Class annotationInterface0) {
		//interface all method is public
		Class annotationInterface = factory().toAnnotationInterface(annotationInterface0);
		if (((annotationInterface != null))) {
			AnnotationInterfaceMapping mapping = newAnnotationInterfaceMapping(annotationInterface);
			Method[] methods = getAnnotationInterfaceMethods(annotationInterface);
			if (null != methods && methods.length > 0) {
				TreeMap<Integer, List<ExtraInterceptor>> treeMap = new TreeMap<Integer, List<ExtraInterceptor>>(new Comparator<Integer>() {
						@Override
						public int compare(Integer x, Integer y) {
							return (x < y) ? -1 : ((x.intValue() == y.intValue()) ? 0 : 1);
						}
					});
				Map<ExtraInterceptor, Method> extraInterceptorMap = new HashMap<>();
				for (Method method : methods) {
					ExtraInterceptor annotation = method.getAnnotation(ExtraInterceptor.class);
					if (null != annotation) {
						String name = annotation.namePattern();
						if (null == name || name.length() == 0) {
							throw new RuntimeException("null interceptor name: " + name + ", " + method);
						}
						Object cacheMethod = mapping.methodInterceptorMap.get(name);
						if (null != cacheMethod) {
							throw new RuntimeException("repeat interceptor: " + name + ", " + cacheMethod);
						}
						int order = annotation.interceptOrder();

						List<ExtraInterceptor> value = treeMap.get(order);
						if (null == value) {
							treeMap.put(order, value = new ArrayList<>());
						}
						value.add(annotation);

						treeMap.put(order, value);
						extraInterceptorMap.put(annotation, method);
					}
				}
				for (Map.Entry<Integer, List<ExtraInterceptor>> entry : treeMap.entrySet()) {
					List<ExtraInterceptor> value = entry.getValue();
					for (ExtraInterceptor annotation : value) {
						String name = annotation.namePattern();
						Method method = extraInterceptorMap.get(annotation);
						mapping.methodInterceptorRegexCacheMap.put(Pattern.compile(name), method);
					}
				}
//					System.out.println(TabPrint.wrap(Strings.join(mapping.methodInterceptorMap, "\n")));
			}
			return mapping;
		} else {
			throw new RuntimeException("not a annotation interface class: " + annotationInterface0);
		}
	}




	public static class AnnotationInterfaceMapping {
		Class annotationInterface;

		AnnotationInterfaceMapping(Class annotationInterface) {
			this.annotationInterface = annotationInterface;
		}



		protected MethodMapping newMethodMapping() { return new MethodMapping(); }

		public InterceptorMessage newInterceptorMessage(Method target, Annotation[] targetAnnotations) {
			return new InterceptorMessage(target, targetAnnotations);
		}
		public InterceptorMessage newInterceptorMessage(InterceptorMessage clone) {
			return new InterceptorMessage(clone);
		}


		public Class getAnnotationInterface() {
			return annotationInterface;
		}


		@Override
		public String toString() {
			JSONObject builder = new JSONObject()
				.put("annotationInterface",      this.annotationInterface)
				.put("interceptorRegexCacheMap", this.methodInterceptorRegexCacheMap)
				.put("methodMapping",            this.methodMappingMap);

			JSONObject interceptors = new JSONObject();
			for (String key: this.methodInterceptorMap.keySet()) {
				interceptors.put(key, Objects.first(this.methodInterceptorMap.get(key)));
			}
			builder.put("interceptors", interceptors);

			return builder.toFormatString();
		}


		final Map<Method, MethodMapping> methodMappingMap = new HashMap<>();
		public MethodMapping getMethodMapping(ProxyFilterImpl proxyx, Method annotationInterfaceMethod) {
			MethodMapping mapping = methodMappingMap.get(annotationInterfaceMethod);
			if (null == mapping) {
				synchronized (methodMappingMap) {
					methodMappingMap.put(annotationInterfaceMethod, mapping = createMethodMapping(proxyx, this.getAnnotationInterface(), annotationInterfaceMethod));
				}
			}
			return mapping; // no sort
		}

		/**
		 * The run method is not necessarily related to my interface
		 */
		public MethodMapping createMethodMapping(ProxyFilterImpl handler, Class<?> annotationInterface, Method targetMethod) {
			MethodMapping mapping = newMethodMapping();

			//my interface rewrite method, or  extends other interface method
			Method      annotationInterfaceMethod = fromAnnotationInterfaceFindEqualsMethod(handler, annotationInterface, targetMethod);
			if (null == annotationInterfaceMethod) {
				//other interface method
				mapping.targetAnnotations   = new Annotation[]{};
				mapping.target        		= targetMethod;
				mapping.isAnnotationInterfaceMethod = false;
				return mapping;
			} else {
				//my interface rewrite method
				Map<Integer, List<Annotation>> treeAnnotations = new TreeMap<Integer, List<Annotation>>(new Comparator<Integer>() {
						@Override
						public int compare(Integer x, Integer y) {
							return (x < y) ? -1 : ((x.intValue() == y.intValue()) ? 0 : 1);
						}
					});
				for (Annotation a: annotationInterface.getAnnotations()) {
					AnnotationExecutor annotationExecutor = handler.findAnnotationExecutor(a.annotationType());
					if (null != annotationExecutor) {
						a = annotationExecutor.cloneAnnotation(a); //cloneExecutor

						Integer order = annotationExecutor.order(a);
						List<Annotation> list = treeAnnotations.get(order);
						if (null == list) {
							treeAnnotations.put(order, list = new ArrayList<>());
						}
						list.add(a);
					}
				}
				for (Annotation a: annotationInterfaceMethod.getAnnotations()) {
					AnnotationExecutor annotationExecutor = handler.findAnnotationExecutor(a.annotationType());
					if (null != annotationExecutor) {
						a = annotationExecutor.cloneAnnotation(a); //cloneExecutor

						Integer order = annotationExecutor.order(a);
						List<Annotation> list = treeAnnotations.get(order);
						if (null == list) {
							treeAnnotations.put(order, list = new ArrayList<>());
						}
						list.add(a);
					}
				}

				List<Annotation> annotations = new ArrayList<>();
				for (List<Annotation> as: treeAnnotations.values()) {
					for (Annotation an: as) {
						annotations.add(an);
					}
				}

				mapping.targetAnnotations   = annotations.toArray(new Annotation[]{});
				mapping.target              = annotationInterfaceMethod;
				mapping.isAnnotationInterfaceMethod = true;
				return mapping;
			}
		}

		/**
		 * The run method is not necessarily related to my interface
		 */
		public Method fromAnnotationInterfaceFindEqualsMethod(ProxyFilterImpl filter, Class<?> annotationInterface, Method targetMethod) {
			if (targetMethod.getDeclaringClass() == annotationInterface) {
				return targetMethod;
			} else {
				//only current interface method
				Method[] methods = filter.getAnnotationInterfaceMethods(annotationInterface);

				String  runName             = targetMethod.getName();
				Class   runReturnType       = targetMethod.getReturnType();
				Class[] runMethodParamTypes = targetMethod.getParameterTypes();
				for (Method m: methods) {
					if (m.getName().equals(runName)) {
						if (m.getReturnType() == runReturnType) {
							if (Objects.identityEquals(m.getParameterTypes(), runMethodParamTypes)) {
								return m;
							}
						}
					}
				}
				return null;
			}
		}

		public static class MethodMapping {
			public Method target;
			public Annotation[] targetAnnotations;
			public InterceptorMessage interceptor;

			boolean isAnnotationInterfaceMethod = false;
			public boolean isAnnotationInterfaceMethod() {
				return this.isAnnotationInterfaceMethod;
			}

			@Override
			public String toString() {
				return new JSONObject()
					.put("targetAnnotations", Arrayz.toString(targetAnnotations))
					.put("target", target)
					.put("interceptor", interceptor)
					.put("isAnnotationInterfaceMethod", isAnnotationInterfaceMethod)
					.toString();
			}
		}





		public InterceptorMessage findInterceptorMessage(ProxyFilterImpl proxyx, Method annotationInterfaceMethod) {
			String methodName = annotationInterfaceMethod.getName();
			InterceptorMessage[] find = methodInterceptorMap.get(methodName);
			if (null == find) {
				Set<Pattern> set = methodInterceptorRegexCacheMap.keySet();
				synchronized (methodInterceptorMap) {
					for (Pattern s: set) {
						if (s.matcher(methodName).find()) {
							Method pattern = methodInterceptorRegexCacheMap.get(s);
							InterceptorMessage interceptorMessage = newInterceptorMessage(pattern, createMethodMapping(proxyx, this.getAnnotationInterface(), pattern).targetAnnotations);
							methodInterceptorMap.put(methodName, find = new InterceptorMessage[] {interceptorMessage});
							return Objects.first(find);
						}
					}
					methodInterceptorMap.put(methodName, new InterceptorMessage[]{});//not this method
				}
			}
			return Objects.first(find);
		}


		//<method name Pattern, method>
		private final Map<Pattern, Method> 				 methodInterceptorRegexCacheMap = new LinkedHashMap<>();
		//<method name, one-Interceptor-method>
		private final Map<String,  InterceptorMessage[]> methodInterceptorMap = new HashMap<>();


		public static class InterceptorMessage {
			public Method target;
			public Annotation[] targetAnnotations;
			public InterceptorMessage(Method target, Annotation[] targetAnnotations) {
				this.target = target;
				this.targetAnnotations = targetAnnotations;
			}
			public InterceptorMessage(InterceptorMessage clone) {
				this.target = clone.target;
				this.targetAnnotations = clone.targetAnnotations;
			}

			@Override
			public String toString() {
				// TODO: Implement this method
				return new JSONObject()
					.put("target", target)
					.put("targetAnnotations", Arrays.toString(targetAnnotations))
					.toString();
			}
		}
	}




	@Override
	public void doFilter(ProxyInvocationHandler handler,
						 Object tempObject,
						 Method annotationClassMethod, Object[] params,
						 ProxyReturn result) throws Throwable {
		ProxyFilterImpl filter    = this;
		ProxyReflecter  reflecter = this.factory().reflecter();

		AnnotationInterfaceMapping 				 annotationInterfaceMapping       = filter.getAnnotationInterfaceMapping(handler.getAnnotationInterface()); //find interface runMethod... weakmap
		AnnotationInterfaceMapping.MethodMapping annotationInterfaceMethodMapping = annotationInterfaceMapping.getMethodMapping(filter, annotationClassMethod);//map
//			System.out.println("annotationClassMethod: " + annotationClassMethod);
//			System.out.println("mapping: " + methodMapping);
//			System.out.println("interfaceMapping: " + interfaceMapping);
		Annotation[] runMethodAnnotations = annotationInterfaceMethodMapping.targetAnnotations;
		Method 		 runMethod 			  = annotationInterfaceMethodMapping.target;
		//Query the table at most two
		if (!annotationInterfaceMethodMapping.isAnnotationInterfaceMethod()) {
			if (null == annotationInterfaceMethodMapping.interceptor) {
//					System.out.println("from: " + declaringClass + " " + originalMethod);
				//Methods of the interface are not intercepted
				AnnotationInterfaceMapping.InterceptorMessage interceptor = annotationInterfaceMapping.findInterceptorMessage(filter, annotationClassMethod);//weakmap
//						System.out.println("interceptor" + interceptor);
				if (null == interceptor) {
					AnnotationInterfaceMapping.InterceptorMessage  newInterceptor = annotationInterfaceMapping.newInterceptorMessage(runMethod, runMethodAnnotations);
					annotationInterfaceMethodMapping.interceptor = newInterceptor; //cache
				} else {
					AnnotationInterfaceMapping.InterceptorMessage  newInterceptor = annotationInterfaceMapping.newInterceptorMessage(interceptor);
					//Make weakmap work
					annotationInterfaceMethodMapping.interceptor = newInterceptor; //cache

					runMethodAnnotations = interceptor.targetAnnotations;
					runMethod            = interceptor.target;//has interceptor, run interceptor
				}
//			        System.out.println("to: " + runMethod);
			} else {
				runMethodAnnotations = annotationInterfaceMethodMapping.interceptor.targetAnnotations;
				runMethod   		 = annotationInterfaceMethodMapping.interceptor.target;//has interceptor, run interceptor
			}
		}
//		System.out.println("from >> " + annotationClassMethod);
//		System.out.println("to >> "   + runMethod);
		filterateForeach(handler, filter, reflecter,
						 runMethodAnnotations, runMethod, 
						 annotationClassMethod,
						 params,
						 result);
	}


	public void filterateForeach(ProxyInvocationHandler handler, ProxyFilterImpl filter, ProxyReflecter reflecter,
								 Annotation[] runMethodAnnotations, Method runMethod,
								 Method annotationClassMethod, 
								 Object[] params,
								 ProxyReturn result) throws Throwable {
		if (runMethodAnnotations.length != 0) {
			for (Annotation annotation : runMethodAnnotations) {
				AnnotationExecutor executor;
				executor = filter.findAnnotationExecutor(annotation.annotationType());//non null
				executor.execute(handler, filter, reflecter,
								 annotation, runMethod,

								 handler.getInvokeObjectClass(), handler.getInvokeObject(), 
								 annotationClassMethod,

								 params, result);
			}
		}
	}
}
