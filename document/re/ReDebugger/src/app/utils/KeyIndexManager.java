package app.utils;

import java.io.Serializable;
import java.util.*;

/**
 * Both indexMap and nameMap are unique
 */
public class KeyIndexManager implements Serializable {
    private static final long serialVersionUID = 42L;


    LinkedHashMap<Integer, String> indexMap = new LinkedHashMap<>();
    LinkedHashMap<String, Integer> nameMap = new LinkedHashMap<>();
    List<String> list = new ArrayList<>();
    List<String> unmodifiableList = Collections.unmodifiableList(this.list);

    public boolean contains(String key) { synchronized (this) { return this.nameMap.containsKey(key); } }
    public boolean contains(int key) { synchronized (this) { return this.indexMap.containsKey(key); } }

    public int size() { synchronized (this) { return this.nameMap.size(); } }

    public int getIndex(String key) { Integer index = this.nameMap.get(key);return  null == index? -1: index; }
    public String getKey(int index) { String value  = this.indexMap.get(index);return value; }

    public List<String> getList() {
        return unmodifiableList;
    }

    public boolean isFirst(int index) {
        return index == 0;
    }

    public boolean isLast(int index) {
        synchronized (this) {
            return index == size() - 1;
        }
    }

    public void set(int index, String key) {
        if (index < 0) { throw new ArrayIndexOutOfBoundsException("index=" + index); }
        synchronized (this) {
            if (index >= this.list.size()) { throw new ArrayIndexOutOfBoundsException("index=" + index+", size=" + this.list.size()); }
            if (this.nameMap.containsKey(key)) { throw new RuntimeException("contains key: "+key); }

            String old = this.list.get(index);
            this.indexMap.put(index, key);
            this.nameMap.remove(old);
            this.nameMap.put(key, index);
            this.list.set(index, key);
        }
    }

    public void add(int index, String key) {
        if (index < 0) { throw new ArrayIndexOutOfBoundsException("index=" + index); }
        synchronized (this) {
            if (this.nameMap.containsKey(key)) { throw new RuntimeException("contains key: "+key); }

            int size = this.list.size();
            if (size < index) { throw new ArrayIndexOutOfBoundsException("index=" + index+", size="+size); }

            if (index == size) {
                this.indexMap.put(index, key);
                this.nameMap.put(key, index);
                this.list.add(key);
            } else {
                this.list.add(index, key);
                this.resetMap();
            }
        }
    }
    public void add(String key) {
        synchronized (this) {
            this.add(this.size(), key);
        }
    }
    public void addAll(String[] keys) {
        synchronized (this) {
            for (String key: keys){
                this.add(this.size(), key);
            }
        }
    }
    public void addAll(Collection<String> keys) {
        synchronized (this) {
            for (String key: keys){
                this.add(this.size(), key);
            }
        }
    }

    public boolean remove(int index) {
        if (index < 0) { return false; }
        synchronized (this) {
            int size = this.list.size();
            if (size < index) { return false; }

            if (size == index) {
                String key = this.list.get(index);
                this.indexMap.remove(index);
                this.nameMap.remove(key);
                this.list.remove(index);
            } else {
                this.list.remove(index);
                this.resetMap();
            }
            return true;
        }
    }
    public boolean remove(String key) {
        synchronized (this) {
            boolean has = this.nameMap.containsKey(key);
            if (has) {
                int size = this.list.size();
                int index = this.nameMap.get(key);
                if (index == size) {
                    this.indexMap.remove(index);
                    this.nameMap.remove(key);
                    this.list.remove(index);
                } else {
                    this.list.remove(index);
                    this.resetMap();
                }
                return true;
            } else {
                return false;
            }
        }
    }

    public void clear(){
        synchronized (this) {
            this.indexMap.clear();
            this.nameMap.clear();
            this.list.clear();
        }
    }

    void resetMap() {
        int size = this.list.size();

        LinkedHashMap<Integer, String> newIndexMap = new LinkedHashMap<>();
        for (int i = 0; i < size; i++) { newIndexMap.put(i, list.get(i)); }
        this.indexMap = newIndexMap;

        LinkedHashMap<String, Integer> newNameMap = new LinkedHashMap<>();
        for (int i = 0; i < size; i++) { newNameMap.put(list.get(i), i); }
        this.nameMap = newNameMap;
    }

    @Override
    public String toString() {
        return "KeyIndexManager{" +
                "indexMap=" + indexMap +
                '}';
    }
}
