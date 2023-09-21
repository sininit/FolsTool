package top.fols.atri.util.map;

import java.util.*;

public class CasesLinkedHashMap<V> {
	public static class DataMap<V> {
		CasesLinkedHashMap<V> s;
		public DataMap(CasesLinkedHashMap<V> s) {
			this.s = s;
		}

		private final Map<String, V> kvMap = new LinkedHashMap<>();
		private Map<String, String> bestMap;
		private Map<String, String> newBestMap() {
			return new WeakHashMap<>();
		}

		private final Iterable<String>  unmodifiableKeySet = Collections.unmodifiableSet(kvMap.keySet());
		private final Collection<V> 	unmodifiableValues = Collections.unmodifiableCollection(kvMap.values());

		String  firstKey;
		boolean firstKeySeted;

		public String getFirstKey() {
			return this.firstKey;
		}

		public int size() {
			return this.kvMap.size();
		}

		public V get(String str) {
			return this.kvMap.get(str);
		}
		public boolean contains(String str) {
			return this.kvMap.containsKey(str);
		}

		public void put(String str, V v) {
			this.kvMap.put(str, v);
			this.s.count = -1;

			if (this.bestMap != null && this.bestMap.size() > 0)
				this.bestMap =  null;
			if (!this.firstKeySeted) {
				this.firstKeySeted = true;
				this.firstKey = str;
			}
		}

		public void remove(String str) {
			this.kvMap.remove(str);
			this.s.count = -1;

			if (this.bestMap != null && this.bestMap.size() > 0) {
				this.bestMap = null;
			}
			if (this.firstKeySeted && Objects.equals(str, this.firstKey)) {
				Set<String> keySet = this.kvMap.keySet();
				if (keySet.isEmpty()) {
					this.firstKey = null;
					this.firstKeySeted = false;
				} else {
					this.firstKey = keySet.iterator().next();
				}
			}
		}

		public Iterable<String> keySet() {
			return this.unmodifiableKeySet;
		}
		protected Collection<V> values() {
			return this.unmodifiableValues;
		}

		public String toString() {
			return this.kvMap.toString();
		}


		public String getBestKey(String key) {
			if (null == key)
				return null;
			if (kvMap.size() == 1)
				return firstKey;

			Map<String, String> cache = bestMap == null ? (bestMap = newBestMap()): bestMap;
			String      k =     cache.get(key);
			if (null == k) {
				Iterable<String> keySet = keySet();
				String best = null;

				if (contains(key)) {
					best = key;
				} else {
					int mkMagic = -1;
					int mkLx = 0;
					for (String mk : keySet) {
						int cMagic = 0;
						int cLength;
						int cLx = 0;
						if (mk != null && (cLength = key.length()) == mk.length()) {
							for (int i2 = 0; i2 < cLength; i2++) {
								if (key.charAt(i2) == mk.charAt(i2)) {
									cMagic++;
									cLx++;
								} else {
									cLx = 0;
								}
							}
							if (mkMagic < 0 || (cMagic > mkMagic) || (cMagic == mkMagic && cLx > mkLx)) {
								mkMagic = cMagic;
								mkLx = cLx;
								best = mk;
							}
						}
					}
				}
				cache.put(key, k = best);
			}
			return k;
		}

	}
	final Map<String, DataMap<V>> keyMap 	= new LinkedHashMap<>();
	final Set<String>   unmodifiableKeySet 	= Collections.unmodifiableSet(keyMap.keySet());
	int count = 0;


	protected String formatKey(String o) { return null != o ? o.toLowerCase() : null; }


	private DataMap<V> newDataMap() {
		return new DataMap<>(this);
	}


	protected DataMap<V> getMap(String key) {
		String k  = formatKey(key);

		DataMap<V>  set = keyMap.get(k);
		if (null == set) {
			return null;
		}
		return set;
	}
	protected DataMap<V> getOrCreateMap(String key) {
		String k  = formatKey(key);

		DataMap<V> set = keyMap.get(k);
		if (null == set) {
			keyMap.put(k, set = newDataMap());
		}
		return set;
	}

	protected DataMap<V> removeMap(String key) {
		String k  = formatKey(key);
		DataMap<V> remove = keyMap.remove(k);
		this.count = -1;
		return remove;
	}

	public void clear() {
		this.keyMap.clear();
		this.count = -1;
	}


	public V getExact(String key) {
		DataMap<V>  table = getMap(key);
		if (null != table) {
			return table.get(key);
		}
		return null;
	}
	public V getExactOrFirst(String key) {
		DataMap<V>  table = getMap(key);
		if (null != table) {
			V   v = table.get(key);
			if (v == null) {
				String k = table.getFirstKey();
				return table.get(k);
			}
			return v;
		}
		return null;
	}

	public V getFirst(String key) {
		DataMap<V>  table = getMap(key);
		if (null != table) {
			String k = table.getFirstKey();
			return table.get(k);
		}
		return null;
	}

	public DataMap<V> getAll(String key) { 
		return getMap(key);
	}



	public void put(String key, V value) {
		getOrCreateMap(key).put(key, value);
	}

	public boolean removeExact(String key) {
		DataMap<V>  table = getMap(key);
		if (null != table) {
			table.remove(key);

			if (table.size() == 0) 
				removeMap(key);
			return true;
		}
		return false;
	}
	public DataMap<V> removeAll(String key) {
		return removeMap(key);
	}





