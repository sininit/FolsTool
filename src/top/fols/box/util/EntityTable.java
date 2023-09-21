package top.fols.box.util;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import top.fols.atri.assist.json.JSONObject;
import top.fols.atri.assist.json.JSONArray;

@SuppressWarnings({"SpellCheckingInspection", "unchecked"})
public abstract class EntityTable<K, E extends EntityTable.Entity> {
	protected String getPropertyClass()    { return ".class"; }
    protected String getPropertyId()       { return "id"; }
    protected String getPropertyElements() { return "elements"; }



    @SuppressWarnings("FieldMayBeFinal")
    public static class Entity<K> {
		private EntityTable $$master;
        private Entity      $$parent;
		
        private K      		$id;
        private Set<Entity> $element = new LinkedHashSet<>();

        public Entity(EntityTable master, K key) {
			this.$$master = master;
			
            this.$id = key;
        }
		
        public K getId() { return $id; }

        public boolean isRoot() { return this == getTable().proot; }


		public EntityTable getTable() { return $$master; }

		
        public Entity        getParent()      { return $$parent;}
        public Set<Entity>   getElements()    { return new LinkedHashSet<Entity>(getElementsInternal()); }

        protected void   	  setParent(Entity p)    { this.$$parent = p; }
        protected Set<Entity> getElementsInternal()  { return $element; }



        public void append(Entity append)  { getTable().appendEntity(this, append); }
        public void remove(Entity element) { getTable().removeEntity(element);      }
		public void remove() { remove(this); }

        public Set<Map<String, Object>> elemensSetMap() {
            Set<Map<String, Object>> elements = new LinkedHashSet<>();
            Set<Entity>     elementEntities = getElementsInternal();
            for (Entity ee: elementEntities) {
                elements.add(ee.toMap());
            }
            return elements;
        }
        public Map<String, Object> toMap() {
			EntityTable et = getTable();

            Map<String, Object> map = new LinkedHashMap<>();
            map.put(et.getPropertyId(), 	  getId());
            map.put(et.getPropertyElements(), elemensSetMap());
			map.put(et.getPropertyClass(), 	  getClass().getName());
			
            et.escapeEntityMap(this, map);
            return map;
        }

        public boolean isParent(Entity entity) {
            for (Entity p = getParent(); null != p; p = p.getParent())
                if (entity == p)
                    return true;
            return false;
        }
        public boolean isChild(Entity entity) {
            return entity.isParent(this);
        }

		@Override
        public String toString() {
            // TODO: Implement this method
            return new JSONObject(this.toMap()).toFormatString();
        }
    }

    protected Map<K, E> entityMap;
    protected E proot;
    protected K prootKey;

    public EntityTable() {
        proot     = newRootEntity();
        prootKey  = getId(proot);

        entityMap = newEntityMap();
        entityMap.put(prootKey, proot);
    }



    public K getId(E entry)  {
        return (K) entry.getId();
    }


    public int getTableHash()            { return hashCode(); }


    public    E   		newEntity(K key) { return (E) new Entity(this, key); }
    protected Map<K, E> newEntityMap()   { return new LinkedHashMap<>(); }
    protected E   		newRootEntity()  { return newEntity(null);  }

    protected void escapeEntityMap(E source, Map<String, Object> writer)      {}
    protected void unescapeEntityMap(Map<String, Object> source, E writer) {}


    public E fromMap(Map<String, Object> e) {
        if (e == null) return null;

		String ks = getPropertyId();
        K   id = (K) e.get(ks);
		if (id == null) {
			if (!e.containsKey(ks)) {
				throw new UnsupportedOperationException("not found property: " + ks + ", list: " + e.keySet().toString());
			}
		}
        E entity = newEntity(id);

		checkFromMap(e, entity);

        Collection<Map<String, Object>>   elements = (Collection<Map<String, Object>>) e.get(getPropertyElements());
        if (null != elements) {
            for (Map<String, Object> element: elements) {
                entity.getElementsInternal().add(fromMap(element));
            }
        }
        unescapeEntityMap(e, entity);
        return entity;
    }
    public Map<String, Object> toMap() {
        return proot.toMap();
    }



