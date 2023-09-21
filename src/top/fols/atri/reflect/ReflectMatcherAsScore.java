package top.fols.atri.reflect;


import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import top.fols.atri.cache.WeakCache;
import top.fols.atri.cache.WeakMapCacheConcurrentHash;
import top.fols.atri.lang.Arrayz;
import top.fols.atri.lang.Boxing;
import top.fols.atri.lang.Finals;
import top.fols.atri.util.Lists;
import top.fols.atri.lang.Classz;


@SuppressWarnings({"rawtypes", "RedundantSuppression"})
public class ReflectMatcherAsScore<T extends ReflectCache> extends ReflectMatcher<T> {
	public ReflectMatcherAsScore(T cache) {
		super(cache);
	}


	public static class MatchScore {
		static final Class   OBJECT_CLAAS                 = Finals.OBJECT_CLASS;
		static final Class[] OBJECT_ARRAY_CLAAS_INTERFACE = Finals.OBJECT_ARRAY_CLASS.getInterfaces();


		static void addObjectClassToRight(final Map<Integer, Set<Class>> result,
										  final Set<Class> added, final Class baseClass) {
			if (baseClass != OBJECT_CLAAS) {
				//把OBJECT_CLAAS推到最后
				if (added.contains(OBJECT_CLAAS)) {
					added.remove(OBJECT_CLAAS);
					for (Set<Class> classes: result.values()) {
						if (classes.remove(OBJECT_CLAAS)) {
							break;
						}
					}
				}
				int addIndex =  result.size();
				if (addIndex >= result.size()) {
					result.put(addIndex, new LinkedHashSet<Class>());
				}
				Set<Class> last = result.get(addIndex);
				last.add(OBJECT_CLAAS);
				result.put(addIndex, last);
			}
		}
		@SuppressWarnings({"rawtypes"})
		static void getSuperclassGroupLevel0PutResult(final Map<Integer, Set<Class>> result, 
													  final Set<Class> added, final Class baseClass, int i, 
													  Class aClass) {
			if (null == aClass) return;

			Class aClassSuperclass = aClass.getSuperclass();
			Class[] interfaces     = aClass.getInterfaces();
			if (null == aClassSuperclass &&
				0    == interfaces.length) return;

			Set<Class>  classes = result.get(i);
			if (null == classes)
				result.put(i, classes = new LinkedHashSet<Class>() {
							   @Override public boolean add(Class cls) {
								   if (cls != baseClass) {
									   if (!added.contains(cls)) {
										   added.add(cls);
										   super.add(cls);
									   }
									   if (super.remove(OBJECT_CLAAS)) {
										   super.add(OBJECT_CLAAS);
									   }
									   return true;
								   }
								   return false;
							   }

						   });

			if (null != aClassSuperclass)
				classes.add(aClassSuperclass);
			for (Class anInterface : interfaces) 
				classes.add(anInterface);

			i++;

			if (null != aClassSuperclass)
				getSuperclassGroupLevel0PutResult(result, added, baseClass, i, aClassSuperclass);
			for (Class anInterface : interfaces) 
				getSuperclassGroupLevel0PutResult(result, added, baseClass, i, anInterface);
		}

		//对所有父类进行分组(按层级)  从0开始
		WeakMapCacheConcurrentHash<Class, Class[][], RuntimeException> getSuperclassGroupLevelCache = new WeakMapCacheConcurrentHash<Class, Class[][], RuntimeException>(){
			@Override
			public Class[][] newCache(Class cls) throws RuntimeException {
				// TODO: Implement this method
				Map<Integer, Set<Class>> result = new LinkedHashMap<>();
				Set<Class> added = new LinkedHashSet<Class>();
				if (null != cls) {
					if (cls.isArray()) {
						Class root = Arrayz.getRootComponentType(cls);
						int d      = Arrayz.dimension(cls);

						final Class filter = root;
						getSuperclassGroupLevel0PutResult(result, added, filter, 0,
														  root);

						// replace result up dimension
						for (int i = 0; i < result.size(); i++) {
							Set<Class> newClasses = new LinkedHashSet<>();

							Set<Class> classes = result.get(i);
							for (Class clazz: classes) {
								newClasses.add(Arrayz.dimensionClass(clazz, d));
							}

							result.put(i, newClasses);
						}

						int addIndex =  0;
						if (addIndex >= result.size()) {
							result.put(addIndex, new LinkedHashSet<Class>());
						}
						Set<Class> last = result.get(addIndex);

						last.addAll(Lists.asLinkedHashSet(OBJECT_ARRAY_CLAAS_INTERFACE)); //[Cloneable.class, Serialable.class]
						result.put(addIndex, last);

						addObjectClassToRight(result, added, cls);
					} else {
						final Class filter = cls;
						getSuperclassGroupLevel0PutResult(result,  added, filter, 0,
														  cls);
						addObjectClassToRight(result, added, filter);
					}
				}
				Class[][] finalReturn = new Class[result.size()][];
				for (int i = 0; i < result.size(); i++) {
					Set<Class> classesSet = result.get(i);
					finalReturn[i] = (null == classesSet ? Finals.EMPTY_CLASS_ARRAY : classesSet.toArray(Finals.EMPTY_CLASS_ARRAY));
				}
				return finalReturn;
			}
		};
		//low
		Class[][] getSuperclassGroupLevelFromCache(Class cls) {
			return getSuperclassGroupLevelCache.getOrCreateCache(cls);
		}

