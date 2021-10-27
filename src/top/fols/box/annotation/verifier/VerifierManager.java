package top.fols.box.annotation.verifier;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import top.fols.atri.lang.Arrayz;
import top.fols.atri.lang.Clasz;
import top.fols.atri.lang.Finals;
import top.fols.atri.lang.Objects;
import top.fols.atri.util.TabPrint;
import top.fols.box.annotation.verifier.annotation.VKey;
import top.fols.box.annotation.verifier.annotation.VKeys;


public class VerifierManager<O extends VerifierObject> {
	
	
	public static final Class<VerifierManager> CLASS = VerifierManager.class;
	public static final Annotation[] EMPTY_ANNOTATION = {};


	public static Annotation[] getAnnotations(AnnotatedElement ae) {
		Annotation[] aes = ae.getAnnotations();
		return null == aes ? EMPTY_ANNOTATION: aes;
	}
	public static Annotation   getAnnotation(AnnotatedElement ae, Class type) {
		return ae.getAnnotation(type);
	}
	public static <T extends Annotation> Class<T> getAnnotationClass(T annotation) {
		return null == annotation ?null: (Class<T>) (Class<Annotation>) annotation.annotationType();
	}



	public static String[]     getKeys(AnnotatedElement ae) {
		Annotation[] aes = getAnnotations(ae);
		if (aes.length == 0) {
			return Finals.EMPTY_STRING_ARRAY;
		}
		List<String> list = new ArrayList<>();
		VKey key   =  (VKey) getAnnotation(ae, VKey.CLASS);
		VKeys keyw = (VKeys) getAnnotation(ae, VKeys.CLASS);
		if (null != key) {
			list.add(key.value());
		} 
		if (null != keyw) {
			list.addAll(Arrays.asList(keyw.value()));
		}
		return list.toArray(Finals.EMPTY_STRING_ARRAY);
	}


	public static Verifier[] getVerifierList(AnnotatedElement ae) {
		Annotation annotations[] = getAnnotations(ae);
		List<Verifier> verifiers = new ArrayList<>();
		for (Annotation annotation: annotations) {
			Verifier vers = getVerifier0(annotation);
			verifiers.add(vers);
		}
		return verifiers.toArray(Verifier.EMPTY);
	}
	public static Verifier getVerifier(Annotation ae) {
		return getVerifier0(ae);
	}

	/**
	 * 获取该annotation类内的 static {检验器} 类型字段值
	 * 如
	 * public @interface Hello { public static final Verifier A = [Verifier1]; }
	 * 返回 [Verifier1]
	 */
	static Verifier getVerifier0(Annotation annotation) {
		Class<?> aCls = getAnnotationClass(annotation);
		return getAnnotationVerifier0(aCls);
	}
	static Verifier getAnnotationVerifier0(Class<?> annotationType) {
		if (null == annotationType) {
			return null;
		}
		Field[] elements =  getFields(annotationType);
		for (Field element: elements) {
			if (Modifier.isStatic(element.getModifiers())) {
				Class type = element.getType();
				if (type == Verifier.CLASS) {
					try {
						Verifier   value = (Verifier) element.get(null);
						if (null != value) {
							return value;
						}
					} catch (IllegalArgumentException e) {} catch (IllegalAccessException e) {}
				}
			}
		}
		return null;
	}





	/**
	 * annotation 检验器
	 * 每个annotation必须都有一个唯一的AnnotationVerifier
	 */
	public static class AnnotationVerifier<T extends Annotation, V, O extends VerifierObject> {
        T annotation;
        Verifier<T, V, O> annotationVerifier;


        /**
		 * 注解的值都是固定的 
		 * 用于将注解值转为需要的对象
		 */
        public volatile Object tag;


        public AnnotationVerifier(T annotation, Verifier<T, V, O> requireVerifier) {
            this.annotation = annotation;
            this.annotationVerifier = requireVerifier;
        }

        public T annotation() {
            return annotation;
        }
        public Verifier<T, V, O> annotationVerifier() {
            return annotationVerifier;
        }




        public boolean verify(O object) {
            // TODO: Implement this method
            return annotationVerifier.verify(this, object);
        }

        @Override
        public String toString() {
            // TODO: Implement this method
            return this.annotation.toString();
        }

        @Override
        public boolean equals(Object obj) {
            // TODO: Implement this method
            if (obj instanceof AnnotationVerifier == false) { return false; }
            AnnotationVerifier instance = (AnnotationVerifier) obj;

            boolean result;
            result = true;
            result &= (Objects.equals(this.annotation, instance.annotation));

            return result;
        }

