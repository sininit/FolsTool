package top.fols.box.reflect;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.WeakHashMap;
import top.fols.atri.lang.Boxing;
import top.fols.atri.lang.Strings;
import top.fols.atri.reflect.Reflects;
import top.fols.atri.util.map.CasesLinkedHashMap;

import static top.fols.box.reflect.Reflectx.*;

@SuppressWarnings({"rawtypes", "UnnecessaryModifier", "unchecked"})
public class ClassProperties {
	public static class Unlocker {
		protected void setAccessibleField(AccessibleObject field) {
			Reflects.accessible(field);
		}
	}
	Unlocker unlocker = new Unlocker();
	protected Unlocker getUnlocker() { return unlocker; }




	static final Class<Object> 	CLASS_OBJECT  	= Object.class;
	static final Class<Boolean> CLASS_BOOLEAN 	= boolean.class;
	static final Class<Void> 	CLASS_VOID    	= void.class;

	static final Field[]  EMPTY_FIELD_ARRAY     = {};
	static final Method[] EMPTY_METHOD_ARRAY    = {};




	public static Class[] classMap(Class clazz) {
		if (null == clazz) { return null; }
        return Reflects.classes(clazz);
    }
	public static Constructor[] constructorMap(Class clazz) {
		if (null == clazz) { return null; }
        return Reflects.constructors(clazz);
    }
    /**
     * signature insensitive
     */
	public static Field[] fieldMap(Class clazz) {
		if (null == clazz) { return null; }
        Map<String, Field> values = new LinkedHashMap<>();
        try {
            Class tempClass = clazz;
            while (null != tempClass) {
                for (Field field : Reflects.getDeclaredFields(tempClass)) {
                    String fieldName = field.getName();
                    if (!values.containsKey(fieldName)) {
						values.put(fieldName, field);
                    }
                }
                tempClass = tempClass.getSuperclass();
            }
        } catch (Exception ignore) {}

        for (Field field : clazz.getFields()) {
            String fieldName = field.getName();
            //if (!values.containsKey(fieldName)) {
		    values.put(fieldName, field);
            //}
        }
        return values.values().toArray(new Field[]{});
    }
    public static Method[] methodMap(Class clazz) {
        if (null == clazz) { return null; }
        Map<Reflects.SignatureMethodWrap, Method> values = new LinkedHashMap<>();
        try {
            Class tempClass = clazz;
            while (null != tempClass) {
                for (Method cf: tempClass.getDeclaredMethods()) {
                    Reflects.SignatureMethodWrap cacheFieldObject = Reflects.SignatureMethodWrap.wrap(cf);
                    Method cacheField = values.get(cacheFieldObject);
                    if (null == cacheField) {
                        values.put(cacheFieldObject, cf);
                    }
                }
                tempClass = tempClass.getSuperclass();
            }
        } catch (Exception ignore) {}

        for (Method cf: clazz.getMethods()) {
            Reflects.SignatureMethodWrap cacheFieldObject = Reflects.SignatureMethodWrap.wrap(cf);
            Method cacheField = values.get(cacheFieldObject);
            if (null == cacheField) {
                values.put(cacheFieldObject, cf);
            }
        }
        return values.values().toArray(EMPTY_METHOD_ARRAY);
    }


	public Class[] 			getClassMap(Class clazz) { return classMap(clazz); }
	public Constructor[] 	getConstructorMap(Class clazz) { return constructorMap(clazz); }
	public Field[] 			getFieldMap(Class clazz) { return fieldMap(clazz); }
    public Method[] 		getMethodMap(Class clazz) { return methodMap(clazz); }





//------------------------/
    /**
     * field or method
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD, ElementType.METHOD})
    public @interface Ignored {}

	public boolean isIgnored(AccessibleObject... objs) {
        for (AccessibleObject obj: objs) {
            if (null != obj) {
				if (obj.getAnnotation(Ignored.class) != null) {
					return true;
				}
            }
        }
        return false;
    }


	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD, ElementType.METHOD})
	public @interface Transient {}

	/**
     * field or method
     */
	public boolean isTransient(AccessibleObject... objs) {
		for (AccessibleObject obj: objs) {
			if (null != obj) {
				if (obj instanceof Field) {
					Field field = (Field) obj;
					if (Modifier.isTransient(field.getModifiers()))
						return true;
				}
				if (obj.getAnnotation(Transient.class) != null) {
					return true;
				}
			}
		}
		return false;
	}
//-----------/
	@Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD, ElementType.METHOD})
    public @interface Name { String value(); }

	public String getProxyName(AccessibleObject... objs) {
		for (AccessibleObject obj: objs) {
            if (null != obj) {
                Name element;
				if ((element = obj.getAnnotation(Name.class)) != null) {
					return element.value();
				}
            }
        }
		return null;
	}
