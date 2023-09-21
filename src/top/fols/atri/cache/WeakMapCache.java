package top.fols.atri.cache;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Map;
import top.fols.atri.interfaces.annotations.ThreadSafe;
import top.fols.atri.interfaces.interfaces.IInnerMap;
import top.fols.atri.interfaces.interfaces.IReleasable;

@ThreadSafe
public abstract class WeakMapCache<K, V, Ex extends Throwable> implements IInnerMap<K, WrapValue<V>>, IReleasable {
	protected abstract Map<K, WrapValue<V>> buildMap();



	Reference<WrapValue<Map<K, WrapValue<V>>>> createReference(WrapValue<Map<K, WrapValue<V>>> value) {
        return new WeakReference<>(value);
    }
    Reference<WrapValue<Map<K, WrapValue<V>>>> reference = createReference(null);

	@Override
    public final Map<K, WrapValue<V>> getInnerMap() {
        WrapValue<Map<K, WrapValue<V>>> vWrap;
        if (null == (vWrap = reference.get())) {
            reference = createReference(vWrap = new WrapValue<>(buildMap()));
        }
        return vWrap.value;
    }


	public V getOrCreateCache(K k) throws Ex {
        Map<K, WrapValue<V>> map = getInnerMap();
        WrapValue<V>   lastCache;
        if (null ==   (lastCache = map.get(k))) {
            //noinspection SynchronizationOnLocalVariableOrMethodParameter
            synchronized (map) {
                map.put(k, lastCache = new WrapValue<>(newCache(k)));
            }
        }
        return lastCache.value;
    }

	public boolean hashCached(K k) {
		WrapValue<Map<K, WrapValue<V>>> vWrap = reference.get();
		return null != vWrap && vWrap.value.containsKey(k);
	}
	public int cachedCount() {
		WrapValue<Map<K, WrapValue<V>>> vWrap = reference.get();
		return null != vWrap ? vWrap.value.size() : 0;
	}

	
	
	public final int size() {
        return getInnerMap().size();
    }

    @Override
    public final boolean release() {
        reference.clear();
        return true;
    }
    @Override
    public boolean released() {
        return getInnerMap().size() == 0;
    }





    public abstract V newCache(K k) throws Ex;
}

