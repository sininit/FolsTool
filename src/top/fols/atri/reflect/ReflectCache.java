package top.fols.atri.reflect;

import top.fols.atri.cache.WeakCache;
import top.fols.atri.interfaces.interfaces.ICaller;
import top.fols.atri.interfaces.interfaces.IFilter;
import top.fols.atri.lang.Finals;
import top.fols.atri.lang.Objects;
import top.fols.box.lang.Classx;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@SuppressWarnings({"rawtypes", "unchecked"})
public class ReflectCache {
    public ReflectCache() {}


    public static class ClassesList implements Cloneable {
        private Class[] list0;

        public static ClassesList wrap(Class[] list) {
            ClassesList wrap = new ClassesList();
            wrap.list0 = list.clone();
            return wrap;
        }

        protected Class[] list() {
            return this.list0;
        }
        public Class[] listClone() {
            return this.list0.clone();
        }


        @Override
        public String toString() {
            // TODO Auto-generated method stub
            Class[] list = list();
            return null == list ? null : Arrays.toString(list);
        }

        @Override
        public ClassesList clone() {
            try {
                ClassesList newInstance = (ClassesList) super.clone();
                // TODO Auto-generated method stub
                newInstance.list0 = this.listClone();
                return newInstance;
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }

        public static ClassesList empty() {
            return ClassesList.wrap(Finals.EMPTY_CLASS_ARRAY);
        }
        public ClassesList filter(IFilter<Class> filter) {
            List<Class> list = new ArrayList<>();
            for (Class element : this.list0) {
                if (filter.next(element)) {
                    list.add(element);
                }
            }
            return ClassesList.wrap(list.toArray(Finals.EMPTY_CLASS_ARRAY));
        }
    }

    public static class FieldList implements Cloneable {
        private Field[] list0;

        public static FieldList wrap(Field[] list) {
            FieldList wrap = new FieldList();
            wrap.list0 = list.clone();
            return wrap;
        }

        protected Field[] list() {
            return this.list0;
        }
        public Field[] listClone() {
            return this.list0.clone();
        }

        @Override
        public String toString() {
            // TODO Auto-generated method stub
            Field[] list = list();
            return null == list ? null : Arrays.toString(list);
        }

        @Override
        public FieldList clone() {
            try {
                FieldList newInstance = (FieldList) super.clone();
                // TODO Auto-generated method stub
                newInstance.list0 = this.listClone();
                return newInstance;
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }


        public static FieldList empty() {
            return FieldList.wrap(Finals.EMPTY_FIELD_ARRAY);
        }
        public FieldList filter(IFilter<Field> filter) {
            List<Field> list = new ArrayList<>();
            for (Field element : this.list0) {
                if (filter.next(element)) {
                    list.add(element);
                }
            }
            return FieldList.wrap(list.toArray(Finals.EMPTY_FIELD_ARRAY));
        }
    }

    public static class ConstructorList implements Cloneable {
        private Constructor[] list0;
        private Class[][] parameterTypes0;

        public static ConstructorList wrap(Constructor[] list) {
            ConstructorList wrap = new ConstructorList();
            wrap.list0 = list.clone();
            wrap.parameterTypes0 = new Class[list.length][];
            for (int i = 0; i < list.length; i++) {
                wrap.parameterTypes0[i] = list[i].getParameterTypes();// already cloneExecutor
            }
            return wrap;
        }

        protected Constructor[] list() {
            return this.list0;
        }
        public Constructor[] listClone() {
            return this.list0.clone();
        }

        protected Class[]   parameterTypes(int index) {
            return this.parameterTypes0[index];
        }
        protected Class[][] parameterTypes() {
            return this.parameterTypes0;
        }
        public Class[][] parameterTypesClone() {
            Class[][] parameterTypes = this.parameterTypes();
            Class[][] clones = new Class[parameterTypes.length][];
            for (int i = 0; i < parameterTypes.length; i++) {
                clones[i] = parameterTypes[i].clone();
            }
            return clones;
        }

        @Override
        public String toString() {
            // TODO Auto-generated method stub
            Constructor[] list = list();
            return null == list ? null : Arrays.toString(list);
        }

        @Override
        public ConstructorList clone() {
            try {
                ConstructorList newInstance = (ConstructorList) super.clone();
                // TODO Auto-generated method stub
                newInstance.list0 = this.listClone();
                newInstance.parameterTypes0 = this.parameterTypesClone();
                return newInstance;
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }


        public static ConstructorList empty() {
            return ConstructorList.wrap(Finals.EMPTY_CONSTRUCTOR_ARRAY);
        }
        public ConstructorList filter(IFilter<Constructor> filter) {
            List<Constructor> list = new ArrayList<>();
            for (Constructor element : this.list0) {
                if (filter.next(element)) {
                    list.add(element);
                }
            }
            return ConstructorList.wrap(list.toArray(Finals.EMPTY_CONSTRUCTOR_ARRAY));
        }
    }

