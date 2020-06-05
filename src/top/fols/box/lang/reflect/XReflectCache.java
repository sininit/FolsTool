package top.fols.box.lang.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import top.fols.box.lang.reflect.optdeclared.XReflectAccessibleInherit;
import top.fols.box.lang.reflect.safety.XReflect;
import top.fols.box.statics.XStaticFixedValue;
import top.fols.box.util.XArrays;
import top.fols.box.util.XObjects;

/**
 * no exception
 * 
 * because the cache is used, all AccessibleObject (Constructor, Field, Method)
 * are unique and may be setAccessible at any time. so it is recommended to call
 * setAccessible
 * <p>
 * 因为使用的是缓存，所以所有AccessibleObject (Constructor, Field, Method) 都是唯一的，可能随时会被
 * setAccessible 所以建议调用的时候 setAccessible
 * 
 */
public class XReflectCache implements Cloneable {
	public static final XReflectCache defaultInstance = new XReflectCache() {
		@Override
		public XReflectCache setWeakMode(boolean b) throws RuntimeException {
			throw new RuntimeException("the default instance does not support this operation");
		}

		@Override
		protected boolean isLockSource0() {
			return true;
		}
	};

	/**
	 * weak mode
	 */
	private boolean weakMode = false;

	public XReflectCache setWeakMode(boolean b) {
		if (this.weakMode != b) {
			this.weakMode = b;
			this.clear();
		}
		return this;
	}

	public boolean isWeakMode() {
		return this.weakMode;
	}

	/**
	 * 
	 * @return is weak mode return weakhashmap,if not return hashmap
	 */
	private Map newMap0() {
		if (this.weakMode) {
			return new WeakHashMap<>();
		} else {
			return new HashMap<>();
		}
	}

	/**
	 * prevent data from being modified *
	 * <p>
	 * if islocksource return clone(cache data), if not return cache data
	 */
	protected boolean isLockSource0() {
		return false;
	}

	/**
	 * no cache
	 * 
	 * @return Constructors if private method cannot be reflected
	 */
	public static Class[] getAllInheritClasses(Class cls) {
		if (null == cls) {
			return null;
		} else {
			try {
				Class[] classes = XReflectAccessibleInherit.getClassesAll(cls);
				return classes;
			} catch (Throwable e) {
				return XReflect.getClasses(cls);
			}
		}
	}

	/**
	 * no cache
	 * 
	 * @return Constructors if private method cannot be reflected
	 */
	public static Constructor[] getAllInheritConstructors(Class cls) {
		if (null == cls) {
			return null;
		} else {
			try {
				Constructor[] constructors = XReflectAccessibleInherit.getConstructorsAllSetAccessible(cls);
				return constructors;
			} catch (Throwable e) {
				return XReflect.getConstructors(cls);
			}
		}
	}

	/**
	 * no cache
	 * 
	 * @return Methods if private method cannot be reflected
	 */
	public static Method[] getAllInheritMethods(Class cls) {
		if (null == cls) {
			return null;
		} else {
			try {
				Method[] methods = XReflectAccessibleInherit.getMethodsAllSetAccessible(cls);
				return methods;
			} catch (Throwable e) {
				return XReflect.getMethods(cls);
			}
		}
	}

	/**
	 * no cache
	 * 
	 * @return Methods if private method cannot be reflected
	 */
	public static Method[] getAllInheritMethods(Class cls, String name) {
		if (null == cls) {
			return null;
		} else {
			try {
				Method[] methods = XReflectAccessibleInherit.getMethodsAllSetAccessible(cls, name);
				return methods;
			} catch (Throwable e) {
				return XReflect.getMethodsAll(cls, name);
			}
		}
	}

	/**
	 * no cache
	 * 
	 * do not process duplicate data
	 * 
	 * @return Methods if private method cannot be reflected
	 */
	public static Method[] getAllInheritMethodsFast(Class cls) {
		if (null == cls) {
			return null;
		} else {
			try {
				Method[] methods = XReflectAccessibleInherit.getMethodsAllFastSetAccessible(cls);
				return methods;
			} catch (Throwable e) {
				return XReflect.getMethods(cls);
			}
		}
	}

