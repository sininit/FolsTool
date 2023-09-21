package top.fols.box.reflect.re;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

import top.fols.atri.assist.json.*;
import top.fols.atri.interfaces.interfaces.IFilter;
import top.fols.atri.lang.Arrayz;
import top.fols.atri.lang.Value;
import top.fols.atri.reflect.ReflectCache;
import top.fols.atri.reflect.Reflects;
import top.fols.box.reflect.Reflectx;

/**
 * {对象} 和 它的抽象 {类}
 */
@SuppressWarnings({"rawtypes"})
public class Re_PrimitiveClass_json extends Re_PrimitiveClass {

    protected Re_PrimitiveClass_json(Re re) {
        this(re, Re_Keywords.INNER_CLASS__JSON);
    }
    protected Re_PrimitiveClass_json(Re re, String className) {
        super(re, className, new InitializedBefore() {
            @Override
            public void doExecute(Re_PrimitiveClass thatClass) {
                thatClass.addFunction(Reflectx.getCallLine(), Re_Modifiers.asString(Re_Modifiers.STRUCT),
                        new Re_PrimitiveClassMyCVF.IFunction("parse") {
                            @Override
                            public Object unauthenticationExecuteOnNative(Re_Executor executor, Re_Class runInClass, Re_ClassInstance runInInstance, Object[] arguments) throws Throwable {
                                int length = arguments.length;
                                if (length == 1) {
                                    String str = Re_Utilities.toJString(arguments[0]);
                                    return Re_PrimitiveClass_json.parse(executor, str);
                                }
                                executor.setThrow(Re_Accidents.unable_to_process_parameters(name, length));
                                return null;
                            }
                        });

                thatClass.addFunction(Reflectx.getCallLine(), Re_Modifiers.asString(Re_Modifiers.STRUCT),
                        new Re_PrimitiveClassMyCVF.IFunction("stringify") {
                            @Override
                            public Object unauthenticationExecuteOnNative(Re_Executor executor, Re_Class runInClass, Re_ClassInstance runInInstance, Object[] arguments) throws Throwable {
                                int length = arguments.length;
                                if (length == 1) {
                                    Object json = arguments[0];
                                    return Re_PrimitiveClass_json.stringify(executor, json);
                                }
                                executor.setThrow(Re_Accidents.unable_to_process_parameters(name , arguments.length));
                                return null;
                            }
                        });
            }
        });
    }


    @Override
    protected Re_PrimitiveClassInstance newUndefinedInstance(Re_Class reClass) {
        return new Re_PrimitiveClassInstance(reClass, Re.newPrimitiveJsonVariableMap());
    }





    public static Re_PrimitiveClass_list.Instance parseToReList(Re_Executor executor, JSONArray object) throws Throwable {
        if (null == object) return null;
        Object[] array = new Object[object.length()];
        for (int i = 0; i < object.length(); i++) {
            Object value = object.get(i);
            if (value instanceof JSONObject) {
                value = parseToReInstance(executor, (JSONObject) value);
                if (executor.isReturnOrThrow()) return null;
            } else if (value instanceof JSONArray) {
                value = parseToReList(executor, (JSONArray) value);
                if (executor.isReturnOrThrow()) return null;
            }
            array[i] = value;
        }

        Re_PrimitiveClass_list.Instance instance = Rez.SafesRe.createInstance_list(executor);
        if (executor.isReturnOrThrow()) return null;

        instance.setElements(executor, array);
        if (executor.isReturnOrThrow()) return null;
        return instance;
    }

    public static Re_ClassInstance parseToReInstance(Re_Executor executor, JSONObject object) throws Throwable {
        if (null == object) return null;

        Re_ClassInstance instance = Rez.SafesRe.createInstance_json(executor);
        if (executor.isReturnOrThrow()) return null;

        for (String s : object.keySet()) {
            Object value = object.get(s);
            if (value instanceof JSONObject) {
                value = parseToReInstance(executor, (JSONObject) value);
                if (executor.isReturnOrThrow()) return null;
            } else if (value instanceof JSONArray) {
                value = parseToReList(executor, (JSONArray) value);
                if (executor.isReturnOrThrow()) return null;
            }
            Re_Utilities.setattr(executor, instance, s, value);
            if (executor.isReturnOrThrow()) return null;
        }
        return instance;
    }