        @Override
        public AnnotationVerifier<T, V, O> clone() {
            // TODO: Implement this method
            AnnotationVerifier<T, V, O> instance = new AnnotationVerifier<T, V, O>(this.annotation, this.annotationVerifier);
            instance.tag = null;
            return instance;
        }

        static AnnotationVerifier mergeAnnotationVrifier(AnnotationVerifier base, AnnotationVerifier cover) {
            if (null == base) { return cover;}
            if (null == cover) { return base; }

            Verifier rv = cover.annotationVerifier;

            Annotation newAnnotation = rv.selectMerge(base.annotation(), cover.annotation());

            AnnotationVerifier newAnnotationVrifier = cover.clone();
            newAnnotationVrifier.annotation = newAnnotation;
            return newAnnotationVrifier;
        }
    }


	/**
	 * 相当于 AnnotationVerifier 的 检验集
	 * 一个方法可以有多个注解
	 */
	public static class MappingVerifier {
		public static final Class<MappingVerifier> CLASS = MappingVerifier.class;

        /**
         * 每个 AnnotationCollectionVerifier 都是唯一的   对应 一个 Method
         * 而每个 AnnotationVerifier 既 Method 的 一个Annotation
         * Method 的 Annotation 不会重复 所以可以使用 <Class, AnnotationVerifier>
         */
        /*
         * <Class[Annotation], AnnotationVerifier>
         */
        private Map<Class, AnnotationVerifier> linked = new HashMap<>();
        private Method method0;
		private MethodExecutor executor;

        public boolean isEmpty() { return null == linked || linked.isEmpty(); }

		protected void    setMappingMethod(Method method) {
			this.setMappingMethod(method, null);
		}
        protected void    setMappingMethod(Method method, MethodExecutor methodExecutor) {
			this.method0 = method; 
			this.executor = null == methodExecutor ? 
				MethodExecutor.parse(method):
				methodExecutor;
		}
        public Method getMappingMethod() { return this.method0; }
        public boolean isMappingMethod() {
            return null != this.method0;
        }


		public MethodExecutor getMethodExecutor() { return this.executor; }



        @Override
        public String toString() {
            // TODO: Implement this method


			TabPrint toString = new TabPrint(CLASS.getSimpleName());
			toString.add("method", Arrayz.toString(method0));
			toString.add("check",  Arrayz.toString(this.linked.values().toArray()));

			return toString.toString();
        }



        /**
		 * Must be called after all variables are determined
		 */
        protected AnnotationVerifier[] verifier_cache;
        protected AnnotationVerifier[] verifier_cache() {
            AnnotationVerifier[] verification = this.verifier_cache;
            if (null == verification) {
                return this.verifier_cache = verification = linked.values().toArray(new AnnotationVerifier[linked.size()]);
            } else {
                return verification;
            }
        }

        public boolean verifys(VerifierObject object) {
//			System.out.println(Arrayz.toString(this.verification()));
            for (AnnotationVerifier annotationVerifier: this.verifier_cache()) {
                if (annotationVerifier.verify(object) == false) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public MappingVerifier clone() {
            // TODO: Implement this method
            MappingVerifier instance = new MappingVerifier();
            instance.linked = new HashMap<Class, AnnotationVerifier>(this.linked);
            instance.method0 = this.method0;
			instance.executor = this.executor;
            return instance;
        }
    }






	public static class DynamicMapping extends Mapping {
		private DynamicMapping() {
			super.dynamic = true;
		}


		public static DynamicMapping create(Method method, Annotation... annotations) {

			final Map<Class<? extends Annotation>, Annotation> map = new HashMap<>();
			final Annotation[] clone = annotations.clone();

			for (Annotation annotation: annotations){
				map.put(annotation.annotationType(), annotation);
			}
			Mapping mapping1 = parseMappingElement(new AnnotatedElement() {
				@SuppressWarnings("unchecked")
				@Override
				public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
					return (T) map.get(annotationClass);
				}
				@Override
				public Annotation[] getAnnotations() {
					return clone;
				}
				@Override
				public Annotation[] getDeclaredAnnotations() {
					throw new UnsupportedOperationException(getClass().getName());
				}
			});

			DynamicMapping methodMappingElement = new DynamicMapping();
			methodMappingElement.keys = mapping1.keys;
			methodMappingElement.verifiers = null == methodMappingElement.verifiers ?new MappingVerifier(): methodMappingElement.verifiers;
			methodMappingElement.verifiers.setMappingMethod(method);

			return methodMappingElement;
		}
	}
	public static class Mapping {
		public static final Class<Mapping> CLASS = Mapping.class;
		public static final Mapping[]      EMPTY = {};