//------------------------/


    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    public @interface ScanConfiguration {
		boolean isScanField()					default true;
		boolean isScanMethod()					default true;

        boolean isScanStaticField()             default false;
		boolean isScanStaticMethod()            default false;
		


        public static final ScanConfiguration DEFAULT = new ScanConfiguration() {
			@Override public boolean isScanField()    {return true;}
			@Override public boolean isScanMethod()   {return true;}
			@Override public boolean isScanStaticField()  {return false;}
			@Override public boolean isScanStaticMethod() {return false;}

            @Override
            public Class<? extends Annotation> annotationType() {
                return ScanConfiguration.class;
            }
            @Override
            public String toString() {
                return "@" + ScanConfiguration.class.getSimpleName() + 
					"(" +
					"isScanField="  + isScanField() + ", " +
					"isScanMethod=" + isScanMethod() + ", " +

					"isScanStaicField="  + isScanStaticField() + ", " +
					"isScanStaicMethod=" + isScanStaticMethod() + ", " +
					
					"hash=" + hashCode() +
					")";
            }
        };
    }
	protected ScanConfiguration getDefaultScanConfiguration() {
		return ScanConfiguration.DEFAULT;
	}
	public ScanConfiguration getClassScanConfiguration(Class clazz) {
		if (null != clazz) {
			ScanConfiguration a;
			if (null != (a = (ScanConfiguration) clazz.getAnnotation(ScanConfiguration.class)))
				return   a;
		}
		return getDefaultScanConfiguration();
	}

