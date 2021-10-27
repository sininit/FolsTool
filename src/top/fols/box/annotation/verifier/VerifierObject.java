package top.fols.box.annotation.verifier;

import static top.fols.box.annotation.verifier.VerifierManager.*;

public class VerifierObject {
	public static final Class<VerifierObject> CLASS = VerifierObject.class;
	
	
	
//	public static final PublicReflectCache cache = new PublicReflectCache();
//
//	/**
//	 * key 支持.语法 可以逐级获取 可以为{字段} 
//	 */
//	public Object query(String key) {
//		if (null == key || key.length() == 0) {
//			return null;
//		}
//		String separatorRegex = ".";
//		List<String> splits = Strings.split(key, separatorRegex);
//		if (splits.size() == 0) { 
//			return query(this, key);
//		} else {
//			int i = 0;
//			Object value = query(this, splits.get(i));
//			i++;
//			for (; i < splits.size(); i++) {
//				value = query(value, splits.get(i));
//			}
//			return value;
//		}
//	}
//
//	/**
//	 * @param name 符合Java规范的名称
//	 */
//	static Object query(Object object, String name) {
//		if (null == object) {
//			return null;
//		}
//		Class<?> type = object.getClass();
//		ReflectCache.FieldList fieldList = cache.getFieldsList(type, name);
//		if (!(null == fieldList || fieldList.list().length == 0)) {
//			Field element = fieldList.list()[0];
//			return value(object, element);
//		}
//		throw new NoSuchFieldError(name);
//	}
//
//	static Object value(Object instance, Field element) {
//		if (null == element) {  
//			return null;
//		}
//		element = Reflects.accessible(element);
//		try {
//			instance = element.get(instance);
//		} catch (Throwable e) {
//			throw new UnsupportedOperationException(e);
//		}
//		return instance;
//	}
//



	String key;
	public void setKey(String key) {
		this.key = key;
	}

	public String key() {
		return this.key;
	}
}
