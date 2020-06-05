package top.fols.box.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * XCollections
 */
public class XCollections {

    public static <T> void sort(T[] cover, Comparator<T> c) {
        XCollections.sort(cover, 0, cover.length, c);
    }

    public static <T> void sort(T[] cover, int off, int ed, Comparator<T> c) {
        int i, j;
        for (i = off; i < ed - 1; i++) {
            for (j = off; j < ed - 1 - i + off; j++) {
                if (c.compare(cover[j], cover[j + 1]) > 0) {
                    T temp = cover[j];
                    cover[j] = cover[j + 1];
                    cover[j + 1] = temp;
                }
            }
        }
    }

    @SuppressWarnings("all")
    public static <T> void sort(List<T> cover, Comparator<T> c) {
        XCollections.sort(cover, 0, cover.size(), c);
    }

    @SuppressWarnings("all")
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

    public static <T> List<T> filter(List<T> origin, XObjects.AcceptProcess<T> fp) {
        return XCollections.filter(origin, 0, origin.size(), fp);
    }

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

    public static <T> T[] filter(T[] origin, XObjects.AcceptProcess<T> fp) {
        return XCollections.filter(origin, 0, origin.length, fp);
    }

    public static <T> T[] filter(T[] origin, int off, int len, XObjects.AcceptProcess<T> fp) {
        List<T> newList = new ArrayList<T>();
        for (int i = 0; i < len; i++) {
            T content = origin[off + i];
            if (fp.accept(content)) {
                newList.add(content);
            }
        }
        Object array = XArray.newInstance(XArray.getElementClass(origin), newList.size());
        T[] newArray = newList.toArray((T[]) array);
        newList = null;
        return newArray;
    }

}
