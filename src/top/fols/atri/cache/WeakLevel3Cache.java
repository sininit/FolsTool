package top.fols.atri.cache;

import top.fols.atri.interfaces.annotations.ThreadSafe;
import top.fols.atri.interfaces.interfaces.IReleasable;

@ThreadSafe
@SuppressWarnings("unchecked")
public abstract class WeakLevel3Cache<K, V, EX extends Throwable> implements IReleasable {
	@Override
	public boolean release() {
		cache1 = cache2 = cache3 = null;
		return true;
	}
	@Override
	public boolean released() {
		return 
			null == cache1 &&
			null == cache2 &&
			null == cache3;
	}


	private volatile Object[] cache1 = null; // "Level 1" cache
	private volatile Object[] cache2 = null; // "Level 2" cache
	private volatile Object[] cache3 = null; // "Level 3" cache
	private void cache(K k, V data) {
		cache3 = cache2;
		cache2 = cache1;
		cache1 = new Object[] { k, data };
	}

	public V lookup(K k) throws EX {
		Object[] a;
		if ((a = cache1) != null && (k == a[0] || (null != k && k.equals(a[0]))))
			return (V)a[1];
		if ((a = cache2) != null && (k == a[0] || (null != k && k.equals(a[0])))) {
			cache2 = cache1;
			cache1 = a;
			return (V)a[1];
		}
		if ((a = cache3) != null && (k == a[0] || (null != k && k.equals(a[0])))) {
			cache3 = cache1;
			cache1 = a;
			return (V)a[1];
		}

		V cache = newLookupCache(k);
		cache(k, cache);
		return cache;
	}

	public boolean hashCached(K k) {
		Object[] a;
		if ((a = cache1) != null && (k == a[0] || (null != k && k.equals(a[0]))))
			return true;
		if ((a = cache2) != null && (k == a[0] || (null != k && k.equals(a[0]))))
			return true;
		if ((a = cache3) != null && (k == a[0] || (null != k && k.equals(a[0]))))
			return true;
		return false;
	}
	public int cachedCount() {
		int i = 0;
		if ((cache1) != null)
			i++;
		if ((cache2) != null)
			i++;
		if ((cache3) != null)
			i++;
		return i;
	}

	protected abstract V newLookupCache(K key) throws EX;
}

