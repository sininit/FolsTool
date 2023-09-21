package top.fols.box.reflect.re;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import top.fols.atri.assist.json.JSONArray;
import top.fols.atri.assist.json.JSONObject;
import top.fols.atri.cache.TemporaryChangedCache;
import top.fols.atri.cache.WeakMapCacheConcurrentHash;
import top.fols.atri.lang.Classz;
import top.fols.atri.lang.Finals;
import top.fols.atri.reflect.Reflects;
import top.fols.atri.interfaces.annotations.NotNull;
import top.fols.atri.util.Lists;

import static top.fols.atri.reflect.Reflects.isOverride;

@SuppressWarnings("rawtypes")
public class Re_ReflectJavaFilter {
	protected static <T> Class<T> requireFinal(Class<T> cls) {
		if (null == cls) {
			throw new UnsupportedOperationException("null == class");
		}
		if (Modifier.isFinal(cls.getModifiers())) {
			return cls;
		} else {
			throw new UnsupportedOperationException("not a final class!!!: " + cls);
		}
	}


	public Re_ReflectJavaFilter() {}





	@SuppressWarnings("UnnecessaryInterfaceModifier")
	interface Filter<T extends SubQuery> {
		public Field[]  filterFields(Field[] all, T query);
		public Method[] filterMethods(Method[] all, T query);
	}


	static class QueryResource {
		Field[]    oldFields;
		Method[]   oldMethods;

		Field[]    queryClassFields;
		Method[]   queryClassMethods;

		public QueryResource(Field[]  queryClassFields, 
							 Method[] queryClassMethods) {
			this.oldFields  = queryClassFields.clone();
			this.oldMethods = queryClassMethods.clone();
		}
	}
	static class SubQuery {
		Filter         filter;
		Class      queryClass;
		Set<Class> matchClass;

		public SubQuery(
			Filter filter, 
			Class queryClass, Set<Class> matchClass) {
			this.filter = filter;
			this.queryClass = queryClass;
			this.matchClass = matchClass;
		}

		@Override
		public String toString() {
			// TODO: Implement this method
			return new JSONObject(){{
					put("filter", filter);
					put("queryClass", queryClass);
					put("matchClass", new JSONArray(matchClass));
				}}.toFormatString();
		}
	}
	@SuppressWarnings("unchecked")
	public static class Query {
		QueryResource resource;
		SubQuery[] subQuerys;

		public Query(QueryResource r, SubQuery[] sq) {
			this.resource = r;
			this.subQuerys = sq;
		}

		public Field[] fields()  {
			if (null == resource.queryClassFields) {
				Field[] result = resource.oldFields.clone();
				for (SubQuery sq: this.subQuerys) {
					result = sq.filter.filterFields(result, sq);
				}
				resource.queryClassFields = result;
				return result;
			} else {
				return resource.queryClassFields;
			}
		}
		public Method[] method() {
			if (null == resource.queryClassMethods) {
				Method[] result = resource.oldMethods.clone();
				for (SubQuery sq: this.subQuerys) {
					result = sq.filter.filterMethods(result, sq);
				}
				resource.queryClassMethods = result;
				return result;
			} else {
				return resource.queryClassMethods;
			}
		}

		@Override
		public String toString() {
			// TODO: Implement this method
			return Arrays.asList(subQuerys).toString();
		}
	}

	public static enum HideMode {
		process_filter_PROTECTED, //只是对匹配到类需要屏蔽的继承对象进行处理
		process_filter_ALL;       //只是对匹配到类需要屏蔽的继承对象进行处理
	}