//------------------------/



	public static class AccessFieldException extends RuntimeException {
        public AccessFieldException(String var1) {
            super(var1);
        }
        public AccessFieldException(Throwable var1) {
            super(var1);
        }
    }


	public static interface ObjectConstructor<T> {
		public Class<T> getType();
		public T newInstance();
	}
	public static class JavaConstructor<T> implements ObjectConstructor<T> {
		Class<T> type;
		Constructor<T> field;

		public JavaConstructor(Unlocker unlocker, Class<T> type) {
			Constructor<T> field = Reflects.getEmptyArgsConstructor(type);
			if (null != unlocker) unlocker.setAccessibleField(field);

			this.type  = type;
			this.field = field;
		}
		public JavaConstructor(Unlocker unlocker, Class<T> type, Constructor<T> field) {
			if (null != unlocker) unlocker.setAccessibleField(field);

			this.type  = type;
			this.field = field;
		}


		public T newInstance() {
			// TODO: Implement this method
			try {
				return field.newInstance();
			} catch (Exception e) {
				throw new AccessFieldException(e);
			}
		}

		public Constructor getConstructor() { return field; }

		@Override
		public Class<T> getType() {
			// TODO: Implement this method
			return type;
		}
	}



	public static interface Getter {
		public Class  getType();
        public Object get(Object invoke) throws AccessFieldException;
    }
	protected static JavaFieldGetter newJavaFieldGetter(Unlocker unlocker, Field field, Class fieldType) {
		return new JavaFieldGetter(unlocker, field, fieldType);
	}
	public static class JavaFieldGetter implements Getter {
		protected Field field;
		protected Class fieldType;
		public JavaFieldGetter(Unlocker unlocker, Field field, Class fieldType) {
			if (null != unlocker) unlocker.setAccessibleField(field);

			this.field     = field;
			this.fieldType = fieldType;
		}
		@Override
		public Object get(Object invoke) throws AccessFieldException {
			// TODO: Implement this method
			try {
				return this.field.get(invoke);
			} catch (Exception e2) {
				throw new AccessFieldException(e2);
			}
		}
		@Override
		public String toString() {
			// TODO: Implement this method
			return field.toString();
		}

		public Field getField() {
			return field;
		}

		@Override
		public Class getType() {
			// TODO: Implement this method
			return fieldType;
		}
	}
	protected static JavaMethodGetter newJavaMethodGetter(Unlocker unlocker, Method field, Class fieldType) {
		return new JavaMethodGetter(unlocker, field, fieldType);
	}
	public static class JavaMethodGetter implements Getter {
		protected Method field;
		protected Class  fieldType;
		public JavaMethodGetter(Unlocker unlocker, Method field, Class fieldType) {
			if (null != unlocker) unlocker.setAccessibleField(field);

			this.field     = field;
			this.fieldType = fieldType;
		}
		@Override
		public Object get(Object invoke) throws AccessFieldException {
			// TODO: Implement this method
			try {
				return this.field.invoke(invoke);
			} catch (Exception e) {
				throw new AccessFieldException(e);
			}
		}

		@Override
		public String toString() {
			// TODO: Implement this method
			return field.toString();
		}

		public Method getMethod() {
			return field;
		}

		@Override
		public Class getType() {
			// TODO: Implement this method
			return fieldType;
		}
	}



	public static interface Setter {
        public Class   getType();
		public void    set(Object invoke, Object value) throws AccessFieldException;
		public boolean isAvailable();
    }

	protected static JavaFieldSetter newJavaFieldSetter(Unlocker unlocker, Field field, Class setType) {
		if (setType.isPrimitive()) {
			return new JavaFieldSetterPrimitive(unlocker, field, setType);
		} else {
			return new JavaFieldSetter(unlocker, field, setType);
		}
	}
	public static class JavaFieldSetter implements Setter {
		protected Field  field;
		protected Class  fieldType;

		protected JavaFieldSetter(Unlocker unlocker, Field field, Class fieldType) {
			if (null != unlocker) unlocker.setAccessibleField(field);

			this.field = field;
			this.fieldType  = fieldType;
		}
		@Override
		public void set(Object invoke, Object value) throws AccessFieldException {
			// TODO: Implement this method
			try {
				this.field.set(invoke, value);
			} catch (Exception e) {
				throw new AccessFieldException(e);
			}
		}

		@Override
		public String toString() {
			// TODO: Implement this method
			return field.toString();
		}

		@Override public Class getType() { return fieldType; }

		@Override
		public boolean isAvailable() {
			// TODO: Implement this method
			return true;
		}
	}
	public static class JavaFieldSetterPrimitive extends JavaFieldSetter {
		protected Object defaultValue;

		protected JavaFieldSetterPrimitive(Unlocker unlocker, Field field, Class setType) {
			super(unlocker, field, setType);

			this.defaultValue = Boxing.getDefaultValue(this.fieldType);
		}
		@Override
		public void set(Object invoke, Object value) throws AccessFieldException {
			// TODO: Implement this method
			try {
				if (value != null) {
					this.field.set(invoke, value);
				} else {
					this.field.set(invoke, this.defaultValue);
				}
			} catch (Exception e) {
				throw new AccessFieldException(e);
			}
		}

		@Override
		public String toString() {
			// TODO: Implement this method
			return field.toString();
		}

		@Override public Class getType() { return fieldType; }

		@Override
		public boolean isAvailable() {
			// TODO: Implement this method
			return true;
		}
	}

	protected static JavaMethodSetter newJavaMethodSetter(Unlocker unlocker, Method field, Class fieldType) {
		if (fieldType.isPrimitive()) {
			return new JavaMethodSetterPrimitive(unlocker, field, fieldType);
		} else {
			return new JavaMethodSetter(unlocker, field, fieldType);
		}
	}
	public static class JavaMethodSetter implements Setter {
		protected Method field;
		protected Class  fieldType;
		protected JavaMethodSetter(Unlocker unlocker, Method field, Class setType) {
			if (null != unlocker) unlocker.setAccessibleField(field);

			this.field      = field;
			this.fieldType  = setType;
		}
		@Override
		public void set(Object invoke, Object value) throws AccessFieldException {
			// TODO: Implement this method
			try {
				this.field.invoke(invoke, value);
			} catch (Exception e) {
				throw new AccessFieldException(e);
			}
		}

		@Override
		public String toString() {
			// TODO: Implement this method
			return field.toString();
		}

		@Override public Class getType() { return fieldType; }

		@Override
		public boolean isAvailable() {
			// TODO: Implement this method
			return true;
		}
	}
	public static class JavaMethodSetterPrimitive extends JavaMethodSetter {
		protected Object defaultValue;

		protected JavaMethodSetterPrimitive(Unlocker unlocker, Method field, Class setType) {
			super(unlocker, field, setType);

			this.defaultValue = Boxing.getDefaultValue(this.fieldType);
		}

		@Override
		public void set(Object invoke, Object value) throws AccessFieldException {
			// TODO: Implement this method
			try {
				if (value != null) {
					this.field.invoke(invoke, value);
				} else {
					this.field.invoke(invoke, this.defaultValue);
				}
			} catch (Exception e) {
				throw new AccessFieldException(e);
			}
		}
	}
	public static Setter unavailableSetter(final String n) {
		return new Setter() {
			final String name = n;

			@Override
			public Class getType() {
				// TODO: Implement this method
				return CLASS_OBJECT;
			}

			@Override
			public void set(Object field, Object value) {
				// TODO: Implement this method
				throw new UnsupportedOperationException("not found setter: " + this.name);
			}

			@Override
			public String toString() {
				return "unavailable";
			}

			@Override
			public boolean isAvailable() {
				// TODO: Implement this method
				return false;
			}
		};
	}




	public static class ClassProperty {
		ClassProperty() {}

		boolean isInit;
		Class type;
		ObjectConstructor constructor;
		Map<String, Property> propertys;
		Set<String>      	  propertysSet;

		
		
		

		public void setConstructor(ObjectConstructor v) {
			if (isInit) throw new UnsupportedOperationException("already init");
			this.constructor = v;
		}
		public ObjectConstructor getConstructor() { 
			return constructor; 
		}
		
		

		public Property 				propertyGet(Object name)    				 { return propertys.get(name); }
		public int 						propertyCount() 							 { return propertys.size(); }
		public Set<String> 			    propertyKeySet() 							 {
			if (propertys == null)
				return ((((((null))))));
			if (propertysSet == null) 
				propertysSet = Collections.unmodifiableSet(propertys.keySet());
			return propertysSet;
		}
		public Property 				propertyRemove(Object name) 				 {
			if (isInit) throw new UnsupportedOperationException("already init"); 
			return propertys.remove(name); 
		}
		public Property 				propertyPut(String name, Property attritube) { 
			if (isInit) throw new UnsupportedOperationException("already init");
			return propertys.put(name, attritube);
		}


		public <T> T newInstance() {
			ObjectConstructor oc = this.getConstructor();
			if (null == oc) 
				throw new UnsupportedOperationException("no available constructor(): " + type);
			return (T) oc.newInstance();
		}
		public Object get(Object instance, Object name) {
			Property property = propertyGet(name);
			if (null == property)
				throw new UnsupportedOperationException("not found getter " + name);
			return property.get(instance);
		}
		public <T> Object get(Class<T> type, Object instance, Object name) {
			Property property = propertyGet(name);
			if (null == property)
				throw new UnsupportedOperationException("not found getter " + name);
			return property.get(type, instance);
		}
		public void set(Object instance, Object name, Object v) {
			Property property = propertyGet(name);
			if (null == property)
				throw new UnsupportedOperationException("not found setter " + name);
			property.set(instance, v);
		}
		public <T> void set(Class<T> type, Object instance, Object name, Object v) {
			Property property = propertyGet(name);
			if (null == property)
				throw new UnsupportedOperationException("not found setter " + name);
			property.set(type, instance, v);
		}
		
		public Set<String> keySet() {
			return propertyKeySet();
		}

		@Override
		public String toString() {
			// TODO: Implement this method
			return String.valueOf(type) + propertys.toString();
		}
	}
	public static class Property {
		/*require*/ private String oldName;
		/*require*/ private String name;
		/*require*/ private Getter getter;
		/*require*/ private Setter setter;

		private Class declaringClass;

		private boolean isFinal;
		private boolean isTransient;

		private transient Boolean cache_isSettable;


        public String getName()      	  { return name; }
        public Class  getDeclaringClass() { return declaringClass; }

		public Class getGetterType() { return null != getter ? getter.getType() : CLASS_OBJECT; }
		public Class getSetterType() { return null != setter ? setter.getType() : CLASS_OBJECT; }

		public boolean isFinal()     { return isFinal; }
		public boolean isTransient() { return isTransient; }

		public boolean isGettable() {
			return null != getter; 
		}
		public boolean isSettable() {
			Boolean     cache = cache_isSettable;
			if (null == cache) {
				if (null == setter || !setter.isAvailable() || isFinal) {
					cache_isSettable = cache = false;
				} else {
					if (setter instanceof JavaFieldSetter) {
						JavaFieldSetter jfg = (JavaFieldSetter) setter;
						Field  field =  jfg.field;
						cache_isSettable = cache = field != null && !Modifier.isFinal(field.getModifiers());
					} else {
						cache_isSettable = cache = true;
					}
				}
			}
			return cache;
		}

        @Override
        public String toString() {
            // TODO: Implement this method
            return (name)
				+ "("
				+   "getter=" + getter  + ", " + "setter=" + setter + ", "
				+ 	"final="  + isFinal + ", " + "transient="  + isTransient + ", "
				+   "gettype=" + getGetterType() + ", " + "settype=" + getSetterType()
				+ ")";
        }



		public Getter getGetter() {return getter;}
		public Setter getSetter() {return setter;}

		public Object get(Object instance) {
			if (null == instance)
				throw new NullPointerException("instance");
			return get(instance.getClass(), instance);
		}
		public <T> Object get(Class<T> instanceClass, Object instance) {
			Getter getter = this.getGetter();
			if (null == getter)
				throw new UnsupportedOperationException("not found getter " + (instanceClass == null ? "" : instanceClass.getName() + ".") + getName());
			return getter.get(instance);
		}

		public void set(Object instance, Object v) {
			if (null == instance)
				throw new NullPointerException("instance");
			set(instance.getClass(), instance, v);
		}
		public <T> void set(Class<T> instanceClass, Object instance, Object v) {
			Setter setter = this.getSetter();
			if (null == setter)
				throw new UnsupportedOperationException("not found setter " + (instanceClass == null ? "" : instanceClass.getName() + ".") + "." + getName());
			setter.set(instance, v);
		}

		Property() {}
	}

	public static Property newProperty(Unlocker unlocker, Class<?> declaringClass,
									   Field field,
									   String oldName, String finalName,
									   /*@Nullable*/ Method mget, /*@Nullable*/ Method mset,
									   boolean isTransient) {
		Property attr = new Property();
		attr.oldName = oldName;
		attr.name    = finalName;
		attr.declaringClass = declaringClass;

		if (null == mget) {
			if (null == mset) {
				attr.getter = newJavaFieldGetter(unlocker,
												 field, fieldType(field));
				attr.setter = newJavaFieldSetter(unlocker,
												 field, fieldType(field));
				attr.isFinal = Modifier.isFinal(field.getModifiers());
			} else {
				attr.getter = newJavaFieldGetter(unlocker,
												 field, fieldType(field));
				attr.setter = newJavaMethodSetter(unlocker, 
												  mset, setterType(mset));
				attr.isFinal = false;
			}
		} else {
			if (null == mset) {
				attr.getter = newJavaMethodGetter(unlocker, 
												  mget,  getterType(mget));
				attr.setter = newJavaFieldSetter(unlocker,
												 field,  fieldType(field));
				attr.isFinal = Modifier.isFinal(field.getModifiers());
			} else {
				attr.getter = newJavaMethodGetter(unlocker,
												  mget, getterType(mget));
				attr.setter = newJavaMethodSetter(unlocker,
												  mset, setterType(mset));
				attr.isFinal = false;
			}
		}
		attr.isTransient = isTransient;
		return attr;
	}
	public static Property newProperty(Unlocker unlocker, Class<?> declaringClass,
									   String oldName, String finalName,
									   Method mget, /*@Nullable*/ Method mset,
									   boolean isTransient) {
		Property attr = new Property();
		attr.oldName        = oldName;
		attr.name           = finalName;
		attr.declaringClass = declaringClass;


		Getter getter = newJavaMethodGetter(unlocker, 
											mget, getterType(mget));
		Setter setter = null == mset
			? unavailableSetter(finalName) 
			: (newJavaMethodSetter(unlocker,
								   mset, setterType(mset)));

		attr.getter         = getter;
		attr.setter         = setter;
		attr.isFinal 		= !setter.isAvailable();
		attr.isTransient	= isTransient;

		return attr;
	}
	public static Property newProperty(String oldName, String name,
									   Getter getter, Setter setter,
									   Class declaringClass,
									   boolean isFinal, boolean isTransient) {
		Property property = new Property();
		property.oldName        = Objects.requireNonNull(oldName, 	     "oldName");
		property.name           = Objects.requireNonNull(name, 			 "name");
		property.getter         = Objects.requireNonNull(getter, 		 "getter");
		property.setter         = Objects.requireNonNull(setter, 		 "seter");

		property.declaringClass = declaringClass;
		property.isFinal 		= isFinal;
		property.isTransient 	= isTransient;
		return property;
	}






	private final Map<Class, ClassProperty> cache       = new WeakHashMap<>();

	protected void clear() {
		synchronized (cache) { cache.clear(); }
	}

	@SuppressWarnings("UnnecessaryContinue")
	protected ClassProperty buildClassProperty(Class cls) {
		ScanConfiguration configuration = getClassScanConfiguration(cls);

		boolean cScanField        = configuration.isScanField();
		boolean cScanMethod       = configuration.isScanMethod();
		
		boolean cScanStaticField  = configuration.isScanStaticField();
		boolean cScanStaticMethod = configuration.isScanStaticMethod();

		@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
			final Map<String, Property> propertyMap;
		final ClassProperty classProperty;
		classProperty = new ClassProperty();
		classProperty.type = cls;
		classProperty.propertys = propertyMap = newClassPropertyVarMap();

		final Constructor[] constructors = getConstructorMap(cls);
		for (int i = 0; i < constructors.length;i++) {
			final Constructor field = constructors[i];
			if (field.getParameterTypes().length == 0) {
				classProperty.setConstructor(new JavaConstructor(unlocker, cls, field));
				break;
			}
		}
		final Field[] fields = !cScanField ? EMPTY_FIELD_ARRAY : getFieldMap(cls);
		for (int i = 0; i < fields.length;i++) {
			Field field = fields[i];
			if (Modifier.isStatic(field.getModifiers())) {
				if (!cScanStaticField) {
					fields[i] = null;
					continue;
				}
			}
		}
		final Method[] methods = !cScanMethod ? EMPTY_METHOD_ARRAY : getMethodMap(cls);
		for (int i = 0; i < methods.length;i++) {
			Method method = methods[i];
			if (Modifier.isStatic(method.getModifiers())) {
				if (!cScanStaticMethod) {
					methods[i] = null;
					continue;
				}
			} 
		}


		final GetterSetterMethodNameMap gss = new GetterSetterMethodNameMap(); //all method
		for (int i = 0; i < methods.length;i++) {
			Method method = methods[i];
			if (null != method) {
				String name       = method.getName();
				Class[] param     = method.getParameterTypes();

				boolean isGet = param.length == 0 && method.getReturnType() != CLASS_VOID;
				boolean isSet = param.length == 1/*&&method.getReturnType() == CLASS_VOID*/;

				if (isGet) {
					gss.addGetterMethod(new GetterSetterMethodNameMap.MethodElement(method, getterType(method)));
					methods[i] = null;
				} else if (isSet) {
					gss.addSetterMethod(new GetterSetterMethodNameMap.MethodElement(method, setterType(method)));
					methods[i] = null;
				}
			}
		}
//				System.out.println(Joiner.on("\n").withKeyValueSeparator(" = ").join(gss.lowercaseNameGetMethods));
//				System.out.println();
//				System.out.println(Joiner.on("\n").withKeyValueSeparator(" = ").join(gss.lowercaseNameSetMethods));
//				System.out.println();
		FIELD: {
			for (int i = 0; i < fields.length;i++) {
				Field field = fields[i];
				if (null != field) {
					final String oldName  = field.getName();
					final Class fieldType = fieldType(field);

					GetterSetterMethodNameMap.FindMethodElements mgetr = gss.findBestGetterFromFieldName(fieldType, oldName);
					gss.removeGetterMethod(mgetr); //These methods should be removed regardless of whether they match the type or not

					GetterSetterMethodNameMap.FindMethodElements msetr = gss.findBestSetterFromFieldName(fieldType, oldName);
					gss.removeSetterMethod(msetr); //These methods should be removed regardless of whether they match the type or not

					Method mget = (null == mgetr || ! mgetr.isMatch()) ? null : mgetr.getFirstValue().getValue(); //Only select methods that can be matched
					Method mset = (null == msetr || ! msetr.isMatch()) ? null : msetr.getFirstValue().getValue(); //Only select methods that can be matched

					/* ---- */
					String proxyName = getProxyName(field, mget, mset);
					final String finalName = (null != proxyName && !proxyName.isEmpty()) ? proxyName: oldName;

					boolean isIgnored = isIgnored(field, mget, mset);
					if (isIgnored) {
						fields[i] = null;
						continue;
					}
					boolean isTransient = isTransient(field, mget, mset);
					/* ---- */


					Property attr = newProperty(unlocker, field.getDeclaringClass(),
												field,
												oldName, finalName,
												mget, mset,
												isTransient);
					propertyMap.put(finalName, attr);
					fields[i] = null;
				}
			}
		}
//				System.out.println("avail-result: " + result);
//				System.out.println("avail-getter: " + lowercaseNameGSetMethods.lowercaseNameGetMethods);
//				System.out.println("avail-setter: " + lowercaseNameGSetMethods.lowercaseNameSetMethods);
		//理论上get只有一个, 因为没有参数重载只有一个, 排除混淆
		METHOD: {
			for (Map<Class, GetterSetterMethodNameMap.MethodElement> getterElements: gss.getterMethods()) {
				for (GetterSetterMethodNameMap.MethodElement getterElement: getterElements.values())	{
					Class fieldType   = getterElement.getFieldType();
					String methodName = getterElement.getName();
					boolean isPrefix  = isGetterOrSetterPrefix(fieldType, methodName);
					if (isPrefix) {
						@SuppressWarnings("UnnecessaryLocalVariable") 
							GetterSetterMethodNameMap.MethodElement mgetr = getterElement;

						String oldName = toGetterOrSetterAsSimpleFieldName(mgetr.getValue());
						GetterSetterMethodNameMap.FindMethodElements msetr = gss.findBestSetterFromFieldName(fieldType, oldName);

						gss.removeGetterMethod(mgetr);
						gss.removeSetterMethod(msetr);

						Method mget = mgetr.getValue();
						Method mset = null == msetr  || ! msetr.isMatch() ? null : msetr.getFirstValue().getValue();

						/* ---- */
						boolean isIgnored   = isIgnored(mget, mset);
						if (isIgnored) {
							continue;
						}
						boolean isTransient = isTransient(mget, mset);

						String proxyName = getProxyName(mget, mset);
						final String finalName = (null != proxyName && !proxyName.isEmpty()) ? proxyName: oldName;
						/* ---- */

						Property attr = newProperty(unlocker, mget.getDeclaringClass(),
													oldName, finalName,
													mget, mset,
													isTransient);
						propertyMap.put(finalName, attr);
					}
				}
			}
		}
//					System.out.println("avail-result: " + result);
//					System.out.println("avail-getter: " + lowercaseNameGSetMethods.lowercaseNameGetMethods);
//					System.out.println("avail-setter: " + lowercaseNameGSetMethods.lowercaseNameSetMethods);
		return classProperty;
	}

	public ClassProperty getClassProperties(Object obj) {
		if (null == obj) return null;

		return getClassProperties(obj.getClass());
	}
	@SuppressWarnings({"UnnecessaryContinue", "ForLoopReplaceableByForEach"})
	public ClassProperty getClassProperties(Class cls) {
		final ClassProperty cacheClassProperty = cache.get(cls);
		if (null != cacheClassProperty)
			return cacheClassProperty;
		if (null == cls)
			return null;
		synchronized (cache)	{
			ClassProperty buildClassProperty;
			buildClassProperty = buildClassProperty(cls);
			buildClassPropertyAfter(cls, buildClassProperty);
			buildClassProperty.isInit = true;

			cache.put(cls, buildClassProperty);
			return buildClassProperty;
		}
	}




	public static Class fieldType(Field f) {
        return f.getType();
    }
	public static Class getterType(Method getter) {
        return null == getter ? CLASS_OBJECT: getter.getReturnType();
    }
	public static Class setterType(Method setter) {
        return null == setter ? CLASS_OBJECT: setter.getParameterTypes()[0];
    }

	public static boolean isGetterOrSetterPrefix(Class returnType, String n) {
        if (Strings.startsWithIgnoreCase(n, GETTER_NAME_IS_PREFIX)) {
            if (returnType == CLASS_BOOLEAN) {
                return true;
            }
        } else if (Strings.startsWithIgnoreCase(n, GETTER_NAME_PREFIX)) {
            return true;
        } else if (Strings.startsWithIgnoreCase(n, SETTER_NAME_PREFIX)) {
            return true;
        }
        return false;
    }
    public static String toGetterOrSetterAsSimpleFieldName(Method o) {
        Class returnType = getterType(o);
        String n = o.getName();
        int len = n.length();
        if (len > 0) {
            if (Strings.startsWithIgnoreCase(n, GETTER_NAME_IS_PREFIX)) {
                if (returnType == CLASS_BOOLEAN) {
                    n = n.substring(GETTER_NAME_IS_PREFIX.length(), len);
                }
            } else if (Strings.startsWithIgnoreCase(n, GETTER_NAME_PREFIX)) {
                n = n.substring(GETTER_NAME_PREFIX.length(), len);
            } else if (Strings.startsWithIgnoreCase(n, SETTER_NAME_PREFIX)) {
                n = n.substring(SETTER_NAME_PREFIX.length(), len);
            }
            if ((len = n.length()) > 0) {
                if (Character.isLowerCase(n.charAt(0))) {//lineContent > lineContent
                    return n;
                } else if (len > 1) {
                    if (Character.isLowerCase(n.charAt(1))) {//GetLineContent > lineContent
                        return n.substring(0, 1).toLowerCase() + n.substring(1, len);
                    } else {
                        return n;
                    }
                } else {
                    return n.toLowerCase();
                }
            }
        }
        return n;
    }





    @SuppressWarnings("ConstantValue")
	static class GetterSetterMethodNameMap {
        final CasesLinkedHashMap<Map<Class, MethodElement>> getMethods = new CasesLinkedHashMap<>(); //all method
        final CasesLinkedHashMap<Map<Class, MethodElement>> setMethods = new CasesLinkedHashMap<>(); //all method
		final ClassMapperMatcher clm = new ClassMapperMatcher();

		public static class MethodElement {
			Method value;
			public MethodElement(Method value, Class fieldType) {
				this.value     = value;
				this.fieldType = fieldType;
			}


			protected void setValue(Method value) { this.value = value; }
			public Method  getValue() { return value;  }

			Class fieldType;
			protected void  setFieldType(Class fieldType) { this.fieldType = fieldType;}
			public Class    getFieldType() { return fieldType; }

			public String getName() { return getValue().getName(); }
		}


        public void addGetterMethod(MethodElement me) {
			CasesLinkedHashMap<Map<Class, MethodElement>> table = getMethods;
			String name = me.getValue().getName();
			Map<Class, MethodElement> map = table.getExact(name);
			if (null == map) {
				table.put(name, map = new HashMap<>());
			}
            map.put(me.getFieldType(), me);
        }
        public void addSetterMethod(MethodElement me) {
            CasesLinkedHashMap<Map<Class, MethodElement>> table = setMethods;
			String name = me.getValue().getName();
			Map<Class, MethodElement> map = table.getExact(name);
			if (null == map) {
				table.put(name, map = new HashMap<>());
			}
            map.put(me.getFieldType(), me);
        }





		public static class FindMethodElements {
			String name;
			Map<Class, MethodElement> map;

			ClassMapperMatcher.Result<MethodElement> match;
			public FindMethodElements(String name,
									  Map<Class, MethodElement> v,
									  ClassMapperMatcher.Result<MethodElement> match) {
				this.name = name;
				this.map   = v;
				this.match = match;
			}
			public boolean isMatch() {
				return !match.isEmpty();
			}

			public String getName() { return name;}
			public Class         getFirstClass() {
				return match.getFirstClass();
			}
			public MethodElement getFirstValue() {
				return match.getFirstValue();
			}
		}
		/**
		 * best match matching standard getter setter
		 */
        public FindMethodElements findBestGetterFromFieldName(Class fieldType, String name) {
			String n = null;
			Map<Class, MethodElement> v = null;
            if (fieldType == CLASS_BOOLEAN) {//boolean v;
                if (v == null)
                    v = (n = getMethods.findBestKey(GETTER_NAME_IS_PREFIX + name)) 	!= null ? getMethods.getExact(n) : null;//isV()
                if (v == null)
                    v = (n = getMethods.findBestKey(GETTER_NAME_PREFIX + name)) 	!= null ? getMethods.getExact(n) : null;//getV()
                if (v == null)
                    v = (n = getMethods.findBestKey(name)) 							!= null ? getMethods.getExact(n) : null;//equalsName v()
            } else {//String v;
                if (v == null)
                    v = (n = getMethods.findBestKey(GETTER_NAME_PREFIX + name))	    != null ? getMethods.getExact(n) : null;//getV()
			    if (v == null)
                    v = (n = getMethods.findBestKey(name)) 							!=  null ? getMethods.getExact(n) : null;//equalsName v()
            }
			if (v ==   null)
				return null;

			ClassMapperMatcher.Result<MethodElement> result = clm.findAssignableFroms((Map<Class, MethodElement>)v, (Class<?>)fieldType);
			return new FindMethodElements(n, 
										  v,
										  result);
        }
        public FindMethodElements findBestSetterFromFieldName(Class fieldType, String name) {
            String n = null;
			Map<Class, MethodElement> v = null;
            if (v == null)
                v = (n = setMethods.findBestKey(SETTER_NAME_PREFIX + name)) != null ? setMethods.getExact(n) : null;//setV(p)
            if (v == null)
                v = (n = setMethods.findBestKey(name)) 						!= null ? setMethods.getExact(n) : null;//equalsName v(p)

			if (v ==   null)
				return null;


            ClassMapperMatcher.Result<MethodElement> result = clm.findAssignableFroms((Map<Class, MethodElement>)v, (Class<?>)fieldType);
			return new FindMethodElements(n,
										  v,
										  result);
        }

		public void removeGetterMethod(FindMethodElements m) {
			if (null != m) 
				getMethods.removeExact(m.getName());
        }
        public void removeGetterMethod(MethodElement m) {
			if (null != m) 
				getMethods.removeExact(m.getName());
        }

		public void removeSetterMethod(FindMethodElements m) {
			if (null != m) 
				setMethods.removeExact(m.getName());
        }
        public void removeSetterMethod(MethodElement m) {
            if (null != m) 
				setMethods.removeExact(m.getName());
        }

		public List<Map<Class, MethodElement>> getterMethods() {
			return getMethods.valuesList();
        }
		public List<Map<Class, MethodElement>> setterMethods() {
            return setMethods.valuesList();
        }
    }



	static final  ClassProperties DEFAULT = new ClassProperties();
	static public ClassProperties getDefault() {
		return DEFAULT;
	}

	protected void buildClassPropertyAfter(Class cls, ClassProperty baseFieldAttribute) {}
	protected Map<String, Property> newClassPropertyVarMap() {
		return new LinkedHashMap<>();
	}

}

