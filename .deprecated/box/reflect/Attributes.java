package top.fols.box.reflect;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import top.fols.atri.assist.json.JSONObject;
import top.fols.atri.interfaces.interfaces.IFilter;
import top.fols.atri.lang.Finals;
import top.fols.atri.lang.Mathz;
import top.fols.atri.lang.Objects;
import top.fols.atri.lang.Strings;
import top.fols.atri.reflect.Reflects;
import top.fols.atri.util.Iterators;
import top.fols.box.reflect.AttributesFieldAccessor;
import top.fols.box.util.Collectionz;

import static top.fols.box.reflect.Reflectx.*;

@SuppressWarnings({"rawtypes", "TryWithIdenticalCatches", "SynchronizeOnNonFinalField", "UnusedLabel", "ArraysAsListWithZeroOrOneArgument", "unchecked", "RedundantCast", "ConstantConditions"})
public class Attributes implements Cloneable, AttributesFieldAccessor, Map<String, Object> {

//------------------------/
    /**
     * ignored field or method
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD, ElementType.METHOD})
    public  @interface Ignored {}

//-----------/

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    public @interface ScanConfiguration {
        /**
         * Scan static fields without any annotations
         *
         * 默认不扫描不带任何注解的 static 变量 如果需要可以添加这个注解
         */
        boolean  isScanStaicField()             default false;
//		String[] isScanDefaultGetAndSetMethod() default {"public"};

        public static final ScanConfiguration DEFAULT = new ScanConfiguration() {
//			@Override
//			public boolean isScanPublicGetAndSetMethod() {
//				// TODO: Implement this method
//				return false;
//			}

            @Override
            public boolean isScanStaicField() {
                // TODO: Implement this method
                return false;
            }

            @Override public Class<? extends Annotation> annotationType() { return ScanConfiguration.class; }
            @Override
            public String toString() {
                Attributes attrs = new Attributes();
                attrs.setAttribute("isScanStaicField", isScanStaicField());
//				attrs.setAttribute("isScanPublicGetAndSetMethod", isScanPublicGetAndSetMethod());
                return "@" + ScanConfiguration.class.getName()
                        + attrs.deepToJson().toString()
                        .replace("{", "(")
                        .replace("}", ")");
            }
        };
    }