	//只禁用被继承的字段/方法
	final Filter DEFAULT_FILTER_MATCH_EXTENDS_CLASS = new Filter() {
		HideMode getHideMode(String name) {
			return processMatchExtendsClassMap.get(name);
		}

		@Override
		public Field[] filterFields(Field[] fields, final SubQuery query) {
			if (null == query.queryClass) return Finals.EMPTY_FIELD_ARRAY;

			Map<String, Set<Field>> queryClassField = new LinkedHashMap<>();
			for (Field f: fields) {
				String name = f.getName();
				Set<Field> mcf = queryClassField.get(name);
				if (null == mcf) {
					queryClassField.put(name, mcf = new HashSet<>());
				}
				mcf.add(f);
			}

			Map<String, List<Field>> parentHideMap = new HashMap<>();
			for (Class match: query.matchClass) {
				HideMode hideMod = getHideMode(match.getName());
				for (Field f: Reflects.getDeclaredFields(match)) {//获得某个类的所有声明的字段，即包括public、private和proteced，但是不包括父类的申明字段。
					String name = f.getName();
					int fmod    = f.getModifiers();
					List<Field> parentHide = parentHideMap.get(name);
					if (null == parentHide) {
						parentHideMap.put(name, parentHide = new ArrayList<>());
					}
					HIDES: {
						if (hideMod == HideMode.process_filter_PROTECTED) {
							if (!Modifier.isPublic(fmod)) {
								parentHide.add(f);
							}
						} else if (hideMod == HideMode.process_filter_ALL) {
							parentHide.add(f);
						} else {
							throw new RuntimeException("unknown hide mode: " + hideMod);
						}
					}
				}
			}

			List<Field> finalFields = new ArrayList<>();
			for (String name: queryClassField.keySet()) {
				Set<Field> typeAndFields = queryClassField.get(name);
				List<Field> hides        = parentHideMap.get(name);
				for (Field thisField: typeAndFields) {
					if (null == hides) {
						finalFields.add(thisField);
					} else {
						boolean b = true;
						for (Field parentField: hides) {
							if (isOverride(parentField, thisField)) {
								b = false;
							}
						}
						if (b) {
							finalFields.add(thisField);
						}
					}
				}
			}
			return finalFields.toArray(Finals.EMPTY_FIELD_ARRAY);
		}
		@Override
		public Method[] filterMethods(Method[] fields, final SubQuery query) {
			if (null == query.queryClass) return Finals.EMPTY_METHOD_ARRAY;

			Map<String, Set<Method>> queryClassField = new LinkedHashMap<>();
			for (Method f: fields) {
				String name = f.getName();
				Set<Method> mcf = queryClassField.get(name);
				if (null == mcf) {
					queryClassField.put(name, mcf = new HashSet<>());
				}
				mcf.add(f);
			}

			Map<String, List<Method>> parentHideMap = new HashMap<>();
			for (Class match: query.matchClass) {
				HideMode hideMod = getHideMode(match.getName());
				for (Method f: Reflects.getDeclaredMethods(match)) {//获得某个类的所有声明的字段，即包括public、private和proteced，但是不包括父类的申明字段。
					if (Reflects.isOverrideObjectClassMethod(f))
						continue; /* Method+Object公开方法白名单 */
					String name = f.getName();
					int fmod    = f.getModifiers();
					List<Method> parentHide = parentHideMap.get(name);
					if (null == parentHide) {
						parentHideMap.put(name, parentHide = new ArrayList<>());
					}
					HIDES: {
						if (hideMod == HideMode.process_filter_PROTECTED) {
							if (!Modifier.isPublic(fmod)) {
								parentHide.add(f);
							}
						} else if (hideMod == HideMode.process_filter_ALL) {
							parentHide.add(f);
						} else {
							throw new RuntimeException("unknown hide mode: " + hideMod);
						}
					}
				}
			}

			List<Method> finalFields = new ArrayList<>();
			for (String name: queryClassField.keySet()) {
				Set<Method> typeAndFields = queryClassField.get(name);
				List<Method> hides        = parentHideMap.get(name);
				for (Method thisField: typeAndFields) {
					if (null == hides) {
						finalFields.add(thisField);
					} else {
						boolean b = true;
						for (Method parentField: hides) {
							if (isOverride(parentField, thisField)) {
								b = false;
							}
						}
						if (b) {
							finalFields.add(thisField);
						}
					}
				}
			}
			return finalFields.toArray(Finals.EMPTY_METHOD_ARRAY);
		}
	};
	//只禁用被继承的字段/方法
	final Filter DEFAULT_FILTER_MATCH_EXTENDS_PAGECK_CLASS = new Filter() {
		HideMode getHideMode(Package name) {
			return processMatchExtendsPackageClassNameMap.get(name.getName());
		}

		@Override
		public Field[] filterFields(Field[] fields, final SubQuery query) {
			if (null == query.queryClass) return Finals.EMPTY_FIELD_ARRAY;

			Map<String, Set<Field>> queryClassField = new LinkedHashMap<>();
			for (Field f: fields) {
				String name = f.getName();
				Set<Field> mcf = queryClassField.get(name);
				if (null == mcf) {
					queryClassField.put(name, mcf = new HashSet<>());
				}
				mcf.add(f);
			}

			Map<String, List<Field>> parentHideMap = new HashMap<>();
			for (Class match: query.matchClass) {
				HideMode hideMod = getHideMode(match.getPackage());
				for (Field f: Reflects.getDeclaredFields(match)) {//获得某个类的所有声明的字段，即包括public、private和proteced，但是不包括父类的申明字段。
					String name = f.getName();
					int fmod    = f.getModifiers();
					List<Field> parentHide = parentHideMap.get(name);
					if (null == parentHide) {
						parentHideMap.put(name, parentHide = new ArrayList<>());
					}
					HIDES: {
						if (hideMod == HideMode.process_filter_PROTECTED) {
							if (!Modifier.isPublic(fmod)) {
								parentHide.add(f);
							}
						} else if (hideMod == HideMode.process_filter_ALL) {
							parentHide.add(f);
						} else {
							throw new RuntimeException("unknown hide mode: " + hideMod);
						}
					}
				}
			}

			List<Field> finalFields = new ArrayList<>();
			for (String name: queryClassField.keySet()) {
				Set<Field> typeAndFields = queryClassField.get(name);
				List<Field> hides        = parentHideMap.get(name);
				for (Field thisField: typeAndFields) {
					if (null == hides) {
						finalFields.add(thisField);
					} else {
						boolean b = true;
						for (Field parentField: hides) {
							if (isOverride(parentField, thisField)) {
								b = false;
							}
						}
						if (b) {
							finalFields.add(thisField);
						}
					}
				}
			}
			return finalFields.toArray(Finals.EMPTY_FIELD_ARRAY);
		}
		@Override
		public Method[] filterMethods(Method[] fields, final SubQuery query) {
			if (null == query.queryClass) return Finals.EMPTY_METHOD_ARRAY;

			Map<String, Set<Method>> queryClassField = new LinkedHashMap<>();
			for (Method f: fields) {
				String name = f.getName();
				Set<Method> mcf = queryClassField.get(name);
				if (null == mcf) {
					queryClassField.put(name, mcf = new HashSet<>());
				}
				mcf.add(f);
			}

			Map<String, List<Method>> parentHideMap = new HashMap<>();
			for (Class match: query.matchClass) {
				HideMode hideMod = getHideMode(match.getPackage());
				for (Method f: Reflects.getDeclaredMethods(match)) {//获得某个类的所有声明的字段，即包括public、private和proteced，但是不包括父类的申明字段。
					if (Reflects.isOverrideObjectClassMethod(f))
						continue; /* Method+Object公开方法白名单 */
					String name = f.getName();
					int fmod    = f.getModifiers();
					List<Method> parentHide = parentHideMap.get(name);
					if (null == parentHide) {
						parentHideMap.put(name, parentHide = new ArrayList<>());
					}
					HIDES: {
						if (hideMod == HideMode.process_filter_PROTECTED) {
							if (!Modifier.isPublic(fmod)) {
								parentHide.add(f);
							}
						} else if (hideMod == HideMode.process_filter_ALL) {
							parentHide.add(f);
						} else {
							throw new RuntimeException("unknown hide mode: " + hideMod);
						}
					}
				}
			}

			List<Method> finalFields = new ArrayList<>();
			for (String name: queryClassField.keySet()) {
				Set<Method> typeAndFields = queryClassField.get(name);
				List<Method> hides        = parentHideMap.get(name);
				for (Method thisField: typeAndFields) {
					if (null == hides) {
						finalFields.add(thisField);
					} else {
						boolean b = true;
						for (Method parentField: hides) {
							if (isOverride(parentField, thisField)) {
								b = false;
							}
						}
						if (b) {
							finalFields.add(thisField);
						}
					}
				}
			}
			return finalFields.toArray(Finals.EMPTY_METHOD_ARRAY);
		}
	};



//只禁用本类的字段/方法 (相当于对结果处理)
	final Filter DEFAULT_FILTER_EQUAL_CLASS = new Filter() {
		@Override
		public Field[] filterFields(Field[] fields, SubQuery query) {
			if (null == query.queryClass) return Finals.EMPTY_FIELD_ARRAY;

			// queryClass == matchClass
			Class queryClass = query.queryClass;
			HideMode hideMod = equalsClassNameMap.get(queryClass.getName());
			Set<Field> finalFields = new LinkedHashSet<>();
			Collections.addAll(finalFields, fields);

			List<Field> parentHide = new ArrayList<>();
			for (Field f: fields) {
				int fmod    = f.getModifiers();
				if (hideMod == HideMode.process_filter_PROTECTED) {
					if (!Modifier.isPublic(fmod)) {
						parentHide.add(f);
					}
				} else if (hideMod == HideMode.process_filter_ALL) {
					parentHide.add(f);
				} else {
					throw new RuntimeException("unknown hide mode: " + hideMod);
				}
			}
			for (Field f: parentHide) {
				finalFields.remove(f);
			}
			return finalFields.toArray(Finals.EMPTY_FIELD_ARRAY);
		}
		@Override
		public Method[] filterMethods(Method[] fields, SubQuery query) {
			// TODO: Implement this method
			if (null == query.queryClass) return Finals.EMPTY_METHOD_ARRAY;

			Class queryClass = query.queryClass;
			HideMode hideMod = equalsClassNameMap.get(queryClass.getName());
			Set<Method> finalFields = new LinkedHashSet<>();
			Collections.addAll(finalFields, fields);

			List<Method> parentHide = new ArrayList<>();
			for (Method f: fields) {
				if (Reflects.isOverrideObjectClassMethod(f))
					continue; /* Method+Object公开方法白名单 */
				int fmod    = f.getModifiers();
				if (hideMod == HideMode.process_filter_PROTECTED) {
					if (!Modifier.isPublic(fmod)) {
						parentHide.add(f);
					}
				} else if (hideMod == HideMode.process_filter_ALL) {
					parentHide.add(f);
				} else {
					throw new RuntimeException("unknown hide mode: " + hideMod);
				}
			}
			for (Method f: parentHide) {
				finalFields.remove(f);
			}
			return finalFields.toArray(Finals.EMPTY_METHOD_ARRAY);
		}
	};


//只禁用本类的字段/方法 (相当于对结果处理)
	protected Filter      equalsClassFilter    = DEFAULT_FILTER_EQUAL_CLASS;
	Map<String, HideMode> equalsClassNameMap   = new HashMap<>();
	public void addFinalEqualsClass_Name(Class<?> cls) {
		this.addFinalEqualsClass_Name(cls, HideMode.process_filter_PROTECTED);
	}
	public void addFinalEqualsClass_Name(Class<?> cls, HideMode mod) {
		if (null != cls) {
			this.equalsClassNameMap.put(cls.getName(), mod);
			this.faster.setChanged();
		}
	}







