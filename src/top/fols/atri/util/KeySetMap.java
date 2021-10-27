package top.fols.atri.util;

import top.fols.atri.util.interfaces.IInnerMap;
import top.fols.box.util.interfaces.XInterfaceSetInnerMap;

import java.io.Serializable;
import java.util.*;

public class KeySetMap<K> implements Serializable, IInnerMap<K, Object>, XInterfaceSetInnerMap<K, Object> {
	private static final long serialVersionUID = 1L;


	private Map<K, Object> map = new LinkedHashMap<>();
	public KeySetMap() {
		super();
	}

	public KeySetMap(Map<K,  ?> putAll) throws NullPointerException {
		if (null == putAll) {
			throw new NullPointerException("put map");
		}
		this.putAll(putAll);
	}


	@Override
	public void setInnerMap(Map<K, Object> setMap) {
		if (null == setMap) {
			throw new NullPointerException("set map");
		}
		this.map = setMap;
	}
	@Override
	public Map<K, Object> getInnerMap() {
		return this.map;
	}




	public KeySetMap<K> put(K key) {
		this.map.put(key, null);
		return this;
	}

	public KeySetMap<K> putAll(KeySetMap<K> key) {
		this.map.putAll((Map) key.map);
		return this;
	}
	public KeySetMap<K> putAll(Map<K, ?> keys) {
		for (K k : keys.keySet()) {
			this.put(k);
		}
		return this;
	}
	public KeySetMap<K> putAll(Collection<K> keys) {
		for (K k : keys) {
			this.put(k);
		}
		return this;
	}
	public KeySetMap<K> putAll(K... keys) {
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
		if (!(obj instanceof KeySetMap)) {
			return false;
		}
		return this.map.equals(((KeySetMap) obj).map);
	}

	@Override
	public int hashCode() {
		// TODO: Implement this method
		return this.map.hashCode();
	}

	@Override
	public KeySetMap<K> clone() {
		return new KeySetMap<K>(this.map);
	}

	@Override
	public String toString() {
		// TODO: Implement this method
		return Arrays.toString(this.toArray((K[]) new Object[this.size()]));
	}

}

