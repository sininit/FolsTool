package top.fols.box.reflect;

import top.fols.atri.cache.WeakMapCacheConcurrentHash;
import top.fols.atri.reflect.ReflectMatcherAsScore;

import java.util.*;

public class ClassMapperMatcher<V> {
	public static abstract class ClassMapper {
		protected final Class<?> key;
		public ClassMapper(Class<?> key) {
			this.key = key;
		}
		
		public Class<?> getType() { return key; }

		@Override
		public int hashCode() {
			// TODO: Implement this method
			return null == key ?0: key.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			// TODO: Implement this method
			if (obj == this) return true;
			if (obj == null) return false;
			if (obj.getClass() == getClass())
				return Objects.equals(key, ((ClassEqualOrInheritMapper)obj).key);
			return false;
		}
		
		public boolean isAccept(Class find) {
			if (key == null)
				return null == find;
			return key.isAssignableFrom(find);
		}
	}
	
	public static class ClassEqualMapper extends ClassMapper {
		public ClassEqualMapper(Class<?> key) {
			super(key);
		}
		@Override
		public boolean isAccept(Class find) {
			// TODO: Implement this method
			return key == find;
		}
	}
	public static class ClassEqualOrInheritMapper extends ClassMapper {
		public ClassEqualOrInheritMapper(Class<?> key) {
			super(key);
		}
		@Override
		public boolean isAccept(Class find) {
			if (key == null)
				return null == find;
			return key.isAssignableFrom(find);
		}
	}
	
	public static class AllArrayMapper extends ClassMapper {
		public AllArrayMapper() {
			super(Object.class);
		}
		@Override
		public boolean isAccept(Class find) {
			// TODO: Implement this method
			return null != find && find.isArray();
		}
	}


	private final Map<ClassMapper, V> table;

	ReflectMatcherAsScore.MatchScore matchScore = new ReflectMatcherAsScore.MatchScore();
	double calcAssignableFromLevel(Class parentClass, Class thisClass) {
		return matchScore.calcAssignableFromLevel(parentClass, thisClass);
	}


	public ClassMapper setAllArrayMapper(V v) {
		synchronized (table) {
			return this.setClassMapper(new AllArrayMapper(), v);
		}
	}
    public ClassMapper setClassEqualMapper(Class<?> sourceType, V v) {
		return this.setClassMapper(new ClassEqualMapper(sourceType), v);
    }
	public ClassMapper setClassEqualOrInheritMapper(Class<?> sourceType, V v) {
		return this.setClassMapper(new ClassEqualOrInheritMapper(sourceType), v);
    }
	public ClassMapper setClassMapper(ClassMapper sourceType, V v) {
		synchronized (table) {
			this.table.put(sourceType, v);
			this.cached.release();
		}
		return sourceType;
    }

    public void removeMapper(ClassMapper sourceType) {
		synchronized (table) {
			this.table.remove(sourceType);
			this.cached.release();
		}
    }



	/* cache */
	public Result<V> getAssignableFroms(final Class<?> find) {
		return cached.getOrCreateCache(find);
	}
	private <X> Result<X> getAssignableFroms0(Map<ClassMapper, X> set, final Class<?> find) {
		Set<ClassMapper> buffer = new LinkedHashSet<>();
		ClassMapper findEq = new ClassEqualMapper(find);
		if (set.containsKey(findEq)) 
			buffer.add(findEq);
		if (null != find) {
			List<ClassMapper> instanceofList = new ArrayList<>();
			for (ClassMapper s: set.keySet()) 
				if ((null != s)) 
					if (s.key.isAssignableFrom(find))
						if (s.isAccept(find)) 
							instanceofList.add(s);
			Collections.sort(instanceofList, new Comparator<ClassMapper>() {
					@Override
					public int compare(ClassMapper p1, ClassMapper p2) {
						return Double.compare(
							calcAssignableFromLevel(p1.key, find), 
							calcAssignableFromLevel(p2.key, find));
					}
				});
			buffer.addAll(instanceofList);
		}
		Map <Class<?>, X> result = new LinkedHashMap<>();
		for (ClassMapper element: buffer) 
			result.put(element.key, set.get(element));
		Class<?> fk;
		return new Result<X>(Collections.unmodifiableMap(result),
							 fk = buffer.size() > 0 ? buffer.iterator().next().key : null, 
							 result.get(fk)
							 );
	}
	final WeakMapCacheConcurrentHash<Class, Result<V>, RuntimeException> cached = new WeakMapCacheConcurrentHash<Class, Result<V>, RuntimeException>() {
		@Override
		public ClassMapperMatcher.Result<V> newCache(Class source) throws RuntimeException {
			// TODO: Implement this method
			return getAssignableFroms0(ClassMapperMatcher.this.table, source);
		}
	};
	/* cache */



	/* no-cache */
	public <X> Result<X> findAssignableFroms(Map<Class<?>, X> set, final Class<?> find) {
		Set<Class<?>> buffer = new LinkedHashSet<>();
		Class<?> findEq = find;
		if (set.containsKey(findEq)) 
			buffer.add(findEq);
		if (null != find) {
			List<Class<?>> instanceofList = new ArrayList<>();
			for (Class<?> s: set.keySet()) 
				if ((null != s)) 
					if (s.isAssignableFrom(find)) 
						instanceofList.add(s);
			Collections.sort(instanceofList, new Comparator<Class>() {
					@Override
					public int compare(Class p1, Class p2) {
						return Double.compare(
							calcAssignableFromLevel(p1, find), 
							calcAssignableFromLevel(p2, find));
					}
				});
			buffer.addAll(instanceofList);
		}
		Map <Class<?>, X> result = new LinkedHashMap<>();
		for (Class<?>  element: buffer) 
			result.put(element, set.get(element));
		Class<?> fk;
		return new Result<X>(Collections.unmodifiableMap(result),
							 fk = buffer.size() > 0 ? buffer.iterator().next() : null, 
							 result.get(fk)
							 );
	}
	/* no-cache */



	public static class Result<V> {
		Map<Class<?>, V> result;
		Class<?> firstClass;
		V        firstValue;
		public Result(Map<Class<?>, V> result,
					  Class<?> firstClass,
					  V        firstValue) {
			this.result     = result;
			this.firstClass = firstClass;
			this.firstValue = firstValue;
		}

		public Class<?> getFirstClass() {
			return firstClass;
		}
		public V getFirstValue() {
			return firstValue;
		}

		public Map<Class<?>, V> getMap() { return result; }

		public boolean isEmpty() {
			return result.isEmpty();
		}

		@Override
		public String toString() {
			// TODO: Implement this method
			return result.toString();
		}
	}

	public ClassMapperMatcher() {
		this.table = new LinkedHashMap<>();
	}
	public ClassMapperMatcher(ClassMapperMatcher<V> p) {
		this.table = null != p
			? new LinkedHashMap<ClassMapper, V>(p.table)
			: new LinkedHashMap<ClassMapper, V>();
	}
}
