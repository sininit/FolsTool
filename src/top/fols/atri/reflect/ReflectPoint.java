package top.fols.atri.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SuppressWarnings({"PointlessBooleanExpression", "SpellCheckingInspection"})
public class ReflectPoint implements Cloneable {

	public static class StaticOption implements Cloneable {
        private Class<?> optionClass;

        public StaticOption(Class<?> cls) {
			this.optionClass = cls;
		}
        public void setOptionClass(Class<?> optionClass) { this.optionClass = optionClass; }
        public Class<?> getOptionClass() { return optionClass; }

		@Override
		public int hashCode() {
			// TODO: Implement this method
			return this.getOptionClass().hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			// TODO: Implement this method
			if (this == obj) { return true; }
			if (obj instanceof StaticOption == false) { return false; }
			StaticOption value = (StaticOption) obj;
			return this.optionClass == value.optionClass;
		}

		@Override
		public String toString() {
			// TODO: Implement this method
			return ("static operation: "+ this.optionClass.getName());
		}

		@Override
		public StaticOption clone() {
			// TODO: Implement this method
			return new StaticOption(this.getOptionClass());
		}
    }

	private ReflectMatcher matcher;
	private Object object;
	public ReflectPoint(ReflectMatcher matcher, Object object) {
		this.matcher = matcher;
		this.object = object;
	}
	public static ReflectPoint from(ReflectMatcher matcher, Object object) {
		return new ReflectPoint(matcher, object);
	}
	public static ReflectPoint fromClass(ReflectMatcher matcher, Class<?> object) {
		return new ReflectPoint(matcher, new StaticOption(object));
	}





	/**
	 * join new instance
	 */
	public ReflectPoint newInstance(Object... args) throws InvocationTargetException, InstantiationException, IllegalAccessException, IllegalArgumentException {
		Constructor<?> c = constructor(args);
		return from(this.matcher, c.newInstance(args));
	}
	public ReflectPoint newInstance(Class<?>[] argsTypes, Object... args) throws InvocationTargetException, InstantiationException, IllegalAccessException, IllegalArgumentException {
		Constructor<?> c = constructor(argsTypes);
		return from(this.matcher, c.newInstance(args));
	}


	public Object newInstanceValue(Object... args) throws InvocationTargetException, InstantiationException, IllegalAccessException, IllegalArgumentException {
		Constructor<?> c = constructor(args);
		return c.newInstance(args);
	}
	public Object newInstanceValue(Class<?>[] argsTypes, Object... args) throws InvocationTargetException, InstantiationException, IllegalAccessException, IllegalArgumentException {
		Constructor<?> c = constructor(argsTypes);
		return c.newInstance(args);
	}







    public Constructor<?> constructor(Object... args) {
		return (Constructor<?>) this.matcher().getConstructor(this.valueClass(), args);
	}
	public Constructor<?> constructor(Class<?>[] argsTypes) {
		return (Constructor<?>) this.matcher().getConstructor(this.valueClass(), argsTypes);
	}
	public Constructor<?>[] constructors() {
		return (Constructor<?>[]) this.matcher().cacher().constructors(this.valueClass());
	}



	/**
	 * join field
	 */
    public ReflectPoint get(String name) throws IllegalAccessException, IllegalArgumentException {
        return this.get(null, name);
    }
	public ReflectPoint get(Class<?> returnClass, String name) throws IllegalAccessException, IllegalArgumentException {
		Field f = field(returnClass, name);
		return from(this.matcher, f.get(this.value()));
	}


	public Object getValue(String name) throws IllegalAccessException, IllegalArgumentException {
		return this.getValue(null, name);
	}
	public Object getValue(Class<?> returnClass, String name) throws IllegalAccessException, IllegalArgumentException {
        Field f = field(returnClass, name);
        return f.get(this.value());
    }





    public Field field(String name) {
		return field(null, name);
	}
	public Field field(Class<?> returnClass, String name) {
		return this.matcher().getField(this.valueClass(), returnClass, name);
	}
	public Field[] fields() {
		return this.cacher().fields(this.valueClass());
	}


	public ReflectCache cacher() {
		return this.matcher().cacher();
	}