	/**
	 * no cache
	 * 
	 * do not process duplicate data
	 * 
	 * @return Methods if private method cannot be reflected
	 */
	public static Method[] getAllInheritMethodsFast(Class cls, String name) {
		if (null == cls) {
			return null;
		} else {
			try {
				Method[] methods = XReflectAccessibleInherit.getMethodsAllFastSetAccessible(cls, name);
				return methods;
			} catch (Throwable e) {
				return XReflect.getMethods(cls);
			}
		}
	}

	/**
	 * no cache
	 * 
	 * @return Methods if private method cannot be reflected
	 */
	public static Field[] getAllInheritFields(Class cls) {
		if (null == cls) {
			return null;
		} else {
			try {
				Field[] fields = XReflectAccessibleInherit.getFieldsAllSetAccessible(cls);
				return fields;
			} catch (Throwable e) {
				return XReflect.getFields(cls);
			}
		}
	}

	/**
	 * auto update cache use
	 */
	protected Class[] updatingGetJavaClassesAll0(Class cls) {
		return XReflectCache.getAllInheritClasses(cls);
	}

	protected Constructor[] updatingGetJavaClassConstructorsAll0(Class cls) {
		return XReflectCache.getAllInheritConstructors(cls);
	}

	protected Method[] updatingGetJavaClassMethodsAll0(Class cls) {
		return XReflectCache.getAllInheritMethods(cls);
	}

	protected Field[] updatingGetJavaClassFieldsAll0(Class cls) {
		return XReflectCache.getAllInheritFields(cls);
	}

	/**
	* 
	*/

	public static class ClassesList implements Cloneable {
		private Class[] list;

		public static ClassesList wrap(Class[] list) {
			ClassesList wrap = new ClassesList();
			wrap.list = list.clone();
			return wrap;
		}

		public Class[] list() {
			return this.list;
		}

		public Class[] listClone() {
			return this.list.clone();
		}

		@Override
		public int hashCode() {
			// TODO Auto-generated method stub
			return null == this.list ? 0 : this.list.hashCode();
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return null == this.list ? null : Arrays.toString(this.list);
		}

		@Override
		public ClassesList clone() {
			// TODO Auto-generated method stub
			ClassesList newInstance = new ClassesList();
			newInstance.list = this.listClone();
			return newInstance;
		}
	}

	public static class ConstructorList implements Cloneable {
		private Constructor[] list;
		private Class[][] parameterTypes;

		public static ConstructorList wrap(Constructor[] list) {
			ConstructorList wrap = new ConstructorList();
			wrap.list = list.clone();
			wrap.parameterTypes = new Class[list.length][];
			for (int i = 0; i < list.length; i++) {
				wrap.parameterTypes[i] = list[i].getParameterTypes();// already clone
			}
			return wrap;
		}

		public Constructor[] list() {
			return this.list;
		}

		public Constructor[] listClone() {
			return this.list.clone();
		}

		public Class[][] parameterTypes() {
			return this.parameterTypes;
		}

		public Class[][] parameterTypesClone() {
			Class[][] clones = new Class[this.parameterTypes.length][];
			for (int i = 0; i < this.parameterTypes.length; i++) {
				clones[i] = this.parameterTypes[i].clone();
			}
			return clones;
		}

		@Override
		public int hashCode() {
			// TODO Auto-generated method stub
			return null == this.list ? 0 : this.list.hashCode();
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return null == this.list ? null : Arrays.toString(this.list);
		}

		@Override
		public ConstructorList clone() {
			// TODO Auto-generated method stub
			ConstructorList newInstance = new ConstructorList();
			newInstance.list = this.listClone();
			newInstance.parameterTypes = this.parameterTypesClone();
			return newInstance;
		}
	}

	public static class MethodList implements Cloneable {
		private Method[] list;
		private Class[][] parameterTypes;