//-----------/

    /**
     * Corresponds to a get () and a set() method.
     *   When accessing a variable,
     *   the variable will be accessed from the method rather than directly
     *
     * 对应一个get()和一个set()方法，访问变量的时候将从方法访问而不是直接访问变量
     *
     * 可以通过value指定变量名 但是方法名要对应
     * get变量名() 方法参数需要为0个 set变量名()方法参数需要为1个
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    public @interface GetterAndSetterField {
        String value() default "";
    }



//------------------------/

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    public @interface Private {}

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    public  @interface Public {}

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    public  @interface Final {}


//------------------------/
    /**
     * The value corresponding to Setter specifies the variable name,
     *   and the Getter method parameter needs to be 0
     *
     * 和Setter对应 value指定变量名，Getter方法参数需要为0个
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD})
    public @interface Getter {
        String value() default "";
    }
    /**
     * The value corresponding to Getter specifies the variable name,
     *   and the Setter method parameter needs to be 1
     *
     * 和Getter对应 value指定变量名，Setter方法参数需要为1个
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD})
    public @interface Setter {
        String value() default "";
    }

//------------------------/

    static final Annotation[] EMPTY_ANNOTATION_ARRAY = new Annotation[]{};
    static final Set<Class<? extends Annotation>> ANNOTATIONS;
    static {
        ANNOTATIONS = new HashSet<>();
        for (Class cls: Attributes.class.getDeclaredClasses()) {
            if (cls.isAnnotation()) {
                ANNOTATIONS.add(cls);
            }
        }
    }
    static boolean hasAttributesAnnotation(AccessibleObject obj) {
        Annotation[] annotations = obj.getAnnotations();
        if (null !=  annotations) {
            for (Annotation annotation : annotations) {
                Class clasz = annotation.getClass();
                if (ANNOTATIONS.contains(clasz)) {
                    return true;
                } else {
                    Class[] interfaces = clasz.getInterfaces();
                    for (Class i: interfaces) {
                        if (ANNOTATIONS.contains(i)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }


    public static class AccessFieldException extends RuntimeException {
        public AccessFieldException(String var1) {
            super(var1);
        }
        public AccessFieldException(Throwable var1) {
            super(var1);
        }
    }

    public static abstract class AttrGetter {
        public abstract Object get(Object attribute, Attribute attr);

        static class AttributeGetter extends AttrGetter {
            @Override
            public Object get(Object attribute, Attribute attr) {
                // TODO: Implement this method
                return attr.value;//实际上所有的Attribute都是新的, 不会使用上一个类的Attr
            }
        }
        static class FieldGetter extends AttrGetter {
            Field field;
            public FieldGetter(Object attributes, Field field) {
                setAccessibleField(attributes, field);
                this.field = field;
            }
            @Override
            public Object get(Object attribute, Attribute attr) {
                // TODO: Implement this method
                try {
                    return field.get(attribute);
                } catch (IllegalAccessException e) {
                    throw new AccessFieldException(e);
                } catch (IllegalArgumentException e2) {
                    throw new AccessFieldException(e2);
                }
            }
        }
        static class MethodGetter extends AttrGetter {
            Method field;
            public MethodGetter(Object attributes, Method field) {
                setAccessibleField(attributes, field);
                this.field = field;
            }
            @Override
            public Object get(Object attribute, Attribute attr) {
                // TODO: Implement this method
                try {
                    return field.invoke(attribute);
                } catch (IllegalAccessException e) {
                    throw new AccessFieldException(e);
                } catch (IllegalArgumentException e2) {
                    throw new AccessFieldException(e2);
                } catch (InvocationTargetException e) {
                    throw new AccessFieldException(e);
                }
            }
        }
    }

    public static abstract class AttrSetter {
        public abstract void set(Object attribute, Attribute attr, Object value);

        static class AttributeSetter extends AttrSetter {
            @Override
            public void set(Object attribute, Attribute attr, Object value) {
                // TODO: Implement this method
                attr.value = value;//实际上所有的Attribute都是新的, 不会使用上一个类的Attr
            }
        }
        static class FieldSetter extends AttrSetter {
            Field field;
            public FieldSetter(Object attributes, Field field) {
                setAccessibleField(attributes, field);
                this.field = field;
            }
            @Override
            public void set(Object attribute, Attribute attr, Object value) {
                // TODO: Implement this method
                try {
                    field.set(attribute, value);
                } catch (IllegalAccessException e) {
                    throw new AccessFieldException(e);
                } catch (IllegalArgumentException e) {
                    throw new AccessFieldException(e);
                }
            }
        }
        @SuppressWarnings("RedundantArrayCreation")
        static class MethodSetter extends AttrSetter {
            Method field;
            public MethodSetter(Object attributes, Method field) {
                setAccessibleField(attributes, field);
                this.field = field;
            }
            @Override
            public void set(Object attribute, Attribute attr, Object value) {
                // TODO: Implement this method
                try {
                    field.invoke(attribute, new Object[]{value});
                } catch (IllegalAccessException e) {
                    throw new AccessFieldException(e);
                } catch (IllegalArgumentException e) {
                    throw new AccessFieldException(e);
                } catch (InvocationTargetException e) {
                    throw new AccessFieldException(e);
                }
            }
        }
    }
    public static class Attribute {
        public static final int PRIVATE = 0x00000001;
        public static final int FINAL   = 0x00000002;

        private boolean isJavaField;
        private Class   clasz;
        private int     mod;
        private String  name;
        private AttrGetter getter;
        private AttrSetter setter;

        private Object value;//普通变量值存储器 field和method并不是用这个


        Attribute() {}

        public static final int GROUP_UN_GETTABLE = PRIVATE;
        public static final int GROUP_UN_SETTABLE = PRIVATE | FINAL;

        public boolean isPrivate() { return (this.mod & PRIVATE) != 0;}
        private void setPrivate0(boolean b) {
            if (b) {
                this.mod |= PRIVATE;
            } else {
                mod = mod & ~GROUP_UN_GETTABLE;
            }
        }

        public boolean isFinal() { return (this.mod & FINAL) != 0; }
        private void setFinal0(boolean b) {
            if (b) {
                this.mod |= FINAL;
            } else {
                mod = mod & ~GROUP_UN_GETTABLE;
            }
        }
        //
        boolean inadmissibilityGettable()  { return (this.mod & GROUP_UN_GETTABLE) != 0; }
        boolean inadmissibilitySettable()  { return (this.mod & GROUP_UN_SETTABLE) != 0; }
        boolean hasRemovable() { return !(isJavaField) && inadmissibilitySettable();  }

        @Override
        public String toString() {
            // TODO: Implement this method
            return clasz.getName() + "." + name;
        }
    }


    static int ModifierAndAnnotationsAsModifier(int fm, Annotation[] annotations) {
        int mod = 0;

        if (Modifier.isPrivate(fm)) mod |= Attribute.PRIVATE;
        if (Modifier.isFinal(fm))   mod |= Attribute.FINAL;
        //if (Modifier.isPublic(fm))  hashPublic = true; //no!!! don't count this

        boolean hashPublicAnnotation = false;
        if (annotations != null) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof Public)  { hashPublicAnnotation = true; }
                if (annotation instanceof Private) { mod |= Attribute.PRIVATE; }
                if (annotation instanceof Final)   { mod |= Attribute.FINAL; }
            }
        }
        //public
        if (hashPublicAnnotation)
            mod = mod & ~Attribute.GROUP_UN_GETTABLE;

        return mod;
    }
    static int FieldAsModifier(Field f) {
        int mod = ModifierAndAnnotationsAsModifier(f.getModifiers(), f.getAnnotations());
        if (null != f.getAnnotation(GetterAndSetterField.class) &&
                null == f.getAnnotation(Private.class))
            mod = mod & ~Attribute.GROUP_UN_GETTABLE;
        return mod;
    }
    static int GetterAndSetterAsModifier(Method getter, Method setter) {
        return
                ModifierAndAnnotationsAsModifier(0, getter.getAnnotations()) |
                        ModifierAndAnnotationsAsModifier(0, null == setter ? EMPTY_ANNOTATION_ARRAY : setter.getAnnotations());
    }


    static Attribute findSupperAttribute(Object instance, Class cls, String name) {
        Class c = cls.getSuperclass();
        if (null != c) {
            do {
                if (c == ATTRIBUTES_CLASS) break;
                Map<String, Attribute> l = ATTRIBUTES_CLASS_ATTRIBUTE.get(c);
                if (null == l) {
                    ATTRIBUTES_CLASS_ATTRIBUTE.put(c, l = getAttributesClassBaseFieldAttribute(instance, c));
                }
                if (null != l) {
                    Attribute a = l.get(name);
                    if (null != a) {
                        return  a;
                    }
                }
            } while (null != (c = c.getSuperclass()));
        }
        return null;
    }



    static String toLowerCaseName(String o) {
        if (null == o) return null;
        return o.toLowerCase();
    }
    static String toSubstring(String o, int st, int ed) {
        if (null == o) return null;
        return o.substring(st, Mathz.min(o.length(), ed));
    }

    static String toGetterOrSetterAsSimpleName(Method o) {
        Class returnType = o.getReturnType();
        String n = o.getName();
        int len = n.length();
        if (len > 0) {
            if (len >= GETTER_NAME_PREFIX.length() ||
                    len >= SETTER_NAME_PREFIX.length() ||
                    len >= GETTER_NAME_IS_PREFIX.length()) {

                if (toSubstring(n, 0, GETTER_NAME_IS_PREFIX.length()).equalsIgnoreCase(GETTER_NAME_IS_PREFIX)) {
                    if (returnType == Finals.BOOLEAN_CLASS) {
                        n = n.substring(GETTER_NAME_IS_PREFIX.length(), len);
                    }
                } else if (toSubstring(n, 0, GETTER_NAME_PREFIX.length()).equalsIgnoreCase(GETTER_NAME_PREFIX)) {
                    n = n.substring(GETTER_NAME_PREFIX.length(), len);
                } else if (toSubstring(n, 0, SETTER_NAME_PREFIX.length()).equalsIgnoreCase(SETTER_NAME_PREFIX)) {
                    n = n.substring(SETTER_NAME_PREFIX.length(), len);
                }
                len = n.length();
            }
            if (len > 0) {
                if (Character.isLowerCase(n.charAt(0))) {//lineContent
                    return n;
                } else if (len > 1) {
                    if (Character.isLowerCase(n.charAt(1))) {//getLineContent
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


    static Map<String, Attribute> getAttributesClassBaseFieldAttribute(Object attributes, Class cls) {
        Map<String, Attribute> cache = ATTRIBUTES_CLASS_ATTRIBUTE.get(cls);
        if (null == cache) {
            synchronized (ATTRIBUTES_CLASS_ATTRIBUTE)	{
                ScanConfiguration configuration;
                if (null == (configuration = (ScanConfiguration) cls.getAnnotation(ScanConfiguration.class)))
                    configuration = ScanConfiguration.DEFAULT;
                boolean cScanStaticField = configuration.isScanStaicField();

                final Field[] fields   = Reflects.fields(cls);
                for (int i = 0; i < fields.length;i++) {
                    Field field = fields[i];
                    if (ATTRIBUTES_SUPERCLASS.contains(field.getDeclaringClass())) {
                        fields[i] = null;
                        continue;
                    }
                    if (null != field.getAnnotation(Ignored.class)) {
                        fields[i] = null;
                        continue;
                    }

                    if (Modifier.isStatic(field.getModifiers())) {
                        if (!cScanStaticField && !hasAttributesAnnotation(field)) {
                            fields[i] = null;
                            continue;
                        }
                    }
                }
                final Method[] methods = Reflects.methods(cls);
                for (int i = 0; i < methods.length;i++) {
                    Method method = methods[i];
                    if (ATTRIBUTES_SUPERCLASS.contains(method.getDeclaringClass())) {
                        methods[i] = null;
                        continue;
                    }
                    if (null != method.getAnnotation(Ignored.class)) {
                        methods[i] = null;
                        continue;
                    }
                }
                final Map<String, Method> lowercaseNameGetMethods = new LinkedHashMap<>(); //all method
                final Map<String, Method> lowercaseNameSetMethods = new LinkedHashMap<>(); //all method

                final Map<String, Method> annotationsSimpleNameGetter  = new LinkedHashMap<>();
                final Map<String, Method> annotationsSimpleNameSetter  = new LinkedHashMap<>();
                for (int i = 0; i < methods.length;i++) {
                    Method method = methods[i];
                    if (null != method) {
                        String name       = method.getName();
                        Class[] param     = method.getParameterTypes();
                        String simpleName = toLowerCaseName(name);


                        boolean isGet = param.length == 0 && method.getReturnType() != Finals.VOID_CLASS;
                        boolean isSet = param.length == 1 && method.getReturnType() == Finals.VOID_CLASS;

                        Getter getter = method.getAnnotation(Getter.class);
                        if (null != getter && !isGet) {
                            throw new UnsupportedOperationException("error param count @Getter: " + Arrays.asList(method));
                        }
                        Setter setter = method.getAnnotation(Setter.class);
                        if (null != setter && !isSet) {
                            throw new UnsupportedOperationException("error param count @Setter: " + Arrays.asList(method));
                        }
                        if (null != getter && null != setter) {
                            throw new UnsupportedOperationException("not at the same time @Getter @Setter: " + Arrays.asList(method));
                        }

                        if (isGet) {
                            if (lowercaseNameGetMethods.containsKey(simpleName)) {
                                throw new UnsupportedOperationException("repeat method: " + Arrays.asList(method, lowercaseNameGetMethods.get(simpleName)));
                            } else {
                                lowercaseNameGetMethods.put(simpleName, method);
                                methods[i] = null;
                            }

                            if (null != getter) {
                                String n = Strings.empty(getter.value()) ? toGetterOrSetterAsSimpleName(method) : getter.value();
                                if (annotationsSimpleNameGetter.containsKey(n)) {
                                    throw new UnsupportedOperationException("repeat @Getter: " + Arrays.asList(annotationsSimpleNameGetter.get(n), method));
                                } else {
                                    annotationsSimpleNameGetter.put(n, method);
                                }
                            }
                        } else if (isSet) {
                            if (lowercaseNameSetMethods.containsKey(simpleName)) {
                                throw new UnsupportedOperationException("repeat method: " + Arrays.asList(method, lowercaseNameSetMethods.get(simpleName)));
                            } else {
                                lowercaseNameSetMethods.put(simpleName, method);
                                methods[i] = null;
                            }

                            if (null != setter) {
                                String n = Strings.empty(setter.value()) ? toGetterOrSetterAsSimpleName(method) : setter.value();
                                if (annotationsSimpleNameSetter.containsKey(n)) {
                                    throw new UnsupportedOperationException("repeat @Setter: " + Arrays.asList(annotationsSimpleNameSetter.get(n), method));
                                } else {
                                    annotationsSimpleNameSetter.put(n, method);
                                }
                            }
                        }
                    }
                }


                final Map<String, Attribute> result = new LinkedHashMap<>();
                FIELD: {
                    Map<String, String>         FIELD_GETTERANDSETTERFIELD_LowerCaseNameMappingMaNameMap = new LinkedHashMap<>(); //简单名称对应真实名称
                    /*    */Map<String, Field>  FIELD_GETTERANDSETTERFIELD_MaNameFieldMap                = new LinkedHashMap<>(); //          真实名称对应字段
                    FIELD_FIND: for (int i = 0; i < fields.length;i++) {
                        Field field = fields[i];
                        if (null != field) {
                            GetterAndSetterField ma = field.getAnnotation(GetterAndSetterField.class);
                            if (null == ma) {
                                //检测不同类是否重复field
                                String fieldName = field.getName();
                                Attribute    pa;
                                if (null == (pa = findSupperAttribute(attributes, cls, fieldName)))
                                    pa = result.get(fieldName);

                                if (null == pa) {
                                    Attribute m = new Attribute();
                                    m.isJavaField = true;
                                    m.clasz = cls;
                                    m.name = fieldName;
                                    m.mod = FieldAsModifier(field);
                                    m.getter = new AttrGetter.FieldGetter(attributes, field);
                                    m.setter = new AttrSetter.FieldSetter(attributes, field);

                                    result.put(fieldName, m);
                                    fields[i] = null;
                                } else {
                                    if (field.getDeclaringClass() == cls) {
                                        throw new UnsupportedOperationException("same attribute as parent class: " + Arrays.asList("parent=" + pa, "name=" + fieldName, field, "result=" + result));
                                    } else {
                                        result.put(fieldName, pa);
                                        fields[i] = null;
                                    }
                                }
                            } else {
                                String maName = ma.value();
                                if (maName.length() == 0)
                                    maName = field.getName();

                                String simpleName = toLowerCaseName(maName);
                                if (null != FIELD_GETTERANDSETTERFIELD_LowerCaseNameMappingMaNameMap.remove(simpleName)) {
                                    throw new UnsupportedOperationException("repeat @GetterAndSetterField name: " + Arrays.asList("name=" + maName, "result=" + result));
                                } else {
                                    FIELD_GETTERANDSETTERFIELD_LowerCaseNameMappingMaNameMap.put(simpleName, maName);
                                    /*                       */FIELD_GETTERANDSETTERFIELD_MaNameFieldMap.put(maName, field);
                                    fields[i] = null;
                                }
                            }
                        }
                    }
                    FIELD_GETTERANDSETTERFIELD: {  //GetterAndSetterField
                        for (String needLowerCaseName: FIELD_GETTERANDSETTERFIELD_LowerCaseNameMappingMaNameMap.keySet().toArray(Finals.EMPTY_STRING_ARRAY)) {
                            final String maName = FIELD_GETTERANDSETTERFIELD_LowerCaseNameMappingMaNameMap.get(needLowerCaseName);
                            final Field field   = FIELD_GETTERANDSETTERFIELD_MaNameFieldMap.get(maName);

                            Class fieldType = field.getType();
                            Method mget = null;
                            if (fieldType == Finals.BOOLEAN_CLASS) {//boolean v;
                                if (null == mget)
                                    mget = lowercaseNameGetMethods.get(GETTER_NAME_IS_PREFIX + needLowerCaseName);//isV()
                                if (null == mget)
                                    mget = lowercaseNameGetMethods.get(GETTER_NAME_PREFIX + needLowerCaseName);//getV()
                                if (null == mget)
                                    mget = lowercaseNameGetMethods.get(needLowerCaseName);//equalsName v()
                            } else {//String v;
                                if (null == mget)
                                    mget = lowercaseNameGetMethods.get(GETTER_NAME_PREFIX + needLowerCaseName);//getV()
                                if (null == mget)
                                    mget = lowercaseNameGetMethods.get(needLowerCaseName);//equalsName v()
                            }
                            Collectionz.removeValueMappingKey(lowercaseNameGetMethods, mget);

                            if (null == mget)
                                throw new UnsupportedOperationException("not found @GetterAndSetterField getter: " + maName + ", getterList=" + lowercaseNameGetMethods);

                            Method mset = null;
                            if (null == mset)
                                mset = lowercaseNameSetMethods.get(SETTER_NAME_PREFIX + needLowerCaseName);//setV(p)
                            if (null == mset)
                                mset = lowercaseNameSetMethods.get(needLowerCaseName);//equalsName v(p)
                            Collectionz.removeValueMappingKey(lowercaseNameSetMethods, mset);



                            Attribute    pa;
                            if (null == (pa = findSupperAttribute(attributes, cls, maName)))
                                pa = result.get(maName);

                            if (null == pa) {
                                Attribute m = new Attribute();
                                m.isJavaField = true;
                                m.clasz = cls;
                                m.name = maName;
                                m.mod = FieldAsModifier(field);
                                m.getter = new AttrGetter.MethodGetter(attributes, mget);
                                m.setter = null == mset ? new AttrSetter() {
                                    @Override
                                    public void set(Object attribute, Attributes.Attribute attr, Object value) {
                                        // TODO: Implement this method
                                        throw new UnsupportedOperationException("not found @GetterAndSetterField setter: " + maName + ", setterList=" + lowercaseNameSetMethods);
                                    }
                                }: (new AttrSetter.MethodSetter(attributes, mset));

                                result.put(maName, m);
                            } else {
                                if (field.getDeclaringClass() == cls) {
                                    throw new UnsupportedOperationException("repeat @GetterAndSetterField: " + Arrays.asList("parent=" + pa, "name=" + maName, field, "result=" + result));
                                } else {
                                    result.put(maName, pa);
                                }
                            }
                        }
                    }
                    FIELD_GETTERANDSETTERFIELD_LowerCaseNameMappingMaNameMap.clear();
                    FIELD_GETTERANDSETTERFIELD_MaNameFieldMap.clear();
                }


                METHOD: {
                    for (final String needSimpleName: annotationsSimpleNameGetter.keySet().toArray(Finals.EMPTY_STRING_ARRAY)) {
                        Method mget = annotationsSimpleNameGetter.remove(needSimpleName);
                        Collectionz.removeValueMappingKey(annotationsSimpleNameGetter, mget);

                        Method mset = annotationsSimpleNameSetter.remove(needSimpleName);
                        Collectionz.removeValueMappingKey(annotationsSimpleNameSetter, mset);


                        Attribute    pa;
                        if (null == (pa = findSupperAttribute(attributes, cls, needSimpleName)))
                            pa = result.get(needSimpleName);

                        if (null == pa) {
                            Attribute m = new Attribute();
                            m.isJavaField = true;
                            m.clasz = cls;
                            m.name = needSimpleName;
                            m.mod = GetterAndSetterAsModifier(mget, mset);
                            m.getter = new AttrGetter.MethodGetter(attributes, mget);
                            m.setter = null == mset ? new AttrSetter() {
                                @Override
                                public void set(Object attribute, Attributes.Attribute attr, Object value) {
                                    // TODO: Implement this method
                                    throw new UnsupportedOperationException("not found @Getter setter: " + Arrays.asList("name=" + needSimpleName, "setterList=" + annotationsSimpleNameSetter, "result=" + result));
                                }
                            }: (new AttrSetter.MethodSetter(attributes, mset));

                            result.put(needSimpleName, m);
                        } else {
                            if (mget.getDeclaringClass() == cls) {
                                throw new UnsupportedOperationException("repeat @Getter: " + Arrays.asList("parent=" + pa, "name=" + needSimpleName, mget, "result=" + result));
                            } else if ((null != mset) && mset.getDeclaringClass() == cls) {
                                throw new UnsupportedOperationException("repeat @Setter: " + Arrays.asList("parent=" + pa, "name=" + needSimpleName, mset, "result=" + result));
                            } else {
                                result.put(needSimpleName, pa);
                            }
                        }
                    }
                    if (annotationsSimpleNameSetter.size() != 0) {
                        throw new UnsupportedOperationException("only @Setter: " + Arrays.asList(annotationsSimpleNameSetter, "result=" + result));
                    }
                }

                cache = result;
                ATTRIBUTES_CLASS_ATTRIBUTE.put(cls, result);
            }
        }
        return cache;
    }


    static void setAccessibleField(Object attributes, AccessibleObject field) {
        if (attributes instanceof AttributesFieldAccessor) {
            ((AttributesFieldAccessor)attributes).setAccessibleObject(field);
        } else {
            Reflects.accessible(field);
        }
    }
    @Override
    public void setAccessibleObject(AccessibleObject field) {
        try {
            field.setAccessible(true);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    protected Attributes thatClone() {
        try {
            return (Attributes) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    private static final Class<Attributes>                  ATTRIBUTES_CLASS = Attributes.class;
    private static final Set<Class>                         ATTRIBUTES_SUPERCLASS = Reflects.getInherites(ATTRIBUTES_CLASS);
    private static final Map<Class, Map<String, Attribute>> ATTRIBUTES_CLASS_ATTRIBUTE = new HashMap<>();

    private Attributes     root   = this;
    private Object         invoke = this;
    private AttributeTable attributeTable;
    private boolean        unchecked;
    private Attributes     uncheckObject;

    //no sync
    private class AttributeTable {
        private Map<String, Attribute> attributeMap;

        private void initialize() {
            this.attributeMap = new LinkedHashMap<String, Attribute>(getAttributesClassBaseFieldAttribute(invoke, invoke.getClass()));
        }


        void add(String k, Attribute v) {
            attributeMap.put(k, v);
        }
        void remove(String k) {
            Attribute v = attributeMap.remove(k);
        }
        void updated(Attribute v) {
        }

        Attribute get(Object k) { return attributeMap.get(k); }
        int size()              { return attributeMap.size(); }
        Set<String> keySet()    { return attributeMap.keySet(); }



        int         checkedSize()   {
            int size = 0;
            for (String ignored : checkedKeySet())
                size++;
            return size;
        }

        Set<String> checkedKeySet;
        Set<String> checkedKeySet() {
            Set<String> ks = checkedKeySet;
            if (ks == null) {
                ks = new CheckedKeySet();
                checkedKeySet = ks;
            }
            return ks;
        }
        final class CheckedKeySet extends AbstractSet<String> {
            final AttributeTable map = AttributeTable.this;
            final Iterable<String> iterable = Iterators.filterIterable(map.keySet(), new IFilter<String>() {
                @Override
                public Boolean next(String p1) {
                    // TODO: Implement this method
                    Attribute m = map.get(p1);
                    return !m.inadmissibilityGettable();
                }
            });

            public final int size()                  { return Attributes.this.size(); }
            public final void clear()                { Attributes.this.clear(); }
            public final Iterator<String> iterator() {
                return iterable.iterator();
            }
            public final boolean contains(Object o)  { return Attributes.this.containsKey(o); }
            public final boolean remove(String key) {
                return Attributes.this.removeAttribute(key);
            }
        }

    }


    public Attributes() {}

    AttributeTable initialize() {
        AttributeTable cache = this.attributeTable;
        if (null != cache)
            return  cache;

        synchronized (root) {
            cache = this.attributeTable;
            if (null != cache)
                return  cache;

            cache = new AttributeTable();
            cache.initialize();

            return this.attributeTable = cache;
        }
    }
    public Attributes cloneAttributes() {
        Attributes object = root.thatClone();//clone checked
        object.root           = object;
        object.invoke         = object;
        object.unchecked      = false;
        object.uncheckObject  = null;
        object.attributeTable = null;
        object.entrySet       = null;

        object.uncheckedObject().setAttributes(uncheckedObject());
        return object;
    }





    protected Attributes uncheckedObject() {
        Attributes  object = this.uncheckObject;
        if (null != object)
            return  object;

        synchronized (root) {
            object = this.uncheckObject;
            if (null != object)
                return  object;
            object = root.thatClone();   //clone checked Object
//          object.root             = this.root;
//          object.invoke           = this.invoke;
            object.unchecked        = true; //set unchecked
            object.uncheckObject    = object;
            //object.attributeTable = this.attributeTable;
            object.entrySet         = null;

            this.uncheckObject = object;
            return object;
        }
    }

    protected boolean isUnchecked() {
        return this.unchecked;
    }

    protected Attribute getAttributeOrNewObject(String name) {
        Attribute  m = initialize().get(name);
        if (null != m)
            return  m;

        synchronized (root) {
            AttributeTable map = initialize();
            if ((m = map.get(name)) == null) {
                m = new Attribute();
                m.isJavaField = false;//动态变量
                m.clasz = invoke.getClass();//只是获取名称用 基本没什么用
                m.name = name;
                m.mod = 0;//没有修饰符
                m.getter = new AttrGetter.AttributeGetter();
                m.setter = new AttrSetter.AttributeSetter();

                map.add(name, m);
            }
            return m;
        }
    }
    protected boolean removeAttributeOrSetNull(String name) {
        Attribute   m = initialize().get(name);
        if (null != m) {
            if (m.isJavaField) {
                setAttribute(name, null);
                return true;
            } else {
                synchronized (root) {
                    AttributeTable map = initialize();
                    if ((m = map.get(name)) != null) {
                        if (m.hasRemovable()) {
                            map.remove(name);
                            return true;
                        } else {
                            throw new AccessFieldException("no removable: " + name);
                        }
                    }
                }
            }
        }
        return false;
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public boolean containsKey(Object str) {
        Attribute m = this.initialize().get(str);
        if (m == null) {
            return false;
        }
        if (!this.unchecked && m.inadmissibilityGettable()) {
            return false;
        }
        return true;
    }

    public Object getAttribute(String str) {
        Attribute m = this.initialize().get(str);
        if (m == null) {
            return null;
        }
        if (!this.unchecked && m.inadmissibilityGettable()) {
            return null;
        }
        return m.getter.get(invoke, m);
    }

    public void setAttribute(String str, Object obj) {
        Attribute m = getAttributeOrNewObject(str);
        if (!this.unchecked && m.inadmissibilitySettable()) {
            throw new AccessFieldException("unmodifiable: " + str);
        }
        m.setter.set(invoke, m, obj);
    }

    public boolean removeAttribute(String str) {
        return removeAttributeOrSetNull(str);
    }


    @Override
    public int size() {
        if (this.unchecked) {
            return initialize().size();
        }
        return initialize().checkedSize();
    }

    @Override
    public Set<String> keySet() {
        if (this.unchecked) {
            return initialize().keySet();
        }
        return initialize().checkedKeySet();
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Attributes)) return false;

        Attributes that = (Attributes) o;
        if (root == that.root) return true; //my unchecked object

        if (size() != that.size()) return false;

        for (String name : keySet())
            if (!Objects.equals(getAttribute(name), that.getAttribute(name))) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        for (String name : root.keySet())
            result += name.hashCode();
        return result;
    }

    public JSONObject toJson() {
        return new JSONObject((Map) this); //this.entrySet()
    }




    public void setAttributes(Attributes attrs) {
        for (String k: attrs.keySet()) {
            setAttribute(k, attrs.getAttribute(k));
        }
    }
    public void setAttributes(Map<String, Object> map) {
        for (String k: map.keySet()) {
            setAttribute(k, map.get(k));
        }
    }

    public String toString() {
        return deepToJson().toString();
    }

    public Map<String, Object> toMap() {
        Map<String, Object> kv = new LinkedHashMap<>();
        for (String k : keySet()) {
            kv.put(k, getAttribute(k));
        }
        return kv;
    }


//	static class Aa extends Attributes {
//		Aa() { super(); }
//
//		@MethodField
//		private int line;
//		public int getline() {
//			return line;
//		}
//		public void setline(int line) {
//			this.line = line + 1;
//		}
//
//
//		@Getter("Current")
//		public long time() { return System.currentTimeMillis(); }
//	}
//	static class A extends Aa {
//		A() { super(); }
//
//		@Public
//		private String v = "";
//
//		//@MethodMapping
//		//private int line;
//	}
//
//
//	public static void test(){
//		A a = new A();
//		a.setAttribute("line", 7);
//		System.out.println(a);

//	A a = new A();
//	a.setAttribute("a", 666);
//	new A().setAttribute("a", 777);
//	System.out.println(a);
//	}
//public static class Hello extends Attributes {
//    private int k = 8;        @Override
//    protected Attributes uncheckedObject() {
//        return super.uncheckedObject();
//    }
//}
//    public static class Hello2 extends Attributes {
//        private int k = 9;
//
//        @Override
//        protected Attributes uncheckedObject() {
//            return super.uncheckedObject();
//        }
//    }
//    Hello2 attributes = new Hello2();
//        attributes.uncheckedObject().put("obj", new Hello().uncheckedObject());
//        System.out.println(attributes.uncheckedObject().toJson());




    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsValue(Object value) {
        for (String s : keySet()) {
            Object v = getAttribute(s);
            if (Objects.equals(v, value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object get(Object key) {
        String k = null == key ? null : key.toString();
        return getAttribute(k);
    }

    @Override
    public Object remove(Object keyo) {
        String key = null == keyo ? null : keyo.toString();
        Object attribute = getAttribute(key);
        removeAttribute(key);
        return attribute;
    }

    @Override
    public void clear() {
        for (String s : keySet())
            removeAttribute(s);
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        setAttributes((Map<String, Object>) m);
    }

    @Override
    public Object put(String key, Object value) {
        Object attribute = getAttribute(key);
        setAttribute(key, value);
        return attribute;
    }

    @Override
    public Collection<Object> values() {
        List<Object> result = new ArrayList<>();
        for (String s : keySet()) {
            Object v = getAttribute(s);
            result.add(v);
        }
        return result;
    }

    transient Set<Map.Entry<String,Object>> entrySet;
    @Override
    public Set<Map.Entry<String,Object>> entrySet() {
        Set<Map.Entry<String,Object>> es;
        return (es = entrySet) == null ? (entrySet = new EntrySet()) : es;
    }
    final class EntrySet extends AbstractSet<Map.Entry<String,Object>> {
        public final int size()                 { return Attributes.this.size(); }
        public final void clear()               { Attributes.this.clear(); }
        public final Iterator<Map.Entry<String,Object>> iterator() {
            return new EntryIterator();
        }
        public final boolean contains(Object o) {
            return Attributes.this.containsKey(o);
        }
        public final boolean remove(Object o) {
            return null != Attributes.this.remove(o);
        }


        final class EntryIterator implements Iterator<Map.Entry<String,Object>> {
            Set<String> set;
            Iterator<String> iterator;
            EntryIterator() {
                set = keySet();
                iterator = set.iterator();
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            final class Element implements Entry<String, Object> {
                String next;
                Object value;

                public String getKey() {
                    return next;
                }
                public Object getValue() {
                    return value;
                }
                public Object setValue(Object value) {
                    return Attributes.this.put(next, value);
                }
            }
            final Element entry = new Element();

            public final Map.Entry<String,Object> next() {
                entry.next  = iterator.next();
                entry.value = Attributes.this.getAttribute(entry.next);
                return entry;
            }

            @Override
            public void remove() {
                iterator.remove();
            }
        }
    }
}


