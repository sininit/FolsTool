package top.fols.box.util;

import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.util.*;

import top.fols.atri.cache.WeakCache;
import top.fols.atri.cache.WrapValue;
import top.fols.atri.interfaces.annotations.NotNull;
import top.fols.atri.interfaces.annotations.ThreadSafe;
import top.fols.atri.interfaces.annotations.Private;
import top.fols.atri.interfaces.interfaces.IReleasable;
import top.fols.atri.lang.Arrayz;
import top.fols.atri.lang.Boxing;
import top.fols.atri.reflect.ReflectMatcherAsScore;
import top.fols.atri.reflect.Reflects;

@SuppressWarnings({"rawtypes", "unchecked", "ConstantConditions"})
public class DataTypeConverter {
	/**
	 * copyed 20230326
	 * 
	 * @see top.fols.atri.cache.TemporaryMapValueModCache
	 */
	@ThreadSafe
	public static abstract class TemporaryMapValueModCacheTwoKey<K, K2, V, Ex extends Throwable> {
		@Private
		transient long mod;

		public void addMod() { mod ++; }

		public abstract V fromMapGetValue(K k, K2 k2) throws Ex;


		public static abstract class Updated<K, K2, V, Ex extends Throwable> {
			public abstract void updated(K k, K2 k2, V v) throws Ex;
		}

		public ValueGetter createValueGetter(K k, K2 k2) {
			return new ValueGetter(k, k2);
		}
		public class ValueGetter implements IReleasable {
			@Private
			@Override
			public boolean release() {
				// TODO: Implement this method
				this.lastMod = (TemporaryMapValueModCacheTwoKey.this.mod - 1);
				this.v = null;
				return true;
			}

			@Override
			public boolean released() {
				// TODO: Implement this method
				return this.lastMod != TemporaryMapValueModCacheTwoKey.this.mod &&
					this.v == null;
			}


			@Private
			long lastMod = (TemporaryMapValueModCacheTwoKey.this.mod - 1);

			final K k;
			final K2 k2;
			V v;

			ValueGetter(K k, K2 k2) {
				this.k  = k;
				this.k2 = k2;
			}

			public V getValue() throws Ex {
				if (this.lastMod == TemporaryMapValueModCacheTwoKey.this.mod) {
					return v;
				} else {
					this.lastMod = TemporaryMapValueModCacheTwoKey.this.mod;
					this.v = TemporaryMapValueModCacheTwoKey.this.fromMapGetValue(k, k2);
					return v;
				}
			}

			public V getValue(Updated<K,K2,V,Ex> updated) throws Ex {
				if (this.lastMod == TemporaryMapValueModCacheTwoKey.this.mod) {
					return v;
				} else {
					this.lastMod = TemporaryMapValueModCacheTwoKey.this.mod;
					this.v = TemporaryMapValueModCacheTwoKey.this.fromMapGetValue(k, k2);
					if (null != updated)
						updated.updated(k, k2, v);
					return v;
				}
			}
		}
	}
	public static class DataConvertException extends RuntimeException {
		public DataConvertException(String message)    {super(message);}
		public DataConvertException(Throwable message) {super(message);}
	}
	public static class CloneFailedException extends DataConvertException {
		public CloneFailedException(String message)    {super(message);}
		public CloneFailedException(Throwable message) {super(message);}
	}
	public static class NotFoundConverterException extends RuntimeException {
		public NotFoundConverterException(String message)    {super(message);}
		public NotFoundConverterException(Throwable message) {super(message);}
	}



	protected static boolean isBaseType(Object object) {
		if (null == object)
			return true;
		if (Boxing.isWrapperType(object.getClass()))
			return true;
		if (object instanceof String)
			return true;
		return false;
	}

	protected static boolean isAbstractOrInterface(Class cls) {
		int mod = cls.getModifiers();
		return 
			Modifier.isAbstract(mod) ||
			Modifier.isInterface(mod) ;
	}

	protected static boolean isAssignableFrom(Class c, Class subclass) {
	    return null != c && null != subclass && c.isAssignableFrom(subclass);
	}