		public static MethodList wrap(Method[] list) {
			MethodList wrap = new MethodList();
			wrap.list = list.clone();
			wrap.parameterTypes = new Class[list.length][];
			for (int i = 0; i < list.length; i++) {
				wrap.parameterTypes[i] = list[i].getParameterTypes();// already clone
			}
			return wrap;
		}

		public Method[] list() {
			return this.list;
		}

		public Method[] listClone() {
			return this.list.clone();
		}

		public Class[][] parameterTypes() {
			return this.parameterTypes;
		}

		public Class[][] parameterTypesClone() {
			Class[][] clones = new Class[this.parameterTypes.length][];
			for (int i = 0; i < this.parameterTypes.length; i++) {
				clones[i] = this.parameterTypes[i].clone();
			}
			return clones;
		}

		@Override
		public int hashCode() {
			// TODO Auto-generated method stub
			return null == this.list ? 0 : this.list.hashCode();
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return null == this.list ? null : Arrays.toString(this.list);
		}

		@Override
		public MethodList clone() {
			// TODO Auto-generated method stub
			MethodList newInstance = new MethodList();
			newInstance.list = this.listClone();
			newInstance.parameterTypes = this.parameterTypesClone();
			return newInstance;
		}
	}

	public static class FieldList implements Cloneable {
		private Field[] list;

		public static FieldList wrap(Field[] list) {
			FieldList wrap = new FieldList();
			wrap.list = list.clone();
			return wrap;
		}

		public Field[] list() {
			return this.list;
		}

		public Field[] listClone() {
			return this.list.clone();
		}

		@Override
		public int hashCode() {
			// TODO Auto-generated method stub
			return null == this.list ? 0 : this.list.hashCode();
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return null == this.list ? null : Arrays.toString(this.list);
		}

		@Override
		public FieldList clone() {
			// TODO Auto-generated method stub
			FieldList newInstance = new FieldList();
			newInstance.list = this.listClone();
			return newInstance;
		}
	}

	private static ClassesList clone(ClassesList object) {
		return null == object ? null : object.clone();
	}

	private static ConstructorList clone(ConstructorList object) {
		return null == object ? null : object.clone();
	}

	private static MethodList clone(MethodList object) {
		return null == object ? null : object.clone();
	}

	private static FieldList clone(FieldList object) {
		return null == object ? null : object.clone();
	}

	private static Map<String, Field> cloneClassFieldMap(Map<String, Field> object) {
		return null == object ? null : new HashMap<>(object);
	}

	private static Map<String, MethodList> cloneClassMethodsMap(Map<String, MethodList> object) {
		if (null == object) {
			return null;
		}

		Map<String, MethodList> newInstance = new HashMap<>();
		for (String key : object.keySet()) {
			MethodList value = clone(object.get(key));
			newInstance.put(key, value);
		}
		return newInstance;
	}

	/**
	* 
	*/

	private Map<Class, ClassesList> hashClasses;

	protected Map<Class, ClassesList> classes() {
		if (null == this.hashClasses) {
			this.hashClasses = this.newMap0();
		}
		return this.hashClasses;
	}

	public void clearClassClassesCache() {
		this.hashClasses = null;
	}

	public List<Class> listClassClassesCacheKeys() {
		return XObjects.keys(this.classes());
	}

	public boolean existClassClassesCache(Class cls) {
		return this.classes().containsKey(cls);
	}

	public void removeClassClassesCache(Class cls) {
		this.classes().remove(cls);
	}

	public boolean updateClassClassesCache(Class cls, ClassesList newCS) {
		if (this.isLockSource0()) {
			return false;
		} else {
			this.updateClassClassesCache0(cls, newCS);
			return true;
		}
	}

	protected void updateClassClassesCache0(Class cls, ClassesList newCS) {
		if (null == cls) {
			return;
		}

		this.classes().put(cls, newCS);
	}

	public ClassesList getClassClassesCache(Class cls) {
		ClassesList object = this.classes().get(cls);
		return this.isLockSource0() ? clone(object) : object;
	}

	/**
	 * Update cache if not present
	 * <p>
	 * if islocksource return clone(cache data), if not return cache data
	 * 
	 * @return cache
	 */
	public ClassesList getClasses(Class cls) {
		ClassesList object = this.getClasses0(cls);
		return this.isLockSource0() ? clone(object) : object;
	}

