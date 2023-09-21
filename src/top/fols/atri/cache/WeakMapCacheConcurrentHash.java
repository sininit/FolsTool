package top.fols.atri.cache;

import top.fols.atri.interfaces.annotations.ThreadSafe;
import top.fols.atri.interfaces.interfaces.IReleasable;
import top.fols.atri.interfaces.annotations.NotNull;
import top.fols.atri.interfaces.interfaces.IInnerMap;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Using GC to empty map automatically
 * It should be noted that your cache must be able to be regenerated at any time through the key
 */
@ThreadSafe
public abstract class WeakMapCacheConcurrentHash<K, V,
Ex extends Throwable> implements IInnerMap<K, V>, IReleasable {
//    static final WeakMapCacheConcurrentHash<Integer, int[],RuntimeException> cache = new WeakMapCacheConcurrentHash<Integer, int[], RuntimeException>() {
//        @Override
//        public int[] newCache(Integer integer) {
//            return new int[100 * 10000];
//        }
//    };
//    public static  void main(String[] args) {
//        synchronized (cache) {
//            int max = 10000;
//            for (int c = 0; c < max; c++) {
//                cache.getOrCreateCache(c);
//				System.out.println(cache.size());
//            }
//            System.out.println(cache.size() != max);
//            cache.release();
//        }
//    }




    Reference<WrapValue<Map<K, V>>> createReference(WrapValue<Map<K, V>> value) {
        return new WeakReference<>(value);
    }
    Reference<WrapValue<Map<K, V>>> reference = createReference(null);

    @Override
    public final Map<K, V> getInnerMap() {
        WrapValue<Map<K, V>> vWrap;
        if (null == (vWrap = reference.get())) {
            reference = createReference(vWrap = new WrapValue<Map<K, V>>(new ConcurrentHashMap<K, V>()));
        }
        return vWrap.value;
    }

    @NotNull 
    public final V getOrCreateCache(@NotNull K k) throws Ex {
        Map<K, V> map = getInnerMap();
        V lastCache;
        if (null ==   (lastCache = map.get(k))) {
            map.put(k, lastCache = newCache(k));
        }
        return lastCache;
    }
	public boolean hashCached(K k) {
		WrapValue<Map<K, V>> vWrap = reference.get();
		return null != vWrap && vWrap.value.containsKey(k);
	}
	public int cachedCount() {
		WrapValue<Map<K, V>> vWrap = reference.get();
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

    @NotNull
    public abstract V newCache(K k) throws Ex;

}


