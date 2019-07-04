package top.fols.box.lang.reflect.optdeclared;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import top.fols.box.annotation.XAnnotations;
import top.fols.box.lang.XClass;
import top.fols.box.lang.reflect.XReflectAccessible;
import top.fols.box.statics.XStaticFixedValue;
import top.fols.box.util.XObjects;

/*
 * acquisition and parameter matching method or constructor
 * 获取与参数匹配的 方法 或者 构造器
 */
public class XReflectMatcher {
	public static final XReflectMatcher defaultInstance = new XReflectMatcher();

	protected Constructor[] getConstructorsAll0(Class cls) { return XReflectAccessible.getConstructorsAllSetAccessibleTrue(cls); }
	protected Method[] getMethodsAll0(Class cls) { return XReflectAccessible.getMethodsAllSetAccessibleTrue(cls); }
	protected Method[] getMethodsAll0(Class cls, String name) { return XReflectAccessible.getMethodsAllSetAccessibleTrue(cls, name); }
	protected Field[] getFieldsAll0(Class cls) { return XReflectAccessible.getFieldsAllSetAccessibleTrue(cls); }
	
	
	
	
	private static String getObjArrClss2String(Object[] objs) {
		if (null == objs || objs.length == 0)
			return "()";
		StringJoiner sb = new StringJoiner(",");
		for (Object oi:objs) sb.add(null == oi ?null: oi.getClass().getCanonicalName());
		return new StringBuilder()
			.append("(").append(sb.toString()).append(")")
			.toString();
	}
	
	
	@XAnnotations("not cache option")
	public Object newInstance(Class cls, Object... paramInstanceArr) throws InvocationTargetException, InstantiationException, IllegalAccessException, IllegalArgumentException {
		Constructor con = matchConstructor(getConstructorsAll0(cls), paramInstanceArr);
		return con.newInstance(paramInstanceArr);
	}
	@XAnnotations("not cache option")
	public Object newInstance(Object instance, Object... paramInstanceArr) throws InvocationTargetException, InstantiationException, IllegalAccessException, IllegalArgumentException {
		Constructor con = matchConstructor(getConstructorsAll0(instance.getClass()), paramInstanceArr);
		return con.newInstance(paramInstanceArr);
	}
	@XAnnotations("not cache option")
	public Object execMethod(Class instanceCls, Object instance, String method, Object... paramInstanceArr) throws InvocationTargetException, IllegalAccessException, IllegalArgumentException {
		Method con = matchMethod(getMethodsAll0(instanceCls, method), paramInstanceArr);
		return con.invoke(instance, paramInstanceArr);
	}
	@XAnnotations("not cache option")
	public Object execMethod(Object instance, String method, Object... paramInstanceArr) throws InvocationTargetException, IllegalAccessException, IllegalArgumentException {
		return execMethod(instance.getClass(), instance, method, paramInstanceArr);
	}
	@XAnnotations("not cache option")
	public Object execStaticMethod(Class cls, String method, Object... paramInstanceArr) throws InvocationTargetException, IllegalAccessException, IllegalArgumentException {
		return execMethod(cls, null, method, paramInstanceArr);
	}
	@XAnnotations("not cache option")
	public Object execStaticMethod(Object instance, String method, Object... paramInstanceArr) throws InvocationTargetException, IllegalAccessException, IllegalArgumentException {
		return execMethod(instance.getClass(), null, method, paramInstanceArr);
	}