	@SuppressWarnings("SameParameterValue")
	protected static Map<Class<?>, Map<Class<?>, Converter>> cloneConverterMapDeeply(Map<Class<?>, Map<Class<?>, Converter>> converterMap) {
		Map<Class<?>, Map<Class<?>, Converter>> clone = new LinkedHashMap<>();

		for (Map.Entry<Class<?>, Map<Class<?>, Converter>> entry : converterMap.entrySet()) {
			Class<?> key = entry.getKey();
			Map<Class<?>, Converter> value = entry.getValue();
			Map<Class<?>, Converter> clonedValue = new LinkedHashMap<>();

			for (Map.Entry<Class<?>, Converter> subEntry : value.entrySet()) {
				Class<?> subKey = subEntry.getKey();
				Converter subValue = subEntry.getValue();

				clonedValue.put(subKey, subValue);
			}

			clone.put(key, clonedValue);
		}
		return clone;
	}






//    @SuppressWarnings({"rawtypes"})
//    static void getSuperclassGroupLevelFrom0PutResult(Map<Integer, Set<Class>> result, Class aClass) {
//		int i = 0;
//		getSuperclassGroupLevel0PutResult(result, new LinkedHashSet<Class>(), i, aClass, aClass);
//    }
//    @SuppressWarnings({"rawtypes"})
//    static void getSuperclassGroupLevel0PutResult(Map<Integer, Set<Class>> result, final Set<Class> added, int i, final Class oldClass, Class aClass) {
//		if (null == aClass) return;
//
//		Class aClassSuperclass = aClass.getSuperclass();
//		Class[] interfaces     = aClass.getInterfaces();
//		if (null == aClassSuperclass &&
//			0    == interfaces.length) return;
//
//        Set<Class>  classes = result.get(i);
//		if (null == classes)
//			result.put(i, classes = new LinkedHashSet<Class>() {
//						   @Override public boolean add(Class cls) {
//							   if (cls == oldClass) {
//								   return false;
//							   }
//							   if (added.contains(cls)) {
//								   super.remove(cls);
//								   super.add(cls); //to tail
//							   } else {
//								   added.add(cls);
//								   super.add(cls);
//							   }
//							   return true;
//						   }
//					   });
//		//classes.add(aClass);
//
//		if (null != aClassSuperclass)
//			classes.add(aClassSuperclass);
//		for (Class anInterface : interfaces)
//			classes.add(anInterface);
//
//		i++;
//
//		if (null != aClassSuperclass)
//			getSuperclassGroupLevel0PutResult(result, added, i, oldClass, aClassSuperclass);
//		for (Class anInterface : interfaces)
//			getSuperclassGroupLevel0PutResult(result, added, i, oldClass, anInterface);
//    }
//
//	//对所有父类进行分组(按层级)  从0开始
//	static WeakMapCacheConcurrentHash<Class, Class[][], RuntimeException> getSuperclassGroupLevelCache = new WeakMapCacheConcurrentHash<Class, Class[][], RuntimeException>(){
//		@Override
//		public Class[][] newCache(Class cls) throws RuntimeException {
//			// TODO: Implement this method
//			Map<Integer, Set<Class>> result = new LinkedHashMap<>();
//			if (null != cls) {
//				if (cls.isArray()) {
//					Class root = Arrayz.getRootComponentType(cls);
//					int d      = Arrayz.dimension(cls);
//
//					getSuperclassGroupLevelFrom0PutResult(result, root);
//
//					for (int i = 0; i < result.size(); i++) {
//						Set<Class> newClasses = new LinkedHashSet<>();
//
//						Set<Class> classes = result.get(i);
//						for (Class clazz: classes) {
//							newClasses.add(Arrayz.dimensionClass(clazz, d));
//						}
//
//						result.put(i, newClasses);
//					}
//
//					Set<Class> first;
//					int index;
//					int count = result.size();
//					if (count == 0) {
//						first = new LinkedHashSet<>(); index = 0;
//					} else {
//						first = result.get(index = 0);
//					}
//					first.add(Object.class);
//					first.addAll(Arrays.asList(Cloneable.class, Serializable.class));
//
//					if (first.size() > 0) {
//						result.put(index, first);
//					}
//				} else {
//					getSuperclassGroupLevelFrom0PutResult(result, cls);
//				}
//			}
//			Class[][] finalReturn = new Class[result.size()][];
//			for (int i = 0; i < result.size(); i++) {
//				Set<Class> classesSet = result.get(i);
//				finalReturn[i] = (null == classesSet ? Finals.EMPTY_CLASS_ARRAY : classesSet.toArray(Finals.EMPTY_CLASS_ARRAY));
//			}
//			return finalReturn;
//		}
//	};
//	//low
//	static Class[][] getSuperclassGroupLevel(Class cls) {
//		return getSuperclassGroupLevelCache.getOrCreateCache(cls);
//	}
//
//
//
//    @SuppressWarnings({"rawtypes", "unchecked"})
//    static double calcAssignableFromLevel(Class parentClass, Class thisClass) {
//        double level = 0;
//        if (parentClass == thisClass) { return level = 0; }
//
//		if (null == parentClass || null == thisClass) {
//            throw new NullPointerException();
//        }
//        if (!parentClass.isAssignableFrom(thisClass)) {
//            throw new UnsupportedOperationException("!parentClass.isAssignableFrom(thisClass)");
//        }
//
//        Class s = thisClass.getSuperclass();
//        if (null == s) { // thisClass is java.lang.Object
//            if (parentClass != Object.class)
//                throw new UnsupportedOperationException("parentClass != Object.class");
//            return level = 0;
//        }
//
//		if (parentClass.isArray() || thisClass.isArray()) {
//			Class pt = parentClass, tt = thisClass;
//			Class pc = pt.getComponentType(), tc=tt.getComponentType();
//			while ((null != pc) &&
//				   (null != tc) &&
//				   pc.isAssignableFrom(tc)) {
//				parentClass = pc;
//				thisClass   = tc;
//				pc = pc.getComponentType();
//				tc = tc.getComponentType();
//			}
//		}
//
//		level ++;
//		Class[][] supers = getSuperclassGroupLevel(thisClass);
//        M: 	{
//			for (int i = 0; i < supers.length; i++) {
//				Class[] superclass = supers[i];
//				for (Class compare: superclass) {
//					if (compare == parentClass) {
//						level += i;
//						break M;
//					}
//				}
//			}
//			throw new UnsupportedOperationException("not found: " + thisClass + ", from: " + Arrays.deepToString(supers));
//		}
//        return level;
//    }



	 ReflectMatcherAsScore.MatchScore matchScore = new ReflectMatcherAsScore.MatchScore();
	 double calcAssignableFromLevel(Class parentClass, Class thisClass) {
		return matchScore.calcAssignableFromLevel(parentClass, thisClass);
	}