	protected ClassesList getClasses0(Class cls) {
		if (null == cls) {
			return null;
		}

		ClassesList object = this.classes().get(cls);
		if (null == object) {
			this.updateClassClassesCache0(cls, ClassesList.wrap(updatingGetJavaClassesAll0(cls)));
			object = this.classes().get(cls);
		}
		return object;
	}

	/* ---- */

	private Map<Class, ConstructorList> hashClassConstructor;

	protected Map<Class, ConstructorList> constructors() {
		if (null == this.hashClassConstructor) {
			this.hashClassConstructor = this.newMap0();
		}
		return this.hashClassConstructor;
	}

	public void clearClassConstructorCache() {
		this.hashClassConstructor = null;
	}

	public List<Class> listClassConstructorCacheKeys() {
		return XObjects.keys(this.constructors());
	}

	public boolean existClassConstructorCache(Class cls) {
		return this.constructors().containsKey(cls);
	}

	public void removeClassConstructorCache(Class cls) {
		this.constructors().remove(cls);
	}

	public boolean updateClassConstructorCache(Class cls, ConstructorList newCS) {
		if (this.isLockSource0()) {
			return false;
		} else {
			this.updateClassConstructorCache0(cls, newCS);
			return true;
		}
	}

	protected void updateClassConstructorCache0(Class cls, ConstructorList newCS) {
		if (null == cls) {
			return;
		}

		this.constructors().put(cls, newCS);
	}

	public ConstructorList getClassConstructorCache(Class cls) {
		ConstructorList object = this.constructors().get(cls);
		return this.isLockSource0() ? clone(object) : object;
	}

	/**
	 * Update cache if not present
	 * <p>
	 * if islocksource return clone(cache data), if not return cache data
	 * 
	 * @return cache
	 */
	public ConstructorList getConstructors(Class cls) {
		ConstructorList object = this.getConstructors0(cls);
		return this.isLockSource0() ? clone(object) : object;
	}

	protected ConstructorList getConstructors0(Class cls) {
		if (null == cls) {
			return null;
		}

		ConstructorList object = this.constructors().get(cls);
		if (null == object) {
			this.updateClassConstructorCache0(cls, ConstructorList.wrap(updatingGetJavaClassConstructorsAll0(cls)));
			object = this.constructors().get(cls);
		}
		return object;
	}

	/**
	 * Update cache if not present
	 * <p>
	 * if islocksource return clone(cache data), if not return cache data
	 * 
	 * @return cache
	 */
	public Constructor getConstructor(Class cls, Class... parameterTypes) {
		if (null == cls || null == parameterTypes) {
			return null;
		}

		ConstructorList list = this.getConstructors0(cls);
		if (null == list) {
			return null;
		}
		int length = list.list.length;
		for (int i = 0; i < length; i++) {
			Class[] listElementParameterTypes = list.parameterTypes[i];
			if (listElementParameterTypes.length == parameterTypes.length
					&& XArrays.arrayContentsEquals(listElementParameterTypes, parameterTypes)) {
				return list.list[i];
			}
		}
		return null;
	}

	/* ---- */
	private Map<Class, MethodList> hashClassMethodAll;

	protected Map<Class, MethodList> methodsall() {
		if (null == this.hashClassMethodAll) {
			this.hashClassMethodAll = this.newMap0();
		}
		return this.hashClassMethodAll;
	}

	private Map<Class, Map<String, MethodList>> hashClassMethod;

	protected Map<Class, Map<String, MethodList>> methods() {
		if (null == this.hashClassMethod) {
			this.hashClassMethod = this.newMap0();
		}
		return this.hashClassMethod;
	}

	public void clearClassMethodCache() {
		this.hashClassMethod = null;
		this.hashClassMethodAll = null;
	}

	public List<Class> listClassMethodCacheKeys() {
		return XObjects.keys(this.methods());
	}

	public boolean existClassMethodCache(Class cls) {
		return this.methods().containsKey(cls);
	}