	protected Filter                processMatchExtendsClassFilter  = DEFAULT_FILTER_MATCH_EXTENDS_CLASS;
	protected Map<String, HideMode> processMatchExtendsClassMap = new HashMap<>();
	public void addProcessInheritedClass_Name(Class<?> cls) {
		this.addProcessInheritedClass_Name(cls, HideMode.process_filter_PROTECTED);
	}
	public void addProcessInheritedClass_Name(Class<?> cls, HideMode mod) {
		if (null != cls) {
			this.processMatchExtendsClassMap.put(cls.getName(), mod);
			this.faster.setChanged();
		}
	}


	protected Filter                processMatchExtendsPackageClassClassFilter = DEFAULT_FILTER_MATCH_EXTENDS_PAGECK_CLASS;
	protected Map<String, HideMode> processMatchExtendsPackageClassNameMap     = new HashMap<>();
	public void addProcessInheritedPackage_Name(Package pack) {
		this.addProcessInheritedPackage_Name(pack, HideMode.process_filter_PROTECTED);
	}
	public void addProcessInheritedPackage_Name(Package pack, HideMode mod) {
		if (null != pack) {
			String packageName = pack.getName();
			this.processMatchExtendsPackageClassNameMap.put(packageName, mod);
			this.faster.setChanged();
		}
	}