    public static Object parse(Re_Executor executor, String str) throws Throwable {
        if (null == str || str.length() == 0) {
            return null;
        }
        try {
            JSONTokener tokener = new JSONTokener(str);
            Object object = tokener.nextValue();

            if (object instanceof JSONObject) {
                return parseToReInstance(executor, (JSONObject) object);
            } else if (object instanceof JSONArray) {
                return parseToReList(executor, (JSONArray) object);
            }
        } catch (JSONException ignored) {}
        return null;
    }




    public static class StringifyCache {
        static abstract class Stringify {
            abstract void stringify(JSONStringer stringer, Re_Executor executor, Object value) throws Throwable;
        }

        public String stringify(Re_Executor executor, Object value) throws Throwable {
            JSONStringer stringer = new JSONStringer();
            try {
                Stringify   stringify = getStringify(executor, value);
                if (executor.isReturnOrThrow()) return null;

                if (null == stringify) {
                    return null;
                }

                stringify.stringify(stringer, executor, value);
                if (executor.isReturnOrThrow()) return null;

                return stringer.toString();
            } catch (JSONException e) {
                return null;
            }
        }

        Map<Class, Value<Stringify>> map = new WeakHashMap<>();
        Stringify getStringify(Re_Executor executor, Object value) throws Throwable {
            Class type = null == value ? null : value.getClass();
            Value<Stringify> stringify = map.get(type);
            if (null == stringify) {
                Stringify s = wrapType(executor, value);
                if (executor.isReturnOrThrow()) return null;

                map.put(type, stringify = new Value<>(s));
            }
            return stringify.get();
        }


