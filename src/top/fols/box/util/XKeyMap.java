package top.fols.box.util;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import top.fols.box.util.interfaces.XInterfaceGetInnerMap;

import java.util.Collection;

public class XKeyMap<K extends Object> implements Serializable, XInterfaceGetInnerMap {
	private static final long serialVersionUID = 1L;


	private Map<K, ?> map = new LinkedHashMap<>();

	public XKeyMap() {
		super();
	}

	public XKeyMap(Map<K,  ?> putAll) throws NullPointerException {
		if (null == putAll) {
			throw new NullPointerException("put map");
		}
		this.putAll(putAll);
	}




	public XKeyMap<K> setMap(Map<K, ?> setMap) {
        if (null == setMap) {
            throw new NullPointerException("set map");
        }
        this.map = setMap;
        return this;
	}
	
	@Override
    public Map<K, ?> getInnerMap() {
        return this.map;
	}
	



	public XKeyMap<K> put(K key) {
		this.map.put(key, null);
		return this;
	}

	public XKeyMap<K> putAll(XKeyMap<K> key) {
		this.map.putAll((Map) key.map);
		return this;
	}

	public XKeyMap<K> putAll(Map<K, ?> keys) {
		for (K k : keys.keySet()) {
			this.put(k);
		}
		return this;
	}

	public XKeyMap<K> putAll(Collection<K> keys) {
		for (K k : keys) {
			this.put(k);
		}
		return this;
	}

	public XKeyMap<K> putAll(K... keys) {
		for (K k : keys) {
			this.put(k);
		}
		keys = null;
		return this;
	}

	public K[] toArray(K[] array) {
		return this.map.keySet().toArray(array);
	}

	public boolean containsKey(K key) {
		return this.map.containsKey(key);
	}

	public int size() {
		return this.map.size();
	}

	public Set<K> keySet() {
		return this.map.keySet();
	}

	public void remove(K key) {
		this.map.remove(key);
	}

	public void clear() {
		this.map.clear();
	}






	@Override
	public boolean equals(Object obj) {
		// TODO: Implement this method
		if (!(obj instanceof XKeyMap)) {
			return false;
		}
		return this.map.equals(((XKeyMap) obj).map);
	}

	@Override
	public int hashCode() {
		// TODO: Implement this method
		return this.map.hashCode();
	}

	@Override
    public XKeyMap<K> clone() {
        return new XKeyMap<K>(this.map);
    }

	@Override
	public String toString() {
		// TODO: Implement this method
		return Arrays.toString(this.toArray((K[]) new Object[this.size()]));
	}

}
