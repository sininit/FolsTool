package top.fols.atri.cache;

import java.util.Map;
import java.util.WeakHashMap;

import top.fols.atri.interfaces.annotations.ThreadSafe;
import top.fols.atri.interfaces.interfaces.IReleasable;

@ThreadSafe
public abstract class WeakLevel3CacheAndWeakMap<K, V, Ex extends Throwable> extends WeakLevel3Cache<K, V, Ex> implements IReleasable {
	@Override
	public boolean release() {
		synchronized (map) {
			map.clear();
			return super.release() && map.size() == 0;
		}
	}
	@Override
	public boolean released() {
		return super.released() && map.size() == 0;
	}



	protected final Map<K, WrapValue <V> > map = new WeakHashMap<>();

	@Override
	protected final V newLookupCache(K key) throws Ex {
		// TODO: Implement this method 
		WrapValue <V> cache = map.get(key);
		if (null == cache) {
			synchronized (map) {
				map.put(key, cache = new WrapValue<>(newValueCache(key)));
			}
		}
		return cache.value;
	}

	@Override
	public boolean hashCached(K k) {
		// TODO: Implement this method
		return super.hashCached(k) ||
			map.containsKey(k);
	}

	@Override
	public int cachedCount() {
		// TODO: Implement this method
		return super.cachedCount() + map.size();
	}
	


	protected abstract V newValueCache(K key) throws Ex;
}