	public void removeClassMethodCache(Class cls) {
		Map clsmi = this.methods().get(cls);
		if (null != clsmi) {
			clsmi.clear();
		}
		this.methods().remove(cls);

		this.hashClassMethodAll.remove(cls);
	}

	public boolean updateClassMethodCache(Class cls, Method[] clsMethods) {
		if (this.isLockSource0()) {
			return false;
		} else {
			this.updateClassMethodCache0(cls, clsMethods);
			return true;
		}
	}

	protected void updateClassMethodCache0(Class cls, Method[] clsMethods) {
		if (null == cls) {
			return;
		}

		Map<String, MethodList> methods = new HashMap<>();
		Map<String, List<Method>> tmp = new HashMap<>();
		Method[] ms = null == clsMethods ? XStaticFixedValue.nullMethodArray : clsMethods;
		for (Method m : ms) {
			if (null == m) {
				continue;
			}
			String mn = m.getName();
			List<Method> list = tmp.get(mn);
			if (null == list) {
				list = new ArrayList<Method>();
			}
			list.add(m);
			tmp.put(mn, list);
		}
		for (String m : tmp.keySet()) {
			List<Method> listArr = tmp.get(m);
			Method[] newMethod = listArr.toArray(new Method[listArr.size()]);
			methods.put(m, MethodList.wrap(newMethod));
		}
		this.methods().put(cls, methods);

		this.methodsall().put(cls, MethodList.wrap(ms));

		tmp = null;
	}

	public Map<String, MethodList> getClassMethodCache(Class cls) {
		Map<String, MethodList> object = this.methods().get(cls);
		return this.isLockSource0() ? cloneClassMethodsMap(object) : object;
	}

	public MethodList getClassMethodCache(Class cls, String methodName) {
		Map<String, MethodList> object0 = this.methods().get(cls);
		if (null == object0) {
			return null;
		}

		MethodList object = object0.get(methodName);
		return this.isLockSource0() ? clone(object) : object;
	}

	public List<String> listClassMethodCacheNameKeys(Class cls) {
		return XObjects.keys(this.methods().get(cls));
	}

	/**
	 * Update cache if not present
	 * <p>
	 * if islocksource return clone(cache data), if not return cache data
	 * 
	 * @return cache
	 */
	public MethodList getMethods(Class cls) {
		MethodList object = this.getMethods0(cls);
		return this.isLockSource0() ? clone(object) : object;
	}

	protected MethodList getMethods0(Class cls) {
		if (null == cls) {
			return null;
		}

		MethodList object = this.methodsall().get(cls);
		if (null == object) {
			this.updateClassMethodCache0(cls, updatingGetJavaClassMethodsAll0(cls));
			object = this.methodsall().get(cls);
		}
		return object;
	}

	/**
	 * Update cache if not present
	 * <p>
	 * if islocksource return clone(cache data), if not return cache data
	 * 
	 * @return cache
	 */
	public MethodList getMethods(Class cls, String name) {
		MethodList object = this.getMethods0(cls, name);
		return this.isLockSource0() ? clone(object) : object;
	}

	protected MethodList getMethods0(Class cls, String name) {
		if (null == cls || null == name) {
			return null;
		}

		Map<String, MethodList> methods = this.methods().get(cls);
		if (null == methods) {
			this.updateClassMethodCache0(cls, updatingGetJavaClassMethodsAll0(cls));
			methods = this.methods().get(cls);
		}
		MethodList object = null == methods ? null : methods.get(name);
		return object;
	}

	/**
	 * Update cache if not present
	 * <p>
	 * if islocksource return clone(cache data), if not return cache data
	 * 
	 * @return cache
	 */
	public Method getMethod(Class cls, String name, Class... parameterTypes) {
		if (null == cls || null == name || null == parameterTypes) {
			return null;
		}

		MethodList list = this.getMethods0(cls, name);
		if (null == list) {
			return null;
		}
		int length = list.list.length;
		for (int i = 0; i < length; i++) {
			Class[] listElementParameterTypes = list.parameterTypes[i];
			if (listElementParameterTypes.length == parameterTypes.length
					&& XArrays.arrayContentsEquals(listElementParameterTypes, parameterTypes)) {
				return list.list[i];
			}
		}
		return null;
	}

