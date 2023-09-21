package top.fols.atri.util;


import top.fols.atri.assist.util.IgnoreCaseHashMap;
import top.fols.atri.assist.util.IgnoreCaseLinkedHashMap;
import top.fols.atri.interfaces.annotations.Nullable;
import top.fols.atri.interfaces.interfaces.IFilter;
import top.fols.atri.interfaces.interfaces.IValue;
import top.fols.atri.lang.*;
import top.fols.atri.lang.Objects;
import top.fols.box.lang.Arrayx;

import java.lang.reflect.Array;
import java.util.*;

@SuppressWarnings({"SpellCheckingInspection"})
public class Lists {

    public static <T> void addIfNonNull(List<T> list, T element) {
        if (null != list)
            if (null != element)
                list.add(element);
    }
    public static <V> void fillToIndex(List<V> list, int index) {
        fillToSize(list, index + 1, (V) null);
    }
    public static <V> void fillToSize(List<V> list, int size) {
        fillToSize(list, size, (V) null);
    }

    public static <V> void fillToIndex(List<V> list, int index, V value) {
        fillToSize(list, index + 1, value);
    }
    public static <V> void fillToSize(List<V> list, int size, V value) {
        if (size > 0) {
            int rsize = list.size();
            if (rsize < size) {
                int add = size - rsize;
                while (add > 0) {
                    list.add(value);
                    add--;
                }
            }
        }
    }

    public static <V> void fillToIndex(List<V> list, int index, IValue<V> value) {
        fillToSize(list, index + 1, value);
    }
    public static <V> void fillToSize(List<V> list, int size, IValue<V> value) {
        if (size > 0) {
            int rsize = list.size();
            if (rsize < size) {
                int add = size - rsize;
                while (add > 0) {
                    list.add(value.get());
                    add--;
                }
            }
        }
    }




    public static <K, V> IgnoreCaseHashMap<K, V> toIgnoreCaseHashMap(Map<K, V> m) {
        if (null == m) return null;
        if (m instanceof IgnoreCaseHashMap)
            return (IgnoreCaseHashMap<K, V>) m;
        return new IgnoreCaseHashMap<K, V>(m);
    }
    public static <K, V> IgnoreCaseLinkedHashMap<K, V> toIgnoreCaseLinkedHashMap(Map<K, V> m) {
        if (null == m) return null;
        if (m instanceof IgnoreCaseLinkedHashMap)
            return (IgnoreCaseLinkedHashMap<K, V>) m;
        return new IgnoreCaseLinkedHashMap<K, V>(m);
    }

    public static <K, V> Set<K> getListMapKeySet(List<Map<K, V>> list) {
        Set<K> keys = new LinkedHashSet<>();
        for (int i = 0, size = list.size(); i < size; i++) {
            Map<K, V>   element = list.get(i);
            if (null != element)
                keys.addAll(element.keySet());
        }
        return keys;
    }


    /**
     //	 * [{num:1, file: 8},{num: 2, file: 9}]
     //	 *  >>
     //	 * [[1, 2], [8, 9]]
     //	 */
    public static <K, V> List<List<V>> toColumnValuesList(List<Map<K, V>> list) {
        List<List<V>> result = new ArrayList<>();
        int size = list.size();
        if (size > 0) {
            Set<K> firstSet = Objects.first(list).keySet();
            fillToSize(result, firstSet.size(), new IValue<List<V>>() {
                @Override
                public List<V> get() {
                    // TODO: Implement this method
                    return new ArrayList<V>();
                }
            });
            for (int i = 0; i < size; i++) {
                Map<K, V> element = list.get(i);
                int kIndex = 0;
                for (K k: firstSet) {
                    result.get(kIndex).add(element.get(k));
                    kIndex++;
                }
            }
        }
        return result;
    }


