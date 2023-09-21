package top.fols.atri.cache;

import java.lang.ref.Reference;

import top.fols.atri.interfaces.annotations.ThreadSafe;
import top.fols.atri.interfaces.interfaces.IReleasable;

import java.lang.ref.WeakReference;

@ThreadSafe
public abstract class WeakCache<V, Ex extends Throwable> implements IReleasable {
//    static List<int[]> caches = new ArrayList<>();
//    static final WeakCache<Map<?, ?>, RuntimeException> cache = new WeakCache<Map<?, ?>, RuntimeException>() {
//        @Override
//        public Map<?, ?> createCache() {
//            caches.clear();
//            return new ConcurrentHashMap<>();
//        }
//    };
//    public static void main(String[] args) {
//        synchronized (cache) {
//            for (int c = 0; c < 1000 * 10000; c++) {
//                Map<?, ?> orCreateCache = cache.getOrCreateCache();
//                caches.add(new int[100 * 10000]);
//                System.out.println(c + ":" + orCreateCache);
//                System.out.println(c + ":" + caches.size());
//				try {
//					Thread.sleep(100);
//				} catch (InterruptedException e) {}
//				System.out.println(cache.hashCached());
//            }
//            System.out.println(cache.getOrCreateCache());
//
//            cache.release();
//            caches.clear();
//            caches = null;
//        }
//    }
//
//  //fast !!!
//	static final WeakCache<List<int[]>, RuntimeException> cache = new WeakCache<List<int[]>, RuntimeException>() {
//        @Override
//        public List<int[]> createCache() {
//            return new ArrayList<>();
//        }
//    };
//	public static void main(String[] args) {
//        synchronized (cache) {
//            for (int c = 0; c < 1000 * 10000; c++) {
//                List<int[]> orCreateCache = cache.getOrCreateCache();
//                orCreateCache.add(new int[100 * 10000]);
//                System.out.println(c + ":" + orCreateCache.size());
//				System.out.println(cache.hashCached());
//            }
//        }
//    }

//
    protected Reference<WrapValue<V>> createReference(WrapValue<V> value) {
        return new WeakReference<>(value);
    }
    Reference<WrapValue<V>> reference = createReference(null);


    public abstract V createCache() throws Ex;

    public final WrapValue<V> getOrCreateCacheValue() throws Ex {
        WrapValue<V> vw;
        if (null == (vw = reference.get())) {
            reference = createReference(vw = new WrapValue<>(createCache()));
        }
        return vw;
    }
	public final boolean hashCached() {
		return null != reference.get();
	}


    public final V getOrCreateCache() throws Ex {
        return getOrCreateCacheValue().value;
    }



    @Override
    public final boolean release() {
        reference.clear();
        return true;
    }
    @Override
    public boolean released() {
        return null == reference.get();
    }


}