	/* clone */
	@XAnnotations("not cache option")
	public static Constructor matchConstructor(Constructor[] constructors, Class... paramClassArr) {
		if (null == constructors)
			return null;
		for (int i = 0;i < constructors.length; i++) {
			Constructor constructor2 = constructors[i];
			Class[] parameterTypes = constructor2.getParameterTypes();
			if (parameterTypes.length == paramClassArr.length) {
				boolean b = true;
				for (int i2 = 0;i2 < parameterTypes.length;i2++) {
					if (!XClass.isInstance(paramClassArr[i2], parameterTypes[i2])) {
						b = false;
						break;
					}	
				}
				if (b) {
					return constructor2;
				}
			}
		}
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("invalid constructor method call. invalid parameters.").append("\n");
		stringBuilder.append('\t').append("found: ").append(getObjArrClss2String(paramClassArr)).append("\n").append("\n");
		for (Constructor constructor22 : constructors) {
			stringBuilder.append('\t');
			stringBuilder.append(constructor22.toString());
			stringBuilder.append('\n');
		}
		throw new RuntimeException(stringBuilder.toString());
	}
	@XAnnotations("not cache option")
	public static Constructor[] matchConstructors(Constructor[] constructors, Class... paramClassArr) {
		if (null == constructors)
			return null;
		int forlength = constructors.length;
		if (forlength == 0)
			return XStaticFixedValue.nullConstructorArray;
		Constructor[] cs2 = null;
		int index = 0;
		for (int i = 0;i < forlength;i++) {
			Constructor constructor2 = constructors[i];
			Class[] parameterTypes = constructor2.getParameterTypes();
			if (parameterTypes.length == paramClassArr.length) {
				boolean b = true;
				for (int i2 = 0;i2 < parameterTypes.length;i2++) {
					if (!XClass.isInstance(paramClassArr[i2], parameterTypes[i2])) {
						b = false;
						break;
					}	
				}
				if (b) {
					if (null == cs2)
						cs2 = new Constructor[constructors.length];
					cs2[index] = constructor2;
					index++;
				}
			}
		}
		if (null != cs2)
			return Arrays.copyOf(cs2, index);
		else
			return XStaticFixedValue.nullConstructorArray;
	}
	@XAnnotations("not cache option")
	public static Method matchMethod(Method[] msArr, Class... paramClassArr) {
		if (null == msArr)
			return null;
		int forlength = msArr.length;
		for (int i = 0;i < forlength;i++) {
			Method constructor2 = msArr[i];
			Class[] parameterTypes = constructor2.getParameterTypes();
			if (parameterTypes.length == paramClassArr.length) {
				boolean b = true;
				for (int i2 = 0;i2 < parameterTypes.length;i2++) {
					if (!XClass.isInstance(paramClassArr[i2], parameterTypes[i2])) {
						b = false;
						break;
					}	
				}
				if (b) {
					return constructor2;
				}
			}
		}
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("invalid constructor method call. invalid parameters.").append("\n");
		stringBuilder.append('\t').append("found: ").append(getObjArrClss2String(paramClassArr)).append("\n").append("\n");
		for (Method constructor22 : msArr) {
			stringBuilder.append('\t');
			stringBuilder.append(constructor22.toString());
			stringBuilder.append('\n');
		}
		throw new RuntimeException(stringBuilder.toString());
	}
	@XAnnotations("not cache option")
	public static Method[] matchMethods(Method[] msArr, Class... paramClassArr) {
		if (null == msArr)
			return null;
		int forlength = msArr.length;
		if (forlength == 0)
			return XStaticFixedValue.nullMethodArray;
		Method[] cs2 = null;
		int index = 0;
		for (int i = 0;i < forlength;i++) {
			Method constructor2 = msArr[i];
			Class[] parameterTypes = constructor2.getParameterTypes();
			if (parameterTypes.length == paramClassArr.length) {
				boolean b = true;
				for (int i2 = 0;i2 < parameterTypes.length;i2++) {
					if (!XClass.isInstance(paramClassArr[i2], parameterTypes[i2])) {
						b = false;
						break;
					}	
				}
				if (b) {
					if (null == cs2)
						cs2 = new Method[msArr.length];
					cs2[index] = constructor2;
					index++;
				}
			}
		}
		if (null != cs2)
			return Arrays.copyOf(cs2, index);
		else
			return XStaticFixedValue.nullMethodArray;
	}
	/* ________ */