		String[] keys = null;
		MappingVerifier verifiers = null;


		boolean dynamic = false;
		boolean emptyKey() {
			return null == keys || keys.length == 0;
		}
		boolean emptyVerifiers() {
			return null == verifiers;
		}


		public Method getMethod() {
			return this.verifiers.getMappingMethod();
		}
		public MethodExecutor getMethodExecutor() { return this.verifiers.getMethodExecutor(); }

		public boolean isDynamic() {
			return this.dynamic;
		}


		@Override
		public String toString() {
			// TODO: Implement this method

			TabPrint toString = new TabPrint(CLASS.getSimpleName());
			toString.add("dynamic",   Arrayz.toString(dynamic));
			toString.add("keys",      Arrayz.toString(keys));
			toString.add("verifiers", Arrayz.toString(verifiers));

			return toString.toString();
		} 
	}



	/**
	 * key 不会被合并
	 */
	protected static Mapping mergeMappingElement(Mapping base, Mapping cover) {
        if (null == base)  { return cover; }
        if (null == cover) { return base;  }

        Mapping mapping = new Mapping();
        if (base.emptyKey()) {
            mapping.keys = cover.keys.clone();

            if (base.emptyVerifiers()) {
                mapping.verifiers = cover.verifiers.clone();
                return mapping;
            } else {
                mapping.verifiers = base.verifiers.clone();
                Map<Class, AnnotationVerifier> vs = new HashMap<>();
                for (Class cls: base.verifiers.linked.keySet()) {
                    vs.put(cls, base.verifiers.linked.get(cls));
                }
                for (Class cls: cover.verifiers.linked.keySet()) {
                    vs.put(cls, AnnotationVerifier.mergeAnnotationVrifier(base.verifiers.linked.get(cls), cover.verifiers.linked.get(cls)));
                }
                mapping.verifiers.linked = vs;
            }
        } else {
            if (cover.emptyKey()) {
                mapping.keys = base.keys.clone();
                mapping.verifiers = base.verifiers.clone();
                Map<Class, AnnotationVerifier> vs = new HashMap<>();
                for (Class cls: base.verifiers.linked.keySet()) {
                    vs.put(cls, base.verifiers.linked.get(cls));
                }
                for (Class cls: cover.verifiers.linked.keySet()) {
                    vs.put(cls, AnnotationVerifier.mergeAnnotationVrifier(base.verifiers.linked.get(cls), cover.verifiers.linked.get(cls)));
                }
                mapping.verifiers.linked = vs;
            } else {
                mapping.keys = cover.keys.clone();
                mapping.verifiers = (null == cover.verifiers ?null: cover.verifiers.clone());
            }
        }
        return mapping;
    }
	protected static Mapping parseMappingElement(AnnotatedElement annotatedElement) {
        if (null == annotatedElement) { return null; }


		TabPrint toString = new TabPrint("parseMappingElement");
		toString.add("annotatedElement", annotatedElement);

        String[] baseToken;
        MappingVerifier baseConditionVerifier;

        {
            baseToken = null;

            List<Annotation> list = new ArrayList<Annotation>(Arrays.asList(annotatedElement.getAnnotations()));
            String[] key = getKeys(annotatedElement);
            if (null != key) {
                baseToken = key;
                list.remove(key);
            }
		    toString.add("key", Arrayz.toString(baseToken));

            baseConditionVerifier = null;
            MappingVerifier conditionVerifier = new MappingVerifier();
            for (Annotation annotation: list) {
				Class<? extends Annotation> annotationClass = getAnnotationClass(annotation);
				Verifier verifier = getVerifier(annotation);
                if (null != verifier) {
                    annotation = verifier.clone(annotation);

					toString.add("annotation", Arrayz.toString(annotation));

                    AnnotationVerifier annotationVerifier = new AnnotationVerifier(annotation, verifier);
                    conditionVerifier.linked.put(annotationClass, annotationVerifier);

//					System.out.println(annotation);
//					System.out.println(verifier);

                }
            }

            if (!conditionVerifier.isEmpty()) {
                baseConditionVerifier = conditionVerifier;
            }
        }

        System.out.println(toString);

        if (!(
            null == baseToken &&
            null == baseConditionVerifier)) {
            Mapping me = new Mapping();
            me.keys = baseToken;
            me.verifiers = baseConditionVerifier;

			return me;
        }
        return null;
    }

