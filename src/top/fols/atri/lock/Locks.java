package top.fols.atri.lock;

import top.fols.atri.lang.Value;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Because the returned data contains keys, which are not automatically released if in use
 */
public class Locks<K> {

//	static final Locks locks = new Locks();
//	static final WeakIdentityHashMap IDENTITY_HASH_MAP = new WeakIdentityHashMap();
//	@SuppressWarnings("unchecked")
//	public static void test() throws InterruptedException {
//		List<Object> f = new ArrayList<>();
//		Value<Object> first = (Value<Object>) locks.getLock("784920843");
//		IDENTITY_HASH_MAP.put(first, 8888);
//		String firstString = locks.getLock(first.get()).toString();
//		Thread.sleep(1000);
//
//		int lastCount = 0;
//		int max = 10000 * 10000;
//		for (int c = 0; c < max; c++) {
//			Value<Object> lock = (Value<Object>) locks.getLock("" + c);
//			IDENTITY_HASH_MAP.put(lock, 8888);
//			lock = null;
//
//
//			f.add(new int[100_0000]);
//
//			int newCount = locks.count();
//			if (newCount < lastCount) {
//				f.clear();
//
//				if (newCount == 1) {
//					System.out.println("test success"+IDENTITY_HASH_MAP.size());
//					Thread.sleep(1000);
//				}
//			} else {
//				lastCount = newCount;
//			}
//
//			System.out.println("count: " + locks.count());
//			System.out.println("cache: " + locks.getLock(new String("784920843")).toString().equals(firstString));
//		}
//		System.out.println("count: " + locks.count());
//		System.out.println("cache: " + locks.getLock(new String("784920843")).toString().equals(firstString));
//	}


	protected final Object lock = new Object();

	@SuppressWarnings("all")
	Map<K, Reference<Value<K>>> createMap() {
		return new WeakHashMap<>();
	}


	Map<K, Reference<Value<K>>> map = createMap();

	public int count() {
		synchronized (lock) {
			return map.size();
		}
	}
	public boolean hasLock(K key){
		synchronized (lock) {
			return map.containsKey(key);
		}
	}

	public Object getLock(K key) {
		synchronized (lock) {
			Value<K> lv; //A whole
			Reference<Value<K>>  value;
			if (null == (value = map.get(key)) || null == (lv = value.get())) {
				if (map.isEmpty()) {
					map = createMap();
				}
				map.put(key, new WeakReference<>(lv = new Value<>(key)));
			}
			return lv;
		}
	}
}
