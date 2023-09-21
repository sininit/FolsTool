package top.fols.box.reflect.re;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;


@SuppressWarnings("rawtypes")
class Re_VariableMapAsObject implements Re_IRe_VariableMap {
    private ConcurrentHashMap<Object, Re_Variable> map = new ConcurrentHashMap<>();
    private Collection<?> keySetCache;

    @Override
    public Re_IRe_VariableMap _variable_clone_all() {
        Re_VariableMapAsObject instance = new Re_VariableMapAsObject();
        instance.map = (ConcurrentHashMap<Object, Re_Variable>) Re_Variable.Unsafes.cloneToMap(this.map, new ConcurrentHashMap<Object, Re_Variable>());
        instance.keySetCache = null;
        return instance;
    }


    static final class NULL_KEY {
        NULL_KEY() {}

        @Override
        public int hashCode() {
            return 0;
        }

        @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
        @Override
        public boolean equals(Object obj) {
            if (null == obj) {
                return true;
            }
            return this == obj;
        }

        @SuppressWarnings("MethodDoesntCallSuperMethod")
        @Override
        public Object clone() {
            return this;
        }

        @Override
        public String toString() {
            return String.valueOf((Object) null);
        }
    }
    static final Object NULL_KEY = new NULL_KEY();

    @Override
    public Re_Variable _variable_remove(Object key) {
        return map.remove(null == key ? NULL_KEY : key);
    }

    @Override
    public Re_Variable _variable_find_table_or_parent(Object key) {
        return map.get(null == key ? NULL_KEY : key);
    }

    @Override
    public Re_Variable _variable_find_table_var(Object key) {
        return map.get(null == key ? NULL_KEY : key);
    }

    @Override
    public Re_Variable _variable_get(Object key) {
        return map.get(null == key ? NULL_KEY : key);
    }

    @Override
    public Re_Variable _variable_put(Object key, Re_Variable value) {
        return map.put(null == key ? NULL_KEY : key, value);
    }

    @Override
    public boolean _variable_has(Object key) {
        return map.containsKey(null == key ? NULL_KEY : key);
    }

    @Override
    public int _variable_key_count() {
        return map.size();
    }


    @Override
    public Collection<?> _variable_keys() {
        return null == keySetCache ? keySetCache = new ReplaceCollection(this) : keySetCache;
    }


    @Override
    public int hashCode() {
        return map.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Re_VariableMapAsObject) {
            return map.equals(((Re_VariableMapAsObject) obj).map);
        }
        return map.equals(obj);
    }

    @Override
    public String toString() {
        return map.toString();
    }


    @SuppressWarnings("rawtypes")
    public static class ReplaceCollection implements Collection {
        final Re_VariableMapAsObject parent;
        final Collection<Object> objectCollection;
        public ReplaceCollection(Re_VariableMapAsObject parent) {
            this.parent = parent;
            this.objectCollection = parent.map.keySet();
        }


        @Override
        public int size() {
            return objectCollection.size();
        }

        @Override
        public boolean isEmpty() {
            return objectCollection.isEmpty();
        }

        @Override
        public void clear() {
            objectCollection.clear();
        }

        @Override
        public int hashCode() {
            return objectCollection.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof ReplaceCollection) {
                return objectCollection.equals(((ReplaceCollection) obj).objectCollection);
            }
            return objectCollection.equals(obj);
        }

        @Override
        public String toString() {
            return objectCollection.toString();
        }

        @Override
        public Iterator<?> iterator() {
            return wrapIterator(objectCollection.iterator());
        }

        @Override
        public boolean add(Object key) {
            return objectCollection.add(null == key ? NULL_KEY : key);
        }
        @Override
        public boolean remove(Object key) {
            return objectCollection.remove(null == key ? NULL_KEY : key);
        }
        @Override
        public boolean contains(Object key) {
            return objectCollection.contains(null == key ? NULL_KEY : key);
        }



        @Override
        public boolean containsAll(Collection c) {
            if (c != this) {
                for (Object e : c) {
                    if (!contains(e))
                        return false;
                }
            }
            return true;
        }
        @Override
        public boolean addAll(Collection c) {
            if (c == null) return false;
            for (Object e : c) {
                add(e);
            }
            return true;
        }

        @Override
        public boolean retainAll(Collection c) {
            if (c == null) return false;
            boolean modified = false;
            for (Iterator it = iterator(); it.hasNext();) {
                Object next = it.next();
                if (!c.contains(NULL_KEY == next ? null : next)) {
                    it.remove();
                    modified = true;
                }
            }
            return modified;
        }
        @Override
        public boolean removeAll(Collection c) {
            if (c == null) return true;
            boolean modified = false;
            for (Iterator this_it = iterator(); this_it.hasNext();) {
                Object this_next = this_it.next();
                if (c.contains(NULL_KEY == this_next ? null : this_next)) {
                    this_it.remove();
                    modified = true;
                }
            }
            return modified;
        }




        @Override
        public Object[] toArray() {
            Object[] objects = objectCollection.toArray();
            for (int i = 0; i < objects.length; i++) {
                if (objects[i] == NULL_KEY) {
                    objects[i] = null;
                }
            }
            return objects;
        }

        //... 入侵度较高
        @Override
        public Object[] toArray(Object[] a) {
            Object[] objects = objectCollection.toArray(a);
            for (int i = 0; i < objects.length; i++) {
                if (objects[i] == NULL_KEY) {
                    objects[i] = null;
                }
            }
            return objects;
        }
    }
    /**
     * 因为替换了null key 所以需要对Set进行过滤
     */
    public static <T> Iterator<T> wrapIterator(final Iterator<T> iterator) {
        if (null == iterator) {
            return null;
        }

        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                // TODO: Implement this method
                return iterator.hasNext();
            }

            @Override
            public T next() {
                // TODO: Implement this method
                T o = iterator.next();
                return NULL_KEY == o ? null : o;
            }

            @Override
            public void remove() {
                // TODO: Implement this method
                iterator.remove();
            }
        };
    }
}
