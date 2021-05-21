package top.fols.atri.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class ReflectCache {
	public ReflectCache() { }


	public static class ClassesList implements Cloneable {
		Class[] list;

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
		Constructor[] list;
		Class[][] parameterTypes;

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
		Method[] list;
		Class[][] parameterTypes;

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
		Field[] list;

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





	/**
	 * Update cache if not present
	 * @return cache
	 */






	protected Map<Class, ClassesList> classesListMap;
	protected ClassesList 	getClassesList(Class cls) { return this.getClassesListProcess(null == this.classesListMap ?this.classesListMap = new IdentityHashMap<>(): this.classesListMap, cls); }
	ClassesList 	getClassesListProcess(Map<Class, ClassesList> map, Class cls) {
		ClassesList object;
		if (null == cls) { return null; }
		if (null == (object = map.get(cls))) {
			map.put(cls, object = ClassesList.wrap(this.listClassesProcess(cls)));
		}
		return object;
	}
	protected Class[] 		listClassesProcess(Class cls) { return Reflects.classes(cls); }




	protected Map<Class, ConstructorList> constructorListMap;
	protected ConstructorList getConstructorsList(Class cls) { return this.getConstructorsListProcess(null == this.constructorListMap ?this.constructorListMap = new IdentityHashMap<>(): this.constructorListMap, cls); }
	ConstructorList getConstructorsListProcess(Map<Class, ConstructorList> map, Class cls) {
		ConstructorList object;
		if (null == cls) { return null; }
		if (null == (object = map.get(cls))) {
			map.put(cls, object = ConstructorList.wrap(this.listConstructorListProcess(cls)));
		}
		return object;
	}
	protected Constructor[] listConstructorListProcess(Class cls) { return Reflects.constructors(cls); }





	protected Map<Class, FieldList> fieldsListMap;
	protected FieldList getFieldsList(Class cls) { return this.getFieldsListProcess(null == this.fieldsListMap ?this.fieldsListMap = new IdentityHashMap<>(): this.fieldsListMap, cls); }
	FieldList getFieldsListProcess(Map<Class, FieldList> map, Class cls) {
		FieldList object;
		if (null == cls) { return null; }
		if (null == (object = map.get(cls))) {
			map.put(cls, object = FieldList.wrap(this.listFieldListProcess(cls)));
		}
		return object;
	}

	protected Map<Class, Map<String, FieldList>> fieldsNameListMap;
	protected FieldList getFieldsList(Class cls, String name) { 
		Map<Class, Map<String, FieldList>> values = (null == this.fieldsNameListMap ?(this.fieldsNameListMap = new IdentityHashMap<>()): this.fieldsNameListMap);
		return this.getFieldsListProcess(values, cls, name); 
	}
	FieldList getFieldsListProcess(Map<Class, Map<String, FieldList>> map, Class cls, String name) {
		if (null == cls) { return null; }
		if (null == name) { return this.getFieldsList(cls); }
		if (null == map.get(cls)) {
			this.update(map, cls, this.listFieldListProcess(cls));
		}
		return map.get(cls).get(name);
	}
	void update(Map<Class, Map<String, FieldList>> map, Class cls, Field[] values) {
		Map<String, List<Field>> temp = new LinkedHashMap<>();
		for (Field value: values) {
			String fn = value.getName();
			List<Field> tempList = temp.get(value.getName());
			if (null == tempList) {
				temp.put(fn, tempList = new ArrayList<>());
			}
			tempList.add(value);
		}
		Map<String, FieldList> temp2 = new LinkedHashMap<>();
		for (String n: temp.keySet()) {
			List<Field> tempList = temp.get(n);
			temp2.put(n, FieldList.wrap(tempList.toArray(new Field[]{})));
		}
		temp = null;
		Map<String, FieldList> last = map.put(cls, temp2);
		last = null;
	}

	protected Field[] listFieldListProcess(Class cls) { return Reflects.fields(cls); }







	protected Map<Class, MethodList> methodsListMap;
	protected MethodList getMethodsList(Class cls) { return this.getMethodsListProcess(null == this.methodsListMap ?this.methodsListMap = new IdentityHashMap<>(): this.methodsListMap, cls); }
	MethodList getMethodsListProcess(Map<Class, MethodList> map, Class cls) {
		MethodList object;
		if (null == cls) { return null; }
		if (null == (object = map.get(cls))) {
			map.put(cls, object = MethodList.wrap(this.listMethodListProcess(cls)));
		}
		return object;
	}


	protected Map<Class, Map<String, MethodList>> methodsNameListMap;
	protected MethodList getMethodsList(Class cls, String name) { return this.getMethodsListProcess(null == this.methodsNameListMap ?this.methodsNameListMap = new IdentityHashMap<>(): this.methodsNameListMap, cls, name); }
	MethodList getMethodsListProcess(Map<Class, Map<String, MethodList>> map, Class cls, String name) {
		if (null == cls) { return null; }
		if (null == name) { return this.getMethodsList(cls); }
		if (null == map.get(cls)) {
			this.update(map, cls, this.listMethodListProcess(cls));
		}
		return map.get(cls).get(name);
	}
	void update(Map<Class, Map<String, MethodList>> map, Class cls, Method[] values) {
		Map<String, List<Method>> temp = new LinkedHashMap<>();
		for (Method value: values) {
			String fn = value.getName();
			List<Method> tempList = temp.get(value.getName());
			if (null == tempList) {
				temp.put(fn, tempList = new ArrayList<>());
			}
			tempList.add(value);
		}
		Map<String, MethodList> temp2 = new LinkedHashMap<>();
		for (String n: temp.keySet()) {
			List<Method> tempList = temp.get(n);
			temp2.put(n, MethodList.wrap(tempList.toArray(new Method[]{})));
		}
		temp = null;
		Map<String, MethodList> last = map.put(cls, temp2);
		last = null;
	}

	protected Method[] listMethodListProcess(Class cls) { return Reflects.methods(cls); }


	
	
	
	public Class[] classes(Class cls) {
		if (null == cls) { return null; }
		ClassesList list = this.getClassesList(cls);
		if (null == list) { return null; }
		return list.listClone();
	}

	/**
	 * Update cache if not present
	 * @return cache
	 */
	public Field field(Class cls, String name) {
		return this.field(cls, null, name);
	}
	public Field field(Class cls, Class returnClass, String name) {
		if (null == cls || null == name) { return null; }
		FieldList list = this.getFieldsList(cls, name);
		if (null == list) { return null; }
		if (null == returnClass) {
			Field f = list.list.length == 0 ? null : list.list[0];
			return f;
		} else {
			int length = list.list.length;
			for (int i = 0; i < length; i++) {
				if (returnClass == list.list[i].getType()) {
					return list.list[i];
				}
			}
		}
		return null;
	}

	/**
	 * Update cache if not present
	 * @return cache
	 */
	public Constructor constructor(Class cls, Class... parameterTypes) {
		if (null == cls || null == parameterTypes) { return null; }
		ConstructorList list = this.getConstructorsList(cls);
		if (null == list) { return null; }
		int length = list.list.length;
		for (int i = 0; i < length; i++) {
			Class[] listElementParameterTypes = list.parameterTypes[i];
			if (listElementParameterTypes.length == parameterTypes.length
				&& Reflects.parameterTypesEquals(listElementParameterTypes, parameterTypes)) {
				return list.list[i];
			}
		}
		return null;
	}

	/**
	 * Update cache if not present
	 * @return cache
	 */
	public Method method(Class cls, String name, Class... parameterTypes) {
		return this.method(cls, null, name, parameterTypes);
	}
	public Method method(Class cls, Class returnClass, String name, Class... parameterTypes) {
		if (null == cls || null == name || null == parameterTypes) { return null; }
		MethodList list = this.getMethodsList(cls, name);
		if (null == list) {return null; }
		if (null == returnClass) {
			int length = list.list.length;
			for (int i = 0; i < length; i++) {
				Class[] listElementParameterTypes = list.parameterTypes[i];
				if (listElementParameterTypes.length == parameterTypes.length
					&& Reflects.parameterTypesEquals(listElementParameterTypes, parameterTypes)) {
					return list.list[i];
				}
			}
		} else {
			int length = list.list.length;
			for (int i = 0; i < length; i++) {
				Class[] listElementParameterTypes = list.parameterTypes[i];
				if (returnClass == list.list[i].getReturnType()
					&& listElementParameterTypes.length == parameterTypes.length
					&& Reflects.parameterTypesEquals(listElementParameterTypes, parameterTypes)) {
					return list.list[i];
				}
			}
		}
		return null;
	}





	public ReflectCache classesRelease()		{ this.classesListMap = null; 									return this; }
	public ReflectCache constructorsRelease()	{ this.constructorListMap = null; 								return this; }
	public ReflectCache fieldsRelease()			{ this.fieldsListMap = null; 	this.fieldsNameListMap = null;		return this; }
	public ReflectCache methodsRelease()		{ this.methodsListMap = null; 	this.methodsNameListMap = null;		return this; }

	public ReflectCache release() { 
		return this
			. classesRelease()
			. constructorsRelease()
			. fieldsRelease()
			. methodsRelease()
			;
	}


	public boolean isDefault() { return this == DEFAULT_INSTANCE; }
	public static final ReflectCache DEFAULT_INSTANCE = new ReflectCache() {
		@Override public ReflectCache classesRelease() { throw new UnsupportedOperationException(); }
		@Override public ReflectCache constructorsRelease() { throw new UnsupportedOperationException(); }
		@Override public ReflectCache fieldsRelease() { throw new UnsupportedOperationException(); }
		@Override public ReflectCache methodsRelease() { throw new UnsupportedOperationException(); }

		@Override public ReflectCache release() { throw new UnsupportedOperationException(); }
	};











}	
