package top.fols.box.util;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
public class XMapKeyCheck <E extends Object> {
	public XMapKeyCheck(E[] array) {
		putAll(array);
		array = null;
	}
	public XMapKeyCheck(Map<E,?> map) {
		Set<E> set = map.keySet();
		for (E i:set)
			Hash.put(i, null);
	}
	public XMapKeyCheck() {
		super();
	}
	
	public Map<E,?> map(){ return Hash; }
	
	private Map<E,?> Hash = new HashMap<E,Object>();
	public boolean contains(E o) {
		return Hash.containsKey(o);
	}
	public void clear() {
		Hash.clear();
	}
	public int size() {
		return Hash.size();
	}
	public void put(E Key) {
		Hash.put(Key, null);
	}
	public void putAll(E... array) {
		if (null == array)
			return;
		for (E Key:array)
			Hash.put(Key, null);
	}
	public void putAll(Map<E,?> map) {
		if (null == map)
			return;
		for (E k:Hash.keySet())
			Hash.put(k, null);
	}
	public Set<E> keySet() {
		return Hash.keySet();
	}


	public boolean containsAll(E... obj) {
		for (E i:obj)
			if (!Hash.containsKey(i))
				return false;
		return true;
	}


	public void remove(E key) {
		Hash.remove(key);
	}
	public void removeAll(E... obj) {
		for (E i:obj)
			Hash.remove(i);
	}

	@Override
	public String toString() {
		// TODO: Implement this method
		return Hash.toString();
	}
}