		@SuppressWarnings({"rawtypes", "unchecked"})
		public double calcAssignableFromLevel(Class parentClass, Class thisClass) {
			double level = SCORE_FULL_MATCH;
			if (parentClass == thisClass) { return level = SCORE_FULL_MATCH; }

			if (null == parentClass || null == thisClass) {
				throw new NullPointerException();
			}
			if (!parentClass.isAssignableFrom(thisClass)) {
				throw new UnsupportedOperationException("!parentClass.isAssignableFrom(thisClass)");
			}

			Class s = thisClass.getSuperclass();
			if (null == s) { // thisClass is java.lang.Object
				if (parentClass != OBJECT_CLAAS)
					throw new UnsupportedOperationException("parentClass != OBJECT_CLAAS");
				return level = SCORE_FULL_MATCH;
			}

			if (parentClass.isArray() || thisClass.isArray()) {
				Class pt = parentClass, tt = thisClass;
				Class pc = pt.getComponentType(), tc=tt.getComponentType();
				while ((null != pc) && 
					   (null != tc) &&
					   pc.isAssignableFrom(tc)) {
					parentClass = pc;
					thisClass   = tc;
					pc = pc.getComponentType();
					tc = tc.getComponentType();
				}
			}

			level ++; 
			Class[][] supers = getSuperclassGroupLevelFromCache(thisClass);
			M: 	{
				for (int i = 0; i < supers.length; i++) {
					Class[] superclass = supers[i];
					for (int j = 0; j < superclass.length;j++) {
						Class compare = superclass[j];
						if (compare == parentClass) {

							double cc = j * 0.1D;
							if (cc >= 1D)
								cc = 0.9D + (j * 0.00001D);
							if (cc > 0.99999D)
								cc = 0.99999D;
							level += i;
							level += cc;

//							System.out.println(thisClass);
//							System.out.println(compare);
//							System.out.println(":"+level);
							break M;
						}
					}
				}
				throw new UnsupportedOperationException("not found: " + thisClass + ", from: " + Arrays.deepToString(supers));
			}
			return level;
		}





		//----DataTypeConverter


		@SuppressWarnings({"rawtypes", "unchecked", "ConstantConditions"})
		public double calcMethodParamTypeMatchOffsetScore0(Class methodParamType, Class argsType) {
			double level = SCORE_FULL_MATCH;
			if (methodParamType == argsType) { return level = SCORE_FULL_MATCH; } //equals 
			if (methodParamType == null) {
				throw new NullPointerException("methodParamType == null");
			}
			if (null == argsType) {
				if (!methodParamType.isPrimitive())
					return level = 0.2 + 1D;// 1. args(null) -> method(Object)  is ok
				return level = SCORE_UNMATCH;
			}
			M: {
				boolean pPrimitive = methodParamType.isPrimitive();
				boolean aPrimitive = argsType.isPrimitive();
				if (pPrimitive || aPrimitive) {
					if (pPrimitive && aPrimitive) {
						//unreachable
//				throw new UnsupportedOperationException(methodParamType + " != " + argsClass);
						return level = SCORE_UNMATCH; // no equals primitive class
					} else if (pPrimitive) {
						if (methodParamType == Boxing.toPrimitiveType(argsType)) {
							return level = 0.2 + 1D; // args(Integer) -> method(int)
						}
					} else if (aPrimitive) {
						if (Boxing.toPrimitiveType(methodParamType) == argsType) {
							return level = 0.1; // args(int) -> method(Integer) equals
						}
						if (methodParamType.isAssignableFrom(Boxing.toWrapperType(argsType))) {
							// >= 1.1
							return level = 0.1 + calcAssignableFromLevel(methodParamType, Boxing.toWrapperType(argsType));// args(int) -> method(Number) 
						}
					}
					return level = SCORE_UNMATCH;
				}
			}

			if (methodParamType.isAssignableFrom(argsType)) {
//				System.out.println(methodParamType);
//				System.out.println(argsType);
//				System.out.println(calcAssignableFromLevel(methodParamType, argsType));
//				System.out.println();
				return level += calcAssignableFromLevel(methodParamType, argsType);
			}

			return level = SCORE_UNMATCH;
		}