	/*
	 * no callback
	 */
	public ReflectPoint set(String name, Object value) throws IllegalAccessException, IllegalArgumentException {
        return this.set(null, name, value);
    }
	public ReflectPoint set(Class<?> returnClass, String name, Object value) throws IllegalAccessException, IllegalArgumentException {
		Field f = this.matcher().getField(this.valueClass(), returnClass, name);
		f.set(this.value(), value);
		return from(this.matcher, value);
	}


	public Object setValue(String name, Object value) throws IllegalAccessException, IllegalArgumentException {
		return this.setValue(null, name, value);
	}
	public Object setValue(Class<?> returnClass, String name, Object value) throws IllegalAccessException, IllegalArgumentException {
        Field f = this.matcher().getField(this.valueClass(), returnClass, name);
        f.set(this.value(), value);
        return value;
    }



	/**
	 * join method result
	 */
    public ReflectPoint invoke(String name, Object... args) throws InvocationTargetException, IllegalAccessException, IllegalArgumentException {
        return this.invoke(null, name, args);
    }
    public ReflectPoint invoke(Class<?> returnClass, String name, Object... args) throws InvocationTargetException, IllegalAccessException, IllegalArgumentException {
        Method m = method(returnClass, name, args);
		return from(this.matcher, m.invoke(this.value(), args));
    }
	public ReflectPoint invoke(Class<?> returnClass, String name, Class<?>[] argsTypes, Object... args) throws InvocationTargetException, IllegalAccessException, IllegalArgumentException {
		Method m = method(returnClass, name, argsTypes);
		return from(this.matcher, m.invoke(this.value(), args));
	}



	public Object invokeValue(String name, Object... args) throws InvocationTargetException, IllegalAccessException, IllegalArgumentException {
		return this.invokeValue(null, name, args);
	}
	public Object invokeValue(Class<?> returnClass, String name, Object... args) throws InvocationTargetException, IllegalAccessException, IllegalArgumentException {
		Method m = method(returnClass, name, args);
		return m.invoke(this.value(), args);
	}
	public Object invokeValue(Class<?> returnClass, String name, Class<?>[] argsTypes, Object... args) throws InvocationTargetException, IllegalAccessException, IllegalArgumentException {
		Method m = method(returnClass, name, argsTypes);
		return m.invoke(this.value(), args);
	}





	public Method method(String name, Object... args) {
		return method(null, name, args);
	}
	public Method method(Class<?> returnClass, String name, Object... args) {
		return this.matcher().getMethod(this.valueClass(), returnClass, name, args);
	}
    public Method method(Class<?> returnClass, String name, Class<?>[] argsTypes) {
		return this.matcher().getMethod(this.valueClass(), returnClass, name, argsTypes);
	}
	public Method[] methods() {
		return this.cacher().methods(this.valueClass());
	}






    public ReflectPoint 	matcher(ReflectMatcher obj) { this.matcher = obj; return this; }
    public ReflectMatcher 	matcher() {  return this.matcher; }

    public Object 			value() { return (this.object instanceof StaticOption) ? ((StaticOption) this.object).optionClass : this.object; }
	public Class<?> 		valueClass() {
        return null == this.object ? null
            : (this.object instanceof StaticOption ? ((StaticOption) this.object).optionClass
            : this.object.getClass());
    }


    public boolean 		staticOption() {  return this.object instanceof StaticOption; }
	public boolean 		objectOption() {  return this.object instanceof StaticOption == false; }

    private ReflectPoint() { }

	@Override
	public boolean equals(Object obj) {
		// TODO: Implement this method
		if (null  == (obj)) { return false; }
		if (false == (obj instanceof ReflectPoint)) { return false; }

		ReflectPoint value = (ReflectPoint)obj;
		return null != this.object && this.object.equals(value);
	}

	@Override
	public int hashCode() {
		// TODO: Implement this method
		Object value = this.value();
		return null == value ?0: value.hashCode();
	}

	@Override
	public String toString() {
		// TODO: Implement this method
		Object value = this.value();
		return null == value ?null: value.toString();
	}

    @Override
    public ReflectPoint clone() {
		// TODO Auto-generated method stub
        ReflectPoint newInstance = new ReflectPoint();
        newInstance.object = this.object;
        newInstance.matcher = this.matcher;
        return newInstance;
    }


}