	public String findValueMapperKey(V value) {
		if (null == value) {
			for (String k: keyMap.keySet()) {
				DataMap<V> dataMap = keyMap.get(k);
				for (String mk: dataMap.keySet()) 
					if (dataMap.get(mk) == null) 
						return mk;
			}
		} else {
			for (String k: keyMap.keySet()) {
				DataMap<V> dataMap = keyMap.get(k);
				for (String mk: dataMap.keySet()) 
					if (value.equals(dataMap.get(mk))) 
						return mk;
			}
		}
		return null;
	}
	public String findValueMapperKey(String key, V value) {
		if (null == value) {
			DataMap<V>  dataMap = getMap(key);
			if (null != dataMap)
				for (String mk: dataMap.keySet()) 
					if (null == dataMap.get(mk)) 
						return mk;
		} else {
			DataMap<V>  dataMap = getMap(key);
			if (null != dataMap)
				for (String mk: dataMap.keySet()) 
					if (value.equals(dataMap.get(mk))) 
						return mk;
		}
		return null;
	}


	public List<String> removeMapValueMappingKeyReturnKey(V value) {
		List<String> keys = new ArrayList<>();
		if (null == value) {
			for (String k: keyMap.keySet()) {
				DataMap<V> dataMap = keyMap.get(k);
				for (String mk : dataMap.keySet()) {
					if (dataMap.get(mk) == null) {
						dataMap.remove(mk);
						keys.add(mk);
					}
				}
			}
		} else {
			for (String k: keyMap.keySet()) {
				DataMap<V> dataMap = keyMap.get(k);
				for (String mk : dataMap.keySet()) {
					if (value.equals(dataMap.get(mk))) {
						dataMap.remove(mk);
						keys.add(mk);
					}
				}
			}
		}
		return keys;
	}
	public List<String> removeMapValueMappingKeyReturnKey(String key, V value) {
		List<String> keys = new ArrayList<>();
		if (null == value) {
			DataMap<V>  dataMap = getMap(key);
			if (null != dataMap) {
				for (String mk : dataMap.keySet()) {
					if (dataMap.get(mk) == null) {
						dataMap.remove(mk);
						keys.add(mk);
					}
				}
			}
		} else {
			DataMap<V>  dataMap = getMap(key);
			if (null != dataMap) {
				for (String mk : dataMap.keySet()) {
					if (value.equals(dataMap.get(mk))) {
						dataMap.remove(mk);
						keys.add(mk);
					}
				}
			}
		}
		return keys;
	}





	public String findBestKey(String key) {
		DataMap<V>  table = getMap(key);
		if (null != table) {
			return  table.getBestKey(key);
		}
		return null;
	}
	public V findBest(String key) {
		DataMap<V>  table = getMap(key);
		if (null != table) {
			String k = table.getBestKey(key);
			return table.get(k);
		}
		return null;
	}




//	public int size() {return keyMap.size();}
//	public int size(String key) {
//		DataMap<V>  table = getMap(key);
//		if (null != table) {
//			return table.size();
//		}
//		return 0;
//	}


	public Set<String> 	ignoredCaseKeySet()   {return unmodifiableKeySet;}
	public int 			ignoredCaseKeyCount() {return unmodifiableKeySet.size();}


	public int count() {
		int c = this.count;
		if (c == -1) {
			c = 0;
			for (String next : ignoredCaseKeySet()) {
				DataMap<V> vDataMap = keyMap.get(next);
				c += vDataMap.size();
			}
			this.count = c;
		}
		return c;
	}

	Iterable<String> allKeys = new Iterable<String>() {
		@Override
		public Iterator<String> iterator() {
			return new Iterator<String>() {
				@Override
				public void remove() {
					// TODO: Implement this method
					throw new UnsupportedOperationException();
				}

				private final Iterator<String> mapIterator = keyMap.keySet().iterator();
				private Iterator<String> valueIterator = Collections.emptyIterator(); // 初始为空迭代器

				@Override
				public boolean hasNext() {
					// 检查当前迭代器是否还有元素，如果没有，则尝试移动到下一个Map的迭代器
					while (!valueIterator.hasNext() && mapIterator.hasNext()) {
						String next         = mapIterator.next();
						DataMap<V> vDataMap = keyMap.get(next);
						valueIterator = vDataMap.keySet().iterator();
					}
					return valueIterator.hasNext();
				}

				@Override
				public String next() {
					// 返回当前值并移动到下一个
					return valueIterator.next();
				}
			};
		}
	};
	public Iterable<String> allKeys() {
		return allKeys;
	}
	public List<String>     keysList() {
		List<String> result = new ArrayList<>();
		for (String k: ignoredCaseKeySet()) {
			DataMap<V>  dataMap = keyMap.get(k);
			for (String key : dataMap.keySet()) {
				result.add(key);
			}
		}
		return result;
	}

	Iterable<V> allValues = new Iterable<V>() {
		@Override
		public Iterator<V> iterator() {
			return new Iterator<V>() {
				@Override
				public void remove() {
					// TODO: Implement this method
					throw new UnsupportedOperationException();
				}

				private final Iterator<DataMap<V>> mapIterator = keyMap.values().iterator();
				private Iterator<V> valueIterator = Collections.emptyIterator(); // 初始为空迭代器

				@Override
				public boolean hasNext() {
					// 检查当前迭代器是否还有元素，如果没有，则尝试移动到下一个Map的迭代器
					while (!valueIterator.hasNext() && mapIterator.hasNext()) {
						valueIterator = mapIterator.next().values().iterator();
					}
					return valueIterator.hasNext();
				}

				@Override
				public V next() {
					// 返回当前值并移动到下一个
					return valueIterator.next();
				}
			};
		}
	};
	public Iterable<V> allValues() { return allValues; }
	public List<V>     valuesList() {
		List<V> result = new ArrayList<>();
		for (String k: ignoredCaseKeySet()) {
			DataMap<V>  dataMap = getMap(k);
			result.addAll(dataMap.values());
		}
		return result;
	}

	@Override
	public String toString() {
		// TODO: Implement this method
		return keyMap.toString();
	}
}