	/* ---- */

	private Map<Class, FieldList> hashClassFieldAll;

	protected Map<Class, FieldList> fieldsall() {
		if (null == this.hashClassFieldAll) {
			this.hashClassFieldAll = this.newMap0();
		}
		return this.hashClassFieldAll;
	}

	private Map<Class, Map<String, Field>> hashClassField;

	protected Map<Class, Map<String, Field>> fields() {
		if (null == this.hashClassField) {
			this.hashClassField = this.newMap0();
		}
		return this.hashClassField;
	}

	public void clearClassFieldCache() {
		this.hashClassField = null;
		this.hashClassFieldAll = null;
	}

	public List<Class> listClassFieldCacheKeys() {
		return XObjects.keys(this.fields());
	}

	public boolean existClassFieldCache(Class cls) {
		return this.fields().containsKey(cls);
	}

	public void removeClassFieldCache(Class cls) {
		Map<String, Field> clsmi = this.fields().get(cls);
		if (null != clsmi) {
			clsmi.clear();
		}
		this.fields().remove(cls);

		this.fieldsall().remove(cls);
	}

	public boolean updateClassFieldCache(Class cls, Field[] newFields) {
		if (this.isLockSource0()) {
			return false;
		} else {
			this.updateClassFieldCache0(cls, newFields);
			return true;
		}
	}

	protected void updateClassFieldCache0(Class cls, Field[] newFields) {
		if (null == cls) {
			return;
		}

		Map<String, Field> Fields = new HashMap<String, Field>();
		Field[] fs = null == newFields ? XStaticFixedValue.nullFieldArray : newFields;
		for (Field m : fs) {
			if (null == m) {
				continue;
			}
			String mn = m.getName();
			Fields.put(mn, m);
		}
		this.fields().put(cls, null);
		this.fields().put(cls, Fields);

		this.fieldsall().put(cls, FieldList.wrap(fs));
	}

	public Map<String, Field> getClassFieldCache(Class cls) {
		Map<String, Field> object = this.fields().get(cls);
		return this.isLockSource0() ? cloneClassFieldMap(object) : object;
	}

	public List<String> listClassFieldCacheNameKeys(Class cls) {
		return XObjects.keys(this.fields().get(cls));
	}

	/**
	 * Update cache if not present
	 * <p>
	 * if islocksource return clone(cache data), if not return cache data
	 * 
	 * @return cache
	 */
	public FieldList getFields(Class cls) {
		FieldList object = this.getFields0(cls);
		return this.isLockSource0() ? clone(object) : object;
	}

	protected FieldList getFields0(Class cls) {
		if (null == cls) {
			return null;
		}

		FieldList object = this.fieldsall().get(cls);
		if (null == object) {
			this.updateClassFieldCache0(cls, updatingGetJavaClassFieldsAll0(cls));
			object = this.fieldsall().get(cls);
		}
		return object;
	}

	/**
	 * Update cache if not present
	 * <p>
	 * if islocksource return clone(cache data), if not return cache data
	 * 
	 * @return cache
	 */
	public Field getField(Class cls, String name) {
		if (null == cls || null == name) {
			return null;
		}

		Map<String, Field> fs = this.fields().get(cls);
		if (null == fs) {
			this.updateClassFieldCache0(cls, updatingGetJavaClassFieldsAll0(cls));
			fs = this.fields().get(cls);
		}
		Field f = null == fs ? null : fs.get(name);
		return f;
	}

	/* ---- */

	/**
	 * clear all cache
	 */
	public XReflectCache clear() {
		this.clearClassClassesCache();
		this.clearClassConstructorCache();
		this.clearClassMethodCache();
		this.clearClassFieldCache();
		return this;
	}

	public boolean existClassCache(Class cls) {
		return this.existClassClassesCache(cls) ||
		this.existClassConstructorCache(cls) || this.existClassFieldCache(cls)
				|| this.existClassMethodCache(cls);
	}
}
