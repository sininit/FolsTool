package top.fols.atri.lang;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

import top.fols.atri.array.ArrayObject;
import top.fols.atri.reflect.ReflectCache;
import top.fols.atri.reflect.Reflects;

@SuppressWarnings("rawtypes")
public abstract class Iterables<K, V> implements Iterable {
    private Iterables() {
    }



    @Override
    public abstract Iterator<K> iterator();

    public abstract int  length();

    public abstract V    get(K key);
    public abstract void set(K key, V value);






    public static Iterables wrap(final Object object) {
        if (null == object) { return new Iterables() {
            @Override
            public Iterator iterator() {
                // TODO: Implement this method
                return Finals.EMPTY_ITERATOR;
            }
            @Override
            public int length() {
                // TODO: Implement this method
                return 0;
            }
            @Override
            public Object get(Object key) {
                // TODO: Implement this method
                return null;
            }
            @Override
            public void set(Object key, Object value) {
                // TODO: Implement this method
            }
        }; }

        final Class<?> type = object.getClass();
        if (object instanceof Class)        {
            return new Iterables() {
                final Map<String, Field> names = new HashMap<String, Field>() {{
                    Field[] fields = fields((Class)object);
                    for (Field field: fields) {
                        String name = field.getName();
                        if (!containsKey(name))
                            put(name, field);
                    }
                }};
                final Iterator iterator = new WrapIterator(names.keySet().iterator());

                @Override
                public Iterator iterator() {
                    // TODO: Implement this method
                    return iterator;
                }

                @Override
                public int length() {
                    // TODO: Implement this method
                    return names.size();
                }

                @Override
                public Object get(Object key) {
                    // TODO: Implement this method
                    if (key instanceof String) {
                        Field  field = names.get(key);
                        if (null == field) return null;
                        try {
                            return field.get(object);
                        } catch (Throwable e) {
                            return null;
                        }
                    }
                    return null;
                }

                @Override
                public void set(Object key, Object value) {
                    // TODO: Implement this method
                    if (key instanceof Integer) {
                        Field  field = names.get(key);
                        if (null == field) return;
                        try {
                            field.set(object, value);
                        } catch (Throwable e) {
                            return;
                        }
                    }
                }
            };
        }
        if (object instanceof List)   {
            return new Iterables() {
                final List collection = (List) object;
                final Iterator iterator = new IndexIterator(collection.size());

                @Override
                public Iterator iterator() {
                    // TODO: Implement this method
                    return iterator;
                }

                @Override
                public int length() {
                    // TODO: Implement this method
                    return collection.size();
                }

                @Override
                public Object get(Object key) {
                    // TODO: Implement this method
                    if (key instanceof Integer) {
                        return collection.get(((Integer)key).intValue());
                    }
                    return null;
                }

                @Override
                public void set(Object key, Object value) {
                    // TODO: Implement this method
                    if (key instanceof Integer) {
                        collection.set(((Integer)key).intValue(), value);
                    }
                }
            };
        }
        if (object instanceof Collection)   {
            throw new UnsupportedOperationException(object.getClass().getName());
        }
        if (object instanceof Map)          {
            return new Iterables() {
                final Map collection = (Map) object;
                final Iterator iterator = new WrapIterator(collection.keySet().iterator());

                @Override
                public Iterator iterator() {
                    // TODO: Implement this method
                    return iterator;
                }

                @Override
                public int length() {
                    // TODO: Implement this method
                    return collection.size();
                }

                @Override
                public Object get(Object key) {
                    // TODO: Implement this method
                    return collection.get(key);
                }

                @Override
                public void set(Object key, Object value) {
                    // TODO: Implement this method
                    collection.put(key, value);
                }
            };
        }

        if (type.isArray()) {
            return new Iterables() {
                final ArrayObject collection = ArrayObject.wrap(object);
                final Iterator iterator = new IndexIterator(collection.length());

                @Override
                public Iterator iterator() {
                    // TODO: Implement this method
                    return iterator;
                }

                @Override
                public int length() {
                    // TODO: Implement this method
                    return collection.length();
                }

                @Override
                public Object get(Object key) {
                    // TODO: Implement this method
                    if (key instanceof Integer) {
                        return collection.objectValue(((Integer)key).intValue());
                    }
                    return null;
                }

                @Override
                public void set(Object key, Object value) {
                    // TODO: Implement this method
                    if (key instanceof Integer) {
                        collection.set(((Integer)key).intValue(), value);
                    }
                }
            };
        }

//		if (object instanceof CharSequence) { return ((CharSequence)object).length(); }

        return new Iterables() {
            final Map<String, Field> names = new HashMap<String, Field>(){{
                Field[] fields = fields(type);
                for (Field field: fields) {
                    field = Reflects.accessible(field);

                    String name = field.getName();
                    if (!containsKey(name))
                        put(name, field);
                }
            }};
            final Iterator iterator = new WrapIterator(names.keySet().iterator());

            @Override
            public Iterator iterator() {
                // TODO: Implement this method
                return iterator;
            }

            @Override
            public int length() {
                // TODO: Implement this method
                return names.size();
            }

            @Override
            public Object get(Object key) {
                // TODO: Implement this method
                if (key instanceof String) {
                    Field  field = names.get(key);
                    if (null == field) return null;
                    try {
                        return field.get(object);
                    } catch (Throwable e) {
                        return null;
                    }
                }
                return null;
            }

            @Override
            public void set(Object key, Object value) {
                // TODO: Implement this method
                if (key instanceof Integer) {
                    Field  field = names.get(key);
                    if (null == field) return;
                    try {
                        field.set(object, value);
                    } catch (Throwable e) {
                    }
                }
            }
        };
    }