    public static class MethodList implements Cloneable {
        private Method[] list0;
        private Class[][] parameterTypes0;

        public static MethodList wrap(Method[] list) {
            MethodList wrap = new MethodList();
            wrap.list0 = list.clone();
            wrap.parameterTypes0 = new Class[list.length][];
            for (int i = 0; i < list.length; i++) {
                wrap.parameterTypes0[i] = list[i].getParameterTypes();// already cloneExecutor
            }
            return wrap;
        }

        protected Method[] list() {
            return this.list0;
        }
        public Method[] listClone() {
            return this.list0.clone();
        }

        protected Class[] parameterTypes(int index) {
            return this.parameterTypes0[index];
        }
        protected Class[][] parameterTypes() {
            return this.parameterTypes0;
        }

        public Class[][] parameterTypesClone() {
            Class[][] parameterTypes = parameterTypes();
            Class[][] clones = new Class[parameterTypes.length][];
            for (int i = 0; i < parameterTypes.length; i++) {
                clones[i] = parameterTypes[i].clone();
            }
            return clones;
        }

        @Override
        public String toString() {
            // TODO Auto-generated method stub
            Method[] list = list();
            return null == list ? null : Arrays.toString(list);
        }

        @Override
        public MethodList clone() {
            try {
                MethodList newInstance = (MethodList) super.clone();
                newInstance.list0 = this.listClone();
                newInstance.parameterTypes0 = this.parameterTypesClone();
                return newInstance;
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }

        public static MethodList empty() {
            return MethodList.wrap(Finals.EMPTY_METHOD_ARRAY);
        }
        public MethodList filter(IFilter<Method> filter) {
            List<Method> list = new ArrayList<>();
            for (Method element : this.list0) {
                if (filter.next(element)) {
                    list.add(element);
                }
            }
            return MethodList.wrap(list.toArray(Finals.EMPTY_METHOD_ARRAY));
        }
    }



    /**
     * All tip updates are obtained here
     */
    protected String getMemberName0(Member member) {
        return member.getName();
    }

    /**
     * All tip updates are obtained here
     */
    protected String getSimpleName0(Class<?> name) {
        return Classx.findSimpleName(name);
    }















    //all map variable suffix is Map



    /*
     * Update cache if not present
     */
    private WeakCache<Map<Class, ClassesList>, RuntimeException> classesListMap;

    protected ClassesList getClassesList(Class cls) {
        if (null == cls) {
            return null;
        }
        Map<Class, ClassesList> cacheMap = this.classesListMap.getOrCreateCache();
        ClassesList object;
        if (null == (object = cacheMap.get(cls))) {
//            this.putCacheBefore();
            cacheMap.put(cls, object = this.createClassesList(cls));
        }
        return object;
    }



    private WeakCache<Map<Class, Map<String, Class>>, RuntimeException> classesNameListMap;

    protected Class getClasses(Class cls, String name) {
        if (null == cls || null == name) {
            return null;
        }
        Map<Class, Map<String, Class>> cacheMap = this.classesNameListMap.getOrCreateCache();
        Map<String, Class> listMap;
        if (null == (listMap = cacheMap.get(cls))) {
//            this.putCacheBefore();
            return this.update(cacheMap, cls, this.createClassesList(cls)).get(name);
        }
        return listMap.get(name);
    }
    private Map<String, Class> update(Map<Class, Map<String, Class>> cacheMap, Class cls, ClassesList values) {
        Map<String, Class> tempMap = new LinkedHashMap<>();
        for (Class value : values.list()) {
            String fn = getSimpleName0(value);
            if (tempMap.containsKey(fn))
                continue;
            tempMap.put(fn, value);
        }
        cacheMap.put(cls, tempMap);
        return tempMap;
    }






    private WeakCache<Map<Class, ConstructorList>, RuntimeException> constructorListMap;

    protected ConstructorList getConstructorList(Class cls) {
        if (null == cls) {
            return null;
        }
        Map<Class, ConstructorList> cacheMap = this.constructorListMap.getOrCreateCache();
        ConstructorList object;
        if (null == (object = cacheMap.get(cls))) {
//            this.putCacheBefore();
            cacheMap.put(cls, object = this.createConstructorList(cls));
        }
        return object;
    }







    private WeakCache<Map<Class, FieldList>, RuntimeException> fieldsListMap;