    public E getRootEntity() { return proot; }
    public E       findEntity(K k)      { return entityMap.get(k); }
    public boolean  hasEntity(E entity) { return entityMap.get(getId(entity)) == entity; }



	boolean checkFromMapEntityType(Class clazz, E entity) {
		return clazz == entity.getClass();
	}
	void 	checkFromMap(Map<String, Object> source, E writer) {
		//check type
		String pClass  = getPropertyClass();
		String      className = (String) source.get(pClass);
		if (null != className) {
			Class clazz;
			try { 
				clazz = classForName(className);
			} catch (ClassNotFoundException e) {
				clazz = null;
			}
			if (checkFromMapEntityType(clazz, writer) == false) {
				throw new UnsupportedOperationException("unsupported entity type: " + className);
			}
		}
	}
    E    	checkTable(E entity, String message) {
        if (entity.getTable() == this)
            return entity;
        throw new UnsupportedOperationException(message);
    }


    void appendEntityMapId(E entity) {
        E   last = entityMap.put(getId(entity), entity);
        if (last != entity) {
            Set<E> elements = entity.getElementsInternal();
            if (((!elements.isEmpty()))) {
                for (E e: elements) {
                    appendEntityMapId(e);
                }
            }
        }
    }
    void removeEntiryMapId(E entity) {
        E   remove = entityMap.remove(entity.getId());
        if (remove != null) {
            Set<E> elements = remove.getElementsInternal();
            if (((!elements.isEmpty()))) {
                for (E e: elements) {
                    removeEntiryMapId(e);
                }
            }
        }
    }




    public E removeEntityFromId(K ek) {
        return removeEntity0(findEntity(ek), true);
    }
    public E removeEntity(E entity) {
        return removeEntity0(entity, true);
    }
    private E removeEntity0(E ee, boolean removeEntiryMapId) {
        if (Objects.equals(proot, ee)) throw new UnsupportedOperationException("remove root node");

        if (ee != null) {
            Entity pentity = ee.getParent();
            if (pentity != null) {
                pentity.getElementsInternal().remove(ee);
            }
            ee.setParent(null);

            if (removeEntiryMapId)
                removeEntiryMapId(ee);
        }
        return ee;
    }


    public void appendEntity(E pentity, E entity) {
        if (pentity == entity) return;// throw new UnsupportedOperationException("append slef");
        if (pentity == null)   throw new UnsupportedOperationException("null parent");
        if (entity  == null)   throw new UnsupportedOperationException("append null entity");
        if (entity  == proot)  throw new UnsupportedOperationException("change root node");

        checkTable(pentity, "other table parent");
        checkTable(entity,  "other table entity");

        K   ek = getId(entity);
        if (ek.equals(prootKey)) throw new UnsupportedOperationException("repeat root id: " + ek);

        if (pentity.isParent(entity)) {
            throw new UnsupportedOperationException("cannot add parent node A of child node B to child node B");
        } else {
            boolean pappend = hasEntity(pentity);

            removeEntity0(entity, !pappend);

            pentity.getElementsInternal().add(entity);
            entity.setParent(pentity);

            if (pappend)
                appendEntityMapId(entity);
        }
    }




    @Override
    public String toString() {
        return new JSONArray(proot.elemensSetMap()).toFormatString();
    }


	
	
	
	protected Class classForName(String name) throws ClassNotFoundException {
		return null == name 
			? null
			: classForNameCache.lookup(name);
	}
	private WeakLevel3Cache<String, Class, ClassNotFoundException> classForNameCache = new WeakLevel3Cache<String, Class, ClassNotFoundException>(){
		@Override
		protected Class newLookupCache(String key) throws ClassNotFoundException {
			// TODO: Implement this method
			return Class.forName(key);
		}
	};
	private abstract class WeakLevel3Cache<K, V, EX extends Throwable> {
		public boolean release() {
			cache1 = cache2 = cache3 = null;
			return true;
		}
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


}