    public static <V> Iterables<Integer, V> wrap(Collection<V> object) { return wrap((Object) object); }
    public static <K, V> Iterables<K, V>    wrap(Map<K, V> object)     { return wrap((Object) object); }
    public static <V> Iterables<Integer, V> wrap(V[] object)           { return wrap((Object) object); }



    static Field[] fields(Class object) {
        if (null == object) {
            return Finals.EMPTY_FIELD_ARRAY;
        } else {
            return ReflectCache.DEFAULT_INSTANCE.fields(object);
        }
    }
    public static int       fieldCount(Class object) {
        if (null == object) { return 0; }
        return fields(object).length;
    }
    public static int       fieldCount(Object object) {
        if (null == object) { return 0; }

        if (object instanceof Class)        { return fieldCount((Class)object); }
        if (object instanceof Collection)   { return ((Collection)object).size(); }
        if (object instanceof Map)          { return ((Map)object).size(); }

        Class<?> type = object.getClass();
        if (type.isArray()) { return Array.getLength(object); }

//		if (object instanceof CharSequence) { return ((CharSequence)object).length(); }

        return fieldCount(type);
    }
    public static Iterable  fieldIterable(Object object) {
        if (null == object) return Finals.EMPTY_LIST;

        final Class type = object.getClass();
        if (object instanceof Class)        { return new IterableWrap(new IndexIterator(fieldCount(type))); }
        if (object instanceof Collection)   { return new IterableWrap(new IndexIterator(((Collection)object).size())); }
        if (object instanceof Map)          { return new IterableWrap(new WrapIterator(((Map)object).keySet().iterator())); }

        if (type.isArray()) { return new IterableWrap(new IndexIterator(Array.getLength(object))); }
//		if (object instanceof CharSequence) { return new IterableWrap(new IndexIterator(((CharSequence)object).length())); }

        return new IterableWrap(new IndexIterator(fieldCount(type)));
    }

    public static class IndexIterator implements Iterator {

        int length;
        int index = -1;

        public IndexIterator(int length) {
            this.length = length;
        }

        @Override
        public boolean hasNext() {
            // TODO: Implement this method
            return !(index + 1 >= length);
        }
        @Override
        public Object next() {
            // TODO: Implement this method
            if (index + 1 >= length) {
                return null;
            }
            return ++index;
        }
        @Override
        public void remove() {
            // TODO: Implement this method
            throw new UnsupportedOperationException("index type");
        }
    }
    public static class WrapIterator  implements Iterator {
        Iterator iterator;
        public WrapIterator(Iterator iterator) {
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            // TODO: Implement this method
            return iterator.hasNext();
        }
        @Override
        public Object next() {
            // TODO: Implement this method
            return iterator.next();
        }
        @Override
        public void remove() {
            // TODO: Implement this method
            throw new UnsupportedOperationException("index type");
        }
    }
    public static class IterableWrap  implements Iterable {
        Iterator iterator;
        public IterableWrap(Iterator iterator) {
            this.iterator = iterator;
        }

        @Override
        public Iterator iterator() {
            // TODO: Implement this method
            return this.iterator;
        }
    }
}