    protected FieldList getFieldList(Class cls) {
        if (null == cls) {
            return null;
        }
        Map<Class, FieldList> cacheMap = this.fieldsListMap.getOrCreateCache();
        FieldList object;
        if (null == (object = cacheMap.get(cls))) {
//            this.putCacheBefore();
            cacheMap.put(cls, object = this.createFieldList(cls));
        }
        return object;
    }






    private WeakCache<Map<Class, Map<String, FieldList>>, RuntimeException> fieldsNameListMap;
    protected FieldList getFieldList(Class cls, String name) {
        if (null == cls || null == name) {
            return null;
        }
        Map<Class, Map<String, FieldList>> cacheMap = this.fieldsNameListMap.getOrCreateCache();
        Map<String, FieldList> listMap;
        if (null == (listMap = cacheMap.get(cls))) {
//            this.putCacheBefore();
            return this.update(cacheMap, cls, this.createFieldList(cls)).get(name);
        }
        return listMap.get(name);
    }
    private Map<String, FieldList> update(Map<Class, Map<String, FieldList>> cacheMap, Class cls, FieldList values) {
        Map<String, List<Field>> tempMap = new LinkedHashMap<>();
        for (Field value : values.list()) {
            String fn = getMemberName0(value);
            List<Field> tempList = tempMap.get(fn);
            if (null == tempList) {
                tempMap.put(fn, tempList = new ArrayList<>());
            }
            tempList.add(value);
        }
        Map<String, FieldList> temp2Map = new HashMap<>();
        for (String n : tempMap.keySet()) {
            List<Field> tempList = tempMap.get(n);
            temp2Map.put(n, FieldList.wrap(tempList.toArray(Finals.EMPTY_FIELD_ARRAY)));
        }
        cacheMap.put(cls, temp2Map);
        return temp2Map;
    }





    private WeakCache<Map<Class, MethodList>, RuntimeException> methodsListMap;
    protected MethodList getMethodList(Class cls) {
        if (null == cls) {
            return null;
        }
        Map<Class, MethodList> cacheMap = this.methodsListMap.getOrCreateCache();
        MethodList object;
        if (null == (object = cacheMap.get(cls))) {
//            this.putCacheBefore();
            cacheMap.put(cls, object = this.createMethodList(cls));
        }
        return object;
    }


    private WeakCache<Map<Class, Map<String, MethodList>>, RuntimeException> methodsNameListMap;
    protected MethodList getMethodList(Class cls, String name) {
        if (null == cls || null == name) {
            return null;
        }
        Map<Class, Map<String, MethodList>> cacheMap = this.methodsNameListMap.getOrCreateCache();
        Map<String, MethodList> listMap;
        if (null == (listMap = cacheMap.get(cls))) {
//            this.putCacheBefore();
            return this.update(cacheMap, cls, this.createMethodList(cls)).get(name);
        }
        return listMap.get(name);
    }
    private Map<String, MethodList> update(Map<Class, Map<String, MethodList>> cacheMap, Class cls, MethodList values) {
        Map<String, List<Method>> tempMap = new LinkedHashMap<>();
        for (Method value : values.list()) {
            String fn = getMemberName0(value);
            List<Method> tempList = tempMap.get(fn);
            if (null == tempList) {
                tempMap.put(fn, tempList = new ArrayList<>());
            }
            tempList.add(value);
        }
        Map<String, MethodList> temp2Map = new HashMap<>();
        for (String n : tempMap.keySet()) {
            List<Method> tempList = tempMap.get(n);
            temp2Map.put(n, MethodList.wrap(tempList.toArray(Finals.EMPTY_METHOD_ARRAY)));
        }
        cacheMap.put(cls, temp2Map);
        return temp2Map;
    }









    /**
     * All tip updates are obtained here
     */
    protected ClassesList createClassesList(Class cls) {
        return ClassesList.wrap(Reflects.classes(cls));
    }
    /**
     * All tip updates are obtained here
     */
    protected ConstructorList createConstructorList(Class cls) {
        return ConstructorList.wrap(Reflects.accessible(Reflects.constructors(cls)));
    }
    /**
     * All tip updates are obtained here
     */
    protected FieldList createFieldList(Class cls) {
        return FieldList.wrap(Reflects.accessible(Reflects.fields(cls)));
    }
    /**
     * All tip updates are obtained here
     */
    protected MethodList createMethodList(Class cls) {
        return MethodList.wrap(Reflects.accessible(Reflects.methods(cls)));
    }







    public Class[] classes(Class cls) {
        ClassesList list = this.getClassesList(cls);
        if (null == list) {
            return null;
        }
        return list.listClone();
    }
    public Class classes(Class cls, String simpleName) {
        return this.getClasses(cls, simpleName);
    }