	// long[][], long[] -> long[], long, 1
	static Object[] geLowestLevelComponentType(Class parentClass, Class thisClass) {
		int deep = 0;
		Class pt = parentClass, tt = thisClass;
		Class pc = pt.getComponentType(), tc=tt.getComponentType();
		while ((null != pc) && 
			   (null != tc)) {
			parentClass = pc;
			thisClass   = tc;
			deep++;

			pc = pc.getComponentType();
			tc = tc.getComponentType();
		}
		return new Object[] {parentClass, thisClass, deep};
	}



//	static interface testi3 {}
//	static interface testi2 extends testi3 {}
//	static interface testi1 extends testi2 {}
//	static interface test21 extends Cloneable {}
//	static class test2 implements test21 {}
//	static class test1 extends test2 implements testi1 {}
//
//    static {
//		try {
//			Class[] classes = {Object.class, CharSequence.class, Appendable.class};
//			Class thisClass = StringBuffer.class;
//
//
//
//			for (Class aClass : classes) {
//				System.out.println(aClass + " compare " + thisClass);
//				System.out.println(Arrays.deepToString(getSuperclassCacbe.getOrCreateCache(thisClass)));
//				System.out.println(calcAssignableFromLevel(aClass, thisClass));
//				System.out.println();
//			}
//
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}
//        if (true) throw new RuntimeException();
//    }






	Set<Class> findAssignableFromMatch(Set<Class<?>> set, final Class find) {
		Set<Class> result = new LinkedHashSet<>();
		if (set.contains(find)) {
			result.add(find);
		}
		if (null != find) {
			List<Class> instanceofList = new ArrayList<>();
			for (Class s: set) {
				if ((null != s && !s.equals(find))) {
					if (s.isAssignableFrom(find)) {
						instanceofList.add(s);
					}
				}
			}
			Collections.sort(instanceofList, new Comparator<Class>() {
					@Override
					public int compare(Class p1, Class p2) {
						// TODO: Implement this method
						double cq = calcAssignableFromLevel(p1, find);
						double cw = calcAssignableFromLevel(p2, find);
						return Double.compare(cq, cw);
					}
				});
			//System.out.println(Arrays.asList(instanceofs));
			result.addAll(instanceofList);
		}
		return result;
	}






	public static abstract class Converter<T, U> {
		/**
		 * @param isAssignableFromTrueClass Previously, the result of isAssignableFrom=false has been filtered out
		 */
		public boolean isSupportSourceType(Class isAssignableFromTrueClass) {
			Class input = getSourceType();
			return isAssignableFromTrueClass == input || isAbstractOrInterface(input);
		}
		/**
		 * @param isAssignableFromTrueClass Previously, the result of isAssignableFrom=false has been filtered out
		 */
		public boolean isSupportTargetType(Class isAssignableFromTrueClass) {
			Class input = getTargetType();
			return isAssignableFromTrueClass == input || isAbstractOrInterface(input);
		}


		Class[] getGenericSuperclasses;
		Class[] getGenericSuperclasses() {
			if (null == getGenericSuperclasses)
				getGenericSuperclasses = Reflects.getGenericSuperclasses(getClass()).getGenericElementsAsTargetClass();
			return getGenericSuperclasses;
		}

		public Class<T> getSourceType() {
			// TODO: Implement this method
			return getGenericSuperclasses()[0];
		}
		public Class<U> getTargetType() {
			// TODO: Implement this method
			return getGenericSuperclasses()[1];
		}

		public boolean convertCustomQuery() {
			return false;
		}
        public abstract U       convert(T object, Object query) throws DataConvertException;

		protected U requireCloned(T object, U value) throws DataConvertException, CloneFailedException {
			if (value == object) {
				if (isBaseType(object))
					return value;
				else throw new CloneFailedException("cannot clone: " + top.fols.atri.lang.Objects.identityToString(object) + " from " + this);
			}
			return value;
		}
		public U requireClone(T object, Object query) throws DataConvertException, CloneFailedException {
			return requireCloned(object, convert(object, query));
		}

		@Override
		public String toString() {
			// TODO: Implement this method
			return "(" + getClass().getCanonicalName() + ": " + getSourceType() + " -> " + getTargetType() + ")";
		}

		protected final boolean isTemporaryConverter()    { return this instanceof TemporaryConverter;}
    }
	protected static abstract class TemporaryConverter<T, U> extends Converter<T,U> {} // 原来没有提交的但是动态生成的















	@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
	private static <T, U> void addConverterToTable(Map<Class<?>, Map<Class<?>, Converter>> lock, Class<T> sourceType, Class<U> targetType, Converter<T,U> converter) {
		if (converter.getSourceType() != sourceType) 
			throw new UnsupportedOperationException("converter.sourceType: " + converter.getSourceType() + " != " + sourceType);
		if (converter.getTargetType() != targetType) 
			throw new UnsupportedOperationException("converter.targetType: " + converter.getTargetType() + " != " + targetType);
		converter  = Objects.requireNonNull(converter, "converter");
		synchronized (lock) {
			Map<Class<?>, Converter> targetConverters = lock.get(sourceType);
			if (targetConverters == null) {
				targetConverters = new LinkedHashMap<Class<?>, Converter>();
				lock.put(sourceType, targetConverters);
			}
			targetConverters.put(targetType, converter);
		}
	}








    public <T, U> void addConverter(Class<T> sourceType, Class<U> targetType, Converter<T,U> converter) {
		converter  = Objects.requireNonNull(converter, "converter");
		synchronized (convertsLock) {
			addConverterToTable(convertsLock,
								sourceType, targetType, converter);

			this.getConverterOrCreatedTempGetterCached.addMod();
			this.tryGetConverterOrCreatedTempCached.release();
		}
    }

    public void removeConverter(Class sourceType, Class targetType) {
		synchronized (convertsLock) {
			Map<Class<?>, Converter> targetConverters = convertsLock.get(sourceType);
			if (targetConverters != null) {
				targetConverters.remove(targetType);
			}
		}
    }



	//@LowSpeed
	protected <T, U> Converter<T, U> tryMatchConverterOrCreateTemp(Class sourceType, Class targetType, boolean isDynamicGenerationTempConverter) {
		Converter<T, U> converter = null;
		if (null == converter) {
			M: { //equals or instanceof
				for (Class sType: findAssignableFromMatch(convertsLock.keySet(), sourceType)) {
					Map<Class<?>, Converter> sourceMappingTargetConverters = convertsLock.get(sType);
					for (Class tType: findAssignableFromMatch(sourceMappingTargetConverters.keySet(), targetType)) {
						Converter c;
						if (null  != (c = sourceMappingTargetConverters.get(tType))) {
							if ((c.isSupportSourceType(sourceType)) && 
								(c.isSupportTargetType(targetType))) {
								converter = c;
								break M;
							}
						}
					}
				}
			}
		}
		if (null == converter) {
			M: { //data intermediate converter
				for (Class sType: findAssignableFromMatch(convertsLock.keySet(), sourceType)) {
					Map<Class<?>, Converter> stConverters = convertsLock.get(sType);
					for (Class sm: stConverters.keySet()) {
						Map<Class<?>, Converter> smmMap = convertsLock.get(sm); //中间转换 直接使用绝对类型convertsLock.get(m) 
						if (null != smmMap) {
							for (Class tType: findAssignableFromMatch(smmMap.keySet(), targetType)) {
								Converter smc   = stConverters.get(sm);
								Converter smmtc = smmMap.get(tType);
								if ((smc.  isSupportSourceType(sourceType)) && 
									(smmtc.isSupportTargetType(targetType))) { 
									converter = createTempConvertFromIntermediate(smc, smmtc); // createIntermediateConvert 的 (isSourceTypeMatchInherit,isTargetTypeMatchInherit) 和这里的逻辑应该是一样的
									break M;
								}
							}
						}
					}
				}
			}
		}
		M: {//deal match inherit array
			if (null != sourceType && null != targetType) {
				if (sourceType.isArray() && targetType.isArray()) {
					boolean fullMatch = false;
					if (null != converter) {
						fullMatch =
							(converter.getSourceType() == sourceType &&
							(converter.getTargetType() == targetType));// already has converter
					}
					if (fullMatch) { 
						break M;
					} else {
						if (Arrayz.getRootComponentType(sourceType) == Object.class) {
							Converter atc = createDynamicConverterFromObjectArraySource(converter, sourceType, targetType);
							converter = atc;
							break M;
						} else {
							Converter atc = createTempConverterFromNotObjectArraySource(converter, sourceType, targetType);
							if (null != atc) {
								converter = atc;
								break M;
							}
						}
					}
				}
			}
		}
		if (null == converter) {
			M: {	//default
				if (isDynamicGenerationTempConverter) {
					if (sourceType  == targetType || isAssignableFrom(targetType, sourceType)) {
						converter = createTempConverterFromDirectReturn(sourceType, targetType);
					} else if (null == targetType || (null == sourceType && null == targetType)) {
						converter = createTempConvertFromNull();
					} 
				}
			}
		}
		return converter;
    }
	//@LowSpeed
	protected <T, U> Converter<T, U> tryMatchConverterOrCreateTemp(Class<T> sourceType, Class<U> targetType) {
		return tryMatchConverterOrCreateTemp(sourceType, targetType, true);
    }








	/**
	 * 每次克隆元素都获取新的转换器
	 */
	private <T,U> Converter<T,U> createDynamicConverterFromObjectArraySource(final Converter<T,U> converter,
																			 final Class<T> sourceType, final Class<U> targetType) {
		return new TemporaryConverter<T, U>() {
			@Override public boolean isSupportSourceType(Class cls) { return cls == getSourceType(); }
			@Override public boolean isSupportTargetType(Class cls) { return cls == getTargetType(); }

			@Override public Class getSourceType() { return sourceType; }
			@Override public Class getTargetType() { return targetType; }

			@Override public boolean convertCustomQuery() {
				Converter   converter = tryGetConverterOrCreatedTemp(sourceType, targetType);
				if (null == converter) {
					return false;
				}
				return converter.convertCustomQuery(); 
			}
			@Override public U convert(T sourceObject, Object query) {
				if (null == sourceObject) {
					Converter converter = tryGetConverterOrCreatedTemp(null, targetType);
					return null == converter ? null : (U) converter.convert(null, query);
				} else {
					return (U) convertAsTop(sourceObject, query);
				}
			}

			@Override public String toString() {
				return "(" + "array-convert-yobject: (" + sourceType + " -> " + targetType  + ")";
			}

			Object convertAsTop(@NotNull T sourceObject, Object query) {
				int sourceLength = Array.getLength(sourceObject);
				Class  targetComponent = targetType.getComponentType();
				Object targetArray     = Array.newInstance(targetComponent, sourceLength);
				for (int i = 0; i < sourceLength; i++) {
					Array.set(targetArray, i, convertAsElement(targetComponent, Array.get(sourceObject, i), query));
				}
				return targetArray;
			}
			Object convertAsElement(Class targetComponent, Object sourceElement, Object query) {
				if (sourceElement == null) {
					Converter converter = tryGetConverterOrCreatedTemp(null, targetComponent);
					return null == converter ? null : converter.convert(null, query) ; 
				} else {
					Class sourceElementClass = sourceElement.getClass();
					if (sourceElementClass.isArray()) {
						int sourceElementLen = Array.getLength(sourceElement);
						Object newarr = Array.newInstance(targetComponent.getComponentType(), sourceElementLen);
						for (int i = 0; i < sourceElementLen; i++) {
							Array.set(newarr, i, convertAsElement(targetComponent, Array.get(sourceElement, i), query));
						}
						return newarr;
					} else {
						Converter converter = tryGetConverterOrCreatedTemp(sourceElementClass, targetComponent);
						requireNonNullConverter(converter, 
												sourceElementClass, targetComponent);
						return converter.convert(sourceElement, query) ;
					}

				}
			}
		};
	}
	/**
	 * 只获取一次转换器，运行时不会获取
	 */
	private <T,U> Converter<T,U> createTempConverterFromNotObjectArraySource(final Converter<T,U> converter,
																			 final Class<T> sourceType, final Class<U> targetType) {
		//is inherit match
		double originalAflSource = 0;
		double originalAflTarget = 0;
		if (null != converter) {
			if (!converter.isTemporaryConverter()) { // non-temp-converter
				return null;
			}

			originalAflSource = calcAssignableFromLevel(converter.getSourceType(), sourceType);
			originalAflTarget = calcAssignableFromLevel(converter.getTargetType(), targetType);
		}
		Object[] lowComponents = geLowestLevelComponentType(sourceType, targetType);
		Class sourceLowCompont = (Class) lowComponents[0];
		Class taegetLowCompont = (Class) lowComponents[1];
		final int deepLimit = (int) lowComponents[2];
		if (deepLimit == 0) {
			throw new UnsupportedOperationException("deep == 0"); //? !!!
		}

		final Converter lowCompontConverter = tryMatchConverterOrCreateTemp(sourceLowCompont, taegetLowCompont, 
																			false);
		if (null != lowCompontConverter) {
			final Converter[] deepConverters = new Converter[1 + deepLimit]; // self + deep
			{
				int index = 0;
				deepConverters[index++] = tryMatchConverterOrCreateTemp(null, targetType, true); //null -> slef
				Class component = targetType.getComponentType();
				for (int i = 0; i < deepLimit;i++) {
					deepConverters[index++] = tryMatchConverterOrCreateTemp(null, component,   true); //null -> element
					component = component.getComponentType();
				}
			}

			final Converter nullTopConverter = deepConverters[0]; //self
			final TemporaryConverter tempArrayConverter = new TemporaryConverter<T, U>() {
				@Override public boolean isSupportSourceType(Class cls) { return cls == getSourceType(); }
				@Override public boolean isSupportTargetType(Class cls) { return cls == getTargetType(); }

				@Override public Class getSourceType() { return sourceType; }
				@Override public Class getTargetType() { return targetType; }

				@Override public boolean convertCustomQuery() { return false; }
				@Override public U convert(T sourceObject, Object query) {
					return null == sourceObject 
						? (null == nullTopConverter ? null : (U) nullTopConverter.convert(sourceObject, query)) 
						: ((U) convertTop(sourceObject, query));
				}

				@Override public String toString() {
					return "(" + "array-convert-nobject: (" + sourceType + " -> " + targetType  + ") from " + lowCompontConverter + ")";
				}

				Object convertTop(@NotNull T sourceObject, Object query) {
					int sourceLength = Array.getLength(sourceObject);
					Class  targetComponent = targetType.getComponentType();
					Object targetArray     = Array.newInstance(targetComponent, sourceLength);
					for (int i = 0; i < sourceLength; i++) {
						Array.set(targetArray, i, convertDeepElement((1), targetComponent, Array.get(sourceObject, i), query));
					}
					return targetArray;
				}
				Object convertDeepElement(int deepLevel, Class targetComponent, Object sourceElement, Object query) {
					if (deepLevel == deepLimit) {
						return lowCompontConverter.convert(sourceElement, query);
					} else {
						if (sourceElement == null) {
							Converter converter = deepConverters[deepLevel]; //System.out.println("found: (null -> "+compoment+") result: " + c + " deep=" + deepLevel + Arrays.toString(deeps));
							return null == converter ? null : converter.convert(null, query) ; // get converter？
						} else {
							Class cls = sourceElement.getClass();
							if (cls.isArray()) {
								int sourceElementLen = Array.getLength(sourceElement);
								Object newarr = Array.newInstance(targetComponent.getComponentType(), sourceElementLen);
								for (int i = 0; i < sourceElementLen; i++) {
									Array.set(newarr, i, convertDeepElement((deepLevel + 1), targetComponent, Array.get(sourceElement, i), query));
								}
								return newarr;
							} else {
								throw new UnsupportedOperationException();//? !!!
							}

						}
					}
				}
			};
			if (null == converter) {
				return tempArrayConverter;
			} else {
				double cvtAflSource = calcAssignableFromLevel(lowCompontConverter.getSourceType(), sourceLowCompont);//err?
				double cvtAflTarget = calcAssignableFromLevel(lowCompontConverter.getTargetType(), taegetLowCompont);//err?
				if ((cvtAflSource + cvtAflTarget) < (originalAflSource + originalAflTarget)) {
					return tempArrayConverter;
				}
			}
		}
		return null;
	}
	/**
	 * 只获取一次转换器，运行时不会获取
	 * 它是等效的
	 */
	private <T,B, U> Converter<T,U> createTempConvertFromIntermediate(final Converter<T,B> smc, final Converter<B,U> smmtc) {
		return new TemporaryConverter<T,U>() {
			@Override public boolean isSupportSourceType(Class cls) { return smc.isSupportSourceType(cls);   }
			@Override public boolean isSupportTargetType(Class cls) { return smmtc.isSupportTargetType(cls); }

			@Override public Class getSourceType() { return smc.getSourceType(); }
			@Override public Class getTargetType() { return smmtc.getTargetType(); }


			@Override
			public boolean convertCustomQuery() {
				// TODO: Implement this method
				return smc.convertCustomQuery() || smmtc.convertCustomQuery();
			}
			@Override
			public U convert(T object, Object query) {
				// TODO: Implement this method
				B m = smc.convert(object, query);
				U r = smmtc.convert(m, query);
				return r;
			}

			@Override public String toString() {
				return "(" + "relay: " + smc + " -> " + smmtc + ")";
			}
		};
	}
	/**
	 * 只获取一次转换器，运行时不会获取
	 */
	protected <T,U> Converter<T,U> createTempConvertFromNull() {
		return new TemporaryConverter<T,U>() {
			@Override public boolean isSupportSourceType(Class cls) { return cls == getSourceType(); }
			@Override public boolean isSupportTargetType(Class cls) { return cls == getTargetType(); }

			@Override public Class getSourceType() { return null; }
			@Override public Class getTargetType() { return null; }

			@Override public boolean convertCustomQuery() { 
				return false;
			}
			@Override public U convert(T object, Object query) {
				return null; 
			}

			@Override public String toString() {
				return "(" + "null:" + null + " -> " + null + ")";
			}
		};
	}
	/**
	 * 只获取一次转换器，运行时不会获取
	 */
	protected static <T,U> Converter<T,U> createTempConverterFromDirectReturn(final Class<T> sourceType, final Class<U> targetType) {
		return new TemporaryConverter<T,U>(){
			@Override public boolean isSupportSourceType(Class cls) { return cls == getSourceType(); }
			@Override public boolean isSupportTargetType(Class cls) { return cls == getTargetType(); }

			@Override public Class getSourceType()  { return sourceType; }
			@Override public Class  getTargetType() { return targetType; }

			@Override public boolean convertCustomQuery() {
				return false;
			}
			@Override public U convert(T object, Object query) { 
				return (U) object; 
			}

			@Override public String toString() {
				return "(" + "direct: " + sourceType + " -> " + targetType + ")";
			}
		};
	};



	TemporaryMapValueModCacheTwoKey<Class, Class,
	Converter, RuntimeException> getConverterOrCreatedTempGetterCached = new TemporaryMapValueModCacheTwoKey<Class, Class,
	Converter, RuntimeException>() {
		@Override
		public Converter fromMapGetValue(Class source, Class target) throws RuntimeException {
			// TODO: Implement this method
			return tryMatchConverterOrCreateTemp(source, target);
		}
	};
	public TemporaryMapValueModCacheTwoKey<Class, Class, Converter, RuntimeException>.ValueGetter getConverterOrCreatedTempGetter(Class source, Class target) {
		return getConverterOrCreatedTempGetterCached.createValueGetter(source, target);
	}





