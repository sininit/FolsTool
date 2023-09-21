package top.fols.atri.cache;

import java.util.Map;

import top.fols.atri.interfaces.annotations.ThreadUnsafe;
import top.fols.atri.lang.Objects;
import java.util.Set;
import java.util.Collection;
import java.util.Iterator;

import top.fols.atri.interfaces.interfaces.IReleasable;

/*
 * It is thread safe only when Collections.synchronizedMap is passed in
 */
@ThreadUnsafe
public class TemporaryMapValueModCacheWrap<K, V, Ex extends Throwable>
extends TemporaryMapValueModCache<K, V, Ex>
implements Map<K, V>, IReleasable {

	@Override
	public boolean release() {
		// TODO: Implement this method
		clear();
		return true;
	}

	@Override
	public boolean released() {
		// TODO: Implement this method
		return size() == 0;
	}
	

//	public static void main(String[] args) {
//		final TemporaryWrapMapValueModCache<Class, Object, RuntimeException> setConvertsCaches = new TemporaryWrapMapValueModCache<>(new HashMap<>());//instance field ?
//		setConvertsCaches.put(String.class, 1);
//		
//		final TemporaryWrapMapValueModCache<Class, Object, RuntimeException>.ValueCache v =  setConvertsCaches.createValueGetter(String.class);//instance field ?
//		v.getValue(); //fast
//		v.getValue(); //fast
//	}




	@Override
	public V fromMapGetValue(K k) throws Ex {
		// TODO: Implement this method
		return get(k);
	}


	Map<K, V> map;

	public TemporaryMapValueModCacheWrap(Map<K, V> map) {
		this.map = Objects.requireNonNull(map, "map");
	}

	@Override
	public V get(Object k) {
		return map.get(k);
	}

	@Override
	public int size() {
// TODO: Implement this method
		return map.size();
	}

	@Override
	public boolean isEmpty() {
// TODO: Implement this method
		return map.isEmpty();
	}

	@Override
	public boolean containsKey(Object p1) {
// TODO: Implement this method
		return map.containsKey(p1);
	}

	@Override
	public boolean containsValue(Object p1) {
// TODO: Implement this method
		return map.containsValue(p1);
	}


	@Override
	public V put(K k, V v) {
		try {
			return map.put(k, v);
		} finally { super.addMod(); }
	}

	@Override
	public V remove(Object p1) {
// TODO: Implement this method
		try {
			return map.remove(p1);
		} finally { super.addMod(); }
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> p1) {
// TODO: Implement this method
		try {
			map.putAll(p1);
		} finally { super.addMod(); }
	}

	@Override
	public void clear() {
// TODO: Implement this method
		try {
			map.clear();
		} finally { super.addMod(); }
	}

	Set<K> keySet;
	@Override
	public Set<K> keySet() {
// TODO: Implement this method
		Set<K> keySet = this.keySet;
		if (null == keySet) {
			this.keySet = keySet = new KeySet();
		}
		return keySet;
	}
	class KeySet implements Set<K> {
		Set<K> map = TemporaryMapValueModCacheWrap.this.map.keySet();

		@Override
		public int size() {
			// TODO: Implement this method
			return map.size();
		}

		@Override
		public boolean isEmpty() {
			// TODO: Implement this method
			return map.isEmpty();
		}

		@Override
		public boolean contains(Object p1) {
			// TODO: Implement this method
			return map.contains(p1);
		}

		@Override
		public Object[] toArray() {
			// TODO: Implement this method
			return map.toArray();
		}

		@Override
		public <T extends Object> T[] toArray(T[] p1) {
			// TODO: Implement this method
			return map.toArray(p1);
		}

		@Override
		public boolean containsAll(Collection<?> p1) {
			// TODO: Implement this method
			return map.containsAll(p1);
		}


		@Override
		public boolean add(K p1) {
			// TODO: Implement this method
			try {
				return map.add(p1);
			} finally { addMod(); }
		}

		@Override
		public boolean remove(Object p1) {
			// TODO: Implement this method
			try {
				return map.remove(p1);
			} finally { addMod(); }
		}

		@Override
		public boolean addAll(Collection<? extends K> p1) {
			// TODO: Implement this method
			try {
				return map.addAll(p1);
			} finally { addMod(); }
		}

		@Override
		public boolean retainAll(Collection<?> p1) {
			// TODO: Implement this method
			try {
				return map.retainAll(p1);
			} finally { addMod(); }
		}

		@Override
		public boolean removeAll(Collection<?> p1) {
			// TODO: Implement this method
			try {
				return map.removeAll(p1);
			} finally { addMod(); }
		}

		@Override
		public void clear() {
			// TODO: Implement this method
			try {
				map.clear();
			} finally { addMod(); }
		}

		@Override
		public Iterator<K> iterator() {
			// TODO: Implement this method
			return new KeySetIterator();
		}

		class KeySetIterator implements Iterator<K> {
			Iterator<K> iterator = KeySet.this.map.iterator();

			@Override
			public boolean hasNext() {
				// TODO: Implement this method
				return iterator.hasNext();
			}

			@Override
			public K next() {
				// TODO: Implement this method
				return iterator.next();
			}

			@Override
			public void remove() {
				// TODO: Implement this method
				try {
					iterator.remove();
				} finally { addMod(); }
			}
		}
	}

	Collection<V> values;
	@Override
	public Collection<V> values() {
		// TODO: Implement this method
		Collection<V> values = this.values;
		if (null == values) {
			this.values = values = new Values();
		}
		return values;
	}
	class Values implements Collection<V> {
		Collection<V> map = TemporaryMapValueModCacheWrap.this.map.values();

		@Override
		public int size() {
			// TODO: Implement this method
			return map.size();
		}

		@Override
		public boolean isEmpty() {
			// TODO: Implement this method
			return map.isEmpty();
		}

		@Override
		public boolean contains(Object p1) {
			// TODO: Implement this method
			return map.contains(p1);
		}

		@Override
		public Object[] toArray() {
			// TODO: Implement this method
			return map.toArray();
		}

		@Override
		public <T extends Object> T[] toArray(T[] p1) {
			// TODO: Implement this method
			return map.toArray(p1);
		}

		@Override
		public boolean containsAll(Collection<?> p1) {
			// TODO: Implement this method
			return map.containsAll(p1);
		}

		@Override
		public boolean add(V p1) {
			// TODO: Implement this method
			try {
				return map.add(p1);
			} finally { addMod(); }
		}

		@Override
		public boolean remove(Object p1) {
			// TODO: Implement this method
			try {
				return map.remove(p1);
			} finally { addMod(); }
		}

		@Override
		public boolean addAll(Collection<? extends V> p1) {
			// TODO: Implement this method
			try {
				return map.addAll(p1);
			} finally { addMod(); }
		}

		@Override
		public boolean retainAll(Collection<?> p1) {
			// TODO: Implement this method
			try {
				return map.retainAll(p1);
			} finally { addMod(); }
		}

		@Override
		public boolean removeAll(Collection<?> p1) {
			// TODO: Implement this method
			try {
				return map.removeAll(p1);
			} finally { addMod(); }
		}

		@Override
		public void clear() {
			// TODO: Implement this method
			try {
				map.clear();
			} finally { addMod(); }
		}

		@Override
		public Iterator<V> iterator() {
			// TODO: Implement this method
			return new ValueSetIterator();
		}

		class ValueSetIterator implements Iterator<V> {
			Iterator<V> iterator = Values.this.map.iterator();

			@Override
			public boolean hasNext() {
				// TODO: Implement this method
				return iterator.hasNext();
			}

			@Override
			public V next() {
				// TODO: Implement this method
				return iterator.next();
			}

			@Override
			public void remove() {
				// TODO: Implement this method
				try {
					iterator.remove();
				} finally { addMod(); }
			}
		}
	}

	Set<Entry<K, V>> entrySet;
	@Override
	public Set<Entry<K, V>> entrySet() {
		// TODO: Implement this method
		Set<Entry<K, V>> entrySet = this.entrySet;
		if (null == entrySet) {
			this.entrySet = entrySet = new EntrySet();
		}
		return entrySet;
	}
	class EntrySet implements Set<Entry<K, V>> {
		Set<Entry<K, V>> map = TemporaryMapValueModCacheWrap.this.map.entrySet();

		@Override
		public int size() {
			// TODO: Implement this method
			return map.size();
		}

		@Override
		public boolean isEmpty() {
			// TODO: Implement this method
			return map.isEmpty();
		}

		@Override
		public boolean contains(Object p1) {
			// TODO: Implement this method
			return map.contains(p1);
		}

		@Override
		public Object[] toArray() {
			// TODO: Implement this method
			return map.toArray();
		}

		@Override
		public <T extends Object> T[] toArray(T[] p1) {
			// TODO: Implement this method
			return map.toArray(p1);
		}

		@Override
		public boolean containsAll(Collection<?> p1) {
			// TODO: Implement this method
			return map.containsAll(p1);
		}


		@Override
		public boolean add(Entry<K, V> p1) {
			// TODO: Implement this method
			try {
				return map.add(p1);
			} finally { addMod(); }
		}

		@Override
		public boolean remove(Object p1) {
			// TODO: Implement this method
			try {
				return map.remove(p1);
			} finally { addMod(); }
		}

		@Override
		public boolean addAll(Collection<? extends Entry<K, V>> p1) {
			// TODO: Implement this method
			try {
				return map.addAll(p1);
			} finally { addMod(); }
		}

		@Override
		public boolean retainAll(Collection<?> p1) {
			// TODO: Implement this method
			try {
				return map.retainAll(p1);
			} finally { addMod(); }
		}

		@Override
		public boolean removeAll(Collection<?> p1) {
			// TODO: Implement this method
			try {
				return map.removeAll(p1);
			} finally { addMod(); }
		}

		@Override
		public void clear() {
			// TODO: Implement this method
			try {
				map.clear();
			} finally { addMod(); }
		}

		@Override
		public Iterator<Entry<K, V>> iterator() {
			// TODO: Implement this method
			return new EntrySetIterator();
		}

		class EntrySetIterator implements Iterator<Entry<K, V>> {
			Iterator<Entry<K, V>> iterator = EntrySet.this.map.iterator();

			@Override
			public boolean hasNext() {
				// TODO: Implement this method
				return iterator.hasNext();
			}

			@Override
			public Entry<K, V> next() {
				// TODO: Implement this method
				return iterator.next();
			}

			@Override
			public void remove() {
				// TODO: Implement this method
				try {
					iterator.remove();
				} finally { addMod(); }
			}
		}
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof TemporaryMapValueModCacheWrap)) return false;

		TemporaryMapValueModCacheWrap<?, ?, ?> that = (TemporaryMapValueModCacheWrap<?, ?, ?>) o;

		if (!Objects.equals(map, that.map)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return map != null ? map.hashCode() : 0;
	}

	@Override
	public String toString() {
		return map.toString();
	}
}