	public static class MethodExecutor {
        Method method;
        int 				paramCount;
        Class[] 			paramTypes;

        static MethodExecutor parse(
			Method method) {

            MethodExecutor instance = new MethodExecutor();
            instance.method = method;

            Class<?>[] paramTypes = method.getParameterTypes();

            instance.paramCount = paramTypes.length;
            instance.paramTypes = paramTypes;

            if (paramTypes.length != 1 || !Clasz.isInstance(paramTypes[0], VerifierObject.CLASS)) {
                throw new UnsupportedOperationException(String.format("method [%s] parameter needs to match {%s}", method, VerifierObject.CLASS));
            }
            return instance;
        }

        public Object[] createMethodParam(VerifierObject dataPacket) { return new Object[]{dataPacket}; }
    }






	private Map<String, List<Mapping>> mMappingMap = new LinkedHashMap<>();
	private Map<Method, Mapping> mMappingMethodMap = new LinkedHashMap<>();

	private void addMapping(Mapping conditionVerifier) {
		this.addMapping0(conditionVerifier.keys, conditionVerifier);
	}
	private void addMapping0(String[] keys, Mapping conditionVerifier) {
		if (null == keys || keys.length == 0) { throw new NullPointerException("token"); }

		Method method = conditionVerifier.verifiers.getMappingMethod();
		if (null == method) {
			throw new RuntimeException("no set mapping method: " + method);
		}
        if (this.hasMapping(method)) {
            throw new RuntimeException("already mapping: " + method);
        }
		for (String tokens: keys) {
			List<Mapping> list = mMappingMap.get(tokens);
			if (null != conditionVerifier) {
				if (null == list) {
					this.mMappingMap.put(tokens, list = new ArrayList<>());
				}
				list.add(conditionVerifier);
				Collections.sort(list, new Comparator<Mapping>() {
						@Override
						public int compare(Mapping left, Mapping right) {
							// TODO: Implement this method
							int leftCount = null == left ?0: left.verifiers.linked.size();
							int rightCount = null == right ?0: right.verifiers.linked.size();
							return leftCount >= rightCount ?-1: 1;
						}
					});
			}
			this.mMappingMap.put(tokens, list);
			this.mMappingMethodMap.put(method, conditionVerifier);
		}
	}


    public boolean hasMapping(Method method) {
		return this.mMappingMethodMap.containsKey(method);
	}
	public boolean hasMapping(Mapping method) {
		return this.mMappingMethodMap.containsKey(method.getMethod());
	}

    public boolean removeMapping(Method method) {
        if (hasMapping(method)) {
            Mapping mappingElement = this.mMappingMethodMap.get(method);
            return removeMapping(mappingElement);
        } else {
            return false;
        }
    }

	public boolean removeMapping(Mapping mappingElement) {
		if (null == mappingElement) {
			return false;
		}
        Method method = mappingElement.getMethod();
		boolean result = false;
		this.mMappingMethodMap.remove(method);//删除正常注册
		this.mDynamicMappingElementMap.remove(mappingElement);//删除动态注册
		for (String k: mappingElement.keys) {
			List<Mapping> cs = this.mMappingMap.get(k);
			if (null != cs) {
				cs.remove(mappingElement);
				result = true;
				if (cs.size() == 0) {
					this.mMappingMap.remove(k);
				}
			}
		}
		return result;
    }

	public Mapping getMapping(Method method) {
		return this.mMappingMethodMap.get(method);
	}
	public Mapping[] getMappings(String key) {
		List<Mapping> lists = this.mMappingMap.get(key);
		return lists.toArray(Mapping.EMPTY);
	}

    public boolean containsKey(String token) {
        List<Mapping> list = this.mMappingMap.get(token);
        return null != list;
    }


    public Mapping matchMapping(String token, O dataPacket) {
        List<Mapping> list = this.mMappingMap.get(token);
        if (null == list) {
            return null;
        } 
		for (int i = 0; i < list.size(); i++) {
			Mapping conditionVerifier = list.get(i);
			if (conditionVerifier.verifiers.verifys(dataPacket)) {
				return conditionVerifier;
			}
		}
		return null;
    }
	public Mapping matchMapping(O dataPacket) {
		String key = dataPacket.key();
        return matchMapping(key, dataPacket);
    }