    public List<LinkedHashMap<String, Object>> listsToListMap(List<List<?>> rows, String[] columnName) {
        return listsToListMap(rows, columnName, new IValue<LinkedHashMap<String, Object>>(){
            @Override
            public LinkedHashMap<String, Object> get() {
                // TODO: Implement this method
                return new LinkedHashMap<String, Object>();
            }
        });
    }
    public <T extends Map<String, Object>> List<T> listsToListMap(List<List<?>> rows, String[] columnName, IValue<T> mapFactory) {
        if (rows == null)
            return  null;
        List<T> result = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            List row = rows.get(i);
            T map = mapFactory.get();
            for (int c = 0; c < columnName.length; c++) {
                String cn = columnName[c];
                Object v = Lists.opt(row, c);
                map.put(cn, v);
            }
            result.add(map);
        }
        return result;
    }




    public static <V> ArrayList<V> asArrayList(V... values) {
        if (null == values)
            return null;
        ArrayList<V> list = new ArrayList<V>();
        for (V v: values) {
            list.add(v);
        }
        return list;
    }



    public static Map asHashMap(Object... decaled) {
        if (decaled == null)
            return ((((null))));
        HashMap map = new HashMap();
        putMapFromVars((map), decaled);
        return  map;
    }
    public static Map asLinkedHashMap(Object... decaled) {
        if (decaled == null)
            return ((((null))));
        HashMap map = new LinkedHashMap();
        putMapFromVars((map), decaled);
        return  map;
    }




    public static void putMapFromVars(Map map, Object... decaled) {
        int  size = decaled.length;
        if ((size -= size % 2) > 0) {
            for (int i = 0; i < size;) {
                map.put(
                        decaled[i++],
                        decaled[i++]);
            }
        }
    }





    public static <T> Map<Integer, T> toIndexMap(List<T> list) {
        return toIndexMap(list, 0, list.size(),
                0);
    }
    public static <T> Map<Integer, T> toIndexMap(List<T> list, int listOffset, int listLen,
                                                 int mapIndexOffset) {
        Map<Integer, T> map = new LinkedHashMap<>();
        for (int i = 0; i < listLen; i++) {
            map.put(i + mapIndexOffset, list.get(listOffset + i));
        }
        return map;
    }

    public static <T> ArrayList<T> indexMapToArrayList(Map<Integer, T> map) {
        return indexMapToArrayList(map, 0, map.size());
    }
    public static <T> ArrayList<T> indexMapToArrayList(Map<Integer, T> map, int offset, int len) {
        if (offset >= 0) {
            ArrayList<T> list = new ArrayList<>();
            int size = 0;
            int limit = offset + len;
            for (Map.Entry<Integer, T> entry: map.entrySet()) {
                Integer     index = entry.getKey();
                if (null != index) {
                    if (index >= offset && index < limit) {
                        int i = index - offset;
                        if (i >= size) {
                            fillToSize(list, size = i + 1);
                        }
                        list.set(i, entry.getValue());
                    }
                }
            }
            return list;
        } else throw new ArrayIndexOutOfBoundsException("offset: " + offset);
    }


    public static <T> boolean has(List<T> list, int index) {
        return index >= 0 && index < list.size();
    }

    public static <T> void put(List<T> list, int index, T value) {
        if (index >= 0) {
            if (index >= list.size()) {
                fillToIndex(list, index);
            }
            list.set(index, value);
        } else throw new ArrayIndexOutOfBoundsException("index: " + index);
    }
    @Nullable
    public static <T> T computeIfAbsent(List<T> list, int index, IValue<T> orValue) {
        if (index >= 0) {
            if (index >= list.size()) {
                fillToIndex(list, index);
                T v = orValue.get();
                list.set(index, v);
                return v;
            } else {
                return list.get(index);
            }
        } else throw new ArrayIndexOutOfBoundsException("index: " + index);
    }





    @Nullable
    public static <T> T opt(List<T> list, int index) {
        if (index >= 0 && index < list.size()) {
            return list.get(index);
        }
        return null;
    }
    @Nullable
    public static <T> T opt(List<T> list, int index, T orValue) {
        if (index >= 0 && index < list.size()) {
            return list.get(index);
        }
        return orValue;
    }
    @Nullable
    public static <T> T opt(List<T> list, int index, IValue<T> orValue) {
        if (index >= 0 && index < list.size()) {
            return list.get(index);
        }
        return orValue.get();
    }



    public static <K, V> List<K> removeMapValueMappingKeyReturnKey(Map<K, V> kv, V value) {
        List<K> keys = new ArrayList<>();
        Iterator<Map.Entry<K, V>> iterator = kv.entrySet().iterator();
        Map.Entry<K, V> entry;
        if (null == value) {
            while (iterator.hasNext()) {
                entry = iterator.next();
                K k = entry.getKey();
                V v = entry.getValue();
                if (v == null) {
                    iterator.remove();
                    keys.add(k);
                }
            }
        } else {
            while (iterator.hasNext()) {
                entry = iterator.next();
                K k = entry.getKey();
                V v = entry.getValue();
                if (v.equals(value)) {
                    iterator.remove();
                    keys.add(k);
                }
            }
        }
        return keys;
    }

    public static <K, V> void removeValueMappingKey(Map<K, V> kv, V value) {
        Iterator<V> vs =  kv.values().iterator();
        if (null == value) {
            while (vs.hasNext())
                if (null == vs.next())
                    vs.remove();
        } else {
            while (vs.hasNext())
                if (value.equals(vs.next()))
                    vs.remove();
        }
    }
    public static <K, V> Map<V, K> reverseMapKV(Map<K, V> oldMap, Map<V, K> newMap) {
        for (K k:  oldMap.keySet())	{
            V v = oldMap.get(k);

            if (newMap.containsKey(v)) {
                throw new UnsupportedOperationException("repeat key: " + v);
            }
            newMap.put(v, k);
        }
        return newMap;
    }

    /**
     * @see Arrayz#sort
     */
    public static <T> void sort(List<T> cover, Comparator<T> c) {
        Lists.sort(cover, 0, cover.size(), c);
    }
    /**
     * @see Arrayz#sort
     */
    public static <T> void sort(List<T> cover, int off, int ed, Comparator<T> c) {
        int i, j;
        for (i = off; i < ed - 1; i++) {
            for (j = off; j < ed - 1 - i + off; j++) {
                if (c.compare(cover.get(j), cover.get(j + 1)) > 0) {
                    T temp = cover.get(j);
                    cover.set(j, cover.get(j + 1));
                    cover.set(j + 1, temp);
                }
            }
        }
    }


    /**
     * @see Lists#filter
     */
    public static <T> List<T> filter(List<T> origin, IFilter<T> fp) {
        return Lists.filter(origin, 0, origin.size(), fp);
    }
    /**
     * @see Arrayz#filter(Object[], int, int, top.fols.atri.interfaces.interfaces.IFilter)
     */
    public static <T> List<T> filter(List<T> origin, int off, int len, IFilter<T> fp) {
        List<T> newList = new ArrayList<T>();
        for (int i = 0; i < len; i++) {
            T content = origin.get(off + i);
            if (fp.next(content)) {
                newList.add(content);
            }
        }
        return newList;
    }







    /**
     * 去除重复元素
     *
     * @see Arrayx#deduplication
     */
    public static <T> Collection<T> deduplication(Collection<T> array) {
        if (null == array) {
            return null;
        }
        Map<T, Object> map = new LinkedHashMap<>();//局部变量
        for (T element : array) {
            map.put(element, null);
        }
        Collection<T> newArray = new ArrayList<>(map.keySet());
        map = null;
        return newArray;
    }
    public static <T> List<T> deduplication(List<T> array) {
        return null == array ? null : deduplication(array, 0, array.size());
    }
    public static <T> List<T> deduplication(List<T> array, int off, int len) {
        if (null == array) {
            return null;
        }
        Map<T, Object> map = new LinkedHashMap<>();//局部变量
        for (int i = 0; i < len; i++) {
            T element = array.get(off + i);
            map.put(element, null);
        }
        ArrayList<T> newArray = new ArrayList<>(map.keySet());
        map = null;
        return newArray;
    }

    @SafeVarargs
    public static <T> Set<T> asLinkedHashSet(T... arr) {
        return new LinkedHashSet<>(Arrays.asList(arr));
    }
    @SafeVarargs
    public static <T> Set<T> asHashSet(T... arr) {
        return new HashSet<>(Arrays.asList(arr));
    }




    public static <T> T[] toArray(List<T> collection, T[] buffer) {
        return toArray(collection, 0, collection.size(), buffer);
    }
    public static <T> T[] toArray(List<T> collection, int offset, int len,
                                  T[] buffer) {
        if (buffer.length < len)
            buffer = (T[]) Array.newInstance(buffer.getClass().getComponentType(), len);
        for (int i = offset, limit = offset + len, wi = 0;
             i < limit;
             i++, wi++) {
            buffer[wi] = collection.get(i);
        }
        return buffer;
    }

    public static <T> T[] toArray(Iterable<T> collection, T[] buffer) {
        if (collection instanceof List) {
            return ((List<T>) collection).toArray(buffer);
        } else {
            return toArray(collection.iterator(), buffer);
        }
    }
    public static <T> T[] toArray(Iterable<T> collection, int offset, int len,
                                  T[] buffer) {
        if (collection instanceof List) {
            return toArray(((List<T>) collection), offset, len, buffer);
        } else {
            return toArray(collection.iterator(),
                    offset, len, buffer);
        }
    }


    public static <T> T[] toArray(Iterator<T> collection, T[] buffer) {
        List<T> result = new ArrayList<>();
        while (collection.hasNext()) {
            result.add(collection.next());
        }
        return result.toArray(buffer);
    }
    public static <T> T[] toArray(Iterator<T> iterator , int offset, int len,
                                  T[] buffer) {
        if (buffer.length < len)
            buffer = (T[]) Array.newInstance(buffer.getClass().getComponentType(), len);
        for (int i = 0; i < offset; i++)
            iterator.next();
        for (int i = 0; i < len; i++)
            buffer[i] = iterator.next();
        return buffer;
    }



    public static <T> Iterable<T> filterIterable(final Iterable<T> iterable, final IFilter<T> filter) {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                // TODO: Implement this method
                final Iterator<T> iterator = iterable.iterator();
                return new Iterator<T>() {
                    private T v;
                    private boolean geted = true;

                    @Override
                    public boolean hasNext() {
                        // TODO: Implement this method
                        if (geted) {
                            while (iterator.hasNext())	{
                                T next = iterator.next();
                                if (filter.next(next)) {
                                    this.v = next;
                                    return true;
                                }
                            }
                            return false;
                        } else {
                            return true;
                        }
                    }
                    @Override
                    public T next() {
                        // TODO: Implement this method
                        geted = true;
                        return v;
                    }

                    @Override
                    public void remove() {
                        // TODO: Implement this method
                        iterator.remove();
                    }
                };
            }
        };
    }







    public static <T> Iterable<T> wrapArray(final T[] array, final int astart, final int alen) {
        if (null == array || astart < 0 || alen < 0 || astart > array.length) {
            return new Iterable<T>() {
                @Override
                public Iterator<T> iterator() {
                    return Collections.emptyIterator();
                }
            };
        }
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return new Iterator<T>() {
                    int cursor = astart;
                    final int range = astart + alen;

                    @Override
                    public boolean hasNext() {
                        return cursor != range;
                    }

                    @Override
                    public T next() {
                        return array[cursor++];
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    /**
     * 类似于python的 range
     * 假设 start是0 stop是10 只会遍历到 0和 10之前的数字
     */
    public static Iterable<Integer> wrapRange(final int start, final int stop) {
        if (stop > start) {
            return wrapRange(start, stop, 1);
        } else if (stop == start) {
            return wrapRange(start, stop, 0);
        } else {
            return wrapRange(start, stop, -1);
        }
    }

    public static Iterable<Integer> wrapRange(final int start, final int stop, final int setp) {
        if (stop > start) {
            return new Iterable<Integer>() {
                @Override
                public Iterator<Integer> iterator() {
                    // TODO: Implement this method
                    return new Iterator<Integer>() {

                        boolean ed = false;
                        int key = start;

                        @Override
                        public boolean hasNext() {
                            // TODO: Implement this method
                            if (ed) {
                                return key + setp < stop;
                            } else {
                                return true;
                            }
                        }

                        @Override
                        public Integer next() {
                            // TODO: Implement this method
                            if (ed) {
                                key += setp;
                            } else {
                                key = start;
                                ed = true;
                            }
                            return key;
                        }

                        @Override
                        public void remove() {
                            throw new UnsupportedOperationException("remove");
                        }
                    };
                }

                @Override
                public String toString() {
                    Iterator<Integer> iterator = this.iterator();
                    StringBuilder sb = new StringBuilder();
                    sb.append("[");
                    while (iterator.hasNext()) {
                        sb.append(iterator.next());
                        if (iterator.hasNext()) {
                            sb.append(", ");
                        }
                    }
                    sb.append("]");
                    return sb.toString();
                }
            };
        } else if (start == stop) {
            return new Iterable<Integer>() {
                @Override
                public Iterator<Integer> iterator() {
                    // TODO: Implement this method
                    return new Iterator<Integer>() {
                        @Override
                        public boolean hasNext() {
                            // TODO: Implement this method
                            return false;
                        }

                        @Override
                        public Integer next() {
                            // TODO: Implement this method
                            return null;
                        }

                        @Override
                        public void remove() {
                            throw new UnsupportedOperationException("remove");
                        }

                    };
                }

                @Override
                public String toString() {
                    Iterator<Integer> iterator = this.iterator();
                    StringBuilder sb = new StringBuilder();
                    sb.append("[");
                    while (iterator.hasNext()) {
                        sb.append(iterator.next());
                        if (iterator.hasNext()) {
                            sb.append(", ");
                        }
                    }
                    sb.append("]");
                    return sb.toString();
                }
            };
        } else {
            return new Iterable<Integer>() {
                @Override
                public Iterator<Integer> iterator() {
                    // TODO: Implement this method
                    return new Iterator<Integer>() {

                        boolean ed = false;
                        int key = start;

                        @Override
                        public boolean hasNext() {
                            // TODO: Implement this method
                            if (ed) {
                                return key + setp > stop;
                            } else {
                                return true;
                            }
                        }

                        @Override
                        public Integer next() {
                            // TODO: Implement this method
                            if (ed) {
                                key += setp;
                            } else {
                                key = start;
                                ed = true;
                            }
                            return key;
                        }

                        @Override
                        public void remove() {
                            throw new UnsupportedOperationException("remove");
                        }

                    };
                }

                @Override
                public String toString() {
                    Iterator<Integer> iterator = this.iterator();
                    StringBuilder sb = new StringBuilder();
                    sb.append("[");
                    while (iterator.hasNext()) {
                        sb.append(iterator.next());
                        if (iterator.hasNext()) {
                            sb.append(", ");
                        }
                    }
                    sb.append("]");
                    return sb.toString();
                }
            };
        }
    }









//    public static <RETURN, ELEMENT> Collection<RETURN> filter(Collection<RETURN> buffer, Next<RETURN, ELEMENT> executor,
//                                                              ELEMENT[] filter) {
//        return filter(buffer, executor, filter, 0, null == filter ?0: filter.length);
//    }
//    public static <RETURN, ELEMENT> Collection<RETURN> filter(Collection<RETURN> buffer, Next<RETURN, ELEMENT> executor,
//                                                              ELEMENT[] filter, int filter_offset, int filter_count) {
//        Objects.requireNonNull(buffer, "null buffer");
//        if (null == executor) {
//        } else {
//            if (null == filter) { return buffer; }
//            Value<RETURN> result = new Value<>();
//            for (int max = filter_offset + filter_count; filter_offset < max;) {
//                Value<RETURN> n = executor.next(result, filter[filter_offset++]);
//                if (null != n) {
//                    buffer.add(n.get());
//                }
//            }
//        }
//        return buffer;
//    }
//
//
//    public static <RETURN, ELEMENT> Collection<RETURN> filter(Collection<RETURN> buffer, Next<RETURN, ELEMENT> executor,
//                                                              List<ELEMENT> filter) {
//        return filter(buffer, executor, filter, 0, null == filter ?0: filter.size());
//    }
//    public static <RETURN, ELEMENT> Collection<RETURN> filter(Collection<RETURN> buffer, Next<RETURN, ELEMENT> executor,
//                                                              List<ELEMENT> filter, int filter_offset, int filter_count) {
//        Objects.requireNonNull(buffer, "null buffer");
//        if (null == executor) {
//        } else {
//            if (null == filter) { return buffer; }
//            Value<RETURN> result = new Value<>();
//            for (int max = filter_offset + filter_count; filter_offset < max;) {
//                Value<RETURN> n = executor.next(result, filter.get(filter_offset++));
//                if (null != n) {
//                    buffer.add(n.get());
//                }
//            }
//        }
//        return buffer;
//    }
//
//
//    public static <RETURN, ELEMENT> Collection<RETURN> filter(Collection<RETURN> buffer, Next<RETURN, ELEMENT> executor,
//                                                              Collection<ELEMENT> filter) {
//        Objects.requireNonNull(buffer, "null buffer");
//        if (null == executor) {
//        } else {
//            if (null == filter) { return buffer; }
//            Iterator<ELEMENT> array_iterator = filter.iterator();
//            Value<RETURN> result = new Value<>();
//            while  (array_iterator.hasNext()) {
//                Value<RETURN> n = executor.next(result, array_iterator.next());
//                if (null != n) {
//                    buffer.add(n.get());
//                }
//            }
//        }
//        return buffer;
//    }
//
//
//    public static <RETURN> Collection<RETURN> create(Collection<RETURN> buffer, Traverse<RETURN> executor) {
//        Objects.requireNonNull(buffer, "null buffer");
//        if (null == executor) {
//        } else {
//            Value<RETURN> result = new Value<>();
//            while (null != (result = executor.invoke(result))) {
//                buffer.add(result.get());
//            }
//        }
//        return buffer;
//    }
//    /**
//     * @param executor If executor returns null, it means the creation process is over
//     */
//    public static <RETURN> RETURN[] create(RETURN[] array, Traverse<RETURN> executor) {
//        Objects.requireNonNull(array, "array type");
//        List<RETURN> list = new ArrayList<>();
//        create(list, executor);
//        return  list.toArray(array);
//    }
//
//    public static <RETURN, ELEMENT> RETURN[] filter(RETURN[] toType,
//Next<RETURN, ELEMENT> executor, ELEMENT[] filter) {
//        return filter(toType, executor, filter, 0, null == filter ?0: filter.length);
//    }
//
//    public static <RETURN, ELEMENT> RETURN[] filter(RETURN[] toType,
//Next<RETURN, ELEMENT> executor, ELEMENT[] filter,
//int filter_offset, int filter_count) {
//        Objects.requireNonNull(toType, "toType");
//
//        List<RETURN> list = new ArrayList<>();
//        filter(list, executor, filter, filter_offset, filter_count);
//        return  list.toArray(toType);
//    }
//
//
//    public interface Next<RETURN, ELEMENT> {
//        Value<RETURN> next(Value<RETURN> returnValue, ELEMENT array_element);
//    }
//
//
//    /*
//     * try get next, If invoke return null then end
//     */
//    @SuppressWarnings("UnusedReturnValue")
//    public static abstract class Traverse<T> {
//        public abstract Value<T> invoke(Value<T> next);
//    }
//
//    /**
//     * traverse all elements, whether it encounters null or not
//     */
//    @SuppressWarnings({"unchecked", "ForLoopReplaceableByWhile"})
//    public static abstract class TraverseArray<RETURN, ELEMENT> extends Traverse<RETURN> implements Next<RETURN, ELEMENT> {
//        Object[] array;
//        public TraverseArray(ELEMENT[] array) {
//            this.array = null == array ? Finals.EMPTY_OBJECT_ARRAY: array;
//        }
//        int i = 0;
//
//
//
//        public Value<RETURN> end()    { this.i = array.length; return null; }
//        public int  index()  { return this.i; }
//        public int  index(int newIndex) { return this.i = newIndex;}
//        public int  length() { return this.array.length; }
//
//        Value<RETURN> next_result = new Value<>();
//
//
//
//        @Override
//        public final Value<RETURN> invoke(Value<RETURN> next) {
//            for (;i < array.length;) {
//                Value<RETURN> n = next(this.next_result, (ELEMENT) array[i++]);
//                if (null != n) {
//                    return  n;
//                }
//            }
//            return null;
//        }
//    }
//
//    public static abstract class TraverseList<RETURN, ELEMENT> extends Traverse<RETURN> implements Next<RETURN, ELEMENT> {
//        List<ELEMENT> array;
//        public TraverseList(List<ELEMENT> array) {
//            this.array = null == array ?new ArrayList<ELEMENT>() : array;
//        }
//        int i = 0;
//
//
//        public Value<RETURN> end()    { this.i = array.size(); return null; }
//        public int  index()  { return this.i; }
//        public int  index(int newIndex) { this.i = newIndex; return i;}
//        public int  length() { return this.array.size(); }
//
//        Value<RETURN> next_result = new Value<>();
//
//
//
//        @Override
//        public final Value<RETURN> invoke(Value<RETURN> next) {
//            for (int size = array.size(); i < size;) {
//                Value<RETURN> n = next(this.next_result, array.get(i++));
//                if (null != n) {
//                    return  n;
//                }
//            }
//            return null;
//        }
//    }
//
//    @SuppressWarnings("unchecked")
//    public static abstract class TraverseCollection<RETURN, ELEMENT> extends Traverse<RETURN> implements Next<RETURN, ELEMENT> {
//        Collection<ELEMENT> array;
//        Iterator<ELEMENT>   array_iterator;
//        public TraverseCollection(Collection<ELEMENT> array) {
//            this.array = null == array ?new ArrayList<ELEMENT>(): array;
//            this.array_iterator = this.array.iterator();
//        }
//
//        static final Iterator EMPTY = new Iterator<Object>() {
//            @Override
//            public boolean hasNext() {
//                return false;
//            }
//
//            @Override
//            public Object next() {
//                return null;
//            }
//
//            @Override
//            public void remove() {
//                throw new UnsupportedOperationException();
//            }
//        };
//
//
//        public Value<RETURN> end()    { this.array_iterator = EMPTY; return null; }
//        public int  length() { return this.array.size(); }
//
//        Value<RETURN> next_result = new Value<>();
//
//        @Override
//        public final Value<RETURN> invoke(Value<RETURN> next) {
//            while  (array_iterator.hasNext()) {
//                Value<RETURN> n = next(this.next_result, array_iterator.next());
//                if (null != n) {
//                    return  n;
//                }
//            }
//            return null;
//        }
//    }
}