		final static class CalcMethodParamMatchScoreCached extends WeakCache<Map<Class, Map<Class, Double>>, RuntimeException> {
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
			public Map<Class, Map<Class, Double>> createCache() throws RuntimeException {
				// TODO: Implement this method
				return createWriteLockMap();
			}
		}

		CalcMethodParamMatchScoreCached calcMethodParamMatchScoreCached = new CalcMethodParamMatchScoreCached();
		public double calcMethodParamTypeMatchOffsstScore(Class argsType, Class methodParamType, boolean nullObjectCanCastToClass) {
			if (null == argsType) {
				if (!nullObjectCanCastToClass) {
					return SCORE_UNMATCH;
				}
			}

			double level = SCORE_FULL_MATCH;
			if (methodParamType == argsType) { return level = SCORE_FULL_MATCH; }

			Map<Class, Map<Class, Double>> table = calcMethodParamMatchScoreCached.getOrCreateCache();
			Map<Class, Double> map = table.get(methodParamType);
			Double wv;
			if (null == map)
				table.put(methodParamType, map = calcMethodParamMatchScoreCached.createWriteLockMap());
			if (null == (wv = map.get(argsType))) 
				map.put(argsType, wv = calcMethodParamTypeMatchOffsetScore0(methodParamType, argsType));
			return wv;
		}


		public static void sortPeakList(double[] peakMods, Object[] peakList , int off, int ed) {
//			System.out.println(Arrays.toString(peakMods));
//			System.out.println(Arrays.toString(peakList));
			int i, j;
			for (i = off; i < ed - 1; i++) {
				for (j = off; j < ed - 1 - i + off; j++) {
					if (peakMods[j] > peakMods[j + 1]) {
						double tempp = peakMods[j];
						peakMods[j] = peakMods[j + 1];
						peakMods[j + 1] = tempp;

						Object tempc = peakList[j];
						peakList[j] = peakList[j + 1];
						peakList[j + 1] = tempc;
					}
				}
			}
		}

	}
	public static final int SCORE_UNMATCH    = -1;
	public static final int SCORE_FULL_MATCH = 0 ;

	final MatchScore classScore = new MatchScore();
	public double calcMethodParamTypeMatchOffsetScore(Class argsType, Class methodParamType, boolean nullObjectCanCastToClass) {
		return classScore.calcMethodParamTypeMatchOffsstScore(
			argsType,
			methodParamType, 
			nullObjectCanCastToClass);

	}

	public double calcMethodParamTypeMatchOffsetScore(Object argsType, Class methodParamType, boolean nullObjectCanCastToClass) {
		return  classScore.calcMethodParamTypeMatchOffsstScore(
			null == argsType ? null: argsType.getClass(),
			methodParamType, 
			nullObjectCanCastToClass);
	}






//	static{
//		{
//			ReflectMatcherAsPeak peak = new ReflectMatcherAsPeak(new ReflectCache());
//			System.out.println(Arrays.deepToString(peak.getSuperclassGroupLevelFromCache(Object.class)));
//			System.out.println(Arrays.deepToString(peak.getSuperclassGroupLevelFromCache(Object[].class)));
//			System.out.println(Arrays.deepToString(peak.getSuperclassGroupLevelFromCache(Integer.class)));
//			System.out.println(Arrays.deepToString(peak.getSuperclassGroupLevelFromCache(StringBuffer[].class)));
//			System.out.println(Arrays.deepToString(peak.getSuperclassGroupLevelFromCache(int[].class)));
//
//			Method[] find = peak.methods(StringBuffer.class, null, "append", new Object[]{
//											 ""});
//			System.out.println(Joiner.on("\n").join(find));
//
//			if (true) throw new Throwables.MessageException();
//		}
//	}