//	final WeakMapCacheConcurrentHash<DoubleKey, WrapValue<Converter>, RuntimeException> tryGetConverterOrCreatedTempCached = new WeakMapCacheConcurrentHash<DoubleKey, WrapValue<Converter>, RuntimeException>(){
//		@Override
//		public WrapValue<Converter> newCache(DataTypeConverter.DoubleKey p1) throws RuntimeException {
//			// TODO: Implement this method
//			return new WrapValue<Converter>(tryMatchConverterOrCreateTemp((Class)p1.k1, (Class)p1.k2));
//		}
//	};
//	public <T, U> Converter<T,U> tryGetConverterOrCreatedTemp(Class<T> sourceType, Class<U> targetType) {
//		return tryGetConverterOrCreatedTempCached
//			.getOrCreateCache(new DoubleKey(sourceType, targetType))
//			.value;
//	}


	final static class TryGetConverterOrCreatedTempCached extends WeakCache<Map<Class, Map<Class, WrapValue<Converter>>>, RuntimeException> {
		final static <K,V> Map<K,V> createWriteLockMap() {
			return new HashMap<K,V>() {
				@Override
				public V get(Object k) {
					return super.get(k);
				}
				@Override
				public V put(K k, V v) {
					synchronized (this) { return super.put(k, v); }
				}
			};
		}
		@Override
		public Map<Class, Map<Class, WrapValue<Converter>>> createCache() throws RuntimeException {
			// TODO: Implement this method
			return createWriteLockMap();
		}
	}
	final TryGetConverterOrCreatedTempCached tryGetConverterOrCreatedTempCached = new TryGetConverterOrCreatedTempCached();



	public <T, U> Converter<T,U> tryGetConverterOrCreatedTemp(Class<T> sourceType, Class<U> targetType) {
		Map<Class, Map<Class, WrapValue<Converter>>> table = tryGetConverterOrCreatedTempCached.getOrCreateCache();
		Map<Class, WrapValue<Converter>> map = table.get(sourceType);
		WrapValue<Converter> wv;
		if (null == map)
			table.put(sourceType, map = tryGetConverterOrCreatedTempCached.createWriteLockMap());
		if (null == (wv = map.get(targetType))) 
			map.put(targetType, wv = new WrapValue<Converter>(tryMatchConverterOrCreateTemp(sourceType, targetType)));
		return wv.value;
	}



	public <T, U> Converter<T,U> getConverterOrCreatedTemp(Class<T> sourceType, Class<U> targetType) {
		return requireNonNullConverter(tryGetConverterOrCreatedTemp(sourceType, targetType), sourceType, targetType);
	}
	public <T, U> Converter<T,U> getConverterOrCreatedTemp(T source, Class<U> targetType) {
		return getConverterOrCreatedTemp((Class) (null == source ? null : source.getClass()), targetType);
	}







	public static Converter requireNonNullConverter(Converter converter, Class sourceType, Class targetType) {
		if (converter == null) 
            throw new NotFoundConverterException("no converter found for source type " + "'" + (null == sourceType ?null: sourceType.getCanonicalName()) + "'" + " -> " + "'" + (null == targetType ?null: targetType.getCanonicalName()) + "'");
		return converter;
	}











	static protected final Map<Class<?>, Map<Class<?>, Converter>> DEFAULT_CONVERTS = new LinkedHashMap<>();
	static <T, U> void addConverterToNewInstanceBefore(Converter<T,U> converter) {
		converter  = Objects.requireNonNull(converter, "converter");
		addConverterToNewInstanceBefore(converter.getSourceType(), 
										converter.getTargetType(), 
										converter);
	}
	static void addConverterToNewInstanceBefore(Class sourceType, Class targetType, Converter converter) {
		converter  = Objects.requireNonNull(converter, "converter");
		addConverterToTable(DEFAULT_CONVERTS,
							sourceType, targetType, 
							converter);
	}


	static {
		Class[] primitiveToStringClass = {
			int.class,   long.class, byte.class, short.class,
			float.class, double.class, 
			char.class,  boolean.class
		};
		Class[] wrapperToStringClass = new Class[primitiveToStringClass.length];
		for (int i = 0;i < primitiveToStringClass.length;i++) {
			Class wrap = Boxing.toWrapperType(primitiveToStringClass[i]);
			if (null == wrap) {
				throw new UnsupportedOperationException("" + primitiveToStringClass[i]);
			}
			wrapperToStringClass[i] = wrap;
		}
		{
			Class[] numberPrimitiveAndWrapperClass = Arrayz.marge(primitiveToStringClass, wrapperToStringClass);
			for (final Class c: Arrayz.marge(new Class[]{String.class}, Arrayz.marge(new Class[]{Number.class}, numberPrimitiveAndWrapperClass))) {
				addConverterToNewInstanceBefore(new Converter<Object, String>() {
						@Override
						public Class getSourceType() { return c; }
						@Override
						public Class getTargetType() { return String.class; }
						@Override
						public String convert(Object object, Object query) {
							// TODO: Implement this method
							return null == object ? null : object.toString();
						}
					});
			}

			addConverterToNewInstanceBefore(new Converter<Object, String>() {
					@Override public Class getSourceType() { return Object.class; }
					@Override public Class getTargetType() { return String.class; }

					@Override
					public String convert(Object object, Object query) {
						// TODO: Implement this method
						return null == object ? null: object.toString();
					}
				});
			addConverterToNewInstanceBefore(new Converter<Object, String>() {
					@Override public Class getSourceType() { return null; }
					@Override public Class getTargetType() { return String.class; }

					@Override
					public String convert(Object object, Object query) {
						// TODO: Implement this method
						return null;
					}
				});
		}


		Class[] primitiveNumberToStringClass = {
			int.class, long.class, byte.class, short.class,
			float.class, double.class
		};
		Class[] wrapperNumberToStringClass = new Class[primitiveNumberToStringClass.length];
		for (int i = 0;i < primitiveNumberToStringClass.length;i++) {
			Class wrap = Boxing.toWrapperType(primitiveNumberToStringClass[i]);
			if (null == wrap)
				throw new UnsupportedOperationException("" + primitiveNumberToStringClass[i]);
			if (Number.class.isAssignableFrom(wrap))	
				wrapperNumberToStringClass[i] = wrap;
			else 
				throw new UnsupportedOperationException("" + wrap);
		}
		{//number and extends number and primitive-number
			Class[] numberPrimitiveAndWrapperClass = Arrayz.marge(primitiveNumberToStringClass, wrapperNumberToStringClass);
			for (final Class wp: Arrayz.marge(new Class[]{null, Number.class}, numberPrimitiveAndWrapperClass)) {
				for (final Class c: new Class[]{int.class, Integer.class}) {
					addConverterToNewInstanceBefore(new Converter<Number, Integer>() {
							@Override public Class getSourceType() { return wp; }
							@Override
							public Class getTargetType() { return c; }

							@Override
							public Integer convert(Number object, Object query) {
								// TODO: Implement this method
								return null == object ? (c.isPrimitive() ? (int) 0: null): object instanceof Integer ? (Integer) object: object.intValue();
							}
						});
				}
				for (final Class c: new Class[]{long.class, Long.class}) {
					addConverterToNewInstanceBefore(new Converter<Number, Long>() {
							@Override public Class getSourceType() { return wp; }
							@Override
							public Class getTargetType() { return c; }

							@Override
							public Long convert(Number object, Object query) {
								// TODO: Implement this method
								return null == object ? (c.isPrimitive() ? (long) 0: null): object instanceof Long ? (Long) object: object.longValue();
							}
						});
				}
				for (final Class c: new Class[]{byte.class, Byte.class}) {
					addConverterToNewInstanceBefore(new Converter<Number, Byte>() {
							@Override public Class getSourceType() { return wp; }
							@Override
							public Class getTargetType() { return c; }

							@Override
							public Byte convert(Number object, Object query) {
								// TODO: Implement this method
								return null == object ? (c.isPrimitive() ? (byte) 0: null): object instanceof Byte ? (Byte) object: object.byteValue();
							}
						});
				}
				for (final Class c: new Class[]{short.class, Short.class}) {
					addConverterToNewInstanceBefore(new Converter<Number, Short>() {
							@Override public Class getSourceType() { return wp; }
							@Override
							public Class getTargetType() { return c; }

							@Override
							public Short convert(Number object, Object query) {
								// TODO: Implement this method
								return null == object ? (c.isPrimitive() ? (short) 0: null): object instanceof Short ? (Short) object: object.shortValue();
							}
						});
				}
				for (final Class c: new Class[]{float.class, Float.class}) {
					addConverterToNewInstanceBefore(new Converter<Number, Float>() {
							@Override public Class getSourceType() { return wp; }
							@Override
							public Class getTargetType() { return c; }

							@Override
							public Float convert(Number object, Object query) {
								// TODO: Implement this method
								return null == object ? (c.isPrimitive() ? (float) 0: null): object instanceof Float ? (Float) object: object.floatValue();
							}
						});
				}
				for (final Class c: new Class[]{double.class, Double.class}) {
					addConverterToNewInstanceBefore(new Converter<Number, Double>() {
							@Override public Class getSourceType() { return wp; }
							@Override
							public Class getTargetType() { return c; }

							@Override
							public Double convert(Number object, Object query) {
								// TODO: Implement this method
								return null == object ? (c.isPrimitive() ? (double) 0: null) : object instanceof Double ? (Double) object: object.doubleValue();
							}
						});

				}
				for (final Class c: new Class[]{char.class, Character.class}) {
					addConverterToNewInstanceBefore(new Converter<Number, Character>() {
							@Override public Class getSourceType() { return wp; }
							@Override
							public Class getTargetType() { return c; }

							@Override
							public Character convert(Number object, Object query) {
								// TODO: Implement this method
								return null == object ? (c.isPrimitive() ? (char) 0: null): (char) object.intValue();
							}
						});
				}
				for (final Class c: new Class[] {boolean.class, Boolean.class}) {
					addConverterToNewInstanceBefore(new Converter<Number, Boolean>() {
							@Override public Class getSourceType() { return wp; }
							@Override
							public Class getTargetType() { return c; }

							@Override
							public Boolean convert(Number object, Object query) {
								// TODO: Implement this method
								return null == object ? (c.isPrimitive() ? false: null) : object.intValue() != 0;
							}
						});
				}
			}
		}

		//char
		for (final Class wp: new Class[]{char.class, Character.class}) {
			for (final Class c: new Class[]{int.class, Integer.class}) {
				addConverterToNewInstanceBefore(new Converter<Character, Integer>() {
						@Override public Class getSourceType() { return wp; }
						@Override public Class getTargetType() { return c; }

						@Override
						public Integer convert(Character object, Object query) {
							// TODO: Implement this method
							return null == object ? (c.isPrimitive() ? (int) 0: null): (int) object.charValue();
						}
					});
			}
			for (final Class c: new Class[]{long.class, Long.class}) {
				addConverterToNewInstanceBefore(new Converter<Character, Long>() {
						@Override public Class getSourceType() { return wp; }
						@Override public Class getTargetType() { return c; }

						@Override
						public Long convert(Character object, Object query) {
							// TODO: Implement this method
							return null == object ? (c.isPrimitive() ? (long) 0: null): (long) object.charValue();
						}
					});
			}
			for (final Class c: new Class[]{byte.class, Byte.class}) {
				addConverterToNewInstanceBefore(new Converter<Character, Byte>() {
						@Override public Class getSourceType() { return wp; }
						@Override public Class getTargetType() { return c; }

						@Override
						public Byte convert(Character object, Object query) {
							// TODO: Implement this method
							return null == object ? (c.isPrimitive() ? (byte) 0: null): (byte) object.charValue();
						}
					});
			}
			for (final Class c: new Class[]{short.class, Short.class}) {
				addConverterToNewInstanceBefore(new Converter<Character, Short>() {
						@Override public Class getSourceType() { return wp; }
						@Override public Class getTargetType() { return c; }

						@Override
						public Short convert(Character object, Object query) {
							// TODO: Implement this method
							return null == object ? (c.isPrimitive() ? (short) 0: null): (short) object.charValue();
						}
					});
			}
			for (final Class c: new Class[]{float.class, Float.class}) {
				addConverterToNewInstanceBefore(new Converter<Character, Float>() {
						@Override public Class getSourceType() { return wp; }
						@Override public Class getTargetType() { return c; }

						@Override
						public Float convert(Character object, Object query) {
							// TODO: Implement this method
							return null == object ? (c.isPrimitive() ? (float) 0: null): (float) object.charValue();
						}
					});
			}
			for (final Class c: new Class[]{double.class, Double.class}) {
				addConverterToNewInstanceBefore(new Converter<Character, Double>() {
						@Override public Class getSourceType() { return wp; }
						@Override public Class getTargetType() { return c; }

						@Override
						public Double convert(Character object, Object query) {
							// TODO: Implement this method
							return null == object ? (c.isPrimitive() ? (double) 0: null): (double) object.charValue();
						}
					});

			}
			for (final Class c: new Class[]{char.class, Character.class}) {
				addConverterToNewInstanceBefore(new Converter<Character, Character>() {
						@Override public Class getSourceType() { return wp; }
						@Override public Class getTargetType() { return c; }

						@Override
						public Character convert(Character object, Object query) {
							// TODO: Implement this method
							return null == object ? (c.isPrimitive() ? (char) 0: null): object;
						}
					});
			}
			for (final Class c: new Class[] {boolean.class, Boolean.class}) {
				addConverterToNewInstanceBefore(new Converter<Character, Boolean>() {
						@Override public Class getSourceType() { return wp; }
						@Override public Class getTargetType() { return c; }

						@Override
						public Boolean convert(Character object, Object query) {
							// TODO: Implement this method
							return null == object ? (c.isPrimitive() ? false : null) : (object.charValue() != 0);
						}
					});
			}
		}

		//boolean
		for (final Class wp: new Class[] {boolean.class, Boolean.class}) {
			for (final Class c: new Class[]{int.class, Integer.class}) {
				addConverterToNewInstanceBefore(new Converter<Boolean, Integer>() {
						@Override public Class getSourceType() { return wp; }
						@Override public Class getTargetType() { return c; }

						@Override
						public Integer convert(Boolean object, Object query) {
							// TODO: Implement this method
							return null == object ? (c.isPrimitive() ? (int) 0: null): object ? 1 : 0;
						}
					});
			}
			for (final Class c: new Class[]{long.class, Long.class}) {
				addConverterToNewInstanceBefore(new Converter<Boolean, Long>() {
						@Override public Class getSourceType() { return wp; }
						@Override public Class getTargetType() { return c; }

						@Override
						public Long convert(Boolean object, Object query) {
							// TODO: Implement this method
							return null == object ? (c.isPrimitive() ? (long) 0: null): object ? 1L : 0L;
						}
					});
			}
			for (final Class c: new Class[]{byte.class, Byte.class}) {
				addConverterToNewInstanceBefore(new Converter<Boolean, Byte>() {
						@Override public Class getSourceType() { return wp; }
						@Override public Class getTargetType() { return c; }

						@Override
						public Byte convert(Boolean object, Object query) {
							// TODO: Implement this method
							return null == object ? (c.isPrimitive() ? (byte) 0: null): object ? (byte) 1 : (byte) 0;
						}
					});
			}
			for (final Class c: new Class[]{short.class, Short.class}) {
				addConverterToNewInstanceBefore(new Converter<Boolean, Short>() {
						@Override public Class getSourceType() { return wp; }
						@Override public Class getTargetType() { return c; }

						@Override
						public Short convert(Boolean object, Object query) {
							// TODO: Implement this method
							return null == object ? (c.isPrimitive() ? (short) 0: null): object ? (short) 1 : (short) 0;
						}
					});
			}
			for (final Class c: new Class[]{float.class, Float.class}) {
				addConverterToNewInstanceBefore(new Converter<Boolean, Float>() {
						@Override public Class getSourceType() { return wp; }
						@Override public Class getTargetType() { return c; }

						@Override
						public Float convert(Boolean object, Object query) {
							// TODO: Implement this method
							return null == object ? (c.isPrimitive() ? (float) 0: null): object ? 1F : 0F;
						}
					});
			}
			for (final Class c: new Class[]{double.class, Double.class}) {
				addConverterToNewInstanceBefore(new Converter<Boolean, Double>() {
						@Override public Class getSourceType() { return wp; }
						@Override public Class getTargetType() { return c; }

						@Override
						public Double convert(Boolean object, Object query) {
							// TODO: Implement this method
							return null == object ? (c.isPrimitive() ? (double) 0: null): object ? 1D : 0D;
						}
					});

			}
			for (final Class c: new Class[]{char.class, Character.class}) {
				addConverterToNewInstanceBefore(new Converter<Boolean, Character>() {
						@Override public Class getSourceType() { return wp; }
						@Override public Class getTargetType() { return c; }

						@Override
						public Character convert(Boolean object, Object query) {
							// TODO: Implement this method
							return null == object ? (c.isPrimitive() ? (char) 0: null): object ? (char) 1: (char) 0;
						}
					});
			}
			for (final Class c: new Class[] {boolean.class, Boolean.class}) {
				addConverterToNewInstanceBefore(new Converter<Boolean, Boolean>() {
						@Override public Class getSourceType() { return wp; }
						@Override public Class getTargetType() { return c; }

						@Override
						public Boolean convert(Boolean object, Object query) {
							// TODO: Implement this method
							return null == object ? (c.isPrimitive() ? false : null): object;
						}
					});
			}
		}

		//str
		for (final Class wp: new Class[] {String.class}) {
			for (final Class c: new Class[]{int.class, Integer.class}) {
				addConverterToNewInstanceBefore(new Converter<String, Integer>() {
						@Override public Class getSourceType() { return wp; }
						@Override public Class getTargetType() { return c; }

						@Override
						public Integer convert(String object, Object query) {
							// TODO: Implement this method
							return null == object || object.isEmpty() ? (c.isPrimitive() ? (int) 0: null) : Integer.parseInt(object);
						}
					});
			}
			for (final Class c: new Class[]{long.class, Long.class}) {
				addConverterToNewInstanceBefore(new Converter<String, Long>() {
						@Override public Class getSourceType() { return wp; }
						@Override public Class getTargetType() { return c; }

						@Override
						public Long convert(String object, Object query) {
							// TODO: Implement this method
							return null == object || object.isEmpty() ? (c.isPrimitive() ? (long) 0: null): Long.parseLong(object);
						}
					});
			}
			for (final Class c: new Class[]{byte.class, Byte.class}) {
				addConverterToNewInstanceBefore(new Converter<String, Byte>() {
						@Override public Class getSourceType() { return wp; }
						@Override public Class getTargetType() { return c; }

						@Override
						public Byte convert(String object, Object query) {
							// TODO: Implement this method
							return null == object || object.isEmpty() ? (c.isPrimitive() ? (byte) 0: null) : Byte.parseByte(object);
						}
					});
			}
			for (final Class c: new Class[]{short.class, Short.class}) {
				addConverterToNewInstanceBefore(new Converter<String, Short>() {
						@Override public Class getSourceType() { return wp; }
						@Override public Class getTargetType() { return c; }

						@Override
						public Short convert(String object, Object query) {
							// TODO: Implement this method
							return null == object || object.isEmpty() ? (c.isPrimitive() ? (short) 0: null): Short.parseShort(object);
						}
					});
			}
			for (final Class c: new Class[]{float.class, Float.class}) {
				addConverterToNewInstanceBefore(new Converter<String, Float>() {
						@Override public Class getSourceType() { return wp; }
						@Override public Class getTargetType() { return c; }

						@Override
						public Float convert(String object, Object query) {
							// TODO: Implement this method
							return null == object || object.isEmpty() ? (c.isPrimitive() ? (float) 0: null): Float.parseFloat(object);
						}
					});
			}
			for (final Class c: new Class[]{double.class, Double.class}) {
				addConverterToNewInstanceBefore(new Converter<String, Double>() {
						@Override public Class getSourceType() { return wp; }
						@Override public Class getTargetType() { return c; }

						@Override
						public Double convert(String object, Object query) {
							// TODO: Implement this method
							return null == object || object.isEmpty() ? (c.isPrimitive() ? (double) 0: null): Double.parseDouble(object);
						}
					});

			}
			for (final Class c: new Class[]{char.class, Character.class}) {
				addConverterToNewInstanceBefore(new Converter<String, Character>() {
						@Override public Class getSourceType() { return wp; }
						@Override public Class getTargetType() { return c; }

						@Override
						public Character convert(String object, Object query) {
							// TODO: Implement this method
							return null == object || object.isEmpty() ? (c.isPrimitive() ? (char) 0: null): top.fols.atri.lang.Objects.parseChar(object);
						}
					});
			}
			for (final Class c: new Class[] {boolean.class, Boolean.class}) {
				addConverterToNewInstanceBefore(new Converter<String, Boolean>() {
						@Override public Class getSourceType() { return wp; }
						@Override public Class getTargetType() { return c; }

						@Override
						public Boolean convert(String object, Object query) {
							// TODO: Implement this method
							return null == object || object.isEmpty() ? (c.isPrimitive() ? false: null) : Boolean.parseBoolean(object);
						}
					});
			}
		}


//		Map<String,Object> o = new LinkedHashMap<>();
//		for (Class cls: DEFAULT_CONVERTS.keySet()) {
//			Map<Class<?>, Converter> m = DEFAULT_CONVERTS.get(cls);
//			Map<String, String> ms = new LinkedHashMap<>();
//			for (Class cls2 : m.keySet()) {
//				ms.put(null==cls2?"null":Classz.getCanonicalName(cls2), m.get(cls2).toString());
//			}
//			o.put(null==cls?"null":Classz.getCanonicalName(cls), ms);
//		}
//		System.out.println(new JSONObject(o).toFormatString());
	}


	private final Map<Class<?>, Map<Class<?>, Converter>> convertsLock = new LinkedHashMap<>();

	//default-primitive:
	{
		convertsLock.putAll(cloneConverterMapDeeply(DEFAULT_CONVERTS));
	}

	//default-date:
	{
		final DateFormatDetector detector = new DateFormatDetector();

		addConverter(null, Date.class, new DataTypeConverter.Converter<Object, Date>() {
			@Override public Class<Object> getSourceType() { return null; }

			@Override
			public Date convert(Object object, Object query) throws DataTypeConverter.DataConvertException {
				// TODO: Implement this method
				return null;
			}
		});
		addConverter(Number.class, Date.class, new DataTypeConverter.Converter<Number, Date>() {
			@Override
			public Date convert(Number object, Object query) throws DataTypeConverter.DataConvertException {
				// TODO: Implement this method
				if (null == object)
					return null;
				return new Date(object.longValue());
			}
		});
		addConverter(String.class, Date.class, new DataTypeConverter.Converter<String, Date>() {
			@Override
			public Date convert(String object, Object query) {
				// TODO: Implement this method
				return detector.convert(object, query);
			}
		} );


		addConverter(Date.class, String.class, new DataTypeConverter.Converter<Date, String>() {
			@Override
			public String convert(Date object, Object query) {
				// TODO: Implement this method
				return null == object ? null : object.toString();
			}
		});
		addConverter(Date.class, Long.class, new DataTypeConverter.Converter<Date, Long>() {
			@Override
			public Long convert(Date object, Object query) {
				// TODO: Implement this method
				return null == object ? null : object.getTime();
			}
		});
		addConverter(Date.class, long.class, new DataTypeConverter.Converter<Date, Long>() {
			@Override
			public Class getTargetType() { return long.class; }
			@Override
			public Long convert(Date object, Object query) {
				// TODO: Implement this method
				return null == object ? 0L : object.getTime();
			}
		});
	}


}

