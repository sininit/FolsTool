package top.fols.box.reflect.re;

import java.util.*;

/**
 * 关键词应该是可见的
 */
// no sync lock
@SuppressWarnings({"unchecked", "rawtypes", "SynchronizeOnNonFinalField"})
class Re_VariableMapAsKeyword implements Re_IRe_VariableMap {
    private Object lock = new Object();
    private HashMap<Object, Re_Variable> map = new HashMap<>();
    private IdentityHashMap<Object, Object> valueMappingKey = new IdentityHashMap<>();  //可能存在重复的value，所以用IdentityHashMap
    private Collection<?> keySetCache;

    @SuppressWarnings("unchecked")
    @Override
    public Re_IRe_VariableMap _variable_clone_all() {
        Re_VariableMapAsKeyword instance = new Re_VariableMapAsKeyword();
        instance.lock = new Object();
        instance.map = (HashMap<Object, Re_Variable>) Re_Variable.Unsafes.cloneToMap(this.map, new HashMap<Object, Re_Variable>());
        instance.valueMappingKey = (IdentityHashMap<Object, Object>) valueMappingKey.clone();
        instance.keySetCache = null;
        return instance;
    }


    public boolean isKeywordKey(Object value) {
        return map.containsKey(value);
    }
    public boolean isKeywordValue(Object value) {
        return valueMappingKey.containsKey(value);
    }
    public Object getKeywordKey(Object value) {
        return valueMappingKey.get(value);
    }
    public Object findKeyword(Object key) {
        try {
            return Re_Variable.Unsafes.fromUnsafeAccessorGetValueOrThrowEx(key, this);
        } catch (Throwable ignored) {}
        return null;
    }
    public Object findRuntimeKeyword(Re_Executor executor, Object key) {
        return Re_Variable.accessGetValue(executor, key, this);
    }
    public boolean isRuntimeKeyword(Object key) {
        return Re_Variable.Unsafes.isDynamicVariable(key, this);
    }

    public String[] getKeywordNames() {
        Set <Object> x = new LinkedHashSet<>();
        for (Object o : Re_Variable.key(this)) {
            x.add(o);
        }

        String[] keys = new String[x.size()];
        int i = 0;
        for (Object o: x) {
            keys[i++] = (String) o;
        }
        return keys;
    }






    @Override
    public Re_Variable _variable_remove(Object key) {
        synchronized (lock) {
            Re_Variable<?> remove = map.remove(key);
            if (null != remove) {
                Object value = null;
                try {
                    value = Re_Variable.Unsafes.fromUnsafeAccessorGetValueOrThrowEx(remove);
                } catch (Throwable ignored) {}
                valueMappingKey.remove(value);
            }
            return remove;
        }
    }

    @Override
    public Re_Variable _variable_find_table_or_parent(Object key) {
        synchronized (lock) {
            return map.get(key);
        }
    }


    /**
     * 原始获取
     * 理论上你必须使用 {@link Re_Variable} 而不是自己操作， 这些方法主要 {@link Re_Variable} 调用
     *
     */
    @Override
    public Re_Variable _variable_find_table_var(Object key) {
        synchronized (lock) {
            return map.get(key);
        }
    }

    @Override
    public Re_Variable _variable_get(Object key) {
        synchronized (lock) {
            return map.get(key);
        }
    }

    @Override
    public Re_Variable _variable_put(Object key, Re_Variable variable) {
        if (key instanceof String) {
            synchronized (lock) {
                if (null != variable) {
                    Object value = null;
                    try {
                        value = Re_Variable.Unsafes.fromUnsafeAccessorGetValueOrThrowEx(variable);
                    } catch (Throwable ignored) {}
                    valueMappingKey.put(value, key);
                }
                return map.put(key, variable);
            }
        }
        throw new IllegalArgumentException("key must be String");
    }

    @Override
    public boolean _variable_has(Object key) {
        synchronized (lock) {
            return map.containsKey(key);
        }
    }

    @Override
    public int _variable_key_count() {
        synchronized (lock) {
            return map.size();
        }
    }


    @Override
    public Collection<?> _variable_keys() {
        synchronized (lock) {
            return null == keySetCache ? keySetCache = new ReplaceCollection(this) : keySetCache;
        }
    }



    @Override
    public int hashCode() {
        synchronized (lock) {
            return map.hashCode();
        }
    }

    @Override
    public boolean equals(Object obj) {
        synchronized (lock) {
            if (obj instanceof Re_VariableMapAsKeyword) {
                return map.equals(((Re_VariableMapAsKeyword) obj).map);
            }
            return map.equals(obj);
        }
    }

    @Override
    public String toString() {
        synchronized (lock) {
            return map.toString();
        }
    }





    @SuppressWarnings("rawtypes")
    public static class ReplaceCollection implements Collection {
        final Re_VariableMapAsKeyword parent;
        final Collection<Object> objectCollection;
        public ReplaceCollection(Re_VariableMapAsKeyword parent) {
            this.parent = parent;
            this.objectCollection = parent.map.keySet();
        }


        @Override
        public int size() {
            synchronized (parent.lock) {
                return objectCollection.size();
            }
        }

        @Override
        public boolean isEmpty() {
            synchronized (parent.lock) {
                return objectCollection.isEmpty();
            }
        }

        @Override
        public void clear() {
            synchronized (parent.lock) {
                parent.map.clear();
                parent.valueMappingKey.clear();
            }
        }

        @Override
        public int hashCode() {
            synchronized (parent.lock) {
                return objectCollection.hashCode();
            }
        }

        @Override
        public boolean equals(Object obj) {
            synchronized (parent.lock) {
                if (obj instanceof ReplaceCollection) {
                    return objectCollection.equals(((ReplaceCollection) obj).objectCollection);
                }
                return objectCollection.equals(obj);
            }
        }

        @Override
        public String toString() {
            synchronized (parent.lock) {
                return objectCollection.toString();
            }
        }

        @Override
        public Iterator<?> iterator() {
            return wrapIterator(this);
        }


        @Override
        public boolean add(Object key) {
            synchronized (parent.lock) {
                boolean b = parent._variable_has(key);
                if (b) {
                    return false;
                } else {
                    return null == parent._variable_put(key, null);
                }
            }
        }
        @Override
        public boolean remove(Object key) {
            return parent._variable_remove(key) != null;
        }

        @Override
        public boolean contains(Object key) {
            return parent._variable_has(key);
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
                if (!c.contains(next)) {
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
                if (c.contains(this_next)) {
                    this_it.remove();
                    modified = true;
                }
            }
            return modified;
        }




        @Override
        public Object[] toArray() {
            return objectCollection.toArray();
        }

        @Override
        public Object[] toArray(Object[] a) {
            return objectCollection.toArray(a);
        }
    }
    /**
     * 因为替换了null key 所以需要对Set进行过滤
     */
    public static Iterator wrapIterator(final ReplaceCollection parent) {
        if (null == parent) {
            return null;
        }

        final Iterator iterator = parent.objectCollection.iterator();
        return new Iterator() {
            final Object lock = new Object();
            Object current;

            @Override
            public boolean hasNext() {
                // TODO: Implement this method
                return iterator.hasNext();
            }


            @Override
            public Object next() {
                // TODO: Implement this method
                synchronized (lock) {
                    return current = iterator.next();
                }
            }

            @Override
            public void remove() {
                // TODO: Implement this method
                synchronized (lock) {
                    parent.remove(current);
                }
            }
        };
    }
}
