package top.fols.box.reflect.proxyx;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import top.fols.atri.lang.Finals;
import top.fols.atri.reflect.ReflectCache;
import top.fols.atri.reflect.ReflectMatcher;
import top.fols.atri.reflect.Reflects;
import top.fols.atri.reflect.ReflectMatcherAsScore;
import top.fols.box.reflect.ReflectMatcherAsPeak;

public class ProxyReflecter {
	protected ReflectMatcher<ReflectCache> m = new ReflectMatcherAsPeak<ReflectCache>(new ReflectCache());
	
	
	
	public <T> T newInstance(Class<T> type) {
		Constructor<T> con = Reflects.getEmptyArgsConstructor(type);
		try {
			return con.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Field field(Class clazz, String name) {
		return m.field(clazz, null, name);
	}
	public Field field(Class clazz, Class type, String name) {
		return m.field(clazz, type, name);
	}
	
	
	public Constructor constructor(Class clazz, Object... value) {
		return m.constructor(clazz, 
							 (null == value ? Finals.EMPTY_OBJECT_ARRAY : value));
	}

	public Method method(Class clazz, String name, Object... value) {
		return m.method(clazz, 
						null,
						name,
						(null == value ? Finals.EMPTY_OBJECT_ARRAY : value));
	}
	public Method method(Class clazz, Class returnType, String name, Object... value) {
		return m.method(clazz, 
						returnType,
						name,
						(null == value ? Finals.EMPTY_OBJECT_ARRAY : value));
	}

	
	

	public Field requireField(Class clazz, String name) {
		return m.requireField(clazz, null, name);
	}
	public Field requireField(Class clazz, Class type, String name) {
		return m.requireField(clazz, type, name);
	}
	
	
	public Constructor requireConstructor(Class clazz, Object... value) {
		return m.requireConstructor(clazz, 
							 (null == value ? Finals.EMPTY_OBJECT_ARRAY : value));
	}

	public Method requireMethod(Class clazz, String name, Object... value) {
		return m.requireMethod(clazz, 
						null,
						name,
						(null == value ? Finals.EMPTY_OBJECT_ARRAY : value));
	}
	public Method requireMethod(Class clazz, Class returnType, String name, Object... value) {
		return m.requireMethod(clazz, 
						returnType,
						name,
						(null == value ? Finals.EMPTY_OBJECT_ARRAY : value));
	}
	
	
}