	@XAnnotations("not cache option")
	public static Constructor matchConstructor(Constructor[] constructors, Object... paramInstanceArr) {
		if (null == constructors)
			return null;
		for (int i = 0;i < constructors.length; i++) {
			Constructor constructor2 = constructors[i];
			Class[] parameterTypes = constructor2.getParameterTypes();
			if (parameterTypes.length == paramInstanceArr.length) {
				boolean b = true;
				for (int i2 = 0;i2 < parameterTypes.length;i2++) {
					if (!XClass.isInstance(paramInstanceArr[i2], parameterTypes[i2])) {
						b = false;
						break;
					}	
				}
				if (b) {
					return constructor2;
				}
			}
		}
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("invalid constructor method call. invalid parameters.").append("\n");
		stringBuilder.append('\t').append("found: ").append(getObjArrClss2String(paramInstanceArr)).append("\n").append("\n");
		for (Constructor constructor22 : constructors) {
			stringBuilder.append('\t');
			stringBuilder.append(constructor22.toString());
			stringBuilder.append('\n');
		}
		throw new RuntimeException(stringBuilder.toString());
	}
	@XAnnotations("not cache option")
	public static Constructor[] matchConstructors(Constructor[] constructors, Object... paramInstanceArr) {
		if (null == constructors)
			return null;
		int forlength = constructors.length;
		if (forlength == 0)
			return XStaticFixedValue.nullConstructorArray;
		Constructor[] cs2 = null;
		int index = 0;
		for (int i = 0;i < forlength;i++) {
			Constructor constructor2 = constructors[i];
			Class[] parameterTypes = constructor2.getParameterTypes();
			if (parameterTypes.length == paramInstanceArr.length) {
				boolean b = true;
				for (int i2 = 0;i2 < parameterTypes.length;i2++) {
					if (!XClass.isInstance(paramInstanceArr[i2], parameterTypes[i2])) {
						b = false;
						break;
					}	
				}
				if (b) {
					if (null == cs2)
						cs2 = new Constructor[constructors.length];
					cs2[index] = constructor2;
					index++;
				}
			}
		}
		if (null != cs2)
			return Arrays.copyOf(cs2, index);
		else
			return XStaticFixedValue.nullConstructorArray;
	}
	@XAnnotations("not cache option")
	public static Method matchMethod(Method[] msArr, Object... paramInstanceArr) {
		if (null == msArr)
			return null;
		int forlength = msArr.length;
		for (int i = 0;i < forlength;i++) {
			Method constructor2 = msArr[i];
			Class[] parameterTypes = constructor2.getParameterTypes();
			if (parameterTypes.length == paramInstanceArr.length) {
				boolean b = true;
				for (int i2 = 0;i2 < parameterTypes.length;i2++) {
					if (!XClass.isInstance(paramInstanceArr[i2], parameterTypes[i2])) {
						b = false;
						break;
					}	
				}
				if (b) {
					return constructor2;
				}
			}
		}
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("invalid constructor method call. invalid parameters.").append("\n");
		stringBuilder.append('\t').append("found: ").append(getObjArrClss2String(paramInstanceArr)).append("\n").append("\n");
		for (Method constructor22 : msArr) {
			stringBuilder.append('\t');
			stringBuilder.append(constructor22.toString());
			stringBuilder.append('\n');
		}
		throw new RuntimeException(stringBuilder.toString());
	}
	@XAnnotations("not cache option")
	public static Method[] matchMethods(Method[] msArr, Object... paramInstanceArr) {
		if (null == msArr)
			return null;
		int forlength = msArr.length;
		if (forlength == 0)
			return XStaticFixedValue.nullMethodArray;
		Method[] cs2 = null;
		int index = 0;
		for (int i = 0;i < forlength;i++) {
			Method constructor2 = msArr[i];
			Class[] parameterTypes = constructor2.getParameterTypes();
			if (parameterTypes.length == paramInstanceArr.length) {
				boolean b = true;
				for (int i2 = 0;i2 < parameterTypes.length;i2++) {
					if (!XClass.isInstance(paramInstanceArr[i2], parameterTypes[i2])) {
						b = false;
						break;
					}	
				}
				if (b) {
					if (null == cs2)
						cs2 = new Method[msArr.length];
					cs2[index] = constructor2;
					index++;
				}
			}
		}
		if (null != cs2)
			return Arrays.copyOf(cs2, index);
		else
			return XStaticFixedValue.nullMethodArray;
	}










	private Map<Class,Constructor[]> hashClassConstructor = new HashMap<Class,Constructor[]>();
	public void clearClassConstructorCache() {
		hashClassConstructor.clear();
	}
	public List<Class> listClassConstructorCache() {
		return XObjects.keys(hashClassConstructor);
	}
	public boolean existConstructorCache(Class cls) {
		return hashClassConstructor.containsKey(cls);
	}
	public void removeClassConstructorCache(Class cls) {
		hashClassConstructor.remove(cls);
	}
	public Constructor[] updateClassConstructorCache(Class cls, Constructor[] newCS) {
		hashClassConstructor.put(cls, newCS);
		return newCS;
	}
	public Constructor[] getClassConstructorCache(Class cls) {
		return hashClassConstructor.get(cls);
	}
	public Constructor getConstructor(Class<?> cls, Object... paramInstanceArr) {
		Constructor[] constructors = hashClassConstructor.get(cls);
		if (null == constructors)
			constructors = updateClassConstructorCache(cls, getConstructorsAll0(cls));
		return matchConstructor(constructors, paramInstanceArr);
	}
	public Constructor[] getConstructors(Class<?> cls, Object...paramInstanceArr) {
		Constructor[] constructors = hashClassConstructor.get(cls);
		if (null == constructors)
			constructors = updateClassConstructorCache(cls, getConstructorsAll0(cls));
		return matchConstructors(constructors, paramInstanceArr);
	}