    public String[] getKeys() {
        Map<String, List<Mapping>> map = this.mMappingMap;
        String[] paths = new String[map.size()];
        int i = 0;
        for (String token : map.keySet()) {
            String url = token;
            paths[i++] = url;
        }
        return paths;
    }

	
	
	public static Field[] getFields(Class<?> cls) {
		return cls.getFields();
	}
	public static Method[] getMethods(Class<?> cls) {
		return cls.getMethods();
	}




    public Method[] registerReceiver(Class<?> cls) {
        List<Method> result = new ArrayList<>();
        Mapping baeMappingElement = parseMappingElement(cls);
        {
            Method[] methods = getMethods(cls);
            for (int methodIndex = 0; methodIndex < methods.length; methodIndex++) {
                Method method = methods[methodIndex];
                if (!Objects.empty(getVerifierList(method))) {
                    Mapping coverMappingElement = parseMappingElement(method);

                    Mapping methodMappingElement;
					methodMappingElement = mergeMappingElement(baeMappingElement, coverMappingElement);
					methodMappingElement.verifiers = null == methodMappingElement.verifiers ?new MappingVerifier(): methodMappingElement.verifiers;
					methodMappingElement.verifiers.setMappingMethod(method);

					TabPrint toString = new TabPrint("mergeMappingElement");
					toString.add("Class-Annotations",  baeMappingElement.toString());
					toString.add("Method-Annotations", coverMappingElement.toString());
					toString.add("Merge-Annotation",   methodMappingElement.toString());
					System.out.println(toString);

                    this.addMapping(methodMappingElement);
					result.add(method);
                }
            } 
        }
		return result.toArray(Finals.EMPTY_METHOD_ARRAY);
    }

	public boolean unregisterReceiver(Class<?> cls) {
		boolean removed = false;
		Method[] methods = getMethods(cls);
		for (int methodIndex = 0; methodIndex < methods.length; methodIndex++) {
			Method method = methods[methodIndex];
			if (!Objects.empty(getVerifierList(method))) {
				removeMapping(method);
				removed = true;
			}
		} 
        return removed;
    }




	public int keyCount() {
		return this.mMappingMap.size();
	}
	public int mappingCount() {
		return this.mMappingMethodMap.size();
	}

	public void clear() {
		this.mMappingMap.clear();
		this.mMappingMethodMap.clear();
		this.mDynamicMappingElementMap.clear();
	}








	private Set<Mapping> mDynamicMappingElementMap = new LinkedHashSet<>();



	//添加 Annotation... 参数
	public boolean addDynamicMapping(DynamicMapping mappingElement) {
		if (hasMapping(mappingElement)) {
			return false;
		} else {
			this.addMapping(mappingElement);
			this.mDynamicMappingElementMap.add(mappingElement);
			return true;
		}
    }
	public boolean hasDynamicMapping(Mapping mappingElement) {
		return this.mDynamicMappingElementMap.contains(mappingElement);
	}
	public Mapping[] listDynamicReceiver() {
		return this.mDynamicMappingElementMap.toArray(Mapping.EMPTY);
	}






	@Override
	public String toString() {
		// TODO: Implement this method
		TabPrint toString = new TabPrint(CLASS.getSimpleName());
		for (String key: this.mMappingMap.keySet()) {
			toString.add(key, Arrayz.toString(this.mMappingMap.get(key).toArray()));  
		}
		return toString.toString();
	}






	/**
	 * >> 创建 方法映射器
	 *    方法合并后最终 annotation 存在主键(PATH)
	 *    方法映射器 将 方法的所有 Annotation 转换为  MappingVerifier  一个方法 对应一个Mapping
	 *    			可以理解为 Method 转换为 Mapping, Mapping包含Key和姓名
	 *    在 PATH 对应的 [MappingVerifier 列表] 添加 当前方法 MappingVerifier
	 * 
	 * 
	 * >> 客户端访问服务器
	 * >> 服务端解析客户数据包头 
	 *        解析到的数据转换为 VerifierObject
	 *        方法映射器 寻找 PATH的  [MappingVerifier 列表] 如果找不到则直接返回
	 *        
	 *        遍历  [MappingVerifier 列表]   
	 *        MappingVerifier 根据 VerifierObject 检验 是否符合 如果符合就执行方法
	 * >> 返回数据给用户
	 */
}
