package top.fols.box.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

import top.fols.box.util.interfaces.XInterfaceGetOriginMap;

/**
 * each operation will format the key
 */
public class XBlurryKeyMap<K extends Object, V extends Object> implements Map<K, V>, Serializable, XInterfaceGetOriginMap {
    private static final long serialVersionUID = 1L;




    public static interface KeyFormatProcess {
        public static final KeyFormatProcess TO_LOWER_CASE = new KeyFormatProcess(){
            @Override
            public Object format(Object originKey) {
                if (!(originKey instanceof CharSequence)) {
                    return originKey;
                }
                return originKey.toString().toLowerCase();
            }
        };
        public static final KeyFormatProcess TO_UPPER_CASE = new KeyFormatProcess(){
            @Override
            public Object format(Object originKey) {
                if (!(originKey instanceof CharSequence)) {
                    return originKey;
                }
                return originKey.toString().toUpperCase();
            }
        };

        public Object format(Object originKey);
    }







    private KeyFormatProcess keyFormatProcess;
    private Object formatKey(Object originKey) {
        return (null == this.keyFormatProcess ?KeyFormatProcess.TO_LOWER_CASE: this.keyFormatProcess).format(originKey);
    }
    public XBlurryKeyMap<K,V> setKeyFormatProcess(KeyFormatProcess kfp) {
        this.keyFormatProcess = kfp;
        return this;
    }
    public KeyFormatProcess getKeyFormatProcess() {
        return this.keyFormatProcess;
    }



    @Override
    public int hashCode() {
        return this.map.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Map) {
            return this.map.equals(obj);
        } else if (obj instanceof XBlurryKeyMap) {
            return super.equals(obj);
        }
        return false;
    }

    @Override
    public XBlurryKeyMap<K, V> clone() {
        return new XBlurryKeyMap<K, V>(this.map);
    }

    @Override
    public String toString() {
        return this.map.toString();
    }





    private Map<K, V> map;
    public XBlurryKeyMap() {
        this.setMap(new LinkedHashMap<K, V>());
    }
    public XBlurryKeyMap(Map<K, V> putAll) {
        this.setMap(new LinkedHashMap<K, V>(putAll));
    }



    public XBlurryKeyMap<K,V> setMap(Map<K, V> setMap) {
        if (null == setMap) {
            throw new NullPointerException("set map");
        }
        this.map = setMap;
        this.reformatMap();
        return this;
    }

    @Override
    public Map<K, V> getMap() {
        return this.map;
    }

    public XBlurryKeyMap<K,V> reformatMap() {
        Map<K, V> tempMap = new LinkedHashMap<>();
        for (K k : this.map.keySet()) {
            tempMap.put((K) formatKey(k), this.map.get(k));
        }
        this.map.clear();
        this.map.putAll(tempMap);
        return this;
    }



    @Override
    public int size() {
        return this.map.size();
    }

    @Override
    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.map.containsKey(formatKey(key));
    }

    @Override
    public boolean containsValue(Object value) {
        return this.map.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return this.map.get(formatKey(key));
    }

    @Override
    public V put(K key, V p2) {
        return this.map.put((K)formatKey(key), p2);
    }

    @Override
    public V remove(Object key) {
        return this.map.remove(formatKey(key));
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        for (K key: map.keySet()) {
            V value = map.get(key);
            this.put((K)formatKey(key), value);
        }
    }

    @Override
    public void clear() {
        this.map.clear();
    }

    @Override
    public Set<K> keySet() {
        return this.map.keySet();
    }

    @Override
    public Collection<V> values() {
        return this.map.values();
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return this.map.entrySet();
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        return this.map.getOrDefault(formatKey(key) , defaultValue);
    }

    @Override
    public V putIfAbsent(K key, V value) {
        return this.map.putIfAbsent((K)formatKey(key) , value);
    }

    @Override
    public V replace(K key, V value) {
        return this.map.replace((K)formatKey(key) , value);
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        return this.map.computeIfAbsent((K)formatKey(key) , mappingFunction);
    }

    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return this.map.computeIfPresent((K)formatKey(key), remappingFunction);
    }

    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return this.compute((K)formatKey(key), remappingFunction);
    }

    @Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return this.map.merge((K)formatKey(key), value, remappingFunction);
    }

}