	@NotNull 
	public Query getQuery(Class cls) {
		WeakMapCacheConcurrentHash<Class, Query, RuntimeException> queryCache = faster.getOrCreateCache();
		return queryCache.getOrCreateCache(cls);
	}

	/**
	 * fast match
	 */
	TemporaryChangedCache<WeakMapCacheConcurrentHash<Class, Query, RuntimeException>, RuntimeException> faster = new TemporaryChangedCache<WeakMapCacheConcurrentHash<Class, Query, RuntimeException>, RuntimeException>() {
		@Override
		public WeakMapCacheConcurrentHash<Class, Query, RuntimeException> createCache() throws RuntimeException {
			// TODO: Implement this method
			return new WeakMapCacheConcurrentHash<Class, Query, RuntimeException>() {
				@Override
				public Query newCache(Class cls) throws RuntimeException {
					List<SubQuery> querys = new ArrayList<>();
					matchExtendsClassFilter: {
						Set<Class> matchs = new LinkedHashSet<>();
						Map<String, Set<Class>> inherits = getClassInherits(cls);//获取类的所有继承
						for (String matchInheritClassName: processMatchExtendsClassMap.keySet()) {//遍历需要禁用的类名
							Set<Class>  match = inherits.get(matchInheritClassName);//获取是否有需要禁用的类
							if (null != match) {
								for (Class mathc: match) {
									matchs.add(mathc);
								}
							}
						}
						if (matchs.size() > 0) {
							querys.add(new SubQuery(processMatchExtendsClassFilter, cls, matchs));
						}
					}
					matchExtendsPackageFilter: {
						Set<Class> matchs = new LinkedHashSet<>();

						Map<String, Set<Class>>  inherits = getClassInherits(cls);
						for (String inherit: inherits.keySet()) {
							Set<Class> cs =  inherits.get(inherit);
							for (Class c: cs) {
								Package p = c.getPackage();
								if (null != p) {
									String pn = p.getName();
									if (processMatchExtendsPackageClassNameMap.containsKey(pn)) {
										matchs.add(c);
									}
								}
							}
						}
						if (matchs.size() > 0) {
							querys.add(new SubQuery(processMatchExtendsPackageClassClassFilter, cls, matchs));
						}
					}

					equalsClassFilter: {
						if (equalsClassNameMap.containsKey(Classz.getName(cls))) {
							querys.add(new SubQuery(equalsClassFilter, cls, Lists.asLinkedHashSet(cls)));
						}
					}

					return new Query(
							new QueryResource(Reflects.fields(cls), Reflects.methods(cls)),//获取类的所有字段
							querys.toArray(new SubQuery[]{}));
				}
			};
		}



		/**
		 * 不同ClassLoader 类名可以相同
		 */
		public Map<String, Set<Class>> getClassInherits(Class cls) {
			if (null == cls) {
				return new LinkedHashMap<String, Set<Class>>();
			} else {
				LinkedHashMap<String, Set<Class>> map = new LinkedHashMap<>();
				{
					String name = cls.getName();
					Set<Class> Sets = map.get(name);
					if (null == Sets) {
						map.put(name, Sets = new HashSet<>());
					}
					Sets.add(cls);
				}

				Class[] interfaces =  cls.getInterfaces(); //public interface x extends a,b,c
				Class[] superclass = {cls.getSuperclass()};

				for (Class inter: interfaces) {
					Map<String, Set<Class>> interIndex = getClassInherits(inter);
					for (String name:       interIndex.keySet()) {
						Set<Class> Sets = map.get(name);
						if (null == Sets) {
							map.put(name, Sets = new HashSet<>());
						}
						Set <Class> nc = interIndex.get(name);
						for (Class c: nc) {
							Sets.add(c);
						}
					}
				}
				for (Class inter: superclass) {
					Map<String, Set<Class>> interIndex = getClassInherits(inter);
					for (String name:       interIndex.keySet()) {
						Set<Class> Sets = map.get(name);
						if (null == Sets) {
							map.put(name, Sets = new HashSet<>());
						}
						Set <Class> nc = interIndex.get(name);
						for (Class c: nc) {
							Sets.add(c);
						}
					}
				}
				return map;
			}
		}
	};