	/**
	 * @param returnClass Nullable
	 * @param name Nullable
	 */
    @Override
	public Field matchField(Field[] list, Class returnClass, String name) {
        for (int i = 0; i < list.length; i++) {
            Field element = list[i];
            if ((null == returnClass || returnClass == element.getType())
				&& (null == name || name.equals(element.getName()))) {
				return element;
			}
        }
        return null;
    }





	@Override
    public Constructor matchConstructorArgumentsTypes(Constructor[] list, Class[][] listParameterTypes, boolean nullObjectCanCastToClass,
                                                      Class... paramClassArr) {
        Constructor peak = null;
        double peakScore = 0;

        int forlength = list.length;
        for (int i = 0; i < forlength; i++) {
            Constructor element = list[i];
            Class[] elementParameterTypes = listParameterTypes[i];
            if (elementParameterTypes.length == paramClassArr.length) {
                boolean b = true;
				double score = SCORE_FULL_MATCH;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
					if (!Classz.isInstance(paramClassArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass)) {
						b = false;
                        break;
					}
					double t = calcMethodParamTypeMatchOffsetScore(paramClassArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass);
                    if (t == SCORE_UNMATCH) {
                        b = false;
                        break;
                    }
					score += t;
                }
                if (b) {
					if (score == SCORE_FULL_MATCH) {
						peak = element;
                        return peak;
					}
					@SuppressWarnings("ConstantConditions")
					double v = elementParameterTypes.length == 0 ? 0 : score / elementParameterTypes.length;
					if (null == peak || v < peakScore) {
                        peak = element;
                        peakScore = v;
                    }
                }
            }
        }
        return peak;
    }
    @Override
    public Constructor[] matchConstructorsArgumentsTypes(Constructor[] list, Class[][] listParameterTypes, boolean nullObjectCanCastToClass,
                                                         Class... paramClassArr) {
        int forLength = list.length;
        if (forLength == 0) {
            return null;
        }
        Constructor[] peakList = null;
        double[] peakMods = null;

        int index = 0;
        for (int i = 0; i < forLength; i++) {
            Constructor element = list[i];
            Class[] elementParameterTypes = listParameterTypes[i];
            if (elementParameterTypes.length == paramClassArr.length) {
                boolean b = true;
				double score = SCORE_FULL_MATCH;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
					if (!Classz.isInstance(paramClassArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass)) {
						b = false;
                        break;
					}
					double t = calcMethodParamTypeMatchOffsetScore(paramClassArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass);
                    if (t == SCORE_UNMATCH) {
                        b = false;
                        break;
                    }
					score += t;
                }
                if (b) {
                    if (null == peakList) {
                        peakList = new Constructor[list.length];
                        peakMods = new double[list.length];
                    }
					@SuppressWarnings("ConstantConditions")
					double v = elementParameterTypes.length == 0 ? 0 : score / elementParameterTypes.length;
                    peakList[index] = element;
					peakMods[index] = v;
                    index++;
                }
            }
        }
        if (null != peakList) {
            //sort array
            MatchScore.sortPeakList(peakMods, peakList, 0, index);
            return Arrays.copyOf(peakList, index);
        } else {
            return null;
        }
    }





    /**
     * @param returnClass nullable
     * @param name nullable
     */
    @Override
    public Method matchMethodArgumentsTypes(Method[] list, Class[][] listParameterTypes, Class returnClass, String name, boolean nullObjectCanCastToClass,
                                            Class... paramClassArr) {
        Method peak = null;
        double peakScore = 0;

        int forLength = list.length;
        for (int i = 0; i < forLength; i++) {
            Method element = list[i];
            Class[] elementParameterTypes = listParameterTypes[i];
            if (elementParameterTypes.length == paramClassArr.length &&
				(null == returnClass || returnClass == element.getReturnType()) &&
				(null == name || name.equals(element.getName()))) {
                boolean b = true;
				double score = SCORE_FULL_MATCH;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
					if (!Classz.isInstance(paramClassArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass)) {
						b = false;
                        break;
					}
					double t = calcMethodParamTypeMatchOffsetScore(paramClassArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass);
                    if (t == SCORE_UNMATCH) {
                        b = false;
                        break;
                    }
					score += t;
                }
                if (b) {
					if (score == SCORE_FULL_MATCH) {
						peak = element;
                        return peak;
					}
					@SuppressWarnings("ConstantConditions")
					double v = elementParameterTypes.length == 0 ? 0 : score / elementParameterTypes.length;
					if (null == peak || v < peakScore) {
                        peak = element;
                        peakScore = v;
                    }
                }
            }
        }
        return peak;

    }

    /**
     * @param returnClass nullable
     * @param name nullable
     */
    @Override
    public Method[] matchMethodsArgumentsTypes(Method[] list, Class[][] listParameterTypes, Class returnClass, String name, boolean nullObjectCanCastToClass,
                                               Class... paramClassArr) {
        int forLength = list.length;
        if (forLength == 0) {
            return null;
        }
        Method[] peakList = null;
        double[] peakMods = null;

        int index = 0;
        for (int i = 0; i < forLength; i++) {
            Method element = list[i];
            Class[] elementParameterTypes = listParameterTypes[i];
            if (elementParameterTypes.length == paramClassArr.length &&
				(null == returnClass || returnClass == element.getReturnType())  &&
				(null == name || name.equals(element.getName()))) {
                boolean b = true;
				double score = SCORE_FULL_MATCH;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
					if (!Classz.isInstance(paramClassArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass)) {
						b = false;
                        break;
					}
					double t = calcMethodParamTypeMatchOffsetScore(paramClassArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass);
                    if (t == SCORE_UNMATCH) {
                        b = false;
                        break;
                    }
					score += t;
                }
                if (b) {
                    if (null == peakList) {
                        peakList = new Method[list.length];
                        peakMods = new double[list.length];
                    }
					@SuppressWarnings("ConstantConditions")
					double v = elementParameterTypes.length == 0 ? 0 : score / elementParameterTypes.length;
                    peakList[index] = element;
					peakMods[index] = v;
                    index++;
                }
            }
        }
        if (null != peakList) {
            //sort array
            MatchScore.sortPeakList(peakMods, peakList, 0, index);
            return Arrays.copyOf(peakList, index);
        } else {
            return null;
        }
    }









	/* matcher object type param */

    @Override
    public Constructor matchConstructorArguments(Constructor[] list, Class[][] listParameterTypes, boolean nullObjectCanCastToClass,
                                                 Object... paramInstanceArr) {
        Constructor peak = null;
        double peakScore = 0;

        int forLength = list.length;
        for (int i = 0; i < forLength; i++) {
            Constructor element = list[i];
            Class[] elementParameterTypes = listParameterTypes[i];
            if (elementParameterTypes.length == paramInstanceArr.length) {
                boolean b = true;
				double score = SCORE_FULL_MATCH;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
					if (!Classz.isInstance(paramInstanceArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass)) {
						b = false;
                        break;
					}
					double t = calcMethodParamTypeMatchOffsetScore(paramInstanceArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass);
                    if (t == SCORE_UNMATCH) {
                        b = false;
                        break;
                    }
					score += t;
                }
                if (b) {
					if (score == SCORE_FULL_MATCH) {
						peak = element;
                        return peak;
					}
					@SuppressWarnings("ConstantConditions")
					double v = elementParameterTypes.length == 0 ? 0 : score / elementParameterTypes.length;
					if (null == peak || v < peakScore) {
                        peak = element;
                        peakScore = v;
                    }
                }
            }
        }
        return peak;
    }

    @Override
    public Constructor[] matchConstructorsArguments(Constructor[] list, Class[][] listParameterTypes, boolean nullObjectCanCastToClass,
                                                    Object... paramInstanceArr) {
        int forLength = list.length;
        if (forLength == 0) {
            return null;
        }
        Constructor[] peakList = null;
        double[] peakMods = null;

        int index = 0;
        for (int i = 0; i < forLength; i++) {
            Constructor element = list[i];
            Class[] elementParameterTypes = listParameterTypes[i];
            if (elementParameterTypes.length == paramInstanceArr.length) {
                boolean b = true;
				double score = SCORE_FULL_MATCH;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
					if (!Classz.isInstance(paramInstanceArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass)) {
						b = false;
                        break;
					}
					double t = calcMethodParamTypeMatchOffsetScore(paramInstanceArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass);
                    if (t == SCORE_UNMATCH) {
                        b = false;
                        break;
                    }
					score += t;
                }
                if (b) {
                    if (null == peakList) {
                        peakList = new Constructor[list.length];
                        peakMods = new double[list.length];
                    }

					@SuppressWarnings("ConstantConditions")
					double v = elementParameterTypes.length == 0 ? 0 : score / elementParameterTypes.length;
                    peakList[index] = element;
					peakMods[index] = v;
                    index++;
                }
            }
        }
        if (null != peakList) {
            //sort array
            MatchScore.sortPeakList(peakMods, peakList, 0, index);
            return Arrays.copyOf(peakList, index);
        } else {
            return null;
        }
    }

    /**
     * @param returnClass nullable
     * @param name nullable
     */
    @Override
    public Method matchMethodArguments(Method[] list, Class[][] listParameterTypes, Class returnClass, String name, boolean nullObjectCanCastToClass,
                                       Object... paramInstanceArr) {
        Method peak = null;
        double peakScore = 0;

        int forLength = list.length;
        for (int i = 0; i < forLength; i++) {
            Method element = list[i];
            Class[] elementParameterTypes = listParameterTypes[i];
            if (elementParameterTypes.length == paramInstanceArr.length &&
				(null == returnClass || returnClass == element.getReturnType()) &&
				(null == name || name.equals(element.getName()))) {
                boolean b = true;
				double score = SCORE_FULL_MATCH;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
					if (!Classz.isInstance(paramInstanceArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass)) {
						b = false;
                        break;
					}
					double t = calcMethodParamTypeMatchOffsetScore(paramInstanceArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass);
                    if (t == SCORE_UNMATCH) {
                        b = false;
                        break;
                    }
					score += t;
                }
                if (b) {
					if (score == SCORE_FULL_MATCH) {
						peak = element;
                        return peak;
					}
					@SuppressWarnings("ConstantConditions")
					double v = elementParameterTypes.length == 0 ? 0 : score / elementParameterTypes.length;
					if (null == peak || v < peakScore) {
                        peak = element;
                        peakScore = v;
                    }
                }
            }
        }
        return peak;
    }

    /**
     * @param returnClass nullable
     * @param name nullable
     */
    @Override
    public Method[] matchMethodsArguments(Method[] list, Class[][] listParameterTypes, Class returnClass, String name, boolean nullObjectCanCastToClass,
                                          Object... paramInstanceArr) {
        int forLength = list.length;
        if (forLength == 0) {
            return null;
        }
        Method[] peakList = null;
        double[] peakMods = null;

        int index = 0;
        for (int i = 0; i < forLength; i++) {
            Method element = list[i];
            Class[] elementParameterTypes = listParameterTypes[i];
            if (elementParameterTypes.length == paramInstanceArr.length &&
				(null == returnClass || returnClass == element.getReturnType())  &&
				(null == name || name.equals(element.getName()))) {
                boolean b = true;
				double score = SCORE_FULL_MATCH;
                for (int i2 = 0; i2 < elementParameterTypes.length; i2++) {
					if (!Classz.isInstance(paramInstanceArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass)) {
						b = false;
                        break;
					}
					double t = calcMethodParamTypeMatchOffsetScore(paramInstanceArr[i2], elementParameterTypes[i2], nullObjectCanCastToClass);
                    if (t == SCORE_UNMATCH) {
                        b = false;
                        break;
                    }
					score += t;
                }
                if (b) {
                    if (null == peakList) {
                        peakList = new Method[list.length];
                        peakMods = new double[list.length];
                    }

					@SuppressWarnings("ConstantConditions")
					double v = elementParameterTypes.length == 0 ? 0 : score / elementParameterTypes.length;
                    peakList[index] = element;
					peakMods[index] = v;
                    index++;
                }
            }
        }
        if (null != peakList) {
            //sort array
            MatchScore.sortPeakList(peakMods, peakList, 0, index);
            return Arrays.copyOf(peakList, index);
        } else {
            return null;
        }
    }



    //****************************

    @Override  public T 	            cacher() { return super.cacher(); }


    //****************************


    @Override
    public boolean isDefault() { return this == DEFAULT; }
	public static final ReflectMatcherAsScore<ReflectCache> DEFAULT = new ReflectMatcherAsScore<ReflectCache>(ReflectCache.DEFAULT){};
}