	private Map<Class, Map<String,Method[]>> hashClassMethod = new HashMap<Class, Map<String,Method[]>>();
	public void clearClassMethodCache() {
		for (Class cls:hashClassMethod.keySet()) {
			Map<String,Method[]> clsmi = hashClassMethod.get(cls);
			if (null != clsmi)
				clsmi.clear();
		}
		hashClassMethod.clear();
	}
	public List<Class> listClassMethodCache() {
		return XObjects.keys(hashClassMethod);
	}
	public boolean existClassMethodCache(Class cls) {
		return hashClassMethod.containsKey(cls);
	}
	public void removeClassMethodCache(Class cls) {
		Map<String,Method[]> clsmi = hashClassMethod.get(cls);
		if (null != clsmi)
			clsmi.clear();
		hashClassMethod.remove(cls);
	}
	public Map<String,Method[]> updateClassMethodCache(Class cls, Method[] clsMethods) {
		Map<String,Method[]> Methods = new HashMap<String,Method[]>();
		Map<String,List<Method>> tmp = new HashMap<String,List<Method>>();
		Method[] ms =  clsMethods;
		for (Method m : ms) {
			if (null == m)
				continue;
			String mn = m.getName();
			List<Method> list = tmp.get(mn);
			if (null == list)
				list = new ArrayList<Method>();
			list.add(m);
			tmp.put(mn, list);
		}
		for (String m:tmp.keySet()) {
			Method[] newMethod = new Method[tmp.get(m).size()];
			tmp.get(m).toArray(newMethod);
			Methods.put(m, newMethod);
		}
		tmp.clear();
		hashClassMethod.put(cls, Methods);
		return Methods;
	}
	public Method[] getClassMethodCache(Class cls, String methodName) {
		return hashClassMethod.get(cls).get(methodName);
	}
	public Map<String,Method[]> getClassMethodCache(Class cls) {
		return hashClassMethod.get(cls);
	}
	public List<String> getClassMethodCacheNameKeys(Class cls) {
		return XObjects.keys(hashClassMethod.get(cls));
	}
	public Method getMethod(Class cls, String MethodName, Object... paramInstanceArr) {
		if (null == MethodName)
			throw new NullPointerException();
		Map<String,Method[]> Methods = hashClassMethod.get(cls);
		if (null == Methods)
			Methods = updateClassMethodCache(cls, getMethodsAll0(cls));
		Method[] ms = Methods.get(MethodName);
		return matchMethod(ms, paramInstanceArr);
	}
	public Method[] getMethods(Class cls, String MethodName, Object... paramInstanceArr) {
		if (null == MethodName)
			throw new NullPointerException();
		Map<String,Method[]> Methods = hashClassMethod.get(cls);
		if (null == Methods)
			Methods = updateClassMethodCache(cls, getMethodsAll0(cls));
		Method[] ms = Methods.get(MethodName);
		return matchMethods(ms, paramInstanceArr);
	}











	private Map<Class, Map<String,Field>> hashClassField = new HashMap<Class, Map<String,Field>>();
	public void clearClassFieldCache() {
		for (Class cls:hashClassField.keySet()) {
			Map<String,Field> clsmi = hashClassField.get(cls);
			if (null != clsmi)
				clsmi.clear();
		}
		hashClassField.clear();
	}
	public List<Class> listClassFieldCache() {
		return XObjects.keys(hashClassField);
	}
	public boolean existClassFieldCache(Class cls) {
		return hashClassField.containsKey(cls);
	}
	public void removeClassFieldCache(Class cls) {
		Map<String,Field> clsmi = hashClassField.get(cls);
		if (null != clsmi)
			clsmi.clear();
		hashClassField.remove(cls);
	}
	public Map<String,Field> updateClassFieldCache(Class cls, Field[] newFields) {
		Map<String,Field> Fields = new HashMap<String,Field>();
		Field[] fs =  newFields;
		for (Field m : fs) {
			if (null == m)
				continue;
			String mn = m.getName();
			Fields.put(mn, m);
		}
		hashClassField.put(cls, null);
		hashClassField.put(cls, Fields);
		return Fields;
	}
	public Map<String,Field> getClassFieldCache(Class cls) {
		return hashClassField.get(cls);
	}
	public List<String> getClassFieldCacheNameKeys(Class cls) {
		return XObjects.keys(hashClassField.get(cls));
	}
	public Field getField(Class cls, String name) {
		if (null == name)
			throw new NullPointerException();
		Map<String,Field> fs = hashClassField.get(cls);
		if (null == fs)
			fs = updateClassFieldCache(cls, getFieldsAll0(cls));
		Field f = fs.get(name);
		return f;
	}
	
	public XReflectMatcher clear() {
		clearClassConstructorCache();
		clearClassMethodCache();
		clearClassFieldCache();
		return this;
	}
}