	static class Base0 {
		public int base0 = 4;
	}
	static class Base1 extends Base0 {
		private int bass1  =4;
		public  int base1_2=9;

		public Object k = 3;

		private int a;
		protected void a() {}

		public int b;
		public void b() {}
	}
	static class Base2 extends Base1 {
		private   int base2_as  = 999;
		protected int base2_pro = 888;
		public    int base2 = 666;
		public Object k = 3;

		Object hello(Object v, Long i) { return null; }
		Object hello2(Object v, Long i) { return null; }

		public void c() { }
	}
	static class Tests extends Base2 {
		public  int base1_2=9;

		public static int       tests_1 = 1;
		public transient Object tests_2 = 3;
		private int 			tests_k_pri  = 4;
		private int 			tests_k_pri2 = 5;
		private Object 			tests_value = 784920843L;

		public String k = "2";

		public void setValue(String v) {}
		public String getValue() {
			return "666"; 
		}

		@Override Integer hello(Object v, Long i)  { return null; }
		@Override Integer hello2(Object v, Long i) { return null; }

		public int a;
		public int b;
		@Override
		public void a() {}
		@Override
		public void b() {}
		@Override
		public void c() { }
	}
	static public void test() {
		Re_ReflectJavaFilter filter = new Re_ReflectJavaFilter();
		filter.addFinalEqualsClass_Name(requireFinal(String.class));
		filter.addFinalEqualsClass_Name(requireFinal(Class.class));

		filter.addFinalEqualsClass_Name(requireFinal(Integer.class));
		filter.addFinalEqualsClass_Name(requireFinal(Character.class));
		filter.addFinalEqualsClass_Name(requireFinal(Byte.class));
		filter.addFinalEqualsClass_Name(requireFinal(Long.class));
		filter.addFinalEqualsClass_Name(requireFinal(Float.class));
		filter.addFinalEqualsClass_Name(requireFinal(Double.class));
		filter.addFinalEqualsClass_Name(requireFinal(Short.class));
		filter.addFinalEqualsClass_Name(requireFinal(Boolean.class));


		filter.addProcessInheritedClass_Name(Object.class);

		filter.addProcessInheritedClass_Name(Base0.class);
		filter.addProcessInheritedClass_Name(Base1.class,  Re_ReflectJavaFilter.HideMode.process_filter_ALL);
		filter.addProcessInheritedClass_Name(Base2.class);
//		filter.addProcessInheritedPackage_Name(Re.class.getPackage(), Re_ReflectJavaFilter.HideMode.process_filter_ALL);



		Query query = filter.getQuery(Tests.class);
		System.out.println(query);

		System.out.println(System.currentTimeMillis());
		System.out.println(new JSONArray(query.fields()).toFormatString());
// low version
//			"public int Re_ReflectJavaFilter$Tests.a",
//			"public java.lang.String Re_ReflectJavaFilter$Tests.k",
//			"public transient java.lang.Object Re_ReflectJavaFilter$Tests.tests_2",
//			"public static int Re_ReflectJavaFilter$Tests.tests_1",
//			"public int Re_ReflectJavaFilter$Base2.base2",
//			"public int Re_ReflectJavaFilter$Base0.base0",
//			"private int Re_ReflectJavaFilter$Tests.tests_k_pri",
//			"private int Re_ReflectJavaFilter$Tests.tests_k_pri2",
//			"private java.lang.Object Re_ReflectJavaFilter$Tests.tests_value"

//		[
//				"public int top.fols.box.reflect.re.Re_ReflectJavaFilter$Tests.base1_2",
//						"public static int top.fols.box.reflect.re.Re_ReflectJavaFilter$Tests.tests_1",
//						"public transient java.lang.Object top.fols.box.reflect.re.Re_ReflectJavaFilter$Tests.tests_2",
//						"public java.lang.Object top.fols.box.reflect.re.Re_ReflectJavaFilter$Base2.k",
//						"public java.lang.String top.fols.box.reflect.re.Re_ReflectJavaFilter$Tests.k",
//						"public int top.fols.box.reflect.re.Re_ReflectJavaFilter$Tests.a",
//						"public int top.fols.box.reflect.re.Re_ReflectJavaFilter$Tests.b",
//						"public int top.fols.box.reflect.re.Re_ReflectJavaFilter$Base2.base2",
//						"public int top.fols.box.reflect.re.Re_ReflectJavaFilter$Base0.base0",
//						"private int top.fols.box.reflect.re.Re_ReflectJavaFilter$Tests.tests_k_pri",
//						"private int top.fols.box.reflect.re.Re_ReflectJavaFilter$Tests.tests_k_pri2",
//						"private java.lang.Object top.fols.box.reflect.re.Re_ReflectJavaFilter$Tests.tests_value"
//		]
		System.out.println(System.currentTimeMillis());

		System.out.println(new JSONArray(query.method()).toFormatString());
		//low version
//			"public void Main$Tests.c()",
//			"public boolean java.lang.Object.equals(java.lang.Object)",
//			"public final java.lang.Class java.lang.Object.getClass()",
//			"public java.lang.String Main$Tests.getValue()",
//			"public int java.lang.Object.hashCode()",
//			"public final native void java.lang.Object.notify()",
//			"public final native void java.lang.Object.notifyAll()",
//			"public void Main$Tests.setValue(java.lang.String)",
//			"public java.lang.String java.lang.Object.toString()",
//			"public final void java.lang.Object.wait() throws java.lang.InterruptedException",
//			"public final void java.lang.Object.wait(long) throws java.lang.InterruptedException",
//			"public final native void java.lang.Object.wait(long,int) throws java.lang.InterruptedException"

//		[
//    "public java.lang.String top.fols.box.reflect.re.Re_ReflectJavaFilter$Tests.getValue()",
//    "public void top.fols.box.reflect.re.Re_ReflectJavaFilter$Tests.setValue(java.lang.String)",
//    "public void top.fols.box.reflect.re.Re_ReflectJavaFilter$Tests.c()",
//    "public final void java.lang.Object.wait() throws java.lang.InterruptedException",
//    "public final void java.lang.Object.wait(long,int) throws java.lang.InterruptedException",
//    "public final native void java.lang.Object.wait(long) throws java.lang.InterruptedException",
//    "public boolean java.lang.Object.equals(java.lang.Object)",
//    "public java.lang.String java.lang.Object.toString()",
//    "public native int java.lang.Object.hashCode()",
//    "public final native java.lang.Class java.lang.Object.getClass()",
//    "public final native void java.lang.Object.notify()",
//    "public final native void java.lang.Object.notifyAll()",
//    "protected void java.lang.Object.finalize() throws java.lang.Throwable",
//    "protected native java.lang.Object java.lang.Object.clone() throws java.lang.CloneNotSupportedException"
//]
		System.out.println(System.currentTimeMillis());

		System.out.println("---------------------------------");

		System.out.println(filter.getQuery(Re_Executor.class));
		System.out.println(new JSONArray(filter.getQuery(Re_Executor.class).method()).toFormatString());
		System.out.println(System.currentTimeMillis());

		System.out.println(new JSONArray(filter.getQuery(Re_Utilities.class).method()).toFormatString());
		System.out.println(System.currentTimeMillis());
	}


}