    /**
     * Update cache if not present
     *
     * @return cache
     */
    public Field field(Class cls, String name) {
        return this.field(cls, null, name);
    }

    public Field field(Class cls, Class returnClass, String name) {
        FieldList   list = this.getFieldList(cls, name);
        if (null == list) {
            return null;
        }
        Field[] fields = list.list();
        if (null == returnClass) {
            return fields.length == 0 ? null : fields[0];
        } else {
            for (Field field : fields) {
                if (returnClass == field.getType()) {
                    return field;
                }
            }
        }
        return null;
    }

    public Field[] fields(Class cls) {
        FieldList list = this.getFieldList(cls);
        return null == list ? null : list.listClone();
    }


    /**
     * Update cache if not present
     *
     * @return cache
     */
    public Constructor constructor(Class cls, Class... parameterTypes) {
        if (null == parameterTypes) {
            return null;
        }
        ConstructorList list = this.getConstructorList(cls);
        if (null == list) {
            return null;
        }
        Constructor[] constructors = list.list();
        int length =  constructors.length;
        for (int i = 0; i < length; i++) {
            if (Objects.identityEquals(list.parameterTypes(i), parameterTypes)) {
                return constructors[i];
            }
        }
        return null;
    }

    public Constructor[] constructors(Class cls) {
        ConstructorList list = this.getConstructorList(cls);
        return null == list ? null : list.listClone();
    }


    /**
     * Update cache if not present
     *
     * @return cache
     */
    public Method method(Class cls, String name, Class... parameterTypes) {
        return this.method(cls, null, name, parameterTypes);
    }

    public Method method(Class cls, Class returnClass, String name, Class... parameterTypes) {
        if (null == parameterTypes) {
            return null;
        }
        MethodList  list = this.getMethodList(cls, name);
        if (null == list) {
            return null;
        }
        Method[] methods = list.list();
        int length = methods.length;
        if (null == returnClass) {
            for (int i = 0; i < length; i++) {
                if (Objects.identityEquals(list.parameterTypes(i), parameterTypes)) {
                    return methods[i];
                }
            }
        } else {
            for (int i = 0; i < length; i++) {
                if (returnClass == methods[i].getReturnType() &&
                    Objects.identityEquals(list.parameterTypes(i), parameterTypes)) {
                    return methods[i];
                }
            }
        }
        return null;
    }

    public Method[] methods(Class cls) {
        MethodList list = this.getMethodList(cls);
        return null == list ? null : list.listClone();
    }

    public Method[] methods(Class cls, String name) {
        MethodList list = this.getMethodList(cls, name);
        return null == list ? null : list.listClone();
    }









    public ReflectCache release(Class<?> cls) {
        for (WeakCache<Map<Class, ?>, RuntimeException> cacheMap: innerMaps)
            cacheMap.getOrCreateCache().remove(cls);
        return this;
    }

    public ReflectCache release(ClassLoader classLoader) {
        for (WeakCache<Map<Class, ?>, RuntimeException> cacheMap: innerMaps) {
            Map<Class, ?> orCreateCache = cacheMap.getOrCreateCache();
            for (Class aClass : orCreateCache.keySet()) {
                if (null != aClass)
                    if (aClass.getClassLoader() == classLoader)
                        orCreateCache.remove(aClass);
            }
        }
        return this;
    }






    WeakCache<Map<Class, ?>, RuntimeException> createStoreMap() { return new WeakCache<Map<Class, ?>, RuntimeException>() {
        @Override
        public Map createCache() {
            return new ConcurrentHashMap();
        }
    }; }



    public boolean isDefault() { return this == DEFAULT; }
    public static final ReflectCache DEFAULT = new ReflectCache();


    private WeakCache[] initTable() {
        return new WeakCache[] {
                this.classesListMap     = (WeakCache) createStoreMap(),
                this.classesNameListMap = (WeakCache) createStoreMap(),

                this.constructorListMap = (WeakCache) createStoreMap(),

                this.fieldsListMap      = (WeakCache) createStoreMap(),
                this.fieldsNameListMap  = (WeakCache) createStoreMap(),

                this.methodsListMap     = (WeakCache) createStoreMap(),
                this.methodsNameListMap = (WeakCache) createStoreMap()
        };
    }

    private WeakCache<Map<Class, ?>, RuntimeException>[] innerMaps = initTable();

    public void release() {
        for (WeakCache<Map<Class, ?>, RuntimeException> cacheMap: innerMaps) {
            cacheMap.release();
        }
        innerMaps = initTable();
    }
}