        Stringify wrapType(final Re_Executor executor, Object o) throws Throwable {
            if (o instanceof Re_IRe_Object) {
                if (Re_Utilities.isReClass(o)) {
                    return wrapType_ReIObject();
                } else if (Re_Utilities.isReClassInstance(o)) {
                    if (Re_Utilities.isReClassInstance_list(o)) {
                        return new Stringify() {
                            @Override
                            void stringify(JSONStringer stringer, Re_Executor executor, Object value0) throws Throwable {
                                // TODO: Implement this method
                                stringer.array();
                                Re_PrimitiveClass_list.Instance list = (Re_PrimitiveClass_list.Instance) value0;
                                int size = Re_Variable.size(list);

                                for (int i = 0; i < size; i++) {
                                    Object value  = list.getElement(executor, i);
                                    if (executor.isReturnOrThrow()) return;

                                    Stringify   stringify = getStringify(executor, value);
                                    if (executor.isReturnOrThrow()) return ;
                                    if (null == stringify) {
                                        continue;
                                    } else {
                                        stringify.stringify(stringer, executor, value);
                                        if (executor.isReturnOrThrow()) return ;
                                    }
                                }
                                stringer.endArray();
                            }
                        };
                    } else {
                        return wrapType_ReIObject();
                    }
                } else if (Re_Utilities.isSpace(o)) {
                    return wrapType_ReIObject();
                } else {
                    Re_IRe_Object  re_object = (Re_IRe_Object) o;
                    if (re_object.hasObjectKeys()) {
                        return wrapType_ReIObject();
                    } else {
                        return null;
                    }
                }
            } else {
                if (null == o ||
                        o instanceof Boolean   ||
                        o instanceof Byte      ||
                        o instanceof Character ||
                        o instanceof Double    ||
                        o instanceof Float     ||
                        o instanceof Integer   ||
                        o instanceof Long      ||
                        o instanceof Short     ||
                        o instanceof String) {
                    return new Stringify() {
                        @Override
                        void stringify(JSONStringer stringer, Re_Executor executor, Object value0) throws Throwable {
                            // TODO: Implement this method
                            stringer.value(value0);
                        }
                    };
                }

                final Class oClass = o.getClass();
                if (oClass.isArray()) {
                    return new Stringify() {
                        @Override
                        void stringify(JSONStringer stringer, Re_Executor executor, Object value0) throws Throwable {
                            // TODO: Implement this method
                            stringer.array();
                            int size = Array.getLength(value0);
                            for (int i = 0; i < size; i++) {
                                Object value  = Array.get(value0, i);

                                Stringify   stringify = getStringify(executor, value);
                                if (executor.isReturnOrThrow()) return ;
                                if (null == stringify) {
                                    continue;
                                } else {
                                    stringify.stringify(stringer, executor, value);
                                    if (executor.isReturnOrThrow()) return ;
                                }
                            }
                            stringer.endArray();
                        }
                    };
                }
                if (o instanceof Iterable) {//Set Collection
                    return new Stringify() {
                        @Override
                        void stringify(JSONStringer stringer, Re_Executor executor, Object value0) throws Throwable {
                            // TODO: Implement this method
                            stringer.array();
                            Iterable able = (Iterable) value0;
                            Iterator iterator = able.iterator();
                            while (iterator.hasNext()) {
                                Object value  = iterator.next();

                                Stringify   stringify = getStringify(executor, value);
                                if (executor.isReturnOrThrow()) return ;
                                if (null == stringify) {
                                    continue;
                                } else {
                                    stringify.stringify(stringer, executor, value);
                                    if (executor.isReturnOrThrow()) return ;
                                }
                            }
                            stringer.endArray();
                        }
                    };
                }
                if (o instanceof Map) {
                    return new Stringify() {
                        @Override
                        void stringify(JSONStringer stringer, Re_Executor executor, Object value0) throws Throwable {
                            // TODO: Implement this method
                            stringer.object();
                            Map able = (Map) value0;

                            for (Object key: able.keySet()) {
                                Object value = able.get(key);
                                Stringify   stringify = getStringify(executor, value);
                                if (executor.isReturnOrThrow()) return ;
                                if (null == stringify) {
                                    continue;
                                } else {
                                    stringify.stringify(stringer, executor, value);
                                    if (executor.isReturnOrThrow()) return ;
                                }
                            }
                            stringer.endObject();
                        }
                    };
                }


                return new Stringify() {
                    final Re_IJavaReflector reflector = executor.re.reflector;
                    final ReflectCache cache = reflector.cacher();
                    final Field[] fields = Arrayz.filter(Reflects.accessible(cache.fields(oClass)), new IFilter<Field>(){
                        @Override
                        public boolean next(Field p1) {
                            // TODO: Implement this method
                            return !(
                                    Modifier.isStatic(p1.getModifiers()) ||
                                            Modifier.isTransient(p1.getModifiers())
                            );
                        }
                    });

                    @Override
                    void stringify(JSONStringer stringer, Re_Executor executor, Object value0) throws Throwable {
                        // TODO: Implement this method
                        stringer.object();
                        for (Field f: fields) {
                            Object v = f.get(value0);
                            Stringify   stringify = getStringify(executor, v);
                            if (executor.isReturnOrThrow()) return ;
                            if (null == stringify) {
                                continue;
                            } else {
                                stringify.stringify(stringer, executor, value0);
                                if (executor.isReturnOrThrow()) return ;
                            }
                        }
                        stringer.endObject();
                    }
                };
            }
        }

        Stringify wrapType_ReIObject() {
            return new Stringify() {
                @Override
                void stringify(JSONStringer stringer, Re_Executor executor, Object value0) throws Throwable {
                    // TODO: Implement this method
                    stringer.object();

                    Re_IRe_Object reObj = (Re_IRe_Object) value0;
                    Iterable value_key = reObj.getObjectKeys(executor);
                    if (executor.isReturnOrThrow()) return;

                    for (Object key0: value_key) {
                        String key = Re_Utilities.toJString(key0); // key is string

                        Object value = reObj.getObjectValue(executor, key0);
                        if (executor.isReturnOrThrow()) return;

                        Stringify   stringify = getStringify(executor, value);
                        if (executor.isReturnOrThrow()) return ;
                        if (null == stringify) {
                            continue;
                        } else {
                            stringer.key(key);
                            stringify.stringify(stringer, executor, value);
                            if (executor.isReturnOrThrow()) return ;
                        }
                    }
                    stringer.endObject();
                }
            };
        }
    }

    public static String stringify(Re_Executor executor, Object value) throws Throwable {
        StringifyCache sc = new StringifyCache();

        String stringify = sc.stringify(executor, value);
        if (executor.isReturnOrThrow()) return null;

        return stringify;

    }
}

