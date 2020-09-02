package top.fols.box.util;

import java.util.*;

/**
 * XCollections
 */
public class XCollections {


    /**
     * @see top.fols.box.util.XArray#sort
     */
    public static <T> void sort(List<T> cover, Comparator<T> c) {
        XCollections.sort(cover, 0, cover.size(), c);
    }
    /**
     * @see top.fols.box.util.XArray#sort
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
     * @see top.fols.box.util.XArray#filter
     */
    public static <T> List<T> filter(List<T> origin, XObjects.AcceptProcess<T> fp) {
        return XCollections.filter(origin, 0, origin.size(), fp);
    }
    /**
     * @see top.fols.box.util.XArray#filter(Object[], int, int, XObjects.AcceptProcess)
     */
    public static <T> List<T> filter(List<T> origin, int off, int len, XObjects.AcceptProcess<T> fp) {
        List<T> newList = new ArrayList<T>();
        for (int i = 0; i < len; i++) {
            T content = origin.get(off + i);
            if (fp.accept(content)) {
                newList.add(content);
            }
        }
        return newList;
    }


    /**
     * 去除重复元素
     *
     * @see top.fols.box.util.XArray#deduplication
     */
    public static <T> Collection<T> deduplication(Collection<T> array) {
        if (null == array) {
            return null;
        }
        Map<T, Object> map = new LinkedHashMap<>();
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
        Map<T, Object> map = new LinkedHashMap<>();
        for (int i = 0; i < len; i++) {
            T element = array.get(off + i);
            map.put(element, null);
        }
        ArrayList newArray = new ArrayList(map.keySet());
        map = null;
        return newArray;
    }


}
